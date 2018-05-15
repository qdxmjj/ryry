package com.ruyiruyi.merchant.ui.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.CountDownTimer;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

import com.ruyiruyi.merchant.MainActivity;
import com.ruyiruyi.merchant.R;
import com.ruyiruyi.merchant.db.DbConfig;
import com.ruyiruyi.merchant.db.model.Category;
import com.ruyiruyi.merchant.db.model.Province;
import com.ruyiruyi.merchant.db.model.ServiceType;
import com.ruyiruyi.merchant.utils.UtilsRY;
import com.ruyiruyi.merchant.utils.UtilsURL;
import com.ruyiruyi.rylibrary.base.BaseActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.DbManager;
import org.xutils.common.Callback;
import org.xutils.ex.DbException;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.Date;
import java.util.List;

public class StartAppActivity extends BaseActivity {
    private final String TAG = StartAppActivity.class.getSimpleName();
    TimeCount mTimeCount;
    private int isLogin = 0;  //0 未登录  1 已登录

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_app);
        //权限获取
        requestPower();
        //initIsLogin
        DbConfig dbConfig = new DbConfig();
        if (dbConfig.getIsLogin()) {
            isLogin = 1;
        } else {
            isLogin = 0;
        }

        mTimeCount = new TimeCount(3000, 1000);
        mTimeCount.start();

        initProvinceData();


    }


    private void initProvinceData() {
//   0     date = new Date();
        List<Province> provinceList = null;
        try {
            provinceList = new DbConfig().getDbManager().selector(Province.class).orderBy("time").findAll();
        } catch (DbException e) {

        }
        JSONObject jsonObject = new JSONObject();
        try {
            //时间请求 ！！
            if (provinceList == null) {
                jsonObject.put("time", "2000-00-00 00:00:00");
            } else {
                String time = provinceList.get(provinceList.size() - 1).getTime();
                jsonObject.put("time", time);
//                Log.e(TAG, "initProvinceData: time = " + time);
            }

        } catch (JSONException e) {

        }
        RequestParams params = new RequestParams(UtilsURL.REQUEST_URL + "getAllPositon");
        params.addBodyParameter("reqJson", jsonObject.toString());
        Log.e(TAG, "initProvinceData: params = " + params);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess: " + result);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(result);
                    String status = jsonObject.getString("status");
                    String msg = jsonObject.getString("msg");
                    JSONArray data = jsonObject.getJSONArray("data");
                    Log.e(TAG, "onSuccess: getData and To Db ??  status = " + status + "msg = " + msg + "data = " + data.toString());
                    saveProvinceToDb(data);
                } catch (JSONException e) {
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e(TAG, "onError: getData and To Db ?? ");
            }

            @Override
            public void onCancelled(CancelledException cex) {
                Log.e(TAG, "onCancelled: getData and To Db ??");
            }

            @Override
            public void onFinished() {

            }
        });
    }

    private void saveProvinceToDb(JSONArray data) {
        Province province;
        DbManager db = (new DbConfig()).getDbManager();
        for (int i = 0; i < data.length(); i++) {
            province = new Province();
            try {
                JSONObject obj = (JSONObject) data.get(i);
                //保存并转换time请求的time
                long time = obj.getLong("time");
                String timestampToStringAll = new UtilsRY().getTimestampToStringAll(time);
                province.setTime(timestampToStringAll);
                province.setDefinition(obj.getInt("definition"));
                province.setFid(obj.getInt("fid"));
                province.setId(obj.getInt("id"));
                province.setName(obj.getString("name"));
//                Log.e(TAG, "saveProvinceToDb: definition==>" + province.getDefinition());
                db.saveOrUpdate(province);
            } catch (JSONException e) {

            } catch (DbException e) {

            }
        }
    }

    //权限获取
    public void requestPower() {
        //判断是否已经赋予权限
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            //如果应用之前请求过此权限但用户拒绝了请求，此方法将返回 true。
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {//这里可以写个对话框之类的项向用户解释为什么要申请权限，并在对话框的确认键后续再次申请权限
            } else {
                //申请权限，字符串数组内是一个或多个要申请的权限，1是申请权限结果的返回参数，在onRequestPermissionsResult可以得知申请结果
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.CAMERA,
                                Manifest.permission.ACCESS_FINE_LOCATION
                        }, 1);
            }
        }
    }


    class TimeCount extends CountDownTimer {

        /**
         * @param millisInFuture    The number of millis in the future from the call
         *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
         *                          is called.
         * @param countDownInterval The interval along the way to receive
         *                          {@link #onTick(long)} callbacks.
         */
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            //进行中
        }

        @Override
        public void onFinish() {
            //结束
            if (isLogin == 0) {
                Intent intent = new Intent(StartAppActivity.this, LoginActivity.class);
                startActivity(intent);
                StartAppActivity.this.finish();
            } else {
                Intent intent = new Intent(StartAppActivity.this, MainActivity.class);
                startActivity(intent);
                StartAppActivity.this.finish();
            }
        }
    }
}
