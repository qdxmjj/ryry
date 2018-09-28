package com.ruyiruyi.merchant;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.baidu.mapapi.SDKInitializer;

import org.xutils.x;

import cn.jpush.android.api.JPushInterface;

public class MyApplication extends Application {
    private static MyApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
        x.Ext.init(this);
        x.Ext.setDebug(true);
        SDKInitializer.initialize(getApplicationContext());
        mInstance = this;

        // 将MultiDex注入到项目中
        MultiDex.install(this);
    }

    /**
     * 获取context
     *
     * @return
     */
    public static Context getInstance() {
        // 因为我们程序运行后，Application是首先初始化的，如果在这里不用判断instance是否为空
        return mInstance;
    }
}