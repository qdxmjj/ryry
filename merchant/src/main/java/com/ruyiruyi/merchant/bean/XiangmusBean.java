package com.ruyiruyi.merchant.bean;

public class XiangmusBean {
    private int id ;
    private String color;
    private String name;
    private String time;

    public XiangmusBean() {
    }

    public XiangmusBean(int id, String color, String name, String time) {
        this.id = id;
        this.color = color;
        this.name = name;
        this.time = time;
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
}