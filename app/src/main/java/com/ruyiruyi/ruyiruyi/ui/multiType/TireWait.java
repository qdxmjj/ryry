package com.ruyiruyi.ruyiruyi.ui.multiType;

public class TireWait {

    public String tireImage;
    public String tireTitle;
    public String username;
    public int tireCount;
    public String carName;
    public String tirePlace;
    public String orderNo;
    public Boolean rejectStatus;

    public TireWait(String tireImage, String tireTitle, String username, int tireCount, String carName, String tirePlace, String orderNo, Boolean rejectStatus) {
        this.tireImage = tireImage;
        this.tireTitle = tireTitle;
        this.username = username;
        this.tireCount = tireCount;
        this.carName = carName;
        this.tirePlace = tirePlace;
        this.orderNo = orderNo;
        this.rejectStatus = rejectStatus;
    }

    public int getTireCount() {
        return tireCount;
    }

    public void setTireCount(int tireCount) {
        this.tireCount = tireCount;
    }

    public Boolean getRejectStatus() {
        return rejectStatus;
    }

    public void setRejectStatus(Boolean rejectStatus) {
        this.rejectStatus = rejectStatus;
    }

    public TireWait() {
    }

    public String getTireImage() {
        return tireImage;
    }

    public void setTireImage(String tireImage) {
        this.tireImage = tireImage;
    }

    public String getTireTitle() {
        return tireTitle;
    }

    public void setTireTitle(String tireTitle) {
        this.tireTitle = tireTitle;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }



    public String getCarName() {
        return carName;
    }

    public void setCarName(String carName) {
        this.carName = carName;
    }

    public String getTirePlace() {
        return tirePlace;
    }

    public void setTirePlace(String tirePlace) {
        this.tirePlace = tirePlace;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }
}