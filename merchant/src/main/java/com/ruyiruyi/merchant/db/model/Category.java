package com.ruyiruyi.merchant.db.model;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

@Table(name = "category")
public class Category {

    @Column(name = "id", isId = true, autoGen = false)
    private int id;

    @Column(name = "color")
    private String color;

    @Column(name = "name")
    private String name;

    @Column(name = "time")
    private String time;

    public Category() {
    }

    public Category(int id, String color, String name, String time) {
        this.id = id;
        this.color = color;
        this.name = name;
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public String getColor() {
        return color;
    }

    public String getName() {
        return name;
    }

    public String getTime() {
        return time;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTime(String time) {
        this.time = time;
    }
}