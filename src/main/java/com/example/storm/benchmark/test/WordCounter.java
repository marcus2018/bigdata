package com.example.storm.benchmark.test;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.IRichBolt;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;



import java.util.*;

public class WordCounter implements IRichBolt {
   // public class WordCounter implements IRichBolt {
    private static final long serialVersionUID = 5683648523524179434L;
    private static Map<String, Integer> counters = new ConcurrentHashMap<String, Integer>();
    private volatile boolean edit = true;

    @Override
    public void prepare(final Map stormConf, TopologyContext context, final OutputCollector collector) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    //5秒后counter不再变化，可以认为spout已经发送完毕
                    if (!edit) {
                        if (counters.size() > 0) {
                            List<Entry<String, Integer>> list = new ArrayList<Entry<String, Integer>>();
                            list.addAll(counters.entrySet());
                            Collections.sort(list, new ValueComparator());

                            //向下一个bolt发送前N个word
                            for (int i = 0; i < list.size(); i++) {
                               // System.out.println("pre="+stormConf.get("N").toString());
                                if (i < Integer.parseInt(stormConf.get("N").toString())) {
                                    collector.emit(new Values(list.get(i).getKey() + ":" + list.get(i).getValue()));
                                    System.out.println(new Values(list.get(i).getKey() + ":" + list.get(i).getValue()));
                                }
                            }
                        }

                        //发送之后，清空counters，以防spout再次发送word过来
                        counters.clear();
                    }

                    edit = false;
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    @Override
    public void execute(Tuple tuple) {
        String str = tuple.getString(0);
        System.out.println("tuple="+tuple.getStringByField("word"));
        if (counters.containsKey(str)) {
            Integer c = counters.get(str) + 1;
            counters.put(str, c);
            System.out.println("execute:"+str+" "+c);
        } else {
            counters.put(str, 1);
            System.out.println("execute:"+str+" "+1);
        }

        edit = true;
    }

    private static class ValueComparator implements Comparator<Map.Entry<String, Integer>> {
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
