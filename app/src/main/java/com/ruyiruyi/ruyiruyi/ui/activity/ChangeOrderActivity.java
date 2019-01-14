package com.ruyiruyi.ruyiruyi.ui.activity;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.ruyiruyi.ruyiruyi.R;
import com.ruyiruyi.ruyiruyi.db.DbConfig;
import com.ruyiruyi.ruyiruyi.ui.activity.base.RyBaseActivity;
import com.ruyiruyi.ruyiruyi.ui.listener.OnLoadMoreListener;
import com.ruyiruyi.ruyiruyi.ui.multiType.EmptyBig;
import com.ruyiruyi.ruyiruyi.ui.multiType.EmptyBigViewBinder;
import com.ruyiruyi.ruyiruyi.ui.multiType.ExchangeGoodsOrder;
import com.ruyiruyi.ruyiruyi.ui.multiType.ExchangeGoodsOrderViewBinder;
import com.ruyiruyi.ruyiruyi.ui.multiType.LoadMore;
import com.ruyiruyi.ruyiruyi.ui.multiType.LoadMoreViewBinder;
import com.ruyiruyi.ruyiruyi.ui.multiType.UserEvaluate;
import com.ruyiruyi.ruyiruyi.utils.RequestUtils;
import com.ruyiruyi.ruyiruyi.utils.UtilsRY;
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

public class ChangeOrderActivity extends RyBaseActivity implements ExchangeGoodsOrderViewBinder.OnOrderClickiListener {

    private static final String TAG = ChangeOrderActivity.class.getSimpleName();
    private ActionBar actionBar;
    private RecyclerView listView;
    private List<Object> items = new ArrayList<>();
    private MultiTypeAdapter adapter;

    public int currentPage = 1;
    public int allPager = 1;
    public int currentPageCount = 10;
    public List<ExchangeGoodsOrder> orderList;
    public boolean isCleanData = false;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_order);
        actionBar = (ActionBar) findViewById(R.id.my_action);
        actionBar.setTitle("订单列表");
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
        orderList = new ArrayList<>();

        initView();

        initDataFromService();
    }

    private void initDataFromService() {
        int userId = new DbConfig(this).getId();
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL_JIFEN + "score/order");
        String token = new DbConfig(this).getToken();
        params.addParameter("userId", userId);
        params.addParameter("page", currentPage);
        params.addParameter("rows", currentPageCount);
        params.addParameter("token", token);
        Log.e(TAG, "initDataFromService:--- " + params);
        x.http().get(params, new Callback.CommonCallback<String>() {
            private int total;
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess:---- " + result);
                JSONObject jsonObject1 = null;
                try {
                    jsonObject1 = new JSONObject(result);
                    String status = jsonObject1.getString("status");
                    String msg = jsonObject1.getString("msg");
                    if (status.equals("1")) {
                        JSONObject data = jsonObject1.getJSONObject("data");
                        total = data.getInt("total");
                        //页数计算
                        if (total % currentPageCount > 0) {
                            allPager = (total / currentPageCount) + 1;
                        } else {
                            allPager = total / currentPageCount;
                        }

                        orderList.clear();
                        JSONArray items = data.getJSONArray("items");
                        for (int i = 0; i < items.length(); i++) {
                            JSONObject object = items.getJSONObject(i);
                            String orderNo = object.getString("orderNo");
                            String orderTime = object.getString("orderTime");
                            String orderType = object.getString("orderType");
                            String orderStatus = object.getString("orderStatus");

                            JSONObject scoreSku = object.getJSONObject("scoreSku");
                            int id = scoreSku.getInt("id");
                            String goodsNamre = scoreSku.getString("name");
                            String imgUrl = scoreSku.getString("imgUrl");
                            String score = scoreSku.getString("score");
                            String price = scoreSku.getString("price");
                            ExchangeGoodsOrder exchangeGoodsOrder = new ExchangeGoodsOrder(id, orderTime, orderNo, imgUrl, goodsNamre, price, score, orderType, orderStatus);
                            orderList.add(exchangeGoodsOrder);

                        }

                        initdata();
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

    private void initdata() {
        if (isCleanData) {
            items.clear();
        }
        if (orderList.size() == 0 && currentPage ==1){
            items.add(new EmptyBig());
        }else {
            if (items.size() > 0) {
                items.remove(items.size() - 1);
            }
            for (int i = 0; i < orderList.size(); i++) {
                items.add(orderList.get(i));
            }
            if (allPager > 1) {
                if (allPager == currentPage) {
                    items.add(new LoadMore("全部加载完毕！"));
                } else {
                    items.add(new LoadMore("加载更多...."));
                }
            }
        }
        isCleanData = false;
        assertAllRegistered(adapter, items);
        adapter.notifyDataSetChanged();
    }

    private void initView() {
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipre_refresh_layout);
        swipeRefreshLayout.setProgressViewEndTarget(true, 200);

        listView = (RecyclerView) findViewById(R.id.change_order_listview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        listView.setLayoutManager(linearLayoutManager);
        adapter = new MultiTypeAdapter(items);
        register();

        listView.setAdapter(adapter);
        assertHasTheSameAdapter(listView, adapter);
        //下拉刷新
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //刷新处理
                isCleanData = true;
                currentPage = 1;
                initDataFromService();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        //加载更多
        listView.setOnScrollListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (allPager > currentPage) {
                    currentPage += 1;
                    isCleanData = false;
                    initDataFromService();
                }
            }
        });

    }

    private void register() {
        ExchangeGoodsOrderViewBinder exchangeGoodsOrderViewBinder = new ExchangeGoodsOrderViewBinder(this);
        exchangeGoodsOrderViewBinder.setListener(this);
        adapter.register(ExchangeGoodsOrder.class, exchangeGoodsOrderViewBinder);
        adapter.register(LoadMore.class, new LoadMoreViewBinder());
        adapter.register(EmptyBig.class, new EmptyBigViewBinder());
    }

    @Override
    public void onWuliuClickListener(String goodsName, String goodsImage, String orderNo) {
        Intent intent = new Intent(this,WuLiuActivity.class);
        intent.putExtra("GOODS_NAME",goodsName);
        intent.putExtra("GOODS_IMAGE",goodsImage);
        intent.putExtra("ORDER_NO",orderNo);
        startActivity(intent);
    }
}
