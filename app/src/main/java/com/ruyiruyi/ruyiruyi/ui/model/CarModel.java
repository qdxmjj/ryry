package com.ruyiruyi.ruyiruyi.ui.model;

import me.yokeyword.indexablerv.IndexableEntity;

public class CarModel implements IndexableEntity {
    public int carId;
    public String carName;
    public String carIcon;

    public CarModel(int carId, String carName, String carIcon) {
        this.carId = carId;
        this.carName = carName;
        this.carIcon = carIcon;
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

    @Override
    public String getFieldIndexBy() {
        return carName;
    }

    @Override
    public void setFieldIndexBy(String indexField) {

    }

    @Override
    public void setFieldPinyinIndexBy(String pinyin) {

    }
}