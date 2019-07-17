package com.ruyiruyi.ruyiruyi.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ruyiruyi.ruyiruyi.R;
import com.ruyiruyi.ruyiruyi.db.DbConfig;
import com.ruyiruyi.ruyiruyi.db.model.User;
import com.ruyiruyi.ruyiruyi.ui.activity.base.RyBaseActivity;
import com.ruyiruyi.ruyiruyi.ui.model.BarCode;
import com.ruyiruyi.ruyiruyi.utils.RequestUtils;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;
import com.ruyiruyi.rylibrary.cell.ActionBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import rx.functions.Action1;

public class RightsActivity extends RyBaseActivity {
    private static final String TAG = RightsActivity.class.getSimpleName();
    private ActionBar actionBar;
    private TextView serviceEndYearView;
    private TextView tireFirstView;
    private TextView tireTwoView;
    private TextView tireThreeView;
    private TextView tireFourView;
    public List<BarCode> barCodeList;
    private String remainingServiceDays;
    public boolean hasTire = false;
    private TextView goBuyButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rights, R.id.my_action);
        actionBar = (ActionBar) findViewById(R.id.my_action);
        actionBar.setTitle("我的权益");
        ;
        actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(int var1) {
                switch ((var1)) {
                    case -1:
                        onBackPressed();
                        break;
                }
            }
        });
        barCodeList = new ArrayList<>();

        initView();
        initDataFromService();
    }
    private void initDataFromService() {
        User user = new DbConfig(this).getUser();
        int carId = user.getCarId();
        int userId = user.getId();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userCarId",carId);
            jsonObject.put("userId",userId);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "userEquityInfo/getUserEquityInfo");
        params.addBodyParameter("reqJson", jsonObject.toString());
        String token = new DbConfig(this).getToken();
        params.addParameter("token", token);
        params.setConnectTimeout(10000);
        x.http().post(params, new Callback.CommonCallback<String>() {


            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess: " + result);

                JSONObject jsonObject1 = null;
                try {
                    hasTire = true;
                    jsonObject1 = new JSONObject(result);
                    String status = jsonObject1.getString("status");
                    String msg = jsonObject1.getString("msg");
                    if (status.equals("1")){
                        hasTire = true;
                        JSONObject data = jsonObject1.getJSONObject("data");
                        JSONArray barCodeVoList = data.getJSONArray("barCodeVoList");
                        barCodeList.clear();
                        for (int i = 0; i < barCodeVoList.length(); i++) {
                            JSONObject barCodeObj = barCodeVoList.getJSONObject(i);
                            String barCode = barCodeObj.getString("barCode");
                            String usedDays = barCodeObj.getString("usedDays");
                            barCodeList.add(new BarCode(barCode,usedDays));
                        }
                        remainingServiceDays = data.getString("remainingServiceDays");

                        initData();
                    }else {
                        hasTire = false;
                        initData();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
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


    private void initData() {
        if (hasTire){
            serviceEndYearView.setText("剩余" + remainingServiceDays + "天");

            if (barCodeList.size() == 0){
                tireFirstView.setText("您还未购买轮胎！");
                tireFirstView.setVisibility(View.VISIBLE);
                tireTwoView.setVisibility(View.GONE);
                tireThreeView.setVisibility(View.GONE);
                tireFourView.setVisibility(View.GONE);
            }else if (barCodeList.size() == 1){
                tireFirstView.setVisibility(View.VISIBLE);
                tireTwoView.setVisibility(View.GONE);
                tireThreeView.setVisibility(View.GONE);
                tireFourView.setVisibility(View.GONE);
                tireFirstView.setText( "NO." + barCodeList.get(0).getBarCode()+"  " + barCodeList.get(0).getUsedDays() +"天");
            }else if (barCodeList.size() == 2){
                tireFirstView.setVisibility(View.VISIBLE);
                tireTwoView.setVisibility(View.VISIBLE);
                tireThreeView.setVisibility(View.GONE);
                tireFourView.setVisibility(View.GONE);
                tireFirstView.setText( "NO." + barCodeList.get(0).getBarCode()+"  " + barCodeList.get(0).getUsedDays()+"天");
                tireTwoView.setText( "NO." + barCodeList.get(1).getBarCode()+"  " + barCodeList.get(1).getUsedDays()+"天");
            }else if (barCodeList.size() == 3){
                tireFirstView.setVisibility(View.VISIBLE);
                tireTwoView.setVisibility(View.VISIBLE);
                tireThreeView.setVisibility(View.VISIBLE);
                tireFourView.setVisibility(View.GONE);
                tireFirstView.setText( "NO." + barCodeList.get(0).getBarCode()+"  " + barCodeList.get(0).getUsedDays()+"天");
                tireTwoView.setText( "NO." + barCodeList.get(1).getBarCode()+"  " + barCodeList.get(1).getUsedDays()+"天");
                tireThreeView.setText( "NO." + barCodeList.get(2).getBarCode()+"  " + barCodeList.get(2).getUsedDays()+"天");
            }else if (barCodeList.size() == 4){
                tireFirstView.setVisibility(View.VISIBLE);
                tireTwoView.setVisibility(View.VISIBLE);
                tireThreeView.setVisibility(View.VISIBLE);
                tireFourView.setVisibility(View.VISIBLE);
                tireFirstView.setText( "NO." + barCodeList.get(0).getBarCode()+"  " + barCodeList.get(0).getUsedDays()+"天");
                tireTwoView.setText( "NO." + barCodeList.get(1).getBarCode()+"  " + barCodeList.get(1).getUsedDays()+"天");
                tireThreeView.setText( "NO." + barCodeList.get(2).getBarCode()+"  " + barCodeList.get(2).getUsedDays()+"天");
                tireFourView.setText( "NO." + barCodeList.get(3).getBarCode()+"  " + barCodeList.get(3).getUsedDays()+"天");
            }
        }else {
            serviceEndYearView.setText("剩余0天");
            tireFirstView.setVisibility(View.VISIBLE);
            tireTwoView.setVisibility(View.GONE);
            tireThreeView.setVisibility(View.GONE);
            tireFourView.setVisibility(View.GONE);
            tireFirstView.setText( "您还未购买轮胎");
        }

    }

    private void initView() {
        goBuyButton = (TextView) findViewById(R.id.go_buy_button);
        serviceEndYearView = (TextView) findViewById(R.id.service_end_year_view);
        tireFirstView = (TextView) findViewById(R.id.tire_first_view);
        tireTwoView = (TextView) findViewById(R.id.tire_two_view);
        tireThreeView = (TextView) findViewById(R.id.tire_three_view);
        tireFourView = (TextView) findViewById(R.id.tire_four_view);

        RxViewAction.clickNoDouble(goBuyButton)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (hasTire){
                            startActivity(new Intent(getApplicationContext(),RenewActivity.class));
                        }else {
                            Toast.makeText(RightsActivity.this, "您还未购买轮胎！", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
