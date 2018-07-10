package com.ruyiruyi.ruyiruyi.ui.activity;

import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.ruyiruyi.ruyiruyi.R;
import com.ruyiruyi.ruyiruyi.ui.activity.base.RyBaseActivity;
import com.ruyiruyi.rylibrary.cell.ActionBar;

public class UnderstandUsActivity extends RyBaseActivity {
    private TextView title;
    private TextView sign;
    private TextView content;
    private ActionBar mActionBar;
    public String versionCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_understand_us);
        mActionBar = (ActionBar) findViewById(R.id.acbar);
        mActionBar.setTitle("关于我们");
        /*mActionBar.setRightImage(R.drawable.ic_kefu);*/
        mActionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(int var1) {
                switch (var1) {
                    case -1:
                        onBackPressed();
                        break;
                  /*  case -2:
                        Toast.makeText(UnderstandUsActivity.this, "hello", Toast.LENGTH_SHORT).show();
                        break;*/
                }
            }
        });

        title = findViewById(R.id.title);
        sign = findViewById(R.id.sign);
        content = findViewById(R.id.content);

        try {
            versionCode = this.getPackageManager().getPackageInfo(this.getPackageName(),0).versionName;
        } catch (PackageManager.NameNotFoundException e) {

        }

        title.setText("一次换轮胎 终身免费开");
        sign.setText("V" + versionCode);
        content.setText("\t\t如驿如意“一次换轮胎 终身免费开”,自此车主再也不用担心爱车轮胎！\n\n\t\t我们立志于将高性价比且符合" +
                "中国车主习惯的轮胎推荐给广大车主，从而解决困扰车主的“换胎贵 换胎难”问题;同时,如驿如意用户可以享受免费" +
                "动平衡、免费四轮定位、免费洗车、免费轮胎换位、免费补胎等超级VIP服务。我们将联合上游供应厂商和终端服务门店，继续" +
                "提高实惠、全面的汽车养护服务。\n\n\t\t在繁重的生活压力下，我们不仅仅希望能给车主解决一些轮胎问题！我们更想让车主" +
                "养车更省钱省心省时间！\n\n\t\t最后，我们的征途是星辰大海！！！");
    }
}
