package com.ruyiruyi.ruyiruyi.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.ruyiruyi.ruyiruyi.R;
import com.ruyiruyi.ruyiruyi.ui.multiType.CreditLimit;
import com.ruyiruyi.ruyiruyi.ui.multiType.CreditLimitViewBinder;
import com.ruyiruyi.rylibrary.base.BaseActivity;
import com.ruyiruyi.rylibrary.cell.ActionBar;

import java.util.ArrayList;
import java.util.List;

import me.drakeet.multitype.MultiTypeAdapter;

import static me.drakeet.multitype.MultiTypeAsserts.assertAllRegistered;
import static me.drakeet.multitype.MultiTypeAsserts.assertHasTheSameAdapter;

public class CreditLimitActivity extends BaseActivity {
    private ActionBar actionBar;
    private RecyclerView listView;
    private List<Object> items = new ArrayList<>();
    private MultiTypeAdapter adapter;
    public List<CreditLimit> creditLimitList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit_limit,R.id.my_action);
        actionBar = (ActionBar) findViewById(R.id.my_action);
        actionBar.setTitle("信用额度");;
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
        creditLimitList = new ArrayList<>();


        initView();

        for (int i = 0; i < 10; i++) {
            creditLimitList.add(new CreditLimit("http://180.76.243.205:8111/images/car_brand/baoma.png","奥迪" + i,"鲁B 2356" + i,"500" + i,"300" + i));
        }
        initData();
    }

    private void initData() {
        items.clear();
        for (int i = 0; i < creditLimitList.size(); i++) {
            items.add(creditLimitList.get(i));
        }
        assertAllRegistered(adapter,items);
        adapter.notifyDataSetChanged();
    }

    private void initView() {
        listView = (RecyclerView) findViewById(R.id.credit_limit_listview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        listView.setLayoutManager(linearLayoutManager);
        adapter = new MultiTypeAdapter(items);
        register();

        listView.setAdapter(adapter);
        assertHasTheSameAdapter(listView, adapter);
    }

    private void register() {
        adapter.register(CreditLimit.class,new CreditLimitViewBinder(this));
    }
}
