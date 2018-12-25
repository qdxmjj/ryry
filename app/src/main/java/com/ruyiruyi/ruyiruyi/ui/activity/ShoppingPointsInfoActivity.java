package com.ruyiruyi.ruyiruyi.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ruyiruyi.ruyiruyi.R;
import com.ruyiruyi.ruyiruyi.ui.activity.base.RyBaseFragmentActivity;
import com.ruyiruyi.ruyiruyi.ui.adapter.MainFragmentPagerAdapter;
import com.ruyiruyi.ruyiruyi.ui.fragment.ShoppingPointsInfoFragment;
import com.ruyiruyi.rylibrary.cell.ActionBar;

import java.util.ArrayList;
import java.util.List;

public class ShoppingPointsInfoActivity extends RyBaseFragmentActivity {
    private ActionBar actionBar;
    private ActionBar actionBar_mid;
    private TabLayout mTab;
    private ViewPager mVPager;
    private List<Fragment> fragments;
    private List<String> title_list;
    private List<Integer> tabRes_list;
    private CoordinatorLayout main;
    private ProgressDialog mDialog;
    private TextView points;
    private int total_points = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_points_info);

        actionBar = (ActionBar) findViewById(R.id.acbar);
        actionBar.setTitle("积分明细");
        actionBar.setBackground(0);
        actionBar.setRightView("积分兑换");
        actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(int var1) {
                switch ((var1)) {
                    case -1:
                        onBackPressed();
                        break;
                    case -3:
                        startActivity(new Intent(ShoppingPointsInfoActivity.this, PointsChangeActivity.class));
                        break;
                }
            }
        });

        main = findViewById(R.id.main);
        initView();
        initData();
    }

    private void initData() {
        main.setVisibility(View.GONE);
        mDialog = new ProgressDialog(this);
        showDialogProgress(mDialog, "数据加载中...");

        /*x.http().post()*/

        total_points = 9000;
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
        title_list.add("积分支出");
        title_list.add("积分收入");
        return title_list;
    }

    private List<Integer> getTabRes() {
        tabRes_list = new ArrayList<>();
        tabRes_list.add(R.drawable.ic_no);
        tabRes_list.add(R.drawable.ic_yes);
        return tabRes_list;
    }

    private List<Fragment> getFragments() {
        fragments = new ArrayList();
        ShoppingPointsInfoFragment noFragment = new ShoppingPointsInfoFragment();
        Bundle nobundle = new Bundle();
        nobundle.putInt("status", 0);//status 0 积分支出  1 积分收入
        noFragment.setArguments(nobundle);
        fragments.add(noFragment);

        ShoppingPointsInfoFragment yesFragment = new ShoppingPointsInfoFragment();
        Bundle yesbundle = new Bundle();
        yesbundle.putInt("status", 1);//status 0 积分支出  1 积分收入
        yesFragment.setArguments(yesbundle);
        fragments.add(yesFragment);

        return fragments;
    }
}
