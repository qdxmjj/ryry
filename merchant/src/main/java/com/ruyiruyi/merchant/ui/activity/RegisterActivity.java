package com.ruyiruyi.merchant.ui.activity;

import android.content.DialogInterface;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ruyiruyi.merchant.R;
import com.ruyiruyi.merchant.utils.UtilsRY;
import com.ruyiruyi.merchant.utils.UtilsURL;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;
import com.ruyiruyi.rylibrary.base.BaseActivity;
import com.ruyiruyi.rylibrary.cell.ActionBar;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import rx.functions.Action1;

public class RegisterActivity extends BaseActivity {
    private EditText et_userName;
    private EditText et_userPhone;
    private TextView tv_getCode;
    private EditText et_Code;
    private ImageView img_CodeRight;
    private EditText et_Passworda;
    private EditText et_Passwordb;
    private EditText et_Shopname;
    private TextView tv_ShopCategory;
    private EditText et_ShopPhone;
    private TextView tv_ShopTime;
    private TextView tv_ShopCity;
    private EditText et_ShopLocation;
    private TextView tv_ShopPoint;
    private ImageView img_yyzz;
    private ImageView img_yyzz_delete;
    private ImageView img_mdpic_a;
    private ImageView img_mdpic_a_delete;
    private ImageView img_mdpic_b;
    private ImageView img_mdpic_b_delete;
    private ImageView img_mdpic_c;
    private ImageView img_mdpic_c_delete;
    private ImageView img_shou_a;
    private ImageView img_shou_a_delete;
    private CheckBox ckbox_ltfw;
    private CheckBox ckbox_mrqx;
    private CheckBox ckbox_bywh;
    private CheckBox ckbox_az;
    private CheckBox ckbox_gz;
    private CheckBox ckbox_sl;
    private CheckBox ckbox_bsl;
    private TextView tv_save;

    private ActionBar mActionBar;
    private TimeCount mTimeCount;
    private String TAG = RegisterActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mActionBar = (ActionBar) findViewById(R.id.acbar_rigister);
        mActionBar.setTitle("门店注册");
        mActionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick(){
            @Override
            public void onItemClick(int var1) {
                switch (var1){
                    case -1:
                        onBackPressed();
                        break;
                }
            }
        });
        initView();
        RxViewAction.clickNoDouble(tv_getCode)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        String phone = et_userPhone.getText().toString();
                        if (phone.isEmpty()) {
                            showDialog("手机号不能为空");
                        }else if (!UtilsRY.isMobile(phone)){
                            showDialog("手机号格式错误");
                        }else {
                            getCode(phone);
                        }
                    }
                });
//        RxViewAction.clickNoDouble()
    }

    private void getCode(String phoneNumber) {

        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("phone",phoneNumber);
        } catch (JSONException e) {
        }
        RequestParams params = new RequestParams(UtilsURL.LOGIN_PASS_REQUEST_URL + "sendMsg");
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
                        Toast.makeText(RegisterActivity.this, "发送成功", Toast.LENGTH_SHORT).show();
                        mTimeCount.start();
                    }else {
                        Toast.makeText(RegisterActivity.this, "发送失败", Toast.LENGTH_SHORT).show();
                        tv_getCode.setText("重新发送");
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

    private void initView() {
        et_userName = (EditText) findViewById(R.id.et_person);
        et_userPhone = (EditText) findViewById(R.id.et_phone);
        tv_getCode = (TextView) findViewById(R.id.tv_getcode);
        et_Code = (EditText) findViewById(R.id.et_code);
        img_CodeRight = (ImageView) findViewById(R.id.img_code_right);
        et_Passworda = (EditText) findViewById(R.id.et_pass_a);
        et_Passwordb = (EditText) findViewById(R.id.et_pass_b);
        et_Shopname = (EditText) findViewById(R.id.et_shopname);
        tv_ShopCategory = (TextView) findViewById(R.id.tv_shopcategory);
        et_ShopPhone = (EditText) findViewById(R.id.et_shopphone);
        tv_ShopTime = (TextView) findViewById(R.id.tv_shoptime);
        tv_ShopCity = (TextView) findViewById(R.id.tv_shopcity);
        et_ShopLocation = (EditText) findViewById(R.id.et_shoplocation);
        tv_ShopPoint = (TextView) findViewById(R.id.tv_shoppoint);
        img_yyzz = (ImageView) findViewById(R.id.img_yyzz);
        img_yyzz_delete = (ImageView) findViewById(R.id.img_yyzz_delete);
        img_mdpic_a = (ImageView) findViewById(R.id.img_mdpic_a);
        img_mdpic_a_delete = (ImageView) findViewById(R.id.img_mdpic_a_delete);
        img_mdpic_b = (ImageView) findViewById(R.id.img_mdpic_b);
        img_mdpic_b_delete = (ImageView) findViewById(R.id.img_mdpic_b_delete);
        img_mdpic_c = (ImageView) findViewById(R.id.img_mdpic_c);
        img_mdpic_c_delete = (ImageView) findViewById(R.id.img_mdpic_c_delete);
        img_shou_a = (ImageView) findViewById(R.id.img_shou_a);
        img_shou_a_delete = (ImageView) findViewById(R.id.img_shou_a_delete);
        ckbox_ltfw = (CheckBox) findViewById(R.id.checkbox_ltfw);
        ckbox_mrqx = (CheckBox) findViewById(R.id.checkbox_mrqx);
        ckbox_bywh = (CheckBox) findViewById(R.id.checkbox_bywh);
        ckbox_az = (CheckBox) findViewById(R.id.checkbox_az);
        ckbox_gz = (CheckBox) findViewById(R.id.checkbox_gz);
        ckbox_sl = (CheckBox) findViewById(R.id.checkbox_sl);
        ckbox_bsl = (CheckBox) findViewById(R.id.checkbox_bsl);
        tv_save = (TextView) findViewById(R.id.tv_save);
        mTimeCount = new TimeCount(60000,1000);
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
            tv_getCode.setBackgroundResource(R.drawable.btn_primary_enable);
            tv_getCode.setClickable(false);
            tv_getCode.setText("("+millisUntilFinished / 1000 + "后重发)");
        }

        @Override
        public void onFinish() {
            tv_getCode.setText("重新获取");
            tv_getCode.setClickable(true);
            tv_getCode.setBackgroundResource(R.drawable.login_code_button);
        }
    }
}
