package com.ruyiruyi.ruyiruyi.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ruyiruyi.ruyiruyi.R;
import com.ruyiruyi.ruyiruyi.db.DbConfig;
import com.ruyiruyi.ruyiruyi.db.model.Location;
import com.ruyiruyi.ruyiruyi.db.model.User;
import com.ruyiruyi.ruyiruyi.ui.activity.base.RyBaseActivity;
import com.ruyiruyi.ruyiruyi.ui.cell.ShopChooseCell;
import com.ruyiruyi.ruyiruyi.ui.fragment.MerchantFragment;
import com.ruyiruyi.ruyiruyi.ui.fragment.OrderFragment;
import com.ruyiruyi.ruyiruyi.ui.model.ServiceType;
import com.ruyiruyi.ruyiruyi.ui.multiType.Shop;
import com.ruyiruyi.ruyiruyi.utils.RequestUtils;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;
import com.ruyiruyi.rylibrary.cell.ActionBar;
import com.ruyiruyi.rylibrary.cell.AmountView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import rx.functions.Action1;

public class CxwyFreeChangeActivity extends RyBaseActivity {
    private static final String TAG = CxwyFreeChangeActivity.class.getSimpleName();
    private ActionBar actionBar;
    private TextView userNameText;
    private TextView userPhoneText;
    private TextView carNumberText;
    private TextView catFontText;
    private TextView carRearText;
    private AmountView fontAmountView;
    private AmountView rearAmountView;
    private LayoutInflater mInflater;

    private ShopChooseCell shopChooseView;
    private TextView isuseCountText;
    private int fontFreeAmount;
    private int rearFreeAmount;
    private int fontRearFlag;

    private FrameLayout tireCountLayout;
    private FrameLayout fontTireCountLayout;
    private FrameLayout rearTireCountLayout;
    private TextView tireCountText;
    private int isuseCount;     //选择的畅行无忧数量 可更换轮胎数

    public int currentFontCount = 0;
    public int currentRearCount = 0;

    private int fontMaxCount = 0;
    private int rearMaxCount = 0;
    private Shop shop;

    private TextView postOrder;
    private String cxwyListStr;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cxwy_free_change);

        actionBar = (ActionBar) findViewById(R.id.my_action);
        actionBar.setTitle("免费再换");

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
        Intent intent = getIntent();
        fontFreeAmount = intent.getIntExtra("FONT_FREE_AMOUNT",0);
        rearFreeAmount = intent.getIntExtra("REAR_FREE_AMOUNT",0);
        fontRearFlag = intent.getIntExtra("FONT_REAR_FLAG",0);
        isuseCount = intent.getIntExtra("CHOOSE_COUNT",0);
        cxwyListStr = intent.getStringExtra("CHOOSE_CXWY_ID");

        progressDialog = new ProgressDialog(this);
        initView();
        initAmountView();
        initData();
        initShop();

    }

    /**
     * 获取默认门店
     */
    private void initShop() {
        Location location = new DbConfig(this).getLocation();
        String city = location.getCity();
        Double jingdu = location.getJingdu();
        Double weidu = location.getWeidu();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("page", 1);
            jsonObject.put("rows", 1);
            jsonObject.put("cityName", city);
            jsonObject.put("storeName", "");
            jsonObject.put("storeType", "");//门店类型  1:4S店   2:快修店  3:维修厂  4:美容店

            jsonObject.put("rankType", 1);//排序方式  0:默认排序  1：附近优先
            //门店服务类型 2:汽车保养  3:美容清洗  4:改装  5:轮胎服务
            jsonObject.put("serviceType", 5); //针对性门店

            jsonObject.put("longitude", jingdu + "");
            jsonObject.put("latitude", weidu + "");
        } catch (JSONException e) {
        }
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "selectStoreByCondition");
        params.addBodyParameter("reqJson", jsonObject.toString());
        String token = new DbConfig(this).getToken();
        params.addParameter("token", token);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject1 = new JSONObject(result);
                    String status = jsonObject1.getString("status");
                    String msg = jsonObject1.getString("msg");

                    if (status.equals("1")) {
                        JSONObject data = jsonObject1.getJSONObject("data");
                        JSONArray storeQuaryResVos = data.getJSONArray("storeQuaryResVos");

                        JSONObject storeObjecct = storeQuaryResVos.getJSONObject(0);
                        String distance = storeObjecct.getString("distance");
                        String storeAddress = storeObjecct.getString("storeAddress");
                        String storeId = storeObjecct.getString("storeId");
                        int storeIdInt = Integer.parseInt(storeId);
                        String storeImg = storeObjecct.getString("storeImg");
                        String storeName = storeObjecct.getString("storeName");
                        String storeType = storeObjecct.getString("storeType");
                        String storeTypeColor = storeObjecct.getString("storeTypeColor");
                        JSONArray serviceArray = storeObjecct.getJSONArray("storeServcieList");
                        List<ServiceType> serviceTypeList = new ArrayList<ServiceType>();
                        for (int j = 0; j < serviceArray.length(); j++) {
                            JSONObject serviceObject = serviceArray.getJSONObject(j);
                            JSONObject service = serviceObject.getJSONObject("service");
                            String color = service.getString("color");
                            String name = service.getString("name");
                            serviceTypeList.add(new ServiceType(name, color));
                        }
                        shop = new Shop(storeIdInt, storeType, storeTypeColor, storeName, storeImg, storeAddress, distance);
                        shop.setServiceTypeList(serviceTypeList);
                        shopChooseView.setValue(shop.getStoreName(), shop.getStoreImage(), shop.getStoreAddress(), shop.getStoreDistence(), shop.getServiceTypeList(), mInflater);

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
     * 获取车牌号
     */
    private void initData() {
        User user = new DbConfig(this).getUser();
        int carId = user.getCarId();
        int userId = user.getId();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userId", userId);
            jsonObject.put("userCarId", carId);
        } catch (JSONException e) {
        }
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "getCarByUserIdAndCarId");
        params.addBodyParameter("reqJson", jsonObject.toString());
        String token = new DbConfig(this).getToken();
        params.addParameter("token", token);
        x.http().post(params, new Callback.CommonCallback<String>() {
            private String carNumber;

            @Override
            public void onSuccess(String result) {
                JSONObject jsonObject1 = null;
                try {
                    jsonObject1 = new JSONObject(result);
                    String status = jsonObject1.getString("status");
                    String msg = jsonObject1.getString("msg");
                    if (status.equals("1")){
                        JSONObject data = jsonObject1.getJSONObject("data");
                        carNumber = data.getString("platNumber");
                        carNumberText.setText(carNumber);

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
        mInflater = LayoutInflater.from(this);
        shopChooseView = (ShopChooseCell) findViewById(R.id.shop_choose_cell);
        userNameText = (TextView) findViewById(R.id.user_name_text);
        userPhoneText = (TextView) findViewById(R.id.user_phone_text);
        carNumberText = (TextView) findViewById(R.id.car_number_text);
        catFontText = (TextView) findViewById(R.id.font_tire_count_text);
        carRearText = (TextView) findViewById(R.id.rear_tire_count_text);
        fontAmountView = (AmountView) findViewById(R.id.font_amount_view);
        rearAmountView = (AmountView) findViewById(R.id.area_amount_view);
        isuseCountText = (TextView) findViewById(R.id.isuser_count_text);

        tireCountLayout = (FrameLayout) findViewById(R.id.tire_count_layout);
        fontTireCountLayout = (FrameLayout) findViewById(R.id.font_tire_count_layout);
        rearTireCountLayout = (FrameLayout) findViewById(R.id.rear_tire_count_layout);
        tireCountText = (TextView) findViewById(R.id.tire_count_text);

        postOrder = (TextView) findViewById(R.id.tire_repair_button);
        isuseCountText.setText(isuseCount +"");

        if (fontRearFlag == 0){
            tireCountLayout.setVisibility(View.VISIBLE);
            fontTireCountLayout.setVisibility(View.GONE);
            rearTireCountLayout.setVisibility(View.GONE);
            tireCountText.setText(fontFreeAmount+"");
        }else if(fontRearFlag == 1 || fontRearFlag == 2){
            tireCountLayout.setVisibility(View.GONE);
            fontTireCountLayout.setVisibility(View.VISIBLE);
            rearTireCountLayout.setVisibility(View.VISIBLE);
            catFontText.setText(fontFreeAmount +"");
            carRearText.setText(rearFreeAmount +"");
        }else {
            tireCountLayout.setVisibility(View.VISIBLE);
            fontTireCountLayout.setVisibility(View.GONE);
            rearTireCountLayout.setVisibility(View.GONE);
            tireCountText.setText(0+"");
        }

        fontAmountView.setOnAmountChangeListener(new AmountView.OnAmountChangeListener() {
            @Override
            public void onAmountChange(View view, int amount) {
               currentFontCount = amount;
                initAmountView();

           /*     if (amount == fontMaxCount) {
                    Toast.makeText(CxwyFreeChangeActivity.this, "轮胎数量达到购买上限", Toast.LENGTH_SHORT).show();
                }*/

            }
        });
        // rearAmountView.setGoods_storage(2);
        rearAmountView.setOnAmountChangeListener(new AmountView.OnAmountChangeListener() {
            @Override
            public void onAmountChange(View view, int amount) {
                currentRearCount = amount;
                initAmountView();
/*
                if (amount == rearMaxCount) {
                    Toast.makeText(CxwyFreeChangeActivity.this, "轮胎数量达到购买上限", Toast.LENGTH_SHORT).show();
                }*/

            }
        });

        //初始化数据信息
        User user = new DbConfig(this).getUser();
        String userNick = user.getNick();
        String userPhone = user.getPhone();
        userNameText.setText(userNick);
        userPhoneText.setText(userPhone);
        //门店选择
        RxViewAction.clickNoDouble(shopChooseView)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Intent intent = new Intent(getApplicationContext(), ShopChooseActivity.class);
                        intent.putExtra(MerchantFragment.SHOP_TYPE, 5);
                        startActivityForResult(intent, TireChangeActivity.CHOOSE_SHOP);
                    }
                });

        RxViewAction.clickNoDouble(postOrder)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (currentRearCount + currentFontCount < isuseCount) {
                            Toast.makeText(CxwyFreeChangeActivity.this, "您的轮胎还未全部选完！", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        freeChangeOrder();
                    }
                });


    }

    private void freeChangeOrder() {
        showDialogProgress(progressDialog, "免费再换订单提交中...");

        User user = new DbConfig(this).getUser();
        int userId = user.getId();
        int userCarId = user.getCarId();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("storeId", shop.getStoreId());
            jsonObject.put("userCarId", userCarId);
            jsonObject.put("userId", userId);
            if (fontRearFlag == 0) {
                jsonObject.put("fontAmount", currentFontCount + currentRearCount);
                jsonObject.put("rearAmount", 0);
            } else {
                jsonObject.put("fontAmount", currentFontCount);
                jsonObject.put("rearAmount", currentRearCount);
            }
            jsonObject.put("fontRearFlag", fontRearFlag);
            jsonObject.put("orderType", 3);
            jsonObject.put("reason", 0);
            jsonObject.put("cxwyList",cxwyListStr );
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "addFreeChangeOrderWithCXWY");
        Log.e(TAG, "initOrderFromService: -++-" + jsonObject.toString());
        params.addBodyParameter("reqJson", jsonObject.toString());
        String token = new DbConfig(this).getToken();
        params.addParameter("token", token);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess:---- " + result);
                JSONObject jsonObject1 = null;
                try {
                    jsonObject1 = new JSONObject(result);
                    String status = jsonObject1.getString("status");
                    String msg = jsonObject1.getString("msg");
                    if (status.equals("1")) {
                        Intent intent = new Intent(getApplicationContext(), OrderActivity.class);
                        intent.putExtra(OrderFragment.ORDER_TYPE, "DFH");
                        intent.putExtra(OrderActivity.ORDER_FROM, 1);
                        startActivity(intent);
                        finish();
                    }else {
                        Toast.makeText(CxwyFreeChangeActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {

                }
            }

            @Override
            public void onError(Throwable throwable, boolean b) {

            }

            @Override
            public void onCancelled(CancelledException e) {

            }

            @Override
            public void onFinished() {
                hideDialogProgress(progressDialog);
            }
        });

    }

    private void initAmountView() {
        if (fontRearFlag == 0){ //前后轮一致
            fontMaxCount = 2;
            rearMaxCount = 2;
            if (isuseCount == 4){ //可更换的有4个
                fontAmountView.setGoods_storage(2);
                rearAmountView.setGoods_storage(2);
            }else { //不足四条
                fontAmountView.setGoods_storage(2);
                rearAmountView.setGoods_storage(2);
                if (currentFontCount + currentRearCount == isuseCount) {        //达到最大轮胎数时 设置为最大数
                    fontAmountView.setGoods_storage(currentFontCount);
                    rearAmountView.setGoods_storage(currentRearCount);
                    fontMaxCount = currentFontCount;
                    rearMaxCount = currentRearCount;
                }
            }
        }else { //前后轮不一致
            fontMaxCount = fontFreeAmount;
            rearMaxCount = rearFreeAmount;
            fontAmountView.setGoods_storage(fontFreeAmount);
            rearAmountView.setGoods_storage(rearFreeAmount);
            if (currentFontCount + currentRearCount == isuseCount){
                fontAmountView.setGoods_storage(currentFontCount);
                rearAmountView.setGoods_storage(currentRearCount);
                fontMaxCount = currentFontCount;
                rearMaxCount = currentRearCount;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e(TAG, "onActivityResult: ------------" + data);
        Log.e(TAG, "onActivityResult: ------------" + requestCode);
        Log.e(TAG, "onActivityResult: ------------" + resultCode);
        if (resultCode == TireChangeActivity.CHOOSE_SHOP) {
            Bundle bundle = data.getExtras();
            shop = ((Shop) bundle.getSerializable("shop"));
            shopChooseView.setValue(shop.getStoreName(), shop.getStoreImage(), shop.getStoreAddress(), shop.getStoreDistence(), shop.getServiceTypeList(), mInflater);
        }
    }
}
