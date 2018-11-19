package com.example.warehouse;

import android.support.multidex.MultiDexApplication;

import org.xutils.x;

/**
 * Created by Lenovo on 2018/11/13.
 */

public class MyApplication extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        x.Ext.setDebug(true);
    }
}
