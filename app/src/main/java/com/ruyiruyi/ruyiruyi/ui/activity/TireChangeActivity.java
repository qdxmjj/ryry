package com.ruyiruyi.ruyiruyi.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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
import com.ruyiruyi.rylibrary.cell.AmountView;
import com.ruyiruyi.rylibrary.cell.flowlayout.TagFlowLayout;
import com.ruyiruyi.rylibrary.ui.viewpager.CustomBanner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import rx.functions.Action1;

public class TireChangeActivity extends RyBaseActivity {
    public final static String CHANGE_TIRE = "CHANGE_TIRE";
    public static final int CHOOSE_SHOP = 2;
    public static final String TAG = TireChangeActivity.class.getSimpleName();
    public int currentChangeType = 0;  //0是首次更换  1是免费更换

    private ActionBar actionBar;
    private TagFlowLayout typeFlowLayout;
    private String[] mVals = new String[]
            {"快修店", "4S", "美容 ", "清洗", "轮胎"};

    public List<StoreType> typeList;
    private CustomBanner mBanner;
    private LinearLayout freeChangeLayout;
    public int currentFontCount = 0;
    public int currentRearCount = 0;

    private AmountView fontAmountView;
    private AmountView rearAmountView;
    private FrameLayout tireLiuchengLayout;
    private LinearLayout reasonOneLayout;
    private LinearLayout reasonTwoLayout;
    public boolean oneReasonCheck = false;
    public boolean twoReasonCheck = false;
    private ImageView reasonOneImage;
    private ImageView reasonTwoImage;
    private ShopChooseCell shopChooseView;
    private LayoutInflater mInflater;
    private int rearAvaliableAmount = 0;
    private int fontAvaliableAmount = 0;
    private int fontRearFlag;       //fontRearFlag：0：前后轮一致   非0:前后轮不一致
    private int fontMaxCount = 0;
    private int rearMaxCount = 0;
    private Shop shop;
    private TextView postButton;

    private int isReach5Years;   //`0不满 1是满五年
    private int rearFreeAmount;
    private int fontFreeAmount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tire_change, R.id.my_action);
        actionBar = (ActionBar) findViewById(R.id.my_action);

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
        currentChangeType = intent.getIntExtra(CHANGE_TIRE, 0);

        if (currentChangeType == 0) {
            actionBar.setTitle("首次更换");
            ;
        } else if (currentChangeType == 1) {
            actionBar.setTitle("免费再换");
            ;
        }


        initData();
        if (currentChangeType == 0) {
            initDataFromService();  //获取首次更换数据
        } else if (currentChangeType == 1) {
            initFreeTireFromService();
        }

        initShop();
        initView();
        initReasonView();
        //  fontRearFlag = 0;
        //  fontAvaliableAmount = 3;
        //  rearAvaliableAmount = 0;

    }

    private void initFreeTireFromService() {
        User user = new DbConfig(this).getUser();
        int carId = user.getCarId();
        int userId = user.getId();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userId", userId);
            jsonObject.put("userCarId", carId);
        } catch (JSONException e) {

        }
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "getUserChangedShoeNumAnd5Year");
        params.addBodyParameter("reqJson", jsonObject.toString());
        String token = new DbConfig(this).getToken();
        params.addParameter("token", token);
        x.http().post(params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                JSONObject jsonObject1 = null;
                Log.e(TAG, "onSuccess:----------------------+- " + result);
                try {
                    jsonObject1 = new JSONObject(result);
                    String status = jsonObject1.getString("status");
                    String msg = jsonObject1.getString("msg");
                    if (status.equals("1")) {
                        JSONObject data = jsonObject1.getJSONObject("data");
                        fontFreeAmount = data.getInt("fontAmount");
                        ;
                        rearFreeAmount = data.getInt("rearAmount");
                        fontRearFlag = data.getInt("fontRearFlag");
                        isReach5Years = data.getInt("isReach5Years");

                        if (fontFreeAmount == 0 && rearFreeAmount == 0) {
                            Toast.makeText(TireChangeActivity.this, "暂无可更换轮胎", Toast.LENGTH_SHORT).show();
                        }
                        initAmountView();
                    } else {
                        Toast.makeText(TireChangeActivity.this, msg, Toast.LENGTH_SHORT).show();
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

    private void initDataFromService() {
        User user = new DbConfig(this).getUser();
        int carId = user.getCarId();
        int userId = user.getId();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userId", userId);
            jsonObject.put("userCarId", carId);
        } catch (JSONException e) {

        }
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "getUserUnusedShoeNum");
        params.addBodyParameter("reqJson", jsonObject.toString());
        String token = new DbConfig(this).getToken();
        params.addParameter("token", token);
        Log.e(TAG, "initDataFromService: -----------------------" + jsonObject.toString());
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                JSONObject jsonObject1 = null;
                Log.e(TAG, "onSuccess:----------------------+- " + result);
                try {
                    jsonObject1 = new JSONObject(result);
                    String status = jsonObject1.getString("status");
                    String msg = jsonObject1.getString("msg");
                    if (status.equals("1")) {
                        JSONObject data = jsonObject1.getJSONObject("data");
                        fontRearFlag = data.getInt("fontRearFlag");
                        fontAvaliableAmount = data.getInt("fontAvaliableAmount");
                        rearAvaliableAmount = data.getInt("rearAvaliableAmount");
                    } else if (status.equals("-999")) {
                        showUserTokenDialog("您的账号在其它设备登录,请重新登录");
                    } else {
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                    }
                    initAmountView();


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
        Log.e(TAG, "initDataFromService:---------- " + jsonObject.toString());
        params.addBodyParameter("reqJson", jsonObject.toString());
        String token = new DbConfig(this).getToken();
        params.addParameter("token", token);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    Log.e(TAG, "onSuccess: " + result);
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

    private void initReasonView() {
        reasonOneImage.setImageResource(oneReasonCheck ? R.drawable.ic_check : R.drawable.ic_check_no);
        reasonTwoImage.setImageResource(twoReasonCheck ? R.drawable.ic_check : R.drawable.ic_check_no);
    }


    private void initData() {
        typeList = new ArrayList<>();
        typeList.add(new StoreType(1, "快修店"));
        typeList.add(new StoreType(2, "4s"));
        typeList.add(new StoreType(3, "轮胎"));
        typeList.add(new StoreType(4, "轮胎"));
    }

    private void initView() {
        mInflater = LayoutInflater.from(this);
        //typeFlowLayout = (TagFlowLayout) findViewById(R.id.type_flowlayout);
        mBanner = (CustomBanner) findViewById(R.id.tire_change_banner);
        freeChangeLayout = (LinearLayout) findViewById(R.id.free_change_layout);
        fontAmountView = (AmountView) findViewById(R.id.font_amount_view);
        rearAmountView = (AmountView) findViewById(R.id.area_amount_view);
        tireLiuchengLayout = (FrameLayout) findViewById(R.id.tire_liucheng_layout);
        reasonOneLayout = (LinearLayout) findViewById(R.id.reason_one_layout);
        reasonTwoLayout = (LinearLayout) findViewById(R.id.reason_two_layout);
        reasonOneImage = (ImageView) findViewById(R.id.reason_one_image);
        reasonTwoImage = (ImageView) findViewById(R.id.reason_two_image);
        shopChooseView = (ShopChooseCell) findViewById(R.id.shop_choose_cell);
        postButton = (TextView) findViewById(R.id.first_change_button);

        RxViewAction.clickNoDouble(postButton)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {

                        if (currentChangeType == 0) {
                            postChangeOrder();
                        } else if (currentChangeType == 1) {
                            freeChangeOrder();
                        }

                    }
                });

        RxViewAction.clickNoDouble(shopChooseView)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Intent intent = new Intent(getApplicationContext(), ShopChooseActivity.class);
                        intent.putExtra(MerchantFragment.SHOP_TYPE, 5);
                        startActivityForResult(intent, CHOOSE_SHOP);
                    }
                });


        freeChangeLayout.setVisibility(currentChangeType == 0 ? View.GONE : View.VISIBLE);


        initAmountView();
        fontAmountView.setOnAmountChangeListener(new AmountView.OnAmountChangeListener() {
            @Override
            public void onAmountChange(View view, int amount) {
                currentFontCount = amount;
                initAmountView();
                if (amount == fontMaxCount) {
                    Toast.makeText(TireChangeActivity.this, "轮胎数量达到购买上限", Toast.LENGTH_SHORT).show();
                }
            }
        });
        // rearAmountView.setGoods_storage(2);
        rearAmountView.setOnAmountChangeListener(new AmountView.OnAmountChangeListener() {
            @Override
            public void onAmountChange(View view, int amount) {
                currentRearCount = amount;
                initAmountView();
                if (amount == rearMaxCount) {
                    Toast.makeText(TireChangeActivity.this, "轮胎数量达到购买上限", Toast.LENGTH_SHORT).show();
                }
            }
        });


        List<String> imageList = new ArrayList<>();

        imageList.add("http://180.76.243.205:8111/images/Advertisement/cxwy.png");
        imageList.add("http://180.76.243.205:8111/images/Advertisement/cxwy1000.png");
        imageList.add("http://180.76.243.205:8111/images/Advertisement/cxwy1000.png");


        mBanner.setPages(new CustomBanner.ViewCreator<String>() {
            @Override
            public View createView(Context context, int position) {
                ImageView imageView = new ImageView(context);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                return imageView;
            }

            @Override
            public void updateUI(Context context, View view, int position, String entity) {
                Glide.with(context).load(entity).into((ImageView) view);
            }
        }, imageList)//                //设置指示器为普通指示器
//                .setIndicatorStyle(CustomBanner.IndicatorStyle.ORDINARY)
//                //设置两个点图片作为翻页指示器，不设置则没有指示器，可以根据自己需求自行配合自己的指示器,不需要圆点指示器可用不设
//                .setIndicatorRes(R.drawable.shape_point_select, R.drawable.shape_point_unselect)
//                //设置指示器的方向
//                .setIndicatorGravity(CustomBanner.IndicatorGravity.CENTER)
//                //设置指示器的指示点间隔
//                .setIndicatorInterval(20)
                //设置自动翻页
                .startTurning(5000);

/*
        typeFlowLayout.setAdapter(new TagAdapter<StoreType>(typeList) {


            @Override
            public View getView(FlowLayout parent, int position, StoreType storeType) {
                TextView tv = (TextView) mInflater.inflate(R.layout.item_type,
                        typeFlowLayout, false);
                tv.setText(storeType.getStoreName());
                if (storeType.getType() == 1){
                    tv.setBackgroundResource(R.drawable.tag_bg_hong);
                } else if (storeType.getType() == 2) {
                    tv.setBackgroundResource(R.drawable.tag_bg_huang);
                }else if (storeType.getType() == 3){
                    tv.setBackgroundResource(R.drawable.tag_bg_qing);
                }else if (storeType.getType() == 4){
                    tv.setBackgroundResource(R.drawable.tag_bg_lv);
                }
                return tv;
            }
        });
        typeFlowLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getActivity(), "FlowLayout Clicked", Toast.LENGTH_SHORT).show();
            }
        });*/

        RxViewAction.clickNoDouble(tireLiuchengLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        startActivity(new Intent(getApplicationContext(), TireLiuchengActivity.class));
                    }
                });


        RxViewAction.clickNoDouble(reasonOneLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (isReach5Years == 1) {
                            oneReasonCheck = !oneReasonCheck;
                            initReasonView();
                        } else {
                            Toast.makeText(TireChangeActivity.this, "您的轮胎不满5年", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
        RxViewAction.clickNoDouble(reasonTwoLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        twoReasonCheck = !twoReasonCheck;
                        initReasonView();
                    }
                });
    }

    /**
     * 免费再换
     */
    private void freeChangeOrder() {
        if (currentRearCount == 0 && currentFontCount == 0) {
            Toast.makeText(TireChangeActivity.this, "请选择轮胎数量", Toast.LENGTH_SHORT).show();
            return;
        }
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
            if (oneReasonCheck && twoReasonCheck) {
                jsonObject.put("reason", 3);
            } else if (oneReasonCheck && !twoReasonCheck) {
                jsonObject.put("reason", 1);
            } else if (!oneReasonCheck && twoReasonCheck) {
                jsonObject.put("reason", 2);
            }

        } catch (JSONException e) {
        }
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "addUserFreeChangeOrder");
        Log.e(TAG, "initOrderFromService: -++-" + jsonObject.toString());
        params.addBodyParameter("reqJson", jsonObject.toString());
        String token = new DbConfig(this).getToken();
        params.addParameter("token", token);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess: " + result);
                JSONObject jsonObject1 = null;
                try {
                    jsonObject1 = new JSONObject(result);
                    String status = jsonObject1.getString("status");
                    String msg = jsonObject1.getString("msg");
                    if (status.equals("1")) { //添加成功
                        Intent intent = new Intent(getApplicationContext(), OrderActivity.class);
                        intent.putExtra(OrderFragment.ORDER_TYPE, "ALL");
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(TireChangeActivity.this, msg, Toast.LENGTH_SHORT).show();
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
     * 首次更换
     */
    private void postChangeOrder() {
        if (currentRearCount == 0 && currentFontCount == 0) {
            Toast.makeText(TireChangeActivity.this, "请选择轮胎数量", Toast.LENGTH_SHORT).show();
            return;
        }
        if (shop == null){
            Toast.makeText(TireChangeActivity.this, "请选择门店", Toast.LENGTH_SHORT).show();
            return;
        }
        if (currentChangeType == 0) {//首次更换
            User user = new DbConfig(this).getUser();
            int userId = user.getId();
            int userCarId = user.getCarId();
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("storeId", shop.getStoreId());
                jsonObject.put("userCarId", userCarId);
                jsonObject.put("userId", userId);
                jsonObject.put("fontAmount", currentFontCount);
                jsonObject.put("rearAmount", currentRearCount);
                jsonObject.put("fontRearFlag", fontRearFlag);
                jsonObject.put("orderType", 2);
            } catch (JSONException e) {
            }
            RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "addFirstChangeShoeOrder");
            Log.e(TAG, "initOrderFromService: -++-" + jsonObject.toString());
            params.addBodyParameter("reqJson", jsonObject.toString());
            String token = new DbConfig(this).getToken();
            params.addParameter("token", token);
            x.http().post(params, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    Log.e(TAG, "onSuccess: " + result);
                    JSONObject jsonObject1 = null;
                    try {
                        jsonObject1 = new JSONObject(result);
                        String status = jsonObject1.getString("status");
                        String msg = jsonObject1.getString("msg");
                        if (status.equals("1")) { //添加成功
                            Intent intent = new Intent(getApplicationContext(), OrderActivity.class);
                            intent.putExtra(OrderFragment.ORDER_TYPE, "ALL");
                            startActivity(intent);
                            finish();
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
        } else {
        }
    }

    private void initAmountView() {
        if (currentChangeType == 0) {//首次更换
            if (fontRearFlag == 0) {//前后轮一致
                if (fontAvaliableAmount >= 4) { //当轮胎数大于4时 最大选择两个
                    fontAmountView.setGoods_storage(2);
                    rearAmountView.setGoods_storage(2);
                    fontMaxCount = 2;
                    rearMaxCount = 2;
                } else { //当轮胎数小于4时  前后轮加起来不能超过轮胎数   没达到轮胎数时  设置最大选择两个
                    fontMaxCount = 2;
                    rearMaxCount = 2;
                    fontAmountView.setGoods_storage(2);
                    rearAmountView.setGoods_storage(2);
                    if (currentFontCount + currentRearCount == fontAvaliableAmount) {        //达到最大轮胎数时 设置为最大数
                        fontAmountView.setGoods_storage(currentFontCount);
                        rearAmountView.setGoods_storage(currentRearCount);
                        fontMaxCount = currentFontCount;
                        rearMaxCount = currentRearCount;
                    }
                }
            } else {     //前后轮不一致
                if (fontAvaliableAmount > 2) {
                    fontMaxCount = 2;
                    fontAmountView.setGoods_storage(2);
                } else {
                    fontMaxCount = fontAvaliableAmount;
                    fontAmountView.setGoods_storage(fontAvaliableAmount);
                }

                if (rearAvaliableAmount > 2) {
                    rearMaxCount = 2;
                    rearAmountView.setGoods_storage(2);
                } else {
                    rearMaxCount = rearAvaliableAmount;
                    rearAmountView.setGoods_storage(rearAvaliableAmount);
                }

            }

        } else { //免费再换
            if (fontRearFlag == 0) { //前后轮一致
                fontMaxCount = 2;
                rearMaxCount = 2;
                if (fontFreeAmount == 4) {   //一共四条轮胎
                    fontAmountView.setGoods_storage(2);
                    rearAmountView.setGoods_storage(2);
                } else {     //不足四条轮胎

                    fontAmountView.setGoods_storage(2);
                    rearAmountView.setGoods_storage(2);
                    if (currentFontCount + currentRearCount == fontFreeAmount) {        //达到最大轮胎数时 设置为最大数
                        fontAmountView.setGoods_storage(currentFontCount);
                        rearAmountView.setGoods_storage(currentRearCount);
                        fontMaxCount = currentFontCount;
                        rearMaxCount = currentRearCount;
                    }
                }
            } else {     //前后轮不一致
                fontMaxCount = fontFreeAmount;
                rearMaxCount = rearFreeAmount;
                fontAmountView.setGoods_storage(fontFreeAmount);
                rearAmountView.setGoods_storage(rearFreeAmount);
            }

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == CHOOSE_SHOP) {
            Bundle bundle = data.getExtras();
            shop = ((Shop) bundle.getSerializable("shop"));
            shopChooseView.setValue(shop.getStoreName(), shop.getStoreImage(), shop.getStoreAddress(), shop.getStoreDistence(), shop.getServiceTypeList(), mInflater);

            Log.e(TAG, "onActivityResult: " + shop.getStoreId());
            Log.e(TAG, "onActivityResult: " + shop.getStoreName());
        }
    }
}
