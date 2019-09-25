package com.example.storm.benchmark.produce.cabinet.cabinetFullPort;

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
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.topology.base.BaseWindowedBolt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class Log_fullandValidPort {
    private static final Logger logger = LoggerFactory.getLogger(Log_fullandValidPort.class);
    public static void main( String[] args )
    {
        String mode = "Remote";  // 使用本地测试模式
        String conumser_group_name = "produce_full_and_valid_inStation";   // 每个Topology 需要设定唯一的 consumer group 名字，不能为空，支持 [a-z][0-9] 和 '_'，'-'，长度在 [3-63] 字符，只能以小写字母和数字开头结尾
        String project = "elastic";    // 日志服务的Project
        String logstore = "station_log";   // 日志服务的Logstore
        //String endpoint = "https://cn-shenzhen.log.aliyuncs.com";   // 日志服务访问域名
        String endpoint = "https://cn-shenzhen-intranet.log.aliyuncs.com";   // 日志服务访问域名
        String access_id = "HmTjtVwGWaEvbDw5";  // 用户 ak 信息
        String access_key = "GEKUNH2wv7fen6LxQhH2655ZcgRvmd";
        // 构建一个 Loghub Storm Spout 需要使用的配置
        LogHubSpoutConfig config = new LogHubSpoutConfig(conumser_group_name,
                endpoint, project, logstore, access_id,
                access_key, LogHubCursorPosition.END_CURSOR);
        TopologyBuilder builder = new TopologyBuilder();
        // 构建 loghub storm spout
        LogHubSpout spout = new LogHubSpout(config);
        // 在实际场景中，Spout的个数可以和Logstore Shard 个数相同
        builder.setSpout("spout", spout, 1);
        builder.setBolt("slidingwindowbolt", new FullPortBolt()
                .withWindow(new BaseWindowedBolt.Duration(5, TimeUnit.SECONDS)   ,           new BaseWindowedBolt.Duration(5, TimeUnit.SECONDS)),2)
                .shuffleGrouping("spout");

        //每收到2条tuple就统计最近两条统的数据
       // builder.setBolt("exclaim", new SampleBolt()).shuffleGrouping("spout");
        Config conf = new Config();
        conf.setDebug(true);
        conf.setMessageTimeoutSecs(40000);
        conf.setMaxSpoutPending(100000);

        // 如果使用Kryo进行数据的序列化和反序列化，则需要显示设置 LogGroupData 的序列化方法 LogGroupDataSerializSerializer
       Config.registerSerialization(conf, LogGroupData.class, LogGroupDataSerializSerializer.class);
        if (mode.equals("Local")) {
          //  logger.info("Local mode...");
            LocalCluster cluster  = new LocalCluster();
            cluster.submitTopology("test-jstorm-spout", conf, builder.createTopology());

        } else if (mode.equals("Remote")) {
            logger.info("Remote mode...");
            conf.setNumWorkers(2);
            try {
                try {
                    StormSubmitter.submitTopology("produce_full_and_valid_inStation", conf, builder.createTopology());
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
            logger.error("invalid mode: " + mode);
        }
    }
}

