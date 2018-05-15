package com.ruyiruyi.ruyiruyi.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.ruyiruyi.ruyiruyi.MainActivity;
import com.ruyiruyi.ruyiruyi.R;
import com.ruyiruyi.ruyiruyi.db.DbConfig;
import com.ruyiruyi.ruyiruyi.db.model.User;
import com.ruyiruyi.ruyiruyi.ui.cell.CarInfoCell;
import com.ruyiruyi.ruyiruyi.ui.fragment.MyFragment;
import com.ruyiruyi.ruyiruyi.ui.model.Car;
import com.ruyiruyi.ruyiruyi.utils.RequestUtils;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;
import com.ruyiruyi.rylibrary.base.BaseActivity;
import com.ruyiruyi.rylibrary.cell.ActionBar;
import com.ruyiruyi.rylibrary.utils.AndroidUtilities;

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

import rx.functions.Action1;

public class CarManagerActivity extends BaseActivity {
    private static final String TAG = CarManagerActivity.class.getSimpleName();
    private ActionBar actionBar;
    private TextView addCarView;
    private SwipeMenuListView listView;
    private final List<Car> carList = new ArrayList<>();
    private ListAdapter adapter;
    private String actionType;
    public static int CARMANAMGER_RESULT = 0;
    private String fromFragment = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_car,R.id.my_action);

        actionBar = (ActionBar) findViewById(R.id.my_action);
        actionBar.setTitle("管理车辆");;
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
        if (intent!=null){
            fromFragment = intent.getStringExtra(MyFragment.FROM_FRAGMENT);
        }


        initView();

        initDataFromService();
       // initData();


    }

    private void initDataFromService() {
        DbConfig dbConfig = new DbConfig();
        int id = dbConfig.getId();
        JSONObject jsonObject = new JSONObject();
        Log.e(TAG, "initDataFromService: " + id);
        try {
            jsonObject.put("userId",id);
        } catch (JSONException e) {

        }
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "getCarListByUserId");
        params.addBodyParameter("reqJson",jsonObject.toString());
        String token = new DbConfig().getToken();
        params.addParameter("token",token);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess: " + result);
                try {
                    JSONObject jsonObject1 = new JSONObject(result);
                    String status = jsonObject1.getString("status");
                    String msg = jsonObject1.getString("msg");
                    if (status.equals("1")){
                        JSONArray data = jsonObject1.getJSONArray("data");
                        carList.clear();
                        for (int i = 0; i < data.length(); i++) { ;
                            int car_id = data.getJSONObject(i).getInt("car_id");
                            int uesrCarId = data.getJSONObject(i).getInt("user_car_id");
                            int moren = data.getJSONObject(i).getInt("is_default");
                            String name = data.getJSONObject(i).getString("car_name");
                            String number = data.getJSONObject(i).getString("plat_number");
                            String icon = data.getJSONObject(i).getString("car_brand");
                            carList.add(new Car(car_id,uesrCarId,name,number,icon,moren));
                            if (moren ==1){
                                User user = new DbConfig().getUser();
                                user.setCarId(uesrCarId);
                                saveUserIntoDb(user);
                            }
                        }
                        Log.e(TAG, "onSuccess: " + carList.size());
                    }

                    adapter.notifyDataSetChanged();
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
     /*   carList.clear();
        Car car = new Car(10, "大众" , "鲁F56123", "http://180.76.243.205:8111/images/car_brand/aerfaluomiou.png", 1);
        carList.add(car);
        for (int i = 0; i < 8; i++) {
            Car ca1r = new Car(i, "大众" + i, "鲁F56123", "http://180.76.243.205:8111/images/car_brand/aerfaluomiou.png", 2);
            carList.add(ca1r);
        }

        adapter.notifyDataSetChanged();*/
    }

    private void initView() {
        addCarView = (TextView) findViewById(R.id.add_car_button);
        listView = (SwipeMenuListView) findViewById(R.id.car_list);

        adapter = new ListAdapter();
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {//点击查看车辆信息
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Car car = carList.get(position);
                int uesrCarId = car.getUserCarId();
                Log.e(TAG, "onItemClick:------- " + uesrCarId);
                Intent intent = new Intent(getApplicationContext(), CarInfoActivity.class);
                intent.putExtra("USERCARID" ,uesrCarId);
                intent.putExtra("FROM",1);
                intent.putExtra("CANCLICK",1);
                startActivity(intent);
            }
        });
        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                SwipeMenuItem deleteItem = new SwipeMenuItem(getApplicationContext());
                SwipeMenuItem morenItem = new SwipeMenuItem(getApplicationContext());
                morenItem.setBackground(R.color.c4);
                morenItem.setWidth(AndroidUtilities.dp(260));
                morenItem.setIcon(R.drawable.ic_setmoren);
                menu.addMenuItem(morenItem);

                deleteItem.setBackground(R.color.theme_primary);
                deleteItem.setWidth(AndroidUtilities.dp(250));
                deleteItem.setIcon(R.drawable.ic_delete_text);
                menu.addMenuItem(deleteItem);

            }
        };
        listView.setMenuCreator(creator);
        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                Car car = carList.get(position);
                int userCarId = car.getUserCarId();
                switch (index){
                    case 0:             //设为默认
                        Log.e(TAG, "onMenuItemClick: " + userCarId);
                        setMorenCar(userCarId);
                        break;
                    case 1:         //clear
                        Log.e(TAG, "onMenuItemClick: " + userCarId);
                        deleteCar(userCarId);
                        break;
                }
                return false;
            }
        });

        RxViewAction.clickNoDouble(addCarView)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Intent intent = new Intent(getApplicationContext(), CarInfoActivity.class);
                        intent.putExtra("CANCLICK",0);
                        intent.putExtra("FROM",3);
                        startActivityForResult(intent,CARMANAMGER_RESULT);
                    }
                });
    }

    private void deleteCar(int userCarId) {
        int userId = new DbConfig().getId();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userId",userId);
            jsonObject.put("userCarId",userCarId);
        } catch (JSONException e) {
        }
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "deleteCar");
        params.addBodyParameter("reqJson",jsonObject.toString());
        String token = new DbConfig().getToken();
        params.addParameter("token",token);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                JSONObject jsonObject1 = null;
                try {
                    jsonObject1 = new JSONObject(result);
                    String status = jsonObject1.getString("status");
                    String msg = jsonObject1.getString("msg");
                    Toast.makeText(CarManagerActivity.this,msg , Toast.LENGTH_SHORT).show();
                    if (status.equals("1")){
                        initDataFromService();//修改完成后更新数据
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

    private void setMorenCar(int userCarId) {
        User user = new DbConfig().getUser();
        int userId = user.getId();
        user.setCarId(userCarId);
        saveUserIntoDb(user);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userId",userId);
            jsonObject.put("userCarId",userCarId);
        } catch (JSONException e) {
        }
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "changeDefaultCar");
        params.addBodyParameter("reqJson",jsonObject.toString());
        String token = new DbConfig().getToken();
        params.addParameter("token",token);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                JSONObject jsonObject1 = null;
                try {
                    jsonObject1 = new JSONObject(result);
                    String status = jsonObject1.getString("status");
                    String msg = jsonObject1.getString("msg");
                    Toast.makeText(CarManagerActivity.this,msg , Toast.LENGTH_SHORT).show();
                    if (status.equals("1")){
                        initDataFromService();//修改完成后更新数据
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

    private void saveUserIntoDb(User user) {
        DbConfig dbConfig = new DbConfig();
        DbManager db = dbConfig.getDbManager();
        try {
            db.saveOrUpdate(user);
        } catch (DbException e) {

        }
    }

    /**
     * * listView的Adapter适配器
    */
    class ListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return carList.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null){
                convertView = new CarInfoCell(CarManagerActivity.this);
            }
            //加载item布局
            CarInfoCell cell = (CarInfoCell) convertView;

            //获取每个用户实例
            Car car = carList.get(position);

            //赋值
            cell.setValue(car.getCarIcon(),car.getCarName(),car.getCarNumber(),car.moren);
            return convertView;
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra(MyFragment.FROM_FRAGMENT,fromFragment);
        startActivity(intent);
    }
    /*
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
           if (fromFragment.equals("HOMEFRAGMENT")){   //从homefragment进来
               Intent intent = new Intent();
               setResult(MainActivity.HOMEFRAGMENT_RESULT,intent);
               finish();
           }else {//从MyFragment进来
               Intent intent = new Intent();
               setResult(MainActivity.MYFRAGMENT_RESULT,intent);
               finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }*/

}
