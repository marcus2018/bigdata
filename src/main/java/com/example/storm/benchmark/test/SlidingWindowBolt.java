package com.example.storm.benchmark.test;

import breeze.linalg.sum;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseWindowedBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;
import org.apache.storm.windowing.TupleWindow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

public class SlidingWindowBolt extends BaseWindowedBolt {
           private OutputCollector collector;

            @Override
            public void prepare(Map stormConf, TopologyContext context,
               OutputCollector collector) {
               this.collector = collector;
                System.out.println("im coming");
            }
           @Override
            public void execute(TupleWindow inputWindow) {
               // System.out.println("ha coming");
                int sum=0;
               List<Tuple> tuplesInWindow = inputWindow.get();
               List<Tuple> newTuples = inputWindow.getNew();
               List<Tuple> expiredTuples = inputWindow.getExpired();
                System.out.print("一个窗口内的数据");
//               for(Tuple tuple: inputWindow.getNew()) {
//                   int  str=(Integer)tuple.getValue(0);
//                   // int str=(Integer) tuple.getValueByField("intsmaze");
//                   //  System.out.print(" "+str);
//                   // int str=0;
//                   sum+=str;
//
//               System.out.print("======="+sum);
//              }
                for(Tuple tuple: inputWindow.getNew()) {
                       int  str=(Integer)tuple.getValue(0);
                       // int str=(Integer) tuple.getValueByField("intsmaze");
                      //  System.out.print(" "+str);
                   // int str=0;
                        sum+=str;
                    }
                System.out.println("======="+sum);
                collector.emit(new Values(sum));
            }

            @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
             declarer.declare(new Fields("count"));
            }

  }
