package com.immotor.collectData.model;

public class AndriodBatteryInfo extends  LogCommon{
    private  Integer maybeIn;
    private Integer[] origin;
    private Integer[] inPort;
    private Integer[] outPort;
    private Integer  sureIn;
    private  String  pID;

    public String getpID() {
        return pID;
    }

    public void setpID(String pID) {
        this.pID = pID;
    }

    public Integer getMaybeIn() {
        return maybeIn;
    }

    public void setMaybeIn(Integer maybeIn) {
        this.maybeIn = maybeIn;
    }

    public Integer[] getOrigin() {
        return origin;
    }

    public void setOrigin(Integer[] origin) {
        this.origin = origin;
    }

    public Integer[] getInPort() {
        return inPort;
    }

    public void setInPort(Integer[] intPort) {
        this.inPort = intPort;
    }

    public Integer[] getOutPort() {
        return outPort;
    }

    public void setOutPort(Integer[] outPort) {
        this.outPort = outPort;
    }

    public Integer getSureIn() {
        return sureIn;
    }

    public void setSureIn(Integer sureIn) {
        this.sureIn = sureIn;
    }
}
