package com.ruyiruyi.ruyiruyi.ui.model;

/**
 * Created by Lenovo on 2018/12/6.
 */

public class Event {
    public int id;
    public String content;
    public String imageUrl;
    public String positionIdList;
    public String positionNameList;
    public int skip;     //0 skip 0 H5不到分享功能 webUrl   1是到H5带分享功能  webUrl  2 是到商品分类列表 serviceid   3是到门店商品界面 stockId
    public String storeIdList;
    public int type;     //1是 一排一个活动  2是一排两个活动 3是一排三活动
    public String webUrl;
    public int stockId;
    public int serviceId;

    public Event(int id, String content, String imageUrl, String positionIdList, String positionNameList, int skip, String storeIdList, int type, String webUrl) {
        this.id = id;
        this.content = content;
        this.imageUrl = imageUrl;
        this.positionIdList = positionIdList;
        this.positionNameList = positionNameList;
        this.skip = skip;
        this.storeIdList = storeIdList;
        this.type = type;
        this.webUrl = webUrl;
    }

    public Event(int id, String content, String imageUrl, String positionIdList, String positionNameList, int skip, String storeIdList, int type, String webUrl, int stockId, int serviceId) {
        this.id = id;
        this.content = content;
        this.imageUrl = imageUrl;
        this.positionIdList = positionIdList;
        this.positionNameList = positionNameList;
        this.skip = skip;
        this.storeIdList = storeIdList;
        this.type = type;
        this.webUrl = webUrl;
        this.stockId = stockId;
        this.serviceId = serviceId;
    }

    public int getStockId() {
        return stockId;
    }

    public void setStockId(int stockId) {
        this.stockId = stockId;
    }

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getPositionIdList() {
        return positionIdList;
    }

    public void setPositionIdList(String positionIdList) {
        this.positionIdList = positionIdList;
    }

    public String getPositionNameList() {
        return positionNameList;
    }

    public void setPositionNameList(String positionNameList) {
        this.positionNameList = positionNameList;
    }

    public int getSkip() {
        return skip;
    }

    public void setSkip(int skip) {
        this.skip = skip;
    }

    public String getStoreIdList() {
        return storeIdList;
    }

    public void setStoreIdList(String storeIdList) {
        this.storeIdList = storeIdList;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }
}
