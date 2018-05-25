package com.ruyiruyi.merchant.ui.fragment;


import android.app.Activity;
import android.content.Context;
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
import com.ruyiruyi.merchant.db.DbConfig;
import com.ruyiruyi.merchant.ui.activity.MyGoodsActivity;
import com.ruyiruyi.merchant.ui.multiType.GoodsItemProvider;
import com.ruyiruyi.merchant.ui.multiType.ItemBottomProvider;
import com.ruyiruyi.merchant.ui.multiType.ItemNullProvider;
import com.ruyiruyi.merchant.ui.multiType.listener.OnLoadMoreListener;
import com.ruyiruyi.merchant.utils.UtilsURL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.functions.Function;
import me.drakeet.multitype.MultiTypeAdapter;
import rx.android.schedulers.AndroidSchedulers;

import static me.drakeet.multitype.MultiTypeAsserts.assertAllRegistered;
import static me.drakeet.multitype.MultiTypeAsserts.assertHasTheSameAdapter;

public class MyGoodsFragment extends Fragment {
    public static String SALE_TYPE = "SALE_TYPE";
    public static String LEFT_ID = "LEFT_ID";
    public static String RIGHT_ID = "RIGHT_ID";
    private String leftid;
    private String rightid;
    private int total_all_page;
    private int mRows = 10;  // 设置默认一页加载10条数据
    private int current_page;
    private String sale_type;
    private SwipeRefreshLayout mSwipeLayout;
    private RecyclerView mRlv;
    private MultiTypeAdapter multiTypeAdapter;
    private List<Object> items = new ArrayList<>();
    private String TAG = MyGoodsFragment.class.getSimpleName();
    private List<GoodsItemBean> itemBeanList;
    private boolean isLoadMore = false;
    private boolean isLoadOver = false;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    if (itemBeanList == null || itemBeanList.size() == 0) {
                        items.clear();
                        items.add(new ItemNullBean("暂无数据"));
                        assertAllRegistered(multiTypeAdapter, items);
                        multiTypeAdapter.notifyDataSetChanged();
                    } else {
                        items.clear();
                        for (int i = 0; i < itemBeanList.size(); i++) {
                            items.add(itemBeanList.get(i));
                        }
                        assertAllRegistered(multiTypeAdapter, items);
                        multiTypeAdapter.notifyDataSetChanged();
                    }
                    break;
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.mygoods_list_fg, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        Bundle bundle = getArguments();
        sale_type = "";
        leftid = "";
        rightid = "";
        sale_type = bundle.getString(SALE_TYPE);
        leftid = bundle.getString(LEFT_ID);
        rightid = bundle.getString(RIGHT_ID);
        Log.e(TAG, "onActivityCreated: 6668" + "  bundle.getString(LEFT_ID)= " + bundle.getString(LEFT_ID) + " bundle.getString(RIGHT_ID)= " + bundle.getString(RIGHT_ID));
        Log.e(TAG, "onActivityCreated: 6668" + " leftid= " + leftid + " rightid= " + rightid);
        Log.e(TAG, "onActivityCreated:6668 sale_type = " + sale_type);
        itemBeanList = new ArrayList<>();

        initView();
        initData();
        initSwipeLayout();

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
        mRlv.setOnScrollListener(new OnLoadMoreListener() {

            @Override
            public void onLoadMore() {
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

    //公用下载
    private void initDataByLoadMoreType() {
        isLoadOver = false;

        if (!isLoadMore) {//只有加载更多(不清空原数据)
            itemBeanList.clear();
            current_page = 1;
        }

        //下载数据
        JSONObject object = new JSONObject();
        String storeId = new DbConfig().getId() + "";
        try {
            object.put("storeId", storeId);
            object.put("serviceTypeId", leftid);//空为查询所有数据
            object.put("servicesId", rightid);
            object.put("page", current_page);
            object.put("rows", mRows);
            switch (sale_type) {//上架下架状态 1 yes    2 no
                case "ONSALE":
                    object.put("stockStatus", "1");
                    Log.e(TAG, "passToEnterFragment:222 ONSALE sale_type=" + sale_type);
                    break;
                case "NOSALE":
                    object.put("stockStatus", "2");
                    Log.e(TAG, "passToEnterFragment:222 NOSALE sale_type=" + sale_type);
                    break;
            }
        } catch (JSONException e) {
        }
        RequestParams params = new RequestParams(UtilsURL.REQUEST_URL + "getStockByCondition");
        params.addBodyParameter("reqJson", object.toString());
        Log.e(TAG, "initDataByLoadMoreType: oovm" + params.toString());
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess:oovm " + result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONObject data = jsonObject.getJSONObject("data");
                    JSONArray rows = data.getJSONArray("rows");
                    total_all_page = (data.getInt("total")) / mRows;//处理页数
                    if (data.getInt("total") % mRows > 0) {
                        total_all_page++;
                    }
                    for (int i = 0; i < rows.length(); i++) {
                        JSONObject one = (JSONObject) rows.get(i);
                        GoodsItemBean itemBean = new GoodsItemBean();
                        itemBean.setAmount(one.getInt("amount"));
                        itemBean.setId(one.getInt("id"));
                        itemBean.setImgUrl(one.getString("imgUrl"));
                        itemBean.setName(one.getString("name"));
                        itemBean.setPrice(one.getDouble("price"));
                        itemBean.setServiceId(one.getInt("serviceId"));
                        itemBean.setServiceTypeId(one.getInt("serviceTypeId"));
                        itemBean.setSoldNo(one.getInt("soldNo"));
                        itemBean.setStatus(one.getInt("status"));
                        itemBean.setStockTypeId(one.getInt("stockTypeId"));
                        itemBean.setStoreId(one.getInt("storeId"));
                        itemBean.setTime(one.getLong("time"));
                        Log.e(TAG, "onSuccess:333 1 itemBean.getName() = " + itemBean.getName());

                        itemBeanList.add(itemBean);
                    }
                    //更新适配器数据 handler 中进行
                    Message message = new Message();
                    message.what = 1;
                    mHandler.sendMessage(message);

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

    //下拉刷新
    private void myDownRefreshByServer() {//下拉刷新
        initDataByLoadMoreType();
    }

    //加载原始数据
    private void initData() {
        initDataByLoadMoreType();
    }

    private void initView() {
        mSwipeLayout = getView().findViewById(R.id.mygoods_swipelayout);
        mRlv = getView().findViewById(R.id.mygoods_rlv);

        LinearLayoutManager manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mRlv.setLayoutManager(manager);
        multiTypeAdapter = new MultiTypeAdapter(items);
        GoodsItemProvider provider = new GoodsItemProvider(getActivity());
  /*      provider.setListener(this);//绑定*/
        multiTypeAdapter.register(GoodsItemBean.class, provider);
        multiTypeAdapter.register(ItemBottomBean.class, new ItemBottomProvider());
        multiTypeAdapter.register(ItemNullBean.class, new ItemNullProvider());
        mRlv.setAdapter(multiTypeAdapter);
        assertHasTheSameAdapter(mRlv, multiTypeAdapter);
    }

    /*    //接口回调方法
    @Override
    public void passtoFG(String id) {

    }*/
}