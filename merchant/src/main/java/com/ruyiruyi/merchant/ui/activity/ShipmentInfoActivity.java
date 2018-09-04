package com.ruyiruyi.merchant.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.ruyiruyi.merchant.R;
import com.ruyiruyi.rylibrary.base.BaseActivity;

public class ShipmentInfoActivity extends BaseActivity {

    private String orderNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipment_info);
        //获取传递数据
        Intent intent = getIntent();
        orderNo = intent.getStringExtra("orderNo");
        Toast.makeText(this, "orderNo = " + orderNo, Toast.LENGTH_SHORT).show();

        initView();
        bindView();
        initData();
        bindData();
    }

    private void bindData() {

    }

    private void bindView() {

    }

    private void initData() {

    }

    private void initView() {

    }
}
