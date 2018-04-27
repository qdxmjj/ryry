package com.ruyiruyi.merchant.ui.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ruyiruyi.merchant.R;
import com.ruyiruyi.merchant.ui.fragment.MyGoodsFragment;
import com.ruyiruyi.merchant.ui.fragment.MyServiceFragment;
import com.ruyiruyi.rylibrary.cell.ActionBar;

import java.util.ArrayList;
import java.util.List;

public class MyServiceActivity extends AppCompatActivity {
    private ActionBar mAcBar;
    private TabLayout mTab;
    private ViewPager mVp;

    private List<Fragment> fragments;
    private List<String> title_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_service);
        mAcBar = (ActionBar) findViewById(R.id.myservice_acbar);
        mAcBar.setTitle("我的服务");
        mAcBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
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
        mTab = (TabLayout) findViewById(R.id.myservice_tab);
        mVp = (ViewPager) findViewById(R.id.myservice_vp);

        title_list = getTitles();
        fragments = getFragments();

        for (int i = 0; i < title_list.size(); i++) {
            TabLayout.Tab tab = mTab.newTab();
            tab.setText(title_list.get(i));
            mTab.addTab(tab);
        }
        mVp.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
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
        mTab.setupWithViewPager(mVp);
    }

    private List<String> getTitles() {
        title_list = new ArrayList();
        title_list.add("汽车保养");
        title_list.add("美容清洗");
        title_list.add("安装");
        title_list.add("轮胎服务");
        return title_list;
    }

    private List<Fragment> getFragments() {
        fragments = new ArrayList();
        MyServiceFragment qcby_fragment = new MyServiceFragment();
        Bundle bundle_qcby = new Bundle();
        bundle_qcby.putString(MyServiceFragment.SALE_TYPE, "QCBY");
        qcby_fragment.setArguments(bundle_qcby);
        fragments.add(qcby_fragment);

        MyServiceFragment mrqx_fragment = new MyServiceFragment();
        Bundle bundle_mrqx = new Bundle();
        bundle_mrqx.putString(MyServiceFragment.SALE_TYPE, "MRQX");
        mrqx_fragment.setArguments(bundle_mrqx);
        fragments.add(mrqx_fragment);

        MyServiceFragment az_fragment = new MyServiceFragment();
        Bundle bundle_az = new Bundle();
        bundle_az.putString(MyServiceFragment.SALE_TYPE, "AZ");
        az_fragment.setArguments(bundle_az);
        fragments.add(az_fragment);

        MyServiceFragment ltfw_fragment = new MyServiceFragment();
        Bundle bundle_ltfw = new Bundle();
        bundle_ltfw.putString(MyServiceFragment.SALE_TYPE, "LTFW");
        ltfw_fragment.setArguments(bundle_ltfw);
        fragments.add(ltfw_fragment);

        return fragments;
    }
}
