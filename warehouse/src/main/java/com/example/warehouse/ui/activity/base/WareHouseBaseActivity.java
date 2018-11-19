package com.example.warehouse.ui.activity.base;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.warehouse.R;
import com.example.warehouse.db.DbConfig;
import com.example.warehouse.db.User;
import com.example.warehouse.ui.activity.LoginActivity;
import com.ruyiruyi.rylibrary.base.BaseActivity;

import org.xutils.DbManager;
import org.xutils.ex.DbException;

public class WareHouseBaseActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return;
        } else {
            Window window = this.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(com.ruyiruyi.rylibrary.R.color.theme_primary));
        }

    }

    public void showDialogProgress(ProgressDialog dialog, String message) {
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setMessage(message);
        dialog.show();
    }

    /*
  * 用户版Token错误跳转dialog
  * */
    public void showUserTokenDialog(String error) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_error, null);
        TextView error_text = (TextView) dialogView.findViewById(R.id.error_text);
        error_text.setText(error);
        dialog.setTitle("如意如驿");
        dialog.setIcon(R.drawable.ic_logo_login);
        dialog.setView(dialogView);
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                //登录失效 更新本地User信息
                DbConfig dbConfig = new DbConfig(getApplicationContext());
                User user = dbConfig.getUser();
                user.setIsLogin("0");
                DbManager db = dbConfig.getDbManager();

                try {
                    db.saveOrUpdate(user);
                } catch (DbException e) {

                }
                //即将跳转登录界面
                finish();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });
        dialog.show();
    }
}
