package com.ruyiruyi.ruyiruyi.ui.multiType;

import java.io.Serializable;

public class RoadChoose implements Serializable {

    public int roadId;
    public boolean isChoose;
    public String imageUrl;
    public String title;
    public String content;

    public RoadChoose(int roadId, boolean isChoose, String imageUrl, String title, String content) {
        this.roadId = roadId;
        this.isChoose = isChoose;
        this.imageUrl = imageUrl;
        this.title = title;
        this.content = content;
    }

    public int getRoadId() {
        return roadId;
    }

    public void setRoadId(int roadId) {
        this.roadId = roadId;
    }

    public boolean isChoose() {
        return isChoose;
    }

    public void setChoose(boolean choose) {
        isChoose = choose;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return roadId +";"+isChoose+";"+imageUrl+";"+title+";"+content;
    }
}