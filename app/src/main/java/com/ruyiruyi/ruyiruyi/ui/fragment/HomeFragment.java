package com.ruyiruyi.ruyiruyi.ui.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
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
import com.ruyiruyi.ruyiruyi.ui.activity.CarInfoActivity;
import com.ruyiruyi.ruyiruyi.ui.activity.CarManagerActivity;
import com.ruyiruyi.ruyiruyi.ui.activity.CityChooseActivity;
import com.ruyiruyi.ruyiruyi.ui.activity.GoodsShopActivity;
import com.ruyiruyi.ruyiruyi.ui.activity.LoginActivity;
import com.ruyiruyi.ruyiruyi.ui.activity.LunboContentActivity;
import com.ruyiruyi.ruyiruyi.ui.activity.RightsActivity;
import com.ruyiruyi.ruyiruyi.ui.activity.ShopGoodsNewActivity;
import com.ruyiruyi.ruyiruyi.ui.activity.TireBuyNewActivity;
import com.ruyiruyi.ruyiruyi.ui.activity.TireChangeActivity;
import com.ruyiruyi.ruyiruyi.ui.activity.TireFreeChangeActivity;
import com.ruyiruyi.ruyiruyi.ui.activity.TirePlaceActivity;
import com.ruyiruyi.ruyiruyi.ui.activity.TireRepairActivity;
import com.ruyiruyi.ruyiruyi.ui.activity.TireWaitChangeActivity;
import com.ruyiruyi.ruyiruyi.ui.fragment.base.RyBaseFragment;
import com.ruyiruyi.ruyiruyi.ui.model.Event;
import com.ruyiruyi.ruyiruyi.ui.multiType.Function;
import com.ruyiruyi.ruyiruyi.ui.multiType.FunctionViewBinder;
import com.ruyiruyi.ruyiruyi.ui.multiType.Hometop;
import com.ruyiruyi.ruyiruyi.ui.multiType.HometopViewBinder;
import com.ruyiruyi.ruyiruyi.ui.multiType.NameEvent;
import com.ruyiruyi.ruyiruyi.ui.multiType.NameEventViewBinder;
import com.ruyiruyi.ruyiruyi.ui.multiType.OneEvent;
import com.ruyiruyi.ruyiruyi.ui.multiType.OneEventViewBinder;
import com.ruyiruyi.ruyiruyi.ui.multiType.ThreeEvent;
import com.ruyiruyi.ruyiruyi.ui.multiType.ThreeEventViewBinder;
import com.ruyiruyi.ruyiruyi.ui.multiType.TwoEvent;
import com.ruyiruyi.ruyiruyi.ui.multiType.TwoEventViewBinder;
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
         , OneEventViewBinder.OnEventClick {

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
    private int carUUId;
    private int authenticatedState;  //s是否认证  1已认证   2未认证
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
    private String service_year;            //最大服务年限
    private String service_year_length;     //当前服务年限
    private String service_end_date;
    private List<OneEvent> activitys;
    public List<Event> eventList;

    private int uesrCarId;

    private ProgressDialog progressDialog;

    private String redpacketUrl;
    private String redpacketTitle;
    private String redpacketBody;
    private AlertDialog carInfoDialog;
    private AlertDialog carAutoDialog;


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
        eventList = new ArrayList<>();
        listView = (RecyclerView) getView().findViewById(R.id.home_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        listView.setLayoutManager(linearLayoutManager);
        adapter = new MultiTypeAdapter(items);
        Bundle bundle = getArguments();
        currentCity = bundle.getString("city");
        ischoos = bundle.getInt("ischoos", 0); //1是选择返回

        progressDialog = new ProgressDialog(getContext());

        carInfoDialog = new AlertDialog.Builder(getContext())
                .setTitle("请完善车辆信息")
                .setMessage("是否前往完善信息界面")
                .setIcon(R.mipmap.ic_logo)
                .setPositiveButton("前往", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(getContext(), CarManagerActivity.class);
                        intent.putExtra("FRAGMENT", "HOMEFRAGMENT");
                        startActivityForResult(intent, MainActivity.HOMEFRAGMENT_RESULT);
                    //    getActivity().finish();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                }).create();

        carAutoDialog = new AlertDialog.Builder(getContext())
                .setTitle("请认证车辆")
                .setMessage("你的车辆还未认证，是否前往认证车前？")
                .setIcon(R.mipmap.ic_logo)
                .setPositiveButton("前往", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(getContext(), CarManagerActivity.class);
                        intent.putExtra("FRAGMENT", "HOMEFRAGMENT");
                        startActivityForResult(intent, MainActivity.HOMEFRAGMENT_RESULT);
                        //    getActivity().finish();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                }).create();


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
        }else {
            startActivity(new Intent(getContext(),CityChooseActivity.class));
        }

        if (currentCity!=null){
            if (currentCity.equals("选择城市")){
                startActivity(new Intent(getContext(),CityChooseActivity.class));
            }
        }



       // initdataFromService();
    }


    private void initdataFromService() {
        DbConfig dbConfig = new DbConfig(getContext());
        int id = dbConfig.getId();

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userId", id);
            if (currentCity !=null && !currentCity.equals("")){
                jsonObject.put("position", currentCity);
            }else {
                jsonObject.put("position", "");
            }

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
                        activitys.clear();
                        try {
                            JSONArray jsActivityList = data.getJSONArray("activityList");
                            if (jsActivityList.length() > 0){
                                eventList.clear();
                                for (int i = 0; i < jsActivityList.length(); i++) {
                                    JSONObject objBean = (JSONObject) jsActivityList.get(i);
                                    int id1 = objBean.getInt("id");
                                    int skip = objBean.getInt("skip");
                                    int type = objBean.getInt("type");
                                    String content = objBean.getString("content");
                                    String imageUrl = objBean.getString("imageUrl");
                                    String positionIdList = objBean.getString("positionIdList");
                                    String positionNameList = objBean.getString("positionNameList");
                                    String storeIdList = objBean.getString("storeIdList");
                                    String webUrl = objBean.getString("webUrl");
                                    int stockId = objBean.getInt("stockId");
                                    int serviceId = objBean.getInt("serviceId");
                                    Event event = new Event(id1, content, imageUrl, positionIdList, positionNameList, skip, storeIdList, type, webUrl,stockId,serviceId);
                                    eventList.add(event);
                                }
                            }

                        }catch (JSONException exception){
                            eventList.clear();
                        }
                        //获取轮播数据
                        lunbos.clear();
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
                                carUUId = carObject.getInt("car_id");

                                if (carUUId == 0){
                                    fontSize = carObject.getString("font");
                                    tireSame = carObject.getBoolean("same");
                                    rearSize = "";
                                    carImage = "";
                                    carName = "";
                                    authenticatedState = carObject.getInt("authenticatedState");
                                }else {
                                    fontSize = carObject.getString("font");
                                    tireSame = carObject.getBoolean("same");
                                    rearSize = carObject.getString("rear");
                                    carImage = carObject.getString("car_brand_url");
                                    carName = carObject.getString("car_verhicle");
                                    authenticatedState = carObject.getInt("authenticatedState");

                                }
                                service_end_date = carObject.getString("service_end_date");
                                service_year = carObject.getString("service_year");
                                service_year_length = carObject.getString("service_year_length");
                                carId = carObject.getInt("user_car_id");
                                User user = new DbConfig(getContext()).getUser();
                                user.setCarId(carId);
                                user.setAuthenticatedState(authenticatedState);
                                saveUserIntoDb(user);
                            } else {
                                int carId = 0;
                                User user = new DbConfig(getContext()).getUser();
                                user.setCarId(carId);
                                user.setAuthenticatedState(2);
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
        /*DbConfig dbConfig = new DbConfig(getContext());
        DbManager db = dbConfig.getDbManager();
        try {
            db.saveOrUpdate(lunbos);
        } catch (DbException e) {

        }*/

    }


    private void register() {
        HometopViewBinder hometopViewBinder = new HometopViewBinder(getContext());
        hometopViewBinder.setListener(this);
        adapter.register(Hometop.class, hometopViewBinder);

        FunctionViewBinder functionViewBinder = new FunctionViewBinder();
        functionViewBinder.setListener(this);
        adapter.register(Function.class, functionViewBinder);

        ThreeEventViewBinder threeEventViewBinder = new ThreeEventViewBinder(getContext());
        threeEventViewBinder.setListener(this);
        adapter.register(ThreeEvent.class, threeEventViewBinder);

        OneEventViewBinder oneEventViewBinder = new OneEventViewBinder(getContext());
        oneEventViewBinder.setListener(this);
        adapter.register(OneEvent.class, oneEventViewBinder);

        TwoEventViewBinder twoEventViewBinder = new TwoEventViewBinder(getContext());
        twoEventViewBinder.setListener(this);
        adapter.register(TwoEvent.class, twoEventViewBinder);

        adapter.register(NameEvent.class,new NameEventViewBinder(getContext()));
    }

    private void initdata() {
        DbConfig dbConfig = new DbConfig(getContext());
        User user = new User();
        user = dbConfig.getUser();
        if (user != null) {
            int carId = user.getCarId();
        }

/*

        List<Lunbo> lunboList = new ArrayList<>();
        lunboList = dbConfig.getLunbo();

*/

        items.clear();
        ArrayList<String> images = new ArrayList<>();
        for (int i = 0; i < lunbos.size(); i++) {
            images.add(lunbos.get(i).getContentImageUrl());
        }
        if (!(user == null)) {
            //int firstAddCar1 = user.getFirstAddCar();
            if (carId == 0) {
                items.add(new Hometop(images, "添加我的宝驹", "邀请好友绑定车辆可得现金券", 1, currentCity));
            } else {
                if (carUUId == 0){  //小程序购买轮胎 信息车辆不完整
                    Hometop carInfo = new Hometop(images, "请完善车辆信息", "完善车辆信息后可享受特色服务", 3, currentCity);
                    items.add(carInfo);

                }else {
                    Hometop carInfo = new Hometop(images, carName, "买轮胎即送畅行无忧", 2, currentCity);
                    carInfo.setCarImage(carImage);
                    items.add(carInfo);
                }

            }
        } else {//未登陆
            items.add(new Hometop(images, "新人注册享好礼", "购买轮胎即送畅行无忧", 0, currentCity));
        }


        items.add(new Function());

        items.add(new OneEvent(new Event(1000,"","http://180.76.243.205:8111/images-new/activity/activity/ic_quanyi.png","","",4,"",1,"")));

        if (eventList.size()>0){
            for (int i = 0; i < eventList.size(); i++) {
                int type = eventList.get(i).getType();
                if (type == 0){
                    items.add(new NameEvent(eventList.get(i)));
                }else if (type == 1){
                    items.add(new OneEvent(eventList.get(i)));
                }else if (type == 2){
                    items.add(new TwoEvent(eventList.get(i),eventList.get(i+1)));
                    i = i+1;
                }else if (type == 3){
                    items.add(new ThreeEvent(eventList.get(i),eventList.get(i+1),eventList.get(i+2)));
                    i = i+2;
                }
            }
        }
       /*
        items.add(new ThreeEvent());
        if (activitys.size() > 0){
            for (int i = 0; i < activitys.size(); i++) {
                items.add( activitys.get(i));
            }
        }*/

       // items.add(activitys.get(0));//TODO 活动列表暂1条数据
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
    public void onCityLayoutClickListener(String cityName) {
        Intent intent = new Intent(getContext(), CityChooseActivity.class);
        intent.putExtra("CITY_NAME",cityName);
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
        //    getActivity().finish();
        }
    }

    /**
     * 轮播图点击
     * @param position
     */
    @Override
    public void onLunboClikcListener(int position) {
        Log.e(TAG, "onLunboClikcListener: " + position);


        if (position == -1) {

/*      //跳转到轮胎购买界面
        //判断是否登录（未登录提示登录）
            if (!judgeIsLogin()) {
                return;
            }
            if (carId == 0) {
                Toast.makeText(getContext(), "您还未添加车辆，请添加默认车辆", Toast.LENGTH_SHORT).show();
                return;
            }
            if (tireSame) {  //前后轮一样
                if (service_end_date.equals("")){   //未选服务年限
                    Intent intent = new Intent(getContext(), TireBuyNewActivity.class);
                    intent.putExtra("TIRESIZE", fontSize);
                    intent.putExtra("FONTREARFLAG", "0");
                    intent.putExtra("SERVICE_YEAR_MAX", service_year);
                    intent.putExtra("SERVICE_YEAR", service_year_length);
                    intent.putExtra("SERVICE_END_YEAR", service_end_date);
                    intent.putExtra("CHOOSE_SERVICE_YEAR", false);
                    intent.putExtra("CARID", carId);
                    intent.putExtra("USERCARID",uesrCarId);
                    startActivity(intent);
                }else {     //已选服务年限
                    Intent intent = new Intent(getContext(), TireBuyNewActivity.class);
                    intent.putExtra("TIRESIZE", fontSize);
                    intent.putExtra("FONTREARFLAG", "0");
                    intent.putExtra("SERVICE_YEAR_MAX", service_year);
                    intent.putExtra("SERVICE_YEAR", service_year_length);
                    intent.putExtra("SERVICE_END_YEAR", service_end_date);
                    intent.putExtra("CHOOSE_SERVICE_YEAR", true);
                    intent.putExtra("CARID", carId);
                    intent.putExtra("USERCARID",uesrCarId);
                    startActivity(intent);
                }


            } else {         //前后轮不一样
                Intent intent = new Intent(getContext(), TirePlaceActivity.class);
                intent.putExtra("FONTSIZE", fontSize);
                intent.putExtra("REARSIZE", rearSize);
                intent.putExtra("SERVICE_YEAR_MAX", service_year);
                intent.putExtra("SERVICE_YEAR", service_year_length);
                intent.putExtra("SERVICE_END_YEAR", service_end_date);
                intent.putExtra("CARID", carId);
                intent.putExtra("USERCARID",uesrCarId);
                startActivity(intent);
            }*/
        }else {
            //判断是否登录（未登录提示登录）
            if (!judgeIsLogin()) {
                return;
            }
            if (carId == 0){
                Toast.makeText(getContext(), "您还未添加车辆，请添加默认车辆", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent intent = new Intent(getContext(), LunboContentActivity.class);
            if (tireSame){
                intent.putExtra("FONTREARFLAG",0);
            }else {
                intent.putExtra("FONTREARFLAG", 1);
            }
            intent.putExtra("FONTSIZE", fontSize);
            intent.putExtra("REARSIZE", rearSize);
            intent.putExtra("SERVICE_YEAR_MAX", service_year);      //最大服务年限
            intent.putExtra("SERVICE_YEAR", service_year_length);   //当前服务年限
            intent.putExtra("SERVICE_END_YEAR", service_end_date);
            intent.putExtra("CARID", carId);
            intent.putExtra("USERCARID",uesrCarId);
            intent.putExtra("CARUUID",carUUId);
            intent.putExtra(LunboContentActivity.LUNBO_POSITION, position);
            startActivity(intent);
        }
    }

    @Override
    public void onFunctionClickListener(int type) {
        //判断是否登录（未登录提示登录）
        if (!judgeIsLogin()) {
            return;
        }
        if (carUUId == 0){
            carInfoDialog.show();
            return;
        }
        if (type == 0) {//轮胎购买
            if (carId == 0){
                //点击轮胎购买，没有添加车辆，先跳转到添加车辆界面
                Intent intent = new Intent(getContext(), CarInfoActivity.class);
                intent.putExtra("CANCLICK", 0);
                intent.putExtra("FROM", 3);
                startActivity(intent);
             /*   Toast.makeText(getContext(), "您还未添加车辆，请添加默认车辆", Toast.LENGTH_SHORT).show();
                return;*/
            }
            if (tireSame) {  //前后轮一样
                if (service_end_date.equals("")){   //未选服务年限
                    Intent intent = new Intent(getContext(), TireBuyNewActivity.class);
                    intent.putExtra("TIRESIZE", fontSize);
                    intent.putExtra("FONTREARFLAG", "0");
                    intent.putExtra("SERVICE_YEAR_MAX", service_year);
                    intent.putExtra("SERVICE_YEAR", service_year_length);
                    intent.putExtra("CHOOSE_SERVICE_YEAR", false);
                    intent.putExtra("CARID", carId);
                    intent.putExtra("USERCARID",uesrCarId);
                    startActivity(intent);
                }else {     //已选服务年限
                    Intent intent = new Intent(getContext(), TireBuyNewActivity.class);
                    intent.putExtra("TIRESIZE", fontSize);
                    intent.putExtra("FONTREARFLAG", "0");

                    intent.putExtra("SERVICE_YEAR_MAX", service_year);
                    intent.putExtra("SERVICE_YEAR", service_year_length);
                    intent.putExtra("CHOOSE_SERVICE_YEAR", true);
                    intent.putExtra("CARID", carId);
                    intent.putExtra("USERCARID",uesrCarId);
                    startActivity(intent);
                }
            } else {         //前后轮不一样
                Intent intent = new Intent(getContext(), TirePlaceActivity.class);
                intent.putExtra("FONTSIZE", fontSize);
                intent.putExtra("REARSIZE", rearSize);
                intent.putExtra("SERVICE_YEAR_MAX", service_year);
                intent.putExtra("SERVICE_YEAR", service_year_length);
                intent.putExtra("SERVICE_END_YEAR", service_end_date);
                intent.putExtra("CARID", carId);
                intent.putExtra("USERCARID",uesrCarId);
                startActivity(intent);
            }

        } else if (type == 1) {//免费更换

            if (carId == 0){
                Toast.makeText(getContext(), "您还未添加车辆，请添加默认车辆", Toast.LENGTH_SHORT).show();
                return;
            }
            if (authenticatedState  == 2){      //wei认证
                carAutoDialog.show();
            }else {
                Intent intent = new Intent(getContext(), TireFreeChangeActivity.class);
                intent.putExtra(TireChangeActivity.CHANGE_TIRE, 1);
                startActivity(intent);
            }

        } else if (type == 2) {//轮胎修补
            if (carId == 0){
                Toast.makeText(getContext(), "您还未添加车辆，请添加默认车辆", Toast.LENGTH_SHORT).show();
                return;
            }

            if (authenticatedState  == 2){      //wei认证
                carAutoDialog.show();
            }else {
                startActivity(new Intent(getContext(), TireRepairActivity.class));
            }

        } else if (type == 3) { //待更换轮胎

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
    public void onOneEventClickListener(int skip, String content, String webUrl,int stockId,int serviceId) {
        Log.e(TAG, "onOneEventClickListener: " + skip );
        if (skip == 0){
            //跳转活动页面
            if (!judgeIsLogin()) {
                return;
            }
            Intent intent = new Intent(getContext(), BottomEventActivity.class);
            intent.putExtra("webUrl", webUrl);
            startActivity(intent);
        }else if (skip == 1){
            //跳转活动页面
            if (!judgeIsLogin()) {
                return;
            }
            initHongbaoData(webUrl);

        }else if (skip == 2){       //查看商品分类
            if (!judgeIsLogin()) {
                return;
            }
            Intent intent = new Intent(getContext(), GoodsShopActivity.class);
            intent.putExtra(GoodsShopActivity.CLASS_ID,serviceId);
            intent.putExtra(GoodsShopActivity.CLASS_NAME,"");
            intent.putExtra(GoodsShopActivity.FROMTYPE,0);
            startActivity(intent);

        }else if (skip == 3){
            if (!judgeIsLogin()) {
                return;
            }
            getStockInfo(stockId);      //前往查看门店详情
        }else if (skip == 4){   //我的权益跳转
            if (!judgeIsLogin()) {
                return;
            }
           startActivity(new Intent(getContext(), RightsActivity.class));
        }
    }

    /**
     * 获取分享链接
     */
    private void initHongbaoData(final String webUrl) {
        JSONObject object = new JSONObject();
        try {
            object.put("a", "aa");
        } catch (JSONException e) {
        }
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL_ACTIVITY_RELEASE + "invite/Url");
        params.addBodyParameter("reqJson", object.toString());
        Log.e(TAG, "initGoodsHongbaoData: ---22---" + params);
        x.http().get(params, new Callback.CommonCallback<String>() {


            @Override
            public void onSuccess(String result) {
                JSONObject object = null;
                try {
                    object = new JSONObject(result);
                    JSONObject inviteRegister = object.getJSONObject("inviteRegister");

                  //  wenUrl = inviteRegister.getString("url");
                    redpacketUrl = inviteRegister.getString("shareUrl") + "?userId=" + new DbConfig(getContext()).getId();
                    redpacketTitle = inviteRegister.getString("shareTitle");
                    redpacketBody = inviteRegister.getString("shareTitle");
                    goH5(webUrl);
                } catch (JSONException e) {
                    e.printStackTrace();
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

    private void goH5(String webUrl) {
        Intent intent = new Intent(getContext(), BottomEventActivity.class);
        intent.putExtra("webUrl", webUrl +"?userId=" + new DbConfig(getContext()).getId());
        intent.putExtra("canShare", true);
        intent.putExtra("shareUrl", redpacketUrl);
        intent.putExtra("shareDescription", redpacketTitle);
        startActivity(intent);
    }

    /**
     * 根据商品id获取商品详情
     * @param stockId
     */
    private void getStockInfo(final int stockId) {
       // showDialogProgress(progressDialog,"正在前往购买商品...");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("page", "1");
            jsonObject.put("rows","5" );
            jsonObject.put("stockId",stockId);
        } catch (JSONException e) {
        }
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "getStockByCondition");
        params.addBodyParameter("reqJson", jsonObject.toString());
        Log.e(TAG, "initDataFromService: --" + jsonObject.toString());
        String token = new DbConfig(getContext()).getToken();
        params.addParameter("token", token);
        x.http().get(params, new Callback.CommonCallback<String>() {
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
                        JSONObject object = rows.getJSONObject(0);
                        int storeId = object.getInt("storeId");
                        int serviceId = object.getInt("serviceId");
                        int serviceTypeId = object.getInt("serviceTypeId");

                        getShopInfo(stockId,storeId,serviceId,serviceTypeId);
                    } else if (status.equals("-999")) {
                        showUserTokenDialog("您的账号在其它设备登录,请重新登录");
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

    /**
     * 查店铺信息
     * @param stockId
     * @param storeId
     * @param serviceId
     * @param serviceTypeId
     */
    private void getShopInfo(int stockId, final int storeId, final int serviceId, final int serviceTypeId) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("storeId", storeId);
            jsonObject.put("longitude", "");
            jsonObject.put("latitude", "");
        } catch (JSONException e) {
        }
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "getStoreInfoByStoreId");
        params.addBodyParameter("reqJson", jsonObject.toString());
        String token = new DbConfig(getContext()).getToken();
        params.addParameter("token", token);
        x.http().get(params, new Callback.CommonCallback<String>() {
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
                        String storeName = data.getString("storeName");
                        String storeImg = data.getString("storeImg");
                        goShopHome(storeId,storeName,storeImg,serviceId,serviceTypeId);

                    } else if (status.equals("-999")) {
                        showUserTokenDialog("您的账号在其它设备登录,请重新登录");
                    } else {
                        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
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

    /**
     * 跳转到门店商品详情页
     * @param storeId
     * @param storeName
     * @param storeImg
     * @param serviceId
     * @param serviceTypeId
     */
    private void goShopHome(int storeId, String storeName, String storeImg, int serviceId, int serviceTypeId) {
      //  hideDialogProgress(progressDialog);
        Intent intent = new Intent(getContext(), ShopGoodsNewActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(ShopGoodsNewActivity.FROM_TYPE,1);
        bundle.putInt(ShopGoodsNewActivity.STORE_ID,storeId);
        bundle.putString(ShopGoodsNewActivity.STORE_NAME,storeName);
        bundle.putString(ShopGoodsNewActivity.STORE_IMAGE,storeImg);
        bundle.putInt(ShopGoodsNewActivity.GOODS_CLASS_ID,serviceId);
        bundle.putInt(ShopGoodsNewActivity.GOODS_CLASS_TYPE_ID,serviceTypeId);
        intent.putExtras(bundle);
        startActivity(intent);
    }


/*    @Override
    public void onEventClickListener(String tag) {
        if (tag.equals("cxwy")) {//（畅行无忧）
            //判断是否登录（未登录提示登录）
            if (!judgeIsLogin()) {
                return;
            }
            if (carId == 0){
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

    *//*
    * 底部活动点击事件回调
    * *//*
    @Override
    public void onOneEventClickListener(String webUrl) {
        //跳转活动页面
        if (!judgeIsLogin()) {
            return;
        }
        Intent intent = new Intent(getContext(), BottomEventActivity.class);
        intent.putExtra("webUrl", webUrl);
        startActivity(intent);
    }*/

    public interface OnIconClikc {
        void onShopClassClickListener();
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e(TAG, "onStart:onStart "  );
    }

    @Override
    public void onResume() {
        super.onResume();
        initdataFromService();
        Log.e(TAG, "onStart:onResume "  );
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e(TAG, "onStart:onPause "  );
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.e(TAG, "onStart:onStop "  );
    }


}