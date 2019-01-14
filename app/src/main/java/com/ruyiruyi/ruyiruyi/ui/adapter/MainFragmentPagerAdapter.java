package com.ruyiruyi.ruyiruyi.ui.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * @作者 倾春
 * eml.dongbinjava@163.com
 * @创建日期 2018/8/16 16:40
 */

public class MainFragmentPagerAdapter extends FragmentPagerAdapter {
    private Context context;
    private List<Fragment> fragmentList;


    public MainFragmentPagerAdapter(FragmentManager fm, Context context, List<Fragment> fragmentList) {
        super(fm);
        this.context = context;
        this.fragmentList = fragmentList;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList == null ? 0 : fragmentList.size();
    }
}
