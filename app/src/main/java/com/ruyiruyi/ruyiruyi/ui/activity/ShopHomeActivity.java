package com.ruyiruyi.ruyiruyi.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.ruyiruyi.ruyiruyi.R;
import com.ruyiruyi.ruyiruyi.ui.model.ServiceType;
import com.ruyiruyi.ruyiruyi.ui.multiType.ShopGoods;
import com.ruyiruyi.ruyiruyi.ui.multiType.ShopGoodsViewBinder;
import com.ruyiruyi.ruyiruyi.ui.multiType.ShopInfo;
import com.ruyiruyi.ruyiruyi.ui.multiType.ShopInfoViewBinder;
import com.ruyiruyi.rylibrary.base.BaseActivity;
import com.ruyiruyi.rylibrary.cell.ActionBar;

import java.util.ArrayList;
import java.util.List;

import me.drakeet.multitype.MultiTypeAdapter;

import static me.drakeet.multitype.MultiTypeAsserts.assertAllRegistered;
import static me.drakeet.multitype.MultiTypeAsserts.assertHasTheSameAdapter;

public class ShopHomeActivity extends BaseActivity {
    private ActionBar actionBar;
    private List<Object> items = new ArrayList<>();
    private MultiTypeAdapter adapter;
    private RecyclerView listView;
    public List<ServiceType> typeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_home,R.id.my_action);
        actionBar = (ActionBar) findViewById(R.id.my_action);
        actionBar.setTitle("门店首页");;
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
        initdata();

    }

    private void initdata() {
        typeList = new ArrayList<>();
        typeList.add(new ServiceType("汽车保养",0xffff3f7e));
        typeList.add(new ServiceType("美容清洗",0xff00a600));
        typeList.add(new ServiceType("安装",0xff007cf0));
        typeList.add(new ServiceType("轮胎",0xffb600ea));
        List<String> imageList = new ArrayList<>();

        imageList.add("http://180.76.243.205:8111/images/Advertisement/cxwy.png");
        imageList.add("http://180.76.243.205:8111/images/Advertisement/cxwy1000.png");
        imageList.add("http://180.76.243.205:8111/images/Advertisement/cxwy1000.png");

        items.clear();
        items.add(new ShopInfo(imageList,typeList));
        items.add(new ShopGoods());
        assertAllRegistered(adapter,items);
        adapter.notifyDataSetChanged();
    }

    private void initView() {
        listView = (RecyclerView) findViewById(R.id.shop_home_listview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        listView.setLayoutManager(linearLayoutManager);
        adapter = new MultiTypeAdapter(items);
        register();

        listView.setAdapter(adapter);
        assertHasTheSameAdapter(listView, adapter);

    }

    private void register() {
        adapter.register(ShopInfo.class,new ShopInfoViewBinder(this));
        ShopGoodsViewBinder shopGoodsViewBinder = new ShopGoodsViewBinder(this,getSupportFragmentManager());
        adapter.register(ShopGoods.class, shopGoodsViewBinder);

    }
}
