package com.ruyiruyi.merchant.ui.fragment;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ruyiruyi.merchant.R;
import com.ruyiruyi.merchant.bean.GoodsItemBean;
import com.ruyiruyi.merchant.bean.ItemBottomBean;
import com.ruyiruyi.merchant.bean.ItemNullBean;
import com.ruyiruyi.merchant.bean.OrderItemBean;
import com.ruyiruyi.merchant.db.DbConfig;
import com.ruyiruyi.merchant.ui.multiType.GoodsItemProvider;
import com.ruyiruyi.merchant.ui.multiType.ItemBottomProvider;
import com.ruyiruyi.merchant.ui.multiType.ItemNullProvider;
import com.ruyiruyi.merchant.ui.multiType.OrderItemProvider;
import com.ruyiruyi.merchant.ui.multiType.listener.OnLoadMoreListener;
import com.ruyiruyi.merchant.utils.UtilsURL;
import com.ruyiruyi.rylibrary.base.BaseFragment;

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

public class MyOrderFragment extends BaseFragment {
    public static String ORDER_TYPE = "ORDER_TYPE";
    private SwipeRefreshLayout mSwipeLayout;
    private RecyclerView mRlv;
    private MultiTypeAdapter multiTypeAdapter;
    private List<Object> items = new ArrayList<>();
    private String TAG = MyOrderFragment.class.getSimpleName();
    private String order_type;
    private List<OrderItemBean> orderBeanList;
    private String storeId;
    private String state;//state:  0:全部订单 1:待发货2:待收货 3:待服务 4:已完成
    private int total_all_page;
    private int mRows = 10;  // 设置默认一页加载10条数据
    private int current_page;
    private boolean isLoadMore = false;
    private boolean isLoadOver = false;
    private boolean isLoadMoreSingle = false;//上拉单次标志位
    private ProgressDialog startDialog;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.mygoods_list_fg, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Bundle bundle = getArguments();
        order_type = bundle.getString(ORDER_TYPE);
        storeId = new DbConfig(getActivity()).getId() + "";
        state = "";

        initView();
        initData();
        initSwipeLayout();
    }

    //加载原始数据
    private void initData() {
        initDataByLoadMoreType();
    }

    //公用下载
    private void initDataByLoadMoreType() {
        //数据加载完成前显示加载动画
        startDialog = new ProgressDialog(getContext());
        showDialogProgress(startDialog, "订单信息加载中...");


        isLoadOver = false;

        if (!isLoadMore) {//只有加载更多(不清空原数据)
            orderBeanList.clear();
            current_page = 1;
        }

        //下载数据
        switch (order_type) {
            case "QUANBU":
                state = "0";//state:  0:全部订单 1:待发货2:待收货 3:待服务 4:已完成
                requestFromServer(storeId, state);
                break;
            case "DAIFAHUO":
                state = "1";//state:  0:全部订单 1:待发货2:待收货 3:待服务 4:已完成
                requestFromServer(storeId, state);
                break;
            case "DAISHOUHUO":
                state = "2";//state:  0:全部订单 1:待发货2:待收货 3:待服务 4:已完成
                requestFromServer(storeId, state);
                break;
            case "DAIFUWU":
                state = "3";//state:  0:全部订单 1:待发货2:待收货 3:待服务 4:已完成
                requestFromServer(storeId, state);
                break;
            case "YIWANCHENG":
                state = "4";//state:  0:全部订单 1:待发货2:待收货 3:待服务 4:已完成
                requestFromServer(storeId, state);
                break;
        }

    }

    private void requestFromServer(final String storeId, String state) {

        JSONObject object = new JSONObject();
        try {
            object.put("storeId", storeId);
            object.put("state", state);
            object.put("page", current_page);
            object.put("rows", mRows);

        } catch (JSONException e) {
        }
        RequestParams params = new RequestParams(UtilsURL.REQUEST_URL + "getStoreGeneralOrderByState");
        params.addBodyParameter("reqJson", object.toString());
        params.addBodyParameter("token", new DbConfig(getActivity()).getToken());
        params.setConnectTimeout(6000);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess0808: result =  " + result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONObject data = jsonObject.getJSONObject("data");
                    total_all_page = (data.getInt("total")) / mRows;//处理页数
                    if (data.getInt("total") % mRows > 0) {
                        total_all_page++;
                    }
                    JSONArray rows = data.getJSONArray("rows");
                    for (int i = 0; i < rows.length(); i++) {
                        JSONObject orders = (JSONObject) rows.get(i);
                        OrderItemBean bean = new OrderItemBean();
                        bean.setStoreId(storeId);
                        bean.setImgUrl(orders.getString("orderImage"));
                        bean.setTitle(orders.getString("orderName"));
                        bean.setBianhao(orders.getString("orderNo"));
                        bean.setOrderStage(orders.getString("orderStage"));
                        bean.setPrice(orders.getString("orderPrice"));
                        bean.setStatus(orders.getString("orderState"));
                        bean.setOrderTime(orders.getLong("orderTime"));
                        bean.setOrderType(orders.getString("orderType"));
                        orderBeanList.add(bean);
                    }

                    //更新数据
                    updataData();

                    isLoadMoreSingle = false;//重置加载更多单次标志位


                } catch (JSONException e) {
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(getContext(), "订单信息加载失败,请检查网络", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                //加载完成 隐藏加载动画
                hideDialogProgress(startDialog);
            }
        });
    }

    private void updataData() {
        if (orderBeanList == null || orderBeanList.size() == 0) {
            items.clear();
            items.add(new ItemNullBean("暂无数据"));
            assertAllRegistered(multiTypeAdapter, items);
            multiTypeAdapter.notifyDataSetChanged();
        } else {
            items.clear();
            for (int i = 0; i < orderBeanList.size(); i++) {
                items.add(orderBeanList.get(i));
            }
            assertAllRegistered(multiTypeAdapter, items);
            multiTypeAdapter.notifyDataSetChanged();
        }
    }


    private void initView() {
        mSwipeLayout = getView().findViewById(R.id.mygoods_swipelayout);
        mRlv = getView().findViewById(R.id.mygoods_rlv);

        LinearLayoutManager manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mRlv.setLayoutManager(manager);
        multiTypeAdapter = new MultiTypeAdapter(items);
        OrderItemProvider provider = new OrderItemProvider(getActivity());
        multiTypeAdapter.register(OrderItemBean.class, provider);
        multiTypeAdapter.register(ItemNullBean.class, new ItemNullProvider());
        multiTypeAdapter.register(ItemBottomBean.class, new ItemBottomProvider());
        mRlv.setAdapter(multiTypeAdapter);
        assertHasTheSameAdapter(mRlv, multiTypeAdapter);

        orderBeanList = new ArrayList<>();
    }

    //初始化下拉上拉
    private void initSwipeLayout() {
        mSwipeLayout.setColorSchemeResources(
                R.color.theme_primary,
                R.color.c5,
                R.color.c6,
                R.color.c7
        );
        mSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //加载最新数据并更新adapter数据
                isLoadMore = false;
                myDownRefreshByServer();

                mSwipeLayout.setRefreshing(false);
            }
        });
        //加载更多
        mRlv.setOnScrollListener(new OnLoadMoreListener() {

            @Override
            public void onLoadMore() {
                if (isLoadMoreSingle) {
                    return;
                }
                isLoadMoreSingle = true;//上拉单次标志位

                if (total_all_page > current_page) {
                    current_page++;
                    items.add(new ItemBottomBean("加载更多..."));

                    isLoadMore = true;
                    initDataByLoadMoreType();
                } else {
                    if (!isLoadOver && (total_all_page > 1)) {//用于判断是否加  加载完成底部
                        items.add(new ItemBottomBean("全部加载完毕!"));
                        isLoadOver = true;
                    }
                }
                assertAllRegistered(multiTypeAdapter, items);
                multiTypeAdapter.notifyDataSetChanged();
            }
        });
    }

    //下拉刷新
    private void myDownRefreshByServer() {
        initDataByLoadMoreType();
    }

}