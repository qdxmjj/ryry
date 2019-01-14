package com.ruyiruyi.ruyiruyi;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.ruyiruyi.ruyiruyi.db.DbConfig;
import com.ruyiruyi.ruyiruyi.db.model.User;
import com.ruyiruyi.ruyiruyi.ui.activity.BottomEventActivity;
import com.ruyiruyi.ruyiruyi.ui.activity.IntegralShopActivity;
import com.ruyiruyi.ruyiruyi.ui.activity.base.RyBaseFragmentActivity;
import com.ruyiruyi.ruyiruyi.ui.fragment.GoodsClassFragment;
import com.ruyiruyi.ruyiruyi.ui.fragment.HomeFragment;
import com.ruyiruyi.ruyiruyi.ui.fragment.MerchantFragment;
import com.ruyiruyi.ruyiruyi.ui.fragment.MyFragment;
import com.ruyiruyi.ruyiruyi.ui.model.HotActivity;
import com.ruyiruyi.ruyiruyi.utils.RequestUtils;
import com.ruyiruyi.rylibrary.cell.HomeTabsCell;
import com.ruyiruyi.rylibrary.cell.NoCanSlideViewPager;
import com.ruyiruyi.rylibrary.cell.downcell.CommonProgressDialog;
import com.ruyiruyi.rylibrary.popdblibrary.AdConstant;
import com.ruyiruyi.rylibrary.popdblibrary.AdManager;
import com.ruyiruyi.rylibrary.popdblibrary.bean.AdInfo;
import com.ruyiruyi.rylibrary.popdblibrary.transformer.ZoomOutPageTransformer;
import com.ruyiruyi.rylibrary.ui.adapter.FragmentViewPagerAdapter;
import com.ruyiruyi.rylibrary.utils.AndroidUtilities;
import com.ruyiruyi.rylibrary.utils.LayoutHelper;
import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends RyBaseFragmentActivity implements HomeFragment.OnIconClikc {

    private static final String TAG = MainActivity.class.getSimpleName();
    public static int HOMEFRAGMENT_RESULT = 0;
    public static int MYFRAGMENT_RESULT = 1;
    private FrameLayout content;
    private NoCanSlideViewPager viewPager;
    private HomeTabsCell tabsCell;
    private List<String> titles;
    private HomePagerAdapeter pagerAdapter;
    private static boolean isExit = false;
    private long time = 0;
    private static Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isExit = false;
        }
    };
    private String city = "";
    private double jingdu = 0.00;
    private double weidu = 0.00;
    private int hasjingweidu = 10; //0来自fragment
    private Intent intent;
    private int ischoos = 0;
    private String fromFragment = "";
    private String versionCode;
    private String version;
    private String downloadUrl;
    // 下载存储的文件名
    private static final String DOWNLOAD_NAME = "ruyiruyi_";
    private CommonProgressDialog mBar;
    private Uri tempUri;
    public boolean isGengxin = false;
    public List<HotActivity> hotActivityList;

    private int forceUpate = 0;  // 1强制更新 0不强制更新
    private String updateContent;
    private List<AdInfo> advList;//首页弹窗广告
    public List<String> quanList;
    private int userId = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        content = new FrameLayout(this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(content, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));
        hotActivityList = new ArrayList<>();
        quanList = new ArrayList<>();

        //信鸽绑定解绑账号置于onResume中

        //判断权限
        judgePower();

        isGengxin = false;
        int sum = 10;
        for (int i = 0; i < 10; i++) {
            sum = sum-- ;
        }

        Log.e(TAG, "onCreate: ---------------------------" + sum );


        /*//版本更新 (修改至首页弹窗后检测)
        getVersion();*/

        //签到功能
        initSignData();


        //弹窗活动
        getHotActivity();

        Intent intent = getIntent();

        if (intent != null) {
            fromFragment = intent.getStringExtra(MyFragment.FROM_FRAGMENT);
        } else {
            fromFragment = "";
        }
        Log.e(TAG, "onCreate: -----------" + fromFragment);
        viewPager = new NoCanSlideViewPager(this);

        content.addView(viewPager, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT, Gravity.TOP, 0, 0, 0, AndroidUtilities.dp(HomeTabsCell.CELL_HEIGHT)));

        tabsCell = new HomeTabsCell(this);
        content.addView(tabsCell, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, AndroidUtilities.dp(HomeTabsCell.CELL_HEIGHT), Gravity.BOTTOM));
        tabsCell.setViewPager(viewPager);

        initTitle();
        pagerAdapter = new HomePagerAdapeter(getSupportFragmentManager(), initPagerTitle(), initFragment());
        viewPager.setAdapter(pagerAdapter);


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Log.e(TAG, "onPageSelected: " + position);
               /* if (position == 0){
                    pagerAdapter.notifyDataSetChanged();
                }*/

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        if (fromFragment == null) {
            viewPager.setCurrentItem(0);
            tabsCell.setSelected(0);
        } else if (fromFragment.equals("MYFRAGMENT")) {
            viewPager.setCurrentItem(3);
            tabsCell.setSelected(3);
        } else if (fromFragment.equals("HOMEFRAGMENT")) {
            viewPager.setCurrentItem(0);
            tabsCell.setSelected(0);
        }


        //获取车辆品牌数据
        // initCarDataIntoDb();
        //获取车辆图标数据
        // initCarBrand();
        //获取车辆型号数据
        // initCarVerhicle();
        //获取车辆轮胎和排量数据
        // initCarrTireInfo();
        //获取轮胎型号
        // initTireType();
        //获取省市县
        // initProvice();


    }

    /**
     * 判断是否签到
     */
    private void getSignInfo() {
        SharedPreferences sf = getSharedPreferences("data", MODE_PRIVATE);//判断是否首页弹窗
        boolean isShow = sf.getBoolean("isShow", true);
        SharedPreferences.Editor editor = sf.edit();
        if (isShow){
            initSignData();
        }
    }

    /**
     * 获取签到信息
     */
    private void initSignData() {
        User user = new DbConfig(this).getUser();

        if (user!=null){
            userId= user.getId();
        }else {
            return;
        }

/*        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userId",userId);

        } catch (JSONException e) {
        }*/

        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL_JIFEN + "score/info");
        params.addBodyParameter("userId",userId + "");

        String token = new DbConfig(this).getToken();
        params.addParameter("token",token);
        Log.e(TAG, "initSignData:---jifen-- " + params);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess: ---" + result);
                JSONObject jsonObject1 = null;
                try {
                    jsonObject1 = new JSONObject(result);
                    String status = jsonObject1.getString("status");


                    if (status.equals("1")){
                        JSONObject data = jsonObject1.getJSONObject("data");
                        String signState = data.getString("signState");

                        if (signState.equals("0")){
                            goSign();
                        }


                    }else {
                        String msg = jsonObject1.getString("msg");
                        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }


    /**
     * 去签到
     */
    private void goSign() {

        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL_JIFEN + "score/sign");
        params.addBodyParameter("userId",userId + "");
        String token = new DbConfig(this).getToken();
        params.addParameter("token",token);
        Log.e(TAG, "goSign: ---jifen---" + params);
        x.http().post(params, new Callback.CommonCallback<String>() {
            private String addedScore;
            private String continuousMonth;
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess:---jifen--- " +result);
                JSONObject jsonObject1 = null;
                try {
                    jsonObject1 = new JSONObject(result);
                    String status = jsonObject1.getString("status");

                    if (status.equals("1")){
                        JSONObject data = jsonObject1.getJSONObject("data");
                        continuousMonth = data.getString("continuousMonth");
                        addedScore = data.getString("addedScore");
                        JSONArray couponList = data.getJSONArray("couponList");
                        quanList.clear();
                        if (couponList.length()>0){
                            String couponName = couponList.getJSONObject(0).getString("couponName");

                            Toast.makeText(MainActivity.this, "已连续签到" + continuousMonth + "次，本次签到获取" + addedScore +"积分，" + couponName, Toast.LENGTH_LONG).show();
                        }else {
                            Toast.makeText(MainActivity.this, "已连续签到" + continuousMonth + "次，本次签到获取" + addedScore +"积分", Toast.LENGTH_LONG).show();
                        }


                    }else {
                        String msg = jsonObject1.getString("msg");
                        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private void judgeShowPop() {
        SharedPreferences sf = getSharedPreferences("data", MODE_PRIVATE);//判断是否首页弹窗
        boolean isShow = sf.getBoolean("isShow", true);
        SharedPreferences.Editor editor = sf.edit();
        if (isShow) {     //若为true，则首页弹窗
            //显示首页弹窗
            showPop();

            //修改判断首次主页弹窗
            editor.putBoolean("isShow", false);//弹窗后修改弹窗标志位
        } else {
            //next
            //版本更新
            getVersion();
        }
        editor.commit();
    }

    /**
     * 显示首页弹窗
     */
    private void showPop() {

        //创建广告活动管理对象
        AdManager adManager = new AdManager(MainActivity.this, advList);
        adManager.setOverScreen(true)//设置弹窗背景全屏显示还是在内容区域显示
                .setPageTransformer(new ZoomOutPageTransformer())//设置ViewPager的滑动动画
                .setPadding(100)//设置弹窗距离屏幕两侧的距离（单位dp）
                .setWidthPerHeight(0.50f)//设置弹窗的宽高比
                .setBackViewColor(Color.parseColor("#AA333333"))//设置弹窗的背景色（当弹窗背景设置透明时，此设置失效）
                .setAnimBackViewTransparent(false)//设置弹窗背景是否透明
                .setDialogCloseable(true)//设置弹窗关闭图标是否可见
                .setDialogOutCloseable(true)//设置点击边缘是否可关闭
                .setBounciness(15)//设置弹窗弹性滑动弹性值
                .setSpeed(5)//设置弹窗弹性滑动速度值
                .setOnImageClickListener(new AdManager.OnImageClickListener() {//设定弹窗点击事件回调
                    @Override
                    public void onImageClick(View view, AdInfo advInfo) {
                        if (advInfo.isCanClick()) {
                            Intent intent = new Intent(MainActivity.this, BottomEventActivity.class);
                            intent.putExtra("canShare", advInfo.isCanShare());
                            intent.putExtra("webUrl", advInfo.getUrl());
                            intent.putExtra("shareUrl", advInfo.getShareUrl());
                            intent.putExtra("shareDescription", advInfo.getDescription());
                            startActivity(intent);
                        }
                    }
                })
                .setOnCloseClickListener(new View.OnClickListener() {//设定关闭按钮点击事件回调
                    @Override
                    public void onClick(View view) {
                        //版本更新
                        getVersion();
                    }
                })
                .setOnOutCloseClickListener(new View.OnClickListener() {//设定啊弹窗边缘点击事件监听
                    @Override
                    public void onClick(View view) {
                        //版本更新
                        getVersion();
                    }
                });
        //开始执行弹窗的显示操作，可传值为0-360，0表示从右开始弹出，逆时针方向，也可以传入自定义的方向值
        adManager.showAdDialog(AdConstant.ANIM_DOWN_TO_UP);
    }

    /**
     * 获取首页弹窗数据
     */
    private void getHotActivity() {
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "getAppActivity");
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess:getAppActivity-- " + result);
                JSONObject jsonObject1 = null;
                try {
                    jsonObject1 = new JSONObject(result);
                    String status = jsonObject1.getString("status");
                    String msg = jsonObject1.getString("msg");
                    if (status.equals("1")) {
                        JSONArray data = jsonObject1.getJSONArray("data");
                        //初始化数据
                        advList = new ArrayList<>();
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject object = data.getJSONObject(i);
                            AdInfo adInfo = new AdInfo();
                            adInfo.setActivityImg(object.getString("imageUrl")); //活动弹窗图片
                            int canClick = object.getInt("clickable");// 是否可点击  0 否 1 是
                            adInfo.setCanClick(canClick == 1 ? true : false);
                            adInfo.setUrl(object.getString("webUrl")); //页面url
                            if (new DbConfig(MainActivity.this).getIsLogin()) {
                                int canShare = object.getInt("shareable"); // 是否可分享 0 否 1 是
                                adInfo.setCanShare(canShare == 1 ? true : false);
                                adInfo.setShareUrl(object.getString("shareUrl") + "?userId=" + new DbConfig(MainActivity.this).getId()); //分享url
                            } else {
                                adInfo.setCanShare(false);
                                adInfo.setShareUrl(object.getString("shareUrl")); //分享url
                            }
                            adInfo.setAdId(object.getInt("id") + ""); //活动id
                            adInfo.setDescription(object.getString("text")); // 活动介绍
                            advList.add(adInfo);
                        }

                        //判断显示弹窗
                        judgeShowPop();
                    }
                } catch (JSONException e) {

                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(MainActivity.this, "应用初始化失败，请检查网络", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isGengxin) {
            update();
        }

        //信鸽推送绑定手机号
        DbConfig dbConfig = new DbConfig(this);
        if (dbConfig.getIsLogin()) {
            //信鸽token注册 绑定手机号
            XGPushManager.bindAccount(this, dbConfig.getPhone(), new XGIOperateCallback() {
                @Override
                public void onSuccess(Object data, int flag) {
                    //token在设备卸载重装的时候有可能会变
                    Log.d("TPush", "注册成功，设备token为：" + data);
                }

                @Override
                public void onFail(Object data, int errCode, String msg) {
                    Log.d("TPush", "注册失败，错误码：" + errCode + ",错误信息：" + msg);
                }
            });
        } else {
            //解绑信鸽手机号
            XGPushManager.delAccount(getApplicationContext(), new DbConfig(getApplicationContext()).getPhone());
            //反注册
            XGPushManager.unregisterPush(this);
        }
    }

    /**
     * 获取版本号
     */
    private void getVersion() {
        try {
            versionCode = this.getPackageManager().getPackageInfo(this.getPackageName(), 0).versionName;
            Log.e(TAG, "getVersion: versionCode -- " + versionCode);
        } catch (PackageManager.NameNotFoundException e) {

        }
        JSONObject jsonObject = new JSONObject();
        try {
            //   jsonObject.put("appVersion", "2.2.2");
            jsonObject.put("appVersion", versionCode);
            jsonObject.put("versionType", "user");
        } catch (JSONException e) {

        }
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "getAppNewestVersion");
        params.addBodyParameter("reqJson", jsonObject.toString());
        x.http().post(params, new Callback.CommonCallback<String>() {


            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess:version-- " + result);
                JSONObject jsonObject1 = null;
                try {
                    jsonObject1 = new JSONObject(result);
                    String status = jsonObject1.getString("status");
                    String msg = jsonObject1.getString("msg");
                    if (status.equals("1")) {
                        JSONObject data = jsonObject1.getJSONObject("data");
                        downloadUrl = data.getString("downloadUrl");
                        version = data.getString("version");
                        updateContent = data.getString("content");
                        forceUpate = data.getInt("forceUpdate"); //是否强制更新  1强制更新 0不强制更新
                        ShowDialog(version, downloadUrl, updateContent, forceUpate);
                        ;//安装应用的逻辑(写自己的就可以)
                    }
                } catch (JSONException e) {

                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 10086) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                ShowDialog(version, downloadUrl, updateContent, forceUpate);
            } else {
                Intent localIntent = new Intent();
                localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                if (Build.VERSION.SDK_INT >= 9) {//2.3
                    localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                    localIntent.setData(Uri.fromParts("package", this.getPackageName(), null));
                } else if (Build.VERSION.SDK_INT <= 8) {//2.2
                    localIntent.setAction(Intent.ACTION_VIEW);
                    localIntent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
                    localIntent.putExtra("com.android.settings.ApplicationPkgName", this.getPackageName());
                }
                this.startActivity(localIntent);
            }
        }
    }

/*    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == 10086) {
            ShowDialog(version,downloadUrl);//再次执行安装流程，包含权限判等
        }
    }*/

    /**
     * 弹出更新dialog
     *
     * @param version
     * @param downloadUrl
     */
    private void ShowDialog(String version, final String downloadUrl, final String updateContent, final int forceUpate) {
        if (forceUpate == 0) { //不强制更新
            new android.app.AlertDialog.Builder(this)
                    .setTitle("版本更新")
                    .setMessage(updateContent)
                    .setPositiveButton("更新", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int i) {
                            dialog.dismiss();
                            mBar = new CommonProgressDialog(MainActivity.this);
                            mBar.setCanceledOnTouchOutside(false);
                            mBar.setTitle("正在下载");
                            mBar.setCustomTitle(LayoutInflater.from(
                                    MainActivity.this).inflate(
                                    R.layout.title_dialog, null));
                            mBar.setMessage("正在下载");
                            mBar.setIndeterminate(true);
                            mBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                            mBar.setCancelable(false);
                            // downFile(URLData.DOWNLOAD_URL);
                            final DownloadTask downloadTask = new DownloadTask(
                                    MainActivity.this);
                            downloadTask.execute(downloadUrl);
                            mBar.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                @Override
                                public void onCancel(DialogInterface dialog) {
                                    downloadTask.cancel(true);
                                }
                            });
                        }
                    }).show();
        } else {
            new android.app.AlertDialog.Builder(this)
                    .setTitle("版本更新")
                    .setMessage(updateContent)
                    .setPositiveButton("更新", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int i) {
                            dialog.dismiss();
                            mBar = new CommonProgressDialog(MainActivity.this);
                            mBar.setCanceledOnTouchOutside(false);
                            mBar.setTitle("正在下载");
                            mBar.setCustomTitle(LayoutInflater.from(
                                    MainActivity.this).inflate(
                                    R.layout.title_dialog, null));
                            mBar.setMessage("正在下载");
                            mBar.setIndeterminate(true);
                            mBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                            mBar.setCancelable(false);
                            // downFile(URLData.DOWNLOAD_URL);
                            final DownloadTask downloadTask = new DownloadTask(
                                    MainActivity.this);
                            downloadTask.execute(downloadUrl);
                            mBar.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                @Override
                                public void onCancel(DialogInterface dialog) {
                                    downloadTask.cancel(true);
                                }
                            });
                         }
                    })
                    .setCancelable(false)
                    .show();
        }

    }


    /**
     * 下载应用
     *
     * @author Administrator
     */
    class DownloadTask extends AsyncTask<String, Integer, String> {

        private Context context;
        private PowerManager.WakeLock mWakeLock;

        public DownloadTask(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... sUrl) {
            InputStream input = null;
            OutputStream output = null;
            HttpURLConnection connection = null;
            File file = null;
            try {
                URL url = new URL(sUrl[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                // expect HTTP 200 OK, so we don't mistakenly save error
                // report
                // instead of the file
                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    return "Server returned HTTP "
                            + connection.getResponseCode() + " "
                            + connection.getResponseMessage();
                }
                // this will be useful to display download percentage
                // might be -1: server did not report the length
                int fileLength = connection.getContentLength();
                if (Environment.getExternalStorageState().equals(
                        Environment.MEDIA_MOUNTED)) {
                    file = new File(MainActivity.this.getObbDir().getAbsolutePath(),
                            DOWNLOAD_NAME + version + ".apk");

                    if (!file.exists()) {
                        // 判断父文件夹是否存在
                        if (!file.getParentFile().exists()) {
                            file.getParentFile().mkdirs();
                        }
                    }

                } else {
                    Toast.makeText(MainActivity.this, "sd卡未挂载",
                            Toast.LENGTH_LONG).show();
                }
                input = connection.getInputStream();
                output = new FileOutputStream(file);
                byte data[] = new byte[4096];
                long total = 0;
                int count;
                while ((count = input.read(data)) != -1) {
                    // allow canceling with back button
                    if (isCancelled()) {
                        input.close();
                        return null;
                    }
                    total += count;
                    // publishing the progress....
                    if (fileLength > 0) // only if total length is known
                        publishProgress((int) (total * 100 / fileLength));
                    output.write(data, 0, count);

                }
            } catch (Exception e) {
                System.out.println(e.toString());
                return e.toString();

            } finally {
                try {
                    if (output != null)
                        output.close();
                    if (input != null)
                        input.close();
                } catch (IOException ignored) {
                }
                if (connection != null)
                    connection.disconnect();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // take CPU lock to prevent CPU from going off if the user
            // presses the power button during download
            PowerManager pm = (PowerManager) context
                    .getSystemService(Context.POWER_SERVICE);
            mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                    getClass().getName());
            mWakeLock.acquire();
            mBar.show();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
            // if we get here, length is known, now set indeterminate to false
            mBar.setIndeterminate(false);
            mBar.setMax(100);
            mBar.setProgress(progress[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            mWakeLock.release();
            mBar.dismiss();
            if (result != null) {

//                // 申请多个权限。大神的界面
//                AndPermission.with(MainActivity.this)
//                        .requestCode(REQUEST_CODE_PERMISSION_OTHER)
//                        .permission(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
//                        // rationale作用是：用户拒绝一次权限，再次申请时先征求用户同意，再打开授权对话框，避免用户勾选不再提示。
//                        .rationale(new RationaleListener() {
//                                       @Override
//                                       public void showRequestPermissionRationale(int requestCode, Rationale rationale) {
//                                           // 这里的对话框可以自定义，只要调用rationale.resume()就可以继续申请。
//                                           AndPermission.rationaleDialog(MainActivity.this, rationale).show();
//                                       }
//                                   }
//                        )
//                        .send();
                // 申请多个权限。
               /* AndPermission.with(MainActivity.this)
                        .requestCode(REQUEST_CODE_PERMISSION_SD)
                        .permission(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                        // rationale作用是：用户拒绝一次权限，再次申请时先征求用户同意，再打开授权对话框，避免用户勾选不再提示。
                        .rationale(rationaleListener
                        )
                        .send();*/


                Toast.makeText(context, "您未打开SD卡权限" + result, Toast.LENGTH_LONG).show();
            } else {
                // Toast.makeText(context, "File downloaded",
                // Toast.LENGTH_SHORT)
                // .show();
                isGengxin = true;
                if (Build.VERSION.SDK_INT >= 26) {
                    boolean b = getPackageManager().canRequestPackageInstalls();
                    if (b) {
                        update();
                    } else {
//请求安装未知应用来源的权限
                        //  ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.REQUEST_INSTALL_PACKAGES}, 10086);
                        //   Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES,Uri.fromParts("package:"+ getPackageName()));
                        //  startActivityForResult(intent, 10086);

                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.REQUEST_INSTALL_PACKAGES}, 10086);
                    }
                } else {
                    update();
                }

            }

        }
    }

    private void update() {
        isGengxin = false;
        //安装应用
        //  Intent intent = new Intent(Intent.ACTION_VIEW);


        String fileName = MainActivity.this.getObbDir().getAbsolutePath() + "/" + DOWNLOAD_NAME + version + ".apk";
      /*  File file = null;
        file = new File(fileName);
        //判断是否是AndroidN以及更高的版本
        if (Build.VERSION.SDK_INT >= 24) {
            tempUri = FileProvider.getUriForFile(MainActivity.this, "com.ruyiruyi.ruyiruyi.fileProvider", file);
        } else {
            tempUri = Uri.fromFile(new File(this.getObbDir().getAbsolutePath(), DOWNLOAD_NAME + version + ".apk"));
        }

        Log.e(TAG, "update: ----" + tempUri);
        intent.setDataAndType(tempUri,
                "application/vnd.android.package-archive");
        startActivity(intent);*/
        if (Build.VERSION.SDK_INT >= 24) {
            File file = new File(fileName);
            tempUri = FileProvider.getUriForFile(MainActivity.this, "com.ruyiruyi.ruyiruyi.fileProvider", file);
            Intent install = new Intent(Intent.ACTION_VIEW);
            install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);//添加这一句表示对目标应用临时授权该Uri所代表的文件
            install.setDataAndType(tempUri, "application/vnd.android.package-archive");
            startActivity(install);
        } else {
            Intent install = new Intent(Intent.ACTION_VIEW);
            install.setDataAndType(Uri.fromFile(new File(fileName)), "application/vnd.android.package-archive");
            install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(install);
        }


    }

    private List<Fragment> initFragment() {
        Log.e(TAG, "initFragment: -2-" + ischoos);
        List<Fragment> fragments = new ArrayList<>();
        HomeFragment homeFragment = new HomeFragment();
        homeFragment.setListener(this);
        Bundle bundle = new Bundle();
        homeFragment.setArguments(bundle);
        fragments.add(homeFragment);
        MerchantFragment merchantFragment = new MerchantFragment();
        Bundle bundleMerchant = new Bundle();
        bundleMerchant.putInt(MerchantFragment.SHOP_TYPE, 0);
        merchantFragment.setArguments(bundleMerchant);
        fragments.add(merchantFragment);
        GoodsClassFragment goodsClassFragment = new GoodsClassFragment();
        fragments.add(goodsClassFragment);
        fragments.add(new MyFragment());

        return fragments;
    }

    protected List<String> initPagerTitle() {
        titles = new ArrayList<>();
        titles.add("首页");
        titles.add("附近门店");
        titles.add("分类");
        titles.add("我的");
        return titles;
    }

    private void initTitle() {
        tabsCell.addView(R.drawable.ic_home, R.drawable.ic_home_pressed, "首页 ");
        tabsCell.addView(R.drawable.ic_merchant, R.drawable.ic_merchant_pressed, "附近门店 ");
        tabsCell.addView(R.drawable.ic_shangpin, R.drawable.ic_shangpin_2, "分类");

        tabsCell.addView(R.drawable.ic_my, R.drawable.ic_my_pressed, "我的 ");

    }

    /**
     * 商品分类点击回调
     */
    @Override
    public void onShopClassClickListener() {
        viewPager.setCurrentItem(2);
        tabsCell.setSelected(2);
    }

    class HomePagerAdapeter extends FragmentViewPagerAdapter {

        private final List<String> mPageTitle = new ArrayList<>();

        public HomePagerAdapeter(FragmentManager fragmentManager, List<String> pageTitles, List<Fragment> fragments) {
            super(fragmentManager, fragments);
            mPageTitle.clear();
            mPageTitle.addAll(pageTitles);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mPageTitle.get(position);
        }
    }

    /*  @Override
      public boolean onKeyDown(int keyCode, KeyEvent event) {

          return super.onKeyDown(keyCode, event);
      }
  */
    @Override
    public void onBackPressed() {

        exit();

    }

    private void exit() {
        if (!isExit) {
            isExit = true;
            Toast.makeText(getApplicationContext(), "再按一次后退键退出程序",
                    Toast.LENGTH_SHORT).show();
            // 利用handler延迟发送更改状态信息
            mHandler.sendEmptyMessageDelayed(0, 2000);
        } else {
            Log.e(TAG, "exit: -----");
            Intent intent = new Intent("qd.xmjj.baseActivity");
            intent.putExtra("closeAll", 1);
            sendBroadcast(intent);//发送广播

            /* this.finish();
             System.exit(0);*/
        }
    }


    private void judgePower() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Toast.makeText(this, "请授权读写手机存储权限", Toast.LENGTH_SHORT).show();
            finish();
        }
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            //  Toast.makeText(this, "请授权读写手机存储权限", Toast.LENGTH_SHORT).show();
            finish();
        }
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            //  Toast.makeText(this, "请授权相机权限", Toast.LENGTH_SHORT).show();
            finish();
        }
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            //  Toast.makeText(this, "请授权定位权限", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
