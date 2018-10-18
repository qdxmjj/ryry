package com.ruyiruyi.merchant.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ruyiruyi.merchant.R;
import com.ruyiruyi.merchant.cell.CircleImageView;
import com.ruyiruyi.merchant.ui.activity.base.MerchantBaseActivity;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;
import com.ruyiruyi.rylibrary.cell.ActionBar;

import rx.functions.Action1;

public class PutForwardLoginWXActivity extends MerchantBaseActivity {

    private ActionBar mActionBar;
    private CircleImageView civ_weixin;
    private TextView tv_nickname;
    private EditText et_wxrealname;
    private TextView tv_submit;

    private String mNickname;
    private String headimgurl;
    private String weixinRealName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_put_forward_login_wx);
        mActionBar = (ActionBar) findViewById(R.id.acbars);
        mActionBar.setTitle("补充提现信息");
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

        //获取传递数据
        Intent intent = getIntent();
        mNickname = intent.getStringExtra("mNickname");
        headimgurl = intent.getStringExtra("headimgurl");

        initView();
        bindView();


    }

    private void bindView() {
        //提交
        RxViewAction.clickNoDouble(tv_submit).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                judgebeforeSubmit();
            }
        });
    }

    private void judgebeforeSubmit() {
        if (et_wxrealname.getText() == null || et_wxrealname.getText().length() == 0) {
            Toast.makeText(this, "请填写姓名信息", Toast.LENGTH_SHORT).show();
            return;
        }
        weixinRealName = et_wxrealname.getText().toString();
        showSubmitDialog();
    }

    private void showSubmitDialog() {
        final AlertDialog dialog = new AlertDialog.Builder(this).create();
        dialog.setTitle("如驿如意商家版");
        dialog.setIcon(R.drawable.ic_launcher);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_error, null);
        TextView error_text = (TextView) dialogView.findViewById(R.id.error_text);
        error_text.setText("请再次确认\"" + weixinRealName + "\"与微信号\"" + mNickname + "\"的实名信息一致");
        dialog.setView(dialogView);
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialog.dismiss();
            }
        });
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "确认一致", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                closeResult();
            }
        });
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.theme_primary));
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.theme_primary));
    }

    private void initView() {
        civ_weixin = findViewById(R.id.civ_weixin);
        tv_nickname = findViewById(R.id.tv_nickname);
        et_wxrealname = findViewById(R.id.et_wxrealname);
        tv_submit = findViewById(R.id.tv_submit);

        //设置数据
        Glide.with(this).load(headimgurl).into(civ_weixin);
        tv_nickname.setText(mNickname);
    }

    /**
     * forresult返回数据
     */
    private void closeResult() {
        Intent intent = new Intent();
        intent.putExtra("isloginSuccess", true);
        intent.putExtra("weixinRealName", weixinRealName);
        PutForwardLoginWXActivity.this.setResult(RESULT_OK, intent);
        PutForwardLoginWXActivity.this.finish();
    }
}
