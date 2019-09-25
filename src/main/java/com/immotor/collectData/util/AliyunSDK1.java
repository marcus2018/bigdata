package com.immotor.collectData.util;

import com.aliyun.datahub.DatahubClient;
import com.aliyun.datahub.DatahubConfiguration;
import com.aliyun.datahub.auth.AliyunAccount;
import com.aliyun.datahub.common.data.FieldType;
import com.aliyun.datahub.common.data.RecordSchema;
import com.aliyun.datahub.common.data.RecordType;
import com.aliyun.datahub.model.ErrorEntry;
import com.aliyun.datahub.model.ListShardResult;
import com.aliyun.datahub.model.PutRecordsResult;
import com.aliyun.datahub.model.RecordEntry;
import com.immotor.collectData.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class AliyunSDK1 {
    private static final Logger logger = LoggerFactory.getLogger(AliyunSDK.class);
    private static final String accessId = "HmTjtVwGWaEvbDw5";
    private static final String accessKey = "GEKUNH2wv7fen6LxQhH2655ZcgRvmd";
    private static  final String endpoint = "http://dh-cn-hangzhou.aliyuncs.com";
    private static final AliyunAccount account = new AliyunAccount(accessId, accessKey);
    private static final DatahubConfiguration conf = new DatahubConfiguration(account, endpoint);
    private static final DatahubClient client = new DatahubClient(conf);
    private SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");
    private  Random r=new Random();
    private  String pojectName="android_collectdata";
   private   static  List<LogCommon> list=new ArrayList<LogCommon>();

    public void write(List<LogCommon> list,String topicName){
        ListShardResult listShardResult = client.listShard(pojectName, topicName);
        List<RecordEntry> recordEntries = new ArrayList<RecordEntry>();
        String shardId= listShardResult.getShards().get(r.nextInt(6)).getShardId();
        RecordSchema schema = client.getTopic(pojectName, topicName).getRecordSchema();
        RecordEntry entry = new RecordEntry(schema);
        for(int i=0;i<list.size();i++){
            if(list.get(i)instanceof EcLocLog ){
                EcLocLog ecLocLog=(EcLocLog) list.get(i);
                entry.setString(0,ecLocLog.getSid());
                entry.setDouble(1,  ecLocLog.getLongitude());
                entry.setDouble(2,  ecLocLog.getLatitude());
                entry.setTimeStampInDate(3, new Date(ecLocLog.getTime()));//time
                entry.setString(4, sdf.format(new Date()));//pt
                entry.setShardId(shardId);
                recordEntries.add(entry);
            }
        }
//        if(log instanceof EcLocLog){
//            EcLocLog ecLocLog=(EcLocLog) log;
//            entry.setString(0,ecLocLog.getSid());
//            entry.setDouble(1,  ecLocLog.getLongitude());
//            entry.setDouble(2,  ecLocLog.getLatitude());
//            entry.setTimeStampInDate(3, new Date(ecLocLog.getTime()));//time
//            entry.setString(4, sdf.format(new Date()));//pt
////            logger.info("BatteryOnceDataCollect pid="+batteryOnceDataCollect.getPid()+" id:"+  batteryOnceDataCollect.getId()+" "
////                    +"CollectTime:"+batteryOnceDataCollect.getCollectTime()+"PartDeviceInfo:"+ batteryOnceDataCollect.getPartDeviceInfo()+
////                    " PartProtectionParameter:"+batteryOnceDataCollect.getPartProtectionParameter()+"Date:"+new Date());
//        }
//        entry.setShardId(shardId);
//        recordEntries.add(entry);
        PutRecordsResult result = client.putRecords(pojectName, topicName, recordEntries);

    }
    public  static void main(String[] args){
           list.add(0,new EcLocLog());
        list.add(1,new EcLocLog());
        list.add(2,new EcLocLog());
        list.add(3,new EcLocLog());
        AliyunSDK1 aliyunSDK1=new AliyunSDK1();
        aliyunSDK1.write(list,"hahha");
    }
}

