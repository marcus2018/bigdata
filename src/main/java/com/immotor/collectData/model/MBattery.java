package com.immotor.collectData.model;

public class MBattery {


    private Integer current;
    private long cycle;
    private long damage;

    public long getCurrent() {
        return current;
    }

    public void setCurrent(Integer current) {
        this.current = current;
    }

    public long getCycle() {
        return cycle;
    }

    public void setCycle(long cycle) {
        this.cycle = cycle;
    }

    public long getDamage() {
        return damage;
    }

    public void setDamage(long damage) {
        this.damage = damage;
    }

    public long getDesignCapacity() {
        return designCapacity;
    }

    public void setDesignCapacity(long designCapacity) {
        this.designCapacity = designCapacity;
    }

    public long getFault() {
        return fault;
    }

    public void setFault(long fault) {
        this.fault = fault;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getNominalCurrent() {
        return nominalCurrent;
    }

    public void setNominalCurrent(long nominalCurrent) {
        this.nominalCurrent = nominalCurrent;
    }

    public long getNominalVoltage() {
        return nominalVoltage;
    }

    public void setNominalVoltage(long nominalVoltage) {
        this.nominalVoltage = nominalVoltage;
    }

    public long getPort() {
        return port;
    }

    public void setPort(long port) {
        this.port = port;
    }

    public long getSoc() {
        return soc;
    }

    public void setSoc(long soc) {
        this.soc = soc;
    }

    public long getSoh() {
        return soh;
    }

    public void setSoh(long soh) {
        this.soh = soh;
    }

    public long getTemperature() {
        return temperature;
    }

    public void setTemperature(long temperature) {
        this.temperature = temperature;
    }

    public long getVoltage() {
        return voltage;
    }

    public void setVoltage(long voltage) {
        this.voltage = voltage;
    }

    private long designCapacity;
    private long fault;
    private String id;
    private long nominalCurrent;
    private long nominalVoltage;
    private long port;
    private long soc;
    private long soh;
    private long temperature;
    private long voltage;

}