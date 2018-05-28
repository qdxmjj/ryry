package com.ruyiruyi.merchant.ui.multiType;

public class PublicBarCode {
    private String barCode;
    private String status;//status:   "1" 一致  "2" 不一致    默认不选 1 一致  选中 2 不一致
    private String id;
    private String orderNo;
    private String abcd;
    private String isShow;//0 不显示   1 显示   Switch

    public PublicBarCode(String barCode, String status, String id, String orderNo, String abcd, String isShow) {
        this.barCode = barCode;
        this.status = status;
        this.id = id;
        this.orderNo = orderNo;
        this.abcd = abcd;
        this.isShow = isShow;
    }

    public PublicBarCode(String barCode, String status, String id, String orderNo, String abcd) {
        this.barCode = barCode;
        this.status = status;
        this.id = id;
        this.orderNo = orderNo;
        this.abcd = abcd;
    }

    public PublicBarCode() {
    }

    public void setShow(String show) {
        isShow = show;
    }

    public String isShow() {
        return isShow;
    }

    public void setAbcd(String abcd) {
        this.abcd = abcd;
    }

    public String getAbcd() {
        return abcd;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getBarCode() {
        return barCode;
    }

    public String getStatus() {
        return status;
    }

    public String getId() {
        return id;
    }

    public String getOrderNo() {
        return orderNo;
    }
}