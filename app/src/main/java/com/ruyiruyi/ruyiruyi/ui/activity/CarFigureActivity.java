package com.ruyiruyi.ruyiruyi.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.ruyiruyi.ruyiruyi.R;
import com.ruyiruyi.ruyiruyi.db.model.CarTireInfo;
import com.ruyiruyi.ruyiruyi.ui.multiType.TireFigure;
import com.ruyiruyi.ruyiruyi.ui.multiType.TireFigureViewBinder;
import com.ruyiruyi.ruyiruyi.ui.multiType.TitleStr;
import com.ruyiruyi.ruyiruyi.ui.multiType.TitleStrViewBinder;
import com.ruyiruyi.rylibrary.base.BaseActivity;
import com.ruyiruyi.rylibrary.cell.ActionBar;

import java.util.ArrayList;
import java.util.List;

import me.drakeet.multitype.MultiTypeAdapter;

import static me.drakeet.multitype.MultiTypeAsserts.assertHasTheSameAdapter;

public class CarFigureActivity extends BaseActivity {
    private ActionBar actionBar;
    private RecyclerView listView;
    private TextView nextButton;
    private List<Object> items = new ArrayList<>();
    private MultiTypeAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_figure,R.id.my_action);
        actionBar = (ActionBar) findViewById(R.id.my_action);
        actionBar.setTitle("选择花纹");;
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
        initData();
    }

    private void initData() {
        items.clear();
        items.add(new TitleStr("轮胎花纹类别选择"));
        for (int i = 0; i < 6; i++) {

        }

    }

    private void initView() {
        listView = (RecyclerView) findViewById(R.id.car_figure_list);
        nextButton = (TextView) findViewById(R.id.car_figure_button);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        listView.setLayoutManager(linearLayoutManager);
        adapter = new MultiTypeAdapter(items);
        register();

        listView.setAdapter(adapter);
        assertHasTheSameAdapter(listView, adapter);
    }

    private void register() {
        adapter.register(TitleStr.class,new TitleStrViewBinder());
        adapter.register(TireFigure.class,new TireFigureViewBinder());
    }
}
