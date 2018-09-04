package com.ruyiruyi.ruyiruyi.ui.model;

import java.util.List;

/**
 * Created by Lenovo on 2018/8/8.
 */

public class TireRank {
    public String shoeId;       //轮胎id
    public String rankName;     //速度级别名称
    public List<TirePrice> tirePriceList;       //轮胎价格列表

    public TireRank(String shoeId, String rankName, List<TirePrice> tirePriceList) {
        this.shoeId = shoeId;
        this.rankName = rankName;
        this.tirePriceList = tirePriceList;
    }

    public String getShoeId() {
        return shoeId;
    }

    public void setShoeId(String shoeId) {
        this.shoeId = shoeId;
    }

    public List<TirePrice> getTirePriceList() {
        return tirePriceList;
    }

    public void setTirePriceList(List<TirePrice> tirePriceList) {
        this.tirePriceList = tirePriceList;
    }

    public String getRankName() {
        return rankName;
    }

    public void setRankName(String rankName) {
        this.rankName = rankName;
    }
}
