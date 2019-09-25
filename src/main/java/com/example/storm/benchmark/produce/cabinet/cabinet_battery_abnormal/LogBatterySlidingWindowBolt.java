package com.example.storm.benchmark.produce.cabinet.cabinet_battery_abnormal;

import com.aliyun.openservices.log.common.LogGroupData;
import com.aliyun.openservices.log.common.Logs;
import com.aliyun.openservices.log.exception.LogException;
import com.aliyun.openservices.loghub.stormspout.LogHubSpout;
import com.example.storm.benchmark.produce.cabinet.cabinet_battery_abnormal.bean.BatteryMonitorTime;
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

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.*;

public class LogBatterySlidingWindowBolt extends BaseWindowedBolt {
           private OutputCollector collector;
           private SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
           private ConnectionProvider connectionProvider;
           private JdbcClient jdbcClient;
           private volatile Map<String,Integer> map=new HashMap<>();
           private  static volatile Long times=new Date().getTime();


    @Override
            public void prepare(Map stormConf, TopologyContext context,
               OutputCollector collector) {
               this.collector = collector;
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
                                       String s54=Integer.toBinaryString(Integer.parseInt(strs[48]+strs[49], 16));//设备故障字1(36-51)
                                       int len54=s54.length();
                                       for(int m=0;m<8-len54;m++){
                                               s54=s54+"0";
                                       }
                                       s54.substring(4,5);
                                       batteryMonitorTime.setDisChargingMOS(Integer.parseInt(  s54.substring(4,5)));//放电MOS失效
                                       s54.substring(5,6);
                                       batteryMonitorTime.setChargingMOS(Integer.parseInt( s54.substring(5,6)));//充电MOS失效
                                       String s58=Integer.toBinaryString(Integer.parseInt(strs[52]+strs[53], 16));//运行故障字1(68-83)
                                       int len58=s58.length();
                                       for(int m=0;m<8-len58;m++){
                                           s58=s58+"0";
                                       }
                                       s58.substring(0,1);
                                       s58.substring(2,3);
                                       batteryMonitorTime.setOverVoltage( Integer.parseInt( s58.substring(0,1)));//过压
                                       batteryMonitorTime.setOverfall(Integer.parseInt(s58.substring(2,3)));//二级过放

                                       String s106=Integer.toBinaryString(Integer.parseInt(strs[104]+strs[105], 16));//电池均衡详细状态 139-154

                                       String s62=Integer.toBinaryString(Integer.parseInt(strs[56]+strs[57], 16));//运行告警字1(100-115)
                                       int len62=s62.length();
                                       for(int m=0;m<8-len62;m++){
                                           s62=s62+"0";
                                       }
                                       s62.substring(0,1);
                                       s62.substring(1,2);//过放告警
                                       s62.substring(4,5);
                                       s62.substring(6,7);
                                       batteryMonitorTime.setOverChargingWarning(Integer.parseInt(   s62.substring(0,1)));//过充告警
                                       batteryMonitorTime.setOverDisChargingWarning(Integer.parseInt( s62.substring(1,2)));//过放告警
                                       batteryMonitorTime.setHighTempWarning(Integer.parseInt(s62.substring(4,5)));//充电高温告警
                                       batteryMonitorTime.setLowTempWarning(Integer.parseInt(s62.substring(6,7)));//充电低温告警
                                       if(Integer.parseInt( s58.substring(0,1))==0&&Integer.parseInt(s58.substring(2,3))==0
                                               &&Integer.parseInt(   s62.substring(0,1))==0&&Integer.parseInt( s62.substring(1,2))==0
                                               &&Integer.parseInt(s62.substring(4,5))==0&&Integer.parseInt(s62.substring(6,7))==0
                                               ){
                                           break;
                                       }
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



               }
               LoghubService loghubService=new LoghubService();
               loghubService.sendMonitorBatteryInfo(list);



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
           //  declarer.declare(new Fields("count"));
            }

  }
