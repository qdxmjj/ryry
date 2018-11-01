package com.ruyiruyi.ruyiruyi.ui.model;

/**
 * Created by Lenovo on 2018/10/2.
 */

public class HotActivity {
    public int id;
    public String imageUrl;
    public String webUrl;

    public HotActivity(int id, String imageUrl, String webUrl) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.webUrl = webUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }
}
