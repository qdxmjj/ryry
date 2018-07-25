package com.ruyiruyi.merchant.ui.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;

import com.ruyiruyi.merchant.R;

public class BackgroundMp3Service extends Service {
    private String messageWeb;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    Log.e(TAG, "handleMessage: 播放完毕");
                    BackgroundMp3Service.this.onDestroy();
                    break;
            }
        }
    };
    private String TAG = BackgroundMp3Service.class.getSimpleName();


    @Override
    public void onCreate() {
        super.onCreate();
    }

    private void startVoice() {
        Log.e(TAG, "onCreate:  messageWeb = " + messageWeb);
        //接下来根据messageWeb分类通知语音播报

      /*  //方案二：网络音频
        String stringExtra = "http://zjlt.sc.chinaz.com/Files/DownLoad/sound1/201511/6553.mp3";
        Uri parse = Uri.parse(stringExtra);*/

        try {
          /*  //方案二：网络音频
            MediaPlayer mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(BackgroundMp3Service.this, parse);
            mediaPlayer.prepare();// 进行缓冲
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    mediaPlayer.start();
                }
            });*/

            //方案一：本地音频
            MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.neworder);
            mediaPlayer.start();
            mHandler.sendEmptyMessageDelayed(1, 3000);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        messageWeb = intent.getStringExtra("messageWeb");
        startVoice();
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}