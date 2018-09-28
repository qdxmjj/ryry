package com.ruyiruyi.ruyiruyi.ui.model;

/**
 * Created by Lenovo on 2018/9/18.
 */

public class CxwyPrice {
    public int times;
    public String cxwyPrice;

    public CxwyPrice(int times, String cxwyPrice) {
        this.times = times;
        this.cxwyPrice = cxwyPrice;
    }

    public int getTimes() {
        return times;
    }

    public void setTimes(int times) {
        this.times = times;
    }

    public String getCxwyPrice() {
        return cxwyPrice;
    }

    public void setCxwyPrice(String cxwyPrice) {
        this.cxwyPrice = cxwyPrice;
    }
}
