package com.ruyiruyi.merchant.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ruyiruyi.merchant.R;
import com.ruyiruyi.rylibrary.base.BaseActivity;
import com.ruyiruyi.rylibrary.cell.ActionBar;

public class OrderXiangqingActivity extends BaseActivity {

    private ActionBar mActionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_xiangqing);

        mActionBar = (ActionBar) findViewById(R.id.order_xiangqing_acbar);
        mActionBar.setTitle("订单详情");
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
    }
}
