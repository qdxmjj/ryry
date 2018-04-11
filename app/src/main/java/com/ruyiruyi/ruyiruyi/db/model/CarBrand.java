package com.ruyiruyi.ruyiruyi.db.model;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

@Table(name = "carbrand")
public class CarBrand {
    @Column(name = "id",isId = true,autoGen = false)
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "imageurl")
    private String imageUrl;

    @Column(name = "icon")
    private String icon;


    @Column(name = "time")
    private String time;

    public CarBrand() {
    }

    public CarBrand(int id, String name, String imageUrl, String icon, String time) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.icon = icon;
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}