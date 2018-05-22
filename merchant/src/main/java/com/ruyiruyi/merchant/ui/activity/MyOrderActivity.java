package com.ruyiruyi.merchant.ui.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.ruyiruyi.merchant.R;
import com.ruyiruyi.merchant.ui.fragment.MyGoodsFragment;
import com.ruyiruyi.merchant.ui.fragment.MyOrderFragment;
import com.ruyiruyi.rylibrary.base.BaseActivity;
import com.ruyiruyi.rylibrary.cell.ActionBar;

import java.util.ArrayList;
import java.util.List;

public class MyOrderActivity extends FragmentActivity {
    private ActionBar mActionBar;
    private TabLayout mTab;
    private ViewPager mVPager;
    private List<Fragment> fragments;
    private List<String> title_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);
        mActionBar = (ActionBar) findViewById(R.id.myorder_acbar);
        mActionBar.setTitle("我的订单");
        mActionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(int var1) {
                switch (var1) {
                    case -1:
                        onBackPressed();
                        break;
                }
            }
        });

        initView();

    }

    private void initView() {
        mTab = (TabLayout) findViewById(R.id.myorder_tab);
        mVPager = (ViewPager) findViewById(R.id.myorder_vpager);
        fragments = getFragments();
        title_list = getTitles();
        for (int i = 0; i < title_list.size(); i++) {
            TabLayout.Tab tab = mTab.newTab();
            tab.setText(title_list.get(i));
            mTab.addTab(tab);
        }
        mVPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public int getCount() {
                return fragments == null ? 0 : fragments.size();
            }

            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            //设置适配器（适配器中必须重写这一步，不然标题不会显示出来）
            @Override
            public CharSequence getPageTitle(int position) {
                return title_list.get(position);
            }
        });
        mTab.setupWithViewPager(mVPager);


    }

    private List<String> getTitles() {
        title_list = new ArrayList();
        title_list.add("全部");
        title_list.add("待发货");
        title_list.add("待收货");
        title_list.add("待服务");
        title_list.add("已完成");
        return title_list;
    }

    private List<Fragment> getFragments() {
        fragments = new ArrayList();
        MyOrderFragment quanbu_fg = new MyOrderFragment();
        Bundle quanbu = new Bundle();
        quanbu.putString(MyOrderFragment.ORDER_TYPE, "QUANBU");
        quanbu_fg.setArguments(quanbu);
        fragments.add(quanbu_fg);

        MyOrderFragment daizhifu_fg = new MyOrderFragment();
        Bundle daizhifu = new Bundle();
        daizhifu.putString(MyOrderFragment.ORDER_TYPE, "DAIFAHUO");
        daizhifu_fg.setArguments(daizhifu);
        fragments.add(daizhifu_fg);

        MyOrderFragment daifahuo_fg = new MyOrderFragment();
        Bundle daifahuo = new Bundle();
        daifahuo.putString(MyOrderFragment.ORDER_TYPE, "DAISHOUHUO");
        daifahuo_fg.setArguments(daifahuo);
        fragments.add(daifahuo_fg);

        MyOrderFragment daifuwu_fg = new MyOrderFragment();
        Bundle daifuwu = new Bundle();
        daifuwu.putString(MyOrderFragment.ORDER_TYPE, "DAIFUWU");
        daifuwu_fg.setArguments(daifuwu);
        fragments.add(daifuwu_fg);

        MyOrderFragment wancheng_fg = new MyOrderFragment();
        Bundle wancheng = new Bundle();
        wancheng.putString(MyOrderFragment.ORDER_TYPE, "YIWANCHENG");
        wancheng_fg.setArguments(wancheng);
        fragments.add(wancheng_fg);

        return fragments;
    }
}
