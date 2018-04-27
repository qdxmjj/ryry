package com.ruyiruyi.ruyiruyi.ui.activity;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.ruyiruyi.ruyiruyi.R;
import com.ruyiruyi.ruyiruyi.ui.listener.OnLoadMoreListener;
import com.ruyiruyi.ruyiruyi.ui.model.ServiceType;
import com.ruyiruyi.ruyiruyi.ui.multiType.Empty;
import com.ruyiruyi.ruyiruyi.ui.multiType.EmptyViewBinder;
import com.ruyiruyi.ruyiruyi.ui.multiType.EvaImageViewBinder;
import com.ruyiruyi.ruyiruyi.ui.multiType.LoadMore;
import com.ruyiruyi.ruyiruyi.ui.multiType.LoadMoreViewBinder;
import com.ruyiruyi.ruyiruyi.ui.multiType.ShopInfo;
import com.ruyiruyi.ruyiruyi.ui.multiType.ShopStr;
import com.ruyiruyi.ruyiruyi.ui.multiType.UserEvaluate;
import com.ruyiruyi.ruyiruyi.ui.multiType.UserEvaluateViewBinder;
import com.ruyiruyi.ruyiruyi.utils.ImagPagerUtil;
import com.ruyiruyi.rylibrary.base.BaseActivity;
import com.ruyiruyi.rylibrary.cell.ActionBar;

import java.util.ArrayList;
import java.util.List;

import me.drakeet.multitype.MultiTypeAdapter;

import static me.drakeet.multitype.MultiTypeAsserts.assertAllRegistered;
import static me.drakeet.multitype.MultiTypeAsserts.assertHasTheSameAdapter;

public class ShopEvaluateActivity extends BaseActivity implements EvaImageViewBinder.OnEvaluateImageClick{
    private ActionBar actionBar;
    private RecyclerView listView;
    private List<Object> items = new ArrayList<>();
    private MultiTypeAdapter adapter;
    public List<UserEvaluate> userEvaluateList;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_evaluate,R.id.my_action);
        actionBar = (ActionBar) findViewById(R.id.my_action);
        actionBar.setTitle("门店评价");;
        actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick(){
            @Override
            public void onItemClick(int var1) {
                switch ((var1)){
                    case -1:
                        onBackPressed();
                        break;
                }
            }
        });
        userEvaluateList = new ArrayList<>();

        List<String> imageList = new ArrayList<>();

        imageList.add("http://192.168.0.167/images/user/carImage/70/zhuye.png");
        imageList.add("http://180.76.243.205:8111/images/Advertisement/cxwy.png");
        imageList.add("http://192.168.0.167/images/user/carImage/70/fuye.png");
        imageList.add("http://180.76.243.205:8111/images/Advertisement/cxwy1000.png");


        for (int i = 0; i < 5; i++) {

            userEvaluateList.add(new UserEvaluate(i,"http://180.76.243.205:8111/images/Advertisement/cxwy1000.png","一只大鸟"+i,"2017-02-1" + i,
                    "有一只大鸟从天上掉了下来",imageList));
        }

        initView();
        initdata();
        //配置点击查看大图
        initImageLoader();
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
        //下拉刷新
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //刷新处理
                //initdataFromService();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        listView = (RecyclerView) findViewById(R.id.evaluate_listview); LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        listView.setLayoutManager(linearLayoutManager);
        adapter = new MultiTypeAdapter(items);
        register();

        listView.setAdapter(adapter);
        assertHasTheSameAdapter(listView, adapter);
        listView.setOnScrollListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                //initdata();
            }
        });
    }

    private void register() {
        UserEvaluateViewBinder userEvaluateViewBinder = new UserEvaluateViewBinder(this);
        userEvaluateViewBinder.setListener(this);
        adapter.register(UserEvaluate.class, userEvaluateViewBinder);
        adapter.register(Empty.class,new EmptyViewBinder());
        adapter.register(LoadMore.class,new LoadMoreViewBinder());
    }
    private void initdata() {
        //items.clear();
      //  items.add(new ShopStr("门店评价"));
        if (items.size() > 0){
            items.remove(items.size()-1);
        }
        if (userEvaluateList.size()>0){
            for (int i = 0; i < userEvaluateList.size(); i++) {
                items.add(userEvaluateList.get(i));
            }
        }else {
            items.add(new Empty());
        }
        items.add(new LoadMore());
        assertAllRegistered(adapter,items);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onEvaluateImageClickListener(String url, int evaluateId) {
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
