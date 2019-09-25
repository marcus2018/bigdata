package com.immotor.collectData.model;

public class ChargeTimeLog  extends  LogCommon{
    private String group   ;  //类型
    private String pid ;         //电池柜id
    private String id;             //电池id
    private Long startTime;   //开始放入时间
    private Long startSoc;                  //放入时电量
    private Long stopTime ;   //结束统计时间，可能是充满电，也可能是拿走
    private Long stopSoc;                  //结束统计电量，统计过程中达到的最大值
    private Boolean onceFull;

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

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

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getStartSoc() {
        return startSoc;
    }

    public void setStartSoc(Long startSoc) {

        this.startSoc = startSoc;
    }

    public Long getStopTime() {
        return stopTime;
    }

    public void setStopTime(Long stopTime) {
        this.stopTime = stopTime;
    }

    public Long getStopSoc() {
        return stopSoc;
    }

    public void setStopSoc(Long stopSoc) {
        this.stopSoc = stopSoc;
    }

    public Boolean getOnceFull() {
        return onceFull;
    }

    public void setOnceFull(Boolean onceFull) {
        this.onceFull = onceFull;
    }


}
