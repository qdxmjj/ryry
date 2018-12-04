package com.ruyiruyi.ruyiruyi.ui.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.ruyiruyi.ruyiruyi.R;
import com.ruyiruyi.ruyiruyi.ui.fragment.MyInvitePersonFragment;
import com.ruyiruyi.rylibrary.base.BaseFragmentActivity;
import com.ruyiruyi.rylibrary.cell.ActionBar;

import java.util.ArrayList;
import java.util.List;

public class MyInvitePersonActivity extends BaseFragmentActivity {
    private ActionBar mActionBar;
    private TabLayout mTab;
    private ViewPager mVPager;
    private List<Fragment> fragments;
    private List<String> title_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_invite_person);
        mActionBar = (ActionBar) findViewById(R.id.acbar);
        mActionBar.setTitle("助力好友");
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
        //--<
        mTab = (TabLayout) findViewById(R.id.tab);
        mVPager = (ViewPager) findViewById(R.id.vpager);
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
        //-->

    }

    private List<String> getTitles() {
        title_list = new ArrayList();
        title_list.add("未消费");
        title_list.add("已消费");
        return title_list;
    }

    private List<Fragment> getFragments() {
        fragments = new ArrayList();
        MyInvitePersonFragment noFragment = new MyInvitePersonFragment();
        Bundle nobundle = new Bundle();
        nobundle.putInt("status", 0);//status 0 未消费  1 已消费
        noFragment.setArguments(nobundle);
        fragments.add(noFragment);

        MyInvitePersonFragment yesFragment = new MyInvitePersonFragment();
        Bundle yesbundle = new Bundle();
        yesbundle.putInt("status", 1);//status 0 未消费  1 已消费
        yesFragment.setArguments(yesbundle);
        fragments.add(yesFragment);

        return fragments;
    }
}
