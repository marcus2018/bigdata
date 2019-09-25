package com.example.storm.bean;

import java.io.Serializable;
import java.util.Date;

public class StationLost implements Serializable {
    private  Date date;
    private  String pid;

    private Long duration;
    private  Integer times;

    public StationLost(Date date, String pid, Long duration, Integer times) {
        this.date = date;
        this.pid = pid;
        this.duration = duration;
        this.times = times;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public Integer getTimes() {
        return times;
    }

    public void setTimes(Integer times) {
        this.times = times;
    }
}
