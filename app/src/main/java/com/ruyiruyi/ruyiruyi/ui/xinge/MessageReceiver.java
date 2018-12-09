package com.ruyiruyi.ruyiruyi.ui.xinge;

import android.content.Context;
import android.util.Log;

import com.tencent.android.tpush.XGPushBaseReceiver;
import com.tencent.android.tpush.XGPushClickedResult;
import com.tencent.android.tpush.XGPushRegisterResult;
import com.tencent.android.tpush.XGPushShowedResult;
import com.tencent.android.tpush.XGPushTextMessage;

/**
 * @作者 倾春
 * eml.dongbinjava@163.com
 * @创建日期 2018/8/13 9:39
 */

public class MessageReceiver extends XGPushBaseReceiver {
    private String TAG = "MessageReceiver";

    @Override
    public void onRegisterResult(Context context, int i, XGPushRegisterResult xgPushRegisterResult) {
        Log.e(TAG, "onRegisterResult:  ");
    }

    @Override
    public void onUnregisterResult(Context context, int i) {
        Log.e(TAG, "onUnregisterResult:  ");
    }

    @Override
    public void onSetTagResult(Context context, int i, String s) {
        Log.e(TAG, "onSetTagResult: ");
    }

    @Override
    public void onDeleteTagResult(Context context, int i, String s) {
        Log.e(TAG, "onDeleteTagResult: ");
    }

    @Override
    public void onTextMessage(Context context, XGPushTextMessage xgPushTextMessage) {
        Log.e(TAG, "onTextMessage: " + xgPushTextMessage.getTitle() + "  " + xgPushTextMessage.getContent());
    }

    @Override
    public void onNotifactionClickedResult(Context context, XGPushClickedResult xgPushClickedResult) {
        Log.e(TAG, "onNotifactionClickedResult: ");
    }

    @Override
    public void onNotifactionShowedResult(Context context, XGPushShowedResult xgPushShowedResult) {
        Log.e(TAG, "onNotifactionShowedResult: ");

    }
}
