package com.ruyiruyi.ruyiruyi.ui.activity;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.ruyiruyi.ruyiruyi.R;
import com.ruyiruyi.ruyiruyi.db.DbConfig;
import com.ruyiruyi.ruyiruyi.ui.activity.base.RyBaseActivity;
import com.ruyiruyi.ruyiruyi.ui.cell.TSeekBar;
import com.ruyiruyi.ruyiruyi.utils.RequestUtils;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;
import com.ruyiruyi.rylibrary.cell.ActionBar;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import rx.functions.Action1;

public class YearChooseActivity extends RyBaseActivity {
    private static final String TAG = YearChooseActivity.class.getSimpleName();
    private ActionBar actionBar;
    private TSeekBar seekBar;
    private String tiresize;
    private String fontrearflag;
    private int serviceyear;
    private TextView chooseYear;
    private int carid;
    private int usercarid;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_year_choose);
        actionBar = (ActionBar) findViewById(R.id.my_action);
        actionBar.setTitle("请选择服务年限");;
        actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick(){
            @Override
            public void onItemClick(int var1) {
                switch ((var1)){
                    case -1:
                        onBackPressed();
                        break;
                }
            }
        });

        Intent intent = getIntent();
        tiresize = intent.getStringExtra("TIRESIZE");
        fontrearflag = intent.getStringExtra("FONTREARFLAG");
        carid = intent.getIntExtra("CARID",0);
        usercarid = intent.getIntExtra("USERCARID",0);
        serviceyear = Integer.parseInt(intent.getStringExtra("SERVICEYEAR"));

        progressDialog = new ProgressDialog(this);


        initView();
    }

    private void initView() {
        Log.e(TAG, "initView:--- " + serviceyear );
        chooseYear = (TextView) findViewById(R.id.choose_year_view);
        seekBar = (TSeekBar) findViewById(R.id.seekBar);
        seekBar.setMax(serviceyear);
        seekBar.setProgress(100);

        RxViewAction.clickNoDouble(chooseYear)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Log.e(TAG, "call: " + seekBar.currentYear());
                        //修改服务年限
                        updateServiceYear();
                    }
                });



    }

    private void updateServiceYear() {
        int userId = new DbConfig(getApplicationContext()).getId();
        JSONObject jsonObject = new JSONObject();
        showDialogProgress(progressDialog,"正在提交中...");
        try {
            jsonObject.put("id",usercarid);
            jsonObject.put("userId",userId);
            jsonObject.put("carId",carid);
            jsonObject.put("serviceYearLength",seekBar.currentYear());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "userCar/updateUserCarInfo");
        params.addBodyParameter("reqJson",jsonObject.toString());
        String token = new DbConfig(this).getToken();
        params.addParameter("token",token);
        Log.e(TAG, "updateCar: " + params);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                JSONObject jsonObject1 = null;
                try {
                    jsonObject1 = new JSONObject(result);
                    String status = jsonObject1.getString("status");
                    String msg = jsonObject1.getString("msg");
                    if (status.equals("1")){
                        Intent intent = new Intent(getApplicationContext(), CarFigureActivity.class);
                        intent.putExtra("TIRESIZE", tiresize);
                        intent.putExtra("FONTREARFLAG", "0");
                        startActivity(intent);
                    }else if (status.equals("-999")){
                        showUserTokenDialog("您的账号在其它设备登录,请重新登录");
                    }else {
                        Toast.makeText(YearChooseActivity.this, msg, Toast.LENGTH_SHORT).show();
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
                hideDialogProgress(progressDialog);
            }
        });


    }
}
