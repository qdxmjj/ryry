package com.ruyiruyi.merchant.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.ruyiruyi.merchant.R;
import com.ruyiruyi.merchant.db.DbConfig;
import com.ruyiruyi.merchant.ui.multiType.modle.Promotion;
import com.ruyiruyi.merchant.ui.multiType.modle.PromotionHasperson;
import com.ruyiruyi.merchant.ui.multiType.PromotionHaspersonViewBinder;
import com.ruyiruyi.merchant.ui.multiType.modle.PromotionNoperson;
import com.ruyiruyi.merchant.ui.multiType.PromotionNopersonViewBinder;
import com.ruyiruyi.merchant.ui.multiType.PromotionViewBinder;
import com.ruyiruyi.merchant.utils.UtilsRY;
import com.ruyiruyi.merchant.utils.UtilsURL;
import com.ruyiruyi.rylibrary.base.BaseActivity;
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

import static me.drakeet.multitype.MultiTypeAsserts.assertAllRegistered;
import static me.drakeet.multitype.MultiTypeAsserts.assertHasTheSameAdapter;

public class PromotionActivity extends BaseActivity {

    private ActionBar actionBar;
    private RecyclerView mRlv;
    private MultiTypeAdapter multiTypeAdapter;
    private List<Object> items = new ArrayList<>();
    private List<PromotionHasperson> personList;
    private String award;
    private String content;
    private String img;
    private String rule;
    private String title;
    private String url;
    private String invitationCode;
    private String TAG = PromotionActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promotion);
        actionBar = (ActionBar) findViewById(R.id.my_acbar);
        actionBar.setTitle("推广有礼");
        actionBar.setRightImage(R.drawable.ic_erweima);
        actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(int var1) {
                switch ((var1)) {
                    case -1:
                        onBackPressed();
                        break;
                    case -2:
                        onQrCodePressed();
                        break;
                }
            }


        });

        initView();
        initData();
    }

    private void initData() {
        //下载数据
        JSONObject object = new JSONObject();
        try {
            object.put("storeId", new DbConfig(this).getId());

        } catch (JSONException e) {
        }
        RequestParams params = new RequestParams(UtilsURL.REQUEST_URL + "preferentialInfo/getStoreShareInfo");
        params.addBodyParameter("reqJson", object.toString());
        params.addBodyParameter("token", new DbConfig(this).getToken());
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess:  result789 = " + result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String status = jsonObject.getString("status");
                    String msg = jsonObject.getString("msg");
                    if (status.equals("1")) {
                        JSONObject data = jsonObject.getJSONObject("data");
                        award = data.getString("award");
                        content = data.getString("content");
                        img = data.getString("img");
                        rule = data.getString("rule");
                        title = data.getString("title");
                        url = data.getString("url");
                        invitationCode = data.getString("invitationCode");
                        JSONArray shareRelationList = data.getJSONArray("shareRelationList");
                        for (int i = 0; i < shareRelationList.length(); i++) {
                            JSONObject bean = (JSONObject) shareRelationList.get(i);
                            PromotionHasperson hasperson = new PromotionHasperson();
                            hasperson.setUserState("已邀请");
                            long createdTime = bean.getLong("createdTime");
                            String s = new UtilsRY().getTimestampToString(createdTime);
                            hasperson.setUserTime(s);
                            String phone = bean.getString("phone");
                            String subPhone = phone.substring(0, 3) + "****" + phone.substring(7, 11);
                            hasperson.setUserPhone(subPhone);
                            if (i == 0) {
                                hasperson.setFirst(true);
                            } else {
                                hasperson.setFirst(false);
                            }
                            personList.add(hasperson);
                        }

                        //下载完成更新数据
                        updataData();
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

    private void updataData() {
        //下载完成
        items.clear();
        items.add(new Promotion(invitationCode, award, rule));
        if (personList == null || personList.size() == 0) {
            items.add(new PromotionNoperson());
        } else {
            items.addAll(personList);
        }
        assertAllRegistered(multiTypeAdapter, items);
        multiTypeAdapter.notifyDataSetChanged();
    }

    private void onQrCodePressed() {
        Intent intent = new Intent(getApplicationContext(), QRCodeActivity.class);
        intent.putExtra("url", url);
        intent.putExtra("content", content);
        startActivity(intent);
    }

    private void initView() {
        mRlv = findViewById(R.id.my_rlv);
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRlv.setLayoutManager(manager);
        multiTypeAdapter = new MultiTypeAdapter(items);
        multiTypeAdapter.register(Promotion.class, new PromotionViewBinder(this));
        multiTypeAdapter.register(PromotionHasperson.class, new PromotionHaspersonViewBinder());
        multiTypeAdapter.register(PromotionNoperson.class, new PromotionNopersonViewBinder());
        mRlv.setAdapter(multiTypeAdapter);
        assertHasTheSameAdapter(mRlv, multiTypeAdapter);

        personList = new ArrayList<>();
    }
}
