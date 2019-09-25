package com.immotor.bean;

public class TrackInfo {
    private  String sAddr;
    private  String sDistrict;
    private String sTime;
    private  String eTime;
    private String eAddr;
    private String eDistrict;
    private int code;
    private String location;
    private String distance;
    private  String sLocation;
    private String str;
    private  int  soc;

    public int getSoc() {
        return soc;
    }

    public void setSoc(int soc) {
        this.soc = soc;
    }

    public String getsLocation() {
        return sLocation;
    }

    public void setsLocation(String sLocation) {
        this.sLocation = sLocation;
    }

    public TrackInfo(String sAddr, String sDistrict, String sTime, String eTime, String eAddr, String eDistrict, int code, String location, String distance, String str,String sLocation,int soc) {
        this.sAddr = sAddr;
        this.sDistrict = sDistrict;
        this.sTime = sTime;
        this.eTime = eTime;
        this.eAddr = eAddr;
        this.eDistrict = eDistrict;
        this.code = code;
        this.location = location;
        this.distance = distance;
        this.str = str;
        this.sLocation=sLocation;
        this.soc=soc;
    }

    public String getsAddr() {
        return sAddr;
    }

    public void setsAddr(String sAddr) {
        this.sAddr = sAddr;
    }

    public String getsDistrict() {
        return sDistrict;
    }

    public void setsDistrict(String sDistrict) {
        this.sDistrict = sDistrict;
    }

    public String getsTime() {
        return sTime;
    }

    public void setsTime(String sTime) {
        this.sTime = sTime;
    }

    public String geteTime() {
        return eTime;
    }

    public void seteTime(String eTime) {
        this.eTime = eTime;
    }

    public String geteAddr() {
        return eAddr;
    }

    public void seteAddr(String eAddr) {
        this.eAddr = eAddr;
    }

    public String geteDistrict() {
        return eDistrict;
    }

    public void seteDistrict(String eDistrict) {
        this.eDistrict = eDistrict;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }
}
