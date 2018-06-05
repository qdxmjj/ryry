package com.ruyiruyi.merchant.ui.fragment;


import android.app.ProgressDialog;
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
import android.widget.Toast;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.ruyiruyi.merchant.R;
import com.ruyiruyi.merchant.bean.ItemBottomBean;
import com.ruyiruyi.merchant.bean.ItemNullBean;
import com.ruyiruyi.merchant.bean.StorePingJiaBean;
import com.ruyiruyi.merchant.db.DbConfig;
import com.ruyiruyi.merchant.ui.multiType.ItemBottomProvider;
import com.ruyiruyi.merchant.ui.multiType.ItemNullProvider;
import com.ruyiruyi.merchant.ui.multiType.StorePingJiaItemProvider;
import com.ruyiruyi.merchant.ui.multiType.listener.OnLoadMoreListener;
import com.ruyiruyi.merchant.utils.ImagPagerUtil;
import com.ruyiruyi.merchant.utils.UtilsRY;
import com.ruyiruyi.merchant.utils.UtilsURL;
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

import static me.drakeet.multitype.MultiTypeAsserts.assertAllRegistered;
import static me.drakeet.multitype.MultiTypeAsserts.assertHasTheSameAdapter;

public class StorePingJiaFragment extends BaseFragment implements StorePingJiaItemProvider.OnPingjiaPicClick {
    private RecyclerView mRlv;
    private SwipeRefreshLayout mSwipeLayout;
    private List<StorePingJiaBean> pingjiaBeanList;
    private MultiTypeAdapter multiTypeAdapter;
    private List<Object> items = new ArrayList<>();
    private String TAG = StorePingJiaFragment.class.getSimpleName();
    private int mRows = 10;// 设置默认一页加载10条数据
    private int currentPage;
    private int total_page;
    private String storeId;
    private boolean isLoadMore = false;
    private boolean isLoadOver = false;
    private boolean isLoadMoreSingle = false;//上拉单次标志位
    private ProgressDialog startDialog;
    private boolean isFirstLoad = true;


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
        //配置点击查看大图
        initImageLoader();

    }


    private void initImageLoader() {//配置点击查看大图
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                getActivity()).threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .writeDebugLogs() // Remove for release app
                .build();
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);

    }

    //加载原始数据
    private void initData() {
        initDataByLoadMoreType();
    }

    //公用下载
    private void initDataByLoadMoreType() {
        //数据加载完成前显示加载动画
        startDialog = new ProgressDialog(getContext());
        if (isFirstLoad) {
            showDialogProgress(startDialog, "信息加载中...");
        }

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
        params.setConnectTimeout(6000);
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
                        bean.setPingjia_id(object1.getInt("id"));
                        bean.setUser_name(object1.getString("storeCommitUserName"));
                        bean.setPj_txt(object1.getString("content"));
                        long time = object1.getLong("time");
                        String strings = new UtilsRY().getTimestampToStringAll(time);
                        String pjtime = strings.substring(0, 10);
                        bean.setPj_time(pjtime);

                        pingjiaBeanList.add(bean);
                    }

                    upDataData();//更新数据

                    isLoadMoreSingle = false;//重置加载更多单次标志位


                } catch (JSONException e) {
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(getContext(), "信息加载失败,请检查网络", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                //加载完成 隐藏加载动画
                if (isFirstLoad) {
                    hideDialogProgress(startDialog);
                    isFirstLoad = false;
                }
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
                if (isLoadMoreSingle) {
                    return;
                }
                isLoadMoreSingle = true;//上拉单次标志位

                if (total_page > currentPage) {
                    currentPage++;
                    items.add(new ItemBottomBean("加载更多..."));

                    isLoadMore = true;
                    initDataByLoadMoreType();
                } else {
                    if (!isLoadOver && (total_page > 1)) {//用于判断是否加  加载完成底部
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
        StorePingJiaItemProvider provider = new StorePingJiaItemProvider(getContext());
        provider.setListener(StorePingJiaFragment.this);
        multiTypeAdapter.register(StorePingJiaBean.class, provider);
        multiTypeAdapter.register(ItemNullBean.class, new ItemNullProvider());
        multiTypeAdapter.register(ItemBottomBean.class, new ItemBottomProvider());
        mRlv.setAdapter(multiTypeAdapter);
        assertHasTheSameAdapter(mRlv, multiTypeAdapter);

        pingjiaBeanList = new ArrayList<>();
        storeId = new DbConfig().getId() + "";
    }

    @Override
    public void onPingjiaPicClickListener(String url, int pjId) {
        List<String> picList = new ArrayList<>();
        picList.add(url);
        String content = "";
        for (int i = 0; i < pingjiaBeanList.size(); i++) {
            if (pingjiaBeanList.get(i).getPingjia_id() == pjId) { //根据id找到该条评论
                StorePingJiaBean beans = pingjiaBeanList.get(i);
                ArrayList<String> arrayList = new ArrayList<>();//url不为空 即有图片再加入
                if (beans.getPingjia_pica_url() == null || beans.getPingjia_pica_url().length() == 0) {
                } else {
                    arrayList.add(beans.getPingjia_pica_url());
                }
                if (beans.getPingjia_picb_url() == null || beans.getPingjia_picb_url().length() == 0) {
                } else {
                    arrayList.add(beans.getPingjia_picb_url());
                }
                if (beans.getPingjia_picc_url() == null || beans.getPingjia_picc_url().length() == 0) {
                } else {
                    arrayList.add(beans.getPingjia_picc_url());
                }
                if (beans.getPingjia_picd_url() == null || beans.getPingjia_picd_url().length() == 0) {
                } else {
                    arrayList.add(beans.getPingjia_picd_url());
                }
                if (beans.getPingjia_pice_url() == null || beans.getPingjia_pice_url().length() == 0) {
                } else {
                    arrayList.add(beans.getPingjia_pice_url());
                }

                content = beans.getPj_txt();

                for (int j = 0; j < arrayList.size(); j++) {
                    if (!arrayList.get(j).equals(url)) {
                        picList.add(arrayList.get(j));
                    }
                }

            }
        }
        ImagPagerUtil imagPagerUtil = new ImagPagerUtil(getActivity(), picList);
        imagPagerUtil.setContentText(content);
        imagPagerUtil.show();

    }
}