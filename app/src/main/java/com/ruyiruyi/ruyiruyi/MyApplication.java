package com.ruyiruyi.ruyiruyi;

import android.app.Application;

import org.xutils.x;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        x.Ext.init(this);
        x.Ext.setDebug(true);
    }
}