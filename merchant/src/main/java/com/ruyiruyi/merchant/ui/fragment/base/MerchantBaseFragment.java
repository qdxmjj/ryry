package com.ruyiruyi.merchant.ui.fragment.base;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.ruyiruyi.merchant.MyApplication;
import com.ruyiruyi.merchant.R;
import com.ruyiruyi.merchant.db.DbConfig;
import com.ruyiruyi.merchant.db.model.User;
import com.ruyiruyi.merchant.ui.activity.LoginActivity;
import com.ruyiruyi.rylibrary.base.BaseFragment;

import org.xutils.DbManager;
import org.xutils.ex.DbException;

public class MerchantBaseFragment extends BaseFragment {

    private Activity mActivity;


    /*
     * 商家版Token错误跳转dialog
     * */
    public void showMerchantTokenDialog(String error) {
        AlertDialog dialog = new AlertDialog.Builder(getContext()).create();
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_error, null);
        TextView error_text = (TextView) dialogView.findViewById(R.id.error_text);
        error_text.setText(error);
        dialog.setTitle("如意如驿商家版");
        dialog.setIcon(R.drawable.ic_launcher);
        dialog.setView(dialogView);
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                //登录失效 更新本地User信息
                DbConfig dbConfig = new DbConfig(getActivity());
                User user = dbConfig.getUser();
                user.setIsLogin("0");
                DbManager db = dbConfig.getDbManager();

                try {
                    db.saveOrUpdate(user);
                } catch (DbException e) {

                }
                //即将跳转登录界面
                getActivity().finish();
                startActivity(new Intent(getContext(), LoginActivity.class));
            }
        });
        dialog.show();
        //设置按钮颜色
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.theme_primary));
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.theme_primary));
    }


    /*
    * 判断是否登录 （未登录提示登录跳转）
    *
    * 用法：
            //判断是否登录（未登录提示登录）
            if (!judgeIsLogin()) {
                return;
            }
    *
    * */
    public boolean judgeIsLogin() {
        if (!new DbConfig(getActivity()).getIsLogin()) {
            AlertDialog dialog = new AlertDialog.Builder(getContext()).create();
            View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_error, null);
            TextView error_text = (TextView) dialogView.findViewById(R.id.error_text);
            error_text.setText("您还没有登录");
            dialog.setTitle("如意如驿商家版");
            dialog.setIcon(R.drawable.ic_launcher);
            dialog.setView(dialogView);
            dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "暂不登录", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            dialog.setButton(DialogInterface.BUTTON_POSITIVE, "前往登录", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    getActivity().finish();
                    startActivity(new Intent(getContext(), LoginActivity.class));
                }
            });
            dialog.show();
            //设置按钮颜色
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.theme_primary));
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.theme_primary));
        }
        return new DbConfig(getActivity()).getIsLogin();
    }




    public Context getmContext() {
        if (mActivity == null) {
            return MyApplication.getInstance();
        }
        return mActivity;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = getActivity();
    }


}