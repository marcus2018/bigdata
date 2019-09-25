package com.example.storm.benchmark.test;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.topology.base.BaseWindowedBolt;
import org.apache.storm.topology.base.BaseWindowedBolt;
import org.apache.storm.topology.base.BaseWindowedBolt.Count;
import java.util.concurrent.TimeUnit;

public class WindowsTopology {

    public static void main(String[] args) throws Exception {
              TopologyBuilder builder = new TopologyBuilder();
              builder.setSpout("spout", new RandomSentenceSpout(), 1);
//              builder.setBolt("slidingsum", new SlidingWindowSumBolt_back().withWindow(Count.of(30), Count.of(10)), 1)
//                .shuffleGrouping("spout");
              builder.setBolt("slidingwindowbolt", new SlidingWindowBolt()
              .withWindow(new BaseWindowedBolt.Duration(6, TimeUnit.SECONDS)   ,           new BaseWindowedBolt.Duration(2, TimeUnit.SECONDS)),2)
               .shuffleGrouping("spout");//每两秒统计最近6秒的数据

               builder.setBolt("countwordbolt", new CountWord()
               .withWindow(new BaseWindowedBolt.Count(2), new BaseWindowedBolt.Count(2)),1)
               .shuffleGrouping("slidingwindowbolt");
               //每收到2条tuple就统计最近两条统的数据
               Config conf = new Config();
               conf.setNumWorkers(1);
               conf.setDebug(true);
              // conf.setMaxSpoutPending(1);
               LocalCluster cluster = new LocalCluster();
               System.out.println("start.........");
               cluster.submitTopology("word-count", conf, builder.createTopology());
               System.out.println("end.........");
          //     Thread.sleep(1000*1000*60);
//
           }

}
