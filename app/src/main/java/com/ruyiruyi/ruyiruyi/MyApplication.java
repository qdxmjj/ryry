package com.ruyiruyi.ruyiruyi;

import android.app.Activity;
import android.app.Application;
import android.app.Service;
import android.os.Vibrator;
import android.util.DisplayMetrics;

import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.ruyiruyi.ruyiruyi.ui.service.LocationService;
import com.ruyiruyi.rylibrary.popdblibrary.utils.DisplayUtil;
import com.tencent.android.tpush.XGPushConfig;
import com.tencent.android.tpush.XGPushManager;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

public class MyApplication extends Application {
    private List<Activity> oList;
    public LocationService locationService;
    public Vibrator mVibrator;

    public static IWXAPI mWxApi;
    /*public static String WEIXIN_APP_ID = Constants.APP_ID;
    public static String WEIXIN_APP_SECRET = Constants.APP_SECRET_WX;*/
    public static String WEIXIN_APP_ID = "wx407c59de8b10c601";
    public static String WEIXIN_APP_SECRET = "62a20f41249091afa6075d3cfb7ea93f";


    @Override
    public void onCreate() {
        super.onCreate();


        //华为注册代码之前调用   防止otherPushType = huawei otherPushToken = null
        XGPushConfig.setHuaweiDebug(true);
        //信鸽开启debug日志数据
        XGPushConfig.enableDebug(this, true);

        //设置账号//注意在3.2.2 版本信鸽对账号绑定和解绑接口进行了升级具体详情请参考API文档。
        XGPushManager.bindAccount(getApplicationContext(), "XINGE");
        //设置标签
        XGPushManager.setTag(this, "XINGE");
        //设置小米APPID和AppKey
        XGPushConfig.setMiPushAppId(getApplicationContext(), "2882303761517833664");
        //设置魅族APPID和APPKEY
        XGPushConfig.setMzPushAppId(this, "1003042");//APP_ID
        XGPushConfig.setMzPushAppKey(this, "82f9df2a55f54682b5f0b8ef5f479b84");//APP_KEY
        XGPushConfig.setMiPushAppKey(getApplicationContext(), "5211783390664");
        //打开第三方推送
        XGPushConfig.enableOtherPush(getApplicationContext(), true);


        //初始化Fresco
        initDisplayOpinion();

        Fresco.initialize(this);
        //初始化xUtils
        x.Ext.init(this);
        x.Ext.setDebug(true);
        oList = new ArrayList<Activity>();
        /***
         * 初始化定位sdk，建议在Application中创建
         */
        locationService = new LocationService(getApplicationContext());
        mVibrator = (Vibrator) getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);

        // 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext
        SDKInitializer.initialize(this);
        //自4.3.0起，百度地图SDK所有接口均支持百度坐标和国测局坐标，用此方法设置您使用的坐标类型.
        //包括BD09LL和GCJ02两种坐标，默认是BD09LL坐标。
        SDKInitializer.setCoordType(CoordType.BD09LL);

        //   api = WXAPIFactory.createWXAPI(this, Constants.APP_ID,true);
        //   api.registerApp(Constants.APP_ID);

        /*
        // 将MultiDex注入到项目中
        MultiDex.install(this);*/

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
     * 添加Activity
     */
    public void addActivity_(Activity activity) {
// 判断当前集合中不存在该Activity
        if (!oList.contains(activity)) {
            oList.add(activity);//把当前Activity添加到集合中
        }
    }

    /**
     * 销毁单个Activity
     */
    public void removeActivity_(Activity activity) {
        //判断当前集合中存在该Activity
        if (oList.contains(activity)) {
            oList.remove(activity);//从集合中移除
            activity.finish();//销毁当前Activity
        }
    }

    /**
     * 销毁所有的Activity
     */
    public void removeALLActivity_() {
        //通过循环，把集合中的所有Activity销毁
        for (Activity activity : oList) {
            activity.finish();
        }
    }


    private void initDisplayOpinion() {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        DisplayUtil.density = dm.density;
        DisplayUtil.densityDPI = dm.densityDpi;
        DisplayUtil.screenWidthPx = dm.widthPixels;
        DisplayUtil.screenhightPx = dm.heightPixels;
        DisplayUtil.screenWidthDip = DisplayUtil.px2dip(getApplicationContext(), dm.widthPixels);
        DisplayUtil.screenHightDip = DisplayUtil.px2dip(getApplicationContext(), dm.heightPixels);
    }
}