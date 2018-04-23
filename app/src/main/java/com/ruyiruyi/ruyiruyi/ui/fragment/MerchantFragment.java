package com.ruyiruyi.ruyiruyi.ui.fragment;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.ruyiruyi.ruyiruyi.MyApplication;
import com.ruyiruyi.ruyiruyi.R;
import com.ruyiruyi.ruyiruyi.db.DbConfig;
import com.ruyiruyi.ruyiruyi.db.model.Location;
import com.ruyiruyi.ruyiruyi.ui.activity.CityChooseActivity;
import com.ruyiruyi.ruyiruyi.ui.activity.SearchActivity;
import com.ruyiruyi.ruyiruyi.ui.activity.ShopGoodActivity;
import com.ruyiruyi.ruyiruyi.ui.activity.ShopHomeActivity;
import com.ruyiruyi.ruyiruyi.ui.adapter.MenuListAdapter;
import com.ruyiruyi.ruyiruyi.ui.listener.OnLoadMoreListener;
import com.ruyiruyi.ruyiruyi.ui.model.ServiceType;
import com.ruyiruyi.ruyiruyi.ui.model.StoreType;
import com.ruyiruyi.ruyiruyi.ui.multiType.LoadMore;
import com.ruyiruyi.ruyiruyi.ui.multiType.LoadMoreViewBinder;
import com.ruyiruyi.ruyiruyi.ui.multiType.Shop;
import com.ruyiruyi.ruyiruyi.ui.multiType.ShopViewBinder;
import com.ruyiruyi.ruyiruyi.ui.service.LocationService;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;
import com.ruyiruyi.rylibrary.cell.DropDownMenu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.drakeet.multitype.MultiTypeAdapter;
import rx.functions.Action1;

import static me.drakeet.multitype.MultiTypeAsserts.assertAllRegistered;
import static me.drakeet.multitype.MultiTypeAsserts.assertHasTheSameAdapter;

public class MerchantFragment extends Fragment implements ShopViewBinder.OnShopItemClick {

    private static final int MERCHANT_CITY_CHOOSE = 5;
    private static final String TAG = MerchantFragment.class.getSimpleName();
    public static final String SHOP_TYPE = "SHOPTYPE";  //0所有  1轮胎  2美容清洗   3汽车保养
    public static final int SEARCH_CODE = 6;
    private RecyclerView listView;
    private List<Object> items = new ArrayList<>();
    private MultiTypeAdapter adapter;
    public List<ServiceType> typeList;
    public List<Shop> shopList;
    private String shops[] = {"全部门店", "快修店", "维修厂", "美容店", "4S"};

    private String paixu[] = {"默认排序", "附近优先", "评分最高", "累计安装"};
   // private String shaixuan[] = {"全部", "汽车保养", "美容清洗", "改装", "安装"};
    public List<String> shaixuan;
    private DropDownMenu mDropDownMenu;

    //菜单标题
    private String headers[] = {"全部门店", "默认排序", "条件筛选"};
    private ListView listView1;
    private ListView listView2;
    private ListView listView3;
    private MenuListAdapter mMenuAdapter1;
    private MenuListAdapter mMenuAdapter2;
    private MenuListAdapter mMenuAdapter3;
    private List<View> popupViews = new ArrayList<>();
    private LinearLayout cityLayout;
    public String currentCity = "选择城市";
    private TextView cityText;
    private ImageView searchView;
    private String city;
    private double jingdu;
    private double weidu;
    private LocationService locationService;
    private int fromFragment;
    private int ischoos;
    private SwipeRefreshLayout swipeRefreshLayout;
    private OnLoadMoreListener onLoadMoreListener;
    private int shopType;
    private ImageView backLayout;
    public OnMerchantViewClick listener;
    private TextView fragmentTitle;

    public void setListener(OnMerchantViewClick listener) {
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_merchant,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = getArguments();
        shopType = bundle.getInt("SHOPTYPE",0);


        shaixuan = new ArrayList<>();

        //获取位置
        Location location = new DbConfig().getLocation();
        if (location!=null){
            currentCity = location.getCity();
            jingdu = location.getJingdu();
            weidu = location.getWeidu();
        }
        initView();

        typeList = new ArrayList<>();
        typeList.add(new ServiceType("汽车保养",0xffff3f7e));
        typeList.add(new ServiceType("美容清洗",0xff00a600));
        typeList.add(new ServiceType("安装",0xff007cf0));
        typeList.add(new ServiceType("轮胎",0xffb600ea));
        shopList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Shop shop = new Shop();
            shop.setServiceTypeList(typeList);
            shopList.add(shop);
        }

        initData();

    }

    private void initData() {


       // items.clear();
        if (items.size() > 0){
            items.remove(items.size()-1);
        }
        for (int i = 0; i < shopList.size(); i++) {
            items.add(shopList.get(i));
        }

        items.add(new LoadMore());
        assertAllRegistered(adapter,items);
        adapter.notifyDataSetChanged();

    }

    private void initView() {
        mDropDownMenu = (DropDownMenu)getView().findViewById(R.id.dropDownMenu);
        cityLayout = (LinearLayout) getView().findViewById(R.id.city_layout);
        cityText = (TextView) getView().findViewById(R.id.city_text);
        searchView = (ImageView) getView().findViewById(R.id.search_view);
        backLayout = (ImageView) getView().findViewById(R.id.back_layout);
        fragmentTitle = (TextView) getView().findViewById(R.id.fragment_title);
        if (shopType == 0){ //0所有  1轮胎  2美容清洗   3汽车保养
            fragmentTitle.setText("附近门店");
            shaixuan.add("全部");
            shaixuan.add("汽车保养");
            shaixuan.add("美容清洗");
            shaixuan.add("改装");
            shaixuan.add("轮胎");
        }else if (shopType ==1 ){
            fragmentTitle.setText("轮胎服务");
            shaixuan.add("轮胎");
        }else if (shopType ==2 ){
            fragmentTitle.setText("美容清洗");
            shaixuan.add("美容清洗");
        }else if (shopType ==3 ){
            fragmentTitle.setText("汽车保养");
            shaixuan.add("汽车保养");
        }

        cityLayout.setVisibility(shopType==0?View.VISIBLE:View.GONE);
        backLayout.setVisibility(shopType==0?View.GONE:View.VISIBLE);

        cityText.setText(currentCity);
        //init menu listview

        //这里是每个下拉菜单之后的布局,目前只是简单的listview作为展示
        listView1 = new ListView(getContext());
        listView2 = new ListView(getContext());
        listView3 = new ListView(getContext());

        listView1.setDividerHeight(0);
        listView2.setDividerHeight(0);
        listView3.setDividerHeight(0);

        mMenuAdapter1 = new MenuListAdapter(getContext(), Arrays.asList(shops));
        mMenuAdapter2 = new MenuListAdapter(getContext(), Arrays.asList(paixu));
        mMenuAdapter3 = new MenuListAdapter(getContext(), shaixuan);

        listView1.setAdapter(mMenuAdapter1);
        listView2.setAdapter(mMenuAdapter2);
        listView3.setAdapter(mMenuAdapter3);

        popupViews.add(listView1);
        popupViews.add(listView2);
        popupViews.add(listView3);

        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                mDropDownMenu.setTabText(shops[position]);
                mDropDownMenu.closeMenu();
            }
        });

        listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                mDropDownMenu.setTabText(paixu[position]);
                mDropDownMenu.closeMenu();
            }
        });

        listView3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                mDropDownMenu.setTabText(shaixuan.get(position));
                mDropDownMenu.closeMenu();
            }
        });


        swipeRefreshLayout = (SwipeRefreshLayout) getView().findViewById(R.id.swipre_refresh_layout);
        swipeRefreshLayout.setProgressViewEndTarget(true, 200);
        //下拉刷新
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //initdataFromService();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        listView = new RecyclerView(getContext());
        //加载更多
        listView.setOnScrollListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                Log.e(TAG, "onLoadMore: " + 1111 );
                initData();
            }
        });

        mDropDownMenu.setDropDownMenu(Arrays.asList(headers), popupViews,listView );
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        listView.setLayoutManager(linearLayoutManager);
        adapter = new MultiTypeAdapter(items);

        register();

        listView.setAdapter(adapter);
        assertHasTheSameAdapter(listView, adapter);

        RxViewAction.clickNoDouble(cityLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Intent intent = new Intent(getContext(), CityChooseActivity.class);
                        startActivityForResult(intent,HomeFragment.CITY_CHOOSE);
                    }
                });

        RxViewAction.clickNoDouble(searchView)
            .subscribe(new Action1<Void>() {
                @Override
                public void call(Void aVoid) {
                    Intent intent = new Intent(getContext(), SearchActivity.class);
                    startActivityForResult(intent,SEARCH_CODE);
                }
            });

        //回退方法
        RxViewAction.clickNoDouble(backLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Log.e(TAG, "call: --1--");
                        listener.onBackViewClickListener();
                    }
                });
    }

    private void register() {
        ShopViewBinder shopViewBinder = new ShopViewBinder(getContext());
        shopViewBinder.setListener(this);
        adapter.register(Shop.class, shopViewBinder);
        adapter.register(LoadMore.class,new LoadMoreViewBinder());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == HomeFragment.CITY_CHOOSE){
            String city = data.getStringExtra("CITY");
            currentCity = city;
            Log.e(TAG, "onActivityResult: " + currentCity);
            cityText.setText(currentCity);
        }
        if (resultCode == SEARCH_CODE){
            String search_str = data.getStringExtra("SEARCH_STR");
            Log.e(TAG, "onActivityResult: -----------+++++---------" + search_str);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * item的点击回调
     */
    @Override
    public void onShopItemClickListener() {
        if (shopType == 1){//轮胎服务  点击传值给Activity  做forResult返回
            listener.onShopItemClickListener();
        }else {
            startActivity(new Intent(getContext(), ShopGoodActivity.class));
        }
    }


    public interface OnMerchantViewClick{
        void onBackViewClickListener();
        void onShopItemClickListener();
    }
}