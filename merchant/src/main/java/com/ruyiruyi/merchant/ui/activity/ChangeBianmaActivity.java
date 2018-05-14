package com.ruyiruyi.merchant.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.ruyiruyi.merchant.R;
import com.ruyiruyi.rylibrary.base.BaseActivity;
import com.ruyiruyi.rylibrary.cell.ActionBar;

public class ChangeBianmaActivity extends BaseActivity {
    private ActionBar mActionBar;
    private FrameLayout fl_bianma_a_pic;
    private FrameLayout fl_bianma_b_pic;
    private FrameLayout fl_bianma_c_pic;
    private FrameLayout fl_bianma_d_pic;
    private boolean isOpen_a = false;
    private boolean isOpen_b = false;
    private boolean isOpen_c = false;
    private boolean isOpen_d = false;
    private ImageView bianma_a_change_pic;
    private ImageView bianma_b_change_pic;
    private ImageView bianma_c_change_pic;
    private ImageView bianma_d_change_pic;
    private TextView bianma_a;
    private String TAG = ChangeBianmaActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_bianma);
        mActionBar = (ActionBar) findViewById(R.id.bianma_acbar);
        mActionBar.setTitle("更改编码");
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

        initBefore();
        initView();
        Log.e(TAG, "onCreate:  bianma_a.getText() = " + bianma_a.getText());
    }

    private void initView() {
        bianma_a = findViewById(R.id.bianma_a);
    }

    private void initBefore() {
        fl_bianma_a_pic = findViewById(R.id.fl_bianma_a_pic);
        fl_bianma_b_pic = findViewById(R.id.fl_bianma_b_pic);
        fl_bianma_c_pic = findViewById(R.id.fl_bianma_c_pic);
        fl_bianma_d_pic = findViewById(R.id.fl_bianma_d_pic);
        bianma_a_change_pic = findViewById(R.id.bianma_a_change_pic);
        bianma_b_change_pic = findViewById(R.id.bianma_b_change_pic);
        bianma_c_change_pic = findViewById(R.id.bianma_c_change_pic);
        bianma_d_change_pic = findViewById(R.id.bianma_d_change_pic);
    }


    public void changeclick(View view) {//change点击事件 before
        switch (view.getId()) {
            case R.id.bianma_a_change://a
                if (isOpen_a) {
                    fl_bianma_a_pic.setVisibility(View.VISIBLE);
                    bianma_a_change_pic.setImageResource(R.drawable.down_huise_t);
                    isOpen_a = false;
                } else {
                    fl_bianma_a_pic.setVisibility(View.GONE);
                    bianma_a_change_pic.setImageResource(R.drawable.rignt_huise_t);
                    isOpen_a = true;
                }
                break;
            case R.id.bianma_b_change://b
                if (isOpen_b) {
                    fl_bianma_b_pic.setVisibility(View.VISIBLE);
                    bianma_b_change_pic.setImageResource(R.drawable.down_huise_t);
                    isOpen_b = false;
                } else {
                    fl_bianma_b_pic.setVisibility(View.GONE);
                    bianma_b_change_pic.setImageResource(R.drawable.rignt_huise_t);
                    isOpen_b = true;
                }
                break;
            case R.id.bianma_c_change://c
                if (isOpen_c) {
                    fl_bianma_c_pic.setVisibility(View.VISIBLE);
                    bianma_c_change_pic.setImageResource(R.drawable.down_huise_t);
                    isOpen_c = false;
                } else {
                    fl_bianma_c_pic.setVisibility(View.GONE);
                    bianma_c_change_pic.setImageResource(R.drawable.rignt_huise_t);
                    isOpen_c = true;
                }
                break;
            case R.id.bianma_d_change://d
                if (isOpen_d) {
                    fl_bianma_d_pic.setVisibility(View.VISIBLE);
                    bianma_d_change_pic.setImageResource(R.drawable.down_huise_t);
                    isOpen_d = false;
                } else {
                    fl_bianma_d_pic.setVisibility(View.GONE);
                    bianma_d_change_pic.setImageResource(R.drawable.rignt_huise_t);
                    isOpen_d = true;
                }
                break;
        }
    }
}
