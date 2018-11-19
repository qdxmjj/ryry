package com.ruyiruyi.merchant;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.baidu.mapapi.SDKInitializer;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.xutils.x;

import cn.jpush.android.api.JPushInterface;

public class MyApplication extends MultiDexApplication {
    private static MyApplication mInstance;
    public static IWXAPI mWxApi;
    public static String WEIXIN_APP_ID = "wxe7d25890f6c97a1a";
    public static String WEIXIN_APP_SECRET = "e1d142170ca69c78838af93dbcdc6b1e";

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

        //注册微信
        rgisterWX();
    }

    private void rgisterWX() {
        //第二个参数是指你应用在微信开放平台上的AppID
        mWxApi = WXAPIFactory.createWXAPI(this, WEIXIN_APP_ID, false);
        // 将该app注册到微信
        mWxApi.registerApp(WEIXIN_APP_ID);
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