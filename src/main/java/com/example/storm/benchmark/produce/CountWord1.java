package com.example.storm.benchmark.produce;


import com.example.storm.bean.CabinetHBTime;
import com.example.storm.bean.StationLost;
import com.example.storm.benchmark.util.DateUtils4Vo;
import com.google.common.collect.Maps;
import org.apache.storm.jdbc.common.ConnectionProvider;
import org.apache.storm.jdbc.common.HikariCPConnectionProvider;
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
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class CountWord1 extends BaseWindowedBolt {

    private static final long serialVersionUID = -5283595260540124273L;
    private FileWriter writer = null;

    private OutputCollector collector;
    private static Map<String, Integer> counters = new ConcurrentHashMap<String, Integer>();

    private JedisPool pool;
    private  static     Map<String,StationLost> stringStationLostMap=new HashMap<>();
    private SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private SimpleDateFormat sdf1=new SimpleDateFormat("yyyy-MM-dd");
    private static String preLostStation="loststationPid:";
    private ConnectionProvider connectionProvider;




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
        pool = new JedisPool(config, "r-wz9gjghb9zyfa6ciyv.redis.rds.aliyuncs.com", 6379,20000,"Ehdbigdata190418");
        //pool = new JedisPool(config, "119.23.133.72", 6574,20000,"immotor!6574@.com");
        Map hikariConfigMap = Maps.newHashMap();
        hikariConfigMap.put("dataSourceClassName","com.mysql.jdbc.jdbc2.optional.MysqlDataSource");
        hikariConfigMap.put("dataSource.url", "jdbc:mysql://rm-wz99y4784rwj1w48txo.mysql.rds.aliyuncs.com:3306/immotordata");
        // hikariConfigMap.put("dataSource.url", "jdbc:mysql://rm-wz99y4784rwj1w48t.mysql.rds.aliyuncs.com:3306/immotordata");
        hikariConfigMap.put("dataSource.user","immotor_biddata");
        hikariConfigMap.put("dataSource.password","immotor!99");
        connectionProvider = new HikariCPConnectionProvider(hikariConfigMap);
        //对数据库连接池进行初始化
        connectionProvider.prepare();


    }

    public void execute(TupleWindow inputWindow) {
        Jedis jedis= pool.getResource();
        int sum = 0;
        Map<String,List<CabinetHBTime>> listMap=new HashMap<>();
        for (Tuple tuple : inputWindow.get()) {
            Map<String,List<CabinetHBTime>> tmp = (Map<String,List<CabinetHBTime>>)tuple.getValueByField("count");
            if(listMap.size()==0){
                listMap=tmp;
            }else {
               for (Map.Entry map:
                     tmp.entrySet()) {
                    if(listMap.containsKey( (String) map.getKey())){
                         List<CabinetHBTime> list=  listMap.get(map.getKey()) ;
                         list.addAll((List)map.getValue());
                         listMap.put((String) map.getKey(),list);
                    }else{
                        List<CabinetHBTime> list=  new ArrayList<>();
                        list.addAll((List)map.getValue());
                        listMap.put((String) map.getKey(),list);
                    }
                }
            }



            for (Map.Entry maps:listMap.entrySet()){

                Set<String> redisKeysSet=jedis.keys(preLostStation+maps.getKey()+"*");
                String redisKeys="";
                for (String str:
                redisKeysSet) {
                    redisKeys=str;
                }

                List<CabinetHBTime> list=(List<CabinetHBTime>)maps.getValue();
                Collections.sort(list);
                String keys=preLostStation+maps.getKey()+":"+list.get(list.size() - 1).getDate().getTime();
                if( jedis.exists(redisKeys)){
                   String maxTime= redisKeys.split(":")[2];
                   long duration= Long.valueOf(jedis.hget(redisKeys,"duration")==null? "0":jedis.hget(redisKeys,"duration"));
                   int times= Integer.parseInt(jedis.hget(redisKeys,"times")==null?"1":jedis.hget(redisKeys,"times"));
                    if(list.get(0).getDate().getTime()- Long.parseLong(maxTime)>=1000*60){//1分钟无心跳算失联
                        times++;
                        duration+=((list.get(0).getDate().getTime()-Long.parseLong(maxTime))/1000);
                    }
                    jedis.del(redisKeys);
                    if(DateUtils4Vo.judgmentDate(new Date(Long.parseLong(maxTime)))<0){
                        long duration1=  list.get(0).getDate().getTime()-DateUtils4Vo.getDate(DateUtils4Vo.getToday(),"yyyy-MM-dd").getTime();
                        if(duration1-1000*60>=0) {

                            jedis.hset(keys,"maxTime", String.valueOf(list.get(list.size() - 1).getDate().getTime()));
                            jedis.hset(keys,"duration", String.valueOf( duration1/1000));
                            jedis.hset(keys,"times", String.valueOf( 1));  }
                    }else {
                        if(list.get(list.size() - 1).getDate().getTime()-Long.parseLong(maxTime)>0){
                        jedis.hset(keys,"maxTime", String.valueOf(list.get(list.size() - 1).getDate().getTime()));
                        jedis.hset(keys,"duration", String.valueOf( duration));
                        jedis.hset(keys,"times", String.valueOf( times));}
                    }

                }else{
                    long duration= list.get(list.size()-1).getDate().getTime()-DateUtils4Vo.getDate(DateUtils4Vo.getToday(),"yyyy-MM-dd").getTime();
                    if(duration-1000*60>=0) {//失联状态  maxTime,pid,duration,times,sn,addr,ccode
                        jedis.hset(keys,"maxTime", String.valueOf(list.get(list.size() - 1).getDate().getTime()));
                        jedis.hset(keys,"duration", String.valueOf( duration/1000));
                        jedis.hset(keys,"times", String.valueOf( 1));

                    }
                }
            }


        }
        if(jedis!=null){
            jedis.close();
        }

    }

    public void declareOutputFields(OutputFieldsDeclarer declarer) {
    }
}