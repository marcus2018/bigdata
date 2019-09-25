package com.immotor.collectData.model;

public class BatteryLostIndex {
    private  String LostIndex;
    private String LostLength;
    private String LostTime;

    public BatteryLostIndex(String lostIndex, String lostLength, String lostTime) {
        LostIndex = lostIndex;
        LostLength = lostLength;
        LostTime = lostTime;
    }

    public String getLostIndex() {
        return LostIndex;
    }

    public void setLostIndex(String lostIndex) {
        LostIndex = lostIndex;
    }

    public String getLostLength() {
        return LostLength;
    }

    public void setLostLength(String lostLength) {
        LostLength = lostLength;
    }

    public String getLostTime() {
        return LostTime;
    }

    public void setLostTime(String lostTime) {
        LostTime = lostTime;
    }
}
