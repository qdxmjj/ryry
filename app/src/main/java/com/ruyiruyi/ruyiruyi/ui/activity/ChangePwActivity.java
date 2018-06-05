package com.ruyiruyi.ruyiruyi.ui.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ruyiruyi.ruyiruyi.R;
import com.ruyiruyi.ruyiruyi.db.DbConfig;
import com.ruyiruyi.ruyiruyi.db.model.User;
import com.ruyiruyi.ruyiruyi.ui.activity.base.RyBaseActivity;
import com.ruyiruyi.ruyiruyi.utils.RequestUtils;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;
import com.ruyiruyi.rylibrary.cell.ActionBar;
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

public class ChangePwActivity extends RyBaseActivity {

    private ActionBar mActionBar;
    private EditText et_yuanmima;
    private EditText et_xinmima_a_;
    private EditText et_xinmima_b_;
    private TextView tv_wancheng;

    private String passWord_yuan_;
    private String passWord_xin_a_;
    private String passWord_xin_b_;
    private ProgressDialog progressDialog;
    private String TAG = ChangePwActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pw);
        mActionBar = (ActionBar) findViewById(R.id.acbar_change_pw);
        mActionBar.setTitle("修改密码");
        mActionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(int var1) {
                switch (var1) {
                    case -1:
                        onBackPressed();
                        break;
                }
            }
        });
        progressDialog = new ProgressDialog(ChangePwActivity.this);

        initView();
        bindView();


    }

    private void bindView() {
        RxViewAction.clickNoDouble(tv_wancheng).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (et_yuanmima.getText() == null || et_yuanmima.getText().length() == 0) {
                    showDialog("请输入原密码");
                    return;
                } else {
                    byte[] md5 = new byte[0];
                    try {
                        md5 = TripleDESUtil.MD5(et_yuanmima.getText().toString());
                    } catch (NoSuchAlgorithmException e) {
                    } catch (UnsupportedEncodingException e) {
                    }
                    passWord_yuan_ = TripleDESUtil.bytes2HexString(md5);
                }
                if (et_xinmima_a_.getText() == null || et_xinmima_a_.getText().length() == 0) {
                    showDialog("请输入新密码");
                    return;
                } else {
                    byte[] md5 = new byte[0];
                    try {
                        md5 = TripleDESUtil.MD5(et_xinmima_a_.getText().toString());
                    } catch (NoSuchAlgorithmException e) {
                    } catch (UnsupportedEncodingException e) {
                    }
                    passWord_xin_a_ = TripleDESUtil.bytes2HexString(md5);
                }
                if (et_xinmima_a_.getText().toString().length() < 6) {
                    showDialog("密码不能少于6位");
                    return;
                }
                if (et_xinmima_b_.getText() == null || et_xinmima_b_.getText().length() == 0) {
                    showDialog("请输入确认密码");
                    return;
                } else {
                    byte[] md5 = new byte[0];
                    try {
                        md5 = TripleDESUtil.MD5(et_xinmima_b_.getText().toString());
                    } catch (NoSuchAlgorithmException e) {
                    } catch (UnsupportedEncodingException e) {
                    }
                    passWord_xin_b_ = TripleDESUtil.bytes2HexString(md5);
                }
                if (et_xinmima_b_.getText().toString().length() < 6) {
                    showDialog("密码不能少于6位");
                    return;
                }
                if (!passWord_xin_a_.equals(passWord_xin_b_)) {
                    showDialog("两次输入密码不一致");
                    return;
                }

                showSaveDialog("确定修改吗？");
            }
        });
    }

    private void initView() {
        et_yuanmima = findViewById(R.id.et_yuanmima);
        et_xinmima_a_ = findViewById(R.id.et_xinmima_a_);
        et_xinmima_b_ = findViewById(R.id.et_xinmima_b_);
        tv_wancheng = findViewById(R.id.tv_wancheng);
    }


    private void showSaveDialog(String error) {
        AlertDialog dialog = new AlertDialog.Builder(this).create();
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_save_commit, null);
        TextView error_text = (TextView) dialogView.findViewById(R.id.save_text);
        error_text.setText(error);
        dialog.setTitle("如意如驿");
        dialog.setIcon(R.drawable.ic_logo_login);
        dialog.setView(dialogView);
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "再看看", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        //确认提交 请求提交数据
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "是的", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                showDialogProgress(progressDialog, "修改提交中...");
                JSONObject object = new JSONObject();
                try {
                    object.put("oldPassword", passWord_yuan_);
                    object.put("newPassword", passWord_xin_a_);
                    User user = new DbConfig().getUser();
                    object.put("phone", user.getPhone());

                } catch (JSONException e) {
                }
                RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "changeStorePwdByOldPwd");
                params.addBodyParameter("reqJson", object.toString());
                params.addBodyParameter("token", new DbConfig().getToken());
                params.setConnectTimeout(6000);
                Log.e(TAG, "onClick: params.toString() " + params.toString());
                x.http().post(params, new Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        Log.e(TAG, "onSuccess:  result = " + result);

                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            String msg = jsonObject.getString("msg");
                            int status = jsonObject.getInt("status");
                            Toast.makeText(ChangePwActivity.this, msg, Toast.LENGTH_SHORT).show();
                            if (status == 1) {
                                //退出登录更改本地用户信息并跳转登录界面
                                User user = new DbConfig().getUser();
                                user.setIsLogin("0");
                                DbManager dbManager = new DbConfig().getDbManager();
                                try {
                                    dbManager.saveOrUpdate(user);
                                } catch (DbException e) {
                                }
                                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                startActivity(intent);
                            }

                        } catch (JSONException e) {
                        }

                    }

                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {
                        Toast.makeText(ChangePwActivity.this, "密码修改失败,请检查网络", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(CancelledException cex) {

                    }

                    @Override
                    public void onFinished() {
                        hideDialogProgress(progressDialog);
                    }
                });
            }
        });

        dialog.show();
        //设置按钮颜色
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.theme_primary));
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.theme_primary));

    }

    private void showDialog(String error) {
        AlertDialog dialog = new AlertDialog.Builder(this).create();
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_error, null);
        TextView error_text = (TextView) dialogView.findViewById(R.id.error_text);
        error_text.setText(error);
        dialog.setTitle("如意如驿");
        dialog.setIcon(R.drawable.ic_logo_login);
        dialog.setView(dialogView);
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show();
        //设置按钮颜色
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.theme_primary));
    }
}
