package com.ruyiruyi.merchant.ui.multiType.modle;

public class Promotion {
    private String promotion;
    private String reward;
    private String way;

    public void setReward(String reward) {
        this.reward = reward;
    }

    public void setWay(String way) {
        this.way = way;
    }

    public String getReward() {
        return reward;
    }

    public String getWay() {
        return way;
    }

    public void setPromotion(String promotion) {
        this.promotion = promotion;
    }

    public String getPromotion() {

        return promotion;
    }

    public Promotion() {

    }

    public Promotion(String promotion, String reward, String way) {
        this.promotion = promotion;
        this.reward = reward;
        this.way = way;
    }
}