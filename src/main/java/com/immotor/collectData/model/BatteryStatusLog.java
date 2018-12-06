package com.immotor.collectData.model;

public class BatteryStatusLog  extends LogCommon{
    private String  id ;// 电池id
    private Long  port;//充电仓号
//    private Long  energy;//电量
//    private Long  time;//时间
//    private Long  temperature;// 温度
//    private Long  electricity;//电流
//    private Long  voltage;//电压
    private String value;
    private String  pid ;// 电池pid
    private  Long  outValidSoc;//电池可借出时的电量
    private  Long  outValidSocTime;//电池充到可借出电量时的时间

    public Long getOutValidSoc() {
        return outValidSoc;
    }

    public void setOutValidSoc(Long outValidSoc) {
        this.outValidSoc = outValidSoc;
    }



    public Long getOutValidSocTime() {
        return outValidSocTime;
    }

    public void setOutValidSocTime(Long outValidSocTime) {
        this.outValidSocTime = outValidSocTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getPort() {
        return port;
    }

    public void setPort(Long port) {
        this.port = port;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }


}
