package com.ruyiruyi.ruyiruyi.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ruyiruyi.ruyiruyi.R;
import com.ruyiruyi.ruyiruyi.ui.cell.TabItemView;
import com.ruyiruyi.ruyiruyi.ui.fragment.GoodsListFragment;
import com.ruyiruyi.ruyiruyi.ui.multiType.GoodsHorizontal;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;
import com.ruyiruyi.rylibrary.base.BaseActivity;
import com.ruyiruyi.rylibrary.cell.ActionBar;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import rx.functions.Action1;

public class ShopGoodActivity extends FragmentActivity implements GoodsListFragment.OnGoodsListSend{
    private static final String TAG = ShopGoodActivity.class.getSimpleName();
    private ActionBar actionBar;
    private  ViewPager viewPager;
    private  TabLayout tabLayout;
    private SimpleFragmentPagerAdapter pagerAdapter;
    private int storeid;
    private TextView qcbyCountText;
    private TextView mrqxCountText;
    private TextView gzCountText;
    private TextView ltfwCountText;
    public List<GoodsHorizontal> goodsChooseList;
    public List<GoodsHorizontal> goodsCommitList;
    public Double allPrice = 0.00;
    private TextView allPriceText;
    public int qcbyCount = 0;
    public int mrqxCount = 0;
    public int azCount = 0;
    public int ltfwCount = 0;
    private TextView goodBuyButton;
    private String storename;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_good);
        actionBar = (ActionBar) findViewById(R.id.my_action);
        actionBar.setTitle("商品列表");;
        actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick(){
            @Override
            public void onItemClick(int var1) {
                switch ((var1)){
                    case -1:
                        onBackPressed();
                        break;
                }
            }
        });
        Intent intent = getIntent();
        storeid = intent.getIntExtra("STOREID",0);
        storename = intent.getStringExtra("STORENAME");
        goodsChooseList = new ArrayList<>();
        goodsCommitList = new ArrayList<>();

        initView();


    }

    private void initView() {
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        qcbyCountText = (TextView) findViewById(R.id.qcby_count_text);
        mrqxCountText = (TextView) findViewById(R.id.mrqx_count_text);
        gzCountText = (TextView) findViewById(R.id.gz_count_text);
        ltfwCountText = (TextView) findViewById(R.id.ltfw_count_text);
        allPriceText = (TextView)findViewById(R.id.all_price_text);
        goodBuyButton = (TextView) findViewById(R.id.goods_buy_button);
        initCoutView();


        allPriceText.setText(allPrice+"");
        getFragments();
        getTitles();
        getTabViews();
        pagerAdapter = new SimpleFragmentPagerAdapter(getSupportFragmentManager(), this);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOffscreenPageLimit(3);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        if (getTabViews()!=null){
            for (int i = 0; i < tabLayout.getTabCount(); i++) {
                TabLayout.Tab tab = tabLayout.getTabAt(i);
                tab.setCustomView(pagerAdapter.getTabView(i));
            }
        }
        RxViewAction.clickNoDouble(goodBuyButton)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        goodsCommitList.clear();
                        for (int i = 0; i < goodsChooseList.size(); i++) {
                            if (goodsChooseList.get(i).getCurrentCount() != 0) {
                                goodsCommitList.add(goodsChooseList.get(i));
                            }
                        }
                        if (goodsCommitList.size() == 0){
                            Toast.makeText(ShopGoodActivity.this, "请选择商品", Toast.LENGTH_SHORT).show();
                        }else {
                            Intent intent = new Intent(getApplicationContext(), OrderGoodsAffirmActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("GOODSLIST", (Serializable) goodsCommitList);
                            bundle.putDouble("ALLPRICE",allPrice);
                            bundle.putInt("STOREID",storeid);
                            bundle.putString("STORENAME",storename);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                    }
                });
    }

    private void initCoutView() {
        qcbyCountText.setVisibility(qcbyCount==0?View.GONE:View.VISIBLE);
        mrqxCountText.setVisibility(mrqxCount==0?View.GONE:View.VISIBLE);
        gzCountText.setVisibility(azCount==0?View.GONE:View.VISIBLE);
        ltfwCountText.setVisibility(ltfwCount==0?View.GONE:View.VISIBLE);
        qcbyCountText.setText("" + qcbyCount);
        mrqxCountText.setText("" + mrqxCount);
        gzCountText.setText("" + azCount);
        ltfwCountText.setText("" + ltfwCount);
    }
    /**
     * 商品选择的回调
     * @param list
     */
    @Override
    public void onGoodsListSend(int classId,List<GoodsHorizontal> list) {
        if (goodsChooseList.size() == 0) {
            goodsChooseList.addAll(list);
        }else {
            for (int i = 0; i < goodsChooseList.size(); i++) { //将改变的那一小服务的商品数量全部归零
                if (goodsChooseList.get(i).getGoodsClassId() == classId) {
                    goodsChooseList.get(i).setCurrentCount(0);
                }
            }
            boolean hasGoods = false;
            for (int i = 0; i < list.size(); i++) {     //将不存在的商品添加到goodsChooseList中  已存在的归零后重新赋值
                hasGoods = false;
                for (int j = 0; j < goodsChooseList.size(); j++) {
                    if (goodsChooseList.get(j).getGoodsId() == list.get(i).getGoodsId()) {
                        hasGoods = true;
                        goodsChooseList.get(j).setCurrentCount(list.get(i).getCurrentCount());
                    }
                }
                if (!hasGoods){
                    goodsChooseList.add(list.get(i));
                }
            }
        }
        allPrice = 0.00;
        qcbyCount = 0;
        mrqxCount = 0;
        azCount = 0;
        ltfwCount = 0;
        for (int i = 0; i < goodsChooseList.size(); i++) { //计算所有商品总价
            int currentCount = goodsChooseList.get(i).getCurrentCount();
            double goodsPrice = Double.parseDouble(goodsChooseList.get(i).getGoodsPrice());
            allPrice = allPrice + (currentCount * goodsPrice);

            int serviceTypeId = goodsChooseList.get(i).getServiceTypeId();

            if (serviceTypeId == 2){
                qcbyCount += currentCount;
            }else if (serviceTypeId == 3){
                mrqxCount += currentCount;
            }else if (serviceTypeId == 4){
                azCount += currentCount;
            }else if (serviceTypeId == 5){
                ltfwCount += currentCount;
            }
        }
        Log.e(TAG, "onGoodsListSend:qcbyCount---- " + qcbyCount);
        Log.e(TAG, "onGoodsListSend:mrqxCount---- " + mrqxCount);
        Log.e(TAG, "onGoodsListSend:azCount---- " + azCount);
        Log.e(TAG, "onGoodsListSend:ltfwCount---- " + ltfwCount);
        initCoutView();
        allPriceText.setText(allPrice+"");

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
        qcbyBundle.putInt(GoodsListFragment.STORE_ID,storeid);
        qcbyFragment.setArguments(qcbyBundle);
        qcbyFragment.setListener(this);
        fragments.add(qcbyFragment);

        GoodsListFragment mrqxFragment = new GoodsListFragment();
        Bundle mrqxBundle = new Bundle();
        mrqxBundle.putString(GoodsListFragment.SHOP_SERVICE_TYPE,"MRQX");
        mrqxBundle.putInt(GoodsListFragment.STORE_ID,storeid);
        mrqxFragment.setArguments(mrqxBundle);
        mrqxFragment.setListener(this);
        fragments.add(mrqxFragment);

        GoodsListFragment azFragment = new GoodsListFragment();
        Bundle azBundle = new Bundle();
        azBundle.putString(GoodsListFragment.SHOP_SERVICE_TYPE,"GZ");
        azBundle.putInt(GoodsListFragment.STORE_ID,storeid);
        azFragment.setArguments(azBundle);
        azFragment.setListener(this);
        fragments.add(azFragment);

        GoodsListFragment ltfwFragment = new GoodsListFragment();
        Bundle ltfwBundle = new Bundle();
        ltfwBundle.putString(GoodsListFragment.SHOP_SERVICE_TYPE,"LTFW");
        ltfwBundle.putInt(GoodsListFragment.STORE_ID,storeid);
        ltfwFragment.setArguments(ltfwBundle);
        ltfwFragment.setListener(this);
        fragments.add(ltfwFragment);
        return fragments;
    }
    protected List<String> getTitles() {
        List list = new ArrayList();
        list.add("汽车保养");
        list.add("美容清洗");
        list.add("改装");
        list.add("轮胎服务");
        return list;
    }
    protected List<TabItemView> getTabViews() {
        return null;
    }
}
