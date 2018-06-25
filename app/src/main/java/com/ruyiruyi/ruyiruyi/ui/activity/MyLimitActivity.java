package com.ruyiruyi.ruyiruyi.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.ruyiruyi.ruyiruyi.MainActivity;
import com.ruyiruyi.ruyiruyi.R;
import com.ruyiruyi.ruyiruyi.db.DbConfig;
import com.ruyiruyi.ruyiruyi.ui.activity.base.RyBaseActivity;
import com.ruyiruyi.ruyiruyi.ui.model.PayResult;
import com.ruyiruyi.ruyiruyi.ui.multiType.RechargeMoney;
import com.ruyiruyi.ruyiruyi.ui.multiType.RechargeMoneyViewBinder;
import com.ruyiruyi.ruyiruyi.utils.RequestUtils;
import com.ruyiruyi.ruyiruyi.utils.UtilsRY;
import com.ruyiruyi.ruyiruyi.utils.XMJJUtils;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;
import com.ruyiruyi.rylibrary.cell.ActionBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import me.drakeet.multitype.MultiTypeAdapter;
import rx.functions.Action1;

import static me.drakeet.multitype.MultiTypeAsserts.assertAllRegistered;
import static me.drakeet.multitype.MultiTypeAsserts.assertHasTheSameAdapter;

public class MyLimitActivity extends RyBaseActivity /*implements RechargeMoneyViewBinder.OnMoneyClick */ {
    private ActionBar actionBar;
    //    private RecyclerView listView;
//    private List<Object> items = new ArrayList<>();
//    private MultiTypeAdapter adapter;
//    public List<RechargeMoney> rechargeMoneyList;
//    private EditText otherMoneyEdit;
//    public boolean showOtherMoneyView = false;
    private FrameLayout weixinLayout;
    private FrameLayout zhifubaoLayout;
    private ImageView weixinImage;
    private ImageView zhifubaoImageView;
    private TextView tv_pay;
    private String money;
    private EditText et_money;
    private ImageView img_delete;
    private TextView tv_change;
    private TextView tv_maxmoney;
    private TextView right_syed;
    private TextView left_yhed;
    private TextView top_xyed;
    private String xyed;
    private String yhed;
    private String syed;
    public int payType =1;  //0是微信支付 1是支付宝支付
    private ProgressDialog payDialog;
    private ProgressDialog startDialog;
    private String TAG = MyLimitActivity.class.getSimpleName();
    private static final int SDK_PAY_FLAG = 1;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            PayResult payResult = new PayResult((Map<String, String>) msg.obj);
            /**
             对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
             */
            String resultInfo = payResult.getResult();// 同步返回需要验证的信息
            String resultStatus = payResult.getResultStatus();
            // 判断resultStatus 为9000则代表支付成功
            Log.e(TAG, "handleMessage: ------" + payResult.getResult());
            if (TextUtils.equals(resultStatus, "9000")) {
                // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                Toast.makeText(MyLimitActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                /*//支付成功 刷新页面数据
                initData();*/
                //支付成功 跳转主页面
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();

            } else {
                // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                Toast.makeText(MyLimitActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
            }
            // Toast.makeText(PaymentActivity.this, payResult.getResult(), Toast.LENGTH_LONG).show();
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_my_limit, R.id.my_action);
        actionBar = (ActionBar) findViewById(R.id.my_action);
        actionBar.setTitle("我的额度");
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


        payDialog = new ProgressDialog(this);
        startDialog = new ProgressDialog(this);
/*        rechargeMoneyList = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            rechargeMoneyList.add(new RechargeMoney(i, i + "00", i + "0", false, 0));
        }
        rechargeMoneyList.add(new RechargeMoney(5, "其他", "", true, 1));
        showOtherMoneyView = true;*/
        initData();
     /*   initData();*/

    }

    private void initData() {
        showDialogProgress(startDialog, "信息加载中...");
        JSONObject object = new JSONObject();
        try {
            object.put("userId", new DbConfig(this).getId());
            object.put("userCarId", new DbConfig(this).getUser().getCarId());
        } catch (JSONException e) {
        }
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "userCarInfo/queryCarCreditInfo");
        params.addBodyParameter("reqJson", object.toString());
        params.addBodyParameter("token", new DbConfig(this).getToken());
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess: result = " + result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    int status = jsonObject.getInt("status");
                    String msg = jsonObject.getString("msg");
                    if (status == 1) {
                        JSONArray data = jsonObject.getJSONArray("data");
                        JSONObject objBean = (JSONObject) data.get(0);
                        xyed = objBean.getDouble("credit") + "";// 信用额度
                        syed = objBean.getDouble("remain") + "";// 剩余额度
                        yhed = objBean.getDouble("credit") - objBean.getInt("remain") + "";// 应还额度
                        initView();
                    } else if (status == -999) {
                        hideDialogProgress(startDialog);
                        showUserTokenDialog("您的账号在其它设备登录,请重新登录");
                    } else {
                        hideDialogProgress(startDialog);
                        Toast.makeText(MyLimitActivity.this, msg, Toast.LENGTH_SHORT).show();
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
/*
    private void initData() {
        items.clear();
        for (int i = 0; i < rechargeMoneyList.size(); i++) {
            items.add(rechargeMoneyList.get(i));
        }
        assertAllRegistered(adapter, items);
        adapter.notifyDataSetChanged();
    }*/

    private void initView() {
        weixinLayout = (FrameLayout) findViewById(R.id.weixin_layout);
        zhifubaoLayout = (FrameLayout) findViewById(R.id.zhifubao_layout);
        weixinImage = (ImageView) findViewById(R.id.weixin_image);
        zhifubaoImageView = (ImageView) findViewById(R.id.zhifubao_image);
        tv_pay = (TextView) findViewById(R.id.save_car);
        top_xyed = (TextView) findViewById(R.id.top_xyed);
        left_yhed = (TextView) findViewById(R.id.left_yhed);
        right_syed = (TextView) findViewById(R.id.right_syed);
        et_money = (EditText) findViewById(R.id.et_money);
        img_delete = (ImageView) findViewById(R.id.img_delete);
        tv_change = (TextView) findViewById(R.id.tv_change);
        tv_maxmoney = (TextView) findViewById(R.id.tv_maxmoney);

        //设置下载数据
        top_xyed.setText(xyed);
        left_yhed.setText(yhed);
        right_syed.setText(syed);
        et_money.setText(yhed);
        et_money.setFocusable(false);
        tv_maxmoney.setText(yhed);

        initPayLayout();

//        otherMoneyEdit = (EditText) findViewById(R.id.other_money_edit);
//        initOtherMoneyView();
//        listView = (RecyclerView) findViewById(R.id.money_grid_list);
/*        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        listView.setLayoutManager(gridLayoutManager);
        adapter = new MultiTypeAdapter(items);

        register();
        listView.setAdapter(adapter);
        assertHasTheSameAdapter(listView, adapter);*/
        RxViewAction.clickNoDouble(weixinLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        payType = 0;
                        initPayLayout();
                    }
                });
        RxViewAction.clickNoDouble(zhifubaoLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        payType = 1;
                        initPayLayout();
                    }
                });

        RxViewAction.clickNoDouble(tv_pay).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (Double.parseDouble(et_money.getText().toString()) < 1) {
                    Toast.makeText(MyLimitActivity.this, "每次还款不能少于1元!", Toast.LENGTH_SHORT).show();
                    return;
                }

                //pay
                if (payType == 0) {//微信支付

                }
                if (payType == 1) {//支付宝支付
                    getOrderNoBeforeSign();
                }


            }
        });
        RxViewAction.clickNoDouble(tv_change).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                et_money.setFocusable(true);
                et_money.setFocusableInTouchMode(true);
            }
        });
        RxViewAction.clickNoDouble(img_delete).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                et_money.setFocusable(true);
                et_money.setFocusableInTouchMode(true);
                et_money.setText("0");
            }
        });
        et_money.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (et_money.getText() != null && et_money.getText().length() != 0) {//不为空
                    //不能超出应还额度
                    if (Integer.parseInt(et_money.getText().toString()) > Integer.parseInt(yhed)) {
                        et_money.setText(yhed);
                    }


                } else {//为空
                    et_money.setText("0");
                }
            }
        });

        hideDialogProgress(startDialog);
    }

    private void getOrderNoBeforeSign() {
        showDialogProgress(payDialog, "订单提交中...");
        int userId = new DbConfig(this).getId();
        int userCarId = new DbConfig(this).getUser().getCarId();
        money = et_money.getText().toString();

        JSONObject object = new JSONObject();
        try {
            object.put("associationOrderNo", "");
            object.put("userId", userId + "");
            object.put("userCarId", userCarId + "");
            object.put("price", money + "");
        } catch (JSONException e) {
        }
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "addRechargeCreditOrder");
        params.addBodyParameter("reqJson", object.toString());
        params.addBodyParameter("token", new DbConfig(this).getToken());
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    int status = jsonObject.getInt("status");
                    String msg = jsonObject.getString("msg");
                    String associationOrderNo = jsonObject.getString("data");
                    if (status == 1) {
                        //下单成功 获取签名后的OrderInfo
                        getSign(money, associationOrderNo);
                    } else if (status == -999) {
                        showUserTokenDialog("您的账号在其它设备登录,请重新登录");
                    } else {
                        Toast.makeText(MyLimitActivity.this, msg, Toast.LENGTH_SHORT).show();
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
                hideDialogProgress(payDialog);
            }
        });

    }

    private void getSign(String price, String associationOrderNo) {
        JSONObject object = new JSONObject();
        try {
            object.put("userId", new DbConfig(this).getId() + "");
            object.put("orderNo", associationOrderNo);
            object.put("orderName", "额度充值");
//            object.put("orderPrice", price);//正式
            object.put("orderPrice", 0.01);//测试
            object.put("orderType", "5");
        } catch (JSONException e) {
        }
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "getAliPaySign");
        String token = new DbConfig(this).getToken();
        String jsonByToken = "";
        try {
            jsonByToken = XMJJUtils.encodeJsonByToken(object.toString(), token);


        } catch (UnsupportedEncodingException e) {

        } catch (IllegalBlockSizeException e) {

        } catch (InvalidKeyException e) {

        } catch (BadPaddingException e) {

        } catch (NoSuchAlgorithmException e) {

        } catch (NoSuchPaddingException e) {

        }
        String s2 = jsonByToken.replaceAll("\\n", "");
        params.addBodyParameter("reqJson", s2);
        params.addParameter("token", token);
        Log.e(TAG, "postOrderSuccess: paramas---" + params.toString());
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                postToZhifubao(result);
                Log.e(TAG, "onSuccess: " + result);
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

    private void postToZhifubao(final String orderInfo) {
     /*   boolean rsa2 = (RSA2_PRIVATE.length() > 0);
        Map<String, String> params = OrderInfoUtil2_0.buildOrderParamMap(APPID, rsa2);
        String orderParam = OrderInfoUtil2_0.buildOrderParam(params);
        String privateKey = RSA2_PRIVATE;
        String sign = OrderInfoUtil2_0.getSign(params, privateKey, rsa2);
        //final String orderInfo = orderParam + "&" + sign;
        final String orderInfo = "alipay_sdk=alipay-sdk-java-dynamicVersionNo&app_id=2018051760091839&biz_content=%7B%22out_trade_no%22%3A%222018456498791421%22%2C%22product_code%22%3A%22QUICK_MSECURITY_PAY%22%2C%22subject%22%3A%22ddddd%22%2C%22total_amount%22%3A%220.01%22%7D&charset=UTF-8&format=json&method=alipay.trade.app.pay&notify_url=http%3A%2F%2Fwww.xxx.com%2Falipay%2Fnotify_url.do&sign=VaMNuMv4QJrtR98lrz%2FeGBzB4Db4eJ%2B58yZkXZLrepC3k5utJgvT6b0clrmG%2BWj5LCSIG1w3OPRsANEPtxq07ImpvW759QbjShUe%2B6HZZ5Gva%2BOVYcaes%2ByWBEeGhd5x8d5iAnQUPAS140ueqTHqBO8GOuwfd0smSY1Zot1JFdkPkj4QNKLvYEGB8Pz%2B5fvHwkXGurd6oD%2FM2GbWUeUqVOQ9lb6sXYRKVTey8mr5xhBHB49PjA%2BhpdqP53YkyUYQXRounnqANNX31UqmStOp9hxJrN7dmbbWWoVq0uo8QdhTVpiaUXucBaCcgjLlpuUJ3%2FLzXzH8CUen%2FQvy7bLNdQ%3D%3D&sign_type=RSA2&timestamp=2018-05-18+11%3A40%3A49&version=1.0";
        Log.e(TAG, "call:----*--- " +  orderInfo);*/
        //    postOrderSuccess();
        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                // String orderInfoStr = orderInfo.substring(1, orderInfo.length()-1);
                // Log.e(TAG, "run:---- " + orderInfo);
                //Log.e(TAG, "run:---- " + orderInfo);
                PayTask alipay = new PayTask(MyLimitActivity.this);
                Map<String, String> result = alipay.payV2(orderInfo, true);
                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }


    private void initPayLayout() {
        weixinImage.setImageResource(payType == 0 ? R.drawable.ic_check : R.drawable.ic_check_no);
        zhifubaoImageView.setImageResource(payType == 0 ? R.drawable.ic_check_no : R.drawable.ic_check);
    }

/*    private void initOtherMoneyView() {
        otherMoneyEdit.setVisibility(showOtherMoneyView ? View.VISIBLE : View.GONE);
    }*/

/*    private void register() {
        RechargeMoneyViewBinder rechargeMoneyViewBinder = new RechargeMoneyViewBinder();
        rechargeMoneyViewBinder.setListener(this);
        adapter.register(RechargeMoney.class, rechargeMoneyViewBinder);
    }*/

/*    @Override
    public void onMonyeClickListener(int moneyId) {
        for (int i = 0; i < rechargeMoneyList.size(); i++) {
            if (rechargeMoneyList.get(i).getMoneyId() == moneyId) {
                rechargeMoneyList.get(i).setIscheck(true);
            } else {
                rechargeMoneyList.get(i).setIscheck(false);
            }
        }
        showOtherMoneyView = rechargeMoneyList.get(rechargeMoneyList.size() - 1).ischeck;
        initOtherMoneyView();
        initData();
    }*/
}
