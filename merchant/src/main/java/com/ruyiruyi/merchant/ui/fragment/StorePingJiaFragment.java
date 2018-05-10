package com.ruyiruyi.merchant.ui.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import com.ruyiruyi.merchant.bean.StorePingJiaBean;
import com.ruyiruyi.merchant.db.DbConfig;
import com.ruyiruyi.merchant.ui.multiType.ItemBottomProvider;
import com.ruyiruyi.merchant.ui.multiType.ItemNullProvider;
import com.ruyiruyi.merchant.ui.multiType.StorePingJiaItemProvider;
import com.ruyiruyi.merchant.ui.multiType.listener.OnLoadMoreListener;
import com.ruyiruyi.merchant.utils.UtilsRY;
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

public class StorePingJiaFragment extends Fragment {
    private RecyclerView mRlv;
    private SwipeRefreshLayout mSwipeLayout;
    private List<StorePingJiaBean> pingjiaBeanList;
    private MultiTypeAdapter multiTypeAdapter;
    private List<Object> items = new ArrayList<>();
    private String TAG = StorePingJiaFragment.class.getSimpleName();
    private int mRows = 10;
    private int currentPage ;
    private int total_page;
    private String storeId;
    private boolean isLoadMore = false;
    private boolean isLoadOver = false;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.store_pingjia_fg, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

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
        isLoadOver = false;

        if (!isLoadMore) {//只有加载更多(不清空原数据)
            pingjiaBeanList.clear();
            currentPage = 1;
        }

        //获取评论数据
        JSONObject object = new JSONObject();
        try {
            object.put("storeId", storeId);
            object.put("userId", "");
            object.put("page", currentPage);
            object.put("rows", mRows);
        } catch (JSONException e) {
        }
        RequestParams params = new RequestParams(UtilsURL.REQUEST_URL + "getCommitByCondition");
        params.addBodyParameter("reqJson", object.toString());
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONObject data = jsonObject.getJSONObject("data");
                    String msg = jsonObject.getString("msg");
                    int status = jsonObject.getInt("status");
                    JSONArray rows = data.getJSONArray("rows");
                    total_page = data.getInt("total") / mRows;
                    if (data.getInt("total") % mRows > 0) {
                        total_page++;
                    }
                    for (int i = 0; i < rows.length(); i++) {
                        JSONObject object1 = (JSONObject) rows.get(i);
                        StorePingJiaBean bean = new StorePingJiaBean();
                        bean.setUser_img_url(object1.getString("storeCommitUserHeadImg"));
                        bean.setPingjia_pica_url(object1.getString("img1Url"));
                        bean.setPingjia_picb_url(object1.getString("img2Url"));
                        bean.setPingjia_picc_url(object1.getString("img3Url"));
                        bean.setPingjia_picd_url(object1.getString("img4Url"));
                        bean.setPingjia_pice_url(object1.getString("img5Url"));
                        bean.setUser_id(object1.getInt("userId"));
                        bean.setUser_name(object1.getString("storeCommitUserName"));
                        bean.setPj_txt(object1.getString("content"));
                        long time = object1.getLong("time");
                        String strings = new UtilsRY().getTimestampToStringAll(time);
                        String pjtime = strings.substring(0, 10);
                        bean.setPj_time(pjtime);
                        pingjiaBeanList.add(bean);
                    }

                    upDataData();//更新数据


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

    //更新数据
    private void upDataData() {
        if (pingjiaBeanList == null || pingjiaBeanList.size() == 0) {
            items.clear();
            items.add(new ItemNullBean("暂无数据"));
            assertAllRegistered(multiTypeAdapter, items);
            multiTypeAdapter.notifyDataSetChanged();
        } else {
            items.clear();
            for (int i = 0; i < pingjiaBeanList.size(); i++) {
                items.add(pingjiaBeanList.get(i));
            }
            assertAllRegistered(multiTypeAdapter, items);
            multiTypeAdapter.notifyDataSetChanged();
        }
    }

    //下拉刷新
    private void myDownRefreshByServer() {//下拉刷新
        initDataByLoadMoreType();
    }


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
                if (total_page > currentPage) {
                    currentPage++;
                    items.add(new ItemBottomBean("加载更多..."));

                    isLoadMore = true;
                    initDataByLoadMoreType();
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

    private void initView() {
        mRlv = (RecyclerView) getView().findViewById(R.id.store_pingjia_rlv);
        mSwipeLayout = getView().findViewById(R.id.pingjia_swipelayout);

        LinearLayoutManager manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mRlv.setLayoutManager(manager);
        multiTypeAdapter = new MultiTypeAdapter(items);
        multiTypeAdapter.register(StorePingJiaBean.class, new StorePingJiaItemProvider(getContext()));
        multiTypeAdapter.register(ItemNullBean.class, new ItemNullProvider());
        multiTypeAdapter.register(ItemBottomBean.class, new ItemBottomProvider());
        mRlv.setAdapter(multiTypeAdapter);
        assertHasTheSameAdapter(mRlv, multiTypeAdapter);

        pingjiaBeanList = new ArrayList<>();
        storeId = new DbConfig().getId() + "";
    }
}