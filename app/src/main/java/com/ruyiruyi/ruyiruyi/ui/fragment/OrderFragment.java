package com.ruyiruyi.ruyiruyi.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ruyiruyi.ruyiruyi.R;
import com.ruyiruyi.ruyiruyi.db.DbConfig;
import com.ruyiruyi.ruyiruyi.ui.multiType.Order;
import com.ruyiruyi.ruyiruyi.ui.multiType.OrderViewBinder;
import com.ruyiruyi.ruyiruyi.utils.FullyLinearLayoutManager;
import com.ruyiruyi.ruyiruyi.utils.RequestUtils;

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

public class OrderFragment extends Fragment {
    private static final String TAG = OrderFragment.class.getSimpleName();
    public static String ORDER_TYPE;  //ALL DZF DFH DFW YWC
    private String orderType;
    private RecyclerView listView;
    private List<Object> items = new ArrayList<>();
    private MultiTypeAdapter adapter;
    public List<Order> orderList ;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_order,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle arguments = getArguments();
        orderType = arguments.getString(ORDER_TYPE);
        orderList = new ArrayList<>();
        initView();
        initDataFromService();
    }

    private void initDataFromService() {
        JSONObject jsonObject = new JSONObject();
        int userId = new DbConfig().getId();
        try {
            jsonObject.put("userId",userId);
            if (orderType.equals("ALL")){
                jsonObject.put("state",0);
            }else if (orderType.equals("DZF")){
                jsonObject.put("state",1);
            }else if (orderType.equals("DFH")){
                jsonObject.put("state",2);
            }else if (orderType.equals("DFW")){
                jsonObject.put("state",3);
            }else if (orderType.equals("YWC")){
                jsonObject.put("state",4);
            }
        } catch (JSONException e) {
        }
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "getGeneralOrderByState");
        params.addBodyParameter("reqJson",jsonObject.toString());
        String token = new DbConfig().getToken();
        params.addParameter("token",token);
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
                        orderList.clear();
                        JSONArray data = jsonObject1.getJSONArray("data");
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject object = data.getJSONObject(i);
                            String orderImage = object.getString("orderImage");
                            String orderName = object.getString("orderName");
                            String orderNo = object.getString("orderNo");
                            String orderPrice = object.getString("orderPrice");
                            String orderState = object.getString("orderState");
                            String orderTime = object.getString("orderTime");
                            String orderType = object.getString("orderType");
                            orderList.add(new Order(orderImage,orderName,orderNo,orderPrice,orderState,orderTime,orderType));
                        }
                        initData();
                    }else {
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
        assertAllRegistered(adapter,items);
        adapter.notifyDataSetChanged();
    }

    private void initView() {
        listView = (RecyclerView) getView().findViewById(R.id.order_listview);
        LinearLayoutManager linearLayoutManager = new FullyLinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        listView.setLayoutManager(linearLayoutManager);
        adapter = new MultiTypeAdapter(items);
        register();

        listView.setAdapter(adapter);
        assertHasTheSameAdapter(listView, adapter);

    }

    private void register() {
        adapter.register(Order.class,new OrderViewBinder(getContext()));
    }
}