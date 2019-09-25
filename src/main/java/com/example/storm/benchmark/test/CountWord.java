package com.example.storm.benchmark.test;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseWindowedBolt;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.windowing.TupleWindow;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class CountWord extends BaseWindowedBolt {

    private static final long serialVersionUID = -5283595260540124273L;
    private FileWriter writer = null;

    private OutputCollector collector;


    public void prepare(Map stormConf, TopologyContext context
            , OutputCollector collector) {
        this.collector = collector;
    }

    public void execute(TupleWindow inputWindow) {
        int sum = 0;
        for (Tuple tuple : inputWindow.getNew()) {
            int i = (Integer) tuple.getIntegerByField("count");
           // int i=0;
         //   String s = tuple.getString(0);

            System.out.println("接收到一个bolt的总和值为:" + i);
            sum += i;
        }
        System.out.println("一个窗口内的总值为:" + sum);
    }

    public void declareOutputFields(OutputFieldsDeclarer declarer) {
    }
}