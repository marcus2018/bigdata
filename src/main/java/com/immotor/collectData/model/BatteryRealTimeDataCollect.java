package com.immotor.collectData.model;

public class BatteryRealTimeDataCollect extends LogCommon{
    private String  pid;//电池柜Id
    private  String id;//电池Id
    private Long collectTime;//数据采集的时间
    private String  partReadOnlyData;//BMS只读数据部分，对应寄存器地址256至316
    private String  partRunControlData;//BMS运行控制数据部分，对应寄存器地址512至538

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getPartRunControlData() {
        return partRunControlData;
    }

    public void setPartRunControlData(String partRunControlData) {
        this.partRunControlData = partRunControlData;
    }


}
