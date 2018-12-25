package com.ruyiruyi.ruyiruyi.ui.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.ruyiruyi.ruyiruyi.R;
import com.ruyiruyi.ruyiruyi.db.DbConfig;
import com.ruyiruyi.ruyiruyi.ui.activity.base.RyBaseActivity;
import com.ruyiruyi.ruyiruyi.ui.listener.OnLoadMoreListener;
import com.ruyiruyi.ruyiruyi.ui.multiType.ItemBottomProvider;
import com.ruyiruyi.ruyiruyi.ui.multiType.ItemNullProvider;
import com.ruyiruyi.ruyiruyi.ui.multiType.PointsChange;
import com.ruyiruyi.ruyiruyi.ui.multiType.PointsChangeViewBinder;
import com.ruyiruyi.ruyiruyi.ui.multiType.bean.ItemBottomBean;
import com.ruyiruyi.ruyiruyi.ui.multiType.bean.ItemNullBean;
import com.ruyiruyi.ruyiruyi.ui.multiType.divider.PointsGridDivider;
import com.ruyiruyi.ruyiruyi.utils.RequestUtils;
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

public class PointsChangeActivity extends RyBaseActivity {

    private ActionBar actionBar;
    private static final String TAG = PointsChangeActivity.class.getSimpleName();
    private RecyclerView mRecyclerView;
    private MultiTypeAdapter multiTypeAdapter;
    private List<Object> items = new ArrayList<>();
    private List<PointsChange> orderBeanList;
    private int total_all_page;
    private int mRows = 10;  // 设置默认一页加载10条数据
    private int current_page;
    private boolean isLoadMore = false;
    private boolean isLoadOver = false;
    private boolean isLoadMoreSingle = false;//上拉单次标志位
    private boolean isFirstLoad = true;
    private SwipeRefreshLayout mSwipeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_points_change);

        actionBar = (ActionBar) findViewById(R.id.acbar);
        actionBar.setTitle("积分兑换");
        actionBar.setBackground(0);
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

        initView();

        initFackData();
        initSwipeLayout();

    }

    private void initData() {
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
            object.put("userId", new DbConfig(getApplicationContext()).getId() + "");
            object.put("page", current_page);
            object.put("rows", mRows);
        } catch (JSONException e) {
        }
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "preferentialInfo/getUserShareRelationList");
        params.addBodyParameter("reqJson", object.toString());
        params.addBodyParameter("token", new DbConfig(getApplicationContext()).getToken());
        params.setConnectTimeout(6000);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess: result656 = " + result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONObject data = jsonObject.getJSONObject("data");
                    int totla = data.getInt("total");
                    total_all_page = totla / mRows;//处理页数
                    if (totla % mRows > 0) {
                        total_all_page++;
                    }
                    JSONArray rows = data.getJSONArray("rows");
                    for (int i = 0; i < rows.length(); i++) {
                        JSONObject order = (JSONObject) rows.get(i);
                        PointsChange bean = new PointsChange();
                        bean.setGoodsPic(order.getString("goodsPic"));
                        bean.setTitle(order.getString("title"));
                        bean.setPoints(order.getInt("points"));
                        bean.setGoodsId(order.getInt("goodsId"));
                        bean.setPrice(order.getDouble("price"));
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

    /**
     * 测试数据
     */
    private void initFackData() {
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

        total_all_page = 10;
        for (int i = 0; i < 10; i++) {
            PointsChange bean = new PointsChange();
            bean.setGoodsPic("http://180.76.243.205:8111/images/orderDefaultImg/ic_free.png");
            bean.setTitle("品牌玻璃水" + i);
            bean.setPoints(i);
            bean.setGoodsId(i);
            bean.setPrice(i);
            bean.setChangeNum(i);
            orderBeanList.add(bean);
        }


        //更新数据
        updataData();

        isLoadMoreSingle = false;//重置加载更多单次标志位


        //加载完成 隐藏加载动画
        if (isFirstLoad) {
//                    hideDialogProgress(startDialog);
            isFirstLoad = false;
        }


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
                    initFackData();
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
        initFackData();
    }

    private void initView() {
        mSwipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe);
        mRecyclerView = (RecyclerView) findViewById(R.id.rlv);
        LinearLayoutManager manager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false) {
            /*@Override
            public boolean canScrollVertically() {
                //解决ScrollView里存在多个RecyclerView时滑动卡顿的问题
                return false;
            }*/

            @Override
            public void onMeasure(RecyclerView.Recycler recycler, RecyclerView.State state, int widthSpec, int heightSpec) {
                super.onMeasure(recycler, state, widthSpec, heightSpec);
            }
        };
        //解决数据加载不完的问题
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setHasFixedSize(true);
        //解决数据加载完成后, 没有停留在顶部的问题
        mRecyclerView.setFocusable(false);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                mRecyclerView.setNestedScrollingEnabled(true);
            }
        });

        mRecyclerView.setLayoutManager(manager);

        multiTypeAdapter = new MultiTypeAdapter(items);
        /* 注册 */
        multiTypeAdapter.register(PointsChange.class, new PointsChangeViewBinder(this));
        multiTypeAdapter.register(ItemNullBean.class, new ItemNullProvider());
        multiTypeAdapter.register(ItemBottomBean.class, new ItemBottomProvider());

        mRecyclerView.addItemDecoration(new PointsGridDivider(this));
        mRecyclerView.setAdapter(multiTypeAdapter);
        //测试绑定
        assertHasTheSameAdapter(mRecyclerView, multiTypeAdapter);

        orderBeanList = new ArrayList<>();
    }
}
