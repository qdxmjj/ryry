package com.ruyiruyi.ruyiruyi.ui.multiType;

import com.ruyiruyi.ruyiruyi.ui.model.ServiceType;

import java.util.List;

public class ShopInfo {
    public List<String> imageList;
    public List<ServiceType> serviceTypeList;


    public ShopInfo(List<String> imageList, List<ServiceType> serviceTypeList) {
        this.imageList = imageList;
        this.serviceTypeList = serviceTypeList;
    }

    public List<String> getImageList() {
        return imageList;
    }

    public void setImageList(List<String> imageList) {
        this.imageList = imageList;
    }

    public List<ServiceType> getServiceTypeList() {
        return serviceTypeList;
    }

    public void setServiceTypeList(List<ServiceType> serviceTypeList) {
        this.serviceTypeList = serviceTypeList;
    }
}