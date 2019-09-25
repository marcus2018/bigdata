package com.example.storm.benchmark.produce.cabinet.cabinet_battery_temp.bean;

public class BatteryDamageHappenTime {
    private  int cityCode=-1;
    private String sn="-1";
    private String name="-1";
    private String pid="-1";
    private int p=-1;
    private int damage=-1;
    private int falut=-1;
    private long happenTime=0;
    private  String bid;
    private  int cycle=-1;
    private int current=-1;
    private  int soc;
    private  int soh;
    private  int  t;
    private  int v;
    private  String bSn="-1";

    public String getbSn() {
        return bSn;
    }

    public void setbSn(String bSn) {
        this.bSn = bSn;
    }

    public int getCycle() {
        return cycle;
    }

    public void setCycle(int cycle) {
        this.cycle = cycle;
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
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

    public String getBid() {
        return bid;
    }

    public void setBid(String bid) {
        this.bid = bid;
    }

    public long getHappenTime() {
        return happenTime;
    }

    public void setHappenTime(long happenTime) {
        this.happenTime = happenTime;
    }

    public int getCityCode() {
        return cityCode;
    }

    public void setCityCode(int cityCode) {
        this.cityCode = cityCode;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public int getP() {
        return p;
    }

    public void setP(int p) {
        this.p = p;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public int getFalut() {
        return falut;
    }

    public void setFalut(int falut) {
        this.falut = falut;
    }
}
