package com.example.storm.benchmark.produce.scooter.bean;

import java.util.Date;
import java.util.List;

public class ScooterBats {
    private List<Bats> bats;
    private Date date;
    private  String sID;
    private  int number;
    private String location;
    private  int soc;

    public int getSoc() {
        return soc;
    }

    public void setSoc(int soc) {
        this.soc = soc;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getsID() {
        return sID;
    }

    public void setsID(String sID) {
        this.sID = sID;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setBats(List<Bats> bats) {
        this.bats = bats;
    }
    public List<Bats> getBats() {
        return bats;
    }

}
