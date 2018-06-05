package com.ruyiruyi.ruyiruyi.ui.activity;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ruyiruyi.ruyiruyi.R;
import com.ruyiruyi.ruyiruyi.db.DbConfig;
import com.ruyiruyi.ruyiruyi.db.model.Location;
import com.ruyiruyi.ruyiruyi.ui.listener.OnLoadMoreListener;
import com.ruyiruyi.ruyiruyi.ui.activity.base.RyBaseActivity;
import com.ruyiruyi.ruyiruyi.ui.multiType.Empty;
import com.ruyiruyi.ruyiruyi.ui.multiType.EmptyViewBinder;
import com.ruyiruyi.ruyiruyi.ui.multiType.GoodsShop;
import com.ruyiruyi.ruyiruyi.ui.multiType.GoodsShopViewBinder;
import com.ruyiruyi.ruyiruyi.ui.multiType.LoadMore;
import com.ruyiruyi.ruyiruyi.ui.multiType.LoadMoreViewBinder;
import com.ruyiruyi.ruyiruyi.utils.RequestUtils;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;
import com.ruyiruyi.rylibrary.cell.ActionBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import me.drakeet.multitype.MultiTypeAdapter;
import rx.functions.Action1;

import static me.drakeet.multitype.MultiTypeAsserts.assertAllRegistered;
import static me.drakeet.multitype.MultiTypeAsserts.assertHasTheSameAdapter;

public class  GoodsShopActivity extends RyBaseActivity implements GoodsShopViewBinder.OnGoodsShopItemClick {
    private static final String TAG = GoodsShopActivity.class.getSimpleName();
    private ActionBar actionBar;
    public static String CLASS_NAME = "CLASS_NAME";
    public static String CLASS_ID = "CLASS_ID";
    public static String FROMTYPE = "FROMTYPE";
    private String className;
    private int classId;
    private RecyclerView listView;
    private List<Object> items = new ArrayList<>();
    private MultiTypeAdapter adapter;
    public List<GoodsShop> goodsShopList;
    public int currentType = 0;  //0是综合  1是价格上  2是价格下   3是距离上
    private LinearLayout zongheLayour;
    private LinearLayout priceLayout;
    private LinearLayout distenceLayout;
    private TextView zongheText;
    private TextView priceText;
    private TextView distenceText;
    private ImageView priceImage;
    private SwipeRefreshLayout swipeRefreshLayout;
    private int fromType = 0; //0来自商品  1来自搜索
    private String searchStr;
    public String currentCity = "选择城市";
    private double jingdu;
    private double weidu;
    public int currentPage = 1;
    public int currentPageCount = 10;
    private int total;
    private int allPager = 0;
    public boolean isCleanData =  false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_shop);
        actionBar = (ActionBar) findViewById(R.id.my_action);
        //actionBar.setTitle("测试");;
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

        goodsShopList = new ArrayList<>();
        isCleanData = true;
        Intent intent = getIntent();
        fromType = intent.getIntExtra(FROMTYPE,0);
        if (fromType == 0){
            className = intent.getStringExtra(CLASS_NAME);
            actionBar.setTitle(className);;
            classId = intent.getIntExtra(CLASS_ID,0);
        }else if (fromType == 1){
            searchStr = intent.getStringExtra("SEARCH_STR");
            actionBar.setTitle(searchStr);;
        }
        //获取位置
        Location location = new DbConfig().getLocation();
        if (location!=null){
            currentCity = location.getCity();
            jingdu = location.getJingdu();
            weidu = location.getWeidu();
        }


        initView();
        initDataFromService();

    }

    private void intiData() {
        if (isCleanData) {
            items.clear();
        }

        if (goodsShopList.size() == 0 && currentPage ==1){  //当数据源为空
            items.add(new Empty());
        }else {
            if (items.size() > 0 ){         //当item不是空 >0  移除最后的加载更多的item
                items.remove(items.size()-1);
            }
            for (int i = 0; i < goodsShopList.size(); i++) {        //加载数据源
                items.add(goodsShopList.get(i));
            }
            if (allPager>1 ){           //下拉加载
                if (allPager ==  currentPage){
                    items.add(new LoadMore("全部加载完毕！"));
                }else {
                    items.add(new LoadMore("加载更多...."));
                }
            }
        }

        isCleanData = false;
        assertAllRegistered(adapter,items);
        adapter.notifyDataSetChanged();
    }

    private void initDataFromService() {

        JSONObject jsonObject = new JSONObject();
        try {
            if (fromType == 0){         //商品
                jsonObject.put("serviceId",classId);
                jsonObject.put("serviceName","" );
            }else if (fromType == 1){   //搜索
                jsonObject.put("serviceId", 0);
                jsonObject.put("serviceName",searchStr );
            }
            jsonObject.put("cityName", currentCity);
            jsonObject.put("longi",jingdu +"");
            jsonObject.put("lati", weidu + "");
            //0是综合  1是价格上  2是价格下   3是距离上
            if (currentType == 0){
                jsonObject.put("sort","");
                jsonObject.put("order","");
            }else if (currentType == 1){
                jsonObject.put("sort","price");
                jsonObject.put("order","desc");
            }else if (currentType == 2){
                jsonObject.put("sort","price");
                jsonObject.put("order","asc");
            }else if (currentType == 3){
                jsonObject.put("sort","distance");
                jsonObject.put("order","");
            }
            jsonObject.put("page", currentPage);
            jsonObject.put("rows",currentPageCount );
        } catch (JSONException e) {
        }
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "stockInfo/queryStockListByService");
        params.addBodyParameter("reqJson", jsonObject.toString());
        Log.e(TAG, "initDataFromService: --" + jsonObject.toString());
        String token = new DbConfig().getToken();
        params.addParameter("token", token);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess: " + result);
                JSONObject jsonObject1 = null;
                try {
                    jsonObject1 = new JSONObject(result);
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
                        JSONArray rows = data.getJSONArray("rows");
                        goodsShopList.clear();
                        for (int i = 0; i < rows.length(); i++) {
                            JSONObject object = rows.getJSONObject(i);
                            int id = object.getInt("id");
                            int serviceId = object.getInt("serviceId");
                            int serviceTypeId = object.getInt("serviceTypeId");
                            int storeId = object.getInt("storeId");
                            String imgUrl = object.getString("imgUrl");
                            String name = object.getString("name");
                            String price = object.getString("price");
                            String distance = object.getString("distance");
                            String storeName = object.getString("storeName");
                            String storeImage = object.getString("storeImg");
                            GoodsShop goodsShop = new GoodsShop(id, imgUrl, name , price ,distance,serviceId,serviceTypeId,storeId,storeName,storeImage);
                            goodsShopList.add(goodsShop);
                        }
                        intiData();

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

    private void initView() {
        listView = (RecyclerView) findViewById(R.id.goods_activity_listview);
        zongheLayour = (LinearLayout) findViewById(R.id.zonghe_layout);
        priceLayout = (LinearLayout) findViewById(R.id.price_layout);
        distenceLayout = (LinearLayout) findViewById(R.id.diatence_layout);
        zongheText = (TextView) findViewById(R.id.zonghe_text);
        priceText = (TextView) findViewById(R.id.price_text);
        distenceText = (TextView) findViewById(R.id.distence_text);
        priceImage = (ImageView) findViewById(R.id.price_image);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.goods_swipre_refresh_layout);
        swipeRefreshLayout.setProgressViewEndTarget(true, 200);
        //下拉刷新
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                isCleanData = true;
                currentPage = 1;
                initDataFromService();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        //加载更多
        listView.setOnScrollListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (allPager > currentPage){
                    currentPage += 1;
                    isCleanData = false;
                    initDataFromService();
                }
            }
        });

        RxViewAction.clickNoDouble(zongheLayour)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        currentType = 0;
                        initTopView();
                    }

                });
        RxViewAction.clickNoDouble(priceLayout) //1是价格上  2是价格下
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (currentType == 1){
                            currentType = 2;
                        }else if (currentType == 2){
                            currentType = 1;
                        }else {
                            currentType = 1;
                        }
                        initTopView();
                    }
                });
        RxViewAction.clickNoDouble(distenceLayout) //3是距离上  4是距离下
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (currentType == 3){
                            currentType = 4;
                        }else if (currentType == 4){
                            currentType = 3;
                        }else {
                            currentType = 3;
                        }
                        initTopView();
                    }
                });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        listView.setLayoutManager(linearLayoutManager);
        adapter = new MultiTypeAdapter(items);
        register();

        listView.setAdapter(adapter);
        assertHasTheSameAdapter(listView, adapter);
    }

    private void initTopView() {
        isCleanData = true;
        if (currentType == 0){
            zongheText.setTextColor(getResources().getColor(R.color.theme_primary));
            priceText.setTextColor(getResources().getColor(R.color.c6));
            distenceText.setTextColor(getResources().getColor(R.color.c6));
            priceImage.setVisibility(View.INVISIBLE);
        }else if (currentType == 1){
            zongheText.setTextColor(getResources().getColor(R.color.c6));
            priceText.setTextColor(getResources().getColor(R.color.theme_primary));
            distenceText.setTextColor(getResources().getColor(R.color.c6));
            priceImage.setVisibility(View.VISIBLE);
            priceImage.setImageResource(R.drawable.ic_xia);
        }else if (currentType == 2){
            zongheText.setTextColor(getResources().getColor(R.color.c6));
            priceText.setTextColor(getResources().getColor(R.color.theme_primary));
            distenceText.setTextColor(getResources().getColor(R.color.c6));
            priceImage.setVisibility(View.VISIBLE);
            priceImage.setImageResource(R.drawable.ic_shang);
        }else if (currentType == 3){
            zongheText.setTextColor(getResources().getColor(R.color.c6));
            priceText.setTextColor(getResources().getColor(R.color.c6));
            distenceText.setTextColor(getResources().getColor(R.color.theme_primary));
            priceImage.setVisibility(View.INVISIBLE);
        }
        initDataFromService();
    }

    private void register() {
        GoodsShopViewBinder goodsShopViewBinder = new GoodsShopViewBinder(this);
        goodsShopViewBinder.setListener(this);
        adapter.register(GoodsShop.class, goodsShopViewBinder);
        adapter.register(LoadMore.class,new LoadMoreViewBinder());
        adapter.register(Empty.class,new EmptyViewBinder());
    }

    @Override
    public void onGoodsShopItemClickListener(GoodsShop goodsShop) {
        Intent intent = new Intent(getApplicationContext(), ShopGoodsNewActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(ShopGoodsNewActivity.FROM_TYPE,1);
        bundle.putInt(ShopGoodsNewActivity.STORE_ID,goodsShop.getStoreId());
        bundle.putString(ShopGoodsNewActivity.STORE_NAME,goodsShop.getStoreName());
        bundle.putString(ShopGoodsNewActivity.STORE_IMAGE,goodsShop.getStoreImage());
        bundle.putInt(ShopGoodsNewActivity.GOODS_CLASS_ID,goodsShop.getServiceId());
        bundle.putInt(ShopGoodsNewActivity.GOODS_CLASS_TYPE_ID,goodsShop.getServiceTypeId());
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
