package com.ruyiruyi.ruyiruyi.ui.activity;

import android.app.ProgressDialog;
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

import com.ruyiruyi.ruyiruyi.MainActivity;
import com.ruyiruyi.ruyiruyi.R;
import com.ruyiruyi.ruyiruyi.db.DbConfig;
import com.ruyiruyi.ruyiruyi.db.model.User;
import com.ruyiruyi.ruyiruyi.ui.activity.base.RYBaseActivity;
import com.ruyiruyi.ruyiruyi.utils.RequestUtils;
import com.ruyiruyi.ruyiruyi.utils.UtilsRY;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;
import com.ruyiruyi.rylibrary.cell.ActionBar;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.DbManager;
import org.xutils.common.Callback;
import org.xutils.ex.DbException;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.x;

import rx.functions.Action1;

public class WxPhoneActivity extends RYBaseActivity {
    private static final String TAG = WxPhoneActivity.class.getSimpleName();
    private ActionBar actionBar;
    private EditText wxPhoneEdit;
    private EditText wxCodeEdit;
    private TextView wxCodeButton;
    private TextView wxPhonePostButton;
    private TimeCount mTime;
    private ProgressDialog codeDialog;
    private String nickName;
    private String openId;
    private String headUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wx_phone);
        actionBar = (ActionBar) findViewById(R.id.my_action);
        actionBar.setTitle("绑定手机号");;
        actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick(){
            @Override
            public void onItemClick(int var1) {
                switch ((var1)){
                    case -1:
                        onBackPressed();
                        break;
                }
            }
        });
        Intent intent = getIntent();
        nickName = intent.getStringExtra(LoginActivity.NICKNAME);
        openId = intent.getStringExtra(LoginActivity.OPENID);
        headUrl = intent.getStringExtra(LoginActivity.HEADURL);
        codeDialog = new ProgressDialog(this);


        initView();
    }

    private void initView() {
        wxPhoneEdit = (EditText) findViewById(R.id.wx_phone_number_edit);
        wxCodeEdit = (EditText) findViewById(R.id.wx_code_edit);
        wxCodeButton = (TextView) findViewById(R.id.wx_get_code_button);
        wxPhonePostButton = (TextView) findViewById(R.id.wx_phone_button);
        mTime = new TimeCount(60000,1000);

        //获取验证码
        RxViewAction.clickNoDouble(wxCodeButton)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        String phone = wxPhoneEdit.getText().toString();
                        if (phone.isEmpty()){
                            showDialog("手机号不能为空");
                        }else if(!UtilsRY.isMobile(phone)) {
                            showDialog("手机号格式错误");
                        }else {
                            getCode(phone);
                        }
                    }
                });

        //绑定手机号
        RxViewAction.clickNoDouble(wxPhonePostButton)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        String phoneNumber = wxPhoneEdit.getText().toString();
                        String code = wxCodeEdit.getText().toString();
                        if (phoneNumber.isEmpty()){
                            showDialog("手机号不能为空");
                        }else if (code.isEmpty()){
                            showDialog("验证码不能为空");
                        }else if (!UtilsRY.isMobile(phoneNumber)){
                            showDialog("手机号格式错误");
                        }else {
                            codeIsTrue(phoneNumber,code);
                        }

                    }
                });
    }

    private void codeIsTrue(String phoneNumber, String code) {
        showDialogProgress(codeDialog,"提交中中...");
        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("phone",phoneNumber);
            jsonObject.put("code",code);
            jsonObject.put("wxInfoId",openId);
        } catch (JSONException e) {
        }
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "verificationCode");
        params.setConnectTimeout(10000);
        params.addBodyParameter("reqJson",jsonObject.toString());
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess: " + result);
                try {
                    JSONObject jsonObject1 = new JSONObject(result);
                    String status = jsonObject1.getString("status");
                    String msg = jsonObject1.getString("msg");
                    Log.e(TAG, "onSuccess: " + status);
                    if (status.equals("1")){//用户信息不存在  跳转到注册界面
                        Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                        intent.putExtra("PHONE",wxPhoneEdit.getText().toString());
                        intent.putExtra(LoginActivity.HEADURL,headUrl);
                        intent.putExtra(LoginActivity.NICKNAME,nickName);
                        intent.putExtra(LoginActivity.OPENID,openId);
                        startActivity(intent);
                    }else if (status.equals("111111")){ //用户信息存在返回用户信息
                        JSONObject data = jsonObject1.getJSONObject("data");
                        saveUserToDb(data);//保存用户信息到数据库
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    }else {
                        Toast.makeText(WxPhoneActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {

                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                //  codeDialog.dismiss();
                //   hideDialogProgress(codeDialog);
                Log.e(TAG, "onError: " );
                Toast.makeText(WxPhoneActivity.this, "登陆失败，请检查网络链接", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {
                //   codeDialog.dismiss();
                //hideDialogProgress(codeDialog);
                Log.e(TAG, "onCancelled: ");
            }

            @Override
            public void onFinished() {
                // codeDialog.dismiss();
                hideDialogProgress(codeDialog);
                Log.e(TAG, "onFinished: ");
            }
        });
    }

    private void showDialog(String error){
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

    /**
     * 获取短信验证码
     */
    private void getCode(String phoneNumber) {
        mTime.start();
        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("phone",phoneNumber);
        } catch (JSONException e) {
        }
        showDialogProgress(codeDialog,"获取验证码中...");
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "sendMsg");
        params.addBodyParameter("reqJson",jsonObject.toString());
        params.setConnectTimeout(10000);
        params.setMethod(HttpMethod.POST);
        x.http().post(params, new Callback.CommonCallback<String>() {
            private String msg;
            private String status;

            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess: " + result );
                try {
                    JSONObject jsonObject1 = new JSONObject(result);
                    status = jsonObject1.getString("status");
                    msg = jsonObject1.getString("msg");
                    if (status.equals("1")){
                        Toast.makeText(WxPhoneActivity.this, "发送成功", Toast.LENGTH_SHORT).show();
                        // mTime.start();
                        //startCountDown();
                    }else {
                        mTime.onFinish();
                        Toast.makeText(WxPhoneActivity.this, msg , Toast.LENGTH_SHORT).show();
                        wxCodeButton.setText("重新发送");

                    }
                } catch (JSONException e) {
                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                mTime.onFinish();
                mTime.cancel();
                Toast.makeText(WxPhoneActivity.this, "登陆失败，请检查网络链接", Toast.LENGTH_SHORT).show();
                // getCodeButton.setText("重新发送");

            }

            @Override
            public void onCancelled(CancelledException cex) {
                Log.e(TAG, "onCancelled: ");

            }

            @Override
            public void onFinished() {
                hideDialogProgress(codeDialog);
            }
        });
    }

    /**
     * 将用户保存到数据库
     * @param data
     */
    private void saveUserToDb(JSONObject data) {
        User user = new User();
        try {
            user.setId(data.getInt("id"));
            user.setNick(data.getString("nick"));
            // user.setPassword(data.getString("password"));
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
            wxCodeButton.setBackgroundResource(R.drawable.btn_primary_enable);
            wxCodeButton.setClickable(false);
            wxCodeButton.setText("("+millisUntilFinished / 1000 + "后重发)");
        }

        @Override
        public void onFinish() {
            wxCodeButton.setText("重新获取");
            wxCodeButton.setClickable(true);
            wxCodeButton.setBackgroundResource(R.drawable.login_button);
        }
    }

}
