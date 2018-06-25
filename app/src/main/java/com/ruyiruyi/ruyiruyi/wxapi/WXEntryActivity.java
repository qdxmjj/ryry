package com.ruyiruyi.ruyiruyi.wxapi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.ruyiruyi.ruyiruyi.MainActivity;
import com.ruyiruyi.ruyiruyi.MyApplication;
import com.ruyiruyi.ruyiruyi.R;
import com.ruyiruyi.ruyiruyi.db.DbConfig;
import com.ruyiruyi.ruyiruyi.db.model.User;
import com.ruyiruyi.ruyiruyi.ui.activity.GetFromWXActivity;
import com.ruyiruyi.ruyiruyi.ui.activity.LoginActivity;
import com.ruyiruyi.ruyiruyi.ui.activity.PaymentActivity;
import com.ruyiruyi.ruyiruyi.ui.activity.PromotionActivity;
import com.ruyiruyi.ruyiruyi.ui.activity.SendToWXActivity;
import com.ruyiruyi.ruyiruyi.ui.activity.ShowFromWXActivity;
import com.ruyiruyi.ruyiruyi.ui.activity.WxPhoneActivity;
import com.ruyiruyi.ruyiruyi.ui.model.WXAccessTokenEntity;
import com.ruyiruyi.ruyiruyi.ui.model.WXBaseRespEntity;
import com.ruyiruyi.ruyiruyi.ui.model.WXUserInfo;
import com.ruyiruyi.ruyiruyi.utils.Constants;
import com.ruyiruyi.ruyiruyi.utils.RequestUtils;
import com.ruyiruyi.ruyiruyi.utils.UtilsRY;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.ShowMessageFromWX;
import com.tencent.mm.opensdk.modelmsg.WXAppExtendObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.DbManager;
import org.xutils.common.Callback;
import org.xutils.ex.DbException;
import org.xutils.http.RequestParams;
import org.xutils.x;

import rx.functions.Action1;

public class WXEntryActivity extends AppCompatActivity implements IWXAPIEventHandler{

    private IWXAPI api;
    private TextView shareText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wxentry);
        shareText = (TextView) findViewById(R.id.share_text);


        api = WXAPIFactory.createWXAPI(this, Constants.APP_ID, false);
        api.registerApp(Constants.APP_ID);

     //   startActivity(new Intent(WXEntryActivity.this, SendToWXActivity.class));
       // finish();
        /*RxViewAction.clickNoDouble(shareText)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        startActivity(new Intent(WXEntryActivity.this, SendToWXActivity.class));
                        finish();
                    }
                });*/
        try {
            api.handleIntent(getIntent(), this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        setIntent(intent);
        api.handleIntent(intent, this);
    }

    /**
     * 微信发送的请求将回调到onReq方法
     */
    @Override
    public void onReq(BaseReq req) {
        Toast.makeText(WXEntryActivity.this, "000", Toast.LENGTH_SHORT).show();
        switch (req.getType()) {
            case ConstantsAPI.COMMAND_GETMESSAGE_FROM_WX:
                Toast.makeText(WXEntryActivity.this, "222", Toast.LENGTH_SHORT).show();
               goToGetMsg();
                break;
            case ConstantsAPI.COMMAND_SHOWMESSAGE_FROM_WX:
            //    goToShowMsg((ShowMessageFromWX.Req) req);

                Toast.makeText(WXEntryActivity.this, "111", Toast.LENGTH_SHORT).show();
                goToShowMsg((ShowMessageFromWX.Req) req);
                break;
            default:
                break;
        }
    }
    private void goToShowMsg(ShowMessageFromWX.Req showReq) {
        WXMediaMessage wxMsg = showReq.message;
        WXAppExtendObject obj = (WXAppExtendObject) wxMsg.mediaObject;

        StringBuffer msg = new StringBuffer(); // ��֯һ������ʾ����Ϣ����
        msg.append("description: ");
        msg.append(wxMsg.description);
        msg.append("\n");
        msg.append("extInfo: ");
        msg.append(obj.extInfo);
        msg.append("\n");
        msg.append("filePath: ");
        msg.append(obj.filePath);

        Intent intent = new Intent(this, ShowFromWXActivity.class);
        intent.putExtra(Constants.ShowMsgActivity.STitle, wxMsg.title);
        intent.putExtra(Constants.ShowMsgActivity.SMessage, msg.toString());
        intent.putExtra(Constants.ShowMsgActivity.BAThumbData, wxMsg.thumbData);
        startActivity(intent);
        finish();
    }

    private void goToGetMsg() {
        Intent intent = new Intent(this, GetFromWXActivity.class);
        intent.putExtras(getIntent());
        startActivity(intent);
        finish();
    }

    /**
     * 发送到微信请求的响应结果将回调到onResp方法
     */
    @Override
    public void onResp(BaseResp resp) {
        int result = 0;
        WXBaseRespEntity entity = JSON.parseObject(JSON.toJSONString(resp),WXBaseRespEntity.class);

  //      Toast.makeText(this, "baseresp.getType = " + resp.getType(), Toast.LENGTH_SHORT).show();

        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK://正确返回
                result = R.string.errcode_success;
                if (resp.getType() == ConstantsAPI.COMMAND_SENDAUTH) {//登录成功的回调
                    OkHttpUtils.get().url("https://api.weixin.qq.com/sns/oauth2/access_token")
                            .addParams("appid",Constants.APP_ID)
                            .addParams("secret",Constants.APP_SECRET_WX)
                            .addParams("code",entity.getCode())
                            .addParams("grant_type","authorization_code")
                            .build()
                            .execute(new StringCallback() {
                                @Override
                                public void onError(okhttp3.Call call, Exception e, int id) {
                                    Toast.makeText(WXEntryActivity.this, "请求错误", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onResponse(String response, int id) {
                                    // ViseLog.d("response:"+response);
                                    WXAccessTokenEntity accessTokenEntity = JSON.parseObject(response,WXAccessTokenEntity.class);
                                    if(accessTokenEntity!=null){
                                        getUserInfo(accessTokenEntity);
                                    }else {
                                        Toast.makeText(WXEntryActivity.this, "获取失败", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }else if (resp.getType() == ConstantsAPI.COMMAND_SENDMESSAGE_TO_WX){//分享成功的回调
                }
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL://用户取消
                result = R.string.errcode_cancel;
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED://认证被否决
                result = R.string.errcode_deny;
                break;
            case BaseResp.ErrCode.ERR_UNSUPPORT://不支持错误
                result = R.string.errcode_unsupported;
                break;
            case BaseResp.ErrCode.ERR_SENT_FAILED://发送失败
                result = R.string.errcode_fail;;
                break;
            case BaseResp.ErrCode.ERR_COMM: //一般错误
                result = R.string.errcode_usuall;;
                break;
            default:
                result = R.string.errcode_fail;
                break;
        }

        Toast.makeText(this, result, Toast.LENGTH_LONG).show();
        finish();
    }


    /**
     * 获取个人信息
     * @param accessTokenEntity
     */
    private void getUserInfo(WXAccessTokenEntity accessTokenEntity) {
        //https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID
        OkHttpUtils.get()
                .url("https://api.weixin.qq.com/sns/userinfo")
                .addParams("access_token",accessTokenEntity.getAccess_token())
                .addParams("openid",accessTokenEntity.getOpenid())//openid:授权用户唯一标识
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(okhttp3.Call call, Exception e, int id) {
                        //ViseLog.d("获取错误..");
                        Toast.makeText(WXEntryActivity.this, "获取失败", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                     //   ViseLog.d("userInfo:"+response);
                      //  Toast.makeText(WXEntryActivity.this, response, Toast.LENGTH_SHORT).show();
                        WXUserInfo wxResponse = JSON.parseObject(response,WXUserInfo.class);
                       // ViseLog.d("微信登录资料已获取，后续未完成");
                        String headUrl = wxResponse.getHeadimgurl();
                        String nickname = wxResponse.getNickname();
                        String openid = wxResponse.getOpenid();
                     /*   User user = new User();
                        user.setNick(nickname);
                        user.setHeadimgurl(headUrl);
                        user.setGender(sex);
                        DbConfig dbConfig = new DbConfig();
                        DbManager db = dbConfig.getDbManager();
                        try {
                            db.saveOrUpdate(user);
                        } catch (DbException e) {

                        }*/
                        //  ViseLog.d("头像Url:"+headUrl);
                      //  MyApplication.getShared().putString("headUrl",headUrl);
                        wxLoigin(headUrl,nickname,openid);

                    /*    Intent intent = getIntent();
                        intent.putExtra(LoginActivity.HEADURL,headUrl);
                        intent.putExtra(LoginActivity.NICKNAME,nickname);
                        intent.putExtra(LoginActivity.OPENID,openid);
                        WXEntryActivity.this.setResult(0,intent);*/
                       /* Intent intent = new Intent(getApplicationContext(), WxPhoneActivity.class);
                        intent.putExtra(LoginActivity.HEADURL,headUrl);
                        intent.putExtra(LoginActivity.NICKNAME,nickname);
                        intent.putExtra(LoginActivity.OPENID,openid);
                        startActivity(intent);*/
                        finish();
                    }
                });
    }

    private void wxLoigin(final String headUrl, final String nickName, final String openId) {
        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("wxInfoId",openId);

        } catch (JSONException e) {
        }
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "checkWXInfoId");
        params.addBodyParameter("reqJson",jsonObject.toString());
        params.setConnectTimeout(10000);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String status = jsonObject.getString("status");
                    String msg = jsonObject.getString("msg");
                    if(status.equals("1")){     //微信已注册  返回用户信息
                        JSONObject data = jsonObject.getJSONObject("data");
                        saveUserToDb(data);
                    //    Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                    }else if (status.equals("2")){    //微信未注册   调到填写信息界面
                        Toast.makeText(WXEntryActivity.this, "chenggong", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), WxPhoneActivity.class);
                        intent.putExtra(LoginActivity.HEADURL,headUrl);
                        intent.putExtra(LoginActivity.NICKNAME,nickName);
                        intent.putExtra(LoginActivity.OPENID,openId);
                        startActivity(intent);
                    }else {
                        Toast.makeText(WXEntryActivity.this, msg, Toast.LENGTH_SHORT).show();
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
     * 将用户保存到数据库
     * @param data
     */
    private void saveUserToDb(JSONObject data) {
        User user = new User();
        try {
            user.setId(data.getInt("id"));
            user.setNick(data.getString("nick"));
            // user.setPassword(data.getString("password"));
            user.setPhone(data.getString("phone"));
            user.setAge(data.getString("age"));
            long birthday = data.getLong("birthday");
            String birthdayStr = new UtilsRY().getTimestampToString(birthday);
            user.setBirthday(birthdayStr);
            user.setEmail(data.getString("email"));
            user.setGender(data.getInt("gender"));
            user.setHeadimgurl(data.getString("headimgurl"));
            user.setToken(data.getString("token"));
            user.setStatus(data.getString("status"));
            user.setFirstAddCar(data.getInt("firstAddCar"));
            user.setIsLogin("1");
            DbConfig dbConfig = new DbConfig(this);
            DbManager db = dbConfig.getDbManager();
            db.saveOrUpdate(user);
        } catch (JSONException e) {
        } catch (DbException e) {

        }
    }

}
