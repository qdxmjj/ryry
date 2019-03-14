package com.ruyiruyi.ruyiruyi.ui.model;

public class Car {
    public int carId;

    public int userCarId;


    public String carName;

    public String carNumber;

    public String carIcon;

    public int moren;

    public int proveStatus ;

    public Car(int carId, int userCarId, String carName, String carNumber, String carIcon, int moren) {
        this.carId = carId;
        this.userCarId = userCarId;
        this.carName = carName;
        this.carNumber = carNumber;
        this.carIcon = carIcon;
        this.moren = moren;

        this.proveStatus = 0;//默认0未认证
    }

    public Car(int carId, int userCarId, String carName, String carNumber, String carIcon, int moren, int proveStatus) {
        this.carId = carId;
        this.userCarId = userCarId;
        this.carName = carName;
        this.carNumber = carNumber;
        this.carIcon = carIcon;
        this.moren = moren;
        this.proveStatus = proveStatus;
    }

    public Car() {
        this.proveStatus = 0;//默认0未认证
    }

    public int getProveStatus() {
        return proveStatus;
    }

    public void setProveStatus(int proveStatus) {
        this.proveStatus = proveStatus;
    }

    public int getUserCarId() {
        return userCarId;
    }

    public void setUserCarId(int userCarId) {
        this.userCarId = userCarId;
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

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public String getCarIcon() {
        return carIcon;
    }

    public void setCarIcon(String carIcon) {
        this.carIcon = carIcon;
    }

    public int getMoren() {
        return moren;
    }

    public void setMoren(int moren) {
        this.moren = moren;
    }
}