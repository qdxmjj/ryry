package com.ruyiruyi.ruyiruyi.ui.multiType;

public class PublicCheckNum {
    private String title;
    private int storage;//上限
    private int current_num;//当前数量
    private String hasBottomline;//1 有  0 没有

    public PublicCheckNum() {
    }

    public PublicCheckNum(String title, int storage, int current_num, String hasBottomline) {
        this.title = title;
        this.storage = storage;
        this.current_num = current_num;
        this.hasBottomline = hasBottomline;
    }

    public void setStorage(int storage) {
        this.storage = storage;
    }

    public void setCurrent_num(int current_num) {
        this.current_num = current_num;
    }

    public int getStorage() {
        return storage;
    }

    public int getCurrent_num() {
        return current_num;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setHasBottomline(String hasBottomline) {
        this.hasBottomline = hasBottomline;
    }

    public String getTitle() {
        return title;
    }


    public String getHasBottomline() {
        return hasBottomline;
    }
}