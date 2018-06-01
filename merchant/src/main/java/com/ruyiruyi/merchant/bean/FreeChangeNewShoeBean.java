package com.ruyiruyi.merchant.bean;

public class FreeChangeNewShoeBean {
    private String barcodeImgUrl;
    private String shoeImgUrl;
    private String shoeName;
    private String orderType;
    private String fontRearFlag;
    private String id;
    private String barCode;
    private String orderNo;
    private long time;
    private String status;


    public void setShoeImgUrl(String shoeImgUrl) {
        this.shoeImgUrl = shoeImgUrl;
    }

    public String getShoeImgUrl() {
        return shoeImgUrl;
    }

    public void setBarcodeImgUrl(String barcodeImgUrl) {
        this.barcodeImgUrl = barcodeImgUrl;
    }

    public String getBarcodeImgUrl() {
        return barcodeImgUrl;
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

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getOrderNo() {
        return orderNo;
    }

    public long getTime() {
        return time;
    }

    public String getStatus() {
        return status;
    }

    public FreeChangeNewShoeBean() {
    }
}