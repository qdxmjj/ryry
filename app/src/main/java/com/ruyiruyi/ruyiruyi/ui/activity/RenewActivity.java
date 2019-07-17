package com.ruyiruyi.ruyiruyi.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.ruyiruyi.ruyiruyi.R;
import com.ruyiruyi.ruyiruyi.db.DbConfig;
import com.ruyiruyi.ruyiruyi.db.model.User;
import com.ruyiruyi.ruyiruyi.ui.activity.base.RyBaseActivity;
import com.ruyiruyi.ruyiruyi.ui.model.BarCode;
import com.ruyiruyi.ruyiruyi.ui.multiType.RenewYear;
import com.ruyiruyi.ruyiruyi.ui.multiType.RenewYearViewBinder;
import com.ruyiruyi.ruyiruyi.utils.RequestUtils;
import com.ruyiruyi.ruyiruyi.utils.UtilsRY;
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

import me.drakeet.multitype.MultiTypeAdapter;
import rx.functions.Action1;

import static me.drakeet.multitype.MultiTypeAsserts.assertAllRegistered;
import static me.drakeet.multitype.MultiTypeAsserts.assertHasTheSameAdapter;

public class RenewActivity extends RyBaseActivity implements RenewYearViewBinder.OnRenewChoosListener {
    private static final String TAG = RenewActivity.class.getSimpleName();
    private ActionBar actionBar;
    private RecyclerView listView;
    private List<Object> items = new ArrayList<>();
    private MultiTypeAdapter adapter;
    public List<RenewYear> renewYearList;
    private TextView serviceEndView;
    private TextView dayHaveView;

    private String remainingServiceDays;
    private String serviceEndDate;
    public boolean hasTire = false;
    private TextView goBuyButton;

    private long serviceEndDateLong;
    private int tireCount;

    private int serviceYearLength;

    public int renewalYear = 0;
    public String renewalPrice = "";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_renew, R.id.my_action);
        actionBar = (ActionBar) findViewById(R.id.my_action);
        actionBar.setTitle("续费");
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

        renewYearList = new ArrayList<>();
        intiView();
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

                JSONObject jsonObject1 = null;
                try {
                    jsonObject1 = new JSONObject(result);
                    String status = jsonObject1.getString("status");
                    String msg = jsonObject1.getString("msg");
                    if (status.equals("1")){
                        hasTire =true;
                        JSONObject data = jsonObject1.getJSONObject("data");
                        JSONArray barCodeVoList = data.getJSONArray("barCodeVoList");
                        tireCount = barCodeVoList.length();
                        JSONObject renewalPriceInfo = data.getJSONObject("renewalPriceInfo");
                        renewYearList.clear();
                        for (int i = 1; i <= renewalPriceInfo.length(); i++) {
                            String price = renewalPriceInfo.getString(i+"");
                            renewYearList.add(new RenewYear(i,price));
                        }
                        serviceEndDateLong = data.getLong("serviceEndDate");
                        serviceEndDate = new UtilsRY().getTimestampToStringAll(data.getLong("serviceEndDate")).substring(0,10);
                        remainingServiceDays = data.getString("remainingServiceDays");
                        serviceYearLength = data.getInt("serviceYearLength");

                        initData();
                    }else if (status.equals("-1")){
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
            serviceEndView.setText(serviceEndDate+"到期");
            dayHaveView.setText(remainingServiceDays +"天");
            items.clear();
            for (int i = 0; i < renewYearList.size(); i++) {
                items.add(renewYearList.get(i));
            }
            assertAllRegistered(adapter, items);
            adapter.notifyDataSetChanged();
        }else {

        }

    }

    private void intiView() {
        goBuyButton = (TextView) findViewById(R.id.go_buy_button_rennew);
        serviceEndView = (TextView) findViewById(R.id.service_end_year_renew_view);
        dayHaveView = (TextView) findViewById(R.id.have_day_view);
        listView = (RecyclerView) findViewById(R.id.renew_listview);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        listView.setLayoutManager(layoutManager);
        adapter = new MultiTypeAdapter(items);
        register();

        listView.setAdapter(adapter);
        assertHasTheSameAdapter(listView, adapter);

        RxViewAction.clickNoDouble(goBuyButton)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        postData();
                    }
                });
    }

    private void postData() {

        for (int i = 0; i < renewYearList.size(); i++) {
            if (renewYearList.get(i).isChoose) {
                Log.e(TAG, "postData: renewalPrice1---" + renewYearList.get(i).getPrice());
                renewalYear = renewYearList.get(i).getYear();
                renewalPrice = renewYearList.get(i).getPrice();
            }
        }
        User user = new DbConfig(this).getUser();
        int carId = user.getCarId();
        int userId = user.getId();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userCarId",carId);
            jsonObject.put("userId",userId);
            jsonObject.put("shoeNum",tireCount);
            jsonObject.put("serviceYearLength",serviceYearLength);
            jsonObject.put("renewalYear",renewalYear);
            Log.e(TAG, "postData: renewalPrice1-1--" +renewalPrice);
            jsonObject.put("renewalPrice",renewalPrice);
            jsonObject.put("serviceEndTime",serviceEndDateLong);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e(TAG, "postData: ---");
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "renewalOrderInfo/addRenewalOrder");
        params.addBodyParameter("reqJson", jsonObject.toString());
        String token = new DbConfig(this).getToken();
        params.addParameter("token", token);
        params.setConnectTimeout(10000);
        x.http().post(params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess: " +result );
                JSONObject jsonObject1 = null;
                try {
                    jsonObject1 = new JSONObject(result);
                    String status = jsonObject1.getString("status");
                    String msg = jsonObject1.getString("msg");
                    if (status.equals("1")){

                     //   JSONObject data = jsonObject1.getJSONObject("data");
                        String orderNo =  jsonObject1.getString("data");

                        Intent intent = new Intent(getApplicationContext(), PaymentActivity.class);
                        intent.putExtra(PaymentActivity.ALL_PRICE, Double.valueOf(renewalPrice) );
                         intent.putExtra(PaymentActivity.ORDERNO, orderNo);
                        intent.putExtra(PaymentActivity.ORDER_TYPE, 8);
                        startActivity(intent);
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

    private void register() {
        RenewYearViewBinder renewYearViewBinder = new RenewYearViewBinder(this);
        renewYearViewBinder.setListener(this);
        adapter.register(RenewYear.class, renewYearViewBinder);
    }

    /**
     * 年份选择的回调
     * @param year
     */
    @Override
    public void onReneItemClickListener(int year) {
        for (int i = 0; i < renewYearList.size(); i++) {
            if (renewYearList.get(i).getYear() == year) {
                renewYearList.get(i).setChoose(true);
            }else {
                renewYearList.get(i).setChoose(false);
            }
        }
        initData();
    }
}
