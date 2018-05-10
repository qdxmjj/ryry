package com.ruyiruyi.merchant.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ruyiruyi.merchant.R;
import com.ruyiruyi.rylibrary.base.BaseActivity;
import com.ruyiruyi.rylibrary.cell.ActionBar;

public class OpenOrderXiuBuActivity extends BaseActivity {
    private TextView service_num;
    private TextView mid_title;
    private LinearLayout ll_mid_a;
    private LinearLayout ll_mid_b;
    private TextView tv_bottom_a;
    private TextView tv_bottom_b;
    private TextView tv_bottom_c;

    private ActionBar mActionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_open_order_free_change);
        mActionBar = (ActionBar) findViewById(R.id.open_order_freechange_acbar);
        mActionBar.setTitle("订单确认");
        mActionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(int var1) {
                switch (var1) {
                    case -1:
                        onBackPressed();
                        break;
                }
            }
        });
        service_num = findViewById(R.id.service_num);
        mid_title = findViewById(R.id.mid_title);
        ll_mid_a = findViewById(R.id.ll_mid_a);
        ll_mid_b = findViewById(R.id.ll_mid_b);
        tv_bottom_a = findViewById(R.id.tv_bottom_a);
        tv_bottom_b = findViewById(R.id.tv_bottom_b);
        tv_bottom_c = findViewById(R.id.tv_bottom_c);
        service_num.setText("修补数量:");
        mid_title.setText("补胎编号");
        ll_mid_a.setVisibility(View.GONE);
        ll_mid_b.setVisibility(View.VISIBLE);
        tv_bottom_a.setText("确认服务");
        tv_bottom_b.setText("补差服务");
        tv_bottom_c.setText("拒绝服务");


        initView();

    }

    private void initView() {

    }
}
