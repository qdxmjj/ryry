package com.ruyiruyi.ruyiruyi.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.ruyiruyi.ruyiruyi.R;
import com.ruyiruyi.ruyiruyi.ui.activity.base.RyBaseActivity;
import com.ruyiruyi.ruyiruyi.ui.multiType.Button;
import com.ruyiruyi.ruyiruyi.ui.multiType.ButtonViewBinder;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;
import com.ruyiruyi.rylibrary.cell.ActionBar;

import java.util.ArrayList;
import java.util.List;

import me.drakeet.multitype.MultiTypeAdapter;
import rx.functions.Action1;

import static me.drakeet.multitype.MultiTypeAsserts.assertHasTheSameAdapter;

public class CouponActivity extends RyBaseActivity {
    private ActionBar actionBar;
    private FrameLayout couponLayout;
    private FrameLayout oldCouponLayout;
    private View couponView;
    private View oldCouponView;
    public int couponType = 0;  //0是可用  1是历史
    private TextView couponText;
    private TextView oldCouponText;
    private RecyclerView listView;
    private List<Object> items = new ArrayList<>();
    private MultiTypeAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon);

        actionBar = (ActionBar) findViewById(R.id.my_action);
        actionBar.setTitle("我的优惠券");;
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
    }

    private void initView() {
        couponLayout = (FrameLayout) findViewById(R.id.coupon_layout);
        oldCouponLayout = (FrameLayout) findViewById(R.id.old_coupon_layout);
        couponView = (View) findViewById(R.id.coupon_view);
        oldCouponView = (View) findViewById(R.id.old_coupon_view);
        couponText = (TextView) findViewById(R.id.coupon_text);
        oldCouponText = (TextView) findViewById(R.id.old_coupon_text);
        listView = (RecyclerView) findViewById(R.id.coupon_listview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        listView.setLayoutManager(linearLayoutManager);
        adapter = new MultiTypeAdapter(items);
        register();

        listView.setAdapter(adapter);
        assertHasTheSameAdapter(listView, adapter);



        RxViewAction.clickNoDouble(couponLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        couponType = 0;
                        initCouponView();
                    }
                });
        RxViewAction.clickNoDouble(oldCouponView)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        couponType = 1;
                        initCouponView();
                    }
                });
    }

    private void register() {
        adapter.register(Button.class,new ButtonViewBinder());
    }

    private void initCouponView() {
        if (couponType == 0){
            couponView.setVisibility(View.VISIBLE);
            oldCouponView.setVisibility(View.GONE);
            couponText.setTextColor(getResources().getColor(R.color.theme_primary));
            oldCouponText.setTextColor(getResources().getColor(R.color.c6));
        }else {
            couponView.setVisibility(View.GONE);
            oldCouponView.setVisibility(View.VISIBLE);
            couponText.setTextColor(getResources().getColor(R.color.c6));
            oldCouponText.setTextColor(getResources().getColor(R.color.theme_primary));
        }
    }
}
