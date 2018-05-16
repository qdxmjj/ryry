package com.ruyiruyi.ruyiruyi.ui.activity;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.ruyiruyi.ruyiruyi.R;
import com.ruyiruyi.ruyiruyi.db.DbConfig;
import com.ruyiruyi.ruyiruyi.ui.fragment.GoodsListFragment;
import com.ruyiruyi.ruyiruyi.ui.listener.OnLoadMoreListener;
import com.ruyiruyi.ruyiruyi.ui.multiType.Empty;
import com.ruyiruyi.ruyiruyi.ui.multiType.EmptyBig;
import com.ruyiruyi.ruyiruyi.ui.multiType.EmptyBigViewBinder;
import com.ruyiruyi.ruyiruyi.ui.multiType.EmptyViewBinder;
import com.ruyiruyi.ruyiruyi.ui.multiType.GoodsHorizontal;
import com.ruyiruyi.ruyiruyi.ui.multiType.GoodsVertical;
import com.ruyiruyi.ruyiruyi.ui.multiType.GoodsVerticalViewBinder;
import com.ruyiruyi.ruyiruyi.ui.multiType.LoadMore;
import com.ruyiruyi.ruyiruyi.ui.multiType.LoadMoreViewBinder;
import com.ruyiruyi.ruyiruyi.utils.RequestUtils;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;
import com.ruyiruyi.rylibrary.base.BaseActivity;
import com.ruyiruyi.rylibrary.cell.ActionBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import me.drakeet.multitype.MultiTypeAdapter;
import rx.functions.Action1;

import static me.drakeet.multitype.MultiTypeAsserts.assertAllRegistered;
import static me.drakeet.multitype.MultiTypeAsserts.assertHasTheSameAdapter;

public class GoodsActivity extends BaseActivity implements GoodsVerticalViewBinder.OnGoodsVerItemClick {
    private static final String TAG = GoodsActivity.class.getSimpleName();
    private ActionBar actionBar;
    private RecyclerView listView;
    private List<Object> items = new ArrayList<>();
    private MultiTypeAdapter adapter;
    private int goodsClassId;
    private TextView buyButton;
    public List<GoodsVertical> goodsVerticalList ;
    private SwipeRefreshLayout swipeRefreshLayout;
    public int currentPage = 1;
    public int currentPageCount = 10;
    private int total;
    private int allPager = 0;
    public boolean isCleanData =  false;
    public List<GoodsVertical> goodsList;
    public List<GoodsVertical> currenGoodsList;
    private TextView priceText;
    public double allPrice = 0.00;
    private List<GoodsHorizontal> chooseGoodsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods,R.id.my_action);
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
        Bundle bundle = intent.getExtras();
        goodsClassId = bundle.getInt(GoodsListFragment.GOODS_CLASS_ID);
        chooseGoodsList = ((List<GoodsHorizontal>) bundle.getSerializable("GOODSLIST"));
        goodsList = new ArrayList<>();
        goodsVerticalList = new ArrayList<>();
        currenGoodsList = new ArrayList<>();
        goodsList.clear();
        goodsVerticalList.clear();
        currenGoodsList.clear();
        initView();
        initDataFromService();
       // initData();
    }

    private void initDataFromService() {
        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("page",1);
            jsonObject.put("rows",10);
            jsonObject.put("storeId","");
            jsonObject.put("serviceTypeId","");
            jsonObject.put("servicesId",goodsClassId);
            jsonObject.put("stockStatus",1);
        } catch (JSONException e) {
        }
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "getStockByCondition");
        params.addBodyParameter("reqJson",jsonObject.toString());
        String token = new DbConfig().getToken();
        params.addParameter("token",token);
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
                        JSONObject data1 = jsonObject1.getJSONObject("data");
                        total = data1.getInt("total");
                        //页数计算
                        if (total % currentPageCount > 0) {
                            allPager = (total / currentPageCount) +1;
                        }else {
                            allPager = total / currentPageCount;
                        }
                        //获取商品页
                        JSONArray rows = data1.getJSONArray("rows");
                        goodsVerticalList.clear();
                        for (int i = 0; i < rows.length(); i++) {
                            JSONObject object = rows.getJSONObject(i);
                            int id = object.getInt("id");
                            String imgUrl = object.getString("imgUrl");
                            String name = object.getString("name");
                            int amount = object.getInt("amount");
                            int serviceTypeId = object.getInt("serviceTypeId");
                            long price = object.getLong("price");
                            GoodsVertical goodsVertical = new GoodsVertical(id, imgUrl, name, price +"", amount,0,goodsClassId,serviceTypeId);
                            goodsVerticalList.add(goodsVertical);
                        }

                        for (int i = 0; i <  goodsVerticalList.size(); i++) {
                            Log.e(TAG, "onSuccess: " + goodsVerticalList.get(i).getGoodsId()  + "---" + goodsVerticalList.get(i).getCurrentGoodsAmount());
                        }

                        for (int i = 0; i <  chooseGoodsList.size(); i++) {
                            Log.e(TAG, "onSuccess: " + chooseGoodsList.get(i).getGoodsId()  + "++++" + chooseGoodsList.get(i).getCurrentCount());
                        }

                        for (int i = 0; i < goodsVerticalList.size(); i++) {
                            for (int j = 0; j < chooseGoodsList.size(); j++) {
                                if (goodsVerticalList.get(i).getGoodsId() == chooseGoodsList.get(j).getGoodsId()) {
                                    Log.e(TAG, "onSuccess: ----" + chooseGoodsList.get(j).getCurrentCount());
                                    goodsVerticalList.get(i).setCurrentGoodsAmount(chooseGoodsList.get(j).getCurrentCount());
                                }
                            }
                        }
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
        if (goodsVerticalList.size() == 0 && currentPage == 1){
            items.add(new EmptyBig());
        }else {
            if (items.size() > 0 ){ //去除加载更多item
                items.remove(items.size()-1);
            }
            for (int i = 0; i < goodsVerticalList.size(); i++) {
                items.add(goodsVerticalList.get(i));
            }
            if (allPager>1 ){
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

    private void initView() {
        listView = (RecyclerView) findViewById(R.id.goods_listview);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipre_refresh_goods_layout);
        priceText = (TextView) findViewById(R.id.all_price_text);
        priceText.setText(allPrice + "");

        buyButton = (TextView) findViewById(R.id.tire_buy_button);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        listView.setLayoutManager(linearLayoutManager);
        adapter = new MultiTypeAdapter(items);
        register();

        listView.setAdapter(adapter);
        assertHasTheSameAdapter(listView, adapter);
        RxViewAction.clickNoDouble(buyButton)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        for (int i = 0; i < goodsList.size(); i++) {
                            if (goodsList.get(i).getCurrentGoodsAmount()!=0) {
                                currenGoodsList.add(goodsList.get(i));
                            }
                        }
                        Intent intent = new Intent();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("GOODSLIST", (Serializable) currenGoodsList);
                        bundle.putInt(GoodsListFragment.GOODS_CLASS_ID,goodsClassId);
                        bundle.putDouble("ALLPRICE",allPrice);
                        intent.putExtras(bundle);
                        setResult(GoodsListFragment.GOODS_FRAGMENT_RESULT,intent);
                        finish();
                    }
                });

        //下拉刷新
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                /*isCleanData = true;
                currentPage = 1;
                initDataFromService();*/
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
    }

    private void register() {
        GoodsVerticalViewBinder goodsVerticalViewBinder = new GoodsVerticalViewBinder(this);
        goodsVerticalViewBinder.setListener(this);
        adapter.register(GoodsVertical.class, goodsVerticalViewBinder);
        adapter.register(EmptyBig.class,new EmptyBigViewBinder());
        adapter.register(LoadMore.class,new LoadMoreViewBinder());
    }

    /**
     * 商品数量点击回调
     * @param goodsVertical
     */
    @Override
    public void onGoodsItemClickListener(GoodsVertical goodsVertical) {
        allPrice = 0.00;
        boolean hasGoods = false;
        if (goodsList.size() == 0){
            hasGoods = true;
            goodsList.add(goodsVertical);
        }else {
            for (int i = 0; i < goodsList.size(); i++) {
                if (goodsList.get(i).getGoodsId() == goodsVertical.getGoodsId()) {
                    hasGoods = true;
                    goodsList.get(i).setCurrentGoodsAmount(goodsVertical.getCurrentGoodsAmount());
                }
            }
        }
        if (!hasGoods){
            goodsList.add(goodsVertical);
        }
        for (int i = 0; i < goodsList.size(); i++) {
            double goodsPrice = Double.parseDouble(goodsList.get(i).getGoodsPrice());
            int currentGoodsAmount = goodsList.get(i).getCurrentGoodsAmount();
            allPrice = allPrice + (goodsPrice * currentGoodsAmount);
            Log.e(TAG, "onGoodsItemClickListener: " + goodsList.get(i).getGoodsName() + "----"  + currentGoodsAmount);
        }
        priceText.setText(allPrice + "");

       /* int hasGoods = 0; //0是添加  1是修改  2是删除
        int currenPosition = 0;

        if (goodsVertical.getCurrentGoodsAmount() > 0) {
            if (goodsList.size() == 0){
                goodsList.add(goodsVertical);
            }else {
                for (int i = 0; i < goodsList.size(); i++) {
                    if (goodsList.get(i).getGoodsId() == goodsVertical.getGoodsId()) { //判断数据之前是否已经存在
                        if (goodsList.get(i).getCurrentGoodsAmount() == 0){ //如果选中数量为0  删除改商品
                            hasGoods = 2;
                            currenPosition = i;
                        }else {
                            hasGoods = 1;
                            goodsList.get(i).setCurrentGoodsAmount(goodsVertical.getCurrentGoodsAmount());  //如果数量不为零 并且已存在 修改当前商品数量
                        }
                    }
                }
            }
        }else {
            hasGoods = 2;
        }

        if (hasGoods == 0){
            goodsList.add(goodsVertical);
        }else if (hasGoods == 2){
            goodsList.remove(currenPosition);
        }
        for (int i = 0; i < goodsList.size(); i++) {
            Log.e(TAG, "onGoodsItemClickListener: " + goodsList.get(i).getGoodsName() + "----"  + goodsList.get(i).getCurrentGoodsAmount());
        }

        Log.e(TAG, "onGoodsItemClickListener: " + goodsList.size());*/


    }
}
