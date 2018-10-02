package com.ruyiruyi.merchant.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * @作者 倾春
 * eml.dongbinjava@163.com
 * @创建日期 2018/10/02 16:40
 */

public class IncomeFragmentPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragmentList;
    private List<String> tags;
    private FragmentManager manager;


    public IncomeFragmentPagerAdapter(FragmentManager fm, List<Fragment> fragmentList) {
        super(fm);
        this.fragmentList = fragmentList;
        this.tags = new ArrayList<>();
        manager = fm;
    }

    public IncomeFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList == null ? 0 : fragmentList.size();
    }



    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        Fragment fragment = fragmentList.get(position);
        manager.beginTransaction().hide(fragment).commit();
    }
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        tags.add(makeFragmentName(container.getId(), getItemId(position)));
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        this.manager.beginTransaction().show(fragment).commit();
        return fragment;
    }

    public void UpdataNewData( List<Fragment> fragments) {
        if (this.tags != null) {
            FragmentTransaction fragmentTransaction = manager.beginTransaction();
            for (int i = 0; i < tags.size(); i++) {
                fragmentTransaction.remove(manager.findFragmentByTag(tags.get(i)));
            }
            fragmentTransaction.commit();
            manager.executePendingTransactions();
            tags.clear();
        }
        this.fragmentList = fragments;
        notifyDataSetChanged();
    }
    private static String makeFragmentName(int viewId, long id) {
        return "android:switcher:" + viewId + ":" + id;
    }

}
