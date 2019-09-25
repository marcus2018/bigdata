package com.example.storm.benchmark.produce.cabinet.cabinetLogInfoIndex.bean;

import java.util.List;

public class LogInfo {
    private List<String> list;
    private String pId;
    private  int type;

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
