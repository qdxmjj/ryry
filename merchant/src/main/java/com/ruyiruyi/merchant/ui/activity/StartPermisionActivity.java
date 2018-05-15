package com.ruyiruyi.merchant.ui.activity;

import android.Manifest;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ruyiruyi.merchant.R;
import com.ruyiruyi.rylibrary.base.BaseActivity;

public class StartPermisionActivity extends BaseActivity {
    private static final int REQUEST_CODE = 0 ;//请求码
    static final String[] PERMISSIONS = new String[]{
            Manifest.permission.CAMERA ,
            Manifest.permission.READ_EXTERNAL_STORAGE ,
            Manifest.permission.WRITE_EXTERNAL_STORAGE ,
            Manifest.permission.ACCESS_COARSE_LOCATION ,
            Manifest.permission.ACCESS_FINE_LOCATION
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_permision);
    }
}
