package com.ruyiruyi.ruyiruyi.utils;

import android.content.Context;
import android.content.Intent;

import com.ruyiruyi.ruyiruyi.MainActivity;
import com.ruyiruyi.ruyiruyi.ui.activity.LoginActivity;

public class UIOpenHelper {

    public static void openLogin(Context context){
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    public static void openHomeActivity(Context context){
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }
}