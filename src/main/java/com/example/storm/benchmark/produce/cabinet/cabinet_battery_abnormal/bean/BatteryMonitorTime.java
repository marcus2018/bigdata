package com.example.storm.benchmark.produce.cabinet.cabinet_battery_abnormal.bean;

import java.io.Serializable;
import java.util.Date;

public class BatteryMonitorTime implements Serializable {
    private double soh;
    private int cycle;
    private int highTempWarning;
    private int lowTempWarning;
    private int overChargingWarning;
    private int code;

    public int getBatDisBalance() {
        return batDisBalance;
    }

    public void setBatDisBalance(int batDisBalance) {
        this.batDisBalance = batDisBalance;
    }

    private  int batDisBalance;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "BatteryMonitorTime{" +
                "soh=" + soh +
                ", cycle=" + cycle +
                ", highTempWarning=" + highTempWarning +
                ", lowTempWarning=" + lowTempWarning +
                ", overChargingWarning=" + overChargingWarning +
                ", overDisChargingWarning=" + overDisChargingWarning +
                ", chargingMOS=" + chargingMOS +
                ", disChargingMOS=" + disChargingMOS +
                ", overVoltage=" + overVoltage +
                ", overfall=" + overfall +
                ", date=" + date +
                ", pid='" + pid + '\'' +
                ", id='" + id + '\'' +
                '}';
    }

    private int overDisChargingWarning;
    private int chargingMOS;
    private int disChargingMOS;
    private int  overVoltage;
    private  int overfall;
    private Date  date;
    private String pid;
    private String id;

    public double getSoh() {
        return soh;
    }

    public void setSoh(double soh) {
        this.soh = soh;
    }

    public int getCycle() {
        return cycle;
    }

    public void setCycle(int cycle) {
        this.cycle = cycle;
    }

    public int getHighTempWarning() {
        return highTempWarning;
    }

    public void setHighTempWarning(int highTempWarning) {
        this.highTempWarning = highTempWarning;
    }

    public int getLowTempWarning() {
        return lowTempWarning;
    }

    public void setLowTempWarning(int lowTempWarning) {
        this.lowTempWarning = lowTempWarning;
    }

    public int getOverChargingWarning() {
        return overChargingWarning;
    }

    public void setOverChargingWarning(int overChargingWarning) {
        this.overChargingWarning = overChargingWarning;
    }

    public int getOverDisChargingWarning() {
        return overDisChargingWarning;
    }

    public void setOverDisChargingWarning(int overDisChargingWarning) {
        this.overDisChargingWarning = overDisChargingWarning;
    }

    public int getChargingMOS() {
        return chargingMOS;
    }

    public void setChargingMOS(int chargingMOS) {
        this.chargingMOS = chargingMOS;
    }

    public int getDisChargingMOS() {
        return disChargingMOS;
    }

    public void setDisChargingMOS(int disChargingMOS) {
        this.disChargingMOS = disChargingMOS;
    }

    public int getOverVoltage() {
        return overVoltage;
    }

    public void setOverVoltage(int overVoltage) {
        this.overVoltage = overVoltage;
    }

    public int getOverfall() {
        return overfall;
    }

    public void setOverfall(int overfall) {
        this.overfall = overfall;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
