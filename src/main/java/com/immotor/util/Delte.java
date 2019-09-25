package com.immotor.util;

import com.google.gson.Gson;
import redis.clients.jedis.Jedis;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Delte {
    private Jedis jedis;

    public Delte() {
        jedis = new Jedis("127.0.0.1", 6379);
    }

    public Delte(Jedis jedis) {
        this.jedis = jedis;
    }

    /**
     * 修改购物车中的商品
     *
     * @param userName  用户名
     * @param productId 商品编号
     * @param num       操作商品的数量
     */
    public void updateProduct2Cart(String userName, String productId, int num) {
        jedis.hincrBy("shop:cart:" + userName, productId, num);
    }





    public static void main(String[] args) {
        //初始化商品的信息
        initData();
        //创建购物车对象
   /*     Cart cart = new Cart();
        //创建用户
        String userName = "liudehua";
        //往用户购物车中添加商品
        cart.updateProduct2Cart(userName, "1645080454", 10);
        cart.updateProduct2Cart(userName, "1788744384", 1000);
        cart.updateProduct2Cart(userName, "1645139266", -1000);
        //打印当前用户的购物车信息
        List<Product> products = cart.getProductsByUserName(userName);
        for (Product product : products) {
            System.out.println(product);
        }*/
    }

    private static void initData() {
        System.out.println("========================初始化商品信息===========================");
        Jedis jedis = new Jedis("119.23.133.72", 6574);
        jedis.auth("immotor!6574@.com");
       /* //准备数据
        Product product1 = new Product("1645139266", "战地鳄2015秋冬新款马甲可脱卸帽休闲时尚无袖男士羽绒棉外套马甲", new BigDecimal("168"));
        Product product2 = new Product("1788744384", "天乐时 爸爸装加厚马甲秋冬装中年大码男士加绒马夹中老年坎肩老年人", new BigDecimal("40"));
        Product product3 = new Product("1645080454", "战地鳄2015秋冬新款马甲可脱卸帽休闲时尚无袖男士羽绒棉外套马甲", new BigDecimal("230"));
        //将数据写入到Redis
        jedis.set("shop:product:" + product1.getId(), new Gson().toJson(product1));
        jedis.set("shop:product:" + product2.getId(), new Gson().toJson(product2));
        jedis.set("shop:product:" + product3.getId(), new Gson().toJson(product3));
        //打印所有产品信息*/
        long  startTime=System.currentTimeMillis();
       /* Set<String> allProductKeys=jedis.keys("monitor:scooter_track:*");
         Map<String,String>  maps     = jedis.hgetAll("monitor:scooter_battery_numbers"); //获取所有的商品信息
        Set<String> stringSet=  jedis.smembers("monitor:station_pid_lost");*/
        //  jedis.del("monitor:scooter_track_distance_gte1000");
        //ef33ae5b04563e24
        //f8e5925b08f8c924
        //2bdc1f5c02f22124
        // jedis.del("monitor:station_battery_damage");


        //   System.out.println(key);
        //Map<String,String> map= jedis.hgetAll("monitor:scooter_track:"+key);
        //     System.out.println(key+map.get("nowDistance"));
        //  jedis.del("monitor:scooter_battery_numbers");
        // System.out.println(map.size());
        //    int total=0;
        // for(Map.Entry<String,String> maps:map.entrySet()){
        Set<String> list=new HashSet<String>();
        list.add("101,15,5D,8C,95,85");
        list.add("101,15,5D,8C,95,82");
        list.add("101,15,5D,8C,95,81");
        list.add("101,15,5D,8C,95,80");
        list.add("101,15,5D,8C,95,90");
        list.add("101,15,5D,8C,95,91");
        list.add("101,15,5D,8C,95,92");
        String[] strings = new String[list.size()];
        List<String> redisValues =jedis.hmget("collect",list.toArray(strings));
        System.out.println(redisValues.size());
        for(int i=0;i<redisValues.size();i++){
            if(redisValues.get(i)==null)
                continue;
            System.out.println(redisValues.get(i));
            System.out.println(redisValues.get(i).split("\\|").length);
            System.out.println(redisValues.get(i).split("\\|")[0]);
            String redisBid=redisValues.get(i).split("\\|")[0];
            String redisEventType=redisValues.get(i).split("\\|")[1];
            String redisTime=redisValues.get(i).split("\\|")[2];
            String redisOmit=redisValues.get(i).split("\\|")[3];
        }

        //  }

        //    System.out.println("===========");
    }

    // jedis.del("monitor:scooter_battery_numbers");
     /*for (String key:keys){
         System.out.println(key);
         Map<String,String> map2=jedis.hgetAll(key);
         System.out.println(map2.get("minTime")+" "+map2.get("duration")+" "+map2.get("times"));
         // jedis.del(key);
     }*/


     /* for (String key : allProductKeys) {

         jedis.del(key);
      }*/

      /*      Product product = new Gson().fromJson(json, Product.class);//从字符串中解析出对象
            System.out.println(product);*//*
        }*/
    //    System.out.println("========================用户购物车信息如下===========================");



    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
