package com.example.storm.benchmark.produce.cabinet.cabinet_battery_temp.bean;

import java.io.Serializable;
import java.util.Date;

public class BatteryHB3InStationTemp implements Serializable{//写到loghub
    private  String id;
    private String pid;
    private int port;
    private double temp;
    private  int code;
    private Date date;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getTemp() {
        return temp;

    }

    public void setTemp(double temp) {
        this.temp = temp;
    }

    public int getCode() {
        return code;
    }

    public BatteryHB3InStationTemp(String id, String pid, int port, double temp, int code, Date date) {
        this.id = id;
        this.pid = pid;
        this.port = port;
        this.temp = temp;
        this.code = code;
        this.date=date;
    }

    public void setCode(int code) {

        this.code = code;
    }
}
