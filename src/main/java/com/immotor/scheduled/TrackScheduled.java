package com.immotor.scheduled;


import com.immotor.bean.TrackInfo;
import com.immotor.constants.Contants;
import com.immotor.constants.RedisConstants;
import com.immotor.loghub.LoghubService;
import com.immotor.util.*;
import com.sun.javafx.collections.MappingChange;
import net.sf.json.JSONObject;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import java.util.*;

@Controller
public class TrackScheduled {
    private static final Logger logger = LoggerFactory.getLogger(TrackScheduled.class);
    private  static   List<String> listArea=new ArrayList<String>();
    private int count = 0;
    private int count2 = 0;
    private int count3 = 0;
    @Autowired
    private RedisUtil jedisUtil;
    @Autowired
    private RedisTemplate redisTemplate;

    static {
        listArea.add("北京市");
        listArea.add("天津市");
       // listArea.add("重庆市");
        listArea.add("上海市");
    }

/*    @Scheduled(fixedRate = 3000)
    public void fixedRate() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            logger.debug("fixedRate", e);
        }
        count++;
        logger.info("fixedRate " + count + " time " + System.currentTimeMillis());
    }

    @Scheduled(fixedDelay = 3000)
    public void fixedDelay() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            logger.debug("fixedDelay", e);
        }
        count2++;
        logger.info("fixedDelay " + count2 + " time " + System.currentTimeMillis());
    }*/
    //  每隔3秒执行一次：*/3 * * * * ?
    @Scheduled(cron = "0 0/2 * * * ?")
    public void cron() {

        Map<String,String> map1= jedisUtil.hgetall(RedisConstants.SCOOTERTRACKDISTANCEGET1000,0);
        System.out.println(map1.size());
        Set<String> sIDset=new HashSet<>();
        Map<String,String> mapTmp=new HashMap<>();
        List<String> delKey=new ArrayList<>();
        List<String> delDetailKey=new ArrayList<>();
        for(Map.Entry<String,String> maps:map1.entrySet()){
            if((new Date().getTime()-Long.parseLong(maps.getValue()))>5*60*1000){
                sIDset.add(maps.getKey()
                );
                map1.put(maps.getKey(),maps.getValue());
            }
        }


        System.out.println("======start========"+sIDset.size());
         List list=new ArrayList();
        long start=System.currentTimeMillis();
       for(String str:sIDset){
            Map<String,String> map=jedisUtil.hgetall(RedisConstants.SCOOTERTRACKER+str,0);

            if(map.size()>0) {
                long nowtime = Long.parseLong(map.get("nowTime"));
                long startTime = Long.parseLong(map.get("startTime"));
                String nowLocation = map.get("nowLocation");
                String startLocation = map.get("startLocation");
                String distance = map.get("nowDistance");
                int  nowSoc=Integer.parseInt(map.get("soc"));

                int ccode=Integer.parseInt(("null").equals(map.get("code"))?"-1":map.get("code"));
                //System.out.pristartLocationntln(str+" "+nowtime+" "+map1.get(str)+" "+(nowtime-Long.parseLong(map1.get(str)))+" "+distance);
                if(Double.parseDouble(distance)<1000.0){
                    jedisUtil.hdel(RedisConstants.SCOOTERTRACKDISTANCEGET1000,str);
                    delKey.add(str);
                }
                if (new Date().getTime() - nowtime > 5 * 60 * 1000&&Double.parseDouble(distance)>1000) {
                    System.out.println();
                    Map<String, String> startLocationInfo = GeoUtil.getAddrInfo(Double.parseDouble(startLocation.split(",")[1]), Double.parseDouble(startLocation.split(",")[0]));//start
                    String sAddr=startLocationInfo.get("address");
                    String sDistrict=startLocationInfo.get("district");
                    String sTime= DateUtils4Vo.longToDateString(startTime,DateUtils4Vo.TIMEF_FORMAT);
                    String eTime= DateUtils4Vo.longToDateString(nowtime,DateUtils4Vo.TIMEF_FORMAT);
                    Map<String, String> nowLocationInfo = GeoUtil.getAddrInfo(Double.parseDouble(nowLocation.split(",")[1]), Double.parseDouble(nowLocation.split(",")[0]));//end
                    String eAddr=nowLocationInfo.get("address");
                    String eDistrict=nowLocationInfo.get("district");
                    String location=nowLocation;
                    int code=ccode;
                    String province=nowLocationInfo.get("province");
                    if(listArea.contains(province)){
                           code=Integer.parseInt(nowLocationInfo.get("cityCode").substring(0,2)+"0000");
                    }else {
                           code=Integer.parseInt(nowLocationInfo.get("cityCode"));
                    }

                    list.add(new TrackInfo(sAddr,sDistrict,sTime,eTime,eAddr,eDistrict,code,location,distance,str,startLocation,nowSoc));
                    //delKey.add(str);
                     jedisUtil.del(RedisConstants.SCOOTERTRACKER+str);
                   // delDetailKey.add(RedisConstants.SCOOTERTRACKER+str);
                     jedisUtil.hdel(RedisConstants.SCOOTERTRACKDISTANCEGET1000,str);
                }
            }



        }
        if(list.size()>0){
            LoghubService loghubService=new  LoghubService();
            loghubService.sendScooterTrack(list);
        }
        if(delKey.size()>0){
            String[] strings = new String[delKey.size()];
            jedisUtil.hdel(RedisConstants.SCOOTERTRACKDISTANCEGET1000,strings);
            System.out.println("==================end=========="+strings.length);
        }
        if(delDetailKey.size()>0){
            String[] strings = new String[delDetailKey.size()];
            jedisUtil.del(strings);
        }
        long end=System.currentTimeMillis();
        logger.info("cron " + count3 + " time " + (end-start)/1000);
    }

    public static void main(String[] args) {

    }
}
