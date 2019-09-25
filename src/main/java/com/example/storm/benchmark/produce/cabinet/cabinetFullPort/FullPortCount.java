package com.example.storm.benchmark.produce.cabinet.cabinetFullPort;


import com.example.storm.bean.CabinetHBTime;
import com.example.storm.bean.StationLost;
import org.apache.storm.jdbc.common.ConnectionProvider;
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

public class FullPortCount extends BaseWindowedBolt {

    private static final long serialVersionUID = -5283595260540124273L;
    private FileWriter writer = null;

    private OutputCollector collector;
    private static Map<String, Integer> counters = new ConcurrentHashMap<String, Integer>();

    private JedisPool pool;
    private  static     Map<String,StationLost> stringStationLostMap=new HashMap<>();
    private SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private SimpleDateFormat sdf1=new SimpleDateFormat("yyyy-MM-dd");
    private static String fullStationDetail="monitor:fullStationPidDetail:";
    private ConnectionProvider connectionProvider;
   /* private  static String hasHBStationKey="monitor:station_pid_HB";
    private static String lostStationKey="monitor:station_pid_lost";*/
    private static String fullStationKey="monitor:station_pid_full";






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



    }

    public void execute(TupleWindow inputWindow) {
        Jedis jedis= pool.getResource();
        int sum = 0;
        Map<String,CabinetHBTime> listMap=new HashMap<>();

        for (Tuple tuple : inputWindow.get()) {

            Map<String,CabinetHBTime> tmp = (Map<String,CabinetHBTime>)tuple.getValueByField("count");
            if(listMap.size()==0){
                listMap=tmp;
            }else {
               for (Map.Entry<String,CabinetHBTime> map:
                     tmp.entrySet()) {

                        listMap.put( map.getKey(), map.getValue());

                }
            }
            Set<String>  fullStationPidKey=jedis.smembers(fullStationKey);
           List<String> insertFullStationKey=new ArrayList<>();
           List<String> deleteFullStationKey=new ArrayList<>();
         for(Map.Entry<String,CabinetHBTime> maps:listMap.entrySet()){
             Map<String,String> mapFullDatil= jedis.hgetAll(fullStationDetail+maps.getKey());
            if (fullStationPidKey.contains( maps.getKey())){//之前是满仓
                if (maps.getValue().getFull()==1&&maps.getValue().getDate().getTime()-Long.parseLong(mapFullDatil.get("minTime"))<0){//一直是满仓，更新小时间（tuple无序）
                      jedis.hset(fullStationDetail+ maps.getKey(),"minTime",maps.getValue().getDate().getTime()+"");
                }else if(maps.getValue().getFull()==0&&maps.getValue().getDate().getTime()-Long.parseLong(mapFullDatil.get("minTime"))>0){//变成非满仓，更新信息
                      deleteFullStationKey.add(maps.getKey());
                          Map<String, String> filedMap = new HashMap();
                          filedMap.put("duration", (Integer.parseInt(mapFullDatil.get("duration"))+1)+"");
                          filedMap.put("times", (Integer.parseInt(mapFullDatil.get("times"))+1)+"");
                          jedis.hmset(fullStationDetail+maps.getKey(),filedMap);

                }
            }else{//前一时刻不是满仓
                if (maps.getValue().getFull()==1){
                    if(mapFullDatil.size()!=0)//判断之前是否存在detail表中
                    {
                        if(maps.getValue().getDate().getTime()-Long.parseLong(mapFullDatil.get("minTime"))>0){//更新又一次满仓时间
                            jedis.hset(fullStationDetail+ maps.getKey(),"minTime",maps.getValue().getDate().getTime()+"");
                        }
                    }else {
                        Map<String, String> filedMap = new HashMap();
                        filedMap.put("minTime", maps.getValue().getDate().getTime() + "");
                        filedMap.put("duration", "0");
                        filedMap.put("times", "0");
                        jedis.hmset(fullStationDetail+maps.getKey(),filedMap);
                    }
                    insertFullStationKey.add(maps.getKey());
                }
            }
         }
           if(insertFullStationKey.size()>0) {
               String[] strings = new String[insertFullStationKey.size()];
               jedis.sadd(fullStationKey, insertFullStationKey.toArray(strings));
           }
            if(deleteFullStationKey.size()>0) {
                String[] strings = new String[deleteFullStationKey.size()];
                jedis.srem(fullStationKey, deleteFullStationKey.toArray(strings));
            }




        }
        if(jedis!=null){
            jedis.close();
        }

    }

    public void declareOutputFields(OutputFieldsDeclarer declarer) {
    }
}