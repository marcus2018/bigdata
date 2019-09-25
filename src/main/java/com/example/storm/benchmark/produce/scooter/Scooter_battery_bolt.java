package com.example.storm.benchmark.produce.scooter;

import com.aliyun.openservices.log.common.LogGroupData;
import com.aliyun.openservices.log.common.Logs;
import com.aliyun.openservices.log.exception.LogException;
import com.aliyun.openservices.loghub.stormspout.LogHubSpout;
import com.example.storm.benchmark.produce.cabinet.cabinet_battery_temp.LogBatteryInStationBolt;
import com.example.storm.benchmark.produce.scooter.bean.ScooterBats;
import com.example.storm.benchmark.util.DistanceUtil;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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

public class Scooter_battery_bolt extends BaseWindowedBolt {
    private OutputCollector collector;


    private JedisPool pool;
    private SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private SimpleDateFormat sdf1=new SimpleDateFormat("yyyy-MM-dd");

    private static String batteryNumbersInScooter="monitor:scooter_battery_numbers";
    private static String scooterTracker="monitor:scooter_track:";//轨迹详情
    private static String scooterTrackerDistanceGTE1000="monitor:scooter_track_distance_gte1000";//轨迹距离大于1000m的车
    private ConnectionProvider connectionProvider;
    private JdbcClient jdbcClient;
    private volatile Map<String,Integer> mapScooter=new HashMap<>();
    private  static volatile Long times=new Date().getTime();
    private  static  Map<String,Integer> scooterCodeMap=new HashMap<>();
    private static Set<String> scooterCodeIS1=new HashSet<>();

    @Override
    public void prepare(Map stormConf, TopologyContext context,
                        OutputCollector collector) {
        this.collector = collector;
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
       /* hikariConfigMap.put("dataSourceClassName","com.mysql.jdbc.jdbc2.optional.MysqlDataSource");
        hikariConfigMap.put("dataSource.url", "jdbc:mysql://119.23.133.72:3306/power2_test");
        hikariConfigMap.put("dataSource.user","root");
        hikariConfigMap.put("dataSource.password","Immotor!99");*/
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

        Map<String,ScooterBats> listMap=new HashMap<>();



        if(new Date().getTime()>times) {
            synchronized (LogBatteryInStationBolt.class){
                if(new Date().getTime()>times) {
                    List<List<Column>> select = getScooterCode();
                    for(int i=0;i<select.size();i++){
                        mapScooter.put((String) select.get(i).get(1).getVal(),(Integer) select.get(i).get(0).getVal());
                    }
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
                    ScooterBats scooterBats=new ScooterBats();

                    for (Logs.Log.Content content : log.getContentsList()) {
                        if(content.getKey().equals("sID")){
                            sb.append("\t").append(content.getKey()).append(":")
                                    .append(content.getValue());
                            scooterBats.setsID(content.getValue());
                        }
                        if(content.getKey().equals("soc")){
                            scooterBats.setSoc(Integer.parseInt(content.getValue()));
                        }
                        if(content.getKey().equals("location")){
                            sb.append("\t").append(content.getKey()).append(":")
                                    .append(content.getValue());
                            scooterBats.setLocation(content.getValue());
                        }
                        if(content.getKey().equals("bats")){
                            sb.append("\t").append(content.getKey()).append(":")
                                    .append(content.getValue());
                            datas=content.getValue();
                            String jsonString="";
                            if(datas.contains("\"bats\":{")){
                                 jsonString=datas;
                            }else {
                                 jsonString = "{ \"bats\":" + datas + "}";
                            }
                            JsonObject obj = new JsonParser().parse(jsonString).getAsJsonObject();
                            ScooterBats scooterBatsInner = new Gson().fromJson(obj, ScooterBats.class);
                            scooterBats.setNumber(scooterBatsInner.getBats().size());
                          //  scooterBats.setBats(scooterBatsInner.getBats());
                        }

                        if(content.getKey().equals("time1")){
                            sb.append("\t").append(content.getKey()).append(":")
                                    .append(content.getValue());

                            try {
                                scooterBats.setDate( sdf.parse(content.getValue()));
                            } catch (ParseException e) {

                            }

                        }
                    }
                    listMap.put(scooterBats.getsID(),scooterBats);

                }
            }

        }
       Jedis jedis= pool.getResource();
        //更新车上电池数量
        Map<String,String> insertBatteryNumbersInScooter=new HashMap<>();
        //删除车（没有城市属性的）
        List<String> delBatteryNumbersInScooter=new ArrayList<>();
       // Map<String,String> mapInsert=new HashMap<>();
        for (Map.Entry<String,ScooterBats> scooterBats:listMap.entrySet()){
                    String sID= scooterBats.getKey();
                    Integer code=mapScooter.get(sID);
                    if (code!=null) {
                        if(code==-1||code==0){
                            code=scooterCodeMap.get(sID);
                            if(code==null){
                                code=-1;
                                scooterCodeIS1.add(sID);
                            }
                            insertBatteryNumbersInScooter.put(sID + "|" + code, scooterBats.getValue().getNumber() + "");

                        }else {

                            insertBatteryNumbersInScooter.put(sID + "|" + code, scooterBats.getValue().getNumber() + "");
                            if(scooterCodeIS1.contains(sID)){
                                delBatteryNumbersInScooter.add(sID+"|-1");
                                scooterCodeIS1.remove(sID);
                            }
                            scooterCodeMap.put(sID,code);
                        }
                    }
                ScooterBats scooterBats1= scooterBats.getValue();
               // List<Bats> batsList= scooterBats1.getBats();
                Date nowTime=scooterBats1.getDate();
                String nowLocation=scooterBats1.getLocation();
                int soc=scooterBats1.getSoc();
                Map<String,String> redisMapScooterTrack=jedis.hgetAll(scooterTracker+sID);
                Map<String,String> insertMapScooterTrack=new HashMap<>();
                if(nowLocation.equals("0,0"))
                    continue;
                if(redisMapScooterTrack.size()>0){
                    long redisDetailNowTimes= Long.parseLong(redisMapScooterTrack.get("nowTime"));
                    double nowDistance=Double.parseDouble(redisMapScooterTrack.get("nowDistance"));
                    String location=redisMapScooterTrack.get("nowLocation");

                    if((nowTime.getTime()-redisDetailNowTimes>5*60*1000)){
                        //大于5分钟
                      //  if(nowDistance<1000) {
                            jedis.del(scooterTracker + sID);
                            insertMapScooterTrack.put("startTime", nowTime.getTime() + "");
                            insertMapScooterTrack.put("nowTime", nowTime.getTime() + "");
                            insertMapScooterTrack.put("startLocation", nowLocation + "");
                            insertMapScooterTrack.put("nowLocation", nowLocation + "");
                            insertMapScooterTrack.put("nowDistance", 0 + "");
                            insertMapScooterTrack.put("code", mapScooter.get(sID) + "");
                            insertMapScooterTrack.put("soc",soc+"");
                      //  }
                    }else if(redisDetailNowTimes<nowTime.getTime()){
                        insertMapScooterTrack.put("nowTime",nowTime.getTime()+"");
                        insertMapScooterTrack.put("nowLocation",nowLocation+"");
                        Double distance= DistanceUtil.LantitudeLongitudeDist(Double.parseDouble(nowLocation.split(",")[0]),
                                Double.parseDouble(nowLocation.split(",")[1]),
                                Double.parseDouble(location.split(",")[0]),Double.parseDouble(location.split(",")[1]));
                        insertMapScooterTrack.put("nowDistance",(distance+nowDistance)+"");
                        insertMapScooterTrack.put("soc",soc+"");
                        if((distance+nowDistance)>=1000){
                            Map<String,String> mapInsert=new HashMap<>();
                            mapInsert.put(sID,nowTime.getTime()+"");
                            jedis.hmset(scooterTrackerDistanceGTE1000, mapInsert);
                        }
                    }

                }else{
                    insertMapScooterTrack.put("startTime",nowTime.getTime()+"");
                    insertMapScooterTrack.put("nowTime",nowTime.getTime()+"");
                    insertMapScooterTrack.put("startLocation",nowLocation+"");
                    insertMapScooterTrack.put("nowLocation",nowLocation+"");
                    insertMapScooterTrack.put("nowDistance",0+"");
                    insertMapScooterTrack.put("code",mapScooter.get(sID)+"");
                    insertMapScooterTrack.put("soc",soc+"");
                }
                if(insertMapScooterTrack.size()>0){
                    jedis.hmset(scooterTracker+sID,insertMapScooterTrack);
                }
        }
        if(insertBatteryNumbersInScooter.size()>0) {
            jedis.hmset(batteryNumbersInScooter, insertBatteryNumbersInScooter);
        }
        if(delBatteryNumbersInScooter.size()>0){
           String[] strings = new String[delBatteryNumbersInScooter.size()];
            jedis.hdel(batteryNumbersInScooter, delBatteryNumbersInScooter.toArray(strings));
        }
        if(jedis!=null){
            jedis.close();
        }

    }

    public List<List<Column>>  getScooterCode(){
        //查询该word是否存在
        List<Column> listMysql = new ArrayList();
        //创建一列将值传入   列名  值    值的类型
        listMysql.add(new Column("1", 1, Types.INTEGER));

        return jdbcClient.select("select ccode, sID from scooter_iccid where 1 = ?", listMysql);



    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {

        //declarer.declare(new Fields("count"));
    }


}

