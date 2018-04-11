package com.ruyiruyi.ruyiruyi.db.model;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

@Table(name = "carverhicle")
public class CarVerhicle {
    @Column(name = "id",isId = true,autoGen = false)
    private int id;

    @Column(name = "verhicle")
    private String verhicle;

    @Column(name = "carbrandid")
    private int carBrandId;

    @Column(name = "factoryid")
    private int factoryId;

    @Column(name = "carversion")
    private String carVersion;

    @Column(name = "verify")
    private int verify;

    @Column(name = "time")
    private String time;

    public CarVerhicle() {
    }

    public CarVerhicle(int id, String verhicle, int carBrandId, int factoryId, String carVersion, int verify, String time) {
        this.id = id;
        this.verhicle = verhicle;
        this.carBrandId = carBrandId;
        this.factoryId = factoryId;
        this.carVersion = carVersion;
        this.verify = verify;
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getVerhicle() {
        return verhicle;
    }

    public void setVerhicle(String verhicle) {
        this.verhicle = verhicle;
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

    public int getVerify() {
        return verify;
    }

    public void setVerify(int verify) {
        this.verify = verify;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}