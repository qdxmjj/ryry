package com.ruyiruyi.ruyiruyi.ui.model;

public class StoreType {
    public int type;
    public String storeName;


    public StoreType(int type, String storeName) {
        this.type = type;
        this.storeName = storeName;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }
}