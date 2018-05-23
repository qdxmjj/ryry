package com.ruyiruyi.ruyiruyi.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.ruyiruyi.ruyiruyi.MainActivity;
import com.ruyiruyi.ruyiruyi.R;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;
import com.ruyiruyi.rylibrary.base.BaseActivity;
import com.ruyiruyi.rylibrary.cell.ActionBar;

import rx.functions.Action1;

public class PaySuccessActivity extends BaseActivity {
    private ActionBar actionBar;
    private TextView tireChangeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_pay_success,R.id.my_action);//方案一
        setContentView(R.layout.activity_pay_success_two, R.id.my_action);//方案二

        actionBar = (ActionBar) findViewById(R.id.my_action);
        actionBar.setTitle("支付成功");
        actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(int var1) {
                switch ((var1)) {
                    case -1:
                        onBackPressed();
                        break;
                }
            }
        });

        initView();
    }

    private void initView() {
        tireChangeButton = (TextView) findViewById(R.id.tire_change_button);

        RxViewAction.clickNoDouble(tireChangeButton)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        startActivity(new Intent(getApplicationContext(), TireChangeActivity.class));
                    }
                });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));

    }


}
