package com.example.storm.benchmark.produce.cabinet.cabinet_battery_temp;

import com.aliyun.openservices.log.common.LogGroupData;
import com.aliyun.openservices.log.common.Logs;
import com.aliyun.openservices.log.exception.LogException;
import com.aliyun.openservices.loghub.stormspout.LogHubSpout;
import com.example.storm.bean.StationLost;

import com.example.storm.benchmark.produce.cabinet.cabinet_battery_temp.bean.*;
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
import java.util.regex.Pattern;

public class Station_battery_inTemp_and_damageHappenBolt extends BaseWindowedBolt {
    private OutputCollector collector;
    private static Map<String, Integer> counters = new ConcurrentHashMap<String, Integer>();

    private static volatile JedisPool pool;
    private  static     Map<String,StationLost> stringStationLostMap=new HashMap<>();
    private SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private SimpleDateFormat sdf1=new SimpleDateFormat("yyyy-MM-dd");
    private static String batteryInstation="monitor:batteryInstation:";
    private static String batteryNumbersInStation="monitor:batteryNumbersInStation";
    private static String lostStationKey="monitor:station_pid_lost";
    private  static String batteryDamageInStation="monitor:station_battery_damage";//统计柜子的电池故障
    private ConnectionProvider connectionProvider;
    private JdbcClient jdbcClient;
    private static volatile Map<String,Integer> mapStation=new HashMap<>();
    private static volatile Set<String> stationCode0=new HashSet<>();
    private static volatile Map<String,StationInfo> mapStationInfo=new HashMap<>();//柜子信息
    private static volatile Map<String,String> mapBatteryInfo=new HashMap<>();//电池信息
    private  static volatile Long times=new Date().getTime();

    @Override
            public void prepare(Map stormConf, TopologyContext context,
               OutputCollector collector) {
               this.collector = collector;
        this.collector = collector;
        if(pool==null){
            synchronized (Station_battery_inTemp_and_damageHappenBolt.class){
                if(pool==null){
                    JedisPoolConfig config = new JedisPoolConfig();
                    config.setMaxIdle(5);
                    config.setMaxTotal(1000 * 100);
                    config.setMaxWaitMillis(30);
                    config.setTestOnBorrow(true);
                    config.setTestOnReturn(true);
                    pool = new JedisPool(config, "r-wz9gjghb9zyfa6ciyv.redis.rds.aliyuncs.com", 6379,20000,"Ehdbigdata190418");
                    //pool = new JedisPool(config, "10.27.169.187", 6574,20000,"immotor!6574@.com");
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
                   synchronized (Station_battery_inTemp_and_damageHappenBolt.class){
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
                           Map<String,String> map=jedis.hgetAll(batteryNumbersInStation);
                           Map<String,Integer> mapNumber=new HashMap<>();
                           List<String> delKey=new ArrayList<>();
                           for (Map.Entry<String,String> en:map.entrySet()){
                              String key= en.getKey();
                              String val=en.getValue();
                              String key0=  key.split("\\|")[0];
                              if(mapNumber.containsKey(key0)){
                                  delKey.add(key0+"|0");
                              }else{
                                  mapNumber.put(key0,Integer.parseInt(val));
                              }
                           }
                           if(delKey.size()>0){
                               String[] strings = new String[delKey.size()];
                               jedis.hdel(batteryNumbersInStation,delKey.toArray( strings));
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
                               cabinetHBTime_type31.setD(datas1.getBattery().get(i).getD());
                               cabinetHBTime_type31.setF(datas1.getBattery().get(i).getF());
                               cabinetHBTime_type31.setCycle(datas1.getBattery().get(i).getCcl());
                               cabinetHBTime_type31.setSoh(datas1.getBattery().get(i).getSoh());
                               cabinetHBTime_type31.setCurrent(datas1.getBattery().get(i).getC());
                               cabinetHBTime_type31.setSoc(datas1.getBattery().get(i).getSoc());
                               list.add(cabinetHBTime_type31);

                           }

                           listMap.put(cabinetHBTime_type3.getpID(),list);



                       }
                   }





               }
               //柜子电池数量
               Map<String,String> insertBatteryNumbersInStation=new HashMap<>();
               //删除没有城市属性的柜子
               List<String> delBatteryNumbersInStation=new ArrayList<>();
               Map<String,String> mapStationDamage=jedis.hgetAll(batteryDamageInStation);//获取故障电池列表
               Map<String,String> mapInsertBatteryDamage=new HashMap<>();//新的故障插入
               List<BatteryDamageHappenTime> batteryDamageHappenTimeList= new ArrayList<>();
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
                 //  jedis.hset(batteryNumbersInStation,(maps.getKey().trim()+"|"+  (mapStation.get((String) maps.getKey())==null?0+"": mapStation.get((String) maps.getKey())+"").trim()),(list2.size()>0?list2.get(0).getSum():0)+"");
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



                       //统计故障电池数
                       String damage="";
                       if(maps.getValue().get(j).getD()!=0){
                           if(mapStationDamage.get(maps.getValue().get(j).getId())==null//之前无故障
                                   ||(mapStationDamage.get(maps.getValue().get(j).getId()).split("\\|")[0]!=null
                                   &&(!isNumeric(mapStationDamage.get(maps.getValue().get(j).getId()).split("\\|")[0] )
                                   ||maps.getValue().get(j).getD()!=Integer.parseInt( mapStationDamage.get(maps.getValue().get(j).getId()).split("\\|")[0]  ) ))){
                               BatteryDamageHappenTime batteryDamageHappenTime=new BatteryDamageHappenTime();
                               batteryDamageHappenTime.setDamage(maps.getValue().get(j).getD());
                               batteryDamageHappenTime.setHappenTime(maps.getValue().get(j).getDate().getTime());
                               batteryDamageHappenTime.setPid(maps.getKey());
                               batteryDamageHappenTime.setBid(maps.getValue().get(j).getId());

                               batteryDamageHappenTime.setP(maps.getValue().get(j).getPort());
                               batteryDamageHappenTime.setV(maps.getValue().get(j).getVoltage());
                               batteryDamageHappenTime.setCurrent(maps.getValue().get(j).getCurrent());
                               batteryDamageHappenTime.setSoc(maps.getValue().get(j).getSoc());
                               batteryDamageHappenTime.setSoh(maps.getValue().get(j).getSoh());
                               batteryDamageHappenTime.setCycle(maps.getValue().get(j).getCycle());
                               batteryDamageHappenTime.setCurrent(maps.getValue().get(j).getCurrent());
                               batteryDamageHappenTime.setT(maps.getValue().get(j).getT());
                               batteryDamageHappenTime.setV(maps.getValue().get(j).getVoltage());
                               if(mapStationInfo.get(maps.getKey())!=null) {
                                   StationInfo stationInfo = mapStationInfo.get(maps.getKey());
                                   batteryDamageHappenTime.setCityCode(stationInfo.getCityCode());
                                   batteryDamageHappenTime.setName(stationInfo.getName());
                                   batteryDamageHappenTime.setSn(stationInfo.getSn());

                               }
                               if(mapBatteryInfo.get(maps.getValue().get(j).getId())!=null) {
                                   batteryDamageHappenTime.setbSn(mapBatteryInfo.get(maps.getValue().get(j).getId()));
                               }
                               damage=maps.getValue().get(j).getD()+"";
                               batteryDamageHappenTimeList.add(batteryDamageHappenTime);

                           }
                       }


                       if(maps.getValue().get(j).getF()!=0){
                           if(mapStationDamage.get(maps.getValue().get(j).getId())==null//之前无故障
                                   ||(mapStationDamage.get(maps.getValue().get(j).getId()).split("\\|").length==2&&(maps.getValue().get(j).getF()+1000)!=Integer.parseInt( mapStationDamage.get(maps.getValue().get(j).getId()).split("\\|")[1] ))){
                               BatteryDamageHappenTime batteryDamageHappenTime=new BatteryDamageHappenTime();
                               batteryDamageHappenTime.setDamage(maps.getValue().get(j).getF()+1000);
                               batteryDamageHappenTime.setHappenTime(maps.getValue().get(j).getDate().getTime());
                               batteryDamageHappenTime.setPid(maps.getKey());
                               batteryDamageHappenTime.setBid(maps.getValue().get(j).getId());
                               batteryDamageHappenTime.setP(maps.getValue().get(j).getPort());
                               batteryDamageHappenTime.setV(maps.getValue().get(j).getVoltage());
                               batteryDamageHappenTime.setCurrent(maps.getValue().get(j).getCurrent());
                               batteryDamageHappenTime.setSoc(maps.getValue().get(j).getSoc());
                               batteryDamageHappenTime.setSoh(maps.getValue().get(j).getSoh());
                               batteryDamageHappenTime.setCycle(maps.getValue().get(j).getCycle());
                               batteryDamageHappenTime.setCurrent(maps.getValue().get(j).getCurrent());
                               batteryDamageHappenTime.setT(maps.getValue().get(j).getT());
                               batteryDamageHappenTime.setV(maps.getValue().get(j).getVoltage());
                               if(mapStationInfo.get(maps.getKey())!=null) {
                                   StationInfo stationInfo = mapStationInfo.get(maps.getKey());
                                   batteryDamageHappenTime.setCityCode(stationInfo.getCityCode());
                                   batteryDamageHappenTime.setName(stationInfo.getName());
                                   batteryDamageHappenTime.setSn(stationInfo.getSn());
                               }
                               if(mapBatteryInfo.get(maps.getValue().get(j).getId())!=null) {
                                   batteryDamageHappenTime.setbSn(mapBatteryInfo.get(maps.getValue().get(j).getId()));
                               }
                               damage+=("|"+(maps.getValue().get(j).getF()+1000));
                               batteryDamageHappenTimeList.add(batteryDamageHappenTime);

                           }
                       }
                       if(!damage.equals("")){
                           mapInsertBatteryDamage.put(maps.getValue().get(j).getId(),damage);
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
                   jedis.hdel(batteryNumbersInStation,delBatteryNumbersInStation.toArray( strings));
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


               if(batteryHB3InStationTempList.size()>0){
                   LoghubService loghubService=new LoghubService();
                   loghubService.batteryHB3InStationTempList(batteryHB3InStationTempList);
               }

               //插入新的故障
               if(mapInsertBatteryDamage.size()>0){
                   jedis.hmset(batteryDamageInStation,mapInsertBatteryDamage);
               }
               if(batteryDamageHappenTimeList.size()>0){
                   LoghubService loghubService=new LoghubService();
                   loghubService.batteryDamageHappenTimeList(batteryDamageHappenTimeList);
               }
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
