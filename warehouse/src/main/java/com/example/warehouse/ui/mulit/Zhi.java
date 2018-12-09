package com.example.warehouse.ui.mulit;

/**
 * Created by Lenovo on 2018/11/19.
 */
public class Zhi {
    public String zhiName;
    public boolean isClick;

    public Zhi(String zhiName, boolean isClick) {
        this.zhiName = zhiName;
        this.isClick = isClick;
    }

    public String getZhiName() {
        return zhiName;
    }

    public void setZhiName(String zhiName) {
        this.zhiName = zhiName;
    }

    public boolean isClick() {
        return isClick;
    }

    public void setClick(boolean click) {
        isClick = click;
    }
}