package com.example.storm.benchmark.util;

import com.google.common.collect.Maps;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.Map;

public class DataSource {

    public  static Map getJdbcDataSource(){
        Map hikariConfigMap = Maps.newHashMap();
        hikariConfigMap.put("dataSourceClassName","com.mysql.jdbc.jdbc2.optional.MysqlDataSource");
        hikariConfigMap.put("dataSource.url", "jdbc:mysql://rr-wz93pd9mi921e30j0.mysql.rds.aliyuncs.com:3306/power2");
        hikariConfigMap.put("dataSource.user","etl");
        hikariConfigMap.put("dataSource.password","immotor!99");
        return hikariConfigMap;
    }
    public  static JedisPool  getJedisPool(){
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxIdle(5);
        config.setMaxTotal(1000 * 100);
        config.setMaxWaitMillis(30);
        config.setTestOnBorrow(true);
        config.setTestOnReturn(true);
        //                    pool = new JedisPool(config, "10.27.169.187", 6574,20000,"immotor!6574@.com");
        return new JedisPool(config, "r-wz9gjghb9zyfa6ciyv.redis.rds.aliyuncs.com", 6379,20000,"Ehdbigdata190418");
      //  return new JedisPool(config, "119.23.133.72", 6574,20000,"immotor!6574@.com");
    }
}
