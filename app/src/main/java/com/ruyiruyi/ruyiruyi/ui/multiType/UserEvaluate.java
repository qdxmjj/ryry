package com.ruyiruyi.ruyiruyi.ui.multiType;

import java.util.List;

public class UserEvaluate {
    public int evaluateId;
    public int starNo;
    public String userImage;
    public String usetName;
    public String evaluateTime;
    public String evaluateContent;
    public List<String> evaluateImageList;
    public String storeImage;
    public String storeName;
    public String storeAddress;

    public UserEvaluate() {
    }

    public UserEvaluate(int evaluateId, int starNo, String userImage, String usetName, String evaluateTime, String evaluateContent, List<String> evaluateImageList) {
        this.evaluateId = evaluateId;
        this.starNo = starNo;
        this.userImage = userImage;
        this.usetName = usetName;
        this.evaluateTime = evaluateTime;
        this.evaluateContent = evaluateContent;
        this.evaluateImageList = evaluateImageList;
    }

    public UserEvaluate(int evaluateId, int starNo, String userImage, String usetName, String evaluateTime, String evaluateContent, List<String> evaluateImageList, String storeImage, String storeName, String storeAddress) {
        this.evaluateId = evaluateId;
        this.starNo = starNo;
        this.userImage = userImage;
        this.usetName = usetName;
        this.evaluateTime = evaluateTime;
        this.evaluateContent = evaluateContent;
        this.evaluateImageList = evaluateImageList;
        this.storeImage = storeImage;
        this.storeName = storeName;
        this.storeAddress = storeAddress;
    }

    public int getStarNo() {
        return starNo;
    }

    public void setStarNo(int starNo) {
        this.starNo = starNo;
    }

    public String getStoreImage() {
        return storeImage;
    }

    public void setStoreImage(String storeImage) {
        this.storeImage = storeImage;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getStoreAddress() {
        return storeAddress;
    }

    public void setStoreAddress(String storeAddress) {
        this.storeAddress = storeAddress;
    }

    public int getEvaluateId() {
        return evaluateId;
    }

    public void setEvaluateId(int evaluateId) {
        this.evaluateId = evaluateId;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getUsetName() {
        return usetName;
    }

    public void setUsetName(String usetName) {
        this.usetName = usetName;
    }

    public String getEvaluateTime() {
        return evaluateTime;
    }

    public void setEvaluateTime(String evaluateTime) {
        this.evaluateTime = evaluateTime;
    }

    public String getEvaluateContent() {
        return evaluateContent;
    }

    public void setEvaluateContent(String evaluateContent) {
        this.evaluateContent = evaluateContent;
    }

    public List<String> getEvaluateImageList() {
        return evaluateImageList;
    }

    public void setEvaluateImageList(List<String> evaluateImageList) {
        this.evaluateImageList = evaluateImageList;
    }
}