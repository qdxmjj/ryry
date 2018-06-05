package com.ruyiruyi.ruyiruyi.ui.activity;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.ruyiruyi.ruyiruyi.R;
import com.ruyiruyi.ruyiruyi.ui.activity.base.RyBaseActivity;
import com.ruyiruyi.ruyiruyi.ui.multiType.Button;
import com.ruyiruyi.ruyiruyi.ui.multiType.ButtonViewBinder;
import com.ruyiruyi.ruyiruyi.ui.multiType.Coupon;
import com.ruyiruyi.ruyiruyi.ui.multiType.CouponViewBinder;
import com.ruyiruyi.ruyiruyi.ui.multiType.Empty;
import com.ruyiruyi.ruyiruyi.ui.multiType.EmptyBig;
import com.ruyiruyi.ruyiruyi.ui.multiType.EmptyBigViewBinder;
import com.ruyiruyi.ruyiruyi.ui.multiType.EmptyViewBinder;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;
import com.ruyiruyi.rylibrary.cell.ActionBar;

import java.util.ArrayList;
import java.util.List;

import me.drakeet.multitype.MultiTypeAdapter;
import rx.functions.Action1;

import static me.drakeet.multitype.MultiTypeAsserts.assertAllRegistered;
import static me.drakeet.multitype.MultiTypeAsserts.assertHasTheSameAdapter;

public class CouponActivity extends RyBaseActivity {
    public static String FROM_TYPE = "FROM_TYPE";   //0查看  1使用
    public static String CAR_ID = "CAR_ID";   //0查看  1使用
    private ActionBar actionBar;
    private FrameLayout couponLayout;
    private FrameLayout oldCouponLayout;
    private View couponView;
    private View oldCouponView;
    public int couponType = 0;  //0是可用  1是历史
    private TextView couponText;
    private TextView oldCouponText;
    private RecyclerView listView;
    private List<Object> items = new ArrayList<>();
    private MultiTypeAdapter adapter;
    public List<Coupon> couponList;
    public List<Coupon> oldCouponList;
    private int fromType;
    private int carId;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon);

        actionBar = (ActionBar) findViewById(R.id.my_action);
        actionBar.setTitle("我的优惠券");;
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

        Intent intent = getIntent();
        fromType = intent.getIntExtra(FROM_TYPE,0);
        carId = intent.getIntExtra(CAR_ID, 0);


        couponList = new ArrayList<>();
        oldCouponList = new ArrayList<>();

        initView();
        initDataFromService();
    }

    private void initDataFromService() {
        couponList.clear();
        for (int i = 0; i < 10; i++) {
            if (10 % 2 == 0) {
                Coupon coupon = new Coupon(i, "精致洗车券", 1, 2, 2, "2018.05.05", "2019.05.05", "鲁F 2056D");
                couponList.add(coupon);
            }else {
                Coupon coupon = new Coupon(i,"四轮定位",1,3,2,"2018.05.05","2019.05.05","鲁F 2056D");
                couponList.add(coupon);
            }
        }
        Coupon coupon = new Coupon(11,"10元现金券",2,7,2,"2018.05.05","2019.05.05","鲁F 2056D");
        couponList.add(coupon);

        oldCouponList.clear();
        for (int i = 0; i < 10; i++) {
            if (10 / 2 == 0) {
                Coupon oldcoupon = new Coupon(i, "精致洗车券", 1, 2, 1, "2018.05.05", "2019.05.05", "鲁F 2056D");
                oldCouponList.add(oldcoupon);
            }else {
                Coupon oldcoupon = new Coupon(i,"四轮定位",1,3,3,"2018.05.05","2019.05.05","鲁F 2056D");
                oldCouponList.add(oldcoupon);
            }
        }
        Coupon oldcoupon = new Coupon(11,"10元现金券",2,7,3,"2018.05.05","2019.05.05","鲁F 2056D");
        oldCouponList.add(oldcoupon);
        initData();

    }

    private void initData() {
        items.clear();
        if (fromType == 1){
            items.add(new Button());
        }
        if (couponType == 0){       //可用
            for (int i = 0; i < couponList.size(); i++) {
                items.add(couponList.get(i));
            }
        }else {     //历史
            for (int i = 0; i < oldCouponList.size(); i++) {
                items.add(oldCouponList.get(i));
            }
        }
        if (items.size() == 0){
            items.add(new EmptyBig());
        }
        assertAllRegistered(adapter, items);
        adapter.notifyDataSetChanged();

    }

    private void initView() {
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.coupon_refresh_layout);
        swipeRefreshLayout.setProgressViewEndTarget(true, 200);

        couponLayout = (FrameLayout) findViewById(R.id.coupon_layout);
        oldCouponLayout = (FrameLayout) findViewById(R.id.old_coupon_layout);
        couponView = (View) findViewById(R.id.coupon_view);
        oldCouponView = (View) findViewById(R.id.old_coupon_view);
        couponText = (TextView) findViewById(R.id.coupon_text);
        oldCouponText = (TextView) findViewById(R.id.old_coupon_text);
        listView = (RecyclerView) findViewById(R.id.coupon_listview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        listView.setLayoutManager(linearLayoutManager);
        adapter = new MultiTypeAdapter(items);
        register();

        listView.setAdapter(adapter);
        assertHasTheSameAdapter(listView, adapter);

        initCouponView();

        //下拉刷新
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //刷新处理 ;
                initDataFromService();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        RxViewAction.clickNoDouble(couponLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        couponType = 0;
                        initCouponView();
                        initData();
                    }
                });
        RxViewAction.clickNoDouble(oldCouponLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        couponType = 1;
                        initCouponView();
                        initData();
                    }
                });
    }

    private void register() {
        adapter.register(Button.class,new ButtonViewBinder());
        adapter.register(Coupon.class,new CouponViewBinder());
        adapter.register(Empty.class, new EmptyViewBinder());
        adapter.register(EmptyBig.class, new EmptyBigViewBinder());
    }

    private void initCouponView() {
        if (couponType == 0){
            couponView.setVisibility(View.VISIBLE);
            oldCouponView.setVisibility(View.GONE);
            couponText.setTextColor(getResources().getColor(R.color.theme_primary));
            oldCouponText.setTextColor(getResources().getColor(R.color.c6));
        }else {
            couponView.setVisibility(View.GONE);
            oldCouponView.setVisibility(View.VISIBLE);
            couponText.setTextColor(getResources().getColor(R.color.c6));
            oldCouponText.setTextColor(getResources().getColor(R.color.theme_primary));
        }
    }
}
