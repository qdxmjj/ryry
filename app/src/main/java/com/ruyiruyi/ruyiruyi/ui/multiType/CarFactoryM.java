package com.ruyiruyi.ruyiruyi.ui.multiType;

public class CarFactoryM {
    public int factoryId;
    public String carFractory;

    public CarFactoryM(int factoryId, String carFractory) {
        this.factoryId = factoryId;
        this.carFractory = carFractory;
    }

    public int getFactoryId() {
        return factoryId;
    }

    public void setFactoryId(int factoryId) {
        this.factoryId = factoryId;
    }

    public String getCarFractory() {
        return carFractory;
    }

    public void setCarFractory(String carFractory) {
        this.carFractory = carFractory;
    }
}