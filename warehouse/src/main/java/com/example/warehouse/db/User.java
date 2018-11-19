package com.example.warehouse.db;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.math.BigDecimal;

/**
 * Created by Lenovo on 2018/11/14.
 */

@Table(name = "user")
public class User {
    @Column(name = "id",isId = true,autoGen = false)
    private int id;

    @Column(name = "nick")
    private String nick;        //用户名

    @Column(name = "password")
    private String password;    //密码  可不要

    @Column(name = "phone")
    private String phone;       //手机号

    @Column(name = "headimgurl")
    private String headimgurl; //头像

    @Column(name = "remark")
    private String remark;  //备注

    @Column(name = "creeatetime")
    private String creeateTime; //创建时间

    @Column(name = "updatetime")
    private String updateTime;  //更新时间

    @Column(name = "token")
    private String token;       //token

    @Column(name = "status")
    private String status;  //用户状态 1 已锁定 2 未锁定 默认 2

    @Column(name = "islogin")
    private String isLogin; //0未登陆  1已登陆

    public User() {
    }

    public User(int id,  String nick, String password, String phone, String headimgurl, String remark, String creeateTime, String updateTime, String token, String status, String isLogin) {
        this.id = id;
        this.nick = nick;
        this.password = password;
        this.phone = phone;
        this.headimgurl = headimgurl;
        this.remark = remark;
        this.creeateTime = creeateTime;
        this.updateTime = updateTime;
        this.token = token;
        this.status = status;
        this.isLogin = isLogin;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getHeadimgurl() {
        return headimgurl;
    }

    public void setHeadimgurl(String headimgurl) {
        this.headimgurl = headimgurl;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCreeateTime() {
        return creeateTime;
    }

    public void setCreeateTime(String creeateTime) {
        this.creeateTime = creeateTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIsLogin() {
        return isLogin;
    }

    public void setIsLogin(String isLogin) {
        this.isLogin = isLogin;
    }
}
