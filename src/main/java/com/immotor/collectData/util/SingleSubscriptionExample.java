package com.immotor.collectData.util;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.aliyun.datahub.DatahubClient;
import com.aliyun.datahub.DatahubConfiguration;
import com.aliyun.datahub.auth.AliyunAccount;
import com.aliyun.datahub.exception.DatahubClientException;
import com.aliyun.datahub.exception.OffsetResetedException;
import com.aliyun.datahub.exception.OffsetSessionChangedException;
import com.aliyun.datahub.exception.SubscriptionOfflineException;
import com.aliyun.datahub.model.*;
import com.aliyun.datahub.model.GetCursorRequest.CursorType;
public class SingleSubscriptionExample {
    private String accessId = "HmTjtVwGWaEvbDw5";
    private String accessKey = "GEKUNH2wv7fen6LxQhH2655ZcgRvmd";
    private String endpoint = "http://dh-cn-hangzhou.aliyuncs.com";
    private String projectName = "android_collectdata";
    private String topicName = "app_chargetime";
    private String subId = "1531101805607Hd04Q";
    private String shardId = "0";
    private DatahubConfiguration conf;
    private DatahubClient client;
    public SingleSubscriptionExample() {
        this.conf = new DatahubConfiguration(new AliyunAccount(accessId, accessKey), endpoint);
        this.client = new DatahubClient(conf);
    }
    public void Start() {
        try {
            boolean bExit = false;
            GetTopicResult topicResult = client.getTopic(projectName, topicName);
            // 首先初始化offset上下文
            OffsetContext offsetCtx = client.initOffsetContext(projectName, topicName, subId, shardId);
            String cursor = null; // 开始消费的cursor
            if (!offsetCtx.hasOffset()) {
                // 之前没有存储过点位，先获取初始点位，比如这里获取当前该shard最早的数据
                GetCursorResult cursorResult = client.getCursor(projectName, topicName, shardId, CursorType.OLDEST);
                cursor = cursorResult.getCursor();
            } else {
                // 否则，获取当前已消费点位的下一个cursor
                cursor = client.getNextOffsetCursor(offsetCtx).getCursor();
            }
            System.out.println("Start consume records, begin offset context:" + offsetCtx.toObjectNode().toString()
                    + ", cursor:" + cursor);
            long recordNum = 0L;
            while (!bExit) {
                try {
                    GetRecordsResult recordResult = client.getRecords(projectName, topicName, shardId, cursor, 10,
                            topicResult.getRecordSchema());
                    List<RecordEntry> records = recordResult.getRecords();
                    if (records.size() == 0) {
                        // 将最后一次消费点位上报
                        client.commitOffset(offsetCtx);
                        System.out.println("commit offset suc! offset context: " + offsetCtx.toObjectNode().toString());
                        // 可以先休眠一会，再继续消费新记录
                        Thread.sleep(1000);
                        System.out.println("sleep 1s and continue consume records! shard id:" + shardId);
                    } else {
                        for (RecordEntry record : records) {
                            // 处理记录逻辑
                             System.out.println("Consume shard:" + shardId + " thread process record:"
                                    + record.toJsonNode().toString());
                            // 上报点位，该示例是每处理100条记录上报一次点位
                            offsetCtx.setOffset(record.getOffset());
                            recordNum++;
                            if (recordNum % 100 == 0) {
                                client.commitOffset(offsetCtx);
                                System.out.println("commit offset suc! offset context: " + offsetCtx.toObjectNode().toString());
                            }
                        }
                        cursor = recordResult.getNextCursor();
                    }
                } catch (SubscriptionOfflineException e) {
                    // 订阅下线，退出
                    bExit = true;
                    e.printStackTrace();
                } catch (OffsetResetedException e) {
                    // 点位被重置，更新offset上下文
                    client.updateOffsetContext(offsetCtx);
                    cursor = client.getNextOffsetCursor(offsetCtx).getCursor();
                    System.out.println("Restart consume shard:" + shardId + ", reset offset:"
                            + offsetCtx.toObjectNode().toString() + ", cursor:" + cursor);
                } catch (OffsetSessionChangedException e) {
                    // 其他consumer同时消费了该订阅下的相同shard，退出
                    bExit = true;
                    e.printStackTrace();
                } catch (Exception e) {
                    bExit = true;
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void createDataConnector () {
        // Create SinkOdps DataConnector
        // ODPS相关配置设置
        String odpsProject = "immotor_bigdata";
        String odpsTable = "app_chargetime_back";
        String odpsEndpoint = "http://service-all.ext.odps.aliyun-inc.com/api";
        String tunnelEndpoint = "http://dt-all.ext.odps.aliyun-inc.com";
        OdpsDesc odpsDesc = new OdpsDesc();
        odpsDesc.setProject(odpsProject);
        odpsDesc.setTable(odpsTable);
        odpsDesc.setOdpsEndpoint(odpsEndpoint);
        odpsDesc.setTunnelEndpoint(tunnelEndpoint);
        odpsDesc.setAccessId(accessId);
        odpsDesc.setAccessKey(accessKey);
        odpsDesc.setPartitionMode(OdpsDesc.PartitionMode.USER_DEFINE);
        // 顺序选中topic中部分列或全部列 同步到odps，未选中的列将不会同步
        List<String> columnFields = new ArrayList<String>();
        columnFields.add("startsoc");
        columnFields.add("oncefull");
        columnFields.add("stoptime");
        columnFields.add("id");
        columnFields.add("pid");
        columnFields.add("starttime");
        columnFields.add("stopsoc");
        columnFields.add("ds");


        // 默认是使用UserDefine 的分区模式，具体参见文档[https://help.aliyun.com/document_detail/47453.html?spm=5176.product53345.6.555.MpixiB]
        // 如果需要使用SYSTEM_TIME或EVENT_TIME模式，需要如下设置
        // 对于EVENT_TIME需要在schema中增加一个字段：
        // "event_time"，类型是TIMESTAMP
        // begin
     //   int timeRange = 15;  // 分钟，分区时间间隔，最小15分钟
     //   odpsDesc.setPartitionMode(OdpsDesc.PartitionMode.USER_DEFINE);
        //odpsDesc.setTimeRange(timeRange);
      //  Map<String, String> partitionConfig = new LinkedHashMap<String, String>();
        //目前仅支持 %Y%m%d%H%M 的组合，任意多级分区
       // partitionConfig.put("pt", "%Y%m%d");
       // partitionConfig.put("ct", "%H%M");
      //  odpsDesc.setPartitionConfig(partitionConfig);
        // end
        client.createDataConnector(projectName, topicName, ConnectorType.SINK_ODPS, columnFields, odpsDesc);
        // 特殊需求下可以间歇性 如每15分钟获取Connector状态查看是否有异常,遍历所有shard
        String shard = "0";
        GetDataConnectorShardStatusResult getDataConnectorShardStatusResult =
                client.getDataConnectorShardStatus(projectName, topicName, ConnectorType.SINK_ODPS, shard);
        System.out.println(getDataConnectorShardStatusResult.getCurSequence());
        System.out.println(getDataConnectorShardStatusResult.getLastErrorMessage());
    }
    public static void main(String[] args) {
        SingleSubscriptionExample example = new SingleSubscriptionExample();
        try {
            example.createDataConnector();
          //  example.Start();
        } catch (DatahubClientException e) {
            e.printStackTrace();
        }
    }
}
