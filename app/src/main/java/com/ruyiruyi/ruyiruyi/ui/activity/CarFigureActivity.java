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

import static me.drakeet.multitype.MultiTypeAsserts.assertAllRegistered;
import static me.drakeet.multitype.MultiTypeAsserts.assertHasTheSameAdapter;

public class CarFigureActivity extends BaseActivity implements TireFigureViewBinder.OnFigureItemClick {
    private ActionBar actionBar;
    private RecyclerView listView;
    private TextView nextButton;
    private List<Object> items = new ArrayList<>();
    private MultiTypeAdapter adapter;
    private List<TireFigure> tireFigureList ;

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
        getData();

        initView();
        initData();
    }

    private void getData() {
        tireFigureList = new ArrayList<>();
        TireFigure tireFigure = new TireFigure(false, 0,"经济运动型", "http://180.76.243.205:8111/images/flgure/9F5CD167-866A-C9B3-4406-7E0E36A4D003.jpg",
                "http://180.76.243.205:8111/images/flgure/9F5CD167-866A-C9B3-4406-7E0E36A4D003.jpg",
                "http://180.76.243.205:8111/images/flgure/112E28A5-68FD-B758-16CA-E1C7F67939C6.jpg",
                "路酷泽品牌轮胎是山东新大陆橡胶科技有限公司针对高端乘用车推出的拳头产品！如驿如意平台结合中国车主用车习惯独家推出“一次换轮胎 终身免费开”的全新升级服务！");
        tireFigureList.add(tireFigure);
        for (int i = 0; i < 6; i++) {
            TireFigure tireFigure1 = new TireFigure(false, 0,"经济运动型" + i, "http://180.76.243.205:8111/images/flgure/9F5CD167-866A-C9B3-4406-7E0E36A4D003.jpg",
                    "http://180.76.243.205:8111/images/flgure/9F5CD167-866A-C9B3-4406-7E0E36A4D003.jpg",
                    "http://180.76.243.205:8111/images/flgure/112E28A5-68FD-B758-16CA-E1C7F67939C6.jpg",
                    "路酷泽品牌轮胎是山东新大陆橡胶科技有限公司针对高端乘用车推出的拳头产品！如驿如意平台结合中国车主用车习惯独家推出“一次换轮胎 终身免费开”的全新升级服务！");
            tireFigureList.add(tireFigure1);
        }
    }

    private void initData() {
        items.clear();
        items.add(new TitleStr("轮胎花纹类别选择"));

        for (int i = 0; i < tireFigureList.size(); i++) {
            items.add(tireFigureList.get(i));
        }
        assertAllRegistered(adapter,items);
        adapter.notifyDataSetChanged();
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
        TireFigureViewBinder tireFigureViewBinder = new TireFigureViewBinder(this);
        tireFigureViewBinder.setListener(this);
        adapter.register(TireFigure.class, tireFigureViewBinder);
    }

    @Override
    public void onFigureClickListener(String name) {
        for (int i = 0; i < tireFigureList.size(); i++) {
            if (tireFigureList.get(i).getTitleStr().equals(name)) {

                tireFigureList.get(i).setCheck(!tireFigureList.get(i).isCheck);
            }else {
                tireFigureList.get(i).setCheck(false);
            }
        }
        initData();
    }
}
