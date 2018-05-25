package com.ruyiruyi.merchant.bean;

public class BarCode {
    public String barCode;
    public String status;
    public String id;
    public String orderNo;


    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public BarCode() {
    }

    public BarCode(String barCode, String status, String id, String orderNo) {
        this.barCode = barCode;
        this.status = status;
        this.id = id;
        this.orderNo = orderNo;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBarCode() {
        return barCode;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        String str = "";
        str = "{" + "\"barCode\"" + ":" + "\"" + barCode + "\"" + "," + "\"status\"" + ":" + "\"" + status + "\"" + "," + "\"id\"" + ":" + "\"" + id + "\"" + "," + "\"orderNo\"" + ":" + "\"" + orderNo + "\""+ "}";

        return str;
    }
}