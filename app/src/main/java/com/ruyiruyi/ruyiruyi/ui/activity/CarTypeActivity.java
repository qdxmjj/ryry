package com.ruyiruyi.ruyiruyi.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.ruyiruyi.ruyiruyi.R;
import com.ruyiruyi.ruyiruyi.db.DbConfig;
import com.ruyiruyi.ruyiruyi.db.model.CarTireInfo;
import com.ruyiruyi.ruyiruyi.ui.activity.base.RyBaseActivity;
import com.ruyiruyi.ruyiruyi.ui.multiType.CarTitle;
import com.ruyiruyi.ruyiruyi.ui.multiType.CarTitleViewBinder;
import com.ruyiruyi.ruyiruyi.ui.multiType.CarType;
import com.ruyiruyi.ruyiruyi.ui.multiType.CarTypeViewBinder;
import com.ruyiruyi.ruyiruyi.utils.RequestUtils;
import com.ruyiruyi.ruyiruyi.utils.UtilsRY;
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

import static me.drakeet.multitype.MultiTypeAsserts.assertAllRegistered;
import static me.drakeet.multitype.MultiTypeAsserts.assertHasTheSameAdapter;

public class CarTypeActivity extends RyBaseActivity implements CarTypeViewBinder.OnCarTypeClick ,CarTitleViewBinder.OnCarTitlrClick{
    private static final String TAG = CarTypeActivity.class.getSimpleName();
    private ActionBar actionBar;
    private RecyclerView listView;
    private List<Object> items = new ArrayList<>();
    public  List<CarTitle> titleList ;
    public  List<CarTireInfo> carTireInfoList;
    private MultiTypeAdapter adapter;
    private int vercicleid;
    public static int currentType = 0;
    public static String currentPaiLiang ;
    public static String currentYear ;
    public static boolean ishave = false;
    private CarType carType;
    private int from;
    private int userCarId = 0;
    private int proveStatus = 2;//是否进行车主认证 (1 已认证 2 未认证)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_type,R.id.my_action);
        actionBar = (ActionBar) findViewById(R.id.my_action);
        actionBar.setTitle("车型选择");;
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
        titleList = new ArrayList<>();
        carTireInfoList = new ArrayList<>();
        Intent intent = getIntent();
        vercicleid = intent.getIntExtra("VERCICLEID",0);
        from = intent.getIntExtra("FROM",0);
        userCarId = intent.getIntExtra("USERCARID", 0);
        proveStatus = intent.getIntExtra("PROVESTATUS", 2);//是否进行车主认证 (1 已认证 2 未认证)
        carType = new CarType();

        initView();
        currentType = 0;
        //initData();

        initDataFromService();
    }

    private void initDataFromService() {
        int userId = new DbConfig(this).getId();
        JSONObject jsonObject = new JSONObject();
        try {
            if (currentType == 0){
                jsonObject.put("verhicleId", vercicleid);
                jsonObject.put("pailiang", "");
                jsonObject.put("year", "");
            }else if (currentType == 1){
                jsonObject.put("verhicleId", vercicleid);
                jsonObject.put("pailiang", currentPaiLiang);
                jsonObject.put("year", "");
            }else {
                jsonObject.put("verhicleId", vercicleid);
                jsonObject.put("pailiang", currentPaiLiang);
                jsonObject.put("year", currentYear);
            }

        } catch (JSONException e) {
        }
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "getCarTireInfoByCondition");
        Log.e(TAG, "initOrderFromService: -++-" + jsonObject.toString());
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
                        JSONArray data = jsonObject1.getJSONArray("data");
                        carTireInfoList.clear();
                        titleList.clear();
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject object = data.getJSONObject(i);
                            String brand = object.getString("brand");
                            String font = object.getString("font");
                            String rear = object.getString("rear");
                            String name = object.getString("name");
                            String verhicle = object.getString("verhicle");
                            String pailiang = object.getString("pailiang");
                            String year = object.getString("year");
                            long time = object.getLong("time");
                            String timestampToStringAll = new UtilsRY().getTimestampToStringAll(time);
                            int carBrandId = object.getInt("carBrandId");
                            int id = object.getInt("id");
                            int verhicleId = object.getInt("verhicleId");
                            Double price = object.getDouble("price");
                            CarTireInfo carTireInfo = new CarTireInfo(id, brand, carBrandId, verhicle, verhicleId, pailiang, year, name, font, rear, timestampToStringAll);
                            carTireInfoList.add(carTireInfo);
                        }
                        if (currentType == 0){
                            for (int i = 0; i < carTireInfoList.size(); i++) {
                                CarTitle carTitle = new CarTitle(carTireInfoList.get(i).getPailiang());
                                titleList.add(carTitle);
                            }
                            carType.setCarType(0);
                        }else if (currentType == 1){
                            for (int i = 0; i < carTireInfoList.size(); i++) {
                                CarTitle carTitle = new CarTitle(carTireInfoList.get(i).getYear());
                                titleList.add(carTitle);
                            }
                            carType.setCarType(1);
                            carType.setPailiang(currentPaiLiang);
                        }else {
                            for (int i = 0; i < carTireInfoList.size(); i++) {
                                CarTitle carTitle = new CarTitle(carTireInfoList.get(i).getName());
                                titleList.add(carTitle);
                            }
                            carType.setCarType(2);
                            carType.setPailiang(currentPaiLiang);
                            carType.setYear(currentYear);
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
      /*  DbManager db = new DbConfig(this).getDbManager();
        List<CarTitle> titleList = new ArrayList<>();
        List<CarType>  typeList = new ArrayList<>();
        CarType carType = new CarType();
        try {
            if (currentType == 0){      //选择排量
                List<CarTireInfo> carTireInfoList = db.selector(CarTireInfo.class)
                        .where("verhicle_id", "=", vercicleid)
                        .findAll();
                for (int i = 0; i < carTireInfoList.size(); i++) {
                    String pailiang = carTireInfoList.get(i).getPailiang();
                    ishave =false;

                    for (int j = 0; j < titleList.size(); j++) {        //去重
                        if (pailiang.equals(titleList.get(j).getCarTitle())){
                            ishave  = true;//是否是重复数据
                        }
                    }

                    if (!ishave){
                        CarTitle carTitle = new CarTitle(pailiang);
                        titleList.add(carTitle);
                    }

                }
                carType.setCarType(0);
            }else if (currentType == 1){
                List<CarTireInfo> carTireInfoList = db.selector(CarTireInfo.class)
                        .where("verhicle_id", "=", vercicleid)
                        .and("pailiang","=",currentPaiLiang)
                        .findAll();
                for (int i = 0; i < carTireInfoList.size(); i++) {
                    String year = carTireInfoList.get(i).getYear();
                    ishave = false;

                    for (int j = 0; j < titleList.size(); j++) {        //去重
                        if (year.equals(titleList.get(j).getCarTitle())){
                            ishave = true;
                        }
                    }

                    if (!ishave){
                        CarTitle carTitle = new CarTitle(year);
                        titleList.add(carTitle);
                    }

                }
                carType.setCarType(1);
                carType.setPailiang(currentPaiLiang);
            }else {
                List<CarTireInfo> carTireInfoList = db.selector(CarTireInfo.class)
                        .where("verhicle_id", "=", vercicleid)
                        .and("pailiang","=",currentPaiLiang)
                        .and("year","=",currentYear)
                        .findAll();
                for (int i = 0; i < carTireInfoList.size(); i++) {
                    String verhicle = carTireInfoList.get(i).getName();
                    CarTitle carTitle = new CarTitle(verhicle);
                    titleList.add(carTitle);
                }
                carType.setCarType(2);
                carType.setPailiang(currentPaiLiang);
                carType.setYear(currentYear);
            }
        } catch (DbException e) {

        }

*/

        items.clear();
        items.add(carType);

        for (int i = 0; i < titleList.size(); i++) {
            items.add(titleList.get(i));
        }
        assertAllRegistered(adapter,items);
        adapter.notifyDataSetChanged();


    }

    private void initView() {
        listView = (RecyclerView) findViewById(R.id.car_type_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        listView.setLayoutManager(linearLayoutManager);
        adapter = new MultiTypeAdapter(items);
        register();
        listView.setAdapter(adapter);
        assertHasTheSameAdapter(listView, adapter);
    }

    private void register() {
        CarTitleViewBinder carTitleViewBinder = new CarTitleViewBinder();
        carTitleViewBinder.setListener(this);
        adapter.register(CarTitle.class, carTitleViewBinder);
        CarTypeViewBinder carTypeViewBinder = new CarTypeViewBinder();
        carTypeViewBinder.setListener(this);
        adapter.register(CarType.class, carTypeViewBinder);
    }

    @Override
    public void onCarYearLayoutClickListener(int type, String pailiang) {
        currentType = type;
        currentPaiLiang = pailiang;
        initDataFromService();;
    }

    @Override
    public void onCarPaiLiangLayoutClickListener(int type) {
        currentType = type;
        initDataFromService();;
    }

    @Override
    public void onCarTitleItemClikcListener(String title) {
        if (currentType == 0){
            currentPaiLiang = title;
        }else if (currentType == 1){
            currentYear = title;
        }else {
            for (int i = 0; i < carTireInfoList.size(); i++) {
                if (carTireInfoList.get(i).getName().equals(title)) {
                    int id = carTireInfoList.get(i).getId();
                    String font = carTireInfoList.get(i).getFont();
                    String rear = carTireInfoList.get(i).getRear();
                    String brand = carTireInfoList.get(i).getBrand();
                    Log.e(TAG, "onCarTitleItemClikcListener: ----" + id );
                    Intent intent = new Intent(this, CarInfoActivity.class);
                    intent.putExtra("CARTIREIINFO",id);
                    intent.putExtra("FONT",font);
                    intent.putExtra("REAR",rear);
                    intent.putExtra("BRAND",brand);
                    intent.putExtra("USERCARID",userCarId);
                    intent.putExtra("PROVESTATUS",proveStatus);
                    if (from == 4){
                        intent.putExtra("FROM",5);
                    }else if (from == 7){
                        intent.putExtra("FROM", 7);
                    }else {
                        intent.putExtra("FROM",0);
                    }

                    startActivity(intent);
                }
            }
          /*  DbManager db = new DbConfig(this).getDbManager();
            try {
                List<CarTireInfo> carTireInfoList = db.selector(CarTireInfo.class)
                        .where("name", "=", title)
                        .and("year" ,"=",currentYear)
                        .and("pailiang" ,"=",currentPaiLiang)
                        .findAll();
                Log.e(TAG, "onCarTitleItemClikcListener: " + carTireInfoList.size());
                int id = carTireInfoList.get(0).getId();
                Intent intent = new Intent(this, CarInfoActivity.class);
                intent.putExtra("CARTIREIINFO",id);
                intent.putExtra("FROM",0);
                startActivity(intent);
            } catch (DbException e) {

            }*/
        }
        currentType += 1;
        initDataFromService();;
    }
}
