package com.ruyiruyi.merchant.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ruyiruyi.merchant.R;
import com.ruyiruyi.merchant.db.DbConfig;
import com.ruyiruyi.merchant.utils.UtilsURL;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;
import com.ruyiruyi.rylibrary.base.BaseActivity;
import com.ruyiruyi.rylibrary.cell.ActionBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import rx.functions.Action1;

public class OrderXiangqingActivity extends BaseActivity {

    private ActionBar mActionBar;
    private String orderNo;
    private String orderType;
    private String storeId;
    private String statusStr;

    private String userName;
    private String userPhone;
    private String platNumber;
    private String consistentAmount;
    private String fontAmount;
    private String rearAmount;
    private String consistentShoeName;
    private String fontshoeName;
    private String rearshoeName;
    private String consistentShoeImg;
    private String fontshoeImg;
    private String rearshoeImg;
    private Boolean isFontRearFlagConsistent = false;

    private TextView user_name;
    private TextView user_phone;
    private TextView car_num;
    private TextView order_num;
    private TextView order_state;

    private LinearLayout ll_order_shoe_consistent;
    private ImageView img_shoe_consistent;
    private TextView name_shoe_consistent;
    private TextView num_shoe_consistent;

    private LinearLayout ll_order_shoe_font;
    private ImageView img_shoe_font;
    private TextView name_shoe_font;
    private TextView num_shoe_font;
    private LinearLayout ll_order_shoe_rear;
    private ImageView img_shoe_rear;
    private TextView name_shoe_rear;
    private TextView num_shoe_rear;
    private ImageView user_phone_img;
    private String TAG = OrderXiangqingActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_xiangqing);

        mActionBar = (ActionBar) findViewById(R.id.order_xiangqing_acbar);
        mActionBar.setTitle("订单详情");
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

        Bundle bundle = getIntent().getExtras();
        orderNo = bundle.getString("orderNo");
        orderType = bundle.getString("orderType");
        storeId = bundle.getString("storeId");
        statusStr = bundle.getString("statusStr");

        initView();
        initData();
        bindView();
    }

    private void bindView() {
        RxViewAction.clickNoDouble(user_phone_img).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
//                //先申请电话权限 (暂不用申请 可直接到电话页面)
//                requestPower();
                //再判断用户是否给与电话权限
                if (judgeIsPower()) {
                    //用户已给权限拨打电话
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    Uri data = Uri.parse("tel:" + userPhone);
                    intent.setData(data);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "请给与电话权限", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //权限获取
    public void requestPower() {
        //判断是否已经赋予权限
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
            //如果应用之前请求过此权限但用户拒绝了请求，此方法将返回 true。
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CALL_PHONE)) {//这里可以写个对话框之类的项向用户解释为什么要申请权限，并在对话框的确认键后续再次申请权限
            } else {
                //申请权限，字符串数组内是一个或多个要申请的权限，1是申请权限结果的返回参数，在onRequestPermissionsResult可以得知申请结果
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CALL_PHONE
                        }, 1);
            }
        }
    }

    private boolean judgeIsPower() {
        Boolean isPOW;
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "请授权读写手机存储权限", Toast.LENGTH_SHORT).show();
            isPOW = false;
        } else {
            isPOW = true;
        }

        return isPOW;
    }

    private void initData() {
        JSONObject object = new JSONObject();
        try {
            object.put("orderNo", orderNo);
            object.put("orderType", orderType);
            object.put("storeId", storeId);
        } catch (JSONException e) {
        }
        RequestParams params = new RequestParams(UtilsURL.REQUEST_URL + "getStoreOrderInfoByNoAndType");
        params.addBodyParameter("reqJson", object.toString());
        params.addBodyParameter("token", new DbConfig().getToken());
        Log.e(TAG, "initDataSSS: params.toString() " + params.toString());
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONObject data = jsonObject.getJSONObject("data");
                    Log.e(TAG, "onSuccessSSS: result = " + result);
                    userName = data.getString("userName");
                    userPhone = data.getString("userPhone");
                    platNumber = data.getString("platNumber");
                    JSONArray firstChangeOrderVoList = data.getJSONArray("firstChangeOrderVoList");
                    for (int i = 0; i < firstChangeOrderVoList.length(); i++) {
                        JSONObject abc = (JSONObject) firstChangeOrderVoList.get(i);
                        String fontRearFlag = abc.getString("fontRearFlag");
                        if (fontRearFlag.equals("1")) {//前胎
                            fontAmount = abc.getInt("fontAmount") + "";
                            fontshoeImg = abc.getString("shoeImg");
                            fontshoeName = abc.getString("shoeName");
                        } else if (fontRearFlag.equals("2")) {//后胎
                            rearAmount = abc.getInt("fontAmount") + "";
                            rearshoeImg = abc.getString("shoeImg");
                            rearshoeName = abc.getString("shoeName");
                        } else if (fontRearFlag.equals("0")) {//一致
                            consistentAmount = abc.getInt("fontAmount") + "";
                            consistentShoeImg = abc.getString("shoeImg");
                            consistentShoeName = abc.getString("shoeName");
                            isFontRearFlagConsistent = true;//前后一致
                        }
                    }
                    Log.e(TAG, "onSuccessSSS: userName = " + userName + "userPhone = " + userPhone + "platNumber = " + platNumber);
                    Log.e(TAG, "onSuccessSSS: 前 fontAmount = " + fontAmount + "fontshoeImg = " + fontshoeImg + "fontshoeName = " + fontshoeName);
                    Log.e(TAG, "onSuccessSSS: 后 rearAmount = " + rearAmount + "rearshoeImg = " + rearshoeImg + "rearshoeName = " + rearshoeName);
                    Log.e(TAG, "onSuccessSSS: isFontRearFlagConsistent = " + isFontRearFlagConsistent);
                    Log.e(TAG, "onSuccessSSS: 一致 consistentAmount = " + consistentAmount + "consistentShoeImg = " + consistentShoeImg + "consistentShoeName = " + consistentShoeName);

                    //设置数据
                    setData();

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

    private void setData() {
        //显示隐藏布局
        if (isFontRearFlagConsistent) {
            ll_order_shoe_consistent.setVisibility(View.VISIBLE);
            name_shoe_consistent.setText(consistentShoeName);
            num_shoe_consistent.setText(consistentAmount);
            Glide.with(getApplicationContext()).load(consistentShoeImg).into(img_shoe_consistent);
        } else {
            if (fontAmount != null) {
                ll_order_shoe_consistent.setVisibility(View.GONE);
                ll_order_shoe_font.setVisibility(View.VISIBLE);
                name_shoe_font.setText(fontshoeName);
                num_shoe_font.setText(fontAmount);
                Glide.with(getApplicationContext()).load(fontshoeImg).into(img_shoe_font);
            }
            if (rearAmount != null) {
                ll_order_shoe_consistent.setVisibility(View.GONE);
                ll_order_shoe_rear.setVisibility(View.VISIBLE);
                name_shoe_rear.setText(rearshoeName);
                num_shoe_rear.setText(rearAmount);
                Glide.with(getApplicationContext()).load(rearshoeImg).into(img_shoe_rear);
            }
        }
        user_name.setText(userName);
        user_phone.setText(userPhone);
        car_num.setText(platNumber);
        order_num.setText(orderNo);
        order_state.setText(statusStr);
    }

    private void initView() {
        user_name = findViewById(R.id.user_name);
        user_phone = findViewById(R.id.user_phone);
        car_num = findViewById(R.id.car_num);
        order_num = findViewById(R.id.order_num);
        order_state = findViewById(R.id.order_state);

        ll_order_shoe_consistent = findViewById(R.id.ll_order_shoe_consistent);
        img_shoe_consistent = findViewById(R.id.img_shoe_consistent);
        name_shoe_consistent = findViewById(R.id.name_shoe_consistent);
        num_shoe_consistent = findViewById(R.id.num_shoe_consistent);

        ll_order_shoe_font = findViewById(R.id.ll_order_shoe_font);
        img_shoe_font = findViewById(R.id.img_shoe_font);
        name_shoe_font = findViewById(R.id.name_shoe_font);
        num_shoe_font = findViewById(R.id.num_shoe_font);
        ll_order_shoe_rear = findViewById(R.id.ll_order_shoe_rear);
        img_shoe_rear = findViewById(R.id.img_shoe_rear);
        name_shoe_rear = findViewById(R.id.name_shoe_rear);
        num_shoe_rear = findViewById(R.id.num_shoe_rear);
        user_phone_img = findViewById(R.id.user_phone_img);
    }
}
