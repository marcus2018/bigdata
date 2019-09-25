package com.example.storm.benchmark.produce;


import com.example.storm.bean.CabinetHBTime;
import com.example.storm.bean.StationLost;
import com.example.storm.benchmark.produce.cabinet.cabinetFullPort.bean.FullStationStoE;
import com.example.storm.benchmark.util.*;
import com.google.common.collect.Sets;
import org.apache.storm.jdbc.common.Column;
import org.apache.storm.jdbc.common.ConnectionProvider;
import org.apache.storm.jdbc.common.JdbcClient;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseWindowedBolt;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.windowing.TupleWindow;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.FileWriter;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class CountWord extends BaseWindowedBolt {

    private static final long serialVersionUID = -5283595260540124273L;
    private FileWriter writer = null;

    private OutputCollector collector;
    private static Map<String, Integer> counters = new ConcurrentHashMap<String, Integer>();
    private JdbcClient jdbcClient;
    private JedisPool pool;
    private  static     Map<String,StationLost> stringStationLostMap=new HashMap<>();
    private SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private SimpleDateFormat sdf1=new SimpleDateFormat("yyyy-MM-dd");
    private static String preLostStation="monitor:loststationPidDetail:";
    private ConnectionProvider connectionProvider;
    private  static String hasHBStationKey="monitor:station_pid_HB";
    private static String lostStationKey="monitor:station_pid_lost";
    private static  Set<String> setStation=new HashSet<>();
    private  static volatile Long times=new Date().getTime();






    public void prepare(Map stormConf, TopologyContext context
            , OutputCollector collector) {
        this.collector = collector;
        pool = JedisUtil.getJedisPool();
        jdbcClient= MysqlUtil.getJdbcClient();

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
            //有心跳的柜子    无心跳的柜子     柜子停止时长
            //有心跳的柜子
            Set<String>  hasHBStationPid=jedis.smembers(hasHBStationKey);
           Set<String> diffSet= Sets.difference((Set<String>)listMap.keySet(),hasHBStationPid);
            //失联时间计算
            List<FullStationStoE> lostStationStoEList=new ArrayList();

            //每八个小时增加新增电柜，删除无效电柜
            if(new Date().getTime()>times) {
                synchronized (CountWord.class){
                    if(new Date().getTime()>times) {
                        setStation.clear();
                        List<List<Column>> select = getStationCode();
                        List<String> listHasHBStationPid=new ArrayList();//有心跳的柜子
                        for(int i=0;i<select.size();i++){
                            setStation.add((String) select.get(i).get(0).getVal());
                            String pid=(String)select.get(i).get(0).getVal();
                            listHasHBStationPid.add((String) select.get(i).get(0).getVal());
                            if(!StringVerifyUtils.isBlank(pid)&&hasHBStationPid.size()!=0&&!hasHBStationPid.contains(pid)){
                                    Map<String,String> filedMap=new HashMap();
                                    filedMap.put("maxTime", String.valueOf(new Date().getTime()));
                                    filedMap.put("duration", "0");
                                    filedMap.put("times","0");
                                    jedis.hmset(preLostStation+pid,filedMap);
                            }
                        }
                        if(listHasHBStationPid.size()>0) {
                            String[] strings = new String[listHasHBStationPid.size()];
                            jedis.sadd(hasHBStationKey, (String[]) listHasHBStationPid.toArray(strings));//之前有过心跳贵子
                        }
                        Set<String> inVaild= Sets.difference(hasHBStationPid,setStation);
                        if(inVaild.size()>0){
                            List<String> inValidPid=new ArrayList();//无效柜子删除
                            for(String key:inVaild){
                                inValidPid.add(key);
                            }
                            String[] inValidPidstring = new String[inValidPid.size()];
                            jedis.srem(hasHBStationKey,(String[])inValidPid.toArray(inValidPidstring));
                            jedis.srem(lostStationKey, (String[])inValidPid.toArray(inValidPidstring));
                        }
                        }
                        times += 8 * 60 * 60 * 1000;

                    }
                }



         if(hasHBStationPid.size()!=0){

                List<String> listHasLostStationPid=new ArrayList();//失联的柜子
               List<String> listHasAgainHBStationPid=new ArrayList();//重联的柜子

             for (String pid : hasHBStationPid) {
                    Map<String,String> map=  jedis.hgetAll(preLostStation+pid);
                    if(listMap.containsKey(pid)){
                        //redis保存的柜子当前有心跳
                       CabinetHBTime cabinetHBTimeList= listMap.get(pid);
                        listHasAgainHBStationPid.add(pid);

                        Map<String,String> filedMap=new HashMap();
                        long duration=cabinetHBTimeList.getDate().getTime()/1000-Long.parseLong( map.get("maxTime"))/1000;
                        if(duration>90){
                            if(DateUtils4Vo.isToday(Long.parseLong(map.get("maxTime")),DateUtils4Vo.DATE_STR_FORMAT)){
                                filedMap.put("duration",(Long.parseLong(map.get("duration"))+duration)+"");
                                filedMap.put("times",(Integer.parseInt(map.get("times"))+1)+"");
                            }else{
                                filedMap.put("duration",duration+"");
                                filedMap.put("times",1+"");
                            }
                          /*  filedMap.put("duration",(Long.parseLong(map.get("duration"))+duration)+"");
                            filedMap.put("times",(Integer.parseInt(map.get("times"))+1)+"");*/
                            lostStationStoEList.add(new FullStationStoE(Long.parseLong( map.get("maxTime")),cabinetHBTimeList.getDate().getTime(),duration,pid,2));

                        }
                        if(duration>0) {
                            filedMap.put("maxTime", String.valueOf(cabinetHBTimeList.getDate().getTime()));
                            jedis.hmset(preLostStation+pid,filedMap );//更新最大心跳时间
                        }

                    }else{
                        //redis保存的柜子当前无心跳，计算失联时间
                        long duration=new Date().getTime()/1000-Long.parseLong( map.get("maxTime"))/1000;
                        if( duration>=90){
                            if(!DateUtils4Vo.isToday(Long.parseLong(map.get("maxTime")),DateUtils4Vo.DATE_STR_FORMAT)){
                                Map<String,String> filedMap=new HashMap();
                                filedMap.put("duration",0+"");
                                filedMap.put("times",0+"");
                                jedis.hmset(preLostStation+pid,filedMap );//更新详情信息
                            }
                            listHasLostStationPid.add(pid);
                        }

                    }
                }
                if(lostStationStoEList.size()>0){
                    LoghubService loghubService=new LoghubService();
                    loghubService.sendFullStationStoE(lostStationStoEList);
                }
             if(listHasAgainHBStationPid.size()>0) {
                 String[] strings = new String[listHasAgainHBStationPid.size()];
                 jedis.srem(lostStationKey, (String[]) listHasAgainHBStationPid.toArray(strings));
             }
                if(listHasLostStationPid.size()>0) {
                    String[] strings = new String[listHasLostStationPid.size()];
                   // jedis.del(lostStationKey);
                    jedis.sadd(lostStationKey, (String[]) listHasLostStationPid.toArray(strings));
                }

                if(diffSet.size()>0){
                    List<String> listHasHBStationPid=new ArrayList();//有心跳的柜子
                    for(String key:diffSet){
                        listHasHBStationPid.add(key);
                        CabinetHBTime list=(CabinetHBTime)listMap.get(key);
                        Map<String,String> filedMap=new HashMap();
                        filedMap.put("maxTime", String.valueOf(list.getDate().getTime()));
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
                for (Map.Entry<String,CabinetHBTime> maps:listMap.entrySet()){
                    listHasHBStationPid.add((String)maps.getKey());
                    CabinetHBTime cabinetHBTime=maps.getValue();
                    Map<String,String> filedMap=new HashMap();
                    filedMap.put("maxTime", String.valueOf(cabinetHBTime.getDate().getTime()));
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

    public List<List<Column>>  getStationCode(){
        //查询该word是否存在
        List<Column> listMysql = new ArrayList<>();
        //创建一列将值传入   列名  值    值的类型
        listMysql.add(new Column("status", "1", Types.VARCHAR));

        return jdbcClient.select("select pid from t_station where status = ?", listMysql);



    }

    public void declareOutputFields(OutputFieldsDeclarer declarer) {
    }
}