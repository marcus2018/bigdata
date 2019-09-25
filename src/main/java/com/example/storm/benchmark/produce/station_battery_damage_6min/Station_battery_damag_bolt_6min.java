package com.example.storm.benchmark.produce.station_battery_damage_6min;

import com.aliyun.openservices.log.common.LogGroupData;
import com.aliyun.openservices.log.common.Logs;
import com.aliyun.openservices.log.exception.LogException;
import com.aliyun.openservices.loghub.stormspout.LogHubSpout;
import com.example.storm.bean.StationLost;
import com.example.storm.benchmark.produce.cabinet.cabinet_battery_temp.LogBatteryInStationBolt;
import com.example.storm.benchmark.produce.cabinet.cabinet_battery_temp.bean.BatteryHB3InStationTemp;
import com.example.storm.benchmark.produce.cabinet.cabinet_battery_temp.bean.CabinetHBTime_type3;
import com.example.storm.benchmark.produce.cabinet.cabinet_battery_temp.bean.StationInfo;
import com.example.storm.benchmark.produce.station_battery_damage_6min.bean.Station_battery_Collection_type3;
import com.example.storm.benchmark.util.LoghubService;
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

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

public class Station_battery_damag_bolt_6min extends BaseWindowedBolt {
    private OutputCollector collector;
    private static Map<String, Integer> counters = new ConcurrentHashMap<String, Integer>();

    private static volatile JedisPool pool;
    private  static     Map<String,StationLost> stringStationLostMap=new HashMap<>();
    private SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private SimpleDateFormat sdf1=new SimpleDateFormat("yyyy-MM-dd");
    private  static String batteryDamageInStation6min="monitor:station_battery_damage6min";
    private ConnectionProvider connectionProvider;
    private JdbcClient jdbcClient;
    private static volatile Map<String,Integer> mapStation=new HashMap<>();
    private static volatile Map<String,StationInfo> mapStationInfo=new HashMap<>();//柜子信息
    private static volatile Map<String,String> mapBatteryInfo=new HashMap<>();//电池信息
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
                    pool = new JedisPool(config, "r-wz9gjghb9zyfa6ciyv.redis.rds.aliyuncs.com", 6379,20000,"Ehdbigdata190418");

//                    pool = new JedisPool(config, "10.27.169.187", 6574,20000,"immotor!6574@.com");
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
     List<BatteryMonitorTime> list=new ArrayList<>();//

        if(new Date().getTime()>times) {
            synchronized (LogBatteryInStationBolt.class){
                if(new Date().getTime()>times) {
                    List<List<Column>> select = getStationCode();
                    for(int i=0;i<select.size();i++){
                       mapStation.put((String) select.get(i).get(0).getVal(),(Integer) select.get(i).get(1).getVal());
                        mapStationInfo.put((String) select.get(i).get(0).getVal(),new StationInfo(
                                (Integer)( select.get(i).get(1).getVal()==null?0:select.get(i).get(1).getVal()), (String) select.get(i).get(2).getVal(),
                                (String) select.get(i).get(3).getVal()
                        ));
                    }
                    List<List<Column>> selectBattery = getBatterySn();
                    for(int i=0;i<selectBattery.size();i++){
                        mapBatteryInfo.put((String) selectBattery.get(i).get(0).getVal(),(String) selectBattery.get(i).get(1).getVal());
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
                            HardBatteryRealTimePartOnlyData hardBatteryRealTimePartOnlyData=new HardBatteryRealTimePartOnlyData();

                            for (Logs.Log.Content content : log.getContentsList()) {
                                if(content.getKey().equals("content")){
                                    if(content.getValue().contains("partReadOnlyData")){
                                        JsonObject obj = new JsonParser().parse(content.getValue()).getAsJsonObject();
                                        Station_battery_Collection_type3 stationBatteryCollectionType3 = new Gson().fromJson(obj, Station_battery_Collection_type3.class);
                                        if(mapStation.get(stationBatteryCollectionType3.getpId())!=null){
                                            batteryMonitorTime.setCode(mapStation.get(stationBatteryCollectionType3.getpId()));
                                        }
                                        batteryMonitorTime.setPid(stationBatteryCollectionType3.getpId());
                                        batteryMonitorTime.setId(stationBatteryCollectionType3.getbId());
                                        batteryMonitorTime.setDate( new Date(stationBatteryCollectionType3.getCollectTime()));
                                        String partReadOnlyData= null;
                                        try {
                                            partReadOnlyData = URLDecoder.decode(stationBatteryCollectionType3.getPartReadOnlyData(), "UTF-8").replaceAll("\\[", "").replaceAll("]", "");
                                            String[] strs=partReadOnlyData.split(",");



                                            if (strs.length!=122)
                                                return;

                                            hardBatteryRealTimePartOnlyData.setUserid(String.valueOf(Integer.parseInt(strs[0], 16))+
                                                    String.valueOf(Integer.parseInt(strs[1], 16))+
                                                    String.valueOf(Integer.parseInt(strs[2], 16))+
                                                    String.valueOf(Integer.parseInt(strs[3], 16))+
                                                    String.valueOf(Integer.parseInt(strs[4], 16))+
                                                    String.valueOf(Integer.parseInt(strs[5], 16))+
                                                    String.valueOf(Integer.parseInt(strs[6], 16))+
                                                    String.valueOf(Integer.parseInt(strs[7], 16)));//userID
                                            String s8=Integer.toBinaryString(Integer.parseInt(strs[8]+strs[9], 16));//BMS状态
                                            int lenS8=s8.length();
                                            for(int m=0;m<16-lenS8;m++){
                                                s8=s8+"0";
                                            }
                                            hardBatteryRealTimePartOnlyData.setState_0(Long.parseLong(s8.substring(0,1)));
                                            hardBatteryRealTimePartOnlyData.setState_1(Long.parseLong(s8.substring(1,2)));
                                            hardBatteryRealTimePartOnlyData.setState_2(Long.parseLong(s8.substring(2,3)));
                                            hardBatteryRealTimePartOnlyData.setState_3(Long.parseLong(s8.substring(3,4)));
                                            hardBatteryRealTimePartOnlyData.setState_4(Long.parseLong(s8.substring(4,5)));
                                            hardBatteryRealTimePartOnlyData.setState_5(Long.parseLong(s8.substring(5,6)));
                                            hardBatteryRealTimePartOnlyData.setState_6(Long.parseLong(s8.substring(6,7)));
                                            hardBatteryRealTimePartOnlyData.setState_7(Long.parseLong(s8.substring(7,8)));
                                            hardBatteryRealTimePartOnlyData.setState_8(Long.parseLong(s8.substring(8,9)));
                                            hardBatteryRealTimePartOnlyData.setState_9(Long.parseLong(s8.substring(9,10)));
                                            hardBatteryRealTimePartOnlyData.setState_10(Long.parseLong(s8.substring(10,11)));
                                            hardBatteryRealTimePartOnlyData.setState_11(Long.parseLong(s8.substring(11,12)));
                                            hardBatteryRealTimePartOnlyData.setState_12(Long.parseLong(s8.substring(12,13)));
                                            hardBatteryRealTimePartOnlyData.setState_13(Long.parseLong(s8.substring(13,14)));
                                            hardBatteryRealTimePartOnlyData.setState_14(Long.parseLong(s8.substring(14,15)));
                                            hardBatteryRealTimePartOnlyData.setState_15(Long.parseLong(s8.substring(15,16)));


                                            hardBatteryRealTimePartOnlyData.setSoc(Integer.parseInt(strs[10]+strs[11], 16)==65535?-100.0:(Integer.parseInt(strs[10]+strs[11], 16)/10.0));//电池组荷电状态

                                            hardBatteryRealTimePartOnlyData.setTvolt(Integer.parseInt(strs[12]+strs[13], 16)==65535?-100.0:(Integer.parseInt(strs[12]+strs[13], 16)/100.0));//总电压

                                            hardBatteryRealTimePartOnlyData.setTcurr(Integer.parseInt(strs[14]+strs[15], 16)==65535?-100.0:((Integer.parseInt(strs[14]+strs[15], 16)-30000)/100.0));//总电流

                                            hardBatteryRealTimePartOnlyData.setHtemp(Integer.parseInt(strs[16]+strs[17], 16)==65535?-100.0:((Integer.parseInt(strs[16]+strs[17], 16)-400)/10.0));//最高电池温度

                                            hardBatteryRealTimePartOnlyData.setHtnum(Integer.parseInt(strs[18]+strs[19], 16)==65535?-100:Integer.parseInt(strs[18]+strs[19], 16));//最高电池温度传感器编号

                                            hardBatteryRealTimePartOnlyData.setLtemp(Integer.parseInt(strs[20]+strs[21], 16)==65535?-100.0:((Integer.parseInt(strs[20]+strs[21], 16)-400)/10.0));//最低电池温度

                                            hardBatteryRealTimePartOnlyData.setLtnum(Integer.parseInt(strs[22]+strs[23], 16)==65535?-100:Integer.parseInt(strs[22]+strs[23], 16));//最低电池温度传感器编号

                                            hardBatteryRealTimePartOnlyData.setHvolt(Integer.parseInt(strs[24]+strs[25], 16)==65535?-100:Integer.parseInt(strs[24]+strs[25], 16));//最高单体电压

                                            hardBatteryRealTimePartOnlyData.setHvnum(Integer.parseInt(strs[26]+strs[27], 16)==65535?-100:Integer.parseInt(strs[26]+strs[27], 16));//最高单体电压电池编号

                                            hardBatteryRealTimePartOnlyData.setLvolt(Integer.parseInt(strs[28]+strs[29], 16)==65535?-100:Integer.parseInt(strs[28]+strs[29], 16));//最低单体电压


                                            hardBatteryRealTimePartOnlyData.setLvnum(Integer.parseInt(strs[30]+strs[31], 16)==65535?-100:Integer.parseInt(strs[30]+strs[31], 16));//最低单体电压电池编号


                                            hardBatteryRealTimePartOnlyData.setDsop(Integer.parseInt(strs[32]+strs[33], 16)==65535?-100.0:((Integer.parseInt(strs[32]+strs[33], 16))/100.0));//10s最大允许放电电流


                                            hardBatteryRealTimePartOnlyData.setCsop(Integer.parseInt(strs[34]+strs[35], 16)==65535?-100.0:((Integer.parseInt(strs[34]+strs[35], 16))/100.0));//10s最大允许充电电流

                                            hardBatteryRealTimePartOnlyData.setSoh(Integer.parseInt(strs[36]+strs[37], 16)==65535?-100.0:((Integer.parseInt(strs[36]+strs[37], 16))/10.0));//健康状态
                                            //    one.set(31,Integer.parseInt(strs[38]+strs[39], 16));//循环次数
                                            hardBatteryRealTimePartOnlyData.setCycle(Integer.parseInt(strs[38]+strs[39], 16)==65535?-100:Integer.parseInt(strs[38]+strs[39], 16));//循环次数

                                            hardBatteryRealTimePartOnlyData.setRcap(Integer.parseInt(strs[40]+strs[41], 16)==65535?-100:Integer.parseInt(strs[40]+strs[41], 16));//剩余容量

                                            hardBatteryRealTimePartOnlyData.setFcap(Integer.parseInt(strs[42]+strs[43], 16)==65535?-100:Integer.parseInt(strs[42]+strs[43], 16));//充满容量

                                            hardBatteryRealTimePartOnlyData.setFctime(Integer.parseInt(strs[44]+strs[45], 16)==65535?-100:Integer.parseInt(strs[44]+strs[45], 16));//充满时间

                                            hardBatteryRealTimePartOnlyData.setRpow(Integer.parseInt(strs[46]+strs[47], 16)==65535?-100.0:((Integer.parseInt(strs[46]+strs[47], 16))/10.0));//剩余能量


                                            hardBatteryRealTimePartOnlyData.setOpwarn2(Integer.parseInt(strs[58]+strs[59], 16));//运行告警字2
                                            hardBatteryRealTimePartOnlyData.setCmost(Integer.parseInt(strs[60]+strs[61], 16)==65535?-100.0:((Integer.parseInt(strs[60]+strs[61], 16)-400)/10.0));
                                            hardBatteryRealTimePartOnlyData.setDmost(Integer.parseInt(strs[62]+strs[63], 16)==65535?-100.0:((Integer.parseInt(strs[62]+strs[63], 16)-400)/10.0));
                                            hardBatteryRealTimePartOnlyData.setFuelt(Integer.parseInt(strs[64]+strs[65], 16)==65535?-100.0:((Integer.parseInt(strs[64]+strs[65], 16)-400)/10.0));
                                            hardBatteryRealTimePartOnlyData.setCont(Integer.parseInt( strs[66]+strs[67], 16)==65535?-100.0:((Integer.parseInt(strs[66]+strs[67], 16)-400)/10.0));
                                            hardBatteryRealTimePartOnlyData.setBtemp1(Integer.parseInt(strs[68]+strs[69], 16)==65535?-100.0:((Integer.parseInt(strs[68]+strs[69], 16)-400)/10.0));
                                            hardBatteryRealTimePartOnlyData.setBtemp2(Integer.parseInt(strs[70]+strs[71], 16)==65535?-100.0:((Integer.parseInt(strs[70]+strs[71], 16)-400)/10.0));
                                            hardBatteryRealTimePartOnlyData.setBvolt1(Integer.parseInt(strs[72]+strs[73], 16)==65535?-100:Integer.parseInt(strs[72]+strs[73], 16));
                                            hardBatteryRealTimePartOnlyData.setBvolt2(Integer.parseInt(strs[74]+strs[75], 16)==65535?-100:Integer.parseInt(strs[74]+strs[75], 16));
                                            hardBatteryRealTimePartOnlyData.setBvolt3(Integer.parseInt(strs[76]+strs[77], 16)==65535?-100:Integer.parseInt(strs[76]+strs[77], 16));
                                            hardBatteryRealTimePartOnlyData.setBvolt4(Integer.parseInt(strs[78]+strs[79], 16)==65535?-100:Integer.parseInt(strs[78]+strs[79], 16));
                                            hardBatteryRealTimePartOnlyData.setBvolt5(Integer.parseInt(strs[80]+strs[81], 16)==65535?-100:Integer.parseInt(strs[80]+strs[81], 16));
                                            hardBatteryRealTimePartOnlyData.setBvolt6(Integer.parseInt(strs[82]+strs[83], 16)==65535?-100:Integer.parseInt(strs[82]+strs[83], 16));
                                            hardBatteryRealTimePartOnlyData.setBvolt7(Integer.parseInt(strs[84]+strs[85], 16)==65535?-100:Integer.parseInt(strs[84]+strs[85], 16));
                                            hardBatteryRealTimePartOnlyData.setBvolt8(Integer.parseInt(strs[86]+strs[87], 16)==65535?-100:Integer.parseInt(strs[86]+strs[87], 16));
                                            hardBatteryRealTimePartOnlyData.setBvolt9(Integer.parseInt(strs[88]+strs[89], 16)==65535?-100:Integer.parseInt(strs[88]+strs[89], 16));
                                            hardBatteryRealTimePartOnlyData.setBvolt10(Integer.parseInt(strs[90]+strs[91], 16)==65535?-100:Integer.parseInt(strs[90]+strs[91], 16));
                                            hardBatteryRealTimePartOnlyData.setBvolt11(Integer.parseInt(strs[92]+strs[93], 16)==65535?-100:Integer.parseInt(strs[92]+strs[93], 16));
                                            hardBatteryRealTimePartOnlyData.setBvolt12(Integer.parseInt(strs[94]+strs[95], 16)==65535?-100:Integer.parseInt(strs[94]+strs[95], 16));
                                            hardBatteryRealTimePartOnlyData.setBvolt13(Integer.parseInt(strs[96]+strs[97], 16)==65535?-100:Integer.parseInt(strs[96]+strs[97], 16));
                                            hardBatteryRealTimePartOnlyData.setBvolt14(Integer.parseInt(strs[98]+strs[99], 16)==65535?-100:Integer.parseInt(strs[98]+strs[99], 16));
                                            hardBatteryRealTimePartOnlyData.setBvolt15(Integer.parseInt(strs[100]+strs[101], 16)==65535?-100:Integer.parseInt(strs[100]+strs[101], 16));
                                            hardBatteryRealTimePartOnlyData.setBvolt16(Integer.parseInt(strs[102]+strs[103], 16)==65535?-100:Integer.parseInt(strs[102]+strs[103], 16));

                                            String s106=Integer.toBinaryString(Integer.parseInt(strs[104]+strs[105], 16));//电池均衡详细状态 139-154
                                            int lenS106=s106.length();
                                            for(int m=0;m<16-lenS106;m++){
                                                s106=s106+"0";
                                            }
                                            hardBatteryRealTimePartOnlyData.setBalasta_0(Long.parseLong(s106.substring(0,1)));
                                            hardBatteryRealTimePartOnlyData.setBalasta_1(Long.parseLong(s106.substring(1,2)));
                                            hardBatteryRealTimePartOnlyData.setBalasta_2(Long.parseLong(s106.substring(2,3)));
                                            hardBatteryRealTimePartOnlyData.setBalasta_3(Long.parseLong(s106.substring(3,4)));
                                            hardBatteryRealTimePartOnlyData.setBalasta_4(Long.parseLong(s106.substring(4,5)));
                                            hardBatteryRealTimePartOnlyData.setBalasta_5(Long.parseLong(s106.substring(5,6)));
                                            hardBatteryRealTimePartOnlyData.setBalasta_6(Long.parseLong(s106.substring(6,7)));
                                            hardBatteryRealTimePartOnlyData.setBalasta_7(Long.parseLong(s106.substring(7,8)));
                                            hardBatteryRealTimePartOnlyData.setBalasta_8(Long.parseLong(s106.substring(8,9)));
                                            hardBatteryRealTimePartOnlyData.setBalasta_9(Long.parseLong(s106.substring(9,10)));
                                            hardBatteryRealTimePartOnlyData.setBalasta_10(Long.parseLong(s106.substring(10,11)));
                                            hardBatteryRealTimePartOnlyData.setBalasta_11(Long.parseLong(s106.substring(11,12)));
                                            hardBatteryRealTimePartOnlyData.setBalasta_12(Long.parseLong(s106.substring(12,13)));
                                            hardBatteryRealTimePartOnlyData.setBalasta_13(Long.parseLong(s106.substring(13,14)));
                                            hardBatteryRealTimePartOnlyData.setBalasta_14(Long.parseLong(s106.substring(14,15)));
                                            hardBatteryRealTimePartOnlyData.setBalasta_15(Long.parseLong(s106.substring(15,16)));
                                            hardBatteryRealTimePartOnlyData.setAccelerated_speed_x(Integer.parseInt(strs[106]+strs[107], 16));
                                            hardBatteryRealTimePartOnlyData.setAccelerated_speed_y(Integer.parseInt(strs[108]+strs[109], 16));
                                            hardBatteryRealTimePartOnlyData.setAccelerated_speed_z(Integer.parseInt(strs[110]+strs[111], 16));
                                            hardBatteryRealTimePartOnlyData.setMcu3v3(Integer.parseInt(strs[112]+strs[113], 16));
                                            hardBatteryRealTimePartOnlyData.setPre_electric_infer_vol(Integer.parseInt(strs[114]+strs[115], 16));
                                            hardBatteryRealTimePartOnlyData.setElectric_quantity_ma(Integer.parseInt(strs[116]+strs[117], 16));
                                            hardBatteryRealTimePartOnlyData.setMax_charge_electric((Integer.parseInt(strs[118]+strs[119], 16))/100.0);//最大充电电压
                                            hardBatteryRealTimePartOnlyData.setElectric_meter_charge(Integer.parseInt(strs[120]+strs[121], 16));;//电量计电压


                                            double soh=Integer.parseInt(strs[36]+strs[37], 16)==65535?-100.0:((Integer.parseInt(strs[36]+strs[37], 16))/10.0);//健康状态
                                            batteryMonitorTime.setSoh(soh);
                                            int cycle=Integer.parseInt(strs[38]+strs[39], 16)==65535?-100:Integer.parseInt(strs[38]+strs[39], 16);//循环次数
                                            batteryMonitorTime.setCycle(cycle);

                                            String devft1=Integer.toBinaryString(Integer.parseInt(strs[48]+strs[49], 16));//设备故障字1(36-51)
                                            int lenDevft1=devft1.length();
                                            for(int m=0;m<16-lenDevft1;m++){
                                                devft1=devft1+"0";
                                            }
                                            hardBatteryRealTimePartOnlyData.setDevft1_0(Long.parseLong(devft1.substring(0,1)));
                                            hardBatteryRealTimePartOnlyData.setDevft1_1(Long.parseLong(devft1.substring(1,2)));
                                            hardBatteryRealTimePartOnlyData.setDevft1_2(Long.parseLong(devft1.substring(2,3)));
                                            hardBatteryRealTimePartOnlyData.setDevft1_3(Long.parseLong(devft1.substring(3,4)));
                                            hardBatteryRealTimePartOnlyData.setDevft1_4(Long.parseLong(devft1.substring(4,5)));
                                            hardBatteryRealTimePartOnlyData.setDevft1_5(Long.parseLong(devft1.substring(5,6)));
                                            hardBatteryRealTimePartOnlyData.setDevft1_6(Long.parseLong(devft1.substring(6,7)));
                                            hardBatteryRealTimePartOnlyData.setDevft1_7(Long.parseLong(devft1.substring(7,8)));
                                            hardBatteryRealTimePartOnlyData.setDevft1_8(Long.parseLong(devft1.substring(8,9)));
                                            hardBatteryRealTimePartOnlyData.setDevft1_9(Long.parseLong(devft1.substring(9,10)));
                                            hardBatteryRealTimePartOnlyData.setDevft1_10(Long.parseLong(devft1.substring(10,11)));
                                            hardBatteryRealTimePartOnlyData.setDevft1_11(Long.parseLong(devft1.substring(11,12)));
                                            hardBatteryRealTimePartOnlyData.setDevft1_12(Long.parseLong(devft1.substring(12,13)));
                                            hardBatteryRealTimePartOnlyData.setDevft1_13(Long.parseLong(devft1.substring(13,14)));
                                            hardBatteryRealTimePartOnlyData.setDevft1_14(Long.parseLong(devft1.substring(14,15)));
                                            hardBatteryRealTimePartOnlyData.setDevft1_15(Long.parseLong(devft1.substring(15,16)));
                                            batteryMonitorTime.setDamage1(devft1);

                                            String devft2=Integer.toBinaryString(Integer.parseInt(strs[50]+strs[51], 16));//设备故障字2(52-67)
                                            int lenDevft2=devft2.length();
                                            for(int m=0;m<16-lenDevft2;m++){
                                                devft2=devft2+"0";
                                            }
                                            hardBatteryRealTimePartOnlyData.setDevft2_0(Long.parseLong(devft2.substring(0,1)));
                                            hardBatteryRealTimePartOnlyData.setDevft2_1(Long.parseLong(devft2.substring(1,2)));
                                            hardBatteryRealTimePartOnlyData.setDevft2_2(Long.parseLong(devft2.substring(2,3)));
                                            hardBatteryRealTimePartOnlyData.setDevft2_3(Long.parseLong(devft2.substring(3,4)));
                                            hardBatteryRealTimePartOnlyData.setDevft2_4(Long.parseLong(devft2.substring(4,5)));
                                            hardBatteryRealTimePartOnlyData.setDevft2_5(Long.parseLong(devft2.substring(5,6)));
                                            hardBatteryRealTimePartOnlyData.setDevft2_6(Long.parseLong(devft2.substring(6,7)));
                                            hardBatteryRealTimePartOnlyData.setDevft2_7(Long.parseLong(devft2.substring(7,8)));
                                            hardBatteryRealTimePartOnlyData.setDevft2_8(Long.parseLong(devft2.substring(8,9)));
                                            hardBatteryRealTimePartOnlyData.setDevft2_9(Long.parseLong(devft2.substring(9,10)));
                                            hardBatteryRealTimePartOnlyData.setDevft2_10(Long.parseLong(devft2.substring(10,11)));
                                            hardBatteryRealTimePartOnlyData.setDevft2_11(Long.parseLong(devft2.substring(11,12)));
                                            hardBatteryRealTimePartOnlyData.setDevft2_12(Long.parseLong(devft2.substring(12,13)));
                                            hardBatteryRealTimePartOnlyData.setDevft2_13(Long.parseLong(devft2.substring(13,14)));
                                            hardBatteryRealTimePartOnlyData.setDevft2_14(Long.parseLong(devft2.substring(14,15)));
                                            hardBatteryRealTimePartOnlyData.setDevft2_15(Long.parseLong(devft2.substring(15,16)));
                                            batteryMonitorTime.setDamage2(devft2);
                                            String opft1=Integer.toBinaryString(Integer.parseInt(strs[52]+strs[53], 16));//运行故障字1(68-83)
                                            int lenOpft1=opft1.length();
                                            for(int m=0;m<16-lenOpft1;m++){
                                                opft1=opft1+"0";
                                            }
                                            hardBatteryRealTimePartOnlyData.setOpft1_0(Long.parseLong(opft1.substring(0,1)));
                                            hardBatteryRealTimePartOnlyData.setOpft1_1(Long.parseLong(opft1.substring(1,2)));
                                            hardBatteryRealTimePartOnlyData.setOpft1_2(Long.parseLong(opft1.substring(2,3)));
                                            hardBatteryRealTimePartOnlyData.setOpft1_3(Long.parseLong(opft1.substring(3,4)));
                                            hardBatteryRealTimePartOnlyData.setOpft1_4(Long.parseLong(opft1.substring(4,5)));
                                            hardBatteryRealTimePartOnlyData.setOpft1_5(Long.parseLong(opft1.substring(5,6)));
                                            hardBatteryRealTimePartOnlyData.setOpft1_6(Long.parseLong(opft1.substring(6,7)));
                                            hardBatteryRealTimePartOnlyData.setOpft1_7(Long.parseLong(opft1.substring(7,8)));
                                            hardBatteryRealTimePartOnlyData.setOpft1_8(Long.parseLong(opft1.substring(8,9)));
                                            hardBatteryRealTimePartOnlyData.setOpft1_9(Long.parseLong(opft1.substring(9,10)));
                                            hardBatteryRealTimePartOnlyData.setOpft1_10(Long.parseLong(opft1.substring(10,11)));
                                            hardBatteryRealTimePartOnlyData.setOpft1_11(Long.parseLong(opft1.substring(11,12)));
                                            hardBatteryRealTimePartOnlyData.setOpft1_12(Long.parseLong(opft1.substring(12,13)));
                                            hardBatteryRealTimePartOnlyData.setOpft1_13(Long.parseLong(opft1.substring(13,14)));
                                            hardBatteryRealTimePartOnlyData.setOpft1_14(Long.parseLong(opft1.substring(14,15)));
                                            hardBatteryRealTimePartOnlyData.setOpft1_15(Long.parseLong(opft1.substring(15,16)));
                                            batteryMonitorTime.setDamage3(opft1);
                                            String opft2=Integer.toBinaryString(Integer.parseInt(strs[54]+strs[55], 16));//运行故障字2(84-99)
                                            int lenOpft2=opft2.length();
                                            for(int m=0;m<16-lenOpft2;m++){
                                                opft2=opft2+"0";
                                            }
                                            hardBatteryRealTimePartOnlyData.setOpft2_0(Long.parseLong(opft2.substring(0,1)));
                                            hardBatteryRealTimePartOnlyData.setOpft2_1(Long.parseLong(opft2.substring(1,2)));
                                            hardBatteryRealTimePartOnlyData.setOpft2_2(Long.parseLong(opft2.substring(2,3)));
                                            hardBatteryRealTimePartOnlyData.setOpft2_3(Long.parseLong(opft2.substring(3,4)));
                                            hardBatteryRealTimePartOnlyData.setOpft2_4(Long.parseLong(opft2.substring(4,5)));
                                            hardBatteryRealTimePartOnlyData.setOpft2_5(Long.parseLong(opft2.substring(5,6)));
                                            hardBatteryRealTimePartOnlyData.setOpft2_6(Long.parseLong(opft2.substring(6,7)));
                                            hardBatteryRealTimePartOnlyData.setOpft2_7(Long.parseLong(opft2.substring(7,8)));
                                            hardBatteryRealTimePartOnlyData.setOpft2_8(Long.parseLong(opft2.substring(8,9)));
                                            hardBatteryRealTimePartOnlyData.setOpft2_9(Long.parseLong(opft2.substring(9,10)));
                                            hardBatteryRealTimePartOnlyData.setOpft2_10(Long.parseLong(opft2.substring(10,11)));
                                            hardBatteryRealTimePartOnlyData.setOpft2_11(Long.parseLong(opft2.substring(11,12)));
                                            hardBatteryRealTimePartOnlyData.setOpft2_12(Long.parseLong(opft2.substring(12,13)));
                                            hardBatteryRealTimePartOnlyData.setOpft2_13(Long.parseLong(opft2.substring(13,14)));
                                            hardBatteryRealTimePartOnlyData.setOpft2_14(Long.parseLong(opft2.substring(14,15)));
                                            hardBatteryRealTimePartOnlyData.setOpft2_15(Long.parseLong(opft2.substring(15,16)));
                                            batteryMonitorTime.setDamage4(opft2);
                                            String opwarn1=Integer.toBinaryString(Integer.parseInt(strs[56]+strs[57], 16));//运行告警字1(100-115)
                                            int lenOpwarn1=opwarn1.length();
                                            for(int m=0;m<16-lenOpwarn1;m++){
                                                opwarn1=opwarn1+"0";
                                            }
                                            hardBatteryRealTimePartOnlyData.setOpwarn_0(Long.parseLong(opwarn1.substring(0,1)));
                                            hardBatteryRealTimePartOnlyData.setOpwarn_1(Long.parseLong(opwarn1 .substring(1,2)));
                                            hardBatteryRealTimePartOnlyData.setOpwarn_2(Long.parseLong(opwarn1.substring(2,3)));
                                            hardBatteryRealTimePartOnlyData.setOpwarn_3(Long.parseLong(opwarn1.substring(3,4)));
                                            hardBatteryRealTimePartOnlyData.setOpwarn_4(Long.parseLong(opwarn1.substring(4,5)));
                                            hardBatteryRealTimePartOnlyData.setOpwarn_5(Long.parseLong(opwarn1.substring(5,6)));
                                            hardBatteryRealTimePartOnlyData.setOpwarn_6(Long.parseLong(opwarn1.substring(6,7)));
                                            hardBatteryRealTimePartOnlyData.setOpwarn_7(Long.parseLong(opwarn1.substring(7,8)));
                                            hardBatteryRealTimePartOnlyData.setOpwarn_8(Long.parseLong(opwarn1.substring(8,9)));
                                            hardBatteryRealTimePartOnlyData.setOpwarn_9(Long.parseLong(opwarn1.substring(9,10)));
                                            hardBatteryRealTimePartOnlyData.setOpwarn_10(Long.parseLong(opwarn1.substring(10,11)));
                                            hardBatteryRealTimePartOnlyData.setOpwarn_11(Long.parseLong(opwarn1.substring(11,12)));
                                            hardBatteryRealTimePartOnlyData.setOpwarn_12(Long.parseLong(opwarn1.substring(12,13)));
                                            hardBatteryRealTimePartOnlyData.setOpwarn_13(Long.parseLong(opwarn1.substring(13,14)));
                                            hardBatteryRealTimePartOnlyData.setOpwarn_14(Long.parseLong(opwarn1.substring(14,15)));
                                            hardBatteryRealTimePartOnlyData.setOpwarn_15(Long.parseLong(opwarn1.substring(15,16)));
                                            batteryMonitorTime.setDamage5(opwarn1);
                                        } catch (UnsupportedEncodingException e) {
                                            e.printStackTrace();
                                        }
                                        batteryMonitorTime.setHardBatteryRealTimePartOnlyData(hardBatteryRealTimePartOnlyData);
                                        list.add(batteryMonitorTime);
                                    }
                                }
                            }

                        }
                    }
                }



        Map<String,String> mapBatteryDamageInStation6min=jedis.hgetAll(batteryDamageInStation6min);//获取6min之前报的错误
        List<BatteryMonitorTimeToLoghub> listSendLoghub=new ArrayList<BatteryMonitorTimeToLoghub>();
        Map<String,String> insertMapBatteryDamage6min=new HashMap<>();
        for(int i=0;i<list.size();i++){
            BatteryMonitorTime batteryMonitorTime=  list.get(i);
            String damageValueRedis="";
            boolean flag=false;
            if(mapBatteryDamageInStation6min.get(batteryMonitorTime.getId())==null){
                insertMapBatteryDamage6min.put(batteryMonitorTime.getId(),batteryMonitorTime.getDamage1()+"|"+batteryMonitorTime.getDamage2()
                   +"|"+batteryMonitorTime.getDamage3()+"|"+batteryMonitorTime.getDamage4()+"|"+batteryMonitorTime.getDamage5());
                flag=true;
            }else {
                damageValueRedis = mapBatteryDamageInStation6min.get(batteryMonitorTime.getId());
            }
            String[] damagesRedis=damageValueRedis.split("\\|");
            if(flag||!damagesRedis[0].equals(batteryMonitorTime.getDamage1())){
                //    for(int m=0;m<damages[0].length();m++){
                for(int j=0;j<batteryMonitorTime.getDamage1().length();j++){
                    if(j==11||j==12||j==13||j==14)
                        continue;
                    if((flag||!damagesRedis[0].substring(j,j+1).equals(batteryMonitorTime.getDamage1().substring(j,j+1)))&&batteryMonitorTime.getDamage1().substring(j,j+1).equals("1")){
                        BatteryMonitorTimeToLoghub batteryMonitorTimeToLoghub=new BatteryMonitorTimeToLoghub();
                        batteryMonitorTimeToLoghub.setBid(batteryMonitorTime.getId());
                        batteryMonitorTimeToLoghub.setbSn(mapBatteryInfo.get(batteryMonitorTime.getId()));
                        batteryMonitorTimeToLoghub.setStationInfo(mapStationInfo.get(batteryMonitorTime.getPid()));
                        batteryMonitorTimeToLoghub.setDamagetype(1);
                        batteryMonitorTimeToLoghub.setBitType(j+1);
                        batteryMonitorTimeToLoghub.setDate(batteryMonitorTime.getDate());
                        batteryMonitorTimeToLoghub.setPid(batteryMonitorTime.getPid());
                      //  batteryMonitorTimeToLoghub.setContent(batteryMonitorTime.getHardBatteryRealTimePartOnlyData().toString());
                        listSendLoghub.add(batteryMonitorTimeToLoghub);
                        }
                }
            }
            if(flag||!damagesRedis[1].equals(batteryMonitorTime.getDamage2())){
                for(int j=0;j<batteryMonitorTime.getDamage1().length();j++){
                    if(j>=6)
                        continue;
                    if((flag||!damagesRedis[1].substring(j,j+1).equals(batteryMonitorTime.getDamage1().substring(j,j+1)))&&batteryMonitorTime.getDamage1().substring(j,j+1).equals("1")){
                        BatteryMonitorTimeToLoghub batteryMonitorTimeToLoghub=new BatteryMonitorTimeToLoghub();
                        batteryMonitorTimeToLoghub.setBid(batteryMonitorTime.getId());
                        batteryMonitorTimeToLoghub.setbSn(mapBatteryInfo.get(batteryMonitorTime.getId()));
                        batteryMonitorTimeToLoghub.setStationInfo(mapStationInfo.get(batteryMonitorTime.getPid()));
                        batteryMonitorTimeToLoghub.setDamagetype(2);
                        batteryMonitorTimeToLoghub.setBitType(j+1);
                        batteryMonitorTimeToLoghub.setDate(batteryMonitorTime.getDate());
                        batteryMonitorTimeToLoghub.setPid(batteryMonitorTime.getPid());
                    //    batteryMonitorTimeToLoghub.setContent(batteryMonitorTime.getHardBatteryRealTimePartOnlyData().toString());
                        listSendLoghub.add(batteryMonitorTimeToLoghub);
                    }
                }

            }
            if(flag||!damagesRedis[2].equals(batteryMonitorTime.getDamage3())){
                for(int j=0;j<batteryMonitorTime.getDamage1().length();j++){
                    if(j==13)
                        continue;
                    if((flag||!damagesRedis[2].substring(j,j+1).equals(batteryMonitorTime.getDamage1().substring(j,j+1)))&&batteryMonitorTime.getDamage1().substring(j,j+1).equals("1")){
                        BatteryMonitorTimeToLoghub batteryMonitorTimeToLoghub=new BatteryMonitorTimeToLoghub();
                        batteryMonitorTimeToLoghub.setBid(batteryMonitorTime.getId());
                        batteryMonitorTimeToLoghub.setbSn(mapBatteryInfo.get(batteryMonitorTime.getId()));
                        batteryMonitorTimeToLoghub.setStationInfo(mapStationInfo.get(batteryMonitorTime.getPid()));
                        batteryMonitorTimeToLoghub.setDamagetype(3);
                        batteryMonitorTimeToLoghub.setBitType(j+1);
                        batteryMonitorTimeToLoghub.setDate(batteryMonitorTime.getDate());
                        batteryMonitorTimeToLoghub.setPid(batteryMonitorTime.getPid());
                     //   batteryMonitorTimeToLoghub.setContent(batteryMonitorTime.getHardBatteryRealTimePartOnlyData().toString());
                        listSendLoghub.add(batteryMonitorTimeToLoghub);
                    }
                }
            }
            if(flag||!damagesRedis[3].equals(batteryMonitorTime.getDamage4())){
                for(int j=0;j<batteryMonitorTime.getDamage1().length();j++){
                    if(j>=6)
                        continue;
                    if((flag||!damagesRedis[3].substring(j,j+1).equals(batteryMonitorTime.getDamage1().substring(j,j+1)))&&batteryMonitorTime.getDamage1().substring(j,j+1).equals("1")){
                        BatteryMonitorTimeToLoghub batteryMonitorTimeToLoghub=new BatteryMonitorTimeToLoghub();
                        batteryMonitorTimeToLoghub.setBid(batteryMonitorTime.getId());
                        batteryMonitorTimeToLoghub.setbSn(mapBatteryInfo.get(batteryMonitorTime.getId()));
                        batteryMonitorTimeToLoghub.setStationInfo(mapStationInfo.get(batteryMonitorTime.getPid()));
                        batteryMonitorTimeToLoghub.setDamagetype(4);
                        batteryMonitorTimeToLoghub.setBitType(j+1);
                        batteryMonitorTimeToLoghub.setDate(batteryMonitorTime.getDate());
                        batteryMonitorTimeToLoghub.setPid(batteryMonitorTime.getPid());
                      //  batteryMonitorTimeToLoghub.setContent(batteryMonitorTime.getHardBatteryRealTimePartOnlyData().toString());
                        listSendLoghub.add(batteryMonitorTimeToLoghub);
                    }
                }
            }
            if(flag||!damagesRedis[4].equals(batteryMonitorTime.getDamage5())){
                for(int j=0;j<batteryMonitorTime.getDamage1().length();j++){
                    if(j<=7)
                        continue;
                    if((flag||!damagesRedis[4].substring(j,j+1).equals(batteryMonitorTime.getDamage1().substring(j,j+1)))&&batteryMonitorTime.getDamage1().substring(j,j+1).equals("1")){
                        BatteryMonitorTimeToLoghub batteryMonitorTimeToLoghub=new BatteryMonitorTimeToLoghub();
                        batteryMonitorTimeToLoghub.setBid(batteryMonitorTime.getId());
                        batteryMonitorTimeToLoghub.setbSn(mapBatteryInfo.get(batteryMonitorTime.getId()));
                        batteryMonitorTimeToLoghub.setStationInfo(mapStationInfo.get(batteryMonitorTime.getPid()));
                        batteryMonitorTimeToLoghub.setDamagetype(5);
                        batteryMonitorTimeToLoghub.setBitType(j+1);
                        batteryMonitorTimeToLoghub.setDate(batteryMonitorTime.getDate());
                        batteryMonitorTimeToLoghub.setPid(batteryMonitorTime.getPid());
                      //  batteryMonitorTimeToLoghub.setContent(batteryMonitorTime.getHardBatteryRealTimePartOnlyData().toString());
                        listSendLoghub.add(batteryMonitorTimeToLoghub);
                    }
                }
            }
        }
      if(listSendLoghub.size()>0) {
          LoghubService loghubService = new LoghubService();
          loghubService.sendMonitorBatteryDetail(listSendLoghub);
      }
      if(insertMapBatteryDamage6min.size()>0){
            jedis.hmset(batteryDamageInStation6min,insertMapBatteryDamage6min);
      }
        //可用换电减去失联的
        if(jedis!=null){
            jedis.close();
        }







    }
    public List<List<Column>>  getStationCode(){
        //查询该word是否存在
        List<Column> listMysql = new ArrayList();
        //创建一列将值传入   列名  值    值的类型
        listMysql.add(new Column("status", "1", Types.VARCHAR));

        return jdbcClient.select("select pid,city_code, sn,name from t_station where status = ?", listMysql);



    }
    public static boolean isNumeric(String str){
        Pattern pattern = Pattern.compile("[0-9]+");
        return pattern.matcher(str).matches();
    }
    public List<List<Column>>  getBatterySn(){
        //查询该word是否存在
        List<Column> listMysql = new ArrayList();
        //创建一列将值传入   列名  值    值的类型
        listMysql.add(new Column("1", 1, Types.INTEGER));

        return jdbcClient.select("select lower(bid),sn from t_battery where 1 = ?", listMysql);



    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        // declarer.declare(new Fields("count"));
    }



}

