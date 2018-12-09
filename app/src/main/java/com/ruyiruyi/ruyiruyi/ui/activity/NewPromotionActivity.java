package com.ruyiruyi.ruyiruyi.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.ruyiruyi.ruyiruyi.R;
import com.ruyiruyi.ruyiruyi.ui.activity.base.RyBaseActivity;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;
import com.ruyiruyi.rylibrary.cell.ActionBar;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import rx.functions.Action1;

public class NewPromotionActivity extends RyBaseActivity {
    private ActionBar actionBar;
    private FrameLayout fl_invite_register;
    private FrameLayout fl_invite_buyshoe;
    private FrameLayout fl_invite_person;
    private FrameLayout fl_invite_award;

    private String INVITE_REGISTER_URL = "";
    private String INVITE_BUYSHOE_URL = "";
    private boolean INVITE_REGISTER_CANSHARE = false;
    private boolean INVITE_BUYSHOE_CANSHARE = false;
    private String SHARE_URL_REGISTER = "";
    private String SHARE_DESCRIPTION_REGISTER = "";
    private String SHARE_URL_BUYSHOE = "";
    private String SHARE_DESCRIPTION_BUYSHOE = "";
    private String TAG = NewPromotionActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_promotion);
        actionBar = (ActionBar) findViewById(R.id.my_acbar);
        actionBar.setTitle("邀请有礼");
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

        initView();
        initData();
        bindView();
    }

    private void initData() {
//        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "invite/Url");
//        RequestParams params = new RequestParams("http://180.76.243.205:10002/xmjj-webservice/invite/Url");
        RequestParams params = new RequestParams("http://192.168.0.94:8888/invite/Url");
        Log.e(TAG, "initData:  params.toString() = " + params.toString());
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    Log.e(TAG, "onSuccess: result = " + result);
                    JSONObject object = new JSONObject(result);
                    JSONObject inviteRegister = object.getJSONObject("inviteRegister");
                    JSONObject inviteBuy = object.getJSONObject("inviteBuy");

                    INVITE_REGISTER_URL = inviteRegister.getString("url");
                    INVITE_BUYSHOE_URL = inviteBuy.getString("url");
                    int shareAble_register = Integer.parseInt(inviteRegister.getString("shareAble"));
                    INVITE_REGISTER_CANSHARE = shareAble_register == 1 ? true : false;
                    int shareAble_buy = Integer.parseInt(inviteBuy.getString("shareAble"));
                    INVITE_BUYSHOE_CANSHARE = shareAble_buy == 1 ? true : false;
                    SHARE_URL_REGISTER = inviteRegister.getString("shareUrl");
                    SHARE_DESCRIPTION_REGISTER = inviteRegister.getString("shareTitle");
                    SHARE_URL_BUYSHOE = inviteBuy.getString("shareUrl");
                    SHARE_DESCRIPTION_BUYSHOE = inviteBuy.getString("shareTitle");

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

    private void bindView() {
        //邀请好友注册如驿如意
        RxViewAction.clickNoDouble(fl_invite_register).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (INVITE_REGISTER_URL.length() == 0) {
                    Toast.makeText(NewPromotionActivity.this, "请检查网络", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(NewPromotionActivity.this, BottomEventActivity.class);
                intent.putExtra("webUrl", INVITE_REGISTER_URL);
                intent.putExtra("canShare", INVITE_REGISTER_CANSHARE);
                intent.putExtra("shareUrl", SHARE_URL_REGISTER);
                intent.putExtra("shareDescription", SHARE_DESCRIPTION_REGISTER);
                startActivity(intent);
            }
        });
        //邀请车友购买轮胎
        RxViewAction.clickNoDouble(fl_invite_buyshoe).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (INVITE_BUYSHOE_URL.length() == 0) {
                    Toast.makeText(NewPromotionActivity.this, "请检查网络", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(NewPromotionActivity.this, BottomEventActivity.class);
                intent.putExtra("webUrl", INVITE_BUYSHOE_URL);
                intent.putExtra("canShare", INVITE_BUYSHOE_CANSHARE);
                intent.putExtra("shareUrl", SHARE_URL_BUYSHOE);
                intent.putExtra("shareDescription", SHARE_DESCRIPTION_BUYSHOE);
                startActivity(intent);
            }
        });
        //我推荐的人
        RxViewAction.clickNoDouble(fl_invite_person).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Intent intent = new Intent(NewPromotionActivity.this, MyInvitePersonActivity.class);
                startActivity(intent);
            }
        });
        //我的奖品
        RxViewAction.clickNoDouble(fl_invite_award).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Intent intent = new Intent(NewPromotionActivity.this, MyInviteAwardActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initView() {
        fl_invite_register = findViewById(R.id.fl_invite_register);
        fl_invite_buyshoe = findViewById(R.id.fl_invite_buyshoe);
        fl_invite_person = findViewById(R.id.fl_invite_person);
        fl_invite_award = findViewById(R.id.fl_invite_award);
    }


    /**
     * 标题栏右侧图标点击事件
     */
    private void onQrCodePressed() {
        Intent intent = new Intent(getApplicationContext(), NewQrCodeActivity.class);
        startActivity(intent);
    }
}
