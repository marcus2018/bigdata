package com.example.hadoop;

import java.io.IOException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.hbase.mapreduce.TableReducer;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class HDFS2HBASEMR extends Configured implements Tool{
    private static final String ZK_CONNECT_KEY = "hbase.zookeeper.quorum";
    private static final String ZK_CONNECT_VALUE = "emr-worker-2.cluster-105374,emr-worker-1.cluster-105374,emr-header-1.cluster-105374";

    public static void main(String[] args) throws Exception {
        int isDone = ToolRunner.run(new HDFS2HBASEMR(), args);
        System.exit(isDone);
    }


    @Override
    public int run(String[] arg0) throws Exception {
        Configuration conf = new Configuration();
        conf.set(ZK_CONNECT_KEY, ZK_CONNECT_VALUE);
        conf.set("fs.defaultFS", "hdfs://emr-header-1.cluster-105374:9000");
        conf.addResource("/etc/ecm/hadoop-conf/core-site.xml");
        conf.addResource("/etc/ecm/hadoop-conf/config/hdfs-site.xml");
        //System.setProperty("HADOOP_USER_NAME", "hadoop");

        Job job = Job.getInstance(conf, "hdfs2hbase");
        job.setJarByClass(HDFS2HBASEMR.class);

        /**
         * mapper
         */
        job.setMapperClass(H2H_Mapper.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(NullWritable.class);

        //指定输入格式  可以不用指明 默认的是这个
        job.setInputFormatClass(TextInputFormat.class);

        /**
         * reducer
         */
        //因为要往表里面的插入数据  所以使用:initTableReducerJob
        TableMapReduceUtil.initTableReducerJob("users", H2H_Reducer.class, job, null, null, null, null, false);
        job.setOutputKeyClass(NullWritable.class);
        job.setOutputValueClass(Put.class);

        FileInputFormat.addInputPath(job, new Path("/student/input"));

        boolean isDone = job.waitForCompletion(true);

        return isDone ? 0 : 1;
    }

    /*
     * mapper端直接输出到reduce端进行处理
     */
    public static class H2H_Mapper extends Mapper<LongWritable, Text, Text, NullWritable>{
        @Override
        protected void map(LongWritable key, Text value,Context context) throws IOException, InterruptedException {
            context.write(value, NullWritable.get());
        }
    }


    public static class H2H_Reducer extends TableReducer<Text, NullWritable, NullWritable>{
        @Override
        protected void reduce(Text key, Iterable<NullWritable> values ,Context context) throws IOException, InterruptedException {
            /**
             * key  ===  95011,包小柏,男,18,MA
             *
             * 95001:  rowkey
             * 包小柏 : name
             * 18 : age
             * 男  ： sex
             * MA : department
             *
             * column family :  cf
             */
            String[] lines = key.toString().split(",");
            Put put = new Put(lines[0].getBytes());

            put.addColumn("info".getBytes(), "name".getBytes(), lines[1].getBytes());
            put.addColumn("info".getBytes(), "gender".getBytes(), lines[2].getBytes());
            put.addColumn("info".getBytes(), "age".getBytes(), lines[3].getBytes());
            put.addColumn("info".getBytes(), "department".getBytes(), lines[4].getBytes());


            context.write(NullWritable.get(), put);
        }

    }

}
