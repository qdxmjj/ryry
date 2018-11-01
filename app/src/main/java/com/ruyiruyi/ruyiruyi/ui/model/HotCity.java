package com.ruyiruyi.ruyiruyi.ui.model;

/**
 * Created by Lenovo on 2018/10/15.
 */

public class HotCity {
    public int definition;
    public int fid;
    public int id;
    public String icon;
    public String name;
    public String time;

    public HotCity(int definition, int fid, int id, String icon, String name, String time) {
        this.definition = definition;
        this.fid = fid;
        this.id = id;
        this.icon = icon;
        this.name = name;
        this.time = time;
    }

    public int getDefinition() {
        return definition;
    }

    public void setDefinition(int definition) {
        this.definition = definition;
    }

    public int getFid() {
        return fid;
    }

    public void setFid(int fid) {
        this.fid = fid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    ;
}
