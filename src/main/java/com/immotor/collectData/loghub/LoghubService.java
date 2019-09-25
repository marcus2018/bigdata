package com.immotor.collectData.loghub;

import com.aliyun.openservices.log.common.LogItem;
import com.aliyun.openservices.log.exception.LogException;
import com.aliyun.openservices.log.producer.ILogCallback;
import com.aliyun.openservices.log.producer.LogProducer;
import com.aliyun.openservices.log.producer.ProducerConfig;
import com.aliyun.openservices.log.producer.ProjectConfig;
import com.aliyun.openservices.log.response.PutLogsResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LoghubService {
        private static Logger logger = LoggerFactory.getLogger(LoghubService.class);
        private String accessId="HmTjtVwGWaEvbDw5";
        private String accessKey="GEKUNH2wv7fen6LxQhH2655ZcgRvmd";
        private String endpoint="https://cn-shenzhen.log.aliyuncs.com";
        private String project="collectdata";//  elastic
        private String logstore="station_battery_detail";//station_battery_detail logstore_log
        private String logstore_log="station_battery_log";
        private String logstore_cabinetRecord="station_meterrecord";//station_battery_detail
        private String logstore_all="station_battery_all";//station_battery_detail

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


       public   void sendBatteryRealTimeInfo(String pid, String id, Date collecttime,String partreadonlydata,String partruncontroldata,String pt){
           List<LogItem> logItems = new ArrayList<LogItem>();

           LogItem logItem1 = new LogItem((int) (new Date().getTime() / 1000));
           logItem1.PushBack("pid", pid);
           logItem1.PushBack("id", id);
           logItem1.PushBack("collecttime",String.valueOf(collecttime.getTime()));
           logItem1.PushBack("partreadonlydata", partreadonlydata);
           logItem1.PushBack("partruncontroldata", partruncontroldata);
           logItem1.PushBack("pt", pt);
           logItems.add(logItem1);

          sendData(logstore,logItems);
          //producer.flush();

       }
        public void sendData(String logstore, List<LogItem> logItems){
              //  ILogCallback iLogCallback=new
            getLogProduce();
            producer.send(project,logstore,"","",logItems);
        }

    public void sendCabinetRercordInfo(String pid, Double meterrecord, String collecttime, String pt) {
        List<LogItem> logItems = new ArrayList<LogItem>();

        LogItem logItem1 = new LogItem((int) (new Date().getTime() / 1000));
        logItem1.PushBack("pid", pid);
        logItem1.PushBack("meterrecord", String.valueOf(meterrecord));
        logItem1.PushBack("collecttime",collecttime);
        logItem1.PushBack("pt", pt);
        logItems.add(logItem1);
        sendData(logstore_cabinetRecord,logItems);
    }










    public void sendLogHub(String json, Date date, String pt) {
        List<LogItem> logItems = new ArrayList<LogItem>();
        LogItem logItem1 = new LogItem((int) (new Date().getTime() / 1000));
        System.out.println(json+date+pt);
        logItem1.PushBack("content",json);
        logItem1.PushBack("collecttime",String.valueOf(date.getTime()));
        logItem1.PushBack("pt", pt);
        logItems.add(logItem1);
        sendData(logstore_all,logItems);

    }

    public void sendBatteryLogInfo(String pid, String id, Date collecttime, String logIndex, String partLog, String pt) {
        List<LogItem> logItems = new ArrayList<LogItem>();

        LogItem logItem1 = new LogItem((int) (new Date().getTime() / 1000));
        logItem1.PushBack("pid", pid);
        logItem1.PushBack("id", id);
        logItem1.PushBack("collecttime",String.valueOf(collecttime.getTime()));
        logItem1.PushBack("logIndex", logIndex);
        logItem1.PushBack("partLog", partLog);
        logItem1.PushBack("pt", pt);
        logItems.add(logItem1);

        sendData(logstore_log,logItems);
    }

}
