package com.example.storm.benchmark.produce.cabinet.cabinet_battery_temp.bean;

import java.io.Serializable;
import java.util.List;

public class StationHB3_Datas implements Serializable {
    private List<BatteryHB3> battery;
    private  String pID;

    public List<BatteryHB3> getBattery() {
        return battery;
    }

    public void setBattery(List<BatteryHB3> battery) {
        this.battery = battery;
    }

    public String getpID() {
        return pID;
    }

    @Override
    public String toString() {
        return "StationHB3_Datas{" +
                "battery=" + battery +
                ", pID='" + pID + '\'' +
                '}';
    }

    public void setpID(String pID) {
        this.pID = pID;
    }
}
