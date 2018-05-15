package com.ruyiruyi.merchant.ui.fragment;


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

import com.bumptech.glide.Glide;
import com.ruyiruyi.merchant.R;
import com.ruyiruyi.merchant.bean.GoodsItemBean;
import com.ruyiruyi.merchant.bean.ItemNullBean;
import com.ruyiruyi.merchant.bean.OrderItemBean;
import com.ruyiruyi.merchant.db.DbConfig;
import com.ruyiruyi.merchant.ui.multiType.GoodsItemProvider;
import com.ruyiruyi.merchant.ui.multiType.ItemNullProvider;
import com.ruyiruyi.merchant.ui.multiType.OrderItemProvider;
import com.ruyiruyi.merchant.utils.UtilsURL;

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

public class MyOrderFragment extends Fragment {
    public static String ORDER_TYPE = "ORDER_TYPE";
    private SwipeRefreshLayout mSwipeLayout;
    private RecyclerView mRlv;
    private MultiTypeAdapter multiTypeAdapter;
    private List<Object> items = new ArrayList<>();
    private String TAG = MyOrderFragment.class.getSimpleName();
    private String order_type;
    private List<OrderItemBean> orderBeanList;


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
        Log.e(TAG, "onActivityCreated: order_type = " + order_type);

        initView();
        initData();
        initSwipeLayout();
    }

    private void initData() {
        switch (order_type) {
            case "QUANBU":
                for (int i = 0; i < 8; i++) {
                    OrderItemBean bean = new OrderItemBean();
                    bean.setTitle("商品" + i);
                    bean.setGoodsId(i + "");
                    bean.setImgUrl("http://192.168.0.167/images/store/stockImg/72/1525685364190568addgoodsimg.png");
                    bean.setBianhao("1234567890" + i);
                    bean.setPrice("888");
                    bean.setStatus("0");
                    orderBeanList.add(bean);

                    OrderItemBean bean2 = new OrderItemBean();
                    bean2.setTitle("商品" + i);
                    bean2.setGoodsId(i + "");
                    bean2.setImgUrl("http://192.168.0.167/images/store/stockImg/72/1525685364190568addgoodsimg.png");
                    bean2.setBianhao("1234567890" + i);
                    bean2.setPrice("888");
                    bean2.setStatus("1");
                    orderBeanList.add(bean2);
                }
                break;
            case "DAIFAHUO":
                for (int i = 0; i < 10; i++) {
                    OrderItemBean bean = new OrderItemBean();
                    bean.setTitle("商品" + i);
                    bean.setGoodsId(i + "");
                    bean.setImgUrl("http://192.168.0.167/images/store/stockImg/72/1525685364190568addgoodsimg.png");
                    bean.setBianhao("1234567890" + i);
                    bean.setPrice("888");
                    bean.setStatus("0");
                    orderBeanList.add(bean);
                }
                break;
            case "DAISHOUHUO":
                for (int i = 0; i < 10; i++) {
                    OrderItemBean bean = new OrderItemBean();
                    bean.setTitle("商品" + i);
                    bean.setGoodsId(i + "");
                    bean.setImgUrl("http://192.168.0.167/images/store/stockImg/72/1525685364190568addgoodsimg.png");
                    bean.setBianhao("1234567890" + i);
                    bean.setPrice("888");
                    bean.setStatus("0");
                    orderBeanList.add(bean);
                }
                break;
            case "DAIFUWU":
                for (int i = 0; i < 10; i++) {
                    OrderItemBean bean = new OrderItemBean();
                    bean.setTitle("商品" + i);
                    bean.setGoodsId(i + "");
                    bean.setImgUrl("http://192.168.0.167/images/store/stockImg/72/1525685364190568addgoodsimg.png");
                    bean.setBianhao("1234567890" + i);
                    bean.setPrice("888");
                    bean.setStatus("0");
                    orderBeanList.add(bean);
                }
                break;
            case "YIWANCHENG":
                for (int i = 0; i < 10; i++) {
                    OrderItemBean bean = new OrderItemBean();
                    bean.setTitle("商品" + i);
                    bean.setGoodsId(i + "");
                    bean.setImgUrl("http://192.168.0.167/images/store/stockImg/72/1525685364190568addgoodsimg.png");
                    bean.setBianhao("1234567890" + i);
                    bean.setPrice("888");
                    bean.setStatus("1");
                    orderBeanList.add(bean);
                }
                break;
        }

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
        mRlv.setAdapter(multiTypeAdapter);
        assertHasTheSameAdapter(mRlv, multiTypeAdapter);

        orderBeanList = new ArrayList<>();
    }

    //下拉刷新
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
                myDownRefreshByServer();

                mSwipeLayout.setRefreshing(false);
            }
        });
        //加载更多
//        mRlv.setOnScrollListener(new OnLoadMoreListener(){
//
//        });
    }

    private void myDownRefreshByServer() {

    }

}