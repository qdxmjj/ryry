package com.ruyiruyi.ruyiruyi.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ruyiruyi.ruyiruyi.R;
import com.ruyiruyi.ruyiruyi.db.DbConfig;
import com.ruyiruyi.ruyiruyi.db.model.User;
import com.ruyiruyi.ruyiruyi.ui.activity.base.RyBaseActivity;
import com.ruyiruyi.ruyiruyi.ui.multiType.InfoOne;
import com.ruyiruyi.ruyiruyi.ui.multiType.InfoOneViewBinder;
import com.ruyiruyi.ruyiruyi.ui.multiType.PublicBigPic;
import com.ruyiruyi.ruyiruyi.ui.multiType.PublicBigPicViewBinder;
import com.ruyiruyi.ruyiruyi.ui.multiType.PublicCheckNum;
import com.ruyiruyi.ruyiruyi.ui.multiType.PublicCheckNumViewBinder;
import com.ruyiruyi.ruyiruyi.utils.RequestUtils;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;
import com.ruyiruyi.rylibrary.cell.ActionBar;

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

public class BuyCxwyActivity extends RyBaseActivity implements PublicCheckNumViewBinder.OnPubCheckNumItemClick {

    private static final String TAG = BuyCxwyActivity.class.getSimpleName();
    private ActionBar actionBar;
    private ImageView img_agree;
    private TextView agreement;
    private TextView buy_;
    private RecyclerView mRecyclerView;
    private MultiTypeAdapter multiTypeAdapter;
    private List<Object> items = new ArrayList<>();//Object!!!

    private int buyNum = 1; //默认买一个
    private String carNumber;
    private String cxwyPrice;
    private boolean isAgree = false; //是否勾选  默认false 未勾选

    private String finalCxwyPrice;
    private double allPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_buy_cxwy);
        actionBar = (ActionBar) findViewById(R.id.acbar);
        actionBar.setTitle("畅行无忧");
        actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(int var1) {
                switch ((var1)) {
                    case -1:
                        onBackPressed();
                        break;
                }
            }
        });

        initView();
        initData();

        bindView();

    }

    private void bindView() {
        //选择对勾
        RxViewAction.clickNoDouble(img_agree).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (!isAgree) {
                    isAgree = true;
                    img_agree.setImageResource(R.drawable.ic_yes);
                } else {
                    isAgree = false;
                    img_agree.setImageResource(R.drawable.ic_no);
                }
            }
        });

        RxViewAction.clickNoDouble(agreement).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Intent intent = new Intent(getApplicationContext(), AgreementActivity.class);
                intent.putExtra("AGREEMENTTYPE",3);
                startActivity(intent);
            }
        });
        //确认购买
        RxViewAction.clickNoDouble(buy_).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (!isAgree) {
                    showDialog("请同意《如驿如意畅行无忧使用协议》");
                    return;
                }
                if (buyNum < 1) {
                    showDialog("购买数量至少选择1个");
                    return;
                }

                postCxwyOrder();
            }
        });
    }

    /**
     * 提交畅行无忧订单
     */
    private void postCxwyOrder() {
        User user = new DbConfig(this).getUser();
        int carId = user.getCarId();
        int userId = user.getId();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("shoeId", 0);
            jsonObject.put("userId", userId);
            jsonObject.put("fontRearFlag", 0);
            jsonObject.put("amount", 0);
            jsonObject.put("shoeName", "");
            jsonObject.put("shoeTotalPrice", 0);
            jsonObject.put("shoePrice", 0);
            jsonObject.put("cxwyAmount", buyNum);
            jsonObject.put("cxwyPrice", finalCxwyPrice);
            jsonObject.put("cxwyTotalPrice", allPrice);
            jsonObject.put("totalPrice", allPrice);
            jsonObject.put("orderImg", "");
        } catch (JSONException e) {
        }
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "addUserShoeOrder");
        params.addBodyParameter("reqJson", jsonObject.toString());
        String token = new DbConfig(this).getToken();
        params.addParameter("token", token);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess:------ " + result);
                try {
                    JSONObject jsonObject1 = new JSONObject(result);
                    String status = jsonObject1.getString("status");
                    String msg = jsonObject1.getString("msg");
                    if (status.equals("1")) {
                        JSONObject data = jsonObject1.getJSONObject("data");
                        String orderNo = data.getString("orderNo");

                        Intent intent = new Intent(getApplicationContext(), PaymentActivity.class);
                        intent.putExtra(PaymentActivity.ALL_PRICE, allPrice);
                        intent.putExtra(PaymentActivity.ORDERNO, orderNo);
                        intent.putExtra(PaymentActivity.ORDER_TYPE, 99);
                        startActivity(intent);
                    } else if (status.equals("-999")) {
                        showUserTokenDialog("您的账号在其它设备登录,请重新登录");
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

    private void setView() {
        User user = new DbConfig(this).getUser();
        items.clear();
        items.add(new PublicBigPic());
        items.add(new InfoOne("用户名", user.getNick(), true));
        items.add(new InfoOne("联系电话", user.getPhone(), true));
        items.add(new InfoOne("车牌号", carNumber, true));
        items.add(new InfoOne("畅行无忧价格", finalCxwyPrice, true));
        items.add(new PublicCheckNum("购买数量", 999, buyNum, "1"));

        //更新适配器
        assertAllRegistered(multiTypeAdapter, items);
        multiTypeAdapter.notifyDataSetChanged();
    }

    private void initData() {
        User user = new DbConfig(this).getUser();
        int carId = user.getCarId();
        int userId = user.getId();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userId", userId);
            jsonObject.put("userCarId", carId);
        } catch (JSONException e) {
        }
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "getCarByUserIdAndCarId");
        params.addBodyParameter("reqJson", jsonObject.toString());
        Log.e(TAG, "initData: " + jsonObject.toString());
        String token = new DbConfig(this).getToken();
        params.addParameter("token", token);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess: " + result);
                JSONObject jsonObject1 = null;
                try {
                    jsonObject1 = new JSONObject(result);
                    String status = jsonObject1.getString("status");
                    String msg = jsonObject1.getString("msg");
                    if (status.equals("1")){
                        JSONObject data = jsonObject1.getJSONObject("data");
                        carNumber = data.getString("platNumber");

                        initCXWYData();
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
     * 获取畅行无忧的价格
     */
    private void initCXWYData() {
        User user = new DbConfig(this).getUser();
        int carId = user.getCarId();
        int userId = user.getId();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userId", userId);
            jsonObject.put("shoeId", "");
        } catch (JSONException e) {
        }
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "getShoeDetailByShoeId");
        params.addBodyParameter("reqJson", jsonObject.toString());
        String token = new DbConfig(this).getToken();
        params.addParameter("token", token);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess: ------" +  result);
                JSONObject jsonObject1 = null;
                try {
                    jsonObject1 = new JSONObject(result);
                    String status = jsonObject1.getString("status");
                    String msg = jsonObject1.getString("msg");
                    if (status.equals("1")){
                        JSONObject data = jsonObject1.getJSONObject("data");
                        finalCxwyPrice = data.getString("finalCxwyPrice");
                        setView();
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

    private void initView() {
        mRecyclerView = findViewById(R.id.rlv);
        img_agree = findViewById(R.id.img_agree);
        agreement = findViewById(R.id.agreement);
        buy_ = findViewById(R.id.buy_);

        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(manager);
        multiTypeAdapter = new MultiTypeAdapter(items);

        register();

        mRecyclerView.setAdapter(multiTypeAdapter);
        assertHasTheSameAdapter(mRecyclerView, multiTypeAdapter);

    }

    private void register() {
        multiTypeAdapter.register(InfoOne.class, new InfoOneViewBinder());
        PublicCheckNumViewBinder publicCheckNumViewBinder = new PublicCheckNumViewBinder(getApplicationContext());
        publicCheckNumViewBinder.setListener(this);
        multiTypeAdapter.register(PublicCheckNum.class, publicCheckNumViewBinder);
        multiTypeAdapter.register(PublicBigPic.class, new PublicBigPicViewBinder());
    }

    private void showDialog(String error) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_error, null);
        TextView error_text = (TextView) dialogView.findViewById(R.id.error_text);
        error_text.setText(error);
        dialog.setTitle("如意如驿");
        dialog.setIcon(R.drawable.ic_logo_login);
        dialog.setView(dialogView);
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });
        dialog.show();
    }

    /*
    * PublicCheckNumViewBinder    点击接口回调
    * */
    @Override
    public void onPubCheckNumItemClickListener(int num) {
        buyNum = num;
        double cxwyPrice = Double.parseDouble(finalCxwyPrice);
        allPrice = cxwyPrice * num;

    }
}
