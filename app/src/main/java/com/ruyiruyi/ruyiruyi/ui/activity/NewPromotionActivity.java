package com.ruyiruyi.ruyiruyi.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.ruyiruyi.ruyiruyi.R;
import com.ruyiruyi.ruyiruyi.ui.activity.base.RyBaseActivity;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;
import com.ruyiruyi.rylibrary.cell.ActionBar;

import rx.functions.Action1;

public class NewPromotionActivity extends RyBaseActivity {
    private ActionBar actionBar;
    private FrameLayout fl_invite_register;
    private FrameLayout fl_invite_buyshoe;
    private FrameLayout fl_invite_person;
    private FrameLayout fl_invite_award;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_promotion);
        actionBar = (ActionBar) findViewById(R.id.my_acbar);
        actionBar.setTitle("邀请有礼");
        actionBar.setRightImage(R.drawable.ic_erweima);
        actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(int var1) {
                switch ((var1)) {
                    case -1:
                        onBackPressed();
                        break;
                    case -2:
                        onQrCodePressed();
                        break;
                }
            }


        });

        initView();
        bindView();
    }

    private void bindView() {
        //邀请好友注册如驿如意
        RxViewAction.clickNoDouble(fl_invite_register).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                startActivity(new Intent(NewPromotionActivity.this, InviteRegisterActivity.class));
            }
        });
        //邀请车友购买轮胎
        RxViewAction.clickNoDouble(fl_invite_buyshoe).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                startActivity(new Intent(NewPromotionActivity.this, InviteBuyshoeActivity.class));
            }
        });
        //我推荐的人
        RxViewAction.clickNoDouble(fl_invite_person).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Intent intent = new Intent(NewPromotionActivity.this, MyInvitePersonActivity.class);
                startActivity(intent);
            }
        });
        //我的奖品
        RxViewAction.clickNoDouble(fl_invite_award).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Intent intent = new Intent(NewPromotionActivity.this, MyInviteAwardActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initView() {
        fl_invite_register = findViewById(R.id.fl_invite_register);
        fl_invite_buyshoe = findViewById(R.id.fl_invite_buyshoe);
        fl_invite_person = findViewById(R.id.fl_invite_person);
        fl_invite_award = findViewById(R.id.fl_invite_award);
    }


    /**
     * 标题栏右侧图标点击事件
     */
    private void onQrCodePressed() {
        Intent intent = new Intent(getApplicationContext(), NewQrCodeActivity.class);
        startActivity(intent);
    }
}
