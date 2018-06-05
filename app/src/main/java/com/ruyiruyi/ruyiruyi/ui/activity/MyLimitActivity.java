package com.ruyiruyi.ruyiruyi.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.ruyiruyi.ruyiruyi.R;
import com.ruyiruyi.ruyiruyi.db.DbConfig;
import com.ruyiruyi.ruyiruyi.ui.activity.base.RyBaseActivity;
import com.ruyiruyi.ruyiruyi.ui.multiType.RechargeMoney;
import com.ruyiruyi.ruyiruyi.ui.multiType.RechargeMoneyViewBinder;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;
import com.ruyiruyi.rylibrary.cell.ActionBar;

import java.util.ArrayList;
import java.util.List;

import me.drakeet.multitype.MultiTypeAdapter;
import rx.functions.Action1;

import static me.drakeet.multitype.MultiTypeAsserts.assertAllRegistered;
import static me.drakeet.multitype.MultiTypeAsserts.assertHasTheSameAdapter;

public class MyLimitActivity extends RyBaseActivity implements RechargeMoneyViewBinder.OnMoneyClick {
    private ActionBar actionBar;
    private RecyclerView listView;
    private List<Object> items = new ArrayList<>();
    private MultiTypeAdapter adapter;
    public List<RechargeMoney> rechargeMoneyList;
    private EditText otherMoneyEdit;
    public boolean showOtherMoneyView = false;
    private FrameLayout weixinLayout;
    private FrameLayout zhifubaoLayout;
    private ImageView weixinImage;
    private ImageView zhifubaoImageView;
    public int payType = 0;  //0是微信支付 1是支付宝支付

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_limit, R.id.my_action);
        actionBar = (ActionBar) findViewById(R.id.my_action);
        actionBar.setTitle("我的额度");
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
        rechargeMoneyList = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            rechargeMoneyList.add(new RechargeMoney(i, i + "00", i + "0", false, 0));
        }
        rechargeMoneyList.add(new RechargeMoney(5, "其他", "", true, 1));
        showOtherMoneyView = true;
        initView();
        initData();

    }

    private void initData() {
        items.clear();
        for (int i = 0; i < rechargeMoneyList.size(); i++) {
            items.add(rechargeMoneyList.get(i));
        }
        assertAllRegistered(adapter, items);
        adapter.notifyDataSetChanged();
    }

    private void initView() {
        weixinLayout = (FrameLayout) findViewById(R.id.weixin_layout);
        zhifubaoLayout = (FrameLayout) findViewById(R.id.zhifubao_layout);
        weixinImage = (ImageView) findViewById(R.id.weixin_image);
        zhifubaoImageView = (ImageView) findViewById(R.id.zhifubao_image);
        initPayLayout();

        otherMoneyEdit = (EditText) findViewById(R.id.other_money_edit);
        initOtherMoneyView();
        listView = (RecyclerView) findViewById(R.id.money_grid_list);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        listView.setLayoutManager(gridLayoutManager);
        adapter = new MultiTypeAdapter(items);

        register();
        listView.setAdapter(adapter);
        assertHasTheSameAdapter(listView, adapter);
        RxViewAction.clickNoDouble(weixinLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        payType = 0;
                        initPayLayout();
                    }
                });
        RxViewAction.clickNoDouble(zhifubaoLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        payType = 1;
                        initPayLayout();
                    }
                });
    }

    private void initPayLayout() {
        weixinImage.setImageResource(payType == 0 ? R.drawable.ic_check : R.drawable.ic_check_no);
        zhifubaoImageView.setImageResource(payType == 0 ? R.drawable.ic_check_no : R.drawable.ic_check);
    }

    private void initOtherMoneyView() {
        otherMoneyEdit.setVisibility(showOtherMoneyView ? View.VISIBLE : View.GONE);
    }

    private void register() {
        RechargeMoneyViewBinder rechargeMoneyViewBinder = new RechargeMoneyViewBinder();
        rechargeMoneyViewBinder.setListener(this);
        adapter.register(RechargeMoney.class, rechargeMoneyViewBinder);
    }

    @Override
    public void onMonyeClickListener(int moneyId) {
        for (int i = 0; i < rechargeMoneyList.size(); i++) {
            if (rechargeMoneyList.get(i).getMoneyId() == moneyId) {
                rechargeMoneyList.get(i).setIscheck(true);
            } else {
                rechargeMoneyList.get(i).setIscheck(false);
            }
        }
        showOtherMoneyView = rechargeMoneyList.get(rechargeMoneyList.size() - 1).ischeck;
        initOtherMoneyView();
        initData();
    }
}
