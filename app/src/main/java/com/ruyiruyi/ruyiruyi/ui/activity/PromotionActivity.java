package com.ruyiruyi.ruyiruyi.ui.activity;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ruyiruyi.ruyiruyi.R;
import com.ruyiruyi.ruyiruyi.db.DbConfig;
import com.ruyiruyi.ruyiruyi.ui.multiType.Promotion;
import com.ruyiruyi.ruyiruyi.ui.multiType.PromotionHasperson;
import com.ruyiruyi.ruyiruyi.ui.multiType.PromotionHaspersonViewBinder;
import com.ruyiruyi.ruyiruyi.ui.multiType.PromotionNoperson;
import com.ruyiruyi.ruyiruyi.ui.multiType.PromotionNopersonViewBinder;
import com.ruyiruyi.ruyiruyi.ui.multiType.PromotionViewBinder;
import com.ruyiruyi.ruyiruyi.utils.Constants;
import com.ruyiruyi.ruyiruyi.utils.Util;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;
import com.ruyiruyi.rylibrary.base.BaseActivity;
import com.ruyiruyi.rylibrary.cell.ActionBar;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

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
                        Toast.makeText(PromotionActivity.this, "二维码", Toast.LENGTH_SHORT).show();
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
        //缺接口暂用假数据
        getFakeData();


        //下载完成
        items.clear();
        items.add(new Promotion("SADDD", "下载的推广简介", "下载的邀请方式简介"));
        if (personList == null || personList.size() == 0) {
            items.add(new PromotionNoperson());
        } else {
            items.addAll(personList);
        }
        assertAllRegistered(multiTypeAdapter, items);
        multiTypeAdapter.notifyDataSetChanged();

    }

    private void getFakeData() {
        for (int i = 0; i < 6; i++) {
            PromotionHasperson bean = new PromotionHasperson();
            bean.setUserPhone("123****8905");
            bean.setUserState("已邀请");
            bean.setUserTime("2018-05-21");
            if (i == 0) {
                bean.setFirst(true);
            } else {
                bean.setFirst(false);
            }
            personList.add(bean);
        }
    }

    private void initView() {
        mRlv = findViewById(R.id.my_rlv);
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRlv.setLayoutManager(manager);
        multiTypeAdapter = new MultiTypeAdapter(items);
        PromotionViewBinder promotionViewBinder = new PromotionViewBinder(this);
        promotionViewBinder.setListener(this);
        multiTypeAdapter.register(Promotion.class, promotionViewBinder);
        multiTypeAdapter.register(PromotionHasperson.class, new PromotionHaspersonViewBinder());
        multiTypeAdapter.register(PromotionNoperson.class, new PromotionNopersonViewBinder());
        mRlv.setAdapter(multiTypeAdapter);
        assertHasTheSameAdapter(mRlv, multiTypeAdapter);

        personList = new ArrayList<>();

        dialog = new Dialog(this, R.style.ActionSheetDialogStyle);
        inflate = LayoutInflater.from(this).inflate(R.layout.dialog_share,null);
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
        int id = new DbConfig().getId();
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = "http://192.168.0.190:8060/preferentialInfo/share?userId=" + id ;
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = "百度一下";
        msg.description = "百度一下你就知道";
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
