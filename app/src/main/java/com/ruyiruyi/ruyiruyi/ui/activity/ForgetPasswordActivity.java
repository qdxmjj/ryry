package com.ruyiruyi.ruyiruyi.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ruyiruyi.ruyiruyi.R;
import com.ruyiruyi.ruyiruyi.ui.activity.base.RYBaseActivity;
import com.ruyiruyi.ruyiruyi.utils.RequestUtils;
import com.ruyiruyi.ruyiruyi.utils.UtilsRY;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;
import com.ruyiruyi.rylibrary.cell.ActionBar;
import com.ruyiruyi.rylibrary.utils.TripleDESUtil;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import rx.functions.Action1;

public class ForgetPasswordActivity extends RYBaseActivity {

    private static final String TAG = ForgetPasswordActivity.class.getSimpleName();
    private ActionBar actionBar;
    private TextView getCodeButton;
    private TimeCount mTime;
    private EditText phoneEdit;
    private EditText codeEdit;
    private EditText passwordOneEdit;
    private EditText passwordTwoEdit;
    private TextView saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password, R.id.my_action);
        actionBar = (ActionBar) findViewById(R.id.my_action);
        actionBar.setTitle("忘记密码");
        ;
        actionBar.setShowBackView(true);
        actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(int var1) {
                switch ((var1)) {
                    case -1:
                        onBackPressed();
                        break;
                }
            }
        });

        initView();
    }

    private void initView() {
        getCodeButton = (TextView) findViewById(R.id.get_code_button);
        phoneEdit = (EditText) findViewById(R.id.phone_number_edit);
        codeEdit = (EditText) findViewById(R.id.code_edit);
        passwordOneEdit = (EditText) findViewById(R.id.password_one);
        passwordTwoEdit = (EditText) findViewById(R.id.password_two);
        saveButton = (TextView) findViewById(R.id.save_button);


        mTime = new TimeCount(60000, 1000);

        //获取验证码
        RxViewAction.clickNoDouble(getCodeButton)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        String phone = phoneEdit.getText().toString();
                        if (phone.isEmpty()) {
                            showDialog("手机号不能为空");
                        } else if (!UtilsRY.isMobile(phone)) {
                            showDialog("手机号格式错误");
                        } else {
                            getCode(phone);
                        }
                    }
                });
        RxViewAction.clickNoDouble(saveButton)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        String phone = phoneEdit.getText().toString();
                        if (!UtilsRY.isMobile(phone)) {
                            Toast.makeText(ForgetPasswordActivity.this, "手机号格式错误", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        String code = codeEdit.getText().toString();
                        if (code.isEmpty()) {
                            Toast.makeText(ForgetPasswordActivity.this, "验证码不能为空", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        String passwordStr1 = passwordOneEdit.getText().toString();
                        String passwordStr2 = passwordTwoEdit.getText().toString();
                        if (passwordStr1.length() < 6) {
                            Toast.makeText(ForgetPasswordActivity.this, "密码不能少于6位", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (passwordStr1.isEmpty() || passwordStr2.isEmpty()) {
                            Toast.makeText(ForgetPasswordActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        String password1 = null;
                        String password2 = null;
                        try {
                            byte[] md5One = TripleDESUtil.MD5(passwordStr1);
                            byte[] md5Two = TripleDESUtil.MD5(passwordStr2);
                            password1 = TripleDESUtil.bytes2HexString(md5One);
                            password2 = TripleDESUtil.bytes2HexString(md5Two);

                        } catch (NoSuchAlgorithmException e) {

                        } catch (UnsupportedEncodingException e) {

                        }

                        if (!password1.equals(password2)) {
                            Toast.makeText(ForgetPasswordActivity.this, "密码输入不一致", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        savePassword(phone, code, password1);

                    }
                });
    }

    /**
     * 修改密码
     *
     * @param phone
     * @param code
     * @param password
     */
    private void savePassword(String phone, String code, String password) {
        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("phone", phone);
            jsonObject.put("code", code);
            jsonObject.put("password", password);
        } catch (JSONException e) {
        }
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "changeUserPwd");
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
                        Toast.makeText(ForgetPasswordActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    } else if (status.equals("-999")) {
                        showUserTokenDialog("您的账号在其它设备登录,请重新登录");
                    } else {
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
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
     * 获取短信验证码
     */
    private void getCode(String phoneNumber) {

        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("phone", phoneNumber);
        } catch (JSONException e) {
        }
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "sendMsgChangePwd");
        params.addBodyParameter("reqJson", jsonObject.toString());
        x.http().post(params, new Callback.CommonCallback<String>() {
            private String status;

            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess: " + result);
                try {
                    JSONObject jsonObject1 = new JSONObject(result);
                    status = jsonObject1.getString("status");
                    if (status.equals("1")) {
                        Toast.makeText(ForgetPasswordActivity.this, "发送成功", Toast.LENGTH_SHORT).show();
                        mTime.start();
                        //startCountDown();
                    } else {
                        Toast.makeText(ForgetPasswordActivity.this, "发送失败", Toast.LENGTH_SHORT).show();
                        getCodeButton.setText("重新发送");
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

    private void showDialog(String error) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_error, null);
        TextView error_text = (TextView) dialogView.findViewById(R.id.error_text);
        error_text.setText(error);
        dialog.setTitle("如意如驿");
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
            getCodeButton.setBackgroundResource(R.drawable.btn_primary_enable);
            getCodeButton.setClickable(false);
            getCodeButton.setText("(" + millisUntilFinished / 1000 + "后重发)");
        }

        @Override
        public void onFinish() {
            getCodeButton.setText("重新获取");
            getCodeButton.setClickable(true);
            getCodeButton.setBackgroundResource(R.drawable.login_button);
        }
    }


}
