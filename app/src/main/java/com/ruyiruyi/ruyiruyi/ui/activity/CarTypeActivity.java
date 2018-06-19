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
import com.ruyiruyi.rylibrary.cell.ActionBar;

import org.xutils.DbManager;
import org.xutils.ex.DbException;

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
    private MultiTypeAdapter adapter;
    private int vercicleid;
    public static int currentType = 0;
    public static String currentPaiLiang ;
    public static String currentYear ;
    public static boolean ishave = false;

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
        Intent intent = getIntent();
        vercicleid = intent.getIntExtra("VERCICLEID",0);

        initView();
        currentType = 0;
        initData();
    }

    private void initData() {
        DbManager db = new DbConfig(this).getDbManager();
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
        initData();
    }

    @Override
    public void onCarPaiLiangLayoutClickListener(int type) {
        currentType = type;
        initData();
    }

    @Override
    public void onCarTitleItemClikcListener(String title) {
        if (currentType == 0){
            currentPaiLiang = title;
        }else if (currentType == 1){
            currentYear = title;
        }else {
            DbManager db = new DbConfig(this).getDbManager();
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

            }
        }
        currentType += 1;
        initData();
    }
}
