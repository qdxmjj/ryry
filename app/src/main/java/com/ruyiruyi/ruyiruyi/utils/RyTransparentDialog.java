package com.ruyiruyi.ruyiruyi.utils;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.widget.ImageView;
import android.widget.TextView;

import com.ruyiruyi.ruyiruyi.R;

/**
 * @作者 倾春
 * eml.dongbinjava@163.com
 * @创建日期 2018/8/10 9:47
 */

public class RyTransparentDialog extends Dialog {



    public RyTransparentDialog(@NonNull Context context) {
        super(context, R.style.myTransparentDialog);
        setCanceledOnTouchOutside(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
