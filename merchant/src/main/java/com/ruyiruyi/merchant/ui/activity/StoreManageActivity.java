package com.ruyiruyi.merchant.ui.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.ruyiruyi.merchant.R;
import com.ruyiruyi.merchant.ui.fragment.MyGoodsFragment;
import com.ruyiruyi.merchant.ui.fragment.StorePingJiaFragment;
import com.ruyiruyi.merchant.ui.fragment.StoreXiangQingFragment;
import com.ruyiruyi.rylibrary.cell.ActionBar;

import java.util.ArrayList;
import java.util.List;

public class StoreManageActivity extends AppCompatActivity {
    private ActionBar mActionBar;
    private TabLayout mTab;
    private ViewPager mVPager;
    private List<Fragment> fragments;
    private List<String> title_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_manage);
        mActionBar = (ActionBar) findViewById(R.id.mygoods_acbar);
        mActionBar.setTitle("店铺信息");
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
        mTab = (TabLayout) findViewById(R.id.mygoods_tab);
        mVPager = (ViewPager) findViewById(R.id.mygoods_vpager);
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
        title_list.add("店铺详情");
        title_list.add("店铺评价");
        return title_list;
    }

    private List<Fragment> getFragments() {
        fragments = new ArrayList();
        StoreXiangQingFragment storeXiangQingFragment = new StoreXiangQingFragment();
        fragments.add(storeXiangQingFragment);

        StorePingJiaFragment storePingJiaFragment = new StorePingJiaFragment();
        fragments.add(storePingJiaFragment);

        return fragments;
    }
}
