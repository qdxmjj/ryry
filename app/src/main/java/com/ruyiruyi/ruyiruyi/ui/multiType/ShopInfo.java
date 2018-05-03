package com.ruyiruyi.ruyiruyi.ui.multiType;

import com.ruyiruyi.ruyiruyi.ui.model.ServiceType;

import java.util.List;

public class ShopInfo {
    public String storeName;
    public String storeTypeName;
    public String storeTypeColor;
    public String storeAddress;
    public String storeDistence;
    public String storePhone;
    public String storeDescribe;
    public List<String> imageList;
    public List<ServiceType> serviceTypeList;



    public ShopInfo(List<String> imageList, List<ServiceType> serviceTypeList) {
        this.imageList = imageList;
        this.serviceTypeList = serviceTypeList;
    }

    public ShopInfo(String storeName, String storeTypeName, String storeTypeColor, String storeAddress, String storeDistence, String storePhone, String storeDescribe, List<String> imageList, List<ServiceType> serviceTypeList) {
        this.storeName = storeName;
        this.storeTypeName = storeTypeName;
        this.storeTypeColor = storeTypeColor;
        this.storeAddress = storeAddress;
        this.storeDistence = storeDistence;
        this.storePhone = storePhone;
        this.storeDescribe = storeDescribe;
        this.imageList = imageList;
        this.serviceTypeList = serviceTypeList;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getStoreTypeName() {
        return storeTypeName;
    }

    public void setStoreTypeName(String storeTypeName) {
        this.storeTypeName = storeTypeName;
    }

    public String getStoreTypeColor() {
        return storeTypeColor;
    }

    public void setStoreTypeColor(String storeTypeColor) {
        this.storeTypeColor = storeTypeColor;
    }

    public String getStoreAddress() {
        return storeAddress;
    }

    public void setStoreAddress(String storeAddress) {
        this.storeAddress = storeAddress;
    }

    public String getStoreDistence() {
        return storeDistence;
    }

    public void setStoreDistence(String storeDistence) {
        this.storeDistence = storeDistence;
    }

    public String getStorePhone() {
        return storePhone;
    }

    public void setStorePhone(String storePhone) {
        this.storePhone = storePhone;
    }

    public String getStoreDescribe() {
        return storeDescribe;
    }

    public void setStoreDescribe(String storeDescribe) {
        this.storeDescribe = storeDescribe;
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