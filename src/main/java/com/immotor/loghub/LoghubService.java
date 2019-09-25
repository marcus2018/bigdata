package com.immotor.loghub;

import com.aliyun.openservices.log.common.LogItem;
import com.aliyun.openservices.log.producer.LogProducer;
import com.aliyun.openservices.log.producer.ProducerConfig;
import com.aliyun.openservices.log.producer.ProjectConfig;
import com.immotor.bean.TrackInfo;
import com.immotor.util.DateUtils4Vo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LoghubService {
        private static Logger logger = LoggerFactory.getLogger(LoghubService.class);
        private String accessId="HmTjtVwGWaEvbDw5";
        private String accessKey="GEKUNH2wv7fen6LxQhH2655ZcgRvmd";
        private String endpoint="https://cn-shenzhen.log.aliyuncs.com";
       // private String endpoint="https://cn-shenzhen-intranet.log.aliyuncs.com";
        private String project="open-for-interface";
        private String logstore="monitor_scooter_track";

    private volatile static LogProducer producer;

        public   LogProducer   getLogProduce(){
                if(producer==null){
                   synchronized (LoghubService.class) {
                           if (producer == null) {
                              producer=new LogProducer(new ProducerConfig());
                              producer.setProjectConfig(new ProjectConfig(project,endpoint,accessId,accessKey));
                           }
                   }
                }
                return  producer;
        }



        public void sendData(String logstore, List<LogItem> logItems){

            getLogProduce();
            producer.send(project,logstore,"","",logItems);
        }


    public void sendScooterTrack(List<TrackInfo> list) {
        List<LogItem> logItems = new ArrayList<LogItem>();
        for(int i=0;i<list.size();i++) {
            TrackInfo trackInfo=list.get(i);
            LogItem logItem1 = new LogItem((int) (new Date().getTime() / 1000));
            logItem1.PushBack("sAddr", trackInfo.getsAddr());
            logItem1.PushBack("sDistrict", trackInfo.getsDistrict());
            logItem1.PushBack("sTime", trackInfo.getsTime());
            logItem1.PushBack("eTime", trackInfo.geteTime());
            logItem1.PushBack("eAddr", trackInfo.geteAddr());
            logItem1.PushBack("eDistrict", trackInfo.geteDistrict());
            logItem1.PushBack("code", trackInfo.getCode() + "");
            logItem1.PushBack("location", trackInfo.getLocation());
            logItem1.PushBack("distance", trackInfo.getDistance());
            logItem1.PushBack("sID", trackInfo.getStr());
            logItem1.PushBack("sLocation", trackInfo.getsLocation());
            logItem1.PushBack("nowSoc", trackInfo.getSoc()+"");
            logItem1.PushBack("pt",  trackInfo.geteTime().substring(0,10));
            logItems.add(logItem1);
        }
        sendData(logstore,logItems);
        }
}
