package com.ruyiruyi.ruyiruyi.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ruyiruyi.ruyiruyi.R;
import com.ruyiruyi.ruyiruyi.db.DbConfig;
import com.ruyiruyi.ruyiruyi.db.model.Location;
import com.ruyiruyi.ruyiruyi.ui.activity.base.RyBaseActivity;
import com.ruyiruyi.ruyiruyi.ui.model.ServiceType;
import com.ruyiruyi.ruyiruyi.ui.multiType.Empty;
import com.ruyiruyi.ruyiruyi.ui.multiType.EmptyBig;
import com.ruyiruyi.ruyiruyi.ui.multiType.EmptyBigViewBinder;
import com.ruyiruyi.ruyiruyi.ui.multiType.EmptyViewBinder;
import com.ruyiruyi.ruyiruyi.ui.multiType.GoodsCar;
import com.ruyiruyi.ruyiruyi.ui.multiType.GoodsCarViewBinder;
import com.ruyiruyi.ruyiruyi.ui.multiType.GoodsNew;
import com.ruyiruyi.ruyiruyi.ui.multiType.GoodsNewViewBinder;
import com.ruyiruyi.ruyiruyi.ui.multiType.Left;
import com.ruyiruyi.ruyiruyi.ui.multiType.LeftViewBinder;
import com.ruyiruyi.ruyiruyi.utils.RequestUtils;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;
import com.ruyiruyi.rylibrary.cell.ActionBar;
import com.ruyiruyi.rylibrary.cell.flowlayout.FlowLayout;
import com.ruyiruyi.rylibrary.cell.flowlayout.TagAdapter;
import com.ruyiruyi.rylibrary.cell.flowlayout.TagFlowLayout;

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

public class ShopGoodsNewActivity extends RyBaseActivity implements LeftViewBinder.OnBigClassItemClick ,GoodsNewViewBinder.OnGoodsChangeClick,GoodsCarViewBinder.OnGoodsCarChangeClick {
    private static final String TAG = ShopGoodsNewActivity.class.getSimpleName();
    private ActionBar actionBar;
    public static String FROM_TYPE = "FROM_TYPE";
    public static String STORE_IMAGE = "STORE_IMAGE";
    public static String STORE_ID = "STORE_ID";
    public static String STORE_NAME = "STORE_NAME";
    public static String STORE_SERVICE = "STORE_SERVICE";
    public static String GOODS_CLASS_ID = "GOODS_CLASS_ID";
    public static String GOODS_CLASS_TYPE_ID = "GOODS_CLASS_TYPE_ID";
    private FrameLayout qcbyLayout;
    private FrameLayout mrqxLayout;
    private FrameLayout azgzLayout;
    private FrameLayout ltfwLayout;
    private View qcbyView;
    private View mrqxView;
    private View azgzView;
    private View ltfwView;
    public int currentBigType = 2; //2汽车保养  3美容清洗  4安装改装  5轮胎服务
    private int storeId;
    private String storeName;
    private String storeImage;
    private List<ServiceType> serviceList;
    private ImageView storeImageView;
    private TextView storeNameText;
    private TagFlowLayout tagFlowLayout;
    private RecyclerView classListView;
    private RecyclerView goodsListView;

    private List<Object> classItems = new ArrayList<>();
    private MultiTypeAdapter classAdapter;
    public List<Left> classList;
    public List<Left> qcbyClassList;
    public List<Left> mrqxClassList;
    public List<Left> azgzClassList;
    public List<Left> ltfwClassList;
    public List<GoodsNew> goodsNewList;
    public List<GoodsNew> currentGoodsList;
    public List<GoodsCar> goodsCarsList;

    private List<Object> goodsItems = new ArrayList<>();
    private MultiTypeAdapter goodsAdapter;

    private List<Object> goodsCarItems = new ArrayList<>();
    private MultiTypeAdapter goodsCarAdapter;
    private ProgressDialog progressDialog;
    public boolean classLoadIsTrue = false;
    public boolean goodsLoadIsTrue = false;

    public int currentClassId = 0;
    private FrameLayout shopCarLayout;
    private ImageView shopCarImage;
    public boolean isCarShow = false;
    private FrameLayout shopCarContentLayout;
    private RecyclerView goodsCarListView;
    private LinearLayout deleteCarShopLayout;
    private TextView goodsCountText;

    public int goodsAllCount = 0;
    public double goodsAllPrice = 0;
    private TextView goodsAllPriceText;
    private TextView qcbyCountText;
    private TextView mrqxCountText;
    private TextView azgzCountText;
    private TextView ltfwCountText;
    private TextView goodsBuyButton;
    private LinearLayout shopLayout;
    private int fromType = 0;   //0是来自附近门店  1是来自商品
    private int goodsClassId;
    private int goodsClassTypeId;

    private double jingdu;
    private double weidu;
    public String currentCity = "选择城市";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_goods_new);
        actionBar = (ActionBar) findViewById(R.id.my_action);
        actionBar.setTitle("门店首页");;
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
        classList = new ArrayList<>();
        qcbyClassList = new ArrayList<>();
        mrqxClassList = new ArrayList<>();
        azgzClassList = new ArrayList<>();
        ltfwClassList = new ArrayList<>();
        goodsNewList = new ArrayList<>();
        currentGoodsList = new ArrayList<>();
        goodsCarsList = new ArrayList<>();
        progressDialog = new ProgressDialog(this);

        //获取位置
        Location location = new DbConfig(this).getLocation();
        if (location != null) {
            currentCity = location.getCity();
            jingdu = location.getJingdu();
            weidu = location.getWeidu();
        }

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        fromType = bundle.getInt(FROM_TYPE,0);
        storeId = bundle.getInt(STORE_ID,0);
        storeName = bundle.getString(STORE_NAME);
        storeImage = bundle.getString(STORE_IMAGE);

        initView();

        Glide.with(this).load(storeImage).into(storeImageView);
        storeNameText.setText(storeName);

        if (fromType == 1){
            goodsClassId = bundle.getInt(GOODS_CLASS_ID);
            goodsClassTypeId = bundle.getInt(GOODS_CLASS_TYPE_ID);
            currentBigType = goodsClassTypeId;
            currentClassId = goodsClassId;
            initShopData();
          // initShopInfoView();
        }else {
            currentBigType = 2;
            serviceList = ((List<ServiceType>) bundle.getSerializable(STORE_SERVICE));
           // initShopInfoView();
            final LayoutInflater inflater = LayoutInflater.from(this);
            tagFlowLayout.setAdapter(new TagAdapter<ServiceType>(serviceList) {
                @Override
                public View getView(FlowLayout parent, int position, ServiceType serviceType) {
                    TextView tv = (TextView) inflater.inflate(R.layout.item_type,
                            tagFlowLayout, false);
                    int parseColor = Color.parseColor(serviceType.getServiceColor());
                    tv.setTextColor(parseColor);
                    tv.setText(serviceType.getServiceName());
                    GradientDrawable drawable = (GradientDrawable) tv.getBackground();
                    drawable.setStroke(2,parseColor);
                    return tv;
                }
            });
        }





        initBigClassView();
        showDialogProgress(progressDialog,"加载中...");
        initClassDataFromService();


    }

    private void initShopData() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("storeId", storeId);
            jsonObject.put("longitude", jingdu);
            jsonObject.put("latitude", weidu);
        } catch (JSONException e) {
        }
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "getStoreInfoByStoreId");
        params.addBodyParameter("reqJson", jsonObject.toString());
        String token = new DbConfig(this).getToken();
        params.addParameter("token", token);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                JSONObject jsonObject1 = null;
                try {
                    jsonObject1 = new JSONObject(result);
                    String status = jsonObject1.getString("status");
                    String msg = jsonObject1.getString("msg");
                    if (status.equals("1")){
                        JSONObject data = jsonObject1.getJSONObject("data");
                        JSONArray storeServcieList = data.getJSONArray("storeServcieList");
                        serviceList.clear();
                        for (int i = 0; i < storeServcieList.length(); i++) {
                            JSONObject object = storeServcieList.getJSONObject(i);
                            JSONObject service = object.getJSONObject("service");
                            String name = service.getString("name");
                            String color = service.getString("color");
                            serviceList.add(new ServiceType(name, color));
                        }
                        final LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
                        tagFlowLayout.setAdapter(new TagAdapter<ServiceType>(serviceList) {
                            @Override
                            public View getView(FlowLayout parent, int position, ServiceType serviceType) {
                                TextView tv = (TextView) inflater.inflate(R.layout.item_type,
                                        tagFlowLayout, false);
                                int parseColor = Color.parseColor(serviceType.getServiceColor());
                                tv.setTextColor(parseColor);
                                tv.setText(serviceType.getServiceName());
                                GradientDrawable drawable = (GradientDrawable) tv.getBackground();
                                drawable.setStroke(2,parseColor);
                                return tv;
                            }
                        });
                      //  initShopInfoView();
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

    private void initGoodsDataFromService() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("shopId", storeId);
        } catch (JSONException e) {
        }
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "stockInfo/queryStockListByStore");
        params.addBodyParameter("reqJson", jsonObject.toString());
        String token = new DbConfig(this).getToken();
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
                        JSONArray rows = data.getJSONArray("rows");
                        goodsNewList.clear();
                        for (int i = 0; i < rows.length(); i++) {
                            JSONObject object = rows.getJSONObject(i);
                            int id = object.getInt("id");
                            int serviceId = object.getInt("serviceId");
                            int serviceTypeId = object.getInt("serviceTypeId");
                            int amount = object.getInt("amount");
                            String imgUrl = object.getString("imgUrl");
                            String name = object.getString("name");
                            String price = object.getString("price");
                            GoodsNew goodsNew = new GoodsNew(id, imgUrl, name, price, amount, 0, serviceId, serviceTypeId);
                            goodsNewList.add(goodsNew);
                        }
                        Log.e(TAG, "onSuccess: ----------------------");

                        initClassData();
                        //initGoodsData();

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
                hideDialogProgress(progressDialog);
               /* goodsLoadIsTrue = true;
                if (classLoadIsTrue){
                    hideDialogProgress(progressDialog);
                }*/
            }
        });
    }
    private void initClassDataFromService() {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("storeId", storeId);
        } catch (JSONException e) {
        }
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "getStoreAddedServices");
        params.addBodyParameter("reqJson", jsonObject.toString());
        String token = new DbConfig(this).getToken();
        params.addParameter("token", token);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    Log.e(TAG, "onSuccess: " + result);
                    JSONObject jsonObject1 = new JSONObject(result);
                    String status = jsonObject1.getString("status");
                    String msg = jsonObject1.getString("msg");
                    if (status.equals("1")) {
                        JSONObject data = jsonObject1.getJSONObject("data");
                        JSONArray mrqxObject = null;
                        JSONArray qcbyObject = null;

                        JSONArray azgzObject = null;
                        JSONArray ltfwObject = null;
                        try {
                            mrqxObject = data.getJSONArray("美容清洗");
                        }catch (Exception e){

                        }
                        try {
                            qcbyObject = data.getJSONArray("汽车保养");
                        }catch (Exception e){

                        }
                        try {
                            azgzObject = data.getJSONArray("安装改装");
                        }catch (Exception e){

                        }
                        try {
                            ltfwObject = data.getJSONArray("轮胎服务");
                        }catch (Exception e){

                        }


                        //美容清洗的 class
                        if (mrqxObject!=null){
                            for (int i = 0; i < mrqxObject.length(); i++) {
                                JSONObject objectJSONObject = mrqxObject.getJSONObject(i);
                                String serviceId = objectJSONObject.getString("serviceId");
                                int serviceIdInt = Integer.parseInt(serviceId);
                                String serviceName = objectJSONObject.getString("serviceName");
                                String serviceTypeId = objectJSONObject.getString("serviceTypeId");
                                String serviceTypeName = objectJSONObject.getString("serviceTypeName");
                                Left left = null;
                                if (i == 0){
                                    left = new Left(serviceName, true);
                                    left.setClassId(serviceId);
                                }else {
                                    left = new Left(serviceName, false);
                                    left.setClassId(serviceId);
                                }
                                mrqxClassList.add(left);
                            }
                        }
                       if (qcbyObject != null){
                           //汽车保养的 class
                           for (int i = 0; i < qcbyObject.length(); i++) {
                               JSONObject objectJSONObject = qcbyObject.getJSONObject(i);
                               String serviceId = objectJSONObject.getString("serviceId");
                               int serviceIdInt = Integer.parseInt(serviceId);
                               String serviceName = objectJSONObject.getString("serviceName");
                               String serviceTypeId = objectJSONObject.getString("serviceTypeId");
                               String serviceTypeName = objectJSONObject.getString("serviceTypeName");
                               Left left = null;
                               if (i == 0){
                                   if (fromType==0){
                                       currentClassId = serviceIdInt;
                                   }
                                   left = new Left(serviceName, true);
                                   left.setClassId(serviceId);
                               }else {
                                   left = new Left(serviceName, false);
                                   left.setClassId(serviceId);
                               }
                               qcbyClassList.add(left);
                           }
                       }

                        if (azgzObject !=null){
                            //安装改装的 class
                            for (int i = 0; i < azgzObject.length(); i++) {
                                JSONObject objectJSONObject = azgzObject.getJSONObject(i);
                                String serviceId = objectJSONObject.getString("serviceId");
                                int serviceIdInt = Integer.parseInt(serviceId);
                                String serviceName = objectJSONObject.getString("serviceName");
                                String serviceTypeId = objectJSONObject.getString("serviceTypeId");
                                String serviceTypeName = objectJSONObject.getString("serviceTypeName");
                                Left left = null;
                                if (i == 0){
                                    left = new Left(serviceName, true);
                                    left.setClassId(serviceId);
                                }else {
                                    left = new Left(serviceName, false);
                                    left.setClassId(serviceId);
                                }
                                azgzClassList.add(left);
                            }
                        }

                        if (ltfwObject !=null){
                            //轮胎服务的 class
                            for (int i = 0; i < ltfwObject.length(); i++) {
                                JSONObject objectJSONObject = ltfwObject.getJSONObject(i);
                                String serviceId = objectJSONObject.getString("serviceId");
                                String serviceName = objectJSONObject.getString("serviceName");
                                String serviceTypeId = objectJSONObject.getString("serviceTypeId");
                                String serviceTypeName = objectJSONObject.getString("serviceTypeName");
                                Left left = null;
                                if (i == 0){
                                    left = new Left(serviceName, true);
                                    left.setClassId(serviceId);
                                }else {
                                    left = new Left(serviceName, false);
                                    left.setClassId(serviceId);
                                }
                                ltfwClassList.add(left);
                            }
                        }

                        Log.e(TAG, "onSuccess: ++++++++++++++++++++");


                        if (fromType ==1 ){
                            if (goodsClassTypeId == 2){
                                for (int i = 0; i < qcbyClassList.size(); i++) {
                                    if (qcbyClassList.get(i).getClassId().equals(goodsClassId +"")){
                                        qcbyClassList.get(i).setCheck(true);
                                    }else {
                                        qcbyClassList.get(i).setCheck(false);
                                    }
                                }
                            }else if (goodsClassTypeId == 3){
                                for (int i = 0; i < mrqxClassList.size(); i++) {
                                    if (mrqxClassList.get(i).getClassId().equals(goodsClassId +"")){
                                        mrqxClassList.get(i).setCheck(true);
                                    }else {
                                        mrqxClassList.get(i).setCheck(false);
                                    }
                                }
                            }else if (goodsClassTypeId == 4){
                                for (int i = 0; i < azgzClassList.size(); i++) {
                                    if (azgzClassList.get(i).getClassId().equals(goodsClassId +"")){
                                        azgzClassList.get(i).setCheck(true);
                                    }else {
                                        azgzClassList.get(i).setCheck(false);
                                    }
                                }
                            }else if (goodsClassTypeId == 5){
                                for (int i = 0; i < ltfwClassList.size(); i++) {
                                    if (ltfwClassList.get(i).getClassId().equals(goodsClassId +"")){
                                        ltfwClassList.get(i).setCheck(true);
                                    }else {
                                        ltfwClassList.get(i).setCheck(false);
                                    }
                                }
                            }
                        }
                        initGoodsDataFromService();
                       // initClassData();
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
                hideDialogProgress(progressDialog);
               /* classLoadIsTrue = true;
                if (goodsLoadIsTrue){
                    hideDialogProgress(progressDialog);
                }*/
            }
        });
    }

    private void initClassData() {
        classItems.clear();
        if (currentBigType == 2){
            for (int i = 0; i < qcbyClassList.size(); i++) {
                classItems.add(qcbyClassList.get(i));
            }
        }else if (currentBigType == 3){
            for (int i = 0; i < mrqxClassList.size(); i++) {
                classItems.add(mrqxClassList.get(i));
            }
        }else if (currentBigType == 4){
            for (int i = 0; i < azgzClassList.size(); i++) {
                classItems.add(azgzClassList.get(i));
            }
        }else if (currentBigType == 5){
            for (int i = 0; i < ltfwClassList.size(); i++) {
                classItems.add(ltfwClassList.get(i));
            }
        }
        if (classItems.size() == 0){
            classItems.add(new Empty());
        }
        initGoodsData();
        assertAllRegistered(classAdapter, classItems);
        classAdapter.notifyDataSetChanged();

    }

    private void initGoodsData() {
        goodsItems.clear();
        Log.e(TAG, "initGoodsData: " + currentClassId);
        for (int i = 0; i < goodsNewList.size(); i++) {
            if (goodsNewList.get(i).getGoodsClassId() == currentClassId) {
                goodsItems.add(goodsNewList.get(i));
            }
        }
        if (goodsItems.size() == 0){
            goodsItems.add(new EmptyBig());
        }
        if (classItems.size() == 0){
            goodsItems.add(new EmptyBig());
        }

        assertAllRegistered(goodsAdapter, goodsItems);
        goodsAdapter.notifyDataSetChanged();
    }

    private void initView() {
        qcbyLayout = (FrameLayout) findViewById(R.id.qcby_layout);
        mrqxLayout = (FrameLayout) findViewById(R.id.mrqx_layout);
        azgzLayout = (FrameLayout) findViewById(R.id.azgz_layout);
        ltfwLayout = (FrameLayout) findViewById(R.id.ltfw_layout);
        qcbyView = (View) findViewById(R.id.qcby_view);
        mrqxView = (View) findViewById(R.id.mrqx_view);
        azgzView = (View) findViewById(R.id.azgz_view);
        ltfwView = (View) findViewById(R.id.ltfw_view);
        storeImageView = (ImageView) findViewById(R.id.store_image);
        storeNameText = (TextView) findViewById(R.id.store_name_text);
        tagFlowLayout = (TagFlowLayout) findViewById(R.id.store_tag_layout);
        shopCarLayout = (FrameLayout) findViewById(R.id.shop_car_layout);
        shopCarImage = (ImageView) findViewById(R.id.shop_car_image);
        shopCarContentLayout = (FrameLayout) findViewById(R.id.shop_car_content_layout);
        deleteCarShopLayout = (LinearLayout) findViewById(R.id.delete_car_shop_layout);
        goodsCountText = (TextView) findViewById(R.id.goods_count_text);
        goodsAllPriceText = (TextView) findViewById(R.id.all_price_text);
        qcbyCountText = (TextView) findViewById(R.id.qcby_count_text);
        mrqxCountText = (TextView) findViewById(R.id.mrqx_count_text);
        azgzCountText = (TextView) findViewById(R.id.azgz_count_text);
        ltfwCountText = (TextView) findViewById(R.id.ltfw_count_text);
        goodsBuyButton = (TextView) findViewById(R.id.goods_buy_button);
        shopLayout = (LinearLayout) findViewById(R.id.shop_item_layout);
        initBottomView();

        //服务小类listView
        classListView = (RecyclerView) findViewById(R.id.store_class_listview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        classListView.setLayoutManager(linearLayoutManager);
        classAdapter = new MultiTypeAdapter(classItems);
        classRegister();
        classListView.setAdapter(classAdapter);
        assertHasTheSameAdapter(classListView, classAdapter);

        //商品listView
        goodsListView = (RecyclerView) findViewById(R.id.store_goods_listview);
        LinearLayoutManager goodslinearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        goodsListView.setLayoutManager(goodslinearLayoutManager);
        goodsAdapter = new MultiTypeAdapter(goodsItems);
        goodsRegister();
        goodsListView.setAdapter(goodsAdapter);
        assertHasTheSameAdapter(goodsListView, goodsAdapter);

        //购物车的listView
        goodsCarListView = (RecyclerView) findViewById(R.id.goods_car_listview);
        LinearLayoutManager goodsCarlinearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        goodsCarListView.setLayoutManager(goodsCarlinearLayoutManager);
        goodsCarAdapter = new MultiTypeAdapter(goodsCarItems);
        goodsCarRegister();
        goodsCarListView.setAdapter(goodsCarAdapter);
        assertHasTheSameAdapter(goodsCarListView, goodsCarAdapter);




        //店铺点击
        RxViewAction.clickNoDouble(shopLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Intent intent = new Intent(getApplicationContext(), ShopHomeActivity.class);
                        intent.putExtra("STOREID",storeId);
                        startActivity(intent);
                    }
                });

        //商品购买
        RxViewAction.clickNoDouble(goodsBuyButton)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        currentGoodsList.clear();
                        for (int i = 0; i < goodsNewList.size(); i++) {
                            if (goodsNewList.get(i).getCurrentGoodsAmount() > 0) {
                                currentGoodsList.add(goodsNewList.get(i));
                            }
                        }

                        if (currentGoodsList.size() == 0){
                            Toast.makeText(ShopGoodsNewActivity.this, "请选择商品", Toast.LENGTH_SHORT).show();
                        }else {
                            Intent intent = new Intent(getApplicationContext(), OrderGoodsAffirmActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("GOODSLIST", (Serializable) currentGoodsList);
                            bundle.putDouble("ALLPRICE", goodsAllPrice);
                            bundle.putInt("STOREID", storeId);
                            bundle.putString("STORENAME", storeName);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                    }
                });

        //清除购物车
        RxViewAction.clickNoDouble(deleteCarShopLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        for (int i = 0; i < goodsNewList.size(); i++) {
                            goodsNewList.get(i).setCurrentGoodsAmount(0);
                        }
                        initCarData();

                        initBottomView();
                        initClassCount();
                        initClassData();
                        initBigClassCount();
                    }
                });
        //点击阴影处取消布局
        RxViewAction.clickNoDouble(shopCarLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (isCarShow){
                            isCarShow = false;
                        }else {
                            isCarShow = true;
                        }
                        shopCarLayout.setVisibility(isCarShow ? View.VISIBLE:View.GONE);
                    }
                });

        //点击非阴影处不取消布局
        RxViewAction.clickNoDouble(shopCarContentLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {

                    }
                });
        //购物车点击
        RxViewAction.clickNoDouble(shopCarImage)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (isCarShow){
                            isCarShow = false;
                        }else {
                            isCarShow = true;
                        }
                        initCarData();
                        shopCarLayout.setVisibility(isCarShow ? View.VISIBLE:View.GONE);

                    }
                });


        RxViewAction.clickNoDouble(qcbyLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        currentBigType = 2;
                        if (qcbyClassList.size() > 0){
                            for (int i = 0; i < qcbyClassList.size(); i++) {
                                if (qcbyClassList.get(i).isCheck) {
                                    currentClassId = Integer.parseInt(qcbyClassList.get(i).getClassId());
                                }
                            }
                        }else {
                            currentClassId = 0;
                        }

                        initBigClassView();
                        initClassData();


                    }
                });
        RxViewAction.clickNoDouble(mrqxLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        currentBigType = 3;
                        if (mrqxClassList.size() > 0){
                            for (int i = 0; i < mrqxClassList.size(); i++) {
                                if (mrqxClassList.get(i).isCheck) {
                                    currentClassId = Integer.parseInt(mrqxClassList.get(i).getClassId());
                                }
                            }
                        }else {
                            currentClassId = 0;
                        }

                        initBigClassView();
                        initClassData();
                    }
                });
        RxViewAction.clickNoDouble(azgzLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        currentBigType = 4;
                        if (azgzClassList.size() > 0){
                            for (int i = 0; i < azgzClassList.size(); i++) {
                                if (azgzClassList.get(i).isCheck) {
                                    currentClassId = Integer.parseInt(azgzClassList.get(i).getClassId());
                                }
                            }
                        }else {
                            currentClassId = 0;
                        }

                        initBigClassView();
                        initClassData();
                    }
                });
        RxViewAction.clickNoDouble(ltfwLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        currentBigType = 5;
                        if (ltfwClassList.size() > 0){
                            for (int i = 0; i < ltfwClassList.size(); i++) {
                                if (ltfwClassList.get(i).isCheck) {
                                    currentClassId = Integer.parseInt(ltfwClassList.get(i).getClassId());
                                }
                            }
                        }else {
                            currentClassId = 0 ;
                        }

                        initBigClassView();
                        initClassData();
                    }
                });

    }

    private void initShopInfoView() {


    }

    /**
     * 更新服务大类的数量
     */
    public void initBigClassCount(){

        int qcbyCount = 0;
        int mrqxCount = 0;
        int azgzCount = 0;
        int ltfwCount = 0;

        for (int i = 0; i < goodsNewList.size(); i++) {
            if (goodsNewList.get(i).getServiceTypeId() == 2) {
                qcbyCount =  qcbyCount + goodsNewList.get(i).getCurrentGoodsAmount();
            }else if (goodsNewList.get(i).getServiceTypeId() == 3){
                mrqxCount =  mrqxCount + goodsNewList.get(i).getCurrentGoodsAmount();
            }else if (goodsNewList.get(i).getServiceTypeId() == 4){
                azgzCount =  azgzCount + goodsNewList.get(i).getCurrentGoodsAmount();
            }else if (goodsNewList.get(i).getServiceTypeId() == 5){
                ltfwCount =  ltfwCount + goodsNewList.get(i).getCurrentGoodsAmount();
            }
        }

        if (qcbyCount > 0){
            qcbyCountText.setVisibility(View.VISIBLE);
            qcbyCountText.setText(qcbyCount+"");
        }else {
            qcbyCountText.setVisibility(View.GONE);
        }

        if (mrqxCount > 0){
            mrqxCountText.setVisibility(View.VISIBLE);
            mrqxCountText.setText(mrqxCount+"");
        }else {
            mrqxCountText.setVisibility(View.GONE);
        }

        if (azgzCount > 0){
            azgzCountText.setVisibility(View.VISIBLE);
            azgzCountText.setText(azgzCount+"");
        }else {
            azgzCountText.setVisibility(View.GONE);
        }


        if (ltfwCount > 0){
            ltfwCountText.setVisibility(View.VISIBLE);
            ltfwCountText.setText(ltfwCount+"");
        }else {
            ltfwCountText.setVisibility(View.GONE);
        }
    }

    /**
     * 更新选中物品的数量跟价格
     */
    private void initBottomView() {
        goodsAllCount = 0;
        goodsAllPrice = 0;
        for (int i = 0; i < goodsNewList.size(); i++) {
            if (goodsNewList.get(i).getCurrentGoodsAmount() > 0) {
                goodsAllCount = goodsAllCount  + goodsNewList.get(i).getCurrentGoodsAmount();
                double goodsPrice = Double.parseDouble(goodsNewList.get(i).getGoodsPrice());
                goodsAllPrice = goodsAllPrice + (goodsPrice * goodsNewList.get(i).getCurrentGoodsAmount());

            }
        }
        if (goodsAllCount > 0){
            goodsCountText.setText(goodsAllCount + "");
            goodsCountText.setVisibility(View.VISIBLE);
        }else {
            goodsCountText.setVisibility(View.GONE);
        }
        goodsAllPriceText.setText((double)Math.round(goodsAllPrice*100)/100  +"");

    }

    private void initCarData() {
       // goodsCarsList.clear();
        goodsCarItems.clear();
        for (int i = 0; i < goodsNewList.size(); i++) {
            if (goodsNewList.get(i).getCurrentGoodsAmount() > 0) {
                GoodsNew goodsNew = goodsNewList.get(i);
                goodsCarItems.add(new GoodsCar(goodsNew.getGoodsId(),goodsNew.getGoodsImage(),goodsNew.getGoodsName(),goodsNew.getGoodsPrice(),goodsNew.getGoodsAmount(),goodsNew.getCurrentGoodsAmount(),goodsNew.getGoodsClassId(),goodsNew.getServiceTypeId()));
            }
        }
        if (goodsCarItems.size() == 0){
            goodsCarItems.add(new Empty());
        }
        assertAllRegistered(goodsCarAdapter, goodsCarItems);
        goodsCarAdapter.notifyDataSetChanged();
    }

    private void goodsCarRegister() {
        GoodsCarViewBinder goodsCarViewBinder = new GoodsCarViewBinder(this);
        goodsCarViewBinder.setListener(this);
        goodsCarAdapter.register(GoodsCar.class, goodsCarViewBinder);
        goodsCarAdapter.register(Empty.class, new EmptyViewBinder());
        goodsCarAdapter.register(EmptyBig.class, new EmptyBigViewBinder());
    }

    private void goodsRegister() {
        GoodsNewViewBinder goodsNewViewBinder = new GoodsNewViewBinder(this);
        goodsNewViewBinder.setListener(this);
        goodsAdapter.register(GoodsNew.class, goodsNewViewBinder);
        goodsAdapter.register(Empty.class, new EmptyViewBinder());
        goodsAdapter.register(EmptyBig.class, new EmptyBigViewBinder());
    }

    private void classRegister() {
        LeftViewBinder leftViewBinder = new LeftViewBinder();
        leftViewBinder.setListener(this);
        classAdapter.register(Left.class, leftViewBinder);
        classAdapter.register(Empty.class, new EmptyViewBinder());
        classAdapter.register(EmptyBig.class, new EmptyBigViewBinder());
    }

    private void initBigClassView() {
        if (currentBigType == 2){
            qcbyView.setVisibility(View.VISIBLE);
            mrqxView.setVisibility(View.GONE);
            azgzView.setVisibility(View.GONE);
            ltfwView.setVisibility(View.GONE);
        }else if (currentBigType == 3){
            qcbyView.setVisibility(View.GONE);
            mrqxView.setVisibility(View.VISIBLE);
            azgzView.setVisibility(View.GONE);
            ltfwView.setVisibility(View.GONE);
        }else if (currentBigType == 4){
            qcbyView.setVisibility(View.GONE);
            mrqxView.setVisibility(View.GONE);
            azgzView.setVisibility(View.VISIBLE);
            ltfwView.setVisibility(View.GONE);
        }else if (currentBigType == 5){
            qcbyView.setVisibility(View.GONE);
            mrqxView.setVisibility(View.GONE);
            azgzView.setVisibility(View.GONE);
            ltfwView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 服务小类改变监听
     * @param className
     */
    @Override
    public void onLeftItemClikcListener(String className,String classId) {
        int serviceIdInt = Integer.parseInt(classId);
        currentClassId = serviceIdInt;
        if (currentBigType == 2){
            for (int i = 0; i < qcbyClassList.size(); i++) {
                if (qcbyClassList.get(i).getClassId().equals(classId)){
                    qcbyClassList.get(i).setCheck(true);
                }else {
                    qcbyClassList.get(i).setCheck(false);
                }
            }

        }else if(currentBigType == 3){
            for (int i = 0; i < mrqxClassList.size(); i++) {
                if (mrqxClassList.get(i).getClassId().equals(classId)){
                    mrqxClassList.get(i).setCheck(true);
                }else {
                    mrqxClassList.get(i).setCheck(false);
                }
            }
        }else if(currentBigType == 4){
            for (int i = 0; i < azgzClassList.size(); i++) {
                if (azgzClassList.get(i).getClassId().equals(classId)){
                    azgzClassList.get(i).setCheck(true);
                }else {
                    azgzClassList.get(i).setCheck(false);
                }
            }
        }else if(currentBigType == 5){
            for (int i = 0; i < ltfwClassList.size(); i++) {
                if (ltfwClassList.get(i).getClassId().equals(classId)){
                    ltfwClassList.get(i).setCheck(true);
                }else {
                    ltfwClassList.get(i).setCheck(false);
                }
            }
        }
        initClassData();

    }


    /**
     * 商品数量加减监听
     * @param goodsId
     * @param currentGoodsAmount
     */
    @Override
    public void onGoodsChangeClickListener(int goodsId, int currentGoodsAmount,int goodsClassId) {
        Log.e(TAG, "onGoodsChangeClickListener: " + currentGoodsAmount);
        Log.e(TAG, "onGoodsChangeClickListener: " + goodsId);
        for (int i = 0; i < goodsNewList.size(); i++) {
            if (goodsNewList.get(i).getGoodsId() == goodsId) {
                Log.e(TAG, "onGoodsChangeClickListener: ***");
                goodsNewList.get(i).setCurrentGoodsAmount(currentGoodsAmount);
            }
        }
        if (currentBigType == 2){
            for (int i = 0; i < qcbyClassList.size(); i++) {
                if (qcbyClassList.get(i).getClassId().equals(goodsClassId+"")) {  //小类id
                    int classAllAmount = 0;
                    for (int j = 0; j < goodsNewList.size(); j++) {
                        if (goodsNewList.get(j).getGoodsClassId() == goodsClassId) {
                            classAllAmount = classAllAmount + goodsNewList.get(j).getCurrentGoodsAmount();
                        }
                    }
                    qcbyClassList.get(i).setClassAmount(classAllAmount);
                }
            }
        }else if (currentBigType == 3){
            for (int i = 0; i < mrqxClassList.size(); i++) {
                if (mrqxClassList.get(i).getClassId().equals(goodsClassId+"")) {
                    int classAllAmount = 0;
                    for (int j = 0; j < goodsNewList.size(); j++) {
                        if (goodsNewList.get(j).getGoodsClassId() == goodsClassId) {
                            classAllAmount = classAllAmount + goodsNewList.get(j).getCurrentGoodsAmount();
                        }
                    }
                    mrqxClassList.get(i).setClassAmount(classAllAmount);
                }
            }
        }else if (currentBigType == 4){
            for (int i = 0; i < azgzClassList.size(); i++) {
                if (azgzClassList.get(i).getClassId().equals(goodsClassId+"")) {
                    int classAllAmount = 0;
                    for (int j = 0; j < goodsNewList.size(); j++) {
                        if (goodsNewList.get(j).getGoodsClassId() == goodsClassId) {
                            classAllAmount = classAllAmount + goodsNewList.get(j).getCurrentGoodsAmount();
                        }
                    }
                    azgzClassList.get(i).setClassAmount(classAllAmount);
                }
            }
        }else if (currentBigType == 5){
            for (int i = 0; i < ltfwClassList.size(); i++) {
                if (ltfwClassList.get(i).getClassId().equals(goodsClassId+"")) {
                    int classAllAmount = 0;
                    for (int j = 0; j < goodsNewList.size(); j++) {
                        if (goodsNewList.get(j).getGoodsClassId() == goodsClassId) {
                            classAllAmount = classAllAmount + goodsNewList.get(j).getCurrentGoodsAmount();
                        }
                    }
                    ltfwClassList.get(i).setClassAmount(classAllAmount);
                }
            }
        }
        initClassData();

       // initClassCount(goodsClassId);
        //initClassData();
        initBottomView();
        initBigClassCount();
    }


    /**
     * 购物车的商品数目改变点击回调
     * @param goodsId
     * @param currentGoodsAmount
     * @param classId
     */
    @Override
    public void onGoodsCarChangeClickListener(int goodsId, int currentGoodsAmount, int classId) {
        for (int i = 0; i < goodsNewList.size(); i++) {
            if (goodsNewList.get(i).getGoodsId() == goodsId) {
                goodsNewList.get(i).setCurrentGoodsAmount(currentGoodsAmount);
            }
        }



        initClassCount(classId);
        initClassData();
        initCarData();
        //initClassData();
        initBottomView();
        initBigClassCount();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isCarShow = false;
    }

    /**
     * 清除购物车  清除所有小类的数量
     */
    private void initClassCount() {

        for (int i = 0; i < qcbyClassList.size(); i++) {
            qcbyClassList.get(i).setClassAmount(0);
        }
        for (int i = 0; i < mrqxClassList.size(); i++) {
            mrqxClassList.get(i).setClassAmount(0);
        }
        for (int i = 0; i < azgzClassList.size(); i++) {
            azgzClassList.get(i).setClassAmount(0);
        }
        for (int i = 0; i < ltfwClassList.size(); i++) {
            ltfwClassList.get(i).setClassAmount(0);
        }
       /* //修改小类的数量
        if (currentBigType == 2){
            for (int i = 0; i < qcbyClassList.size(); i++) {
                qcbyClassList.get(i).setClassAmount(0);

            }
        }else if (currentBigType == 3){
            for (int i = 0; i < mrqxClassList.size(); i++) {
                mrqxClassList.get(i).setClassAmount(0);
            }
        }else if (currentBigType == 4){
            for (int i = 0; i < azgzClassList.size(); i++) {
                azgzClassList.get(i).setClassAmount(0);
            }
        }else if (currentBigType == 5){
            for (int i = 0; i < ltfwClassList.size(); i++) {
                ltfwClassList.get(i).setClassAmount(0);
            }
        }
        initClassData();*/
    }
    /**
     * 根据商品id修改class小类的数量
     * @param goodsClassId
     */
    private void initClassCount(int goodsClassId) {
        //修改小类的数量


        for (int i = 0; i < qcbyClassList.size(); i++) {
            if (qcbyClassList.get(i).getClassId().equals(goodsClassId+"")) {  //小类id
                int classAllAmount = 0;
                for (int j = 0; j < goodsNewList.size(); j++) {
                    if (goodsNewList.get(j).getGoodsClassId() == goodsClassId) {
                        classAllAmount = classAllAmount + goodsNewList.get(j).getCurrentGoodsAmount();
                    }
                }
                qcbyClassList.get(i).setClassAmount(classAllAmount);
            }
        }
        for (int i = 0; i < mrqxClassList.size(); i++) {
            if (mrqxClassList.get(i).getClassId().equals(goodsClassId+"")) {
                int classAllAmount = 0;
                for (int j = 0; j < goodsNewList.size(); j++) {
                    if (goodsNewList.get(j).getGoodsClassId() == goodsClassId) {
                        classAllAmount = classAllAmount + goodsNewList.get(j).getCurrentGoodsAmount();
                    }
                }
                mrqxClassList.get(i).setClassAmount(classAllAmount);
            }
        }

        for (int i = 0; i < azgzClassList.size(); i++) {
            if (azgzClassList.get(i).getClassId().equals(goodsClassId+"")) {
                int classAllAmount = 0;
                for (int j = 0; j < goodsNewList.size(); j++) {
                    if (goodsNewList.get(j).getGoodsClassId() == goodsClassId) {
                        classAllAmount = classAllAmount + goodsNewList.get(j).getCurrentGoodsAmount();
                    }
                }
                azgzClassList.get(i).setClassAmount(classAllAmount);
            }
        }

        for (int i = 0; i < ltfwClassList.size(); i++) {
            if (ltfwClassList.get(i).getClassId().equals(goodsClassId+"")) {
                int classAllAmount = 0;
                for (int j = 0; j < goodsNewList.size(); j++) {
                    if (goodsNewList.get(j).getGoodsClassId() == goodsClassId) {
                        classAllAmount = classAllAmount + goodsNewList.get(j).getCurrentGoodsAmount();
                    }
                }
                ltfwClassList.get(i).setClassAmount(classAllAmount);
            }
        }

    }

    @Override
    public void onBackPressed() {
        if (isCarShow){
            isCarShow = false;
            shopCarLayout.setVisibility(isCarShow ? View.VISIBLE:View.GONE);
        }else {
            super.onBackPressed();
        }
    }
}
