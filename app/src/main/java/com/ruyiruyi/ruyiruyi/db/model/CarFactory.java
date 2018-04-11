package com.ruyiruyi.ruyiruyi.db.model;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.sql.Timestamp;

@Table(name = "carfactory")
public class CarFactory {
    @Column(name = "id",isId = true,autoGen = false)
    private int id;

    @Column(name =  "carbrandid")
    private int carBrandId;

    @Column(name = "factory")
    private String factory;

    @Column(name = "time")
    private String time;

    public CarFactory() {
    }


    public CarFactory(int id, int carBrandId, String factory, String time) {
        this.id = id;
        this.carBrandId = carBrandId;
        this.factory = factory;
        this.time = time;
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

    public String getFactory() {
        return factory;
    }

    public void setFactory(String factory) {
        this.factory = factory;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}