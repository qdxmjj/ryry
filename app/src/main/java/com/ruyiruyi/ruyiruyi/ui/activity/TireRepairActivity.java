package com.ruyiruyi.ruyiruyi.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
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
import com.ruyiruyi.ruyiruyi.ui.model.StoreType;
import com.ruyiruyi.ruyiruyi.ui.multiType.Shop;
import com.ruyiruyi.ruyiruyi.utils.RequestUtils;
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

public class TireRepairActivity extends RyBaseActivity {
    private static final String TAG = TireRepairActivity.class.getSimpleName();
    private ActionBar actionBar;
    private ShopChooseCell shopChooseView;
    public List<StoreType> typeList;
    private LayoutInflater mInflater;

    private Shop shop;
    private TextView tireRepairButton;
    private TextView userNameText;
    private TextView userPhoneText;
    private TextView carNumberText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tire_repair,R.id.my_action);
        actionBar = (ActionBar) findViewById(R.id.my_action);
        actionBar.setTitle("轮胎修补");;
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


        initView();
        initData();
        initShop();

    }

    private void initShop() {
        Location location = new DbConfig(this).getLocation();
        String city = location.getCity();
        Double jingdu = location.getJingdu();
        Double weidu = location.getWeidu();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("page",1);
            jsonObject.put("rows",1);
            jsonObject.put("cityName",city);
            jsonObject.put("storeName","");
            jsonObject.put("storeType","");//门店类型  1:4S店   2:快修店  3:维修厂  4:美容店

            jsonObject.put("rankType",1);//排序方式  0:默认排序  1：附近优先
            //门店服务类型 2:汽车保养  3:美容清洗  4:改装  5:轮胎服务
            jsonObject.put("serviceType",5); //针对性门店

            jsonObject.put("longitude",jingdu + "");
            jsonObject.put("latitude",weidu + "");
        } catch (JSONException e) {
        }
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "selectStoreByCondition");
        Log.e(TAG, "initDataFromService:---------- " + jsonObject.toString() );
        params.addBodyParameter("reqJson",jsonObject.toString());
        String token = new DbConfig(this).getToken();
        params.addParameter("token",token);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    Log.e(TAG, "onSuccess: " + result);
                    JSONObject jsonObject1 = new JSONObject(result);
                    String status = jsonObject1.getString("status");
                    String msg = jsonObject1.getString("msg");

                    if (status.equals("1")){
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
                            serviceTypeList.add(new ServiceType(name,color));
                        }
                        shop = new Shop(storeIdInt,storeType,storeTypeColor,storeName,storeImg,storeAddress,distance);
                        shop.setServiceTypeList(serviceTypeList);
                        shopChooseView.setValue(shop.getStoreName(),shop.getStoreImage(), shop.getStoreAddress(),shop.getStoreDistence(),shop.getServiceTypeList(),mInflater);

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
                Log.e(TAG, "onSuccess: " + result);
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
        tireRepairButton = (TextView) findViewById(R.id.tire_repair_button);
        userNameText = (TextView) findViewById(R.id.user_name_text);
        userPhoneText = (TextView) findViewById(R.id.user_phone_text);
        carNumberText = (TextView) findViewById(R.id.car_number_text);


        RxViewAction.clickNoDouble(tireRepairButton)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        postRepairOrder();
                    }
                });

        RxViewAction.clickNoDouble(shopChooseView)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Intent intent = new Intent(getApplicationContext(), ShopChooseActivity.class);
                        intent.putExtra(MerchantFragment.SHOP_TYPE,5);
                        startActivityForResult(intent,TireChangeActivity.CHOOSE_SHOP);
                    }
                });

       /* shopChooseView.setValue("青岛汽车总店","http://180.76.243.205:8111/images/flgure/970FB91D-D680-437D-606D-0AFAEC4E5F10.jpg",
                "青岛市城阳区天安数码城","15km",typeList,mInflater);*/
        User user = new DbConfig(this).getUser();
        String userNick = user.getNick();
        String userPhone = user.getPhone();
        userNameText.setText(userNick);
        userPhoneText.setText(userPhone);
    }

    private void postRepairOrder() {
        User user = new DbConfig(this).getUser();
        int userId = user.getId();
        int carId = user.getCarId();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userId",userId);
            jsonObject.put("userCarId",carId);
            jsonObject.put("storeId",shop.getStoreId());
        } catch (JSONException e) {
        }
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "addShoeRepairOrder");
        Log.e(TAG, "initDataFromService:---------- " + jsonObject.toString() );
        params.addBodyParameter("reqJson",jsonObject.toString());
        String token = new DbConfig(this).getToken();
        params.addParameter("token",token);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess: --" + result);
                JSONObject jsonObject1 = null;
                try {
                    jsonObject1 = new JSONObject(result);
                    String status = jsonObject1.getString("status");
                    String msg = jsonObject1.getString("msg");
                    if (status.equals("1")){
                        Intent intent = new Intent(getApplicationContext(), OrderActivity.class);
                        intent.putExtra(OrderFragment.ORDER_TYPE, "DFW");
                        intent.putExtra(OrderActivity.ORDER_FROM,1);
                        startActivity(intent);
                    }else {
                        Toast.makeText(TireRepairActivity.this, msg, Toast.LENGTH_SHORT).show();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == TireChangeActivity.CHOOSE_SHOP){
            Bundle bundle = data.getExtras();
            shop = ((Shop) bundle.getSerializable("shop"));
            shopChooseView.setValue(shop.getStoreName(),shop.getStoreImage(), shop.getStoreAddress(),shop.getStoreDistence(),shop.getServiceTypeList(),mInflater);

            Log.e(TAG, "onActivityResult: " + shop.getStoreId());
            Log.e(TAG, "onActivityResult: " + shop.getStoreName());
        }
    }
}
