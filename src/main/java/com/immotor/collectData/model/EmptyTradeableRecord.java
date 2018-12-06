package com.immotor.collectData.model;

public class EmptyTradeableRecord extends  LogCommon {
    private  String pid;
    private String value;

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
