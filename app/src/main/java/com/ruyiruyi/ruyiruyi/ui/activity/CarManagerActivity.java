package com.ruyiruyi.ruyiruyi.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.ruyiruyi.ruyiruyi.R;
import com.ruyiruyi.ruyiruyi.db.DbConfig;
import com.ruyiruyi.ruyiruyi.ui.cell.CarInfoCell;
import com.ruyiruyi.ruyiruyi.ui.model.Car;
import com.ruyiruyi.ruyiruyi.utils.RequestUtils;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;
import com.ruyiruyi.rylibrary.base.BaseActivity;
import com.ruyiruyi.rylibrary.cell.ActionBar;
import com.ruyiruyi.rylibrary.utils.AndroidUtilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
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
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL_TEST + "getCarListByUserId");
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
                            int moren = data.getJSONObject(i).getInt("is_default");
                            String name = data.getJSONObject(i).getString("car_name");
                            String number = data.getJSONObject(i).getString("plat_number");
                            String icon = data.getJSONObject(i).getString("car_brand");
                            carList.add(new Car(car_id,name,number,icon,moren));
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
        carList.clear();
        Car car = new Car(10, "大众" , "鲁F56123", "http://180.76.243.205:8111/images/car_brand/aerfaluomiou.png", 1);
        carList.add(car);
        for (int i = 0; i < 8; i++) {
            Car ca1r = new Car(i, "大众" + i, "鲁F56123", "http://180.76.243.205:8111/images/car_brand/aerfaluomiou.png", 2);
            carList.add(ca1r);
        }

        adapter.notifyDataSetChanged();
    }

    private void initView() {
        addCarView = (TextView) findViewById(R.id.add_car_button);
        listView = (SwipeMenuListView) findViewById(R.id.car_list);

        adapter = new ListAdapter();
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Car car = carList.get(position);
                int carId = car.getCarId();
                Log.e(TAG, "onItemClick:------- " + carId);
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
                int carId = car.getCarId();
                switch (index){
                    case 0:
                        Log.e(TAG, "onMenuItemClick: " + carId);
                        break;
                    case 1:
                        Log.e(TAG, "onMenuItemClick: " + carId);
                        break;
                }
                return false;
            }
        });

        RxViewAction.clickNoDouble(addCarView)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        startActivity(new Intent(getApplicationContext(),CarInfoActivity.class));
                    }
                });
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
}
