package com.ruyiruyi.ruyiruyi.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ruyiruyi.ruyiruyi.MainActivity;
import com.ruyiruyi.ruyiruyi.R;
import com.ruyiruyi.ruyiruyi.ui.activity.base.RyBaseActivity;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;
import com.ruyiruyi.rylibrary.cell.ActionBar;

import rx.functions.Action1;

public class LunboContentActivity extends RyBaseActivity {
    private ActionBar actionBar;
    private ImageView lunboImageView;
    private TextView lunboButton;

    public static String LUNBO_POSITION = "LUNBO_POSITION";  // 0畅行无忧  1 任何轮胎损坏  2是99一条  3清凉一夏
    private int lunboPosition;
    private int fontrearflag;
    private String fontsize;
    private String rearsize;
    private int carid;
    private int usercarid;
    private String service_year_max;    //最大服务年限
    private String service_year;       //当前服务年限
    private String service_end_year;
    private int carUUid;
    private AlertDialog carInfoDialog;

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
        carUUid = intent.getIntExtra("CARUUID",0);
        usercarid = intent.getIntExtra("USERCARID",0);
        service_year = intent.getStringExtra("SERVICE_YEAR");
        service_year_max = intent.getStringExtra("SERVICE_YEAR_MAX");
        service_end_year = intent.getStringExtra("SERVICE_END_YEAR");

        intiView();
    }

    private void intiView() {
        lunboImageView = (ImageView) findViewById(R.id.lunbo_image_content);
        lunboButton = (TextView) findViewById(R.id.lunbo_button);
        carInfoDialog = new AlertDialog.Builder(this)
                .setTitle("请完善车辆信息")
                .setMessage("是否前往完善信息界面")
                .setIcon(R.mipmap.ic_logo)
                .setPositiveButton("前往", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(getApplicationContext(), CarManagerActivity.class);
                        intent.putExtra("FRAGMENT", "HOMEFRAGMENT");
                        startActivityForResult(intent, MainActivity.HOMEFRAGMENT_RESULT);
                        finish();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                }).create();


        if (lunboPosition == 0){
            lunboImageView.setImageResource(R.drawable.ic_huodong0);
            lunboButton.setBackgroundResource(R.drawable.ic_hd_button0);
        }else if (lunboPosition == 1){
            lunboImageView.setImageResource(R.drawable.ic_huodong1);
            lunboButton.setVisibility(View.GONE);
        } else if (lunboPosition == 2){
            lunboImageView.setImageResource(R.drawable.ic_huodong2);
            lunboButton.setVisibility(View.GONE);
        }

        RxViewAction.clickNoDouble(lunboButton)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (carUUid == 0){
                            carInfoDialog.show();
                            return;
                        }
                        if (lunboPosition == 0){    //购买轮胎
                            if (fontrearflag == 0) {  //前后轮一样
                                if (service_end_year.equals("")){
                                    Intent intent = new Intent(getApplicationContext(), TireBuyNewActivity.class);
                                    intent.putExtra("TIRESIZE", fontsize);
                                    intent.putExtra("FONTREARFLAG", "0");
                                    intent.putExtra("SERVICE_YEAR_MAX", service_year_max);
                                    intent.putExtra("SERVICE_YEAR", service_year);
                                    intent.putExtra("CHOOSE_SERVICE_YEAR", false);
                                    intent.putExtra("CARID", carid);
                                    intent.putExtra("USERCARID",usercarid);
                                    startActivity(intent);
                                }else {
                                    Intent intent = new Intent(getApplicationContext(), TireBuyNewActivity.class);
                                    intent.putExtra("TIRESIZE", fontsize);
                                    intent.putExtra("FONTREARFLAG", "0");
                                    intent.putExtra("SERVICE_YEAR_MAX", service_year_max);
                                    intent.putExtra("SERVICE_YEAR", service_year);
                                    intent.putExtra("CHOOSE_SERVICE_YEAR", true);
                                    intent.putExtra("CARID", carid);
                                    intent.putExtra("USERCARID",usercarid);
                                    startActivity(intent);
                                }
                            } else {         //前后轮不一样
                                Intent intent = new Intent(getApplicationContext(), TirePlaceActivity.class);
                                intent.putExtra("FONTSIZE", fontsize);
                                intent.putExtra("REARSIZE", rearsize);
                                intent.putExtra("SERVICE_YEAR_MAX", service_year_max);
                                intent.putExtra("SERVICE_YEAR", service_year);
                                intent.putExtra("SERVICE_END_YEAR", service_end_year+"");
                                intent.putExtra("CARID", carid);
                                intent.putExtra("USERCARID",usercarid);
                                startActivity(intent);

                            }
                        }else if (lunboPosition == 1){  //购买轮胎
                            if (fontrearflag == 0) {  //前后轮一样
                                if (service_end_year.equals("")){
                                    Intent intent = new Intent(getApplicationContext(), TireBuyNewActivity.class);
                                    intent.putExtra("TIRESIZE", fontsize);
                                    intent.putExtra("FONTREARFLAG", "0");
                                    intent.putExtra("SERVICE_YEAR_MAX", service_year_max);
                                    intent.putExtra("SERVICE_YEAR", service_year);
                                    intent.putExtra("CHOOSE_SERVICE_YEAR", false);
                                    intent.putExtra("CARID", carid);
                                    intent.putExtra("USERCARID",usercarid);
                                    startActivity(intent);
                                }else {
                                    Intent intent = new Intent(getApplicationContext(), TireBuyNewActivity.class);
                                    intent.putExtra("TIRESIZE", fontsize);
                                    intent.putExtra("FONTREARFLAG", "0");
                                    intent.putExtra("SERVICE_YEAR_MAX", service_year_max);
                                    intent.putExtra("SERVICE_YEAR", service_year);
                                    intent.putExtra("CHOOSE_SERVICE_YEAR", true);
                                    intent.putExtra("CARID", carid);
                                    intent.putExtra("USERCARID",usercarid);
                                    startActivity(intent);
                                }
                            } else {         //前后轮不一样
                                Intent intent = new Intent(getApplicationContext(), TirePlaceActivity.class);
                                intent.putExtra("FONTSIZE", fontsize);
                                intent.putExtra("REARSIZE", rearsize);
                                intent.putExtra("SERVICE_YEAR_MAX", service_year_max);
                                intent.putExtra("SERVICE_YEAR", service_year);
                                intent.putExtra("SERVICE_END_YEAR", service_end_year+"");
                                intent.putExtra("CARID", carid);
                                intent.putExtra("USERCARID",usercarid);
                                startActivity(intent);

                            }
                        }
                    }
                       /* else if (lunboPosition == 3){  //免费洗车
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

                            }*/
                          /*  Intent intent = new Intent(getApplicationContext(),GoodsShopActivity.class);
                            intent.putExtra("SEARCH_STR","精致洗车");
                            intent.putExtra(GoodsShopActivity.FROMTYPE,1);
                            startActivity(intent);*/
                    /*    }

                    }*/
                });
    }
}
