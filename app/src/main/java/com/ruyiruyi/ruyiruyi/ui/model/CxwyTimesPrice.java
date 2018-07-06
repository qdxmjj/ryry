package com.ruyiruyi.ruyiruyi.ui.model;

public class CxwyTimesPrice {
    public int id;
    public String rate;
    public int times;

    public CxwyTimesPrice(int id, String rate, int times) {
        this.id = id;
        this.rate = rate;
        this.times = times;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public int getTimes() {
        return times;
    }

    public void setTimes(int times) {
        this.times = times;
    }
}