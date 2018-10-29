package com.ruyiruyi.merchant.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.ruyiruyi.merchant.MainActivity;
import com.ruyiruyi.merchant.R;
import com.ruyiruyi.merchant.ui.fragment.MyOrderFragment;
import com.ruyiruyi.rylibrary.base.BaseFragmentActivity;
import com.ruyiruyi.rylibrary.cell.ActionBar;

import java.util.ArrayList;
import java.util.List;

/*
*
* intent.putExtra("page", "0");             page : 当前选择的页数
* intent.putExtra("typestate", "pingtai");  typestate : 查询订单列表的种类  （  all= 5种所有订单类型   pingtai= 2种平台订单类型 ）
*
* */
public class MyOrderActivity extends BaseFragmentActivity {
    private ActionBar mActionBar;
    private TabLayout mTab;
    private ViewPager mVPager;
    private List<Fragment> fragments;
    private List<String> title_list;
    private String selectPage;
    private String typeState;
    private String TAG = MyOrderActivity.class.getSimpleName();

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
        //获取传递数据
        selectPage = getIntent().getStringExtra("page");
        typeState = getIntent().getStringExtra("typestate");

        initView();

    }


    private void initView() {
        mTab = (TabLayout) findViewById(R.id.myorder_tab);
        mVPager = (ViewPager) findViewById(R.id.myorder_vpager);

        if (typeState.equals("all")) {//所有订单
            fragments = getFragmentsAll();
            title_list = getTitlesAll();
        }
        if (typeState.equals("pingtai")) {//平台订单
            fragments = getFragmentsPingtai();
            title_list = getTitlesPingtai();
        }

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

        //根据传递page设置选中页数
        mVPager.setCurrentItem(Integer.parseInt(selectPage));


    }

    private List<String> getTitlesAll() {
        title_list = new ArrayList();
        title_list.add("全部");
        title_list.add("进行中");
        title_list.add("待服务");
        title_list.add("已完成");
        return title_list;
    }

    private List<Fragment> getFragmentsAll() {
        fragments = new ArrayList();
        MyOrderFragment fg1 = new MyOrderFragment();
        Bundle bundle1 = new Bundle();
        bundle1.putString(MyOrderFragment.ORDER_TYPE, "ALL_QUANBU");
        fg1.setArguments(bundle1);
        fragments.add(fg1);

        MyOrderFragment fg2 = new MyOrderFragment();
        Bundle bundle2 = new Bundle();
        bundle2.putString(MyOrderFragment.ORDER_TYPE, "ALL_JINXINGZHONG");
        fg2.setArguments(bundle2);
        fragments.add(fg2);

        MyOrderFragment fg3 = new MyOrderFragment();
        Bundle bundle3 = new Bundle();
        bundle3.putString(MyOrderFragment.ORDER_TYPE, "ALL_DAIFUWU");
        fg3.setArguments(bundle3);
        fragments.add(fg3);

        MyOrderFragment fg4 = new MyOrderFragment();
        Bundle bundle4 = new Bundle();
        bundle4.putString(MyOrderFragment.ORDER_TYPE, "ALL_YIWANCHENG");
        fg4.setArguments(bundle4);
        fragments.add(fg4);

        return fragments;
    }

    private List<Fragment> getFragmentsPingtai() {
        fragments = new ArrayList();
        MyOrderFragment pt_wwc_fg = new MyOrderFragment();
        Bundle pt_wwc = new Bundle();
        pt_wwc.putString(MyOrderFragment.ORDER_TYPE, "PT_WWC");
        pt_wwc_fg.setArguments(pt_wwc);
        fragments.add(pt_wwc_fg);

        MyOrderFragment pt_ywc_fg = new MyOrderFragment();
        Bundle pt_ywc = new Bundle();
        pt_ywc.putString(MyOrderFragment.ORDER_TYPE, "PT_YWC");
        pt_ywc_fg.setArguments(pt_ywc);
        fragments.add(pt_ywc_fg);

        return fragments;
    }

    private List<String> getTitlesPingtai() {
        title_list = new ArrayList();
        title_list.add("未完成订单");
        title_list.add("已完成订单");
        return title_list;
    }


    /*
    * 重写回退键监听
    * */
    @Override
    public void onBackPressed() {
        Log.e(TAG, "   before  onBackPressed: ");
        Intent intent = new Intent();
        Bundle bundle = new Bundle();

        if (typeState.equals("all")) {
            intent.setClass(getApplicationContext(), MainActivity.class);
            Log.e(TAG, "onBackPressed: MyOrderActivity   all");
            bundle.putString("page", "my");
        }
        if (typeState.equals("pingtai")) {
            intent.setClass(getApplicationContext(), MainActivity.class);
            Log.e(TAG, "onBackPressed: MyOrderActivity   pingtai");
            bundle.putString("page", "order");
        }


        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }
}
