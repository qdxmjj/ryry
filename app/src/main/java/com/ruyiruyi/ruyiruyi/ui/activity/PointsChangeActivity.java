package com.ruyiruyi.ruyiruyi.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ruyiruyi.ruyiruyi.R;
import com.ruyiruyi.ruyiruyi.ui.activity.base.RyBase1Activity;
import com.ruyiruyi.ruyiruyi.ui.multiType.ItemBottomProvider;
import com.ruyiruyi.ruyiruyi.ui.multiType.ItemNullProvider;
import com.ruyiruyi.ruyiruyi.ui.multiType.PointsChange;
import com.ruyiruyi.ruyiruyi.ui.multiType.PointsChangeViewBinder;
import com.ruyiruyi.ruyiruyi.ui.multiType.bean.ItemBottomBean;
import com.ruyiruyi.ruyiruyi.ui.multiType.bean.ItemNullBean;
import com.ruyiruyi.ruyiruyi.ui.multiType.divider.PointsGridDivider;
import com.ruyiruyi.ruyiruyi.utils.RequestUtils;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;
import com.ruyiruyi.rylibrary.cell.GradationScrollView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import me.drakeet.multitype.MultiTypeAdapter;
import rx.functions.Action1;

import static me.drakeet.multitype.MultiTypeAsserts.assertAllRegistered;
import static me.drakeet.multitype.MultiTypeAsserts.assertHasTheSameAdapter;

public class PointsChangeActivity extends RyBase1Activity {

    private FrameLayout action_bar_view;
    private TextView act_title;
    private ImageView back_image_view;
    private GradationScrollView mScrollView;
    private ImageView iv_background;
    private static final String TAG = PointsChangeActivity.class.getSimpleName();
    private RecyclerView mRecyclerView;
    private MultiTypeAdapter multiTypeAdapter;
    private List<Object> items = new ArrayList<>();
    private List<PointsChange> orderBeanList;
    private boolean isFirstLoad = true;
    private SwipeRefreshLayout mSwipeLayout;
    private float height;
    private float height2;

    private int mPoints;

    private LinearLayoutManager manager;
    private LinearLayoutManager errorManager;

    private TextView points;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_points_change);

        action_bar_view = findViewById(R.id.action_bar_view);
        act_title = findViewById(R.id.act_title);
        back_image_view = findViewById(R.id.back_image_view);
        act_title.setText("积分兑换");
        RxViewAction.clickNoDouble(back_image_view).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                finish();
            }
        });

        Intent intent = getIntent();
        mPoints = intent.getIntExtra("total_points", 0);

        mScrollView = findViewById(R.id.scrollView);
        iv_background = findViewById(R.id.iv_background);


        initHeight();
        initView();

        initData();
        initSwipeLayout();

    }

    /**
     * 获取图片高度为自定义ScrollView设置滑动监听
     */
    private void initHeight() {
        ViewTreeObserver vto = iv_background.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                action_bar_view.getViewTreeObserver().removeGlobalOnLayoutListener(
                        this);
                height = iv_background.getHeight();
            }
        });
        ViewTreeObserver vto2 = action_bar_view.getViewTreeObserver();
        vto2.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                action_bar_view.getViewTreeObserver().removeGlobalOnLayoutListener(
                        this);
                height2 = action_bar_view.getHeight();
            }
        });
        //为自定义ScrollView设置滑动距离监听
        mScrollView.setScrollViewListener(new GradationScrollView.ScrollViewListener() {
            @Override
            public void onScrollChanged(GradationScrollView scrollView, int x, int y, int oldx, int oldy) {
                if (y <= 0) {
                    action_bar_view.setBackgroundColor(Color.argb((int) 0, 144, 151, 166));
                } else if (y >= (height - height2)) {
                    action_bar_view.setBackgroundColor(Color.argb((int) 255, 255, 102, 35));
                } else {

                    float scale = (float) y / (height - height2);
                    float alpha = (255 * scale);
                    action_bar_view.setBackgroundColor(Color.argb((int) alpha, 255, 102, 35));
                }
            }
        });
    }

    private void initData() {
        //数据加载完成前显示加载动画
//        startDialog = new ProgressDialog(getContext());
        if (isFirstLoad) {
//            showDialogProgress(startDialog, "信息加载中...");
        }


        orderBeanList.clear();

        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL_JIFEN + "score/sku");
        params.addBodyParameter("skuType", "0");//skuType[商品类型 (0:实物商品,1:优惠券)]
        params.setConnectTimeout(6000);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess: result656 = " + result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray rows = jsonObject.getJSONArray("data");

                    for (int i = 0; i < rows.length(); i++) {
                        JSONObject order = (JSONObject) rows.get(i);
                        PointsChange bean = new PointsChange();
                        bean.setGoodsPic(order.getString("imgUrl"));
                        bean.setTitle(order.getString("name"));
                        bean.setPoints(order.getInt("score"));
                        bean.setGoodsId(order.getInt("id"));
                        bean.setPrice(order.getDouble("price"));
                        bean.setSkuType(order.getInt("skuType"));//[商品类型 (0:实物商品,1:优惠券)]
                        bean.setTotal_points(mPoints);
                        bean.setGoodsAmount(order.getInt("amount"));
                        bean.setGoodsInfo(order.getString("description"));
                        bean.setChangeNum(order.getInt("soldNo"));
                        orderBeanList.add(bean);
                    }

                    //更新数据
                    updataData();


                } catch (JSONException e) {
                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(PointsChangeActivity.this, "网络异常，请检查网络", Toast.LENGTH_SHORT).show();
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

                mSwipeLayout.setRefreshing(false);
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

        orderBeanList.clear();


        for (int i = 0; i < 30; i++) {
            PointsChange bean = new PointsChange();
            bean.setGoodsPic("http://180.76.243.205:8111/images/orderDefaultImg/ic_free.png");
            bean.setTitle("美孚 (Mobil) 金装美孚1号 全合成机油0W-40 SN级 4L" + i);
            bean.setGoodsAmount(i);
            bean.setGoodsInfo(getString(R.string.goods_points_info));
            bean.setPoints(i);
            bean.setGoodsId(i);
            bean.setPrice(i);
            bean.setTotal_points(mPoints);
            bean.setChangeNum(i);
            orderBeanList.add(bean);
        }


        //更新数据
        updataData();


        //加载完成 隐藏加载动画
        if (isFirstLoad) {
//                    hideDialogProgress(startDialog);
            isFirstLoad = false;
        }


    }

    private void updataData() {
        items.clear();
        if (orderBeanList == null || orderBeanList.size() == 0) {
            mRecyclerView.setLayoutManager(errorManager);
            items.add(new ItemNullBean("暂无数据"));
        } else {
            mRecyclerView.setLayoutManager(manager);
            for (int i = 0; i < orderBeanList.size(); i++) {
                items.add(orderBeanList.get(i));
            }
        }
        assertAllRegistered(multiTypeAdapter, items);
        multiTypeAdapter.notifyDataSetChanged();

    }

    private void updataNetError() {
        items.clear();
        mRecyclerView.setLayoutManager(errorManager);
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

                myDownRefreshByServer();


            }
        });
    }

    //下拉刷新
    private void myDownRefreshByServer() {//下拉刷新
        initData();
    }

    private void initView() {
        mSwipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe);
        mRecyclerView = (RecyclerView) findViewById(R.id.rlv);
        manager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false) {
            @Override
            public void onMeasure(RecyclerView.Recycler recycler, RecyclerView.State state, int widthSpec, int heightSpec) {
                super.onMeasure(recycler, state, widthSpec, heightSpec);
            }
        };
        errorManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);

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
        //设置总积分
        points = findViewById(R.id.points);
        points.setText(mPoints + "");
    }
}
