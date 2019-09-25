package com.example.storm.benchmark.produce.cabinet.cabinetFullPort;

import com.aliyun.openservices.log.common.LogGroupData;
import com.aliyun.openservices.log.common.Logs;
import com.aliyun.openservices.log.exception.LogException;
import com.aliyun.openservices.loghub.stormspout.LogHubSpout;
import com.example.storm.bean.CabinetHBTime;
import com.example.storm.bean.Datas;
import com.example.storm.benchmark.produce.cabinet.cabinetFullPort.bean.FullStationStoE;
import com.example.storm.benchmark.util.DateUtils4Vo;
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

public class FullPortBolt extends BaseWindowedBolt {
           private OutputCollector collector;
           private SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
           private static String fullStationDetail="monitor:fullStationPidDetail:";
           private static String fullStationKey="monitor:station_pid_full";
           private static String vaildStationNumbers="monitor:station_vaild_numbers";
           private static String lostStationKey="monitor:station_pid_lost";
           private JedisPool pool;
           private ConnectionProvider connectionProvider;
           private JdbcClient jdbcClient;
           private static volatile Map<String,Integer> mapStation=new HashMap<>();
           private  static volatile Long times=new Date().getTime();


    @Override
            public void prepare(Map stormConf, TopologyContext context,
               OutputCollector collector) {
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

       // pool = new JedisPool(config, "10.27.169.187", 6574,20000,"immotor!6574@.com");
        pool = new JedisPool(config, "r-wz9gjghb9zyfa6ciyv.redis.rds.aliyuncs.com", 6379,20000,"Ehdbigdata190418");

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
                //List list = new ArrayList<>();
                Map<String,CabinetHBTime> listMap=new HashMap<>();

              /* List<Tuple> tuplesInWindow = inputWindow.get();
               List<Tuple> newTuples = inputWindow.getNew();
               List<Tuple> expiredTuples = inputWindow.getExpired();*/



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
                           CabinetHBTime cabinetHBTime=new CabinetHBTime();

                           for (Logs.Log.Content content : log.getContentsList()) {
                              if(content.getKey().equals("pID")){
                                   sb.append("\t").append(content.getKey()).append(":")
                                           .append(content.getValue());
                                        cabinetHBTime.setPid(content.getValue());
                                   }
                               if(content.getKey().equals("type")){
                                   sb.append("\t").append(content.getKey()).append(":")
                                           .append(content.getValue());
                                   cabinetHBTime.setType(Integer.parseInt(content.getValue()));
                               }
                               if(content.getKey().equals("datas")){
                                   sb.append("\t").append(content.getKey()).append(":")
                                           .append(content.getValue());
                               //    System.out.println(content.getValue());
                                  datas=content.getValue();
                               }
                               if(content.getKey().equals("time1")){
                                   sb.append("\t").append(content.getKey()).append(":")
                                           .append(content.getValue());

                                   try {
                                       cabinetHBTime.setDate( sdf.parse(content.getValue()));
                                   } catch (ParseException e) {

                                   }

                               }


                           }

                         if(cabinetHBTime.getType()!=2)
                                continue;

                           Datas datas1= new Gson().fromJson(datas,Datas.class);
                           //||(datas1.getValid()==0&&datas1.getValid48()==0)
                        //   System.out.println(datas1.getVaild()+"==============="+datas1.getVaild48());
                           if(datas1.getEmpty()<0){
                               cabinetHBTime.setFull(1);
                           }else{
                               cabinetHBTime.setFull(0);
                           }
                           cabinetHBTime.setVaild(datas1.getValid()+datas1.getValid48());

                           listMap.put(cabinetHBTime.getPid(),cabinetHBTime);


                       }
                   }



               }
               if(new Date().getTime()>times) {
                   synchronized (FullPortBolt.class){
                       if(new Date().getTime()>times) {
                           List<List<Column>> select = getStationCode();
                           for(int i=0;i<select.size();i++){
                               mapStation.put((String) select.get(i).get(2).getVal(),(Integer) select.get(i).get(0).getVal());
                           }
                           times += 4 * 60 * 60 * 1000;
                       }
                   }
               }

               Jedis jedis= pool.getResource();
               Set<String> fullStationPidKey=jedis.smembers(fullStationKey);
               List<String> insertFullStationKey=new ArrayList<>();
               List<String> deleteFullStationKey=new ArrayList<>();
               //满仓时间计算
               List<FullStationStoE> fullStationStoEList=new ArrayList();
               //可用换电减去失联的
               Set<String> pidLosts= jedis.smembers(lostStationKey);
               List<String> deletePidLostList=new ArrayList<>();
               for(String pidLost: pidLosts){
                   int cityCode=mapStation.get(pidLost)==null?0:mapStation.get(pidLost);
                   deletePidLostList.add(pidLost+"|"+cityCode);
                   //失联删除无空仓状态
                   deleteFullStationKey.add(pidLost);
               }

               for(Map.Entry<String,CabinetHBTime> maps:listMap.entrySet()){
                   int cityCode=mapStation.get(maps.getKey())==null?0:mapStation.get(maps.getKey());
                   Map<String,String> map=new HashMap<>();
                  if(maps.getValue().getVaild()>0) {
                       jedis.hset(vaildStationNumbers, maps.getKey() + "|" + cityCode, maps.getValue().getVaild() + "");//
                  }



                   Map<String,String> mapFullDatil= jedis.hgetAll(fullStationDetail+maps.getKey());
                   if (fullStationPidKey.contains( maps.getKey())){
                       //之前是满仓
                       if (maps.getValue().getFull()==1&&maps.getValue().getDate().getTime()-Long.parseLong(mapFullDatil.get("minTime"))<0){//一直是满仓，更新小时间（tuple无序）
                           jedis.hset(fullStationDetail+ maps.getKey(),"minTime",maps.getValue().getDate().getTime()+"");
                       }else if(maps.getValue().getFull()==0&&maps.getValue().getDate().getTime()-Long.parseLong(mapFullDatil.get("minTime"))>0){
                           //变成非满仓，更新信息
                           deleteFullStationKey.add(maps.getKey());
                           Map<String, String> filedMap = new HashMap();
                          if(!DateUtils4Vo.isToday(Long.parseLong(mapFullDatil.get("minTime")),DateUtils4Vo.DATE_STR_FORMAT)){
                              filedMap.put("minTime", maps.getValue().getDate().getTime() + "");
                              filedMap.put("duration", "0");
                              filedMap.put("times", "0");
                              jedis.hmset(fullStationDetail + maps.getKey(), filedMap);

                          }else {
                              if(((maps.getValue().getDate().getTime() - Long.parseLong(mapFullDatil.get("minTime"))) / 1000) >60) {//60秒
                                  filedMap.put("duration", (Integer.parseInt(mapFullDatil.get("duration")) + (maps.getValue().getDate().getTime() - Long.parseLong(mapFullDatil.get("minTime"))) / 1000) + "");
                                  filedMap.put("times", (Integer.parseInt(mapFullDatil.get("times")) + 1) + "");
                                  jedis.hmset(fullStationDetail + maps.getKey(), filedMap);
                                  //统计一次满仓时长
                                  long sTime = Long.parseLong(mapFullDatil.get("minTime"));
                                  long eTime = maps.getValue().getDate().getTime();
                                  String pid = maps.getKey();
                                  fullStationStoEList.add(new FullStationStoE(sTime, eTime, (eTime - sTime) / 1000, pid, 1));
                              }else{//清零
                                  filedMap.put("minTime", maps.getValue().getDate().getTime() + "");
                                  filedMap.put("duration", (Integer.parseInt(mapFullDatil.get("duration")))+"");
                                  filedMap.put("times",Integer.parseInt(mapFullDatil.get("times")) +"");
                                  jedis.hmset(fullStationDetail + maps.getKey(), filedMap);
                              }
                          }
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
               if(deletePidLostList.size()>0) {
                   String[] strings = new String[deletePidLostList.size()];
                   jedis.hdel(vaildStationNumbers, deletePidLostList.toArray(strings));
               }
               if(fullStationStoEList.size()>0){//满仓开始结束时间
                   LoghubService loghubService=new LoghubService();
                   loghubService.sendFullStationStoE(fullStationStoEList);
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
        //     declarer.declare(new Fields("count"));
            }

  }
