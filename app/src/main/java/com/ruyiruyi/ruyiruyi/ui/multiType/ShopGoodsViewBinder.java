package com.ruyiruyi.ruyiruyi.ui.multiType;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ruyiruyi.ruyiruyi.R;
import com.ruyiruyi.ruyiruyi.ui.cell.TabItemView;
import com.ruyiruyi.ruyiruyi.ui.fragment.GoodsListFragment;

import java.util.ArrayList;
import java.util.List;

import me.drakeet.multitype.ItemViewProvider;
import me.drakeet.multitype.MultiTypeAdapter;

import static me.drakeet.multitype.MultiTypeAsserts.assertHasTheSameAdapter;

public class ShopGoodsViewBinder extends ItemViewProvider<ShopGoods, ShopGoodsViewBinder.ViewHolder> {
    private static final String TAG = ShopGoodsViewBinder.class.getSimpleName();
    public Context context;
    private SimpleFragmentPagerAdapter pagerAdapter;
    public FragmentManager fragmentManager;

    public ShopGoodsViewBinder(Context context, FragmentManager fragmentManager) {
        this.context = context;
        this.fragmentManager = fragmentManager;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_shop_goods, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull ShopGoods shopGoods) {
        getFragments();
        getTitles();
        getTabViews();
        pagerAdapter = new SimpleFragmentPagerAdapter(fragmentManager, context);
        holder.viewPager.setAdapter(pagerAdapter);
        holder.tabLayout.setupWithViewPager(holder.viewPager);
        holder.tabLayout.setTabMode(TabLayout.MODE_FIXED);
        if (getTabViews()!=null){
            for (int i = 0; i < holder.tabLayout.getTabCount(); i++) {
                TabLayout.Tab tab = holder.tabLayout.getTabAt(i);
                tab.setCustomView(pagerAdapter.getTabView(i));
            }
        }


    }

    private void register() {

    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final ViewPager viewPager;
        private final TabLayout tabLayout;

        ViewHolder(View itemView) {
            super(itemView);
            viewPager = (ViewPager) itemView.findViewById(R.id.viewPager);
            tabLayout = (TabLayout) itemView.findViewById(R.id.tabLayout);
        }
    }

    public class SimpleFragmentPagerAdapter extends FragmentPagerAdapter {


        public SimpleFragmentPagerAdapter(FragmentManager fm, Context context) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return getFragments().get(position);
        }

        @Override
        public int getCount() {
            Log.e(TAG, "getCount: --" + getFragments().size());
            return getFragments().size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (getTitles()!=null&&!getTitles().isEmpty()){
                return getTitles().get(position);
            }
            return null;
        }

        public View getTabView(int position) {
            return getTabViews().get(position);
        }

    }


    protected List<Fragment> getFragments() {
        List fragments = new ArrayList();
        GoodsListFragment qcbyFragment = new GoodsListFragment();
        Bundle qcbyBundle = new Bundle();
        qcbyBundle.putString(GoodsListFragment.SHOP_SERVICE_TYPE,"QCBY");
        qcbyFragment.setArguments(qcbyBundle);
        fragments.add(qcbyFragment);

        GoodsListFragment mrqxFragment = new GoodsListFragment();
        Bundle mrqxBundle = new Bundle();
        mrqxBundle.putString(GoodsListFragment.SHOP_SERVICE_TYPE,"MRQX");
        mrqxFragment.setArguments(mrqxBundle);
        fragments.add(mrqxFragment);

        GoodsListFragment azFragment = new GoodsListFragment();
        Bundle azBundle = new Bundle();
        azBundle.putString(GoodsListFragment.SHOP_SERVICE_TYPE,"AZ");
        azFragment.setArguments(azBundle);
        fragments.add(azFragment);

        GoodsListFragment ltfwFragment = new GoodsListFragment();
        Bundle ltfwBundle = new Bundle();
        ltfwBundle.putString(GoodsListFragment.SHOP_SERVICE_TYPE,"LTFW");
        ltfwFragment.setArguments(ltfwBundle);
        fragments.add(ltfwFragment);
        return fragments;
    }
    protected List<String> getTitles() {
        List list = new ArrayList();
        list.add("汽车保养");
        list.add("美容清洗");
        list.add("安装");
        list.add("轮胎服务");
        return list;
    }
    protected List<TabItemView> getTabViews() {
        return null;
    }

}