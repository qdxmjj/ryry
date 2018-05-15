package com.ruyiruyi.ruyiruyi.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.ruyiruyi.ruyiruyi.MainActivity;
import com.ruyiruyi.ruyiruyi.R;
import com.ruyiruyi.ruyiruyi.db.DbConfig;
import com.ruyiruyi.ruyiruyi.ui.multiType.CxwyOrder;
import com.ruyiruyi.ruyiruyi.ui.multiType.CxwyOrderViewBinder;
import com.ruyiruyi.ruyiruyi.ui.multiType.CxwyViewBinder;
import com.ruyiruyi.ruyiruyi.ui.multiType.GoodsInfo;
import com.ruyiruyi.ruyiruyi.ui.multiType.GoodsInfoViewBinder;
import com.ruyiruyi.ruyiruyi.ui.multiType.InfoOne;
import com.ruyiruyi.ruyiruyi.ui.multiType.InfoOneViewBinder;
import com.ruyiruyi.ruyiruyi.ui.multiType.TireInfo;
import com.ruyiruyi.ruyiruyi.ui.multiType.TireInfoViewBinder;
import com.ruyiruyi.ruyiruyi.utils.RequestUtils;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;
import com.ruyiruyi.rylibrary.base.BaseActivity;
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

public class PendingOrderActivity extends BaseActivity implements InfoOneViewBinder.OnInfoItemClick{
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_order,R.id.my_action);
        actionBar = (ActionBar) findViewById(R.id.my_action);
        actionBar.setTitle("待支付");;
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

        goodsInfoList = new ArrayList<>();
        Intent intent = getIntent();
        orderno = intent.getStringExtra(PaymentActivity.ORDERNO);
        orderType = intent.getIntExtra(PaymentActivity.ORDER_TYPE,0);
        orderFrom = intent.getIntExtra(PaymentActivity.ORDER_FROM,0);

        initView();
        initOrderFromService();
    }

    private void initOrderFromService() {
        int userId = new DbConfig().getId();
        JSONObject jsonObject = new JSONObject();
        Log.e(TAG, "initOrderFromService:--- " + orderType );
        try {
            jsonObject.put("orderNo",orderno);
            jsonObject.put("orderType",orderType);
            jsonObject.put("userId",userId);
        } catch (JSONException e) {
        }
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "getUserOrderInfoByNoAndType");
        Log.e(TAG, "initOrderFromService: -++-" + jsonObject.toString() );
        params.addBodyParameter("reqJson",jsonObject.toString());
        String token = new DbConfig().getToken();
        params.addParameter("token",token);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess:---- " + result);
                JSONObject jsonObject1 = null;
                try {
                    if (orderType == 0){ //轮胎订单
                        jsonObject1 = new JSONObject(result);
                        String status = jsonObject1.getString("status");
                        String msg = jsonObject1.getString("msg");
                        if (status.equals("1")){
                            JSONObject data = jsonObject1.getJSONObject("data");
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
                                String tirePrice = "";
                                String totalPrice = "";
                                if (fontRearFlag.equals("2")){ //后轮
                                    tireName = object.getString("rearShoeName");
                                    tireCount = object.getString("rearAmount");
                                    tirePrice = object.getString("rearPrice");
                                    totalPrice = object.getString("rearTotalPrice");
                                }else { //前轮 或  前后轮
                                    tireName = object.getString("fontShoeName");
                                    tireCount = object.getString("fontAmount");
                                    tirePrice = object.getString("fontPrice");
                                    totalPrice = object.getString("fontTotalPrice");
                                }
                                tireInfo = new TireInfo(orderImg,tireName,Integer.parseInt(tireCount),tirePrice,fontRearFlag);
                                if (Integer.parseInt(cxwyAmount) > 0){
                                    cxwyOrder = new CxwyOrder(Integer.parseInt(cxwyAmount),cxwyPrice);
                                }
                            }
                            initData();
                        }
                    }else if (orderType == 1){ //商品服务订单
                        jsonObject1 = new JSONObject(result);
                        String status = jsonObject1.getString("status");
                        String msg = jsonObject1.getString("msg");
                        if (status.equals("1")){
                            JSONObject data = jsonObject1.getJSONObject("data");
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
                            initData();
                        }else {
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

    private void initData() {
        items.clear();
        if (orderType == 0){ //轮胎
            items.add(new InfoOne("联系人",userName,false));
            items.add(new InfoOne("联系电话",userPhone,false));
            items.add(new InfoOne("车牌号",carNumber,false));
            items.add(new InfoOne("订单总价","￥"+orderTotalPrice,true));
            items.add(tireInfo);
            if (cxwyOrder!=null){
                items.add(cxwyOrder);
            }
        }else if (orderType == 1){ //商品
            items.add(new InfoOne("联系人",userName,false));
            items.add(new InfoOne("联系电话",userPhone,false));
            items.add(new InfoOne("订单总价","￥"+orderTotalPrice,false));
            items.add(new InfoOne("店铺名称",storeName,true,true));
            for (int i = 0; i < goodsInfoList.size(); i++) {
                items.add(goodsInfoList.get(i));
            }
        }
        assertAllRegistered(adapter,items);
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
                        Intent intent = new Intent(getApplicationContext(),PaymentActivity.class);
                        intent.putExtra(PaymentActivity.ALL_PRICE,Double.parseDouble(orderTotalPrice));
                        intent.putExtra(PaymentActivity.ORDERNO,orderno);
                        intent.putExtra(PaymentActivity.ORDER_TYPE,orderType);
                        startActivity(intent);
                    }
                });
    }

    private void register() {
        adapter.register(GoodsInfo.class,new GoodsInfoViewBinder(this));
        InfoOneViewBinder infoOneViewBinder = new InfoOneViewBinder();
        infoOneViewBinder.setListener(this);
        adapter.register(InfoOne.class, infoOneViewBinder);
            adapter.register(CxwyOrder.class,new CxwyOrderViewBinder());
        adapter.register(TireInfo.class,new TireInfoViewBinder(this));
    }

    @Override
    public void onBackPressed() {
        if (orderFrom == 0){
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }else {
            super.onBackPressed();
        }

    }

    @Override
    public void onInfoItemClickListener(String name) {
        if (name.equals("店铺名称")){
            Log.e(TAG, "onInfoItemClickListener: storeid :" + storeId );
            Intent intent = new Intent(this, ShopHomeActivity.class);
            intent.putExtra("STOREID",Integer.parseInt(storeId));
            startActivity(intent);
        }
    }
}
