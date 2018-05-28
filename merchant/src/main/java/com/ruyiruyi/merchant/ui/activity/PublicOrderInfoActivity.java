package com.ruyiruyi.merchant.ui.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ruyiruyi.merchant.MainActivity;
import com.ruyiruyi.merchant.R;
import com.ruyiruyi.merchant.bean.BarCode;
import com.ruyiruyi.merchant.db.DbConfig;
import com.ruyiruyi.merchant.ui.multiType.PublicBarCode;
import com.ruyiruyi.merchant.ui.multiType.PublicBarCodeProvider;
import com.ruyiruyi.merchant.ui.multiType.PublicGoodsInfo;
import com.ruyiruyi.merchant.ui.multiType.PublicGoodsInfoProvider;
import com.ruyiruyi.merchant.ui.multiType.PublicOneaddPic;
import com.ruyiruyi.merchant.ui.multiType.PublicOneaddPicProvider;
import com.ruyiruyi.merchant.ui.multiType.PublicShoeFlag;
import com.ruyiruyi.merchant.ui.multiType.PublicShoeFlagProvider;
import com.ruyiruyi.merchant.utils.UtilsURL;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;
import com.ruyiruyi.rylibrary.base.BaseActivity;
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

public class PublicOrderInfoActivity extends BaseActivity implements PublicBarCodeProvider.OnBarCodeSwitchClick {
    private TextView stateButton;
    private ActionBar actionBar;
    private RecyclerView listView;
    private List<Object> items = new ArrayList<>();
    private List<PublicBarCode> barCodeList = new ArrayList<>();
    private List<PublicShoeFlag> shoeFlagList = new ArrayList<>();
    private List<PublicGoodsInfo> goodsInfoList = new ArrayList<>();
    private MultiTypeAdapter adapter;

    private String orderNo;
    private String orderType;//2首次更换订单
    private String orderState;
    private String storeId;

    private String userName;
    private String platNumber;
    private String storeName;
    private String userPhone;

    private String fontShoeImg;
    private String fontShoeName;
    private String fontAmount;
    private String rearShoeImg;
    private String rearShoeName;
    private String rearAmount;
    private String consistentShoeImg;
    private String consistentShoeName;
    private String consistentAmount;

    private String barCode_a_;
    private String barCode_b_;
    private String barCode_c_;
    private String barCode_d_;
    private String barCode_id_a_;
    private String barCode_id_b_;
    private String barCode_id_c_;
    private String barCode_id_d_;

    //（status   1 一致    2 不一致   ）
    private String isNoConsistent_a_;//false 默认为1(网络请求中已设置)未选中 为一致
    private String isNoConsistent_b_;//false
    private String isNoConsistent_c_;//false
    private String isNoConsistent_d_;//false
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_order_info);
        progressDialog = new ProgressDialog(this);
        Bundle bundle = getIntent().getExtras();
        orderNo = bundle.getString("orderNo");
        orderType = bundle.getString("orderType");
        orderState = bundle.getString("orderState");
        storeId = bundle.getString("storeId");
        actionBar = (ActionBar) findViewById(R.id.my_action);
        if (orderType.equals("2")) {//首次更换订单
            if (orderState.equals("2")) {
                actionBar.setTitle("确认收货");
            } else {
                actionBar.setTitle("订单详情");
            }
        }
        if (orderType.equals("1")) {//普通商品订单
            if (orderState.equals("3")) {
                actionBar.setTitle("确认订单");
            } else {
                actionBar.setTitle("订单详情");
            }
        }
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
        initPost();


    }

    private void initPost() {
        JSONObject object = new JSONObject();
        try {
            object.put("orderNo", orderNo);
            object.put("orderType", orderType);
            object.put("storeId", storeId);

        } catch (JSONException e) {
        }
        RequestParams params = new RequestParams(UtilsURL.REQUEST_URL + "getStoreOrderInfoByNoAndType");
        params.addBodyParameter("reqJson", object.toString());
        params.addBodyParameter("token", new DbConfig().getToken());
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    int sta = jsonObject.getInt("status");
                    if (sta == 1) {
                        JSONObject data = jsonObject.getJSONObject("data");

                        userName = data.getString("userName");
                        platNumber = data.getString("platNumber");
                        storeName = data.getString("storeName");
                        userPhone = data.getString("userPhone");

                        if (orderType.equals("2")) {//首次更换订单
                            //获取展示首次更换订单信息
                            getFirstChangeOrderVoList(data);
                            if (orderState.equals("5") || orderState.equals("8")) {
                                //8 待支付  和 5 待发货 不展示条形码信息
                            } else {
                                //获取展示条形码信息
                                getUserCarShoeBarCodeList(data);
                            }
                        }
                        if (orderType.equals("1")) {//普通商品订单
                            //获取展示普通商品订单信息
                            getStockOrderVoList(data);
                        }
                        initData();
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

    private void initData() {
        items.clear();
        if (orderType.equals("2")) {//orderType=2 首次更换订单<

            if (orderState.equals("2")) {//orderState=2 待收货(提交 条形码对比)
                items.add(new PublicOneaddPic("用户名", userName, false, true));
                items.add(new PublicOneaddPic("联系电话", userPhone, true, true));
                items.add(new PublicOneaddPic("车牌号", platNumber, false, true));
                items.add(new PublicOneaddPic("店铺名", storeName, false, true));
                items.add(new PublicOneaddPic("订单编号", orderNo, false, false));
                for (int i = 0; i < shoeFlagList.size(); i++) {
                    items.add(shoeFlagList.get(i));
                }
                items.add(new PublicOneaddPic("条形码", "是否不一致", false, false));
                for (int i = 0; i < barCodeList.size(); i++) {
                    items.add(barCodeList.get(i));
                }
            } else if (orderState.equals("3")) {//orderState=3 待商家确认服务(接单/拒单/客户自提)
                for (int i = 0; i < shoeFlagList.size(); i++) {
                    items.add(shoeFlagList.get(i));
                }
                items.add(new PublicOneaddPic("条形码", "", false, false));
                for (int i = 0; i < barCodeList.size(); i++) {
                    PublicBarCode e = barCodeList.get(i);
                    e.setShow("0");//去除Switch
                    items.add(e);
                }
            } else if (orderState.equals("8") || orderState.equals("5")) {//待支付 和 待发货
                items.add(new PublicOneaddPic("用户名", userName, false, true));
                items.add(new PublicOneaddPic("联系电话", userPhone, true, true));
                items.add(new PublicOneaddPic("车牌号", platNumber, false, true));
                items.add(new PublicOneaddPic("店铺名", storeName, false, true));
                items.add(new PublicOneaddPic("订单编号", orderNo, false, false));
                for (int i = 0; i < shoeFlagList.size(); i++) {
                    items.add(shoeFlagList.get(i));
                }
            } else {//orderState=其它 4已取消   6待用户确认服务   7待评价   1已完成
                items.add(new PublicOneaddPic("用户名", userName, false, true));
                items.add(new PublicOneaddPic("联系电话", userPhone, true, true));
                items.add(new PublicOneaddPic("车牌号", platNumber, false, true));
                items.add(new PublicOneaddPic("店铺名", storeName, false, true));
                items.add(new PublicOneaddPic("订单编号", orderNo, false, false));
                for (int i = 0; i < shoeFlagList.size(); i++) {
                    items.add(shoeFlagList.get(i));
                }
                items.add(new PublicOneaddPic("条形码", "", false, false));
                for (int i = 0; i < barCodeList.size(); i++) {
                    PublicBarCode e = barCodeList.get(i);
                    e.setShow("0");//去除Switch
                    items.add(e);
                }
            }
        }//orderType=2 首次更换订单>

        if (orderType.equals("1")) {//orderType=1 普通商品订单<
            items.add(new PublicOneaddPic("用户名", userName, false, true));
            items.add(new PublicOneaddPic("联系电话", userPhone, true, true));
            items.add(new PublicOneaddPic("车牌号", platNumber, false, true));
            items.add(new PublicOneaddPic("店铺名", storeName, false, true));
            items.add(new PublicOneaddPic("订单编号", orderNo, false, false));
            for (int i = 0; i < goodsInfoList.size(); i++) {
                items.add(goodsInfoList.get(i));
            }
        }//orderType=1 普通商品订单>

        //更新适配器
        assertAllRegistered(adapter, items);
        adapter.notifyDataSetChanged();
    }


    /*
    * //获取条形码信息by data
    * */
    private void getUserCarShoeBarCodeList(JSONObject data) {
        JSONArray userCarShoeBarCodeList = null;
        try {
            userCarShoeBarCodeList = data.getJSONArray("userCarShoeBarCodeList");
            if (userCarShoeBarCodeList == null || userCarShoeBarCodeList.length() == 0) {
                //条形码列表为空 不操作
            } else {
                //条形码列表不为空
                barCodeList.clear();
                for (int j = 0; j < userCarShoeBarCodeList.length(); j++) {
                    if (j == 0) {
                        JSONObject shoeObj0 = (JSONObject) userCarShoeBarCodeList.get(j);
                        barCode_a_ = shoeObj0.getString("barCode");
                        barCode_id_a_ = shoeObj0.getInt("id") + "";
                        isNoConsistent_a_ = "1";//false 默认为1未选中 为一致
                        PublicBarCode barCodebean = new PublicBarCode(barCode_a_, "1", barCode_id_a_, orderNo, "a");
                        barCodeList.add(barCodebean);
                    }
                    if (j == 1) {
                        JSONObject shoeObj1 = (JSONObject) userCarShoeBarCodeList.get(j);
                        barCode_b_ = shoeObj1.getString("barCode");
                        barCode_id_b_ = shoeObj1.getInt("id") + "";
                        isNoConsistent_b_ = "1";//false 默认为1未选中 为一致
                        PublicBarCode barCodebean = new PublicBarCode(barCode_b_, "1", barCode_id_b_, orderNo, "b");
                        barCodeList.add(barCodebean);
                    }
                    if (j == 2) {
                        JSONObject shoeObj2 = (JSONObject) userCarShoeBarCodeList.get(j);
                        barCode_c_ = shoeObj2.getString("barCode");
                        barCode_id_c_ = shoeObj2.getInt("id") + "";
                        isNoConsistent_c_ = "1";//false 默认为1未选中 为一致
                        PublicBarCode barCodebean = new PublicBarCode(barCode_c_, "1", barCode_id_c_, orderNo, "c");
                        barCodeList.add(barCodebean);
                    }
                    if (j == 3) {
                        JSONObject shoeObj3 = (JSONObject) userCarShoeBarCodeList.get(j);
                        barCode_d_ = shoeObj3.getString("barCode");
                        barCode_id_d_ = shoeObj3.getInt("id") + "";
                        isNoConsistent_d_ = "1";//false 默认为1未选中 为一致
                        PublicBarCode barCodebean = new PublicBarCode(barCode_d_, "1", barCode_id_d_, orderNo, "d");
                        barCodeList.add(barCodebean);
                    }
                }
            }
        } catch (JSONException e) {
        }

    }


    /*
    * //获取首次更换订单信息by data
    * */
    private void getFirstChangeOrderVoList(JSONObject data) {
        JSONArray firstChangeOrderVoList = null;
        try {
            firstChangeOrderVoList = data.getJSONArray("firstChangeOrderVoList");
            if (firstChangeOrderVoList == null || firstChangeOrderVoList.length() == 0) {
                //首次更换订单列表为空 不操作
            } else {
                for (int i = 0; i < firstChangeOrderVoList.length(); i++) {
                    JSONObject obj = (JSONObject) firstChangeOrderVoList.get(i);
                    String flag = obj.getString("fontRearFlag");
                    String shoeFlag = "";
                    shoeFlagList.clear();
                    PublicShoeFlag shoeFlagbean = new PublicShoeFlag();
                    if (flag.equals("1")) {
                        shoeFlag = "前胎";
                        fontShoeImg = obj.getString("shoeImg");
                        fontShoeName = obj.getString("shoeName");
                        fontAmount = obj.getString("fontAmount");
                        shoeFlagbean = new PublicShoeFlag(fontShoeImg, fontShoeName, orderType, shoeFlag, fontAmount);
                    }
                    if (flag.equals("2")) {
                        shoeFlag = "后胎";
                        rearShoeImg = obj.getString("shoeImg");
                        rearShoeName = obj.getString("shoeName");
                        rearAmount = obj.getString("rearAmount");
                        shoeFlagbean = new PublicShoeFlag(rearShoeImg, rearShoeName, orderType, shoeFlag, rearAmount);
                    }
                    if (flag.equals("0")) {
                        shoeFlag = "前胎/后胎";
                        consistentShoeImg = obj.getString("shoeImg");
                        consistentShoeName = obj.getString("shoeName");
                        consistentAmount = obj.getString("fontAmount");
                        shoeFlagbean = new PublicShoeFlag(consistentShoeImg, consistentShoeName, orderType, shoeFlag, consistentAmount);
                    }

                    if (i == 0) {//首尾中判断加线
                        shoeFlagbean.setHasTopline("1");
                        shoeFlagbean.setHasBottomline("0");
                    }
                    if (i == firstChangeOrderVoList.length() - 1) {
                        shoeFlagbean.setHasTopline("1");
                        shoeFlagbean.setHasBottomline("1");
                    }
                    if (i != 0 && i != firstChangeOrderVoList.length() - 1) {
                        shoeFlagbean.setHasTopline("1");
                        shoeFlagbean.setHasBottomline("0");
                    }
                    shoeFlagList.add(shoeFlagbean);

                }
            }
        } catch (JSONException e) {
        }

    }


    /*
      * //获取普通商品订单信息by data
      * */
    private void getStockOrderVoList(JSONObject data) {
        JSONArray stockOrderVoList = null;
        try {
            stockOrderVoList = data.getJSONArray("stockOrderVoList");
            if (stockOrderVoList == null || stockOrderVoList.length() == 0) {
                //普通商品订单列表为空 不操作
            } else {
                for (int i = 0; i < stockOrderVoList.length(); i++) {
                    JSONObject goods = (JSONObject) stockOrderVoList.get(i);
                    PublicGoodsInfo goodsInfo = new PublicGoodsInfo();
                    goodsInfo.setDetailImage(goods.getString("detailImage"));
                    goodsInfo.setDetailName(goods.getString("detailName"));
                    goodsInfo.setDetailPrice(goods.getString("detailPrice"));
                    goodsInfo.setDetailTotalPrice(goods.getString("detailTotalPrice"));
                    goodsInfo.setAmount(goods.getInt("amount") + "");
                    goodsInfo.setDetailId(goods.getString("detailId"));
                    goodsInfo.setDetailServiceId(goods.getString("detailServiceId"));
                    goodsInfo.setDetailServiceTypeId(goods.getString("detailServiceTypeId"));
                    if (i == 0) {//首尾中判断加线
                        goodsInfo.setHasTopline("1");//首
                        goodsInfo.setHasBottomline("0");
                    }
                    if (i == stockOrderVoList.length() - 1) {//尾
                        goodsInfo.setHasTopline("1");
                        goodsInfo.setHasBottomline("1");
                    }
                    if (i != 0 && i != stockOrderVoList.length() - 1) {//中
                        goodsInfo.setHasTopline("1");
                        goodsInfo.setHasBottomline("0");
                    }
                    goodsInfoList.add(goodsInfo);
                }
            }

        } catch (JSONException e) {
        }
    }


    private void initView() {
        listView = (RecyclerView) findViewById(R.id.order_info_activity);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        listView.setLayoutManager(linearLayoutManager);
        adapter = new MultiTypeAdapter(items);
        register();
        listView.setAdapter(adapter);
        assertHasTheSameAdapter(listView, adapter);

        stateButton = findViewById(R.id.order_state_button);
        initButton();

    }

    private void register() {
        adapter.register(PublicOneaddPic.class, new PublicOneaddPicProvider(getApplicationContext()));
        adapter.register(PublicShoeFlag.class, new PublicShoeFlagProvider(getApplicationContext()));
        PublicBarCodeProvider barCodeProvider = new PublicBarCodeProvider(getApplicationContext());
        barCodeProvider.setListener(this);
        adapter.register(PublicBarCode.class, barCodeProvider);
        adapter.register(PublicGoodsInfo.class, new PublicGoodsInfoProvider(getApplicationContext()));
    }

    private void initButton() {
        if (orderType.equals("2")) {//首次更换订单
            if (orderState.equals("2")) {//待上商家确认收货
                stateButton.setText("提交");
                stateButton.setBackgroundResource(R.drawable.login_code_button);
                //提交监听
                bindButtonFirstChange();
            }
            if (orderState.equals("3")) {//待商家确认服务
                stateButton.setVisibility(View.GONE);
            }
            if (orderState.equals("4")) {
                stateButton.setText("已取消");
            }
            if (orderState.equals("8")) {
                stateButton.setText("待支付");
            }
            if (orderState.equals("5")) {
                stateButton.setText("待发货");
            }
            if (orderState.equals("6")) {
                stateButton.setText("待用户确认服务");
            }
            if (orderState.equals("7")) {
                stateButton.setText("待评价");
            }
            if (orderState.equals("1")) {
                stateButton.setText("已完成");
            }
        }
        if (orderType.equals("1")) {//商品订单

            if (orderState.equals("3")) {//待商家确认服务
                stateButton.setText("提交");
                stateButton.setBackgroundResource(R.drawable.login_code_button);
                //提交监听
                bindButtonOrdinarygoods();
            }
            if (orderState.equals("8")) {
                stateButton.setText("待支付");
            }
            if (orderState.equals("7")) {
                stateButton.setText("待评价");
            }
            if (orderState.equals("1")) {
                stateButton.setText("已完成");
            }
        }
    }


    /*
   * //提交监听//普通商品订单
   * */
    private void bindButtonOrdinarygoods() {
        RxViewAction.clickNoDouble(stateButton).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                showSaveDialog("确认该订单吗?");
            }
        });
    }


    /*
    * //提交监听//首次更换订单
    * */
    private void bindButtonFirstChange() {
        RxViewAction.clickNoDouble(stateButton).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                showSaveDialog("确认提交吗?");
            }
        });
    }

    /*
    * 提交dialog
    * */
    private void showSaveDialog(String error) {
        AlertDialog dialog = new AlertDialog.Builder(this).create();
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_save_commit, null);
        TextView error_text = (TextView) dialogView.findViewById(R.id.save_text);
        error_text.setText(error);
        dialog.setTitle("如意如驿商家版");
        dialog.setIcon(R.drawable.ic_logo_huise);
        dialog.setView(dialogView);
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "再看看", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        //确认提交 请求提交数据
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "是的", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (orderType.equals("2")) {
                    saveServerFirstChange();//首次更换订单
                }
                if (orderType.equals("1")) {
                    saveServerOrdinarygoods();//普通商品订单
                }

            }
        });

        dialog.show();
        //设置按钮颜色
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.theme_primary));
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.theme_primary));

    }


    /*
   * 确认提交 请求server//普通商品订单
   * */
    private void saveServerOrdinarygoods() {
//        showDialogProgress(progressDialog, "信息提交中...");
        Toast.makeText(getApplicationContext(), "订单确认暂缺接口", Toast.LENGTH_SHORT).show();
    }

    /*
    * 确认提交 请求server//首次更换订单
    * */
    private void saveServerFirstChange() {
        showDialogProgress(progressDialog, "信息提交中...");
        //提交事件
        List<BarCode> barCodeList = new ArrayList<BarCode>();
        if (isNoConsistent_a_ != null) {
            BarCode code_a_ = new BarCode(barCode_a_, isNoConsistent_a_, barCode_id_a_, orderNo);
            barCodeList.add(code_a_);
        }
        if (isNoConsistent_b_ != null) {
            BarCode code_b_ = new BarCode(barCode_b_, isNoConsistent_b_, barCode_id_b_, orderNo);
            barCodeList.add(code_b_);
        }
        if (isNoConsistent_c_ != null) {
            BarCode code_c_ = new BarCode(barCode_c_, isNoConsistent_c_, barCode_id_c_, orderNo);
            barCodeList.add(code_c_);
        }
        if (isNoConsistent_d_ != null) {
            BarCode code_d_ = new BarCode(barCode_d_, isNoConsistent_d_, barCode_id_d_, orderNo);
            barCodeList.add(code_d_);
        }

        RequestParams params = new RequestParams(UtilsURL.REQUEST_URL + "storeConfirmReceiptShoes");
        params.addBodyParameter("reqJson", barCodeList.toString());
        params.addBodyParameter("token", new DbConfig().getToken());
        params.setConnectTimeout(6000);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject object = new JSONObject(result);
                    String msg = object.getString("msg");
                    int status = object.getInt("status");
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                    if (status == 1) {
                        finish();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("page", "order");
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }


                } catch (JSONException e) {
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(getApplicationContext(), "提交失败,请检查网络", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                hideDialogProgress(progressDialog);
            }
        });
    }

    /*
    * BarCode  Switch 点击监听回调
    * */
    @Override
    public void OnBarCodeSwitchClickListener(String barcode, String status, String orderNo, String id, String abcd) {
        switch (abcd) {
            case "a":
                isNoConsistent_a_ = status;
                break;
            case "b":
                isNoConsistent_b_ = status;
                break;
            case "c":
                isNoConsistent_c_ = status;
                break;
            case "d":
                isNoConsistent_d_ = status;
                break;
        }
    }
}
