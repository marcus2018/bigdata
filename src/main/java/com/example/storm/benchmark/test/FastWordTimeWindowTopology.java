package com.example.storm.benchmark.test;

import org.apache.flink.api.common.time.Time;
import org.apache.spark.sql.catalyst.expressions.TimeWindow;
import org.apache.storm.Config;
import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.IRichBolt;
import org.apache.storm.topology.IRichSpout;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.topology.base.BaseWindowedBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class FastWordTimeWindowTopology {
   /* private static final Logger LOG = LoggerFactory.getLogger(TopStrom.class);

    public static class FastRandomSentenceSpout implements IRichSpout {
        SpoutOutputCollector _collector;
        Random _rand;
        long startTime;
        long sentNum = 0;
        long maxSendNum;
        int index = 0;

        private static final String[] CHOICES = {
                "JStorm is a distributed and fault-tolerant realtime computation system.",
                "Whenever a worker process crashes, ",
                "the scheduler embedded in the JStorm instance immediately spawns a new worker process to take the place of the failed one.",
                " The Acking framework provided by JStorm guarantees that every single piece of data will be processed at least once."};

        @Override
        public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
            _collector = collector;
            _rand = new Random();
            startTime = System.currentTimeMillis();
            maxSendNum = JStormUtils.parseLong(conf.get("max.send.num"), 1000L);
        }

        @Override
        public void nextTuple() {
            if (sentNum >= maxSendNum) {
                JStormUtils.sleepMs(1);
                return;
            }

            sentNum++;
            String sentence = CHOICES[index++];
            if (index >= CHOICES.length) {
                index = 0;
            }
            _collector.emit(new Values(sentence));
            JStormUtils.sleepMs(10);
        }

        @Override
        public void declareOutputFields(OutputFieldsDeclarer declarer) {
            declarer.declare(new Fields("sentence"));
        }

        // ...
    }

    public static class SplitSentence implements IRichBolt {
        OutputCollector collector;

        @Override
        public void execute(Tuple tuple) {
            String sentence = tuple.getString(0);
            for (String word : sentence.split("\\s+")) {
                collector.emit(new Values(word));
            }
        }

        @Override
        public void declareOutputFields(OutputFieldsDeclarer declarer) {
            declarer.declare(new Fields("word"));
        }

        // ...
    }

    // window的实现，注意它从BaseWindowedBolt派生，因为构造window时需要BaseWindowedBolt。
    public static class WordCount extends BaseWindowedBolt<Tuple> {
        OutputCollector collector;

        @Override
        public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
            this.collector = collector;
        }

        // 初始化window状态，对于word count的例子，我们需要的是一个word -> count的map
        @Override
        public Object initWindowState() {
            return new HashMap<>();
        }

        // 执行消息，更新word count
        @Override
        public void execute(Tuple tuple, Object state, TimeWindow window) {
            Map<String, Integer> counts = (Map<String, Integer>) state;
            String word = tuple.getString(0);
            Integer count = counts.get(word);
            if (count == null)
                count = 0;
            counts.put(word, ++count);
        }

        // purge窗口。这里我们就简单地打印出这个窗口所有消息的word count。
        @Override
        public void purgeWindow(Object state, TimeWindow window) {
            Map<String, Integer> counts = (Map<String, Integer>) state;
            System.out.println("purging window: " + window);
            System.out.println("=============================");
            for (Map.Entry<String, Integer> entry : counts.entrySet()) {
                System.out.println("word: " + entry.getKey() + "\tcount: " + entry.getValue());
            }
            System.out.println("=============================");
            System.out.println();
        }
    }

    static Config conf = JStormHelper.getConfig(null);

    public static void test() {
        TopologyBuilder builder = new TopologyBuilder();
        builder.setSpout("spout", new FastRandomSentenceSpout(), 1);
        builder.setBolt("split", new SplitSentence(), 1).shuffleGrouping("spout");

        // 构造一个大小为1分钟，每隔500ms滑动一次的窗口
        builder.setBolt("count", new WordCount()
                        .timeWindow(Time.seconds(1L), Time.milliseconds(500L)),
                1).fieldsGrouping("split", new Fields("word"));

        String[] className = Thread.currentThread().getStackTrace()[1].getClassName().split("\\.");
        String topologyName = className[className.length - 1];
        JStormHelper.runTopology(builder.createTopology(), topologyName, conf, 60,
                new JStormHelper.CheckAckedFail(conf), true);
    }

    public static void main(String[] args) throws Exception {
        conf = JStormHelper.getConfig(args);
        test();
    }*/
}