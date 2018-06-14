package com.ruyiruyi.merchant;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.ruyiruyi.merchant.ui.activity.base.MerchantBaseFragmentActivity;
import com.ruyiruyi.merchant.ui.adapter.MyPagerAdapter;
import com.ruyiruyi.merchant.ui.fragment.StoreFragment;
import com.ruyiruyi.merchant.ui.fragment.OrderFragment;
import com.ruyiruyi.merchant.ui.fragment.MyFragment;
import com.ruyiruyi.merchant.utils.NoPreloadHomeTabsCell;
import com.ruyiruyi.rylibrary.cell.HomeTabsCell;
import com.ruyiruyi.rylibrary.utils.LayoutHelper;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends MerchantBaseFragmentActivity implements StoreFragment.ForRefreshStore, MyFragment.ForRefreshMy {

    private FrameLayout content;
    private ViewPager viewPager;
    private HomeTabsCell tabsCell;
    private List<String> titles;
    private MyPagerAdapter pagerAdapter;
    private static boolean isExit = false;
    private static Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isExit = false;
        }
    };
    private String page;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        content = new FrameLayout(this);

        setContentView(content, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));

        //获取传递page标记
        Bundle bundle = getIntent().getExtras();
        page = bundle.getString("page");

        //判断权限
        judgePower();

        viewPager = new ViewPager(this);
        viewPager.setId("MAINACTIVITYS".hashCode());
        content.addView(viewPager, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT, Gravity.TOP, 0, 0, 0, NoPreloadHomeTabsCell.CELL_HEIGHT));

        tabsCell = new HomeTabsCell(this);
        content.addView(tabsCell, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT, Gravity.BOTTOM));
        tabsCell.setViewPager(viewPager);

        initTitle();
//        pagerAdapter = new HomePagerAdapeter(getSupportFragmentManager(), initPagerTitle(), initFragment());
        pagerAdapter = new MyPagerAdapter(getSupportFragmentManager(), initPagerTitle(), initFragment());
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

        switch (page) {
            case "order":
                viewPager.setCurrentItem(0);
                tabsCell.setSelected(0);
                break;
            case "store":
                viewPager.setCurrentItem(1);
                tabsCell.setSelected(1);
                break;
            case "my":
                viewPager.setCurrentItem(2);
                tabsCell.setSelected(2);
                break;
        }


        initView();
        initData();


    }

    private void initData() {

    }

    private void initView() {

    }

    private List<Fragment> initFragment() {
        List<Fragment> fragments = new ArrayList<>();
        OrderFragment orderFragment = new OrderFragment();
        fragments.add(orderFragment);
        StoreFragment storeFragment = new StoreFragment(this);
        storeFragment.setListener(this);
        fragments.add(storeFragment);
        MyFragment myFragment = new MyFragment(this);
        myFragment.setListener(this);
        fragments.add(myFragment);

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
        tabsCell.addView(R.drawable.ic_tubiao1, R.drawable.ic_tubiao1_xuanzhong, "订单 ");
        tabsCell.addView(R.drawable.ic_shop_weixuan, R.drawable.ic_shop_xuanzhong, "店铺 ");
        tabsCell.addView(R.drawable.ic_wode_weixuan, R.drawable.ic_wode_xuanzhong, "我的 ");
    }

 /*   class HomePagerAdapeter extends FragmentViewPagerAdapter {

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
    }*/


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            judgeExit();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    private void judgeExit() {
        if (!isExit) {
            isExit = true;
            Toast.makeText(getApplicationContext(), "再按一次后退键退出程序",
                    Toast.LENGTH_SHORT).show();
            // 利用handler延迟发送更改状态信息
            mHandler.sendEmptyMessageDelayed(0, 2000);
        } else {
            Intent intent = new Intent("qd.xmjj.baseActivity");
            intent.putExtra("closeAll", 1);
            sendBroadcast(intent);//发送广播*/

//            this.finish();
        }
    }

    private void judgePower() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
//            Toast.makeText(this, "请授权读写手机存储权限", Toast.LENGTH_SHORT).show();
            finish();
        }
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
//            Toast.makeText(this, "请授权读写手机存储权限", Toast.LENGTH_SHORT).show();
            finish();
        }
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
//            Toast.makeText(this, "请授权相机权限", Toast.LENGTH_SHORT).show();
            finish();
        }
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
//            Toast.makeText(this, "请授权定位权限", Toast.LENGTH_SHORT).show();
            finish();
        }
    }


    /*
    * 接口回调 StoreFragment修改头像成功后通知刷新activity数据方法
    * */
    @Override
    public void forRefreshStoreListener() {
        //刷新适配器数据
        pagerAdapter.UpdataNewData(initPagerTitle(), initFragment());
    }


    /*
    * 接口回调 MyFragment修改头像成功后通知刷新activity数据方法
    * */
    @Override
    public void forRefreshMyListener() {
        //刷新适配器数据
        pagerAdapter.UpdataNewData(initPagerTitle(), initFragment());

    }

    @Override
    protected void onResume() {
        super.onResume();
      /*  //刷新适配器数据  (未用 暂用接口回调)
        pagerAdapter.UpdataNewData(initPagerTitle(), initFragment());*/
    }
}
