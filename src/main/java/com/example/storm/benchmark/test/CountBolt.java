package com.example.storm.benchmark.test;



import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.storm.jdbc.common.Column;
import org.apache.storm.jdbc.common.ConnectionProvider;
import org.apache.storm.jdbc.common.HikariCPConnectionProvider;
import org.apache.storm.jdbc.common.JdbcClient;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;

import com.google.common.collect.Maps;

/**
 * @author liyijie
 * @date 2018年6月13日下午8:58:54
 * @email 37024760@qq.com
 * @remark
 * @version
 *
 * 词频汇总Bolt
 */
public class CountBolt extends BaseRichBolt{

    private OutputCollector collector;
    private JdbcClient jdbcClient;
    private ConnectionProvider connectionProvider;

    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        this.collector = collector;
        System.out.println("===00000======");
      Map hikariConfigMap = Maps.newHashMap();
        hikariConfigMap.put("dataSourceClassName","com.mysql.jdbc.jdbc2.optional.MysqlDataSource");
        hikariConfigMap.put("dataSource.url", "jdbc:mysql://rm-wz99y4784rwj1w48txo.mysql.rds.aliyuncs.com:3306/immotordata");
        hikariConfigMap.put("dataSource.user","immotor_biddata");
        hikariConfigMap.put("dataSource.password","immotor!99");
        connectionProvider = new HikariCPConnectionProvider(hikariConfigMap);
        //对数据库连接池进行初始化
        connectionProvider.prepare();
        jdbcClient = new JdbcClient(connectionProvider, 30);
    }

    Map<String,Integer> map = new HashMap<String,Integer>();
    /**
     * 业务逻辑
     * 1.获取每个单词
     * 2.对所有单词进行汇总
     * 3.输出
     * */
    public void execute(Tuple input) {
        System.out.println("------");
        String word = input.getStringByField("word");
        Integer count = map.get(word);
        if(count==null){
            count=0;
        }
        count++;

        map.put(word, count);

        //查询该word是否存在
        List<Column> list = new ArrayList();
        //创建一列将值传入   列名  值    值的类型
        list.add(new Column("name", "深圳市", Types.VARCHAR));
        List<List<Column>> select = jdbcClient.select("select name from t_city where name = ?",list);
        System.out.println(word+" "+map.get(word));
        jdbcClient.executeSql("insert into wordcount values( '"+word+"',"+map.get(word)+")");
        //计算出查询的条数
        System.out.println(select.get(0));
        Long n = select.stream().count();
    /*    if(n>=1){
            //update
            jdbcClient.executeSql("update wordcount set word_count = "+map.get(word)+" where word = '"+word+"'");

        }else{
            //insert


        }*/
        //collector.emit(new Values(word,map.get(word)));
    }

    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        //后面jdbc insert bolt直接把这里的输出写Mysql里去了，所以这里的fileds的名字要跟mysql表的字段名字对应
        declarer.declare(new Fields("word","word_count"));
    }

    @Override
    public void cleanup() {
        connectionProvider.cleanup();
    }
}
