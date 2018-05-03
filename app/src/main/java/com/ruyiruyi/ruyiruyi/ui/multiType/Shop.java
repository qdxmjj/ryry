package com.ruyiruyi.ruyiruyi.ui.multiType;

import com.ruyiruyi.ruyiruyi.ui.model.ServiceType;
import com.ruyiruyi.ruyiruyi.ui.model.StoreType;

import java.util.List;

public class Shop {
    public int storeId;
    public String storeTypeName;
    public String storeTypreColoe;
    public String storeName;
    public String storeImage;
    public String storeAddress;
    public String storeDistence;

    public List<ServiceType> serviceTypeList;

    public Shop(int storeId, String storeTypeName, String storeTypreColoe, String storeName, String storeImage, String storeAddress, String storeDistence) {
        this.storeId = storeId;
        this.storeTypeName = storeTypeName;
        this.storeTypreColoe = storeTypreColoe;
        this.storeName = storeName;
        this.storeImage = storeImage;
        this.storeAddress = storeAddress;
        this.storeDistence = storeDistence;
    }

    public Shop(int storeId, String storeTypeName, String storeTypreColoe, String storeName, String storeImage, String storeAddress, String storeDistence, List<ServiceType> serviceTypeList) {
        this.storeId = storeId;
        this.storeTypeName = storeTypeName;
        this.storeTypreColoe = storeTypreColoe;
        this.storeName = storeName;
        this.storeImage = storeImage;
        this.storeAddress = storeAddress;
        this.storeDistence = storeDistence;
        this.serviceTypeList = serviceTypeList;
    }

    public Shop() {
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public String getStoreTypeName() {
        return storeTypeName;
    }

    public void setStoreTypeName(String storeTypeName) {
        this.storeTypeName = storeTypeName;
    }

    public String getStoreTypreColoe() {
        return storeTypreColoe;
    }

    public void setStoreTypreColoe(String storeTypreColoe) {
        this.storeTypreColoe = storeTypreColoe;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getStoreImage() {
        return storeImage;
    }

    public void setStoreImage(String storeImage) {
        this.storeImage = storeImage;
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

    public List<ServiceType> getServiceTypeList() {
        return serviceTypeList;
    }

    public void setServiceTypeList(List<ServiceType> serviceTypeList) {
        this.serviceTypeList = serviceTypeList;
    }
}