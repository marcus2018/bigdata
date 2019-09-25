package com.example.storm.benchmark.produce.station_battery_damage_6min;



import com.example.storm.benchmark.produce.cabinet.cabinet_battery_temp.bean.StationInfo;

import java.util.Date;

public class BatteryMonitorTimeToLoghub {
    private  String bid="-1";
    private String bSn="-1";
    private String pid="-1";
    private  String content;

    private StationInfo stationInfo;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public StationInfo getStationInfo() {
        return stationInfo;
    }

    public void setStationInfo(StationInfo stationInfo) {
        this.stationInfo = stationInfo;
    }

    public String getBid() {
        return bid;
    }

    public void setBid(String bid) {
        this.bid = bid;
    }

    public String getbSn() {
        return bSn;
    }

    public void setbSn(String bSn) {
        this.bSn = bSn;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    @Override
    public String toString() {
        return "BatteryMonitorTimeToLoghub{" +
                "bid='" + bid + '\'' +
                ", bSn='" + bSn + '\'' +
                ", pid='" + pid + '\'' +
                ", stationInfo=" + stationInfo +
                ", date=" + date +
                ", Damagetype=" + Damagetype +
                ", bitType=" + bitType +
                '}';
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getDamagetype() {
        return Damagetype;
    }

    public void setDamagetype(int damagetype) {
        Damagetype = damagetype;
    }

    public int getBitType() {
        return bitType;
    }

    public void setBitType(int bitType) {
        this.bitType = bitType;
    }

    private Date date;
    private int Damagetype;
    private int bitType;
}
