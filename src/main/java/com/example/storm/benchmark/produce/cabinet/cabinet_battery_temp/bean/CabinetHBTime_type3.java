package com.example.storm.benchmark.produce.cabinet.cabinet_battery_temp.bean;

import java.util.Date;
import java.util.List;

public class CabinetHBTime_type3 {
    @Override
    public String toString() {
        return "CabinetHBTime_type3{" +
                "pID='" + pID + '\'' +
                ", date=" + date +
                ", type=" + type +
                ", port=" + port +
                ", id='" + id + '\'' +
                ", t=" + t +
                ", stationHB3_datasList=" + stationHB3_datasList +
                '}';
    }

    private  String pID;
    private Date  date;
    private  Integer type;
    private Integer port;
    private  String id;
    private  int  f;
    private  int d;
    private  int cycle;
    private  int soh;

    public int getSoc() {
        return soc;
    }

    public void setSoc(int soc) {
        this.soc = soc;
    }

    private  int current;
    private  int voltage;
    private  int soc;

    public int getCycle() {
        return cycle;
    }

    public void setCycle(int cycle) {
        this.cycle = cycle;
    }

    public int getSoh() {
        return soh;
    }

    public void setSoh(int soh) {
        this.soh = soh;
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public int getVoltage() {
        return voltage;
    }

    public void setVoltage(int voltage) {
        this.voltage = voltage;
    }

    public int getF() {
        return f;
    }

    public void setF(int f) {
        this.f = f;
    }

    public int getD() {
        return d;
    }

    public void setD(int d) {
        this.d = d;
    }

    public int getSum() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }

    private  int sum;

    public int getT() {
        return t;
    }

    public void setT(int t) {
        this.t = t;
    }

    private int t;
    private List<StationHB3_Datas> stationHB3_datasList;

    public List<StationHB3_Datas> getStationHB3_datasList() {
        return stationHB3_datasList;
    }

    public void setStationHB3_datasList(List<StationHB3_Datas> stationHB3_datasList) {
        this.stationHB3_datasList = stationHB3_datasList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getpID() {
        return pID;
    }

    public void setpID(String pID) {
        this.pID = pID;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
