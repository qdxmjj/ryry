package com.ruyiruyi.ruyiruyi.db.model;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

@Table(name = "usertest")
public class UserTest {
    @Column(name =  "id" ,isId = true,autoGen = false)
    private int id;


    @Column(name = "username")
    private String uesrname;

    @Column(name = "age")
    private String age;


    public UserTest() {
    }

    public UserTest(int id, String uesrname) {
        this.id = id;
        this.uesrname = uesrname;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUesrname() {
        return uesrname;
    }

    public void setUesrname(String uesrname) {
        this.uesrname = uesrname;
    }

    public UserTest(int id, String uesrname, String age) {
        this.id = id;
        this.uesrname = uesrname;
        this.age = age;
    }
}