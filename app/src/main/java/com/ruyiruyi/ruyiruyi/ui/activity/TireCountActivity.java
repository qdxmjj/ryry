package com.ruyiruyi.ruyiruyi.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ruyiruyi.ruyiruyi.R;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;
import com.ruyiruyi.rylibrary.base.BaseActivity;
import com.ruyiruyi.rylibrary.cell.ActionBar;
import com.ruyiruyi.rylibrary.cell.AmountView;
import com.ruyiruyi.rylibrary.ui.viewpager.CustomBanner;

import java.util.ArrayList;
import java.util.List;

import rx.functions.Action1;

public class TireCountActivity extends BaseActivity {
    private static final String TAG = TireCountActivity.class.getSimpleName();
    private ActionBar actionBar;
    private CustomBanner mBanner;
    private String shoeId;
    private String price;
    private AmountView tireAmountView;
    public static int maxCount = 10;
    public int tireCurrentCount = 0;
    public int cxwyCurrentCount = 0;
    private AmountView cxwyAmountView;
    private TextView tireCountButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tire_count,R.id.my_action);
        actionBar = (ActionBar) findViewById(R.id.my_action);
        actionBar.setTitle("轮胎购买");;
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
        shoeId = intent.getStringExtra("SHOEID");
        price = intent.getStringExtra("PRICE");

        initView();
    }

    private void initView() {
        mBanner = (CustomBanner) findViewById(R.id.car_count_banner);
        tireAmountView = (AmountView) findViewById(R.id.amount_view);
        cxwyAmountView = (AmountView) findViewById(R.id.changxingwuyou_count);
        tireCountButton = (TextView) findViewById(R.id.tire_count_button);

        tireAmountView.setGoods_storage(maxCount);
        tireAmountView.setOnAmountChangeListener(new AmountView.OnAmountChangeListener() {
            @Override
            public void onAmountChange(View view, int amount) {
                if (amount == maxCount){
                    Toast.makeText(TireCountActivity.this,"轮胎数量已达到购买上限", Toast.LENGTH_SHORT).show();
                }
                tireCurrentCount =amount;

            }
        });

        cxwyAmountView.setGoods_storage(maxCount);
        cxwyAmountView.setOnAmountChangeListener(new AmountView.OnAmountChangeListener() {
            @Override
            public void onAmountChange(View view, int amount) {
                if (amount == maxCount){
                    Toast.makeText(TireCountActivity.this,"畅行无忧已达到购买上限", Toast.LENGTH_SHORT).show();
                }
                cxwyCurrentCount =amount;

            }
        });

        RxViewAction.clickNoDouble(tireCountButton)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (tireCurrentCount == 0){
                            Toast.makeText(TireCountActivity.this, "你选择轮胎购买数量", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Intent intent = new Intent(getApplicationContext(), QcActivity.class);
                        startActivity(intent);
                    }
                });


        List<String> imageList = new ArrayList<>();

        imageList.add("http://180.76.243.205:8111/images/Advertisement/cxwy.png");
        imageList.add("http://180.76.243.205:8111/images/Advertisement/cxwy1000.png");
        imageList.add("http://180.76.243.205:8111/images/Advertisement/cxwy1000.png");


        mBanner.setPages(new CustomBanner.ViewCreator<String>() {
            @Override
            public View createView(Context context, int position) {
                ImageView imageView = new ImageView(context);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                return imageView;
            }

            @Override
            public void updateUI(Context context, View view, int position, String entity) {
                Glide.with(context).load(entity).into((ImageView) view);
            }
        },imageList)//                //设置指示器为普通指示器
//                .setIndicatorStyle(CustomBanner.IndicatorStyle.ORDINARY)
//                //设置两个点图片作为翻页指示器，不设置则没有指示器，可以根据自己需求自行配合自己的指示器,不需要圆点指示器可用不设
//                .setIndicatorRes(R.drawable.shape_point_select, R.drawable.shape_point_unselect)
//                //设置指示器的方向
//                .setIndicatorGravity(CustomBanner.IndicatorGravity.CENTER)
//                //设置指示器的指示点间隔
//                .setIndicatorInterval(20)
                //设置自动翻页
                .startTurning(5000);
    }
}
