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

public class TirePlaceActivity extends BaseActivity {
    private ActionBar actionBar;
    private String fontsize;
    private String rearsize;
    private TextView fontText;
    private FrameLayout fontLayout;
    private TextView rearText;
    private FrameLayout rearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tire_place,R.id.my_action);
        actionBar = (ActionBar) findViewById(R.id.my_action);
        actionBar.setTitle("选择轮胎位置");;
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
        fontsize = intent.getStringExtra("FONTSIZE");
        rearsize = intent.getStringExtra("REARSIZE");

        initView();
    }

    private void initView() {
        fontText = (TextView) findViewById(R.id.font_text);
        fontLayout = (FrameLayout) findViewById(R.id.font_layout);
        rearText = (TextView) findViewById(R.id.rear_text);
        rearLayout = (FrameLayout) findViewById(R.id.reat_layout);

        fontText.setText("前轮：" + fontsize);
        rearText.setText("后轮：" + rearsize);

        RxViewAction.clickNoDouble(fontLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Intent intent = new Intent(getApplicationContext(), CarFigureActivity.class);
                        intent.putExtra("TIRESIZE",fontsize);
                        intent.putExtra("FONTREARFLAG","1");
                        startActivity(intent);
                    }
                });
        RxViewAction.clickNoDouble(rearLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Intent intent = new Intent(getApplicationContext(), CarFigureActivity.class);
                        intent.putExtra("TIRESIZE",rearsize);
                        intent.putExtra("FONTREARFLAG","2");
                        startActivity(intent);
                    }
                });
    }
}
