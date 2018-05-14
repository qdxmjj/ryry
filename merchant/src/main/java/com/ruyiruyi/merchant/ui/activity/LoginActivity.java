package com.ruyiruyi.merchant.ui.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ruyiruyi.merchant.MainActivity;
import com.ruyiruyi.merchant.db.DbConfig;
import com.ruyiruyi.merchant.db.model.User;
import com.ruyiruyi.merchant.utils.UtilsRY;
import com.ruyiruyi.merchant.utils.UtilsURL;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;

import com.ruyiruyi.merchant.R;
import com.ruyiruyi.rylibrary.utils.TripleDESUtil;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.DbManager;
import org.xutils.common.Callback;
import org.xutils.ex.DbException;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import rx.functions.Action1;

public class LoginActivity extends BaseActivityb {

    private static final String TAG = LoginActivity.class.getSimpleName();
    private EditText et_zhanghao;
    private EditText et_pass;
    private TextView tv_foget;
    private TextView tv_login;
    private TextView tv_register;

    private int status = 1;
    private String msg = null;
/*
   2   private DbManager.DaoConfig daoConfig = new  DbManager.DaoConfig()
            // 设置数据库名字
            .setDbName("abc.db")
            // 设置数据库版本
            .setDbVersion(1)
            // 设置数据库的路径
             .setDbDir(Environment.getExternalStorageDirectory())
            // 设置数据库允许事务
            .setAllowTransaction(true)
            // 升级监听
            .setDbUpgradeListener(new DbManager.DbUpgradeListener() {
                @Override
                public void onUpgrade(DbManager db, int oldVersion, int newVersion) {
                    // 进行表或是数据的变更
                }
            });*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "请授权读写手机存储权限", Toast.LENGTH_SHORT).show();
            finish();
        }
        if (ContextCompat.checkSelfPermission(this,
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
        initView();
/*    2    DbManager db = x.getDb(daoConfig);
        try {
            db.save(new Car(1,"asa"));
        } catch (DbException e) {

        }*/
    }

    private void initView() {

        et_zhanghao = (EditText) findViewById(R.id.et_zhanghao);
        et_pass = (EditText) findViewById(R.id.et_pass);
        tv_foget = (TextView) findViewById(R.id.tv_foget);
        tv_login = (TextView) findViewById(R.id.tv_login);
        tv_register = (TextView) findViewById(R.id.tv_register);

        RxViewAction.clickNoDouble(tv_login)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
//     3                   startActivity(new Intent(getApplicationContext(),RegisterMapActivity.class));
                        String phone = et_zhanghao.getText().toString();
                        String pass = et_pass.getText().toString();
                        byte[] md5 = new byte[0];
                        try {
                            md5 = TripleDESUtil.MD5(pass);
                            String password = TripleDESUtil.bytes2HexString(md5);

                            if (phone.isEmpty()) {
                                showDialog("手机号不能为空");
                                return;
                            }
                            if (!UtilsRY.isMobile(phone)) {
                                showDialog("手机号格式错误");
                                return;
                            }
                            if (pass.isEmpty()) {
                                showDialog("密码不能为空");
                                return;
                            }
                            //login ！！
                            loginByPassWord(phone, password);
                            //测试用登录成功
//                            loginTest();
                        } catch (NoSuchAlgorithmException e) {

                        } catch (UnsupportedEncodingException e) {

                        }

                    }
                });

    }

    //测试用 登录成功
    private void loginTest() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        LoginActivity.this.finish();
    }

    //登录
    private void loginByPassWord(final String phone, final String pass) {
        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("phone", phone);
            jsonObject.put("password", pass);
        } catch (JSONException e) {

        }
        Log.e(TAG, "loginByPassWord:111 ph" + phone + "mm" + pass);
        RequestParams params = new RequestParams(UtilsURL.REQUEST_URL + "storePwdLogin");
        params.addBodyParameter("reqJson", jsonObject.toString());
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess: " + result);
                try {
                    JSONObject jsonObject1 = new JSONObject(result);
                    String msg = jsonObject1.getString("msg");
                    String status = jsonObject1.getString("status");
                    if (status.equals("111111")) {
                        Log.e(TAG, "LoginActivity =-->onSuccess: 登录请求？？？？？？  result = " + result);
                        JSONObject data = jsonObject1.getJSONObject("data");
                        // 存储用户信息
                        saveUserToDb(data);
                        Log.e(TAG, "LoginActivity =-->onSuccess: 登录请求 ？？  data.length() == " + data.length());
                        //login
                        Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        LoginActivity.this.finish();
                    } else {
                        Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {

                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.w(TAG, "onError: 登录请求 onError ??");
            }

            @Override
            public void onCancelled(CancelledException cex) {
                Log.w(TAG, "onCancelled: 登录请求 onCancelled ??");
            }

            @Override
            public void onFinished() {
                Log.w(TAG, "onFinished: 登录请求 onFinished ??");
            }
        });
    }

    //保存用户到数据库
    private void saveUserToDb(JSONObject data) {

        User user = new User();
        try {
            user.setId(data.getInt("id"));
            user.setStoreName(data.getString("storeName"));
            user.setStatus(data.getInt("status"));
            user.setStoreImgUrl(data.getString("storeImgUrl"));
            Log.e(TAG, "saveUserToDb: storeImgUrl == >" + user.getStoreImgUrl());
            user.setStoreLoginName(data.getString("storeLoginName"));
            user.setToken(data.getString("token"));
            user.setUpdateTime(data.getString("updateTime"));
            user.setIsLogin("1");
            DbConfig dbConfig = new DbConfig();
            DbManager db = dbConfig.getDbManager();


            db.saveOrUpdate(user);
        } catch (JSONException e) {
        } catch (DbException e) {

        }
    }

    /**
     * 保存用户到数据库
     */
    private void savaUserDb(String phone, String token) {
        DbConfig dbConfig = new DbConfig();
        DbManager.DaoConfig daoConfig = dbConfig.getDaoConfig();
        DbManager db = x.getDb(daoConfig);
        List<User> data = new ArrayList<>();
        User user = new User();
        user.setPhone(phone);
        user.setToken(token);
        data.add(user);
        try {
            db.saveOrUpdate(data);
        } catch (DbException e) {

        }

    }

    public void myclick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.tv_foget:
                intent = new Intent(LoginActivity.this, FogetActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_register:
                intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    private void showDialog(String error) {
        AlertDialog dialog = new AlertDialog.Builder(this).create();
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_error, null);
        TextView error_text = (TextView) dialogView.findViewById(R.id.error_text);
        error_text.setText(error);
        dialog.setTitle("如意如驿商家版");
        dialog.setIcon(R.drawable.ic_logo_huise);
        dialog.setView(dialogView);
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show();
        //设置按钮颜色
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.theme_primary));
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.theme_primary));

    }
}
