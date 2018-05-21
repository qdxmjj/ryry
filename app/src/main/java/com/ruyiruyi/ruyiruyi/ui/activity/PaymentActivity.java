package com.ruyiruyi.ruyiruyi.ui.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.ruyiruyi.ruyiruyi.MainActivity;
import com.ruyiruyi.ruyiruyi.R;
import com.ruyiruyi.ruyiruyi.db.DbConfig;
import com.ruyiruyi.ruyiruyi.ui.model.PayResult;
import com.ruyiruyi.ruyiruyi.utils.OrderInfoUtil2_0;
import com.ruyiruyi.ruyiruyi.utils.RequestUtils;
import com.ruyiruyi.ruyiruyi.utils.XMJJUtils;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;
import com.ruyiruyi.rylibrary.base.BaseActivity;
import com.ruyiruyi.rylibrary.cell.ActionBar;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import rx.functions.Action1;

public class PaymentActivity extends BaseActivity {

    private static final String TAG = PaymentActivity.class.getSimpleName();
    private ActionBar actionBar;
    private TextView payButton;
    private double allprice;
    private TextView allPriceText;
    private String orderno;
    public static String ALL_PRICE = "ALLPRICE";
    public static String ORDERNO = "ORDERNO";
    public static String ORDER_TYPE = "ORDER_TYPE";//  0:轮胎购买订单 1:普通商品购买订单 2:首次更换订单 3:免费再换订单 4:轮胎修补订单
    public static String ORDER_STATE = "ORDER_STATE";//轮胎订单状态(orderType:0) :1 已安装 2 待服务 3 支付成功 4 支付失败 5 待支付 6 已退货
                                                 // 订单状态(orderType::1 2 3 4 ): 1 交易完成 2 待收货 3 待商家确认服务 4 作废 5 待发货 6 待车主确认服务 7 待评价 8 待支付
    public static String ORDER_FROM = "ORDER_FROM";  //0是来自收银台  1是来自订单
    private int orderType;
    public  String orderInfo = "";   // 订单信息
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
                Toast.makeText(PaymentActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                //支付成功 跳转到待更换轮胎界面
                startActivity(new Intent(getApplicationContext(),TireWaitChangeActivity.class));
            } else {
                // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                Toast.makeText(PaymentActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
            }
           // Toast.makeText(PaymentActivity.this, payResult.getResult(), Toast.LENGTH_LONG).show();
        };
    };

    /** 支付宝支付业务：入参app_id */
    public static final String APPID = "2018051760091839";

    /** 支付宝账户登录授权业务：入参pid值 */
    public static final String PID = "2088821219284232";
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment,R.id.my_action);
        actionBar = (ActionBar) findViewById(R.id.my_action);
        actionBar.setTitle("收银台");;
        actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick(){
            @Override
            public void onItemClick(int var1) {
                switch ((var1)){
                    case -1:
                        onBackPressed();
                        break;
                }
            }
        });
        progressDialog = new ProgressDialog(this);
        Intent intent = getIntent();
        allprice = intent.getDoubleExtra(ALL_PRICE,0.00);
        orderno = intent.getStringExtra(ORDERNO);
        orderType = intent.getIntExtra(ORDER_TYPE,0);
        initView();
        getDataFromService();
    }

    private void getDataFromService() {

    }

    private void initView() {
        payButton = (TextView) findViewById(R.id.payment_button);
        allPriceText = (TextView) findViewById(R.id.price_text);
        allPriceText.setText(allprice+"");


        RxViewAction.clickNoDouble(payButton)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        //获取签名后的orderInfo
                        getSign();
                    }
                });
    }

    private void getSign() {
        int userId = new DbConfig().getId();
        JSONObject jsonObject = new JSONObject();
        Log.e(TAG, "getSign: orderno-" + orderno);
        try {
            jsonObject.put("orderNo",orderno);
            jsonObject.put("orderType",orderType);
            if (orderType == 0){    //轮胎订单
                jsonObject.put("orderName","轮胎购买");
            }else {     //商品订单
                jsonObject.put("orderName","商品购买");
            }
            jsonObject.put("orderPrice",0.01);
            jsonObject.put("userId",userId);
        } catch (JSONException e) {
        }
        Log.e(TAG, "getSign: " + orderno);
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "getAliPaySign");
        String token = new DbConfig().getToken();
        String jsonByToken = "";
        try {
            jsonByToken = XMJJUtils.encodeJsonByToken(jsonObject.toString(),token);


        } catch (UnsupportedEncodingException e) {

        } catch (IllegalBlockSizeException e) {

        } catch (InvalidKeyException e) {

        } catch (BadPaddingException e) {

        } catch (NoSuchAlgorithmException e) {

        } catch (NoSuchPaddingException e) {

        }
        String s2 = jsonByToken.replaceAll("\\n", "");
        params.addBodyParameter("reqJson",s2);
        params.addParameter("token",token);
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
                PayTask alipay = new PayTask(PaymentActivity.this);
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

    private void postOrderSuccess() {
        int userId = new DbConfig().getId();
        JSONObject jsonObject = new JSONObject();
        try {
           jsonObject.put("orderNo",orderno);
           jsonObject.put("userId",userId);

        } catch (JSONException e) {
        }
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "addConfirmUserShoeCxwyOrder");
        String token = new DbConfig().getToken();
        String jsonByToken = "";
        try {
                jsonByToken = XMJJUtils.encodeJsonByToken(jsonObject.toString(),token);


        } catch (UnsupportedEncodingException e) {

        } catch (IllegalBlockSizeException e) {

        } catch (InvalidKeyException e) {

        } catch (BadPaddingException e) {

        } catch (NoSuchAlgorithmException e) {

        } catch (NoSuchPaddingException e) {

        }
        String s2 = jsonByToken.replaceAll("\\n", "");
        params.addBodyParameter("reqJson",s2);
        params.addParameter("token",token);
        Log.e(TAG, "postOrderSuccess: paramas---" + params.toString());
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess: --*--" + result );
                JSONObject jsonObject1 = null;
                try {
                    jsonObject1 = new JSONObject(result);
                    String status = jsonObject1.getString("status");
                    String msg = jsonObject1.getString("msg");
                    if (status.equals("1")){
                        startActivity(new Intent(getApplicationContext(),TireWaitChangeActivity.class));
                    }else {
                        Toast.makeText(PaymentActivity.this, msg, Toast.LENGTH_SHORT).show();
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

    @Override
    public void onBackPressed() {
        showDialog("您确认要离开支付订单界面，离开订单会变为代付款订单，可在待付款订单中查看");

    }
    private void showDialog(String msg){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_error, null);
        TextView error_text = (TextView) dialogView.findViewById(R.id.error_text);
        error_text.setText(msg);
        dialog.setTitle("如意如驿");
        dialog.setIcon(R.drawable.ic_logo_login);
        dialog.setView(dialogView);
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent intent = new Intent(getApplicationContext(), PendingOrderActivity.class);
                intent.putExtra(ORDERNO,orderno);
                intent.putExtra(ORDER_TYPE,orderType);
                intent.putExtra(ORDER_FROM,0);
                startActivity(intent);
            }
        });
        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
