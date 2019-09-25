package com.immotor.collectData.model;

public class LoghubBattryRTPartReadData extends LogCommon{
    private String  pId;//电池柜Id
    private  String bId;//电池Id
    private Long collectTime;//数据采集的时间
    private String  partReadOnlyData;//BMS只读数据部分，对应寄存器地址256至316
    private Integer port;

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }

    public String getbId() {
        return bId;
    }

    public void setbId(String bId) {
        this.bId = bId;
    }

    public Long getCollectTime() {
        return collectTime;
    }

    public void setCollectTime(Long collectTime) {
        this.collectTime = collectTime;
    }

    public String getPartReadOnlyData() {
        return partReadOnlyData;
    }

    public void setPartReadOnlyData(String partReadOnlyData) {
        this.partReadOnlyData = partReadOnlyData;
    }
}
