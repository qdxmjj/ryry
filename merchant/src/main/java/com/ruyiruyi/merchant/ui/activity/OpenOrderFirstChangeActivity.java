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
    private TextView mid_title;
    private LinearLayout ll_mid_a;
    private LinearLayout ll_mid_b;
    private LinearLayout ll_pic_luntai;
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
        mid_title = findViewById(R.id.mid_title);
        ll_mid_a = findViewById(R.id.ll_mid_a);
        ll_mid_b = findViewById(R.id.ll_mid_b);
        ll_pic_luntai = findViewById(R.id.ll_pic_luntai);
        tv_bottom_a = findViewById(R.id.tv_bottom_a);
        tv_bottom_b = findViewById(R.id.tv_bottom_b);
        tv_bottom_c = findViewById(R.id.tv_bottom_c);
        mid_title.setText("轮胎编码");
        ll_mid_a.setVisibility(View.VISIBLE);
        ll_mid_b.setVisibility(View.GONE);
        ll_pic_luntai.setVisibility(View.GONE);
        tv_bottom_a.setText("确认服务");
        tv_bottom_b.setText("拒绝服务");
        tv_bottom_c.setText("客户自提");


        initView();

    }

    private void initView() {

    }
}
