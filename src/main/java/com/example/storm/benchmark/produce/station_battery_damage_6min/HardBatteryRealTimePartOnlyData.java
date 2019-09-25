package com.example.storm.benchmark.produce.station_battery_damage_6min;

import java.util.Date;

public class HardBatteryRealTimePartOnlyData {

    private	String  userid;
    private 	long     state_0 ;
    private	 long     state_1 ;
    private 	long     state_2 ;
    private 	long     state_3 ;
    private	long     state_4 ;
    private	long     state_5 ;
    private	long     state_6 ;
    private	long     state_7 ;
    private	long     state_8 ;
    private	long     state_9 ;
    private	long     state_10;
    private	long     state_11;
    private	long     state_12;
    private	long     state_13;
    private	long     state_14;
    private	long     state_15;
    private	double  soc;
    private	double  tvolt;
    private	double  tcurr;
    private	double  htemp;
    private	long     htnum;
    private	double  ltemp;
    private	long     ltnum;
    private	long     hvolt;
    private	long     hvnum;
    private	long     lvolt;
    private	long     lvnum;
    private	double  dsop  ;
    private	double  csop  ;
    private	double soh   ;
    private	long    cycle ;
    private	long    rcap  ;
    private	long    fcap  ;
    private	long    fctime;
    private	double rpow ;
    private	long devft1_0;
    private	long devft1_1;
    private	long devft1_2;
    private	long devft1_3;
    private	long devft1_4;
    private	long devft1_5;
    private	long devft1_6;
    private	long devft1_7;
    private	long devft1_8;
    private	long devft1_9;

    @Override
    public String toString() {
        return "HardBatteryRealTimePartOnlyData{" +
                "userid='" + userid + '\'' +
                ", state_0=" + state_0 +
                ", state_1=" + state_1 +
                ", state_2=" + state_2 +
                ", state_3=" + state_3 +
                ", state_4=" + state_4 +
                ", state_5=" + state_5 +
                ", state_6=" + state_6 +
                ", state_7=" + state_7 +
                ", state_8=" + state_8 +
                ", state_9=" + state_9 +
                ", state_10=" + state_10 +
                ", state_11=" + state_11 +
                ", state_12=" + state_12 +
                ", state_13=" + state_13 +
                ", state_14=" + state_14 +
                ", state_15=" + state_15 +
                ", soc=" + soc +
                ", tvolt=" + tvolt +
                ", tcurr=" + tcurr +
                ", htemp=" + htemp +
                ", htnum=" + htnum +
                ", ltemp=" + ltemp +
                ", ltnum=" + ltnum +
                ", hvolt=" + hvolt +
                ", hvnum=" + hvnum +
                ", lvolt=" + lvolt +
                ", lvnum=" + lvnum +
                ", dsop=" + dsop +
                ", csop=" + csop +
                ", soh=" + soh +
                ", cycle=" + cycle +
                ", rcap=" + rcap +
                ", fcap=" + fcap +
                ", fctime=" + fctime +
                ", rpow=" + rpow +
                ", devft1_0=" + devft1_0 +
                ", devft1_1=" + devft1_1 +
                ", devft1_2=" + devft1_2 +
                ", devft1_3=" + devft1_3 +
                ", devft1_4=" + devft1_4 +
                ", devft1_5=" + devft1_5 +
                ", devft1_6=" + devft1_6 +
                ", devft1_7=" + devft1_7 +
                ", devft1_8=" + devft1_8 +
                ", devft1_9=" + devft1_9 +
                ", devft1_10=" + devft1_10 +
                ", devft1_11=" + devft1_11 +
                ", devft1_12=" + devft1_12 +
                ", devft1_13=" + devft1_13 +
                ", devft1_14=" + devft1_14 +
                ", devft1_15=" + devft1_15 +
                ", devft2_0=" + devft2_0 +
                ", devft2_1=" + devft2_1 +
                ", devft2_2=" + devft2_2 +
                ", devft2_3=" + devft2_3 +
                ", devft2_4=" + devft2_4 +
                ", devft2_5=" + devft2_5 +
                ", devft2_6=" + devft2_6 +
                ", devft2_7=" + devft2_7 +
                ", devft2_8=" + devft2_8 +
                ", devft2_9=" + devft2_9 +
                ", devft2_10=" + devft2_10 +
                ", devft2_11=" + devft2_11 +
                ", devft2_12=" + devft2_12 +
                ", devft2_13=" + devft2_13 +
                ", devft2_14=" + devft2_14 +
                ", devft2_15=" + devft2_15 +
                ", opft1_0=" + opft1_0 +
                ", opft1_1=" + opft1_1 +
                ", opft1_2=" + opft1_2 +
                ", opft1_3=" + opft1_3 +
                ", opft1_4=" + opft1_4 +
                ", opft1_5=" + opft1_5 +
                ", opft1_6=" + opft1_6 +
                ", opft1_7=" + opft1_7 +
                ", opft1_8=" + opft1_8 +
                ", opft1_9=" + opft1_9 +
                ", opft1_10=" + opft1_10 +
                ", opft1_11=" + opft1_11 +
                ", opft1_12=" + opft1_12 +
                ", opft1_13=" + opft1_13 +
                ", opft1_14=" + opft1_14 +
                ", opft1_15=" + opft1_15 +
                ", opft2_0=" + opft2_0 +
                ", opft2_1=" + opft2_1 +
                ", opft2_2=" + opft2_2 +
                ", opft2_3=" + opft2_3 +
                ", opft2_4=" + opft2_4 +
                ", opft2_5=" + opft2_5 +
                ", opft2_6=" + opft2_6 +
                ", opft2_7=" + opft2_7 +
                ", opft2_8=" + opft2_8 +
                ", opft2_9=" + opft2_9 +
                ", opft2_10=" + opft2_10 +
                ", opft2_11=" + opft2_11 +
                ", opft2_12=" + opft2_12 +
                ", opft2_13=" + opft2_13 +
                ", opft2_14=" + opft2_14 +
                ", opft2_15=" + opft2_15 +
                ", opwarn_0=" + opwarn_0 +
                ", opwarn_1=" + opwarn_1 +
                ", opwarn_2=" + opwarn_2 +
                ", opwarn_3=" + opwarn_3 +
                ", opwarn_4=" + opwarn_4 +
                ", opwarn_5=" + opwarn_5 +
                ", opwarn_6=" + opwarn_6 +
                ", opwarn_7=" + opwarn_7 +
                ", opwarn_8=" + opwarn_8 +
                ", opwarn_9=" + opwarn_9 +
                ", opwarn_10=" + opwarn_10 +
                ", opwarn_11=" + opwarn_11 +
                ", opwarn_12=" + opwarn_12 +
                ", opwarn_13=" + opwarn_13 +
                ", opwarn_14=" + opwarn_14 +
                ", opwarn_15=" + opwarn_15 +
                ", opwarn2=" + opwarn2 +
                ", cmost=" + cmost +
                ", dmost=" + dmost +
                ", fuelt=" + fuelt +
                ", cont=" + cont +
                ", btemp1=" + btemp1 +
                ", btemp2=" + btemp2 +
                ", bvolt1=" + bvolt1 +
                ", bvolt2=" + bvolt2 +
                ", bvolt3=" + bvolt3 +
                ", bvolt4=" + bvolt4 +
                ", bvolt5=" + bvolt5 +
                ", bvolt6=" + bvolt6 +
                ", bvolt7=" + bvolt7 +
                ", bvolt8=" + bvolt8 +
                ", bvolt9=" + bvolt9 +
                ", bvolt10=" + bvolt10 +
                ", bvolt11=" + bvolt11 +
                ", bvolt12=" + bvolt12 +
                ", bvolt13=" + bvolt13 +
                ", bvolt14=" + bvolt14 +
                ", bvolt15=" + bvolt15 +
                ", bvolt16=" + bvolt16 +
                ", balasta_0=" + balasta_0 +
                ", balasta_1=" + balasta_1 +
                ", balasta_2=" + balasta_2 +
                ", balasta_3=" + balasta_3 +
                ", balasta_4=" + balasta_4 +
                ", balasta_5=" + balasta_5 +
                ", balasta_6=" + balasta_6 +
                ", balasta_7=" + balasta_7 +
                ", balasta_8=" + balasta_8 +
                ", balasta_9=" + balasta_9 +
                ", balasta_10=" + balasta_10 +
                ", balasta_11=" + balasta_11 +
                ", balasta_12=" + balasta_12 +
                ", balasta_13=" + balasta_13 +
                ", balasta_14=" + balasta_14 +
                ", balasta_15=" + balasta_15 +
                ", accelerated_speed_x=" + accelerated_speed_x +
                ", accelerated_speed_y=" + accelerated_speed_y +
                ", accelerated_speed_z=" + accelerated_speed_z +
                ", mcu3v3=" + mcu3v3 +
                ", pre_electric_infer_vol=" + pre_electric_infer_vol +
                ", electric_quantity_ma=" + electric_quantity_ma +
                ", max_charge_electric=" + max_charge_electric +
                ", electric_meter_charge=" + electric_meter_charge +

                '}';
    }

    private	long  devft1_10 ;
    private	long  devft1_11 ;
    private	long  devft1_12 ;
    private	long  devft1_13 ;
    private	long  devft1_14 ;
    private	long  devft1_15 ;
    private	long  devft2_0  ;
    private	long  devft2_1  ;
    private	long  devft2_2  ;
    private	long  devft2_3  ;
    private	long  devft2_4  ;
    private	long  devft2_5  ;
    private	long  devft2_6  ;
    private	long  devft2_7  ;
    private	long  devft2_8  ;
    private	long  devft2_9  ;
    private	long  devft2_10 ;
    private	long  devft2_11 ;
    private	long  devft2_12 ;
    private	long  devft2_13 ;
    private	long  devft2_14 ;
    private	long  devft2_15 ;
    private	long  opft1_0 ;
    private	long  opft1_1 ;
    private	long  opft1_2 ;
    private	long  opft1_3 ;
    private	long  opft1_4 ;
    private	long  opft1_5 ;
    private	long  opft1_6 ;
    private	long  opft1_7 ;
    private	long  opft1_8 ;
    private	long  opft1_9 ;
    private	long   opft1_10 ;
    private	long   opft1_11 ;
    private	long   opft1_12 ;
    private	long   opft1_13 ;
    private	long   opft1_14 ;
    private	long   opft1_15 ;
    private	long    opft2_0 ;
    private	long    opft2_1 ;
    private	long    opft2_2 ;
    private	long    opft2_3 ;
    private	long    opft2_4 ;
    private	long    opft2_5 ;
    private	long    opft2_6 ;
    private	long    opft2_7 ;
    private	long    opft2_8 ;
    private	long    opft2_9 ;
    private	long   opft2_10 ;
    private	long   opft2_11 ;
    private	long   opft2_12 ;
    private	long   opft2_13 ;
    private	long   opft2_14 ;
    private	long   opft2_15 ;
    private	long   opwarn_0 ;
    private	long   opwarn_1 ;
    private	long   opwarn_2 ;
    private	long   opwarn_3 ;
    private	long   opwarn_4 ;
    private	long   opwarn_5 ;
    private	long   opwarn_6 ;
    private	long   opwarn_7 ;
    private	long   opwarn_8 ;
    private	long   opwarn_9 ;
    private	 long   opwarn_10;
    private	 long   opwarn_11;
    private	 long   opwarn_12;
    private	 long   opwarn_13;
    private	 long   opwarn_14;
    private	 long   opwarn_15;
    private	 long opwarn2 ;
    private	double  cmost ;
    private	double  dmost ;
    private	double  fuelt ;
    private	double  cont  ;
    private	 double btemp1 ;
    private	 double btemp2 ;
    private	 long bvolt1    ;
    private	 long bvolt2    ;
    private	 long bvolt3    ;
    private	 long bvolt4    ;
    private	 long bvolt5    ;
    private	 long bvolt6    ;
    private	 long bvolt7    ;
    private	 long bvolt8    ;
    private	 long bvolt9    ;
    private	 long bvolt10   ;
    private	 long bvolt11   ;
    private	 long bvolt12   ;
    private	 long bvolt13   ;
    private	 long bvolt14   ;
    private	 long bvolt15   ;
    private	 long bvolt16   ;
    private	 long balasta_0 ;
    private	 long balasta_1 ;
    private	 long balasta_2 ;
    private	 long balasta_3 ;
    private	 long balasta_4 ;
    private	 long balasta_5 ;
    private	 long balasta_6 ;
    private	 long balasta_7 ;
    private	 long balasta_8 ;
    private	 long balasta_9 ;
    private	 long balasta_10;
    private	 long balasta_11;
    private	 long balasta_12;
    private	 long balasta_13;
    private	 long balasta_14;
    private	 long balasta_15;
    private	long accelerated_speed_x  ;
    private	long accelerated_speed_y  ;
    private  String pt;

    public String getPt() {
        return pt;
    }

    public void setPt(String pt) {
        this.pt = pt;
    }

    public Boolean getFlag() {
        return flag;
    }

    public void setFlag(Boolean flag) {
        this.flag = flag;
    }

    private	long accelerated_speed_z  ;
    private	 long mcu3v3;
    private	 long pre_electric_infer_vol;
    private	 long electric_quantity_ma;
    private	 double max_charge_electric;
    private	 long  electric_meter_charge;
    private Date collecttime;
    private	 String  pid;
    private	 String  id;
    private  Boolean flag;
    public int compareTo(HardBatteryRealTimePartOnlyData o) {
        return this.collecttime.compareTo(o.collecttime);
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public long getState_0() {
        return state_0;
    }

    public void setState_0(long state_0) {
        this.state_0 = state_0;
    }

    public long getState_1() {
        return state_1;
    }

    public void setState_1(long state_1) {
        this.state_1 = state_1;
    }

    public long getState_2() {
        return state_2;
    }

    public void setState_2(long state_2) {
        this.state_2 = state_2;
    }

    public long getState_3() {
        return state_3;
    }

    public void setState_3(long state_3) {
        this.state_3 = state_3;
    }

    public long getState_4() {
        return state_4;
    }

    public void setState_4(long state_4) {
        this.state_4 = state_4;
    }

    public long getState_5() {
        return state_5;
    }

    public void setState_5(long state_5) {
        this.state_5 = state_5;
    }

    public long getState_6() {
        return state_6;
    }

    public void setState_6(long state_6) {
        this.state_6 = state_6;
    }

    public long getState_7() {
        return state_7;
    }

    public void setState_7(long state_7) {
        this.state_7 = state_7;
    }

    public long getState_8() {
        return state_8;
    }

    public void setState_8(long state_8) {
        this.state_8 = state_8;
    }

    public long getState_9() {
        return state_9;
    }

    public void setState_9(long state_9) {
        this.state_9 = state_9;
    }

    public long getState_10() {
        return state_10;
    }

    public void setState_10(long state_10) {
        this.state_10 = state_10;
    }

    public long getState_11() {
        return state_11;
    }

    public void setState_11(long state_11) {
        this.state_11 = state_11;
    }

    public long getState_12() {
        return state_12;
    }

    public void setState_12(long state_12) {
        this.state_12 = state_12;
    }

    public long getState_13() {
        return state_13;
    }

    public void setState_13(long state_13) {
        this.state_13 = state_13;
    }

    public long getState_14() {
        return state_14;
    }

    public void setState_14(long state_14) {
        this.state_14 = state_14;
    }

    public long getState_15() {
        return state_15;
    }

    public void setState_15(long state_15) {
        this.state_15 = state_15;
    }

    public double getSoc() {
        return soc;
    }

    public void setSoc(double soc) {
        this.soc = soc;
    }

    public double getTvolt() {
        return tvolt;
    }

    public void setTvolt(double tvolt) {
        this.tvolt = tvolt;
    }

    public double getTcurr() {
        return tcurr;
    }

    public void setTcurr(double tcurr) {
        this.tcurr = tcurr;
    }

    public double getHtemp() {
        return htemp;
    }

    public void setHtemp(double htemp) {
        this.htemp = htemp;
    }

    public long getHtnum() {
        return htnum;
    }

    public void setHtnum(long htnum) {
        this.htnum = htnum;
    }

    public double getLtemp() {
        return ltemp;
    }

    public void setLtemp(double ltemp) {
        this.ltemp = ltemp;
    }

    public long getLtnum() {
        return ltnum;
    }

    public void setLtnum(long ltnum) {
        this.ltnum = ltnum;
    }

    public long getHvolt() {
        return hvolt;
    }

    public void setHvolt(long hvolt) {
        this.hvolt = hvolt;
    }

    public long getHvnum() {
        return hvnum;
    }

    public void setHvnum(long hvnum) {
        this.hvnum = hvnum;
    }

    public long getLvolt() {
        return lvolt;
    }

    public void setLvolt(long lvolt) {
        this.lvolt = lvolt;
    }

    public long getLvnum() {
        return lvnum;
    }

    public void setLvnum(long lvnum) {
        this.lvnum = lvnum;
    }

    public double getDsop() {
        return dsop;
    }

    public void setDsop(double dsop) {
        this.dsop = dsop;
    }

    public double getCsop() {
        return csop;
    }

    public void setCsop(double csop) {
        this.csop = csop;
    }

    public double getSoh() {
        return soh;
    }

    public void setSoh(double soh) {
        this.soh = soh;
    }

    public long getCycle() {
        return cycle;
    }

    public void setCycle(long cycle) {
        this.cycle = cycle;
    }

    public long getRcap() {
        return rcap;
    }

    public void setRcap(long rcap) {
        this.rcap = rcap;
    }

    public long getFcap() {
        return fcap;
    }

    public void setFcap(long fcap) {
        this.fcap = fcap;
    }

    public long getFctime() {
        return fctime;
    }

    public void setFctime(long fctime) {
        this.fctime = fctime;
    }

    public double getRpow() {
        return rpow;
    }

    public void setRpow(double rpow) {
        this.rpow = rpow;
    }

    public long getDevft1_0() {
        return devft1_0;
    }

    public void setDevft1_0(long devft1_0) {
        this.devft1_0 = devft1_0;
    }

    public long getDevft1_1() {
        return devft1_1;
    }

    public void setDevft1_1(long devft1_1) {
        this.devft1_1 = devft1_1;
    }

    public long getDevft1_2() {
        return devft1_2;
    }

    public void setDevft1_2(long devft1_2) {
        this.devft1_2 = devft1_2;
    }

    public long getDevft1_3() {
        return devft1_3;
    }

    public void setDevft1_3(long devft1_3) {
        this.devft1_3 = devft1_3;
    }

    public long getDevft1_4() {
        return devft1_4;
    }

    public void setDevft1_4(long devft1_4) {
        this.devft1_4 = devft1_4;
    }

    public long getDevft1_5() {
        return devft1_5;
    }

    public void setDevft1_5(long devft1_5) {
        this.devft1_5 = devft1_5;
    }

    public long getDevft1_6() {
        return devft1_6;
    }

    public void setDevft1_6(long devft1_6) {
        this.devft1_6 = devft1_6;
    }

    public long getDevft1_7() {
        return devft1_7;
    }

    public void setDevft1_7(long devft1_7) {
        this.devft1_7 = devft1_7;
    }

    public long getDevft1_8() {
        return devft1_8;
    }

    public void setDevft1_8(long devft1_8) {
        this.devft1_8 = devft1_8;
    }

    public long getDevft1_9() {
        return devft1_9;
    }

    public void setDevft1_9(long devft1_9) {
        this.devft1_9 = devft1_9;
    }

    public long getDevft1_10() {
        return devft1_10;
    }

    public void setDevft1_10(long devft1_10) {
        this.devft1_10 = devft1_10;
    }

    public long getDevft1_11() {
        return devft1_11;
    }

    public void setDevft1_11(long devft1_11) {
        this.devft1_11 = devft1_11;
    }

    public long getDevft1_12() {
        return devft1_12;
    }

    public void setDevft1_12(long devft1_12) {
        this.devft1_12 = devft1_12;
    }

    public long getDevft1_13() {
        return devft1_13;
    }

    public void setDevft1_13(long devft1_13) {
        this.devft1_13 = devft1_13;
    }

    public long getDevft1_14() {
        return devft1_14;
    }

    public void setDevft1_14(long devft1_14) {
        this.devft1_14 = devft1_14;
    }

    public long getDevft1_15() {
        return devft1_15;
    }

    public void setDevft1_15(long devft1_15) {
        this.devft1_15 = devft1_15;
    }

    public long getDevft2_0() {
        return devft2_0;
    }

    public void setDevft2_0(long devft2_0) {
        this.devft2_0 = devft2_0;
    }

    public long getDevft2_1() {
        return devft2_1;
    }

    public void setDevft2_1(long devft2_1) {
        this.devft2_1 = devft2_1;
    }

    public long getDevft2_2() {
        return devft2_2;
    }

    public void setDevft2_2(long devft2_2) {
        this.devft2_2 = devft2_2;
    }

    public long getDevft2_3() {
        return devft2_3;
    }

    public void setDevft2_3(long devft2_3) {
        this.devft2_3 = devft2_3;
    }

    public long getDevft2_4() {
        return devft2_4;
    }

    public void setDevft2_4(long devft2_4) {
        this.devft2_4 = devft2_4;
    }

    public long getDevft2_5() {
        return devft2_5;
    }

    public void setDevft2_5(long devft2_5) {
        this.devft2_5 = devft2_5;
    }

    public long getDevft2_6() {
        return devft2_6;
    }

    public void setDevft2_6(long devft2_6) {
        this.devft2_6 = devft2_6;
    }

    public long getDevft2_7() {
        return devft2_7;
    }

    public void setDevft2_7(long devft2_7) {
        this.devft2_7 = devft2_7;
    }

    public long getDevft2_8() {
        return devft2_8;
    }

    public void setDevft2_8(long devft2_8) {
        this.devft2_8 = devft2_8;
    }

    public long getDevft2_9() {
        return devft2_9;
    }

    public void setDevft2_9(long devft2_9) {
        this.devft2_9 = devft2_9;
    }

    public long getDevft2_10() {
        return devft2_10;
    }

    public void setDevft2_10(long devft2_10) {
        this.devft2_10 = devft2_10;
    }

    public long getDevft2_11() {
        return devft2_11;
    }

    public void setDevft2_11(long devft2_11) {
        this.devft2_11 = devft2_11;
    }

    public long getDevft2_12() {
        return devft2_12;
    }

    public void setDevft2_12(long devft2_12) {
        this.devft2_12 = devft2_12;
    }

    public long getDevft2_13() {
        return devft2_13;
    }

    public void setDevft2_13(long devft2_13) {
        this.devft2_13 = devft2_13;
    }

    public long getDevft2_14() {
        return devft2_14;
    }

    public void setDevft2_14(long devft2_14) {
        this.devft2_14 = devft2_14;
    }

    public long getDevft2_15() {
        return devft2_15;
    }

    public void setDevft2_15(long devft2_15) {
        this.devft2_15 = devft2_15;
    }

    public long getOpft1_0() {
        return opft1_0;
    }

    public void setOpft1_0(long opft1_0) {
        this.opft1_0 = opft1_0;
    }

    public long getOpft1_1() {
        return opft1_1;
    }

    public void setOpft1_1(long opft1_1) {
        this.opft1_1 = opft1_1;
    }

    public long getOpft1_2() {
        return opft1_2;
    }

    public void setOpft1_2(long opft1_2) {
        this.opft1_2 = opft1_2;
    }

    public long getOpft1_3() {
        return opft1_3;
    }

    public void setOpft1_3(long opft1_3) {
        this.opft1_3 = opft1_3;
    }

    public long getOpft1_4() {
        return opft1_4;
    }

    public void setOpft1_4(long opft1_4) {
        this.opft1_4 = opft1_4;
    }

    public long getOpft1_5() {
        return opft1_5;
    }

    public void setOpft1_5(long opft1_5) {
        this.opft1_5 = opft1_5;
    }

    public long getOpft1_6() {
        return opft1_6;
    }

    public void setOpft1_6(long opft1_6) {
        this.opft1_6 = opft1_6;
    }

    public long getOpft1_7() {
        return opft1_7;
    }

    public void setOpft1_7(long opft1_7) {
        this.opft1_7 = opft1_7;
    }

    public long getOpft1_8() {
        return opft1_8;
    }

    public void setOpft1_8(long opft1_8) {
        this.opft1_8 = opft1_8;
    }

    public long getOpft1_9() {
        return opft1_9;
    }

    public void setOpft1_9(long opft1_9) {
        this.opft1_9 = opft1_9;
    }

    public long getOpft1_10() {
        return opft1_10;
    }

    public void setOpft1_10(long opft1_10) {
        this.opft1_10 = opft1_10;
    }

    public long getOpft1_11() {
        return opft1_11;
    }

    public void setOpft1_11(long opft1_11) {
        this.opft1_11 = opft1_11;
    }

    public long getOpft1_12() {
        return opft1_12;
    }

    public void setOpft1_12(long opft1_12) {
        this.opft1_12 = opft1_12;
    }

    public long getOpft1_13() {
        return opft1_13;
    }

    public void setOpft1_13(long opft1_13) {
        this.opft1_13 = opft1_13;
    }

    public long getOpft1_14() {
        return opft1_14;
    }

    public void setOpft1_14(long opft1_14) {
        this.opft1_14 = opft1_14;
    }

    public long getOpft1_15() {
        return opft1_15;
    }

    public void setOpft1_15(long opft1_15) {
        this.opft1_15 = opft1_15;
    }

    public long getOpft2_0() {
        return opft2_0;
    }

    public void setOpft2_0(long opft2_0) {
        this.opft2_0 = opft2_0;
    }

    public long getOpft2_1() {
        return opft2_1;
    }

    public void setOpft2_1(long opft2_1) {
        this.opft2_1 = opft2_1;
    }

    public long getOpft2_2() {
        return opft2_2;
    }

    public void setOpft2_2(long opft2_2) {
        this.opft2_2 = opft2_2;
    }

    public long getOpft2_3() {
        return opft2_3;
    }

    public void setOpft2_3(long opft2_3) {
        this.opft2_3 = opft2_3;
    }

    public long getOpft2_4() {
        return opft2_4;
    }

    public void setOpft2_4(long opft2_4) {
        this.opft2_4 = opft2_4;
    }

    public long getOpft2_5() {
        return opft2_5;
    }

    public void setOpft2_5(long opft2_5) {
        this.opft2_5 = opft2_5;
    }

    public long getOpft2_6() {
        return opft2_6;
    }

    public void setOpft2_6(long opft2_6) {
        this.opft2_6 = opft2_6;
    }

    public long getOpft2_7() {
        return opft2_7;
    }

    public void setOpft2_7(long opft2_7) {
        this.opft2_7 = opft2_7;
    }

    public long getOpft2_8() {
        return opft2_8;
    }

    public void setOpft2_8(long opft2_8) {
        this.opft2_8 = opft2_8;
    }

    public long getOpft2_9() {
        return opft2_9;
    }

    public void setOpft2_9(long opft2_9) {
        this.opft2_9 = opft2_9;
    }

    public long getOpft2_10() {
        return opft2_10;
    }

    public void setOpft2_10(long opft2_10) {
        this.opft2_10 = opft2_10;
    }

    public long getOpft2_11() {
        return opft2_11;
    }

    public void setOpft2_11(long opft2_11) {
        this.opft2_11 = opft2_11;
    }

    public long getOpft2_12() {
        return opft2_12;
    }

    public void setOpft2_12(long opft2_12) {
        this.opft2_12 = opft2_12;
    }

    public long getOpft2_13() {
        return opft2_13;
    }

    public void setOpft2_13(long opft2_13) {
        this.opft2_13 = opft2_13;
    }

    public long getOpft2_14() {
        return opft2_14;
    }

    public void setOpft2_14(long opft2_14) {
        this.opft2_14 = opft2_14;
    }

    public long getOpft2_15() {
        return opft2_15;
    }

    public void setOpft2_15(long opft2_15) {
        this.opft2_15 = opft2_15;
    }

    public long getOpwarn_0() {
        return opwarn_0;
    }

    public void setOpwarn_0(long opwarn_0) {
        this.opwarn_0 = opwarn_0;
    }

    public long getOpwarn_1() {
        return opwarn_1;
    }

    public void setOpwarn_1(long opwarn_1) {
        this.opwarn_1 = opwarn_1;
    }

    public long getOpwarn_2() {
        return opwarn_2;
    }

    public void setOpwarn_2(long opwarn_2) {
        this.opwarn_2 = opwarn_2;
    }

    public long getOpwarn_3() {
        return opwarn_3;
    }

    public void setOpwarn_3(long opwarn_3) {
        this.opwarn_3 = opwarn_3;
    }

    public long getOpwarn_4() {
        return opwarn_4;
    }

    public void setOpwarn_4(long opwarn_4) {
        this.opwarn_4 = opwarn_4;
    }

    public long getOpwarn_5() {
        return opwarn_5;
    }

    public void setOpwarn_5(long opwarn_5) {
        this.opwarn_5 = opwarn_5;
    }

    public long getOpwarn_6() {
        return opwarn_6;
    }

    public void setOpwarn_6(long opwarn_6) {
        this.opwarn_6 = opwarn_6;
    }

    public long getOpwarn_7() {
        return opwarn_7;
    }

    public void setOpwarn_7(long opwarn_7) {
        this.opwarn_7 = opwarn_7;
    }

    public long getOpwarn_8() {
        return opwarn_8;
    }

    public void setOpwarn_8(long opwarn_8) {
        this.opwarn_8 = opwarn_8;
    }

    public long getOpwarn_9() {
        return opwarn_9;
    }

    public void setOpwarn_9(long opwarn_9) {
        this.opwarn_9 = opwarn_9;
    }

    public long getOpwarn_10() {
        return opwarn_10;
    }

    public void setOpwarn_10(long opwarn_10) {
        this.opwarn_10 = opwarn_10;
    }

    public long getOpwarn_11() {
        return opwarn_11;
    }

    public void setOpwarn_11(long opwarn_11) {
        this.opwarn_11 = opwarn_11;
    }

    public long getOpwarn_12() {
        return opwarn_12;
    }

    public void setOpwarn_12(long opwarn_12) {
        this.opwarn_12 = opwarn_12;
    }

    public long getOpwarn_13() {
        return opwarn_13;
    }

    public void setOpwarn_13(long opwarn_13) {
        this.opwarn_13 = opwarn_13;
    }

    public long getOpwarn_14() {
        return opwarn_14;
    }

    public void setOpwarn_14(long opwarn_14) {
        this.opwarn_14 = opwarn_14;
    }

    public long getOpwarn_15() {
        return opwarn_15;
    }

    public void setOpwarn_15(long opwarn_15) {
        this.opwarn_15 = opwarn_15;
    }

    public long getOpwarn2() {
        return opwarn2;
    }

    public void setOpwarn2(long opwarn2) {
        this.opwarn2 = opwarn2;
    }

    public double getCmost() {
        return cmost;
    }

    public void setCmost(double cmost) {
        this.cmost = cmost;
    }

    public double getDmost() {
        return dmost;
    }

    public void setDmost(double dmost) {
        this.dmost = dmost;
    }

    public double getFuelt() {
        return fuelt;
    }

    public void setFuelt(double fuelt) {
        this.fuelt = fuelt;
    }

    public double getCont() {
        return cont;
    }

    public void setCont(double cont) {
        this.cont = cont;
    }

    public double getBtemp1() {
        return btemp1;
    }

    public void setBtemp1(double btemp1) {
        this.btemp1 = btemp1;
    }

    public double getBtemp2() {
        return btemp2;
    }

    public void setBtemp2(double btemp2) {
        this.btemp2 = btemp2;
    }

    public long getBvolt1() {
        return bvolt1;
    }

    public void setBvolt1(long bvolt1) {
        this.bvolt1 = bvolt1;
    }

    public long getBvolt2() {
        return bvolt2;
    }

    public void setBvolt2(long bvolt2) {
        this.bvolt2 = bvolt2;
    }

    public long getBvolt3() {
        return bvolt3;
    }

    public void setBvolt3(long bvolt3) {
        this.bvolt3 = bvolt3;
    }

    public long getBvolt4() {
        return bvolt4;
    }

    public void setBvolt4(long bvolt4) {
        this.bvolt4 = bvolt4;
    }

    public long getBvolt5() {
        return bvolt5;
    }

    public void setBvolt5(long bvolt5) {
        this.bvolt5 = bvolt5;
    }

    public long getBvolt6() {
        return bvolt6;
    }

    public void setBvolt6(long bvolt6) {
        this.bvolt6 = bvolt6;
    }

    public long getBvolt7() {
        return bvolt7;
    }

    public void setBvolt7(long bvolt7) {
        this.bvolt7 = bvolt7;
    }

    public long getBvolt8() {
        return bvolt8;
    }

    public void setBvolt8(long bvolt8) {
        this.bvolt8 = bvolt8;
    }

    public long getBvolt9() {
        return bvolt9;
    }

    public void setBvolt9(long bvolt9) {
        this.bvolt9 = bvolt9;
    }

    public long getBvolt10() {
        return bvolt10;
    }

    public void setBvolt10(long bvolt10) {
        this.bvolt10 = bvolt10;
    }

    public long getBvolt11() {
        return bvolt11;
    }

    public void setBvolt11(long bvolt11) {
        this.bvolt11 = bvolt11;
    }

    public long getBvolt12() {
        return bvolt12;
    }

    public void setBvolt12(long bvolt12) {
        this.bvolt12 = bvolt12;
    }

    public long getBvolt13() {
        return bvolt13;
    }

    public void setBvolt13(long bvolt13) {
        this.bvolt13 = bvolt13;
    }

    public long getBvolt14() {
        return bvolt14;
    }

    public void setBvolt14(long bvolt14) {
        this.bvolt14 = bvolt14;
    }

    public long getBvolt15() {
        return bvolt15;
    }

    public void setBvolt15(long bvolt15) {
        this.bvolt15 = bvolt15;
    }

    public long getBvolt16() {
        return bvolt16;
    }

    public void setBvolt16(long bvolt16) {
        this.bvolt16 = bvolt16;
    }

    public long getBalasta_0() {
        return balasta_0;
    }

    public void setBalasta_0(long balasta_0) {
        this.balasta_0 = balasta_0;
    }

    public long getBalasta_1() {
        return balasta_1;
    }

    public void setBalasta_1(long balasta_1) {
        this.balasta_1 = balasta_1;
    }

    public long getBalasta_2() {
        return balasta_2;
    }

    public void setBalasta_2(long balasta_2) {
        this.balasta_2 = balasta_2;
    }

    public long getBalasta_3() {
        return balasta_3;
    }

    public void setBalasta_3(long balasta_3) {
        this.balasta_3 = balasta_3;
    }

    public long getBalasta_4() {
        return balasta_4;
    }

    public void setBalasta_4(long balasta_4) {
        this.balasta_4 = balasta_4;
    }

    public long getBalasta_5() {
        return balasta_5;
    }

    public void setBalasta_5(long balasta_5) {
        this.balasta_5 = balasta_5;
    }

    public long getBalasta_6() {
        return balasta_6;
    }

    public void setBalasta_6(long balasta_6) {
        this.balasta_6 = balasta_6;
    }

    public long getBalasta_7() {
        return balasta_7;
    }

    public void setBalasta_7(long balasta_7) {
        this.balasta_7 = balasta_7;
    }

    public long getBalasta_8() {
        return balasta_8;
    }

    public void setBalasta_8(long balasta_8) {
        this.balasta_8 = balasta_8;
    }

    public long getBalasta_9() {
        return balasta_9;
    }

    public void setBalasta_9(long balasta_9) {
        this.balasta_9 = balasta_9;
    }

    public long getBalasta_10() {
        return balasta_10;
    }

    public void setBalasta_10(long balasta_10) {
        this.balasta_10 = balasta_10;
    }

    public long getBalasta_11() {
        return balasta_11;
    }

    public void setBalasta_11(long balasta_11) {
        this.balasta_11 = balasta_11;
    }

    public long getBalasta_12() {
        return balasta_12;
    }

    public void setBalasta_12(long balasta_12) {
        this.balasta_12 = balasta_12;
    }

    public long getBalasta_13() {
        return balasta_13;
    }

    public void setBalasta_13(long balasta_13) {
        this.balasta_13 = balasta_13;
    }

    public long getBalasta_14() {
        return balasta_14;
    }

    public void setBalasta_14(long balasta_14) {
        this.balasta_14 = balasta_14;
    }

    public long getBalasta_15() {
        return balasta_15;
    }

    public void setBalasta_15(long balasta_15) {
        this.balasta_15 = balasta_15;
    }

    public long getAccelerated_speed_x() {
        return accelerated_speed_x;
    }

    public void setAccelerated_speed_x(long accelerated_speed_x) {
        this.accelerated_speed_x = accelerated_speed_x;
    }

    public long getAccelerated_speed_y() {
        return accelerated_speed_y;
    }

    public void setAccelerated_speed_y(long accelerated_speed_y) {
        this.accelerated_speed_y = accelerated_speed_y;
    }

    public long getAccelerated_speed_z() {
        return accelerated_speed_z;
    }

    public void setAccelerated_speed_z(long accelerated_speed_z) {
        this.accelerated_speed_z = accelerated_speed_z;
    }

    public long getMcu3v3() {
        return mcu3v3;
    }

    public void setMcu3v3(long mcu3v3) {
        this.mcu3v3 = mcu3v3;
    }

    public long getPre_electric_infer_vol() {
        return pre_electric_infer_vol;
    }

    public void setPre_electric_infer_vol(long pre_electric_infer_vol) {
        this.pre_electric_infer_vol = pre_electric_infer_vol;
    }

    public long getElectric_quantity_ma() {
        return electric_quantity_ma;
    }

    public void setElectric_quantity_ma(long electric_quantity_ma) {
        this.electric_quantity_ma = electric_quantity_ma;
    }

    public double getMax_charge_electric() {
        return max_charge_electric;
    }

    public void setMax_charge_electric(double max_charge_electric) {
        this.max_charge_electric = max_charge_electric;
    }

    public long getElectric_meter_charge() {
        return electric_meter_charge;
    }

    public void setElectric_meter_charge(long electric_meter_charge) {
        this.electric_meter_charge = electric_meter_charge;
    }

    public Date getCollecttime() {
        return collecttime;
    }

    public void setCollecttime(Date collecttime) {
        this.collecttime = collecttime;
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
