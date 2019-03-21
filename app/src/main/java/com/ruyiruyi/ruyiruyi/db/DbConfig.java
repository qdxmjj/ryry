package com.ruyiruyi.ruyiruyi.db;

        import android.content.Context;
        import android.os.Environment;

        import com.ruyiruyi.ruyiruyi.db.model.Location;
        import com.ruyiruyi.ruyiruyi.db.model.Lunbo;
        import com.ruyiruyi.ruyiruyi.db.model.TireType;
        import com.ruyiruyi.ruyiruyi.db.model.User;

        import org.xutils.DbManager;
        import org.xutils.db.sqlite.WhereBuilder;
        import org.xutils.ex.DbException;
        import org.xutils.x;

        import java.util.ArrayList;
        import java.util.List;

public class DbConfig {
    public Context context;

    public DbConfig(Context context) {
        this.context = context;
    }

    public DbManager.DaoConfig getDaoConfig() {
        DbManager.DaoConfig daoConfig = new DbManager.DaoConfig()
                .setDbName("ruyiruyi.db")
                .setAllowTransaction(true)
                .setDbDir(context.getFilesDir())
              //  .setDbDir(Environment.getExternalStorageDirectory())
                .setDbVersion(11);

        return daoConfig;
    }
    public DbManager getDbManager(){
        DbManager.DaoConfig daoConfig = getDaoConfig();
        DbManager db = x.getDb(daoConfig);
        return db;
    }

    public User getUserByPhone(String phone){
        DbManager.DaoConfig daoConfig = getDaoConfig();
        DbManager db = x.getDb(daoConfig);
        try {
            List<User> users = db.selector(User.class)
                    .findAll();
            if (users != null){
                for (int i = 0; i < users.size(); i++) {
                    User user = users.get(i);
                    if (user.getPhone().equals(phone)){
                        return user;
                    }
                }
            }

        } catch (DbException e) {
        }
        return null;
    }

    public User getUser(){
        DbManager.DaoConfig daoConfig = getDaoConfig();
        DbManager db = x.getDb(daoConfig);
        try {
            List<User> users = db.selector(User.class)
                    .findAll();
            if (users != null){
                for (int i = 0; i < users.size(); i++) {
                    User user = users.get(i);
                    if (user.getIsLogin().equals("1")){
                        return user;
                    }
                }
            }

        } catch (DbException e) {
        }
        return null;
    }
    public Location getLocation(){
        DbManager.DaoConfig daoConfig = getDaoConfig();
        DbManager db = x.getDb(daoConfig);
        try {
            List<Location> locations = db.selector(Location.class)
                    .findAll();
            if (locations != null){
                Location location = locations.get(0);
                return location;
            }

        } catch (DbException e) {
        }
        return null;
    }


    public List<Lunbo> getLunbo(){
        DbManager.DaoConfig daoConfig = getDaoConfig();
        DbManager db = x.getDb(daoConfig);
        List<Lunbo> lunbos = new ArrayList<>();
        try {
            lunbos= db.selector(Lunbo.class)
                    .findAll();
            } catch (DbException e1) {
        }

        return lunbos;
    }

    public Boolean getIsLogin(){
        DbManager.DaoConfig daoConfig = getDaoConfig();
        DbManager db = x.getDb(daoConfig);
        try {
            List<User> users = db.selector(User.class)
                    .findAll();
            if (users!=null){
                for (int i = 0; i < users.size(); i++) {
                    User user = users.get(i);
                    if (user.getIsLogin().equals("1")){
                        return true;
                    }
                }
            }

        } catch (DbException e) {
        }
        return false;
    }

    public String getToken(){
        DbManager.DaoConfig daoConfig = getDaoConfig();
        DbManager db = x.getDb(daoConfig);
        try {
            List<User> users = db.selector(User.class)
                    .findAll();
            if (users!=null){
                for (int i = 0; i < users.size(); i++) {
                    User user = users.get(i);
                    if (user.getIsLogin().equals("1")){
                        String token = user.getToken();
                        return token;
                    }
                }
            }

        } catch (DbException e) {
        }
        return null;
    }

    public int getId(){
        DbManager.DaoConfig daoConfig = getDaoConfig();
        DbManager db = x.getDb(daoConfig);
        try {
            List<User> users =  db.selector(User.class)
                    .findAll();
            if (users!=null){
                for (int i = 0; i < users.size(); i++) {
                    User user = users.get(i);
                    if (user.getIsLogin().equals("1")){
                        int id = user.getId();
                        return id;
                    }
                }
            }

        } catch (DbException e) {
        }
        return 0;
    }

    public String getPhone(){
        DbManager.DaoConfig daoConfig = getDaoConfig();
        DbManager db = x.getDb(daoConfig);
        try {
            List<User> users = db.selector(User.class)
                    .findAll();
            if (users != null){
                for (int i = 0; i < users.size(); i++) {
                    User user = users.get(i);
                    if (user.getIsLogin().equals("1")){
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