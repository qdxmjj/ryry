package com.ruyiruyi.ruyiruyi.ui.activity;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.ruyiruyi.ruyiruyi.R;
import com.ruyiruyi.ruyiruyi.db.DbConfig;
import com.ruyiruyi.ruyiruyi.ui.activity.base.RyBaseActivity;
import com.ruyiruyi.ruyiruyi.ui.multiType.Button;
import com.ruyiruyi.ruyiruyi.ui.multiType.ButtonViewBinder;
import com.ruyiruyi.ruyiruyi.ui.multiType.Coupon;
import com.ruyiruyi.ruyiruyi.ui.multiType.CouponViewBinder;
import com.ruyiruyi.ruyiruyi.ui.multiType.Empty;
import com.ruyiruyi.ruyiruyi.ui.multiType.EmptyBig;
import com.ruyiruyi.ruyiruyi.ui.multiType.EmptyBigViewBinder;
import com.ruyiruyi.ruyiruyi.ui.multiType.EmptyViewBinder;
import com.ruyiruyi.ruyiruyi.ui.multiType.GoodsNew;
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

import me.drakeet.multitype.MultiTypeAdapter;
import rx.functions.Action1;

import static me.drakeet.multitype.MultiTypeAsserts.assertAllRegistered;
import static me.drakeet.multitype.MultiTypeAsserts.assertHasTheSameAdapter;

public class CouponActivity extends RyBaseActivity implements ButtonViewBinder.OnButtonClick,CouponViewBinder.OnCouponClick {
    private static final String TAG = CouponActivity.class.getSimpleName();
    public static String FROM_TYPE = "FROM_TYPE";   //0查看  1使用
    public static String CHOOSE_TYPE = "CHOOSE_TYPE";   //0默认  1精致洗车 2四轮定位 3洗车定位 5其他商品 使用代金券
    public static String CAR_ID = "CAR_ID";   //0查看  1使用
    private ActionBar actionBar;
    private FrameLayout couponLayout;
    private FrameLayout oldCouponLayout;
    private View couponView;
    private View oldCouponView;
    public int couponType = 0;  //0是可用  1是历史
    private TextView couponText;
    private TextView oldCouponText;
    private RecyclerView listView;
    private List<Object> items = new ArrayList<>();
    private MultiTypeAdapter adapter;
    public List<Coupon> couponList;
    public List<Coupon> useCouponList;
    public List<Coupon> noUseCouponList;
    public List<Coupon> oldCouponList;
    private int fromType;
    private int carId = 0;
    private SwipeRefreshLayout swipeRefreshLayout;
    private int chooseType;
    private List<GoodsNew> goodslist;
    private int store_id = 0;
    private double goodsPrices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon);

        actionBar = (ActionBar) findViewById(R.id.my_action);
        actionBar.setTitle("我的优惠券");;
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
        Bundle bundle = intent.getExtras();
        fromType = bundle.getInt(FROM_TYPE,0);

        if (fromType == 0){

        }else {
            carId = new DbConfig(this).getUser().getCarId();
            goodslist = ((List<GoodsNew>) bundle.getSerializable("GOODSLIST"));
            store_id = bundle.getInt("STORE_ID",0);
            goodsPrices = bundle.getDouble("GOODS_PRICES",0.00);
            Log.e(TAG, "onCreate: " + store_id);
        }

        chooseType = intent.getIntExtra(CHOOSE_TYPE,0);


        couponList = new ArrayList<>();
        oldCouponList = new ArrayList<>();
        useCouponList = new ArrayList<>();
        noUseCouponList = new ArrayList<>();

        initView();
        initDataFromService();
    }

    private void initDataFromService() {
        int userId = new DbConfig(this).getId();
        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("userId", userId);
        } catch (JSONException e) {
        }
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "preferentialInfo/selectsUserCoupons");
        params.addBodyParameter("reqJson", jsonObject.toString());
        String token = new DbConfig(this).getToken();
        params.addParameter("token", token);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess: " + result );
                JSONObject jsonObject1 = null;
                try {
                    jsonObject1 = new JSONObject(result);
                    String status = jsonObject1.getString("status");
                    String msg = jsonObject1.getString("msg");
                    if (status.equals("1")){
                        JSONObject data = jsonObject1.getJSONObject("data");
                        JSONArray availableList = data.getJSONArray("availableList");
                        couponList.clear();
                        useCouponList.clear();
                        noUseCouponList.clear();
                        Log.e(TAG, "onSuccess: chooseType:" + chooseType);
                        for (int i = 0; i < availableList.length(); i++) {
                            JSONObject object = availableList.getJSONObject(i);
                            int id = object.getInt("id");
                            int userCarId = object.getInt("userCarId");
                            int type = object.getInt("type");
                            int viewTypeId = object.getInt("viewTypeId");
                            int couponStatus = object.getInt("status");
                            String couponName = object.getString("couponName");
                            String startTime = object.getString("startTime");
                            String endTime = object.getString("endTime");
                            String platNumber = object.getString("platNumber");
                            String moneyFull = "";
                            String moneyMinus = "";
                            String needPay = "";
                            String deduction  = "";
                            try {
                                moneyFull = object.getString("moneyFull");
                                moneyMinus = object.getString("noneyMinus");
                                needPay = object.getString("needPay");
                                deduction = object.getString("deduction");
                            }catch (Exception e){

                            }


                            List<String> storeNameList = new ArrayList<String>();
                            //获取限制的门店名称
                            try {
                                JSONArray storesName = object.getJSONArray("storesName");
                                for (int j = 0; j < storesName.length(); j++) {
                                    String string = storesName.getString(j);
                                    Log.e(TAG, "onSuccess:-------- " + string);
                                    storeNameList.add(string);
                                }
                            }catch (Exception e){

                            }

                            //获取限制的门店id
                            String storeIdList = object.getString("storeIdList");
                            String store[]=storeIdList.split(",");
                            for (int j = 0; j < store.length; j++) {
                                String s = store[j];
                                Log.e(TAG, "onSuccess: +++++++" + s);
                            }

                            try {
                                //获取限制的区域名称
                                JSONArray positionsName = object.getJSONArray("positionsName");
                                for (int j = 0; j < positionsName.length(); j++) {
                                    String string = positionsName.getString(j);

                                    Log.e(TAG, "onSuccess: " + string);
                                }
                            }catch (Exception e){

                            }


                            //获取限制的区域id
                            String positionIdList = object.getString("positionIdList");
                            String posiyion[]=positionIdList.split(",");
                            for (int j = 0; j < posiyion.length; j++) {
                                String s = posiyion[j];
                                Log.e(TAG, "onSuccess: +++++++" + s);
                            }
                            //获取适用的商品名称
                            String goodsName = object.getString("rule");

                            if (fromType == 0){//查看优惠券
                                Coupon coupon = new Coupon(id, couponName, type, viewTypeId, couponStatus, startTime, endTime,platNumber,true,goodsName);
                                useCouponList.add(coupon);

                            }else { //使用优惠券

                                if (type == 2){  //现金券
                                    Coupon coupon = new Coupon(id, couponName, type, viewTypeId, couponStatus, startTime, endTime,platNumber,true,goodsName);
                                    useCouponList.add(coupon);
                                }else {
                                    if (storeIdList.equals("")) {  //优惠券为空不限制门店
                                        if (type == 3){     //满减券
                                            if (goodsPrices > Double.parseDouble(moneyFull)){       //符合满减规则
                                                Coupon coupon = new Coupon(id, couponName, type, viewTypeId, couponStatus, startTime, endTime,platNumber,true,goodsName,moneyFull,moneyMinus,needPay,deduction);
                                                useCouponList.add(coupon);
                                            }else {             //不符合满减规则
                                                Coupon coupon = new Coupon(id, couponName, type, viewTypeId, couponStatus, startTime, endTime,platNumber,false,goodsName,moneyFull,moneyMinus,needPay,deduction);
                                                noUseCouponList.add(coupon);
                                            }
                                        }else { //全额券  小额券 抵扣券
                                            //是否含有可用优惠券的商品
                                            boolean ishasGoods = false;
                                            for (int j = 0; j < goodslist.size(); j++) {
                                                //选择的商品中包括可使用优惠券的商品
                                                if (goodslist.get(j).getGoodsName().equals(goodsName)) {
                                                    ishasGoods = true;

                                                }
                                            }
                                            if (ishasGoods  && userCarId == carId ){
                                                Coupon coupon = new Coupon(id, couponName, type, viewTypeId, couponStatus, startTime, endTime,platNumber,true,goodsName);
                                                useCouponList.add(coupon);
                                            }else {
                                                Coupon coupon = new Coupon(id, couponName, type, viewTypeId, couponStatus, startTime, endTime,platNumber,false,goodsName);
                                                noUseCouponList.add(coupon);
                                            }
                                        }
                                    }else {     //优惠券限制门店
                                        if (type == 3){     //满减券
                                            if (goodsPrices > Double.parseDouble(moneyFull)){       //符合满减规则
                                                Coupon coupon = new Coupon(id, couponName, type, viewTypeId, couponStatus, startTime, endTime,platNumber,true,storeNameList,goodsName,moneyFull,moneyMinus,needPay,deduction);
                                                useCouponList.add(coupon);
                                            }else {             //不符合满减规则
                                                Coupon coupon = new Coupon(id, couponName, type, viewTypeId, couponStatus, startTime, endTime,platNumber,false,storeNameList,goodsName,moneyFull,moneyMinus,needPay,deduction);
                                                noUseCouponList.add(coupon);
                                            }
                                        }else {
                                            boolean hasShop = false;
                                            for (int j = 0; j < store.length; j++) {
                                                String s = store[j];
                                                if (Integer.parseInt(s) == store_id) {
                                                    hasShop = true;
                                                }
                                                Log.e(TAG, "onSuccess: +++++++" + s);
                                            }
                                            if (hasShop){       //该门店可用优惠券
                                                Coupon coupon = new Coupon(id, couponName, type, viewTypeId, couponStatus, startTime, endTime,platNumber,true,storeNameList,goodsName);
                                                useCouponList.add(coupon);
                                            }else { //该门店不可用优惠券
                                                Coupon coupon = new Coupon(id, couponName, type, viewTypeId, couponStatus, startTime, endTime,platNumber,false,storeNameList,goodsName);
                                                noUseCouponList.add(coupon);
                                            }
                                        }


                                    }

                                }
                            }
                        }
                        Log.e(TAG, "onSuccess: -1-" + useCouponList.size());
                        Log.e(TAG, "onSuccess: -2-" + noUseCouponList.size());
                        useCouponList.addAll(noUseCouponList);

                        oldCouponList.clear();
                        JSONArray unusableList = data.getJSONArray("unusableList");
                        for (int i = 0; i < unusableList.length(); i++) {
                            JSONObject object = unusableList.getJSONObject(i);
                            int id = object.getInt("id");
                            int type = object.getInt("type");
                            int viewTypeId = object.getInt("viewTypeId");
                            int couponStatus = object.getInt("status");
                            String couponName = object.getString("couponName");
                            String startTime = object.getString("startTime");
                            String endTime = object.getString("endTime");
                            String platNumber = object.getString("platNumber");
                            //获取适用的商品名称
                            String goodsName = object.getString("rule");
                            String moneyFull = "";
                            String moneyMinus = "";
                            String needPay = "";
                            String deduction  = "";
                            try {
                                moneyFull = object.getString("moneyFull");
                                moneyMinus = object.getString("noneyMinus");
                                needPay = object.getString("needPay");
                                deduction = object.getString("deduction");
                            }catch (Exception e){

                            }

                         /*   JSONArray storesName = object.getJSONArray("storesName");
                            for (int j = 0; j < storesName.length(); j++) {
                                String string = storesName.getString(j);
                            }*/
                            Coupon coupon = new Coupon(id, couponName, type, viewTypeId, couponStatus, startTime, endTime,platNumber,false,goodsName,moneyFull,moneyMinus,needPay,deduction);
                            oldCouponList.add(coupon);
                        }

                        initData();
                    } else if (status.equals("-999")) {
                        showUserTokenDialog("您的账号在其它设备登录,请重新登录");
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
/*
        couponList.clear();
        for (int i = 0; i < 10; i++) {
            if (10 % 2 == 0) {
                Coupon coupon = new Coupon(i, "精致洗车券", 1, 2, 2, "2018.05.05", "2019.05.05", "鲁F 2056D");
                couponList.add(coupon);
            }else {
                Coupon coupon = new Coupon(i,"四轮定位",1,3,2,"2018.05.05","2019.05.05","鲁F 2056D");
                couponList.add(coupon);
            }
        }
        Coupon coupon = new Coupon(11,"10元现金券",2,7,2,"2018.05.05","2019.05.05","鲁F 2056D");
        couponList.add(coupon);

        oldCouponList.clear();
        for (int i = 0; i < 10; i++) {
            if (10 / 2 == 0) {
                Coupon oldcoupon = new Coupon(i, "精致洗车券", 1, 2, 1, "2018.05.05", "2019.05.05", "鲁F 2056D");
                oldCouponList.add(oldcoupon);
            }else {
                Coupon oldcoupon = new Coupon(i,"四轮定位",1,3,3,"2018.05.05","2019.05.05","鲁F 2056D");
                oldCouponList.add(oldcoupon);
            }
        }
        Coupon oldcoupon = new Coupon(11,"10元现金券",2,7,3,"2018.05.05","2019.05.05","鲁F 2056D");
        oldCouponList.add(oldcoupon);*/


    }

    private void initData() {
        Log.e(TAG, "initData: size = " + useCouponList.size() );
        items.clear();
        if (fromType == 1){
            items.add(new Button());
        }
        if (couponType == 0){       //可用
            for (int i = 0; i < useCouponList.size(); i++) {
                items.add(useCouponList.get(i));
            }
        }else {     //历史
            for (int i = 0; i < oldCouponList.size(); i++) {
                items.add(oldCouponList.get(i));
            }
        }
        if (items.size() == 0){
            items.add(new EmptyBig());
        }
        assertAllRegistered(adapter, items);
        adapter.notifyDataSetChanged();

    }

    private void initView() {
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.coupon_refresh_layout);
        swipeRefreshLayout.setProgressViewEndTarget(true, 200);

        couponLayout = (FrameLayout) findViewById(R.id.coupon_layout);
        oldCouponLayout = (FrameLayout) findViewById(R.id.old_coupon_layout);
        couponView = (View) findViewById(R.id.coupon_view);
        oldCouponView = (View) findViewById(R.id.old_coupon_view);
        couponText = (TextView) findViewById(R.id.coupon_text);
        oldCouponText = (TextView) findViewById(R.id.old_coupon_text);
        listView = (RecyclerView) findViewById(R.id.coupon_listview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        listView.setLayoutManager(linearLayoutManager);
        adapter = new MultiTypeAdapter(items);
        register();

        listView.setAdapter(adapter);
        assertHasTheSameAdapter(listView, adapter);

        initCouponView();

        //下拉刷新
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //刷新处理 ;
              //  initDataFromService();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        RxViewAction.clickNoDouble(couponLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        couponType = 0;
                        initCouponView();
                        initData();
                    }
                });
        RxViewAction.clickNoDouble(oldCouponLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        couponType = 1;
                        initCouponView();
                        initData();
                    }
                });
    }

    private void register() {
        ButtonViewBinder buttonViewBinder = new ButtonViewBinder();
        buttonViewBinder.setListener(this);
        adapter.register(Button.class, buttonViewBinder);
        CouponViewBinder couponViewBinder = new CouponViewBinder();
        couponViewBinder.setListener(this);
        adapter.register(Coupon.class, couponViewBinder);
        adapter.register(Empty.class, new EmptyViewBinder());
        adapter.register(EmptyBig.class, new EmptyBigViewBinder());
    }

    private void initCouponView() {
        if (couponType == 0){
            couponView.setVisibility(View.VISIBLE);
            oldCouponView.setVisibility(View.GONE);
            couponText.setTextColor(getResources().getColor(R.color.theme_primary));
            oldCouponText.setTextColor(getResources().getColor(R.color.c6));
        }else {
            couponView.setVisibility(View.GONE);
            oldCouponView.setVisibility(View.VISIBLE);
            couponText.setTextColor(getResources().getColor(R.color.c6));
            oldCouponText.setTextColor(getResources().getColor(R.color.theme_primary));
        }
    }

    /**
     * 暂不使用优惠券得点击回调
     */
    @Override
    public void onButtonClickListener() {
        Intent intent = new Intent();
        intent.putExtra("COUPONCHOOSE",0);
        setResult(OrderGoodsAffirmActivity.COUPON_REQUEST,intent);
        finish();
    }

    /**
     * 优惠券点击使用
     * @param couponId
     * @param couponName
     */
    @Override
    public void onCouponClcikListener(int couponId, String couponName,String goodsName,int couponType,String moneyFull,String moneyMinus,String needPay,String deduction) {
        Intent intent = new Intent();
        intent.putExtra("COUPONCHOOSE",1);
        intent.putExtra("COUPONID",couponId);
        intent.putExtra("COUPONNAME",couponName);
        intent.putExtra("GOODS_NAME",goodsName);
        intent.putExtra("COUPON_TYPE",couponType);
        intent.putExtra("MONEY_FULL",moneyFull);
        intent.putExtra("MONEY_MINUS",moneyMinus);
        intent.putExtra("NEED_PAY",needPay);
        intent.putExtra("DEDUCTION",deduction);
        setResult(OrderGoodsAffirmActivity.COUPON_REQUEST,intent);
        finish();
    }
}
