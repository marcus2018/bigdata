package com.immotor.collectData.model;

import java.util.List;

public class BatteryLog {
    private  String bid;
    private long index;
    private  String time;
    private List<BatteryLostIndex> list;
    public BatteryLog(String bid, long index, String time) {
        this.bid = bid;
        this.index = index;
        this.time = time;

    }

    public BatteryLog(String bid, long index, String time, List<BatteryLostIndex> list) {
        this.bid = bid;
        this.index = index;
        this.time = time;
        this.list = list;
    }

    public String getBid() {
        return bid;
    }

    public void setBid(String bid) {
        this.bid = bid;
    }

    public long getIndex() {
        return index;
    }

    public void setIndex(long index) {
        this.index = index;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public List<BatteryLostIndex> getList() {
        return list;
    }

    public void setList(List<BatteryLostIndex> list) {
        this.list = list;
    }
}
