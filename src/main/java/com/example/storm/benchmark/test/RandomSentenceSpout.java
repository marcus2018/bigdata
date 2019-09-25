package com.example.storm.benchmark.test;

import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;

import java.util.Map;

public class RandomSentenceSpout extends BaseRichSpout {

      private static final long serialVersionUID = 5028304756439810609L;

      private SpoutOutputCollector collector;

      int intsmaze=0;
       @Override
      public void declareOutputFields(OutputFieldsDeclarer declarer) {
           declarer.declare(new Fields("intsmaze"));
       }
       @Override
       public void open(Map conf, TopologyContext context,
                        SpoutOutputCollector collector) {
           this.collector = collector;
       }
            @Override
            public void nextTuple() {
                System.out.println("发送数据:"+intsmaze);
                collector.emit(new Values(intsmaze++));
                try {
                     Thread.sleep(2000);
                   // Thread.sleep(1000);
                 } catch (InterruptedException e) {
                     e.printStackTrace();
                 }
            }
}
