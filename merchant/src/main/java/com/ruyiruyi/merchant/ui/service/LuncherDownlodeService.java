package com.ruyiruyi.merchant.ui.service;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.ruyiruyi.merchant.db.DbConfig;
import com.ruyiruyi.merchant.db.model.Province;
import com.ruyiruyi.merchant.utils.UtilsRY;
import com.ruyiruyi.merchant.utils.UtilsURL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.DbManager;
import org.xutils.common.Callback;
import org.xutils.ex.DbException;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

public class LuncherDownlodeService extends Service {
    private String TAG = LuncherDownlodeService.class.getSimpleName();


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
//                    initProvinceData();
                    initProvice();
                    break;
                case 2:
                    //发送广播(插入数据完毕)
                    Intent intent2 = new Intent();
                    intent2.putExtra("count", 100);
                    intent2.setAction("com.ruyiruyi.merchant.ui.service.LuncherDownlodeService");
                    Log.e(TAG, "handleMessage: 发送广播(插入数据完毕)");
                    sendBroadcast(intent2);
                    break;
            }
        }
    };

    private int startId;

    public enum Control {
        PLAY, PAUSE, STOP
    }

    public LuncherDownlodeService() {
    }

    @Override
    public void onCreate() {
        Message message = new Message();
        message.what = 1;
        mHandler.sendMessage(message);
        Log.e(TAG, "onCreate");
        super.onCreate();
    }


    /*
    * 未优化版
    * */
    private void initProvinceData() {
        DbConfig dbConfig = new DbConfig(this);
        DbManager db = dbConfig.getDbManager();
        List<Province> provinceList = null;
        try {
            provinceList = db.selector(Province.class)
                    .orderBy("time")
                    .findAll();
        } catch (DbException e) {
        }
        JSONObject jsonObject = new JSONObject();
        try {
            //时间请求 ！！
            if (provinceList == null) {
                jsonObject.put("time", "2000-00-00 00:00:00");
            } else {
                String time = provinceList.get(provinceList.size() - 1).getTime();
                jsonObject.put("time", time);
            }

        } catch (JSONException e) {

        }
        RequestParams params = new RequestParams(UtilsURL.REQUEST_URL + "getAllPositon");
        params.addBodyParameter("reqJson", jsonObject.toString());
        Log.e(TAG, "initProvinceData: params = " + params);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess: " + result);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(result);
                    String status = jsonObject.getString("status");
                    String msg = jsonObject.getString("msg");
                    JSONArray data = jsonObject.getJSONArray("data");
                    Log.e(TAG, "onSuccess: getData and To Db ??  status = " + status + "msg = " + msg + "data = " + data.toString());

                    //下载完毕
                    saveProvinceToDb(data);//异步存储

                    Message message = new Message();
                    message.what = 3;
                    mHandler.sendMessageDelayed(message, 2000);

                } catch (JSONException e) {
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                //下载失败
                Toast.makeText(getApplicationContext(), "网络异常,请检查网络!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private void saveProvinceToDb(JSONArray data) {
        Province province;
        DbManager db = (new DbConfig(getApplicationContext())).getDbManager();
        for (int i = 0; i < data.length(); i++) {
            province = new Province();
            try {
                JSONObject obj = (JSONObject) data.get(i);
                //保存并转换time请求的time
                long time = obj.getLong("time");
                String timestampToStringAll = new UtilsRY().getTimestampToStringAll(time);
                province.setTime(timestampToStringAll);
                province.setDefinition(obj.getInt("definition"));
                province.setFid(obj.getInt("fid"));
                province.setId(obj.getInt("id"));
                province.setName(obj.getString("name"));
//                Log.e(TAG, "saveProvinceToDb: definition==>" + province.getDefinition());
                db.saveOrUpdate(province);

            } catch (JSONException e) {

            } catch (DbException e) {

            }
        }
        //数据插入完毕
        Message message = new Message();
        message.what = 2;
        mHandler.sendMessage(message);


    }


    /*
    * 优化版
    * */
    private void initProvice() {
        DbConfig dbConfig = new DbConfig(this);
        DbManager db = dbConfig.getDbManager();
        List<Province> provinceList = null;
        try {
            provinceList = db.selector(Province.class)
                    .orderBy("time")
                    .findAll();
        } catch (DbException e) {
        }
        JSONObject jsonObject = new JSONObject();
        try {
            if (provinceList == null) {
                jsonObject.put("time", "2000-00-00 00:00:00");
            } else {
                String time = provinceList.get(provinceList.size() - 1).getTime();
                jsonObject.put("time", time);
            }
        } catch (JSONException e) {

        }
        RequestParams params = new RequestParams(UtilsURL.REQUEST_URL + "getAllPositon");
        params.addBodyParameter("reqJson", jsonObject.toString());
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess:getTireTypeData---" + result);
                try {
                    JSONObject jsonObject1 = new JSONObject(result);
                    String status = jsonObject1.getString("status");
                    String msg = jsonObject1.getString("msg");
                    if (status.equals("1")) {
                        JSONArray data = jsonObject1.getJSONArray("data");
                        List<Province> provinceArrayList = new ArrayList<Province>();
                        for (int i = 0; i < data.length(); i++) {

                            String name = data.getJSONObject(i).getString("name");
                            int id = data.getJSONObject(i).getInt("id");
                            int definition = data.getJSONObject(i).getInt("definition");
                            int fid = data.getJSONObject(i).getInt("fid");

                            long time = data.getJSONObject(i).getLong("time");
                            String timestampToStringAll = new UtilsRY().getTimestampToStringAll(time);


                            provinceArrayList.add(new Province(id, fid, definition, name, timestampToStringAll));
                        }

                        //下载完毕
                        saveProvinceIntoDb(provinceArrayList);
                    } else {
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {

                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private void saveProvinceIntoDb(List<Province> provinceArrayList) {
        DbConfig dbConfig = new DbConfig(this);
        DbManager db = dbConfig.getDbManager();
        try {
            db.saveOrUpdate(provinceArrayList);

            //数据插入完毕
            Message message = new Message();
            message.what = 2;
            mHandler.sendMessage(message);

        } catch (DbException e) {

        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        this.startId = startId;
        Log.e(TAG, "onStartCommand---startId: " + startId);
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            Control control = (Control) bundle.getSerializable("Key");
            if (control != null) {
                switch (control) {
                    case PLAY:
                        play();
                        break;
                    case PAUSE:
                        pause();
                        break;
                    case STOP:
                        stop();
                        break;
                }
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy");
/*        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }*/
        super.onDestroy();
    }

    private void play() {
/*        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        }*/
    }

    private void pause() {
/*        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }*/
    }

    private void stop() {
/*        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }*/
        stopSelf(startId);
        onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.e(TAG, "onBind");
        throw new UnsupportedOperationException("Not yet implemented");
    }

}