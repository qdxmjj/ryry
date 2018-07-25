package com.ruyiruyi.merchant.ui.activity;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ruyiruyi.merchant.MainActivity;
import com.ruyiruyi.merchant.R;
import com.ruyiruyi.merchant.db.DbConfig;
import com.ruyiruyi.merchant.db.model.User;
import com.ruyiruyi.merchant.ui.service.LuncherDownlodeService;
import com.ruyiruyi.rylibrary.base.BaseActivity;

import org.xutils.ex.DbException;

import cn.jpush.android.api.JPushInterface;

public class LaunchActivity extends Activity {
    private final String TAG = LaunchActivity.class.getSimpleName();
    private TextView tv_num;
    private TextView tv_txt;
    private boolean isFirst = false;
    //    TimeCount mTimeCount;
    private int isLogin = 0;  //0 未登录  1 已登录
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    //判断是否是首次进入 1.(初次进入)开启并监测下载服务Service并开始计时  2.(再次进入)开启后台下载服务Service
                    SharedPreferences sf = getSharedPreferences("data", MODE_PRIVATE);//判断是否是第一次进入
                    boolean isFirstIn = sf.getBoolean("isFirstIn", true);
                    SharedPreferences.Editor editor = sf.edit();
                    if (isFirstIn) {//初次进入
                    /* editor.putBoolean("isFirstIn", false);  修改标志位移动到service下载完毕数据后*/
                        isFirst = true;
                        tv_txt.setVisibility(View.VISIBLE);
                        tv_num.setVisibility(View.VISIBLE);
                        registerService(); //TODO
                        StartDownlodeService();
                        mTimeCount = new TimeCount(8000, 80);
                        mTimeCount.start();
                    } else {//再次进入
                        isFirst = false;
                        tv_txt.setVisibility(View.GONE);
                        tv_num.setVisibility(View.GONE);
                        StartDownlodeService();
                        Message message = new Message();
                        message.what = 1;
                        handler.sendMessageDelayed(message, 3000);
                    }
                    editor.commit();

                    break;
                case 1:
                    goMain();

                    break;
            }

        }
    };
    private int progress = 0;
    private MyReceiver receiver = null;
    private TimeCount mTimeCount;

    private void goMain() {
        //initIsLogin
        DbConfig dbConfig = new DbConfig(getApplicationContext());
        if (dbConfig.getIsLogin()) {
            isLogin = 1;
        } else {
            isLogin = 0;
        }


        //跳转
        if (isLogin == 0) {
            //跳转登录页面
            Intent intent = new Intent(LaunchActivity.this, LoginActivity.class);
            startActivity(intent);
            LaunchActivity.this.finish();
        } else {
            //跳转主页
            Intent intent = new Intent(LaunchActivity.this, MainActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("page", "my");
            intent.putExtras(bundle);
            startActivity(intent);
            LaunchActivity.this.finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_app);
        tv_num = findViewById(R.id.tv_num);
        tv_txt = findViewById(R.id.tv_txt);

        tv_txt.setVisibility(View.GONE);
        tv_num.setVisibility(View.GONE);

        //权限获取
        requestPower();

        //极光推送绑定别名 初始化0 -->  999
        JPushInterface.setAlias(getApplicationContext(), 0, "999");

    }


    private void judgePower() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "请授权读写手机存储权限", Toast.LENGTH_SHORT).show();
            finish();
        }
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "请授权相机权限", Toast.LENGTH_SHORT).show();
            finish();
        }
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "请授权定位权限", Toast.LENGTH_SHORT).show();
            finish();
        }
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "请授权录音权限", Toast.LENGTH_SHORT).show();
            finish();
        }

    }


    public void StartDownlodeService() {
        //启动服务
        Intent intent = new Intent(this, LuncherDownlodeService.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("Key", LuncherDownlodeService.Control.PLAY);
        intent.putExtras(bundle);
        startService(intent);
    }

    public void registerService() {
        //注册广播接收器
        receiver = new MyReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.ruyiruyi.merchant.ui.service.LuncherDownlodeService");
        LaunchActivity.this.registerReceiver(receiver, filter);

    }


    //权限获取
    public void requestPower() {
        //判断是否已经赋予权限
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this,
                        Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this,
                        Manifest.permission.RECORD_AUDIO)
                        != PackageManager.PERMISSION_GRANTED) {
            //如果应用之前请求过此权限但用户拒绝了请求，此方法将返回 true。
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {//这里可以写个对话框之类的项向用户解释为什么要申请权限，并在对话框的确认键后续再次申请权限
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.CAMERA,
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.RECORD_AUDIO
                        }, 1);
            } else {
                //申请权限，字符串数组内是一个或多个要申请的权限，1是申请权限结果的返回参数，在onRequestPermissionsResult可以得知申请结果
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.CAMERA,
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.RECORD_AUDIO
                        }, 1);
            }
        } else {
            //用户全部点击授权
            Message message = new Message();
            message.what = 0;
            handler.sendMessage(message);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        Log.e(TAG, "onRequestPermissionsResult:requestCode --" + requestCode);
        Log.e(TAG, "onRequestPermissionsResult: permissions--" + permissions);
        Log.e(TAG, "onRequestPermissionsResult: grantResults--" + grantResults);
        if (requestCode == 1) {
            boolean isPassPermision = true;
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == -1) {
                    isPassPermision = false;
                }
            }
            if (isPassPermision) {
                //权限通过
                Message message = new Message();
                message.what = 0;
                handler.sendMessage(message);
            } else {
                //有未授权权限
                judgePower();
            }
        }
    }


    public class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            int count = bundle.getInt("count");
            progress = progress + count;
            tv_num.setText(progress + "%");

            if (progress == 100) {
                //修改判断是否是第一次进入的标志位
                SharedPreferences sf = getSharedPreferences("data", MODE_PRIVATE);//判断是否是第一次进入
                SharedPreferences.Editor editor = sf.edit();
                if (isFirst) {//首次
                    editor.putBoolean("isFirstIn", false);
                    mTimeCount.cancel();
                } else {//非首次
                }
                editor.commit();

                Message message = new Message();
                message.what = 1;
                handler.sendMessage(message);
            }
        }
    }

    class TimeCount extends CountDownTimer {

        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            //进行中
            tv_num.setText((99 - millisUntilFinished / 80) + "%");
            Log.e(TAG, "onTick:   ===" + (99 - millisUntilFinished / 80) + "%");
        }

        @Override
        public void onFinish() {
            //结束
            Log.e(TAG, "onFinish: ~~~~~~");

        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //结束服务.
        stopService(new Intent(LaunchActivity.this, LuncherDownlodeService.class));
        //取消注册广播
        if (isFirst) {
            unregisterReceiver(receiver);
        } //TODO
    }
}
