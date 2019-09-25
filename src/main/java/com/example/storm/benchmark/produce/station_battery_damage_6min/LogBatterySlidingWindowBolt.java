package com.example.storm.benchmark.produce.station_battery_damage_6min;

import com.aliyun.openservices.log.common.LogGroupData;
import com.aliyun.openservices.log.common.Logs;
import com.aliyun.openservices.log.exception.LogException;
import com.aliyun.openservices.loghub.stormspout.LogHubSpout;
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

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.*;

public class LogBatterySlidingWindowBolt extends BaseWindowedBolt {
           private OutputCollector collector;
           private SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
           private ConnectionProvider connectionProvider;
           private static volatile JedisPool pool;
           private JdbcClient jdbcClient;
           private volatile Map<String,Integer> map=new HashMap<>();
           private  static volatile Long times=new Date().getTime();
           private  static String batteryDamageInStation6min="monitor:station_battery_damage_6min";


    @Override
            public void prepare(Map stormConf, TopologyContext context,
               OutputCollector collector) {
               this.collector = collector;
        Map hikariConfigMap = Maps.newHashMap();
        hikariConfigMap.put("dataSourceClassName","com.mysql.jdbc.jdbc2.optional.MysqlDataSource");
        hikariConfigMap.put("dataSource.url", "jdbc:mysql://rr-wz93pd9mi921e30j0.mysql.rds.aliyuncs.com:3306/power2");
        hikariConfigMap.put("dataSource.user","etl");
        hikariConfigMap.put("dataSource.password","immotor!99");
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxIdle(5);
        config.setMaxTotal(1000 * 100);
        config.setMaxWaitMillis(30);
        config.setTestOnBorrow(true);
        config.setTestOnReturn(true);
        pool = new JedisPool(config, "119.23.133.72", 6574,20000,"immotor!6574@.com");
        connectionProvider = new HikariCPConnectionProvider(hikariConfigMap);
        //对数据库连接池进行初始化
        connectionProvider.prepare();
        jdbcClient = new JdbcClient(connectionProvider, 30);
            }
           @Override
            public void execute(TupleWindow inputWindow) {
                List<BatteryMonitorTime> list=new ArrayList();
               if(new Date().getTime()>times) {
                   synchronized (LogBatterySlidingWindowBolt.class){
                       if(new Date().getTime()>times) {
                           List<List<Column>> select= select = getStationCode();
                           for(int i=0;i<select.size();i++){
                               map.put((String) select.get(i).get(2).getVal(),(Integer) select.get(i).get(0).getVal());
                           }
                           times += 4 * 60 * 60 * 1000;
                       }
                   }
               }
                Jedis jedis=pool.getResource();

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
                           // 每条日志，有一个时间字段，以及多个 Key:Value 对，
                           int log_time = log.getTime();
                           sb.append("LogTime:").append(log_time);
                           BatteryMonitorTime batteryMonitorTime=new BatteryMonitorTime();

                           for (Logs.Log.Content content : log.getContentsList()) {
                              if(content.getKey().equals("pid")){
                                   sb.append("\t").append(content.getKey()).append(":")
                                           .append(content.getValue());
                                   if(map.get(content.getValue())!=null){
                                         batteryMonitorTime.setCode(map.get(content.getValue()));
                                   }
                                  batteryMonitorTime.setPid(content.getValue());
                                   }
                               if(content.getKey().equals("id")){
                                   sb.append("\t").append(content.getKey()).append(":")
                                           .append(content.getValue());
                                   batteryMonitorTime.setId(content.getValue());
                               }
                               if(content.getKey().equals("partreadonlydata")){
                                   sb.append("\t").append(content.getKey()).append(":")
                                           .append(content.getValue());
                                   String partReadOnlyData= null;
                                   try {
                                       partReadOnlyData = URLDecoder.decode(content.getValue(), "UTF-8").replaceAll("\\[", "").replaceAll("]", "");
                                       String[] strs=partReadOnlyData.split(",");
                                       double soh=Integer.parseInt(strs[36]+strs[37], 16)==65535?-100.0:((Integer.parseInt(strs[36]+strs[37], 16))/10.0);//健康状态
                                       batteryMonitorTime.setSoh(soh);
                                       int cycle=Integer.parseInt(strs[38]+strs[39], 16)==65535?-100:Integer.parseInt(strs[38]+strs[39], 16);//循环次数
                                       batteryMonitorTime.setCycle(cycle);

                                       String devft1=Integer.toBinaryString(Integer.parseInt(strs[48]+strs[49], 16));//设备故障字1(36-51)
                                       int lenDevft1=devft1.length();
                                       for(int m=0;m<16-lenDevft1;m++){
                                           devft1=devft1+"0";
                                       }
                                       batteryMonitorTime.setDamage1(devft1);

                                       String devft2=Integer.toBinaryString(Integer.parseInt(strs[50]+strs[51], 16));//设备故障字2(52-67)
                                       int lenDevft2=devft2.length();
                                       for(int m=0;m<16-lenDevft2;m++){
                                           devft2=devft2+"0";
                                       }
                                       batteryMonitorTime.setDamage2(devft2);
                                       String opft1=Integer.toBinaryString(Integer.parseInt(strs[52]+strs[53], 16));//运行故障字1(68-83)
                                       int lenOpft1=opft1.length();
                                       for(int m=0;m<16-lenOpft1;m++){
                                           opft1=opft1+"0";
                                       }
                                       batteryMonitorTime.setDamage3(opft1);
                                       String opft2=Integer.toBinaryString(Integer.parseInt(strs[54]+strs[55], 16));//运行故障字2(84-99)
                                       int lenOpft2=opft2.length();
                                       for(int m=0;m<16-lenOpft2;m++){
                                           opft2=opft2+"0";
                                       }
                                       batteryMonitorTime.setDamage4(opft2);
                                       String opwarn1=Integer.toBinaryString(Integer.parseInt(strs[56]+strs[57], 16));//运行告警字1(100-115)
                                       int lenOpwarn1=opwarn1.length();
                                       for(int m=0;m<16-lenOpwarn1;m++){
                                           opwarn1=opwarn1+"0";
                                       }
                                       batteryMonitorTime.setDamage5(opwarn1);

                                   } catch (UnsupportedEncodingException e) {
                                       e.printStackTrace();
                                   }


                               }
                               if(content.getKey().equals("collecttime")){
                                   sb.append("\t").append(content.getKey()).append(":")
                                           .append(content.getValue());
                                   batteryMonitorTime.setDate( new Date(Long.parseLong(content.getValue())));

                               }
                           }
                           list.add(batteryMonitorTime);
                       }
                   }
                    Map<String,String> mapBatteryDamageInStation6min=jedis.hgetAll(batteryDamageInStation6min);//获取6min之前报的错误
                    String values="";
               //     batteryMonitorTime.getId(),"bsn","pid","sn","time","citycode"
                   for(int i=0;i<list.size();i++){
                       BatteryMonitorTime batteryMonitorTime=  list.get(i);
                       String damageValue=mapBatteryDamageInStation6min.get(batteryMonitorTime.getId());
                       String[] damages=damageValue.split("\\|");
                       if(!damages[0].equals(batteryMonitorTime.getDamage1())){
                        //    for(int m=0;m<damages[0].length();m++){
                                if(!damages[0].substring(0,1).equals(batteryMonitorTime.getDamage1().substring(0,1))){
                                //    LoghubService loghubService=new LoghubService();
                                    //    loghubService.sendMonitorBatteryDetail("type","name");
                                }
                      //      }
                       }
                       if(!damages[1].equals(batteryMonitorTime.getDamage2())){

                       }
                       if(!damages[2].equals(batteryMonitorTime.getDamage3())){

                       }
                       if(!damages[3].equals(batteryMonitorTime.getDamage4())){

                       }
                       if(!damages[4].equals(batteryMonitorTime.getDamage5())){

                       }

                       for(int j=0;j<damages.length;j++){



                       }
                  //     batteryMonitorTime.getDamage();
                   }



               }
               //LoghubService loghubService=new LoghubService();
           //    loghubService.sendMonitorBatteryInfo(list);



            }
            public List<List<Column>>  getStationCode(){
                List<Column> listMysql = new ArrayList();
                listMysql.add(new Column("hide", "0", Types.VARCHAR));
                return jdbcClient.select("select city_code, sn,pid from t_station where hide = ?", listMysql);
            }
            public List<List<Column>>  getBatterySn(){
                 List<Column> listMysql = new ArrayList();
                 listMysql.add(new Column("1", 1, Types.INTEGER));
                 return jdbcClient.select("select lower(bid),sn from t_battery where 1 = ?", listMysql);
             }

            @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
           //  declarer.declare(new Fields("count"));
            }

  }
