package com.ruyiruyi.ruyiruyi.ui.multiType;

import java.util.List;

public class Hometop {
    public List<String> imageList;
    public String carTitle;
    public String carContent;
    public int state;  //0未登陆  1未添加车辆  2已添加车辆

    public Hometop(List<String> imageList, String carTitle, String carContent, int state) {
        this.imageList = imageList;
        this.carTitle = carTitle;
        this.carContent = carContent;
        this.state = state;
    }
}