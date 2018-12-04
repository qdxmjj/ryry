package com.ruyiruyi.ruyiruyi.ui.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.ruyiruyi.ruyiruyi.R;
import com.ruyiruyi.ruyiruyi.db.DbConfig;
import com.ruyiruyi.ruyiruyi.ui.activity.base.RyBaseActivity;
import com.ruyiruyi.ruyiruyi.ui.listener.OnLoadMoreListener;
import com.ruyiruyi.ruyiruyi.ui.multiType.ItemBottomProvider;
import com.ruyiruyi.ruyiruyi.ui.multiType.ItemNullProvider;
import com.ruyiruyi.ruyiruyi.ui.multiType.MyInviteAward;
import com.ruyiruyi.ruyiruyi.ui.multiType.MyInviteAwardViewBinder;
import com.ruyiruyi.ruyiruyi.ui.multiType.bean.ItemBottomBean;
import com.ruyiruyi.ruyiruyi.ui.multiType.bean.ItemNullBean;
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

import static com.baidu.mapapi.BMapManager.getContext;
import static me.drakeet.multitype.MultiTypeAsserts.assertAllRegistered;
import static me.drakeet.multitype.MultiTypeAsserts.assertHasTheSameAdapter;

public class MyInviteAwardActivity extends RyBaseActivity {
    private ActionBar actionBar;
    private static final String TAG = MyInviteAwardActivity.class.getSimpleName();
    private RecyclerView mRecyclerView;
    private MultiTypeAdapter multiTypeAdapter;
    private List<Object> items = new ArrayList<>();
    private List<MyInviteAward> orderBeanList;
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
        setContentView(R.layout.activity_my_invite_award);
        actionBar = (ActionBar) findViewById(R.id.my_acbar);
        actionBar.setTitle("我的奖品");
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

        initData();
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
            object.put("userId", new DbConfig(MyInviteAwardActivity.this).getId() + "");
            object.put("page", current_page);
            object.put("rows", mRows);
        } catch (JSONException e) {
        }
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "preferentialInfo/getUserCouponAndRewardList");
        params.addBodyParameter("reqJson", object.toString());
        params.addBodyParameter("token", new DbConfig(MyInviteAwardActivity.this).getToken());
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
                        MyInviteAward bean = new MyInviteAward();
                        JSONObject order = (JSONObject) rows.get(i);
                        bean.setTitleStr(order.getString("title"));
                        bean.setTypeStr(order.getString("type"));
                        bean.setTime(order.getLong("time"));
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

        for (int i = 0; i < 66; i++) {
            MyInviteAward myInviteAward = new MyInviteAward();
            myInviteAward.setTime(1212121121);
            myInviteAward.setType(3);
            myInviteAward.setPhone("100元现金红包" + i);
            myInviteAward.setImgUrl("图片");
            orderBeanList.add(myInviteAward);
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
    private void myDownRefreshByServer() {//下拉刷新
        initData();
    }

    private void initView() {
        mSwipeLayout = findViewById(R.id.swipe);
        mRecyclerView = (RecyclerView) findViewById(R.id.rlv);
        LinearLayoutManager manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(manager);
        multiTypeAdapter = new MultiTypeAdapter(items);
        /* 注册 */
        multiTypeAdapter.register(MyInviteAward.class, new MyInviteAwardViewBinder(getContext()));
        multiTypeAdapter.register(ItemNullBean.class, new ItemNullProvider());
        multiTypeAdapter.register(ItemBottomBean.class, new ItemBottomProvider());
        mRecyclerView.setAdapter(multiTypeAdapter);
        //测试绑定
        assertHasTheSameAdapter(mRecyclerView, multiTypeAdapter);

        orderBeanList = new ArrayList<>();
    }
}
