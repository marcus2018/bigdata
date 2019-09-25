package com.example.storm.benchmark.produce.cabinet.cabinet_battery_temp;


import com.example.storm.bean.StationLost;
import com.example.storm.benchmark.produce.cabinet.cabinet_battery_temp.bean.BatteryHB3InStationTemp;
import com.example.storm.benchmark.produce.cabinet.cabinet_battery_temp.bean.CabinetHBTime_type3;
import com.example.storm.benchmark.util.LoghubService;
import com.google.common.collect.Maps;
import org.apache.storm.jdbc.common.Column;
import org.apache.storm.jdbc.common.ConnectionProvider;
import org.apache.storm.jdbc.common.HikariCPConnectionProvider;
import org.apache.storm.jdbc.common.JdbcClient;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseWindowedBolt;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.windowing.TupleWindow;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.io.FileWriter;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class LogBatteryInStationTemp extends BaseWindowedBolt {

    private static final long serialVersionUID = -5283595260540124273L;
    private FileWriter writer = null;

    private OutputCollector collector;
    private static Map<String, Integer> counters = new ConcurrentHashMap<String, Integer>();

    private JedisPool pool;
    private  static     Map<String,StationLost> stringStationLostMap=new HashMap<>();
    private SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private SimpleDateFormat sdf1=new SimpleDateFormat("yyyy-MM-dd");
    private static String batteryInstation="monitor:batteryInstation:";
    private ConnectionProvider connectionProvider;
    private JdbcClient jdbcClient;
    private volatile Map<String,Integer> mapStation=new HashMap<>();
    private  static volatile Long times=new Date().getTime();







    public void prepare(Map stormConf, TopologyContext context
            , OutputCollector collector) {
        this.collector = collector;

        JedisPoolConfig config = new JedisPoolConfig();
        //控制一个pool最多有多少个状态为idle(空闲的)的jedis实例。
        config.setMaxIdle(5);
        //控制一个pool可分配多少个jedis实例，通过pool.getResource()来获取；
        //如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)。
        //在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
        config.setMaxTotal(1000 * 100);
        //表示当borrow(引入)一个jedis实例时，最大的等待时间，如果超过等待时间，则直接抛出JedisConnectionException；
        config.setMaxWaitMillis(30);
        config.setTestOnBorrow(true);
        config.setTestOnReturn(true);


        /**
         *如果你遇到 java.net.SocketTimeoutException: Read timed out exception的异常信息
         *请尝试在构造JedisPool的时候设置自己的超时值. JedisPool默认的超时时间是2秒(单位毫秒)
         */

        pool = new JedisPool(config, "119.23.133.72", 6574,20000,"immotor!6574@.com");
        Map hikariConfigMap = Maps.newHashMap();
        hikariConfigMap.put("dataSourceClassName","com.mysql.jdbc.jdbc2.optional.MysqlDataSource");
        hikariConfigMap.put("dataSource.url", "jdbc:mysql://rr-wz93pd9mi921e30j0.mysql.rds.aliyuncs.com:3306/power2");
        hikariConfigMap.put("dataSource.user","etl");
        hikariConfigMap.put("dataSource.password","immotor!99");
        connectionProvider = new HikariCPConnectionProvider(hikariConfigMap);
        //对数据库连接池进行初始化
        connectionProvider.prepare();
        jdbcClient = new JdbcClient(connectionProvider, 30);



    }

    public void execute(TupleWindow inputWindow) {
        Jedis jedis= pool.getResource();
        int sum = 0;

        List<BatteryHB3InStationTemp> batteryHB3InStationTempList=new ArrayList<>();
        for (Tuple tuple : inputWindow.getNew()) {
            if(new Date().getTime()>times) {
                synchronized (LogBatteryInStationTemp.class){
                    if(new Date().getTime()>times) {
                        List<List<Column>> select= select = getStationCode();
                        for(int i=0;i<select.size();i++){
                            mapStation.put((String) select.get(i).get(2).getVal(),(Integer) select.get(i).get(0).getVal());
                        }
                        times += 4 * 60 * 60 * 1000;
                    }
                }
            }

          Map<String,List<CabinetHBTime_type3>> map = ( Map<String,List<CabinetHBTime_type3>>)tuple.getValueByField("count");
          for(Map.Entry<String,List<CabinetHBTime_type3>> maps:map.entrySet()){
              //传过来流数据
              Map<String,String> insertMap=new HashMap<>();
              Map<String,String> map1=jedis.hgetAll(batteryInstation+maps.getKey());
              List<CabinetHBTime_type3> list=maps.getValue();
              if(list.size()>0){
                  if (map1.get("time")!=null&&list.get(0).getDate().getTime()-Long.parseLong(map1.get("time"))<=0)//小于当前时间跳过
                      continue;
              }
              for(int j=0;j<list.size();j++){
                  int port=list.get(j).getPort();
                  String id=list.get(j).getId();

                  //这个仓位没有电池，或有电池但不是同一个电池
                  if(( map1.get("id"+port)==null)||( map1.get("id"+port)!=null&&!id.equals(map1.get("id"+port)))){
                         insertMap.put("id"+port,id);
                         batteryHB3InStationTempList.add(new BatteryHB3InStationTemp(id,maps.getKey(),port,list.get(j).getT(), mapStation.get((String) maps.getKey())==null?0: mapStation.get((String) maps.getKey()),list.get(j).getDate()));
                  }

              }
              jedis.hmset(batteryInstation+maps.getKey(),insertMap);
          }







        }
        if(batteryHB3InStationTempList.size()>0){
            LoghubService loghubService=new LoghubService();
            loghubService.batteryHB3InStationTempList(batteryHB3InStationTempList);
        }
        if(jedis!=null){
            jedis.close();
        }

    }
    public List<List<Column>>  getStationCode(){
        //查询该word是否存在
        List<Column> listMysql = new ArrayList();
        //创建一列将值传入   列名  值    值的类型
        listMysql.add(new Column("status", "1", Types.VARCHAR));
        //  synchronized (LogBatterySlidingWindowBolt.class) {
        return jdbcClient.select("select city_code, sn,pid from t_station where status = ?", listMysql);
        //   }


    }

    public void declareOutputFields(OutputFieldsDeclarer declarer) {
    }
}