package com.ruyiruyi.merchant.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ruyiruyi.merchant.R;
import com.ruyiruyi.merchant.utils.UtilsRY;
import com.ruyiruyi.merchant.utils.UtilsURL;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;
import com.ruyiruyi.rylibrary.utils.TripleDESUtil;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import rx.functions.Action1;

public class FogetActivity extends BaseActivityb {

    private static final String TAG = FogetActivity.class.getSimpleName();
    private  TimeCount mtimeC;
    private EditText et_phone;
    private EditText et_code;
    private EditText et_passa;
    private EditText et_passb;
    private TextView tv_code;
    private TextView tv_save;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_foget);

        initView();
    }

    private void initView() {
        et_phone = (EditText) findViewById(R.id.et_phone);
        et_code = (EditText) findViewById(R.id.et_code);
        et_passa = (EditText) findViewById(R.id.et_pass_a);
        et_passb = (EditText) findViewById(R.id.et_pass_b);
        tv_code = (TextView) findViewById(R.id.tv_getcode);
        tv_save = (TextView) findViewById(R.id.tv_reset);

        mtimeC = new TimeCount(60000,1000);
        //获取验证码
        RxViewAction.clickNoDouble(tv_code)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        String phone = et_phone.getText().toString();
                        if (phone.isEmpty()){
                            showDialog("手机号不能为空");
                        }else if(!UtilsRY.isMobile(phone)) {
                            showDialog("手机号格式错误");
                        }else {
                            getCode(phone);
                        }
                    }
                });
        RxViewAction.clickNoDouble(tv_save)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        String phone = et_phone.getText().toString();
                        if (!UtilsRY.isMobile(phone)){
                            Toast.makeText(FogetActivity.this, "手机号格式错误", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        String code = et_code.getText().toString();
                        if (code.isEmpty()){
                            Toast.makeText(FogetActivity.this, "验证码不能为空", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        String passwordStr1 = et_passa.getText().toString();
                        String passwordStr2 = et_passb.getText().toString();
                        if (passwordStr1.length()<6){
                            Toast.makeText(FogetActivity.this, "密码不能少于6位", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (passwordStr1.isEmpty() || passwordStr2.isEmpty()){
                            Toast.makeText(FogetActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
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

                        if (!password1.equals(password2)){
                            Toast.makeText(FogetActivity.this, "密码输入不一致", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        savePassword(phone,code,password1);

                    }
                });
    }

    /**
     * 获取短信验证码
     */
    private void getCode(String phoneNumber) {

        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("phone",phoneNumber);
        } catch (JSONException e) {
        }
        RequestParams params = new RequestParams(UtilsURL.LOGIN_PASS_REQUEST_URL + "sendMsgChangePwd");
        params.addBodyParameter("reqJson",jsonObject.toString());
        x.http().post(params, new Callback.CommonCallback<String>() {
            private String status;

            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess: " + result );
                try {
                    JSONObject jsonObject1 = new JSONObject(result);
                    status = jsonObject1.getString("status");
                    if (status.equals("1")){
                        Toast.makeText(FogetActivity.this, "发送成功", Toast.LENGTH_SHORT).show();
                        mtimeC.start();
                        //startCountDown();
                    }else {
                        Toast.makeText(FogetActivity.this, "发送失败", Toast.LENGTH_SHORT).show();
                        tv_code.setText("重新发送");
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

    private void showDialog(String error){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_error, null);
        TextView error_text = (TextView) dialogView.findViewById(R.id.error_text);
        error_text.setText(error);
        dialog.setTitle("如意如驿商家版" );
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


    public void fogetclick(View view){
        switch (view.getId()){
            case R.id.tv_getcode:

                break;
            case R.id.tv_reset:

                break;
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
            tv_code.setBackgroundResource(R.drawable.btn_primary_enable);
            tv_code.setClickable(false);
            tv_code.setText("("+millisUntilFinished / 1000 + "后重发)");
        }

        @Override
        public void onFinish() {
            tv_code.setText("重新获取");
            tv_code.setClickable(true);
            tv_code.setBackgroundResource(R.drawable.login_code_button);
        }
    }

    /**
     * 修改密码
     * @param phone
     * @param code
     * @param password
     */
    private void savePassword(String phone, String code, String password) {
        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("phone",phone);
            jsonObject.put("code",code);
            jsonObject.put("password",password);
        } catch (JSONException e) {
        }
        RequestParams params = new RequestParams(UtilsURL.LOGIN_PASS_REQUEST_URL + "changeUserPwd");
        params.addBodyParameter("reqJson",jsonObject.toString());
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject1 = new JSONObject(result);
                    String status = jsonObject1.getString("status");
                    String msg = jsonObject1.getString("msg");
                    if (status.equals("1")){
                        Toast.makeText(FogetActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                    }else {
                        Toast.makeText(FogetActivity.this, msg, Toast.LENGTH_SHORT).show();
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
}
