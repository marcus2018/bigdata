package com.immotor.collectData.model;

public class CabinetRecordLog extends  LogCommon {

    private String pid;//柜子id
    private String starttime;//开始统计柜子时间
    private String stoptime;//结束统计柜子时间
    private Double meterrecord; //电表读数
//    private Long blockid;//仓号
//    private Long emptytimeduring; //空仓时间段
//    private Long chargetimeduring;//充电时间段
//    private Long fulltimeduring;//满电不充时间段
    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getStoptime() {
        return stoptime;
    }

    public void setStoptime(String stoptime) {
        this.stoptime = stoptime;
    }

    public Double getMeterrecord() {
        return meterrecord;
    }

    public void setMeterrecord(Double meterrecord) {
        this.meterrecord = meterrecord;
    }




}
