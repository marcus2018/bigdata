package com.example.storm.benchmark.produce.cabinet.cabinetLogInfoIndex;

import com.example.storm.benchmark.produce.cabinet.cabinetLogInfoIndex.bean.BatteryLogInfo;
import com.example.storm.benchmark.produce.cabinet.cabinetLogInfoIndex.bean.BatteryLogOmitInfo;
import org.apache.storm.command.list;

import java.util.*;

public class Test {
    public  static final String MAXINDEX="maxIndex";
    public  static final String MAX="max";
    public  static final String MIN="min";
    public static void main(String[] args) {

        List<BatteryLogInfo> listBatteryLogInfo=new ArrayList<>();
        listBatteryLogInfo.add(new BatteryLogInfo("30,25,8A,94,79,2C",13,"00,00,19,06,06,11"));
        listBatteryLogInfo.add(new BatteryLogInfo("30,25,8A,94,79,2C",14,"00,00,19,06,06,11"));
        listBatteryLogInfo.add(new BatteryLogInfo("30,25,8A,94,79,2C",15,"00,00,19,06,06,11"));
        listBatteryLogInfo.add(new BatteryLogInfo("30,25,8A,94,79,2C",19,"00,00,19,06,06,11"));
        listBatteryLogInfo.add(new BatteryLogInfo("30,25,8A,94,79,2C",20,"00,00,19,06,06,11"));
        listBatteryLogInfo.add(new BatteryLogInfo("30,25,8A,94,79,2C",21,"00,00,19,06,06,11"));
        listBatteryLogInfo.add(new BatteryLogInfo("30,25,8A,94,79,2C",22,"00,00,19,06,06,11"));
        listBatteryLogInfo.add(new BatteryLogInfo("30,25,8A,94,79,2C",23,"00,00,19,06,06,11"));
        listBatteryLogInfo.add(new BatteryLogInfo("30,25,8A,94,79,2C",24,"00,00,19,06,06,11"));
        listBatteryLogInfo.add(new BatteryLogInfo("30,25,8A,94,79,2C",25,"00,00,19,06,06,11"));
        listBatteryLogInfo.add(new BatteryLogInfo("30,25,8A,94,79,2C",26,"00,00,19,06,06,11"));
        listBatteryLogInfo.add(new BatteryLogInfo("30,25,8A,94,79,2C",27,"00,00,19,06,06,11"));
        listBatteryLogInfo.add(new BatteryLogInfo("30,25,8A,94,79,2C",28,"00,00,19,06,06,11"));
        listBatteryLogInfo.add(new BatteryLogInfo("30,25,8A,94,79,2C",39,"00,00,19,06,06,11"));
        listBatteryLogInfo.add(new BatteryLogInfo("30,25,8A,94,79,2C",30,"00,00,19,06,06,11"));
        listBatteryLogInfo.add(new BatteryLogInfo("30,25,8A,94,79,2C",31,"00,00,19,06,06,11"));
        listBatteryLogInfo.add(new BatteryLogInfo("30,25,8A,94,79,2C",32,"00,00,19,06,06,11"));
        listBatteryLogInfo.add(new BatteryLogInfo("30,25,8A,94,79,2C",33,"00,00,19,06,06,11"));
        listBatteryLogInfo.add(new BatteryLogInfo("30,25,8A,94,79,2C",34,"00,00,19,06,06,11"));
        listBatteryLogInfo.add(new BatteryLogInfo("30,25,8A,94,79,2C",35,"00,00,19,06,06,11"));
        listBatteryLogInfo.add(new BatteryLogInfo("30,25,8A,94,79,2C",36,"00,00,19,06,06,11"));
        listBatteryLogInfo.add(new BatteryLogInfo("30,25,8A,94,79,2C",37,"00,00,19,06,06,11"));
        listBatteryLogInfo.add(new BatteryLogInfo("30,25,8A,94,79,2C",38,"00,00,19,06,06,11"));
        listBatteryLogInfo.add(new BatteryLogInfo("30,25,8A,94,79,2C",40,"00,00,19,06,06,11"));
        listBatteryLogInfo.add(new BatteryLogInfo("30,25,8A,94,79,2C",41,"00,00,19,06,06,11"));
        listBatteryLogInfo.add(new BatteryLogInfo("30,25,8A,94,79,2C",42,"00,00,19,06,06,11"));
        listBatteryLogInfo.add(new BatteryLogInfo("30,25,8A,94,79,2C",43,"00,00,19,06,06,11"));
        listBatteryLogInfo.add(new BatteryLogInfo("30,25,8A,94,79,2C",44,"00,00,19,06,06,11"));
        listBatteryLogInfo.add(new BatteryLogInfo("30,25,8A,94,79,2C",45,"00,00,19,06,06,11"));
        listBatteryLogInfo.add(new BatteryLogInfo("30,25,8A,94,79,2C",48,"00,00,19,06,06,11"));
        listBatteryLogInfo.add(new BatteryLogInfo("30,25,8A,94,79,2C",49,"00,00,19,06,06,11"));
        listBatteryLogInfo.add(new BatteryLogInfo("30,25,8A,94,79,2C",17,"00,00,19,06,06,11"));
        listBatteryLogInfo.add(new BatteryLogInfo("30,25,8A,94,79,2C",18,"00,00,19,06,06,11"));
        //listBatteryLogInfo.add(new BatteryLogInfo("30,25,8A,94,79,2C",24,"00,00,19,06,06,11"));
        listBatteryLogInfo.add(new BatteryLogInfo("30,25,8A,94,79,2C",29,"00,00,19,06,06,11"));
      //  listBatteryLogInfo.add(new BatteryLogInfo("30,25,8A,94,79,2C",50,"00,00,19,06,06,11"));
        listBatteryLogInfo.add(new BatteryLogInfo("30,25,8A,94,79,2C",51,"00,00,19,06,06,11"));
        listBatteryLogInfo.add(new BatteryLogInfo("30,25,8A,94,79,2C",52,"00,00,19,06,06,11"));
        listBatteryLogInfo.add(new BatteryLogInfo("30,25,8A,94,79,2C",55,"00,00,19,06,06,11"));
        String str="30,25,8A,94,79,2C|50|00,00,19,06,06,11" +
                "|29-9-00,00,19,06,06,11-00,00,19,06,06,11:" +
                "24-4-00,00,19,06,06,11-00,00,19,06,06,11:" +
                "18-5-00,00,19,06,06,11-00,00,19,06,06,11:" +
                "45-2-00,00,19,06,06,11-00,00,19,06,06,11:" +
                "12-3-00,00,19,06,06,11-00,00,19,06,06,11";

            String redisBid=str.split("\\|")[0];
            String redisEventType=str.split("\\|")[1];
            String redisTime=str.split("\\|")[2];
            String redisOmit=str.split("\\|")[3];
            String[] redisArray = redisOmit.split(":");
            listBatteryLogInfo.add(new BatteryLogInfo(redisBid, Long.parseLong(redisEventType), redisTime,CollectData_batteryLogData.MAXINDEX));

            for(int j=0;j<redisArray.length;j++) {
                String[] redisArrayElement=redisArray[j].split("-");
                listBatteryLogInfo.add(new BatteryLogInfo(redisBid, Long.parseLong(redisArrayElement[0]), redisArrayElement[2],CollectData_batteryLogData.MIN));//遗漏最小游标
                listBatteryLogInfo.add(new BatteryLogInfo(redisBid, Long.parseLong(redisArrayElement[0])+Long.parseLong(redisArrayElement[1])+1, redisArrayElement[3],CollectData_batteryLogData.MAX));//遗漏最大游标
                //    mapListBatteryLogInfo.put(redisBid,listBatteryLogInfo);
            }


     listBatteryLogInfo.add(new BatteryLogInfo("30,25,8A,94,79,2C1",12,"00,00,19,06,06,11"));
        listBatteryLogInfo.add(new BatteryLogInfo("30,25,8A,94,79,2C1",16,"00,00,19,06,06,11"));
        listBatteryLogInfo.add(new BatteryLogInfo("30,25,8A,94,79,2C1",17,"00,00,19,06,06,11"));
        listBatteryLogInfo.add(new BatteryLogInfo("30,25,8A,94,79,2C1",18,"00,00,19,06,06,11"));
        listBatteryLogInfo.add(new BatteryLogInfo("30,25,8A,94,79,2C1",24,"00,00,19,06,06,11"));
        listBatteryLogInfo.add(new BatteryLogInfo("30,25,8A,94,79,2C1",29,"00,00,19,06,06,11"));
        listBatteryLogInfo.add(new BatteryLogInfo("30,25,8A,94,79,2C1",28,"00,00,19,06,06,11"));
        listBatteryLogInfo.add(new BatteryLogInfo("30,25,8A,94,79,2C1",39,"00,00,19,06,06,11"));
        listBatteryLogInfo.add(new BatteryLogInfo("30,25,8A,94,79,2C1",40,"00,00,19,06,06,11"));
        listBatteryLogInfo.add(new BatteryLogInfo("30,25,8A,94,79,2C1",40,"00,00,19,06,06,11"));
        listBatteryLogInfo.add(new BatteryLogInfo("30,25,8A,94,79,2b",12,"00,00,19,06,06,11"));
        listBatteryLogInfo.add(new BatteryLogInfo("30,25,8A,94,79,2b",16,"00,00,19,06,06,11"));
        listBatteryLogInfo.add(new BatteryLogInfo("30,25,8A,94,79,2b",17,"00,00,19,06,06,11"));
        listBatteryLogInfo.add(new BatteryLogInfo("30,25,8A,94,79,2b",18,"00,00,19,06,06,11"));
        listBatteryLogInfo.add(new BatteryLogInfo("30,25,8A,94,79,2b",24,"00,00,19,06,06,11"));
        listBatteryLogInfo.add(new BatteryLogInfo("30,25,8A,94,79,2b",29,"00,00,19,06,06,11"));
        listBatteryLogInfo.add(new BatteryLogInfo("30,25,8A,94,79,2b",39,"00,00,19,06,06,11"));
        listBatteryLogInfo.add(new BatteryLogInfo("30,25,8A,94,79,2b",40,"00,00,19,06,06,11"));
        listBatteryLogInfo.add(new BatteryLogInfo("30,25,8A,94,79,2b",40,"00,00,19,06,06,11"));
        Map<String,List<BatteryLogInfo>> mapListBatteryLogInfo=new HashMap<>();
        Map<String,String> insertMap=new HashMap<>();
        Collections.sort(listBatteryLogInfo);
        for (int i=0;i<listBatteryLogInfo.size();i++){
          BatteryLogInfo batteryLogInfo=   listBatteryLogInfo.get(i);
            System.out.println(batteryLogInfo.getBid()+" "+batteryLogInfo.getEventType()+" "+batteryLogInfo.getTime());
        }
        System.out.println("==============");
        String omitIndex="-1";

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
                if(diff>1&&((!CollectData_batteryLogData.MAXINDEX.equals(listBatteryLogInfo.get(i+1).getOmitLogIndex()))
                        ||(!CollectData_batteryLogData.MAX.equals(listBatteryLogInfo.get(i).getOmitLogIndex())&&
                        !CollectData_batteryLogData.MIN.equals(listBatteryLogInfo.get(i+1).getOmitLogIndex())))){
                  //开始游标，结束游标，开始时间，结束时间
                  List<BatteryLogOmitInfo> list=new ArrayList();
                  list.add(new BatteryLogOmitInfo( listBatteryLogInfo.get(i).getEventType(), listBatteryLogInfo.get(i+1).getEventType(),listBatteryLogInfo.get(i).getTime(),listBatteryLogInfo.get(i+1).getTime()));//遗漏的type
                   if(mapOmit.containsKey(bid1)){
                      list.addAll(mapOmit.get(bid1));
                   }
              //    System.out.println(bid1+"==="+ listBatteryLogInfo.get(i).getEventType()+" "+ listBatteryLogInfo.get(i+1).getEventType()+""+listBatteryLogInfo.get(i).getTime()+" "+listBatteryLogInfo.get(i+1).getTime());
                  mapOmit.put(bid1,list);
              }
            }else {
              //  list.clear();
                mapMaxIndexTime.put(bid1,bid1+"|"+listBatteryLogInfo.get(i).getEventType()+"|"+listBatteryLogInfo.get(i).getTime());//最大的type and time
              //  System.out.println(bid1+"==="+listBatteryLogInfo.get(i).getEventType()+"|"+listBatteryLogInfo.get(i).getTime());
                continue;
            }

            if(i==listBatteryLogInfo.size()-2){//最后电池

                    if(bid1.equals(listBatteryLogInfo.get(i+1).getBid())){//是一个电池 ，取出最后一次游标计作最大游标
                     //   list.clear();
                        mapMaxIndexTime.put(bid1,bid1+"|"+listBatteryLogInfo.get(i+1).getEventType()+"|"+listBatteryLogInfo.get(i+1).getTime());//最大的type and time
                     //   mapMaxIndexTime.put(listBatteryLogInfo.get(i+1).getBid(),
                     //           listBatteryLogInfo.get(i+1).getBid()+"|"+listBatteryLogInfo.get(i+1).getEventType()+"|"+listBatteryLogInfo.get(i+1).getTime());//最大的type and time
                    }else {//不相等 最后一条记录为单独电池
                        mapMaxIndexTime.put(listBatteryLogInfo.get(i+1).getBid(),listBatteryLogInfo.get(i+1).getBid()+"|"+listBatteryLogInfo.get(i+1).getEventType()+"|"+listBatteryLogInfo.get(i+1).getTime());
                    }

            }
        }
        System.out.println("-----------------");
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
                 //  System.out.println(key+"    "+sb.toString());
               }else{
                   //默认-1
                   sb.append("-1");
               }
            System.out.println(key+" "+value+"|"+sb.toString());
             //  insertMap.put(key,value+"|"+ sb.toString());



        }
    }
}
