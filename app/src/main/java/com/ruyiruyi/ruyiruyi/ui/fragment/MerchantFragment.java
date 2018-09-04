package com.ruyiruyi.ruyiruyi.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

import com.ruyiruyi.ruyiruyi.R;
import com.ruyiruyi.ruyiruyi.db.DbConfig;
import com.ruyiruyi.ruyiruyi.db.model.Location;
import com.ruyiruyi.ruyiruyi.ui.activity.CityChooseActivity;
import com.ruyiruyi.ruyiruyi.ui.activity.SearchActivity;
import com.ruyiruyi.ruyiruyi.ui.activity.ShopGoodsNewActivity;
import com.ruyiruyi.ruyiruyi.ui.adapter.MenuListAdapter;
import com.ruyiruyi.ruyiruyi.ui.fragment.base.RyBaseFragment;
import com.ruyiruyi.ruyiruyi.ui.listener.OnLoadMoreListener;
import com.ruyiruyi.ruyiruyi.ui.model.ServiceType;
import com.ruyiruyi.ruyiruyi.ui.multiType.Empty;
import com.ruyiruyi.ruyiruyi.ui.multiType.EmptyViewBinder;
import com.ruyiruyi.ruyiruyi.ui.multiType.LoadMore;
import com.ruyiruyi.ruyiruyi.ui.multiType.LoadMoreViewBinder;
import com.ruyiruyi.ruyiruyi.ui.multiType.Shop;
import com.ruyiruyi.ruyiruyi.ui.multiType.ShopViewBinder;
import com.ruyiruyi.ruyiruyi.ui.service.LocationService;
import com.ruyiruyi.ruyiruyi.utils.RequestUtils;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;
import com.ruyiruyi.rylibrary.cell.DropDownMenu;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.drakeet.multitype.MultiTypeAdapter;
import rx.functions.Action1;

import static me.drakeet.multitype.MultiTypeAsserts.assertAllRegistered;
import static me.drakeet.multitype.MultiTypeAsserts.assertHasTheSameAdapter;

public class MerchantFragment extends RyBaseFragment implements ShopViewBinder.OnShopItemClick {

    private static final int MERCHANT_CITY_CHOOSE = 5;
    private static final String TAG = MerchantFragment.class.getSimpleName();
    public static final String SHOP_TYPE = "SHOPTYPE";  //0所有  1轮胎  2美容清洗   3汽车保养
    public static final int SEARCH_CODE = 6;
    private RecyclerView listView;
    private List<Object> items = new ArrayList<>();
    private MultiTypeAdapter adapter;
    public List<ServiceType> typeList;
    public List<Shop> shopList;
    private String shops[] = {"全部门店", "快修店", "维修厂", "美容店", "4S店"};

    private String paixu[] = {"默认排序", "附近优先"};
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
    public int currentPage = 1;
    public int currentPageCount = 10;
    private int total;
    private int allPager = 0;
    public String currentStoreType = "";//门店类型0:全部门店  1:4S店   2:快修店  3:维修厂  4:美容店
    public String currentRankType = "0"; //0:默认排序  1：附近优先
    public String currentServiceType = "";//门店服务类型 2:汽车保养  3:美容清洗  4:改装  5:轮胎服务


    public boolean isCleanData =  false;


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
        isCleanData = true;

        //获取位置
        Location location = new DbConfig(getContext()).getLocation();
        if (location!=null){
            currentCity = location.getCity();
            jingdu = location.getJingdu();
            weidu = location.getWeidu();
        }
        initView();
        shopList = new ArrayList<>();
        typeList = new ArrayList<>();


       /* typeList = new ArrayList<>();
        typeList.add(new ServiceType("汽车保养",0xffff3f7e));
        typeList.add(new ServiceType("美容清洗",0xff00a600));
        typeList.add(new ServiceType("安装",0xff007cf0));
        typeList.add(new ServiceType("轮胎",0xffb600ea));
        shopList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Shop shop = new Shop();
            shop.setServiceTypeList(typeList);
            shopList.add(shop);
        }*/

        initDataFromService("");


    }

    private void initDataFromService(String searchStr) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("page",currentPage);
            jsonObject.put("rows",10);
            jsonObject.put("cityName",currentCity);
            jsonObject.put("storeName",searchStr);
            Log.e(TAG, "initDataFromService: currentStoreType==" +currentStoreType );
            jsonObject.put("storeType",currentStoreType);//门店类型  1:4S店   2:快修店  3:维修厂  4:美容店

            jsonObject.put("rankType",currentRankType);//排序方式  0:默认排序  1：附近优先
            Log.e(TAG, "initDataFromService:currentServiceType== " +currentServiceType );
            if(shopType!=0){//门店服务类型 2:汽车保养  3:美容清洗  4:改装  5:轮胎服务
                jsonObject.put("serviceType",shopType); //针对性门店
            }else {
                jsonObject.put("serviceType",currentServiceType);
            }

            jsonObject.put("longitude",jingdu + "");
            jsonObject.put("latitude",weidu + "");
        } catch (JSONException e) {
        }
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "selectStoreByCondition");

        params.addBodyParameter("reqJson",jsonObject.toString());
        String token = new DbConfig(getContext()).getToken();
        params.addParameter("token",token);
        Log.e(TAG, "initDataFromService:---------- " + params );
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    Log.e(TAG, "onSuccess: " + result);
                    JSONObject jsonObject1 = new JSONObject(result);
                    String status = jsonObject1.getString("status");
                    String msg = jsonObject1.getString("msg");

                    if (status.equals("1")){
                        JSONObject data = jsonObject1.getJSONObject("data");
                        total = data.getInt("total");
                        //页数计算
                        if (total % currentPageCount > 0) {
                            allPager = (total / currentPageCount) +1;
                        }else {
                            allPager = total / currentPageCount;
                        }
                        Log.e(TAG, "onSuccess: +" + total);
                        Log.e(TAG, "onSuccess: +" + allPager);
                        shopList.clear();
                        JSONArray storeQuaryResVos = data.getJSONArray("storeQuaryResVos");
                        for (int i = 0; i < storeQuaryResVos.length(); i++) {
                            JSONObject storeObjecct = storeQuaryResVos.getJSONObject(i);
                            String distance = storeObjecct.getString("distance");
                            String storeAddress = storeObjecct.getString("storeAddress");
                            String storeId = storeObjecct.getString("storeId");
                            int storeIdInt = Integer.parseInt(storeId);
                            String storeImg = storeObjecct.getString("storeImg");
                            String storeName = storeObjecct.getString("storeName");
                            String storeType = storeObjecct.getString("storeType");
                            String storeTypeColor = storeObjecct.getString("storeTypeColor");
                            JSONArray serviceArray = storeObjecct.getJSONArray("storeServcieList");
                            List<ServiceType> serviceTypeList = new ArrayList<ServiceType>();
                            for (int j = 0; j < serviceArray.length(); j++) {
                                JSONObject serviceObject = serviceArray.getJSONObject(j);
                                JSONObject service = serviceObject.getJSONObject("service");
                                String color = service.getString("color");
                                String name = service.getString("name");
                                serviceTypeList.add(new ServiceType(name,color));
                           }
                            Shop shop = new Shop(storeIdInt,storeType,storeTypeColor,storeName,storeImg,storeAddress,distance);
                            shop.setServiceTypeList(serviceTypeList);
                            shopList.add(shop);
                          //  typeList.clear();
                        }
                        initData();
                    } else if (status.equals("-999")) {
                        showUserTokenDialog("您的账号在其它设备登录,请重新登录");
                    }else {
                        shopList.clear();
                        initData();
                    }

                } catch (JSONException e) {

                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private void initData() {
        if (isCleanData) {
            items.clear();
        }

        if (shopList.size() == 0 && currentPage ==1){//当数据源为空
            items.add(new Empty());
        }else {
            if (items.size() > 0 ){           //当item不是空 >0  移除最后的加载更多的item
                items.remove(items.size()-1);
            }
            for (int i = 0; i < shopList.size(); i++) {     //加载数据源
                items.add(shopList.get(i));
            }

            Log.e(TAG, "initData:---" + currentPage);
            Log.e(TAG, "initData: ---" + allPager);
            if (allPager>1 ){        //下拉加载
                if (allPager ==  currentPage){
                    items.add(new LoadMore("全部加载完毕！"));
                }else {
                    items.add(new LoadMore("加载更多...."));
                }
            }
        }
        isCleanData = false;

       // items.clear();

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
            shaixuan.add("安装");
            shaixuan.add("轮胎服务");
        }else if (shopType == 5 ){
            fragmentTitle.setText("轮胎服务");
            shaixuan.add("轮胎服务");
        }else if (shopType ==2 ){
            fragmentTitle.setText("汽车保养");
            shaixuan.add("汽车保养");
        }else if (shopType ==3 ){
            fragmentTitle.setText("美容清洗");
            shaixuan.add("美容清洗");
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
                isCleanData = true;
                currentPage = 1;
                //门店类型0:全部门店  1:4S店   2:快修店  3:维修厂  4:美容店
                if (shops[position].equals("4S店")) {
                    currentStoreType = "1";
                }else if (shops[position].equals("快修店")){
                    currentStoreType = "2";
                }else if (shops[position].equals("维修厂")){
                    currentStoreType = "3";
                }else if (shops[position].equals("美容店")){
                    currentStoreType = "4";
                }else {
                    currentStoreType = "";
                }
                mDropDownMenu.setTabText(shops[position]);
                mDropDownMenu.closeMenu();
                initDataFromService("");
            }
        });

        listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                isCleanData = true;
                currentPage = 1;
                if (paixu[position].equals("默认排序")) {
                    currentRankType = "0";
                }else if (paixu[position].equals("附近优先")){
                    currentRankType = "1";
                }
                mDropDownMenu.setTabText(paixu[position]);
                mDropDownMenu.closeMenu();
                initDataFromService("");
            }
        });

        listView3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Log.e(TAG, "onItemClick: ---"+shaixuan.get(position) );
                isCleanData = true;
                currentPage = 1;
                //门店服务类型 2:汽车保养  3:美容清洗  4:改装  5:轮胎服务
                if (shaixuan.get(position).equals("汽车保养")) {
                    currentServiceType = "2";
                }else if (shaixuan.get(position).equals("美容清洗")) {
                    currentServiceType = "3";
                }else if (shaixuan.get(position).equals("安装")) {
                    currentServiceType = "4";
                }else if (shaixuan.get(position).equals("轮胎服务")) {
                    currentServiceType = "5";
                }else {
                    currentServiceType = "";
                }
                mDropDownMenu.setTabText(shaixuan.get(position));
                mDropDownMenu.closeMenu();
                initDataFromService("");
            }
        });


        swipeRefreshLayout = (SwipeRefreshLayout) getView().findViewById(R.id.swipre_refresh_layout);
        swipeRefreshLayout.setProgressViewEndTarget(true, 200);
        //下拉刷新
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                isCleanData = true;
                currentPage = 1;
                initDataFromService("");
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        listView = new RecyclerView(getContext());
        //加载更多
        listView.setOnScrollListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {

                if (allPager > currentPage){
                    currentPage += 1;
                    isCleanData = false;
                    initDataFromService("");
                }
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
                    intent.putExtra(SearchActivity.TYPE,0);
                    startActivityForResult(intent,SEARCH_CODE);
                }
            });

        //回退方法
        RxViewAction.clickNoDouble(backLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        listener.onBackViewClickListener();
                    }
                });
    }

    private void register() {
        ShopViewBinder shopViewBinder = new ShopViewBinder(getContext());
        shopViewBinder.setListener(this);
        adapter.register(Shop.class, shopViewBinder);
        adapter.register(LoadMore.class,new LoadMoreViewBinder());
        adapter.register(Empty.class,new EmptyViewBinder());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e(TAG, "onActivityResult: ---2");
        if (resultCode == HomeFragment.CITY_CHOOSE){
            String city = data.getStringExtra("CITY");
            currentCity = city;
            cityText.setText(currentCity);
        }else if (resultCode == SEARCH_CODE){
            //搜索结果的返回
            String search_str = data.getStringExtra("SEARCH_STR");
            isCleanData = true;
            currentPage = 1;
            currentStoreType = "";
            currentServiceType = "";
            initDataFromService(search_str);
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
    public void onShopItemClickListener(int storeId, String storeMame, String storeImage, List<ServiceType> storeService, Shop shop) {
        if (shopType == 5){//轮胎服务  点击传值给Activity  做forResult返回
            listener.onShopItemClickListener(shop);
        }else {
           /* Intent intent = new Intent(getContext(), ShopHomeActivity.class);
            intent.putExtra("STOREID",storeId);
            startActivity(intent);*/
            Intent intent = new Intent(getContext(), ShopGoodsNewActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt(ShopGoodsNewActivity.FROM_TYPE,0);
            bundle.putInt(ShopGoodsNewActivity.STORE_ID,storeId);
            bundle.putString(ShopGoodsNewActivity.STORE_IMAGE,storeImage);
            bundle.putString(ShopGoodsNewActivity.STORE_NAME,storeMame);
            bundle.putSerializable(ShopGoodsNewActivity.STORE_SERVICE, (Serializable) storeService);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }


    public interface OnMerchantViewClick{
        void onBackViewClickListener();
        void onShopItemClickListener(Shop shop);
    }
}