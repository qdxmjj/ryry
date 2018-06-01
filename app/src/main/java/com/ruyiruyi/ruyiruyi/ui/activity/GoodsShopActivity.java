package com.ruyiruyi.ruyiruyi.ui.activity;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ruyiruyi.ruyiruyi.R;
import com.ruyiruyi.ruyiruyi.ui.listener.OnLoadMoreListener;
import com.ruyiruyi.ruyiruyi.ui.activity.base.RYBaseActivity;
import com.ruyiruyi.ruyiruyi.ui.listener.OnLoadMoreListener;
import com.ruyiruyi.ruyiruyi.ui.multiType.GoodsShop;
import com.ruyiruyi.ruyiruyi.ui.multiType.GoodsShopViewBinder;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;
import com.ruyiruyi.rylibrary.base.BaseActivity;
import com.ruyiruyi.rylibrary.cell.ActionBar;

import java.util.ArrayList;
import java.util.List;

import me.drakeet.multitype.MultiTypeAdapter;
import rx.functions.Action1;

import static me.drakeet.multitype.MultiTypeAsserts.assertAllRegistered;
import static me.drakeet.multitype.MultiTypeAsserts.assertHasTheSameAdapter;

public class  GoodsShopActivity extends RYBaseActivity {
    private ActionBar actionBar;
    public static String CLASS_NAME = "CLASS_NAME";
    public static String CLASS_ID = "CLASS_ID";
    public static String FROMTYPE = "FROMTYPE";
    private String className;
    private int classId;
    private RecyclerView listView;
    private List<Object> items = new ArrayList<>();
    private MultiTypeAdapter adapter;
    public List<GoodsShop> goodsShopList;
    public int currentType = 0;  //0是综合  1是价格上  2是价格下   3是距离上  4是距离下
    private LinearLayout zongheLayour;
    private LinearLayout priceLayout;
    private LinearLayout distenceLayout;
    private TextView zongheText;
    private TextView priceText;
    private TextView distenceText;
    private ImageView priceImage;
    private SwipeRefreshLayout swipeRefreshLayout;
    private int fromType = 0; //0来自商品  1来自搜索
    private String searchStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_shop);
        actionBar = (ActionBar) findViewById(R.id.my_action);
        //actionBar.setTitle("测试");;
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

        goodsShopList = new ArrayList<>();
        Intent intent = getIntent();
        fromType = intent.getIntExtra(FROMTYPE,0);
        if (fromType == 0){
            className = intent.getStringExtra(CLASS_NAME);
            actionBar.setTitle(className);;
            classId = intent.getIntExtra(CLASS_ID,0);
        }else if (fromType == 1){
            searchStr = intent.getStringExtra("SEARCH_STR");
            actionBar.setTitle(searchStr);;
        }


        initView();
        initDataFromService();

    }

    private void intiData() {
        items.clear();
        for (int i = 0; i < goodsShopList.size(); i++) {
            items.add(goodsShopList.get(i));
        }
        assertAllRegistered(adapter,items);
        adapter.notifyDataSetChanged();
    }

    private void initDataFromService() {
        goodsShopList.clear();
        for (int i = 0; i < 10; i++) {
            GoodsShop goodsShop = new GoodsShop(i, "", "风油精" + i, "20.2", "晴天天安数码城" + i, "200.0");
            goodsShopList.add(goodsShop);
            intiData();
        }
    }

    private void initView() {
        listView = (RecyclerView) findViewById(R.id.goods_activity_listview);
        zongheLayour = (LinearLayout) findViewById(R.id.zonghe_layout);
        priceLayout = (LinearLayout) findViewById(R.id.price_layout);
        distenceLayout = (LinearLayout) findViewById(R.id.diatence_layout);
        zongheText = (TextView) findViewById(R.id.zonghe_text);
        priceText = (TextView) findViewById(R.id.price_text);
        distenceText = (TextView) findViewById(R.id.distence_text);
        priceImage = (ImageView) findViewById(R.id.price_image);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.goods_swipre_refresh_layout);
        swipeRefreshLayout.setProgressViewEndTarget(true, 200);
        //下拉刷新
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        //加载更多
        listView.setOnScrollListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {

            }
        });

        RxViewAction.clickNoDouble(zongheLayour)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        currentType = 0;
                        initTopView();
                    }

                });
        RxViewAction.clickNoDouble(priceLayout) //1是价格上  2是价格下
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (currentType == 1){
                            currentType = 2;
                        }else if (currentType == 2){
                            currentType = 1;
                        }else {
                            currentType = 1;
                        }
                        initTopView();
                    }
                });
        RxViewAction.clickNoDouble(distenceLayout) //3是距离上  4是距离下
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (currentType == 3){
                            currentType = 4;
                        }else if (currentType == 4){
                            currentType = 3;
                        }else {
                            currentType = 3;
                        }
                        initTopView();
                    }
                });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        listView.setLayoutManager(linearLayoutManager);
        adapter = new MultiTypeAdapter(items);
        register();

        listView.setAdapter(adapter);
        assertHasTheSameAdapter(listView, adapter);
    }

    private void initTopView() {
        if (currentType == 0){
            zongheText.setTextColor(getResources().getColor(R.color.theme_primary));
            priceText.setTextColor(getResources().getColor(R.color.c6));
            distenceText.setTextColor(getResources().getColor(R.color.c6));
            priceImage.setVisibility(View.INVISIBLE);
        }else if (currentType == 1){
            zongheText.setTextColor(getResources().getColor(R.color.c6));
            priceText.setTextColor(getResources().getColor(R.color.theme_primary));
            distenceText.setTextColor(getResources().getColor(R.color.c6));
            priceImage.setVisibility(View.VISIBLE);
            priceImage.setImageResource(R.drawable.ic_xia);
        }else if (currentType == 2){
            zongheText.setTextColor(getResources().getColor(R.color.c6));
            priceText.setTextColor(getResources().getColor(R.color.theme_primary));
            distenceText.setTextColor(getResources().getColor(R.color.c6));
            priceImage.setVisibility(View.VISIBLE);
            priceImage.setImageResource(R.drawable.ic_shang);
        }else if (currentType == 3){
            zongheText.setTextColor(getResources().getColor(R.color.c6));
            priceText.setTextColor(getResources().getColor(R.color.c6));
            distenceText.setTextColor(getResources().getColor(R.color.theme_primary));
            priceImage.setVisibility(View.INVISIBLE);
        }
        initDataFromService();
    }

    private void register() {
        adapter.register(GoodsShop.class,new GoodsShopViewBinder(this));
    }
}
