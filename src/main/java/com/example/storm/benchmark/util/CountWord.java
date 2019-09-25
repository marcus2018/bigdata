package com.example.storm.benchmark.util;


import com.example.storm.bean.CabinetHBTime;
import com.example.storm.bean.StationLost;
import com.google.common.collect.Sets;
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

public class CountWord extends BaseWindowedBolt {

    private static final long serialVersionUID = -5283595260540124273L;
    private FileWriter writer = null;

    private OutputCollector collector;
    private static Map<String, Integer> counters = new ConcurrentHashMap<String, Integer>();

    private JedisPool pool;
    private  static     Map<String,StationLost> stringStationLostMap=new HashMap<>();
    private SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private SimpleDateFormat sdf1=new SimpleDateFormat("yyyy-MM-dd");
    private static String preLostStation="monitor:loststationPidDetail:";
    private static String preMaxTimeHB="monitor:lastStationHBPid:";
    private ConnectionProvider connectionProvider;
    private  static String hasHBStationKey="monitor:station_pid_HB";
    private static String lostStationKey="monitor:station_pid_lost";






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
        Map<String,List<CabinetHBTime>> listMap=new HashMap<>();

        for (Tuple tuple : inputWindow.get()) {
            if(listMap.containsKey(tuple.getValueByField("key"))){
               List<CabinetHBTime> list= listMap.get(tuple.getValueByField("key"));
                list.add((CabinetHBTime) tuple.getValueByField("count"));
                listMap.put((String) tuple.getValueByField("key"),list);
            }else{
                List<CabinetHBTime> list=new ArrayList<>();
                list.add((CabinetHBTime) tuple.getValueByField("count"));
                listMap.put((String) tuple.getValueByField("key"),list);
            }
         //   listMap.put(tuple.getValueByField("key"),tuple.getValueByField("count"));
    /*        Map<String,List<CabinetHBTime>> tmp = (Map<String,List<CabinetHBTime>>)tuple.getValueByField("count");
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
            }*/
            //有心跳的柜子    无心跳的柜子     柜子停止时长
            //有心跳的柜子
            Set<String>  hasHBStationPid=jedis.smembers(hasHBStationKey);
           Set<String> diffSet= Sets.difference((Set<String>)listMap.keySet(),hasHBStationPid);

          for(Map.Entry<String, List<CabinetHBTime>> maps:listMap.entrySet()){
              List<CabinetHBTime>    ss= maps.getValue();
              Collections.sort(ss);
            //  System.out.println(maps.getKey()+"============"+ss.get(ss.size()-1).getDate());
          }
          if(hasHBStationPid.size()!=0){

                List<String> listHasLostStationPid=new ArrayList();//失恋的柜子
                for (String pid : hasHBStationPid) {

                    if(listMap.containsKey(pid)){//redis保存的柜子当前有心跳
                        List<CabinetHBTime> cabinetHBTimeList= listMap.get(pid);
                        Collections.sort(cabinetHBTimeList);
                       // System.out.println(cabinetHBTimeList.get(cabinetHBTimeList.size() - 1).getDate()+"###################### "+pid);
                     //   if (cabinetHBTimeList.get(cabinetHBTimeList.size() - 1).getDate().getTime()-jedis.hm)
                        jedis.hset(preLostStation+pid,"maxTime", String.valueOf(cabinetHBTimeList.get(cabinetHBTimeList.size() - 1).getDate().getTime()));//更新最大心跳时间


                    }else{
                        //redis保存的柜子当前无心跳，计算失联时间
                        Map<String,String> map=  jedis.hgetAll(preLostStation+pid);
                        long duration=new Date().getTime()-Long.parseLong( map.get("maxTime"));
                     //   System.out.println(pid+"==================="+new Date()+" "+Long.parseLong( map.get("maxTime")));
                        if( duration>=90*1000){
                            listHasLostStationPid.add(pid);
                            Map<String,String> filedMap=new HashMap();
                            filedMap.put("maxTime", map.get("maxTime"));
                            filedMap.put("duration",(Long.parseLong(map.get("duration"))+duration)+"");
                            filedMap.put("times",(Integer.parseInt(map.get("times"))+1)+"");
                            jedis.hmset(preLostStation+pid,filedMap);//更改失联详情
                        }

                    }
                }
                if(listHasLostStationPid.size()>0) {
                    String[] strings = new String[listHasLostStationPid.size()];
                    jedis.del(lostStationKey);
                    jedis.sadd(lostStationKey, (String[]) listHasLostStationPid.toArray(strings));
                }
                if(diffSet.size()!=0){
                    List<String> listHasHBStationPid=new ArrayList();//有心跳的柜子
                    for(String key:diffSet){
                        listHasHBStationPid.add(key);
                        List<CabinetHBTime> list=(List<CabinetHBTime>)listMap.get(key);
                        Map<String,String> filedMap=new HashMap();
                        filedMap.put("maxTime", String.valueOf(list.get(list.size() - 1).getDate().getTime()));
                        filedMap.put("duration", "0");
                        filedMap.put("times","0");
                        jedis.hmset(preLostStation+key,filedMap);
                        // jedis.hset(preLostStation+maps.getKey(),"maxTime", String.valueOf(list.get(list.size() - 1).getDate().getTime()));
                    }
                    String[] strings = new String[listHasHBStationPid.size()];
                    jedis.sadd(hasHBStationKey,(String[])listHasHBStationPid.toArray(strings));
                }

            }else{
                List<String> listHasHBStationPid=new ArrayList();//有心跳的柜子
                for (Map.Entry maps:listMap.entrySet()){
                    listHasHBStationPid.add((String)maps.getKey());
                    List<CabinetHBTime> list=(List<CabinetHBTime>)maps.getValue();
                    Map<String,String> filedMap=new HashMap();
                    filedMap.put("maxTime", String.valueOf(list.get(list.size() - 1).getDate().getTime()));
                    filedMap.put("duration", "0");
                    filedMap.put("times","0");
                    jedis.hmset(preLostStation+maps.getKey(),filedMap);
                   // jedis.hset(preLostStation+maps.getKey(),"maxTime", String.valueOf(list.get(list.size() - 1).getDate().getTime()));

                }
                String[] strings = new String[listHasHBStationPid.size()];
                jedis.sadd(hasHBStationKey,(String[])listHasHBStationPid.toArray(strings));
            }



        }
        if(jedis!=null){
            jedis.close();
        }

    }

    public void declareOutputFields(OutputFieldsDeclarer declarer) {
    }
}