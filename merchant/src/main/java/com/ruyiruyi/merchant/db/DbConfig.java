package com.ruyiruyi.merchant.db;

import android.os.Environment;

//import com.ruyiruyi.merchant.db.model.Lunbo;
//import com.ruyiruyi.merchant.db.model.User;

import com.ruyiruyi.merchant.db.model.User;

import org.xutils.DbManager;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

public class DbConfig {

    public DbManager.DaoConfig getDaoConfig() {
        DbManager.DaoConfig daoConfig = new DbManager.DaoConfig()
                .setDbName("merchant.db")
                .setAllowTransaction(true)
                .setDbDir(Environment.getExternalStorageDirectory())
                .setDbVersion(1);
        return daoConfig;
    }

    public DbManager getDbManager() {
        DbManager.DaoConfig daoConfig = getDaoConfig();
        DbManager db = x.getDb(daoConfig);
        return db;
    }

    public User getUserByPhone(String phone) {
        DbManager.DaoConfig daoConfig = getDaoConfig();
        DbManager db = x.getDb(daoConfig);
        try {
            List<User> users = db.selector(User.class)
                    .findAll();
            if (users != null) {
                for (int i = 0; i < users.size(); i++) {
                    User user = users.get(i);
                    if (user.getPhone().equals(phone)) {
                        return user;
                    }
                }
            }

        } catch (DbException e) {
        }
        return null;
    }

    public User getUser() {
        DbManager.DaoConfig daoConfig = getDaoConfig();
        DbManager db = x.getDb(daoConfig);
        try {
            List<User> users = db.selector(User.class)
                    .findAll();
            if (users != null) {
                for (int i = 0; i < users.size(); i++) {
                    User user = users.get(i);
                    if (user.getIsLogin().equals("1")) {
                        return user;
                    }
                }
            }

        } catch (DbException e) {
        }
        return null;
    }

    public Boolean getIsLogin() {
        DbManager.DaoConfig daoConfig = getDaoConfig();
        DbManager db = x.getDb(daoConfig);
        try {
            List<User> users = db.selector(User.class)
                    .findAll();
            if (users != null) {
                for (int i = 0; i < users.size(); i++) {
                    User user = users.get(i);
                    if (user.getIsLogin().equals("1")) {
                        return true;
                    }
                }
            }

        } catch (DbException e) {
        }
        return false;
    }

    public String getToken() {
        DbManager.DaoConfig daoConfig = getDaoConfig();
        DbManager db = x.getDb(daoConfig);
        try {
            List<User> users = db.selector(User.class)
                    .findAll();
            if (users != null) {
                for (int i = 0; i < users.size(); i++) {
                    User user = users.get(i);
                    if (user.getIsLogin().equals("1")) {
                        String token = user.getToken();
                        return token;
                    }
                }
            }

        } catch (DbException e) {
        }
        return null;
    }

    public int getId() {
        DbManager.DaoConfig daoConfig = getDaoConfig();
        DbManager db = x.getDb(daoConfig);
        try {
            List<User> users = db.selector(User.class)
                    .findAll();
            if (users != null) {
                for (int i = 0; i < users.size(); i++) {
                    User user = users.get(i);
                    if (user.getIsLogin().equals("1")) {
                        int id = user.getId();
                        return id;
                    }
                }
            }

        } catch (DbException e) {
        }
        return 0;
    }

    public String getPhone() {
        DbManager.DaoConfig daoConfig = getDaoConfig();
        DbManager db = x.getDb(daoConfig);
        try {
            List<User> users = db.selector(User.class)
                    .findAll();
            if (users != null) {
                for (int i = 0; i < users.size(); i++) {
                    User user = users.get(i);
                    if (user.getIsLogin().equals("1")) {
                        String phone = user.getPhone();
                        return phone;
                    }
                }
            }

        } catch (DbException e) {
        }
        return null;
    }
}
