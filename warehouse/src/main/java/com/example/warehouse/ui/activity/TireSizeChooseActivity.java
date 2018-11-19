package com.example.warehouse.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.example.warehouse.R;
import com.example.warehouse.ui.cell.WareActionBar;
import com.ruyiruyi.rylibrary.cell.ActionBar;

public class TireSizeChooseActivity extends AppCompatActivity {
    private WareActionBar actionBar;
    private RecyclerView kuanListView;
    private RecyclerView bianListView;
    private RecyclerView zhiListView;
    private RecyclerView huawenListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tire_size);

        actionBar = (WareActionBar) findViewById(R.id.my_action);
        actionBar.setTitle("选择轮胎规格");
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

        initView();
    }

    private void initView() {
        kuanListView = (RecyclerView) findViewById(R.id.kuan_listview);
        bianListView = (RecyclerView) findViewById(R.id.bian_listview);
        zhiListView = (RecyclerView) findViewById(R.id.zhi_listview);
        huawenListView = (RecyclerView) findViewById(R.id.huawen_listview);
    }
}
