package com.example.storm.benchmark.produce.station_battery_damage_6min.bean;

public class Station_battery_Collection_type3 {
    private  String bId;
    private long collectTime;
    private  String pId;
    private String  partReadOnlyData;
    private int port;
    private  int type;

    public String getbId() {
        return bId;
    }

    public void setbId(String bId) {
        this.bId = bId;
    }

    public long getCollectTime() {
        return collectTime;
    }

    public void setCollectTime(long collectTime) {
        this.collectTime = collectTime;
    }

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }

    public String getPartReadOnlyData() {
        return partReadOnlyData;
    }

    public void setPartReadOnlyData(String partReadOnlyData) {
        this.partReadOnlyData = partReadOnlyData;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
