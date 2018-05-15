package com.ruyiruyi.ruyiruyi.ui.multiType;

public class Cxwy {
    public int cxwyId;
    public String cxwyStartTime;
    public String cxwyEndTime;
    public int cxwyType; //1是购买  2是赠送

    public Cxwy(int cxwyId, String cxwyStartTime, String cxwyEndTime, int cxwyType) {
        this.cxwyId = cxwyId;
        this.cxwyStartTime = cxwyStartTime;
        this.cxwyEndTime = cxwyEndTime;
        this.cxwyType = cxwyType;
    }

    public int getCxwyId() {
        return cxwyId;
    }

    public void setCxwyId(int cxwyId) {
        this.cxwyId = cxwyId;
    }

    public String getCxwyStartTime() {
        return cxwyStartTime;
    }

    public void setCxwyStartTime(String cxwyStartTime) {
        this.cxwyStartTime = cxwyStartTime;
    }

    public String getCxwyEndTime() {
        return cxwyEndTime;
    }

    public void setCxwyEndTime(String cxwyEndTime) {
        this.cxwyEndTime = cxwyEndTime;
    }

    public int getCxwyType() {
        return cxwyType;
    }

    public void setCxwyType(int cxwyType) {
        this.cxwyType = cxwyType;
    }
}