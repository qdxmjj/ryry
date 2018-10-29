package com.ruyiruyi.merchant.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ruyiruyi.merchant.R;
import com.ruyiruyi.merchant.cell.VerificationCodeView;
import com.ruyiruyi.merchant.db.DbConfig;
import com.ruyiruyi.merchant.ui.activity.base.MerchantBaseActivity;
import com.ruyiruyi.merchant.utils.UtilsURL;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;
import com.ruyiruyi.rylibrary.cell.ActionBar;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import rx.functions.Action1;

public class PutForwardBindZFBActivity extends MerchantBaseActivity {

    private String zhifubaoId;
    private boolean isBindNameCode = false;
    private String bindName = "";
    private String bindCode = "";
    private ActionBar mActionBar;
    private EditText et_bindname;
    private EditText et_bindcode;
    private EditText et_bindzhifubao;
    private TextView tv_hint;
    private TextView tv_bind;

    private String storeregisterphone;
    private TextView tv_count; //绑定dialog中倒计时
    private TimeCount mtimeC;
    private long currentTimeBind = 0;//用于绑定验证码60s内只能发1次
    private String TAG = PutForwardBindZFBActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_put_forward_bind_zfb);
        mActionBar = (ActionBar) findViewById(R.id.acbars);
        mActionBar.setTitle("绑定提现账号");
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

        storeregisterphone = new DbConfig(PutForwardBindZFBActivity.this).getPhone();

        //获取传递数据
        Intent intent = getIntent();
        zhifubaoId = intent.getStringExtra("zhifubaoId");
        isBindNameCode = intent.getBooleanExtra("isBindNameCode", false);
        bindName = intent.getStringExtra("bindName");
        bindCode = intent.getStringExtra("bindCode");

        initView();
        bindView();

    }

    private void bindView() {
        RxViewAction.clickNoDouble(tv_bind).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                //检验
                judgeBeforePost();
            }
        });
    }

    private void judgeBeforePost() {
        if (!isBindNameCode) {//没有实名验证时判断
            if (et_bindname.getText() == null || et_bindname.getText().length() == 0) {
                Toast.makeText(this, "请输入姓名", Toast.LENGTH_SHORT).show();
                return;
            }
            if (et_bindcode.getText() == null || et_bindcode.getText().length() == 0) {
                Toast.makeText(this, "请输入身份证号", Toast.LENGTH_SHORT).show();
                return;
            }
            if (et_bindcode.getText().length() != 18) {
                Toast.makeText(this, "请输入合理的身份证号", Toast.LENGTH_SHORT).show();
                return;
            }
            String codes = et_bindcode.getText().toString();
            /*//验证身份证号是否规范(简单) TODO
            if (UtilsRY.isIdNumber(codes)) {
                Toast.makeText(this, "请输入正确的身份证号", Toast.LENGTH_SHORT).show();
                return;
            }*/
            if (et_bindzhifubao.getText() == null || et_bindzhifubao.getText().length() == 0) {
                Toast.makeText(this, "请输入支付宝账号", Toast.LENGTH_SHORT).show();
                return;
            }
            //post验证身份证号与姓名是否匹配 TODO 暂取消
            if (true) {//验证成功
                //绑定确认(post提交绑定信息)
                bindName = et_bindname.getText().toString();
                bindCode = et_bindcode.getText().toString();
                zhifubaoId = et_bindzhifubao.getText().toString();
                showBindDialog("实名信息绑定后无法修改,确认绑定吗？");
            } else {
                Toast.makeText(this, "身份证号与姓名不匹配", Toast.LENGTH_SHORT).show();
            }
        } else {
            zhifubaoId = et_bindzhifubao.getText().toString();
            if (et_bindzhifubao.getText() == null || et_bindzhifubao.getText().length() == 0) {
                Toast.makeText(this, "请输入支付宝账号", Toast.LENGTH_SHORT).show();
                return;
            }
            showBindDialog("实名信息绑定后无法修改,确认绑定吗？");
        }
    }


    public void showBindDialog(String error) {
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
                //提交前校验验证码
                showSendBindCodeDialog();

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

    private void showSendBindCodeDialog() {

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
                    final RequestParams params = new RequestParams(UtilsURL.REQUEST_URL + "verificationCode");
                    params.addBodyParameter("reqJson", object.toString());
                    x.http().post(params, new Callback.CommonCallback<String>() {
                        @Override
                        public void onSuccess(String result) {
                            try {
                                JSONObject jsonObject = new JSONObject(result);
                                int status = jsonObject.getInt("status");
                                if (status == 1 || status == 111111) {
                                /*if (true) {*/
                                    dialog.dismiss();

                                    RequestParams requestParams = new RequestParams(UtilsURL.REQUEST_URL_FAHUO + "incomeInfo/bindingStoreAccountInfo");
                                    requestParams.addBodyParameter("realName", bindName);
                                    requestParams.addBodyParameter("iDNumber", bindCode);
                                    requestParams.addBodyParameter("aliAccount", zhifubaoId);
                                    requestParams.addBodyParameter("storeId", new DbConfig(PutForwardBindZFBActivity.this).getId() + "");
                                    Log.e(TAG, "onSuccess: requestParams.toString() = " + requestParams.toString());
                                    x.http().post(requestParams, new CommonCallback<String>() {
                                        @Override
                                        public void onSuccess(String result) {
                                            Log.e(TAG, "onSuccess: result = " + result);
                                            try {
                                                JSONObject obj = new JSONObject(result);
                                                boolean isSuccess = obj.getBoolean("isSuccess");
                                                if (isSuccess) {//操作成功
                                                    //绑定成功(退出并传递绑定数据)
                                                    Toast.makeText(PutForwardBindZFBActivity.this, "绑定成功", Toast.LENGTH_LONG).show();
                                                    Intent intent = new Intent();
                                                    intent.putExtra("isbindSuccess", true);
                                                    intent.putExtra("zhifubaoId", et_bindzhifubao.getText().toString());
                                                    intent.putExtra("bindName", bindName);
                                                    intent.putExtra("bindCode", bindCode);
                                                    PutForwardBindZFBActivity.this.setResult(RESULT_OK, intent);
                                                    PutForwardBindZFBActivity.this.finish();

                                                } else {//操作失败
                                                    Toast.makeText(PutForwardBindZFBActivity.this, "支付宝账号绑定失败", Toast.LENGTH_LONG).show();
                                                }

                                            } catch (JSONException e) {
                                                e.printStackTrace();
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

                                } else {//失败
                                    Toast.makeText(PutForwardBindZFBActivity.this, "验证码错误", Toast.LENGTH_SHORT).show();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(Throwable ex, boolean isOnCallback) {
                            Toast.makeText(PutForwardBindZFBActivity.this, "验证码校验失败，请检查网络", Toast.LENGTH_SHORT).show();
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

        if (System.currentTimeMillis() - currentTimeBind > 60000) {
            //首先发送验证码post
            sendCode();

            //开始倒计时
            mtimeC = new TimeCount(60000, 1000);
            mtimeC.start();
            currentTimeBind = System.currentTimeMillis();
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
                    Toast.makeText(PutForwardBindZFBActivity.this, msg, Toast.LENGTH_LONG).show();

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
                Toast.makeText(PutForwardBindZFBActivity.this, "发送短信失败,请检查网络", Toast.LENGTH_LONG).show();
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
        et_bindname = findViewById(R.id.et_bindname);
        et_bindcode = findViewById(R.id.et_bindcode);
        et_bindzhifubao = findViewById(R.id.et_bindzhifubao);
        tv_hint = findViewById(R.id.tv_hint);
        tv_bind = findViewById(R.id.tv_bind);

        if (isBindNameCode) {
            //已绑定身份证号和姓名
            et_bindname.setText(bindName);
            String head = bindCode.substring(0, 6);
            String shoe = bindCode.substring(bindCode.length() - 4);
            et_bindcode.setText(head + "********" + shoe);
            et_bindname.setFocusable(false);
            et_bindcode.setFocusable(false);

        } else {
            //没有绑定过身份证号和姓名
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
}
