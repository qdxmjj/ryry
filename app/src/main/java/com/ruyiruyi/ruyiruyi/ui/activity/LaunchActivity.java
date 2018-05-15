package com.ruyiruyi.ruyiruyi.ui.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.Poi;
import com.bumptech.glide.Glide;
import com.ruyiruyi.ruyiruyi.MainActivity;
import com.ruyiruyi.ruyiruyi.MyApplication;
import com.ruyiruyi.ruyiruyi.R;
import com.ruyiruyi.ruyiruyi.db.DbConfig;
import com.ruyiruyi.ruyiruyi.db.model.CarBrand;
import com.ruyiruyi.ruyiruyi.db.model.CarFactory;
import com.ruyiruyi.ruyiruyi.db.model.CarTireInfo;
import com.ruyiruyi.ruyiruyi.db.model.CarVerhicle;
import com.ruyiruyi.ruyiruyi.db.model.Location;
import com.ruyiruyi.ruyiruyi.db.model.Province;
import com.ruyiruyi.ruyiruyi.db.model.TireType;
import com.ruyiruyi.ruyiruyi.ui.activity.base.RyBaseActivity;
import com.ruyiruyi.ruyiruyi.ui.service.LocationService;
import com.ruyiruyi.ruyiruyi.utils.RequestUtils;
import com.ruyiruyi.ruyiruyi.utils.UtilsRY;
import com.ruyiruyi.rylibrary.base.BaseActivity;

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

public class LaunchActivity extends BaseActivity {

    private static final String TAG = LaunchActivity.class.getSimpleName();
    private ImageView launchImage;
    public String currentCity="";
    private double jingdu  = 0.00;
    private double weidu = 0.00;
    private LocationService locationService;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            goMain();

        }
    };

    private void goMain() {


        initCarDataIntoDb();
        //获取车辆图标数据
        initCarBrand();
        //获取车辆型号数据
        initCarVerhicle();
        //获取车辆轮胎和排量数据
        initCarrTireInfo();
        //获取轮胎型号
        initTireType();
        //获取省市县
        initProvice();
        initDingwei();
        //获取车辆品牌数据
        Log.e(TAG, "goMain: --+--+-" + currentCity);
        Intent intent = new Intent(this, MainActivity.class);

        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void initDingwei() {
        locationService = ((MyApplication) getApplication()).locationService;
        //获取locationservice实例，建议应用中只初始化1个location实例，然后使用，可以参考其他示例的activity，都是通过此种方式获取locationservice实例的
        locationService.registerListener(mListener);
        locationService.setLocationOption(locationService.getDefaultLocationClientOption());
        locationService.start();// 定位SDK
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        handler.sendEmptyMessageDelayed(0,3000);
        //  handler.sendEmptyMessageDelayed(0,3000);

       /* initView();
      //  handler.sendEmptyMessageDelayed(0,3000);
        //获取车辆品牌数据
        initCarDataIntoDb();
        //获取车辆图标数据
        initCarBrand();
        //获取车辆型号数据
        initCarVerhicle();
        //获取车辆轮胎和排量数据
        initCarrTireInfo();
        //获取轮胎型号
        initTireType();
        //获取省市县
        initProvice();*/
    }




    private void initView() {
        launchImage = (ImageView) findViewById(R.id.launch_image);
        Glide.with(this).load("http://180.76.243.205:8111/images/launch/launch.jpg").into(launchImage);
    }



    private void initCarDataIntoDb() {
        DbConfig dbConfig = new DbConfig();
        DbManager db = dbConfig.getDbManager();
        List<CarFactory> carList = null;
        try {
            carList = db.selector(CarFactory.class)
                    .orderBy("time")
                    .findAll();

        } catch (DbException e) {

        }
        JSONObject jsonObject = new JSONObject();

        try {
            if (carList == null){
                jsonObject.put("time","2000-00-00 00:00:00");
            }else {
                String time = carList.get(carList.size() - 1).getTime();
                jsonObject.put("time",time);
            }
        } catch (JSONException e) {
        }
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "getCarFactoryData");
        params.addBodyParameter("reqJson",jsonObject.toString());
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    Log.e(TAG, "onSuccess: " + result);
                    JSONObject jsonObject1 = new JSONObject(result);
                    String status = jsonObject1.getString("status");
                    String msg = jsonObject1.getString("msg");
                    if (status.equals("1")){
                        JSONArray data = jsonObject1.getJSONArray("data");
                        List<CarFactory> factoryList = new ArrayList<CarFactory>();
                        for (int i = 0; i < data.length(); i++) {
                            String factory = data.getJSONObject(i).getString("factory");
                            int id = data.getJSONObject(i).getInt("id");
                            int carBrandId = data.getJSONObject(i).getInt("carBrandId");
                            long time = data.getJSONObject(i).getLong("time");
                            String timestampToStringAll = new UtilsRY().getTimestampToStringAll(time);
                            factoryList.add(new CarFactory(id,carBrandId,factory,timestampToStringAll));
                        }
                        savaCarFactoryIntoDb(factoryList);
                    }else {
                        Toast.makeText(LaunchActivity.this, msg, Toast.LENGTH_SHORT).show();
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
     * 保存车辆工厂信息到数据库
     * @param factoryList
     */
    private void savaCarFactoryIntoDb(List<CarFactory> factoryList) {
        DbConfig dbConfig = new DbConfig();
        DbManager db = dbConfig.getDbManager();
        try {
            db.saveOrUpdate(factoryList);
        } catch (DbException e) {

        }
    }

    private void initCarBrand() {
        DbConfig dbConfig = new DbConfig();
        DbManager db = dbConfig.getDbManager();
        List<CarBrand> carBrandList = null;
        try {
            carBrandList = db.selector(CarBrand.class)
                    .orderBy("time")
                    .findAll();

        } catch (DbException e) {

        }
        JSONObject jsonObject = new JSONObject();

        try {
            if (carBrandList == null){
                jsonObject.put("time","2000-00-00 00:00:00");
            }else {
                String time = carBrandList.get(carBrandList.size() - 1).getTime();
                jsonObject.put("time",time);
            }
        } catch (JSONException e) {
        }
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "getCarBrandData");
        params.addBodyParameter("reqJson",jsonObject.toString());
        Log.e(TAG, "initCarBrand:---*--- " + params.toString());
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    Log.e(TAG, "onSuccess: " + result);
                    JSONObject jsonObject1 = new JSONObject(result);
                    String status = jsonObject1.getString("status");
                    String msg = jsonObject1.getString("msg");
                    if (status.equals("1")){
                        JSONArray data = jsonObject1.getJSONArray("data");
                        List<CarBrand> carBrandArrayList = new ArrayList<CarBrand>();
                        for (int i = 0; i < data.length(); i++) {


                            String icon = data.getJSONObject(i).getString("icon");
                            String imgUrl = data.getJSONObject(i).getString("imgUrl");
                            String name = data.getJSONObject(i).getString("name");
                            int id = data.getJSONObject(i).getInt("id");


                            long time = data.getJSONObject(i).getLong("time");
                            String timestampToStringAll = new UtilsRY().getTimestampToStringAll(time);


                            carBrandArrayList.add(new CarBrand(id,name,imgUrl,icon,timestampToStringAll));
                        }
                        saveCarBrandIntoDb(carBrandArrayList);
                    }else {
                        Toast.makeText(LaunchActivity.this, msg, Toast.LENGTH_SHORT).show();
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

    private void saveCarBrandIntoDb(List<CarBrand> brandList) {
        DbConfig dbConfig = new DbConfig();
        DbManager db = dbConfig.getDbManager();
        try {
            db.saveOrUpdate(brandList);
        } catch (DbException e) {

        }
    }

    private void initCarVerhicle() {
        DbConfig dbConfig = new DbConfig();
        DbManager db = dbConfig.getDbManager();
        List<CarVerhicle> carVerhicleList = null;
        try {
            carVerhicleList = db.selector(CarVerhicle.class)
                    .orderBy("time")
                    .findAll();

        } catch (DbException e) {

        }
        JSONObject jsonObject = new JSONObject();

        try {
            if (carVerhicleList == null){
                jsonObject.put("time","2000-00-00 00:00:00");
            }else {
                String time = carVerhicleList.get(carVerhicleList.size() - 1).getTime();
                Log.e(TAG, "initCarDataIntoDb: " + time);
                jsonObject.put("time",time);
            }
        } catch (JSONException e) {
        }
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "getCarVerhicleData");
        params.addBodyParameter("reqJson",jsonObject.toString());
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    Log.e(TAG, "onSuccess: " + result);
                    JSONObject jsonObject1 = new JSONObject(result);
                    String status = jsonObject1.getString("status");
                    String msg = jsonObject1.getString("msg");
                    if (status.equals("1")){
                        JSONArray data = jsonObject1.getJSONArray("data");
                        List<CarVerhicle> carVerhicleArrayList = new ArrayList<CarVerhicle>();
                        for (int i = 0; i < data.length(); i++) {
                            String carVersion = data.getJSONObject(i).getString("carVersion");
                            String verhicle = data.getJSONObject(i).getString("verhicle");
                            int id = data.getJSONObject(i).getInt("id");
                            int carBrandId = data.getJSONObject(i).getInt("carBrandId");
                            int factoryId = data.getJSONObject(i).getInt("factoryId");
                            int verify = data.getJSONObject(i).getInt("verify");


                            long time = data.getJSONObject(i).getLong("time");
                            String timestampToStringAll = new UtilsRY().getTimestampToStringAll(time);
                            carVerhicleArrayList.add(new CarVerhicle(id,verhicle,carBrandId,factoryId,carVersion,verify,timestampToStringAll));
                        }
                        savaCarVerhicleIntoDb(carVerhicleArrayList);
                    }else {
                        Toast.makeText(LaunchActivity.this, msg, Toast.LENGTH_SHORT).show();
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

    private void savaCarVerhicleIntoDb(List<CarVerhicle> verhiclesList) {
        DbConfig dbConfig = new DbConfig();
        DbManager db = dbConfig.getDbManager();
        try {
            db.saveOrUpdate(verhiclesList);
        } catch (DbException e) {

        }
    }

    private void initCarrTireInfo() {
        DbConfig dbConfig = new DbConfig();
        DbManager db = dbConfig.getDbManager();
        List<CarTireInfo> carTireInfoList = null;
        try {
            carTireInfoList = db.selector(CarTireInfo.class)
                    .orderBy("time")
                    .findAll();

        } catch (DbException e) {

        }
        JSONObject jsonObject = new JSONObject();

        try {
            if (carTireInfoList == null){
                jsonObject.put("time","2000-00-00 00:00:00");
            }else {
                String time = carTireInfoList.get(carTireInfoList.size() - 1).getTime();
                Log.e(TAG, "initCarDataIntoDb: " + time);
                jsonObject.put("time",time);
            }
        } catch (JSONException e) {
        }
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "getCarTireInfoData");
        params.addBodyParameter("reqJson",jsonObject.toString());
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess:getCarTireInfoData---" + result);
                try {
                    JSONObject jsonObject1 = new JSONObject(result);
                    String status = jsonObject1.getString("status");
                    String msg = jsonObject1.getString("msg");
                    if (status.equals("1")){
                        JSONArray data = jsonObject1.getJSONArray("data");
                        List<CarTireInfo> carTireInfoArrayList = new ArrayList<CarTireInfo>();
                        for (int i = 0; i < data.length(); i++) {

                            String brand = data.getJSONObject(i).getString("brand");
                            String verhicle = data.getJSONObject(i).getString("verhicle");
                            String font = data.getJSONObject(i).getString("font");
                            String name = data.getJSONObject(i).getString("name");
                            String pailiang = data.getJSONObject(i).getString("pailiang");
                            String rear = data.getJSONObject(i).getString("rear");
                            String year = data.getJSONObject(i).getString("year");
                            int id = data.getJSONObject(i).getInt("id");
                            int carBrandId = data.getJSONObject(i).getInt("carBrandId");
                            int verhicleId = data.getJSONObject(i).getInt("verhicleId");

                            long time = data.getJSONObject(i).getLong("time");
                            String timestampToStringAll = new UtilsRY().getTimestampToStringAll(time);


                            carTireInfoArrayList.add(new CarTireInfo(id,brand,carBrandId,verhicle,verhicleId,pailiang,year,name,font,rear,timestampToStringAll));
                        }
                        savaCarTireIntoDb(carTireInfoArrayList);
                    }else {
                        Toast.makeText(LaunchActivity.this, msg, Toast.LENGTH_SHORT).show();
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
    private void savaCarTireIntoDb(List<CarTireInfo> carTireInfoArrayList) {
        DbConfig dbConfig = new DbConfig();
        DbManager db = dbConfig.getDbManager();
        try {
            db.saveOrUpdate(carTireInfoArrayList);
        } catch (DbException e) {

        }
    }

    private void initTireType() {
        DbConfig dbConfig = new DbConfig();
        DbManager db = dbConfig.getDbManager();
        List<TireType> tireTypeList = null;
        try {
            tireTypeList = db.selector(TireType.class)
                    .orderBy("time")
                    .findAll();
        } catch (DbException e) {
        }
        JSONObject jsonObject = new JSONObject();

        try {
            if (tireTypeList == null){
                jsonObject.put("time","2000-00-00 00:00:00");
            }else {
                String time = tireTypeList.get(tireTypeList.size() - 1).getTime();
                jsonObject.put("time",time);
            }
        } catch (JSONException e) {

        }

        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "getTireType");
        params.addBodyParameter("reqJson",jsonObject.toString());

        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess:getTireTypeData---" + result);
                try {
                    JSONObject jsonObject1 = new JSONObject(result);
                    String status = jsonObject1.getString("status");
                    String msg = jsonObject1.getString("msg");
                    if (status.equals("1")){
                        JSONArray data = jsonObject1.getJSONArray("data");
                        List<TireType> tireTypeArrayList = new ArrayList<TireType>();
                        for (int i = 0; i < data.length(); i++) {
                            String tireDiameter = data.getJSONObject(i).getString("tireDiameter");
                            String tireFlatWidth = data.getJSONObject(i).getString("tireFlatWidth");
                            String tireFlatnessRatio = data.getJSONObject(i).getString("tireFlatnessRatio");
                            int id = data.getJSONObject(i).getInt("tireTypeId");

                            long time = data.getJSONObject(i).getLong("time");
                            String timestampToStringAll = new UtilsRY().getTimestampToStringAll(time);


                            tireTypeArrayList.add(new TireType(id,tireFlatWidth,tireFlatnessRatio,tireDiameter,timestampToStringAll));
                        }

                        saveTireTypeIntoDb(tireTypeArrayList);
                    }else {
                        Toast.makeText(LaunchActivity.this, msg, Toast.LENGTH_SHORT).show();
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
    private void saveTireTypeIntoDb(List<TireType> tireTypeArrayList) {
        DbConfig dbConfig = new DbConfig();
        DbManager db = dbConfig.getDbManager();
        try {
            db.saveOrUpdate(tireTypeArrayList);
        } catch (DbException e) {

        }
    }

    private void initProvice() {
        DbConfig dbConfig = new DbConfig();
        DbManager db = dbConfig.getDbManager();
        List<Province> provinceList = null;
        try {
            provinceList = db.selector(Province.class)
                    .orderBy("time")
                    .findAll();
        } catch (DbException e) {
        }
        JSONObject jsonObject = new JSONObject();
        try {
            if (provinceList == null){
                jsonObject.put("time","2000-00-00 00:00:00");
            }else {
                String time = provinceList.get(provinceList.size() - 1).getTime();
                jsonObject.put("time",time);
            }
        } catch (JSONException e) {

        }
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "getAllPositon");
        params.addBodyParameter("reqJson",jsonObject.toString());
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess:getTireTypeData---" + result);
                try {
                    JSONObject jsonObject1 = new JSONObject(result);
                    String status = jsonObject1.getString("status");
                    String msg = jsonObject1.getString("msg");
                    if (status.equals("1")){
                        JSONArray data = jsonObject1.getJSONArray("data");
                        List<Province> provinceArrayList = new ArrayList<Province>();
                        for (int i = 0; i < data.length(); i++) {

                            String name = data.getJSONObject(i).getString("name");
                            int id = data.getJSONObject(i).getInt("id");
                            int definition = data.getJSONObject(i).getInt("definition");
                            int fid = data.getJSONObject(i).getInt("fid");

                            long time = data.getJSONObject(i).getLong("time");
                            String timestampToStringAll = new UtilsRY().getTimestampToStringAll(time);


                            provinceArrayList.add(new Province(id,fid,definition,name,timestampToStringAll));
                        }

                        saveProvinceIntoDb(provinceArrayList);
                    }else {
                        Toast.makeText(LaunchActivity.this, msg, Toast.LENGTH_SHORT).show();
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
    /*****
     *
     * 定位结果回调，重写onReceiveLocation方法，可以直接拷贝如下代码到自己工程中修改
     *
     */
    private BDAbstractLocationListener mListener = new BDAbstractLocationListener() {

        private double weidu;
        private double jingdu;

        @Override
        public void onReceiveLocation(BDLocation location) {
            // TODO Auto-generated method stub
            if (null != location && location.getLocType() != BDLocation.TypeServerError) {

                locationService.unregisterListener(mListener); //注销掉监听
                locationService.stop(); //停止定位服务
                currentCity = location.getCity();
                jingdu = location.getLongitude();
                weidu = location.getLatitude();
                Location location1 = new Location(1, currentCity, jingdu, weidu);
                DbManager db = new DbConfig().getDbManager();
                try {
                    db.saveOrUpdate(location1);
                } catch (DbException e) {

                }
            }
        }

    };


    private void saveProvinceIntoDb(List<Province> provinceArrayList) {
        DbConfig dbConfig = new DbConfig();
        DbManager db = dbConfig.getDbManager();
        try {
            db.saveOrUpdate(provinceArrayList);
        } catch (DbException e) {

        }
    }

}
