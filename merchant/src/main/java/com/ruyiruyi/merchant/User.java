package com.ruyiruyi.merchant;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

@Table(name = "user")
public class User {
    @Column(name = "id",isId = true,autoGen = true)
    private int id ;

    @Column(name = "username")
    private String username;

    public User(String username) {
        this.username = username;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}