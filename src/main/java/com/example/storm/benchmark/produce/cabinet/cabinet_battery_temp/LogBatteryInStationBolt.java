package com.example.storm.benchmark.produce.cabinet.cabinet_battery_temp;

import com.aliyun.openservices.log.common.LogGroupData;
import com.aliyun.openservices.log.common.Logs;
import com.aliyun.openservices.log.exception.LogException;
import com.aliyun.openservices.loghub.stormspout.LogHubSpout;
import com.example.storm.bean.StationLost;
import com.example.storm.benchmark.produce.cabinet.cabinet_battery_temp.bean.BatteryHB3InStationTemp;
import com.example.storm.benchmark.produce.cabinet.cabinet_battery_temp.bean.CabinetHBTime_type3;
import com.example.storm.benchmark.produce.cabinet.cabinet_battery_temp.bean.StationHB3_Datas;
import com.example.storm.benchmark.util.LoghubService;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
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

import java.sql.Types;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class LogBatteryInStationBolt extends BaseWindowedBolt {
    private OutputCollector collector;
    private static Map<String, Integer> counters = new ConcurrentHashMap<String, Integer>();

    private static volatile JedisPool pool;
    private  static     Map<String,StationLost> stringStationLostMap=new HashMap<>();
    private SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private SimpleDateFormat sdf1=new SimpleDateFormat("yyyy-MM-dd");
    private static String batteryInstation="monitor:batteryInstation:";
    private static String batteryNumbersInStation="monitor:batteryNumbersInStation";
    private static String lostStationKey="monitor:station_pid_lost";
    private ConnectionProvider connectionProvider;
    private JdbcClient jdbcClient;
    private static volatile Map<String,Integer> mapStation=new HashMap<>();
    private static volatile Set<String> stationCode0=new HashSet<>();
    private  static volatile Long times=new Date().getTime();

    @Override
            public void prepare(Map stormConf, TopologyContext context,
               OutputCollector collector) {
               this.collector = collector;
        this.collector = collector;
        if(pool==null){
            synchronized (LogBatteryInStationBolt.class){
                if(pool==null){
                    JedisPoolConfig config = new JedisPoolConfig();
                    config.setMaxIdle(5);
                    config.setMaxTotal(1000 * 100);
                    config.setMaxWaitMillis(30);
                    config.setTestOnBorrow(true);
                    config.setTestOnReturn(true);
                    pool = new JedisPool(config, "119.23.133.72", 6574,20000,"immotor!6574@.com");
                }
            }
        }

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
           @Override
            public void execute(TupleWindow inputWindow) {
               Jedis jedis= pool.getResource();
               Map<String,List<CabinetHBTime_type3>> listMap=new HashMap<>();
               List<BatteryHB3InStationTemp> batteryHB3InStationTempList=new ArrayList<>();


               if(new Date().getTime()>times) {
                   synchronized (LogBatteryInStationBolt.class){
                       if(new Date().getTime()>times) {
                           List<List<Column>> select = getStationCode();
                           for(int i=0;i<select.size();i++){
                               mapStation.put((String) select.get(i).get(2).getVal(),(Integer) select.get(i).get(0).getVal());
                           }
                         //  jedis.del(batteryNumbersInStation);
                           times += 4 * 60 * 60 * 1000;
                       }
                   }
               }


                for(Tuple tuple: inputWindow.get()){
                   List<LogGroupData> logGroupDatas = (ArrayList<LogGroupData>) tuple.getValueByField(LogHubSpout.FIELD_LOGGROUPS);
                   for (LogGroupData groupData : logGroupDatas) {
                       // 每个 logGroup 由一条或多条日志组成
                       Logs.LogGroup logGroup = null;
                       try {
                           logGroup = groupData.GetLogGroup();
                       } catch (LogException e) {
                           e.printStackTrace();
                       }
                       for (Logs.Log log : logGroup.getLogsList()) {
                           StringBuilder sb = new StringBuilder();
                           String datas="";
                           // 每条日志，有一个时间字段，以及多个 Key:Value 对，
                           int log_time = log.getTime();
                           sb.append("LogTime:").append(log_time);
                           CabinetHBTime_type3 cabinetHBTime_type3=new CabinetHBTime_type3();

                           for (Logs.Log.Content content : log.getContentsList()) {
                              if(content.getKey().equals("pID")){
                                   sb.append("\t").append(content.getKey()).append(":")
                                           .append(content.getValue());
                                  cabinetHBTime_type3.setpID(content.getValue());
                                   }
                               if(content.getKey().equals("type")){
                                   sb.append("\t").append(content.getKey()).append(":")
                                           .append(content.getValue());
                                   cabinetHBTime_type3.setType(Integer.parseInt(content.getValue()));
                               }
                               if(content.getKey().equals("datas")){
                                   sb.append("\t").append(content.getKey()).append(":")
                                           .append(content.getValue());
                                  datas=content.getValue();
                               }
                               if(content.getKey().equals("time1")){
                                   sb.append("\t").append(content.getKey()).append(":")
                                           .append(content.getValue());

                                   try {
                                       cabinetHBTime_type3.setDate( sdf.parse(content.getValue()));
                                   } catch (ParseException e) {

                                   }

                               }


                           }

                         if(cabinetHBTime_type3.getType()!=3)
                                continue;
                           StationHB3_Datas datas1= new Gson().fromJson(datas,StationHB3_Datas.class);
                           List<CabinetHBTime_type3> list = new ArrayList<>();
                           for (int i=0;i<datas1.getBattery().size();i++){
                               CabinetHBTime_type3 cabinetHBTime_type31=new CabinetHBTime_type3();
                               cabinetHBTime_type31.setpID(cabinetHBTime_type3.getpID());
                               cabinetHBTime_type31.setDate(cabinetHBTime_type3.getDate());
                               cabinetHBTime_type31.setId(datas1.getBattery().get(i).getId());
                               cabinetHBTime_type31.setPort(datas1.getBattery().get(i).getP());
                               cabinetHBTime_type31.setT(datas1.getBattery().get(i).getT());
                               cabinetHBTime_type31.setSum(datas1.getBattery().size());
                               list.add(cabinetHBTime_type31);

                           }

                           listMap.put(cabinetHBTime_type3.getpID(),list);



                       }
                   }





               }

               //   Map<String,String> insertBatteryNumberMap=new HashMap<>();//统计柜子电池数量
               //柜子电池数量
               Map<String,String> insertBatteryNumbersInStation=new HashMap<>();
               //删除没有城市属性的柜子
               List<String> delBatteryNumbersInStation=new ArrayList<>();
               for(Map.Entry<String,List<CabinetHBTime_type3>> maps:listMap.entrySet()){
                   //传过来流数据
                   Map<String,String> insertMap=new HashMap<>();
                   Map<String,String> map1=jedis.hgetAll(batteryInstation+maps.getKey());
                   List<CabinetHBTime_type3> list2=maps.getValue();
                   if(null==mapStation.get((String) maps.getKey())){
                       stationCode0.add(maps.getKey().trim());
                   }
                   insertBatteryNumbersInStation.put(maps.getKey().trim()+"|"+  (mapStation.get((String) maps.getKey())==null?0+"": mapStation.get((String) maps.getKey())+"").trim(),(list2.size()>0?list2.get(0).getSum():0)+"");
                   if(null!=mapStation.get((String) maps.getKey())&&stationCode0.contains(maps.getKey().trim())){
                       delBatteryNumbersInStation.add(maps.getKey().trim()+"|0");
                       stationCode0.remove(maps.getKey().trim());
                   }
                  // jedis.hset(batteryNumbersInStation,(maps.getKey().trim()+"|"+  (mapStation.get((String) maps.getKey())==null?0+"": mapStation.get((String) maps.getKey())+"").trim()),(list2.size()>0?list2.get(0).getSum():0)+"");
                   if(list2.size()>0){
                       if ((map1.get("time")!=null)&&list2.get(0).getDate().getTime()-Long.parseLong(map1.get("time"))<=0)//小于当前时间跳过
                           continue;
                   }
                   for(int j=0;j<list2.size();j++){
                       int port=list2.get(j).getPort();
                       String id=list2.get(j).getId();

                       //这个仓位没有电池，或有电池但不是同一个电池
                       if(( map1.get("id"+port)==null)||( !id.equals(map1.get("id"+port)))){
                           insertMap.put("id"+port,id);
                           batteryHB3InStationTempList.add(new BatteryHB3InStationTemp(id,maps.getKey(),port,list2.get(j).getT(), mapStation.get((String) maps.getKey())==null?0: mapStation.get((String) maps.getKey()),list2.get(j).getDate()));
                       }

                   }
                   if(insertMap.size()>0){
                       jedis.hmset(batteryInstation+maps.getKey(),insertMap);
                   }
               }
               if(insertBatteryNumbersInStation.size()>0) {
                   jedis.hmset(batteryNumbersInStation, insertBatteryNumbersInStation);
               }
               if(delBatteryNumbersInStation.size()>0){
                   String[] strings = new String[delBatteryNumbersInStation.size()];
                   jedis.hdel(batteryNumbersInStation, strings);
               }

               //可用换电减去失联的
               Set<String> pidLosts= jedis.smembers(lostStationKey);
               List<String> deletePidLostList=new ArrayList<>();
               for(String pidLost: pidLosts){
                   int cityCode=mapStation.get(pidLost)==null?0:mapStation.get(pidLost);
                   deletePidLostList.add(pidLost+"|"+cityCode);
               }
               if(deletePidLostList.size()>0) {
                   String[] strings = new String[deletePidLostList.size()];
                   jedis.hdel(batteryNumbersInStation, deletePidLostList.toArray(strings));
               }
//                    if(insertBatteryNumberMap.size()>0){
//                        jedis.hmset(batteryNumbersInStation,insertBatteryNumberMap);
//                    }

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
        listMysql.add(new Column("hide", "0", Types.VARCHAR));

        return jdbcClient.select("select city_code, sn,pid from t_station where hide = ?", listMysql);



    }

            @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
            // declarer.declare(new Fields("count"));
            }

  }
