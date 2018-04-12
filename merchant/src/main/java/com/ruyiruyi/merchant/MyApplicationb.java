package com.ruyiruyi.merchant;

import android.app.Application;

import org.xutils.x;

public class MyApplicationb extends Application {

    @Override
    public void onCreate() {
        x.Ext.init(this);
        x.Ext.setDebug(true);
    }
}