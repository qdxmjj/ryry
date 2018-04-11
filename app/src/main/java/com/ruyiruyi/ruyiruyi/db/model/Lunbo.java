package com.ruyiruyi.ruyiruyi.db.model;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

@Table(name = "lunbo")
public class Lunbo {
    @Column(name = "id",isId = true,autoGen = false)
    public int id;

    @Column(name= "imageurl")
    public String contentImageUrl;

    @Column(name = "content")
    public String contentText;

    public Lunbo() {
    }

    public Lunbo(int id, String contentImageUrl, String contentText) {
        this.id = id;
        this.contentImageUrl = contentImageUrl;
        this.contentText = contentText;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContentImageUrl() {
        return contentImageUrl;
    }

    public void setContentImageUrl(String contentImageUrl) {
        this.contentImageUrl = contentImageUrl;
    }

    public String getContentText() {
        return contentText;
    }

    public void setContentText(String contentText) {
        this.contentText = contentText;
    }
}