package com.ruyiruyi.merchant.wxapi;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.ruyiruyi.merchant.MyApplication;
import com.ruyiruyi.merchant.R;
import com.ruyiruyi.merchant.eventbus.WxLoginEvent;
import com.ruyiruyi.merchant.utils.Constants;
import com.ruyiruyi.merchant.utils.OkHttpUtils;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

    private IWXAPI api;
    private String TAG = WXEntryActivity.class.getSimpleName();
    private static final int RETURN_MSG_TYPE_LOGIN = 1; //登录
    private static final int RETURN_MSG_TYPE_SHARE = 2; //分享
    private String openid;
    private String unionid;

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
        String result = "";
        int type = baseResp.getType(); //类型：分享还是登录
        switch (baseResp.errCode) {
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                //用户拒绝授权
                result = "拒绝授权微信登录";
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                //用户取消
                if (type == RETURN_MSG_TYPE_LOGIN) {
                    result = "取消了微信登录";
                } else if (type == RETURN_MSG_TYPE_SHARE) {
                    result = "取消了微信分享";
                }
                break;
            case BaseResp.ErrCode.ERR_OK:
                //用户同意
                if (type == RETURN_MSG_TYPE_LOGIN) {
                    //用户换取access_token的code，仅在ErrCode为0时有效
                    String code = ((SendAuth.Resp) baseResp).code;
                    Log.i(TAG, "code:------>" + code);

                    //这里拿到了这个code，去做2次网络请求获取access_token和用户个人信息
                    Log.e(TAG, "code:------>" + code);
                    getAccessToken(code);


                } else if (type == RETURN_MSG_TYPE_SHARE) {
                    result = "微信分享成功";
                }
                break;
            case BaseResp.ErrCode.ERR_UNSUPPORT:
                result = "不支持错误";//不支持错误
                break;
            default:
                result = "发送返回";//发送返回
                break;
        }
        Log.e(TAG, "onResp: result = " + result);
        if (result != null && result.length() != 0) {
            Toast.makeText(this, result, Toast.LENGTH_LONG).show();
        }
        finish();
    }

    private void getAccessToken(String code) {
        //获取授权
        StringBuffer loginUrl = new StringBuffer();
        loginUrl.append("https://api.weixin.qq.com/sns/oauth2/access_token")
                .append("?appid=")
                .append(MyApplication.WEIXIN_APP_ID)
                .append("&secret=")
                .append(MyApplication.WEIXIN_APP_SECRET)
                .append("&code=")
                .append(code)
                .append("&grant_type=authorization_code");
        OkHttpUtils.ResultCallback<String> resultCallback = new OkHttpUtils.ResultCallback<String>() {
            @Override
            public void onSuccess(final String response) {
                String access = null;
                String openId = null;
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    access = jsonObject.getString("access_token");
                    openId = jsonObject.getString("openid");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //获取个人信息
                String getUserInfo = "https://api.weixin.qq.com/sns/userinfo?access_token=" + access + "&openid=" + openId;
                OkHttpUtils.ResultCallback<String> reCallback = new OkHttpUtils.ResultCallback<String>() {
                    @Override
                    public void onSuccess(String responses) {

                        String nickName = null;
                        String sex = null;
                        String city = null;
                        String province = null;
                        String country = null;
                        String headimgurl = null;
                        try {
                            JSONObject jsonObject = new JSONObject(responses);
                            Log.e(TAG, "onSuccess: response = " + response);

                            openid = jsonObject.getString("openid");
                            nickName = jsonObject.getString("nickname");
                            sex = jsonObject.getString("sex");
                            city = jsonObject.getString("city");
                            province = jsonObject.getString("province");
                            country = jsonObject.getString("country");
                            headimgurl = jsonObject.getString("headimgurl");
                            unionid = jsonObject.getString("unionid");

                            /*Toast.makeText(WXEntryActivity.this, "登录成功", Toast.LENGTH_SHORT).show();*/

                            //发送事件通知登录成功
                            WxLoginEvent wxLoginEvent = new WxLoginEvent();
                            wxLoginEvent.setLoginSuccess((openid != null && nickName != null) ? true : false);
                            wxLoginEvent.setNickname(nickName);
                            wxLoginEvent.setOpenid(openid);
                            wxLoginEvent.setHeadimgurl(headimgurl);
                            EventBus.getDefault().post(wxLoginEvent);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onFailure(Exception e) {
                        Toast.makeText(WXEntryActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                };
                OkHttpUtils.get(getUserInfo, reCallback);
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(WXEntryActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                finish();
            }
        };
        OkHttpUtils.get(loginUrl.toString(), resultCallback);
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
