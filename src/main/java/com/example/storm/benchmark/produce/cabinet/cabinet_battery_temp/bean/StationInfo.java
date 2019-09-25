package com.example.storm.benchmark.produce.cabinet.cabinet_battery_temp.bean;

public class StationInfo {
    private  int cityCode=-1;
    private String sn="-1";
    private String name="-1";

    public StationInfo(int cityCode, String sn, String name) {
        this.cityCode = cityCode;
        this.sn = sn;
        this.name = name;
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
}
