package com.ruyiruyi.merchant.bean;

public class FreeChangeOldShoeBean {
    private String shoeImgUrl;
    private String shoeName;
    private String orderType;
    private String fontRearFlag;
    private String id;
    private String barCode;
    private String isReach5Years;//0 没有   1  达到
    private String orderNo;
    private long time;
    private String userCarId;
    private String userId;

    public FreeChangeOldShoeBean() {
    }

    public void setShoeImgUrl(String shoeImgUrl) {
        this.shoeImgUrl = shoeImgUrl;
    }

    public void setShoeName(String shoeName) {
        this.shoeName = shoeName;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public void setFontRearFlag(String fontRearFlag) {
        this.fontRearFlag = fontRearFlag;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public void setIsReach5Years(String isReach5Years) {
        this.isReach5Years = isReach5Years;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public void setUserCarId(String userCarId) {
        this.userCarId = userCarId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getShoeImgUrl() {
        return shoeImgUrl;
    }

    public String getShoeName() {
        return shoeName;
    }

    public String getOrderType() {
        return orderType;
    }

    public String getFontRearFlag() {
        return fontRearFlag;
    }

    public String getId() {
        return id;
    }

    public String getBarCode() {
        return barCode;
    }

    public String getIsReach5Years() {
        return isReach5Years;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public long getTime() {
        return time;
    }

    public String getUserCarId() {
        return userCarId;
    }

    public String getUserId() {
        return userId;
    }
}