package com.ruyiruyi.ruyiruyi.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.ruyiruyi.ruyiruyi.R;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;
import com.ruyiruyi.rylibrary.base.BaseActivity;
import com.ruyiruyi.rylibrary.cell.ActionBar;

import rx.functions.Action1;

public class QcActivity extends BaseActivity {
    private ActionBar actionBar;
    private FrameLayout tireFlowLayout;
    private TextView qcNextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qc,R.id.my_action);
        actionBar = (ActionBar) findViewById(R.id.my_action);
        actionBar.setTitle("品质服务");;
        actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick(){
            @Override
            public void onItemClick(int var1) {
                switch ((var1)){
                    case -1:
                        onBackPressed();
                        break;
                }
            }
        });

        initView();
    }

    private void initView() {
        tireFlowLayout = (FrameLayout) findViewById(R.id.tire_liucheng_layout);
        qcNextButton = (TextView) findViewById(R.id.tire_qc_button);

        RxViewAction.clickNoDouble(tireFlowLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        startActivity(new Intent(getApplicationContext(),BuyFlowActivity.class));
                    }
                });
        RxViewAction.clickNoDouble(qcNextButton)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Intent intent = new Intent(getApplicationContext(), OrderAffirmActivity.class);
                        startActivity(intent);
                    }
                });

    }
}
