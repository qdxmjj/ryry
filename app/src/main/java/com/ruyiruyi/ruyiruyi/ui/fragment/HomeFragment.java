package com.ruyiruyi.ruyiruyi.ui.fragment;

import android.content.Context;
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
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ruyiruyi.ruyiruyi.MainActivity;
import com.ruyiruyi.ruyiruyi.R;
import com.ruyiruyi.ruyiruyi.db.DbConfig;
import com.ruyiruyi.ruyiruyi.db.model.Location;
import com.ruyiruyi.ruyiruyi.db.model.Lunbo;
import com.ruyiruyi.ruyiruyi.db.model.User;
import com.ruyiruyi.ruyiruyi.ui.activity.BottomEventActivity;
import com.ruyiruyi.ruyiruyi.ui.activity.CarFigureActivity;
import com.ruyiruyi.ruyiruyi.ui.activity.CarInfoActivity;
import com.ruyiruyi.ruyiruyi.ui.activity.CarManagerActivity;
import com.ruyiruyi.ruyiruyi.ui.activity.CityChooseActivity;
import com.ruyiruyi.ruyiruyi.ui.activity.CxwyActivity;
import com.ruyiruyi.ruyiruyi.ui.activity.LoginActivity;
import com.ruyiruyi.ruyiruyi.ui.activity.LunboContentActivity;
import com.ruyiruyi.ruyiruyi.ui.activity.ShopChooseActivity;
import com.ruyiruyi.ruyiruyi.ui.activity.TireChangeActivity;
import com.ruyiruyi.ruyiruyi.ui.activity.TireFreeChangeActivity;
import com.ruyiruyi.ruyiruyi.ui.activity.TirePlaceActivity;
import com.ruyiruyi.ruyiruyi.ui.activity.TireRepairActivity;
import com.ruyiruyi.ruyiruyi.ui.activity.TireWaitChangeActivity;
import com.ruyiruyi.ruyiruyi.ui.activity.YearChooseActivity;
import com.ruyiruyi.ruyiruyi.ui.fragment.base.RyBaseFragment;
import com.ruyiruyi.ruyiruyi.ui.multiType.Function;
import com.ruyiruyi.ruyiruyi.ui.multiType.FunctionViewBinder;
import com.ruyiruyi.ruyiruyi.ui.multiType.Hometop;
import com.ruyiruyi.ruyiruyi.ui.multiType.HometopViewBinder;
import com.ruyiruyi.ruyiruyi.ui.multiType.OneEvent;
import com.ruyiruyi.ruyiruyi.ui.multiType.OneEventViewBinder;
import com.ruyiruyi.ruyiruyi.ui.multiType.ThreeEvent;
import com.ruyiruyi.ruyiruyi.ui.multiType.ThreeEventViewBinder;
import com.ruyiruyi.ruyiruyi.ui.service.LocationService;
import com.ruyiruyi.ruyiruyi.utils.RequestUtils;
import com.ruyiruyi.rylibrary.ui.viewpager.CustomBanner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.DbManager;
import org.xutils.common.Callback;
import org.xutils.ex.DbException;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import me.drakeet.multitype.MultiTypeAdapter;

import static me.drakeet.multitype.MultiTypeAsserts.assertAllRegistered;
import static me.drakeet.multitype.MultiTypeAsserts.assertHasTheSameAdapter;

public class HomeFragment extends RyBaseFragment implements HometopViewBinder.OnHomeTopItemClickListener, FunctionViewBinder.OnFunctionItemClick
        , ThreeEventViewBinder.OnEventItemClickListener, OneEventViewBinder.OnEventClick {

    private static final String TAG = HomeFragment.class.getSimpleName();
    private CustomBanner<String> mBanner;
    private ImageView imageView;
    private RecyclerView listView;
    private List<Object> items = new ArrayList<>();
    private MultiTypeAdapter adapter;
    private boolean tireSame;
    private String rearSize;
    private String fontSize;
    private String carName;
    private int carId;
    private String carImage;
    private List<Lunbo> lunbos;
    private SwipeRefreshLayout refreshLayout;
    public final static int CITY_CHOOSE = 1;
    public String currentCity = "选择城市";
    private double jingdu;
    private double weidu;
    private LocationService locationService;
    private int fromFragment;
    private int ischoos;
    public OnIconClikc listener;
    private String service_year;
    private String service_end_date;
    private List<OneEvent> activitys;

    private int uesrCarId;


    public void setListener(OnIconClikc listener) {
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_home, container, false);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        listView = (RecyclerView) getView().findViewById(R.id.home_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        listView.setLayoutManager(linearLayoutManager);
        adapter = new MultiTypeAdapter(items);
        Bundle bundle = getArguments();
        currentCity = bundle.getString("city");
        ischoos = bundle.getInt("ischoos", 0); //1是选择返回


        register();

        listView.setAdapter(adapter);
        assertHasTheSameAdapter(listView, adapter);
        lunbos = new ArrayList<>();
        activitys = new ArrayList<>();
        refreshLayout = ((SwipeRefreshLayout) getView().findViewById(R.id.home_refresh));
        refreshLayout.setProgressViewEndTarget(true, 200);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initdataFromService();
                refreshLayout.setRefreshing(false);
            }
        });
        // initdata();


        //获取位置
        Location location = new DbConfig(getContext()).getLocation();
        if (location != null) {
            currentCity = location.getCity();
            jingdu = location.getJingdu();
            weidu = location.getWeidu();
            Log.e(TAG, "onActivityCreated: --" + currentCity);
            Log.e(TAG, "onActivityCreated: --" + jingdu);
            Log.e(TAG, "onActivityCreated: --" + weidu);
        }

        initdataFromService();
    }


    private void initdataFromService() {
        DbConfig dbConfig = new DbConfig(getContext());
        int id = dbConfig.getId();

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userId", id);
        } catch (JSONException e) {
        }
        lunbos.clear();
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "getAndroidHomeDate");
        params.addBodyParameter("reqJson", jsonObject.toString());
        Log.e(TAG, "initdataFromService: --" + jsonObject.toString());
        String token = new DbConfig(getContext()).getToken();
        params.addParameter("token", token);
        Log.e(TAG, "initdataFromService: -----------------" + params.toString());
        x.http().post(params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess: --------------" + result);
                try {
                    JSONObject jsonObject1 = new JSONObject(result);
                    String status = jsonObject1.getString("status");
                    String msg = jsonObject1.getString("msg");
                    if (status.equals("1")) {
                        JSONObject data = jsonObject1.getJSONObject("data");
                        //获取底部活动列表
                        JSONArray jsActivityList = data.getJSONArray("activityList");
                        for (int i = 0; i < jsActivityList.length(); i++) {
                            JSONObject objBean = (JSONObject) jsActivityList.get(i);
                            String imageUrl = objBean.getString("imageUrl");
                            String webUrl = objBean.getString("webUrl");
                            OneEvent bean = new OneEvent(imageUrl, webUrl);
                            activitys.add(bean);
                        }

                        //获取轮播数据
                        JSONArray lunboList = data.getJSONArray("lunbo_infos");
                        for (int i = 0; i < lunboList.length(); i++) {
                            JSONObject object = lunboList.getJSONObject(i);
                            String contentImageUrl = object.getString("contentImageUrl");
                            String contentText = object.getString("contentText");
                            int id1 = object.getInt("id");
                            Lunbo lunbo = new Lunbo(id1, contentImageUrl, contentText);
                            lunbos.add(lunbo);
                        }

                        //获取车辆数据
                        try {
                            JSONObject carObject = data.getJSONObject("androidHomeData_cars");
                            if (carObject != null) {
                                carImage = carObject.getString("car_brand_url");
                                carName = carObject.getString("car_verhicle");
                                fontSize = carObject.getString("font");
                                rearSize = carObject.getString("rear");
                                tireSame = carObject.getBoolean("same");
                                carId = carObject.getInt("car_id");
                                service_end_date = carObject.getString("service_end_date");
                                service_year = carObject.getString("service_year");
                                uesrCarId = carObject.getInt("user_car_id");
                                User user = new DbConfig(getContext()).getUser();
                                user.setCarId(uesrCarId);
                                saveUserIntoDb(user);
                            } else {
                                int uesrCarId = 0;
                                User user = new DbConfig(getContext()).getUser();
                                user.setCarId(uesrCarId);
                                saveUserIntoDb(user);
                            }
                        } catch (JSONException e) {
                           /* int uesrCarId = 0;
                            User user = new DbConfig().getUser();
                            user.setCarId(uesrCarId);
                            saveUserIntoDb(user);*/
                        }


                        saveLunboInToDb();

                        initdata();
                    } else if (status.equals("-999")) {
                        showUserTokenDialog("您的账号在其它设备登录,请重新登录");
                    }


                } catch (JSONException e) {

                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e(TAG, "onError: ");
            }

            @Override
            public void onCancelled(CancelledException cex) {
                Log.e(TAG, "onCancelled: ");
            }

            @Override
            public void onFinished() {
                Log.e(TAG, "onFinished:");
            }
        });
    }

    private void saveUserIntoDb(User user) {
        DbConfig dbConfig = new DbConfig(getContext());
        DbManager db = dbConfig.getDbManager();
        try {
            db.saveOrUpdate(user);
        } catch (DbException e) {

        }
    }

    private void saveLunboInToDb() {
        DbConfig dbConfig = new DbConfig(getContext());
        DbManager db = dbConfig.getDbManager();
        try {
            db.saveOrUpdate(lunbos);
        } catch (DbException e) {

        }

    }


    private void register() {
        HometopViewBinder hometopViewBinder = new HometopViewBinder(getContext());
        hometopViewBinder.setListener(this);
        adapter.register(Hometop.class, hometopViewBinder);

        FunctionViewBinder functionViewBinder = new FunctionViewBinder();
        functionViewBinder.setListener(this);
        adapter.register(Function.class, functionViewBinder);

        ThreeEventViewBinder threeEventViewBinder = new ThreeEventViewBinder();
        threeEventViewBinder.setListener(this);
        adapter.register(ThreeEvent.class, threeEventViewBinder);
        OneEventViewBinder oneEventViewBinder = new OneEventViewBinder(getContext());
        oneEventViewBinder.setListener(this);
        adapter.register(OneEvent.class, oneEventViewBinder);
    }

    private void initdata() {
        DbConfig dbConfig = new DbConfig(getContext());
        User user = new User();
        user = dbConfig.getUser();
        if (user != null) {
            int carId = user.getCarId();
        }


        List<Lunbo> lunboList = new ArrayList<>();
        lunboList = dbConfig.getLunbo();


        items.clear();
        ArrayList<String> images = new ArrayList<>();
        for (int i = 0; i < lunboList.size(); i++) {
            images.add(lunboList.get(i).getContentImageUrl());
        }
        if (!(user == null)) {
            //int firstAddCar1 = user.getFirstAddCar();
            if (carId == 0) {
                items.add(new Hometop(images, "添加我的宝驹", "邀请好友绑定车辆可免费洗车", 1, currentCity));
            } else {
                Hometop carInfo = new Hometop(images, carName, "一次性购买四条轮胎送洗车券", 2, currentCity);
                carInfo.setCarImage(carImage);
                items.add(carInfo);
            }
        } else {//未登陆
            items.add(new Hometop(images, "新人注册享好礼", "注册享受价格1000元大礼包", 0, currentCity));
        }


        items.add(new Function());
        items.add(new ThreeEvent());
        items.add(activitys.get(0));//TODO 活动列表暂1条数据
        assertAllRegistered(adapter, items);
        adapter.notifyDataSetChanged();
    }

    public void setBean(ArrayList beans) {
        mBanner.setPages(new CustomBanner.ViewCreator<String>() {
            @Override
            public View createView(Context context, int position) {
                ImageView imageView = new ImageView(context);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                return imageView;
            }

            @Override
            public void updateUI(Context context, View view, int position, String entity) {
                Glide.with(context).load(entity).into((ImageView) view);
            }
        }, beans)
//                //设置指示器为普通指示器
//                .setIndicatorStyle(CustomBanner.IndicatorStyle.ORDINARY)
//                //设置两个点图片作为翻页指示器，不设置则没有指示器，可以根据自己需求自行配合自己的指示器,不需要圆点指示器可用不设
//                .setIndicatorRes(R.drawable.shape_point_select, R.drawable.shape_point_unselect)
//                //设置指示器的方向
//                .setIndicatorGravity(CustomBanner.IndicatorGravity.CENTER)
//                //设置指示器的指示点间隔
//                .setIndicatorInterval(20)
                //设置自动翻页
                .startTurning(5000);
    }

    /**
     * 城市选择
     */
    @Override
    public void onCityLayoutClickListener() {
        Intent intent = new Intent(getContext(), CityChooseActivity.class);
        startActivityForResult(intent, CITY_CHOOSE);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == CITY_CHOOSE) {
            String city = data.getStringExtra("CITY");
            currentCity = city;
            initdata();
            Log.e(TAG, "onActivityResult:-----------*----------- " + city);
        }
    }

    /**
     * 车辆item的点击事件
     *
     * @param state
     */
    @Override
    public void onCarItemClickListener(int state) {
        if (state == 0) {        //未登陆
            startActivity(new Intent(getContext(), LoginActivity.class));
        } else if (state == 1) {  //未添加车辆
            Intent intent = new Intent(getContext(), CarInfoActivity.class);
            intent.putExtra("CANCLICK", 0);
            intent.putExtra("FROM", 3);
            startActivity(intent);
        } else {         //已添加车辆
            Intent intent = new Intent(getContext(), CarManagerActivity.class);
            intent.putExtra("FRAGMENT", "HOMEFRAGMENT");
            startActivityForResult(intent, MainActivity.HOMEFRAGMENT_RESULT);
            getActivity().finish();
        }
    }

    @Override
    public void onLunboClikcListener(int position) {
        Log.e(TAG, "onLunboClikcListener: " + position);

        if (position == 2) { //跳转到轮胎购买界面
            //判断是否登录（未登录提示登录）
            if (!judgeIsLogin()) {
                return;
            }
            if (carId == 0) {
                Toast.makeText(getContext(), "您还未添加车辆，请添加默认车辆", Toast.LENGTH_SHORT).show();
                return;
            }
            if (tireSame) {  //前后轮一样
                if (service_end_date.equals("")) {
                    Intent intent = new Intent(getContext(), YearChooseActivity.class);
                    intent.putExtra("TIRESIZE", fontSize);
                    intent.putExtra("FONTREARFLAG", "0");
                    intent.putExtra("SERVICEYEAR", service_year);
                    intent.putExtra("CARID", carId);
                    intent.putExtra("USERCARID", uesrCarId);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getContext(), CarFigureActivity.class);
                    intent.putExtra("TIRESIZE", fontSize);
                    intent.putExtra("FONTREARFLAG", "0");
                    intent.putExtra("SERVICEYEAR", service_year);
                    intent.putExtra("CARID", carId);
                    intent.putExtra("USERCARID", uesrCarId);
                    startActivity(intent);
                }


            } else {         //前后轮不一样
                Intent intent = new Intent(getContext(), TirePlaceActivity.class);
                intent.putExtra("FONTSIZE", fontSize);
                intent.putExtra("REARSIZE", rearSize);
                intent.putExtra("SERVICEYEAR", service_year);
                intent.putExtra("SERVICE_END_YEAR", service_end_date);
                intent.putExtra("CARID", carId);
                intent.putExtra("USERCARID", uesrCarId);
                startActivity(intent);
            }
        } else {
            //判断是否登录（未登录提示登录）
            if (!judgeIsLogin()) {
                return;
            }
            if (carId == 0) {
                Toast.makeText(getContext(), "您还未添加车辆，请添加默认车辆", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent intent = new Intent(getContext(), LunboContentActivity.class);
            if (tireSame) {
                intent.putExtra("FONTREARFLAG", 0);
            } else {
                intent.putExtra("FONTREARFLAG", 1);
            }
            intent.putExtra("FONTSIZE", fontSize);
            intent.putExtra("REARSIZE", rearSize);

            intent.putExtra("SERVICEYEAR", service_year);
            intent.putExtra("SERVICE_END_YEAR", service_end_date);
            intent.putExtra("CARID", carId);
            intent.putExtra("USERCARID", uesrCarId);
            intent.putExtra(LunboContentActivity.LUNBO_POSITION, position);
            startActivity(intent);
        }


    }

    @Override
    public void onFunctionClickListener(int type) {
        if (type == 0) {//轮胎购买
            //判断是否登录（未登录提示登录）
            if (!judgeIsLogin()) {
                return;
            }
            if (carId == 0) {
                Toast.makeText(getContext(), "您还未添加车辆，请添加默认车辆", Toast.LENGTH_SHORT).show();
                return;
            }
            if (tireSame) {  //前后轮一样
                if (service_end_date.equals("")) {
                    Intent intent = new Intent(getContext(), YearChooseActivity.class);
                    intent.putExtra("TIRESIZE", fontSize);
                    intent.putExtra("FONTREARFLAG", "0");
                    intent.putExtra("SERVICEYEAR", service_year);
                    intent.putExtra("CARID", carId);
                    intent.putExtra("USERCARID", uesrCarId);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getContext(), CarFigureActivity.class);
                    intent.putExtra("TIRESIZE", fontSize);
                    intent.putExtra("FONTREARFLAG", "0");
                    intent.putExtra("SERVICEYEAR", service_year);
                    intent.putExtra("CARID", carId);
                    intent.putExtra("USERCARID", uesrCarId);
                    startActivity(intent);
                }
            } else {         //前后轮不一样
                Intent intent = new Intent(getContext(), TirePlaceActivity.class);
                intent.putExtra("FONTSIZE", fontSize);
                intent.putExtra("REARSIZE", rearSize);
                intent.putExtra("SERVICEYEAR", service_year);
                intent.putExtra("SERVICE_END_YEAR", service_end_date);
                intent.putExtra("CARID", carId);
                intent.putExtra("USERCARID", uesrCarId);
                startActivity(intent);
            }

        } else if (type == 1) {//免费更换
            //判断是否登录（未登录提示登录）
            if (!judgeIsLogin()) {
                return;
            }
            if (carId == 0) {
                Toast.makeText(getContext(), "您还未添加车辆，请添加默认车辆", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent intent = new Intent(getContext(), TireFreeChangeActivity.class);
            intent.putExtra(TireChangeActivity.CHANGE_TIRE, 1);
            startActivity(intent);
        } else if (type == 2) {//轮胎修补
            //判断是否登录（未登录提示登录）   `
            if (!judgeIsLogin()) {
                return;
            }
            if (carId == 0) {
                Toast.makeText(getContext(), "您还未添加车辆，请添加默认车辆", Toast.LENGTH_SHORT).show();
                return;
            }
            startActivity(new Intent(getContext(), TireRepairActivity.class));
        } else if (type == 3) { //待更换轮胎
            // listener.onShopClassClickListener();
            //判断是否登录（未登录提示登录）
            if (!judgeIsLogin()) {
                return;
            }
            Intent intent = new Intent(getContext(), TireWaitChangeActivity.class);
            intent.putExtra(MyFragment.FROM_FRAGMENT, "HOMEFRAGMENT");
            startActivity(intent);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onEventClickListener(String tag) {
        if (tag.equals("cxwy")) {//（畅行无忧）
            //判断是否登录（未登录提示登录）
            if (!judgeIsLogin()) {
                return;
            }
            if (carId == 0) {
                Toast.makeText(getContext(), "您还未添加车辆，请添加默认车辆", Toast.LENGTH_SHORT).show();
                return;
            }
            startActivity(new Intent(getContext(), CxwyActivity.class));
        } else if (tag.equals("qcby")) {//（汽车保养）//3  //门店服务类型 2:汽车保养  3:美容清洗  4:改装  5:轮胎服务
            //判断是否登录（未登录提示登录）
            if (!judgeIsLogin()) {
                return;
            }
            Intent intent = new Intent(getContext(), ShopChooseActivity.class);
            intent.putExtra(MerchantFragment.SHOP_TYPE, 2);
            startActivity(intent);
        } else if (tag.equals("mrqx")) {//（美容清洗）//2
            //判断是否登录（未登录提示登录）
            if (!judgeIsLogin()) {
                return;
            }
            Intent intent = new Intent(getContext(), ShopChooseActivity.class);
            intent.putExtra(MerchantFragment.SHOP_TYPE, 3);
            startActivity(intent);
        }
    }

    /*
    * 底部活动点击事件回调
    * */
    @Override
    public void onOneEventClickListener(String webUrl) {
        //跳转活动页面
        Intent intent = new Intent(getContext(), BottomEventActivity.class);
        intent.putExtra("webUrl", webUrl);
        startActivity(intent);
    }

    public interface OnIconClikc {
        void onShopClassClickListener();
    }
}