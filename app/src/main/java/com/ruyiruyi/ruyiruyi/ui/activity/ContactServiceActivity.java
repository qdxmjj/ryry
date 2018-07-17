package com.ruyiruyi.ruyiruyi.ui.activity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.ruyiruyi.ruyiruyi.R;
import com.ruyiruyi.ruyiruyi.ui.activity.base.RyBaseActivity;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;
import com.ruyiruyi.rylibrary.cell.ActionBar;

import rx.functions.Action1;

public class ContactServiceActivity extends RyBaseActivity {
    private ImageView call;
    private ImageView main;
    private boolean isOpen = true;//动画开关标志位  true 为弹出  false 为隐藏
    private ActionBar actionBar;
    private String servicePhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_talk_service);
        actionBar = (com.ruyiruyi.rylibrary.cell.ActionBar) findViewById(R.id.acbar);
        actionBar.setTitle("联系客服");
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


        servicePhone = "4008080136";

        initView();
        //已在onWindowFocusChanged()中初始化动画   animator.start()方法在onCreate，onStart，onResume中无效
        bindView();


    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        isOpen = false;
        ObjectAnimator animator = ObjectAnimator.ofFloat(call, View.TRANSLATION_X, (call.getWidth() * 0.50f));
        animator.start();
    }

    private void bindView() {
        RxViewAction.clickNoDouble(call).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (isOpen) {
                    //打电话
                    callToService();
                } else {
                    isOpen = true;
                    ObjectAnimator animator = ObjectAnimator.ofFloat(call, View.TRANSLATION_X, 0);
                    animator.start();
                }
            }
        });
        RxViewAction.clickNoDouble(main).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (isOpen) {
                    isOpen = false;
                    ObjectAnimator animator = ObjectAnimator.ofFloat(call, View.TRANSLATION_X, (call.getWidth() * 0.50f));
                    animator.start();
                } else {
                    return;
                }
            }
        });
    }

    private void callToService() {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + servicePhone);
        intent.setData(data);
        startActivity(intent);
    }

    private void initView() {
        main = findViewById(R.id.main);
        call = findViewById(R.id.call);
    }
}
