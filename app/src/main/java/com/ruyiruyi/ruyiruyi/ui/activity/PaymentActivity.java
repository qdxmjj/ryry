package com.ruyiruyi.ruyiruyi.ui.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.ruyiruyi.ruyiruyi.R;
import com.ruyiruyi.ruyiruyi.db.DbConfig;
import com.ruyiruyi.ruyiruyi.ui.activity.base.RyBaseActivity;
import com.ruyiruyi.ruyiruyi.ui.model.PayResult;
import com.ruyiruyi.ruyiruyi.utils.Constants;
import com.ruyiruyi.ruyiruyi.utils.RequestUtils;
import com.ruyiruyi.ruyiruyi.utils.XMJJUtils;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;
import com.ruyiruyi.rylibrary.cell.ActionBar;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import rx.functions.Action1;

public class PaymentActivity extends RyBaseActivity {

    private static final String TAG = PaymentActivity.class.getSimpleName();
    private ActionBar actionBar;
    private TextView payButton;
    private double allprice;
    private TextView allPriceText;
    private String orderno;
    public int currentType = 0;  //0是余额支付  1是微信支付 2是支付宝支付
    public static String ALL_PRICE = "ALLPRICE";
    public static String ORDERNO = "ORDERNO";
    public static String STOREID = "STOREID";
    public static String ORDER_TYPE = "ORDER_TYPE";//  0:轮胎购买订单 1:普通商品购买订单 2:首次更换订单 3:免费再换订单 4:轮胎修补订单  5畅行无忧
    public static String ORDER_STATE = "ORDER_STATE";//轮胎订单状态(orderType:0) :1 已安装 2 待服务 3 支付成功 4 支付失败 5 待支付 6 已退货
    public static String ORDER_STAGE = "ORDER_STAGE";//orderStage:订单二段状态 1 默认(不需要支付差价)  2 待车主支付差价 3 已支付差价 4 待车主支付运费 5 已支付运费
    // 订单状态(orderType::1 2 3 4 ): 1 交易完成 2 待收货 3 待商家确认服务 4 作废 5 待发货 6 待车主确认服务 7 待评价 8 待支付
    public static String ORDER_FROM = "ORDER_FROM";  //0是来自收银台  1是来自订单
    private int orderType;
    public String orderInfo = "";   // 订单信息
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
                Intent intent1 = new Intent(getApplicationContext(), PaySuccessActivity.class);
                intent1.putExtra("ORDERTYPE", orderType);
                intent1.putExtra(ORDER_STAGE, orderStage);
                startActivity(intent1);

            } else {
                // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                Toast.makeText(PaymentActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
            }
            // Toast.makeText(PaymentActivity.this, payResult.getResult(), Toast.LENGTH_LONG).show();
        }

        ;
    };

    /**
     * 支付宝支付业务：入参app_id
     */
    public static final String APPID = "2018051760091839";

    /**
     * 支付宝账户登录授权业务：入参pid值
     */
    public static final String PID = "2088821219284232";
    private ProgressDialog progressDialog;

    //微信
    private static final String APP_ID = "wx407c59de8b10c601";
    private static final int THUMB_SIZE = 150;
    private IWXAPI api;
    private int mTargetScene = SendMessageToWX.Req.WXSceneSession;


    private FrameLayout yuELayout;
    private FrameLayout weixinLayout;
    private FrameLayout zhifubaoLayout;
    private ImageView yuEImage;
    private ImageView weixinImage;
    private ImageView zhifubaoImage;
    public double lineCredit = 1000.00;
    private TextView otherPayLayout;
    private int orderStage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment, R.id.my_action);
        actionBar = (ActionBar) findViewById(R.id.my_action);
        actionBar.setTitle("收银台");
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
        api = WXAPIFactory.createWXAPI(this, Constants.APP_ID,true);
        api.registerApp(Constants.APP_ID);
        progressDialog = new ProgressDialog(this);
        Intent intent = getIntent();
        allprice = intent.getDoubleExtra(ALL_PRICE, 0.00);
        Log.e(TAG, "onCreate: +---" + allprice);
        orderno = intent.getStringExtra(ORDERNO);
        orderType = intent.getIntExtra(ORDER_TYPE, 0);
        if (orderType == 3){
            orderStage = intent.getIntExtra(ORDER_STAGE,0);
        }

        if (orderType == 1){
            currentType = 0;
        }else {
            currentType = 1;
        }


        initView();
        getDataFromService();
    }

    private void getDataFromService() {

    }

    private void initView() {
        payButton = (TextView) findViewById(R.id.payment_button);
        allPriceText = (TextView) findViewById(R.id.price_text);
        allPriceText.setText(allprice + "");
        yuELayout = (FrameLayout) findViewById(R.id.yu_e_layout);
        weixinLayout = (FrameLayout) findViewById(R.id.weixin_layout);
        zhifubaoLayout = (FrameLayout) findViewById(R.id.zhifubao_layout);
        yuEImage = (ImageView) findViewById(R.id.yu_e_image);
        weixinImage = (ImageView) findViewById(R.id.weixin_image);
        zhifubaoImage = (ImageView) findViewById(R.id.zhifubao_image);
        otherPayLayout = (TextView) findViewById(R.id.other_pay_layout);
        initZhifuLayout();

        RxViewAction.clickNoDouble(yuELayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        currentType = 0;
                        initZhifuLayout();
                    }
                });
        RxViewAction.clickNoDouble(weixinLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        currentType = 1;
                        initZhifuLayout();
                    }
                });
        RxViewAction.clickNoDouble(zhifubaoLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        currentType = 2;
                        initZhifuLayout();
                    }
                });


        RxViewAction.clickNoDouble(payButton)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (currentType == 0) {  //余额支付
                            if (allprice > lineCredit) {
                                Toast.makeText(PaymentActivity.this, "信用额度不足，请使用其他支付方式", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            if (orderType == 0) {        //轮胎订单
                                Toast.makeText(PaymentActivity.this, "不支持信用值支付，请选择其他支付方式", Toast.LENGTH_SHORT).show();
                                //  postTireOrder();
                            } else if (orderType == 1) {  //商品订单
                                postGoodsOrder();
                            } else if (orderType == 99) {  //畅行无忧
                                //  postTireOrder();
                                Toast.makeText(PaymentActivity.this, "不支持信用值支付，请选择其他支付方式", Toast.LENGTH_SHORT).show();
                            }


                        } else if (currentType == 1) {//微信支付
                            weixinPay();
                          /*  //初始化一个WXTextObject对象
                            WXTextObject textObject = new WXTextObject();
                            textObject.text = "111";
                            //用WXTextObject对象初始化一个WXMediaMessage对象
                            WXMediaMessage msg = new WXMediaMessage();
                            msg.mediaObject = textObject;
                            msg.description = "111";
                            //构建一个Req
                            SendMessageToWX.Req req = new SendMessageToWX.Req();
                            req.transaction = String.valueOf(System.currentTimeMillis()); //transaction 字段用于唯一标识一个请求
                            req.message = msg;
                            api.sendReq(req);*/


                       /*     WXWebpageObject webpager = new WXWebpageObject();
                            webpager.webpageUrl = "www.baidu.com";

                            WXMediaMessage msg = new WXMediaMessage(webpager);
                            msg.title = "百度一下";
                            msg.description = "百度一下，你就知道";
                            Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
                            Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE, THUMB_SIZE, true);
                            bmp.recycle();
                            msg.thumbData = Util.bmpToByteArray(thumbBmp, true);

                            SendMessageToWX.Req req = new SendMessageToWX.Req();
                            req.transaction = buildTransaction("webpage");
                            req.message = msg;
                            req.scene = mTargetScene;
                            api.sendReq(req);
                            finish();*/
                          /*  WXWebpageObject webpage = new WXWebpageObject();
                            webpage.webpageUrl = "http://www.qq.com";
                            WXMediaMessage msg = new WXMediaMessage(webpage);
                            msg.title = "WebPage Title WebPage Title WebPage Title WebPage Title WebPage Title WebPage Title WebPage Title WebPage Title WebPage Title Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long";
                            msg.description = "WebPage Description WebPage Description WebPage Description WebPage Description WebPage Description WebPage Description WebPage Description WebPage Description WebPage Description Very Long Very Long Very Long Very Long Very Long Very Long Very Long";
                            Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.send_music_thumb);
                            Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE, THUMB_SIZE, true);
                            bmp.recycle();
                            msg.thumbData = Util.bmpToByteArray(thumbBmp, true);

                            SendMessageToWX.Req req = new SendMessageToWX.Req();
                            req.transaction = buildTransaction("webpage");
                            req.message = msg;
                            req.scene = mTargetScene;
                            api.sendReq(req);*/

                        } else if (currentType == 2) { //支付宝支付
                            //获取签名后的orderInfo
                            getSign();
                        }

                    }
                });
    }

    private void weixinPay() {
      /*  JSONObject jsonObject = new JSONObject();
        RequestParams params = new RequestParams("http://wxpay.wxutil.com/pub_v2/app/app_pay.php");

        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess: --------" + result);
                try {
                    JSONObject data = new JSONObject(result);
                    PayReq req = new PayReq();
                    req.appId = data.getString("appid");
                    ;
                    req.partnerId = data.getString("partnerid");
                    req.prepayId = data.getString("prepayid");
                    req.nonceStr = data.getString("noncestr");
                    req.timeStamp = data.getString("timestamp");
                    req.packageValue = data.getString("package");
                    req.sign = data.getString("sign");
                    api.sendReq(req);
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
        });*/


        int userId = new DbConfig(this).getId();
        JSONObject jsonObject = new JSONObject();
        Log.e(TAG, "getSign: orderno-" + orderno);
        try {
            jsonObject.put("orderNo", orderno);
            if (orderType == 99){
                jsonObject.put("orderType", 0);
            }else {
                jsonObject.put("orderType", orderType);
            }

            if (orderType == 0) {    //轮胎订单
                jsonObject.put("orderName", "轮胎购买");
            }else if (orderType == 99){
                jsonObject.put("orderName", "畅行无忧购买");
            }else if (orderType == 3){
                if (orderStage == 4){
                    jsonObject.put("orderName", "轮胎补差");
                }else {
                    jsonObject.put("orderName", "补邮费");
                }

            }else {     //商品订单
                jsonObject.put("orderName", "商品购买");
            }
            jsonObject.put("orderPrice", "0.1");
            jsonObject.put("userId", userId);
        } catch (JSONException e) {
        }
        Log.e(TAG, "getSign: " + orderno);
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "getWeixinPaySign");
        String token = new DbConfig(this).getToken();
        String jsonByToken = "";
        try {
            jsonByToken = XMJJUtils.encodeJsonByToken(jsonObject.toString(), token);


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
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess: --------" + result);
                try {
                    JSONObject jsonObject1 = new JSONObject(result);;
                    String status = jsonObject1.getString("status");
                    String msg = jsonObject1.getString("msg");
                    if (status.equals("200")){
                        JSONObject data = jsonObject1.getJSONObject("data");
                        PayReq req = new PayReq();
                        req.appId = data.getString("appid");
                        req.partnerId = data.getString("partnerid");
                        req.prepayId = data.getString("prepayid");
                        req.nonceStr = data.getString("noncestr");
                        req.timeStamp = data.getString("timestamp");
                        req.packageValue = data.getString("package");
                        req.sign = data.getString("sign");
                        api.sendReq(req);
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

    /**
     * 提交商品订单
     */
    private void postGoodsOrder() {
        int userId = new DbConfig(this).getId();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("orderNo", orderno);
            jsonObject.put("userId", userId);

        } catch (JSONException e) {
        }
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "addConfirmStockOrder");
        String token = new DbConfig(this).getToken();
        String jsonByToken = "";
        try {
            jsonByToken = XMJJUtils.encodeJsonByToken(jsonObject.toString(), token);


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
                Log.e(TAG, "onSuccess: --*--" + result);
                JSONObject jsonObject1 = null;
                try {
                    jsonObject1 = new JSONObject(result);
                    String status = jsonObject1.getString("status");
                    String msg = jsonObject1.getString("msg");
                    if (status.equals("1")) {
                        Intent intent1 = new Intent(getApplicationContext(), PaySuccessActivity.class);
                        intent1.putExtra("ORDERTYPE", orderType);
                        startActivity(intent1);
                        finish();
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

    /**
     * 提交轮胎订单
     */
    private void postTireOrder() {
        int userId = new DbConfig(this).getId();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("orderNo", orderno);
            jsonObject.put("userId", userId);

        } catch (JSONException e) {
        }
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "addConfirmUserShoeCxwyOrder");
        String token = new DbConfig(this).getToken();
        String jsonByToken = "";
        try {
            jsonByToken = XMJJUtils.encodeJsonByToken(jsonObject.toString(), token);


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
                Log.e(TAG, "onSuccess: --*--" + result);
                JSONObject jsonObject1 = null;
                try {
                    jsonObject1 = new JSONObject(result);
                    String status = jsonObject1.getString("status");
                    String msg = jsonObject1.getString("msg");
                    if (status.equals("1")) {

                        Intent intent1 = new Intent(getApplicationContext(), PaySuccessActivity.class);
                        intent1.putExtra("ORDERTYPE", orderType);
                        startActivity(intent1);
                        finish();
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

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }


    private void initZhifuLayout() {
        if (orderType == 1){
            yuELayout.setVisibility(View.VISIBLE);
            otherPayLayout.setVisibility(View.VISIBLE);
        }else {
            yuELayout.setVisibility(View.GONE);
            otherPayLayout.setVisibility(View.GONE);
        }
        if (currentType == 0) {  //余额
            yuEImage.setImageResource(R.drawable.ic_yes);
            weixinImage.setImageResource(R.drawable.ic_no);
            zhifubaoImage.setImageResource(R.drawable.ic_no);
        } else if (currentType == 1) {//微信
            yuEImage.setImageResource(R.drawable.ic_no);
            weixinImage.setImageResource(R.drawable.ic_yes);
            zhifubaoImage.setImageResource(R.drawable.ic_no);
        } else if (currentType == 2) {//支付宝
            yuEImage.setImageResource(R.drawable.ic_no);
            weixinImage.setImageResource(R.drawable.ic_no);
            zhifubaoImage.setImageResource(R.drawable.ic_yes);
        }
    }

    private void getSign() {
        int userId = new DbConfig(this).getId();
        JSONObject jsonObject = new JSONObject();
        Log.e(TAG, "getSign: orderno-" + orderno);
        try {
            jsonObject.put("orderNo", orderno);
            if (orderType == 99){
                jsonObject.put("orderType", 0);
            }else {
                jsonObject.put("orderType", orderType);
            }

            if (orderType == 0) {    //轮胎订单
                jsonObject.put("orderName", "轮胎购买");
            }else if (orderType == 99){
                jsonObject.put("orderName", "畅行无忧购买");
            }else if (orderType == 3){
                if (orderStage == 4){
                    jsonObject.put("orderName", "轮胎补差");
                }else {
                    jsonObject.put("orderName", "补邮费");
                }

            }else {     //商品订单
                jsonObject.put("orderName", "商品购买");
            }
            jsonObject.put("orderPrice", 0.01);
            jsonObject.put("userId", userId);
        } catch (JSONException e) {
        }
        Log.e(TAG, "getSign: " + orderno);
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "getAliPaySign");
        String token = new DbConfig(this).getToken();
        String jsonByToken = "";
        try {
            jsonByToken = XMJJUtils.encodeJsonByToken(jsonObject.toString(), token);


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


    @Override
    public void onBackPressed() {
        if(orderType == 3){
            super.onBackPressed();
        }else {
            showDialog("您确认要离开支付订单界面，离开订单会变为代付款订单，可在待付款订单中查看");

        }

    }

    private void showDialog(String msg) {
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
                intent.putExtra(ORDERNO, orderno);
                intent.putExtra(ORDER_TYPE, orderType);
                intent.putExtra(ORDER_FROM, 0);
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
