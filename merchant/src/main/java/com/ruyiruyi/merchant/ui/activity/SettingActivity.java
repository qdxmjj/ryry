package com.ruyiruyi.merchant.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.ruyiruyi.merchant.R;
import com.ruyiruyi.merchant.db.DbConfig;
import com.ruyiruyi.merchant.db.model.User;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;
import com.ruyiruyi.rylibrary.base.BaseActivity;
import com.ruyiruyi.rylibrary.cell.ActionBar;

import org.xutils.DbManager;
import org.xutils.ex.DbException;

import rx.functions.Action1;

public class SettingActivity extends BaseActivity {

    private ActionBar mActionBar;
    private RelativeLayout rl_change_pw;
    private RelativeLayout rl_voice;
    private Switch swi_voice;
    private TextView tv_exit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_she_zhi);
        mActionBar = (ActionBar) findViewById(R.id.acbar_shezhi);
        mActionBar.setTitle("设置");
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


        rl_change_pw = findViewById(R.id.rl_change_pw);
        rl_voice = findViewById(R.id.rl_voice);
        swi_voice = findViewById(R.id.swi_voice);
        tv_exit = findViewById(R.id.tv_exit);

        //设置原始数据
        String isVoice = new DbConfig(getApplicationContext()).getUser().getIsVoice();
        if ("1".equals(isVoice)) {
            swi_voice.setChecked(true);
        } else if ("0".equals(isVoice)) {
            swi_voice.setChecked(false);
        }


        //修改密码
        RxViewAction.clickNoDouble(rl_change_pw).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Intent intent = new Intent(SettingActivity.this, ChangePwActivity.class);
                startActivity(intent);
            }
        });
        //退出登录
        RxViewAction.clickNoDouble(tv_exit).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                showExitDialog("确定退出当前账号吗？");
            }
        });
/*        //语音播报
        RxViewAction.clickNoDouble(rl_voice).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                showVoiceStateDialog();
            }
        });*/
        //语音播报switch
        swi_voice.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {//选中开启
                    DbConfig dbConfig = new DbConfig(getApplicationContext());
                    User user = dbConfig.getUser();
                    user.setIsVoice("1");
                    try {
                        dbConfig.getDbManager().saveOrUpdate(user);

                    } catch (DbException e) {
                    }
                } else {//关闭
                    DbConfig dbConfig = new DbConfig(getApplicationContext());
                    User user = dbConfig.getUser();
                    user.setIsVoice("0");
                    try {
                        dbConfig.getDbManager().saveOrUpdate(user);

                    } catch (DbException e) {
                    }
                }
            }
        });


    }


    private void showExitDialog(String error) {
        final AlertDialog dialog = new AlertDialog.Builder(this).create();
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_error, null);
        TextView error_text = (TextView) dialogView.findViewById(R.id.error_text);
        error_text.setText(error);
        dialog.setTitle("如意如驿商家版");
        dialog.setIcon(R.drawable.ic_launcher);
        dialog.setView(dialogView);
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "点错了", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialog.dismiss();
            }
        });
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //退出登录
                DbConfig dbConfig = new DbConfig(getApplicationContext());
                User user = dbConfig.getUser();
                if (user != null) {
                    user.setIsLogin("0");
                    DbManager dbManager = dbConfig.getDbManager();
                    try {
                        dbManager.saveOrUpdate(user);
                    } catch (DbException e) {
                    }
                    Intent intent = new Intent(SettingActivity.this, LoginActivity.class);
                    startActivity(intent);
                    //发送广播退出所有
                    Intent intent2 = new Intent("qd.xmjj.baseActivity");
                    intent2.putExtra("closeAll", 1);
                    sendBroadcast(intent2);//发送广播*/

                }
            }
        });
        dialog.show();
        //设置按钮颜色
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.theme_primary));
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.theme_primary));
    }

    private void showVoiceStateDialog() {
        final AlertDialog dialog = new AlertDialog.Builder(this).create();
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_choose, null);
        Switch swi_voice = (Switch) dialogView.findViewById(R.id.swi_voice);

        String isVoice = new DbConfig(getApplicationContext()).getUser().getIsVoice();
        if ("1".equals(isVoice)) {
            swi_voice.setChecked(true);
        } else if ("0".equals(isVoice)) {
            swi_voice.setChecked(false);
        }

        swi_voice.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {//选中开启
                    DbConfig dbConfig = new DbConfig(getApplicationContext());
                    User user = dbConfig.getUser();
                    user.setIsVoice("1");
                    try {
                        dbConfig.getDbManager().saveOrUpdate(user);

                    } catch (DbException e) {
                    }
                } else {//关闭
                    DbConfig dbConfig = new DbConfig(getApplicationContext());
                    User user = dbConfig.getUser();
                    user.setIsVoice("0");
                    try {
                        dbConfig.getDbManager().saveOrUpdate(user);

                    } catch (DbException e) {
                    }
                }
            }
        });

        dialog.setView(dialogView);
        dialog.show();

    }

}
