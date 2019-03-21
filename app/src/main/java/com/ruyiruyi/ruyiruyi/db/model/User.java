package com.ruyiruyi.ruyiruyi.db.model;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.math.BigDecimal;

@Table(name = "user")
public class User {
    @Column(name = "id",isId = true,autoGen = false)
    private int id;

    @Column(name = "carid")
    private int carId;        //用户名

    @Column(name = "authenticatedstate")
    private int authenticatedState;  //是否认证  1认证 2未认证

    @Column(name = "nick")
    private String nick;        //用户名

    @Column(name = "password")
    private String password;    //密码  可不要

    @Column(name = "phone")
    private String phone;       //手机号

    @Column(name = "age")
    private String age;         //年龄   不要了

    @Column(name = "birthday")
    private String birthday;    //出生日期


    @Column(name = "email")
    private String email;       //邮箱

    @Column(name = "gender")
    private int gender;     //性别  1男2女

    @Column(name = "headimgurl")
    private String headimgurl; //头像

    @Column(name = "ml")
    private BigDecimal ml;

    @Column(name = "paypwd")
    private String payPwd;  //支付密码

    @Column(name = "remark")
    private String remark;  //备注

    @Column(name = "creeatetime")
    private String creeateTime; //创建时间

    @Column(name = "updatetime")
    private String updateTime;  //更新时间

    @Column(name = "token")
    private String token;       //token

    @Column(name = "qqinfoId")
    private String qqInfoId; //qq登陆

    @Column(name = "wxinfoId")
    private String wxInfoId;//微信登陆

    @Column(name = "status")
    private String status;  //用户状态 1 已锁定 2 未锁定 默认 2

    @Column(name = "islogin")
    private String isLogin; //0未登陆  1已登陆

    @Column(name = "firstaddcar") //0 未添加 1已添加
    private int firstAddCar;

    public User() {
    }

    public int getCarId() {
        return carId;
    }

    public void setCarId(int carId) {
        this.carId = carId;
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

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getHeadimgurl() {
        return headimgurl;
    }

    public void setHeadimgurl(String headimgurl) {
        this.headimgurl = headimgurl;
    }

    public BigDecimal getMl() {
        return ml;
    }

    public void setMl(BigDecimal ml) {
        this.ml = ml;
    }

    public String getPayPwd() {
        return payPwd;
    }

    public void setPayPwd(String payPwd) {
        this.payPwd = payPwd;
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

    public String getQqInfoId() {
        return qqInfoId;
    }

    public void setQqInfoId(String qqInfoId) {
        this.qqInfoId = qqInfoId;
    }

    public String getWxInfoId() {
        return wxInfoId;
    }

    public void setWxInfoId(String wxInfoId) {
        this.wxInfoId = wxInfoId;
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

    public int getFirstAddCar() {
        return firstAddCar;
    }

    public void setFirstAddCar(int firstAddCar) {
        this.firstAddCar = firstAddCar;
    }

    public int getAuthenticatedState() {
        return authenticatedState;
    }

    public void setAuthenticatedState(int authenticatedState) {
        this.authenticatedState = authenticatedState;
    }
}