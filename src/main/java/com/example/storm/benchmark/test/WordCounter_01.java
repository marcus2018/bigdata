package com.example.storm.benchmark.test;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.IRichBolt;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

public class WordCounter_01 implements IRichBolt {
   // public class WordCounter implements IRichBolt {
    private static final long serialVersionUID = 5683648523524179434L;
    private static Map<String, Integer> counters = new ConcurrentHashMap<String, Integer>();
    private volatile boolean edit = true;

    @Override
    public void prepare(final Map stormConf, TopologyContext context, final OutputCollector collector) {

    }

    @Override
    public void execute(Tuple tuple) {
        System.out.println("lala............");
    }

    private static class ValueComparator implements Comparator<Entry<String, Integer>> {
        @Override
        public int compare(Entry<String, Integer> entry1, Entry<String, Integer> entry2) {
            return entry2.getValue() - entry1.getValue();
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("word_count"));
    }

    @Override
    public void cleanup() {
    }

    @Override
    public Map<String, Object> getComponentConfiguration() {
        return null;
    }
}
