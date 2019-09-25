package com.example.storm.benchmark.test;


import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class WordReader extends BaseRichSpout {
    private static final long serialVersionUID = 2197521792014017918L;
    private SpoutOutputCollector collector;
    private static AtomicInteger i = new AtomicInteger();
    private static String[] words = new String[] {"nathan", "mike", "jackson", "golda", "bertels" };

    @Override
    public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
        this.collector = collector;
        System.out.println("open");
    }

    @Override
    public void nextTuple() {
        if (i.intValue() < 100) {
            Random rand = new Random();
            String word = words[rand.nextInt(words.length)];
            collector.emit(new Values(word));
            i.incrementAndGet();
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("word"));
    }
}
