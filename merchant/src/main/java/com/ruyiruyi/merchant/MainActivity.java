package com.ruyiruyi.merchant;

import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import com.ruyiruyi.merchant.ui.fragment.DianpuFragment;
import com.ruyiruyi.merchant.ui.fragment.DingdanFragment;
import com.ruyiruyi.merchant.ui.fragment.WodeFragment;
import com.ruyiruyi.rylibrary.base.BaseActivity;
import com.ruyiruyi.rylibrary.cell.HomeTabsCell;
import com.ruyiruyi.rylibrary.cell.NoCanSlideViewPager;
import com.ruyiruyi.rylibrary.ui.adapter.FragmentViewPagerAdapter;
import com.ruyiruyi.rylibrary.utils.LayoutHelper;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {

    private FrameLayout content;
    private NoCanSlideViewPager viewPager;
    private HomeTabsCell tabsCell;
    private List<String> titles;
    private HomePagerAdapeter pagerAdapter;
    private static boolean isExit = false;
    private static Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isExit = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        content = new FrameLayout(this);

        setContentView(content, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));

        viewPager = new NoCanSlideViewPager(this);
        content.addView(viewPager,LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT,LayoutHelper.MATCH_PARENT, Gravity.TOP,0,0,0, HomeTabsCell.CELL_HEIGHT));

        tabsCell = new HomeTabsCell(this);
        content.addView(tabsCell,LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT,LayoutHelper.WRAP_CONTENT,Gravity.BOTTOM));
        tabsCell.setViewPager(viewPager);

        initTitle();
        pagerAdapter = new HomePagerAdapeter(getSupportFragmentManager(),initPagerTitle(),initFragment());
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPager.setCurrentItem(2);
        tabsCell.setSelected(2);


        initView();
        initData();


    }

    private void initData() {

    }

    private void initView() {

    }

    private List<Fragment> initFragment() {
        List<Fragment> fragments = new ArrayList<>();
        DingdanFragment dingdanFragment = new DingdanFragment();
        fragments.add(dingdanFragment);
        fragments.add(new DianpuFragment());
        fragments.add(new WodeFragment());

        return fragments;
    }

    private List<String> initPagerTitle() {
        titles = new ArrayList<>();
        titles.add("订单");
        titles.add("店铺");
        titles.add("我的");
        return titles;
    }

    private void initTitle() {
        tabsCell.addView(R.drawable.ic_tubiao1,R.drawable.ic_tubiao1_xuanzhong, "订单 ");
        tabsCell.addView(R.drawable.ic_shop_weixuan,R.drawable.ic_shop_xuanzhong, "店铺 ");
        tabsCell.addView(R.drawable.ic_wode_weixuan,R.drawable.ic_wode_xuanzhong, "我的 ");
    }


    class HomePagerAdapeter extends FragmentViewPagerAdapter {

        private final List<String> mPageTitle = new ArrayList<>();

        public HomePagerAdapeter(FragmentManager fragmentManager, List<String> pageTitles, List<Fragment> fragments) {
            super(fragmentManager, fragments);
            mPageTitle.clear();
            mPageTitle.addAll(pageTitles);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mPageTitle.get(position);
        }
    }



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    private void exit() {
        if (!isExit) {
            isExit = true;
            Toast.makeText(getApplicationContext(), "再按一次后退键退出程序",
                    Toast.LENGTH_SHORT).show();
            // 利用handler延迟发送更改状态信息
            mHandler.sendEmptyMessageDelayed(0, 2000);
        } else {


            this.finish();
        }
    }
}
