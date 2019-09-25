package com.example.storm.benchmark.produce.cabinet.cabinetLogInfoIndex.bean;

public class BatteryLogOmitInfo  implements Comparable<BatteryLogOmitInfo> {
    private  Long  beginIndex;
    private  Long  endIndex;
    private  String  beginTime;
    private  String  endTime;

    public BatteryLogOmitInfo(Long beginIndex, Long endIndex, String beginTime, String endTime) {
        this.beginIndex = beginIndex;
        this.endIndex = endIndex;
        this.beginTime = beginTime;
        this.endTime = endTime;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Long getBeginIndex() {
        return beginIndex;
    }

    public void setBeginIndex(Long beginIndex) {
        this.beginIndex = beginIndex;
    }

    public Long getEndIndex() {
        return endIndex;
    }

    public void setEndIndex(Long endIndex) {
        this.endIndex = endIndex;
    }



    @Override
    public int compareTo(BatteryLogOmitInfo o) {
        return this.beginIndex.compareTo(o.beginIndex);
    }
}
