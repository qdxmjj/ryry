package com.ruyiruyi.ruyiruyi.ui.multiType;

import com.ruyiruyi.ruyiruyi.ui.model.ServiceType;

import java.util.List;

public class ShopInfo {
    public String storeName;
    private String store_latitude;
    private String store_longitude;
    private String user_latitude;
    private String user_longitude;

    public void setUser_latitude(String user_latitude) {
        this.user_latitude = user_latitude;
    }

    public void setUser_longitude(String user_longitude) {
        this.user_longitude = user_longitude;
    }

    public String getUser_latitude() {

        return user_latitude;
    }

    public String getUser_longitude() {
        return user_longitude;
    }

    public void setStore_latitude(String store_latitude) {
        this.store_latitude = store_latitude;
    }

    public void setStore_longitude(String store_longitude) {
        this.store_longitude = store_longitude;
    }

    public String getStore_latitude() {

        return store_latitude;
    }

    public String getStore_longitude() {
        return store_longitude;
    }

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

    public ShopInfo(String storeName, String store_latitude, String store_longitude, String user_latitude, String user_longitude, String storeTypeName, String storeTypeColor, String storeAddress, String storeDistence, String storePhone, String storeDescribe, List<String> imageList, List<ServiceType> serviceTypeList) {
        this.storeName = storeName;
        this.store_latitude = store_latitude;
        this.store_longitude = store_longitude;
        this.user_latitude = user_latitude;
        this.user_longitude = user_longitude;
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