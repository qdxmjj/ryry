package com.ruyiruyi.ruyiruyi.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.ruyiruyi.ruyiruyi.R;
import com.ruyiruyi.ruyiruyi.ui.multiType.Promotion;
import com.ruyiruyi.ruyiruyi.ui.multiType.PromotionHasperson;
import com.ruyiruyi.ruyiruyi.ui.multiType.PromotionHaspersonViewBinder;
import com.ruyiruyi.ruyiruyi.ui.multiType.PromotionNoperson;
import com.ruyiruyi.ruyiruyi.ui.multiType.PromotionNopersonViewBinder;
import com.ruyiruyi.ruyiruyi.ui.multiType.PromotionViewBinder;
import com.ruyiruyi.rylibrary.base.BaseActivity;
import com.ruyiruyi.rylibrary.cell.ActionBar;

import java.util.ArrayList;
import java.util.List;

import me.drakeet.multitype.MultiTypeAdapter;

import static me.drakeet.multitype.MultiTypeAsserts.assertAllRegistered;
import static me.drakeet.multitype.MultiTypeAsserts.assertHasTheSameAdapter;

public class PromotionActivity extends BaseActivity {

    private ActionBar actionBar;
    private RecyclerView mRlv;
    private MultiTypeAdapter multiTypeAdapter;
    private List<Object> items = new ArrayList<>();
    private List<PromotionHasperson> personList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promotion);
        actionBar = (ActionBar) findViewById(R.id.my_acbar);
        actionBar.setTitle("推广有礼");
        actionBar.setRightImage(R.drawable.ic_erweima);
        actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(int var1) {
                switch ((var1)) {
                    case -1:
                        onBackPressed();
                        break;
                    case -2:
                        Toast.makeText(PromotionActivity.this, "二维码", Toast.LENGTH_SHORT).show();
                        break;
                }
            }


        });

        initView();
        initData();
    }

    private void initData() {
        //下载数据
        //缺接口暂用假数据
        getFakeData();


        //下载完成
        items.clear();
        items.add(new Promotion("SADDD", "下载的推广简介", "下载的邀请方式简介"));
        if (personList == null || personList.size() == 0) {
            items.add(new PromotionNoperson());
        } else {
            items.addAll(personList);
        }
        assertAllRegistered(multiTypeAdapter, items);
        multiTypeAdapter.notifyDataSetChanged();

    }

    private void getFakeData() {
        for (int i = 0; i < 6; i++) {
            PromotionHasperson bean = new PromotionHasperson();
            bean.setUserPhone("123****8905");
            bean.setUserState("已邀请");
            bean.setUserTime("2018-05-21");
            if (i == 0) {
                bean.setFirst(true);
            } else {
                bean.setFirst(false);
            }
            personList.add(bean);
        }
    }

    private void initView() {
        mRlv = findViewById(R.id.my_rlv);
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRlv.setLayoutManager(manager);
        multiTypeAdapter = new MultiTypeAdapter(items);
        multiTypeAdapter.register(Promotion.class, new PromotionViewBinder(this));
        multiTypeAdapter.register(PromotionHasperson.class, new PromotionHaspersonViewBinder());
        multiTypeAdapter.register(PromotionNoperson.class, new PromotionNopersonViewBinder());
        mRlv.setAdapter(multiTypeAdapter);
        assertHasTheSameAdapter(mRlv, multiTypeAdapter);

        personList = new ArrayList<>();
    }
}
