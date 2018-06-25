package com.ruyiruyi.ruyiruyi.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ruyiruyi.ruyiruyi.R;
import com.ruyiruyi.ruyiruyi.db.DbConfig;
import com.ruyiruyi.ruyiruyi.db.model.User;
import com.ruyiruyi.ruyiruyi.ui.activity.base.RyBaseActivity;
import com.ruyiruyi.ruyiruyi.ui.fragment.OrderFragment;
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

public class OrderInfoActivity extends RyBaseActivity implements InfoOneViewBinder.OnInfoItemClick,CountOneViewBinder.OnCxwyCountClikc {
    private static final String TAG = OrderInfoActivity.class.getSimpleName();
    private ActionBar actionBar;
    private RecyclerView listView;
    private List<Object> items = new ArrayList<>();
    private MultiTypeAdapter adapter;
    private String orderNo;
    private int orderType;
    private int orderState;
    public List<TireInfo> tireInfoList;
    public List<TireInfo> buchaTireList;
    public List<GoodsInfo> goodsInfoList;
    private String userPhone;
    private String userName;
    private String carNumber;
    private String orderTotalPrice;
    private String orderImg;
    private String storeId;
    private String storeName;
    private TextView orderButton;
    public List<String> codeList;
    public List<String> oldCodeList;
    public List<InfoOne> freeRepairList;
    public TireInfo tireInfo;
    public CxwyOrder cxwyOrder;
    private int orderStage =1;
    private LinearLayout orderBuchaLayout;
    private LinearLayout jujueBuchaLayout;
    private LinearLayout tongyibuchaLayout;
    private int cxwyCount = 0;
    private int currentCxwyCount = 0;
    private int userCarId;
    private Double currentPrice = 0.00;
    private String associationOrderNo;
    private String makeUpDifferencePrice;
    private int usedCxwAmount;
    private String postage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_info);
        actionBar = (ActionBar) findViewById(R.id.my_action);

        actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(int var1) {
                switch ((var1)) {
                    case -1:
                        onBackPressed();
                        break;
                    case -3:
                        if (orderType == 1){
                            goodsTuikuan();
                        }else if (orderType == 0){
                            tireTuikuan();
                        }

                        break;
                }
            }
        });
        tireInfoList = new ArrayList<>();
        goodsInfoList = new ArrayList<>();
        codeList = new ArrayList<>();
        oldCodeList = new ArrayList<>();
        buchaTireList = new ArrayList<>();
        freeRepairList = new ArrayList<>();
        Intent intent = getIntent();
        // OrderType 0:轮胎购买订单 1:普通商品购买订单 2:首次更换订单 3:免费再换订单 4:轮胎修补订单
        //轮胎订单状态(orderType:0) :1 已安装 2 待服务 3 支付成功 4 支付失败 5 待支付 6 已退货
        //订单状态(orderType:1 2 3 4 ): 1 交易完成 2 待收货 3 待商家确认服务 4 作废 5 待发货 6 待车主确认服务 7 待评价 8 待支付
        //orderStage:订单二段状态 1 默认(不需要支付差价)  2 待车主支付差价 3 已支付差价 4 待车主支付运费 5 已支付运费
        orderNo = intent.getStringExtra(PaymentActivity.ORDERNO);
        orderType = intent.getIntExtra(PaymentActivity.ORDER_TYPE, 0);
        orderState = intent.getIntExtra(PaymentActivity.ORDER_STATE, 0);
        if (orderType!=0){
            orderStage = intent.getIntExtra(PaymentActivity.ORDER_STAGE,1);
        }
        Log.e(TAG, "onCreate: ----*-----" + orderStage);
        if (orderType == 0){//轮胎订单状态(orderType:0) :1 已安装 2 待服务 3 支付成功 4 支付失败 5 待支付 6 已退货 7退款中 8是已退款 9作废
            if (orderState == 3){
                actionBar.setTitle("交易完成");
            }else if (orderState == 6){
                actionBar.setTitle("已退货");
            }else if (orderState == 7){
                actionBar.setTitle("退款中");
            }else if (orderState == 8){
                actionBar.setTitle("已退款");
            }else if (orderState == 9){
                actionBar.setTitle("作废");
            }
        }else {
            if (orderStage ==1){
                if (orderState == 5) {
                    actionBar.setTitle("待发货");
                } else if (orderState == 2) {
                    actionBar.setTitle("待收货");
                } else if (orderState == 3) {
                    actionBar.setTitle("待商家确认服务");
                } else if (orderState == 6) {
                    actionBar.setTitle("确认服务");
                }else if (orderState == 1) {
                    actionBar.setTitle("交易完成");
                }else if (orderState == 9){
                    actionBar.setTitle("退款中");
                }else if (orderState == 10){
                    actionBar.setTitle("退款成功");
                }else if (orderState == 4){
                    actionBar.setTitle("作废");
                }
            }else if (orderStage == 2){
                actionBar.setTitle("待车主补差");
            }else if (orderStage == 3){
                actionBar.setTitle("已补差");
            }else if (orderStage == 4){
                actionBar.setTitle("待支付邮费");
            }else if (orderStage == 5){
                actionBar.setTitle("已支付邮费");
            }

        }





        initView();
        initOrderFromService();
    }

    /**'
     * 初始化action Righ
     */
    private void initActionRight() {
        if (orderType == 1){
            if (orderState == 3) {
                actionBar.setRightView("退款");
            }
        }else if (orderType == 0){
            if (orderState == 3 ){
                if (tireInfo.getTireCount() != 0){  //畅行无忧订单不可退款
                    actionBar.setRightView("退款");
                }

            }
        }

    }


    /**
     * 轮胎退款
     */
    private void tireTuikuan() {
        int userId = new DbConfig(this).getId();
        JSONObject jsonObject = new JSONObject();
        Log.e(TAG, "initOrderFromService:--- " + orderType);
        try {
            jsonObject.put("orderNo", orderNo);
            jsonObject.put("userId", userId);
        } catch (JSONException e) {
        }
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "refundShoeCxwyOrder");
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
                        Toast.makeText(OrderInfoActivity.this, msg, Toast.LENGTH_SHORT).show();
                        finish();
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
     * 商品退款
     */
    private void goodsTuikuan() {
        int userId = new DbConfig(this).getId();
        JSONObject jsonObject = new JSONObject();
        Log.e(TAG, "initOrderFromService:--- " + orderType);
        try {
            jsonObject.put("orderNo", orderNo);
            jsonObject.put("userId", userId);
        } catch (JSONException e) {
        }
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "refundStockOrder");
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
                        Toast.makeText(OrderInfoActivity.this, msg, Toast.LENGTH_SHORT).show();
                        finish();
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
        try {
            jsonObject.put("orderNo", orderNo);
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
                Log.e(TAG, "onSuccess: ------" + result);
                JSONObject jsonObject1 = null;
                try {
                    jsonObject1 = new JSONObject(result);
                    String status = jsonObject1.getString("status");
                    String msg = jsonObject1.getString("msg");
                    if (status.equals("1")) {
                        JSONObject data = jsonObject1.getJSONObject("data");
                        if (orderType == 2) {//首次更换订单
                            if (orderState == 5) {  //代发货
                                getFirstTireOrderInfo(data);
                            } else if (orderState == 3 || orderState == 2 || orderState == 6 || orderState ==1) { //待商家确认服务 || 待收货  ||待车主确认服务
                                codeList.clear();
                                getFirstTireOrderInfo(data);
                                getFirstTireOrderCode(data);
                            }
                        } else if (orderType == 1) { //商品订单
                            if (orderState == 3 || orderState == 6 || orderState ==1 || orderState == 9 || orderState == 10 || orderState == 4) {//待商家确认服务 || 待车主确认服务
                                getGoodsOrderInfo(data);
                                initActionRight();
                            }
                        }else if (orderType == 3){ //免费再换
                            if (orderState == 5) {  //代发货
                                getFreeTireOrderInfo(data);
                            } else if (orderState == 3 || orderState == 2 || orderState == 6 || orderState ==1 || orderState == 4 ) { //待商家确认服务 || 待收货  ||待车主确认服务
                                codeList.clear();
                                getFreeTireOrderInfo(data);
                                getFreeTireOrderCode(data);
                                getFreeTireOrderOldCode(data);
                                if (orderStage == 2){   //补差订单 获取畅行无忧数量
                                    getCXWYCountFromService();
                                }
                            }
                        }else if (orderType == 0){  //轮胎购买订单
                       //     if (orderState == 3 ||orderState == 9 || orderState == 10 || orderState == 4) {  //已完成
                                getTireOrderInfo(data);
                                initActionRight();
                         //   }
                        }else if (orderType == 4){  //免费再换订单
                            getFreeRepairOrderInfo(data);
                        }else if (orderType == 5){
                            getLimitInfo(data);
                        }
                        if (orderStage!=2){
                            initData();
                        }

                        initActionRight();

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

    private void getLimitInfo(JSONObject data) throws JSONException {
        orderImg = data.getString("orderImg");
        orderTotalPrice = data.getString("orderTotalPrice");
        carNumber = data.getString("platNumber");
        storeId = data.getString("storeId");
        storeName = data.getString("storeName");
        userName = data.getString("userName");
        userPhone = data.getString("userPhone");
        orderTotalPrice = data.getString("orderTotalPrice");
    }


    /**
     * 免费修补
     * @param data
     */
    private void getFreeRepairOrderInfo(JSONObject data) throws JSONException {
        orderImg = data.getString("orderImg");
        orderTotalPrice = data.getString("orderTotalPrice");
        carNumber = data.getString("platNumber");
        storeId = data.getString("storeId");
        storeName = data.getString("storeName");
        userName = data.getString("userName");
        userPhone = data.getString("userPhone");
        JSONArray array = data.getJSONArray("userCarShoeOldBarCodeList");
        freeRepairList.clear();
        for (int i = 0; i < array.length(); i++) {
            String barCode = array.getJSONObject(i).getString("barCode");
            String repairAmount = array.getJSONObject(i).getString("repairAmount");
            freeRepairList.add(new InfoOne(barCode,repairAmount,false));
        }
    }

    /**
     * 获取畅行无忧数量
     */
    private void getCXWYCountFromService() {
        User user = new DbConfig(this).getUser();
        int userId = user.getId();
      //  int uesrCarId = user.getCarId();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userId", userId);
            jsonObject.put("userCarId", userCarId);
        } catch (JSONException e) {
        }
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "userCarInfo/queryCarCxwyInfo");
        Log.e(TAG, "initOrderFromService: -++-" + jsonObject.toString());
        params.addBodyParameter("reqJson", jsonObject.toString());
        String token = new DbConfig(this).getToken();
        params.addParameter("token", token);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess: ------" + result);
                JSONObject jsonObject1 = null;
                try {
                    jsonObject1 = new JSONObject(result);
                    String status = jsonObject1.getString("status");
                    String msg = jsonObject1.getString("msg");
                    if (status.equals("1")){
                        JSONArray data = jsonObject1.getJSONArray("data");
                        cxwyCount = data.length();
                        initData();
                        Log.e(TAG, "onSuccess:--cxwyCount---- " + cxwyCount);
                    }else if (status.equals("0")){
                        cxwyCount = 0;
                        initData();
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

    private void getTireOrderInfo(JSONObject data) throws JSONException {
        orderImg = data.getString("orderImg");
        orderTotalPrice = data.getString("orderTotalPrice");
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
    }

    /**
     * 获取免费在换 旧轮胎条形吗
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

        for (int i = 0; i < buchaTireList.size(); i++) {
            currentPrice = currentPrice  + buchaTireList.get(i).getTirePrice();
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

    /**
     * 免费再换
     * @param data
     * @throws JSONException
     */
    private void getFreeTireOrderInfo(JSONObject data) throws JSONException {
        orderImg = data.getString("orderImg");
        usedCxwAmount = data.getInt("usedCxwAmount");
        makeUpDifferencePrice = data.getString("makeUpDifferencePrice");
        associationOrderNo = data.getString("associationOrderNo");
        orderTotalPrice = data.getString("orderTotalPrice");
        postage = data.getString("postage");
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
     * 获取商品订单详情
     *
     * @param data
     */
    private void getGoodsOrderInfo(JSONObject data) throws JSONException {
        orderImg = data.getString("orderImg");
        orderTotalPrice = data.getString("orderTotalPrice");
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
    }

    /**
     * 获取首次更换轮胎的条形码
     *
     * @param data
     */
    private void getFirstTireOrderCode(JSONObject data) throws JSONException {
        JSONArray userCarShoeBarCodeList = data.getJSONArray("userCarShoeBarCodeList");
        for (int i = 0; i < userCarShoeBarCodeList.length(); i++) {
            JSONObject object = userCarShoeBarCodeList.getJSONObject(i);
            String code = object.getString("barCode");
            codeList.add(code);
        }
    }

    /**
     * 获取首次更换轮胎的基本信息
     *
     * @throws JSONException
     */
    private void getFirstTireOrderInfo(JSONObject data) throws JSONException {

        orderImg = data.getString("orderImg");
        orderTotalPrice = data.getString("orderTotalPrice");
        carNumber = data.getString("platNumber");
        storeId = data.getString("storeId");
        storeName = data.getString("storeName");
        userName = data.getString("userName");
        userPhone = data.getString("userPhone");
        JSONArray array = data.getJSONArray("firstChangeOrderVoList");
        tireInfoList.clear();
        for (int i = 0; i < array.length(); i++) {
            JSONObject object = array.getJSONObject(i);
            String shoeName = object.getString("shoeName");
            String shoeImg = object.getString("shoeImg");
            String fontRearFlag = object.getString("fontRearFlag");
            int fontAmount = object.getInt("fontAmount");
            int rearAmount = object.getInt("rearAmount");
            TireInfo tireInfo = null;
            if (fontRearFlag.equals("2")) { //后轮
                tireInfo = new TireInfo(orderImg, shoeName, rearAmount, 0.00, fontRearFlag);
            } else if (fontRearFlag.equals("1")) { //前轮
                tireInfo = new TireInfo(orderImg, shoeName, fontAmount, 0.0, fontRearFlag);
            } else {//前后轮一致
                tireInfo = new TireInfo(orderImg, shoeName, fontAmount + rearAmount, 0.00, fontRearFlag);
            }
            tireInfoList.add(tireInfo);
        }
    }

    private void initData() {
        //订单状态(orderType:1 2 3 4 ): 1 交易完成 2 待收货 3 待商家确认服务 4 作废 5 待发货 6 待车主确认服务 7 待评价 8 待支付
        items.clear();
        if (orderType == 2) {    //首次更换
            if (orderState == 5) {   //5 待支付
                items.add(new InfoOne("联系人", userName, false));
                items.add(new InfoOne("联系电话", userPhone, false));
                items.add(new InfoOne("车牌号", carNumber, false));
                items.add(new InfoOne("服务项目", "首次更换", false));
                items.add(new InfoOne("店铺名称", storeName, true, true));
                for (int i = 0; i < tireInfoList.size(); i++) {
                    items.add(tireInfoList.get(i));
                }
            } else if (orderState == 3 || orderState == 2 || orderState == 6 || orderState ==1) { //待商家确认服务 || 待收货 || 带车主确认服务 ||交易完成
                items.add(new InfoOne("联系人", userName, false));
                items.add(new InfoOne("联系电话", userPhone, false));
                items.add(new InfoOne("车牌号", carNumber, false));
                items.add(new InfoOne("服务项目", "首次更换", false));
                items.add(new InfoOne("店铺名称", storeName, true, true));
                for (int i = 0; i < tireInfoList.size(); i++) {
                    items.add(tireInfoList.get(i));
                }
                items.add(new InfoOne("轮胎条码", "", false));
                for (int i = 0; i < codeList.size(); i++) {
                    items.add(new Code(codeList.get(i)));
                }
            }
        } else if (orderType == 1) {  //商品订单
            if (orderState == 3 || orderState == 6 || orderState ==1 || orderState ==9 || orderState == 10 || orderState == 4) {   //待商家确认服务 || 带车主确认服务 || 交易完成 || 退款中 || 退款成功
                items.add(new InfoOne("联系人", userName, false));
                items.add(new InfoOne("联系电话", userPhone, false));
                items.add(new InfoOne("车牌号", carNumber, false));
                items.add(new InfoOne("服务项目", "商品订单", false));
                items.add(new InfoOne("店铺名称", storeName, true, true));
                for (int i = 0; i < goodsInfoList.size(); i++) {
                    items.add(goodsInfoList.get(i));
                }
            }
        }else if (orderType == 3){ //免费再换
            if (orderStage == 2 || orderStage ==3 || orderStage == 4 || orderStage==5){       //补差订单
                items.add(new InfoOne("联系人", userName, false));
                items.add(new InfoOne("联系电话", userPhone, false));
                items.add(new InfoOne("车牌号", carNumber, false));
                items.add(new InfoOne("服务项目", "免费再换", false));
                items.add(new InfoOne("店铺名称", storeName, true, true));
                for (int i = 0; i < tireInfoList.size(); i++) {
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

                Log.e(TAG, "initData:---- " + cxwyCount);
                if (orderStage == 2){           //需要补差
                    items.add(new InfoOne("需要补差得轮胎", "", false));
                    List<Double> priceList= new ArrayList<>();
                    for (int i = 0; i < buchaTireList.size(); i++) {
                        items.add(buchaTireList.get(i));
                        priceList.add(buchaTireList.get(i).getTirePrice());
                    }
                    if (cxwyCount > buchaTireList.size()){  //判断畅行无忧得数量 跟轮胎得数量
                        items.add(new CountOne(buchaTireList.size(),currentCxwyCount,priceList,currentPrice));
                    }else {
                        items.add(new CountOne(cxwyCount,currentCxwyCount,priceList,currentPrice));
                    }
                }else if (orderStage == 3){ //补差完
                    items.add(new InfoOne("使用畅行无忧数量", usedCxwAmount+"", false));
                    items.add(new InfoOne("补差金额", "￥" + makeUpDifferencePrice, false));
                }else if (orderStage == 4){
                    items.add(new InfoOne("需补邮费", "￥" + postage, false));
                }else if (orderStage == 5){
                    items.add(new InfoOne("已支付邮费", "￥" + postage, false));
                }


            }else {                 //普通订单
                if (orderState == 5){ //5 待支付
                    items.add(new InfoOne("联系人",userName,false));
                    items.add(new InfoOne("联系电话",userPhone,false));
                    items.add(new InfoOne("车牌号",carNumber,false));
                    items.add(new InfoOne("服务项目","免费再换",false));
                    items.add(new InfoOne("店铺名称",storeName,true,true));
                    for (int i = 0; i < tireInfoList.size(); i++) {
                        items.add(tireInfoList.get(i));
                    }
                }else if (orderState == 3 || orderState == 2 ) { //待商家确认服务 || 待收货
                    items.add(new InfoOne("联系人", userName, false));
                    items.add(new InfoOne("联系电话", userPhone, false));
                    items.add(new InfoOne("车牌号", carNumber, false));
                    items.add(new InfoOne("服务项目", "免费再换", false));
                    items.add(new InfoOne("店铺名称", storeName, true, true));
                    for (int i = 0; i < tireInfoList.size(); i++) {
                        items.add(tireInfoList.get(i));
                    }
                    items.add(new InfoOne("轮胎条码", "", false));
                    for (int i = 0; i < codeList.size(); i++) {
                        items.add(new Code(codeList.get(i)));
                    }
                }else if (orderState == 6 || orderState ==1) { //带车主确认服务
                    items.add(new InfoOne("联系人", userName, false));
                    items.add(new InfoOne("联系电话", userPhone, false));
                    items.add(new InfoOne("车牌号", carNumber, false));
                    items.add(new InfoOne("服务项目", "免费再换", false));
                    items.add(new InfoOne("店铺名称", storeName, true, true));
                    for (int i = 0; i < tireInfoList.size(); i++) {
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
                }
            }

        }else if (orderType == 0){
            items.add(new InfoOne("联系人", userName, false));
            items.add(new InfoOne("联系电话", userPhone, false));
            items.add(new InfoOne("车牌号", carNumber, false));
            if (tireInfo.getTireCount() == 0){
                items.add(new InfoOne("服务项目", "畅行无忧购买", false));
            }else {
                items.add(new InfoOne("服务项目", "轮胎购买", false));
            }

            items.add(new InfoOne("订单总价", "￥" + orderTotalPrice, true));
            if (tireInfo.getTireCount() != 0){
                items.add(tireInfo);
            }

            if (cxwyOrder != null) {
                items.add(cxwyOrder);
            }
        }else if (orderType == 4){
            items.add(new InfoOne("联系人", userName, false));
            items.add(new InfoOne("联系电话", userPhone, false));
            items.add(new InfoOne("车牌号", carNumber, false));
            items.add(new InfoOne("服务项目", "免费修补", false));
            items.add(new InfoOne("店铺名称", storeName, true, true));
            items.add(new InfoOne("轮胎条码", "修补次数", false));
            for (int i = 0; i < freeRepairList.size(); i++) {
                items.add(freeRepairList.get(i));
            }
        }else if (orderType == 5){
            items.add(new InfoOne("联系人", userName, false));
            items.add(new InfoOne("联系电话", userPhone, false));
            items.add(new InfoOne("车牌号", carNumber, false));
            items.add(new InfoOne("服务项目", "信用额度补差", false));
            items.add(new InfoOne("补差金额", orderTotalPrice+"", true));
        }
        assertAllRegistered(adapter, items);
        adapter.notifyDataSetChanged();
    }

    private void initView() {
        jujueBuchaLayout = (LinearLayout) findViewById(R.id.jujue_bucha_layout);
        tongyibuchaLayout = (LinearLayout) findViewById(R.id.tongyi_bucha_layout);
        orderBuchaLayout = (LinearLayout) findViewById(R.id.order_bucha_layout);;
        listView = (RecyclerView) findViewById(R.id.order_info_activity);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        listView.setLayoutManager(linearLayoutManager);
        adapter = new MultiTypeAdapter(items);
        register();
        listView.setAdapter(adapter);
        assertHasTheSameAdapter(listView, adapter);

        orderButton = (TextView) findViewById(R.id.order_pay_button);
        initButton();

        //拒绝补差
        RxViewAction.clickNoDouble(jujueBuchaLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        showBuchaDialog();


                    }
                });
        //同意补差
        RxViewAction.clickNoDouble(tongyibuchaLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        buchaOrder();
                    }
                });

        RxViewAction.clickNoDouble(orderButton)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (orderStage == 1){
                            if (orderState == 6) {   //待车主确认服务
                                userAffirmService();
                            }
                        }else if (orderStage == 4){ //支付邮费
                                buYoufei();
                        }

                    }
                });
    }

    /**
     * 补邮费
     */
    private void buYoufei() {
        Intent intent = new Intent(getApplicationContext(), PaymentActivity.class);
        Log.e(TAG, "buYoufei: --" + postage);
        intent.putExtra(PaymentActivity.ALL_PRICE, Double.parseDouble(postage));
        intent.putExtra(PaymentActivity.ORDERNO, orderNo);
        intent.putExtra(PaymentActivity.ORDER_TYPE, orderType);  //3
        intent.putExtra(PaymentActivity.ORDER_STAGE, orderStage);  //3
        startActivity(intent);
    }

    /**
     * 补差的dialog
     */
    private void showBuchaDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.dialog_error, null);
        TextView error_text = (TextView) dialogView.findViewById(R.id.error_text);
        error_text.setText("是否拒绝补差？");
        dialog.setTitle("如意如驿");
        dialog.setIcon(R.drawable.ic_logo_login);
        dialog.setView(dialogView);
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                cancleBucha();
            }
        });
        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    /**
     * 生成补差订单
     */
    private void buchaOrder() {
        int userId = new DbConfig(this).getId();
        JSONObject jsonObject = new JSONObject();
        Log.e(TAG, "initOrderFromService:--- " + orderType);
        try {
            jsonObject.put("associationOrderNo", associationOrderNo);
            jsonObject.put("orderNo", orderNo);
            jsonObject.put("userId", userId);
            jsonObject.put("userCarId", userCarId);
            jsonObject.put("price", currentPrice);
            jsonObject.put("cxwyAmount", currentCxwyCount);
        } catch (JSONException e) {
        }
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "addMakeUpDifferenceOrder");
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
                        Toast.makeText(OrderInfoActivity.this, msg, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), PaymentActivity.class);
                        intent.putExtra(PaymentActivity.ALL_PRICE, currentPrice);
                        intent.putExtra(PaymentActivity.ORDERNO, orderNo);
                        intent.putExtra(PaymentActivity.ORDER_TYPE, orderType);  //3
                        startActivity(intent);

                        finish();
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
     * 拒绝补差
     */
    private void cancleBucha() {
        int userId = new DbConfig(this).getId();
        JSONObject jsonObject = new JSONObject();
        Log.e(TAG, "initOrderFromService:--- " + orderType);
        try {
            jsonObject.put("orderNo", orderNo);
            jsonObject.put("userId", userId);
        } catch (JSONException e) {
        }
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "refuseMakeUpDifferenceOrder");
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
                        Toast.makeText(OrderInfoActivity.this, msg, Toast.LENGTH_SHORT).show();
                        finish();
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
     * 车主确认服务
     */
    private void userAffirmService() {
        int userId = new DbConfig(this).getId();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("orderNo", orderNo);
            jsonObject.put("orderType", orderType);
            jsonObject.put("userId", userId);
        } catch (JSONException e) {
        }
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "userConfirmOrderServiced");
        Log.e(TAG, "initOrderFromService: -++-" + jsonObject.toString());
        params.addBodyParameter("reqJson", jsonObject.toString());
        String token = new DbConfig(this).getToken();
        params.addParameter("token", token);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess: ------" + result);
                JSONObject jsonObject1 = null;
                try {
                    jsonObject1 = new JSONObject(result);
                    String status = jsonObject1.getString("status");
                    String msg = jsonObject1.getString("msg");
                    if (status.equals("1")) {
                        Intent intent = new Intent(getApplicationContext(), OrderActivity.class);
                        intent.putExtra(OrderFragment.ORDER_TYPE, "YWC");
                        intent.putExtra(OrderActivity.ORDER_FROM, 1);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(OrderInfoActivity.this, msg, Toast.LENGTH_SHORT).show();
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

    private void initButton() {
    //    if (orderType == 2) {    //首次更换
        if (orderStage == 2){
            orderBuchaLayout.setVisibility(View.VISIBLE);
            orderButton.setVisibility(View.GONE);
        }else if (orderStage == 4){
            orderBuchaLayout.setVisibility(View.GONE);
            orderButton.setVisibility(View.VISIBLE);
            orderButton.setText("前往补邮费");
            orderButton.setClickable(true);
            orderButton.setBackgroundResource(R.drawable.bg_button);
        }else if (orderStage == 5){
            orderBuchaLayout.setVisibility(View.GONE);
            orderButton.setVisibility(View.VISIBLE);
            orderButton.setText("已支付运费");
            orderButton.setClickable(false);
            orderButton.setBackgroundResource(R.drawable.bg_button_noclick);
        }else {
            orderBuchaLayout.setVisibility(View.GONE);
            orderButton.setVisibility(View.VISIBLE);

            if (orderType == 0){
                if (orderState == 3) { //待商家确认服务
                    orderButton.setText("交易完成");
                    orderButton.setClickable(false);
                    orderButton.setBackgroundResource(R.drawable.bg_button_noclick);
                }
                else if (orderState == 6){
                    orderButton.setText("已退货");
                    orderButton.setClickable(false);
                    orderButton.setBackgroundResource(R.drawable.bg_button_noclick);
                }else if (orderState == 7){
                    orderButton.setText("退款中");
                    orderButton.setClickable(false);
                    orderButton.setBackgroundResource(R.drawable.bg_button_noclick);
                }else if (orderState == 8){ ;
                    orderButton.setText("已退款");
                    orderButton.setClickable(false);
                    orderButton.setBackgroundResource(R.drawable.bg_button_noclick);
                }else if (orderState == 9){
                    orderButton.setText("作废");
                    orderButton.setClickable(false);
                    orderButton.setBackgroundResource(R.drawable.bg_button_noclick);
                }
            }else {
                if (orderState == 5) {
                    orderButton.setText("等待发货");
                    orderButton.setClickable(false);
                    orderButton.setBackgroundResource(R.drawable.bg_button_noclick);
                } else if (orderState == 2) {
                    orderButton.setText("待收货");
                    orderButton.setClickable(false);
                    orderButton.setBackgroundResource(R.drawable.bg_button_noclick);
                } else if (orderState == 3) { //待商家确认服务

                    if (orderStage == 3){
                        orderButton.setText("已补差，待商家服务");
                        orderButton.setClickable(false);
                        orderButton.setBackgroundResource(R.drawable.bg_button_noclick);
                    }else {
                        orderButton.setText("待商家确认服务");
                        orderButton.setClickable(false);
                        orderButton.setBackgroundResource(R.drawable.bg_button_noclick);
                    }
                } else if (orderState == 6) { //待车主确认服务
                    orderButton.setText("确认服务");
                    orderButton.setClickable(true);
                    orderButton.setBackgroundResource(R.drawable.bg_button);
                }else if (orderState == 1){
                    orderButton.setText("交易完成");
                    orderButton.setClickable(false);
                    orderButton.setBackgroundResource(R.drawable.bg_button_noclick);
                }else if (orderState == 9){
                    orderButton.setText("退款中");
                    orderButton.setClickable(false);
                    orderButton.setBackgroundResource(R.drawable.bg_button_noclick);
                }else if (orderState == 10){
                    orderButton.setText("退款成功");
                    orderButton.setClickable(false);
                    orderButton.setBackgroundResource(R.drawable.bg_button_noclick);
                }else if (orderState == 4){
                    orderButton.setText("作废订单");
                    orderButton.setClickable(false);
                    orderButton.setBackgroundResource(R.drawable.bg_button_noclick);
                }
            }
        }


      /*  } else if (orderType == 1) {  //商品订单
            if (orderState == 3) { //待商家确认服务
                orderButton.setText("待商家确认服务");
                orderButton.setClickable(false);
                orderButton.setBackgroundResource(R.drawable.bg_button_noclick);
            } else if (orderState == 6) { //待车主确认服务
                orderButton.setText("确认服务");
                orderButton.setClickable(true);
                orderButton.setBackgroundResource(R.drawable.bg_button);
            }
        }else if (orderType == 3){ //免费在还
            if (orderState == 5){
                orderButton.setText("等待发货");
                orderButton.setClickable(false);
                orderButton.setBackgroundResource(R.drawable.bg_button_noclick);
            }else if (orderState == 2) {
                orderButton.setText("待收货");
                orderButton.setClickable(false);
                orderButton.setBackgroundResource(R.drawable.bg_button_noclick);
            } else if (orderState == 3) { //待商家确认服务
                orderButton.setText("待商家确认服务");
                orderButton.setClickable(false);
                orderButton.setBackgroundResource(R.drawable.bg_button_noclick);
            } else if (orderState == 6) { //待车主确认服务
                orderButton.setText("确认服务");
                orderButton.setClickable(true);
                orderButton.setBackgroundResource(R.drawable.bg_button);
            }
        }*/
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
        countOneViewBinder.setListener(this);
        adapter.register(CountOne.class, countOneViewBinder);
    }

    @Override
    public void onInfoItemClickListener(String name) {
        if (name.equals("店铺名称")) {
          /*  Log.e(TAG, "onInfoItemClickListener: storeid :" + storeId );
            Intent intent = new Intent(this, ShopHomeActivity.class);
            intent.putExtra("STOREID",Integer.parseInt(storeId));
            startActivity(intent);*/
        }
    }

    /**
     * 畅行无忧数量改变
     */
    @Override
    public void onCxwyCountClikcListener(int currentCxwyCount,Double currentPrice) {
        Log.e(TAG, "onCxwyCountClikcListener: " + currentCxwyCount );
        Log.e(TAG, "onCxwyCountClikcListener:currentPrice- " + currentPrice );
        this.currentPrice = currentPrice;
        this.currentCxwyCount = currentCxwyCount;
        Log.e(TAG, "onCxwyCountClikcListener: currentPrice-" + currentPrice);
        Log.e(TAG, "onCxwyCountClikcListener: currentCxwyCount-" + currentCxwyCount);
       // initData();
    }


}
