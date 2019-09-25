package com.example.storm.bean;

import java.io.Serializable;
import java.util.Date;

public class CabinetHBTime implements Comparable<CabinetHBTime>,Serializable {
    private  String pid;
    private Date date;
    private  int type;
    private  int full;
    private  int vaild;

    public int getVaild() {
        return vaild;
    }

    public void setVaild(int vaild) {
        this.vaild = vaild;
    }

    public int getFull() {
        return full;
    }

    public void setFull(int full) {
        this.full = full;
    }

    public String getPid() {
        return pid;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public Date getDate() {
        return date;
    }

    @Override
    public String toString() {
        return "CabinetHBTime{" +
                "pid='" + pid + '\'' +
                ", date=" + date +
                '}';
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public int compareTo(CabinetHBTime o) {
        return this.date.compareTo(o.date);
    }
}
