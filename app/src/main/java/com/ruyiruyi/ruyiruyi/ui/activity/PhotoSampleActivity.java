package com.ruyiruyi.ruyiruyi.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ruyiruyi.ruyiruyi.R;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;
import com.ruyiruyi.rylibrary.base.BaseActivity;
import com.ruyiruyi.rylibrary.cell.ActionBar;

import rx.functions.Action1;

public class PhotoSampleActivity extends BaseActivity {
    private ImageView pic_left;
    private ImageView pic_right;
    private ImageView pic_top;
    private LinearLayout ll_two_circlepic;
    private ActionBar mActionBar;
    private TextView txt_a;
    private TextView txt_b;
    private TextView txt_c;
    private TextView start_photo;

    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_sample);
        mActionBar = (ActionBar) findViewById(R.id.photo_acbar);
        mActionBar.setTitle("拍摄示例");
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
        Bundle bundle = getIntent().getExtras();
        type = bundle.getString("type");
        pic_left = findViewById(R.id.circle_pic_left);
        pic_right = findViewById(R.id.circle_pic_right);
        ll_two_circlepic = findViewById(R.id.ll_two_circlepic);
        pic_top = findViewById(R.id.pic_top);
        txt_a = findViewById(R.id.txt_a);
        txt_b = findViewById(R.id.txt_b);
        txt_c = findViewById(R.id.txt_c);
        start_photo = findViewById(R.id.start_photo);
        if ("xiubu".equals(type)) {
            pic_top.setVisibility(View.GONE);
            ll_two_circlepic.setVisibility(View.VISIBLE);
            pic_left.setImageResource(R.drawable.ic_head);
            pic_right.setImageResource(R.drawable.ic_head);
            txt_a.setText("轮胎破损部位特写照");
            txt_b.setText("轮胎条形码特写照");
            txt_c.setText("每条确定免费更换的轮胎必须拍以上两张照片");
        }
        if ("change".equals(type)) {
            pic_top.setVisibility(View.GONE);
            ll_two_circlepic.setVisibility(View.VISIBLE);
            pic_left.setImageResource(R.drawable.ic_head);
            pic_right.setImageResource(R.drawable.ic_head);
            txt_a.setText("轮胎正面照");
            txt_b.setText("轮胎条形码特写照");
            txt_c.setText("每条轮胎都需要拍以上两张照片");
        }
        if ("car".equals(type)) {
            pic_top.setVisibility(View.VISIBLE);
            ll_two_circlepic.setVisibility(View.GONE);
            pic_top.setImageResource(R.drawable.ic_carphoto);
            txt_a.setText("从车前方左侧45°角拍摄");
            txt_b.setText("机动车影像应占相片的三分之二");
            txt_c.setText("机动车相片一定能够清晰辨认车身颜色及外观特征");
        }

        RxViewAction.clickNoDouble(start_photo).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                finish();
            }
        });

    }
}
