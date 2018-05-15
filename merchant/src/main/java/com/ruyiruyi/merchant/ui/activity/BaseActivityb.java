package com.ruyiruyi.merchant.ui.activity;

import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

import com.ruyiruyi.merchant.R;
import com.ruyiruyi.rylibrary.base.BaseActivity;

import org.xutils.x;

public class BaseActivityb extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除标题栏
//        getSupportActionBar().hide();
        //去除状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_base_activityb);
        x.view().inject(this);
    }
}
