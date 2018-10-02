package com.ruyiruyi.merchant.ui.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.ruyiruyi.merchant.R;
import com.ruyiruyi.merchant.bean.ItemBottomBean;
import com.ruyiruyi.merchant.bean.ItemNullBean;
import com.ruyiruyi.merchant.db.DbConfig;
import com.ruyiruyi.merchant.ui.multiType.ItemBottomProvider;
import com.ruyiruyi.merchant.ui.multiType.ItemNullProvider;
import com.ruyiruyi.merchant.ui.multiType.OrderForShipment;
import com.ruyiruyi.merchant.ui.multiType.OrderForShipmentViewBinder;
import com.ruyiruyi.merchant.ui.multiType.listener.OnLoadMoreListener;
import com.ruyiruyi.merchant.utils.UtilsURL;
import com.ruyiruyi.rylibrary.base.BaseActivity;
import com.ruyiruyi.rylibrary.cell.ActionBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import me.drakeet.multitype.MultiTypeAdapter;

import static com.baidu.mapapi.BMapManager.getContext;
import static me.drakeet.multitype.MultiTypeAsserts.assertAllRegistered;
import static me.drakeet.multitype.MultiTypeAsserts.assertHasTheSameAdapter;

public class OrdersForShipmentActivity extends BaseActivity {
    private boolean isfirstResume = true;
    private SwipeRefreshLayout refLayout;
    private RecyclerView mRlv;
    private int total_all_page;
    private int mRows = 5;  // 设置默认一页加载10条数据
    private int current_page;
    private MultiTypeAdapter multiTypeAdapter;
    private List<Object> items = new ArrayList<>();
    private String TAG = OrdersForShipmentActivity.class.getSimpleName();
    private List<OrderForShipment> itemBeanList;
    private boolean isLoadMore = false;
    private boolean isLoadOver = false;
    private boolean isLoadMoreSingle = false;//上拉单次标志位
    private ProgressDialog startDialog;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    if (itemBeanList == null || itemBeanList.size() == 0) {//TODO
                        items.clear();
                        items.add(new ItemNullBean("暂无数据"));
                        assertAllRegistered(multiTypeAdapter, items);
                        multiTypeAdapter.notifyDataSetChanged();
                    } else {
                        items.clear();
                        for (int i = 0; i < itemBeanList.size(); i++) {
                            items.add(itemBeanList.get(i));
                        }
                        assertAllRegistered(multiTypeAdapter, items);
                        multiTypeAdapter.notifyDataSetChanged();
                    }
                    break;
            }
        }
    };
    private ActionBar mActionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders_for_shipment);
        mActionBar = (ActionBar) findViewById(R.id.macbar);
        mActionBar.setTitle("订单发货");
        mActionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(int var1) {
                switch (var1) {
                    case -1:
                        onBackPressed();
                        break;
                }
            }
        });

        initView();
        initSwipeLayout();
        bindView();
        initData();
        bindData();
    }

    private void bindData() {

    }

    private void bindView() {

    }

    //初始化下拉上拉
    private void initSwipeLayout() {
        refLayout.setColorSchemeResources(//下拉刷新圆圈颜色
                R.color.theme_primary,
                R.color.c5,
                R.color.c6,
                R.color.c7
        );
        refLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //加载最新数据并更新adapter数据

                isLoadMore = false;
                myDownRefreshByServer();

                refLayout.setRefreshing(false);
            }
        });
        //加载更多
        mRlv.setOnScrollListener(new OnLoadMoreListener() {

            @Override
            public void onLoadMore() {
                Log.e(TAG, "onLoadMore0: " );
                if (isLoadMoreSingle) {
                    return;
                }
                isLoadMoreSingle = true;//上拉单次标志位

                if (total_all_page > current_page) {
                    current_page++;
                    items.add(new ItemBottomBean("加载更多..."));

                    isLoadMore = true;
                    initDataByLoadMoreType();
                } else {
                    if (!isLoadOver && (total_all_page > 1)) {//用于判断是否加  加载完成底部
                        items.add(new ItemBottomBean("全部加载完毕!"));
                        isLoadOver = true;
                    }
                }
                assertAllRegistered(multiTypeAdapter, items);
                multiTypeAdapter.notifyDataSetChanged();
            }
        });
    }

    private void initDataByLoadMoreTypeFake() { //TODO 假数据
        /*  数据加载完成前显示加载动画 */
        startDialog = new ProgressDialog(getContext());
//        showDialogProgress(startDialog, "商品信息加载中...");

        isLoadOver = false;

        if (!isLoadMore) {//只有加载更多(不清空原数据)
            itemBeanList.clear();
            current_page = 1;
        }

        total_all_page = 100;
        Random random = new Random();
        int ran = random.nextInt(10);
        for (int i = ran; i < mRows; i++) {
            OrderForShipment bean = new OrderForShipment();
            bean.setUserId(i);
            bean.setStoreId(1);
            bean.setOrderNo(123456789 + i + "");
            bean.setOrderTime(131231211);
            bean.setShoeConsistent(i % 2 == 0);
            bean.setOrderImg_front("http://180.76.243.205:8111/images-new/tyreInfo/tyreFigure/RA710-B.jpg");
            bean.setShoeTitle_front("ROADCRUZA/运动操控/225/45R17/94W/W" + i);
            bean.setShoePrice_front(i);
            if (i % 2 != 0) {
                bean.setOrderImg_rear("http://180.76.243.205:8111/images-new/tyreInfo/tyreFigure/RA710-C.jpg");
                bean.setShoeTitle_rear("ROADCRUZA/静谧舒适/225/45R18/93W/W" + i);
                bean.setShoePrice_rear(i);
            }
            bean.setCarNumber("鲁B66666");
            bean.setUserPhone("18888888888");
            itemBeanList.add(bean);
        }

        //更新适配器数据 handler 中进行
        Message message = new Message();
        message.what = 1;
        mHandler.sendMessage(message);

        isLoadMoreSingle = false;//重置加载更多单次标志位
    }

    //公用下载
    private void initDataByLoadMoreType() {
        //数据加载完成前显示加载动画
//        startDialog = new ProgressDialog(getContext());
//        showDialogProgress(startDialog, "商品信息加载中...");

        isLoadOver = false;

        if (!isLoadMore) {//只有加载更多(不清空原数据)
            itemBeanList.clear();
            current_page = 1;
        }

        //下载数据
        final int storeId = new DbConfig(this).getId();
        RequestParams params = new RequestParams(UtilsURL.REQUEST_URL_FAHUO + "orderInfo/selectWaitingPostOrdersList");
        params.addBodyParameter("storeId", storeId + "");
        params.addBodyParameter("page", current_page + "");
        params.addBodyParameter("rows", mRows + "");
        Log.e(TAG, "orderforshipment:  params.toString() = " + params.toString());
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess:orderforshipment result =  " + result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    Log.e(TAG, "onSuccess onLoadMore : total = " + jsonObject.getInt("total") );
                    total_all_page = (jsonObject.getInt("total")) / mRows;//处理页数
                    if (jsonObject.getInt("total") % mRows > 0) {
                        total_all_page++;
                    }
                    JSONArray rows = jsonObject.getJSONArray("rows");
                    for (int i = 0; i < rows.length(); i++) {
                        Log.e(TAG, "onSuccess: " + i);
                        JSONObject one = (JSONObject) rows.get(i);
                        OrderForShipment itemBean = new OrderForShipment();
                        itemBean.setUserId(one.getInt("userId"));
                        itemBean.setStoreId(storeId);
                        itemBean.setOrderNo(one.getString("no"));
                        itemBean.setOrderId(one.getInt("id"));
                        itemBean.setOrderTime(one.getLong("time"));
                        itemBean.setCarNumber(one.getString("platNumber"));
                        itemBean.setUserPhone(one.getString("phone"));
                        /*轮胎信息*/
                        JSONArray shoeOrderDTOList = one.getJSONArray("shoeOrderDTOList");
                        if (shoeOrderDTOList != null) {

                            int cNun_front = 0;
                            int cNun_rear = 0;
                            for (int j = 0; j < shoeOrderDTOList.length(); j++) {
                                JSONObject objbean = (JSONObject) shoeOrderDTOList.get(j);
                                int fontRearFlag = objbean.getInt("tyreFlag");
                                if (fontRearFlag == 0) {
                                    itemBean.setShoeConsistent(true);
                                    int num = objbean.getInt("tyreNum");
                                    cNun_front += num;
                                    itemBean.setShoeNum_front(cNun_front);
                                    itemBean.setTyreId_front(objbean.getInt("tyreId"));
                                    itemBean.setOrderImg_front(objbean.getString("orderImg"));
                                    itemBean.setFontRearFlag_front(fontRearFlag);
                                    itemBean.setShoePrice_front(objbean.getDouble("tyrePrice"));
                                    itemBean.setShoeTitle_front(objbean.getString("tyreName"));

                                }
                                if (fontRearFlag == 1) {
                                    itemBean.setShoeConsistent(false);
                                    int num = objbean.getInt("tyreNum");
                                    cNun_front += num;
                                    itemBean.setShoeNum_front(cNun_front);
                                    itemBean.setTyreId_front(objbean.getInt("tyreId"));
                                    itemBean.setOrderImg_front(objbean.getString("orderImg"));
                                    itemBean.setFontRearFlag_front(fontRearFlag);
                                    itemBean.setShoePrice_front(objbean.getDouble("tyrePrice"));
                                    itemBean.setShoeTitle_front(objbean.getString("tyreName"));

                                }
                                if (fontRearFlag == 2) {
                                    itemBean.setShoeConsistent(false);
                                    int num = objbean.getInt("tyreNum");
                                    cNun_rear += num;
                                    itemBean.setShoeNum_rear(cNun_rear);
                                    itemBean.setTyreId_rear(objbean.getInt("tyreId"));
                                    itemBean.setOrderImg_rear(objbean.getString("orderImg"));
                                    itemBean.setFontRearFlag_rear(fontRearFlag);
                                    itemBean.setShoePrice_rear(objbean.getDouble("tyrePrice"));
                                    itemBean.setShoeTitle_rear(objbean.getString("tyreName"));

                                }
                            }
                        }


                        itemBeanList.add(itemBean);
                    }
                    //更新适配器数据 handler 中进行
                    Message message = new Message();
                    message.what = 1;
                    mHandler.sendMessage(message);

                    isLoadMoreSingle = false;//重置加载更多单次标志位

                } catch (JSONException e) {
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(getContext(), "请检查网络", Toast.LENGTH_SHORT).show();

                //网络异常
                updataNetError();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                //加载完成 隐藏加载动画
//                hideDialogProgress(startDialog);
            }
        });
    }

    private void updataNetError() {
        items.clear();
        items.add(new ItemNullBean(R.drawable.ic_net_error));
        assertAllRegistered(multiTypeAdapter, items);
        multiTypeAdapter.notifyDataSetChanged();

    }

    //下拉刷新
    private void myDownRefreshByServer() {//下拉刷新
        initDataByLoadMoreType();
    }

    //加载原始数据
    private void initData() {
        initDataByLoadMoreType();
    }

    private void initView() {
        refLayout = findViewById(R.id.shipment_swipelayout);
        mRlv = findViewById(R.id.shipment_rlv);

        itemBeanList = new ArrayList<>();

        LinearLayoutManager manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mRlv.setLayoutManager(manager);
        multiTypeAdapter = new MultiTypeAdapter(items);
        OrderForShipmentViewBinder provider = new OrderForShipmentViewBinder(this);
        multiTypeAdapter.register(OrderForShipment.class, provider);
        multiTypeAdapter.register(ItemBottomBean.class, new ItemBottomProvider());
        multiTypeAdapter.register(ItemNullBean.class, new ItemNullProvider());
        mRlv.setAdapter(multiTypeAdapter);
        assertHasTheSameAdapter(mRlv, multiTypeAdapter);

    }

    /**
     * 每次回到此页面刷新数据
     */
    @Override
    protected void onResume() {
        super.onResume();
        if (isfirstResume) {
            isfirstResume = false;
        } else {
            //刷新数据
            isLoadMore = false;
            initDataByLoadMoreType();
            //更新适配器数据 handler 中进行
            Message message = new Message();
            message.what = 1;
            mHandler.sendMessage(message);

            isLoadMoreSingle = false;//重置加载更多单次标志位
        }
    }
}
