package com.ruyiruyi.merchant.wxapi;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.ruyiruyi.merchant.R;
import com.ruyiruyi.merchant.utils.Constants;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.xutils.common.util.LogUtil;

import java.io.ByteArrayOutputStream;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

    private IWXAPI api;
    private String TAG = WXEntryActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wxentry_activity);

        // 通过WXAPIFactory工厂，获取IWXAPI的实例
        api = WXAPIFactory.createWXAPI(this, Constants.APP_ID, false);
        api.handleIntent(getIntent(), this);

    }


    /*
    * 微信发送请求到第三方应用时，会回调到该方法(baseresp.getType 1:第三方授权， 2:分享  )
    * */
    @Override
    public void onReq(BaseReq baseReq) {
        Log.e(TAG, "onReq: baseReq.getType() = " + baseReq.getType());
    }

    /*
    * 第三方应用发送到微信的请求处理后的响应结果，会回调到该方法(baseresp.getType 1:第三方授权， 2:分享  )
    * */
    @Override
    public void onResp(BaseResp baseResp) {
/*        int result = 0;
        switch (baseResp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                result = R.string.errcode_success;//发送成功
                String code = ((SendAuth.Resp) baseResp).code;
                LogUtil.i(code);
                getAccessToken(code);
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL://发送取消
                result = R.string.errcode_cancel;
                finish();
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED://发送被拒绝
                result = R.string.errcode_deny;
                break;
            case BaseResp.ErrCode.ERR_UNSUPPORT:
                result = R.string.errcode_unsupported;//不支持错误
                break;
            default:
                result = R.string.errcode_unknown;//发送返回
                break;
        }
        Toast.makeText(this, result, Toast.LENGTH_LONG).show();*/
    }


    /*
    * 微信分享 (shareType  1:发送到聊天界面  2:发送到朋友圈  3:添加到微信收藏)
    * */
    private void shareWebPage(String shareurl, String sharetitle, String sharecription, int iconR, int shareType) {
        //初始化一个WXWebpageObject对象，填写url
        WXWebpageObject wxWebpageObject = new WXWebpageObject();
        wxWebpageObject.webpageUrl = shareurl;

        //用WXWebpageObject对象初始化一个WXMediaMessage对象，填写标题和描述
        WXMediaMessage wxMediaMessage = new WXMediaMessage(wxWebpageObject);
        wxMediaMessage.title = sharetitle;
        wxMediaMessage.description = sharecription;
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), iconR);//图标
        wxMediaMessage.thumbData = bmpToByteArray(bitmap, true);

        //构造一个Req
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        if (shareType == 1) {//发送到聊天界面——WXSceneSession
            //transaction字段用于唯一标示的一个请求
            req.transaction = buildTransaction("webpagechant");
            req.message = wxMediaMessage;
            req.scene = SendMessageToWX.Req.WXSceneSession;
        }
        if (shareType == 2) {//发送到朋友圈——WXSceneTimeline
            req.transaction = buildTransaction("webpagefriend");
            req.message = wxMediaMessage;
            req.scene = SendMessageToWX.Req.WXSceneTimeline;
        }
        if (shareType == 3) {//添加到微信收藏——WXSceneFavorite
            req.transaction = buildTransaction("webpagemy");
            req.message = wxMediaMessage;
            req.scene = SendMessageToWX.Req.WXSceneFavorite;
        }

        //调用api接口发送数据到微信
        api.sendReq(req);

    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, output);
        if (needRecycle) {
            bmp.recycle();
        }
        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


}
