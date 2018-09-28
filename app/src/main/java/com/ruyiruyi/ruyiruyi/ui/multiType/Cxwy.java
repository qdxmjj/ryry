package com.ruyiruyi.ruyiruyi.ui.multiType;

public class Cxwy {
    public int cxwyId;
    public String cxwyStartTime;
    public String cxwyEndTime;
    public int cxwyType; //1是赠送  2是购买
    public boolean isChoose;

    public Cxwy(int cxwyId, String cxwyStartTime, String cxwyEndTime, int cxwyType) {
        this.cxwyId = cxwyId;
        this.cxwyStartTime = cxwyStartTime;
        this.cxwyEndTime = cxwyEndTime;
        this.cxwyType = cxwyType;
        isChoose = false;
    }

    public Cxwy(int cxwyId, String cxwyStartTime, String cxwyEndTime, int cxwyType, boolean isChoose) {
        this.cxwyId = cxwyId;
        this.cxwyStartTime = cxwyStartTime;
        this.cxwyEndTime = cxwyEndTime;
        this.cxwyType = cxwyType;
        this.isChoose = isChoose;
    }

    public boolean isChoose() {
        return isChoose;
    }

    public void setChoose(boolean choose) {
        isChoose = choose;
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