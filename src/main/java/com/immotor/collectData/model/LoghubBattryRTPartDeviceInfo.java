package com.immotor.collectData.model;

public class LoghubBattryRTPartDeviceInfo extends LogCommon{
    private  String bId;
    private Long collectTime;
    private String pId;
    private String partDeviceInfo;
    private Integer port;
    private Integer type;

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

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }

    public String getPartDeviceInfo() {
        return partDeviceInfo;
    }

    public void setPartDeviceInfo(String partDeviceInfo) {
        this.partDeviceInfo = partDeviceInfo;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
