package com.ruyiruyi.merchant.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.ruyiruyi.merchant.R;
import com.ruyiruyi.merchant.db.DbConfig;
import com.ruyiruyi.merchant.utils.UtilsURL;
import com.ruyiruyi.rylibrary.base.BaseActivity;
import com.ruyiruyi.rylibrary.cell.ActionBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

public class ConfirmReceiveActivity extends BaseActivity {

    private ActionBar mActionBar;
    private TextView user_name;
    private TextView car_num;
    private TextView store_name;
    private TextView order_num;

    private LinearLayout ll_qiantai;
    private ImageView img_qiantai;
    private TextView type_qiantai;
    private TextView xiangmu_qiantai;
    private TextView luntai_qiantai;
    private TextView luntai_num_qiantai;
    private LinearLayout ll_houtai;
    private ImageView img_houtai;
    private TextView type_houtai;
    private TextView xiangmu_houtai;
    private TextView luntai_houtai;
    private TextView luntai_num_houtai;
    private LinearLayout ll_yizhi;
    private ImageView img_yizhi;
    private TextView type_yizhi;
    private TextView xiangmu_yizhi;
    private TextView luntai_yizhi;
    private TextView luntai_num_yizhi;

    private LinearLayout ll_tiaoma_a_;
    private TextView tiaoma_a_;
    private Switch tiaoma_switch_a_;
    private LinearLayout ll_tiaoma_b_;
    private TextView tiaoma_b_;
    private Switch tiaoma_switch_b_;
    private LinearLayout ll_tiaoma_c_;
    private TextView tiaoma_c_;
    private Switch tiaoma_switch_c_;
    private LinearLayout ll_tiaoma_d_;
    private TextView tiaoma_d_;
    private Switch tiaoma_switch_d_;
    private String orderNo;
    private String userName;
    private String platNumber;
    private String storeName;

    private String fontShoeImg;
    private String fontShoeName;
    private String fontAmount;
    private String rearShoeImg;
    private String rearShoeName;
    private String rearAmount;
    private String consistentShoeImg;
    private String consistentShoeName;
    private String consistentAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_receive);
        mActionBar = (ActionBar) findViewById(R.id.acbar_);
        mActionBar.setTitle("确认收货");
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
        //获取订单编号
        Bundle bundle = getIntent().getExtras();
        orderNo = bundle.getString("orderNo");

        initView();
        initData();

    }

    private void initData() {
        JSONObject object = new JSONObject();
        try {
            object.put("orderNo", orderNo);

        } catch (JSONException e) {
        }
        RequestParams params = new RequestParams(UtilsURL.REQUEST_URL + "getWaitForReceivingShoeOrderInfo");
        params.addBodyParameter("reqJson", object.toString());
        params.addBodyParameter("token", new DbConfig().getToken());
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONObject data = jsonObject.getJSONObject("data");
                    JSONArray firstChangeOrderVoList = data.getJSONArray("firstChangeOrderVoList");
                    JSONArray userCarShoeBarCodeList = data.getJSONArray("userCarShoeBarCodeList");
                    userName = data.getString("userName");
                    platNumber = data.getString("platNumber");
                    storeName = data.getString("storeName");
                    for (int i = 0; i < firstChangeOrderVoList.length(); i++) {
                        JSONObject obj = (JSONObject) firstChangeOrderVoList.get(i);
                        String flag = obj.getString("fontRearFlag");
                        if (flag.equals("1")) {
                            fontShoeImg = obj.getString("shoeImg");
                            fontShoeName = obj.getString("shoeName");
                            fontAmount = obj.getString("fontAmount");
                        }
                        if (flag.equals("2")){
                            rearShoeImg = obj.getString("shoeImg");
                            rearShoeName = obj.getString("shoeName");
                            rearAmount = obj.getString("rearAmount");
                        }
                        if (flag.equals("0")){
                            consistentShoeImg = obj.getString("shoeImg");
                            consistentShoeName = obj.getString("shoeName");
                            consistentAmount = obj.getString("fontAmount");
                        }

                        setData();

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

    private void setData() {
        if (fontAmount==null||fontAmount.length()==0) {

        }
    }

    private void initView() {
        user_name = findViewById(R.id.user_name);
        car_num = findViewById(R.id.car_num);
        store_name = findViewById(R.id.store_name);
        order_num = findViewById(R.id.order_num);
        ll_qiantai = findViewById(R.id.ll_qiantai);
        img_qiantai = findViewById(R.id.img_qiantai);
        type_qiantai = findViewById(R.id.type_qiantai);
        xiangmu_qiantai = findViewById(R.id.xiangmu_qiantai);
        luntai_qiantai = findViewById(R.id.luntai_qiantai);
        luntai_num_qiantai = findViewById(R.id.luntai_num_qiantai);
        ll_houtai = findViewById(R.id.ll_houtai);
        img_houtai = findViewById(R.id.img_houtai);
        type_houtai = findViewById(R.id.type_houtai);
        xiangmu_houtai = findViewById(R.id.xiangmu_houtai);
        luntai_houtai = findViewById(R.id.luntai_houtai);
        luntai_num_houtai = findViewById(R.id.luntai_num_houtai);
        ll_yizhi = findViewById(R.id.ll_yizhi);
        img_yizhi = findViewById(R.id.img_yizhi);
        type_yizhi = findViewById(R.id.type_yizhi);
        xiangmu_yizhi = findViewById(R.id.xiangmu_yizhi);
        luntai_yizhi = findViewById(R.id.luntai_yizhi);
        luntai_num_yizhi = findViewById(R.id.luntai_num_yizhi);
        ll_tiaoma_a_ = findViewById(R.id.ll_tiaoma_a_);
        tiaoma_a_ = findViewById(R.id.tiaoma_a_);
        tiaoma_switch_a_ = findViewById(R.id.tiaoma_switch_a_);
        ll_tiaoma_b_ = findViewById(R.id.ll_tiaoma_b_);
        tiaoma_b_ = findViewById(R.id.tiaoma_b_);
        tiaoma_switch_b_ = findViewById(R.id.tiaoma_switch_b_);
        ll_tiaoma_c_ = findViewById(R.id.ll_tiaoma_c_);
        tiaoma_c_ = findViewById(R.id.tiaoma_c_);
        tiaoma_switch_c_ = findViewById(R.id.tiaoma_switch_c_);
        ll_tiaoma_d_ = findViewById(R.id.ll_tiaoma_d_);
        tiaoma_d_ = findViewById(R.id.tiaoma_d_);
        tiaoma_switch_d_ = findViewById(R.id.tiaoma_switch_d_);
    }
}
