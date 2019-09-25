package com.immotor.collectData.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public class CabinetMonitoringInfo extends  LogCommon {
    private String info;

    public String getInfo() {
        return info;
    }

    public CabinetMonitoringInfo(String info) {
        this.info = info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
