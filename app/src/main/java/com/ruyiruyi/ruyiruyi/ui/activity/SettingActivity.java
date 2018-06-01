package com.ruyiruyi.ruyiruyi.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.ruyiruyi.ruyiruyi.R;
import com.ruyiruyi.ruyiruyi.db.DbConfig;
import com.ruyiruyi.ruyiruyi.db.model.User;
import com.ruyiruyi.ruyiruyi.ui.activity.base.RYBaseActivity;
import com.ruyiruyi.ruyiruyi.utils.UIOpenHelper;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;
import com.ruyiruyi.rylibrary.cell.ActionBar;

import org.xutils.DbManager;
import org.xutils.ex.DbException;

import rx.functions.Action1;

public class SettingActivity extends RYBaseActivity {
    private ActionBar actionBar;
    private FrameLayout fl_change_pw;
    private FrameLayout fl_change_phone;
    private FrameLayout fl_talk;
    private FrameLayout fl_understand_us;
    private TextView tv_exit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        actionBar = (ActionBar) findViewById(R.id.acbar);
        actionBar.setTitle("设置");
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

        initView();
        bindView();
    }

    private void bindView() {
        //修改密码
        RxViewAction.clickNoDouble(fl_change_pw).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                //判断是否登录（未登录提示登录）
                if (!judgeIsLogin()) {
                    return;
                }
                startActivity(new Intent(getApplicationContext(), ChangePwActivity.class));
            }
        });
        //修改手机号
        RxViewAction.clickNoDouble(fl_change_phone).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                //判断是否登录（未登录提示登录）
                if (!judgeIsLogin()) {
                    return;
                }

            }
        });
        //联系客服
        RxViewAction.clickNoDouble(fl_talk).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                //判断是否登录（未登录提示登录）
                if (!judgeIsLogin()) {
                    return;
                }


            }
        });
        //关于我们
        RxViewAction.clickNoDouble(fl_understand_us).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {

            }
        });
        //退出登录
        RxViewAction.clickNoDouble(tv_exit).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {

                showExitDialog("确认退出此账号吗?");

            }
        });
    }

    private void showExitDialog(String s) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_error, null);
        TextView error_text = (TextView) dialogView.findViewById(R.id.error_text);
        error_text.setText(s);
        dialog.setTitle("如意如驿");
        dialog.setIcon(R.drawable.ic_logo_login);
        dialog.setView(dialogView);
        dialog.setNegativeButton("点错了", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.setPositiveButton("是的", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                DbConfig dbConfig = new DbConfig();
                if (dbConfig.getIsLogin()) {
                    User user = dbConfig.getUser();
                    user.setIsLogin("0");
                    DbManager db = dbConfig.getDbManager();

                    try {
                        db.saveOrUpdate(user);
                    } catch (DbException e) {
                    }
                    finish();
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                }
            }
        });
        dialog.show();
    }

    private void initView() {
        fl_change_pw = findViewById(R.id.fl_change_pw);
        fl_change_phone = findViewById(R.id.fl_change_phone);
        fl_talk = findViewById(R.id.fl_talk);
        fl_understand_us = findViewById(R.id.fl_understand_us);
        tv_exit = findViewById(R.id.tv_exit);
    }
}
