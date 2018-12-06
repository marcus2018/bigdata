package com.immotor.collectData.service;


import com.immotor.collectData.model.LogCommon;
import com.immotor.collectData.util.AliyunSDK;
import org.springframework.stereotype.Service;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class Collect {
    ExecutorService exec = Executors .newCachedThreadPool( );
    ExecutorService exec1 = Executors .newFixedThreadPool( 20);
    public Object collectLog(final  LogCommon log,final String topicName) {
        {
            exec1.execute(new Runnable() {
                public void run() {
                    AliyunSDK aliyunSDK=new AliyunSDK();
                    aliyunSDK.write(log,topicName);
                }

            });

            return 1;

        }
    }
}
