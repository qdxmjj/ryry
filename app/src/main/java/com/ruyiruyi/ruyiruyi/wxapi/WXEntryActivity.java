package com.ruyiruyi.ruyiruyi.wxapi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.ruyiruyi.ruyiruyi.MainActivity;
import com.ruyiruyi.ruyiruyi.R;
import com.ruyiruyi.ruyiruyi.ui.activity.GetFromWXActivity;
import com.ruyiruyi.ruyiruyi.ui.activity.PaymentActivity;
import com.ruyiruyi.ruyiruyi.ui.activity.SendToWXActivity;
import com.ruyiruyi.ruyiruyi.ui.activity.ShowFromWXActivity;
import com.ruyiruyi.ruyiruyi.utils.Constants;
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
        RxViewAction.clickNoDouble(shareText)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        startActivity(new Intent(WXEntryActivity.this, SendToWXActivity.class));
                        finish();
                    }
                });
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

        Toast.makeText(this, "baseresp.getType = " + resp.getType(), Toast.LENGTH_SHORT).show();

        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK://正确返回
                result = R.string.errcode_success;
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
                result = R.string.errcode_unknown;
                break;
        }

        Toast.makeText(this, result, Toast.LENGTH_LONG).show();
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }
}
