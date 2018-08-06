package com.ruyiruyi.ruyiruyi.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.ruyiruyi.ruyiruyi.MainActivity;
import com.ruyiruyi.ruyiruyi.R;
import com.ruyiruyi.ruyiruyi.db.DbConfig;
import com.ruyiruyi.ruyiruyi.ui.activity.base.RyBaseActivity;
import com.ruyiruyi.ruyiruyi.ui.multiType.Code;
import com.ruyiruyi.ruyiruyi.ui.multiType.CodeViewBinder;
import com.ruyiruyi.ruyiruyi.ui.multiType.CountOne;
import com.ruyiruyi.ruyiruyi.ui.multiType.CountOneViewBinder;
import com.ruyiruyi.ruyiruyi.ui.multiType.CxwyOrder;
import com.ruyiruyi.ruyiruyi.ui.multiType.CxwyOrderViewBinder;
import com.ruyiruyi.ruyiruyi.ui.multiType.GoodsInfo;
import com.ruyiruyi.ruyiruyi.ui.multiType.GoodsInfoViewBinder;
import com.ruyiruyi.ruyiruyi.ui.multiType.InfoOne;
import com.ruyiruyi.ruyiruyi.ui.multiType.InfoOneViewBinder;
import com.ruyiruyi.ruyiruyi.ui.multiType.TireInfo;
import com.ruyiruyi.ruyiruyi.ui.multiType.TireInfoViewBinder;
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

public class PendingOrderActivity extends RyBaseActivity implements InfoOneViewBinder.OnInfoItemClick {
    private static final String TAG = PendingOrderActivity.class.getSimpleName();
    private ActionBar actionBar;
    private String orderno;
    private TextView goPayButton;
    private int orderType;
    private RecyclerView listView;
    private List<Object> items = new ArrayList<>();
    private MultiTypeAdapter adapter;
    public List<GoodsInfo> goodsInfoList;
    private String storeName;
    private String userPhone;
    private String userName;
    private String storeId;
    private String carNumber;
    private String orderTotalPrice;
    private String orderImg;
    private int orderFrom;
    public TireInfo tireInfo;
    public CxwyOrder cxwyOrder;
    public List<TireInfo> tireInfoList;
    public List<String> codeList;
    public List<String> oldCodeList;
    public List<TireInfo> buchaTireList;
    private int userCarId;
    private String saleName;
    private String orderActuallyPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_order, R.id.my_action);
        actionBar = (ActionBar) findViewById(R.id.my_action);
        actionBar.setTitle("待支付");
        actionBar.setRightView("取消订单");
        actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(int var1) {
                switch ((var1)) {
                    case -1:
                        onBackPressed();
                        break;
                    case -3:

                        if (orderType == 0){
                            cancleTireOrder();
                        }  else {
                            cancleOrder();
                        }

                        break;
                }
            }
        });


        goodsInfoList = new ArrayList<>();
        tireInfoList = new ArrayList<>();
        codeList = new ArrayList<>();
        oldCodeList = new ArrayList<>();
        buchaTireList = new ArrayList<>();
        Intent intent = getIntent();
        orderno = intent.getStringExtra(PaymentActivity.ORDERNO);
        orderType = intent.getIntExtra(PaymentActivity.ORDER_TYPE, 0);
        if (orderType == 99){
            orderType = 0;
        }
        orderFrom = intent.getIntExtra(PaymentActivity.ORDER_FROM, 0);

        initView();
        initOrderFromService();
    }

    /**
     * 取消轮胎订单
     */
    private void cancleTireOrder() {
        int userId = new DbConfig(this).getId();
        JSONObject jsonObject = new JSONObject();
        Log.e(TAG, "initOrderFromService:--- " + orderType);
        try {
            jsonObject.put("orderNo", orderno);
            jsonObject.put("userId", userId);
        } catch (JSONException e) {
        }
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "cancelShoeCxwyOrder");
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
                    if (status.equals("1")){
                        Toast.makeText(PendingOrderActivity.this, "取消订单成功", Toast.LENGTH_SHORT).show();
                        if (orderFrom == 0) {
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            finish();
                        } else {
                            finish();
                        }

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
     * 取消商品订单
     */
    private void cancleOrder() {
        int userId = new DbConfig(this).getId();
        JSONObject jsonObject = new JSONObject();
        Log.e(TAG, "initOrderFromService:--- " + orderType);
        try {
            jsonObject.put("orderNo", orderno);
            jsonObject.put("userId", userId);
        } catch (JSONException e) {
        }
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "cancelStockOrder");
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
                    if (status.equals("1")){
                        Toast.makeText(PendingOrderActivity.this, "取消订单成功", Toast.LENGTH_SHORT).show();
                        if (orderFrom == 0) {
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            finish();
                        } else {
                            finish();
                        }
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

    private void initOrderFromService() {
        int userId = new DbConfig(this).getId();
        JSONObject jsonObject = new JSONObject();
        Log.e(TAG, "initOrderFromService:--- " + orderType);
        try {
            jsonObject.put("orderNo", orderno);
            jsonObject.put("orderType", orderType);
            jsonObject.put("userId", userId);
        } catch (JSONException e) {
        }
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "getUserOrderInfoByNoAndType");
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
                    if (orderType == 0) { //轮胎订单
                        jsonObject1 = new JSONObject(result);
                        String status = jsonObject1.getString("status");
                        String msg = jsonObject1.getString("msg");
                        if (status.equals("1")) {
                            JSONObject data = jsonObject1.getJSONObject("data");
                            orderImg = data.getString("orderImg");
                            orderTotalPrice = data.getString("orderTotalPrice");
                            orderActuallyPrice = data.getString("orderActuallyPrice");
                            saleName = data.getString("saleName");
                            carNumber = data.getString("platNumber");
                            userName = data.getString("userName");
                            userPhone = data.getString("userPhone");
                            JSONArray array = data.getJSONArray("shoeOrderVoList");
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject object = array.getJSONObject(i);
                                String cxwyAmount = object.getString("cxwyAmount");
                                String cxwyPrice = object.getString("cxwyPrice");
                                String cxwyTotalPrice = object.getString("cxwyTotalPrice");
                                String fontRearFlag = object.getString("fontRearFlag");
                                String tireName = "";
                                String tireCount = "";
                                Double tirePrice = 0.00;
                                Double totalPrice = 0.00;
                                if (fontRearFlag.equals("2")) { //后轮
                                    tireName = object.getString("rearShoeName");
                                    tireCount = object.getString("rearAmount");
                                    tirePrice = object.getDouble("rearPrice");
                                    totalPrice = object.getDouble("rearTotalPrice");
                                } else { //前轮 或  前后轮
                                    tireName = object.getString("fontShoeName");
                                    tireCount = object.getString("fontAmount");
                                    tirePrice = object.getDouble("fontPrice");
                                    totalPrice = object.getDouble("fontTotalPrice");
                                }
                                tireInfo = new TireInfo(orderImg, tireName, Integer.parseInt(tireCount), tirePrice, fontRearFlag);
                                if (Integer.parseInt(cxwyAmount) > 0) {
                                    cxwyOrder = new CxwyOrder(Integer.parseInt(cxwyAmount), cxwyPrice);
                                }
                            }
                            initData();
                        } else if (status.equals("-999")) {
                            showUserTokenDialog("您的账号在其它设备登录,请重新登录");
                        } else {
                            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                        }
                    } else if (orderType == 1) { //商品服务订单
                        jsonObject1 = new JSONObject(result);
                        String status = jsonObject1.getString("status");
                        String msg = jsonObject1.getString("msg");
                        if (status.equals("1")) {
                            JSONObject data = jsonObject1.getJSONObject("data");
                            orderImg = data.getString("orderImg");
                            orderTotalPrice = data.getString("orderTotalPrice");
                            orderActuallyPrice = data.getString("orderActuallyPrice");
                            saleName = data.getString("saleName");
                            carNumber = data.getString("platNumber");
                            storeId = data.getString("storeId");
                            storeName = data.getString("storeName");
                            userName = data.getString("userName");
                            userPhone = data.getString("userPhone");
                            JSONArray array = data.getJSONArray("stockOrderVoList");
                            goodsInfoList.clear();
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject object = array.getJSONObject(i);
                                int amount = object.getInt("amount");
                                String detailImage = object.getString("detailImage");
                                String detailName = object.getString("detailName");
                                String detailPrice = object.getString("detailPrice");
                                GoodsInfo goodsInfo = new GoodsInfo();
                                goodsInfo.setCurrentCount(amount);
                                goodsInfo.setGoodsImage(detailImage);
                                goodsInfo.setGoodsPrice(detailPrice);
                                goodsInfo.setGoodsName(detailName);
                                goodsInfoList.add(goodsInfo);
                            }
                            initData();
                        } else {
                        }
                    }else if (orderType == 6){
                        jsonObject1 = new JSONObject(result);
                        String status = jsonObject1.getString("status");
                        String msg = jsonObject1.getString("msg");
                        if (status.equals("1")) {
                            JSONObject data = jsonObject1.getJSONObject("data");
                            orderImg = data.getString("orderImg");
                            orderTotalPrice = data.getString("orderTotalPrice");
                            carNumber = data.getString("platNumber");
                            storeId = data.getString("storeId");
                            storeName = data.getString("storeName");
                            userName = data.getString("userName");
                            userPhone = data.getString("userPhone");
                            initData();
                           /* JSONObject data = jsonObject1.getJSONObject("data");
                            getFreeTireOrderInfo(data);
                            getFreeTireOrderCode(data);
                            getFreeTireOrderOldCode(data);
                            initData();*/
                        }



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
     * 免费再换
     * @param data
     * @throws JSONException
     */
    private void getFreeTireOrderInfo(JSONObject data) throws JSONException {
        orderImg = data.getString("orderImg");
        orderTotalPrice = data.getString("orderTotalPrice");
        carNumber = data.getString("platNumber");
        storeId = data.getString("storeId");
        storeName = data.getString("storeName");
        userName = data.getString("userName");
        userPhone = data.getString("userPhone");
        JSONArray array = data.getJSONArray("freeChangeOrderVoList");
        tireInfoList.clear();
        for (int i = 0; i < array.length(); i++) {
            JSONObject object = array.getJSONObject(i);
            String shoeName = object.getString("shoeName");
            String shoeImg = object.getString("shoeImg");
            String fontRearFlag = object.getString("fontRearFlag");
            int fontAmount = object.getInt("fontAmount");
            int rearAmount = object.getInt("rearAmount");
            TireInfo tireInfo = null;
            if (fontRearFlag.equals("2")){ //后轮
                tireInfo = new TireInfo(orderImg,shoeName,rearAmount,0.00,fontRearFlag);
            }else if (fontRearFlag.equals("1")){ //前轮
                tireInfo = new TireInfo(orderImg,shoeName,fontAmount,0.00,fontRearFlag);
            }else {//前后轮一致
                tireInfo = new TireInfo(orderImg,shoeName,fontAmount + rearAmount,0.00,fontRearFlag);
            }
            tireInfoList.add(tireInfo);
        }
    }

    /**
     * 获取免费再换新轮胎条形码
     * @param data
     * @throws JSONException
     */
    private void getFreeTireOrderCode(JSONObject data) throws JSONException {
        codeList.clear();
        JSONArray userCarShoeBarCodeList = data.getJSONArray("userCarShoeBarCodeList");
        for (int i = 0; i < userCarShoeBarCodeList.length(); i++) {
            String barCode = userCarShoeBarCodeList.getJSONObject(i).getString("barCode");
            codeList.add(barCode);
        }
    }

    /* 获取免费在换 旧轮胎条形吗
    * @param data
    * @throws JSONException
    */
    private void getFreeTireOrderOldCode(JSONObject data) throws JSONException {
        oldCodeList.clear();
        buchaTireList.clear();
        JSONArray userCarShoeBarOldCodeList = data.getJSONArray("userCarShoeOldBarCodeList");
        for (int i = 0; i < userCarShoeBarOldCodeList.length(); i++) {
            JSONObject object = userCarShoeBarOldCodeList.getJSONObject(i);
            String barCode = object.getString("barCode");
            int stage = object.getInt("stage");
            oldCodeList.add(barCode);

            if (stage == 1){
                String shoeName = object.getString("shoeName");
                String shoeImgUrl = object.getString("shoeImgUrl");
                userCarId = object.getInt("userCarId");
                String fontRearFlag = object.getString("fontRearFlag");
                Double price = object.getDouble("price");
                TireInfo tireInfo =  new TireInfo(shoeImgUrl, shoeName, 1, price, fontRearFlag);
                tireInfo.setBarCode(barCode);
                buchaTireList.add(tireInfo);
            }

        }
    }

    private void initData() {
        items.clear();
        if (orderType == 0) { //轮胎
            items.add(new InfoOne("联系人", userName, false));
            items.add(new InfoOne("联系电话", userPhone, false));
            items.add(new InfoOne("车牌号", carNumber, false));
            if (!saleName.equals("")){
                items.add(new InfoOne("优惠券", saleName, false));
            }
            items.add(new InfoOne("订单总价", "￥" + orderTotalPrice, true));

            if (tireInfo.getTireCount() > 0){
                items.add(tireInfo);
            }
            if (cxwyOrder != null) {
                items.add(cxwyOrder);
            }
        } else if (orderType == 1) { //商品
            items.add(new InfoOne("联系人", userName, false));
            items.add(new InfoOne("联系电话", userPhone, false));
            if (!saleName.equals("")){
                items.add(new InfoOne("优惠券", saleName, false));
            }
            items.add(new InfoOne("订单总价", "￥" + orderActuallyPrice, false));
            items.add(new InfoOne("店铺名称", storeName, true, true));
            for (int i = 0; i < goodsInfoList.size(); i++) {
                items.add(goodsInfoList.get(i));
            }
        }else if (orderType == 6){  //补差订单
            items.add(new InfoOne("联系人", userName, false));
            items.add(new InfoOne("联系电话", userPhone, false));
            items.add(new InfoOne("车牌号", carNumber, false));
            items.add(new InfoOne("服务项目", "轮胎补差订单", false));
            items.add(new InfoOne("订单总价", "￥" + orderTotalPrice, true));
            /*for (int i = 0; i < tireInfoList.size(); i++) {
                items.add(tireInfoList.get(i));
            }
            items.add(new InfoOne("新轮胎条码", "", false));
            for (int i = 0; i < codeList.size(); i++) {
                items.add(new Code(codeList.get(i)));
            }
            items.add(new InfoOne("旧轮胎条码", "", false));
            for (int i = 0; i < oldCodeList.size(); i++) {
                items.add(new Code(oldCodeList.get(i)));
            }
            items.add(new InfoOne("需要补差得轮胎", "", false));
            List<Double> priceList= new ArrayList<>();*/
           /* for (int i = 0; i < buchaTireList.size(); i++) {
                items.add(buchaTireList.get(i));
                currentPrice = currentPrice  + buchaTireList.get(i).getTirePrice();
                priceList.add(buchaTireList.get(i).getTirePrice());
            }

            if (cxwyCount > buchaTireList.size()){  //判断畅行无忧得数量 跟轮胎得数量
                items.add(new CountOne(buchaTireList.size(),currentCxwyCount,priceList));
            }else {
                items.add(new CountOne(cxwyCount,currentCxwyCount,priceList));
            }*/
        }
        assertAllRegistered(adapter, items);
        adapter.notifyDataSetChanged();
    }

    private void initView() {
        goPayButton = (TextView) findViewById(R.id.go_pay_button);
        listView = (RecyclerView) findViewById(R.id.pending_order_listview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        listView.setLayoutManager(linearLayoutManager);
        adapter = new MultiTypeAdapter(items);
        register();

        listView.setAdapter(adapter);
        assertHasTheSameAdapter(listView, adapter);

        RxViewAction.clickNoDouble(goPayButton)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Intent intent = new Intent(getApplicationContext(), PaymentActivity.class);
                        intent.putExtra(PaymentActivity.ALL_PRICE, Double.parseDouble(orderActuallyPrice));
                        intent.putExtra(PaymentActivity.ORDERNO, orderno);
                        intent.putExtra(PaymentActivity.ORDER_TYPE, orderType);
                        startActivity(intent);
                    }
                });
    }

    private void register() {
        adapter.register(GoodsInfo.class, new GoodsInfoViewBinder(this));
        InfoOneViewBinder infoOneViewBinder = new InfoOneViewBinder();
        infoOneViewBinder.setListener(this);
        adapter.register(InfoOne.class, infoOneViewBinder);
        adapter.register(CxwyOrder.class, new CxwyOrderViewBinder());
        adapter.register(TireInfo.class, new TireInfoViewBinder(this));
        adapter.register(Code.class, new CodeViewBinder());
        CountOneViewBinder countOneViewBinder = new CountOneViewBinder(this);
        adapter.register(CountOne.class, countOneViewBinder);
    }

    @Override
    public void onBackPressed() {
        if (orderFrom == 0) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        } else {
            super.onBackPressed();
        }

    }

    @Override
    public void onInfoItemClickListener(String name) {
        if (name.equals("店铺名称")) {
            Log.e(TAG, "onInfoItemClickListener: storeid :" + storeId);
            Intent intent = new Intent(this, ShopHomeActivity.class);
            intent.putExtra("STOREID", Integer.parseInt(storeId));
            startActivity(intent);
        }
    }
}
