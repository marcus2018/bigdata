package com.example.storm.benchmark.test;



import java.sql.Types;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;

import org.apache.storm.topology.TopologyBuilder;


/**
 * @author liyijie
 * @date 2018年6月13日上午1:01:08
 * @email 37024760@qq.com
 * @remark
 * @version
 */
public class LocalWordCountStormJdbcTopology {

    public static void main(String[] args) {
        //本地模式，没有提交到服务器集群上,不需要搭建storm集群
        LocalCluster cluster = new LocalCluster();

        //TopologyBuilder根据spout和bolt来构建Topology
        //storm中任何一个作业都是通过Topology方式进行提交的
        //Topology中需要指定spout和bolt的执行顺序
        TopologyBuilder tb = new TopologyBuilder();
        tb.setSpout("DataSourceSpout", new WordCountSpout());
        //SumBolt以随机分组的方式从DataSourceSpout中接收数据
       tb.setBolt("CountBolt", new CountBolt()).shuffleGrouping("DataSourceSpout");
        /**
         Map hikariConfigMap = Maps.newHashMap();
         hikariConfigMap.put("dataSourceClassName","com.mysql.jdbc.jdbc2.optional.MysqlDataSource");
         hikariConfigMap.put("dataSource.url", "jdbc:mysql://localhost/sid");
         hikariConfigMap.put("dataSource.user","root");
         hikariConfigMap.put("dataSource.password","Liyijie331");
         ConnectionProvider connectionProvider = new HikariCPConnectionProvider(hikariConfigMap);

         JdbcClient jdbcClient = new JdbcClient(connectionProvider, 30);
         */
        /**写Mysql
         //mysql的表名
         String tableName = "wordcount";
         JdbcMapper simpleJdbcMapper = new SimpleJdbcMapper(tableName, connectionProvider);
         JdbcInsertBolt userPersistanceBolt = new JdbcInsertBolt(connectionProvider, simpleJdbcMapper)
         .withTableName(tableName)
         .withQueryTimeoutSecs(30);

         tb.setBolt("JdbcInsertBolt", userPersistanceBolt).shuffleGrouping("CountBolt");

         */



        //第一个参数是topology的名称，第三个参数是Topology
        cluster.submitTopology("LocalWordCountStormJdbcTopology", new Config(), tb.createTopology());

    }
}
