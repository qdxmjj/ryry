package com.ruyiruyi.merchant.ui.multiType.modle;

import java.util.List;

public class Service {
    public String serviceName;
    public List<XiaoService> xiaoServiceList;

    public Service(String serviceName, List<XiaoService> xiaoServiceList) {
        this.serviceName = serviceName;
        this.xiaoServiceList = xiaoServiceList;
    }

    public Service() {
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public List<XiaoService> getXiaoServiceList() {
        return xiaoServiceList;
    }

    public void setXiaoServiceList(List<XiaoService> xiaoServiceList) {
        this.xiaoServiceList = xiaoServiceList;
    }
}