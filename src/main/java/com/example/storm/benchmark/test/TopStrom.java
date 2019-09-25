package com.example.storm.benchmark.test;

import com.aliyun.openservices.log.common.LogGroupData;
import com.aliyun.openservices.loghub.client.config.LogHubCursorPosition;
import com.aliyun.openservices.loghub.stormspout.LogGroupDataSerializSerializer;
import com.aliyun.openservices.loghub.stormspout.LogHubSpout;
import com.aliyun.openservices.loghub.stormspout.LogHubSpoutConfig;
import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.StormSubmitter;
import org.apache.storm.generated.AlreadyAliveException;
import org.apache.storm.generated.AuthorizationException;
import org.apache.storm.generated.InvalidTopologyException;
import org.apache.storm.testing.TestWordSpout;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.tuple.Fields;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TopStrom {
   private static final Logger logger = LoggerFactory.getLogger(TopStrom.class);

    public static void main(String[] args) {
        String mode = "Local";  // 使用本地测试模式
        String conumser_group_name = "test";   // 每个Topology 需要设定唯一的 consumer group 名字，不能为空，支持 [a-z][0-9] 和 '_'，'-'，长度在 [3-63] 字符，只能以小写字母和数字开头结尾
        String project = "elastic";    // 日志服务的Project
        String logstore = "ec_hb_1";   // 日志服务的Logstore
        // String endpoint = "https://cn-shenzhen.log.aliyuncs.com";   // 日志服务访问域名
        String endpoint = "https://cn-shenzhen-intranet.log.aliyuncs.com";   // 日志服务访问域名
        String access_id = "HmTjtVwGWaEvbDw5";  // 用户 ak 信息
        String access_key = "GEKUNH2wv7fen6LxQhH2655ZcgRvmd";

        // 在实际场景中，Spout的个数可以和Logstore Shard 个数相同
        TopologyBuilder builder = new TopologyBuilder();
        builder.setSpout("wordreader", new WordReader(), 2);
        builder.setBolt("wordcounter", new WordCounter(), 2).shuffleGrouping("wordreader");
    //   builder.setBolt("wordcounter", new WordCounter_01(), 2).fieldsGrouping("wordreader", new Fields("word"));
//        builder.setBolt("wordwriter", new WordWriter()).globalGrouping("wordcounter");

        Config conf = new Config();
        conf.put("N", 6);

        conf.setDebug(false);


        conf.setMaxSpoutPending(1);
        // 如果使用Kryo进行数据的序列化和反序列化，则需要显示设置 LogGroupData 的序列化方法 LogGroupDataSerializSerializer
        Config.registerSerialization(conf, LogGroupData.class, LogGroupDataSerializSerializer.class);
        if (mode.equals("Local")) {
            //  logger.info("Local mode...");
            LocalCluster cluster  = new LocalCluster();
            cluster.submitTopology("test-jstorm-spout", conf, builder.createTopology());
            System.out.println("sout....start");
        //    logger.info("start.........");
            try {
                Thread.sleep(60*1000 * 1000);    //waiting for several minutes
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            cluster.killTopology("test-jstorm-spout");
            cluster.shutdown();
        } else if (mode.equals("Remote")) {
           // logger.info("Remote mode...");
            conf.setNumWorkers(2);
            try {
                try {
                    StormSubmitter.submitTopology("stt-jstorm-spout-4", conf, builder.createTopology());
                } catch (InvalidTopologyException e) {
                    e.printStackTrace();
                } catch (AuthorizationException e) {
                    e.printStackTrace();
                }
            } catch (AlreadyAliveException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
           // logger.error("invalid mode: " + mode);
        }
    }
}
