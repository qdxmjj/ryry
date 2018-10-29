package com.ruyiruyi.merchant.ui.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ruyiruyi.merchant.R;
import com.ruyiruyi.merchant.bean.ItemBottomBean;
import com.ruyiruyi.merchant.bean.ItemNullBean;
import com.ruyiruyi.merchant.db.DbConfig;
import com.ruyiruyi.merchant.ui.fragment.base.MerchantBaseFragment;
import com.ruyiruyi.merchant.ui.multiType.GoodsIncome;
import com.ruyiruyi.merchant.ui.multiType.GoodsIncomeViewBinder;
import com.ruyiruyi.merchant.ui.multiType.ItemBottomProvider;
import com.ruyiruyi.merchant.ui.multiType.ItemNullProvider;
import com.ruyiruyi.merchant.ui.multiType.listener.OnLoadMoreListener;
import com.ruyiruyi.merchant.utils.UtilsURL;
import com.ruyiruyi.rylibrary.utils.FormatDateUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import me.drakeet.multitype.MultiTypeAdapter;

import static me.drakeet.multitype.MultiTypeAsserts.assertAllRegistered;
import static me.drakeet.multitype.MultiTypeAsserts.assertHasTheSameAdapter;

public class GoodsIncomeFragment extends MerchantBaseFragment {

    private String TAG = GoodsIncomeFragment.class.getSimpleName();
    private View parentView;

    private SwipeRefreshLayout mSwipeLayout;
    private RecyclerView mRlv;
    private MultiTypeAdapter multiTypeAdapter;
    private List<Object> items = new ArrayList<>();
    private ArrayList<GoodsIncome> orderBeanList;
    private String currentYear;
    private String currentMonth;
    private int total_all_page;
    private int mRows = 10;  // 设置默认一页加载10条数据
    private int current_page;
    private boolean isLoadMore = false;
    private boolean isLoadOver = false;
    private boolean isLoadMoreSingle = false;//上拉单次标志位

    private GoodsForMainIncome listener;

    public void setListener(GoodsForMainIncome listener) {
        this.listener = listener;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (parentView == null) {
            parentView = inflater.inflate(R.layout.goodsincome_fg, container, false);
        } else {
            ViewGroup viewGroup = (ViewGroup) parentView.getParent();
            if (viewGroup != null)
                viewGroup.removeView(parentView);
        }
        return parentView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Bundle incomeBundle = getArguments();
        currentYear = incomeBundle.getString("year");
        currentMonth = incomeBundle.getString("month");


        initView();
        initData();
        initSwipeLayout();

    }

    private void initData() {
        //post
        isLoadOver = false;

        if (!isLoadMore) {//只有加载更多(不清空原数据)
            orderBeanList.clear();
            current_page = 1;
        }

        JSONObject object = new JSONObject();
        try {
            object.put("storeId", new DbConfig(getContext()).getId());
            String formatMonth = new FormatDateUtil().formatMonthOrDay(Integer.parseInt(currentMonth));
            object.put("date", currentYear + "-" + formatMonth + "-01 00:00:00");
            object.put("page", current_page);
            object.put("rows", mRows);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestParams params = new RequestParams(UtilsURL.REQUEST_URL + "getStoreSaleGoodsEarnings");
        params.addBodyParameter("reqJson", object.toString());
        Log.e(TAG, "initData:  params.toString() = " + params.toString());
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess: result = " + result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONObject data = jsonObject.getJSONObject("data");
                    int numtotal = data.getInt("total");
                    total_all_page = (numtotal) / mRows;//处理页数
                    if (numtotal % mRows > 0) {
                        total_all_page++;
                    }
                    JSONArray rows = data.getJSONArray("rows");
                    for (int i = 0; i < rows.length(); i++) {
                        JSONObject objbean = (JSONObject) rows.get(i);
                        GoodsIncome goodsIncome = new GoodsIncome();
                        /*goodsIncome.setOrderImg(objbean.getString(""));*/
                        /*goodsIncome.setOrderType(objbean.getString("shoeName"));*/
                        goodsIncome.setOrderTime(objbean.getLong("time"));
                        goodsIncome.setOrderPrice(objbean.getDouble("actuallyPrice"));

                        goodsIncome.setOrderNo(objbean.getString("no"));

                        orderBeanList.add(goodsIncome);
                    }

                    //更新数据
                    updataData();

                    isLoadMoreSingle = false;//重置加载更多单次标志位

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                //网络异常
                updataNetError();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    /**
     * 假数据
     */
    private void initFackData() {

        isLoadOver = false;

        if (!isLoadMore) {//只有加载更多(不清空原数据)
            orderBeanList.clear();
            current_page = 1;
        }


        Random random = new Random();
        int anInt = random.nextInt(100);
        for (int i = 0; i < mRows; i++) {
            GoodsIncome goodsIncome = new GoodsIncome();
            goodsIncome.setOrderNo(anInt + " 123456789+" + i);
            goodsIncome.setOrderPrice(6666 + i);
            goodsIncome.setOrderImg("http://180.76.243.205:8111/images-new/tyreInfo/tyreTotal/RA710-B.jpg");
            goodsIncome.setOrderType("普通商品订单" + i + currentYear + currentMonth);
            goodsIncome.setOrderTime(1506673915000l);
            orderBeanList.add(goodsIncome);
        }
        total_all_page = 10;


        //更新数据
        updataData();

        isLoadMoreSingle = false;//重置加载更多单次标志位


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

    private void updataNetError() {
        items.clear();
        items.add(new ItemNullBean(R.drawable.ic_net_error));
        assertAllRegistered(multiTypeAdapter, items);
        multiTypeAdapter.notifyDataSetChanged();

    }


    private void initView() {
        mSwipeLayout = getView().findViewById(R.id.swipe_goodsincome);
        mRlv = getView().findViewById(R.id.rlv_goodsincome);
        LinearLayoutManager manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mRlv.setLayoutManager(manager);
        multiTypeAdapter = new MultiTypeAdapter(items);

        GoodsIncomeViewBinder goodsIncomeViewBinder = new GoodsIncomeViewBinder(getActivity());
        multiTypeAdapter.register(GoodsIncome.class, goodsIncomeViewBinder);
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
                    initData();
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
        initData();
        //接口回调通知总收益刷新
        listener.goodsforMianIncome();
        Log.e(TAG, "refresh:  listener.goodsforMianIncome() ");
    }

    public interface GoodsForMainIncome {
        void goodsforMianIncome();
    }
}