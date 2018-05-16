package com.ruyiruyi.ruyiruyi.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ruyiruyi.ruyiruyi.R;
import com.ruyiruyi.rylibrary.base.BaseActivity;
import com.ruyiruyi.rylibrary.cell.ActionBar;

public class UserInfoActivity extends BaseActivity {

    private ActionBar mActionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        mActionBar = (ActionBar) findViewById(R.id.acbar_info);
        mActionBar.setTitle("个人信息");
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
