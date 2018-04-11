package com.ruyiruyi.ruyiruyi.ui.service;

import android.app.Service;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;

public class CodeTimerService extends Service {

    public static final java.lang.String IN_RUNNING = "AA";
    public static final java.lang.String END_RUNNING = "BB";
    public CountDownTimer mCodeTimer;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mCodeTimer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                // 广播剩余时间
                broadcastUpdate(IN_RUNNING, millisUntilFinished / 1000 + "");
            }

            @Override
            public void onFinish() {
                // 广播倒计时结束
                broadcastUpdate(END_RUNNING);
                // 停止服务
                stopSelf();
            }
        };
        // 开始倒计时
        mCodeTimer.start();
        return super.onStartCommand(intent, flags, startId);
    }

    // 发送广播
    private void broadcastUpdate(final String action) {
        final Intent intent = new Intent(action);
        sendBroadcast(intent);
    }

    // 发送带有数据的广播
    private void broadcastUpdate(final String action, String time) {
        final Intent intent = new Intent(action);
        intent.putExtra("time", time);
        sendBroadcast(intent);
    }

    public CodeTimerService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
