package com.immotor.collectData.model;

public class BatteryVersionLog  extends LogCommon{
    private String id;               //电池id
    private String sw;                //硬件版本
    private String hw;              //固件版本
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHw() {
        return hw;
    }

    public void setHw(String hw) {
        this.hw = hw;
    }

    public String getSw() {
        return sw;
    }

    public void setSw(String sw) {
        this.sw = sw;
    }

}
