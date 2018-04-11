
package com.ruyiruyi.ruyiruyi.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Environment;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ruyiruyi.ruyiruyi.MainActivity;
import com.ruyiruyi.ruyiruyi.R;
import com.ruyiruyi.ruyiruyi.db.DbConfig;
import com.ruyiruyi.ruyiruyi.db.model.Newest;
import com.ruyiruyi.ruyiruyi.db.model.User;
import com.ruyiruyi.ruyiruyi.db.model.UserTest;
import com.ruyiruyi.ruyiruyi.utils.RequestUtils;
import com.ruyiruyi.ruyiruyi.utils.TimeCount;
import com.ruyiruyi.ruyiruyi.utils.UIOpenHelper;
import com.ruyiruyi.ruyiruyi.utils.UtilsRY;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;
import com.ruyiruyi.rylibrary.base.BaseActivity;
import com.ruyiruyi.rylibrary.ui.dialog.ErrorDialog;
import com.ruyiruyi.rylibrary.utils.AndroidUtilities;
import com.ruyiruyi.rylibrary.utils.TripleDESUtil;

import org.json.JSONArray;
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
import java.util.Timer;
import java.util.TimerTask;

import rx.functions.Action1;

public class LoginActivity extends BaseActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();
    private TextView loginType;
    public static boolean isCode = false;
    private FrameLayout codeLayout;
    private EditText phoneEdit;
    private EditText codeEdit;
    private EditText passwordEdit;
    private TextView loginButton;
    private FrameLayout passwordLayout;
    private TextView getCodeButton;

    private volatile int time = 60000;
    private double lastCurrentTime;
    private Timer timeTimer;
    private final Object timerSync = new Object();
    private TimeCount mTime;
    private TextView forgetPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        String ANDROID_ID = Settings.System.getString(this.getContentResolver(), Settings.System.ANDROID_ID);

        initView();


        initDataFromDb();
    }

    private void initView() {
        loginType = (TextView) findViewById(R.id.login_type);
        codeLayout = (FrameLayout) findViewById(R.id.code_login);
        passwordLayout = (FrameLayout) findViewById(R.id.password_login_layout);
        phoneEdit = (EditText) findViewById(R.id.phone_number_edit);
        codeEdit = (EditText) findViewById(R.id.code_edit);
        passwordEdit = (EditText) findViewById(R.id.password_edit);
        loginButton = (TextView) findViewById(R.id.login_button);
        getCodeButton = (TextView) findViewById(R.id.get_code_button);
        forgetPassword = (TextView) findViewById(R.id.forget_password);

        mTime = new TimeCount(60000,1000);



        changeLoginView();
        //更改登陆方式
        RxViewAction.clickNoDouble(loginType)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (isCode){
                            isCode = false;
                            changeLoginView();
                        }else {
                            isCode = true;
                            changeLoginView();
                        }
                    }
                });
        //登陆按钮店家
        RxViewAction.clickNoDouble(loginButton)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        String phoneNumber = phoneEdit.getText().toString();
                        if (phoneNumber.isEmpty()){
                            showDialog("手机号不能为空");
                        }else if (!UtilsRY.isMobile(phoneNumber)){
                            showDialog("手机号格式错误");
                        }else {
                            if (isCode){    //验证码登陆
                                String code = codeEdit.getText().toString();
                                logdinByCode(phoneNumber,code);
                            }else {         //密码登陆
                                String passwordStr = passwordEdit.getText().toString();
                                try {
                                    byte[] md5 = TripleDESUtil.MD5(passwordStr);
                                    String password = TripleDESUtil.bytes2HexString(md5);
                                    loginByPassword(phoneNumber,password);
                                } catch (NoSuchAlgorithmException e) {

                                } catch (UnsupportedEncodingException e) {

                                }

                            }
                        }


                    }
                });
        //获取验证码
        RxViewAction.clickNoDouble(getCodeButton)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        String phone = phoneEdit.getText().toString();
                        if (phone.isEmpty()){
                            showDialog("手机号不能为空");
                        }else if(!UtilsRY.isMobile(phone)) {
                            showDialog("手机号格式错误");
                        }else {
                            getCode(phone);
                        }
                    }
                });


        RxViewAction.clickNoDouble(forgetPassword)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        startActivity(new Intent(getApplicationContext(),ForgetPasswordActivity.class));
                    }
                });
    }

    /**
     * 验证码登陆
     * @param code
     */
    private void logdinByCode(final String phoneNumber, String code) {
        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("phone",phoneNumber);
            jsonObject.put("code",code);
        } catch (JSONException e) {
        }
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL_TEST + "verificationCode");
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
                        intent.putExtra("PHONE",phoneEdit.getText().toString());
                        startActivity(intent);
                    }else if (status.equals("111111")){ //用户信息存在返回用户信息
                        JSONObject data = jsonObject1.getJSONObject("data");
                        saveUserToDb(data);//保存用户信息到数据库
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    }else {
                        Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
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
     * 将用户保存到数据库
     * @param data
     */
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

    /**
     * 获取短信验证码
     */
    private void getCode(String phoneNumber) {

        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("phone",phoneNumber);
        } catch (JSONException e) {
        }
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL_TEST + "sendMsg");
        params.addBodyParameter("reqJson",jsonObject.toString());
        params.addParameter("token","f1b47d968e3a4197afb8297476b02556");
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
                        Toast.makeText(LoginActivity.this, "发送成功", Toast.LENGTH_SHORT).show();
                        mTime.start();
                        //startCountDown();
                    }else {
                        Toast.makeText(LoginActivity.this, msg , Toast.LENGTH_SHORT).show();
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


    /**
     * 密码登陆方法
     */
    private void loginByPassword(String phoneNumber,String password) {
        Log.e(TAG, "login: " + phoneNumber);
        Log.e(TAG, "login: " + password);
        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("phone",phoneNumber);
            jsonObject.put("password",password);

        } catch (JSONException e) {
        }
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL_TEST + "pwdLogin");
        params.addBodyParameter("reqJson",jsonObject.toString());
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess: " + result );

                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String status = jsonObject.getString("status");
                    String msg = jsonObject.getString("msg");
                    Log.e(TAG, "onSuccess: " + status);
                    if(status.equals("111111")){
                        Log.e(TAG, "onSuccess: ---------------------------------------------");
                        JSONObject data = jsonObject.getJSONObject("data");
                        saveUserToDb(data);
                        Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                    }else {

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

    private void changeLoginView() {
        passwordLayout.setVisibility(isCode ? View.GONE:View.VISIBLE);
        codeLayout.setVisibility(isCode ? View.VISIBLE:View.GONE);
        loginType.setText(isCode ? "密码登陆>" : "验证码登陆>");
    }

    private void initTimeText(int minutes,int seconds){
        time = 60000;
        lastCurrentTime = System.currentTimeMillis();
        getCodeButton.setText(String.format("%1$d:%2$02d",minutes,seconds));
        getCodeButton.setClickable(false);
        getCodeButton.setBackgroundResource(R.drawable.btn_primary_enable);
    }

    public void startCountDown(){
        destoryTimer();
        initTimeText(1,0);
        createTimer();
    }

    private void createTimer() {
        if (timeTimer != null) {
            return;
        }
        timeTimer = new Timer();
        timeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                double currentTime = System.currentTimeMillis();
                double diff = currentTime - lastCurrentTime;
                time -= diff;
                lastCurrentTime = currentTime;
                 AndroidUtilities.runOnUIThread(new Runnable() {
                     @Override
                     public void run() {

                     }
                 },0L);
            }
        }, 0, 1000);
    }

    private void destoryTimer() {
        synchronized (timerSync){
            if(timeTimer!=null){
                timeTimer.cancel();
                timeTimer = null;
            }
        }
    }


    private void initDataFromDb() {
        DbConfig dbConfig = new DbConfig();
        DbManager.DaoConfig daoConfig = dbConfig.getDaoConfig();

        DbManager db = x.getDb(daoConfig);

        List<Newest> data = new ArrayList<>();
        Newest newest = new Newest();
        newest.setId(1);
        newest.setTitle("2222");
        data.add(newest);
        for (Newest newest1 : data){
            try {
                db.saveOrUpdate(data);
            } catch (DbException e) {

            }
        }


    }

    class TimeCount extends CountDownTimer{

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
            getCodeButton.setText("("+millisUntilFinished / 1000 + "后重发)");
        }

        @Override
        public void onFinish() {
            getCodeButton.setText("重新获取");
            getCodeButton.setClickable(true);
            getCodeButton.setBackgroundResource(R.drawable.login_button);
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this,MainActivity.class));
    }
}
