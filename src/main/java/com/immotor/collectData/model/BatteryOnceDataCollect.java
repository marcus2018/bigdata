package com.immotor.collectData.model;

public class BatteryOnceDataCollect  extends LogCommon {
    private String  pid;//电池柜Id
    private  String id;//电池Id
    private Long collectTime;//数据采集的时间
    private String  partDeviceInfo;//电池设备信息部分，对应寄存器地址0至32
    private String  partProtectionParameter;//电池保护参数部分，对应寄存器地址773至832

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

    public String getPartDeviceInfo() {
        return partDeviceInfo;
    }

    public void setPartDeviceInfo(String partDeviceInfo) {
        this.partDeviceInfo = partDeviceInfo;
    }

    public String getPartProtectionParameter() {
        return partProtectionParameter;
    }

    public void setPartProtectionParameter(String partProtectionParameter) {
        this.partProtectionParameter = partProtectionParameter;
    }
}
