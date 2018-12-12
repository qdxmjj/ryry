package com.ruyiruyi.ruyiruyi.ui.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ruyiruyi.ruyiruyi.R;
import com.ruyiruyi.ruyiruyi.db.DbConfig;
import com.ruyiruyi.ruyiruyi.db.model.User;
import com.ruyiruyi.ruyiruyi.ui.activity.base.RyBaseActivity;
import com.ruyiruyi.ruyiruyi.ui.fragment.OrderFragment;
import com.ruyiruyi.ruyiruyi.ui.multiType.Code;
import com.ruyiruyi.ruyiruyi.ui.multiType.CodeViewBinder;
import com.ruyiruyi.ruyiruyi.ui.multiType.CountOne;
import com.ruyiruyi.ruyiruyi.ui.multiType.CountOneViewBinder;
import com.ruyiruyi.ruyiruyi.ui.multiType.CxwyOrder;
import com.ruyiruyi.ruyiruyi.ui.multiType.CxwyOrderViewBinder;
import com.ruyiruyi.ruyiruyi.ui.multiType.GoodsInfo;
import com.ruyiruyi.ruyiruyi.ui.multiType.GoodsInfoViewBinder;
import com.ruyiruyi.ruyiruyi.ui.multiType.InfoOne;
import com.ruyiruyi.ruyiruyi.ui.multiType.InfoOneViewBinder;
import com.ruyiruyi.ruyiruyi.ui.multiType.TireInfo;
import com.ruyiruyi.ruyiruyi.ui.multiType.TireInfoViewBinder;
import com.ruyiruyi.ruyiruyi.utils.Constants;
import com.ruyiruyi.ruyiruyi.utils.RequestUtils;
import com.ruyiruyi.ruyiruyi.utils.Util;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;
import com.ruyiruyi.rylibrary.cell.ActionBar;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import me.drakeet.multitype.MultiTypeAdapter;
import rx.functions.Action1;

import static me.drakeet.multitype.MultiTypeAsserts.assertAllRegistered;
import static me.drakeet.multitype.MultiTypeAsserts.assertHasTheSameAdapter;

public class OrderInfoActivity extends RyBaseActivity implements InfoOneViewBinder.OnInfoItemClick, CountOneViewBinder.OnCxwyCountClikc {
    private static final String TAG = OrderInfoActivity.class.getSimpleName();
    private ActionBar actionBar;
    private RecyclerView listView;
    private List<Object> items = new ArrayList<>();
    private MultiTypeAdapter adapter;
    private String orderNo;
    private int orderType;
    private int orderState;
    public List<TireInfo> tireInfoList;
    public List<TireInfo> buchaTireList;
    public List<TireInfo> noBuchaTireList;
    public List<GoodsInfo> goodsInfoList;
    private String userPhone;
    private String userName;
    private String carNumber;
    private String orderTotalPrice;
    private String orderActuallyPrice;
    private String orderImg;
    private String storeId;
    private String storeName;
    private TextView orderButton;
    public List<String> codeList;
    public List<String> oldCodeList;
    public List<InfoOne> freeRepairList;
    public TireInfo tireInfo;
    public CxwyOrder cxwyOrder;
    private int orderStage = 1;
    private LinearLayout orderBuchaLayout;
    private LinearLayout buchaLayout;
    private LinearLayout repairLayout;
    private int cxwyCount = 0;
    private int currentCxwyCount = 0;
    private int userCarId;
    private Double currentPrice = 0.00;
    private String associationOrderNo;
    private String makeUpDifferencePrice;
    private int usedCxwAmount;
    private String postage;
    private String origin;
    public String saleName;
    private ImageView hbImageView;
    private Dialog dialog;
    private View inflate;
    private LinearLayout weixinShareLayout;
    private LinearLayout pengyouquanLayout;
    private int mTargetScene = SendMessageToWX.Req.WXSceneSession;
    private String redpacketUrl;
    private String redpacketTitle;
    private String redpacketBody;
    private static final int THUMB_SIZE = 150;
    private IWXAPI api;
    private String wenUrl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_info);
        actionBar = (ActionBar) findViewById(R.id.my_action);

        actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(int var1) {
                switch ((var1)) {
                    case -1:
                        onBackPressed();
                        break;
                    case -3:
                        if (orderType == 1) {
                            goodsTuikuan();
                        } else if (orderType == 0) {
                            tireTuikuan();
                        } else if (orderType == 2) {
                            cancleFirstTire();
                        } else if (orderType == 4) {
                            cancleTireRepair();
                        } else if (orderType == 3) {
                            cancleFreeTire();
                        }

                        break;
                }
            }
        });

        api = WXAPIFactory.createWXAPI(this, Constants.APP_ID);

        tireInfoList = new ArrayList<>();
        goodsInfoList = new ArrayList<>();
        codeList = new ArrayList<>();
        oldCodeList = new ArrayList<>();
        buchaTireList = new ArrayList<>();
        noBuchaTireList = new ArrayList<>();
        freeRepairList = new ArrayList<>();
        Intent intent = getIntent();
        // OrderType 0:轮胎购买订单 1:普通商品购买订单 2:首次更换订单 3:免费再换订单 4:轮胎修补订单
        //轮胎订单状态(orderType:0) :1 已安装 2 待服务 3 支付成功 4 支付失败 5 待支付 6 已退货 7退款中 8是已退款 9作废
        //订单状态(orderType:1 2 3 4 ): 1 交易完成 2 待收货 3 待商家确认服务 4 作废 5 待发货 6 待车主确认服务 7 待评价 8 待支付  11审核中  12 审核失败
        //orderStage:订单二段状态 1 默认(不需要支付差价)  2 待车主支付差价 3 已支付差价 4 待车主支付运费 5 已支付运费
        orderNo = intent.getStringExtra(PaymentActivity.ORDERNO);
        orderType = intent.getIntExtra(PaymentActivity.ORDER_TYPE, 0);
        orderState = intent.getIntExtra(PaymentActivity.ORDER_STATE, 0);
        if (orderType != 0) {
            orderStage = intent.getIntExtra(PaymentActivity.ORDER_STAGE, 1);
        }
        Log.e(TAG, "onCreate: ----*-----" + orderStage);
        if (orderType == 0) {//轮胎订单状态(orderType:0) :1 已安装 2 待服务 3 支付成功 4 支付失败 5 待支付 6 已退货 7退款中 8是已退款 9作废
            if (orderState == 3) {
                actionBar.setTitle("交易完成");
            } else if (orderState == 6) {
                actionBar.setTitle("已退货");
            } else if (orderState == 7) {
                actionBar.setTitle("退款中");
            } else if (orderState == 8) {
                actionBar.setTitle("已退款");
            } else if (orderState == 9) {
                actionBar.setTitle("订单已取消");
            }
        } else {
            if (orderStage == 1) {
                if (orderState == 5) {
                    actionBar.setTitle("待发货");
                } else if (orderState == 2) {
                    actionBar.setTitle("待收货");
                } else if (orderState == 3) {
                    actionBar.setTitle("待商家确认服务");
                } else if (orderState == 6) {
                    actionBar.setTitle("确认服务");
                } else if (orderState == 1) {
                    actionBar.setTitle("交易完成");
                } else if (orderState == 9) {
                    actionBar.setTitle("退款中");
                } else if (orderState == 10) {
                    actionBar.setTitle("退款成功");
                } else if (orderState == 4) {
                    actionBar.setTitle("订单已取消");
                } else if (orderState == 11) {
                    actionBar.setTitle("审核中");
                } else if (orderState == 12) {
                    actionBar.setTitle("审核未通过");
                } else if (orderState == 13) {
                    actionBar.setTitle("审核通过");
                } else if (orderState == 14) {
                    actionBar.setTitle("拒绝服务");
                } else if (orderState == 15) {
                    actionBar.setTitle("订单已取消");
                }

            } else if (orderStage == 2) {
                actionBar.setTitle("待车主补差");
            } else if (orderStage == 3) {
                actionBar.setTitle("已补差");
            } else if (orderStage == 4) {
                actionBar.setTitle("待支付邮费");
            } else if (orderStage == 5) {
                actionBar.setTitle("已支付邮费");
            }

        }


        initView();
        initOrderFromService();
    }

    /**
     * 取消免费再换定的按
     */
    private void cancleFreeTire() {
        int userId = new DbConfig(this).getId();
        JSONObject jsonObject = new JSONObject();
        Log.e(TAG, "initOrderFromService:--- " + orderType);
        try {
            jsonObject.put("orderNo", orderNo);
            jsonObject.put("userId", userId);
        } catch (JSONException e) {
        }
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "cancelFreeChangeOrder");
        Log.e(TAG, "initOrderFromService: -++-" + jsonObject.toString());
        params.addBodyParameter("reqJson", jsonObject.toString());
        String token = new DbConfig(this).getToken();
        params.addParameter("token", token);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess: " + result);
                JSONObject jsonObject1 = null;
                try {
                    jsonObject1 = new JSONObject(result);
                    String status = jsonObject1.getString("status");
                    String msg = jsonObject1.getString("msg");
                    if (status.equals("1")) {
                        Toast.makeText(OrderInfoActivity.this, msg, Toast.LENGTH_SHORT).show();
                        //    setResult(TireWaitChangeActivity.TIREWAIT, new Intent());
                        finish();
                    } else if (status.equals("-999")) {
                        showUserTokenDialog("您的账号在其它设备登录,请重新登录");
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
     * '
     * 初始化action Righ
     */
    private void initActionRight() {
        if (orderType == 1) {
            if (orderState == 3) {
                actionBar.setRightView("退款");
            }
        } else if (orderType == 0) {
            if (orderState == 3) {
                if (tireInfo.getTireCount() != 0) {  //畅行无忧订单不可退款
                    actionBar.setRightView("退款");
                }

            }
        } else if (orderType == 2) {
            if (orderState == 5) {
                actionBar.setRightView("取消订单");
            }
        } else if (orderType == 4) {
            if (orderState == 3) {
                actionBar.setRightView("取消订单");
            }
        } else if (orderType == 3) {
            if (orderState == 11 || orderState == 5) {
                actionBar.setRightView("取消订单");
            }
        }

    }

    /**
     * 取消轮胎修补订单
     */
    private void cancleTireRepair() {
        int userId = new DbConfig(this).getId();
        JSONObject jsonObject = new JSONObject();
        Log.e(TAG, "initOrderFromService:--- " + orderType);
        try {
            jsonObject.put("orderNo", orderNo);
            jsonObject.put("userId", userId);
        } catch (JSONException e) {
        }
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "cancelShoeRepairOrder");
        Log.e(TAG, "initOrderFromService: -++-" + jsonObject.toString());
        params.addBodyParameter("reqJson", jsonObject.toString());
        String token = new DbConfig(this).getToken();
        params.addParameter("token", token);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess: " + result);
                JSONObject jsonObject1 = null;
                try {
                    jsonObject1 = new JSONObject(result);
                    String status = jsonObject1.getString("status");
                    String msg = jsonObject1.getString("msg");
                    if (status.equals("1")) {
                        Toast.makeText(OrderInfoActivity.this, msg, Toast.LENGTH_SHORT).show();
                        //    setResult(TireWaitChangeActivity.TIREWAIT, new Intent());
                        finish();
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
     * 取消首次更换订单
     */
    private void cancleFirstTire() {
        int userId = new DbConfig(this).getId();
        JSONObject jsonObject = new JSONObject();
        Log.e(TAG, "initOrderFromService:--- " + orderType);
        try {
            jsonObject.put("orderNo", orderNo);
            jsonObject.put("userId", userId);
        } catch (JSONException e) {
        }
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "cancelFirstChangeOrder");
        Log.e(TAG, "initOrderFromService: -++-" + jsonObject.toString());
        params.addBodyParameter("reqJson", jsonObject.toString());
        String token = new DbConfig(this).getToken();
        params.addParameter("token", token);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess: " + result);
                JSONObject jsonObject1 = null;
                try {
                    jsonObject1 = new JSONObject(result);
                    String status = jsonObject1.getString("status");
                    String msg = jsonObject1.getString("msg");
                    if (status.equals("1")) {
                        Toast.makeText(OrderInfoActivity.this, msg, Toast.LENGTH_SHORT).show();
                        //    setResult(TireWaitChangeActivity.TIREWAIT, new Intent());
                        finish();
                    } else if (status.equals("-999")) {
                        showUserTokenDialog("您的账号在其它设备登录,请重新登录");
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
     * 轮胎退款
     */
    private void tireTuikuan() {
        int userId = new DbConfig(this).getId();
        JSONObject jsonObject = new JSONObject();
        Log.e(TAG, "initOrderFromService:--- " + orderType);
        try {
            jsonObject.put("orderNo", orderNo);
            jsonObject.put("userId", userId);
        } catch (JSONException e) {
        }
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "refundShoeCxwyOrder");
        Log.e(TAG, "initOrderFromService: -++-" + jsonObject.toString());
        params.addBodyParameter("reqJson", jsonObject.toString());
        String token = new DbConfig(this).getToken();
        params.addParameter("token", token);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess: " + result);
                JSONObject jsonObject1 = null;
                try {
                    jsonObject1 = new JSONObject(result);
                    String status = jsonObject1.getString("status");
                    String msg = jsonObject1.getString("msg");
                    if (status.equals("1")) {
                        Toast.makeText(OrderInfoActivity.this, msg, Toast.LENGTH_SHORT).show();
                        //    setResult(TireWaitChangeActivity.TIREWAIT, new Intent());
                        finish();
                    } else if (status.equals("-999")) {
                        showUserTokenDialog("您的账号在其它设备登录,请重新登录");
                    } else if (status.equals("-1")) {
                        Toast.makeText(OrderInfoActivity.this, msg, Toast.LENGTH_SHORT).show();
                        //    setResult(TireWaitChangeActivity.TIREWAIT, new Intent());
                        finish();
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
     * 商品退款
     */
    private void goodsTuikuan() {
        int userId = new DbConfig(this).getId();
        JSONObject jsonObject = new JSONObject();
        Log.e(TAG, "initOrderFromService:--- " + orderType);
        try {
            jsonObject.put("orderNo", orderNo);
            jsonObject.put("userId", userId);
        } catch (JSONException e) {
        }
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "refundStockOrder");
        Log.e(TAG, "initOrderFromService: -++-" + jsonObject.toString());
        params.addBodyParameter("reqJson", jsonObject.toString());
        String token = new DbConfig(this).getToken();
        params.addParameter("token", token);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess: " + result);
                JSONObject jsonObject1 = null;
                try {
                    jsonObject1 = new JSONObject(result);
                    String status = jsonObject1.getString("status");
                    String msg = jsonObject1.getString("msg");
                    if (status.equals("1")) {
                        Toast.makeText(OrderInfoActivity.this, msg, Toast.LENGTH_SHORT).show();
                        //  setResult(TireWaitChangeActivity.TIREWAIT, new Intent());
                        Intent intent = new Intent(getApplicationContext(), OrderActivity.class);
                        intent.putExtra(OrderFragment.ORDER_TYPE, "DFH");
                        intent.putExtra(OrderActivity.ORDER_FROM, 1);
                        startActivity(intent);
                        finish();
                    } else if (status.equals("-999")) {
                        showUserTokenDialog("您的账号在其它设备登录,请重新登录");
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

    private void initOrderFromService() {
        int userId = new DbConfig(this).getId();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("orderNo", orderNo);
            jsonObject.put("orderType", orderType);
            jsonObject.put("userId", userId);
        } catch (JSONException e) {
        }
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "getUserOrderInfoByNoAndType");
        Log.e(TAG, "initOrderFromService: -++-" + jsonObject.toString());
        params.addBodyParameter("reqJson", jsonObject.toString());
        String token = new DbConfig(this).getToken();
        params.addParameter("token", token);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess: ------" + result);
                JSONObject jsonObject1 = null;
                try {
                    jsonObject1 = new JSONObject(result);
                    String status = jsonObject1.getString("status");
                    String msg = jsonObject1.getString("msg");
                    if (status.equals("1")) {
                        JSONObject data = jsonObject1.getJSONObject("data");
                        if (orderType == 2) {//首次更换订单
                            if (orderState == 5) {  //代发货
                                getFirstTireOrderInfo(data);
                            } else if (orderState == 3 || orderState == 2 || orderState == 6 || orderState == 15 || orderState == 14) { //待商家确认服务 || 待收货  ||待车主确认服务
                                codeList.clear();
                                getFirstTireOrderInfo(data);
                                getFirstTireOrderCode(data);
                            } else if (orderState == 1) { //交易完成分享红包
                                codeList.clear();
                                getFirstTireOrderInfo(data);
                                getFirstTireOrderCode(data);

                                initHongbaoData();
                            }
                        } else if (orderType == 1) { //商品订单
                            if (orderState == 3 || orderState == 6 || orderState == 9 || orderState == 10 || orderState == 15) {//待商家确认服务 || 待车主确认服务
                                getGoodsOrderInfo(data);
                                initActionRight();
                            } else if (orderState == 1) { //交易完成分享红包
                                getGoodsOrderInfo(data);
                                initActionRight();
                                initGoodsHongbaoData();
                            }
                        } else if (orderType == 3) { //免费再换
                            if (orderState == 5 || orderState == 11) {  //代发货  //审核中
                                getFreeTireOrderInfo(data);
                            } else if (orderState == 3 || orderState == 2 || orderState == 6 || orderState == 1 || orderState == 15 || orderState == 13) { //待商家确认服务 || 待收货  ||待车主确认服务
                                codeList.clear();
                                getFreeTireOrderInfo(data);
                                getFreeTireOrderCode(data);
                                getFreeTireOrderOldCode(data);
                                if (orderStage == 2) {   //补差订单 获取畅行无忧数量
                                    getCXWYCountFromService();
                                }
                            } else if (orderState == 12) {        //审核失败
                                getFreeTireOrderInfo(data);
                                //  getFreeTireOrderOldCode(data);
                               /* if (orderStage == 2){   //补差订单 获取畅行无忧数量
                                    getCXWYCountFromService();
                                }*/
                            }
                        } else if (orderType == 0) {  //轮胎购买订单
                            //     if (orderState == 3 ||orderState == 9 || orderState == 10 || orderState == 4) {  //已完成
                            getTireOrderInfo(data);
                            initActionRight();
                            //   }
                        } else if (orderType == 4) {  //免费再换订单
                            getFreeRepairOrderInfo(data);
                        } else if (orderType == 5) {
                            getLimitInfo(data);
                        }
                        if (orderStage != 2) {
                            initData();
                        }

                        initActionRight();

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
     * 商品红包
     */
    private void initGoodsHongbaoData() {
        JSONObject object = new JSONObject();
        try {
            object.put("a", "aa");
        } catch (JSONException e) {
        }
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL_ACTIVITY_RELEASE + "invite/Url");
        params.addBodyParameter("reqJson", object.toString());
        Log.e(TAG, "initGoodsHongbaoData: ---22---" + params);
        x.http().get(params, new Callback.CommonCallback<String>() {


            @Override
            public void onSuccess(String result) {
                JSONObject object = null;
                try {
                    object = new JSONObject(result);
                    JSONObject inviteRegister = object.getJSONObject("inviteRegister");

                    wenUrl = inviteRegister.getString("url");
                    redpacketUrl = inviteRegister.getString("shareUrl") + "?userId=" + new DbConfig(getApplication()).getId();
                    redpacketTitle = inviteRegister.getString("shareTitle");
                    redpacketBody = inviteRegister.getString("shareTitle");
                    hbImageView.setVisibility(View.VISIBLE);
                } catch (JSONException e) {
                    e.printStackTrace();
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
     * 轮胎红包i
     */
    private void initHongbaoData() {
        JSONObject object = new JSONObject();
        String orderNoBase64 = Base64.encodeToString(orderNo.getBytes(), Base64.DEFAULT);
        try {
            object.put("orderInfo", orderNoBase64);

        } catch (JSONException e) {
        }
        RequestParams params = new RequestParams("http://activity.qdxmjj.com:8888/wechat/appGetRedPacketInfo?orderInfo=MjAxODY2Ng==");
        params.addBodyParameter("reqJson", object.toString());
        x.http().get(params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(result);
                    String status = jsonObject.getString("status");
                    if (status.equals("1") || status.equals("-2") || status.equals("-3")) {
                        redpacketUrl = jsonObject.getString("redpacketUrl");
                        redpacketTitle = jsonObject.getString("title");
                        redpacketBody = jsonObject.getString("body");
                        hbImageView.setVisibility(View.VISIBLE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
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

    private void getLimitInfo(JSONObject data) throws JSONException {
        orderImg = data.getString("orderImg");
        orderTotalPrice = data.getString("orderTotalPrice");
        carNumber = data.getString("platNumber");
        storeId = data.getString("storeId");
        storeName = data.getString("storeName");
        userName = data.getString("userName");
        userPhone = data.getString("userPhone");
        orderTotalPrice = data.getString("orderTotalPrice");
    }


    /**
     * 免费修补
     *
     * @param data
     */
    private void getFreeRepairOrderInfo(JSONObject data) throws JSONException {
        orderImg = data.getString("orderImg");
        orderTotalPrice = data.getString("orderTotalPrice");
        carNumber = data.getString("platNumber");
        storeId = data.getString("storeId");
        storeName = data.getString("storeName");
        userName = data.getString("userName");
        userPhone = data.getString("userPhone");
        JSONArray array = data.getJSONArray("userCarShoeOldBarCodeList");
        freeRepairList.clear();
        for (int i = 0; i < array.length(); i++) {
            String barCode = array.getJSONObject(i).getString("barCode");
            String repairAmount = array.getJSONObject(i).getString("repairAmount");
            freeRepairList.add(new InfoOne(barCode, repairAmount, false));
        }
    }


    /**
     * 获取畅行无忧数量
     */
    private void getCXWYCountFromService() {
        User user = new DbConfig(this).getUser();
        int userId = user.getId();
        //  int uesrCarId = user.getCarId();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userId", userId);
            jsonObject.put("userCarId", userCarId);
        } catch (JSONException e) {
        }
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "userCarInfo/queryCarCxwyInfo");
        Log.e(TAG, "initOrderFromService: -++-" + jsonObject.toString());
        params.addBodyParameter("reqJson", jsonObject.toString());
        String token = new DbConfig(this).getToken();
        params.addParameter("token", token);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess: ------" + result);
                JSONObject jsonObject1 = null;
                try {
                    jsonObject1 = new JSONObject(result);
                    String status = jsonObject1.getString("status");
                    String msg = jsonObject1.getString("msg");
                    if (status.equals("1")) {
                        JSONArray data = jsonObject1.getJSONArray("data");
                        int currentCxwyCount = 0;
                        for (int i = 0; i < data.length(); i++) {
                            int cxwyState = data.getJSONObject(i).getInt("cxwyState");
                            if (cxwyState == 1) {
                                currentCxwyCount = currentCxwyCount + 1;
                            }
                        }
                        cxwyCount = currentCxwyCount;
                        initData();
                        Log.e(TAG, "onSuccess:--cxwyCount---- " + cxwyCount);
                    } else if (status.equals("-999")) {
                        showUserTokenDialog("您的账号在其它设备登录,请重新登录");
                    } else if (status.equals("0")) {
                        cxwyCount = 0;
                        initData();
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

    private void getTireOrderInfo(JSONObject data) throws JSONException {

        orderImg = data.getString("orderImg");
        orderTotalPrice = data.getString("orderTotalPrice");
        carNumber = data.getString("platNumber");
        userName = data.getString("userName");
        userPhone = data.getString("userPhone");
        JSONArray array = data.getJSONArray("shoeOrderVoList");
        for (int i = 0; i < array.length(); i++) {
            JSONObject object = array.getJSONObject(i);
            String cxwyAmount = object.getString("cxwyAmount");
            String cxwyPrice = object.getString("cxwyPrice");
            String cxwyTotalPrice = object.getString("cxwyTotalPrice");
            String fontRearFlag = object.getString("fontRearFlag");
            String tireName = "";
            String tireCount = "";
            Double tirePrice = 0.00;
            Double totalPrice = 0.00;
            if (fontRearFlag.equals("2")) { //后轮
                tireName = object.getString("rearShoeName");
                tireCount = object.getString("rearAmount");
                tirePrice = object.getDouble("rearPrice");
                totalPrice = object.getDouble("rearTotalPrice");
            } else { //前轮 或  前后轮
                tireName = object.getString("fontShoeName");
                tireCount = object.getString("fontAmount");
                tirePrice = object.getDouble("fontPrice");
                totalPrice = object.getDouble("fontTotalPrice");
            }
            tireInfo = new TireInfo(orderImg, tireName, Integer.parseInt(tireCount), tirePrice, fontRearFlag);
            if (Integer.parseInt(cxwyAmount) > 0) {
                cxwyOrder = new CxwyOrder(Integer.parseInt(cxwyAmount), cxwyPrice);
            }
        }
    }

    /**
     * 获取免费在换 旧轮胎条形吗
     *
     * @param data
     * @throws JSONException
     */
    private void getFreeTireOrderOldCode(JSONObject data) throws JSONException {
        oldCodeList.clear();
        buchaTireList.clear();
        noBuchaTireList.clear();
        JSONArray userCarShoeBarOldCodeList = data.getJSONArray("userCarShoeOldBarCodeList");
        for (int i = 0; i < userCarShoeBarOldCodeList.length(); i++) {
            JSONObject object = userCarShoeBarOldCodeList.getJSONObject(i);
            String barCode = object.getString("barCode");
            int stage = object.getInt("stage");
            oldCodeList.add(barCode);

            if (stage == 1) {
                String shoeName = object.getString("shoeName");
                String shoeImgUrl = object.getString("shoeImgUrl");
                userCarId = object.getInt("userCarId");
                String fontRearFlag = object.getString("fontRearFlag");
                Double price = object.getDouble("price");
                TireInfo tireInfo = new TireInfo(shoeImgUrl, shoeName, 1, price, fontRearFlag);
                tireInfo.setBarCode(barCode);
                buchaTireList.add(tireInfo);
            } else {
                String shoeName = object.getString("shoeName");
                String shoeImgUrl = object.getString("shoeImgUrl");
                userCarId = object.getInt("userCarId");
                String fontRearFlag = object.getString("fontRearFlag");
                Double price = object.getDouble("price");
                TireInfo tireInfo = new TireInfo(shoeImgUrl, shoeName, 1, price, fontRearFlag);
                tireInfo.setBarCode(barCode);
                noBuchaTireList.add(tireInfo);
            }

        }

        for (int i = 0; i < buchaTireList.size(); i++) {
            currentPrice = currentPrice + buchaTireList.get(i).getTirePrice();
        }
    }

    /**
     * 获取免费再换新轮胎条形码
     *
     * @param data
     * @throws JSONException
     */
    private void getFreeTireOrderCode(JSONObject data) throws JSONException {
        codeList.clear();
        JSONArray userCarShoeBarCodeList = data.getJSONArray("userCarShoeBarCodeList");
        for (int i = 0; i < userCarShoeBarCodeList.length(); i++) {
            String barCode = userCarShoeBarCodeList.getJSONObject(i).getString("barCode");
            codeList.add(barCode);
        }
    }

    /**
     * 免费再换
     *
     * @param data
     * @throws JSONException
     */
    private void getFreeTireOrderInfo(JSONObject data) throws JSONException {
        if (orderState == 13) {
            origin = data.getString("origin");
        }
        orderImg = data.getString("orderImg");
        usedCxwAmount = data.getInt("usedCxwAmount");
        makeUpDifferencePrice = data.getString("makeUpDifferencePrice");
        associationOrderNo = data.getString("associationOrderNo");
        orderTotalPrice = data.getString("orderTotalPrice");
        postage = data.getString("postage");
        carNumber = data.getString("platNumber");
        storeId = data.getString("storeId");
        storeName = data.getString("storeName");
        userName = data.getString("userName");
        userPhone = data.getString("userPhone");
        JSONArray array = data.getJSONArray("freeChangeOrderVoList");
        tireInfoList.clear();
        for (int i = 0; i < array.length(); i++) {
            JSONObject object = array.getJSONObject(i);
            String shoeName = object.getString("shoeName");
            String shoeImg = object.getString("shoeImg");
            String fontRearFlag = object.getString("fontRearFlag");
            int fontAmount = object.getInt("fontAmount");
            int rearAmount = object.getInt("rearAmount");
            TireInfo tireInfo = null;
            if (fontRearFlag.equals("2")) { //后轮
                tireInfo = new TireInfo(orderImg, shoeName, rearAmount, 0.00, fontRearFlag);
            } else if (fontRearFlag.equals("1")) { //前轮
                tireInfo = new TireInfo(orderImg, shoeName, fontAmount, 0.00, fontRearFlag);
            } else {//前后轮一致
                tireInfo = new TireInfo(orderImg, shoeName, fontAmount + rearAmount, 0.00, fontRearFlag);
            }
            tireInfoList.add(tireInfo);
        }
    }

    /**
     * 获取商品订单详情
     *
     * @param data
     */
    private void getGoodsOrderInfo(JSONObject data) throws JSONException {
        orderImg = data.getString("orderImg");
        orderTotalPrice = data.getString("orderTotalPrice");
        orderActuallyPrice = data.getString("orderActuallyPrice");
        carNumber = data.getString("platNumber");
        storeId = data.getString("storeId");
        storeName = data.getString("storeName");
        userName = data.getString("userName");
        userPhone = data.getString("userPhone");
        JSONArray array = data.getJSONArray("stockOrderVoList");
        goodsInfoList.clear();
        for (int i = 0; i < array.length(); i++) {
            JSONObject object = array.getJSONObject(i);
            int amount = object.getInt("amount");
            String detailImage = object.getString("detailImage");
            String detailName = object.getString("detailName");
            String detailPrice = object.getString("detailPrice");
            GoodsInfo goodsInfo = new GoodsInfo();
            goodsInfo.setCurrentCount(amount);
            goodsInfo.setGoodsImage(detailImage);
            goodsInfo.setGoodsPrice(detailPrice);
            goodsInfo.setGoodsName(detailName);
            goodsInfoList.add(goodsInfo);
        }

        try {
            saleName = data.getString("saleName");
        } catch (Exception e) {

        }


    }

    /**
     * 获取首次更换轮胎的条形码
     *
     * @param data
     */
    private void getFirstTireOrderCode(JSONObject data) throws JSONException {
        JSONArray userCarShoeBarCodeList = data.getJSONArray("userCarShoeBarCodeList");
        for (int i = 0; i < userCarShoeBarCodeList.length(); i++) {
            JSONObject object = userCarShoeBarCodeList.getJSONObject(i);
            String code = object.getString("barCode");
            codeList.add(code);
        }
    }

    /**
     * 获取首次更换轮胎的基本信息
     *
     * @throws JSONException
     */
    private void getFirstTireOrderInfo(JSONObject data) throws JSONException {

        orderImg = data.getString("orderImg");
        orderTotalPrice = data.getString("orderTotalPrice");
        carNumber = data.getString("platNumber");
        storeId = data.getString("storeId");
        storeName = data.getString("storeName");
        userName = data.getString("userName");
        userPhone = data.getString("userPhone");
        JSONArray array = data.getJSONArray("firstChangeOrderVoList");
        tireInfoList.clear();
        for (int i = 0; i < array.length(); i++) {
            JSONObject object = array.getJSONObject(i);
            String shoeName = object.getString("shoeName");
            String shoeImg = object.getString("shoeImg");
            String fontRearFlag = object.getString("fontRearFlag");
            int fontAmount = object.getInt("fontAmount");
            int rearAmount = object.getInt("rearAmount");
            TireInfo tireInfo = null;
            if (fontRearFlag.equals("2")) { //后轮
                tireInfo = new TireInfo(orderImg, shoeName, rearAmount, 0.00, fontRearFlag);
            } else if (fontRearFlag.equals("1")) { //前轮
                tireInfo = new TireInfo(orderImg, shoeName, fontAmount, 0.0, fontRearFlag);
            } else {//前后轮一致
                tireInfo = new TireInfo(orderImg, shoeName, fontAmount + rearAmount, 0.00, fontRearFlag);
            }
            tireInfoList.add(tireInfo);
        }
    }

    private void initData() {
        //订单状态(orderType:1 2 3 4 ): 1 交易完成 2 待收货 3 待商家确认服务 4 作废 5 待发货 6 待车主确认服务 7 待评价 8 待支付
        items.clear();
        if (orderType == 2) {    //首次更换
            if (orderState == 5 || orderState == 15) {   //5 待支付  || 已取消
                items.add(new InfoOne("联系人", userName, false));
                items.add(new InfoOne("联系电话", userPhone, false));
                items.add(new InfoOne("车牌号", carNumber, false));
                items.add(new InfoOne("服务项目", "首次更换", false));
                items.add(new InfoOne("店铺名称", storeName, true, true));
                for (int i = 0; i < tireInfoList.size(); i++) {
                    items.add(tireInfoList.get(i));
                }
            } else if (orderState == 3 || orderState == 2 || orderState == 6 || orderState == 1 || orderState == 14) { //待商家确认服务 || 待收货 || 带车主确认服务 ||交易完成
                items.add(new InfoOne("联系人", userName, false));
                items.add(new InfoOne("联系电话", userPhone, false));
                items.add(new InfoOne("车牌号", carNumber, false));
                items.add(new InfoOne("服务项目", "首次更换", false));
                if (orderState == 14) {
                    items.add(new InfoOne("服务进度", "您已拒绝服务", false));
                }
                items.add(new InfoOne("店铺名称", storeName, true, true));
                for (int i = 0; i < tireInfoList.size(); i++) {
                    items.add(tireInfoList.get(i));
                }
                items.add(new InfoOne("轮胎条码", "", false));
                for (int i = 0; i < codeList.size(); i++) {
                    items.add(new Code(codeList.get(i)));
                }
            }
        } else if (orderType == 1) {  //商品订单
            if (orderState == 3 || orderState == 6 || orderState == 1 || orderState == 9 || orderState == 10 || orderState == 15) {   //待商家确认服务 || 带车主确认服务 || 交易完成 || 退款中 || 退款成功
                items.add(new InfoOne("联系人", userName, false));
                items.add(new InfoOne("联系电话", userPhone, false));
                items.add(new InfoOne("车牌号", carNumber, false));
                items.add(new InfoOne("服务项目", "商品订单", false));
                items.add(new InfoOne("实付金额", "￥" + orderActuallyPrice, false));
                if (saleName != null && !saleName.equals("")) {
                    items.add(new InfoOne("优惠券", saleName, false));
                }
                items.add(new InfoOne("店铺名称", storeName, true, true));
                for (int i = 0; i < goodsInfoList.size(); i++) {
                    items.add(goodsInfoList.get(i));
                }
            }
        } else if (orderType == 3) { //免费再换
            if (orderStage == 2 || orderStage == 3 || orderStage == 4 || orderStage == 5) {       //补差订单
                items.add(new InfoOne("联系人", userName, false));
                items.add(new InfoOne("联系电话", userPhone, false));
                items.add(new InfoOne("车牌号", carNumber, false));
                items.add(new InfoOne("服务项目", "免费再换", false));
                items.add(new InfoOne("审核情况", "审核未通过", false));
                items.add(new InfoOne("失败原因", origin, true));
                items.add(new InfoOne("店铺名称", storeName, true, true));
                for (int i = 0; i < tireInfoList.size(); i++) {
                    items.add(tireInfoList.get(i));
                }
                //修改后注释
             /*   items.add(new InfoOne("新轮胎条码", "", false));
                for (int i = 0; i < codeList.size(); i++) {
                    items.add(new Code(codeList.get(i)));
                }
                items.add(new InfoOne("旧轮胎条码", "", false));
                for (int i = 0; i < oldCodeList.size(); i++) {
                    items.add(new Code(oldCodeList.get(i)));
                }*/

                Log.e(TAG, "initData:---- " + cxwyCount);
                if (orderStage == 2) {           //需要补差
                    items.add(new InfoOne("需要补差得轮胎", "", false));
                    List<Double> priceList = new ArrayList<>();
                    for (int i = 0; i < buchaTireList.size(); i++) {
                        items.add(buchaTireList.get(i));
                        priceList.add(buchaTireList.get(i).getTirePrice());
                    }
                    if (cxwyCount > buchaTireList.size()) {  //判断畅行无忧得数量 跟轮胎得数量
                        items.add(new CountOne(buchaTireList.size(), currentCxwyCount, priceList, currentPrice));
                    } else {
                        items.add(new CountOne(cxwyCount, currentCxwyCount, priceList, currentPrice));
                    }
                } else if (orderStage == 3) { //补差完
                    items.add(new InfoOne("使用畅行无忧数量", usedCxwAmount + "", false));
                    items.add(new InfoOne("补差金额", "￥" + makeUpDifferencePrice, false));
                } else if (orderStage == 4) {
                    items.add(new InfoOne("需补邮费", "￥" + postage, false));
                } else if (orderStage == 5) {
                    items.add(new InfoOne("已支付邮费", "￥" + postage, false));
                }


            } else {                 //普通订单
                if (orderState == 5 || orderState == 15 || orderState == 13) { //5 待支付   13审核通过
                    items.add(new InfoOne("联系人", userName, false));
                    items.add(new InfoOne("联系电话", userPhone, false));
                    items.add(new InfoOne("车牌号", carNumber, false));
                    items.add(new InfoOne("服务项目", "免费再换", false));
                    if (usedCxwAmount != 0) {
                        items.add(new InfoOne("使用畅行无忧数量", usedCxwAmount + "", false));
                    }
                    items.add(new InfoOne("店铺名称", storeName, true, true));
                    for (int i = 0; i < tireInfoList.size(); i++) {
                        items.add(tireInfoList.get(i));
                    }
                } else if (orderState == 3 || orderState == 2) { //待商家确认服务 || 待收货
                    items.add(new InfoOne("联系人", userName, false));
                    items.add(new InfoOne("联系电话", userPhone, false));
                    items.add(new InfoOne("车牌号", carNumber, false));
                    items.add(new InfoOne("服务项目", "免费再换", false));
                    if (usedCxwAmount != 0) {
                        items.add(new InfoOne("使用畅行无忧数量", usedCxwAmount + "", false));
                    }
                    items.add(new InfoOne("店铺名称", storeName, true, true));
                    for (int i = 0; i < tireInfoList.size(); i++) {
                        items.add(tireInfoList.get(i));
                    }
                    items.add(new InfoOne("轮胎条码", "", false));
                    for (int i = 0; i < codeList.size(); i++) {
                        items.add(new Code(codeList.get(i)));
                    }
                } else if (orderState == 6 || orderState == 1) { //带车主确认服务
                    items.add(new InfoOne("联系人", userName, false));
                    items.add(new InfoOne("联系电话", userPhone, false));
                    items.add(new InfoOne("车牌号", carNumber, false));
                    items.add(new InfoOne("服务项目", "免费再换", false));
                    if (usedCxwAmount != 0) {
                        items.add(new InfoOne("使用畅行无忧数量", usedCxwAmount + "", false));
                    }
                    items.add(new InfoOne("店铺名称", storeName, true, true));
                    for (int i = 0; i < tireInfoList.size(); i++) {
                        items.add(tireInfoList.get(i));
                    }
                    items.add(new InfoOne("新轮胎条码", "", false));
                    for (int i = 0; i < codeList.size(); i++) {
                        items.add(new Code(codeList.get(i)));
                    }
                   /* items.add(new InfoOne("旧轮胎条码", "", false));
                    for (int i = 0; i < oldCodeList.size(); i++) {
                        items.add(new Code(oldCodeList.get(i)));
                    }*/
                } else if (orderState == 11) {        //审核中
                    items.add(new InfoOne("联系人", userName, false));
                    items.add(new InfoOne("联系电话", userPhone, false));
                    items.add(new InfoOne("车牌号", carNumber, false));
                    items.add(new InfoOne("服务项目", "免费再换", false));
                    items.add(new InfoOne("服务进程", "审核中", false));
                    items.add(new InfoOne("店铺名称", storeName, true, true));
                    for (int i = 0; i < tireInfoList.size(); i++) {
                        items.add(tireInfoList.get(i));
                    }
                } else if (orderState == 12) {  //审核未通过
                    items.add(new InfoOne("联系人", userName, false));
                    items.add(new InfoOne("联系电话", userPhone, false));
                    items.add(new InfoOne("车牌号", carNumber, false));
                    items.add(new InfoOne("服务项目", "免费再换", true));
                    items.add(new InfoOne("审核情况", "审核未通过", false));
                    items.add(new InfoOne("失败原因", origin, true));
                    items.add(new InfoOne("店铺名称", storeName, true, true));
                    for (int i = 0; i < tireInfoList.size(); i++) {
                        items.add(tireInfoList.get(i));
                    }
                /*    if (noBuchaTireList.size()>0){
                        items.add(new InfoOne("可更换得轮胎","", false));
                        for (int i = 0; i < noBuchaTireList.size(); i++) {
                            items.add(noBuchaTireList.get(i));
                        }
                    }
                    if (buchaTireList.size()>0){
                        items.add(new InfoOne("未达标得轮胎","", false));
                        for (int i = 0; i < buchaTireList.size(); i++) {
                            items.add(buchaTireList.get(i));
                        }
                    }
                    List<Double> priceList= new ArrayList<>();
                    for (int i = 0; i < buchaTireList.size(); i++) {
                        priceList.add(buchaTireList.get(i).getTirePrice());
                    }
                    items.add(new InfoOne("可用畅行无忧数", currentCxwyCount+"" , false));
                    if (cxwyCount > buchaTireList.size()){  //判断畅行无忧得数量 跟轮胎得数量
                        items.add(new CountOne(buchaTireList.size(),currentCxwyCount,priceList,currentPrice));
                    }else {
                        items.add(new CountOne(cxwyCount,currentCxwyCount,priceList,currentPrice));
                    }*/
                }
            }

        } else if (orderType == 0) {
            items.add(new InfoOne("联系人", userName, false));
            items.add(new InfoOne("联系电话", userPhone, false));
            items.add(new InfoOne("车牌号", carNumber, false));
            if (tireInfo.getTireCount() == 0) {
                items.add(new InfoOne("服务项目", "畅行无忧购买", false));
            } else {
                items.add(new InfoOne("服务项目", "轮胎购买", false));
            }

            items.add(new InfoOne("订单总价", "￥" + orderTotalPrice, true));
            if (tireInfo.getTireCount() != 0) {
                items.add(tireInfo);
            }

            if (cxwyOrder != null) {
                items.add(cxwyOrder);
            }
        } else if (orderType == 4) {
            items.add(new InfoOne("联系人", userName, false));
            items.add(new InfoOne("联系电话", userPhone, false));
            items.add(new InfoOne("车牌号", carNumber, false));
            items.add(new InfoOne("服务项目", "免费修补", false));
            items.add(new InfoOne("店铺名称", storeName, true, true));
            items.add(new InfoOne("轮胎条码", "修补次数", false));
            for (int i = 0; i < freeRepairList.size(); i++) {
                items.add(freeRepairList.get(i));
            }
        } else if (orderType == 5) {
            items.add(new InfoOne("联系人", userName, false));
            items.add(new InfoOne("联系电话", userPhone, false));
            items.add(new InfoOne("车牌号", carNumber, false));
            items.add(new InfoOne("服务项目", "信用额度补差", false));
            items.add(new InfoOne("补差金额", orderTotalPrice + "", true));
        }
        assertAllRegistered(adapter, items);
        adapter.notifyDataSetChanged();
    }

    private void initView() {
        buchaLayout = (LinearLayout) findViewById(R.id.jujue_bucha_layout);
        repairLayout = (LinearLayout) findViewById(R.id.tongyi_bucha_layout);
        orderBuchaLayout = (LinearLayout) findViewById(R.id.order_bucha_layout);
        hbImageView = (ImageView) findViewById(R.id.hb_imageview);
        listView = (RecyclerView) findViewById(R.id.order_info_activity);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        listView.setLayoutManager(linearLayoutManager);
        adapter = new MultiTypeAdapter(items);
        register();
        listView.setAdapter(adapter);
        assertHasTheSameAdapter(listView, adapter);

        orderButton = (TextView) findViewById(R.id.order_pay_button);
        initButton();


        //分享
        dialog = new Dialog(this, R.style.ActionSheetDialogStyle);
        inflate = LayoutInflater.from(this).inflate(R.layout.dialog_share, null);
        weixinShareLayout = ((LinearLayout) inflate.findViewById(R.id.weixin_share_layout));
        pengyouquanLayout = ((LinearLayout) inflate.findViewById(R.id.pengyouquan_share_layout));
        dialog.setContentView(inflate);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.y = 50;
        dialogWindow.setAttributes(lp);

        RxViewAction.clickNoDouble(hbImageView)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Log.e(TAG, "call: 111");

                        if (orderType == 1) {
                            Intent intent = new Intent(getApplicationContext(), BottomEventActivity.class);
                            intent.putExtra("webUrl", wenUrl);
                            intent.putExtra("canShare", true);
                            intent.putExtra("shareUrl", redpacketUrl);
                            intent.putExtra("shareDescription", redpacketTitle);
                            startActivity(intent);
                        } else if (orderType == 2) {
                            dialog.show();
                        }
                    }
                });
        //分享到微信
        RxViewAction.clickNoDouble(weixinShareLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        mTargetScene = SendMessageToWX.Req.WXSceneSession;
                        shareToWexin();
                    }
                });
        //分享到朋友圈
        RxViewAction.clickNoDouble(pengyouquanLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        mTargetScene = SendMessageToWX.Req.WXSceneTimeline;
                        shareToWexin();
                    }
                });

        //去补差
        RxViewAction.clickNoDouble(buchaLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (buchaTireList.size() - currentCxwyCount > 0) {
                            showBuchaDialog();
                        } else {
                            Toast.makeText(OrderInfoActivity.this, "您没有未达标轮胎，赶紧前往更换吧！", Toast.LENGTH_SHORT).show();
                        }


                    }
                });
        //去换胎
        RxViewAction.clickNoDouble(repairLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        int allTireCount = 0;
                        for (int i = 0; i < tireInfoList.size(); i++) {
                            int tireCount = tireInfoList.get(i).getTireCount();
                            allTireCount += tireCount;
                        }
                        Log.e(TAG, "call:-------------------------------------------------- " + noBuchaTireList.size());
                        Log.e(TAG, "call:---------------------------------+++-------------- " + buchaTireList.size());
                        Log.e(TAG, "call:---------------------------------***-------------- " + allTireCount);

                        //如果选中的轮胎全部不符合规格
                        if (allTireCount + currentCxwyCount - buchaTireList.size() > 0) {
                            showServiceDialog();
                        } else { //有符合规格 有不符合规格的轮胎
                            Toast.makeText(OrderInfoActivity.this, "您没有达到可更换标准得轮胎，请使用畅行无忧或者补差后继续操作", Toast.LENGTH_SHORT).show();

                        }
                        //buchaOrder();
                    }
                });

        RxViewAction.clickNoDouble(orderButton)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (orderStage == 1) {
                            if (orderState == 6) {   //待车主确认服务
                                userAffirmService();
                            } else if (orderState == 13) {    //审核通过后前往更换轮胎
                                goRepariTire();
                            } else if (orderState == 12) {  //审核未通过 重新下单
                                startActivity(new Intent(getApplicationContext(), TireFreeChangeActivity.class));
                                finish();
                            }

                        } else if (orderStage == 4) { //支付邮费
                            buYoufei();
                        }
                    }
                });
    }

    /**
     * 分享到微信
     */
    private void shareToWexin() {
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = redpacketUrl;
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = redpacketTitle;
        msg.description = redpacketBody;
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_logo);
        Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE, THUMB_SIZE, true);
        bmp.recycle();
        msg.thumbData = Util.bmpToByteArray(thumbBmp, true);
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("webpage");
        req.message = msg;
        req.scene = mTargetScene;
        api.sendReq(req);
    }


    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }


    /**
     * 确认服务
     */
    private void goRepariTire() {
        int userId = new DbConfig(this).getId();
        JSONObject jsonObject = new JSONObject();
        Log.e(TAG, "initOrderFromService:--- " + orderType);
        try {
            jsonObject.put("orderNo", orderNo);
            jsonObject.put("userId", userId);
            jsonObject.put("cxwyAmount", currentCxwyCount);
        } catch (JSONException e) {
        }
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "confirmUserFreeChangeOrder");
        Log.e(TAG, "initOrderFromService: -++-" + jsonObject.toString());
        params.addBodyParameter("reqJson", jsonObject.toString());
        String token = new DbConfig(this).getToken();
        params.addParameter("token", token);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess: ----" + result);
                JSONObject jsonObject1 = null;
                try {
                    jsonObject1 = new JSONObject(result);
                    String status = jsonObject1.getString("status");
                    String msg = jsonObject1.getString("msg");
                    if (status.equals("1")) {
                        Intent intent = new Intent();
                        intent.putExtra("From", 65636);
                        setResult(0, intent);
                        finish();
                    } else if (status.equals("-999")) {
                        showUserTokenDialog("您的账号在其它设备登录,请重新登录");
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
     * 补邮费
     */
    private void buYoufei() {
        Intent intent = new Intent(getApplicationContext(), PaymentActivity.class);
        Log.e(TAG, "buYoufei: --" + postage);
        intent.putExtra(PaymentActivity.ALL_PRICE, Double.parseDouble(postage));
        intent.putExtra(PaymentActivity.ORDERNO, orderNo);
        intent.putExtra(PaymentActivity.ORDER_TYPE, orderType);  //3
        intent.putExtra(PaymentActivity.ORDER_STAGE, orderStage);  //3
        startActivity(intent);
    }

    /**
     * 确认服务
     */
    private void showServiceDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.dialog_error, null);
        TextView error_text = (TextView) dialogView.findViewById(R.id.error_text);
        if ((buchaTireList.size() - currentCxwyCount) > 0) {
            error_text.setText("您还有审核未通过得轮胎，是否免费安装已通过轮胎");
        } else {
            error_text.setText("请确认前往更换");
        }

        dialog.setTitle("如意如驿");
        dialog.setIcon(R.drawable.ic_logo_login);
        dialog.setView(dialogView);
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                goRepariTire();
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

    /**
     * 确认补差
     */
    private void showBuchaDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.dialog_error, null);
        TextView error_text = (TextView) dialogView.findViewById(R.id.error_text);

        error_text.setText("确认前往补差");


        dialog.setTitle("如意如驿");
        dialog.setIcon(R.drawable.ic_logo_login);
        dialog.setView(dialogView);
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                buchaOrder();
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

    /**
     * 生成补差订单
     */
    private void buchaOrder() {
        int userId = new DbConfig(this).getId();
        JSONObject jsonObject = new JSONObject();
        Log.e(TAG, "initOrderFromService:--- " + orderType);
        try {
            jsonObject.put("associationOrderNo", associationOrderNo);
            jsonObject.put("orderNo", orderNo);
            jsonObject.put("userId", userId);
            jsonObject.put("userCarId", userCarId);
            jsonObject.put("price", currentPrice);
            jsonObject.put("cxwyAmount", currentCxwyCount);
        } catch (JSONException e) {
        }
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "addMakeUpDifferenceOrder");
        Log.e(TAG, "initOrderFromService: -++-" + jsonObject.toString());
        params.addBodyParameter("reqJson", jsonObject.toString());
        String token = new DbConfig(this).getToken();
        params.addParameter("token", token);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess: " + result);
                JSONObject jsonObject1 = null;
                try {
                    jsonObject1 = new JSONObject(result);
                    String status = jsonObject1.getString("status");
                    String msg = jsonObject1.getString("msg");
                    if (status.equals("1")) {
                        Toast.makeText(OrderInfoActivity.this, msg, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), PaymentActivity.class);
                        intent.putExtra(PaymentActivity.ALL_PRICE, currentPrice);
                        intent.putExtra(PaymentActivity.ORDERNO, orderNo);
                        intent.putExtra(PaymentActivity.ORDER_TYPE, orderType);  //3
                        startActivity(intent);

                        finish();
                    } else if (status.equals("-999")) {
                        showUserTokenDialog("您的账号在其它设备登录,请重新登录");
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
     * 拒绝补差
     */
    private void cancleBucha() {
        int userId = new DbConfig(this).getId();
        JSONObject jsonObject = new JSONObject();
        Log.e(TAG, "initOrderFromService:--- " + orderType);
        try {
            jsonObject.put("orderNo", orderNo);
            jsonObject.put("userId", userId);
        } catch (JSONException e) {
        }
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "refuseMakeUpDifferenceOrder");
        Log.e(TAG, "initOrderFromService: -++-" + jsonObject.toString());
        params.addBodyParameter("reqJson", jsonObject.toString());
        String token = new DbConfig(this).getToken();
        params.addParameter("token", token);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess: " + result);
                JSONObject jsonObject1 = null;
                try {
                    jsonObject1 = new JSONObject(result);
                    String status = jsonObject1.getString("status");
                    String msg = jsonObject1.getString("msg");
                    if (status.equals("1")) {
                        Toast.makeText(OrderInfoActivity.this, msg, Toast.LENGTH_SHORT).show();
                        finish();
                    } else if (status.equals("-999")) {
                        showUserTokenDialog("您的账号在其它设备登录,请重新登录");
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
     * 车主确认服务
     */
    private void userAffirmService() {
        int userId = new DbConfig(this).getId();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("orderNo", orderNo);
            jsonObject.put("orderType", orderType);
            jsonObject.put("userId", userId);
        } catch (JSONException e) {
        }
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "userConfirmOrderServiced");
        Log.e(TAG, "initOrderFromService: -++-" + jsonObject.toString());
        params.addBodyParameter("reqJson", jsonObject.toString());
        String token = new DbConfig(this).getToken();
        params.addParameter("token", token);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess: ------" + result);
                JSONObject jsonObject1 = null;
                try {
                    jsonObject1 = new JSONObject(result);
                    String status = jsonObject1.getString("status");
                    String msg = jsonObject1.getString("msg");
                    if (status.equals("1")) {
                        Intent intent = new Intent(getApplicationContext(), OrderActivity.class);
                        intent.putExtra(OrderFragment.ORDER_TYPE, "YWC");
                        intent.putExtra(OrderActivity.ORDER_FROM, 1);
                        startActivity(intent);
                        finish();
                    } else if (status.equals("-999")) {
                        showUserTokenDialog("您的账号在其它设备登录,请重新登录");
                    } else {
                        Toast.makeText(OrderInfoActivity.this, msg, Toast.LENGTH_SHORT).show();
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

    private void initButton() {
        //    if (orderType == 2) {    //首次更换
        if (orderStage == 2) {
            orderBuchaLayout.setVisibility(View.VISIBLE);
            orderButton.setVisibility(View.GONE);
        } else if (orderStage == 4) {
            orderBuchaLayout.setVisibility(View.GONE);
            orderButton.setVisibility(View.VISIBLE);
            orderButton.setText("前往补邮费");
            orderButton.setClickable(true);
            orderButton.setBackgroundResource(R.drawable.bg_button);
        } else if (orderStage == 5) {
            orderBuchaLayout.setVisibility(View.GONE);
            orderButton.setVisibility(View.VISIBLE);
            orderButton.setText("已支付运费");
            orderButton.setClickable(false);
            orderButton.setBackgroundResource(R.drawable.bg_button_noclick);
        } else {
            orderBuchaLayout.setVisibility(View.GONE);
            orderButton.setVisibility(View.VISIBLE);

            if (orderType == 0) {
                if (orderState == 3) { //待商家确认服务
                    orderButton.setText("交易完成");
                    orderButton.setClickable(false);
                    orderButton.setBackgroundResource(R.drawable.bg_button_noclick);
                } else if (orderState == 6) {
                    orderButton.setText("已退货");
                    orderButton.setClickable(false);
                    orderButton.setBackgroundResource(R.drawable.bg_button_noclick);
                } else if (orderState == 7) {
                    orderButton.setText("退款中");
                    orderButton.setClickable(false);
                    orderButton.setBackgroundResource(R.drawable.bg_button_noclick);
                } else if (orderState == 8) {
                    ;
                    orderButton.setText("已退款");
                    orderButton.setClickable(false);
                    orderButton.setBackgroundResource(R.drawable.bg_button_noclick);
                } else if (orderState == 9) {
                    orderButton.setText("订单已取消");
                    orderButton.setClickable(false);
                    orderButton.setBackgroundResource(R.drawable.bg_button_noclick);
                }
            } else {
                if (orderState == 5) {
                    orderButton.setText("等待发货");
                    orderButton.setClickable(false);
                    orderButton.setBackgroundResource(R.drawable.bg_button_noclick);
                } else if (orderState == 2) {
                    orderButton.setText("待收货");
                    orderButton.setClickable(false);
                    orderButton.setBackgroundResource(R.drawable.bg_button_noclick);
                } else if (orderState == 3) { //待商家确认服务

                    if (orderStage == 3) {
                        orderButton.setText("已补差，待商家服务");
                        orderButton.setClickable(false);
                        orderButton.setBackgroundResource(R.drawable.bg_button_noclick);
                    } else {
                        orderButton.setText("待商家确认服务");
                        orderButton.setClickable(false);
                        orderButton.setBackgroundResource(R.drawable.bg_button_noclick);
                    }
                } else if (orderState == 6) { //待车主确认服务
                    orderButton.setText("确认服务");
                    orderButton.setClickable(true);
                    orderButton.setBackgroundResource(R.drawable.bg_button);
                } else if (orderState == 1) {
                    orderButton.setText("交易完成");
                    orderButton.setClickable(false);
                    orderButton.setBackgroundResource(R.drawable.bg_button_noclick);
                } else if (orderState == 9) {
                    orderButton.setText("退款中");
                    orderButton.setClickable(false);
                    orderButton.setBackgroundResource(R.drawable.bg_button_noclick);
                } else if (orderState == 10) {
                    orderButton.setText("退款成功");
                    orderButton.setClickable(false);
                    orderButton.setBackgroundResource(R.drawable.bg_button_noclick);
                } else if (orderState == 4) {
                    orderButton.setText("订单已取消");
                    orderButton.setClickable(false);
                    orderButton.setBackgroundResource(R.drawable.bg_button_noclick);
                } else if (orderState == 11) {
                    orderButton.setText("审核中");
                    orderButton.setClickable(false);
                    orderButton.setBackgroundResource(R.drawable.bg_button_noclick);
                } else if (orderState == 13) {
                    orderButton.setText("更换轮胎");
                    orderButton.setClickable(true);
                    orderButton.setBackgroundResource(R.drawable.bg_button);
                } else if (orderState == 12) {
                    orderButton.setText("重新下单");
                    orderButton.setClickable(true);
                    orderButton.setBackgroundResource(R.drawable.bg_button);
                } else if (orderState == 14) {
                    orderButton.setText("您已拒绝服务");
                    orderButton.setClickable(false);
                    orderButton.setBackgroundResource(R.drawable.bg_button_noclick);
                } else if (orderState == 15) {
                    orderButton.setText("订单已取消");
                    orderButton.setClickable(false);
                    orderButton.setBackgroundResource(R.drawable.bg_button_noclick);
                }
            }
        }


      /*  } else if (orderType == 1) {  //商品订单
            if (orderState == 3) { //待商家确认服务
                orderButton.setText("待商家确认服务");
                orderButton.setClickable(false);
                orderButton.setBackgroundResource(R.drawable.bg_button_noclick);
            } else if (orderState == 6) { //待车主确认服务
                orderButton.setText("确认服务");
                orderButton.setClickable(true);
                orderButton.setBackgroundResource(R.drawable.bg_button);
            }
        }else if (orderType == 3){ //免费在还
            if (orderState == 5){
                orderButton.setText("等待发货");
                orderButton.setClickable(false);
                orderButton.setBackgroundResource(R.drawable.bg_button_noclick);
            }else if (orderState == 2) {
                orderButton.setText("待收货");
                orderButton.setClickable(false);
                orderButton.setBackgroundResource(R.drawable.bg_button_noclick);
            } else if (orderState == 3) { //待商家确认服务
                orderButton.setText("待商家确认服务");
                orderButton.setClickable(false);
                orderButton.setBackgroundResource(R.drawable.bg_button_noclick);
            } else if (orderState == 6) { //待车主确认服务
                orderButton.setText("确认服务");
                orderButton.setClickable(true);
                orderButton.setBackgroundResource(R.drawable.bg_button);
            }
        }*/
    }

    private void register() {
        adapter.register(GoodsInfo.class, new GoodsInfoViewBinder(this));
        InfoOneViewBinder infoOneViewBinder = new InfoOneViewBinder();
        infoOneViewBinder.setListener(this);
        adapter.register(InfoOne.class, infoOneViewBinder);
        adapter.register(CxwyOrder.class, new CxwyOrderViewBinder());
        adapter.register(TireInfo.class, new TireInfoViewBinder(this));
        adapter.register(Code.class, new CodeViewBinder());
        CountOneViewBinder countOneViewBinder = new CountOneViewBinder(this);
        countOneViewBinder.setListener(this);
        adapter.register(CountOne.class, countOneViewBinder);
    }

    @Override
    public void onInfoItemClickListener(String name) {
        if (name.equals("店铺名称")) {
            Log.e(TAG, "onInfoItemClickListener: storeid :" + storeId);
            Intent intent = new Intent(this, ShopHomeActivity.class);
            intent.putExtra("STOREID", Integer.parseInt(storeId));
            startActivity(intent);
        }
    }

    /**
     * 畅行无忧数量改变
     */
    @Override
    public void onCxwyCountClikcListener(int currentCxwyCount, Double currentPrice) {
        Log.e(TAG, "onCxwyCountClikcListener: " + currentCxwyCount);
        Log.e(TAG, "onCxwyCountClikcListener:currentPrice- " + currentPrice);
        this.currentPrice = currentPrice;
        this.currentCxwyCount = currentCxwyCount;
        Log.e(TAG, "onCxwyCountClikcListener: currentPrice-" + currentPrice);
        Log.e(TAG, "onCxwyCountClikcListener: currentCxwyCount-" + currentCxwyCount);
        // initData();
    }


}
