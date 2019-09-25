package com.example.storm.benchmark.produce;

import com.aliyun.openservices.log.common.LogGroupData;
import com.aliyun.openservices.log.common.Logs;
import com.aliyun.openservices.log.exception.LogException;
import com.aliyun.openservices.loghub.stormspout.LogHubSpout;
import com.example.storm.bean.CabinetHBTime;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseWindowedBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;
import org.apache.storm.windowing.TupleWindow;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SlidingWindowBolt extends BaseWindowedBolt {
           private OutputCollector collector;
          private SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
            public void prepare(Map stormConf, TopologyContext context,
               OutputCollector collector) {
               this.collector = collector;
            }
           @Override
            public void execute(TupleWindow inputWindow) {
                //List list = new ArrayList<>();
                Map<String,CabinetHBTime> listMap=new HashMap<>();

              /* List<Tuple> tuplesInWindow = inputWindow.get();
               List<Tuple> newTuples = inputWindow.getNew();
               List<Tuple> expiredTuples = inputWindow.getExpired();*/



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
                           CabinetHBTime cabinetHBTime=new CabinetHBTime();

                           for (Logs.Log.Content content : log.getContentsList()) {
                              if(content.getKey().equals("pID")){
                                   sb.append("\t").append(content.getKey()).append(":")
                                           .append(content.getValue());
                                        cabinetHBTime.setPid(content.getValue());
                                   }
                               if(content.getKey().equals("type")){
                                   sb.append("\t").append(content.getKey()).append(":")
                                           .append(content.getValue());
                                   cabinetHBTime.setType(Integer.parseInt(content.getValue()));
                               }
                               if(content.getKey().equals("time1")){
                                   sb.append("\t").append(content.getKey()).append(":")
                                           .append(content.getValue());

                                   try {
                                       cabinetHBTime.setDate( sdf.parse(content.getValue()));
                                       System.out.println("-------------------"+sdf.parse(content.getValue()));
                                   } catch (ParseException e) {

                                   }

                               }


                           }
                         /*  if(cabinetHBTime.getType()!=2)
                               continue;
                           collector.emit(new Values(cabinetHBTime.getPid(),cabinetHBTime));
                    */
                         if(cabinetHBTime.getType()!=2)
                                continue;


                           if(listMap.containsKey(cabinetHBTime.getPid())){
                               CabinetHBTime cabinetHBTime1=listMap.get(cabinetHBTime.getPid());
                               if(cabinetHBTime.getDate().getTime()-cabinetHBTime1.getDate().getTime()>0)
                                listMap.put(cabinetHBTime.getPid(),cabinetHBTime);
                           }else{

                               listMap.put(cabinetHBTime.getPid(),cabinetHBTime);
                           }

                       }
                   }



               }
              collector.emit(new Values(listMap));


            //   counters.clear();
             //  list.clear();


            }

            @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
             declarer.declare(new Fields("count"));
            }

  }
