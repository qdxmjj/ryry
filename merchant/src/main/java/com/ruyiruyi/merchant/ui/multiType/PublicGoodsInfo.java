package com.ruyiruyi.merchant.ui.multiType;

public class PublicGoodsInfo {
    private String detailImage;
    private String detailName;
    private String detailPrice;
    private String detailTotalPrice;
    private String amount;
    private String hasTopline;//0 无  1 有
    private String hasBottomline;//0 无  1 有

    private String detailId;
    private String detailServiceId;
    private String detailServiceTypeId;


    public PublicGoodsInfo() {
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

    public void setDetailImage(String detailImage) {
        this.detailImage = detailImage;
    }

    public void setDetailName(String detailName) {
        this.detailName = detailName;
    }

    public void setDetailPrice(String detailPrice) {
        this.detailPrice = detailPrice;
    }

    public void setDetailTotalPrice(String detailTotalPrice) {
        this.detailTotalPrice = detailTotalPrice;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public void setDetailId(String detailId) {
        this.detailId = detailId;
    }

    public void setDetailServiceId(String detailServiceId) {
        this.detailServiceId = detailServiceId;
    }

    public void setDetailServiceTypeId(String detailServiceTypeId) {
        this.detailServiceTypeId = detailServiceTypeId;
    }

    public String getDetailImage() {
        return detailImage;
    }

    public String getDetailName() {
        return detailName;
    }

    public String getDetailPrice() {
        return detailPrice;
    }

    public String getDetailTotalPrice() {
        return detailTotalPrice;
    }

    public String getAmount() {
        return amount;
    }

    public String getDetailId() {
        return detailId;
    }

    public String getDetailServiceId() {
        return detailServiceId;
    }

    public String getDetailServiceTypeId() {
        return detailServiceTypeId;
    }
}