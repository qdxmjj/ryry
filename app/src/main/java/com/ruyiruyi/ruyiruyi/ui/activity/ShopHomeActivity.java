package com.ruyiruyi.ruyiruyi.ui.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.ruyiruyi.ruyiruyi.R;
import com.ruyiruyi.ruyiruyi.db.DbConfig;
import com.ruyiruyi.ruyiruyi.db.model.Location;
import com.ruyiruyi.ruyiruyi.ui.model.ServiceType;
import com.ruyiruyi.ruyiruyi.ui.multiType.Empty;
import com.ruyiruyi.ruyiruyi.ui.multiType.EmptyViewBinder;
import com.ruyiruyi.ruyiruyi.ui.multiType.EvaImageViewBinder;
import com.ruyiruyi.ruyiruyi.ui.multiType.ShopGoods;
import com.ruyiruyi.ruyiruyi.ui.multiType.ShopGoodsViewBinder;
import com.ruyiruyi.ruyiruyi.ui.multiType.ShopInfo;
import com.ruyiruyi.ruyiruyi.ui.multiType.ShopInfoViewBinder;
import com.ruyiruyi.ruyiruyi.ui.multiType.ShopStr;
import com.ruyiruyi.ruyiruyi.ui.multiType.ShopStrViewBinder;
import com.ruyiruyi.ruyiruyi.ui.multiType.UserEvaluate;
import com.ruyiruyi.ruyiruyi.ui.multiType.UserEvaluateViewBinder;
import com.ruyiruyi.ruyiruyi.utils.ImagPagerUtil;
import com.ruyiruyi.ruyiruyi.utils.RequestUtils;
import com.ruyiruyi.ruyiruyi.utils.UtilsRY;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;
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

import me.drakeet.multitype.MultiTypeAdapter;
import rx.functions.Action1;

import static me.drakeet.multitype.MultiTypeAsserts.assertAllRegistered;
import static me.drakeet.multitype.MultiTypeAsserts.assertHasTheSameAdapter;

public class ShopHomeActivity extends BaseActivity implements EvaImageViewBinder.OnEvaluateImageClick, ShopStrViewBinder.OnAllEvaluateClick {
    private static final String TAG = ShopHomeActivity.class.getSimpleName();
    private ActionBar actionBar;
    private List<Object> items = new ArrayList<>();
    private MultiTypeAdapter adapter;
    private RecyclerView listView;
    public List<ServiceType> typeList;
    private TextView goBuyGoodsButton;
    public List<UserEvaluate> userEvaluateList;
    private int storeid;
    private double jingdu;
    private double weidu;
    public String currentCity = "选择城市";
    public List<ServiceType> serviceTypeList;
    public List<String> imageList;
    private String storeConnet;
    private int commitId;
    private String storeCommitTime;
    private String storeCommitUserHeadImg;
    private String storeCommitUserName;
    private String storeSituation;
    private String distance;
    private String storeAddress;
    private String storeTypeColor;
    private String storeType;
    private String storeName;
    private String storePhone;
    private String store_latitude;
    private String store_longitude;
    private String user_latitude;
    private String user_longitude;
    private LocationClient mLocationClient;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    initDataFromService();
                    // initdata();
                    //配置点击查看大图
                    initImageLoader();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_home, R.id.my_action);
        actionBar = (ActionBar) findViewById(R.id.my_action);
        actionBar.setTitle("门店首页");
        ;
        actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(int var1) {
                switch ((var1)) {
                    case -1:
                        onBackPressed();
                        break;
                }
            }
        });
        serviceTypeList = new ArrayList<>();
        imageList = new ArrayList<>();
        //获取位置
        Location location = new DbConfig().getLocation();
        if (location != null) {
            currentCity = location.getCity();
            jingdu = location.getJingdu();
            weidu = location.getWeidu();
        }
        Intent intent = getIntent();
        storeid = intent.getIntExtra("STOREID", 0);
        userEvaluateList = new ArrayList<>();
        initView();
        initUserLocation();
        //移至handler中

    }

    private void initUserLocation() {
        //获取用户实时定位经纬度  user_latitude&user_longitude
        mLocationClient = new LocationClient(this);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);                                //打开gps
        option.setCoorType("bd09ll");                           //设置坐标类型为bd09ll 百度需要的坐标，也可以返回其他type类型
        option.setPriority(LocationClientOption.NetWorkFirst);  //设置网络优先
//        option.setScanSpan(50000);                               //定时定位，每隔5秒钟定位一次。这个就看大家的需求了

        mLocationClient.setLocOption(option);
        mLocationClient.start();//这句代码百度api上给的没有，没有这个代码下面的回调方法不会执行的

        mLocationClient.registerLocationListener(new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                //  这里可以获取经纬度，这是回调方法
                user_latitude = bdLocation.getLatitude() + "";
                user_longitude = bdLocation.getLongitude() + "";
                Message message = new Message();
                message.what = 1;
                mHandler.sendMessage(message);
            }
        });
    }

    private void initDataFromService() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("storeId", storeid);
            jsonObject.put("longitude", jingdu);
            jsonObject.put("latitude", weidu);
        } catch (JSONException e) {
        }
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "getStoreInfoByStoreId");
        params.addBodyParameter("reqJson", jsonObject.toString());
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
                    if (status.equals("1")) {
                        Log.e(TAG, "onSuccess: 1");
                        JSONObject data = jsonObject1.getJSONObject("data");
                        String factoryImgUrl = data.getString("factoryImgUrl");
                        String indoorImgUrl = data.getString("indoorImgUrl");
                        String locationImgUrl = data.getString("locationImgUrl");
                        store_latitude = data.getString("latitude");
                        store_longitude = data.getString("longitude");
                        imageList.add(factoryImgUrl);
                        imageList.add(indoorImgUrl);
                        imageList.add(locationImgUrl);
                        Log.e(TAG, "onSuccess: 1");
                        storeName = data.getString("storeName");
                        storeType = data.getString("storeType");
                        Log.e(TAG, "onSuccess: 2");
                        storeTypeColor = data.getString("storeTypeColor");
                        storeAddress = data.getString("storeAddress");
                        distance = data.getString("distance");
                        Log.e(TAG, "onSuccess: 3");
                        storePhone = data.getString("storePhone");
                        storeSituation = data.getString("storeSituation");
                        Log.e(TAG, "onSuccess: 4");
                        serviceTypeList.clear();
                        JSONArray storeServcieList = data.getJSONArray("storeServcieList");
                        for (int i = 0; i < storeServcieList.length(); i++) {
                            JSONObject object = storeServcieList.getJSONObject(i);
                            JSONObject service = object.getJSONObject("service");
                            String name = service.getString("name");
                            String color = service.getString("color");
                            serviceTypeList.add(new ServiceType(name, color));
                        }
                        Log.e(TAG, "onSuccess: 5");
                        //获取评论
                        JSONObject commit = null;
                        try {
                            commit = data.getJSONObject("store_first_commit");
                        } catch (JSONException e) {

                        }
                        Log.e(TAG, "onSuccess: " + commit);
                        userEvaluateList.clear();
                        if (commit != null) {
                            commitId = commit.getInt("id");
                            storeCommitUserName = commit.getString("storeCommitUserName");
                            storeCommitUserHeadImg = commit.getString("storeCommitUserHeadImg");
                            long time = commit.getLong("time");
                            storeCommitTime = new UtilsRY().getTimestampToString(time);
                            storeConnet = commit.getString("content");
                            List<String> imageList = new ArrayList<String>();
                            imageList.add(commit.getString("img1Url"));
                            imageList.add(commit.getString("img2Url"));
                            imageList.add(commit.getString("img3Url"));
                            imageList.add(commit.getString("img4Url"));
                            imageList.add(commit.getString("img5Url"));
                            userEvaluateList.add(new UserEvaluate(commitId, storeCommitUserHeadImg, storeCommitUserName, storeCommitTime, storeConnet, imageList));

                        }
                        initdata();

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

    private void initImageLoader() {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                getApplicationContext()).threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .writeDebugLogs() // Remove for release app
                .build();
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);
    }

    private void initdata() {

        /*typeList = new ArrayList<>();
        typeList.add(new ServiceType("汽车保养","#ff3f7e"));
        typeList.add(new ServiceType("美容清洗","#00a600"));
        typeList.add(new ServiceType("安装","#007cf0"));
        typeList.add(new ServiceType("轮胎","#b600ea"));
        List<String> imageList = new ArrayList<>();

        imageList.add("http://192.168.0.167/images/user/carImage/70/zhuye.png");
        imageList.add("http://180.76.243.205:8111/images/Advertisement/cxwy.png");
        imageList.add("http://192.168.0.167/images/user/carImage/70/fuye.png");
        imageList.add("http://180.76.243.205:8111/images/Advertisement/cxwy1000.png");


        for (int i = 0; i < 5; i++) {

            userEvaluateList.add(new UserEvaluate(i,"http://180.76.243.205:8111/images/Advertisement/cxwy1000.png","一只大鸟"+i,"2017-02-1" + i,
                    "有一只大鸟从天上掉了下来",imageList));
        }*/
        items.clear();
        items.add(new ShopInfo(storeName, store_latitude, store_longitude, user_latitude, user_longitude, storeType, storeTypeColor, storeAddress, distance, storePhone, storeConnet, imageList, serviceTypeList));
        items.add(new ShopStr("门店评价"));
        if (userEvaluateList.size() > 0) {
            for (int i = 0; i < userEvaluateList.size(); i++) {
                items.add(userEvaluateList.get(i));
            }
        } else {
            items.add(new Empty());
        }
        assertAllRegistered(adapter, items);
        adapter.notifyDataSetChanged();
    }

    private void initView() {
        listView = (RecyclerView) findViewById(R.id.shop_home_listview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        listView.setLayoutManager(linearLayoutManager);
        adapter = new MultiTypeAdapter(items);
        register();

        listView.setAdapter(adapter);
        assertHasTheSameAdapter(listView, adapter);

        goBuyGoodsButton = (TextView) findViewById(R.id.go_bug_good_button);
        RxViewAction.clickNoDouble(goBuyGoodsButton)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Intent intent = new Intent(getApplicationContext(), ShopGoodActivity.class);
                        intent.putExtra("STOREID", storeid);
                        startActivity(intent);
                    }
                });

    }

    private void register() {
        adapter.register(ShopInfo.class, new ShopInfoViewBinder(this));
        ShopStrViewBinder shopStrViewBinder = new ShopStrViewBinder();
        shopStrViewBinder.setListener(this);
        adapter.register(ShopStr.class, shopStrViewBinder);
        UserEvaluateViewBinder userEvaluateViewBinder = new UserEvaluateViewBinder(this);
        userEvaluateViewBinder.setListener(this);
        adapter.register(UserEvaluate.class, userEvaluateViewBinder);
        adapter.register(Empty.class, new EmptyViewBinder());
        // ShopGoodsViewBinder shopGoodsViewBinder = new ShopGoodsViewBinder(this,getSupportFragmentManager());
        //adapter.register(ShopGoods.class, shopGoodsViewBinder);

    }

    @Override
    public void onEvaluateImageClickListener(String url, int evaluateId) {
        Log.e(TAG, "onEvaluateImageClickListener: 1111");
        ArrayList<String> picList = new ArrayList<>();
        picList.add(url);
        String content = "";
        for (int i = 0; i < userEvaluateList.size(); i++) {
            if (userEvaluateList.get(i).getEvaluateId() == evaluateId) {  //根据id找到该条评论
                List<String> evaluateImageList = userEvaluateList.get(i).getEvaluateImageList();
                content = userEvaluateList.get(i).getEvaluateContent();
                for (int j = 0; j < evaluateImageList.size(); j++) {//去除点击的图片
                    if (!evaluateImageList.get(j).equals(url)) {
                        picList.add(evaluateImageList.get(j));
                    }
                }
            }
        }
        ImagPagerUtil imagPagerUtil = new ImagPagerUtil(ShopHomeActivity.this, picList);
        imagPagerUtil.setContentText(content);
        imagPagerUtil.show();

    }

    @Override
    public void onAllEvaluate() {
        startActivity(new Intent(this, ShopEvaluateActivity.class));
    }
}
