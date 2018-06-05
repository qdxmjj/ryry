package com.ruyiruyi.ruyiruyi.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.ruyiruyi.ruyiruyi.R;
import com.ruyiruyi.ruyiruyi.db.DbConfig;
import com.ruyiruyi.ruyiruyi.db.model.CarFactory;
import com.ruyiruyi.ruyiruyi.db.model.CarVerhicle;
import com.ruyiruyi.ruyiruyi.ui.activity.base.RyBaseActivity;
import com.ruyiruyi.ruyiruyi.ui.multiType.CarFactoryM;
import com.ruyiruyi.ruyiruyi.ui.multiType.CarFactoryViewBinder;
import com.ruyiruyi.ruyiruyi.ui.multiType.CarVersion;
import com.ruyiruyi.ruyiruyi.ui.multiType.CarVersionViewBinder;
import com.ruyiruyi.rylibrary.cell.ActionBar;

import org.xutils.DbManager;
import org.xutils.ex.DbException;

import java.util.ArrayList;
import java.util.List;

import me.drakeet.multitype.MultiTypeAdapter;

import static me.drakeet.multitype.MultiTypeAsserts.assertAllRegistered;
import static me.drakeet.multitype.MultiTypeAsserts.assertHasTheSameAdapter;

public class CarDemioActivity extends RyBaseActivity implements CarVersionViewBinder.OnCarVersionClick{

    private static final String TAG = CarDemioActivity.class.getSimpleName();
    private ActionBar actionBar;
    private RecyclerView listView;
    private List<Object> items = new ArrayList<>();
    private MultiTypeAdapter adapter;
    private Intent intent;
    private int carid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_demio,R.id.my_action);
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

        intent = getIntent();
        carid = intent.getIntExtra("CARID",0);
        initView();
        initData();
    }

    private void initData() {
        DbManager db = new DbConfig().getDbManager();
        List<CarFactoryM> carFactoryData = new ArrayList<>();
        List<CarVersion> carVersionData = new ArrayList<>();
        try {
            List<CarFactory> factoryList = db.selector(CarFactory.class)
                    .where("carbrandid", "=",carid)
                    .findAll();
            for (int i = 0; i < factoryList.size(); i++) {
                CarFactory carFactory = factoryList.get(i);
                carFactoryData.add(new CarFactoryM(carFactory.getId(),carFactory.getFactory()));
            }


            List<CarVerhicle> verhicleList = db.selector(CarVerhicle.class)
                    .where("carbrandid", "=", carid)
                    .findAll();
            for (int i = 0; i < verhicleList.size(); i++) {
                CarVerhicle carVerhicle = verhicleList.get(i);
                carVersionData.add(new CarVersion(carVerhicle.getId(),carVerhicle.getCarBrandId(),carVerhicle.getFactoryId(),carVerhicle.getCarVersion(),carVerhicle.getVerhicle()));
            }

            items.clear();

            for (int i = 0; i < carFactoryData.size(); i++) {
                items.add(carFactoryData.get(i));
                for (int j = 0; j < carVersionData.size(); j++) {
                    if (carFactoryData.get(i).getFactoryId() ==  carVersionData.get(j).getFactoryId()){
                        carVersionData.get(j).setCarFractory(carFactoryData.get(i).getCarFractory());
                        items.add(carVersionData.get(j));
                    }
                }
            }
            assertAllRegistered(adapter,items);
            adapter.notifyDataSetChanged();




        } catch (DbException e) {

        }

    }

    private void initView() {
        listView = (RecyclerView) findViewById(R.id.car_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        listView.setLayoutManager(linearLayoutManager);
        adapter = new MultiTypeAdapter(items);
        register();
        listView.setAdapter(adapter);
        assertHasTheSameAdapter(listView, adapter);

    }

    private void register() {

        adapter.register(CarFactoryM.class,new CarFactoryViewBinder());
        CarVersionViewBinder carVersionViewBinder = new CarVersionViewBinder();
        carVersionViewBinder.setListener(this);
        adapter.register(CarVersion.class, carVersionViewBinder);
    }

    @Override
    public void onCarVersionItemClickListener(int id) {
        Intent intent = new Intent(this, CarTypeActivity.class);
        intent.putExtra("VERCICLEID" ,id);
        startActivity(intent);
    }
}
