package com.ruyiruyi.merchant.db.model;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.math.BigDecimal;

@Table(name = "usermerchant")
public class User {
    @Column(name = "id", isId = true, autoGen = false)
    private int id;

    @Column(name = "producerName")
    private String producerName;

    @Column(name = "producerId")
    private int producerId;

    @Column(name = "phone")
    private String phone;

    @Column(name = "password")
    private String password;

    @Column(name = "storeName")
    private String storeName;

    @Column(name = "storeTypeId")
    private String storeTypeId;

    @Column(name = "tel")
    private String tel;

    @Column(name = "startTime")
    private int startTime;

    @Column(name = "endTime")
    private String endTime;

    @Column(name = "positionId")
    private String positionId;

    @Column(name = "address")
    private String address;

    @Column(name = "longitude")
    private String longitude;

    @Column(name = "latitude")
    private String latitude;

    @Column(name = "storeImgUrl")
    private String storeImgUrl;

    @Column(name = "businessLicenseUrl")
    private String businessLicenseUrl;

    @Column(name = "factoryImgUrl")
    private String factoryImgUrl;

    @Column(name = "indoorImgUrl")
    private String indoorImgUrl;

    @Column(name = "locationImgUrl")
    private String locationImgUrl;

    @Column(name = "idImgUrl")
    private String idImgUrl;

    @Column(name = "appExpert")
    private String appExpert;

    @Column(name = "balance")
    private String balance;

    @Column(name = "cityId")
    private int cityId;

    @Column(name = "createTime")
    private String createTime;

    @Column(name = "situation")
    private String situation;

    @Column(name = "storeLoginName")
    private String storeLoginName;

    @Column(name = "updateTime")
    private String updateTime;

    @Column(name = "token")
    private String token;

    @Column(name = "status")
    private int status;  //用户状态 1 已锁定 2 未锁定 默认 2

    @Column(name = "islogin")
    private String isLogin; //0未登陆  1已登陆

    @Column(name = "isVoice")
    private String isVoice; //关闭语音播报  1开启语音播报

    public User() {
    }

    public void setIsVoice(String isVoice) {
        this.isVoice = isVoice;
    }

    public String getIsVoice() {
        return isVoice;
    }

    public void setStoreImgUrl(String storeImgUrl) {
        this.storeImgUrl = storeImgUrl;
    }

    public String getStoreImgUrl() {
        return storeImgUrl;
    }

    public void setProducerName(String producerName) {
        this.producerName = producerName;
    }

    public void setProducerId(int producerId) {
        this.producerId = producerId;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public void setStoreTypeId(String storeTypeId) {
        this.storeTypeId = storeTypeId;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public void setPositionId(String positionId) {
        this.positionId = positionId;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setBusinessLicenseUrl(String businessLicenseUrl) {
        this.businessLicenseUrl = businessLicenseUrl;
    }

    public void setFactoryImgUrl(String factoryImgUrl) {
        this.factoryImgUrl = factoryImgUrl;
    }

    public void setIndoorImgUrl(String indoorImgUrl) {
        this.indoorImgUrl = indoorImgUrl;
    }

    public void setLocationImgUrl(String locationImgUrl) {
        this.locationImgUrl = locationImgUrl;
    }

    public void setIdImgUrl(String idImgUrl) {
        this.idImgUrl = idImgUrl;
    }

    public void setAppExpert(String appExpert) {
        this.appExpert = appExpert;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public void setSituation(String situation) {
        this.situation = situation;
    }

    public void setStoreLoginName(String storeLoginName) {
        this.storeLoginName = storeLoginName;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setIsLogin(String isLogin) {
        this.isLogin = isLogin;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getProducerName() {
        return producerName;
    }

    public int getProducerId() {
        return producerId;
    }

    public String getPhone() {
        return phone;
    }

    public String getPassword() {
        return password;
    }

    public String getStoreName() {
        return storeName;
    }

    public String getStoreTypeId() {
        return storeTypeId;
    }

    public String getTel() {
        return tel;
    }

    public int getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getPositionId() {
        return positionId;
    }

    public String getAddress() {
        return address;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getBusinessLicenseUrl() {
        return businessLicenseUrl;
    }

    public String getFactoryImgUrl() {
        return factoryImgUrl;
    }

    public String getIndoorImgUrl() {
        return indoorImgUrl;
    }

    public String getLocationImgUrl() {
        return locationImgUrl;
    }

    public String getIdImgUrl() {
        return idImgUrl;
    }

    public String getAppExpert() {
        return appExpert;
    }

    public String getBalance() {
        return balance;
    }

    public int getCityId() {
        return cityId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public String getSituation() {
        return situation;
    }

    public String getStoreLoginName() {
        return storeLoginName;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public String getToken() {
        return token;
    }

    public int getStatus() {
        return status;
    }

    public String getIsLogin() {
        return isLogin;
    }
}