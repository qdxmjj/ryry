package com.example.warehouse.ui.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.warehouse.MainActivity;
import com.example.warehouse.R;
import com.example.warehouse.db.DbConfig;
import com.example.warehouse.db.TireType;
import com.example.warehouse.ui.ChooseDialog;
import com.example.warehouse.ui.ChooseDialogViewBinder;
import com.example.warehouse.ui.activity.base.WareHouseBaseActivity;
import com.example.warehouse.ui.cell.WareActionBar;
import com.example.warehouse.ui.mulit.TireSize;
import com.example.warehouse.ui.mulit.TireSizeViewBinder;
import com.example.warehouse.utils.RequestUtils;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;
import com.ruyiruyi.rylibrary.cell.ActionBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import me.drakeet.multitype.MultiTypeAdapter;
import rx.functions.Action1;

import static me.drakeet.multitype.MultiTypeAsserts.assertAllRegistered;
import static me.drakeet.multitype.MultiTypeAsserts.assertHasTheSameAdapter;

/**
 * 采购
 */
public class PurchaseActivity extends WareHouseBaseActivity implements ChooseDialogViewBinder.onDialogChooseClick,TireSizeViewBinder.OnTireSizeClickListenr {
    private static final String TAG = PurchaseActivity.class.getSimpleName();
    private WareActionBar actionBar;
    private LinearLayout cangkuChooseView;
    private LinearLayout gongyingshangChooseView;
    private LinearLayout pinpaiChooseView;
    private ImageView addTireSizeView;
    private TextView postButton;
    private Dialog dialog;
    private View inflate;
    private RecyclerView listView;
    private List<Object> items = new ArrayList<>();
    private MultiTypeAdapter adapter;
    private List<Object> tireItems = new ArrayList<>();
    private MultiTypeAdapter tireAdapter;
    public int currentDialogType = 0 ; //0是切换仓库 1是切换供应商 2是切换品牌
    private LinearLayout dingweiLayout;
    private LinearLayout butaiLayout;
    private LinearLayout huanxinLayout;
    private TextView titleNameView;
    private int userID;
    private ProgressDialog dataDialog;
    public List<String> brandList;
    public List<String> distributorList;
    public List<String> storeList;
    private TextView cangkuNameText;
    private TextView gongyingshangNameText;
    private TextView pinpaiNameText;

    public String currentCangku = "";
    public String currentGongyingshang = "";
    public String currentPinpai = "";
    public static final int TIRE_CHOOSE_REQUSET = 10;
    public List<TireType> tireList ;
    private RecyclerView tireListView;
    private ImageView tireDeleteView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase);
        actionBar = (WareActionBar) findViewById(R.id.my_action);
        actionBar.setTitle("采购");
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
        userID = new DbConfig(this).getUser().getId();
        dataDialog = new ProgressDialog(this);
        brandList = new ArrayList<>();
        distributorList = new ArrayList<>();
        storeList = new ArrayList<>();
        tireList = new ArrayList<>();

        initView();

        initDataFromService();
    }

    /**
     * 获取仓库，品牌，供应商
     */
    private void initDataFromService() {
        showDialogProgress(dataDialog,"数据加载中...");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userId",userID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "stocks/getStockUserMainStoreDistributor");
        params.addBodyParameter("reqJson", jsonObject.toString());
        params.setConnectTimeout(10000);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess: " + result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String status = jsonObject.getString("status");
                    String msg = jsonObject.getString("msg");

                    if (status.equals("1")) {
                        JSONObject data = jsonObject.getJSONObject("data");
                        brandList.clear();
                        distributorList.clear();
                        storeList.clear();
                        JSONArray brandStringList = data.getJSONArray("brandStringList");
                        for (int i = 0; i < brandStringList.length(); i++) {
                            String brandName = brandStringList.getString(i);
                            brandList.add(brandName);
                        }

                        JSONArray distributorListJson = data.getJSONArray("distributorList");
                        for (int i = 0; i < distributorListJson.length(); i++) {
                            JSONObject object = distributorListJson.getJSONObject(i);
                            String name = object.getString("name");
                            distributorList.add(name);
                        }

                        JSONArray storeListJson = data.getJSONArray("storeList");
                        for (int i = 0; i < storeListJson.length(); i++) {
                            JSONObject object = storeListJson.getJSONObject(i);
                            String storeName = object.getString("storeName");
                            storeList.add(storeName);
                        }

                        currentCangku = storeList.get(0).toString();
                        currentGongyingshang = distributorList.get(0).toString();
                        currentPinpai= brandList.get(0).toString();

                        initData();
                    } else if (status.equals("-999")) {
                        showUserTokenDialog("您的账号在其它设备登录,请重新登录");
                    } else {
                        Toast.makeText(PurchaseActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {

                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(PurchaseActivity.this, "网络异常，请检查网络链接", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                hideDialogProgress(dataDialog);
            }
        });
    }

    private void initData() {

        cangkuNameText.setText(currentCangku);
        gongyingshangNameText.setText(currentGongyingshang);
        pinpaiNameText.setText(currentPinpai);
    }

    private void initView() {

        cangkuChooseView = (LinearLayout) findViewById(R.id.choose_cangku_view);
        gongyingshangChooseView = (LinearLayout) findViewById(R.id.gongyingshang_choose_view);
        pinpaiChooseView = (LinearLayout) findViewById(R.id.pinpai_choose_view);
        addTireSizeView = (ImageView) findViewById(R.id.add_tire_view);
        postButton = (TextView) findViewById(R.id.post_button);
        cangkuNameText = (TextView) findViewById(R.id.cangku_name_text);
        gongyingshangNameText = (TextView) findViewById(R.id.gongyingshang_name_text);
        pinpaiNameText = (TextView) findViewById(R.id.pinpai_name_text);
        tireListView = (RecyclerView) findViewById(R.id.tire_listview);
        tireDeleteView = (ImageView) findViewById(R.id.tire_delete_view);

        RxViewAction.clickNoDouble(tireDeleteView)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {

                        showDeleteDialog();

                    }
                });


        //选择仓库
        RxViewAction.clickNoDouble(cangkuChooseView)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        //dialog.show();
                        currentDialogType = 0;
                        initDialogData();

                    }
                });

        //选择供应商
        RxViewAction.clickNoDouble(gongyingshangChooseView)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        currentDialogType = 1;
                        initDialogData();
                    }
                });

        //选择品牌
        RxViewAction.clickNoDouble(pinpaiChooseView)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        currentDialogType = 2;
                        initDialogData();
                    }
                });

        //添加轮胎规格
        RxViewAction.clickNoDouble(addTireSizeView)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Intent intent = new Intent(getApplicationContext(),TireSizeChooseActivity.class);
                        intent.putExtra("PIN_PAI",currentPinpai);
                        startActivityForResult(intent,TIRE_CHOOSE_REQUSET);
                    }
                });

        //提交采购订单
        RxViewAction.clickNoDouble(postButton)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {

                    }
                });
        LinearLayoutManager tireManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        tireListView.setLayoutManager(tireManager);
        tireAdapter = new MultiTypeAdapter(tireItems);
        tireRegister();
        tireListView.setAdapter(tireAdapter);
        assertHasTheSameAdapter(tireListView, tireAdapter);


/*
        *//**
         * 仓库选择dialog
         *//*
        dialog = new Dialog(this, R.style.ActionSheetDialogStyle);
        inflate = LayoutInflater.from(this).inflate(R.layout.dialog_choose,null);
        inflate.setMinimumWidth(1000);
        listView = ((RecyclerView) inflate.findViewById(R.id.choose_list));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        listView.setLayoutManager(linearLayoutManager);
        adapter = new MultiTypeAdapter(items);
        register();
        listView.setAdapter(adapter);
        assertHasTheSameAdapter(listView, adapter);

        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setAttributes(lp);
        dialog.setCanceledOnTouchOutside(true);*/
        /**
         * 品质服务dialog
         */
        dialog = new Dialog(this, R.style.ActionSheetDialogStyle);
        inflate = LayoutInflater.from(this).inflate(R.layout.dialog_choose, null);
        inflate.setMinimumWidth(10000);
        listView = ((RecyclerView) inflate.findViewById(R.id.choose_list));
        titleNameView = ((TextView) inflate.findViewById(R.id.dialog_title_view));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        listView.setLayoutManager(linearLayoutManager);
        adapter = new MultiTypeAdapter(items);
        register();
        listView.setAdapter(adapter);
        assertHasTheSameAdapter(listView, adapter);
        dialog.setContentView(inflate);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);

        WindowManager.LayoutParams shopLp = dialogWindow.getAttributes();
        WindowManager wm = (WindowManager) this
                .getSystemService(Context.WINDOW_SERVICE);
        int height = wm.getDefaultDisplay().getHeight();
        int width = wm.getDefaultDisplay().getWidth();
        shopLp.height = (int) (height * 0.5);
        shopLp.width = (int) (width * 1);
        dialogWindow.setAttributes(shopLp);

        dialog.setCanceledOnTouchOutside(true);



    }

    private void showDeleteDialog() {
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(this);
        normalDialog.setIcon(R.mipmap.ic_launcher);
        normalDialog.setTitle("如驿如意");
        normalDialog.setMessage("您确认要删除全部型号的轮胎？");
        normalDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        tireList.clear();
                        initTireSizeData();
                    }
                });
        normalDialog.setNegativeButton("关闭",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        // 显示
        normalDialog.show();
    }

    private void tireRegister() {
        TireSizeViewBinder tireSizeViewBinder = new TireSizeViewBinder();
        tireSizeViewBinder.setListenr(this);
        tireAdapter.register(TireSize.class, tireSizeViewBinder);
    }

    private void initDialogData() {
        items.clear();
        if (currentDialogType == 0){
            titleNameView.setText("请选择仓库");
            for (int i = 0; i < storeList.size(); i++) {
                items.add(new ChooseDialog(storeList.get(i)));
            }
        }else if (currentDialogType == 1){
            titleNameView.setText("请选择供应商");
            for (int i = 0; i < distributorList.size(); i++) {
                items.add(new ChooseDialog(distributorList.get(i)));
            }
        }else if (currentDialogType == 2){
            titleNameView.setText("请选择品牌");
            for (int i = 0; i < brandList.size() ; i++) {
                items.add(new ChooseDialog(brandList.get(i)));
            }
        }
        assertAllRegistered(adapter, items);
        adapter.notifyDataSetChanged();
        dialog.show();
    }

    private void register() {
        ChooseDialogViewBinder provider = new ChooseDialogViewBinder();
        provider.setListener(this);
        adapter.register(ChooseDialog.class, provider);
    }

    @Override
    public void onChooseItemClickListener(String name) {
        dialog.hide();
        tireList.clear();
        if (currentDialogType == 0){
            currentCangku = name;
        }else if (currentDialogType ==1){
            currentGongyingshang = name;
        }else if (currentDialogType == 2){
            currentPinpai = name;
        }
        initData();
        initTireSizeData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == TIRE_CHOOSE_REQUSET){
            if (requestCode == TIRE_CHOOSE_REQUSET){
                List<TireType> tireChoostList = (List<TireType>) data.getSerializableExtra("TIRE_LIST");
                Log.e(TAG, "onActivityResult:  tireChoostList.size()---" + tireChoostList.size());
                if (tireList.size() == 0){
                    tireList.addAll(tireChoostList);
                }else {     //去除重复添加
                    for (int i = 0; i < tireChoostList.size(); i++) {
                        Boolean isHave = false;
                        for (int j = 0; j < tireList.size(); j++) {
                            if (tireChoostList.get(i).getId() == tireList.get(i).getId()) {
                                isHave = true;
                            }
                        }
                        if (!isHave){
                            tireList.add(tireChoostList.get(i));
                        }
                    }
                }
                Collections.sort(tireList, new Comparator<TireType>() {
                    @Override
                    public int compare(TireType tireType, TireType t1) {
                        Integer ty1 = Integer.parseInt(tireType.getInchmm());
                        Integer ty2= Integer.parseInt(t1.getInchmm());

                        return ty1.compareTo(ty2  );
                    }
                });

                initTireSizeData();

                Log.e(TAG, "onActivityResult: tireList.size()---- " + tireList.size() );
            }
        }

    }

    /**
     * 加载tiresize数据
     */
    private void initTireSizeData() {
        tireItems.clear();
        for (int i = 0; i < tireList.size(); i++) {
            TireType tireType = tireList.get(i);
            tireItems.add(new TireSize(tireType.getId(),tireType.getSize(),tireType.getFlgureName(),tireType.getCount()));

        }
        if (tireItems.size() >0){
            tireListView.setVisibility(View.VISIBLE );
            assertAllRegistered(tireAdapter, tireItems);
            tireAdapter.notifyDataSetChanged();
        }else {
            tireListView.setVisibility(View.INVISIBLE);
        }

    }

    /**
     * 轮胎数量 毁掉
     * @param flgureName
     * @param id
     */
    @Override
    public void onTireCountClickListener(String flgureName, int id) {

    }

    /**
     * 删除轮胎 回调
     * @param flgureName
     * @param id
     */
    @Override
    public void onTireDeleteClickListener(String flgureName, final int id) {

        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(this);
        normalDialog.setIcon(R.mipmap.ic_launcher);
        normalDialog.setTitle("如驿如意");
        normalDialog.setMessage("您确认要删除" + flgureName +"型号的轮胎？");
        normalDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for (int j = 0; j < tireList.size(); j++) {
                            if (tireList.get(j).getId() == id) {
                                tireList.remove(j);
                                return;
                            }

                        }
                        initTireSizeData();
                    }
                });
        normalDialog.setNegativeButton("关闭",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //...To-do
                    }
                });
        // 显示
        normalDialog.show();

    }
}