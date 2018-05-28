package com.ruyiruyi.merchant.ui.multiType;

public class PublicShoeFlag {
    private String shoeImgUrl;
    private String shoeName;
    private String orderType;
    private String shoeFlag;
    private String shoeAmount;
    private String hasTopline;//0 没有   1  有
    private String hasBottomline;//0 没有   1  有

    public PublicShoeFlag() {
    }
    public PublicShoeFlag(String shoeImgUrl, String shoeName, String orderType, String shoeFlag, String shoeAmount) {
        this.shoeImgUrl = shoeImgUrl;
        this.shoeName = shoeName;
        this.orderType = orderType;
        this.shoeFlag = shoeFlag;
        this.shoeAmount = shoeAmount;
    }

    public void setHasTopline(String hasTopline) {
        this.hasTopline = hasTopline;
    }

    public void setHasBottomline(String hasBottomline) {
        this.hasBottomline = hasBottomline;
    }

    public String getHasTopline() {
        return hasTopline;
    }

    public String getHasBottomline() {
        return hasBottomline;
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

    public void setShoeFlag(String shoeFlag) {
        this.shoeFlag = shoeFlag;
    }

    public void setShoeAmount(String shoeAmount) {
        this.shoeAmount = shoeAmount;
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

    public String getShoeFlag() {
        return shoeFlag;
    }

    public String getShoeAmount() {
        return shoeAmount;
    }
}