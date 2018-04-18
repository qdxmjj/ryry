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
    private String fontrearflag;
    private int tirecount;
    private String tireprice;
    private String tirepname;
    private int cxwycount;
    private String cxwyprice;
    private String username;
    private String userphone;
    private String carnumber;
    private String tireimage;

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

        Intent intent = getIntent();
        fontrearflag = intent.getStringExtra("FONTREARFLAG");
        tirecount = intent.getIntExtra("TIRECOUNT",0);
        tireprice = intent.getStringExtra("TIREPRICE");
        tirepname = intent.getStringExtra("TIREPNAME");
        cxwycount = intent.getIntExtra("CXWYCOUNT",0);
        cxwyprice = intent.getStringExtra("CXWYPRICE");
        username = intent.getStringExtra("USERNAME");
        userphone = intent.getStringExtra("USERPHONE");
        carnumber = intent.getStringExtra("CARNUMBER");
        tireimage = intent.getStringExtra("TIREIMAGE");


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
                        intent.putExtra("FONTREARFLAG",fontrearflag);   //前后轮标识
                        intent.putExtra("TIRECOUNT",tirecount);//轮胎数量
                        intent.putExtra("TIREPRICE",tireprice);     //轮胎单价
                        intent.putExtra("TIREPNAME",tirepname);  //轮胎名称
                        intent.putExtra("CXWYCOUNT",cxwycount);  //畅行无忧数量
                        intent.putExtra("CXWYPRICE",cxwyprice);  //畅行无忧名称
                        intent.putExtra("USERNAME",username);  //用户名
                        intent.putExtra("USERPHONE",userphone);  //手机号
                        intent.putExtra("CARNUMBER",carnumber);  //车牌号
                        intent.putExtra("TIREIMAGE",tireimage);  //轮胎图片

                        startActivity(intent);
                    }
                });

    }
}
