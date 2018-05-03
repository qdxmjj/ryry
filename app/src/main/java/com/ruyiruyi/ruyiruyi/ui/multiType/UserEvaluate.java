package com.ruyiruyi.ruyiruyi.ui.multiType;

import java.util.List;

public class UserEvaluate {
    public int evaluateId;
    public String userImage;
    public String usetName;
    public String evaluateTime;
    public String evaluateContent;
    public List<String> evaluateImageList;

    public UserEvaluate() {
    }

    public UserEvaluate(int evaluateId, String userImage, String usetName, String evaluateTime, String evaluateContent, List<String> evaluateImageList) {
        this.evaluateId = evaluateId;
        this.userImage = userImage;
        this.usetName = usetName;
        this.evaluateTime = evaluateTime;
        this.evaluateContent = evaluateContent;
        this.evaluateImageList = evaluateImageList;
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