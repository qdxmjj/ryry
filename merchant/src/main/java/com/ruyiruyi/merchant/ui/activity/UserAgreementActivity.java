package com.ruyiruyi.merchant.ui.activity;

import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import com.ruyiruyi.merchant.R;
import com.ruyiruyi.merchant.db.DbConfig;
import com.ruyiruyi.merchant.ui.activity.base.MerchantBaseActivity;
import com.ruyiruyi.merchant.utils.UtilsURL;
import com.ruyiruyi.rylibrary.cell.ActionBar;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

public class UserAgreementActivity extends MerchantBaseActivity {
    private WebView web_content;
    private String contentStr;
    private ActionBar mActionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_agreement);
        mActionBar = (ActionBar) findViewById(R.id.acbar);
        mActionBar.setTitle("用户协议");
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

        initView();
        initData();

    }

    private void initData() {
        JSONObject object = new JSONObject();
        try {
            object.put("dealId", "1");//dealId:1:用户版 2:商家版 3用户畅行无忧
        } catch (JSONException e) {
        }
        RequestParams params = new RequestParams(UtilsURL.REQUEST_URL + "getDeal");
        params.addBodyParameter("reqJson", object.toString());
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("123", "onSuccess: " + result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONObject data = jsonObject.getJSONObject("data");
                    contentStr = data.getString("content");
                    Log.e("123", "onSuccess: contentStr = " + contentStr);
                    setView();
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

    private void setView() {
        web_content.loadData(contentStr, "text/html; charset=UTF-8", null);
    }

    private void initView() {
        web_content = findViewById(R.id.web_content);
    }
}
