package com.ruyiruyi.merchant.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.ruyiruyi.merchant.R;
import com.ruyiruyi.rylibrary.base.BaseActivity;

public class RegisterActivity extends BaseActivity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


    }

    public void registerclick(View view){
        switch (view.getId()){
            case R.id.img_back:

                finish();
                break;
        }
    }
}
