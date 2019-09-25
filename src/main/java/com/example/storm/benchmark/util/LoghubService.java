package com.example.storm.benchmark.util;

import com.aliyun.openservices.log.common.LogItem;
import com.aliyun.openservices.log.producer.LogProducer;
import com.aliyun.openservices.log.producer.ProducerConfig;
import com.aliyun.openservices.log.producer.ProjectConfig;
import com.example.storm.benchmark.produce.cabinet.cabinetFullPort.bean.FullStationStoE;
import com.example.storm.benchmark.produce.cabinet.cabinet_battery_abnormal.bean.BatteryMonitorTime;
import com.example.storm.benchmark.produce.cabinet.cabinet_battery_temp.bean.BatteryDamageHappenTime;
import com.example.storm.benchmark.produce.cabinet.cabinet_battery_temp.bean.BatteryHB3InStationTemp;
import com.example.storm.benchmark.produce.station_battery_damage_6min.BatteryMonitorTimeToLoghub;
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
        private String project="open-for-interface";//  elastic
        private String monitor_cabinet_battery_info="monitor_cabinet_battery_info";
        private String monitor_incabinet_battery_temp="monitor_incabinet_battery_temp";
        private  String monitor_cabinet_and_scooter_battery_damage="monitor_cabinet_and_scooter_battery_damage";
        private String monitor_incabinet_battery_damage_6min="monitor_station_battery_damage_6min";
        private String monitor_cabinet_full_and_lost_startEndTime="monitor_cabinet_full_and_lost_time";

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
              //  ILogCallback iLogCallback=new
            getLogProduce();
            producer.send(project,logstore,"","",logItems);
        }









    public void sendMonitorBatteryInfo(List<BatteryMonitorTime> list) {
        List<LogItem> logItems = new ArrayList<LogItem>();
        for (int i=0;i<list.size();i++){
            LogItem logItem1 = new LogItem((int) (new Date().getTime() / 1000));
            BatteryMonitorTime batteryMonitorTime=list.get(i);
            logItem1.PushBack("pid", batteryMonitorTime.getPid());
            logItem1.PushBack("id", batteryMonitorTime.getId());
            logItem1.PushBack("collecttime",String.valueOf(batteryMonitorTime.getDate().getTime()));
            logItem1.PushBack("soh", batteryMonitorTime.getSoh()+"");
            logItem1.PushBack("cycle", batteryMonitorTime.getCycle()+"");
            logItem1.PushBack("highTempWarning", batteryMonitorTime.getHighTempWarning()+"");
            logItem1.PushBack("lowTempWarning", batteryMonitorTime.getLowTempWarning()+"");
            logItem1.PushBack("overChargingWarning",batteryMonitorTime.getOverChargingWarning()+"");
            logItem1.PushBack("overDisChargingWarning", batteryMonitorTime.getOverDisChargingWarning()+"");
            logItem1.PushBack("chargingMOS", batteryMonitorTime.getChargingMOS()+"");
            logItem1.PushBack("disChargingMOS", batteryMonitorTime.getDisChargingMOS()+"");
            logItem1.PushBack("overVoltage",batteryMonitorTime.getOverVoltage()+"");
            logItem1.PushBack("overfall", batteryMonitorTime.getOverfall()+"");
            //if(batteryMonitorTime.getCode()!="") {
                logItem1.PushBack("code", batteryMonitorTime.getCode() + "");
            //}
            logItems.add(logItem1);
        }


        sendData(monitor_cabinet_battery_info,logItems);
    }

    public void batteryHB3InStationTempList(List<BatteryHB3InStationTemp> batteryHB3InStationTempList) {
        List<LogItem> logItems = new ArrayList<LogItem>();
        for (int i = 0; i< batteryHB3InStationTempList.size(); i++){
            LogItem logItem1 = new LogItem((int) (new Date().getTime() / 1000));
            BatteryHB3InStationTemp batteryMonitorTime=batteryHB3InStationTempList.get(i);
            logItem1.PushBack("pid", batteryMonitorTime.getPid());
            logItem1.PushBack("id", batteryMonitorTime.getId());
            logItem1.PushBack("collecttime",String.valueOf(batteryMonitorTime.getDate().getTime()));
            logItem1.PushBack("port", batteryMonitorTime.getPort()+"");
            logItem1.PushBack("temp", batteryMonitorTime.getTemp()+"");
            logItem1.PushBack("code", batteryMonitorTime.getCode() + "");
            logItems.add(logItem1);
        }
        sendData(monitor_incabinet_battery_temp,logItems);
    }

    public void batteryDamageHappenTimeList(List<BatteryDamageHappenTime> batteryDamageHappenTimeList) {
        List<LogItem> logItems = new ArrayList<LogItem>();
        for (int i = 0; i< batteryDamageHappenTimeList.size(); i++){
            LogItem logItem1 = new LogItem((int) (new Date().getTime() / 1000));
            BatteryDamageHappenTime batteryDamageHappenTime=batteryDamageHappenTimeList.get(i);
            logItem1.PushBack("kID", batteryDamageHappenTime.getPid());
            logItem1.PushBack("bid", batteryDamageHappenTime.getBid());
            logItem1.PushBack("happenTime",String.valueOf(batteryDamageHappenTime.getHappenTime()));
            logItem1.PushBack("port", batteryDamageHappenTime.getP()+"");
            logItem1.PushBack("bSn", batteryDamageHappenTime.getbSn());
            logItem1.PushBack("vol", batteryDamageHappenTime.getV()+"");
            logItem1.PushBack("temp", batteryDamageHappenTime.getT() + "");
            logItem1.PushBack("soh", batteryDamageHappenTime.getSoh()+"");
            logItem1.PushBack("soc", batteryDamageHappenTime.getSoc() + "");
            logItem1.PushBack("sn", batteryDamageHappenTime.getSn()+"");
            logItem1.PushBack("name", batteryDamageHappenTime.getName()+"");
            logItem1.PushBack("damage", batteryDamageHappenTime.getDamage() + "");
            logItem1.PushBack("cycle", batteryDamageHappenTime.getCycle()+"");
            logItem1.PushBack("current", batteryDamageHappenTime.getCurrent()+"");
            logItem1.PushBack("cityCode", batteryDamageHappenTime.getCityCode() + "");
            logItem1.PushBack("type",1 + "");
            logItems.add(logItem1);
        }
        sendData(monitor_cabinet_and_scooter_battery_damage,logItems);
    }

    public void sendMonitorBatteryDetail(List<BatteryMonitorTimeToLoghub> list) {
        List<LogItem> logItems = new ArrayList<LogItem>();
        for (int i = 0; i< list.size(); i++){
            LogItem logItem1 = new LogItem((int) (new Date().getTime() / 1000));
            BatteryMonitorTimeToLoghub batteryMonitorTimeToLoghub=list.get(i);
            System.out.println(batteryMonitorTimeToLoghub.toString());
            logItem1.PushBack("pid", batteryMonitorTimeToLoghub.getPid());
            logItem1.PushBack("bid", batteryMonitorTimeToLoghub.getBid());
            logItem1.PushBack("happenTime",String.valueOf(batteryMonitorTimeToLoghub.getDate().getTime()));
            logItem1.PushBack("bSn", batteryMonitorTimeToLoghub.getbSn()+"");

          //  logItem1.PushBack("content",batteryMonitorTimeToLoghub.getContent()+"");
            if(batteryMonitorTimeToLoghub.getStationInfo()!=null) {
                logItem1.PushBack("name",batteryMonitorTimeToLoghub.getStationInfo().getName()+"");
                logItem1.PushBack("sn", batteryMonitorTimeToLoghub.getStationInfo().getSn() + "");

                logItem1.PushBack("cityCode", batteryMonitorTimeToLoghub.getStationInfo().getCityCode() + "");
            }else{
                logItem1.PushBack("sn", -1+"");
                logItem1.PushBack("name", -1+"");
                logItem1.PushBack("cityCode",-1+ "");
            }
            logItem1.PushBack("damage", "damage_"+batteryMonitorTimeToLoghub.getDamagetype()+"_" +batteryMonitorTimeToLoghub.getBitType());
            logItems.add(logItem1);
        }
        sendData(monitor_incabinet_battery_damage_6min,logItems);
    }

    public void sendFullStationStoE(List<FullStationStoE> fullStationStoEList) {
        {
            List<LogItem> logItems = new ArrayList<LogItem>();
            for (int i = 0; i< fullStationStoEList.size(); i++){
                LogItem logItem1 = new LogItem((int) (new Date().getTime() / 1000));
                FullStationStoE fullStationStoE=fullStationStoEList.get(i);
                logItem1.PushBack("pID", fullStationStoE.getPid());
                logItem1.PushBack("sTime", fullStationStoE.getsTime()+"");
                logItem1.PushBack("eTime", fullStationStoE.geteTime()+"");
                logItem1.PushBack("duration", fullStationStoE.getDuration()+"");
                logItem1.PushBack("type",fullStationStoE.getStatus() + "");
                logItems.add(logItem1);
            }
            sendData(monitor_cabinet_full_and_lost_startEndTime,logItems);
        }
    }
}
