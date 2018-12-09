package com.ruyiruyi.ruyiruyi.ui.multiType;

/**
 * @作者 倾春
 * eml.dongbinjava@163.com
 * @创建日期 2018/11/22 14:35
 */

public class MyInvitePerson {
    private String phone;
    private long time;
    private String imgUrl;
    private int imgResId;
    private int status;

    public MyInvitePerson() {
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
