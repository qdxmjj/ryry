package com.ruyiruyi.merchant.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.List;

public class MyPagerAdapter extends FragmentPagerAdapter {
    private List<String> title_list;
    private List<Fragment> fragments;
    private FragmentManager manager;
    private List<String> tags;

    public MyPagerAdapter(FragmentManager fm,  List<String> title_list, List<Fragment> fragments) {
        super(fm);
        this.tags = new ArrayList<>();
        manager = fm;
        this.title_list = title_list;
        this.fragments = fragments;
    }

    public MyPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return fragments == null ? 0 : fragments.size();
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getItemPosition(Object object) {
            return POSITION_NONE;
    }

    //设置适配器（适配器中必须重写这一步，不然标题不会显示出来）
    @Override
    public CharSequence getPageTitle(int position) {
        return title_list.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        Fragment fragment = fragments.get(position);
        manager.beginTransaction().hide(fragment).commit();
    }
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        tags.add(makeFragmentName(container.getId(), getItemId(position)));
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        this.manager.beginTransaction().show(fragment).commit();
        return fragment;
    }

    public void UpdataNewData( List<String> title_list,List<Fragment> fragments) {
        if (this.tags != null) {
            FragmentTransaction fragmentTransaction = manager.beginTransaction();
            for (int i = 0; i < tags.size(); i++) {
                fragmentTransaction.remove(manager.findFragmentByTag(tags.get(i)));
            }
            fragmentTransaction.commit();
            manager.executePendingTransactions();
            tags.clear();
        }
        this.fragments = fragments;
        this.title_list.clear();
        this.title_list = title_list;
        notifyDataSetChanged();
    }
    private static String makeFragmentName(int viewId, long id) {
        return "android:switcher:" + viewId + ":" + id;
    }


}