package com.ruyiruyi.ruyiruyi;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Message;
import android.os.PowerManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import com.ruyiruyi.ruyiruyi.db.DbConfig;
import com.ruyiruyi.ruyiruyi.db.model.CarBrand;
import com.ruyiruyi.ruyiruyi.db.model.CarFactory;
import com.ruyiruyi.ruyiruyi.db.model.CarTireInfo;
import com.ruyiruyi.ruyiruyi.db.model.CarVerhicle;
import com.ruyiruyi.ruyiruyi.db.model.Province;
import com.ruyiruyi.ruyiruyi.db.model.TireType;
import com.ruyiruyi.ruyiruyi.ui.activity.base.RyBaseFragmentActivity;
import com.ruyiruyi.ruyiruyi.ui.fragment.GoodsClassFragment;
import com.ruyiruyi.ruyiruyi.ui.fragment.HomeFragment;
import com.ruyiruyi.ruyiruyi.ui.fragment.MerchantFragment;
import com.ruyiruyi.ruyiruyi.ui.fragment.MyFragment;
import com.ruyiruyi.ruyiruyi.utils.RequestUtils;
import com.ruyiruyi.ruyiruyi.utils.UtilsRY;
import com.ruyiruyi.rylibrary.cell.HomeTabsCell;
import com.ruyiruyi.rylibrary.cell.NoCanSlideViewPager;
import com.ruyiruyi.rylibrary.cell.downcell.CommonProgressDialog;
import com.ruyiruyi.rylibrary.ui.adapter.FragmentViewPagerAdapter;
import com.ruyiruyi.rylibrary.utils.AndroidUtilities;
import com.ruyiruyi.rylibrary.utils.LayoutHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.os.Handler;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.DbManager;
import org.xutils.common.Callback;
import org.xutils.ex.DbException;
import org.xutils.http.RequestParams;
import org.xutils.x;

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        content = new FrameLayout(this);
        setContentView(content, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));

        //判断权限
        judgePower();

        isGengxin = false;
        //版本更新
        getVersion();

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

    @Override
    protected void onResume() {
        super.onResume();
        if (isGengxin) {
            update();
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
                        ShowDialog(version, downloadUrl);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == 10086) {
            ShowDialog(version, downloadUrl);//再次执行安装流程，包含权限判等
        }
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
                .setMessage("ruyiruyi_" + version)
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
                }).show();
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


    private void initProvice() {
        DbConfig dbConfig = new DbConfig(this);
        DbManager db = dbConfig.getDbManager();
        List<Province> provinceList = null;
        try {
            provinceList = db.selector(Province.class)
                    .orderBy("time")
                    .findAll();
        } catch (DbException e) {
        }
        JSONObject jsonObject = new JSONObject();
        try {
            if (provinceList == null) {
                jsonObject.put("time", "2000-00-00 00:00:00");
            } else {
                String time = provinceList.get(provinceList.size() - 1).getTime();
                jsonObject.put("time", time);
            }
        } catch (JSONException e) {

        }
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "getAllPositon");
        params.addBodyParameter("reqJson", jsonObject.toString());
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess:getTireTypeData---" + result);
                try {
                    JSONObject jsonObject1 = new JSONObject(result);
                    String status = jsonObject1.getString("status");
                    String msg = jsonObject1.getString("msg");
                    if (status.equals("1")) {
                        JSONArray data = jsonObject1.getJSONArray("data");
                        List<Province> provinceArrayList = new ArrayList<Province>();
                        for (int i = 0; i < data.length(); i++) {

                            String name = data.getJSONObject(i).getString("name");
                            int id = data.getJSONObject(i).getInt("id");
                            int definition = data.getJSONObject(i).getInt("definition");
                            int fid = data.getJSONObject(i).getInt("fid");

                            long time = data.getJSONObject(i).getLong("time");
                            String timestampToStringAll = new UtilsRY().getTimestampToStringAll(time);


                            provinceArrayList.add(new Province(id, fid, definition, name, timestampToStringAll));
                        }

                        saveProvinceIntoDb(provinceArrayList);
                    } else {
                        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
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

    private void saveProvinceIntoDb(List<Province> provinceArrayList) {
        DbConfig dbConfig = new DbConfig(this);
        DbManager db = dbConfig.getDbManager();
        try {
            Log.e(TAG, "provinceArrayList: 添加成功" + provinceArrayList.size());
            db.saveOrUpdate(provinceArrayList);
        } catch (DbException e) {

        }

    }

    private void initTireType() {
        DbConfig dbConfig = new DbConfig(this);
        DbManager db = dbConfig.getDbManager();
        List<TireType> tireTypeList = null;
        try {
            tireTypeList = db.selector(TireType.class)
                    .orderBy("time")
                    .findAll();
        } catch (DbException e) {
        }
        JSONObject jsonObject = new JSONObject();

        try {
            if (tireTypeList == null) {
                jsonObject.put("time", "2000-00-00 00:00:00");
            } else {
                String time = tireTypeList.get(tireTypeList.size() - 1).getTime();
                jsonObject.put("time", time);
            }
        } catch (JSONException e) {

        }

        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "getTireType");
        params.addBodyParameter("reqJson", jsonObject.toString());

        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess:getTireTypeData---" + result);
                try {
                    JSONObject jsonObject1 = new JSONObject(result);
                    String status = jsonObject1.getString("status");
                    String msg = jsonObject1.getString("msg");
                    if (status.equals("1")) {
                        JSONArray data = jsonObject1.getJSONArray("data");
                        List<TireType> tireTypeArrayList = new ArrayList<TireType>();
                        for (int i = 0; i < data.length(); i++) {
                            String tireDiameter = data.getJSONObject(i).getString("tireDiameter");
                            String tireFlatWidth = data.getJSONObject(i).getString("tireFlatWidth");
                            String tireFlatnessRatio = data.getJSONObject(i).getString("tireFlatnessRatio");
                            int id = data.getJSONObject(i).getInt("tireTypeId");

                            long time = data.getJSONObject(i).getLong("time");
                            String timestampToStringAll = new UtilsRY().getTimestampToStringAll(time);


                            tireTypeArrayList.add(new TireType(id, tireFlatWidth, tireFlatnessRatio, tireDiameter, timestampToStringAll));
                        }

                        saveTireTypeIntoDb(tireTypeArrayList);
                    } else {
                        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
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

    private void saveTireTypeIntoDb(List<TireType> tireTypeArrayList) {
        DbConfig dbConfig = new DbConfig(this);
        DbManager db = dbConfig.getDbManager();
        try {
            Log.e(TAG, "tireTypeArrayList: 添加成功" + tireTypeArrayList.size());
            db.saveOrUpdate(tireTypeArrayList);
        } catch (DbException e) {

        }
    }

    private void initCarrTireInfo() {
        DbConfig dbConfig = new DbConfig(this);
        DbManager db = dbConfig.getDbManager();
        List<CarTireInfo> carTireInfoList = null;
        try {
            carTireInfoList = db.selector(CarTireInfo.class)
                    .orderBy("time")
                    .findAll();

        } catch (DbException e) {

        }
        JSONObject jsonObject = new JSONObject();

        try {
            if (carTireInfoList == null) {
                jsonObject.put("time", "2000-00-00 00:00:00");
            } else {
                String time = carTireInfoList.get(carTireInfoList.size() - 1).getTime();
                Log.e(TAG, "initCarDataIntoDb: " + time);
                jsonObject.put("time", time);
            }
        } catch (JSONException e) {
        }
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "getCarTireInfoData");
        params.addBodyParameter("reqJson", jsonObject.toString());
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess:getCarTireInfoData---" + result);
                try {
                    JSONObject jsonObject1 = new JSONObject(result);
                    String status = jsonObject1.getString("status");
                    String msg = jsonObject1.getString("msg");
                    if (status.equals("1")) {
                        JSONArray data = jsonObject1.getJSONArray("data");
                        List<CarTireInfo> carTireInfoArrayList = new ArrayList<CarTireInfo>();
                        for (int i = 0; i < data.length(); i++) {

                            String brand = data.getJSONObject(i).getString("brand");
                            String verhicle = data.getJSONObject(i).getString("verhicle");
                            String font = data.getJSONObject(i).getString("font");
                            String name = data.getJSONObject(i).getString("name");
                            String pailiang = data.getJSONObject(i).getString("pailiang");
                            String rear = data.getJSONObject(i).getString("rear");
                            String year = data.getJSONObject(i).getString("year");
                            int id = data.getJSONObject(i).getInt("id");
                            int carBrandId = data.getJSONObject(i).getInt("carBrandId");
                            int verhicleId = data.getJSONObject(i).getInt("verhicleId");

                            long time = data.getJSONObject(i).getLong("time");
                            String timestampToStringAll = new UtilsRY().getTimestampToStringAll(time);


                            carTireInfoArrayList.add(new CarTireInfo(id, brand, carBrandId, verhicle, verhicleId, pailiang, year, name, font, rear, timestampToStringAll));
                        }
                        savaCarTireIntoDb(carTireInfoArrayList);
                    } else {
                        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
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

    private void savaCarTireIntoDb(List<CarTireInfo> carTireInfoArrayList) {
        DbConfig dbConfig = new DbConfig(this);
        DbManager db = dbConfig.getDbManager();
        try {
            Log.e(TAG, "savaCarTireIntoDb: 添加成功");
            db.saveOrUpdate(carTireInfoArrayList);
        } catch (DbException e) {

        }
    }

    private void initCarVerhicle() {
        DbConfig dbConfig = new DbConfig(this);
        DbManager db = dbConfig.getDbManager();
        List<CarVerhicle> carVerhicleList = null;
        try {
            carVerhicleList = db.selector(CarVerhicle.class)
                    .orderBy("time")
                    .findAll();

        } catch (DbException e) {

        }
        JSONObject jsonObject = new JSONObject();

        try {
            if (carVerhicleList == null) {
                jsonObject.put("time", "2000-00-00 00:00:00");
            } else {
                String time = carVerhicleList.get(carVerhicleList.size() - 1).getTime();
                Log.e(TAG, "initCarDataIntoDb: " + time);
                jsonObject.put("time", time);
            }
        } catch (JSONException e) {
        }
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "getCarVerhicleData");
        params.addBodyParameter("reqJson", jsonObject.toString());
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess: " + result);
                try {
                    JSONObject jsonObject1 = new JSONObject(result);
                    String status = jsonObject1.getString("status");
                    String msg = jsonObject1.getString("msg");
                    if (status.equals("1")) {
                        JSONArray data = jsonObject1.getJSONArray("data");
                        List<CarVerhicle> carVerhicleArrayList = new ArrayList<CarVerhicle>();
                        for (int i = 0; i < data.length(); i++) {
                            String carVersion = data.getJSONObject(i).getString("carVersion");
                            String verhicle = data.getJSONObject(i).getString("verhicle");
                            int id = data.getJSONObject(i).getInt("id");
                            int carBrandId = data.getJSONObject(i).getInt("carBrandId");
                            int factoryId = data.getJSONObject(i).getInt("factoryId");
                            int verify = data.getJSONObject(i).getInt("verify");


                            long time = data.getJSONObject(i).getLong("time");
                            String timestampToStringAll = new UtilsRY().getTimestampToStringAll(time);
                            carVerhicleArrayList.add(new CarVerhicle(id, verhicle, carBrandId, factoryId, carVersion, verify, timestampToStringAll));
                        }
                        savaCarVerhicleIntoDb(carVerhicleArrayList);
                    } else {
                        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
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

    private void savaCarVerhicleIntoDb(List<CarVerhicle> verhiclesList) {
        DbConfig dbConfig = new DbConfig(this);
        DbManager db = dbConfig.getDbManager();
        try {
            db.saveOrUpdate(verhiclesList);
        } catch (DbException e) {

        }
    }

    private void initCarBrand() {
        DbConfig dbConfig = new DbConfig(this);
        DbManager db = dbConfig.getDbManager();
        List<CarBrand> carBrandList = null;
        try {
            carBrandList = db.selector(CarBrand.class)
                    .orderBy("time")
                    .findAll();

        } catch (DbException e) {

        }
        JSONObject jsonObject = new JSONObject();

        try {
            if (carBrandList == null) {
                jsonObject.put("time", "2000-00-00 00:00:00");
            } else {
                String time = carBrandList.get(carBrandList.size() - 1).getTime();
                jsonObject.put("time", time);
            }
        } catch (JSONException e) {
        }
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "getCarBrandData");
        params.addBodyParameter("reqJson", jsonObject.toString());
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject1 = new JSONObject(result);
                    String status = jsonObject1.getString("status");
                    String msg = jsonObject1.getString("msg");
                    if (status.equals("1")) {
                        JSONArray data = jsonObject1.getJSONArray("data");
                        List<CarBrand> carBrandArrayList = new ArrayList<CarBrand>();
                        for (int i = 0; i < data.length(); i++) {


                            String icon = data.getJSONObject(i).getString("icon");
                            String imgUrl = data.getJSONObject(i).getString("imgUrl");
                            String name = data.getJSONObject(i).getString("name");
                            int id = data.getJSONObject(i).getInt("id");


                            long time = data.getJSONObject(i).getLong("time");
                            String timestampToStringAll = new UtilsRY().getTimestampToStringAll(time);


                            carBrandArrayList.add(new CarBrand(id, name, imgUrl, icon, timestampToStringAll));
                        }
                        saveCarBrandIntoDb(carBrandArrayList);
                    } else {
                        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
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

    private void saveCarBrandIntoDb(List<CarBrand> brandList) {
        DbConfig dbConfig = new DbConfig(this);
        DbManager db = dbConfig.getDbManager();
        try {
            db.saveOrUpdate(brandList);
        } catch (DbException e) {

        }
    }


    private void initCarDataIntoDb() {
        DbConfig dbConfig = new DbConfig(this);
        DbManager db = dbConfig.getDbManager();
        List<CarFactory> carList = null;
        try {
            carList = db.selector(CarFactory.class)
                    .orderBy("time")
                    .findAll();

        } catch (DbException e) {

        }
        JSONObject jsonObject = new JSONObject();

        try {
            if (carList == null) {
                jsonObject.put("time", "2000-00-00 00:00:00");
            } else {
                String time = carList.get(carList.size() - 1).getTime();
                jsonObject.put("time", time);
            }
        } catch (JSONException e) {
        }
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "getCarFactoryData");
        params.addBodyParameter("reqJson", jsonObject.toString());
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject1 = new JSONObject(result);
                    String status = jsonObject1.getString("status");
                    String msg = jsonObject1.getString("msg");
                    if (status.equals("1")) {
                        JSONArray data = jsonObject1.getJSONArray("data");
                        List<CarFactory> factoryList = new ArrayList<CarFactory>();
                        for (int i = 0; i < data.length(); i++) {
                            String factory = data.getJSONObject(i).getString("factory");
                            int id = data.getJSONObject(i).getInt("id");
                            int carBrandId = data.getJSONObject(i).getInt("carBrandId");
                            long time = data.getJSONObject(i).getLong("time");
                            String timestampToStringAll = new UtilsRY().getTimestampToStringAll(time);
                            factoryList.add(new CarFactory(id, carBrandId, factory, timestampToStringAll));
                        }
                        savaCarFactoryIntoDb(factoryList);
                    } else {
                        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
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

    /**
     * 保存车辆工厂信息到数据库
     *
     * @param factoryList
     */
    private void savaCarFactoryIntoDb(List<CarFactory> factoryList) {
        DbConfig dbConfig = new DbConfig(this);
        DbManager db = dbConfig.getDbManager();
        try {
            db.saveOrUpdate(factoryList);
        } catch (DbException e) {

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
