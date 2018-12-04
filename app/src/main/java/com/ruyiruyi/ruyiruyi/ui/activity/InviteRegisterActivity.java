package com.ruyiruyi.ruyiruyi.ui.activity;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ruyiruyi.ruyiruyi.R;
import com.ruyiruyi.ruyiruyi.db.DbConfig;
import com.ruyiruyi.ruyiruyi.ui.activity.base.RyBaseActivity;
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

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import rx.functions.Action1;

public class InviteRegisterActivity extends RyBaseActivity {

    private ActionBar mActionBar;
    private TextView tv_num;
    private TextView tv_share;
    private LinearLayout ll_main;

    private int num;
    private String url;
    private String shareDescription;
    private ProgressDialog downDialog;

    private IWXAPI api;
    private static final int THUMB_SIZE = 150;
    private int mTargetScene = SendMessageToWX.Req.WXSceneSession;
    private String TAG = InviteRegisterActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_register);
        mActionBar = (ActionBar) findViewById(R.id.acbars);
        mActionBar.setTitle("邀请有礼");
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

        api = WXAPIFactory.createWXAPI(this, Constants.APP_ID);
        downDialog = new ProgressDialog(InviteRegisterActivity.this);

        initView();
        initData();
        bindView();
    }

    private void initData() {
        showDialogProgress(downDialog, "数据获取中...");
        ll_main.setVisibility(View.GONE);
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "preferentialInfo/getUserCouponNumAndShareUrl");
        JSONObject object = new JSONObject();
        try {
            object.put("userId", new DbConfig(InviteRegisterActivity.this).getId() + "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        params.addBodyParameter("reqJson", object.toString());

        Log.e("InviteRegisterActivity", "initData: params.toString()  = " + params.toString());
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("InviteRegisterActivity", "onSuccess: result.toString() = " + result.toString());
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONObject data = jsonObject.getJSONObject("data");
                    num = Integer.parseInt(data.getString("couponNum"));
                    JSONObject shareVo = data.getJSONObject("shareVo");
                    url = shareVo.getString("url");
                    shareDescription = shareVo.getString("title");
                    tv_num.setText(num + "");

                    hideDialogProgress(downDialog);
                    ll_main.setVisibility(View.VISIBLE);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e(TAG, "onError: " + ex.toString());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
            }
        });

    }

    private void bindView() {
        //分享
        RxViewAction.clickNoDouble(tv_share).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                showBottomShareMenu();
            }
        });
    }

    private void initView() {
        tv_num = findViewById(R.id.tv_num);
        tv_share = findViewById(R.id.tv_share);
        ll_main = findViewById(R.id.ll_main);
    }

    /**
     * 显示底部分享菜单栏
     */
    private void showBottomShareMenu() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(InviteRegisterActivity.this);
        bottomSheetDialog.setCanceledOnTouchOutside(true);
        View contentView = LayoutInflater.from(InviteRegisterActivity.this).inflate(R.layout.bottomsheet_share, null, false);
        LinearLayout ll_share_weixin = contentView.findViewById(R.id.ll_share_weixin);
        RxViewAction.clickNoDouble(ll_share_weixin).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                //微信分享给好友
                mTargetScene = SendMessageToWX.Req.WXSceneSession;
                shareToWexin();
            }
        });
        LinearLayout ll_share_weixin_friend = contentView.findViewById(R.id.ll_share_weixin_friend);
        RxViewAction.clickNoDouble(ll_share_weixin_friend).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                //微信分享至朋友圈
                mTargetScene = SendMessageToWX.Req.WXSceneTimeline;
                shareToWexin();
            }
        });
        bottomSheetDialog.setContentView(contentView);
        //设置底部白色背景为透明
        bottomSheetDialog.getDelegate().findViewById(android.support.design.R.id.design_bottom_sheet).setBackgroundColor(InviteRegisterActivity.this.getResources().getColor(R.color.transparent));
        bottomSheetDialog.show();
    }

    /**
     * 微信分享
     */
    private void shareToWexin() {
        int id = new DbConfig(this).getId();
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = url;
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = "如驿如意";
        msg.description = shareDescription;//分享活动介绍
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
}
