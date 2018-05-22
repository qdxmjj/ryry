package com.ruyiruyi.ruyiruyi.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.ruyiruyi.ruyiruyi.R;
import com.ruyiruyi.ruyiruyi.db.DbConfig;
import com.ruyiruyi.ruyiruyi.ui.multiType.CxwyOrder;
import com.ruyiruyi.ruyiruyi.ui.multiType.CxwyOrderViewBinder;
import com.ruyiruyi.ruyiruyi.ui.multiType.GoodsInfo;
import com.ruyiruyi.ruyiruyi.ui.multiType.GoodsInfoViewBinder;
import com.ruyiruyi.ruyiruyi.ui.multiType.InfoOne;
import com.ruyiruyi.ruyiruyi.ui.multiType.InfoOneViewBinder;
import com.ruyiruyi.ruyiruyi.ui.multiType.TireInfo;
import com.ruyiruyi.ruyiruyi.ui.multiType.TireInfoViewBinder;
import com.ruyiruyi.ruyiruyi.utils.RequestUtils;
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

import static me.drakeet.multitype.MultiTypeAsserts.assertAllRegistered;
import static me.drakeet.multitype.MultiTypeAsserts.assertHasTheSameAdapter;

public class OrderInfoActivity extends BaseActivity implements InfoOneViewBinder.OnInfoItemClick{
    private static final String TAG = OrderInfoActivity.class.getSimpleName();
    private ActionBar actionBar;
    private RecyclerView listView;
    private List<Object> items = new ArrayList<>();
    private MultiTypeAdapter adapter;
    private String orderNo;
    private int orderType;
    private int orderState;
    public List<TireInfo> tireInfoList;
    private String userPhone;
    private String userName;
    private String carNumber;
    private String orderTotalPrice;
    private String orderImg;
    private String storeId;
    private String storeName;
    private TextView orderButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_info);
        actionBar = (ActionBar) findViewById(R.id.my_action);

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
        tireInfoList = new ArrayList<>();
        Intent intent = getIntent();
        // OrderType 0:轮胎购买订单 1:普通商品购买订单 2:首次更换订单 3:免费再换订单 4:轮胎修补订单
        //轮胎订单状态(orderType:0) :1 已安装 2 待服务 3 支付成功 4 支付失败 5 待支付 6 已退货
        //订单状态(orderType:1 2 3 4 ): 1 交易完成 2 待收货 3 待商家确认服务 4 作废 5 待发货 6 待车主确认服务 7 待评价 8 待支付
        orderNo = intent.getStringExtra(PaymentActivity.ORDERNO);
        orderType = intent.getIntExtra(PaymentActivity.ORDER_TYPE,0);
        orderState = intent.getIntExtra(PaymentActivity.ORDER_STATE,0);
        if (orderType == 2){
            actionBar.setTitle("首次更换");;
        }

        initView();
        initOrderFromService();
    }

    private void initOrderFromService() {
        int userId = new DbConfig().getId();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("orderNo",orderNo);
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
                Log.e(TAG, "onSuccess: ------" +  result);
                JSONObject jsonObject1 = null;
                try {
                    if (orderType == 2) //首次更换订单
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
                            if (fontRearFlag.equals("2")){ //后轮
                                tireInfo = new TireInfo(orderImg,shoeName,rearAmount,"0.00",fontRearFlag);
                            }else if (fontRearFlag.equals("1")){ //前轮
                                tireInfo = new TireInfo(orderImg,shoeName,fontAmount,"0.00",fontRearFlag);
                            }else {//前后轮一致
                                tireInfo = new TireInfo(orderImg,shoeName,fontAmount + rearAmount,"0.00",fontRearFlag);
                            }
                            tireInfoList.add(tireInfo);
                        }
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

    private void initData() {
        items.clear();
        if (orderType == 2){    //首次更换
            items.add(new InfoOne("联系人",userName,false));
            items.add(new InfoOne("联系电话",userPhone,false));
            items.add(new InfoOne("车牌号",carNumber,false));
            items.add(new InfoOne("服务项目","首次更换",false));
            items.add(new InfoOne("店铺名称",storeName,true,true));
            for (int i = 0; i < tireInfoList.size(); i++) {
                items.add(tireInfoList.get(i));
            }
        }
        assertAllRegistered(adapter,items);
        adapter.notifyDataSetChanged();
    }

    private void initView() {
        listView = (RecyclerView) findViewById(R.id.order_info_activity);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        listView.setLayoutManager(linearLayoutManager);
        adapter = new MultiTypeAdapter(items);
        register();
        listView.setAdapter(adapter);
        assertHasTheSameAdapter(listView, adapter);

        orderButton = (TextView) findViewById(R.id.order_pay_button);
        initButton();
    }

    private void initButton() {
        if (orderType == 2){ //代发货
            orderButton.setText("等待发货");
            orderButton.setBackgroundResource(R.drawable.bg_button_noclick);
        }
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
    public void onInfoItemClickListener(String name) {
        if (name.equals("店铺名称")){
          /*  Log.e(TAG, "onInfoItemClickListener: storeid :" + storeId );
            Intent intent = new Intent(this, ShopHomeActivity.class);
            intent.putExtra("STOREID",Integer.parseInt(storeId));
            startActivity(intent);*/
        }
    }
}
