package com.ruyiruyi.merchant.ui.fragment;

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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ruyiruyi.merchant.R;
import com.ruyiruyi.merchant.bean.ItemBottomBean;
import com.ruyiruyi.merchant.bean.ItemNullBean;
import com.ruyiruyi.merchant.db.DbConfig;
import com.ruyiruyi.merchant.ui.activity.MyOrderActivity;
import com.ruyiruyi.merchant.ui.multiType.DingdanItemViewProvider;
import com.ruyiruyi.merchant.ui.multiType.ItemBottomProvider;
import com.ruyiruyi.merchant.ui.multiType.ItemNullProvider;
import com.ruyiruyi.merchant.ui.multiType.listener.OnLoadMoreListener;
import com.ruyiruyi.merchant.ui.multiType.modle.Dingdan;
import com.ruyiruyi.merchant.utils.UtilsURL;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;
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
import rx.functions.Action1;

import static me.drakeet.multitype.MultiTypeAsserts.assertAllRegistered;
import static me.drakeet.multitype.MultiTypeAsserts.assertHasTheSameAdapter;


public class OrderFragment extends BaseFragment {
    private static final String TAG = OrderFragment.class.getSimpleName();
    private RecyclerView mRecyclerView;
    private MultiTypeAdapter multiTypeAdapter;
    private List<Object> items = new ArrayList<>();
    private List<Dingdan> orderBeanList;
    private int total_all_page;
    private int mRows = 10;  // 设置默认一页加载10条数据
    private int current_page;
    private boolean isLoadMore = false;
    private boolean isLoadOver = false;
    private boolean isLoadMoreSingle = false;//上拉单次标志位
    private boolean isFirstLoad = true;

    private String totalNum;
    private String finishedNum;
    private String unfinishedNum;
    private TextView tv_zdd_num;
    private TextView tv_wwcdd_num;
    private TextView tv_ywcdd_num;
    private SwipeRefreshLayout mSwipeLayout;
//    private ProgressDialog startDialog;
    private LinearLayout ll_wwcdd;
    private LinearLayout ll_ywcdd;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dingdan_fg, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initView();

        initData();
        initSwipeLayout();

        bindView();


    }

    private void initData() {
        requestFromServer();
    }

    private void setData() {
        tv_zdd_num.setText(totalNum);
        tv_wwcdd_num.setText(unfinishedNum);
        tv_ywcdd_num.setText(finishedNum);
    }

    //初始化下拉上拉
    private void initSwipeLayout() {
        mSwipeLayout.setColorSchemeResources(//下拉刷新圆圈颜色
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
        mRecyclerView.setOnScrollListener(new OnLoadMoreListener() {

            @Override
            public void onLoadMore() {
                //根据上拉单次标志位判断是否执行加载更多（防止多次加载）
                if (isLoadMoreSingle) {
                    return;
                }
                isLoadMoreSingle = true;//上拉单次标志位

                if (total_all_page > current_page) {
                    current_page++;
                    items.add(new ItemBottomBean("加载更多..."));

                    isLoadMore = true;
                    requestFromServer();
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
    private void myDownRefreshByServer() {//下拉刷新
        requestFromServer();
    }

    private void requestFromServer() {
        //数据加载完成前显示加载动画
//        startDialog = new ProgressDialog(getContext());
        if (isFirstLoad) {
//            showDialogProgress(startDialog, "信息加载中...");
        }


        isLoadOver = false;

        if (!isLoadMore) {//只有加载更多(不清空原数据)
            orderBeanList.clear();
            current_page = 1;
        }


        JSONObject object = new JSONObject();
        try {
            object.put("page", current_page);
            object.put("rows", mRows);
            object.put("storeId", new DbConfig(getActivity()).getId() + "");
            object.put("type", "2");//type: 1:商品订单 2:如意如意平台订单
        } catch (JSONException e) {
        }
        RequestParams params = new RequestParams(UtilsURL.REQUEST_URL + "getStoreGeneralOrderByType");
        params.addBodyParameter("reqJson", object.toString());
        params.addBodyParameter("token", new DbConfig(getActivity()).getToken());
        params.setConnectTimeout(6000);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess: result656 = " + result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONObject data = jsonObject.getJSONObject("data");
                    totalNum = data.getString("totalNum");
                    finishedNum = data.getString("finishedNum");
                    unfinishedNum = data.getString("unfinishedNum");
                    int totla = data.getInt("total");
                    total_all_page = totla / mRows;//处理页数
                    if (totla % mRows > 0) {
                        total_all_page++;
                    }
                    JSONArray rows = data.getJSONArray("rows");
                    for (int i = 0; i < rows.length(); i++) {
                        Dingdan dingdan = new Dingdan();
                        JSONObject order = (JSONObject) rows.get(i);
                        dingdan.setOrderImage(order.getString("orderImage"));
                        dingdan.setOrderName(order.getString("orderName"));
                        dingdan.setPlatNumber(order.getString("platNumber"));
                        dingdan.setOrderType(order.getString("orderType"));
                        dingdan.setOrderStage(order.getString("orderStage"));
                        dingdan.setOrderNo(order.getString("orderNo"));
                        dingdan.setOrderState(order.getString("orderState"));
                        dingdan.setOrderTime(order.getLong("orderTime"));
                        dingdan.setIsRead(order.getString("isRead"));
                        orderBeanList.add(dingdan);
                    }

                    //更新数据
                    updataData();

                    //绑定固定数据
                    setData();

                    isLoadMoreSingle = false;//重置加载更多单次标志位


                } catch (JSONException e) {
                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(getContext(), "信息加载失败,请检查网络", Toast.LENGTH_SHORT).show();

                //网络异常
                updataNetError();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                //加载完成 隐藏加载动画
                if (isFirstLoad) {
//                    hideDialogProgress(startDialog);
                    isFirstLoad = false;
                }
            }
        });
    }

    private void updataData() {
        items.clear();
        if (orderBeanList == null || orderBeanList.size() == 0) {
            items.add(new ItemNullBean("暂无数据"));
        } else {
            for (int i = 0; i < orderBeanList.size(); i++) {
                items.add(orderBeanList.get(i));
            }
        }
        assertAllRegistered(multiTypeAdapter, items);
        multiTypeAdapter.notifyDataSetChanged();
    }

    private void updataNetError() {
        items.clear();
        items.add(new ItemNullBean(R.drawable.ic_net_error));
        assertAllRegistered(multiTypeAdapter, items);
        multiTypeAdapter.notifyDataSetChanged();

    }

    private void bindView() {
        RxViewAction.clickNoDouble(ll_wwcdd).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Intent intent = new Intent(getContext(), MyOrderActivity.class);
                intent.putExtra("page", "0");
                intent.putExtra("typestate", "pingtai");
                startActivity(intent);
            }
        });
        RxViewAction.clickNoDouble(ll_ywcdd).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Intent intent = new Intent(getContext(), MyOrderActivity.class);
                intent.putExtra("page", "1");
                intent.putExtra("typestate", "pingtai");
                startActivity(intent);
            }
        });
    }

    private void initView() {
        tv_zdd_num = getView().findViewById(R.id.tv_zdd_num);
        tv_wwcdd_num = getView().findViewById(R.id.tv_wwcdd_num);
        tv_ywcdd_num = getView().findViewById(R.id.tv_ywcdd_num);
        mSwipeLayout = getView().findViewById(R.id.my_swp);
        ll_wwcdd = getView().findViewById(R.id.ll_wwcdd);
        ll_ywcdd = getView().findViewById(R.id.ll_ywcdd);

        mRecyclerView = (RecyclerView) getView().findViewById(R.id.rlv_dingdan);
        LinearLayoutManager manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(manager);
        multiTypeAdapter = new MultiTypeAdapter(items);
        /* 注册 */
        multiTypeAdapter.register(Dingdan.class, new DingdanItemViewProvider(getContext()));
        multiTypeAdapter.register(ItemNullBean.class, new ItemNullProvider());
        multiTypeAdapter.register(ItemBottomBean.class, new ItemBottomProvider());
        mRecyclerView.setAdapter(multiTypeAdapter);
        //测试绑定
        assertHasTheSameAdapter(mRecyclerView, multiTypeAdapter);

        orderBeanList = new ArrayList<>();
    }
}