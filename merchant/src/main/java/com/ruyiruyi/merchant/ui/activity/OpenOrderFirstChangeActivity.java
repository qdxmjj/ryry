package com.ruyiruyi.merchant.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ruyiruyi.merchant.R;
import com.ruyiruyi.rylibrary.base.BaseActivity;
import com.ruyiruyi.rylibrary.cell.ActionBar;

public class OpenOrderFirstChangeActivity extends BaseActivity {
    private TextView service_num;
    private TextView mid_title;
    private LinearLayout ll_mid_a;
    private LinearLayout ll_mid_b;
    private LinearLayout ll_pic_luntai;
    private TextView code_a_change;
    private TextView code_b_change;
    private TextView code_c_change;
    private TextView code_d_change;
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
        ll_pic_luntai = findViewById(R.id.ll_pic_luntai);
        code_a_change = findViewById(R.id.code_a_change);
        code_b_change = findViewById(R.id.code_b_change);
        code_c_change = findViewById(R.id.code_c_change);
        code_d_change = findViewById(R.id.code_d_change);
        tv_bottom_a = findViewById(R.id.tv_bottom_a);
        tv_bottom_b = findViewById(R.id.tv_bottom_b);
        tv_bottom_c = findViewById(R.id.tv_bottom_c);
        service_num.setText("更换数量:");
        mid_title.setText("轮胎编码");
        ll_mid_a.setVisibility(View.VISIBLE);
        ll_mid_b.setVisibility(View.GONE);
        ll_pic_luntai.setVisibility(View.GONE);
        code_a_change.setVisibility(View.GONE);
        code_b_change.setVisibility(View.GONE);
        code_c_change.setVisibility(View.GONE);
        code_d_change.setVisibility(View.GONE);
        tv_bottom_a.setText("确认服务");
        tv_bottom_b.setText("拒绝服务");
        tv_bottom_c.setText("客户自提");


        initView();

    }

    private void initView() {

    }
}
