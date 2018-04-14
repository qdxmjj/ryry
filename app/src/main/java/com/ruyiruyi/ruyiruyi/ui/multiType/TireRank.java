package com.ruyiruyi.ruyiruyi.ui.multiType;

public class TireRank {
    public int id;
    public String rankName;
    public String figureName;
    public boolean isCheck;
    public String price;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRankName() {
        return rankName;
    }

    public String getFigureName() {
        return figureName;
    }

    public void setFigureName(String figureName) {
        this.figureName = figureName;
    }

    public void setRankName(String rankName) {
        this.rankName = rankName;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public TireRank(int id, String rankName,String figureName, boolean isCheck,String price) {
        this.id = id;
        this.rankName = rankName;
        this.figureName =figureName;
        this.isCheck = isCheck;
        this.price = price;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}