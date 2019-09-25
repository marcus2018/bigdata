package com.immotor.collectData;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@ComponentScan({

      //  "com.immotor.collectData.*"
        "com.*"
})
@EnableTransactionManagement
public class CollectionData {

    public static void main(String[] args) {
        SpringApplication.run(CollectionData.class, args);
    }
}
