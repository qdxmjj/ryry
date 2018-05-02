package com.ruyiruyi.merchant.bean;

public class XiaoService {
    public String serviceId;
    public String serviceName;
    public String serviceTypeId;
    public String serviceTypeName;

    public XiaoService() {
    }

    public XiaoService(String serviceId, String serviceName, String serviceTypeId, String serviceTypeName) {
        this.serviceId = serviceId;
        this.serviceName = serviceName;
        this.serviceTypeId = serviceTypeId;
        this.serviceTypeName = serviceTypeName;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceTypeId() {
        return serviceTypeId;
    }

    public void setServiceTypeId(String serviceTypeId) {
        this.serviceTypeId = serviceTypeId;
    }

    public String getServiceTypeName() {
        return serviceTypeName;
    }

    public void setServiceTypeName(String serviceTypeName) {
        this.serviceTypeName = serviceTypeName;
    }
}