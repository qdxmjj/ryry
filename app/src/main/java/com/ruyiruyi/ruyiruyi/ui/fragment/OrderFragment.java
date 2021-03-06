package com.ruyiruyi.ruyiruyi.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ruyiruyi.ruyiruyi.R;
import com.ruyiruyi.ruyiruyi.db.DbConfig;
import com.ruyiruyi.ruyiruyi.ui.activity.EvaluateActivity;
import com.ruyiruyi.ruyiruyi.ui.activity.OrderInfoActivity;
import com.ruyiruyi.ruyiruyi.ui.activity.PaymentActivity;
import com.ruyiruyi.ruyiruyi.ui.activity.PendingOrderActivity;
import com.ruyiruyi.ruyiruyi.ui.fragment.base.RyBaseFragment;
import com.ruyiruyi.ruyiruyi.ui.multiType.Empty;
import com.ruyiruyi.ruyiruyi.ui.multiType.EmptyBig;
import com.ruyiruyi.ruyiruyi.ui.multiType.EmptyBigViewBinder;
import com.ruyiruyi.ruyiruyi.ui.multiType.Order;
import com.ruyiruyi.ruyiruyi.ui.multiType.OrderViewBinder;
import com.ruyiruyi.ruyiruyi.utils.FullyLinearLayoutManager;
import com.ruyiruyi.ruyiruyi.utils.RequestUtils;
import com.ruyiruyi.ruyiruyi.utils.UtilsRY;

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

public class OrderFragment extends RyBaseFragment implements OrderViewBinder.OnOrderItemClick {
    private static final String TAG = OrderFragment.class.getSimpleName();
    public static String ORDER_TYPE;  //ALL DZF DFH DFW YWC
    private String orderType;
    private RecyclerView listView;
    private List<Object> items = new ArrayList<>();
    private MultiTypeAdapter adapter;
    public List<Order> orderList;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_order, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //判断是否登录（未登录提示登录）
        judgeIsLogin();

        Bundle arguments = getArguments();
        orderType = arguments.getString(ORDER_TYPE);
        orderList = new ArrayList<>();
        initView();
        initDataFromService();
    }

    @Override
    public void onResume() {
        super.onResume();

        initDataFromService();

    }


    private void initDataFromService() {
        JSONObject jsonObject = new JSONObject();
        int userId = new DbConfig(getContext()).getId();
        try {
            jsonObject.put("userId", userId);
            if (orderType.equals("ALL")) {
                jsonObject.put("state", 0);
            } else if (orderType.equals("DZF")) {
                jsonObject.put("state", 1);
            } else if (orderType.equals("DFH")) {
                jsonObject.put("state", 2);
            } else if (orderType.equals("DFW")) {
                jsonObject.put("state", 3);
            } else if (orderType.equals("YWC")) {
                jsonObject.put("state", 4);
            }
        } catch (JSONException e) {
        }
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "getUserGeneralOrderByState");
        params.addBodyParameter("reqJson", jsonObject.toString());
        String token = new DbConfig(getContext()).getToken();
        params.addParameter("token", token);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                orderList.clear();
                swipeRefreshLayout.setRefreshing(false);
                Log.e(TAG, "onSuccess: " + result);
                JSONObject jsonObject1 = null;
                try {
                    jsonObject1 = new JSONObject(result);
                    String status = jsonObject1.getString("status");
                    String msg = jsonObject1.getString("msg");
                    if (status.equals("1")) {
                        JSONArray data = jsonObject1.getJSONArray("data");
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject object = data.getJSONObject(i);
                            String orderImage = object.getString("orderImage");
                            String orderName = object.getString("orderName");
                            String orderNo = object.getString("orderNo");
                            String orderPrice = object.getString("orderPrice");
                            String orderActuallyPrice = object.getString("orderActuallyPrice");
                            String orderState = object.getString("orderState");
                            String orderStage = object.getString("orderStage");
                            //  String orderTime = object.getString("orderTime");
                            String orderTimeStr = new UtilsRY().getTimestampToStringAll(object.getLong("orderTime"));
                            String orderType = object.getString("orderType");
                            String storeId = object.getString("storeId");
                            if (orderType.equals("1")){
                                orderList.add(new Order(orderImage, orderName, orderNo, orderActuallyPrice, orderState, orderTimeStr, orderType, storeId,orderStage));
                            }else {
                                orderList.add(new Order(orderImage, orderName, orderNo, orderPrice, orderState, orderTimeStr, orderType, storeId,orderStage));
                            }

                        }
                        initData();
                    } else if (status.equals("-999")) {
                        showUserTokenDialog("您的账号在其它设备登录,请重新登录");
                    } else {
                        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
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
        for (int i = 0; i < orderList.size(); i++) {
            items.add(orderList.get(i));
        }
        if (orderList.size() == 0){
            items.add(new EmptyBig());
        }
        assertAllRegistered(adapter, items);
        adapter.notifyDataSetChanged();
    }

    private void initView() {
        swipeRefreshLayout = (SwipeRefreshLayout) getView().findViewById(R.id.order_refresh_layout);
        listView = (RecyclerView) getView().findViewById(R.id.order_listview);
        LinearLayoutManager linearLayoutManager = new FullyLinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        listView.setLayoutManager(linearLayoutManager);
        adapter = new MultiTypeAdapter(items);
        register();

        listView.setAdapter(adapter);
        assertHasTheSameAdapter(listView, adapter);
        swipeRefreshLayout.setProgressViewEndTarget(true, 200);
        //下拉刷新
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initDataFromService();

            }
        });

    }

    private void register() {
        OrderViewBinder orderViewBinder = new OrderViewBinder(getContext());
        orderViewBinder.setListener(this);
        adapter.register(Order.class, orderViewBinder);
        adapter.register(EmptyBig.class,new EmptyBigViewBinder());
    }

    @Override
    public void onOrderItemClickListener(String orderState, String orderType, String orderNo, String storeId,String orderStage) {
        // OrderType 0:轮胎购买订单 1:普通商品购买订单 2:首次更换订单 3:免费再换订单 4:轮胎修补订单
        //轮胎订单状态(orderType:0) :1 已安装 2 待服务 3 支付成功 4 支付失败 5 待支付 6 已退货 7退款中 8是已退款 9作废
        //订单状态(orderType:1 2 3 4 ): 1 交易完成 2 待收货 3 待商家确认服务 4 作废 5 待发货 6 待车主确认服务 7 待评价 8 待支付
        //orderStage:订单二段状态 1 默认(不需要支付差价)  2 待车主支付差价 3 已支付差价 4 待车主支付运费 5 已支付运费
        if ((orderType.equals("1") && orderState.equals("8")) || (orderType.equals("0") && orderState.equals("5")) || orderType.equals("6")) { //待支付
            Intent intent = new Intent(getContext(), PendingOrderActivity.class);
            intent.putExtra(PaymentActivity.ORDERNO, orderNo);
            intent.putExtra(PaymentActivity.ORDER_TYPE, Integer.parseInt(orderType));
            intent.putExtra(PaymentActivity.ORDER_FROM, 1);
            startActivity(intent);
        }else if ((orderType.equals("0") && orderState.equals("3"))){
            Intent intent = new Intent(getContext(), OrderInfoActivity.class);
            intent.putExtra(PaymentActivity.ORDERNO, orderNo);
            intent.putExtra(PaymentActivity.ORDER_TYPE, Integer.parseInt(orderType));
            intent.putExtra(PaymentActivity.ORDER_STATE, Integer.parseInt(orderState));
            startActivity(intent);
        }else if (orderState.equals("7") && ((orderType.equals("1") || orderType.equals("2") || orderType.equals("3") || orderType.equals("4")  ))) {  //待评价
            Intent intent = new Intent(getContext(), EvaluateActivity.class);
            intent.putExtra(PaymentActivity.ORDERNO, orderNo);
            intent.putExtra(PaymentActivity.STOREID, storeId);
            startActivity(intent);
        }else if (orderState.equals("1")) {  //已完成
            Intent intent = new Intent(getContext(), OrderInfoActivity.class);
            intent.putExtra(PaymentActivity.ORDERNO, orderNo);
            intent.putExtra(PaymentActivity.ORDER_TYPE, Integer.parseInt(orderType));
            intent.putExtra(PaymentActivity.ORDER_STATE, Integer.parseInt(orderState));
            intent.putExtra(PaymentActivity.ORDER_STAGE, Integer.parseInt(orderStage));
            startActivity(intent);
        }else if (orderState.equals("12")) {  //已完成
            Intent intent = new Intent(getContext(), OrderInfoActivity.class);
            intent.putExtra(PaymentActivity.ORDERNO, orderNo);
            intent.putExtra(PaymentActivity.ORDER_TYPE, Integer.parseInt(orderType));
            intent.putExtra(PaymentActivity.ORDER_STATE, Integer.parseInt(orderState));
            intent.putExtra(PaymentActivity.ORDER_STAGE, Integer.parseInt(orderStage));
            startActivityForResult(intent,100);
        }else if (orderType.equals("0")){
            Intent intent = new Intent(getContext(), OrderInfoActivity.class);
            intent.putExtra(PaymentActivity.ORDERNO, orderNo);
            intent.putExtra(PaymentActivity.ORDER_TYPE, Integer.parseInt(orderType));
            intent.putExtra(PaymentActivity.ORDER_STATE, Integer.parseInt(orderState));
            startActivity(intent);
        }else {                             //订单详情
            Intent intent = new Intent(getContext(), OrderInfoActivity.class);
            intent.putExtra(PaymentActivity.ORDERNO, orderNo);
            intent.putExtra(PaymentActivity.ORDER_TYPE, Integer.parseInt(orderType));
            intent.putExtra(PaymentActivity.ORDER_STATE, Integer.parseInt(orderState));
            intent.putExtra(PaymentActivity.ORDER_STAGE, Integer.parseInt(orderStage));
            startActivity(intent);
        }
        /*else if (orderState.equals("5")) {    //首次更换 免费再换 代发货 orderType 2  ||
            Intent intent = new Intent(getContext(), OrderInfoActivity.class);
            intent.putExtra(PaymentActivity.ORDERNO, orderNo);
            intent.putExtra(PaymentActivity.ORDER_TYPE, Integer.parseInt(orderType));
            intent.putExtra(PaymentActivity.ORDER_STATE, Integer.parseInt(orderState));
            startActivity(intent);
        } else if (orderType.equals("2") && orderState.equals("2") || orderType.equals("2") && orderState.equals("3")
                || orderType.equals("2") && orderState.equals("6")   ) {
            //首次更换订单 待收货 || 待商家确认服务 || 待车主确认服务 || 免费再换 待收货
            Intent intent = new Intent(getContext(), OrderInfoActivity.class);
            intent.putExtra(PaymentActivity.ORDERNO, orderNo);
            intent.putExtra(PaymentActivity.ORDER_TYPE, Integer.parseInt(orderType));
            intent.putExtra(PaymentActivity.ORDER_STATE, Integer.parseInt(orderState));
            startActivity(intent);
        } else if (orderType.equals("3") && orderState.equals("2") || orderType.equals("3") && orderState.equals("3")
                || orderType.equals("3") && orderState.equals("6")   ) {
            //免费在还订单 待收货 || 待商家确认服务 || 待车主确认服务 || 免费再换 待收货
            Intent intent = new Intent(getContext(), OrderInfoActivity.class);
            intent.putExtra(PaymentActivity.ORDERNO, orderNo);
            intent.putExtra(PaymentActivity.ORDER_TYPE, Integer.parseInt(orderType));
            intent.putExtra(PaymentActivity.ORDER_STATE, Integer.parseInt(orderState));
            startActivity(intent);
        } else if (orderType.equals("1") && orderState.equals("3") || orderType.equals("1") && orderState.equals("6")) { //商品订单  待商家确认服务  || 待车主确认服务
            Intent intent = new Intent(getContext(), OrderInfoActivity.class);
            intent.putExtra(PaymentActivity.ORDERNO, orderNo);
            intent.putExtra(PaymentActivity.ORDER_TYPE, Integer.parseInt(orderType));
            intent.putExtra(PaymentActivity.ORDER_STATE, Integer.parseInt(orderState));
            startActivity(intent);
        } */
    }
}