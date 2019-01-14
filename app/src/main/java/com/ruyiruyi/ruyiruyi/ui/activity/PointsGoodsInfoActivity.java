package com.ruyiruyi.ruyiruyi.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ruyiruyi.ruyiruyi.R;
import com.ruyiruyi.ruyiruyi.ui.activity.base.RyBaseActivity;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;

import rx.functions.Action1;

public class PointsGoodsInfoActivity extends RyBaseActivity {
    private ImageView iv_goods;
    private TextView tv_title;
    private TextView tv_points;
    private TextView tv_price;
    private TextView tv_amount;
    private TextView tv_info;
    private TextView tv_haschange;
    private TextView tv_gochange;

    private int total_points;
    private int goods_points;
    private int mGoodsId;
    private String mGoodsPic;
    private String mTitle;
    private double mPrice;
    private int mChangeNum;
    private int goodsAmount;
    private String goodsInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_points_goods_info);

        Intent intent = getIntent();
        mGoodsId = intent.getIntExtra("goodsId", 0);
        mGoodsPic = intent.getStringExtra("goodsPic");
        mTitle = intent.getStringExtra("title");
        total_points = intent.getIntExtra("totalPoints", 0);
        goods_points = intent.getIntExtra("goodsPoints", 0);
        mPrice = intent.getDoubleExtra("price", 0);
        mChangeNum = intent.getIntExtra("changeNum", 0);
        goodsAmount = intent.getIntExtra("goodsAmount", 0);
        goodsInfo = intent.getStringExtra("goodsInfo");

        initView();
        bindData();
        bindView();
    }

    private void bindData() {
        Glide.with(PointsGoodsInfoActivity.this).load(mGoodsPic).into(iv_goods);
        tv_title.setText(mTitle);
        tv_points.setText(goods_points + "");
        tv_price.setText(mPrice + "");
        tv_haschange.setText(mChangeNum + "人已换购");
        tv_amount.setText(goodsAmount + "");
        tv_info.setText(goodsInfo);
    }

    private void bindView() {
        //立即兑换
        RxViewAction.clickNoDouble(tv_gochange).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (total_points < goods_points) {
                    Toast.makeText(PointsGoodsInfoActivity.this, "您的可用积分不足", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(PointsGoodsInfoActivity.this, PointsChangeOrderConfirmActivity.class);
                intent.putExtra("goodsPic", mGoodsPic);
                intent.putExtra("goodsTitle", mTitle);
                intent.putExtra("goodsPoints", goods_points);
                intent.putExtra("totalPoints", total_points);
                intent.putExtra("goodsId", mGoodsId);
                startActivity(intent);
            }
        });
    }

    private void initView() {
        iv_goods = (ImageView) findViewById(R.id.iv_goods);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_points = (TextView) findViewById(R.id.tv_points);
        tv_price = (TextView) findViewById(R.id.tv_price);
        tv_amount = (TextView) findViewById(R.id.tv_amount);//
        tv_info = (TextView) findViewById(R.id.tv_info);//
        tv_haschange = (TextView) findViewById(R.id.tv_haschange);
        tv_gochange = (TextView) findViewById(R.id.tv_gochange);
    }
}
