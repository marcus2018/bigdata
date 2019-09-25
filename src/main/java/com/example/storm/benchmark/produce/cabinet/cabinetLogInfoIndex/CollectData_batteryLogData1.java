package com.example.storm.benchmark.produce.cabinet.cabinetLogInfoIndex;

import com.aliyun.openservices.log.common.LogGroupData;
import com.aliyun.openservices.log.common.Logs;
import com.aliyun.openservices.log.exception.LogException;
import com.aliyun.openservices.loghub.stormspout.LogHubSpout;
import com.example.storm.benchmark.produce.cabinet.cabinetLogInfoIndex.bean.BatteryLogInfo;
import com.example.storm.benchmark.produce.cabinet.cabinetLogInfoIndex.bean.BatteryLogOmitInfo;
import com.example.storm.benchmark.produce.cabinet.cabinetLogInfoIndex.bean.LogInfo;
import com.example.storm.benchmark.util.DataSource;
import com.example.storm.benchmark.util.RedisConstants;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseWindowedBolt;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.windowing.TupleWindow;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.*;
import java.util.regex.Pattern;

public class CollectData_batteryLogData1 extends BaseWindowedBolt {
    private OutputCollector collector;
   public  static final String MAXINDEX="maxIndex";
   public  static final String MAX="max";
   public  static final String MIN="min";
    private static volatile JedisPool pool;
    @Override
    public void prepare(Map stormConf, TopologyContext context,
                        OutputCollector collector) {
        this.collector = collector;
        if(pool==null){
            synchronized (CollectData_batteryLogData1.class){
                if(pool==null){
                    pool=DataSource.getJedisPool();
                }
            }
        }
    }
    @Override
    public void execute(TupleWindow inputWindow) {
        Jedis jedis= pool.getResource();
        Map<String,String> mapBatteryLogInfo=new HashMap<>();//电池历史日志bid,eventType，eventTime
        List<BatteryLogInfo>  listBatteryLogInfo=new ArrayList<>();
        Map<String,List<BatteryLogInfo>> mapListBatteryLogInfo=new HashMap<>();
        Set<String> setBid=new HashSet<>();//bids

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
                    for (Logs.Log.Content content : log.getContentsList()) {
                        if(content.getKey().equals("content")){
                            System.out.println(content.getKey().equals("content"));
                            if(content.getValue().contains("\"type\":11")){
                                JsonObject obj = new JsonParser().parse(content.getValue()).getAsJsonObject();
                                LogInfo logInfo = new Gson().fromJson(obj, LogInfo.class);
                                List<String> list1=logInfo.getList();
                                for(int i=0;i<list1.size();i++){
                                    String[] logInfoDetail=list1.get(i).split(",");
                                    if(logInfoDetail.length!=139)
                                        continue;
                                    String bid=logInfoDetail[5]+","+logInfoDetail[6]+","+logInfoDetail[7]+","+logInfoDetail[8]+","+logInfoDetail[9]+","
                                            +logInfoDetail[10];
                                    long eventType=Long.parseLong(logInfoDetail[14]+logInfoDetail[13]+logInfoDetail[12]+logInfoDetail[11], 16);
                                    String time=logInfoDetail[15]+","+logInfoDetail[16]+","+logInfoDetail[17]+","+logInfoDetail[18]+","+logInfoDetail[19]+","
                                            +logInfoDetail[20];
                                    setBid.add(bid);
                                   // mapBatteryLogInfo.put(bid,bid+"|"+eventType+"|"+time);
                                    listBatteryLogInfo.add(new BatteryLogInfo(bid,eventType,time));
                                  //  mapListBatteryLogInfo.put(bid,listBatteryLogInfo);
                                }

                            }
                        }
                    }

                }
            }
        }
        Map<String,String> insertMap=new HashMap<>();
        Set<String> delKey=new HashSet<>();
        if(setBid.size()>0){
            String[] strings = new String[setBid.size()];
            List<String> redisValues =jedis.hmget(RedisConstants.BATTERY_LOGINFO_NEW_LOGINDEX,setBid.toArray(strings));//获取redis数据
            for (int i=0;i<redisValues.size();i++){
                if(redisValues.get(i)==null)
                    continue;
                if(redisValues.get(i).split("\\|").length!=4){
                    delKey.add(redisValues.get(i).split("\\|")[0]);
                    continue;
                }
                String redisBid=redisValues.get(i).split("\\|")[0];
                String redisEventType=redisValues.get(i).split("\\|")[1];
                String redisTime=redisValues.get(i).split("\\|")[2];
                String redisOmit=redisValues.get(i).split("\\|")[3];

                //redisOmit 格式 xx-xx-xx-xx:xx,xx,xx,xx：。。。。
                listBatteryLogInfo.add(new BatteryLogInfo(redisBid, Long.parseLong(redisEventType), redisTime, CollectData_batteryLogData1.MAXINDEX));//redis最大的游标
                if(!redisOmit.equals("-1")) {
                    //有遗漏的游标
                    String[] redisArray = redisOmit.split(":");
                /*    if(redisArray.length!=4)
                        continue;*/
                    for(int j=0;j<redisArray.length;j++) {
                        String[] redisArrayElement=redisArray[j].split("-");
                        if(redisArrayElement.length!=4)
                            continue;
                        listBatteryLogInfo.add(new BatteryLogInfo(redisBid, Long.parseLong(redisArrayElement[0]), redisArrayElement[2], CollectData_batteryLogData1.MIN));//遗漏最小游标
                        listBatteryLogInfo.add(new BatteryLogInfo(redisBid, Long.parseLong(redisArrayElement[0])+Long.parseLong(redisArrayElement[1])+1, redisArrayElement[3], CollectData_batteryLogData1.MAX));//遗漏最大游标
                        //    mapListBatteryLogInfo.put(redisBid,listBatteryLogInfo);
                    }
                }
            }
        }
        Collections.sort(listBatteryLogInfo);
        String omitIndex="-1";
       // List<BatteryLogOmitInfo> list=new ArrayList();
        Map<String,List<BatteryLogOmitInfo>> mapOmit=new HashMap<>();
        Map<String,String> mapMaxIndexTime=new HashMap<>();
        for (int i=0;i<listBatteryLogInfo.size()-1;i++){
            String bid1=listBatteryLogInfo.get(i).getBid();
            String bid2=listBatteryLogInfo.get(i+1).getBid();
            if(bid1.equals(bid2)){  //同一个电池
              long diff=  listBatteryLogInfo.get(i+1).getEventType()-listBatteryLogInfo.get(i).getEventType();
            /*  if(listBatteryLogInfo.get(i).getOmitLogIndex()!=null){
                  list.add(new BatteryLogOmitInfo(listBatteryLogInfo.get(i).getOmitLogIndex().split(",")[0],//begin
                                                  listBatteryLogInfo.get(i).getOmitLogIndex().split(",")[0]));
              }*/
              if(diff>1&&((!CollectData_batteryLogData1.MAXINDEX.equals(listBatteryLogInfo.get(i+1).getOmitLogIndex()))
                      &&(!CollectData_batteryLogData1.MAX.equals(listBatteryLogInfo.get(i).getOmitLogIndex())&&
                      !CollectData_batteryLogData1.MIN.equals(listBatteryLogInfo.get(i+1).getOmitLogIndex())))){
                  //开始游标，结束游标，开始时间，结束时间
                  List<BatteryLogOmitInfo> list=new ArrayList();
                  list.add(new BatteryLogOmitInfo( listBatteryLogInfo.get(i).getEventType(), listBatteryLogInfo.get(i+1).getEventType(),listBatteryLogInfo.get(i).getTime(),listBatteryLogInfo.get(i+1).getTime()));//遗漏的type
                  if(mapOmit.containsKey(bid1)){
                      list.addAll(mapOmit.get(bid1));
                  }
                  mapOmit.put(bid1,list);
              }
            }else {
               // list.clear();
                mapMaxIndexTime.put(bid1,bid1+"|"+listBatteryLogInfo.get(i).getEventType()+"|"+listBatteryLogInfo.get(i).getTime());//最大的type and time
                continue;
            }

            if(i==listBatteryLogInfo.size()-2){//最后电池

                    if(bid1.equals(listBatteryLogInfo.get(i+1).getBid())){//是一个电池 ，取出最后一次游标计作最大游标
                      //  list.clear();
                        mapMaxIndexTime.put(bid1,bid1+"|"+listBatteryLogInfo.get(i+1).getEventType()+"|"+listBatteryLogInfo.get(i+1).getTime());//最大的type and time
                     //   mapMaxIndexTime.put(listBatteryLogInfo.get(i+1).getBid(),
                     //           listBatteryLogInfo.get(i+1).getBid()+"|"+listBatteryLogInfo.get(i+1).getEventType()+"|"+listBatteryLogInfo.get(i+1).getTime());//最大的type and time
                    }else {//不相等 最后一条记录为单独电池
                        mapMaxIndexTime.put(listBatteryLogInfo.get(i+1).getBid(),listBatteryLogInfo.get(i+1).getBid()+"|"+listBatteryLogInfo.get(i+1).getEventType()+"|"+listBatteryLogInfo.get(i+1).getTime());
                    }

            }
        }
        for(Map.Entry<String,String> elements:mapMaxIndexTime.entrySet()){
               String key=  elements.getKey();
               String value=elements.getValue();
               StringBuffer  sb=new StringBuffer();
               if(mapOmit.containsKey(key)) {
                   List<BatteryLogOmitInfo> list1=mapOmit.get(key);
                   //开始游标，结束游标，开始时间，结束时间
                   for(int i=0;i<list1.size();i++ ){
                       BatteryLogOmitInfo batteryLogOmitInfo= list1.get(i);
                       if(i==list1.size()-1){
                           sb.append(batteryLogOmitInfo.getBeginIndex() + "-" + (batteryLogOmitInfo.getEndIndex() -
                                   batteryLogOmitInfo.getBeginIndex() - 1) + "-" + batteryLogOmitInfo.getBeginTime() + "-" + batteryLogOmitInfo.getEndTime());

                       }else {
                           sb.append(batteryLogOmitInfo.getBeginIndex() + "-" + (batteryLogOmitInfo.getEndIndex() - batteryLogOmitInfo.getBeginIndex() - 1) + "-" + batteryLogOmitInfo.getBeginTime() + "-" + batteryLogOmitInfo.getEndTime()).append(":");
                       }
                   }
               }else{
                   //默认-1
                   sb.append("-1");
               }
               insertMap.put(key,value+"|"+ sb.toString());



        }

       if(insertMap.size()>0){
            jedis.hmset(RedisConstants.BATTERY_LOGINFO_NEW_LOGINDEX,insertMap);
       }
       if(delKey.size()>0){
            String[] str=new String[delKey.size()];
            jedis.hdel(RedisConstants.BATTERY_LOGINFO_NEW_LOGINDEX,delKey.toArray(str));
       }
        if(jedis!=null){
            jedis.close();
        }







    }

    public static boolean isNumeric(String str){
        Pattern pattern = Pattern.compile("[0-9]+");
        return pattern.matcher(str).matches();
    }


    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        // declarer.declare(new Fields("count"));
    }


    public static class CollectData_cabinetBatteryLogDataBolt {
    }
}