package com.ruyiruyi.ruyiruyi.eventbus;

/**
 * @作者 倾春
 * eml.dongbinjava@163.com
 * @创建日期 2018/10/15 17:27
 */

public class WxLoginEvent {
    private boolean isLoginSuccess;
    private String openid;
    private String nickname;
    private String headimgurl;

    public String getHeadimgurl() {
        return headimgurl;
    }

    public void setHeadimgurl(String headimgurl) {
        this.headimgurl = headimgurl;
    }

    public boolean isLoginSuccess() {
        return isLoginSuccess;
    }

    public void setLoginSuccess(boolean loginSuccess) {
        isLoginSuccess = loginSuccess;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
