package com.example.storm.benchmark.produce.cabinet.cabinet_battery_temp.bean;

import java.io.Serializable;

public class BatteryHB3 implements Serializable{
    private int c;
    private int ccl;
    private int d;
    private int dc;
    private int f;
    private String id;
    private int nc;
    private int nv;
    private int p;
    private int soc;
    private int soh;
    private  int t;
    private int v;

    public int getC() {
        return c;
    }

    public void setC(int c) {
        this.c = c;
    }

    public int getCcl() {
        return ccl;
    }

    @Override
    public String toString() {
        return "BatteryHB3{" +
                "c=" + c +
                ", ccl=" + ccl +
                ", d=" + d +
                ", dc=" + dc +
                ", f=" + f +
                ", id='" + id + '\'' +
                ", nc=" + nc +
                ", nv=" + nv +
                ", p=" + p +
                ", soc=" + soc +
                ", soh=" + soh +
                ", t=" + t +
                ", v=" + v +
                '}';
    }

    public void setCcl(int ccl) {
        this.ccl = ccl;
    }

    public int getD() {
        return d;
    }

    public void setD(int d) {
        this.d = d;
    }

    public int getDc() {
        return dc;
    }

    public void setDc(int dc) {
        this.dc = dc;
    }

    public int getF() {
        return f;
    }

    public void setF(int f) {
        this.f = f;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getNc() {
        return nc;
    }

    public void setNc(int nc) {
        this.nc = nc;
    }

    public int getNv() {
        return nv;
    }

    public void setNv(int nv) {
        this.nv = nv;
    }

    public int getP() {
        return p;
    }

    public void setP(int p) {
        this.p = p;
    }

    public int getSoc() {
        return soc;
    }

    public void setSoc(int soc) {
        this.soc = soc;
    }

    public int getSoh() {
        return soh;
    }

    public void setSoh(int soh) {
        this.soh = soh;
    }

    public int getT() {
        return t;
    }

    public void setT(int t) {
        this.t = t;
    }

    public int getV() {
        return v;
    }

    public void setV(int v) {
        this.v = v;
    }
}
