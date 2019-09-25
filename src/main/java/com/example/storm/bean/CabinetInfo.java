package com.example.storm.bean;

import java.io.Serializable;

public class CabinetInfo implements Serializable {
    private String sn;

    @Override
    public String toString() {
        return "CabinetInfo{" +
                "sn='" + sn + '\'' +
                ", addr='" + addr + '\'' +
                ", pid='" + pid + '\'' +
                '}';
    }

    private String addr;

    public CabinetInfo(String pid,String sn, String addr ) {
        this.sn = sn;
        this.addr = addr;
        this.pid = pid;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    private  String pid;

    public CabinetInfo(String sn, String addr) {
        this.sn = sn;
        this.addr = addr;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }
}
