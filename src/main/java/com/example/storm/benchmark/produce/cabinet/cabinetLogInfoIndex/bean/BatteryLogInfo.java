package com.example.storm.benchmark.produce.cabinet.cabinetLogInfoIndex.bean;

public class BatteryLogInfo  implements Comparable<BatteryLogInfo>{
    private  String bid;
    private  Long eventType;
    private   String time;
    private  String omitLogIndex;



    public BatteryLogInfo(String bid, long eventType, String time) {
        this.bid = bid;
        this.eventType = eventType;
        this.time = time;
    }

    public BatteryLogInfo(String bid, long eventType, String time, String omitLogIndex) {
        this.bid = bid;
        this.eventType = eventType;
        this.time = time;
        this.omitLogIndex = omitLogIndex;
    }

    public String getBid() {
        return bid;
    }

    public void setBid(String bid) {
        this.bid = bid;
    }

    public long getEventType() {
        return eventType;
    }

    public void setEventType(long eventType) {
        this.eventType = eventType;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getOmitLogIndex() {
        return omitLogIndex;
    }

    public void setOmitLogIndex(String omitLogIndex) {
        this.omitLogIndex = omitLogIndex;
    }

    @Override
    public int compareTo(BatteryLogInfo o) {
        return this.bid.compareTo(o.bid)==0?this.eventType.compareTo(o.getEventType()):this.bid.compareTo(o.bid);
    }
}
