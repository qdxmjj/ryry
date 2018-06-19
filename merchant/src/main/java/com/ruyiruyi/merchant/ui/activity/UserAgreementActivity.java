package com.ruyiruyi.merchant.ui.activity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.ruyiruyi.merchant.R;
import com.ruyiruyi.merchant.ui.activity.base.MerchantBaseActivity;
import com.ruyiruyi.rylibrary.cell.ActionBar;

public class UserAgreementActivity extends MerchantBaseActivity {
    private TextView tv_content;
    private String contentStr = "\t\t如驿如意“一次换轮胎 终身免费开”,自此车主再也不用担心爱车轮胎！\n\n\t\t我们立志于将高性价比且符合" +
            "中国车主习惯的轮胎推荐给广大车主，从而解决困扰车主的“换胎贵 换胎难”问题;同时,如驿如意用户可以享受免费" +
            "动平衡、免费四轮定位、免费洗车、免费轮胎换位、免费补胎等超级VIP服务。我们将联合上游供应厂商和终端服务门店，继续" +
            "提高实惠、全面的汽车养护服务。\n\n\t\t在繁重的生活压力下，我们不仅仅希望能给车主解决一些轮胎问题！我们更想让车主" +
            "养车更省钱省心省时间！\n\n\t\t最后，我们的征途是星辰大海！！！";
    private ActionBar mActionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_agreement);
        mActionBar = (ActionBar) findViewById(R.id.acbar);
        mActionBar.setTitle("用户协议");
        mActionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(int var1) {
                switch (var1) {
                    case -1:
                        onBackPressed();
                        break;
                }
            }
        });


        initView();
        setView();

    }

    private void setView() {
        tv_content.setText(contentStr);
    }

    private void initView() {
        tv_content = findViewById(R.id.tv_content);
    }
}
