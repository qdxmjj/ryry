package com.ruyiruyi.ruyiruyi.ui.model;

/**
 * Created by Lenovo on 2018/8/7.
 */

public class TirePrice {
    public int serviceYear; //服务年限 轮胎价格
    public int tirePrice;

    public TirePrice(int serviceYear, int tirePrice) {
        this.serviceYear = serviceYear;
        this.tirePrice = tirePrice;
    }

    public int getServiceYear() {
        return serviceYear;
    }

    public void setServiceYear(int serviceYear) {
        this.serviceYear = serviceYear;
    }

    public int getTirePrice() {
        return tirePrice;
    }

    public void setTirePrice(int tirePrice) {
        this.tirePrice = tirePrice;
    }
}
