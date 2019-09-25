package com.example.storm.benchmark.test;



import java.util.Map;
import java.util.Random;

import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;
import org.apache.storm.utils.Utils;

/**
 * @author liyijie
 * @date 2018年6月13日下午8:32:24
 * @email 37024760@qq.com
 * @remark
 * @version
 */
public class WordCountSpout extends BaseRichSpout{

    private SpoutOutputCollector collector;

    public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
        this.collector = collector;
        System.out.println("init");
    }

    public static final String[] words = new String[]{"aaa","bbb","ccc","aa","bb","a"};

    /**
     * 1.把每一行数据发射出去
     * */
    public void nextTuple() {
        Random random = new Random();
        String word =words[random.nextInt(words.length)];	                    //获取文件中的每行内容
        //发射出去
        this.collector.emit(new Values(word));

    //    System.out.println("emit: "+word);

     //   Utils.sleep(1000L);
    }


    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("word"));
    }

}
