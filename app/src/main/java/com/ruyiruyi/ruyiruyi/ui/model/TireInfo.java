package com.ruyiruyi.ruyiruyi.ui.model;

import java.util.List;

/**
 * Created by Lenovo on 2018/8/7.
 */

public class TireInfo {
    public String description;      //描述
    public String imgLeftUrl;       //图片
    public String imgMiddleUrl; //图片
    public String imgRightUrl; //图片
    public String shoeDownImg;      //轮播图
    public String shoeLeftImg;//轮播图
    public String shoeMiddleImg;//轮播图
    public String shoeRightImg;//轮播图
    public String shoeUpImg;//轮播图
    public String detailStr;        //速度级别
    public String figure;       //花纹
    public String shoeBasePrice;    //轮胎基准价 算畅行无忧使用
    public String shoeFlgureName;   //轮胎花纹名称
    public List<CxwyTimesPrice> cxwyTimesPriceList;     //畅行无忧的价格列表
    public List<TireRank> tireRankList;     //速度级别的价格列表
    public String tirePriceOld; //已经买过轮胎的价格
    public String platNumber;   //车牌号
    //public List<CxwyYear> cxwyYearList;

    public TireInfo(String description, String imgLeftUrl, String imgMiddleUrl, String imgRightUrl, String shoeDownImg, String shoeLeftImg, String shoeMiddleImg, String shoeRightImg, String shoeUpImg, String detailStr, String figure, String shoeBasePrice, String shoeFlgureName, List<CxwyTimesPrice> cxwyTimesPriceList, List<TireRank> tireRankList) {
        this.description = description;
        this.imgLeftUrl = imgLeftUrl;
        this.imgMiddleUrl = imgMiddleUrl;
        this.imgRightUrl = imgRightUrl;
        this.shoeDownImg = shoeDownImg;
        this.shoeLeftImg = shoeLeftImg;
        this.shoeMiddleImg = shoeMiddleImg;
        this.shoeRightImg = shoeRightImg;
        this.shoeUpImg = shoeUpImg;
        this.detailStr = detailStr;
        this.figure = figure;
        this.shoeBasePrice = shoeBasePrice;
        this.shoeFlgureName = shoeFlgureName;
        this.cxwyTimesPriceList = cxwyTimesPriceList;
        this.tireRankList = tireRankList;
    }

    public TireInfo(String description, String imgLeftUrl, String imgMiddleUrl, String imgRightUrl, String shoeDownImg, String shoeLeftImg, String shoeMiddleImg, String shoeRightImg, String shoeUpImg, String detailStr, String figure, String shoeBasePrice, String shoeFlgureName, List<CxwyTimesPrice> cxwyTimesPriceList, List<TireRank> tireRankList, String platNumber) {
        this.description = description;
        this.imgLeftUrl = imgLeftUrl;
        this.imgMiddleUrl = imgMiddleUrl;
        this.imgRightUrl = imgRightUrl;
        this.shoeDownImg = shoeDownImg;
        this.shoeLeftImg = shoeLeftImg;
        this.shoeMiddleImg = shoeMiddleImg;
        this.shoeRightImg = shoeRightImg;
        this.shoeUpImg = shoeUpImg;
        this.detailStr = detailStr;
        this.figure = figure;
        this.shoeBasePrice = shoeBasePrice;
        this.shoeFlgureName = shoeFlgureName;
        this.cxwyTimesPriceList = cxwyTimesPriceList;
        this.tireRankList = tireRankList;
        this.platNumber = platNumber;
    }
/*    public TireInfo(String description, String imgLeftUrl, String imgMiddleUrl, String imgRightUrl, String shoeDownImg, String shoeLeftImg, String shoeMiddleImg, String shoeRightImg, String shoeUpImg, String detailStr, String figure, String shoeBasePrice, String shoeFlgureName, List<CxwyTimesPrice> cxwyTimesPriceList, List<TireRank> tireRankList, String platNumber, List<CxwyYear> cxwyYearList) {
        this.description = description;
        this.imgLeftUrl = imgLeftUrl;
        this.imgMiddleUrl = imgMiddleUrl;
        this.imgRightUrl = imgRightUrl;
        this.shoeDownImg = shoeDownImg;
        this.shoeLeftImg = shoeLeftImg;
        this.shoeMiddleImg = shoeMiddleImg;
        this.shoeRightImg = shoeRightImg;
        this.shoeUpImg = shoeUpImg;
        this.detailStr = detailStr;
        this.figure = figure;
        this.shoeBasePrice = shoeBasePrice;
        this.shoeFlgureName = shoeFlgureName;
        this.cxwyTimesPriceList = cxwyTimesPriceList;
        this.tireRankList = tireRankList;
        this.platNumber = platNumber;
        this.cxwyYearList = cxwyYearList;
    }*/

/*    public TireInfo(String description, String imgLeftUrl, String imgMiddleUrl, String imgRightUrl, String shoeDownImg, String shoeLeftImg, String shoeMiddleImg, String shoeRightImg, String shoeUpImg, String detailStr, String figure, String shoeBasePrice, String shoeFlgureName, List<CxwyTimesPrice> cxwyTimesPriceList, List<TireRank> tireRankList, String tirePriceOld, String platNumber, List<CxwyYear> cxwyYearList) {
        this.description = description;
        this.imgLeftUrl = imgLeftUrl;
        this.imgMiddleUrl = imgMiddleUrl;
        this.imgRightUrl = imgRightUrl;
        this.shoeDownImg = shoeDownImg;
        this.shoeLeftImg = shoeLeftImg;
        this.shoeMiddleImg = shoeMiddleImg;
        this.shoeRightImg = shoeRightImg;
        this.shoeUpImg = shoeUpImg;
        this.detailStr = detailStr;
        this.figure = figure;
        this.shoeBasePrice = shoeBasePrice;
        this.shoeFlgureName = shoeFlgureName;
        this.cxwyTimesPriceList = cxwyTimesPriceList;
        this.tireRankList = tireRankList;
        this.tirePriceOld = tirePriceOld;
        this.platNumber = platNumber;
        this.cxwyYearList = cxwyYearList;
    }*/

/*
    public List<CxwyYear> getCxwyYearList() {
        return cxwyYearList;
    }

    public void setCxwyYearList(List<CxwyYear> cxwyYearList) {
        this.cxwyYearList = cxwyYearList;
    }
*/

    public String getTirePriceOld() {
        return tirePriceOld;
    }

    public void setTirePriceOld(String tirePriceOld) {
        this.tirePriceOld = tirePriceOld;
    }

    public String getPlatNumber() {
        return platNumber;
    }

    public void setPlatNumber(String platNumber) {
        this.platNumber = platNumber;
    }

    public String getImgLeftUrl() {
        return imgLeftUrl;
    }

    public void setImgLeftUrl(String imgLeftUrl) {
        this.imgLeftUrl = imgLeftUrl;
    }

    public String getImgMiddleUrl() {
        return imgMiddleUrl;
    }

    public void setImgMiddleUrl(String imgMiddleUrl) {
        this.imgMiddleUrl = imgMiddleUrl;
    }

    public String getImgRightUrl() {
        return imgRightUrl;
    }

    public void setImgRightUrl(String imgRightUrl) {
        this.imgRightUrl = imgRightUrl;
    }

    public String getShoeUpImg() {
        return shoeUpImg;
    }

    public void setShoeUpImg(String shoeUpImg) {
        this.shoeUpImg = shoeUpImg;
    }

    public List<TireRank> getTireRankList() {
        return tireRankList;
    }

    public void setTireRankList(List<TireRank> tireRankList) {
        this.tireRankList = tireRankList;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getShoeDownImg() {
        return shoeDownImg;
    }

    public void setShoeDownImg(String shoeDownImg) {
        this.shoeDownImg = shoeDownImg;
    }

    public String getShoeLeftImg() {
        return shoeLeftImg;
    }

    public void setShoeLeftImg(String shoeLeftImg) {
        this.shoeLeftImg = shoeLeftImg;
    }

    public String getShoeMiddleImg() {
        return shoeMiddleImg;
    }

    public void setShoeMiddleImg(String shoeMiddleImg) {
        this.shoeMiddleImg = shoeMiddleImg;
    }

    public String getShoeRightImg() {
        return shoeRightImg;
    }

    public void setShoeRightImg(String shoeRightImg) {
        this.shoeRightImg = shoeRightImg;
    }

    public String getDetailStr() {
        return detailStr;
    }

    public void setDetailStr(String detailStr) {
        this.detailStr = detailStr;
    }

    public String getFigure() {
        return figure;
    }

    public void setFigure(String figure) {
        this.figure = figure;
    }



    public String getShoeBasePrice() {
        return shoeBasePrice;
    }

    public void setShoeBasePrice(String shoeBasePrice) {
        this.shoeBasePrice = shoeBasePrice;
    }

    public String getShoeFlgureName() {
        return shoeFlgureName;
    }

    public void setShoeFlgureName(String shoeFlgureName) {
        this.shoeFlgureName = shoeFlgureName;
    }

    public List<CxwyTimesPrice> getCxwyTimesPriceList() {
        return cxwyTimesPriceList;
    }

    public void setCxwyTimesPriceList(List<CxwyTimesPrice> cxwyTimesPriceList) {
        this.cxwyTimesPriceList = cxwyTimesPriceList;
    }

}
