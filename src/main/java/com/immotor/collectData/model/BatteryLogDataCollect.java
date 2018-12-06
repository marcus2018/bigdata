package com.immotor.collectData.model;

public class BatteryLogDataCollect extends  LogCommon {
    private String  pid;//电池柜Id
    private  String id;//电池Id
    private Long collectTime;//数据采集的时间
    private String  logIndex;//日志游标
    private String  partLog;//日志信息，每条事件记录由128字节组成

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

    public String getLogIndex() {
        return logIndex;
    }

    public void setLogIndex(String logIndex) {
        this.logIndex = logIndex;
    }

    public String getPartLog() {
        return partLog;
    }

    public void setPartLog(String partLog) {
        this.partLog = partLog;
    }


}
