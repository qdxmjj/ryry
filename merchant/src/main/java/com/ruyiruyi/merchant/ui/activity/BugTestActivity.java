package com.ruyiruyi.merchant.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.ruyiruyi.merchant.R;
import com.ruyiruyi.rylibrary.base.BaseActivity;

/**
 * @作者 倾春
 * eml.dongbinjava@163.com
 * @创建日期 2018/7/30 9:41
 */
public class BugTestActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bug_test);
    }


    //测试 用
    public void bugtestclick(View view) {
        switch (view.getId()) {
            case R.id.service_record_act:
                Intent intent2 = new Intent(this, ServiceRecordActivity.class);
                startActivity(intent2);
                break;
            case R.id.open_order_freechange:
                Intent intent3 = new Intent(this, OpenOrderFreeChangeActivity.class);
                startActivity(intent3);
                break;
            case R.id.open_order_xiubu:
                Intent intent4 = new Intent(this, OpenOrderXiuBuActivity.class);
                startActivity(intent4);
                break;
            case R.id.open_order_firstchange:
                Intent intent5 = new Intent(this, OpenOrderFirstChangeActivity.class);
                startActivity(intent5);
                break;
            case R.id.open_order_photosample_xiubu:
                Intent intent6 = new Intent(this, PhotoSampleActivity.class);
                Bundle bundle6 = new Bundle();
                bundle6.putString("type", "xiubu");
                intent6.putExtras(bundle6);
                startActivity(intent6);
                break;
            case R.id.open_order_photosample_change:
                Intent intent7 = new Intent(this, PhotoSampleActivity.class);
                Bundle bundle7 = new Bundle();
                bundle7.putString("type", "change");
                intent7.putExtras(bundle7);
                startActivity(intent7);
                break;
            case R.id.open_order_photosample_car:
                Intent intent8 = new Intent(this, PhotoSampleActivity.class);
                Bundle bundle8 = new Bundle();
                bundle8.putString("type", "car");
                intent8.putExtras(bundle8);
                startActivity(intent8);
                break;
            case R.id.change_bianma:
                Intent intent9 = new Intent(this, ChangeBianmaActivity.class);
                startActivity(intent9);
                break;
            case R.id.qrcode_circlelogo:
                Intent intent10 = new Intent(this, TestCircleLogoQrCodeActivity.class);
                startActivity(intent10);
                break;
        }
    }
}
