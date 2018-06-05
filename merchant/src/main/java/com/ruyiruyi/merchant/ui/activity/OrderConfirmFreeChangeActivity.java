package com.ruyiruyi.merchant.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ruyiruyi.merchant.R;
import com.ruyiruyi.merchant.bean.FreeChangeNewShoeBean;
import com.ruyiruyi.merchant.bean.FreeChangeOldShoeBean;
import com.ruyiruyi.merchant.db.DbConfig;
import com.ruyiruyi.merchant.ui.activity.base.MerchantBaseActivity;
import com.ruyiruyi.merchant.ui.multiType.PublicShoeFlag;
import com.ruyiruyi.merchant.utils.UtilsURL;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;
import com.ruyiruyi.rylibrary.cell.ActionBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import rx.functions.Action1;

public class OrderConfirmFreeChangeActivity extends MerchantBaseActivity {
    private TextView user_name;
    private TextView user_phone;
    private TextView car_num;
    private TextView store_name;
    private TextView order_num;
    private ImageView user_phone_img;
    private LinearLayout ll_shoe_font;
    private LinearLayout ll_shoe_rear;
    private LinearLayout ll_shoe_consistent;

    private ActionBar actionBar;
    private String orderNo;
    private String orderType;
    private String storeId;
    private String userName;
    private String userPhone;
    private String platNumber;
    private String storeName;
    private boolean isShoeConsistent = false;  //轮胎是否一致标记 默认false不一致
    private int fiveYearsAmount = 0;// 满五年更换的轮胎数 默认0
    private List<PublicShoeFlag> shoeFlagList; //shoeFlagListlist
    private List<FreeChangeOldShoeBean> oldShoeList; //旧轮胎list
    private List<FreeChangeNewShoeBean> newShoeList; //新轮胎list
    private String TAG = OrderConfirmFreeChangeActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirm_free_change);
        actionBar = findViewById(R.id.open_order_freechange_acbar);
        actionBar.setTitle("订单确认");
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
        //获取传递数据
        orderNo = getIntent().getStringExtra("orderNo");
        orderType = getIntent().getStringExtra("orderType");
        storeId = new DbConfig().getId() + "";

        initView();
        initData();
        bindView();

    }

    private void bindView() {
        //拨打电话监听
        RxViewAction.clickNoDouble(user_phone_img).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                String phoneStr = user_phone.getText().toString();
                //跳转拨打电话页面
                Intent intent = new Intent(Intent.ACTION_DIAL);
                Uri data = Uri.parse("tel:" + phoneStr);
                intent.setData(data);
                startActivity(intent);

            }
        });
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
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    int status = jsonObject.getInt("status");
                    String msg = jsonObject.getString("msg");
                    if (status == 1) {
                        JSONObject data = jsonObject.getJSONObject("data");
                        userName = data.getString("userName");
                        userPhone = data.getString("userPhone");
                        platNumber = data.getString("platNumber");
                        storeName = data.getString("storeName");
                        JSONArray freeChangeOrderVoList = data.getJSONArray("freeChangeOrderVoList");
                        for (int i = 0; i < freeChangeOrderVoList.length(); i++) {//存取前后胎更换位置数量list数据
                            PublicShoeFlag bean = new PublicShoeFlag();
                            JSONObject objBean = (JSONObject) freeChangeOrderVoList.get(i);
                            String fontRearFlag = objBean.getString("fontRearFlag");
                            if (fontRearFlag.equals("0")) {
                                isShoeConsistent = true;
                            }
                            bean.setShoeImgUrl(objBean.getString("shoeImg"));
                            bean.setShoeName(objBean.getString("shoeName"));
                            bean.setOrderType(orderType);
                            bean.setShoeFlag(objBean.getString("fontRearFlag"));
                            bean.setShoeAmount(objBean.getInt("rearAmount") + "");
                            shoeFlagList.add(bean);

                        }
                        JSONArray userCarShoeOldBarCodeList = data.getJSONArray("userCarShoeOldBarCodeList");
                        for (int i = 0; i < userCarShoeOldBarCodeList.length(); i++) {//存取旧轮胎数据
                            FreeChangeOldShoeBean bean = new FreeChangeOldShoeBean();
                            JSONObject objBean = (JSONObject) userCarShoeOldBarCodeList.get(i);
                            bean.setShoeImgUrl(objBean.getString("shoeImgUrl"));
                            bean.setShoeName(objBean.getString("shoeName"));
                            bean.setOrderType(orderType);
                            bean.setFontRearFlag(objBean.getInt("fontRearFlag") + "");
                            bean.setId(objBean.getInt("id") + "");
                            bean.setBarCode(objBean.getString("barCode"));
                            bean.setIsReach5Years(objBean.getInt("isReach5Years") + "");
                            bean.setOrderNo(objBean.getString("orderNo"));
                            bean.setTime(objBean.getLong("time"));
                            bean.setUserCarId(objBean.getInt("userCarId") + "");
                            bean.setUserId(objBean.getInt("userId") + "");
                            oldShoeList.add(bean);
                        }
                        JSONArray userCarShoeBarCodeList = data.getJSONArray("userCarShoeBarCodeList");
                        for (int i = 0; i < userCarShoeBarCodeList.length(); i++) {//存取新轮胎数据
                            FreeChangeNewShoeBean bean = new FreeChangeNewShoeBean();
                            JSONObject objBean = (JSONObject) userCarShoeBarCodeList.get(i);
                            bean.setShoeImgUrl(objBean.getString("shoeImgUrl"));
                            bean.setBarcodeImgUrl(objBean.getString("barcodeImgUrl"));
                            bean.setShoeName(objBean.getString("shoeName"));
                            bean.setOrderType(orderType);
                            bean.setFontRearFlag(objBean.getInt("fontRearFlag") + "");
                            bean.setId(objBean.getInt("id") + "");
                            bean.setBarCode(objBean.getString("barCode"));
                            bean.setOrderNo(objBean.getString("orderNo"));
                            bean.setTime(objBean.getLong("time"));
                            bean.setStatus(objBean.getInt("status") + "");
                            newShoeList.add(bean);
                        }
                        Log.e(TAG, "onSuccess: " + "onSuccess");

                        //设置数据
                        setData();


                    } else {
                        Toast.makeText(OrderConfirmFreeChangeActivity.this, msg, Toast.LENGTH_SHORT).show();
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

    private void setData() {
        //设置外层数据
        user_name.setText(userName);
        user_phone.setText(userPhone);
        car_num.setText(platNumber);
        store_name.setText(storeName);
        order_num.setText(orderNo);
        //设置内层数据
        if (shoeFlagList.size() == 2) {

        }
        if (shoeFlagList.size() == 1) {
            if (shoeFlagList.get(0).getShoeFlag().equals("0")) {
                ll_shoe_font.setVisibility(View.GONE);
                ll_shoe_rear.setVisibility(View.GONE);
                ll_shoe_consistent.setVisibility(View.VISIBLE);
            }
            if (shoeFlagList.get(0).getShoeFlag().equals("1")) {
                ll_shoe_font.setVisibility(View.VISIBLE);
                ll_shoe_rear.setVisibility(View.GONE);
                ll_shoe_consistent.setVisibility(View.GONE);
            }
            if (shoeFlagList.get(0).getShoeFlag().equals("2")) {
                ll_shoe_font.setVisibility(View.GONE);
                ll_shoe_rear.setVisibility(View.VISIBLE);
                ll_shoe_consistent.setVisibility(View.GONE);
            }

        }

    }

    private void initView() {
        user_name = findViewById(R.id.user_name);
        user_phone = findViewById(R.id.user_phone);
        car_num = findViewById(R.id.car_num);
        store_name = findViewById(R.id.store_name);
        order_num = findViewById(R.id.order_num);
        user_phone_img = findViewById(R.id.user_phone_img);
        ll_shoe_font = findViewById(R.id.ll_shoe_font);
        ll_shoe_rear = findViewById(R.id.ll_shoe_rear);
        ll_shoe_consistent = findViewById(R.id.ll_shoe_consistent);

        shoeFlagList = new ArrayList<>();
        oldShoeList = new ArrayList<>();
        newShoeList = new ArrayList<>();
    }
}
