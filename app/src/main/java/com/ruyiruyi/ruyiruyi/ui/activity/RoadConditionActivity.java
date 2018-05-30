package com.ruyiruyi.ruyiruyi.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.ruyiruyi.ruyiruyi.R;
import com.ruyiruyi.ruyiruyi.ui.activity.base.RYBaseActivity;
import com.ruyiruyi.ruyiruyi.ui.multiType.RoadChoose;
import com.ruyiruyi.ruyiruyi.ui.multiType.RoadChooseViewBinder;
import com.ruyiruyi.ruyiruyi.ui.multiType.RoadType;
import com.ruyiruyi.ruyiruyi.ui.multiType.RoadTypeViewBinder;
import com.ruyiruyi.ruyiruyi.utils.RequestUtils;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;
import com.ruyiruyi.rylibrary.cell.ActionBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import me.drakeet.multitype.MultiTypeAdapter;
import rx.functions.Action1;

import static me.drakeet.multitype.MultiTypeAsserts.assertAllRegistered;
import static me.drakeet.multitype.MultiTypeAsserts.assertHasTheSameAdapter;

public class RoadConditionActivity extends RYBaseActivity implements RoadChooseViewBinder.OnRoadChooseClick {

    private static final String TAG = RoadConditionActivity.class.getSimpleName();
    private ActionBar actionBar;
    private RecyclerView listView;
    private List<Object> items = new ArrayList<>();
    private MultiTypeAdapter adapter;

    public List<RoadChoose> roadList;
    public List<RoadChoose> ouerOrBuRoadList;
    public List<RoadChoose> ouerRoadList;
    public List<RoadChoose> jingchangRoadList;
    public List<RoadChoose> bujingchangRoadList;

    public static int currentRoad = 0;
    private TextView roadButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_road_condition, R.id.my_action);
        actionBar = (ActionBar) findViewById(R.id.my_action);
        actionBar.setTitle("路况选择");
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
        currentRoad = 0;
        ouerOrBuRoadList = new ArrayList<RoadChoose>();
        jingchangRoadList = new ArrayList<RoadChoose>();
        bujingchangRoadList = new ArrayList<RoadChoose>();
        ouerRoadList = new ArrayList<RoadChoose>();
        initView();
        initDataFromService();

    }

    private void initDataFromService() {
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "getAllRoad");
        params.addBodyParameter("reqJson", "");
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                roadList = new ArrayList<RoadChoose>();
                Log.e(TAG, "onSuccess: " + result);
                JSONObject jsonObject1 = null;
                try {
                    jsonObject1 = new JSONObject(result);
                    String status = jsonObject1.getString("status");
                    String msg = jsonObject1.getString("msg");
                    if (status.equals("1")) {
                        JSONArray data = jsonObject1.getJSONArray("data");
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject object = data.getJSONObject(i);
                            String content = object.getString("description");
                            String img = object.getString("img");
                            int id = object.getInt("id");
                            String name = object.getString("name");
                            roadList.add(new RoadChoose(id, false, img, name, content));
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
        if (currentRoad == 0) {
            items.add(new RoadType(0));
            for (int i = 0; i < roadList.size(); i++) {
                items.add(roadList.get(i));
            }
        } else {
            items.add(new RoadType(1));
            for (int i = 0; i < ouerOrBuRoadList.size(); i++) {
                items.add(ouerOrBuRoadList.get(i));
            }
        }
        assertAllRegistered(adapter, items);
        adapter.notifyDataSetChanged();

    }

    private void initView() {
        roadButton = (TextView) findViewById(R.id.road_button);
        roadButton.setText("下一步");
        listView = (RecyclerView) findViewById(R.id.road_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        listView.setLayoutManager(linearLayoutManager);
        adapter = new MultiTypeAdapter(items);
        register();
        listView.setAdapter(adapter);
        assertHasTheSameAdapter(listView, adapter);


        RxViewAction.clickNoDouble(roadButton)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {

                        if (currentRoad == 0) {
                            roadButton.setText("保存");

                            for (int i = 0; i < roadList.size(); i++) {
                                if (!roadList.get(i).isChoose) {
                                    ouerOrBuRoadList.add(roadList.get(i));
                                } else {
                                    jingchangRoadList.add(roadList.get(i));
                                }
                            }
                            if (jingchangRoadList.size() == 5) { //经常行驶全部选择
                                Intent intent = new Intent();
                                intent.putExtra("JINGCHANG", (Serializable) jingchangRoadList);
                                intent.putExtra("OUER", (Serializable) ouerRoadList);
                                intent.putExtra("BUJINGCHANG", (Serializable) bujingchangRoadList);
                                Log.e(TAG, "call:jingchangRoadList " + jingchangRoadList.size());
                                setResult(CarInfoActivity.ROAD_CONDITITION, intent);
                                finish();
                            } else {
                                currentRoad = 1;
                                initData();
                            }


                        } else {
                            for (int i = 0; i < ouerOrBuRoadList.size(); i++) {
                                if (!ouerOrBuRoadList.get(i).isChoose) {
                                    bujingchangRoadList.add(ouerOrBuRoadList.get(i));
                                } else {
                                    ouerRoadList.add(ouerOrBuRoadList.get(i));
                                }
                            }
                            Intent intent = new Intent();
                            intent.putExtra("JINGCHANG", (Serializable) jingchangRoadList);
                            intent.putExtra("OUER", (Serializable) ouerRoadList);
                            intent.putExtra("BUJINGCHANG", (Serializable) bujingchangRoadList);
                            Log.e(TAG, "call:jingchangRoadList " + jingchangRoadList.size());
                            Log.e(TAG, "call:ouerRoadList " + ouerRoadList.size());
                            Log.e(TAG, "call:bujingchangRoadList " + bujingchangRoadList.size());
                            setResult(CarInfoActivity.ROAD_CONDITITION, intent);
                            finish();
                        }
                    }
                });
    }

    private void register() {
        adapter.register(RoadType.class, new RoadTypeViewBinder());
        RoadChooseViewBinder roadChooseViewBinder = new RoadChooseViewBinder(this);
        roadChooseViewBinder.setListener(this);
        adapter.register(RoadChoose.class, roadChooseViewBinder);

    }

    @Override
    public void onRoadChossClickListener(int id, boolean choose) {
        for (int i = 0; i < roadList.size(); i++) {
            if (roadList.get(i).getRoadId() == id) {
                roadList.get(i).setRoadId(id);
                roadList.get(i).setChoose(choose);
            }
        }
        initData();
    }
}
