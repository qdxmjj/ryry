package com.example.warehouse.ui.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.warehouse.MainActivity;
import com.example.warehouse.R;
import com.example.warehouse.db.DbConfig;
import com.example.warehouse.db.User;
import com.example.warehouse.ui.activity.base.WareHouseBaseActivity;
import com.example.warehouse.utils.RequestUtils;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;
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

import rx.functions.Action1;

public class LoginActivity extends WareHouseBaseActivity {
    private static final String TAG = LoginActivity.class.getSimpleName();

    private EditText userPhoneEdit;
    private EditText userPasswordEdit;
    private TextView loginButton;
    private ProgressDialog loginDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //导航栏沉浸式
        fullScreen(this);
        loginDialog = new ProgressDialog(this);

        initView();

    }

    private void initView() {
        userPhoneEdit = (EditText) findViewById(R.id.login_userphone_edittext);
        userPasswordEdit = ((EditText) findViewById(R.id.login_password_edittext));
        loginButton = (TextView) findViewById(R.id.login_button);

        RxViewAction.clickNoDouble(loginButton)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        String userPhone = userPhoneEdit.getText().toString();
                        String passwordStr = userPasswordEdit.getText().toString();
                        if (userPhone.isEmpty()){
                            Toast.makeText(LoginActivity.this, "账号不能为空", Toast.LENGTH_SHORT).show();
                        }else if (passwordStr.isEmpty()){
                            Toast.makeText(LoginActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
                        }else {

                            try {
                                byte[] md5 = TripleDESUtil.MD5(passwordStr);
                                String password = TripleDESUtil.bytes2HexString(md5);
                                loginPassword(userPhone,password);
                            } catch (NoSuchAlgorithmException e) {
                                e.printStackTrace();
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                        }

                      /*  Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        finish();*/
                    }
                });
    }

    /**
     * 登录方法
     * @param userPhone
     * @param password
     */
    private void loginPassword(String userPhone, String password) {
        showDialogProgress(loginDialog,"账号登录中...");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("phone",userPhone);
            jsonObject.put("password",password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "stocks/pwdLogin");
        params.addBodyParameter("reqJson", jsonObject.toString());
        params.setConnectTimeout(10000);
        Log.e(TAG, "loginPassword:--- " + params);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String status = jsonObject.getString("status");
                    String msg = jsonObject.getString("msg");

                    if (status.equals("111111")) {
                        JSONObject data = jsonObject.getJSONObject("data");
                        saveUserToDb(data);
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else if (status.equals("-999")) {
                        showUserTokenDialog("您的账号在其它设备登录,请重新登录");
                    } else {
                        Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {

                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(LoginActivity.this, "网络异常，请检查网络链接", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                hideDialogProgress(loginDialog);
            }
        });
/*       x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String status = jsonObject.getString("status");
                    String msg = jsonObject.getString("msg");

                    if (status.equals("111111")) {
                        JSONObject data = jsonObject.getJSONObject("data");
                    //    saveUserToDb(data);
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else if (status.equals("-999")) {
                        showUserTokenDialog("您的账号在其它设备登录,请重新登录");
                    } else {
                        Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {

                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(LoginActivity.this, "网络异常，请检查网络链接", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                hideDialogProgress(loginDialog);
            }
        });*/
    }

    /**
     * 保存登录成功后的用户信息
     * @param data
     */
    private void saveUserToDb(JSONObject data) {

        try {
            int id = data.getInt("id");
            String name = data.getString("name");
            String phone = data.getString("phone");
            String headimgurl = data.getString("headimgurl");
            String token = data.getString("token");
            String status = data.getString("status");
            User user = new User(id, name, "", phone, headimgurl, "", "", "", token, status, "1");
            DbConfig dbConfig = new DbConfig(this);
            DbManager db = dbConfig.getDbManager();
            db.saveOrUpdate(user);
        } catch (JSONException e) {
        } catch (DbException e) {

        }
    }

    /**
     * 获取状态栏高度
     * @return
     */
    private int getStatusHeight() {
        int statusHeight=-1;
        int resourceId=getResources().getIdentifier("status_bar_height","dimen","android");
        if(resourceId>0){
            return getResources().getDimensionPixelSize(resourceId);
        }
        return 0;
    }

    private void showDialog(String error) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_error, null);
        TextView error_text = (TextView) dialogView.findViewById(R.id.error_text);
        error_text.setText(error);
        dialog.setTitle("如意如驿库管");
        dialog.setIcon(R.drawable.ic_logo_login);
        dialog.setView(dialogView);
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void fullScreen(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                //5.x开始需要把颜色设置透明，否则导航栏会呈现系统默认的浅灰色
                Window window = activity.getWindow();
                View decorView = window.getDecorView();
                //两个 flag 要结合使用，表示让应用的主体内容占用系统状态栏的空间
                int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
                decorView.setSystemUiVisibility(option);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.TRANSPARENT);
                //导航栏颜色也可以正常设置
//                window.setNavigationBarColor(Color.TRANSPARENT);
            } else {
                Window window = activity.getWindow();
                WindowManager.LayoutParams attributes = window.getAttributes();
                int flagTranslucentStatus = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
                int flagTranslucentNavigation = WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION;
                attributes.flags |= flagTranslucentStatus;
//                attributes.flags |= flagTranslucentNavigation;
                window.setAttributes(attributes);
            }
        }
    }


}
