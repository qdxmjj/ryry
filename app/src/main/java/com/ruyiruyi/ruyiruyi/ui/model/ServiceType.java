package com.ruyiruyi.ruyiruyi.ui.model;

public class ServiceType {
    public String serviceName;
    public int serviceColor;

    public ServiceType(String serviceName, int serviceColor) {
        this.serviceName = serviceName;
        this.serviceColor = serviceColor;
    }

    public ServiceType() {
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public int getServiceColor() {
        return serviceColor;
    }

    public void setServiceColor(int serviceColor) {
        this.serviceColor = serviceColor;
    }
}