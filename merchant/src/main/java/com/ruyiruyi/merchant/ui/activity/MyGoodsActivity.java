package com.ruyiruyi.merchant.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ruyiruyi.merchant.R;
import com.ruyiruyi.merchant.ui.fragment.MyGoodsFragment;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;
import com.ruyiruyi.rylibrary.cell.ActionBar;

import java.util.ArrayList;
import java.util.List;

import rx.functions.Action1;

public class MyGoodsActivity extends AppCompatActivity {
    private ActionBar mActionBar;
    private TabLayout mTab;
    private ViewPager mVPager;
    private List<Fragment> fragments;
    private List<String> title_list;

    private LinearLayout ll_bottom;
    private TextView tv_addgoods;
    private TextView tv_type;
    private TextView tv_all_type;
    private ImageView img_bottom;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_goods);
        mActionBar = (ActionBar) findViewById(R.id.mygoods_acbar);
        mActionBar.setTitle("商品管理");
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
        bindView();

    }

    private void bindView() {
        //添加商品 跳转
        RxViewAction.clickNoDouble(tv_addgoods).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Intent intent = new Intent(MyGoodsActivity.this, GoodsInfoActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initView() {
        mTab = (TabLayout) findViewById(R.id.mygoods_tab);
        mVPager = (ViewPager) findViewById(R.id.mygoods_vpager);
        ll_bottom = (LinearLayout) findViewById(R.id.ll_bottom);
        tv_addgoods = (TextView) findViewById(R.id.tv_addgoods);
        tv_type = (TextView) findViewById(R.id.tv_type);
        tv_all_type = (TextView) findViewById(R.id.tv_all_type);
        img_bottom = (ImageView) findViewById(R.id.img_bottom);
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
        title_list.add("出售中");
        title_list.add("已下架");
        return title_list;
    }

    private List<Fragment> getFragments() {
        fragments = new ArrayList();
        MyGoodsFragment onsale_fragment = new MyGoodsFragment();
        Bundle bundle_onSale = new Bundle();
        bundle_onSale.putString(MyGoodsFragment.SALE_TYPE, "ONSALE");
        onsale_fragment.setArguments(bundle_onSale);
        fragments.add(onsale_fragment);

        MyGoodsFragment nosale_fragment = new MyGoodsFragment();
        Bundle bundle_noSale = new Bundle();
        bundle_noSale.putString(MyGoodsFragment.SALE_TYPE, "NOSALE");
        nosale_fragment.setArguments(bundle_noSale);
        fragments.add(nosale_fragment);

        return fragments;
    }
}
