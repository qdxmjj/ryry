package com.ruyiruyi.ruyiruyi.ui.model;

import java.io.Serializable;

public class ServiceType implements Serializable{
    public String serviceName;
    public String serviceColor;

    public ServiceType(String serviceName, String serviceColor) {
        this.serviceName = serviceName;
        this.serviceColor = serviceColor;
    }

    public String getServiceColor() {
        return serviceColor;
    }

    public void setServiceColor(String serviceColor) {
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


}