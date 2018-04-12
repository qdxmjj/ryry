package com.ruyiruyi.merchant.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

import com.ruyiruyi.merchant.R;

import org.xutils.x;

public class BaseActivityb extends AppCompatActivity {

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
