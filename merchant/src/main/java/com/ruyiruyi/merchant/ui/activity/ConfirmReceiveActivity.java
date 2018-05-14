package com.ruyiruyi.merchant.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ruyiruyi.merchant.R;
import com.ruyiruyi.rylibrary.base.BaseActivity;
import com.ruyiruyi.rylibrary.cell.ActionBar;

public class ConfirmReceiveActivity extends BaseActivity {

    private ActionBar mActionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_receive);
        mActionBar = (ActionBar) findViewById(R.id.acbar_);
        mActionBar.setTitle("确认收货");
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
