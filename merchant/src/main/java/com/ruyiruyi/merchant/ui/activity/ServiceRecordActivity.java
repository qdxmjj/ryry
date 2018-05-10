package com.ruyiruyi.merchant.ui.activity;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.ruyiruyi.merchant.R;
import com.ruyiruyi.merchant.bean.GoodsItemBean;
import com.ruyiruyi.merchant.bean.ItemBottomBean;
import com.ruyiruyi.merchant.bean.ItemNullBean;
import com.ruyiruyi.merchant.bean.ServiceRecordBean;
import com.ruyiruyi.merchant.ui.multiType.GoodsItemProvider;
import com.ruyiruyi.merchant.ui.multiType.ItemBottomProvider;
import com.ruyiruyi.merchant.ui.multiType.ItemNullProvider;
import com.ruyiruyi.merchant.ui.multiType.ServiceRecordItemProvider;
import com.ruyiruyi.merchant.ui.multiType.listener.OnLoadMoreListener;
import com.ruyiruyi.rylibrary.base.BaseActivity;
import com.ruyiruyi.rylibrary.cell.ActionBar;

import java.util.ArrayList;
import java.util.List;

import me.drakeet.multitype.MultiTypeAdapter;

import static me.drakeet.multitype.MultiTypeAsserts.assertAllRegistered;
import static me.drakeet.multitype.MultiTypeAsserts.assertHasTheSameAdapter;

public class ServiceRecordActivity extends BaseActivity {
    private SwipeRefreshLayout mSwipe;
    private RecyclerView mRlv;

    private MultiTypeAdapter multiTypeAdapter;
    private List<Object> items = new ArrayList<>();
    private String TAG = ServiceRecordActivity.class.getSimpleName();
    private int total_all_page;
    private int mRows = 10;  // 设置默认一页加载10条数据
    private int current_page;
    private boolean isLoadMore = false;
    private boolean isLoadOver = false;
    private List<ServiceRecordBean> itemBeanList;
    private ActionBar mActionBar;


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_record);

        mActionBar = (ActionBar) findViewById(R.id.service_record_acbar);
        mActionBar.setTitle("订单详情");
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
    }

    private void initData() {
        initDataByLoadMoreType();  //下载
    }

    private void initSwipeLayout() {
        mSwipe.setColorSchemeResources(//下拉刷新圆圈颜色
                R.color.theme_primary,
                R.color.c5,
                R.color.c6,
                R.color.c7
        );
        mSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //加载最新数据并更新adapter数据

                isLoadMore = false;
                myDownRefreshByServer();

                mSwipe.setRefreshing(false);
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
                    initDataByLoadMoreType();  //下载

                } else {
                    if (!isLoadOver) {
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
        initDataByLoadMoreType();  //下载
    }

    //公用下载
    private void initDataByLoadMoreType() {
        isLoadOver = false;

        if (!isLoadMore) {//只有加载更多(不清空原数据)
            itemBeanList.clear();
            current_page = 1;
        }

        //假数据
        for (int i = 0; i < 6; i++) {
            ServiceRecordBean bean = new ServiceRecordBean();
            bean.setService_xm("首次轮胎更换");
            bean.setService_store("小马驾驾");
            bean.setService_time("2018-05-09");
            bean.setService_luntai_a("NO.123456");
            bean.setService_luntai_b("NO.123456");
            bean.setService_luntai_c("NO.123456");
            bean.setService_luntai_d("NO.123456");
            bean.setType("scltgh");
            itemBeanList.add(bean);

            ServiceRecordBean bean2 = new ServiceRecordBean();
            bean2.setService_xm("免费修补");
            bean2.setService_store("小马驾驾");
            bean2.setService_time("2018-05-09");
            bean2.setService_luntai_a("NO.123456");
            bean2.setService_luntai_b("NO.123456");
            bean2.setService_luntai_c("NO.123456");
            bean2.setService_luntai_d("NO.123456");
            bean2.setType("mfxb");
            itemBeanList.add(bean2);

            ServiceRecordBean bean3 = new ServiceRecordBean();
            bean3.setService_xm("轮胎免费更换");
            bean3.setService_store("小马驾驾");
            bean3.setService_time("2018-05-09");
            bean3.setService_luntai_a("NO.123456");
            bean3.setService_luntai_b("NO.123456");
            bean3.setService_luntai_c("NO.123456");
            bean3.setService_luntai_d("NO.123456");
            bean3.setType("ltmfgh");
            itemBeanList.add(bean3);
        }

        upDataData();


    }

    private void upDataData() {
        //下载完成后操作
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
    }

    private void initView() {
        mSwipe = findViewById(R.id.service_record_swipe);
        mRlv = findViewById(R.id.service_record_rlv);

        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRlv.setLayoutManager(manager);
        multiTypeAdapter = new MultiTypeAdapter(items);
        ServiceRecordItemProvider provider = new ServiceRecordItemProvider(this);
        multiTypeAdapter.register(ServiceRecordBean.class, provider);
        multiTypeAdapter.register(ItemBottomBean.class, new ItemBottomProvider());
        multiTypeAdapter.register(ItemNullBean.class, new ItemNullProvider());
        mRlv.setAdapter(multiTypeAdapter);
        assertHasTheSameAdapter(mRlv, multiTypeAdapter);

        itemBeanList = new ArrayList<>();
    }


}
