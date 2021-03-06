package com.ruyiruyi.merchant.ui.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ruyiruyi.merchant.R;
import com.ruyiruyi.merchant.bean.Service;
import com.ruyiruyi.merchant.bean.ServicesBean;
import com.ruyiruyi.merchant.db.DbConfig;
import com.ruyiruyi.merchant.utils.UtilsURL;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;
import com.ruyiruyi.rylibrary.base.BaseActivity;
import com.ruyiruyi.rylibrary.cell.ActionBar;
import com.ruyiruyi.rylibrary.image.ImageUtils;
import com.ruyiruyi.rylibrary.ui.cell.WheelView;
import com.ruyiruyi.rylibrary.utils.FormatDateUtil;
import com.ruyiruyi.rylibrary.utils.glide.GlideCircleTransform;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import id.zelory.compressor.Compressor;
import rx.functions.Action1;

public class GoodsInfoReeditActivity extends BaseActivity {
    private final int CHOOSE_PICTURE = 0;
    private final int TAKE_PICTURE = 1;

    private ActionBar mActionBar;
    private ImageView mGoodsImg;
    private EditText mGoodsName;
    private EditText mGoodsPrice;
    private TextView mGoodsType;
    private EditText mGoodsKucun;
    private TextView tv_tijiaosp;
    private TextView tv_jixuadd;
    private TextView mGoodsStatus;
    private Bitmap imgBitmap;
    private WheelView leftWheel;
    private WheelView rightWheel;
    private WheelView oneWheel;
    private Uri tempUri;
    private String TAG = GoodsInfoReeditActivity.class.getSimpleName();
    public List<Service> serviceList;
    private int currentLeftPosition = 0;
    private int currentRightPosition = 0;
    private int currentForId = 0;
    private int currentSale = 1;  //2 已下架   1在售
    private String currentSaleString = "出售中";
    private String currentSaleForIdString = "请选择";
    private String currentLeftString = "请选择";
    private String currentRightString = "请选择";
    private List<ServicesBean> servicesBean2;
    private List<ServicesBean> servicesBean3;
    private List<ServicesBean> servicesBean4;
    private List<ServicesBean> servicesBean5;
    private List<ServicesBean> servicesBean2a;
    private List<ServicesBean> servicesBean3a;
    private List<ServicesBean> servicesBean4a;
    private List<ServicesBean> servicesBean5a;
    private List<String> leftTypeList;
    private String leftTypeId;
    private String rightTypeId;
    private String img_Path;
    private boolean isOldPic = true;
    private String goods_typeold_string = "";
    private String goodsid;
    private String amount;
    private String soldno;
    private String price;
    private String imgurl;
    private String storeId;
    private String name;
    private String serviceTypeId;
    private String serviceId;
    private String status = "1";//默认出售中
    private ProgressDialog progressDialog;
    private ProgressDialog mainDialog;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    break;
                case 2:
                    myRequestPostForDataBy("3");
                    break;
                case 3:
                    myRequestPostForDataBy("4");
                    break;
                case 4:
                    myRequestPostForDataBy("5");
                    break;
                case 5:
                    //以下操作
                    if (servicesBean2a.size() != 0) {
                        leftTypeList.add(GoodsInfoReeditActivity.this.getString(R.string.service_type_a));
                    }
                    if (servicesBean3a.size() != 0) {
                        leftTypeList.add(GoodsInfoReeditActivity.this.getString(R.string.service_type_b));
                    }
                    if (servicesBean4a.size() != 0) {
                        leftTypeList.add(GoodsInfoReeditActivity.this.getString(R.string.service_type_c));
                    }
                    if (servicesBean5a.size() != 0) {
                        leftTypeList.add(GoodsInfoReeditActivity.this.getString(R.string.service_type_d));
                    }


                    initOldData();//设置商品修改前数据
                    bindView();
                    break;
            }
        }
    };
    private String path_takepic;
    private ScrollView scrollView;

    //设置商品修改前数据
    private void initOldData() {
        tv_tijiaosp.setText("提交修改");
        tv_jixuadd = (TextView) findViewById(R.id.tv_jixuadd);
        tv_jixuadd.setText("提交修改并继续添加");
        mGoodsName.setText(name);
        mGoodsPrice.setText(price);
        mGoodsKucun.setText(amount);
        switch (serviceTypeId) {
            case "2":
                goods_typeold_string = GoodsInfoReeditActivity.this.getString(R.string.service_type_a);
                for (int i = 0; i < servicesBean2a.size(); i++) {
                    if (serviceId.equals(servicesBean2a.get(i).getService_id() + "")) {
                        goods_typeold_string = goods_typeold_string + " " + servicesBean2a.get(i).getServiceInfo();
                        mGoodsType.setText(goods_typeold_string);
                    }
                }
                break;
            case "3":
                goods_typeold_string = GoodsInfoReeditActivity.this.getString(R.string.service_type_b);
                for (int i = 0; i < servicesBean3a.size(); i++) {
                    if (serviceId.equals(servicesBean3a.get(i).getService_id() + "")) {
                        goods_typeold_string = goods_typeold_string + " " + servicesBean3a.get(i).getServiceInfo();
                        mGoodsType.setText(goods_typeold_string);
                    }
                }
                break;
            case "4":
                goods_typeold_string = GoodsInfoReeditActivity.this.getString(R.string.service_type_c);
                for (int i = 0; i < servicesBean4a.size(); i++) {
                    if (serviceId.equals(servicesBean4a.get(i).getService_id() + "")) {
                        goods_typeold_string = goods_typeold_string + " " + servicesBean4a.get(i).getServiceInfo();
                        mGoodsType.setText(goods_typeold_string);
                    }
                }
                break;
            case "5":
                goods_typeold_string = GoodsInfoReeditActivity.this.getString(R.string.service_type_d);
                for (int i = 0; i < servicesBean5a.size(); i++) {
                    if (serviceId.equals(servicesBean5a.get(i).getService_id() + "")) {
                        goods_typeold_string = goods_typeold_string + " " + servicesBean5a.get(i).getServiceInfo();
                        mGoodsType.setText(goods_typeold_string);
                    }
                }
                break;
        }
        switch (status) {
            case "1":
                mGoodsStatus.setText("出售中");
                break;
            case "2":
                mGoodsStatus.setText("已下架");
                break;
        }
        Glide.with(this).load(imgurl)
                .transform(new GlideCircleTransform(this))
//                .skipMemoryCache(true)//跳过内存缓存
//                .diskCacheStrategy(DiskCacheStrategy.NONE)//跳过硬盘缓存
                .into(mGoodsImg);


        hideDialogProgress(mainDialog);
        scrollView.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_goods_info);
        mActionBar = (ActionBar) findViewById(R.id.goodsinfo_acbar);
        mActionBar.setTitle("商品信息");
        mActionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(int var1) {
                switch (var1) {
                    case -1:
                        onBackPressed();
                        break;
                }
            }
        });
        progressDialog = new ProgressDialog(this);
        mainDialog = new ProgressDialog(this);
        showDialogProgress(mainDialog, "信息加载中...");
        scrollView = findViewById(R.id.scrollView);
        scrollView.setVisibility(View.GONE);

        Bundle bundle = getIntent().getExtras();
        goodsid = bundle.getString("goodsid");
        name = bundle.getString("name");
        serviceTypeId = bundle.getString("serviceTypeId");
        serviceId = bundle.getString("serviceId");
        status = bundle.getString("status");
        if ("2".equals(status)) {
            currentSaleString = "已下架";
            currentSale = 2;
        } else {
            currentSaleString = "出售中";
            currentSale = 1;
        }
        amount = bundle.getString("amount");
        soldno = bundle.getString("soldno");
        price = bundle.getString("price");
        imgurl = bundle.getString("imgurl");
        Log.e(TAG, "onCreate:  imgurl = " + imgurl);
        storeId = new DbConfig(getApplicationContext()).getId() + "";

        serviceList = new ArrayList<>();
        initView();
        initData();
    }

    private void initData() {
        myRequestPostForDataBy("2");
    }

    private void myRequestPostForDataBy(final String s) {
        final List<ServicesBean> servicesBean = new ArrayList<>();
        JSONObject jsonObject = new JSONObject();
        try {
            String storeId = new DbConfig(getApplicationContext()).getId() + "";
            jsonObject.put("storeId", storeId);
            jsonObject.put("serviceTypeId", s);
        } catch (JSONException e) {
        }
        RequestParams params = new RequestParams(UtilsURL.REQUEST_URL + "getStoreServicesAndState");
        params.addBodyParameter("reqJson", jsonObject.toString());
        Log.e(TAG, "myRequestPostForDataBy10086net: params.toString()==>" + "---" + s + "---" + params.toString());
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject object = new JSONObject(result);
                    JSONArray data = object.getJSONArray("data");
                    String msg = object.getString("msg");
                    int status = object.getInt("status");
                    if (data != null && data.length() != 0) {
                        for (int i = 0; i < data.length(); i++) {
                            ServicesBean bean = new ServicesBean();
                            JSONObject obj = (JSONObject) data.get(i);
                            bean.setService_id(obj.getInt("id"));
                            bean.setIsChecked(Integer.parseInt(obj.getString("selectState")));
                            bean.setServiceInfo(obj.getString("name"));
                            servicesBean.add(bean);
                        }
                    }
                    switch (s) {
                        case "2":
                            for (int i = 0; i < servicesBean.size(); i++) {
                                servicesBean2.add(servicesBean.get(i));
                            }
                            for (int i = 0; i < servicesBean2.size(); i++) {
                                if (servicesBean2.get(i).getIsChecked() == 1) {
                                    servicesBean2a.add(servicesBean2.get(i));
                                }
                            }
                            break;
                        case "3":
                            for (int i = 0; i < servicesBean.size(); i++) {
                                servicesBean3.add(servicesBean.get(i));
                            }
                            for (int i = 0; i < servicesBean3.size(); i++) {
                                if (servicesBean3.get(i).getIsChecked() == 1) {
                                    servicesBean3a.add(servicesBean3.get(i));
                                }
                            }
                            break;
                        case "4":
                            for (int i = 0; i < servicesBean.size(); i++) {
                                servicesBean4.add(servicesBean.get(i));
                            }
                            for (int i = 0; i < servicesBean4.size(); i++) {
                                if (servicesBean4.get(i).getIsChecked() == 1) {
                                    servicesBean4a.add(servicesBean4.get(i));
                                }
                            }
                            break;
                        case "5":
                            for (int i = 0; i < servicesBean.size(); i++) {
                                servicesBean5.add(servicesBean.get(i));
                            }
                            for (int i = 0; i < servicesBean5.size(); i++) {
                                if (servicesBean5.get(i).getIsChecked() == 1) {
                                    servicesBean5a.add(servicesBean5.get(i));
                                }
                            }
                            break;

                    }
                    Message message = new Message();
                    message.what = Integer.parseInt(s);
                    mHandler.sendMessage(message);
                    Log.e(TAG, "onSuccess:10086net servicesBean.size()" + "---" + s + "---" + servicesBean.size());
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

    private void bindView() {
        //头像选择
        RxViewAction.clickNoDouble(mGoodsImg).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                showPicInputDialog();
            }
        });

        //商品分类
        RxViewAction.clickNoDouble(mGoodsType).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (leftTypeList == null || leftTypeList.size() == 0) {
                    showErrorToMyServiceActDialog("请先选择您的服务小类!");
                    return;
                }
                showGoodsTypeDialog();
            }
        });

        //商品状态
        RxViewAction.clickNoDouble(mGoodsStatus).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                showGoodsStatusDialog();
            }
        });

        //提交商品
        RxViewAction.clickNoDouble(tv_tijiaosp).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                //初次提交数据
                commitData(1);
            }
        });

        //继续添加
        RxViewAction.clickNoDouble(tv_jixuadd).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                //第二次提交）
                commitData(2);

            }
        });

    }

    private void commitData(int type) {
        if (imgBitmap != null) {
            isOldPic = false;
            img_Path = ImageUtils.savePhoto(imgBitmap, this.getObbDir().getAbsolutePath(), "forpostxiugaigoodsimg");//为提交请求所生成图片 每次提交被替换
        } else {
            isOldPic = true;
        }
        Log.e(TAG, "call: 888 mGoodsName.getText()" + mGoodsName.getText());
        if (mGoodsName.getText() == null || mGoodsName.getText().length() == 0) {
            Toast.makeText(GoodsInfoReeditActivity.this, "请输入商品名称", Toast.LENGTH_SHORT).show();
            return;
        }
        if (mGoodsPrice.getText() == null || mGoodsPrice.getText().length() == 0) {
            Toast.makeText(GoodsInfoReeditActivity.this, "请输入商品单价", Toast.LENGTH_SHORT).show();
            return;
        }
       /* if (mGoodsPrice.getText().toString().equals("0")) {
            Toast.makeText(GoodsInfoReeditActivity.this, "商品单价不能为0", Toast.LENGTH_SHORT).show();
            return;
        }*/
        if (leftTypeId == null || leftTypeId.equals("") || rightTypeId == null || rightTypeId.equals("")) {
            leftTypeId = serviceTypeId;
            rightTypeId = serviceId;
        }
        if (mGoodsKucun.getText() == null || mGoodsKucun.getText().length() == 0) {
            Toast.makeText(GoodsInfoReeditActivity.this, "请输入商品库存", Toast.LENGTH_SHORT).show();
            return;
        }
        if (mGoodsKucun.getText().toString().equals("0")) {
            Toast.makeText(GoodsInfoReeditActivity.this, "商品库存不能为0", Toast.LENGTH_SHORT).show();
            return;
        }
        if (mGoodsStatus.getText() == null || mGoodsStatus.getText().equals("")) {
            currentSale = Integer.parseInt(status);
        }
        showSaveDialog(type);
    }

    private void showSaveDialog(final int type) {
        AlertDialog dialog = new AlertDialog.Builder(this).create();
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_save_commit, null);
        TextView error_text = (TextView) dialogView.findViewById(R.id.save_text);
        switch (type) {
            case 1:
                error_text.setText("确定提交修改吗");
                break;
            case 2:
                error_text.setText("确定提交修改并继续添加吗");
                break;
        }
        dialog.setTitle("如意如驿商家版");
        dialog.setIcon(R.drawable.ic_launcher);
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
                showDialogProgress(progressDialog, "修改提交中...");
                JSONObject object = new JSONObject();
                try {
                    object.put("storeId", storeId);
                    object.put("name", mGoodsName.getText());
                    if (!isOldPic) {
                        object.put("imgUrl", imgurl);
                    }
                    object.put("id", goodsid);
                    object.put("serviceTypeId", leftTypeId);
                    object.put("serviceId", rightTypeId);
                    object.put("amount", mGoodsKucun.getText());
                    object.put("price", mGoodsPrice.getText());
                    object.put("status", currentSale);//2 下架  1 在售
                } catch (JSONException e) {
                }
                RequestParams params = new RequestParams(UtilsURL.REQUEST_URL + "updateStock");
                String replace = object.toString().replace("\\", "");
                params.addBodyParameter("reqJson", replace);
                if (!isOldPic) {
                    File file_s = null;
                    try {
                        file_s = new Compressor(getApplicationContext()).compressToFile(new File(img_Path));
                    } catch (IOException e) {
                    }
                    params.addBodyParameter("stock_img", file_s);
                    Log.e(TAG, "onClick: 012 img_Path " + img_Path);
                }

                x.http().post(params, new Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        Log.e(TAG, "onSuccess: " + result);
                        try {
                            JSONObject object1 = new JSONObject(result);
                            String status = object1.getString("status");
                            if (status.equals("1")) {
                                Toast.makeText(GoodsInfoReeditActivity.this, "商品修改成功", Toast.LENGTH_SHORT).show();
                                switch (type) {
                                    case 1:
                                        finish();
                                        break;
                                    case 2:
                                        Intent intent = new Intent(GoodsInfoReeditActivity.this, GoodsInfoActivity.class);
                                        startActivity(intent);
                                        finish();
                                        break;
                                }
                            } else {
                                Toast.makeText(GoodsInfoReeditActivity.this, "商品修改失败", Toast.LENGTH_SHORT).show();
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
        });

        dialog.show();
        //设置按钮颜色
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.theme_primary));
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.theme_primary));

    }

    private void showGoodsStatusDialog() {
        currentSaleString = "出售中";
        View v_goodstatus = LayoutInflater.from(this).inflate(R.layout.dialog_one_horizontal_wheel_view, null);
        oneWheel = (WheelView) v_goodstatus.findViewById(R.id.whv_one);
        ArrayList<String> strlist = new ArrayList<>();
        strlist.add("出售中");
        strlist.add("已下架");
        oneWheel.setItems(strlist, 0);
        oneWheel.setOnItemSelectedListener(new WheelView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int selectedIndex, String item) {
                if (selectedIndex == 0) {
                    currentSale = 1;
                } else {
                    currentSale = 2;
                }

                currentSaleString = item;
            }
        });
        oneWheel.setIsLoop(false);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("请选择商品状态")
                .setView(v_goodstatus);
        AlertDialog dialog = builder.create();
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        currentSaleForIdString = currentSaleString;//防止未确定dis
                        mGoodsStatus.setText(currentSaleForIdString);
                        if (currentSaleForIdString.equals("出售中")) {
                            currentSale = 1;
                        } else {
                            currentSale = 2;
                        }
                    }
                }
        );
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.theme_primary));
    }


    private void showErrorToMyServiceActDialog(String error) {
        AlertDialog dialog = new AlertDialog.Builder(this).create();
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_error, null);
        TextView error_text = (TextView) dialogView.findViewById(R.id.error_text);
        error_text.setText(error);
        dialog.setTitle("如意如驿商家版");
        dialog.setIcon(R.drawable.ic_launcher);
        dialog.setView(dialogView);
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "去选择", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(getApplicationContext(), MyServiceActivity.class);
                startActivity(intent);
                finish();
            }
        });
        dialog.show();
        //设置按钮颜色
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.theme_primary));
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.theme_primary));
    }


    private void showGoodsTypeDialog() {
        View v_goodstype = LayoutInflater.from(this).inflate(R.layout.dialog_two_horizontal_wheel_view, null);
        leftWheel = (WheelView) v_goodstype.findViewById(R.id.whv_left);
        rightWheel = (WheelView) v_goodstype.findViewById(R.id.whv_right);
        currentLeftPosition = 0;//每次弹Dialog 初始化
        currentRightPosition = 0;//每次弹Dialog 初始化
        currentForId = 0;//每次弹Dialog 初始化
        leftWheel.setItems(leftTypeList, 0);
        currentLeftString = leftTypeList.get(0);//每次弹Dialog 初始化
        String s = leftTypeList.get(0);
        List<String> strings = getRightStringList(s);
        rightWheel.setItems(strings, 0);
        currentRightString = strings.get(0);//每次弹Dialog 初始化
        leftWheel.setOnItemSelectedListener(new WheelView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int selectedIndex, String item) {
                currentLeftPosition = leftWheel.getSelectedPosition();
                currentLeftString = leftWheel.getSelectedItem();
                List<String> strings2 = getRightStringList(currentLeftString);
                rightWheel.setItems(strings2, 0);
                currentRightString = strings2.get(0);//每次点击 初始化
                currentRightPosition = 0;//初始化 R position
            }
        });
        rightWheel.setOnItemSelectedListener(new WheelView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int selectedIndex, String item) {
                currentRightPosition = rightWheel.getSelectedPosition();
                currentRightString = rightWheel.getSelectedItem();
            }
        });
        leftWheel.setIsLoop(false);
        rightWheel.setIsLoop(false);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("选择商品分类")
                .setView(v_goodstype);
        AlertDialog dialog = builder.create();
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mGoodsType.setText(currentLeftString + "  " + currentRightString);
                        currentForId = currentRightPosition;
                        //获取ID
                        if (currentLeftString.equals(GoodsInfoReeditActivity.this.getString(R.string.service_type_a))) {
                            leftTypeId = "2";
                            rightTypeId = servicesBean2a.get(currentForId).getService_id() + "";
                        } else if (currentLeftString.equals(GoodsInfoReeditActivity.this.getString(R.string.service_type_b))) {
                            leftTypeId = "3";
                            rightTypeId = servicesBean3a.get(currentForId).getService_id() + "";
                        } else if (currentLeftString.equals(GoodsInfoReeditActivity.this.getString(R.string.service_type_c))) {
                            leftTypeId = "4";
                            rightTypeId = servicesBean4a.get(currentForId).getService_id() + "";
                        } else {
                            leftTypeId = "5";
                            rightTypeId = servicesBean5a.get(currentForId).getService_id() + "";
                        }
                    }
                }
        );
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.theme_primary));

    }

    private List<String> getRightStringList(String s) {
        ArrayList<String> strings = new ArrayList<>();
        if (s.equals(GoodsInfoReeditActivity.this.getString(R.string.service_type_a))) {
            for (int i = 0; i < servicesBean2a.size(); i++) {
                strings.add(servicesBean2a.get(i).getServiceInfo());
            }
        } else if (s.equals(GoodsInfoReeditActivity.this.getString(R.string.service_type_b))) {
            for (int i = 0; i < servicesBean3a.size(); i++) {
                strings.add(servicesBean3a.get(i).getServiceInfo());
            }
        } else if (s.equals(GoodsInfoReeditActivity.this.getString(R.string.service_type_c))) {
            for (int i = 0; i < servicesBean4a.size(); i++) {
                strings.add(servicesBean4a.get(i).getServiceInfo());
            }
        } else {
            for (int i = 0; i < servicesBean5a.size(); i++) {
                strings.add(servicesBean5a.get(i).getServiceInfo());
            }
        }
        return strings;
    }

    private List<String> getLeftStringList() {
        for (int i = 0; i < serviceList.size(); i++) {
            Service service = serviceList.get(i);
//            XiaoService xiaoService = serviceList.get(i);
        }

        return new ArrayList<String>();
    }

    private void showPicInputDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("上传照片");
        String[] items = {"选择本地照片", "拍照"};
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case CHOOSE_PICTURE://选择本地照片
                        Intent openBendiPicIntent = new Intent(Intent.ACTION_GET_CONTENT);
                        openBendiPicIntent.setType("image/*");
                        startActivityForResult(openBendiPicIntent, CHOOSE_PICTURE);
                        break;
                    case TAKE_PICTURE://拍照
                        takePicture();
                        break;
                }
            }
        });
        final AlertDialog dialog = builder.create();
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialog.dismiss();
            }
        });
        dialog.show();
        //设置按钮颜色
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.theme_primary));
    }

    private void takePicture() {
        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (Build.VERSION.SDK_INT >= 23) {
            // 需要申请动态权限
            int check = ContextCompat.checkSelfPermission(this, permissions[0]);
            // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝
            if (check != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }
        Intent openCameraIntent = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE);
        File file = null;
        file = new File(this.getObbDir().getAbsolutePath(), "goodsinforedeitimg.jpg");
        path_takepic = file.getPath();

        //判断是否是AndroidN以及更高的版本
        if (Build.VERSION.SDK_INT >= 24) {
            openCameraIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            tempUri = FileProvider.getUriForFile(GoodsInfoReeditActivity.this, "com.ruyiruyi.merchant.fileProvider", file);
        } else {
            tempUri = Uri.fromFile(new File(this.getObbDir().getAbsolutePath(), "image.jpg"));
        }
        Log.e(TAG, "takePicture: " + tempUri);
        // 指定照片保存路径（SD卡），image.jpg为一个临时文件，每次拍照后这个图片都会被替换
        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);
        startActivityForResult(openCameraIntent, TAKE_PICTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CHOOSE_PICTURE:
                    Uri uri = data.getData();
                    setImageToViewFromPhone(uri, false);
                    break;
                case TAKE_PICTURE:
                    setImageToViewFromPhone(tempUri, true);
                    break;

            }
        }
    }

    //未剪辑照片
    private void setImageToViewFromPhone(Uri uri, boolean isCamera) {
        int degree = 0;
        if (isCamera) {
            degree = ImageUtils.readPictureDegree(path_takepic);
        } else {
            degree = ImageUtils.getOrientation(getApplicationContext(), uri);
        }
        if (uri != null) {
            Bitmap photo = null;
            try {
                photo = ImageUtils.getBitmapFormUri(getApplicationContext(), uri);
            } catch (IOException e) {
            }
            imgBitmap = rotaingImageView(degree, photo);
//   2          mGoodsImg.setImageBitmap(imgBitmap);
            //Glide 加载BitMap需要先将bitmap对象转换为字节,在加载;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            imgBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] bytes = baos.toByteArray();
            Glide.with(this).load(bytes)
                    .transform(new GlideCircleTransform(this))
//                    .diskCacheStrategy(DiskCacheStrategy.NONE)//跳过硬盘缓存
//                    .skipMemoryCache(true)//跳过内存缓存
                    .into(mGoodsImg);
        }
    }

    public static Bitmap rotaingImageView(int angle, Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        Bitmap returnBm = null;
        // 根据旋转角度，生成旋转矩阵
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        try {
            // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
            returnBm = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        } catch (OutOfMemoryError e) {
        }
        if (returnBm == null) {
            returnBm = bitmap;
        }
        if (bitmap != returnBm) {
            bitmap.recycle();
        }
        return returnBm;
    }

    private void initView() {
        mGoodsImg = (ImageView) findViewById(R.id.img_goodsimg);
        mGoodsName = (EditText) findViewById(R.id.et_goods_name);
        mGoodsPrice = (EditText) findViewById(R.id.et_goods_price);
        mGoodsType = (TextView) findViewById(R.id.tv_goods_type);
        mGoodsKucun = (EditText) findViewById(R.id.et_goods_kucun);
        mGoodsStatus = (TextView) findViewById(R.id.tv_goods_status);
        tv_tijiaosp = (TextView) findViewById(R.id.tv_tijiaosp);

        Log.e(TAG, "initView: 00000 imgurl = " + imgurl);
        servicesBean2 = new ArrayList<>();
        servicesBean3 = new ArrayList<>();
        servicesBean4 = new ArrayList<>();
        servicesBean5 = new ArrayList<>();
        servicesBean2a = new ArrayList<>();
        servicesBean3a = new ArrayList<>();
        servicesBean4a = new ArrayList<>();
        servicesBean5a = new ArrayList<>();
        leftTypeList = new ArrayList<>();
    }
}
