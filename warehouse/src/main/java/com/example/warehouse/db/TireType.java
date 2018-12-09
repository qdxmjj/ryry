package com.example.warehouse.db;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.io.Serializable;

/**
 * Created by Lenovo on 2018/11/15.
 */

@Table(name = "tiretype")
public class TireType implements Serializable {

    @Column(name = "id",isId = true,autoGen = false)
    private int id;

    @Column(name = "inchmm")
    private String inchmm;      //195

    @Column(name = "inch")
    private String inch;        //45

    @Column(name = "diameter")
    private String diameter;        //R16

    @Column(name = "size")
    private String size;        //195/45R16

    @Column(name = "speed")
    private String speed;       //V

    @Column(name = "brand")
    private String brand;       //ROADCRUZA

    @Column(name = "flgureName")
    private String flgureName;  //RA710

    @Column(name = "time")
    private String time;

    @Column(name = "isclick")
    private boolean isclick;

    @Column(name = "count")
    private int count;

    public TireType(int id, String inchmm, String inch, String diameter, String size, String speed, String brand, String flgureName, String time) {
        this.id = id;
        this.inchmm = inchmm;
        this.inch = inch;
        this.diameter = diameter;
        this.size = size;
        this.speed = speed;
        this.brand = brand;
        this.flgureName = flgureName;
        this.time = time;
        this.isclick = false;
        this.count = 0;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public TireType() {
    }

    public boolean isclick() {
        return isclick;
    }

    public void setIsclick(boolean isclick) {
        this.isclick = isclick;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getInchmm() {
        return inchmm;
    }

    public void setInchmm(String inchmm) {
        this.inchmm = inchmm;
    }

    public String getInch() {
        return inch;
    }

    public void setInch(String inch) {
        this.inch = inch;
    }

    public String getDiameter() {
        return diameter;
    }

    public void setDiameter(String diameter) {
        this.diameter = diameter;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getFlgureName() {
        return flgureName;
    }

    public void setFlgureName(String flgureName) {
        this.flgureName = flgureName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
