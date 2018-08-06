package com.ruyiruyi.ruyiruyi.ui.model;

import me.yokeyword.indexablerv.IndexableEntity;

public class CarModel implements IndexableEntity {
    public int carId;
    public String carName;
    public String carIcon;
    public String icon;

    public CarModel(int carId, String carName, String carIcon,String icon) {
        this.carId = carId;
        this.carName = carName;
        this.carIcon = carIcon;
        this.icon = icon;
    }

    public int getCarId() {
        return carId;
    }

    public void setCarId(int carId) {
        this.carId = carId;
    }

    public String getCarName() {
        return carName;
    }

    public void setCarName(String carName) {
        this.carName = carName;
    }

    public String getCarIcon() {
        return carIcon;
    }

    public void setCarIcon(String carIcon) {
        this.carIcon = carIcon;
    }

    //根据排序项
    @Override
    public String getFieldIndexBy() {
        return icon;
    }

    @Override
    public void setFieldIndexBy(String indexField) {

    }

    @Override
    public void setFieldPinyinIndexBy(String pinyin) {

    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}