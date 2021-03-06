package com.ruyiruyi.merchant.ui.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ruyiruyi.merchant.R;
import com.ruyiruyi.merchant.db.DbConfig;
import com.ruyiruyi.merchant.ui.multiType.modle.Promotion;
import com.ruyiruyi.merchant.ui.multiType.modle.PromotionHasperson;
import com.ruyiruyi.merchant.ui.multiType.PromotionHaspersonViewBinder;
import com.ruyiruyi.merchant.ui.multiType.modle.PromotionNoperson;
import com.ruyiruyi.merchant.ui.multiType.PromotionNopersonViewBinder;
import com.ruyiruyi.merchant.ui.multiType.PromotionViewBinder;
import com.ruyiruyi.merchant.utils.Constants;
import com.ruyiruyi.merchant.utils.Util;
import com.ruyiruyi.merchant.utils.UtilsRY;
import com.ruyiruyi.merchant.utils.UtilsURL;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;
import com.ruyiruyi.rylibrary.base.BaseActivity;
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

public class PromotionActivity extends BaseActivity implements PromotionViewBinder.OnShareViewClick {

    private ActionBar actionBar;
    private RecyclerView mRlv;
    private MultiTypeAdapter multiTypeAdapter;
    private List<Object> items = new ArrayList<>();
    private List<PromotionHasperson> personList;
    private IWXAPI api;
    private static final int THUMB_SIZE = 150;
    private int mTargetScene = SendMessageToWX.Req.WXSceneSession;
    private Dialog dialog;
    private View inflate;
    private LinearLayout weixinShreLayout;
    private LinearLayout pengyouquanLayout;
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
        api = WXAPIFactory.createWXAPI(this, Constants.APP_ID);

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
                            int promostatus = bean.getInt("status");
                            switch (promostatus) {
                                case 1:
                                    hasperson.setUserState("已邀请");
                                    break;
                                case 2:
                                    hasperson.setUserState("已注册App");
                                    break;
                                case 3:
                                    hasperson.setUserState("已注册车辆信息");
                                    break;
                            }
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
        PromotionViewBinder provider = new PromotionViewBinder(this);
        provider.setListener(this);
        multiTypeAdapter.register(Promotion.class, provider);
        multiTypeAdapter.register(PromotionHasperson.class, new PromotionHaspersonViewBinder());
        multiTypeAdapter.register(PromotionNoperson.class, new PromotionNopersonViewBinder());
        mRlv.setAdapter(multiTypeAdapter);
        assertHasTheSameAdapter(mRlv, multiTypeAdapter);

        personList = new ArrayList<>();

        dialog = new Dialog(this, R.style.ActionSheetDialogStyle);
        inflate = LayoutInflater.from(this).inflate(R.layout.dialog_share, null);
        weixinShreLayout = ((LinearLayout) inflate.findViewById(R.id.weixin_share_layout));
        pengyouquanLayout = ((LinearLayout) inflate.findViewById(R.id.pengyouquan_share_layout));
        dialog.setContentView(inflate);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.y = 50;
        dialogWindow.setAttributes(lp);

        //分享到微信
        RxViewAction.clickNoDouble(weixinShreLayout)
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
    }

    private void shareToWexin() {
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = url;
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = PromotionActivity.this.getString(R.string.wx_share_title);
        msg.description = PromotionActivity.this.getString(R.string.wx_share_description);
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
        Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE, THUMB_SIZE, true);
        bmp.recycle();
        msg.thumbData = Util.bmpToByteArray(thumbBmp, true);
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("webpage");
        req.message = msg;
        req.scene = mTargetScene;
        api.sendReq(req);
    }

    @Override
    public void onShareViewClikcListenner() {
        dialog.show();
    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

}
