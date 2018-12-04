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
import com.ruyiruyi.ruyiruyi.ui.multiType.PutForwardInfo;
import com.ruyiruyi.ruyiruyi.ui.multiType.PutForwardInfoViewBinder;
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
import java.util.Random;

import me.drakeet.multitype.MultiTypeAdapter;

import static com.baidu.mapapi.BMapManager.getContext;
import static me.drakeet.multitype.MultiTypeAsserts.assertAllRegistered;
import static me.drakeet.multitype.MultiTypeAsserts.assertHasTheSameAdapter;

public class PutForwardInfoActivity extends RyBaseActivity {

    private ActionBar mActionBar;
    private SwipeRefreshLayout mSwipeLayout;
    private RecyclerView mRlv;
    private int total_all_page;
    private int mRows = 14;  // 设置默认一页加载10条数据
    private int current_page;
    private MultiTypeAdapter multiTypeAdapter;
    private List<Object> items = new ArrayList<>();
    private String TAG = PutForwardInfoActivity.class.getSimpleName();
    private List<PutForwardInfo> orderBeanList;
    private boolean isLoadMore = false;
    private boolean isLoadOver = false;
    private boolean isLoadMoreSingle = false;//上拉单次标志位

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_put_forward_info);
        mActionBar = (ActionBar) findViewById(R.id.acbars);
        mActionBar.setTitle("收支明细");
        mActionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(int var1) {
                switch (var1) {
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

        isLoadOver = false;

        if (!isLoadMore) {//只有加载更多(不清空原数据)
            orderBeanList.clear();
            current_page = 1;
        }

        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL_FAHUO + "incomeInfo/queryUserIncomeAndExpensesInfo");
        params.addBodyParameter("userId", new DbConfig(PutForwardInfoActivity.this).getId() + "");
        params.addBodyParameter("page", current_page + "");
        params.addBodyParameter("rows", mRows + "");
        Log.e(TAG, "initData: params.toString() info = " + params.toString());
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess: result = " + result);
                try {
                    JSONObject object = new JSONObject(result);
                    JSONArray rows = object.getJSONArray("rows");
                    total_all_page = (object.getInt("total")) / mRows;//处理页数
                    if (object.getInt("total") % mRows > 0) {
                        total_all_page++;
                    }
                    for (int i = 0; i < rows.length(); i++) {
                        JSONObject obj = (JSONObject) rows.get(i);
                        PutForwardInfo putForwardInfo = new PutForwardInfo();
                        putForwardInfo.setPutForwardType(obj.getInt("expensesType"));//支出类型(1 支付宝 2 微信)
                        putForwardInfo.setPutForwardMoney(obj.getDouble("money"));
                        putForwardInfo.setPutForwardStatus(obj.getInt("status"));//提现状态（1 提现中 2 成功 3 失败）
                        putForwardInfo.setPutForwardTime(obj.getLong("time"));
                        putForwardInfo.setRemark(obj.getString("remark"));
                        putForwardInfo.setBigType(obj.getInt("type"));//收支类型 1 支出 2 收入
                        putForwardInfo.setOrderNo(obj.getString("orderNo"));
                        orderBeanList.add(putForwardInfo);
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
            PutForwardInfo putForwardInfo = new PutForwardInfo();
            putForwardInfo.setPutForwardType(1);
            putForwardInfo.setPutForwardMoney(888.88 + i);
            if (i % 3 == 0) {
                putForwardInfo.setPutForwardStatus(1);
                putForwardInfo.setRemark("提现申请提交后预计7个工作日内到账，请耐心等待");
            } else if (i % 3 == 1) {
                putForwardInfo.setPutForwardStatus(2);
                putForwardInfo.setRemark("提现金额到账成功，请注意查收");
            } else if (i % 3 == 2) {
                putForwardInfo.setPutForwardStatus(3);
                putForwardInfo.setRemark("微信账号实名信息与提现申请姓名不一致");
            }
            putForwardInfo.setOrderNo("123464645454321");
            putForwardInfo.setPutForwardTime(1506673915000l);
            orderBeanList.add(putForwardInfo);
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

    /*
    //网络异常
    updataNetError();
    * */
    private void updataNetError() {
        items.clear();
        items.add(new ItemNullBean(R.drawable.ic_net_error));
        assertAllRegistered(multiTypeAdapter, items);
        multiTypeAdapter.notifyDataSetChanged();

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
    }


    private void initView() {
        mSwipeLayout = findViewById(R.id.putforward_swipelayout);
        mRlv = findViewById(R.id.putforward_rlv);
        LinearLayoutManager manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mRlv.setLayoutManager(manager);
        multiTypeAdapter = new MultiTypeAdapter(items);

        PutForwardInfoViewBinder putForwardInfoViewBinder = new PutForwardInfoViewBinder(this);
        multiTypeAdapter.register(PutForwardInfo.class, putForwardInfoViewBinder);

        mRlv.setAdapter(multiTypeAdapter);
        assertHasTheSameAdapter(mRlv, multiTypeAdapter);
        multiTypeAdapter.register(ItemNullBean.class, new ItemNullProvider());
        multiTypeAdapter.register(ItemBottomBean.class, new ItemBottomProvider());

        orderBeanList = new ArrayList<>();
    }
}
