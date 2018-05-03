package com.ruyiruyi.ruyiruyi.ui.multiType;

public class EvaImage {
    public int evaluateId;
    public String imageUrl;

    public EvaImage(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public EvaImage() {
    }

    public EvaImage(int evaluateId, String imageUrl) {
        this.evaluateId = evaluateId;
        this.imageUrl = imageUrl;
    }

    public int getEvaluateId() {
        return evaluateId;
    }

    public void setEvaluateId(int evaluateId) {
        this.evaluateId = evaluateId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}