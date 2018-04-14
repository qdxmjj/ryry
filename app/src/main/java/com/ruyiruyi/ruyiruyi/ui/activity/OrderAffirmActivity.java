package com.ruyiruyi.ruyiruyi.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ruyiruyi.ruyiruyi.R;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;
import com.ruyiruyi.rylibrary.base.BaseActivity;
import com.ruyiruyi.rylibrary.cell.ActionBar;

import rx.functions.Action1;

public class OrderAffirmActivity extends BaseActivity {
    private ActionBar actionBar;
    private TextView tireBuyButton;
    private ImageView tireImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_affirm,R.id.my_action);
        actionBar = (ActionBar) findViewById(R.id.my_action);
        actionBar.setTitle("订单确认");;
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
        tireBuyButton = (TextView) findViewById(R.id.tire_buy_button);
        tireImage = (ImageView) findViewById(R.id.tire_image);

        Glide.with(this).load("http://180.76.243.205:8111/images/flgure/9F5CD167-866A-C9B3-4406-7E0E36A4D003.jpg").into(tireImage);

        RxViewAction.clickNoDouble(tireBuyButton)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Intent intent = new Intent(getApplicationContext(),PaymentActivity.class);
                        startActivity(intent);
                    }
                });
    }
}
