package com.ruyiruyi.ruyiruyi.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.ruyiruyi.ruyiruyi.R;
import com.ruyiruyi.ruyiruyi.db.DbConfig;
import com.ruyiruyi.ruyiruyi.db.model.User;
import com.ruyiruyi.ruyiruyi.ui.activity.base.RyBaseActivity;
import com.ruyiruyi.ruyiruyi.ui.multiType.Cxwy;
import com.ruyiruyi.ruyiruyi.ui.multiType.CxwyViewBinder;
import com.ruyiruyi.ruyiruyi.utils.FullyLinearLayoutManager;
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

public class CxwyActivity extends RyBaseActivity {
    private static final String TAG = CxwyActivity.class.getSimpleName();
    private ActionBar actionBar;
    private RecyclerView listView;
    private TextView save_car;
    private List<Object> items = new ArrayList<>();
    private MultiTypeAdapter adapter;
    public List<Cxwy> cxwyList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cxwy, R.id.my_action);
        actionBar = (ActionBar) findViewById(R.id.my_action);
        actionBar.setTitle("畅行无忧");
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
        cxwyList = new ArrayList<>();

        for (int i = 0; i < 8; i++) {
            if (i % 2 != 0) {
                cxwyList.add(new Cxwy(i, "2018.08.0" + i, "2019.08.0" + i, 1));
            } else {
                cxwyList.add(new Cxwy(i, "2018.08.0" + i, "2019.08.0" + i, 2));
            }

        }
        initView();
        initDataFromService();
        bindView();
        //initData();
    }

    private void bindView() {
        RxViewAction.clickNoDouble(save_car).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                startActivity(new Intent(getApplicationContext(), BuyCxwyActivity.class));
            }
        });
    }

    private void initDataFromService() {
        User user = new DbConfig().getUser();
        int userId = user.getId();
        int carId = user.getCarId();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userId", userId);
            jsonObject.put("userCarId", carId);
        } catch (JSONException e) {
        }
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "userCarInfo/queryCarCxwyInfo");
        params.addBodyParameter("reqJson", jsonObject.toString());
        String token = new DbConfig().getToken();
        params.addParameter("token", token);
        Log.e(TAG, "initDataFromService: " + params.toString());
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess: " + result);
                JSONObject jsonObject1 = null;
                try {
                    jsonObject1 = new JSONObject(result);
                    String status = jsonObject1.getString("status");
                    String msg = jsonObject1.getString("msg");
                    if (status.equals("1")) {
                        JSONArray data = jsonObject1.getJSONArray("data");
                        cxwyList.clear();
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject object = data.getJSONObject(i);
                            long cxwyBuytime = object.getLong("cxwyBuytime");
                            String cxwyBuyTimeStr = new UtilsRY().getTimestampToStringAll(cxwyBuytime);
                            long cxwyEndtime = object.getLong("cxwyEndtime");
                            String cxwyEndTimeStr = new UtilsRY().getTimestampToStringAll(cxwyEndtime);
                            long cxwyStarttime = object.getLong("cxwyStarttime");
                            String cxwyStartTimeStr = new UtilsRY().getTimestampToStringAll(cxwyStarttime);
                            int cxwyState = object.getInt("cxwyState");
                            int cxwyTypeId = object.getInt("cxwyTypeId");// 1:有期限 2:没有期限
                            int getWay = object.getInt("getWay");//获取方式 1 系统赠送 2 正常购买
                            int id = object.getInt("id");
                            Cxwy cxwy = new Cxwy(id, cxwyStartTimeStr, cxwyEndTimeStr, getWay);
                            cxwyList.add(cxwy);
                        }
                        initData();
                    } else if (status.equals("-999")) {
                        showUserTokenDialog("您的账号在其它设备登录,请重新登录");
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

    private void initData() {
        items.clear();
        for (int i = 0; i < cxwyList.size(); i++) {
            items.add(cxwyList.get(i));
        }
        assertAllRegistered(adapter, items);
        adapter.notifyDataSetChanged();
    }

    private void initView() {
        save_car = (TextView) findViewById(R.id.save_car);
        listView = (RecyclerView) findViewById(R.id.cxwy_list);
        LinearLayoutManager linearLayoutManager = new FullyLinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        listView.setLayoutManager(linearLayoutManager);
        adapter = new MultiTypeAdapter(items);
        register();

        listView.setAdapter(adapter);
        assertHasTheSameAdapter(listView, adapter);
    }

    private void register() {
        adapter.register(Cxwy.class, new CxwyViewBinder());
    }
}
