package com.ruyiruyi.ruyiruyi.ui.multiType;

public class CarType {
    public int carType;  // 0当前选择排量  1 当前选择排量  2当前选择车型
    public String pailiang;
    public String year;

    public CarType() {
    }

    public int getCarType() {
        return carType;
    }

    public void setCarType(int carType) {
        this.carType = carType;
    }

    public String getPailiang() {
        return pailiang;
    }

    public void setPailiang(String pailiang) {
        this.pailiang = pailiang;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }
}