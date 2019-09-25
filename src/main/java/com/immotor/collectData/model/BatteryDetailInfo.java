package com.immotor.collectData.model;

import org.codehaus.jackson.annotate.JsonProperty;

public class BatteryDetailInfo extends  LogCommon{

    @JsonProperty("mBattery")
    private MBattery mBattery;
    @JsonProperty("pID")
    private String pID;
    public void setMBattery(MBattery mBattery) {
        this.mBattery = mBattery;
    }
    public MBattery getMBattery() {
        return mBattery;
    }

    public void setPID(String pID) {
        this.pID = pID;
    }
    public String getPID() {
        return pID;
    }

}