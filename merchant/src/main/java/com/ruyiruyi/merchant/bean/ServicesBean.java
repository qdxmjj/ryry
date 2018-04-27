package com.ruyiruyi.merchant.bean;

public class ServicesBean {
    private int service_id;
    private String serviceInfo;
    private int isChecked;  //0 no 1 yes;


    public ServicesBean() {
    }


    public void setServiceInfo(String serviceInfo) {
        this.serviceInfo = serviceInfo;
    }

    public void setIsChecked(int isChecked) {
        this.isChecked = isChecked;
    }

    public void setService_id(int service_id) {
        this.service_id = service_id;
    }

    public int getService_id() {
        return service_id;
    }

    public String getServiceInfo() {
        return serviceInfo;
    }

    public int getIsChecked() {
        return isChecked;
    }
}