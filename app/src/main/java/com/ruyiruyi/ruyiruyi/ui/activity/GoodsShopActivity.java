package com.ruyiruyi.ruyiruyi.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.ruyiruyi.ruyiruyi.R;
import com.ruyiruyi.ruyiruyi.ui.activity.base.RYBaseActivity;
import com.ruyiruyi.ruyiruyi.ui.multiType.GoodsShop;
import com.ruyiruyi.ruyiruyi.ui.multiType.GoodsShopViewBinder;
import com.ruyiruyi.rylibrary.cell.ActionBar;

import java.util.ArrayList;
import java.util.List;

import me.drakeet.multitype.MultiTypeAdapter;

import static me.drakeet.multitype.MultiTypeAsserts.assertAllRegistered;
import static me.drakeet.multitype.MultiTypeAsserts.assertHasTheSameAdapter;

public class  GoodsShopActivity extends RYBaseActivity {
    private ActionBar actionBar;
    public static String CLASS_NAME = "CLASS_NAME";
    public static String CLASS_ID = "CLASS_ID";
    private String className;
    private int classId;
    private RecyclerView listView;
    private List<Object> items = new ArrayList<>();
    private MultiTypeAdapter adapter;
    public List<GoodsShop> goodsShopList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_shop);
        actionBar = (ActionBar) findViewById(R.id.my_action);
        //actionBar.setTitle("测试");;
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

        goodsShopList = new ArrayList<>();
        Intent intent = getIntent();
        className = intent.getStringExtra(CLASS_NAME);
        actionBar.setTitle(className);;
        classId = intent.getIntExtra(CLASS_ID,0);

        initView();

        initDataFromService();


    }

    private void intiData() {
        items.clear();
        for (int i = 0; i < goodsShopList.size(); i++) {
            items.add(goodsShopList.get(i));
        }
        assertAllRegistered(adapter,items);
        adapter.notifyDataSetChanged();
    }

    private void initDataFromService() {

        goodsShopList.clear();
        for (int i = 0; i < 10; i++) {
            GoodsShop goodsShop = new GoodsShop(i, "", "风油精" + i, "20.2", "晴天天安数码城" + i, "200.0");
            goodsShopList.add(goodsShop);
            intiData();
        }
    }

    private void initView() {
        listView = (RecyclerView) findViewById(R.id.goods_activity_listview);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        listView.setLayoutManager(linearLayoutManager);
        adapter = new MultiTypeAdapter(items);
        register();

        listView.setAdapter(adapter);
        assertHasTheSameAdapter(listView, adapter);
    }

    private void register() {
        adapter.register(GoodsShop.class,new GoodsShopViewBinder(this));
    }
}
