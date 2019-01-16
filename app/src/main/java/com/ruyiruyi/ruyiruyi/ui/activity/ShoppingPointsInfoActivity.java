package com.ruyiruyi.ruyiruyi.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.ruyiruyi.ruyiruyi.R;
import com.ruyiruyi.ruyiruyi.ui.activity.base.RyBase1FragmentActivity;
import com.ruyiruyi.ruyiruyi.ui.adapter.MainFragmentPagerAdapter;
import com.ruyiruyi.ruyiruyi.ui.fragment.ShoppingPointsInfoFragment;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;

import java.util.ArrayList;
import java.util.List;

import rx.functions.Action1;


public class ShoppingPointsInfoActivity extends RyBase1FragmentActivity {
    private FrameLayout action_bar_view;
    private TextView act_title;
    private ImageView back_image_view;
    private ImageView iv_background;
    private TabLayout mTab;
    private ViewPager mVPager;
    private List<Fragment> fragments;
    private List<String> title_list;
    private List<Integer> tabRes_list;
    private CoordinatorLayout main;
    private ProgressDialog mDialog;
    private TextView points;
    private int total_points = 0;
    private String TAG = ShoppingPointsInfoActivity.class.getSimpleName();
    private int height;
    private int height2;

    private Toolbar toolbar;
    private AppBarLayout appbar;

    private AppBarLayout.OnOffsetChangedListener mOnOffsetChangedListener = new AppBarLayout.OnOffsetChangedListener() {
        @Override
        public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
            Log.e(TAG, "onOffsetChanged: verticalOffset = " + verticalOffset);
            int abs = Math.abs(verticalOffset);
            float cal = (float) abs / (height - height2);
            float alpha = cal * 255;
            if (abs <= height - height2) {
                action_bar_view.setBackgroundColor(Color.argb((int) alpha, 255, 102, 35));
                toolbar.setVisibility(View.GONE);
            } else {
                action_bar_view.setBackgroundColor(Color.argb(255, 255, 102, 35));
                toolbar.setVisibility(View.VISIBLE);//占位作用
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_points_info);

        action_bar_view = findViewById(R.id.action_bar_view);
        act_title = findViewById(R.id.act_title);
        back_image_view = findViewById(R.id.back_image_view);
        act_title.setText("积分明细");
        RxViewAction.clickNoDouble(back_image_view).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                finish();
            }
        });

        main = findViewById(R.id.main);
        toolbar = findViewById(R.id.toolbar);
        appbar = findViewById(R.id.appbar);
        appbar.addOnOffsetChangedListener(mOnOffsetChangedListener);
        iv_background = findViewById(R.id.iv_background);

        Intent intent = getIntent();
        total_points = intent.getIntExtra("total_points", 0);


        initHeight();
        initView();
        initData();
    }

    /**
     * 获取图片高度为自定义CoordinatorLayout设置滑动监听
     */
    private void initHeight() {
        ViewTreeObserver vto = iv_background.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mTab.getViewTreeObserver().removeGlobalOnLayoutListener(
                        this);
                height = iv_background.getHeight();
                Log.e(TAG, "onScrollChanged: height = " + height);
            }
        });
        ViewTreeObserver vto2 = action_bar_view.getViewTreeObserver();
        vto2.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mTab.getViewTreeObserver().removeGlobalOnLayoutListener(
                        this);
                height2 = action_bar_view.getHeight();
                Log.e(TAG, "onScrollChanged: height2 = " + height2);
            }
        });

    }

    private void initData() {
        main.setVisibility(View.GONE);
        mDialog = new ProgressDialog(this);
        showDialogProgress(mDialog, "数据加载中...");

        points.setText(total_points + "");
        hideDialogProgress(mDialog);
        main.setVisibility(View.VISIBLE);
    }

    private void initView() {
        points = findViewById(R.id.points);
        mTab = (TabLayout) findViewById(R.id.mtab);
        mVPager = (ViewPager) findViewById(R.id.mpager);
        fragments = getFragments();
        title_list = getTitles();
        tabRes_list = getTabRes();
        MainFragmentPagerAdapter adapter = new MainFragmentPagerAdapter(getSupportFragmentManager(), getApplicationContext(), fragments);
        mVPager.setAdapter(adapter);
        mTab.setupWithViewPager(mVPager);
        for (int i = 0; i < title_list.size(); i++) {
            TabLayout.Tab tab = mTab.getTabAt(i);
            tab.setCustomView(R.layout.tab_main);
            TextView textView = (TextView) tab.getCustomView().findViewById(R.id.txt);
            textView.setText(title_list.get(i));
            ImageView imageView = tab.getCustomView().findViewById(R.id.img);
            imageView.setImageResource(tabRes_list.get(i));
        }

    }

    private List<String> getTitles() {
        title_list = new ArrayList();
        title_list.add("积分收入");
        title_list.add("积分支出");
        return title_list;
    }

    private List<Integer> getTabRes() {
        tabRes_list = new ArrayList<>();
        tabRes_list.add(R.drawable.ic_shouru);
        tabRes_list.add(R.drawable.ic_zhichu);
        return tabRes_list;
    }

    private List<Fragment> getFragments() {
        fragments = new ArrayList();
        ShoppingPointsInfoFragment noFragment = new ShoppingPointsInfoFragment();
        Bundle nobundle = new Bundle();
        nobundle.putInt("status", 0);//status (0:收入,1:支出)
        noFragment.setArguments(nobundle);
        fragments.add(noFragment);

        ShoppingPointsInfoFragment yesFragment = new ShoppingPointsInfoFragment();
        Bundle yesbundle = new Bundle();
        yesbundle.putInt("status", 1);//status (0:收入,1:支出)
        yesFragment.setArguments(yesbundle);
        fragments.add(yesFragment);

        return fragments;
    }
}
