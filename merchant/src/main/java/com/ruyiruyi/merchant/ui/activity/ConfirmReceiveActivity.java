package com.ruyiruyi.merchant.ui.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ruyiruyi.merchant.MainActivity;
import com.ruyiruyi.merchant.R;
import com.ruyiruyi.merchant.bean.BarCode;
import com.ruyiruyi.merchant.db.DbConfig;
import com.ruyiruyi.merchant.utils.UtilsURL;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;
import com.ruyiruyi.rylibrary.base.BaseActivity;
import com.ruyiruyi.rylibrary.cell.ActionBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.functions.Action1;

/*
* 首次更换订单 的  orderState:订单状态(): 1 交易完成 2 待收货 3 待商家确认服务 4 作废 5 待发货 6 待车主确认服务 7 待评价 8 待支付  均复用此页面
* */
public class ConfirmReceiveActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener {

    private ActionBar mActionBar;
    private TextView user_name;
    private TextView car_num;
    private TextView store_name;
    private TextView order_num;
    private TextView save_;

    private LinearLayout ll_qiantai;
    private ImageView img_qiantai;
    private TextView xiangmu_qiantai;
    private TextView luntai_qiantai;
    private TextView luntai_num_qiantai;
    private LinearLayout ll_houtai;
    private ImageView img_houtai;
    private TextView xiangmu_houtai;
    private TextView luntai_houtai;
    private TextView luntai_num_houtai;
    private LinearLayout ll_yizhi;
    private ImageView img_yizhi;
    private TextView xiangmu_yizhi;
    private TextView luntai_yizhi;
    private TextView luntai_num_yizhi;

    private LinearLayout ll_tiaoma_title;
    private TextView state_;
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
    private String acBarTitle;
    private String orderNo;
    private String orderType;
    private String orderState;
    private String storeId;
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

    private String barCode_a_;
    private String barCode_b_;
    private String barCode_c_;
    private String barCode_d_;
    private String barCode_id_a_;
    private String barCode_id_b_;
    private String barCode_id_c_;
    private String barCode_id_d_;

    //（status   1 一致    2 不一致   ）
    private String isNoConsistent_a_;//false 默认为1(网络请求中已设置)未选中 为一致
    private String isNoConsistent_b_;//false
    private String isNoConsistent_c_;//false
    private String isNoConsistent_d_;//false
    private String TAG = ConfirmReceiveActivity.class.getSimpleName();
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_receive);
        getPassData();//获取传递数据
        mActionBar = (ActionBar) findViewById(R.id.acbar_);
        mActionBar.setTitle(acBarTitle);
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

        progressDialog = new ProgressDialog(this);

        initView();
        initData();
        bindView();
    }

    private void getPassData() {
        state_ = findViewById(R.id.state_);
        save_ = findViewById(R.id.save_);
        ll_tiaoma_title = findViewById(R.id.ll_tiaoma_title);
        //获取
        Bundle bundle = getIntent().getExtras();
        orderNo = bundle.getString("orderNo");
        orderType = bundle.getString("orderType");
        orderState = bundle.getString("orderState");
        storeId = bundle.getString("storeId");

        if (orderState.equals("5") || orderState.equals("8")) {
            ll_tiaoma_title.setVisibility(View.GONE);
        }
        if (orderState.equals("2")) {
            state_.setVisibility(View.GONE);
        } else {
            save_.setVisibility(View.GONE);
        }
        switch (orderState) {
            case "8"://8 待支付
                acBarTitle = "待支付";
                break;
            case "5"://5 待发货
                acBarTitle = "待发货";
                break;
            case "2"://2 待收货
                acBarTitle = "待收货";
                break;
            case "6"://6 待车主确认服务
                acBarTitle = "待车主确认服务";
                break;
            case "3"://3 待商家确认服务
                acBarTitle = "待商家确认服务";
                break;
            case "7"://7 待评价
                acBarTitle = "待评价";
                break;
            case "1"://1 交易完成
                acBarTitle = "交易完成";
                break;
        }
        state_.setText(acBarTitle);
    }

    private void bindView() {
        tiaoma_switch_a_.setOnCheckedChangeListener(this);
        tiaoma_switch_b_.setOnCheckedChangeListener(this);
        tiaoma_switch_c_.setOnCheckedChangeListener(this);
        tiaoma_switch_d_.setOnCheckedChangeListener(this);

        //提交
        RxViewAction.clickNoDouble(save_).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {

                showSaveDialog("确认提交吗?");
            }
        });
    }

    private void showSaveDialog(String error) {
        AlertDialog dialog = new AlertDialog.Builder(this).create();
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_save_commit, null);
        TextView error_text = (TextView) dialogView.findViewById(R.id.save_text);
        error_text.setText(error);
        dialog.setTitle("如意如驿商家版");
        dialog.setIcon(R.drawable.ic_logo_huise);
        dialog.setView(dialogView);
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "再看看", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        //确认提交 请求提交数据
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "是的", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                saveServer();
            }
        });

        dialog.show();
        //设置按钮颜色
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.theme_primary));
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.theme_primary));

    }

    private void saveServer() {
        showDialogProgress(progressDialog, "信息提交中...");
        //提交事件
        List<BarCode> barCodeList = new ArrayList<BarCode>();
        if (isNoConsistent_a_ != null) {
            BarCode code_a_ = new BarCode(barCode_a_, isNoConsistent_a_, barCode_id_a_, orderNo);
            barCodeList.add(code_a_);
        }
        if (isNoConsistent_b_ != null) {
            BarCode code_b_ = new BarCode(barCode_b_, isNoConsistent_b_, barCode_id_b_, orderNo);
            barCodeList.add(code_b_);
        }
        if (isNoConsistent_c_ != null) {
            BarCode code_c_ = new BarCode(barCode_c_, isNoConsistent_c_, barCode_id_c_, orderNo);
            barCodeList.add(code_c_);
        }
        if (isNoConsistent_d_ != null) {
            BarCode code_d_ = new BarCode(barCode_d_, isNoConsistent_d_, barCode_id_d_, orderNo);
            barCodeList.add(code_d_);
        }

        RequestParams params = new RequestParams(UtilsURL.REQUEST_URL + "storeConfirmReceiptShoes");
        params.addBodyParameter("reqJson", barCodeList.toString());
        params.addBodyParameter("token", new DbConfig().getToken());
        params.setConnectTimeout(6000);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject object = new JSONObject(result);
                    String msg = object.getString("msg");
                    int status = object.getInt("status");
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                    if (status == 1) {
                        finish();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("page", "order");
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }


                } catch (JSONException e) {
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(getApplicationContext(), "提交失败,请检查网络", Toast.LENGTH_SHORT).show();
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

    private void initData() {
        JSONObject object = new JSONObject();
        try {
            object.put("orderNo", orderNo);
            object.put("orderType", orderType);
            object.put("storeId", storeId);

        } catch (JSONException e) {
        }
        RequestParams params = new RequestParams(UtilsURL.REQUEST_URL + "getStoreOrderInfoByNoAndType");
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
                        if (flag.equals("2")) {
                            rearShoeImg = obj.getString("shoeImg");
                            rearShoeName = obj.getString("shoeName");
                            rearAmount = obj.getString("rearAmount");
                        }
                        if (flag.equals("0")) {
                            consistentShoeImg = obj.getString("shoeImg");
                            consistentShoeName = obj.getString("shoeName");
                            consistentAmount = obj.getString("fontAmount");
                        }

                        //条形码列表为空 则全部隐藏 （没必要  订单至少一个轮胎） 但防止后台bug已加
                        if (userCarShoeBarCodeList == null || userCarShoeBarCodeList.length() == 0) {
                            ll_tiaoma_a_.setVisibility(View.GONE);
                            ll_tiaoma_b_.setVisibility(View.GONE);
                            ll_tiaoma_c_.setVisibility(View.GONE);
                            ll_tiaoma_d_.setVisibility(View.GONE);
                        }
                        //条形码列表不为空
                        for (int j = 0; j < userCarShoeBarCodeList.length(); j++) {
                            if (j == 0) {
                                JSONObject shoeObj0 = (JSONObject) userCarShoeBarCodeList.get(j);
                                barCode_a_ = shoeObj0.getString("barCode");
                                barCode_id_a_ = shoeObj0.getInt("id") + "";
                                ll_tiaoma_a_.setVisibility(View.VISIBLE);
                                isNoConsistent_a_ = "1";//false 默认为1未选中 为一致
                                //默认1个
                                ll_tiaoma_b_.setVisibility(View.GONE);
                                ll_tiaoma_c_.setVisibility(View.GONE);
                                ll_tiaoma_d_.setVisibility(View.GONE);
                            }
                            if (j == 1) {
                                JSONObject shoeObj1 = (JSONObject) userCarShoeBarCodeList.get(j);
                                barCode_b_ = shoeObj1.getString("barCode");
                                barCode_id_b_ = shoeObj1.getInt("id") + "";
                                ll_tiaoma_b_.setVisibility(View.VISIBLE);
                                isNoConsistent_b_ = "1";//false 默认为1未选中 为一致
                            }
                            if (j == 2) {
                                JSONObject shoeObj2 = (JSONObject) userCarShoeBarCodeList.get(j);
                                barCode_c_ = shoeObj2.getString("barCode");
                                barCode_id_c_ = shoeObj2.getInt("id") + "";
                                ll_tiaoma_c_.setVisibility(View.VISIBLE);
                                isNoConsistent_c_ = "1";//false 默认为1未选中 为一致
                            }
                            if (j == 3) {
                                JSONObject shoeObj3 = (JSONObject) userCarShoeBarCodeList.get(j);
                                barCode_d_ = shoeObj3.getString("barCode");
                                barCode_id_d_ = shoeObj3.getInt("id") + "";
                                ll_tiaoma_d_.setVisibility(View.VISIBLE);
                                isNoConsistent_d_ = "1";//false 默认为1未选中 为一致
                            }
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
        //用户与订单信息
        user_name.setText(userName);
        car_num.setText(platNumber);
        store_name.setText(storeName);
        order_num.setText(orderNo);
        //轮胎信息
        if (fontAmount == null || fontAmount.length() == 0) {
            ll_qiantai.setVisibility(View.GONE);
        } else {
            ll_qiantai.setVisibility(View.VISIBLE);
            ll_yizhi.setVisibility(View.GONE);
            Glide.with(this).load(fontShoeImg).into(img_qiantai);
            luntai_qiantai.setText(fontShoeName);
            luntai_num_qiantai.setText(fontAmount);
        }
        if (rearAmount == null || rearAmount.length() == 0) {
            ll_houtai.setVisibility(View.GONE);
        } else {
            ll_houtai.setVisibility(View.VISIBLE);
            ll_yizhi.setVisibility(View.GONE);
            Glide.with(this).load(rearShoeImg).into(img_houtai);
            luntai_houtai.setText(rearShoeName);
            luntai_num_houtai.setText(rearAmount);
        }
        if (consistentAmount == null || consistentAmount.length() == 0) {
            ll_yizhi.setVisibility(View.GONE);
        } else {
            ll_yizhi.setVisibility(View.VISIBLE);
            ll_qiantai.setVisibility(View.GONE);
            ll_houtai.setVisibility(View.GONE);
            Glide.with(this).load(consistentShoeImg).into(img_yizhi);
            luntai_yizhi.setText(consistentShoeName);
            luntai_num_yizhi.setText(consistentAmount);
        }

        //条形码信息
        tiaoma_a_.setText(barCode_a_);
        tiaoma_b_.setText(barCode_b_);
        tiaoma_c_.setText(barCode_c_);
        tiaoma_d_.setText(barCode_d_);

    }

    private void initView() {
        user_name = findViewById(R.id.user_name);
        car_num = findViewById(R.id.car_num);
        store_name = findViewById(R.id.store_name);
        order_num = findViewById(R.id.order_num);
        ll_qiantai = findViewById(R.id.ll_qiantai);
        img_qiantai = findViewById(R.id.img_qiantai);
        xiangmu_qiantai = findViewById(R.id.xiangmu_qiantai);
        luntai_qiantai = findViewById(R.id.luntai_qiantai);
        luntai_num_qiantai = findViewById(R.id.luntai_num_qiantai);
        ll_houtai = findViewById(R.id.ll_houtai);
        img_houtai = findViewById(R.id.img_houtai);
        xiangmu_houtai = findViewById(R.id.xiangmu_houtai);
        luntai_houtai = findViewById(R.id.luntai_houtai);
        luntai_num_houtai = findViewById(R.id.luntai_num_houtai);
        ll_yizhi = findViewById(R.id.ll_yizhi);
        img_yizhi = findViewById(R.id.img_yizhi);
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

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        switch (compoundButton.getId()) {
            case R.id.tiaoma_switch_a_:
                if (b) {
                    isNoConsistent_a_ = "2";//选中为不一致
                } else {
                    isNoConsistent_a_ = "1";
                }
                break;
            case R.id.tiaoma_switch_b_:
                if (b) {
                    isNoConsistent_b_ = "2";//选中为不一致
                } else {
                    isNoConsistent_b_ = "1";
                }
                break;
            case R.id.tiaoma_switch_c_:
                if (b) {
                    isNoConsistent_c_ = "2";//选中为不一致
                } else {
                    isNoConsistent_c_ = "1";
                }
                break;
            case R.id.tiaoma_switch_d_:
                if (b) {
                    isNoConsistent_d_ = "2";//选中为不一致
                } else {
                    isNoConsistent_d_ = "1";
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {//回退键新跳回
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("page", "order");
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
