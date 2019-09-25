package com.example.storm.benchmark.produce.cabinet.cabinetFullPort.bean;

public class FullStationStoE {
    private  long sTime;
    private  long eTime;
    private long duration;
    private  String pid;
   private  int status;
    public FullStationStoE(long sTime, long eTime, long duration, String pid,int status) {
        this.sTime = sTime;
        this.eTime = eTime;
        this.duration = duration;
        this.pid = pid;
        this.status=status;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getsTime() {
        return sTime;
    }

    public void setsTime(long sTime) {
        this.sTime = sTime;
    }

    public long geteTime() {
        return eTime;
    }

    public void seteTime(long eTime) {
        this.eTime = eTime;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }
}
