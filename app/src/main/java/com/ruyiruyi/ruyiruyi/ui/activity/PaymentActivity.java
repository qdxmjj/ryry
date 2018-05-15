package com.ruyiruyi.ruyiruyi.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ruyiruyi.ruyiruyi.MainActivity;
import com.ruyiruyi.ruyiruyi.R;
import com.ruyiruyi.ruyiruyi.db.DbConfig;
import com.ruyiruyi.ruyiruyi.utils.RequestUtils;
import com.ruyiruyi.ruyiruyi.utils.XMJJUtils;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;
import com.ruyiruyi.rylibrary.base.BaseActivity;
import com.ruyiruyi.rylibrary.cell.ActionBar;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import rx.functions.Action1;

public class PaymentActivity extends BaseActivity {

    private static final String TAG = PaymentActivity.class.getSimpleName();
    private ActionBar actionBar;
    private TextView payButton;
    private double allprice;
    private TextView allPriceText;
    private String orderno;
    public static String ALL_PRICE = "ALLPRICE";
    public static String ORDERNO = "ORDERNO";
    public static String ORDER_TYPE = "ORDER_TYPE";
    public static String ORDER_FROM = "ORDER_FROM";  //0是来自收银台  1是来自订单
    private int orderType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment,R.id.my_action);
        actionBar = (ActionBar) findViewById(R.id.my_action);
        actionBar.setTitle("收银台");;
        actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick(){
            @Override
            public void onItemClick(int var1) {
                switch ((var1)){
                    case -1:
                        onBackPressed();
                        break;
                }
            }
        });
        Intent intent = getIntent();
        allprice = intent.getDoubleExtra(ALL_PRICE,0.00);
        orderno = intent.getStringExtra(ORDERNO);
        orderType = intent.getIntExtra(ORDER_TYPE,0);
        initView();
        getDataFromService();
    }

    private void getDataFromService() {

    }

    private void initView() {
        payButton = (TextView) findViewById(R.id.payment_button);
        allPriceText = (TextView) findViewById(R.id.price_text);
        allPriceText.setText(allprice+"");


        RxViewAction.clickNoDouble(payButton)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        postOrderSuccess();

                    }
                });
    }

    private void postOrderSuccess() {
        int userId = new DbConfig().getId();
        JSONObject jsonObject = new JSONObject();
        try {
           jsonObject.put("orderNo",orderno);
           jsonObject.put("userId",userId);
        } catch (JSONException e) {
        }
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "addConfirmUserShoeCxwyOrder");

        String token = new DbConfig().getToken();

        String jsonByToken = "";
        try {
                jsonByToken = XMJJUtils.encodeJsonByToken(jsonObject.toString(),token);


        } catch (UnsupportedEncodingException e) {

        } catch (IllegalBlockSizeException e) {

        } catch (InvalidKeyException e) {

        } catch (BadPaddingException e) {

        } catch (NoSuchAlgorithmException e) {

        } catch (NoSuchPaddingException e) {

        }
      //  String s = jsonByToken.replaceAll("/", "%2F");
      //  String s1 = s.replaceAll("=", "%3D");
        String s2 = jsonByToken.replaceAll("\\n", "");
       /* Log.e(TAG, "postOrderSuccess: " + s2);
        JSONObject jsonObject2 = new JSONObject();
        try {
            jsonObject2.put("orderNo11",s2);
        } catch (JSONException e) {

        }*/

        params.addBodyParameter("reqJson",s2);
        params.addParameter("token",token);
        Log.e(TAG, "postOrderSuccess: paramas---" + params.toString());
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess: --*--" + result );
                JSONObject jsonObject1 = null;
                try {
                    jsonObject1 = new JSONObject(result);
                    String status = jsonObject1.getString("status");
                    String msg = jsonObject1.getString("msg");
                    if (status.equals("1")){
                        startActivity(new Intent(getApplicationContext(),TireWaitChangeActivity.class));
                    }else {
                        Toast.makeText(PaymentActivity.this, msg, Toast.LENGTH_SHORT).show();
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

    @Override
    public void onBackPressed() {
        showDialog("您确认要离开支付订单界面，离开订单会变为代付款订单，可在待付款订单中查看");

    }
    private void showDialog(String msg){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_error, null);
        TextView error_text = (TextView) dialogView.findViewById(R.id.error_text);
        error_text.setText(msg);
        dialog.setTitle("如意如驿");
        dialog.setIcon(R.drawable.ic_logo_login);
        dialog.setView(dialogView);
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent intent = new Intent(getApplicationContext(), PendingOrderActivity.class);
                intent.putExtra(ORDERNO,orderno);
                intent.putExtra(ORDER_TYPE,orderType);
                intent.putExtra(ORDER_FROM,0);
                startActivity(intent);
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
}
