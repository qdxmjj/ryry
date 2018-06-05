package com.ruyiruyi.ruyiruyi.ui.activity;

import android.os.Bundle;
import android.view.LayoutInflater;

import com.ruyiruyi.ruyiruyi.R;
import com.ruyiruyi.ruyiruyi.ui.activity.base.RyBaseActivity;
import com.ruyiruyi.ruyiruyi.ui.cell.ShopChooseCell;
import com.ruyiruyi.ruyiruyi.ui.model.StoreType;
import com.ruyiruyi.rylibrary.cell.ActionBar;

import java.util.ArrayList;
import java.util.List;

public class TireRepairActivity extends RyBaseActivity {
    private ActionBar actionBar;
    private ShopChooseCell shopChooseView;
    public List<StoreType> typeList;
    private LayoutInflater mInflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tire_repair,R.id.my_action);
        actionBar = (ActionBar) findViewById(R.id.my_action);
        actionBar.setTitle("轮胎修补");;
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

        initData();
        initView();

    }

    private void initData() {
        typeList = new ArrayList<>();
        typeList.add(new StoreType(1,"快修店"));
        typeList.add(new StoreType(2,"4s"));
        typeList.add(new StoreType(3,"轮胎"));
        typeList.add(new StoreType(4,"轮胎"));

    }

    private void initView() {
        mInflater = LayoutInflater.from(this);
        shopChooseView = (ShopChooseCell) findViewById(R.id.shop_choose_cell);

       /* shopChooseView.setValue("青岛汽车总店","http://180.76.243.205:8111/images/flgure/970FB91D-D680-437D-606D-0AFAEC4E5F10.jpg",
                "青岛市城阳区天安数码城","15km",typeList,mInflater);*/

    }
}
