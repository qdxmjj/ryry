package com.ruyiruyi.ruyiruyi.ui.multiType;

import java.util.List;

public class Hometop {
    public List<String> imageList;
    public String carTitle;
    public String carImage;
    public String carContent;
    public int state;  //0未登陆  1未添加车辆  2已添加车辆
    public String cityName;

    public Hometop(List<String> imageList, String carTitle, String carContent, int state,String cityName) {
        this.imageList = imageList;
        this.carTitle = carTitle;
        this.carContent = carContent;
        this.state = state;
        this.cityName = cityName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public List<String> getImageList() {
        return imageList;
    }

    public void setImageList(List<String> imageList) {
        this.imageList = imageList;
    }

    public String getCarTitle() {
        return carTitle;
    }

    public void setCarTitle(String carTitle) {
        this.carTitle = carTitle;
    }

    public String getCarImage() {
        return carImage;
    }

    public void setCarImage(String carImage) {
        this.carImage = carImage;
    }

    public String getCarContent() {
        return carContent;
    }

    public void setCarContent(String carContent) {
        this.carContent = carContent;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}