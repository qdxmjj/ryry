package com.ruyiruyi.ruyiruyi.ui.multiType;

public class CreditLimit {
    public String carImage;
    public String carName;
    public String carNumber;
    public String creditLimit;
    public String creditLimitRemain;

    public CreditLimit(String carImage, String carName, String carNumber, String creditLimit, String creditLimitRemain) {
        this.carImage = carImage;
        this.carName = carName;
        this.carNumber = carNumber;
        this.creditLimit = creditLimit;
        this.creditLimitRemain = creditLimitRemain;
    }

    public String getCarImage() {
        return carImage;
    }

    public void setCarImage(String carImage) {
        this.carImage = carImage;
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

    public String getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(String creditLimit) {
        this.creditLimit = creditLimit;
    }

    public String getCreditLimitRemain() {
        return creditLimitRemain;
    }

    public void setCreditLimitRemain(String creditLimitRemain) {
        this.creditLimitRemain = creditLimitRemain;
    }
}