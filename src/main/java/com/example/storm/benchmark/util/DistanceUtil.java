package com.example.storm.benchmark.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

class  LonLat{
    private  Double lon;
    private  Double lat;

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public LonLat(Double lon, Double lat) {
        this.lon = lon;
        this.lat = lat;
    }
}

public class DistanceUtil {
    private static final double EARTH_RADIUS = 6378.137*1000;//地球半径,单位千米
    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }

    public static void main(String[] args) {
        List<LonLat> list=new ArrayList();
        list.add(new LonLat(114.282774,30.572283  ));
        list.add(new LonLat(114.281492,30.573679  ));
        list.add(new LonLat(114.277386,30.580531  ));
        list.add(new LonLat(114.279913,30.590943  ));
        list.add(new LonLat(114.279913,30.590943  ));
        list.add(new LonLat(114.279913,30.590943  ));
        list.add(new LonLat(114.283697,30.568348  ));
        list.add(new LonLat(114.286468,30.56846   ));
        list.add(new LonLat(114.277882,30.579711  ));
        list.add(new LonLat(114.278241,30.590202  ));
        list.add(new LonLat(114.277799,30.590253  ));
        list.add(new LonLat(114.278699,30.590395  ));
        list.add(new LonLat(114.27509,30.590816   ));
        list.add(new LonLat(114.286461,30.568801  ));
        list.add(new LonLat(114.282071,30.569888  ));
        list.add(new LonLat(114.279782,30.576684  ));
        list.add(new LonLat(114.275669,30.586416  ));
        list.add(new LonLat(114.27789,30.589751   ));
        list.add(new LonLat(114.274655,30.589825  ));
        list.add(new LonLat(114.277623,30.590211  ));
         double total=0.0;
        for(int i=0;i<list.size()-1;i++){
            total+=LantitudeLongitudeDist(list.get(i).getLon(),list.get(i).getLat(),list.get(i+1).getLon(),list.get(i+1).getLat());
        }
        System.out.println(new Date());
        System.out.println(new Date(new Date().getTime()-1000000));
        System.out.println(new Date().compareTo( new Date(new Date().getTime()-1000000)));
        System.out.println(total);
    }

    /**
     * 基于余弦定理求两经纬度距离
     *
     * @param lon1 第一点的精度
     * @param lat1 第一点的纬度
     * @param lon2 第二点的精度
     * @param lat3 第二点的纬度
     * @return 返回的距离，单位km
     */
    public static double LantitudeLongitudeDist(double lat1,double lon1,double lat2 ,double lon2 ) {
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);

        double radLon1 = rad(lon1);
        double radLon2 = rad(lon2);

        if (radLat1 < 0)
            radLat1 = Math.PI / 2 + Math.abs(radLat1);// south
        if (radLat1 > 0)
            radLat1 = Math.PI / 2 - Math.abs(radLat1);// north
        if (radLon1 < 0)
            radLon1 = Math.PI * 2 - Math.abs(radLon1);// west
        if (radLat2 < 0)
            radLat2 = Math.PI / 2 + Math.abs(radLat2);// south
        if (radLat2 > 0)
            radLat2 = Math.PI / 2 - Math.abs(radLat2);// north
        if (radLon2 < 0)
            radLon2 = Math.PI * 2 - Math.abs(radLon2);// west
        double x1 = EARTH_RADIUS * Math.cos(radLon1) * Math.sin(radLat1);
        double y1 = EARTH_RADIUS * Math.sin(radLon1) * Math.sin(radLat1);
        double z1 = EARTH_RADIUS * Math.cos(radLat1);

        double x2 = EARTH_RADIUS * Math.cos(radLon2) * Math.sin(radLat2);
        double y2 = EARTH_RADIUS * Math.sin(radLon2) * Math.sin(radLat2);
        double z2 = EARTH_RADIUS * Math.cos(radLat2);

        double d = Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2) + (z1 - z2) * (z1 - z2));
        //余弦定理求夹角
        double theta = Math.acos((EARTH_RADIUS * EARTH_RADIUS + EARTH_RADIUS * EARTH_RADIUS - d * d) / (2 * EARTH_RADIUS * EARTH_RADIUS));
        double dist = theta * EARTH_RADIUS;
        return dist;
    }
}