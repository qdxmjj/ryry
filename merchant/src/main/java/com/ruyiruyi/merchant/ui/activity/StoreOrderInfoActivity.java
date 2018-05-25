package com.ruyiruyi.merchant.ui.activity;

import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.ruyiruyi.merchant.R;
import com.ruyiruyi.merchant.db.DbConfig;
import com.ruyiruyi.merchant.ui.multiType.modle.Dianpu;
import com.ruyiruyi.merchant.utils.UtilsURL;
import com.ruyiruyi.rylibrary.base.BaseActivity;
import com.ruyiruyi.rylibrary.cell.ActionBar;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

public class StoreOrderInfoActivity extends BaseActivity {

    private Dianpu passBean;
    private String stateStr;
    private TextView tv_state;
    private String TAG = StoreOrderInfoActivity.class.getSimpleName();
    private ActionBar mActionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_order_info);
        mActionBar = (ActionBar) findViewById(R.id.acbar);
        mActionBar.setTitle("订单详情");
        mActionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(int var1) {
                switch (var1) {
                    case -1:
                        onBackPressed();
                        break;
                }
            }
        });

        //获取传递信息
        stateStr = getIntent().getStringExtra("stateStr");
        passBean = getIntent().getParcelableExtra("passBean");
        tv_state = findViewById(R.id.tv_state);
        tv_state.setText(stateStr);


        initView();
        initData();
    }

    private void initData() {
        JSONObject object = new JSONObject();
        try {
            object.put("orderNo", passBean.getOrderNo());
            object.put("orderType", passBean.getOrderType());//orderType:  1:普通商品购买订单
            object.put("storeId", new DbConfig().getId() + "");

        } catch (JSONException e) {
        }
        RequestParams params = new RequestParams(UtilsURL.REQUEST_URL + "getStoreOrderInfoByNoAndType");
        params.addBodyParameter("reqJson", object.toString());
        params.addBodyParameter("token", new DbConfig().getToken());
        Log.e(TAG, "initData: 696 params.toString() = " + params.toString());
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess: 696" + result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONObject data = jsonObject.getJSONObject("data");


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

    private void initView() {

    }
}
