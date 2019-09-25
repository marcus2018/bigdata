package com.immotor.collectData.service;


import com.immotor.collectData.model.BatteryLog;
import com.immotor.collectData.model.BatteryLostIndex;
import com.immotor.collectData.model.LogCommon;
import com.immotor.collectData.model.StationBid;
import com.immotor.collectData.util.AliyunSDK;
import com.immotor.collectData.util.RedisContants;
import com.immotor.collectData.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class Collect {
    ExecutorService exec = Executors .newCachedThreadPool( );
    ExecutorService exec1 = Executors .newFixedThreadPool( 20);
    @Autowired
    private RedisUtil redisUtil;
    public Object collectLog(final  Object log,final String topicName) {
        {
            exec1.execute(new Runnable() {
                public void run() {
                    AliyunSDK aliyunSDK=new AliyunSDK();
                    aliyunSDK.write(log,topicName);
                }

            });

            return 1;

        }
    }

    public Object getBatteryLogIndex(StationBid stationBid, String batteryLogIndex) {

      //  System.out.println(stationBid.getContent());
        if(stationBid.getContent().split(",").length<18){
            return  -1;
        }
       String bid = stationBid.getContent().substring(13,30);
        String[] startIndex=stationBid.getContent().substring(31,42).split(",");
        String[] endIndex=stationBid.getContent().substring(43,54).split(",");
       long start= Long.parseLong(startIndex[3]
             +  startIndex[2]
              + startIndex[1]
              + startIndex[0],16);
        long end=Long.parseLong(endIndex[3]+endIndex[2]+endIndex[1]+endIndex[0],16);

        //System.out.println(stationBid.getContent().split(",").length-6+" "+ bid+" "+stationBid.getContent().length());
            List<String> list1 = redisUtil.hmget(RedisContants.BATTERY_LOGINFO_NEW_LOGINDEX, 0, bid);
            List<BatteryLog> returnList=new ArrayList<BatteryLog>();
            for (int i = 0; i < list1.size(); i++) {
                if(list1.get(i)==null){
                    return  -1;
                }
               String[] strs=   list1.get(i).split("\\|");
                System.out.println(start+" "+end+" "+ Long.parseLong(strs[1]));
                if(start> Long.parseLong(strs[1])||end< Long.parseLong(strs[1])){
                    redisUtil.hdel(RedisContants.BATTERY_LOGINFO_NEW_LOGINDEX,  bid);
                    return  -1;
                }
               List<BatteryLostIndex> list2=new ArrayList<BatteryLostIndex>();
               if(strs.length>=4) {
                   String[] str3 = strs[3].split(":");
                   for (int j = 0; j < (str3.length < 50 ? str3.length : 50); j++) {
                       if (!str3[j].equals("-1")) {
                          // if (Long.parseLong(str3[j].split("-")[0]) >= start && (Long.parseLong(str3[j].split("-")[0]) + 1 + Long.parseLong(str3[j].split("-")[2])) <= end) {
                               list2.add(new BatteryLostIndex(str3[j].split("-")[0], str3[j].split("-")[1], str3[j].split("-")[2]));
                        /*   }else{
                               flag=false;
                           }*/


                       }
                   }
               }

                   BatteryLog batteryLog = new BatteryLog(strs[0], Long.parseLong(strs[1]), strs[2], list2);
                   return batteryLog;

        }
        return -1;
    }




}
