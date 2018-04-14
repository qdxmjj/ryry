package com.ruyiruyi.merchant.db.model;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

@Table(name = "province")
public class Province {

    @Column(name = "id",isId = true,autoGen = false)
    private int id;

    @Column(name = "fid")
    private int fid;

    @Column(name = "definition")
    private int definition;

    @Column(name = "name")
    private String name;

    @Column(name = "time")
    private String time;

    public Province() {
    }

    public Province(int id, int fid, int definition, String name, String time) {
        this.id = id;
        this.fid = fid;
        this.definition = definition;
        this.name = name;
        this.time = time;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFid() {
        return fid;
    }

    public void setFid(int fid) {
        this.fid = fid;
    }

    public int getDefinition() {
        return definition;
    }

    public void setDefinition(int definition) {
        this.definition = definition;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}