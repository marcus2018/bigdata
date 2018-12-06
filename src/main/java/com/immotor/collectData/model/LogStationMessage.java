package com.immotor.collectData.model;

public class LogStationMessage extends  LogCommon {
    private  String message;
    private  String pid;

    public LogStationMessage(String message, String pid) {
        this.message = message;
        this.pid = pid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }
}
