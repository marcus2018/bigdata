package com.immotor.collectData.util;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.datahub.DatahubClient;
import com.aliyun.datahub.DatahubConfiguration;
import com.aliyun.datahub.auth.AliyunAccount;
import com.aliyun.datahub.common.data.RecordSchema;
import com.aliyun.datahub.model.ErrorEntry;
import com.aliyun.datahub.model.ListShardResult;
import com.aliyun.datahub.model.PutRecordsResult;
import com.aliyun.datahub.model.RecordEntry;

import com.aliyun.openservices.log.common.LogItem;
import com.aliyun.openservices.log.producer.ProjectConfig;
import com.immotor.collectData.loghub.LoghubService;
import com.immotor.collectData.model.*;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


public class AliyunSDK {
    private static final Logger logger = LoggerFactory.getLogger(AliyunSDK.class);
    private String accessId = "HmTjtVwGWaEvbDw5";
    private String accessKey = "GEKUNH2wv7fen6LxQhH2655ZcgRvmd";
    private String endpoint = "http://dh-cn-hangzhou.aliyuncs.com";
    private AliyunAccount account = new AliyunAccount(accessId, accessKey);
    private DatahubConfiguration conf = new DatahubConfiguration(account, endpoint);
    private DatahubClient client = new DatahubClient(conf);
    private SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");
    private SimpleDateFormat sdf11=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    private  Random r=new Random();



    public void write(Object log,String topicName){
        //loghub
        if(log  instanceof JSONObject){
            LoghubService loghubService=new LoghubService();
          //  logger.info("1231"+((JSONObject) log).toJSONString());
            loghubService.sendLogHub(((JSONObject) log).toJSONString(), new Date(), sdf.format(new Date()));
            return;
        }




        if(log instanceof BatteryRealTimeDataCollect){
            BatteryRealTimeDataCollect batteryRealTimeDataCollect=(BatteryRealTimeDataCollect) log;
            LoghubService loghubService=new LoghubService();
            loghubService.sendBatteryRealTimeInfo(batteryRealTimeDataCollect.getPid(),batteryRealTimeDataCollect.getId(), new Date(),batteryRealTimeDataCollect.getPartReadOnlyData(),batteryRealTimeDataCollect.getPartRunControlData(), sdf.format(new Date()));
            return;

        }

        if(log instanceof BatteryLogDataCollect){
            BatteryLogDataCollect batteryLogDataCollect=(BatteryLogDataCollect) log;

            LoghubService loghubService=new LoghubService();
            loghubService.sendBatteryLogInfo(batteryLogDataCollect.getPid(),batteryLogDataCollect.getId(), new Date(),batteryLogDataCollect.getLogIndex(),batteryLogDataCollect.getPartLog(), sdf.format(new Date()));
            return;
//            logger.info("BatteryLogDataCollect pid="+batteryLogDataCollect.getPid()+" id:"+  batteryLogDataCollect.getId()+" "
//                    +"CollectTime:"+batteryLogDataCollect.getCollectTime()+"LogIndex:"+ batteryLogDataCollect.getLogIndex()+
//                    " PartLog:"+ batteryLogDataCollect.getPartLog()+"Date:"+new Date());
        }

        ListShardResult listShardResult = client.listShard("android_collectdata", topicName);
        List<RecordEntry> recordEntries = new ArrayList<RecordEntry>();
        String shardId=null;
        if(log instanceof BatteryStatusLog||log instanceof CabinetRecordLog ||log instanceof  EmptyTradeableRecord||log instanceof ChargeTimeLog
                ||log instanceof BatteryOnceDataCollect ||log instanceof BatteryLogDataCollect ||log instanceof BatteryRealTimeDataCollect ||log instanceof LogStationMessage||log instanceof AndriodBatteryInfo || log instanceof  BatteryDetailInfo
                || log instanceof CabinetMonitoringInfo ){
            shardId = listShardResult.getShards().get(r.nextInt(6)).getShardId();
        }else {
            shardId = listShardResult.getShards().get(r.nextInt(2)).getShardId();
        }
        RecordSchema schema = client.getTopic("android_collectdata", topicName).getRecordSchema();
        RecordEntry entry = new RecordEntry(schema);
        if(log instanceof ChargeTimeLog){
            ChargeTimeLog chargeTimeLog=(ChargeTimeLog) log;
            entry.setBigint(0,  chargeTimeLog.getStartSoc());
            entry.setBoolean(1,chargeTimeLog.getOnceFull());
           // entry.setTimeStampInDate(2,new Date(chargeTimeLog.getStopTime()));
            entry.setTimeStampInDate(2,new Date());
            entry.setString(3,chargeTimeLog.getId());
            entry.setString(4,chargeTimeLog.getPid());
            entry.setTimeStampInDate(5,new Date(chargeTimeLog.getStartTime()));
            entry.setBigint(6,chargeTimeLog.getStopSoc());
            entry.setString(7, sdf.format(new Date()));
            logger.info("ChargeTimeLog pid="+chargeTimeLog.getPid()+" id:"+  chargeTimeLog.getId()+" "
                    +" StopTime:"+chargeTimeLog.getStopTime()+" StartTime:"+ chargeTimeLog.getStartTime()+
                    " StopSoc:"+chargeTimeLog.getStopSoc()+  " StartSoc:"+chargeTimeLog.getStartSoc()+" Date:"+new Date());

        }
        if(log instanceof BatteryVersionLog){
            BatteryVersionLog batteryVersionLog=(BatteryVersionLog) log;
            entry.setString(0,  batteryVersionLog.getHw());
            entry.setString(1,batteryVersionLog.getId());
            entry.setString(2,batteryVersionLog.getSw());
            entry.setString(3, sdf.format(new Date()));
            logger.info("BatteryVersionLog sw="+batteryVersionLog.getSw()+" id:"+  batteryVersionLog.getId()+" "
                    +" Hw:"+batteryVersionLog.getHw()+" Date:"+new Date());
        }
        if(log instanceof BatteryStatusLog){
            BatteryStatusLog batteryStatusLog=(BatteryStatusLog) log;
            entry.setString(0,batteryStatusLog.getId());
            entry.setBigint(1,  batteryStatusLog.getPort());
            entry.setString(2,  batteryStatusLog.getValue());
            entry.setString(3,  batteryStatusLog.getPid());
            entry.setBigint(4,batteryStatusLog.getOutValidSoc());
            entry.setBigint(5,batteryStatusLog.getOutValidSocTime());
            entry.setString(6, sdf.format(new Date()));
      }
        if(log instanceof CabinetRecordLog){
            CabinetRecordLog cabinetRecordLog=(CabinetRecordLog) log;
            entry.setString(0,cabinetRecordLog.getPid());
            entry.setDouble(1,  cabinetRecordLog.getMeterrecord());
            entry.setString(2,cabinetRecordLog.getValue());
            entry.setTimeStampInDate(4,new Date());

            Date date=new Date();
            String str="";
            try {
            if(DateUtils.SDF1.format(date).substring(DateUtils.SDF1.format(date).indexOf(" ")).trim().compareTo(DateUtils.DEFAULTIME)>0){

                    str=DateUtils.findPreviousOrAfterDays(DateUtils.SDF.format(date),0);

            }else{
                    str=DateUtils.findPreviousOrAfterDays(DateUtils.SDF.format(date),-1);
            }
            } catch (ParseException e) {
                e.printStackTrace();
            }
            entry.setString(5,  sdf.format(new Date()));
            LoghubService loghubService=new LoghubService();
            loghubService.sendCabinetRercordInfo(cabinetRecordLog.getPid(),cabinetRecordLog.getMeterrecord(),DateUtils4Vo.dateToDateString(new Date()), sdf.format(new Date()));

            logger.info("CabinetRecordLog pid="+cabinetRecordLog.getPid()+" Meterrecord:"+ cabinetRecordLog.getMeterrecord()+" "
                   +"Starttime:"+cabinetRecordLog.getStarttime()+"Stoptime:"+cabinetRecordLog.getStoptime()+"Date:"+new Date());
          //  return;
        }
        if(log instanceof CabinetMonitoringInfo){
            CabinetMonitoringInfo cabinetMonitoringInfo=(CabinetMonitoringInfo) log;
            entry.setString(0,cabinetMonitoringInfo.getInfo());
            entry.setTimeStampInDate(1,new Date());
            entry.setString(2,  sdf.format(new Date()));

        }
        if(log instanceof BatteryOnceDataCollect){
            BatteryOnceDataCollect batteryOnceDataCollect=(BatteryOnceDataCollect) log;
            entry.setString(0,batteryOnceDataCollect.getPid());
            entry.setString(1,  batteryOnceDataCollect.getId());
            entry.setTimeStampInDate(2, new Date(batteryOnceDataCollect.getCollectTime()));
            entry.setString(3,  batteryOnceDataCollect.getPartDeviceInfo());
            entry.setString(4,  batteryOnceDataCollect.getPartProtectionParameter());
            entry.setString(5, sdf.format(new Date()));
            logger.info("BatteryOnceDataCollect pid="+batteryOnceDataCollect.getPid()+" id:"+  batteryOnceDataCollect.getId()+" "
                    +"CollectTime:"+batteryOnceDataCollect.getCollectTime()+"PartDeviceInfo:"+ batteryOnceDataCollect.getPartDeviceInfo()+
                    " PartProtectionParameter:"+batteryOnceDataCollect.getPartProtectionParameter()+"Date:"+new Date());

        }





        if(log instanceof EmptyTradeableRecord){
            EmptyTradeableRecord emptyTradeableRecord=(EmptyTradeableRecord) log;
            entry.setString(0,emptyTradeableRecord.getPid());
            entry.setString(1,  emptyTradeableRecord.getValue());
            entry.setString(2, sdf.format(new Date()));
            logger.info("EmptyTradeableRecord pid="+emptyTradeableRecord.getPid()+" value:"+  emptyTradeableRecord.getValue()+"Date:"+new Date());
        }
        if(log instanceof LogStationMessage){
            LogStationMessage logStationMessage=(LogStationMessage) log;
            String[] strs= logStationMessage.getMessage().split("\\n");
            for (int i=0;i<strs.length;i++){
                RecordEntry entry1 = new RecordEntry(schema);
                entry1.setString(0,logStationMessage.getPid());
                entry1.setString(1, strs[i]);
                entry1.setString(2, sdf.format(new Date()));
                entry1.setShardId(shardId);
                recordEntries.add(entry1);
               // System.out.println(logStationMessage.getPid()+" "+strs[i]);
            }
            //client.putRecords("android_collectdata", topicName, recordEntries);
           // return;
        }
        if(log instanceof AndriodBatteryInfo) {//aliyun 日志平台
            AndriodBatteryInfo andriodBatteryInfo = (AndriodBatteryInfo) log;
            entry.setString(0, andriodBatteryInfo.getpID());
            entry.setBigint(1,  andriodBatteryInfo.getSureIn().longValue());
            entry.setBigint(2,  andriodBatteryInfo.getMaybeIn().longValue());
            StringBuffer sbOrigin=new StringBuffer();
            for (int i=0;i<andriodBatteryInfo.getOrigin().length;i++){
                if(i==andriodBatteryInfo.getOrigin().length-1){
                    sbOrigin.append(andriodBatteryInfo.getOrigin()[i]);
                }else {
                    sbOrigin.append(andriodBatteryInfo.getOrigin()[i] + ",");
                }
            }
            entry.setString(3,  sbOrigin.toString());
            StringBuffer sbInPort=new StringBuffer();
            for (int i=0;i<andriodBatteryInfo.getInPort().length;i++){
                if(i==andriodBatteryInfo.getInPort().length-1){
                    sbInPort.append(andriodBatteryInfo.getInPort()[i]);
                }else {
                    sbInPort.append(andriodBatteryInfo.getInPort()[i] + ",");
                }
            }
            entry.setString(4, sbInPort.toString());
            StringBuffer sbOutPort=new StringBuffer();
            for (int i=0;i<andriodBatteryInfo.getOutPort().length;i++){
                if(i==andriodBatteryInfo.getOutPort().length-1){
                    sbOutPort.append(andriodBatteryInfo.getOutPort()[i]);
                }else {
                    sbOutPort.append(andriodBatteryInfo.getOutPort()[i] + ",");
                }
            }
            entry.setString(5, sbOutPort.toString());
            entry.setTimeStampInDate(6, new Date());
            entry.setString(7, sdf.format(new Date()));
            System.out.println("pid="+andriodBatteryInfo.getpID()+" sure="+andriodBatteryInfo.getSureIn().longValue()+
                            " maybe="+ andriodBatteryInfo.getMaybeIn().longValue()+" origin="+12+
            " inPort="+ StringUtils.join(andriodBatteryInfo.getInPort())+" outPort="+StringUtils.join(andriodBatteryInfo.getOutPort()));
        }
        if(log instanceof BatteryDetailInfo){
            BatteryDetailInfo batteryDetailInfo=(BatteryDetailInfo) log;
            System.out.println(batteryDetailInfo.getPID());
            System.out.println(batteryDetailInfo.getMBattery().getId());
            entry.setBigint(0,batteryDetailInfo.getMBattery().getCurrent());
            entry.setBigint(1,  batteryDetailInfo.getMBattery().getCycle());
           // entry.setBigint(2,batteryDetailInfo.getMBattery().getCurrent());
            entry.setBigint(2,  batteryDetailInfo.getMBattery().getDamage());
            entry.setBigint(3,batteryDetailInfo.getMBattery().getDesignCapacity());
            entry.setBigint(4,  batteryDetailInfo.getMBattery().getFault());
            entry.setString(5,batteryDetailInfo.getMBattery().getId());
            entry.setBigint(6,  batteryDetailInfo.getMBattery().getNominalCurrent());
            entry.setBigint(7,batteryDetailInfo.getMBattery().getNominalVoltage());
            entry.setBigint(8,  batteryDetailInfo.getMBattery().getPort());
            entry.setBigint(9,batteryDetailInfo.getMBattery().getSoc());
            entry.setBigint(10,  batteryDetailInfo.getMBattery().getSoh());
            entry.setBigint(11,batteryDetailInfo.getMBattery().getTemperature());
            entry.setBigint(12,  batteryDetailInfo.getMBattery().getVoltage());
            entry.setTimeStampInDate(13, new Date());
            entry.setString(14,batteryDetailInfo.getPID());
            entry.setString(15,  sdf.format(new Date()));
        }

        entry.setShardId(shardId);
        recordEntries.add(entry);
        long start=System.currentTimeMillis();

        PutRecordsResult result = client.putRecords("android_collectdata", topicName, recordEntries);
      //  client.close();
        long end=System.currentTimeMillis();
        System.out.println(end-start);
        if (result.getFailedRecordCount() != 0) {
            List<ErrorEntry> errors = result.getFailedRecordError();
        }
    }


}
