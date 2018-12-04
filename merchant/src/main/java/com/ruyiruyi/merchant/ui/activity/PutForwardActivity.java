package com.ruyiruyi.merchant.ui.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ruyiruyi.merchant.MyApplication;
import com.ruyiruyi.merchant.R;
import com.ruyiruyi.merchant.cell.VerificationCodeView;
import com.ruyiruyi.merchant.db.DbConfig;
import com.ruyiruyi.merchant.eventbus.WxLoginEvent;
import com.ruyiruyi.merchant.ui.activity.base.MerchantBaseActivity;
import com.ruyiruyi.merchant.utils.UtilsRY;
import com.ruyiruyi.merchant.utils.UtilsURL;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;
import com.ruyiruyi.rylibrary.cell.ActionBar;
import com.tencent.mm.opensdk.modelmsg.SendAuth;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.Calendar;

import rx.functions.Action1;


public class PutForwardActivity extends MerchantBaseActivity {

    private ActionBar mActionBar;
    private TextView tv_balance;
    private EditText et_putforward;
    private TextView tv_allputforward;
    private CheckBox ck_zhifubao;
    private CheckBox ck_weixin;
    private FrameLayout fl_zhifubao;
    private FrameLayout fl_weixin;
    private TextView tv_bind;
    private TextView tv_login;
    private TextView tv_submit;
    private TextView tv_zhifubao_phone;
    private TextView tv_weixin_phone;

    private TextView tv_count; //解绑和提现dialog中倒计时(共用)
    private TimeCount mtimeC;

    private double balance = 0;//可用余额
    private double currentPutforward = 0;//提现中金额
    private int putforwardType = 0;  // 0 未选择 1 支付宝  2 微信
    private boolean isBindZFB = false; // 是否已绑定支付宝
    private boolean isLoginWX = false; // 是否已登录微信
    private String zhifubaoId = ""; // 支付宝绑定的账号
    private String weixinRealName = ""; // 微信提现输入真实核对姓名
    private String openId = ""; // 微信openId
    private boolean isBindNameCode = false; // 是否已绑定姓名身份证号
    private String bindName = ""; // 绑定的姓名
    private String bindCode = ""; // 绑定的身份证号
    private long bindingTime = 0; // 上次绑定支付宝的时间
    private boolean canUnbindZFB = false;//本月是否可解绑标志位
    private Calendar calendar = Calendar.getInstance();
    private Calendar calendarNow = Calendar.getInstance();
    private String storeregisterphone;
    private long currentTimeUnbind = 0;//用于解绑验证码60s内只能发1次
    private long currentTimeSubmit = 0;//用于提交验证码60s内只能发1次

    public static final int BIND_REQUEST_CODE = 121;
    public static final int LOGIN_REQUEST_CODE = 122;
    private boolean isbindSuccess;//startActivityForResult返回的是否绑定成功
    private String TAG = PutForwardActivity.class.getSimpleName();

    private ProgressDialog startdialog;
    private String mNickname;
    private String headimgurl;
    private boolean mLoginSuccess = false;

    private ProgressDialog putDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_put_forward);
        mActionBar = (ActionBar) findViewById(R.id.acbars);
        mActionBar.setTitle("收益提现");
        mActionBar.setRightView("记录");
        mActionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(int var1) {
                switch (var1) {
                    case -1:
                        onBackPressed();
                        break;
                    case -3:
                        onRightClick();
                        break;
                }
            }
        });

        EventBus.getDefault().register(this);
        storeregisterphone = new DbConfig(PutForwardActivity.this).getPhone();

        initView();
        bindView();
        initData();


    }

    /**
     * 跳转提现记录页面
     */
    private void onRightClick() {
        Intent intent = new Intent(this, PutForwardInfoActivity.class);
        startActivity(intent);
    }

    private void bindView() {
        //提现
        RxViewAction.clickNoDouble(tv_submit).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                //判断条件
                judgeBeforePost();
            }
        });
        //登录注销微信
        RxViewAction.clickNoDouble(tv_login).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (isLoginWX) {//注销
                    showExitDialog();
                } else {//微信登录
                    SendAuth.Req req = new SendAuth.Req();
                    req.scope = "snsapi_userinfo";
                    req.state = "wechat_sdk_微信登录";
                    //像微信发送请求
                    MyApplication.mWxApi.sendReq(req);
                }
            }
        });
        //绑定(解绑)支付宝
        RxViewAction.clickNoDouble(tv_bind).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (isBindZFB) {
                    if (canUnbindZFB) {//本月未绑定过支付宝账号
                    /*if (true) {//本月未绑定过支付宝账号 测试 // TODO*/
                        //验证码解绑
                        showUnbindDialog("每个月只能解绑一次支付宝账号，确认要解绑吗？");
                    } else {//本月绑定过支付宝账号现不可解绑
                        Toast.makeText(PutForwardActivity.this, "每个月只能绑定一次支付宝账号", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    //前去绑定支付宝
                    Intent intent = new Intent(PutForwardActivity.this, PutForwardBindZFBActivity.class);
                    intent.putExtra("zhifubaoId", zhifubaoId);
                    intent.putExtra("isBindNameCode", isBindNameCode);
                    intent.putExtra("bindName", bindName);
                    intent.putExtra("bindCode", bindCode);
                    PutForwardActivity.this.startActivityForResult(intent, BIND_REQUEST_CODE);

                }
            }
        });
        //CheckBox
        RxViewAction.clickNoDouble(fl_zhifubao).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {

                if (!ck_zhifubao.isChecked()) {
                    initCheckbox();
                    ck_zhifubao.setChecked(true);
                    putforwardType = 1;
                } else {
                    initCheckbox();
                }
            }
        });
        RxViewAction.clickNoDouble(fl_weixin).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {

                if (!ck_weixin.isChecked()) {
                    initCheckbox();
                    ck_weixin.setChecked(true);
                    putforwardType = 2;
                } else {
                    initCheckbox();
                }
            }
        });
        ck_zhifubao.setClickable(false);
        ck_weixin.setClickable(false);
        //全部提现
        RxViewAction.clickNoDouble(tv_allputforward).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                et_putforward.setText(balance + "");
            }
        });
    }

    private void showExitDialog() {
        final AlertDialog dialog = new AlertDialog.Builder(this).create();
        dialog.setTitle("如驿如意商家版");
        dialog.setIcon(R.drawable.ic_launcher);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_error, null);
        TextView error_text = (TextView) dialogView.findViewById(R.id.error_text);
        error_text.setText("确认退出该微信账号吗？");
        dialog.setView(dialogView);
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialog.dismiss();
            }
        });
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                tv_login.setText("未登录 >");
                isLoginWX = false;
                tv_weixin_phone.setVisibility(View.GONE);
                tv_weixin_phone.setText("");
                mNickname = "";
                openId = "";
                initCheckbox();
                Toast.makeText(PutForwardActivity.this, "账号退出成功", Toast.LENGTH_SHORT).show();
            }
        });
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.theme_primary));
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.theme_primary));
    }


    private void judgeBeforePost() {
        if (putforwardType == 2) { // TODO
            Toast.makeText(this, "微信提现即将开放,敬请期待!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (et_putforward.getText() == null || et_putforward.getText().length() == 0) {
            showMerchantErrorDialog("提现金额不能为空!");
            return;
        }
        String putforwardStr = et_putforward.getText().toString();
        if (!UtilsRY.isFloat(putforwardStr) && !UtilsRY.isInt(putforwardStr)) {//正则判断输入是否规范(正浮点数和正整数)
            showMerchantErrorDialog("请输入合理金额");
            return;
        }
        int indexOf = putforwardStr.indexOf(".");
        Log.e("indexOf", "judgeBeforePost: " + indexOf + "+" + putforwardStr.length());
        if (UtilsRY.isFloat(putforwardStr) && (putforwardStr.length() - indexOf - 1) > 2) {//判断小数点后两位
            showMerchantErrorDialog("请输入合理金额");
            return;
        }
        double parseDouble = Double.parseDouble(et_putforward.getText().toString());
        if (parseDouble < 10.0) {  //TODO
            showMerchantErrorDialog("单笔最少提现金额不能少于10元");
            return;
        }
        if (parseDouble > balance) {
            showMerchantErrorDialog("可用余额不足");
            return;
        }
        if (currentPutforward > 0) {//判断是否存在提现中的订单 TODO
            showMerchantErrorDialog("您有提现中的订单，待提现完成后方可再次申请，请耐心等待");
            return;
        }
        if (putforwardType == 0) {
            showMerchantErrorDialog("请选择一种提现方式");
            return;
        }
        if (putforwardType == 1 && !isBindZFB) {
            showMerchantErrorDialog("您还没有绑定支付宝");
            return;
        }
        if (putforwardType == 2 && !isLoginWX) {
            showMerchantErrorDialog("请先登录微信");
            return;
        }
        //校验验证码(内post提现)
        showSendSubmitCodeDialog();
    }

    private void bindData() {
        tv_balance.setText(balance + "");
        if (isBindZFB) {
            tv_zhifubao_phone.setVisibility(View.VISIBLE);
            String head = zhifubaoId.substring(0, 3);
            String shoe = zhifubaoId.substring(zhifubaoId.length() - 4);
            tv_zhifubao_phone.setText(head + "****" + shoe);
            tv_bind.setText("解绑 >");
        } else {
            tv_zhifubao_phone.setVisibility(View.GONE);
            tv_bind.setText("未绑定 >");
        }
        hideDialogProgress(startdialog);
    }

    private void initData() {
        //post
        RequestParams params = new RequestParams(UtilsURL.REQUEST_URL_FAHUO + "incomeInfo/queryStoreAccountInfo");
        params.addBodyParameter("storeId", new DbConfig(PutForwardActivity.this).getId() + "");
        Log.e(TAG, "initData: params.toString() = " + params.toString());
        showDialogProgress(startdialog, "收益信息加载中...");
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess: result = " + result);
                try {
                    JSONObject object = new JSONObject(result);
                    JSONObject data = object.getJSONObject("data");
                    balance = data.getDouble("availableMoney");//余额
                    currentPutforward = data.getDouble("withdrawing");//当前提现中的金额
                    isBindZFB = data.getInt("bindingStatus") == 0 ? false : true; //是否绑定支付宝 0 没有  1 已绑定
                    zhifubaoId = data.getString("aliAccount");//支付宝账号
                    isBindNameCode = data.getInt("status") == 0 ? false : true; //是否实名认证过 0 没有  1 认证过
                    bindName = data.getString("realName");
                    bindCode = data.getString("iDNumber");
                    String timeString = data.getString("bindingTime");

                    if (TextUtils.isEmpty(timeString) || timeString.equals("null")) {//初次绑定是返回为null
                        canUnbindZFB = true;
                    } else {
                        bindingTime = Long.parseLong(timeString);
                        calendar.setTimeInMillis(bindingTime);
                        //判断当月是否可以解绑（每个月只能解绑一次支付宝账号）
                        int bindyear = calendar.get(Calendar.YEAR);
                        int nowyear = calendarNow.get(Calendar.YEAR);
                        int bindMonth = calendar.get(Calendar.MONTH);
                        int nowMonth = calendarNow.get(Calendar.MONTH);
                        if (nowyear == bindyear && nowMonth == bindMonth) {//本月绑定过
                            canUnbindZFB = false;
                        } else {
                            canUnbindZFB = true;
                        }
                    }

                    //绑定数据
                    bindData();


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e(TAG, "onError: ex.toString() = " + ex.toString());
                Toast.makeText(PutForwardActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
                //显示假数据
                initFuckData();
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
     * 初始化所有CheckBox
     */
    private void initCheckbox() {
        ck_zhifubao.setChecked(false);
        ck_weixin.setChecked(false);
        putforwardType = 0;
    }

    private void initFuckData() {
        /*balance = 888.8;//已绑定支付宝账号并实名认证
        isBindZFB = true;
        zhifubaoId = "18254262178";
        isBindNameCode = true;
        bindName = "董斌";
        bindCode = "37068719940812571X";*/

       /* balance = 888.8;//未绑定支付宝账号但已经实名认证(解绑过)
        isBindZFB = false;
        zhifubaoId = "";
        isBindNameCode = true;
        bindName = "董斌";
        bindCode = "37068719940812571X";*/

        balance = 0;//未绑定支付宝账号并未实名认证(第一次提现)
        isBindZFB = false;
        zhifubaoId = "";
        isBindNameCode = false;
        bindName = "";
        bindCode = "";

        //绑定数据
        bindData();

    }

    public void showUnbindDialog(String error) {
        AlertDialog dialog = new AlertDialog.Builder(this).create();
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_error, null);
        TextView error_text = (TextView) dialogView.findViewById(R.id.error_text);
        error_text.setText(error);
        dialog.setTitle("如意如驿商家版");
        dialog.setIcon(R.drawable.ic_launcher);
        dialog.setView(dialogView);
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //发送解绑验证码
                showSendUnbindCodeDialog();
            }
        });
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "再看看", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dialog.show();
        //设置按钮颜色
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.theme_primary));
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.theme_primary));
    }

    private void showSendUnbindCodeDialog() {

        final AlertDialog dialog = new AlertDialog.Builder(this).create();
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_sendcode, null);
        TextView tv_codephone = dialogView.findViewById(R.id.tv_phone);
        final VerificationCodeView codeView = dialogView.findViewById(R.id.codeview);
        tv_count = dialogView.findViewById(R.id.tv_count);
        String head = storeregisterphone.substring(0, 3);
        String shoe = storeregisterphone.substring(storeregisterphone.length() - 4);
        tv_codephone.setText(head + "****" + shoe);
        codeView.setInputCompleteListener(new VerificationCodeView.InputCompleteListener() {
            @Override
            public void inputComplete() {
                if (codeView.getInputContent().length() == 6) {
                    //post校验验证码
                    JSONObject object = new JSONObject();
                    try {
                        object.put("phone", storeregisterphone);
                        object.put("code", codeView.getInputContent());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    RequestParams params = new RequestParams(UtilsURL.REQUEST_URL + "verificationCode");
                    params.addBodyParameter("reqJson", object.toString());
                    x.http().post(params, new Callback.CommonCallback<String>() {
                        @Override
                        public void onSuccess(String result) {
                            JSONObject jsonObject = null;
                            try {
                                jsonObject = new JSONObject(result);
                                int status = jsonObject.getInt("status");
                                if (status == 1 || status == 111111) {
                                /*if (true) {*/
                                    dialog.dismiss();

                                    RequestParams requestParams = new RequestParams(UtilsURL.REQUEST_URL_FAHUO + "incomeInfo/clearStoreAccountInfo");
                                    requestParams.addBodyParameter("storeId", new DbConfig(PutForwardActivity.this).getId() + "");
                                    Log.e(TAG, "onSuccess: requestParams.toString() = " + requestParams.toString());
                                    x.http().post(requestParams, new CommonCallback<String>() {
                                        @Override
                                        public void onSuccess(String result) {
                                            Log.e(TAG, "onSuccess:  result = " + result);
                                            try {
                                                JSONObject obj = new JSONObject(result);
                                                boolean isSuccess = obj.getBoolean("isSuccess");
                                                if (isSuccess) {//操作成功
                                                    Toast.makeText(PutForwardActivity.this, "解绑成功", Toast.LENGTH_SHORT).show();

                                                    //刷新提现页面数据
                                                    tv_zhifubao_phone.setVisibility(View.GONE);
                                                    tv_bind.setText("未绑定 >");
                                                    zhifubaoId = "";
                                                    isBindZFB = false;
                                                    initCheckbox();
                                                } else {//操作失败
                                                    Toast.makeText(PutForwardActivity.this, "支付宝账号解绑失败", Toast.LENGTH_SHORT).show();
                                                }

                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }

                                        @Override
                                        public void onError(Throwable ex, boolean isOnCallback) {
                                            Toast.makeText(PutForwardActivity.this, "解绑失败,请检查网络", Toast.LENGTH_SHORT).show();
                                        }

                                        @Override
                                        public void onCancelled(CancelledException cex) {

                                        }

                                        @Override
                                        public void onFinished() {

                                        }
                                    });

                                } else {//失败
                                    Toast.makeText(PutForwardActivity.this, "验证码错误", Toast.LENGTH_SHORT).show();
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(Throwable ex, boolean isOnCallback) {
                            Toast.makeText(PutForwardActivity.this, "验证码校验失败，请检查网络", Toast.LENGTH_SHORT).show();
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

            @Override
            public void deleteContent() {

            }
        });
        RxViewAction.clickNoDouble(tv_count).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                //再次发送验证码post
                sendCode();

                //再次开始倒计时
                mtimeC = new TimeCount(60000, 1000);
                mtimeC.start();
            }
        });

        if (System.currentTimeMillis() - currentTimeUnbind > 60000) {
            //首先发送验证码post
            sendCode();

            //开始倒计时
            mtimeC = new TimeCount(60000, 1000);
            mtimeC.start();
            currentTimeUnbind = System.currentTimeMillis();
        } else {
            tv_count.setText("您操作太快，请稍候重试");
            tv_count.setTextColor(getResources().getColor(R.color.theme_primary));
            tv_count.setClickable(false);
        }

        dialog.setView(dialogView);
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                mtimeC.cancel();
            }
        });
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

    }

    private void showSendSubmitCodeDialog() {

        final AlertDialog dialog = new AlertDialog.Builder(this).create();
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_sendcode, null);
        TextView tv_codephone = dialogView.findViewById(R.id.tv_phone);
        final VerificationCodeView codeView = dialogView.findViewById(R.id.codeview);
        tv_count = dialogView.findViewById(R.id.tv_count);
        String head = storeregisterphone.substring(0, 3);
        String shoe = storeregisterphone.substring(storeregisterphone.length() - 4);
        tv_codephone.setText(head + "****" + shoe);
        codeView.setInputCompleteListener(new VerificationCodeView.InputCompleteListener() {
            @Override
            public void inputComplete() {
                if (codeView.getInputContent().length() == 6) {//输入6位后
                    //post校验验证码
                    JSONObject object = new JSONObject();
                    try {
                        object.put("phone", storeregisterphone);
                        object.put("code", codeView.getInputContent());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    RequestParams params = new RequestParams(UtilsURL.REQUEST_URL + "verificationCode");
                    params.addBodyParameter("reqJson", object.toString());
                    x.http().post(params, new Callback.CommonCallback<String>() {
                        @Override
                        public void onSuccess(String result) {
                            JSONObject jsonObject = null;
                            try {
                                jsonObject = new JSONObject(result);
                                int status = jsonObject.getInt("status");
                                if (status == 1 || status == 111111) {
                              /*  if (true) {*/
                                    dialog.dismiss();

                                    showDialogProgress(putDialog, "提现申请提交中...");
                                    RequestParams requestParams = new RequestParams(UtilsURL.REQUEST_URL_FAHUO + "withdrawInfo/applyWithdrawOrder");
                                    requestParams.addBodyParameter("type", putforwardType + "");
                                    requestParams.addBodyParameter("storeId", new DbConfig(PutForwardActivity.this).getId() + "");
                                    requestParams.addBodyParameter("storeName", new DbConfig(PutForwardActivity.this).getUser().getStoreName() + "");
                                    requestParams.addBodyParameter("availableMoney", balance + "");//可用余额
                                    requestParams.addBodyParameter("withdrawMoney", et_putforward.getText().toString());//提现金额
                                    if (putforwardType == 2) {
                                        requestParams.addBodyParameter("wxOpenId", openId);//微信openId
                                        requestParams.addBodyParameter("realName", weixinRealName);//微信输入的真实姓名
                                    }
                                    Log.e(TAG, "onSuccess: requestParams.toString() = " + requestParams.toString());
                                    x.http().post(requestParams, new CommonCallback<String>() {
                                        @Override
                                        public void onSuccess(String result) {
                                            Log.e(TAG, "onSuccess: result = " + result);
                                            try {
                                                JSONObject obj = new JSONObject(result);
                                                boolean isSuccess = obj.getBoolean("isSuccess");
                                                if (isSuccess) {
                                                    Toast.makeText(PutForwardActivity.this, "提现申请已提交,请留意提现进度", Toast.LENGTH_LONG).show();
                                                    //结束提现页面
                                                    PutForwardActivity.this.finish();
                                                } else {
                                                    Toast.makeText(PutForwardActivity.this, "提现申请提交失败,请重试", Toast.LENGTH_SHORT).show();
                                                }

                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }

                                        @Override
                                        public void onError(Throwable ex, boolean isOnCallback) {
                                            Toast.makeText(PutForwardActivity.this, "提现申请提交失败,请检查网络", Toast.LENGTH_SHORT).show();
                                        }

                                        @Override
                                        public void onCancelled(CancelledException cex) {

                                        }

                                        @Override
                                        public void onFinished() {
                                            hideDialogProgress(putDialog);
                                        }
                                    });

                                } else {//失败
                                    Toast.makeText(PutForwardActivity.this, "验证码错误", Toast.LENGTH_SHORT).show();
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(Throwable ex, boolean isOnCallback) {
                            Toast.makeText(PutForwardActivity.this, "验证码校验失败，请检查网络", Toast.LENGTH_SHORT).show();
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

            @Override
            public void deleteContent() {

            }
        });
        RxViewAction.clickNoDouble(tv_count).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                //再次发送验证码post
                sendCode();

                //再次开始倒计时
                mtimeC = new TimeCount(60000, 1000);
                mtimeC.start();
            }
        });

        if (System.currentTimeMillis() - currentTimeSubmit > 60000) {
            //首先发送验证码post
            sendCode();

            //开始倒计时
            mtimeC = new TimeCount(60000, 1000);
            mtimeC.start();
            currentTimeSubmit = System.currentTimeMillis();
        } else {
            tv_count.setText("您操作太快，请稍候重试");
            tv_count.setTextColor(getResources().getColor(R.color.theme_primary));
            tv_count.setClickable(false);
        }

        dialog.setView(dialogView);
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                mtimeC.cancel();
            }
        });
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == BIND_REQUEST_CODE) {//前去绑定支付宝后返回的数据
                Bundle extras = data.getExtras();
                isbindSuccess = extras.getBoolean("isbindSuccess", false);
                if (!isbindSuccess) {
                    return;
                }
                zhifubaoId = extras.getString("zhifubaoId");
                bindName = extras.getString("bindName");
                bindCode = extras.getString("bindCode");
                isBindNameCode = true;
                Log.e("logee", "onActivityResult: isBindNameCode = " + isBindNameCode);

                //然后更新页面支付宝绑定数据
                tv_zhifubao_phone.setVisibility(View.VISIBLE);
                String head = zhifubaoId.substring(0, 3);
                String shoe = zhifubaoId.substring(zhifubaoId.length() - 4);
                tv_zhifubao_phone.setText(head + "****" + shoe);
                tv_bind.setText("解绑 >");
                isBindZFB = true;
                canUnbindZFB = false;//更新绑定时间 不可再次绑定
                initCheckbox();
                ck_zhifubao.setChecked(true);
                putforwardType = 1;
            }
            if (requestCode == LOGIN_REQUEST_CODE) {
                Bundle extras = data.getExtras();
                boolean success = extras.getBoolean("isloginSuccess", false);
                if (!success) {
                    return;
                }
                weixinRealName = extras.getString("weixinRealName");
                Toast.makeText(PutForwardActivity.this, "登录成功", Toast.LENGTH_SHORT).show();

                tv_login.setText("注销 >");
                isLoginWX = true;
                tv_weixin_phone.setVisibility(View.VISIBLE);
                tv_weixin_phone.setText(mNickname);
                initCheckbox();
                ck_weixin.setChecked(true);
                putforwardType = 2;
            }
        } else {//异常返回
            if (!isLoginWX) {
                //清除微信数据(只有未登录微信时清除)
                mNickname = "";
                openId = "";
                headimgurl = "";
            }
        }
    }

    /**
     * 发送验证码
     */
    private void sendCode() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("phone", storeregisterphone);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestParams params = new RequestParams(UtilsURL.REQUEST_URL + "sendMsgWithoutLimit");
        params.addBodyParameter("reqJson", jsonObject.toString());
        Log.e(TAG, "sendCode:  params.toString() = " + params.toString());
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "sendCode onSuccess: result = " + result);
                JSONObject object = null;
                try {
                    object = new JSONObject(result);
                    String msg = object.getString("msg");
                    Toast.makeText(PutForwardActivity.this, msg, Toast.LENGTH_SHORT).show();

                    int status = object.getInt("status");
                    if (status != 1) {//发送失败 重新获取
                        mtimeC.cancel();
                        tv_count.setTextColor(getResources().getColor(R.color.theme_primary));
                        tv_count.setClickable(true);
                        tv_count.setText("重新获取");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(PutForwardActivity.this, "发送短信失败,请检查网络", Toast.LENGTH_SHORT).show();
                mtimeC.cancel();
                tv_count.setTextColor(getResources().getColor(R.color.theme_primary));
                tv_count.setClickable(true);
                tv_count.setText("重新获取");
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private void initView() {
        tv_balance = findViewById(R.id.tv_balance);
        et_putforward = findViewById(R.id.et_putforward);
        tv_allputforward = findViewById(R.id.tv_allputforward);
        ck_zhifubao = findViewById(R.id.ck_zhifubao);
        ck_weixin = findViewById(R.id.ck_weixin);
        fl_zhifubao = findViewById(R.id.fl_zhifubao);
        fl_weixin = findViewById(R.id.fl_weixin);
        tv_bind = findViewById(R.id.tv_bind);
        tv_login = findViewById(R.id.tv_login);
        tv_submit = findViewById(R.id.tv_submit);
        tv_zhifubao_phone = findViewById(R.id.tv_zhifubao_phone);
        tv_weixin_phone = findViewById(R.id.tv_weixin_phone);

        startdialog = new ProgressDialog(PutForwardActivity.this);
        putDialog = new ProgressDialog(PutForwardActivity.this);
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
            tv_count.setTextColor(getResources().getColor(R.color.c6));
            tv_count.setClickable(false);
            tv_count.setText(millisUntilFinished / 1000 + "秒后重新获取");
        }

        @Override
        public void onFinish() {
            tv_count.setText("重新获取");
            tv_count.setTextColor(getResources().getColor(R.color.theme_primary));
            tv_count.setClickable(true);
        }
    }


    /**
     * 微信登录返回EventBus
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void saveEvent(WxLoginEvent event) {
        mNickname = event.getNickname();
        openId = event.getOpenid();
        headimgurl = event.getHeadimgurl();
        mLoginSuccess = event.isLoginSuccess();
        if (mLoginSuccess) {
            Intent intent = new Intent(PutForwardActivity.this, PutForwardLoginWXActivity.class);
            intent.putExtra("mNickname", mNickname);
            intent.putExtra("headimgurl", headimgurl);
            PutForwardActivity.this.startActivityForResult(intent, LOGIN_REQUEST_CODE);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
