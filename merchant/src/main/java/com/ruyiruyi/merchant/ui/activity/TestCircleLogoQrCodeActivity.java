package com.ruyiruyi.merchant.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.ruyiruyi.merchant.R;
import com.ruyiruyi.rylibrary.base.BaseActivity;

public class TestCircleLogoQrCodeActivity extends BaseActivity {
    private ImageView qr_code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_circle_logo_qr_code);

        initView();
        initData();
        setData();

    }

    private void setData() {
        
    }

    private void initData() {

    }

    private void initView() {
        qr_code = findViewById(R.id.qr_code);
    }
}
