package com.ruyiruyi.merchant;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DbManager.DaoConfig daoConfig = new DbManager.DaoConfig()
                .setDbName("ruyimerchant.db")
                .setAllowTransaction(true)
                .setDbDir(Environment.getExternalStorageDirectory())
                .setDbVersion(1);

        DbManager db = x.getDb(daoConfig);
        User user = new User("xiaoming");
        try {
            db.save(user);
        } catch (DbException e) {

        }
    }
}
