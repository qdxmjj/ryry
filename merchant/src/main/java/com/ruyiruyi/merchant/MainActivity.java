package com.ruyiruyi.merchant;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.ruyiruyi.merchant.db.DbConfig;
import com.ruyiruyi.merchant.ui.activity.base.MerchantBaseFragmentActivity;
import com.ruyiruyi.merchant.ui.adapter.MyPagerAdapter;
import com.ruyiruyi.merchant.ui.fragment.IncomeFragment;
import com.ruyiruyi.merchant.ui.fragment.MyFragment;
import com.ruyiruyi.merchant.ui.fragment.OrderFragment;
import com.ruyiruyi.merchant.ui.fragment.StoreFragment;
import com.ruyiruyi.merchant.utils.NoPreloadHomeTabsCell;
import com.ruyiruyi.merchant.utils.UtilsURL;
import com.ruyiruyi.rylibrary.cell.HomeTabsCell;
import com.ruyiruyi.rylibrary.cell.downcell.CommonProgressDialog;
import com.ruyiruyi.rylibrary.utils.LayoutHelper;

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

import cn.jpush.android.api.JPushInterface;

public class MainActivity extends MerchantBaseFragmentActivity implements StoreFragment.ForRefreshStore, MyFragment.ForRefreshMy {

    private FrameLayout content;
    private ViewPager viewPager;
    private HomeTabsCell tabsCell;
    private List<String> titles;
    private MyPagerAdapter pagerAdapter;
    private static boolean isExit = false;
    private static Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isExit = false;
        }
    };
    private String page;
    private String TAG = MainActivity.class.getSimpleName();
    private String versionCode;
    private String version;
    private String downloadUrl;
    // 下载存储的文件名
    private static final String DOWNLOAD_NAME = "ryry_merchant";
    private CommonProgressDialog mBar;
    private Uri tempUri;
    public boolean isGengxin = false;
    private String user_phone_jpush;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        content = new FrameLayout(this);

        setContentView(content, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));

        //获取传递page标记
        Bundle bundle = getIntent().getExtras();
        page = bundle.getString("page");

        //判断权限
        judgePower();

        isGengxin = false;
        //版本更新
        getVersion();

        //极光推送绑定别名
        user_phone_jpush = new DbConfig(getApplicationContext()).getPhone();
        JPushInterface.setAlias(getApplicationContext(), 1, user_phone_jpush);

        viewPager = new ViewPager(this);
        viewPager.setId("MAINACTIVITYS".hashCode());
        content.addView(viewPager, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT, Gravity.TOP, 0, 0, 0, NoPreloadHomeTabsCell.CELL_HEIGHT));

        tabsCell = new HomeTabsCell(this);
        content.addView(tabsCell, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT, Gravity.BOTTOM));
        tabsCell.setViewPager(viewPager);

        initTitle();
//        pagerAdapter = new HomePagerAdapeter(getSupportFragmentManager(), initPagerTitle(), initFragment());
        pagerAdapter = new MyPagerAdapter(getSupportFragmentManager(), initPagerTitle(), initFragment());
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        switch (page) {
            case "order"://平台订单
                viewPager.setCurrentItem(0);
                tabsCell.setSelected(0);
                break;
            case "store"://店铺订单
                viewPager.setCurrentItem(1);
                tabsCell.setSelected(1);
                break;
            case "income"://收益
                viewPager.setCurrentItem(2);
                tabsCell.setSelected(2);
                break;
            case "my"://我的
                viewPager.setCurrentItem(3);
                tabsCell.setSelected(3);
                break;
        }


        initView();
        initData();


    }


    /**
     * 弹出更新dialog
     *
     * @param version
     * @param downloadUrl
     */
    private void ShowDialog(String version, final String downloadUrl) {
        new android.app.AlertDialog.Builder(this)
                .setTitle("版本更新")
                .setMessage("ryry_merchant" + version)
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
                        mBar.setCancelable(true);
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
                .show();
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
            tempUri = FileProvider.getUriForFile(MainActivity.this, "com.ruyiruyi.merchant.fileProvider", file);
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 10086) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                ShowDialog(version, downloadUrl);
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
            jsonObject.put("appVersion", versionCode);
            jsonObject.put("versionType", "store");
        } catch (JSONException e) {

        }
        RequestParams params = new RequestParams(UtilsURL.REQUEST_URL + "getAppNewestVersion");
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
                        ShowDialog(version, downloadUrl);
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


    private void initData() {

    }

    private void initView() {

    }

    private List<Fragment> initFragment() {
        List<Fragment> fragments = new ArrayList<>();
        OrderFragment orderFragment = new OrderFragment();
        fragments.add(orderFragment);
        StoreFragment storeFragment = new StoreFragment();
        storeFragment.setListener(this);
        fragments.add(storeFragment);
        IncomeFragment incomeFragment = new IncomeFragment();
        fragments.add(incomeFragment);
        MyFragment myFragment = new MyFragment();
        myFragment.setListener(this);
        fragments.add(myFragment);

        return fragments;
    }

    private List<String> initPagerTitle() {
        titles = new ArrayList<>();
        titles.add("平台订单");
        titles.add("店铺订单");
        titles.add("收益");
        titles.add("我的");
        return titles;
    }

    private void initTitle() {
        tabsCell.addView(R.drawable.ic_tubiao1, R.drawable.ic_tubiao1_xuanzhong, "平台订单 ");
        tabsCell.addView(R.drawable.ic_shop_weixuan, R.drawable.ic_shop_xuanzhong, "店铺订单 ");
        tabsCell.addView(R.drawable.ic_shouyi32, R.drawable.ic_shouyi31, "收益 ");
        tabsCell.addView(R.drawable.ic_wode_weixuan, R.drawable.ic_wode_xuanzhong, "我的 ");
    }

 /*   class HomePagerAdapeter extends FragmentViewPagerAdapter {

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
    }*/


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            judgeExit();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    private void judgeExit() {
        if (!isExit) {
            isExit = true;
            Toast.makeText(getApplicationContext(), "再按一次后退键退出程序",
                    Toast.LENGTH_SHORT).show();
            // 利用handler延迟发送更改状态信息
            mHandler.sendEmptyMessageDelayed(0, 2000);
        } else {
            Intent intent = new Intent("qd.xmjj.baseActivity");
            intent.putExtra("closeAll", 1);
            sendBroadcast(intent);//发送广播*/

//            this.finish();
        }
    }

    private void judgePower() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
//            Toast.makeText(this, "请授权读写手机存储权限", Toast.LENGTH_SHORT).show();
            finish();
        }
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
//            Toast.makeText(this, "请授权读写手机存储权限", Toast.LENGTH_SHORT).show();
            finish();
        }
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
//            Toast.makeText(this, "请授权相机权限", Toast.LENGTH_SHORT).show();
            finish();
        }
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
//            Toast.makeText(this, "请授权定位权限", Toast.LENGTH_SHORT).show();
            finish();
        }
    }


    /*
    * 接口回调 StoreFragment修改头像成功后通知刷新activity数据方法
    * */  //(启用 由于收益页面不能刷新的问题)
    @Override
    public void forRefreshStoreListener() {
        //刷新适配器数据
        pagerAdapter.UpdataNewData(initPagerTitle(), initFragment());
    }


    /*
    * 接口回调 MyFragment修改头像成功后通知刷新activity数据方法
    * */  //(启用 由于收益页面不能刷新的问题)
    @Override
    public void forRefreshMyListener() {
        //刷新适配器数据
        pagerAdapter.UpdataNewData(initPagerTitle(), initFragment());

    }


    @Override
    protected void onResume() {
        super.onResume();
        if (isGengxin) {
            update();
        }
    }

    //未用 由于收益页面不能刷新的问题
  /*  @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (hasFocus) {
            //刷新适配器数据
            pagerAdapter.UpdataNewData(initPagerTitle(), initFragment());
        }
    }*/
}
