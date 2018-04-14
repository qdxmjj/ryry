package com.ruyiruyi.merchant.ui.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.util.Util;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.jakewharton.rxbinding.view.RxView;
import com.ruyiruyi.merchant.MainActivity;
import com.ruyiruyi.merchant.db.DbConfig;
import com.ruyiruyi.merchant.db.model.User;
import com.ruyiruyi.merchant.utils.UtilsRY;
import com.ruyiruyi.merchant.utils.UtilsURL;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;
import com.ruyiruyi.rylibrary.base.BaseActivity;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        initView();
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
                        String phone = et_zhanghao.getText().toString();
                        String pass = et_pass.getText().toString();
                        byte[] md5 = new byte[0];
                        try {
                            md5 = TripleDESUtil.MD5(pass);
                            String password = TripleDESUtil.bytes2HexString(md5);

                            if (phone.isEmpty() ) {
                                showDialog("手机号不能为空");
                                return;
                            } if (!UtilsRY.isMobile(phone)) {
                                showDialog("手机号格式错误");
                                return;
                            }if (pass.isEmpty() ) {
                                showDialog("密码不能为空");
                                return;
                            }
                            //login ！！
//                            loginByPassWord(phone, password);
                            //测试用登录成功
                            loginTest();
                        } catch (NoSuchAlgorithmException e) {

                        } catch (UnsupportedEncodingException e) {

                        }

                    }
                });

    }

    //测试用 登录成功
    private void loginTest() {
        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
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
        Log.e(TAG, "loginByPassWord:111 ph"+phone+"mm"+pass);
        RequestParams params = new RequestParams(UtilsURL.LOGIN_PASS_REQUEST_URL + "pwdLogin");
        params.addBodyParameter("reqJson", jsonObject.toString());
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess: " + result);
                try {
                    JSONObject jsonObject1 = new JSONObject(result);
                    String msg = jsonObject1.getString("msg");
                    int status = jsonObject1.getInt("status");
                    if (status==111111){
                        JSONObject data = jsonObject1.getJSONObject("data");
                        saveUserToDb(data);
                        //login
                        Toast.makeText(LoginActivity.this,"登陆成功",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                        startActivity(intent);
                        LoginActivity.this.finish();
                    }else {
                        Toast.makeText(LoginActivity.this,msg,Toast.LENGTH_SHORT).show();
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
//保存用户到数据库
    private void saveUserToDb(JSONObject data) {

        User user = new User();
        try {
            user.setId(data.getInt("id"));
            user.setNick(data.getString("nick"));
            user.setPassword(data.getString("password"));
            user.setPhone(data.getString("phone"));
            user.setAge(data.getString("age"));
            long birthday = data.getLong("birthday");
            String birthdayStr = new UtilsRY().getTimestampToString(birthday);
            user.setBirthday(birthdayStr);
            user.setEmail(data.getString("email"));
            user.setGender(data.getInt("gender"));
            user.setHeadimgurl(data.getString("headimgurl"));
            user.setToken(data.getString("token"));
            user.setStatus(data.getString("status"));
            user.setFirstAddCar(data.getInt("firstAddCar"));
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
    private void savaUserDb(String phone ,String token) {
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
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_error, null);
        TextView error_text = (TextView) dialogView.findViewById(R.id.error_text);
        error_text.setText(error);
        dialog.setTitle("如意如驿商家版");
        dialog.setIcon(R.drawable.ic_head);
        dialog.setView(dialogView);
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}