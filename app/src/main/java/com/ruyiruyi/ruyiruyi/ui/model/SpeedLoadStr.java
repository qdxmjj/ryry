package com.ruyiruyi.ruyiruyi.ui.model;

/**
 * Created by Lenovo on 2018/8/7.
 */

public class SpeedLoadStr {
    public int serviceYear;
    public String speedLoadStr;

    public SpeedLoadStr(int serviceYear, String speedLoadStr) {
        this.serviceYear = serviceYear;
        this.speedLoadStr = speedLoadStr;
    }

    public int getServiceYear() {
        return serviceYear;
    }

    public void setServiceYear(int serviceYear) {
        this.serviceYear = serviceYear;
    }

    public String getSpeedLoadStr() {
        return speedLoadStr;
    }

    public void setSpeedLoadStr(String speedLoadStr) {
        this.speedLoadStr = speedLoadStr;
    }
}
