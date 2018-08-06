package com.ruyiruyi.ruyiruyi.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.ruyiruyi.ruyiruyi.R;
import com.ruyiruyi.ruyiruyi.ui.activity.base.RyBaseActivity;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;
import com.ruyiruyi.rylibrary.cell.ActionBar;

import rx.functions.Action1;

public class LunboContentActivity extends RyBaseActivity {
    private ActionBar actionBar;
    private ImageView lunboImageView;
    private TextView lunboButton;

    public static String LUNBO_POSITION = "LUNBO_POSITION";
    private int lunboPosition;
    private int fontrearflag;
    private String fontsize;
    private String rearsize;
    private int carid;
    private int usercarid;
    private int serviceyear;
    private String service_end_year;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lunbo_content);
        actionBar = (ActionBar) findViewById(R.id.acbar);
        actionBar.setTitle("最新活动");
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

        Intent intent = getIntent();
        lunboPosition = intent.getIntExtra(LUNBO_POSITION,0);
        fontrearflag = intent.getIntExtra("FONTREARFLAG",0);
        if (fontrearflag == 0){
            fontsize = intent.getStringExtra("FONTSIZE");
        }else {
            fontsize = intent.getStringExtra("FONTSIZE");
            rearsize = intent.getStringExtra("REARSIZE");
        }
        carid = intent.getIntExtra("CARID",0);
        usercarid = intent.getIntExtra("USERCARID",0);
        serviceyear = Integer.parseInt(intent.getStringExtra("SERVICEYEAR"));
        service_end_year = intent.getStringExtra("SERVICE_END_YEAR");

        intiView();
    }

    private void intiView() {
        lunboImageView = (ImageView) findViewById(R.id.lunbo_image_content);
        lunboButton = (TextView) findViewById(R.id.lunbo_button);


        if (lunboPosition == 0){
            lunboImageView.setImageResource(R.drawable.ic_huoodng_3);
            lunboButton.setBackgroundResource(R.drawable.ic_hd_button3);
        }else if (lunboPosition == 1){
            lunboImageView.setImageResource(R.drawable.ic_huoodng_1);
            lunboButton.setBackgroundResource(R.drawable.ic_hd_button1);
        }else if (lunboPosition == 3){
            lunboImageView.setImageResource(R.drawable.ic_huoodng_2);
            lunboButton.setBackgroundResource(R.drawable.ic_hd_button2);
        }

        RxViewAction.clickNoDouble(lunboButton)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (lunboPosition == 0){    //购买轮胎
                            if (fontrearflag == 0) {  //前后轮一样
                                if (service_end_year.equals("")){
                                    Intent intent = new Intent(getApplicationContext(), YearChooseActivity.class);
                                    intent.putExtra("TIRESIZE", fontsize);
                                    intent.putExtra("FONTREARFLAG", "0");
                                    intent.putExtra("SERVICEYEAR", serviceyear+"");
                                    intent.putExtra("CARID", carid);
                                    intent.putExtra("USERCARID",usercarid);
                                    startActivity(intent);
                                }else {
                                    Intent intent = new Intent(getApplicationContext(), CarFigureActivity.class);
                                    intent.putExtra("TIRESIZE", fontsize);
                                    intent.putExtra("FONTREARFLAG", "0");
                                    intent.putExtra("SERVICEYEAR", serviceyear+"");
                                    intent.putExtra("CARID", carid);
                                    intent.putExtra("USERCARID",usercarid);
                                    startActivity(intent);
                                }
                            } else {         //前后轮不一样
                                Intent intent = new Intent(getApplicationContext(), TirePlaceActivity.class);
                                intent.putExtra("FONTSIZE", fontsize);
                                intent.putExtra("REARSIZE", rearsize);
                                intent.putExtra("SERVICEYEAR",serviceyear+"");
                                intent.putExtra("SERVICE_END_YEAR", service_end_year);
                                intent.putExtra("CARID", carid);
                                intent.putExtra("USERCARID",usercarid);
                                startActivity(intent);

                            }
                        }else if (lunboPosition == 1){  //购买轮胎
                            if (fontrearflag == 0) {  //前后轮一样
                                if (service_end_year.equals("")){
                                    Intent intent = new Intent(getApplicationContext(), YearChooseActivity.class);
                                    intent.putExtra("TIRESIZE", fontsize);
                                    intent.putExtra("FONTREARFLAG", "0");
                                    intent.putExtra("SERVICEYEAR", serviceyear+"");
                                    intent.putExtra("CARID", carid);
                                    intent.putExtra("USERCARID",usercarid);
                                    startActivity(intent);
                                }else {
                                    Intent intent = new Intent(getApplicationContext(), CarFigureActivity.class);
                                    intent.putExtra("TIRESIZE", fontsize);
                                    intent.putExtra("FONTREARFLAG", "0");
                                    intent.putExtra("SERVICEYEAR", serviceyear+"");
                                    intent.putExtra("CARID", carid);
                                    intent.putExtra("USERCARID",usercarid);
                                    startActivity(intent);
                                }
                            } else {         //前后轮不一样
                                Intent intent = new Intent(getApplicationContext(), TirePlaceActivity.class);
                                intent.putExtra("FONTSIZE", fontsize);
                                intent.putExtra("REARSIZE", rearsize);
                                intent.putExtra("SERVICEYEAR",serviceyear+"");
                                intent.putExtra("SERVICE_END_YEAR", service_end_year);
                                intent.putExtra("CARID", carid);
                                intent.putExtra("USERCARID",usercarid);
                                startActivity(intent);

                            }
                        }else if (lunboPosition == 3){  //免费洗车
                            Intent intent = new Intent(getApplicationContext(),GoodsShopActivity.class);
                            intent.putExtra("SEARCH_STR","精致洗车");
                            intent.putExtra(GoodsShopActivity.FROMTYPE,1);
                            startActivity(intent);
                        }

                    }
                });
    }
}
