package com.ruyiruyi.ruyiruyi.ui.activity;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.ruyiruyi.ruyiruyi.R;
import com.ruyiruyi.ruyiruyi.db.DbConfig;
import com.ruyiruyi.ruyiruyi.ui.activity.base.RyBaseActivity;
import com.ruyiruyi.ruyiruyi.ui.listener.OnLoadMoreListener;
import com.ruyiruyi.ruyiruyi.ui.multiType.Empty;
import com.ruyiruyi.ruyiruyi.ui.multiType.EmptyBig;
import com.ruyiruyi.ruyiruyi.ui.multiType.EmptyBigViewBinder;
import com.ruyiruyi.ruyiruyi.ui.multiType.EmptyViewBinder;
import com.ruyiruyi.ruyiruyi.ui.multiType.EvaImageViewBinder;
import com.ruyiruyi.ruyiruyi.ui.multiType.LoadMore;
import com.ruyiruyi.ruyiruyi.ui.multiType.LoadMoreViewBinder;
import com.ruyiruyi.ruyiruyi.ui.multiType.UserEvaluate;
import com.ruyiruyi.ruyiruyi.ui.multiType.UserEvaluateViewBinder;
import com.ruyiruyi.ruyiruyi.utils.ImagPagerUtil;
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

public class ShopEvaluateActivity extends RyBaseActivity implements UserEvaluateViewBinder.OnImageItemClick {
    private static final String TAG = ShopEvaluateActivity.class.getSimpleName();
    private ActionBar actionBar;
    private RecyclerView listView;
    private List<Object> items = new ArrayList<>();
    private MultiTypeAdapter adapter;
    public List<UserEvaluate> userEvaluateList;
    private SwipeRefreshLayout swipeRefreshLayout;
    private int storeid = 0;
    private int userId = 0;
    public int currentPage = 1;
    public int allPager = 1;
    public int currentPageCount = 10;
    public boolean isCleanData = false;
    public static String EVALUATE_TYPE = "EVALUATE_TYPE";
    public int evaluateType = 0; //0是门店评价  1是我的评价
    public boolean isFirstGetData = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_evaluate, R.id.my_action);
        actionBar = (ActionBar) findViewById(R.id.my_action);

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
        Intent intent = getIntent();
        evaluateType = intent.getIntExtra(EVALUATE_TYPE, 0);//0是门店评价  1是我的评价
        if (evaluateType == 0) {
            storeid = intent.getIntExtra("STOREID", 0);
            actionBar.setTitle("门店评价");
            ;
        } else {
            userId = intent.getIntExtra("USERID", 0);
            actionBar.setTitle("我的评价");
            ;
        }

        userEvaluateList = new ArrayList<>();
        isCleanData = true;
        isFirstGetData = true;

        initView();
        initDataFromService();


        //  initdata();
        //配置点击查看大图
        initImageLoader();
    }

    private void initDataFromService() {
        JSONObject jsonObject = new JSONObject();
        try {
            if (evaluateType == 0) {
                jsonObject.put("storeId", storeid);
                jsonObject.put("userId", "");
            } else {
                jsonObject.put("storeId", "");
                jsonObject.put("userId", userId);
            }

            jsonObject.put("page", currentPage);
            jsonObject.put("rows", currentPageCount);
        } catch (JSONException e) {
        }
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "getCommitByCondition");
        Log.e(TAG, "initDataFromService: " + jsonObject.toString());
        params.addBodyParameter("reqJson", jsonObject.toString());
        String token = new DbConfig(this).getToken();
        params.addParameter("token", token);
        x.http().post(params, new Callback.CommonCallback<String>() {
            private int total;

            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess: " + result.toString());
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
                        JSONArray rows = data.getJSONArray("rows");
                        userEvaluateList.clear();
                        for (int i = 0; i < rows.length(); i++) {
                            JSONObject object = rows.getJSONObject(i);
                            int id = object.getInt("id");
                            int starNo = object.getInt("starNo");
                            String userImage = object.getString("storeCommitUserHeadImg");
                            String usetName = object.getString("storeCommitUserName");
                            Long evaluateTime = object.getLong("time");
                            String evaluateTimeStr = new UtilsRY().getTimestampToString(evaluateTime);
                            String evaluateContent = object.getString("content");
                            String img1Url = object.getString("img1Url");
                            String img2Url = object.getString("img2Url");
                            String img3Url = object.getString("img3Url");
                            String img4Url = object.getString("img4Url");
                            String img5Url = object.getString("img5Url");
                            String storeAddress = object.getString("storeAddress");
                            String storeName = object.getString("storeName");
                            String storeImg = object.getString("storeImg");
                            List<String> imageList = new ArrayList<String>();
                            if (!img1Url.equals("")) {
                                imageList.add(img1Url);
                            }
                            if (!img2Url.equals("")) {
                                imageList.add(img2Url);
                            }
                            if (!img3Url.equals("")) {
                                imageList.add(img3Url);
                            }
                            if (!img4Url.equals("")) {
                                imageList.add(img4Url);
                            }
                            if (!img5Url.equals("")) {
                                imageList.add(img5Url);
                            }
                            UserEvaluate userEvaluate = new UserEvaluate(id, starNo,userImage, usetName, evaluateTimeStr, evaluateContent, imageList,storeImg,storeName,storeAddress);
                            userEvaluateList.add(userEvaluate);
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

    private void initImageLoader() {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                getApplicationContext()).threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .writeDebugLogs() // Remove for release app
                .build();
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);
    }

    private void initView() {
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipre_refresh_layout);
        swipeRefreshLayout.setProgressViewEndTarget(true, 200);

        listView = (RecyclerView) findViewById(R.id.evaluate_listview);
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
        UserEvaluateViewBinder userEvaluateViewBinder = new UserEvaluateViewBinder(this);
        userEvaluateViewBinder.setListener(this);
        userEvaluateViewBinder.setEvaluateType(evaluateType);
        adapter.register(UserEvaluate.class, userEvaluateViewBinder);
        adapter.register(Empty.class, new EmptyViewBinder());
        adapter.register(EmptyBig.class, new EmptyBigViewBinder());
        adapter.register(LoadMore.class, new LoadMoreViewBinder());
    }

    private void initdata() {
        if (isCleanData) {
            items.clear();
        }
        //items.clear();
        //  items.add(new ShopStr("门店评价"));
        if (userEvaluateList.size() == 0 && currentPage == 1) {
            items.add(new EmptyBig());
        } else {
            if (items.size() > 0) {
                items.remove(items.size() - 1);
            }
            for (int i = 0; i < userEvaluateList.size(); i++) {
                items.add(userEvaluateList.get(i));
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


    @Override
    public void onImageItemClickListener(String url, int evaluateId) {
        ArrayList<String> picList = new ArrayList<>();
        picList.add(url);
        String content = "";
        for (int i = 0; i < userEvaluateList.size(); i++) {
            if (userEvaluateList.get(i).getEvaluateId() == evaluateId) {  //根据id找到该条评论
                List<String> evaluateImageList = userEvaluateList.get(i).getEvaluateImageList();
                content = userEvaluateList.get(i).getEvaluateContent();
                for (int j = 0; j < evaluateImageList.size(); j++) {//去除点击的图片
                    if (!evaluateImageList.get(j).equals(url)) {
                        picList.add(evaluateImageList.get(j));
                    }
                }
            }
        }
        ImagPagerUtil imagPagerUtil = new ImagPagerUtil(ShopEvaluateActivity.this, picList);
        imagPagerUtil.setContentText(content);
        imagPagerUtil.show();
    }
}
