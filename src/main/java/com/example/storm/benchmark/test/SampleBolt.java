package com.example.storm.benchmark.test;

import com.aliyun.openservices.log.common.LogGroupData;
import com.aliyun.openservices.log.common.Logs;
import com.aliyun.openservices.log.exception.LogException;
import com.aliyun.openservices.loghub.stormspout.LogHubSpout;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SampleBolt extends BaseRichBolt {
    private static final long serialVersionUID = 4752656887774402264L;
    private static final Logger logger = LoggerFactory.getLogger(BaseBasicBolt.class);
    private OutputCollector mCollector;
    @Override
    public void prepare(@SuppressWarnings("rawtypes") Map stormConf, TopologyContext context,
                        OutputCollector collector) {
        mCollector = collector;
    }
    @Override
    public void execute(Tuple tuple) {
//        String shardId = (String) tuple
//                .getValueByField(LogHubSpout.FIELD_SHARD_ID);
     //   System.out.println("------------------");
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
                for (Logs.Log.Content content : log.getContentsList()) {
                    sb.append("\t").append(content.getKey()).append(":")
                            .append(content.getValue());
                }
                System.out.println(sb.toString());
                logger.info(sb.toString());
            }
        }
        // 在 loghub spout 中，强制依赖 storm 的 ack 机制，用于确认 spout 将消息正确
        // 发送至 bolt，所以在 bolt 中一定要调用 ack
      //  mCollector.ack(tuple);
    }
    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("count"));
        //do nothing
    }
}
