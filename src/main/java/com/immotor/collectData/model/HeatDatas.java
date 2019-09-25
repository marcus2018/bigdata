package com.immotor.collectData.model;

import java.util.List;

public class HeatDatas {


    private int ammeterCurrent;
    private int ammeterPower;
    private int ammeterVoltage;
    private int disableTemper;
    private int enableTemper;
    private int heatCurrent;
    private int heatPower;
    private int heatVoltage;
    private boolean isAuto;
    private boolean isEnable;
    private boolean isValid;

    @Override
    public String toString() {
        return "HeatDatas{" +
                "ammeterCurrent=" + ammeterCurrent +
                ", ammeterPower=" + ammeterPower +
                ", ammeterVoltage=" + ammeterVoltage +
                ", disableTemper=" + disableTemper +
                ", enableTemper=" + enableTemper +
                ", heatCurrent=" + heatCurrent +
                ", heatPower=" + heatPower +
                ", heatVoltage=" + heatVoltage +
                ", isAuto=" + isAuto +
                ", isEnable=" + isEnable +
                ", isValid=" + isValid +
                ", meterTime=" + meterTime +
                ", recordTime=" + recordTime +
                ", temperBatteries=" + temperBatteries +
                ", temperCabin=" + temperCabin +
                ", temperCharger=" + temperCharger +
                ", temperPMS=" + temperPMS +
                '}';
    }

    private long meterTime;
    private long recordTime;
    private List<Integer> temperBatteries;
    private int temperCabin;
    private int temperCharger;
    private int temperPMS;
    public void setAmmeterCurrent(int ammeterCurrent) {
        this.ammeterCurrent = ammeterCurrent;
    }
    public int getAmmeterCurrent() {
        return ammeterCurrent;
    }

    public void setAmmeterPower(int ammeterPower) {
        this.ammeterPower = ammeterPower;
    }
    public int getAmmeterPower() {
        return ammeterPower;
    }

    public void setAmmeterVoltage(int ammeterVoltage) {
        this.ammeterVoltage = ammeterVoltage;
    }
    public int getAmmeterVoltage() {
        return ammeterVoltage;
    }

    public void setDisableTemper(int disableTemper) {
        this.disableTemper = disableTemper;
    }
    public int getDisableTemper() {
        return disableTemper;
    }

    public void setEnableTemper(int enableTemper) {
        this.enableTemper = enableTemper;
    }
    public int getEnableTemper() {
        return enableTemper;
    }

    public void setHeatCurrent(int heatCurrent) {
        this.heatCurrent = heatCurrent;
    }
    public int getHeatCurrent() {
        return heatCurrent;
    }

    public void setHeatPower(int heatPower) {
        this.heatPower = heatPower;
    }
    public int getHeatPower() {
        return heatPower;
    }

    public void setHeatVoltage(int heatVoltage) {
        this.heatVoltage = heatVoltage;
    }
    public int getHeatVoltage() {
        return heatVoltage;
    }

    public void setIsAuto(boolean isAuto) {
        this.isAuto = isAuto;
    }
    public boolean getIsAuto() {
        return isAuto;
    }

    public void setIsEnable(boolean isEnable) {
        this.isEnable = isEnable;
    }
    public boolean getIsEnable() {
        return isEnable;
    }

    public void setIsValid(boolean isValid) {
        this.isValid = isValid;
    }
    public boolean getIsValid() {
        return isValid;
    }

    public void setMeterTime(long meterTime) {
        this.meterTime = meterTime;
    }
    public long getMeterTime() {
        return meterTime;
    }

    public void setRecordTime(long recordTime) {
        this.recordTime = recordTime;
    }
    public long getRecordTime() {
        return recordTime;
    }

    public void setTemperBatteries(List<Integer> temperBatteries) {
        this.temperBatteries = temperBatteries;
    }
    public List<Integer> getTemperBatteries() {
        return temperBatteries;
    }

    public void setTemperCabin(int temperCabin) {
        this.temperCabin = temperCabin;
    }
    public int getTemperCabin() {
        return temperCabin;
    }

    public void setTemperCharger(int temperCharger) {
        this.temperCharger = temperCharger;
    }
    public int getTemperCharger() {
        return temperCharger;
    }

    public void setTemperPMS(int temperPMS) {
        this.temperPMS = temperPMS;
    }
    public int getTemperPMS() {
        return temperPMS;
    }

}
