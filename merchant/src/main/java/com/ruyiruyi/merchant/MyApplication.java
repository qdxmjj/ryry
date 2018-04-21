package com.ruyiruyi.merchant;

import android.app.Application;

import com.baidu.mapapi.SDKInitializer;

import org.xutils.x;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        x.Ext.init(this);
        x.Ext.setDebug(true);
        SDKInitializer.initialize(getApplicationContext());
    }
}