package com.example.warehouse.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.example.warehouse.db.DbConfig;
import com.example.warehouse.db.TireType;
import com.example.warehouse.utils.RequestUtils;
import com.example.warehouse.utils.UtilsRY;

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

public class LauncherDownlodeService extends Service {


    private static final String TAG = LauncherDownlodeService.class.getSimpleName();
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    //获取轮胎型号
                    initTireType();
                    break;

            }
        }
    };

    public LauncherDownlodeService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Message message = new Message();
        message.what = 1;
        mHandler.sendMessage(message);
    }

    public enum Control {
        PLAY, PAUSE, STOP
    }

    /**
     * 下载轮胎数据
     */
    private void initTireType() {
        DbConfig dbConfig = new DbConfig(this);
        DbManager db = dbConfig.getDbManager();
        List<TireType> tireTypeList = null;
        try {
            tireTypeList = db.selector(TireType.class)
                    .orderBy("time")
                    .findAll();
        } catch (DbException e) {
        }
        JSONObject jsonObject = new JSONObject();

        try {
            if (tireTypeList == null) {
                jsonObject.put("time", "2000-00-00 00:00:00");
            } else {
                String time = tireTypeList.get(tireTypeList.size() - 1).getTime();
                jsonObject.put("time", time);
            }
        } catch (JSONException e) {

        }
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "stocks/getAllShoes");
        params.addBodyParameter("reqJson", jsonObject.toString());
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject1 = new JSONObject(result);
                    String status = jsonObject1.getString("status");
                    String msg = jsonObject1.getString("msg");
                    if (status.equals("1")) {
                        JSONArray data = jsonObject1.getJSONArray("data");
                        List<TireType> tireTypeArrayList = new ArrayList<TireType>();
                        for (int i = 0; i < data.length(); i++) {
                            int id = data.getJSONObject(i).getInt("id");
                            String inchMm = data.getJSONObject(i).getString("inchMm");
                            String inch = data.getJSONObject(i).getString("inch");
                            String diameter = data.getJSONObject(i).getString("diameter");
                            String size = data.getJSONObject(i).getString("size");
                            String speed = data.getJSONObject(i).getString("speed");
                            String brand = data.getJSONObject(i).getString("brand");
                            String flgureName = data.getJSONObject(i).getString("flgureName");

                            long time = data.getJSONObject(i).getLong("time");
                            String timestampToStringAll = new UtilsRY().getTimestampToStringAll(time);


                            tireTypeArrayList.add(new TireType(id,inchMm,inch,diameter,size,speed,brand,flgureName,timestampToStringAll));
                        }

                        saveTireTypeIntoDb(tireTypeArrayList);
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

    private void saveTireTypeIntoDb(List<TireType> tireTypeArrayList) {
        Log.e(TAG, "saveTireTypeIntoDb: 保存数据成果" );
        DbConfig dbConfig = new DbConfig(this);
        DbManager db = dbConfig.getDbManager();
        try {
            db.saveOrUpdate(tireTypeArrayList);
/*
            Message message = new Message();
            message.what = 2;
            mHandler.sendMessage(message);*/

        } catch (DbException e) {

        }
    }
}


