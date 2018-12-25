package com.ruyiruyi.ruyiruyi.ui.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.ruyiruyi.ruyiruyi.R;
import com.ruyiruyi.ruyiruyi.ui.activity.base.RyBase1Activity;
import com.ruyiruyi.ruyiruyi.ui.activity.base.RyBaseActivity;
import com.ruyiruyi.ruyiruyi.ui.multiType.ExchangeCoupon;
import com.ruyiruyi.ruyiruyi.ui.multiType.ExchangeCouponViewBinder;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;
import com.ruyiruyi.rylibrary.cell.GradationScrollView;

import java.util.ArrayList;
import java.util.List;

import me.drakeet.multitype.MultiTypeAdapter;
import rx.functions.Action1;

import static me.drakeet.multitype.MultiTypeAsserts.assertAllRegistered;
import static me.drakeet.multitype.MultiTypeAsserts.assertHasTheSameAdapter;

public class ExchangeCouponActivity extends RyBase1Activity implements GradationScrollView.ScrollViewListener {

    private RecyclerView listView;
    private List<Object> items = new ArrayList<>();
    private MultiTypeAdapter adapter;
    private FrameLayout actionBarView;
    private int height;
    private ImageView ivBanner;

    private GradationScrollView scrollView;
    private ImageView backView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange_coupon);


        initView();
        initListeners();

        initData();
    }
    /**
     * 获取顶部图片高度后，设置滚动监听
     */
    private void initListeners() {

        ViewTreeObserver vto = ivBanner.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                actionBarView.getViewTreeObserver().removeGlobalOnLayoutListener(
                        this);
                height = ivBanner.getHeight();

                scrollView.setScrollViewListener(ExchangeCouponActivity.this);
            }
        });
    }


    private void initData() {
        for (int i = 0; i < 10; i++) {
            items.add(new ExchangeCoupon());
        }
        assertAllRegistered(adapter, items);
        adapter.notifyDataSetChanged();
    }

    private void initView() {
        backView = (ImageView) findViewById(R.id.back_image_view);
        scrollView = (GradationScrollView) findViewById(R.id.scrollview);
        ivBanner = (ImageView) findViewById(R.id.iv_banber);
        actionBarView = (FrameLayout) findViewById(R.id.action_bar_view);
        listView = (RecyclerView) findViewById(R.id.jifen_listview);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        listView.setLayoutManager(linearLayoutManager);
        adapter = new MultiTypeAdapter(items);
        register();

        listView.setAdapter(adapter);
        assertHasTheSameAdapter(listView, adapter);

        RxViewAction.clickNoDouble(backView)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        onBackPressed();
                    }
                });

    }

    private void register() {
        adapter.register(ExchangeCoupon.class,new ExchangeCouponViewBinder());
    }

    /**
     * 滑动监听
     * @param scrollView
     * @param x
     * @param y
     * @param oldx
     * @param oldy
     */
    @Override
    public void onScrollChanged(GradationScrollView scrollView, int x, int y,
                                int oldx, int oldy) {
        // TODO Auto-generated method stub
        if (y <= 0) {   //设置标题的背景颜色
            actionBarView.setBackgroundColor(Color.argb((int) 0, 144,151,166));
        } else if (y > 0 && y <= height) { //滑动距离小于banner图的高度时，设置背景和字体颜色颜色透明度渐变
            float scale = (float) y / height;
            float alpha = (255 * scale);
            //    actionBarView.setTextColor(Color.argb((int) alpha, 255,255,255));
            actionBarView.setBackgroundColor(Color.argb((int) alpha, 255,102,35));
        } else {    //滑动到banner下面设置普通颜色
            actionBarView.setBackgroundColor(Color.argb((int) 255, 255,102,35));
        }
    }



}
