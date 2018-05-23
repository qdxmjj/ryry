package com.ruyiruyi.ruyiruyi.ui.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.ruyiruyi.ruyiruyi.R;
import com.ruyiruyi.ruyiruyi.utils.Constants;
import com.ruyiruyi.ruyiruyi.utils.MMAlert;
import com.ruyiruyi.ruyiruyi.utils.Util;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXTextObject;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import rx.functions.Action1;

public class SendToWXActivity extends AppCompatActivity {
    private static final int THUMB_SIZE = 150;
    private int mTargetScene = SendMessageToWX.Req.WXSceneSession;
    private IWXAPI api;
    private TextView shareUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_to_wx);
        api = WXAPIFactory.createWXAPI(this, Constants.APP_ID);

        shareUrl = (TextView) findViewById(R.id.share_url);

        RxViewAction.clickNoDouble(shareUrl)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        MMAlert.showAlert(SendToWXActivity.this, getString(R.string.send_webpage),
                                SendToWXActivity.this.getResources().getStringArray(R.array.send_webpage_item),
                                null, new MMAlert.OnAlertSelectId(){

                                    @Override
                                    public void onClick(int whichButton) {
                                        WXWebpageObject webpage = new WXWebpageObject();
                                        webpage.webpageUrl = "www.baidu.com";
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
                                        Toast.makeText(SendToWXActivity.this, api.sendReq(req)+"", Toast.LENGTH_SHORT).show();
                                        finish();
                                     /*   WXTextObject textObject = new WXTextObject();
                                        String text ="123456798";

                                        WXMediaMessage msg = new WXMediaMessage();
                                        msg.mediaObject = textObject;
                                        msg.description = text;

                                        SendMessageToWX.Req req = new SendMessageToWX.Req();
                                        req.transaction = buildTransaction("text");
                                        req.message = msg;
                                        req.scene = SendMessageToWX.Req.WXSceneTimeline;
                                        api.sendReq(req);
                                        finish();*/
                                    }
                                });
                    }
                });

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
        api.sendReq(req);

        finish();*/
    }
    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

}
