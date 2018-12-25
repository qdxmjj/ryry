package com.ruyiruyi.ruyiruyi.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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
    private int mPoints;
    private double mPrice;
    private int mChangeNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_points_goods_info);

        Intent intent = getIntent();
        mGoodsId = intent.getIntExtra("goodsId", 0);
        mGoodsPic = intent.getStringExtra("goodsPic");
        mTitle = intent.getStringExtra("title");
        mPoints = intent.getIntExtra("points", 0);
        mPrice = intent.getDoubleExtra("price", 0);
        mChangeNum = intent.getIntExtra("changeNum", 0);

        initView();
        bindView();
    }

    private void bindView() {
        //立即兑换
        RxViewAction.clickNoDouble(tv_gochange).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                showChangeDialog();
            }
        });
    }

    private void showChangeDialog() {
        AlertDialog dialog = new AlertDialog.Builder(this).create();
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_error, null);
        TextView error_text = (TextView) dialogView.findViewById(R.id.error_text);
        error_text.setText("您当前拥有" + total_points + "积分,确定消耗" + goods_points + "积分兑换该商品吗?");
        dialog.setTitle("如意如驿");
        dialog.setIcon(R.mipmap.ic_logo);
        dialog.setView(dialogView);
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //兑换操作


            }
        });
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "再看看", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dialog.show();
        //设置按钮颜色
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.theme_primary));
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.theme_primary));

    }

    private void initView() {
        iv_goods = (ImageView) findViewById(R.id.iv_goods);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_points = (TextView) findViewById(R.id.tv_points);
        tv_price = (TextView) findViewById(R.id.tv_price);
        tv_amount = (TextView) findViewById(R.id.tv_amount);
        tv_info = (TextView) findViewById(R.id.tv_info);
        tv_haschange = (TextView) findViewById(R.id.tv_haschange);
        tv_gochange = (TextView) findViewById(R.id.tv_gochange);
    }
}
