package com.ruyiruyi.ruyiruyi.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.ruyiruyi.ruyiruyi.R;
import com.ruyiruyi.ruyiruyi.ui.cell.TabItemView;
import com.ruyiruyi.ruyiruyi.ui.fragment.GoodsListFragment;
import com.ruyiruyi.ruyiruyi.ui.fragment.OrderFragment;
import com.ruyiruyi.rylibrary.base.BaseActivity;
import com.ruyiruyi.rylibrary.cell.ActionBar;
import com.ruyiruyi.rylibrary.cell.NoCanSlideViewPager;

import java.util.ArrayList;
import java.util.List;

public class OrderActivity extends FragmentActivity {
    private ActionBar actionBar;
    private NoCanSlideViewPager viewPager;
    private  TabLayout tabLayout;
    private SimpleFragmentPagerAdapter pagerAdapter;
    private String orderType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        actionBar = (ActionBar) findViewById(R.id.my_action);
        actionBar.setTitle("我的订单");;
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
        orderType = intent.getStringExtra(OrderFragment.ORDER_TYPE);

        initView();
    }

    private void initView() {
        viewPager = (NoCanSlideViewPager) findViewById(R.id.viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPager.setOffscreenPageLimit(4);

        getFragments();
        getTitles();
        getTabViews();
        pagerAdapter = new SimpleFragmentPagerAdapter(getSupportFragmentManager(), this);
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        if (getTabViews()!=null){
            for (int i = 0; i < tabLayout.getTabCount(); i++) {
                TabLayout.Tab tab = tabLayout.getTabAt(i);
                tab.setCustomView(pagerAdapter.getTabView(i));
            }
        }
        if (orderType.equals("ALL")){
            viewPager.setCurrentItem(0);
            tabLayout.getTabAt(0).select();
        }else if (orderType.equals("DZF")){
            viewPager.setCurrentItem(1);
            tabLayout.getTabAt(1).select();
        }else if (orderType.equals("DFH")){
            viewPager.setCurrentItem(2);
            tabLayout.getTabAt(2).select();
        }else if (orderType.equals("DFW")){
            viewPager.setCurrentItem(3);
            tabLayout.getTabAt(3).select();
        }else if (orderType.equals("YWC")){
            viewPager.setCurrentItem(4);
            tabLayout.getTabAt(4).select();
        }

    }

    public class SimpleFragmentPagerAdapter extends FragmentPagerAdapter {


        public SimpleFragmentPagerAdapter(FragmentManager fm, Context context) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return getFragments().get(position);
        }

        @Override
        public int getCount() {
            return getFragments().size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (getTitles()!=null&&!getTitles().isEmpty()){
                return getTitles().get(position);
            }
            return null;
        }

        public View getTabView(int position) {
            return getTabViews().get(position);
        }

    }


    protected List<Fragment> getFragments() {
        List fragments = new ArrayList();
        OrderFragment allFragment = new OrderFragment();
        Bundle allBundle = new Bundle();
        allBundle.putString(OrderFragment.ORDER_TYPE,"ALL");
        allFragment.setArguments(allBundle);
        fragments.add(allFragment);

        OrderFragment dzfFragment = new OrderFragment();
        Bundle dzfBundle = new Bundle();
        dzfBundle.putString(OrderFragment.ORDER_TYPE,"DZF");
        dzfFragment.setArguments(dzfBundle);
        fragments.add(dzfFragment);

        OrderFragment dfhFragment = new OrderFragment();
        Bundle dfhBundle = new Bundle();
        dfhBundle.putString(OrderFragment.ORDER_TYPE,"DFH");
        dfhFragment.setArguments(dfhBundle);
        fragments.add(dfhFragment);

        OrderFragment dfwFragment = new OrderFragment();
        Bundle dfwBundle = new Bundle();
        dfwBundle.putString(OrderFragment.ORDER_TYPE,"DFW");
        dfwFragment.setArguments(dfwBundle);
        fragments.add(dfwFragment);

        OrderFragment ywcFragment = new OrderFragment();
        Bundle ywcBundle = new Bundle();
        ywcBundle.putString(OrderFragment.ORDER_TYPE,"YWC");
        ywcFragment.setArguments(ywcBundle);
        fragments.add(ywcFragment);
        return fragments;
    }
    protected List<String> getTitles() {
        List list = new ArrayList();
        list.add("全部");
        list.add("待支付");
        list.add("代发货");
        list.add("待服务");
        list.add("已完成");
        return list;
    }
    protected List<TabItemView> getTabViews() {
        return null;
    }
}
