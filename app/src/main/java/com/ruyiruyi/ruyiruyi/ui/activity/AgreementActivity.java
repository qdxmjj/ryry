package com.ruyiruyi.ruyiruyi.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.ruyiruyi.ruyiruyi.R;
import com.ruyiruyi.ruyiruyi.ui.activity.base.RyBaseActivity;
import com.ruyiruyi.rylibrary.cell.ActionBar;

public class AgreementActivity extends RyBaseActivity {

    private ActionBar actionBar;
    private TextView agreementText;
    public String agreementStr = "\"\\t\\t如驿如意“一次换轮胎 终身免费开”,自此车主再也不用担心爱车轮胎！\\n\\n\\t\\t我们立志于将高性价比且符合\" +\n" +
            "                \"中国车主习惯的轮胎推荐给广大车主，从而解决困扰车主的“换胎贵 换胎难”问题;同时,如驿如意用户可以享受免费\" +\n" +
            "                \"动平衡、免费四轮定位、免费洗车、免费轮胎换位、免费补胎等超级VIP服务。我们将联合上游供应厂商和终端服务门店，继续\" +\n" +
            "                \"提高实惠、全面的汽车养护服务。\\n\\n\\t\\t在繁重的生活压力下，我们不仅仅希望能给车主解决一些轮胎问题！我们更想让车主\" +\n" +
            "                \"养车更省钱省心省时间！\\n\\n\\t\\t最后，我们的征途是星辰大海！！！\"";
    private int agreementtype;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agreement);

        actionBar = (ActionBar) findViewById(R.id.my_action);
        actionBar.setTitle("用户协议");;
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
        agreementtype = intent.getIntExtra("AGREEMENTTYPE",0);  //0是畅行无忧协议   1是用户协议

        initView();
    }

    private void initView() {
        agreementText = (TextView) findViewById(R.id.agreement_text);
        agreementText.setText("\t\t如驿如意“一次换轮胎 终身免费开”,自此车主再也不用担心爱车轮胎！\n\n\t\t我们立志于将高性价比且符合" +
                "中国车主习惯的轮胎推荐给广大车主，从而解决困扰车主的“换胎贵 换胎难”问题;同时,如驿如意用户可以享受免费" +
                "动平衡、免费四轮定位、免费洗车、免费轮胎换位、免费补胎等超级VIP服务。我们将联合上游供应厂商和终端服务门店，继续" +
                "提高实惠、全面的汽车养护服务。\n\n\t\t在繁重的生活压力下，我们不仅仅希望能给车主解决一些轮胎问题！我们更想让车主" +
                "养车更省钱省心省时间！\n\n\t\t最后，我们的征途是星辰大海！！！");
    }
}
