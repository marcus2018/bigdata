package com.immotor.collectData.util;

import com.aliyun.openservices.log.common.LogItem;
import com.aliyun.openservices.log.exception.LogException;
import com.aliyun.openservices.log.producer.ILogCallback;
import com.aliyun.openservices.log.producer.ProducerConfig;
import com.aliyun.openservices.log.producer.ProjectConfig;
import com.aliyun.openservices.log.response.PutLogsResponse;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LogProducer {

    private static final String MOCK_IP = "192.168.0.25";


    public static  com.aliyun.openservices.log.producer.LogProducer producer= new com.aliyun.openservices.log.producer.LogProducer(new ProducerConfig());;

    public void setUp() {
        producer = new com.aliyun.openservices.log.producer.LogProducer(new ProducerConfig());
    }

    private static ProjectConfig  projectConfig=null;
    public void cleanUp() {
    }

    private ProjectConfig buildProjectConfig1() {

        String projectName = "test-json";
        String endpoint = "https://cn-shenzhen.log.aliyuncs.com";
        String accessKeyId = "HmTjtVwGWaEvbDw5";
        String accessKey = "GEKUNH2wv7fen6LxQhH2655ZcgRvmd";
        return new ProjectConfig(projectName, endpoint, accessKeyId, accessKey);
    }

    public static ProjectConfig getProjectConfig() {
        if (projectConfig == null) {
            synchronized (ProjectConfig.class) {
                if(projectConfig==null) {
                    projectConfig = new ProjectConfig(Constant.PROJECTNAME, Constant.ENDPOINT,Constant.ACCESSKEYID, Constant.ACCESSKEY);
                }
            }
        }
        return projectConfig;
    }
    private ProjectConfig buildProjectConfig2() {
        String projectName = System.getenv("project2");
        String endpoint = System.getenv("endpoint2");
        String accessKeyId = System.getenv("accessKeyId");
        String accessKey = System.getenv("accessKey");
        return new ProjectConfig(projectName, endpoint, accessKeyId, accessKey);
    }

    private List<LogItem> getLogItems() {
        List<LogItem> logItems = new ArrayList<LogItem>();
        long start=System.currentTimeMillis();
        //  for(int i=0;i<15000;i++) {

        for(int i=0;i<1;i++) {
            LogItem logItem1 = new LogItem((int) (new Date().getTime() / 1000));
            logItem1.PushBack("content","{\"soc\": 85, \"time\": 1537200546171, \"maxopc\": 3600, \"time1\": \"20180918\", \"current\": -1, \"bats\": [{\"soc\": 85, \"temperature\": 36, \"current\": -1, \"voltage\": 6312, \"id\": \"1016AD191EC5\", \"cycle\": 66}], \"voltage\": 6312, \"sID\": \"574C547931C9\", \"location\": \"30.649452,104.079238\"}\n");
            //   LogItem logItem2 = new LogItem((int) (new Date().getTime() / 1000));
            // logItem2.PushBack("key2", "val2");
            logItems.add(logItem1);
            //  logItems.add(logItem2);
        }

        long end=System.currentTimeMillis();
        System.out.println("list="+(end-start));

        return logItems;
    }


    public void testSendToMultiProjects() {

      //  producer = new com.aliyun.openservices.log.producer.LogProducer(new ProducerConfig());
        producer.setProjectConfig(buildProjectConfig1());
        //    producer.setProjectConfig(buildProjectConfig2());
        long start=System.currentTimeMillis();
        System.out.println(System.currentTimeMillis());
        for(int i=0;i<60000;i++) {
            long start1=System.currentTimeMillis();
            LogProducer.TestCallback testCallback1 = new   LogProducer.TestCallback();
            //TestCallback testCallback2 = mock(TestCallback.class);


            producer.send("test-json", "json_scooter", "topic1", MOCK_IP, getLogItems(),
                    testCallback1);
//            producer.flush();
//            producer.close();
            long end1 = System.currentTimeMillis();
//            long end = System.currentTimeMillis();
//
            System.out.println("push1=" + (end1 - start1));
        }
        long end = System.currentTimeMillis();

        System.out.println("push2=" + (end - start));
        //   producer.flush();
        try {
            Thread.sleep(100000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

    static private class TestCallback extends ILogCallback {

        @Override
        public void onCompletion(PutLogsResponse response, LogException e) {
            // re
        }
    }





}
