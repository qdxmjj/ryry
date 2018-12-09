package com.example.warehouse.ui.mulit;

/**
 * Created by Lenovo on 2018/11/19.
 */
public class Huawen {
    public int id;
    public String huawenName;
    public boolean isClick;

    public Huawen(int id, String huawenName, boolean isClick) {
        this.id = id;
        this.huawenName = huawenName;
        this.isClick = isClick;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHuawenName() {
        return huawenName;
    }

    public void setHuawenName(String huawenName) {
        this.huawenName = huawenName;
    }

    public boolean isClick() {
        return isClick;
    }

    public void setClick(boolean click) {
        isClick = click;
    }
}