package com.ruyiruyi.ruyiruyi.ui.activity;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.ruyiruyi.ruyiruyi.R;
import com.ruyiruyi.ruyiruyi.ui.cell.TabItemView;
import com.ruyiruyi.ruyiruyi.ui.fragment.GoodsListFragment;
import com.ruyiruyi.rylibrary.base.BaseActivity;
import com.ruyiruyi.rylibrary.cell.ActionBar;

import java.util.ArrayList;
import java.util.List;

public class ShopGoodActivity extends BaseActivity {
    private ActionBar actionBar;
    private  ViewPager viewPager;
    private  TabLayout tabLayout;
    private SimpleFragmentPagerAdapter pagerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_good,R.id.my_action);
        actionBar = (ActionBar) findViewById(R.id.my_action);
        actionBar.setTitle("商品列表");;
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

        initView();


    }

    private void initView() {
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);

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
        GoodsListFragment qcbyFragment = new GoodsListFragment();
        Bundle qcbyBundle = new Bundle();
        qcbyBundle.putString(GoodsListFragment.SHOP_SERVICE_TYPE,"QCBY");
        qcbyFragment.setArguments(qcbyBundle);
        fragments.add(qcbyFragment);

        GoodsListFragment mrqxFragment = new GoodsListFragment();
        Bundle mrqxBundle = new Bundle();
        mrqxBundle.putString(GoodsListFragment.SHOP_SERVICE_TYPE,"MRQX");
        mrqxFragment.setArguments(mrqxBundle);
        fragments.add(mrqxFragment);

        GoodsListFragment azFragment = new GoodsListFragment();
        Bundle azBundle = new Bundle();
        azBundle.putString(GoodsListFragment.SHOP_SERVICE_TYPE,"AZ");
        azFragment.setArguments(azBundle);
        fragments.add(azFragment);

        GoodsListFragment ltfwFragment = new GoodsListFragment();
        Bundle ltfwBundle = new Bundle();
        ltfwBundle.putString(GoodsListFragment.SHOP_SERVICE_TYPE,"LTFW");
        ltfwFragment.setArguments(ltfwBundle);
        fragments.add(ltfwFragment);
        return fragments;
    }
    protected List<String> getTitles() {
        List list = new ArrayList();
        list.add("汽车保养");
        list.add("美容清洗");
        list.add("安装");
        list.add("轮胎服务");
        return list;
    }
    protected List<TabItemView> getTabViews() {
        return null;
    }
}
