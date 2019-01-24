package com.ruyiruyi.ruyiruyi.ui.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ruyiruyi.ruyiruyi.R;
import com.ruyiruyi.ruyiruyi.db.DbConfig;
import com.ruyiruyi.ruyiruyi.ui.activity.base.RyBaseActivity;
import com.ruyiruyi.ruyiruyi.utils.CircleImageView;
import com.ruyiruyi.ruyiruyi.utils.RequestUtils;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;
import com.ruyiruyi.rylibrary.cell.ActionBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.x;

import rx.functions.Action1;

public class PointsChangeOrderConfirmActivity extends RyBaseActivity {

    public static final int ADD_ADDRESS_REQUEST_CODE = 169;
    public static final int CHOOSE_ADDRESS_REQUEST_CODE = 170;
    private boolean hasAddress;

    private String mGoodsPic;
    private int mPoints;
    private String mTitle;
    private int mTotalPoints;
    private int mGoodsId;
    private ActionBar actionBar;
    private LinearLayout ll_no_address;
    private FrameLayout ll_has_address;
    private int addressId;
    private String name = "";
    private String phone = "";
    private String sheng = "";
    private String shi = "";
    private String xian = "";
    private String addressTxt = "";
    private boolean isDefault = false;

    private TextView tv_name;
    private TextView tv_phone;
    private TextView tv_address;

    private TextView tv_haspoints;
    private TextView tv_buy;
    private TextView tv_title;
    private CircleImageView iv_goods;
    private TextView tv_points;
    private String TAG = PointsChangeOrderConfirmActivity.class.getSimpleName();
    private ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_points_change_order_confirm);
        actionBar = (ActionBar) findViewById(R.id.acbar);
        actionBar.setTitle("订单确认");
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

        Intent intent = getIntent();
        mGoodsPic = intent.getStringExtra("goodsPic");
        mTitle = intent.getStringExtra("goodsTitle");
        mPoints = intent.getIntExtra("goodsPoints", 0);
        mTotalPoints = intent.getIntExtra("totalPoints", 0);
        mGoodsId = intent.getIntExtra("goodsId", 0);

        initView();
        initData();
    }

    private void initData() {
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL_JIFEN + "score/address");
        params.addBodyParameter("userId", new DbConfig(getApplicationContext()).getId() + "");
        Log.e(TAG, "initData:  params.toString() = " + params.toString());
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    Log.e(TAG, "onSuccess: result = " + result);
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray data = jsonObject.getJSONArray("data");
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject bean = (JSONObject) data.get(i);
                        addressId = bean.getInt("id");
                        name = bean.getString("name");
                        phone = bean.getString("phone");
                        sheng = bean.getString("province");
                        shi = bean.getString("city");
                        xian = bean.getString("county");
                        addressTxt = bean.getString("address").replace(sheng + shi + xian, "");
                        if (Integer.parseInt(bean.getString("isDefault")) == 1) {
                            bindView();
                            return;
                        }
                    }

                    //清空(标志)数据
                    name = "";
                    bindView();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                bindView();
                Toast.makeText(PointsChangeOrderConfirmActivity.this, "网络异常，请检查网络", Toast.LENGTH_SHORT).show();
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == CHOOSE_ADDRESS_REQUEST_CODE) {
                addressId = data.getIntExtra("id", 0);
                name = data.getStringExtra("name");
                phone = data.getStringExtra("phone");
                sheng = data.getStringExtra("sheng");
                shi = data.getStringExtra("shi");
                xian = data.getStringExtra("xian");
                addressTxt = data.getStringExtra("addressTxt");
                isDefault = data.getBooleanExtra("isDefault", true);
                ll_no_address.setVisibility(View.GONE);
                ll_has_address.setVisibility(View.VISIBLE);

                tv_name.setText(name);
                tv_phone.setText(phone);
                tv_address.setText(sheng + " " + shi + " " + xian + " " + addressTxt);
            }
        }
    }

    private void bindView() {
        //设置数据
        Glide.with(PointsChangeOrderConfirmActivity.this).load(mGoodsPic).into(iv_goods);
        tv_points.setText(mPoints + "");
        tv_title.setText(mTitle);
        tv_haspoints.setText(mTotalPoints + "");
        //设置顶部地址
        if (name.length() > 0) {
            //有地址
            ll_has_address.setVisibility(View.VISIBLE);
            ll_no_address.setVisibility(View.GONE);
            tv_name.setText(name);
            tv_phone.setText(phone);
            tv_address.setText(sheng + " " + shi + " " + xian + " " + addressTxt);
        } else {
            //无地址
            ll_has_address.setVisibility(View.GONE);
            ll_no_address.setVisibility(View.VISIBLE);
        }


        //添加收货地址
        RxViewAction.clickNoDouble(ll_no_address).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                /*Intent intent = new Intent(PointsChangeOrderConfirmActivity.this, AddAddressActivity.class);
                intent.putExtra("whereIn", "pointsOrder");
                startActivityForResult(intent, ADD_ADDRESS_REQUEST_CODE);*/
                Intent intent = new Intent(PointsChangeOrderConfirmActivity.this, MyAddressActivity.class);
                intent.putExtra("canChoose", true);
                startActivityForResult(intent, CHOOSE_ADDRESS_REQUEST_CODE);

            }
        });
        //选择收货地址
        RxViewAction.clickNoDouble(ll_has_address).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Intent intent = new Intent(PointsChangeOrderConfirmActivity.this, MyAddressActivity.class);
                intent.putExtra("canChoose", true);
                intent.putExtra("addressId", addressId);
                startActivityForResult(intent, CHOOSE_ADDRESS_REQUEST_CODE);
            }
        });

        //购买
        RxViewAction.clickNoDouble(tv_buy).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (mTotalPoints < mPoints) {
                    Toast.makeText(PointsChangeOrderConfirmActivity.this, "您的可用积分不足", Toast.LENGTH_SHORT).show();
                    return;
                }
                showBuyConfirmDialog();
            }
        });
    }

    private void initView() {
        ll_no_address = (LinearLayout) findViewById(R.id.ll_no_address);
        ll_has_address = (FrameLayout) findViewById(R.id.ll_has_address);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_phone = (TextView) findViewById(R.id.tv_phone);
        tv_address = (TextView) findViewById(R.id.tv_address);
        tv_haspoints = (TextView) findViewById(R.id.tv_haspoints);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_buy = (TextView) findViewById(R.id.tv_buy);
        iv_goods = (CircleImageView) findViewById(R.id.iv_goods);
        tv_points = (TextView) findViewById(R.id.tv_points);
        mDialog = new ProgressDialog(PointsChangeOrderConfirmActivity.this);
    }

    private void showBuyConfirmDialog() {
        AlertDialog dialog = new AlertDialog.Builder(PointsChangeOrderConfirmActivity.this).create();
        View dialogView = LayoutInflater.from(PointsChangeOrderConfirmActivity.this).inflate(R.layout.dialog_error, null);
        TextView error_text = (TextView) dialogView.findViewById(R.id.error_text);
        error_text.setText("确认使用" + mPoints + "积分兑换该商品吗?");
        dialog.setTitle("如意如驿");
        dialog.setIcon(R.mipmap.ic_logo);
        dialog.setView(dialogView);
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "确 定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                showDialogProgress(mDialog, "订单提交中...");
                //下单
                RequestParams params = new RequestParams(RequestUtils.REQUEST_URL_JIFEN + "score/order");
                params.addBodyParameter("userId", new DbConfig(PointsChangeOrderConfirmActivity.this).getId() + "");
                params.addBodyParameter("orderType", "1");//订单类型[1:兑换普通商品订单 2:兑换优惠券订单]
                params.addBodyParameter("skuId", mGoodsId + "");
                params.addBodyParameter("skuImg", mGoodsPic);
                params.addBodyParameter("score", mPoints + "");
                params.addBodyParameter("addressId", addressId + "");
                params.addBodyParameter("token", new DbConfig(PointsChangeOrderConfirmActivity.this).getToken());
                Log.e(TAG, "onClick: params.toString() = " + params.toString());
                x.http().request(HttpMethod.POST, params, new Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        Log.e(TAG, "onSuccess: result = " + result);
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            int status = jsonObject.getInt("status");
                            String msg = jsonObject.getString("msg");
                            if (status == 1) {
                                //跳转积分首页
                                PointsChangeOrderConfirmActivity.this.startActivity(new Intent(PointsChangeOrderConfirmActivity.this, IntegralShopActivity.class));
                                finish();
                            } else if (status == -999) {
                                showUserTokenDialog("您的账号在其它设备登录,请重新登录");
                            } else {
                                Toast.makeText(PointsChangeOrderConfirmActivity.this, msg, Toast.LENGTH_SHORT).show();
                            }

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
                        hideDialogProgress(mDialog);
                    }
                });


            }
        });
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取 消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dialog.show();
        //设置按钮颜色
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.theme_primary));
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.theme_primary));

    }
}
