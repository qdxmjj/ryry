package com.ruyiruyi.ruyiruyi.ui.activity;

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
import com.ruyiruyi.ruyiruyi.ui.activity.base.RYBaseActivity;
import com.ruyiruyi.ruyiruyi.ui.model.OrderGoods;
import com.ruyiruyi.ruyiruyi.ui.multiType.GoodsHorizontal;
import com.ruyiruyi.ruyiruyi.ui.multiType.GoodsInfo;
import com.ruyiruyi.ruyiruyi.ui.multiType.GoodsInfoViewBinder;
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

import java.util.ArrayList;
import java.util.List;

import me.drakeet.multitype.MultiTypeAdapter;
import rx.functions.Action1;

import static me.drakeet.multitype.MultiTypeAsserts.assertAllRegistered;
import static me.drakeet.multitype.MultiTypeAsserts.assertHasTheSameAdapter;

public class OrderGoodsAffirmActivity extends RYBaseActivity {
    private static final String TAG = OrderGoodsAffirmActivity.class.getSimpleName();
    private ActionBar actionBar;
    private List<Object> items = new ArrayList<>();
    private MultiTypeAdapter adapter;
    private RecyclerView listView;
    private String username;
    private String phone;
    private List<GoodsHorizontal> goodslist;
    private List<GoodsInfo> goodsInfoList;
    private double allprice = 0.00;
    private TextView allPriceText;
    private TextView goodsBuyButton;
    private int userId;
    private int storeid;
    private String storename;

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
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        goodslist = ((List<GoodsHorizontal>) bundle.getSerializable("GOODSLIST"));
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
                goodsInfo.setGoodsCount(goodslist.get(i).getGoodsCount());
                goodsInfo.setGoodsPrice(goodslist.get(i).getGoodsPrice());
                goodsInfo.setCurrentCount(goodslist.get(i).getCurrentCount());
                goodsInfo.setGoodsClassId(goodslist.get(i).getGoodsClassId());
                goodsInfo.setServiceTypeId(goodslist.get(i).getServiceTypeId());
                goodsInfoList.add(goodsInfo);
            }
        }

        User user = new DbConfig().getUser();
        username = user.getNick();
        phone = user.getPhone();
        userId = user.getId();
        initView();
        initData();
        //sendDataToService();
    }

    private void sendDataToService() {
        OrderGoods orderGoods = new OrderGoods(userId, storeid, storename, allprice + "", goodsInfoList);
        Gson gson = new Gson();
        String json = gson.toJson(orderGoods);
        Log.e(TAG, "sendDataToService: ---" + json);
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "addStockOrder");
        params.addBodyParameter("reqJson", json);
        String token = new DbConfig().getToken();
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
                        intent.putExtra(PaymentActivity.ALL_PRICE, allprice);
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

            }
        });


    }

    private void initData() {
        items.clear();
        items.add(new InfoOne("联系人", username, false));
        items.add(new InfoOne("联系电话", phone, true));
        for (int i = 0; i < goodsInfoList.size(); i++) {
            items.add(goodsInfoList.get(i));
        }
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
        adapter.register(InfoOne.class, new InfoOneViewBinder());
    }
}
