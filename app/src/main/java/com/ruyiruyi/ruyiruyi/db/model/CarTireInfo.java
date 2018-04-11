package com.ruyiruyi.ruyiruyi.db.model;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

@Table(name = "cartireinfo")
public class CarTireInfo {
    @Column(name = "id" , isId = true,autoGen = false)
    private int id;

    @Column(name = "brand")
    private String brand;
    @Column(name = "carbrandid")
    private int carBrandId;
    @Column(name = "verhicle")
    private String verhicle;
    @Column(name = "verhicle_id")
    private int verhicle_id;
    @Column(name = "pailiang")
    private String pailiang;
    @Column(name = "year")
    private String year;
    @Column(name = "name")
    private String name;
    @Column(name = "font")
    private String font;
    @Column(name = "rear")
    private String rear;
    @Column(name = "time")
    private String time;

    public CarTireInfo() {
    }

    public CarTireInfo(int id, String brand, int carBrandId, String verhicle, int verhicle_id, String pailiang, String year, String name, String font, String rear, String time) {
        this.id = id;
        this.brand = brand;
        this.carBrandId = carBrandId;
        this.verhicle = verhicle;
        this.verhicle_id = verhicle_id;
        this.pailiang = pailiang;
        this.year = year;
        this.name = name;
        this.font = font;
        this.rear = rear;
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public int getCarBrandId() {
        return carBrandId;
    }

    public void setCarBrandId(int carBrandId) {
        this.carBrandId = carBrandId;
    }

    public String getVerhicle() {
        return verhicle;
    }

    public void setVerhicle(String verhicle) {
        this.verhicle = verhicle;
    }

    public int getVerhicle_id() {
        return verhicle_id;
    }

    public void setVerhicle_id(int verhicle_id) {
        this.verhicle_id = verhicle_id;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFont() {
        return font;
    }

    public void setFont(String font) {
        this.font = font;
    }

    public String getRear() {
        return rear;
    }

    public void setRear(String rear) {
        this.rear = rear;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}