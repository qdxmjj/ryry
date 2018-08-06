package com.ruyiruyi.ruyiruyi.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ruyiruyi.ruyiruyi.R;
import com.ruyiruyi.ruyiruyi.db.DbConfig;
import com.ruyiruyi.ruyiruyi.ui.activity.base.RyBaseActivity;
import com.ruyiruyi.ruyiruyi.utils.RequestUtils;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;
import com.ruyiruyi.rylibrary.cell.ActionBar;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.text.DecimalFormat;

import rx.functions.Action1;

public class OrderAffirmActivity extends RyBaseActivity {
    private static final String TAG = OrderAffirmActivity.class.getSimpleName();
    private ActionBar actionBar;
    private TextView tireBuyButton;
    private String fontrearflag;
    private int tirecount;
    private String tireprice;
    private String tirepname;
    private int cxwycount;
    private String cxwyprice;
    private TextView tireNameText;
    private TextView tirePriceText;
    private TextView tireCountText;
    private TextView cxwy_count_text;
    private TextView tireAllPriceText;
    private TextView cxwyAllPriceText;
    private double tirePriceAll;
    private double allPrice;
    private TextView allPriceText;
    private double cxwyPriceAll;
    private String username;
    private String userphone;
    private String carnumber;
    private String tireimage;
    private TextView userNameText;
    private TextView userPhoneText;
    private TextView carNumberText;
    private ImageView tireImageView;
    private int shoeid;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_affirm, R.id.my_action);
        actionBar = (ActionBar) findViewById(R.id.my_action);
        actionBar.setTitle("订单确认");
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

        Intent intent = getIntent();
        fontrearflag = intent.getStringExtra("FONTREARFLAG");
        tirecount = intent.getIntExtra("TIRECOUNT", 0);
        tireprice = intent.getStringExtra("TIREPRICE");
        tirepname = intent.getStringExtra("TIREPNAME");
        cxwycount = intent.getIntExtra("CXWYCOUNT", 0);
        cxwyprice = intent.getStringExtra("CXWYPRICE");
        username = intent.getStringExtra("USERNAME");
        userphone = intent.getStringExtra("USERPHONE");
        carnumber = intent.getStringExtra("CARNUMBER");
        tireimage = intent.getStringExtra("TIREIMAGE");
        shoeid = intent.getIntExtra("SHOEID", 0);
        Log.e(TAG, "onCreate:1- " + fontrearflag);
        Log.e(TAG, "onCreate: 2-" + tirecount);
        Log.e(TAG, "onCreate: 3-" + tireprice);
        Log.e(TAG, "onCreate: 4-" + tirepname);
        Log.e(TAG, "onCreate: 5-" + cxwycount);
        Log.e(TAG, "onCreate: 6-" + cxwyprice);
        progressDialog = new ProgressDialog(this);

        initView();
    }

    private void initView() {
        tireBuyButton = (TextView) findViewById(R.id.tire_buy_button);
        tireNameText = (TextView) findViewById(R.id.tire_name_text_oder);
        tirePriceText = (TextView) findViewById(R.id.tire_price_text_order);
        tireCountText = (TextView) findViewById(R.id.tire_count_order);
        cxwy_count_text = (TextView) findViewById(R.id.cxwy_count_text);
        tireAllPriceText = (TextView) findViewById(R.id.tire_all_price_text);
        cxwyAllPriceText = (TextView) findViewById(R.id.cxwy_all_price_text);
        allPriceText = (TextView) findViewById(R.id.all_price_text);
        userNameText = (TextView) findViewById(R.id.user_name_text);
        userPhoneText = (TextView) findViewById(R.id.user_phone_text);
        carNumberText = (TextView) findViewById(R.id.car_number_text);
        tireImageView = (ImageView) findViewById(R.id.tire_image);

        Glide.with(this).load(tireimage).into(tireImageView);

        carNumberText.setText(carnumber);
        userPhoneText.setText(userphone);
        userNameText.setText(username);

        cxwy_count_text.setText("×" + cxwycount);
        tireCountText.setText("×" + tirecount);
        tireNameText.setText(tirepname);
        tirePriceText.setText("￥" + tireprice);


        double tirePriceDouble = Double.parseDouble(tireprice);
        double cxwyPriceDouble = Double.parseDouble(cxwyprice);


        tirePriceAll = Double.parseDouble(new DecimalFormat("0.00").format(tirePriceDouble * tirecount));
        cxwyPriceAll = Double.parseDouble(new DecimalFormat("0.00").format(cxwyPriceDouble));
        tireAllPriceText.setText("￥" + tirePriceAll);
        cxwyAllPriceText.setText("￥" + cxwyPriceAll);

        // allPrice = tirePriceAll + cxwyPriceAll;
        allPrice = Double.parseDouble(new DecimalFormat("0.00").format(tirePriceAll + cxwyPriceAll));
        allPriceText.setText("￥" + allPrice);


        //  Glide.with(this).load("http://180.76.243.205:8111/images/flgure/9F5CD167-866A-C9B3-4406-7E0E36A4D003.jpg").into(tireImage);

        RxViewAction.clickNoDouble(tireBuyButton)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        postOrder();

                    }
                });
    }

    private void postOrder() {
        showDialogProgress(progressDialog,"订单提交中");
        int userId = new DbConfig(this).getId();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("shoeId", shoeid);
            jsonObject.put("userId", userId);
            jsonObject.put("fontRearFlag", fontrearflag);
            jsonObject.put("amount", tirecount);
            jsonObject.put("shoeName", tirepname);
            jsonObject.put("shoeTotalPrice", tirePriceAll + "");
            jsonObject.put("shoePrice", tireprice);
            jsonObject.put("cxwyAmount", cxwycount);
            jsonObject.put("cxwyPrice", cxwyprice);
            jsonObject.put("cxwyTotalPrice", cxwyPriceAll + "");
            jsonObject.put("totalPrice", allPrice);
            Log.e(TAG, "postOrder: ------------------------------" + tireimage);
            jsonObject.put("orderImg", tireimage);
        } catch (JSONException e) {
        }
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "addUserShoeOrder");
        params.addBodyParameter("reqJson", jsonObject.toString());
        String token = new DbConfig(this).getToken();
        params.addParameter("token", token);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess:------ " + result);
                try {
                    JSONObject jsonObject1 = new JSONObject(result);
                    String status = jsonObject1.getString("status");
                    String msg = jsonObject1.getString("msg");
                    if (status.equals("1")) {
                        JSONObject data = jsonObject1.getJSONObject("data");
                        String orderNo = data.getString("orderNo");

                        Intent intent = new Intent(getApplicationContext(), PaymentActivity.class);
                        intent.putExtra(PaymentActivity.ALL_PRICE, allPrice);
                        intent.putExtra(PaymentActivity.ORDERNO, orderNo);
                        intent.putExtra(PaymentActivity.ORDER_TYPE, 0);
                        startActivity(intent);
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
                hideDialogProgress(progressDialog);
            }
        });
    }
}
