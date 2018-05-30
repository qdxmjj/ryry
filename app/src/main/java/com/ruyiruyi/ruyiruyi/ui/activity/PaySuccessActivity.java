package com.ruyiruyi.ruyiruyi.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.ruyiruyi.ruyiruyi.MainActivity;
import com.ruyiruyi.ruyiruyi.R;
import com.ruyiruyi.ruyiruyi.ui.activity.base.RYBaseActivity;
import com.ruyiruyi.ruyiruyi.ui.fragment.OrderFragment;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;
import com.ruyiruyi.rylibrary.cell.ActionBar;

import rx.functions.Action1;

public class PaySuccessActivity extends RYBaseActivity {
    private ActionBar actionBar;
    private Intent intent;
    private int ordertype;
    private TextView goSeeView;
    private TextView goMainView;

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
        intent = getIntent();
        ordertype = intent.getIntExtra("ORDERTYPE",0);  //0轮胎订单  1商品订单;



        initView();
    }

    private void initView() {
        goSeeView = (TextView) findViewById(R.id.go_see_view);
        goMainView = (TextView) findViewById(R.id.go_main_view);

        if (ordertype == 0){
            goSeeView.setText("去换胎");
        }else if (ordertype == 1){
            goSeeView.setText("去服务");
        }

        RxViewAction.clickNoDouble(goSeeView)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (ordertype == 1){    //商品订单
                            Intent intent = new Intent(getApplicationContext(), OrderActivity.class);
                            intent.putExtra(OrderFragment.ORDER_TYPE, "DFW");
                            intent.putExtra(OrderActivity.ORDER_FROM,1);
                            startActivity(intent);
                            finish();
                        }else if (ordertype == 0){  //轮胎订单
                            startActivity(new Intent(getApplicationContext(),TireWaitChangeActivity.class));
                        }
                    }
                });

        RxViewAction.clickNoDouble(goMainView)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                    }
                });


    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));

    }


}
