package com.ruyiruyi.ruyiruyi.ui.multiType;

/**
 * @作者 倾春
 * eml.dongbinjava@163.com
 * @创建日期 2018/11/22 14:35
 */

public class MyInviteAward {
    private String phone;
    private long time;
    private String imgUrl;
    private int imgResId;
    private int type;

    private String typeStr;
    private String titleStr;


    public MyInviteAward() {
    }

    public String getTypeStr() {
        return typeStr;
    }

    public void setTypeStr(String typeStr) {
        this.typeStr = typeStr;
    }

    public String getTitleStr() {
        return titleStr;
    }

    public void setTitleStr(String titleStr) {
        this.titleStr = titleStr;
    }

    public int getImgResId() {
        return imgResId;
    }

    public void setImgResId(int imgResId) {
        this.imgResId = imgResId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
