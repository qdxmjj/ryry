package com.ruyiruyi.ruyiruyi.ui.multiType;

public class CarVersion {

    public int id;
    public int carBrandId;
    public int factoryId;
    public String carVersion;
    public String carFractory;

    public CarVersion(int id, int carBrandId, int factoryId, String carVersion, String carFractory) {
        this.id = id;
        this.carBrandId = carBrandId;
        this.factoryId = factoryId;
        this.carVersion = carVersion;
        this.carFractory = carFractory;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCarBrandId() {
        return carBrandId;
    }

    public void setCarBrandId(int carBrandId) {
        this.carBrandId = carBrandId;
    }

    public int getFactoryId() {
        return factoryId;
    }

    public void setFactoryId(int factoryId) {
        this.factoryId = factoryId;
    }

    public String getCarVersion() {
        return carVersion;
    }

    public void setCarVersion(String carVersion) {
        this.carVersion = carVersion;
    }

    public String getCarFractory() {
        return carFractory;
    }

    public void setCarFractory(String carFractory) {
        this.carFractory = carFractory;
    }
}