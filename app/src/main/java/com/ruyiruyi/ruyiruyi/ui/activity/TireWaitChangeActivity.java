package com.ruyiruyi.ruyiruyi.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.ruyiruyi.ruyiruyi.MainActivity;
import com.ruyiruyi.ruyiruyi.R;
import com.ruyiruyi.ruyiruyi.db.DbConfig;
import com.ruyiruyi.ruyiruyi.ui.activity.base.RYBaseActivity;
import com.ruyiruyi.ruyiruyi.ui.fragment.MyFragment;
import com.ruyiruyi.ruyiruyi.ui.multiType.TireWait;
import com.ruyiruyi.ruyiruyi.ui.multiType.TireWaitViewBinder;
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

import me.drakeet.multitype.MultiTypeAdapter;
import rx.functions.Action1;

import static me.drakeet.multitype.MultiTypeAsserts.assertAllRegistered;
import static me.drakeet.multitype.MultiTypeAsserts.assertHasTheSameAdapter;

public class TireWaitChangeActivity extends RYBaseActivity {
    private static final String TAG = TireWaitChangeActivity.class.getSimpleName();
    private ActionBar actionBar;
    private RecyclerView listView;
    private List<Object> items = new ArrayList<>();
    private MultiTypeAdapter adapter;
    private TextView tireChangeButton;
    public List<TireWait> tireWaitList;
    private String fromFragment = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tire_wait_change, R.id.my_action);
        actionBar = (ActionBar) findViewById(R.id.my_action);
        actionBar.setTitle("待更换轮胎");
        actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(int var1) {
                switch ((var1)) {
                    case -1:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        break;
                }
            }
        });
        Intent intent = getIntent();
        if (intent != null) {
            fromFragment = intent.getStringExtra(MyFragment.FROM_FRAGMENT);
        }

        tireWaitList = new ArrayList<>();
        initView();

        // initData();
        initDataFromService();
    }

    private void initDataFromService() {
        int userId = new DbConfig().getId();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userId", userId);
        } catch (JSONException e) {

        }
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "getUnusedShoeOrder");
        params.addBodyParameter("reqJson", jsonObject.toString());
        String token = new DbConfig().getToken();
        params.addParameter("token", token);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess: " + result.toString());
                JSONObject jsonObject1 = null;
                try {
                    jsonObject1 = new JSONObject(result);
                    String status = jsonObject1.getString("status");
                    String msg = jsonObject1.getString("msg");
                    if (status.equals("1")) {
                        tireWaitList.clear();
                        JSONArray data = jsonObject1.getJSONArray("data");
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject object = data.getJSONObject(i);
                            String name = object.getString("name");
                            String platNumber = object.getString("platNumber");
                            String orderNo = object.getString("orderNo");
                            String orderImg = object.getString("orderImg");
                            Boolean rejectStatus = object.getBoolean("rejectStatus");
                            int fontRearFlag = object.getInt("fontRearFlag");
                            int fontAmount = object.getInt("fontAmount");
                            int rearAmount = object.getInt("rearAmount");
                            String fontShoeName = "";
                            String tirePlace = "";
                            if (fontRearFlag == 0) {//前轮跟后轮
                                tirePlace = "前轮/后轮";
                                fontShoeName = object.getString("fontShoeName");
                                TireWait tireWait = new TireWait(orderImg, fontShoeName, name, fontAmount, platNumber, tirePlace, orderNo, rejectStatus);
                                tireWaitList.add(tireWait);
                            } else if (fontRearFlag == 1) {//前轮
                                tirePlace = "前轮";
                                fontShoeName = object.getString("fontShoeName");
                                TireWait tireWait = new TireWait(orderImg, fontShoeName, name, fontAmount, platNumber, tirePlace, orderNo, rejectStatus);
                                tireWaitList.add(tireWait);
                            } else { //后轮
                                tirePlace = "后轮";
                                fontShoeName = object.getString("rearShoeName");
                                TireWait tireWait = new TireWait(orderImg, fontShoeName, name, rearAmount, platNumber, tirePlace, orderNo, rejectStatus);
                                tireWaitList.add(tireWait);
                            }
                            initData();

                        }

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
        for (int i = 0; i < tireWaitList.size(); i++) {
            items.add(tireWaitList.get(i));
        }
        assertAllRegistered(adapter, items);
        adapter.notifyDataSetChanged();
    }

    private void initView() {
        listView = (RecyclerView) findViewById(R.id.tire_wait_list);
        tireChangeButton = (TextView) findViewById(R.id.tire_change_button);

        RxViewAction.clickNoDouble(tireChangeButton)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Intent intent = new Intent(getApplicationContext(), TireChangeActivity.class);
                        intent.putExtra(TireChangeActivity.CHANGE_TIRE, 0);
                        startActivity(intent);
                    }
                });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        listView.setLayoutManager(linearLayoutManager);
        adapter = new MultiTypeAdapter(items);
        register();

        listView.setAdapter(adapter);
        assertHasTheSameAdapter(listView, adapter);
    }

    private void register() {
        adapter.register(TireWait.class, new TireWaitViewBinder(this));
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra(MyFragment.FROM_FRAGMENT, fromFragment);
        startActivity(intent);
    }
}
