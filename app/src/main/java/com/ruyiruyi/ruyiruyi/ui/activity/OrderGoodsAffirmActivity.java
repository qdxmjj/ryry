package com.ruyiruyi.ruyiruyi.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ruyiruyi.ruyiruyi.R;
import com.ruyiruyi.ruyiruyi.db.DbConfig;
import com.ruyiruyi.ruyiruyi.db.model.User;
import com.ruyiruyi.ruyiruyi.ui.activity.base.RyBaseActivity;
import com.ruyiruyi.ruyiruyi.ui.model.OrderGoods;
import com.ruyiruyi.ruyiruyi.ui.multiType.GoodsInfo;
import com.ruyiruyi.ruyiruyi.ui.multiType.GoodsInfoViewBinder;
import com.ruyiruyi.ruyiruyi.ui.multiType.GoodsNew;
import com.ruyiruyi.ruyiruyi.ui.multiType.InfoOne;
import com.ruyiruyi.ruyiruyi.ui.multiType.InfoOneViewBinder;
import com.ruyiruyi.ruyiruyi.utils.RequestUtils;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;
import com.ruyiruyi.rylibrary.cell.ActionBar;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import me.drakeet.multitype.MultiTypeAdapter;
import rx.functions.Action1;

import static me.drakeet.multitype.MultiTypeAsserts.assertAllRegistered;
import static me.drakeet.multitype.MultiTypeAsserts.assertHasTheSameAdapter;

public class OrderGoodsAffirmActivity extends RyBaseActivity implements InfoOneViewBinder.OnInfoItemClick {
    private static final String TAG = OrderGoodsAffirmActivity.class.getSimpleName();
    private ActionBar actionBar;
    private List<Object> items = new ArrayList<>();
    private MultiTypeAdapter adapter;
    private RecyclerView listView;
    private String username;
    private String phone;
    private List<GoodsNew> goodslist;
    private List<GoodsInfo> goodsInfoList;
    private double allprice = 0.0;
    private double currentPrice = 0.0;
    private TextView allPriceText;
    private TextView goodsBuyButton;
    private int userId;
    private int storeid;
    private String storename;
    public static int COUPON_REQUEST = 10;
    private int couponchoose;
    private String couponName = "选择优惠券";
    private int couponId = 0;
    private int carId;
    private ProgressDialog progressDialog;
    private String goodsName;
    private int couponType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_goods_affirm, R.id.my_action);
        actionBar = (ActionBar) findViewById(R.id.my_action);
        actionBar.setTitle("订单确认");
        ;
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
        progressDialog = new ProgressDialog(this);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        goodslist = ((List<GoodsNew>) bundle.getSerializable("GOODSLIST"));
        allprice = bundle.getDouble("ALLPRICE");
        storeid = bundle.getInt("STOREID");
        storename = bundle.getString("STORENAME");
        goodsInfoList = new ArrayList<>();
        if (goodslist.size() > 0) {
            for (int i = 0; i < goodslist.size(); i++) {
                GoodsInfo goodsInfo = new GoodsInfo();
                goodsInfo.setGoodsName(goodslist.get(i).getGoodsName());
                goodsInfo.setGoodsImage(goodslist.get(i).getGoodsImage());
                goodsInfo.setGoodsId(goodslist.get(i).getGoodsId());
                goodsInfo.setGoodsCount(goodslist.get(i).getGoodsAmount());
                goodsInfo.setGoodsPrice(goodslist.get(i).getGoodsPrice());
                goodsInfo.setCurrentCount(goodslist.get(i).getCurrentGoodsAmount());
                goodsInfo.setGoodsClassId(goodslist.get(i).getGoodsClassId());
                goodsInfo.setServiceTypeId(goodslist.get(i).getServiceTypeId());
                goodsInfoList.add(goodsInfo);
            }
        }

        User user = new DbConfig(this).getUser();
        username = user.getNick();
        phone = user.getPhone();
        userId = user.getId();
        carId = user.getCarId();
        initView();
        initData();
        //sendDataToService();
    }

    private void sendDataToService() {
        for (int i = 0; i < goodsInfoList.size(); i++) {
            if (goodsInfoList.get(i).getGoodsName().equals("精致洗车") || goodsInfoList.get(i).getGoodsName().equals("四轮定位")) {
                if (carId ==0) {
                    Toast.makeText(OrderGoodsAffirmActivity.this, "特殊商品，需要绑定车辆购买", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        }
        if (allprice == 0.0 && carId == 0){
            Toast.makeText(this, "特殊商品，需要绑定车辆购买", Toast.LENGTH_SHORT).show();
            return;
        }


        showDialogProgress(progressDialog,"订单提交中...");

        OrderGoods orderGoods = null;
        if (couponchoose == 0) {    //weixuan
            orderGoods= new OrderGoods(userId, storeid, storename, allprice + "", goodsInfoList,couponId,allprice+"");
        }else {
            orderGoods = new OrderGoods(userId, storeid, storename, allprice + "", goodsInfoList,couponId,currentPrice+"");
        }
        Log.e(TAG, "sendDataToService: " + allprice );

        Gson gson = new Gson();
        String json = gson.toJson(orderGoods);
        Log.e(TAG, "sendDataToService: ---" + json);
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "addStockOrder");
        params.addBodyParameter("reqJson", json);
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
                    if (status.equals("1")) {
                        String orderNo = jsonObject1.getString("data");

                        Intent intent = new Intent(getApplicationContext(), PaymentActivity.class);
                        if (couponchoose == 0) {    //weixuan
                            intent.putExtra(PaymentActivity.ALL_PRICE, allprice);
                        }else {
                            intent.putExtra(PaymentActivity.ALL_PRICE, currentPrice);
                        }
                        intent.putExtra(PaymentActivity.ORDERNO, orderNo);
                        intent.putExtra(PaymentActivity.ORDER_TYPE, 1);
                        startActivity(intent);
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
                hideDialogProgress(progressDialog);
            }
        });


    }

    private void initData() {
        items.clear();
        items.add(new InfoOne("联系人", username, false));
        items.add(new InfoOne("联系电话", phone, false));
        items.add(new InfoOne("店铺名", storename, true));
        for (int i = 0; i < goodsInfoList.size(); i++) {
            items.add(goodsInfoList.get(i));
        }
        items.add(new InfoOne("优惠券", couponName, true,true));

        assertAllRegistered(adapter, items);
        adapter.notifyDataSetChanged();
    }

    private void initView() {
        goodsBuyButton = (TextView) findViewById(R.id.goods_buy_button);
        allPriceText = (TextView) findViewById(R.id.all_price_goods_text);
        allPriceText.setText(allprice + "");
        listView = (RecyclerView) findViewById(R.id.goods_order_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        listView.setLayoutManager(linearLayoutManager);
        adapter = new MultiTypeAdapter(items);
        register();

        listView.setAdapter(adapter);
        assertHasTheSameAdapter(listView, adapter);
        RxViewAction.clickNoDouble(goodsBuyButton)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        sendDataToService();
                    }
                });
    }

    private void register() {
        adapter.register(GoodsInfo.class, new GoodsInfoViewBinder(this));
        InfoOneViewBinder infoOneViewBinder = new InfoOneViewBinder();
        infoOneViewBinder.setListener(this);
        adapter.register(InfoOne.class, infoOneViewBinder);

    }

    /**
     * 优惠券点击选择
     * @param name
     */
    @Override
    public void onInfoItemClickListener(String name) {

        if (name.equals("优惠券")){
            Intent intent = new Intent(getApplicationContext(), CouponActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("GOODSLIST", (Serializable) goodslist);
            bundle.putInt("FROM_TYPE", 1);
            bundle.putInt("CAR_ID", carId);
            bundle.putInt("STORE_ID",storeid);
            intent.putExtras(bundle);
            startActivityForResult(intent,COUPON_REQUEST);

    /*        boolean isXiche = false;
            boolean isDingwei = false;
            for (int i = 0; i < goodslist.size(); i++) {
                if (goodslist.get(i).getGoodsName().equals("精致洗车")) {
                    isXiche = true;
                }else if (goodslist.get(i).getGoodsName().equals("四轮定位")){
                    isDingwei = true;
                }
            }
            User user = new DbConfig(this).getUser();
            int carId = user.getCarId();
            if (isXiche && isDingwei){
                Intent intent = new Intent(this, CouponActivity.class);
                intent.putExtra(CouponActivity.FROM_TYPE,1);
                intent.putExtra(CouponActivity.CHOOSE_TYPE,3);
                intent.putExtra(CouponActivity.CAR_ID,carId);
                startActivityForResult(intent,COUPON_REQUEST);
            }else if (isXiche && !isDingwei){
                Intent intent = new Intent(this, CouponActivity.class);
                intent.putExtra(CouponActivity.FROM_TYPE,1);
                intent.putExtra(CouponActivity.CHOOSE_TYPE,1);
                intent.putExtra(CouponActivity.CAR_ID,carId);
                startActivityForResult(intent,COUPON_REQUEST);
            }else if (!isXiche && isDingwei){
                Intent intent = new Intent(this, CouponActivity.class);
                intent.putExtra(CouponActivity.FROM_TYPE,1);
                intent.putExtra(CouponActivity.CHOOSE_TYPE,2);
                intent.putExtra(CouponActivity.CAR_ID,carId);
                startActivityForResult(intent,COUPON_REQUEST);
            }else {
                Intent intent = new Intent(this, CouponActivity.class);
                intent.putExtra(CouponActivity.FROM_TYPE,1);
                intent.putExtra(CouponActivity.CHOOSE_TYPE,5);
                intent.putExtra(CouponActivity.CAR_ID,carId);
                startActivityForResult(intent,COUPON_REQUEST);
            }
*/


        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e(TAG, "onActivityResult: requestCode-" + requestCode);
        Log.e(TAG, "onActivityResult: resultCode-" + resultCode);
        if (resultCode == COUPON_REQUEST){
            couponchoose = data.getIntExtra("COUPONCHOOSE",0);  //是未选优惠券  1是选择优惠券
            if (couponchoose == 1){
                couponName = data.getStringExtra("COUPONNAME");
                couponId = data.getIntExtra("COUPONID", 0);
                goodsName = data.getStringExtra("GOODS_NAME");
                couponType = data.getIntExtra("COUPON_TYPE",0);
                initCouponView();
            }else {
                couponName = "请选择优惠券";
                couponId = 0;
                initData();
                allPriceText.setText(allprice+"");
            }
        }
    }

    /**
     * 初始化优惠券
     */
    private void initCouponView() {
        if (couponType == 2){       //现金券
            Double couponPrice = Double.parseDouble(goodsName);
            currentPrice = allprice - couponPrice;
            if (currentPrice < 0.00){
                currentPrice = 0.00;
            }
        }else {     //服务券
            double price = 0.00;
            for (int i = 0; i < goodslist.size(); i++) {
                if (goodslist.get(i).getGoodsName().equals(goodsName)){
                    price = Double.parseDouble(goodslist.get(i).getGoodsPrice());
                }
            }
            currentPrice = allprice - price;
            if (currentPrice < 0.00){
                currentPrice = 0.00;
            }
        }
        initData();
        allPriceText.setText(currentPrice+"");

    /*    if (couponName.equals("精致洗车券")){
            double price = 0.00;
            for (int i = 0; i < goodslist.size(); i++) {
                if (goodslist.get(i).getGoodsName().equals("精致洗车")) {
                    price = Double.parseDouble(goodslist.get(i).getGoodsPrice());
                }
            }
            currentPrice = allprice - price;
            if (currentPrice < 0.00){
                currentPrice = 0.00;
            }
        }else if (couponName.equals("四轮定位券")){
            double price = 0.00;
            for (int i = 0; i < goodslist.size(); i++) {
                if (goodslist.get(i).getGoodsName().equals("四轮定位")) {
                    price = Double.parseDouble(goodslist.get(i).getGoodsPrice());
                }
            }
            currentPrice = allprice - price;
            if (currentPrice < 0.00){
                currentPrice = 0.00;
            }
        }else if (couponName.equals("10元现金券")){
            currentPrice = allprice - 10.00;
            if (currentPrice < 0.00){
                currentPrice = 0.00;
            }
        }
        initData();
        allPriceText.setText(currentPrice+"");*/
    }
}
