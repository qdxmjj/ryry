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
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ruyiruyi.merchant.MainActivity;
import com.ruyiruyi.merchant.R;
import com.ruyiruyi.merchant.bean.FreeChangeNewShoeBean;
import com.ruyiruyi.merchant.db.DbConfig;
import com.ruyiruyi.merchant.ui.activity.base.MerchantBaseActivity;
import com.ruyiruyi.merchant.ui.multiType.PublicShoeFlag;
import com.ruyiruyi.merchant.cell.CircleImageView;
import com.ruyiruyi.merchant.utils.UtilsRY;
import com.ruyiruyi.merchant.utils.UtilsURL;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;
import com.ruyiruyi.rylibrary.cell.ActionBar;
import com.ruyiruyi.rylibrary.image.ImageUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import rx.functions.Action1;

public class OrderConfirmFirstChangeActivity extends MerchantBaseActivity {

    private ActionBar actionBar;
    private String orderNo;
    private String orderType;
    private String whereIn;
    private String select;
    private String storeId;
    private String userName;
    private String userPhone;
    private String platNumber;
    private String storeName;
    //拍照示例
    private TextView tv_carpic_sample;
    //底部接单控件
    private TextView tv_bottom_a;
    private TextView tv_bottom_b;
    private TextView tv_bottom_c;
    // 外层数据控件
    private TextView user_name;
    private TextView user_phone;
    private TextView car_num;
    private TextView store_name;
    private TextView order_num;
    private ImageView user_phone_img;
    // shoeFlag
    private LinearLayout ll_shoe_font;
    private LinearLayout ll_shoe_rear;
    private LinearLayout ll_shoe_consistent;
    private ImageView shoe_img_consistent;
    private TextView shoe_name_consistent;
    private TextView shoe_amount_consistent;
    private ImageView shoe_img_font;
    private TextView shoe_name_font;
    private TextView shoe_amount_font;
    private ImageView shoe_img_rear;
    private TextView shoe_name_rear;
    private TextView shoe_amount_rear;
    //行驶证照片和车辆照片
    private CircleImageView pic_xingshizheng;
    private ImageView pic_xingshizheng_delete;
    private LinearLayout pic_xingshizheng_center;
    private CircleImageView pic_car;
    private ImageView pic_car_delete;
    private LinearLayout pic_car_center;
    //条形码
    private FrameLayout fl_code_a;
    private TextView code_a;
    private FrameLayout fl_code_b;
    private TextView code_b;
    private FrameLayout fl_code_c;
    private TextView code_c;
    private FrameLayout fl_code_d;
    private TextView code_d;


    private String TAG = OrderConfirmFirstChangeActivity.class.getSimpleName();
    private List<PublicShoeFlag> shoeFlagList; //shoeFlagListlist
    private boolean hasPic_license = false;//已拍照拍不可点击标志位
    private boolean hasPic_car = false;//已拍照拍不可点击标志位
    private int currentImage = 0; //区分拍照flag 1 行驶证照片 2 车辆照片 ；
    protected static Uri tempUri;//公共pic Uri
    private final int CHOOSE_PICTURE = 0;
    private final int TAKE_PICTURE = 1;
    private Bitmap licenseBitmap;
    private Bitmap carBitmap;
    private ProgressDialog progressDialog;
    private String path_licenseBitmap = "";
    private String path_carBitmap = "";
    private List<FreeChangeNewShoeBean> newShoeList; //条形码轮胎list
    private String path_;
    private ProgressDialog mainDialog;
    private ScrollView scrollView_;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirm_first_change);
        actionBar = findViewById(R.id.acbar);
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
        //获取传递数据
        orderNo = getIntent().getStringExtra("orderNo");
        orderType = getIntent().getStringExtra("orderType");
        whereIn = getIntent().getStringExtra("whereIn");
        select = getIntent().getStringExtra("select");
        storeId = new DbConfig(getApplicationContext()).getId() + "";

        mainDialog = new ProgressDialog(this);
        scrollView_ = findViewById(R.id.scrollView_);
        showDialogProgress(mainDialog, "订单信息加载中...");
        scrollView_.setVisibility(View.INVISIBLE);

        initView();
        initData();

    }

    private void initData() {
        JSONObject object = new JSONObject();
        try {
            object.put("orderNo", orderNo);
            object.put("orderType", orderType);
            object.put("storeId", storeId);

        } catch (JSONException e) {
        }
        RequestParams params = new RequestParams(UtilsURL.REQUEST_URL + "getStoreOrderInfoByNoAndType");
        params.addBodyParameter("reqJson", object.toString());
        params.addBodyParameter("token", new DbConfig(getApplicationContext()).getToken());
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    Log.e(TAG, "onSuccess: Confirmresult = " + result);
                    JSONObject jsonObject = new JSONObject(result);
                    int status = jsonObject.getInt("status");
                    String msg = jsonObject.getString("msg");
                    if (status == 1) {
                        JSONObject data = jsonObject.getJSONObject("data");
                        userName = data.getString("userName");
                        userPhone = data.getString("userPhone");
                        platNumber = data.getString("platNumber");
                        storeName = data.getString("storeName");
                        JSONArray freeChangeOrderVoList = data.getJSONArray("firstChangeOrderVoList");
                        for (int i = 0; i < freeChangeOrderVoList.length(); i++) {//存取前后胎更换位置数量list数据
                            PublicShoeFlag bean = null;
                            JSONObject objBean = (JSONObject) freeChangeOrderVoList.get(i);
                            String fontRearFlag = objBean.getString("fontRearFlag");
                            if (fontRearFlag.equals("0")) {
                                bean = new PublicShoeFlag();
                                bean.setShoeImgUrl(objBean.getString("shoeImg"));
                                bean.setShoeName(objBean.getString("shoeName"));
                                bean.setOrderType(orderType);
                                bean.setShoeFlag(objBean.getString("fontRearFlag"));
                                bean.setShoeAmount(objBean.getInt("fontAmount") + "");
                                shoeFlagList.add(bean);
                            }
                            if (fontRearFlag.equals("1")) {
                                bean = new PublicShoeFlag();
                                bean.setShoeImgUrl(objBean.getString("shoeImg"));
                                bean.setShoeName(objBean.getString("shoeName"));
                                bean.setOrderType(orderType);
                                bean.setShoeFlag(objBean.getString("fontRearFlag"));
                                bean.setShoeAmount(objBean.getInt("fontAmount") + "");
                                shoeFlagList.add(bean);
                            }
                            if (fontRearFlag.equals("2")) {
                                bean = new PublicShoeFlag();
                                bean.setShoeImgUrl(objBean.getString("shoeImg"));
                                bean.setShoeName(objBean.getString("shoeName"));
                                bean.setOrderType(orderType);
                                bean.setShoeFlag(objBean.getString("fontRearFlag"));
                                bean.setShoeAmount(objBean.getInt("rearAmount") + "");
                                shoeFlagList.add(bean);
                            }
                        }

                        JSONArray userCarShoeBarCodeList = data.getJSONArray("userCarShoeBarCodeList");
                        for (int i = 0; i < userCarShoeBarCodeList.length(); i++) {//存取新轮胎数据
                            FreeChangeNewShoeBean bean = new FreeChangeNewShoeBean();
                            JSONObject objBean = (JSONObject) userCarShoeBarCodeList.get(i);
                            bean.setShoeImgUrl(objBean.getString("shoeImgUrl"));
                            bean.setBarcodeImgUrl(objBean.getString("barcodeImgUrl"));
                            bean.setShoeName(objBean.getString("shoeName"));
                            bean.setOrderType(orderType);
                            bean.setFontRearFlag(objBean.getInt("fontRearFlag") + "");
                            bean.setId(objBean.getInt("id") + "");
                            bean.setBarCode(objBean.getString("barCode"));
                            bean.setOrderNo(objBean.getString("orderNo"));
//                            bean.setTime(objBean.getLong("time"));
                            bean.setStatus(objBean.getInt("status") + "");
                            newShoeList.add(bean);
                        }
                        Log.e(TAG, "onSuccess: " + "onSuccess");

                        //设置数据
                        setData();
                        //绑定监听
                        bindView();

                        hideDialogProgress(mainDialog);
                        scrollView_.setVisibility(View.VISIBLE);


                    } else {
                        Toast.makeText(OrderConfirmFirstChangeActivity.this, msg, Toast.LENGTH_SHORT).show();
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

    private void bindView() {
        //拨打电话监听
        RxViewAction.clickNoDouble(user_phone_img).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                String phoneStr = user_phone.getText().toString();
                //跳转拨打电话页面
                Intent intent = new Intent(Intent.ACTION_DIAL);
                Uri data = Uri.parse("tel:" + phoneStr);
                intent.setData(data);
                startActivity(intent);

            }
        });
        //车辆行驶证照片监听
        RxViewAction.clickNoDouble(pic_xingshizheng_center).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (hasPic_license) {
                    return;
                }
                String code = "";
                currentImage = 1;
                showPicInputDialog(code);
            }
        });
        //车辆拍照监听
        RxViewAction.clickNoDouble(pic_car_center).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (hasPic_car) {
                    return;
                }
                String code = "";
                currentImage = 2;
                showPicInputDialog(code);
            }
        });
        RxViewAction.clickNoDouble(pic_xingshizheng_delete).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                pic_xingshizheng_delete.setVisibility(View.GONE);
                pic_xingshizheng_center.setVisibility(View.VISIBLE);
                pic_xingshizheng.setImageResource(R.drawable.img_bg_dark);
                hasPic_license = false;
            }
        });
        RxViewAction.clickNoDouble(pic_car_delete).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                pic_car_delete.setVisibility(View.GONE);
                pic_car_center.setVisibility(View.VISIBLE);
                pic_car.setImageResource(R.drawable.img_bg_dark);
                hasPic_car = false;
            }
        });
        /*底部订单处理按钮监听*/
        RxViewAction.clickNoDouble(tv_bottom_a).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (judgeBeforeSave("1")) {//serviceType1:商家确认服务
                    showOrderDialog("1");
                }
            }
        });
        RxViewAction.clickNoDouble(tv_bottom_b).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {//serviceType3:拒绝服务
                if (judgeBeforeSave("2")) {
                    showOrderDialog("2");
                }
            }
        });
        RxViewAction.clickNoDouble(tv_bottom_c).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {//serviceType2:客户自提
                if (judgeBeforeSave("3")) {
                    showOrderDialog("3");
                }
            }
        });
        //拍照示例
        RxViewAction.clickNoDouble(tv_carpic_sample).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Intent intent = new Intent(getApplicationContext(), PhotoSampleActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("type", "car");
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

    }


    private void showOrderDialog(final String type) {
        AlertDialog dialog = new AlertDialog.Builder(this).create();
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_error, null);
        final TextView error_text = (TextView) dialogView.findViewById(R.id.error_text);
        switch (type) {
            case "1":
                error_text.setText("确定确认服务吗?");
                break;
            case "2":
                error_text.setText("确定拒绝服务吗?");
                break;
            case "3":
                error_text.setText("确定客户自提吗?");
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
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //动画
                dialog.dismiss();
                showDialogProgress(progressDialog, "数据提交中...");
                switch (type) {
                    case "1":
                        //先处理提交数据
                        initBeforePost();

                        //再请求
                        JSONObject object = new JSONObject();
                        try {
                            object.put("orderNo", orderNo);
                            object.put("serviceType", "1");
                            object.put("orderType", orderType);

                        } catch (JSONException e) {
                        }
                        RequestParams params = new RequestParams(UtilsURL.REQUEST_URL + "storeSelectFirstChangeShoeOrderType");
                        params.addBodyParameter("reqJson", object.toString());
                        params.addBodyParameter("token", new DbConfig(getApplicationContext()).getToken());
                        params.addBodyParameter("drivingLicenseImg", new File(path_licenseBitmap));
                        params.addBodyParameter("carImg", new File(path_carBitmap));
                        Log.e(TAG, "onClick:  params.toString() = " + params.toString());
                        x.http().post(params, new Callback.CommonCallback<String>() {
                            @Override
                            public void onSuccess(String result) {
                                try {
                                    JSONObject jsonObject = new JSONObject(result);
                                    int status = jsonObject.getInt("status");
                                    String msg = jsonObject.getString("msg");
                                    Toast.makeText(OrderConfirmFirstChangeActivity.this, msg, Toast.LENGTH_SHORT).show();
                                    if (status == 1) {
                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                        Bundle bundle = new Bundle();
                                        bundle.putString("page", "order");
                                        intent.putExtras(bundle);
                                        finish();
                                        startActivity(intent);
                                    }


                                } catch (JSONException e) {
                                }
                            }

                            @Override
                            public void onError(Throwable ex, boolean isOnCallback) {
                                Toast.makeText(OrderConfirmFirstChangeActivity.this, "网络异常,请检查网络", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCancelled(CancelledException cex) {

                            }

                            @Override
                            public void onFinished() {
                                hideDialogProgress(progressDialog);
                            }
                        });
                        break;
                    case "2":
                        //拒绝服务
                        JSONObject object2 = new JSONObject();
                        try {
                            object2.put("orderNo", orderNo);
                            object2.put("serviceType", "3");
                            object2.put("orderType", orderType);

                        } catch (JSONException e) {
                        }
                        RequestParams params2 = new RequestParams(UtilsURL.REQUEST_URL + "storeSelectFirstChangeShoeOrderType");
                        params2.addBodyParameter("reqJson", object2.toString());
                        params2.addBodyParameter("token", new DbConfig(getApplicationContext()).getToken());
                        Log.e(TAG, "onClick:  params.toString() = " + params2.toString());
                        x.http().post(params2, new Callback.CommonCallback<String>() {
                            @Override
                            public void onSuccess(String result) {
                                try {
                                    JSONObject jsonObject = new JSONObject(result);
                                    int status = jsonObject.getInt("status");
                                    String msg = jsonObject.getString("msg");
                                    Toast.makeText(OrderConfirmFirstChangeActivity.this, msg, Toast.LENGTH_SHORT).show();
                                    if (status == 1) {
                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                        Bundle bundle = new Bundle();
                                        bundle.putString("page", "order");
                                        intent.putExtras(bundle);
                                        finish();
                                        startActivity(intent);
                                    }


                                } catch (JSONException e) {
                                }
                            }

                            @Override
                            public void onError(Throwable ex, boolean isOnCallback) {
                                Toast.makeText(OrderConfirmFirstChangeActivity.this, "网络异常,请检查网络", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCancelled(CancelledException cex) {

                            }

                            @Override
                            public void onFinished() {
                                hideDialogProgress(progressDialog);
                            }
                        });
                        break;
                    case "3":
                        //客户自提
                        //先处理提交数据
                        initBeforePost();

                        //再请求
                        JSONObject object3 = new JSONObject();
                        try {
                            object3.put("orderNo", orderNo);
                            object3.put("serviceType", "2");
                            object3.put("orderType", orderType);

                        } catch (JSONException e) {
                        }
                        RequestParams params3 = new RequestParams(UtilsURL.REQUEST_URL + "storeSelectFirstChangeShoeOrderType");
                        params3.addBodyParameter("reqJson", object3.toString());
                        params3.addBodyParameter("token", new DbConfig(getApplicationContext()).getToken());
                        params3.addBodyParameter("drivingLicenseImg", new File(path_licenseBitmap));
                        params3.addBodyParameter("carImg", new File(path_carBitmap));
                        Log.e(TAG, "onClick:  params.toString() = " + params3.toString());
                        x.http().post(params3, new Callback.CommonCallback<String>() {
                            @Override
                            public void onSuccess(String result) {
                                try {
                                    JSONObject jsonObject = new JSONObject(result);
                                    int status = jsonObject.getInt("status");
                                    String msg = jsonObject.getString("msg");
                                    Toast.makeText(OrderConfirmFirstChangeActivity.this, msg, Toast.LENGTH_SHORT).show();
                                    if (status == 1) {
                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                        Bundle bundle = new Bundle();
                                        bundle.putString("page", "order");
                                        intent.putExtras(bundle);
                                        finish();
                                        startActivity(intent);
                                    }


                                } catch (JSONException e) {
                                }
                            }

                            @Override
                            public void onError(Throwable ex, boolean isOnCallback) {
                                Toast.makeText(OrderConfirmFirstChangeActivity.this, "网络异常,请检查网络", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCancelled(CancelledException cex) {

                            }

                            @Override
                            public void onFinished() {
                                hideDialogProgress(progressDialog);
                            }
                        });
                        break;
                }
            }
        });
        dialog.show();
        //设置按钮颜色
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.theme_primary));
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.theme_primary));
    }

    private void initBeforePost() {
        //处理照片
        path_licenseBitmap = ImageUtils.savePhoto(licenseBitmap, this.getObbDir().getAbsolutePath(), "licensePic");
        path_carBitmap = ImageUtils.savePhoto(carBitmap, this.getObbDir().getAbsolutePath(), "carPic");
    }

    private boolean judgeBeforeSave(String type) {
        switch (type) {
            case "1"://serviceType1:确认服务
                if (!hasPic_license) {
                    showErrorDialog("请上传行驶证照片!");
                    return false;
                }
                if (!hasPic_car) {
                    showErrorDialog("请上传车辆照片!");
                    return false;
                }
                break;
            case "2"://serviceType3:拒绝服务
                //(无需判断)
                break;
            case "3"://serviceType2:客户自提
                if (!hasPic_license) {
                    showErrorDialog("请上传行驶证照片!");
                    return false;
                }
                if (!hasPic_car) {
                    showErrorDialog("请上传车辆照片!");
                    return false;
                }
                break;
        }

        return true;
    }


    private void showErrorDialog(String error) {
        AlertDialog dialog = new AlertDialog.Builder(this).create();
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_error, null);
        TextView error_text = (TextView) dialogView.findViewById(R.id.error_text);
        error_text.setText(error);
        dialog.setTitle("如意如驿商家版");
        dialog.setIcon(R.drawable.ic_launcher);
        dialog.setView(dialogView);
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show();
        //设置按钮颜色
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.theme_primary));
    }

    private void showPicInputDialog(final String code) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("上传照片");
        String[] items = {"选择本地照片", "拍照"};
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               /* takePicture(code);*/

                switch (which) {
                    case CHOOSE_PICTURE://选择本地照片
                        Intent openBendiPicIntent = new Intent(Intent.ACTION_GET_CONTENT);
                        openBendiPicIntent.setType("image/*");
                        startActivityForResult(openBendiPicIntent, CHOOSE_PICTURE);
                        break;
                    case TAKE_PICTURE://拍照
                        takePicture(code);
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

    private void takePicture(String code) {
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
        if (currentImage == 1) {
            file = new File(this.getObbDir().getAbsolutePath(), "license" + code + ".jpg");
            path_ = file.getPath();
        } else if (currentImage == 2) {
            file = new File(this.getObbDir().getAbsolutePath(), "car" + code + ".jpg");
            path_ = file.getPath();
        }

        //判断是否是AndroidN以及更高的版本
        if (Build.VERSION.SDK_INT >= 24) {
            openCameraIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            tempUri = FileProvider.getUriForFile(getApplicationContext(), "com.ruyiruyi.merchant.fileProvider", file);
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
                /*case TAKE_PICTURE:
                    setImageToViewFromPhone(tempUri);
                    break;*/

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
        /*int degree = ImageUtils.readPictureDegree(path_);*/

        int degree = 0;
        if (isCamera) {
            degree = ImageUtils.readPictureDegree(path_);
        } else {
            degree = ImageUtils.getOrientation(getApplicationContext(), uri);
        }

        if (uri != null) {
            Bitmap photo = null;
            try {
                photo = ImageUtils.getBitmapFormUri(getApplicationContext(), uri);
            } catch (IOException e) {
            }
            if (currentImage == 1) {
                Log.e(TAG, "setImageToViewFromPhone  licenseBitmap: ");
                licenseBitmap = rotaingImageView(degree, photo);
                pic_xingshizheng.setImageBitmap(licenseBitmap);
                pic_xingshizheng_delete.setVisibility(View.VISIBLE);
                pic_xingshizheng_center.setVisibility(View.GONE);
                hasPic_license = true;
            } else if (currentImage == 2) {
                Log.e(TAG, "setImageToViewFromPhone  carBitmap: ");
                carBitmap = rotaingImageView(degree, photo);
                pic_car.setImageBitmap(carBitmap);
                pic_car_delete.setVisibility(View.VISIBLE);
                pic_car_center.setVisibility(View.GONE);
                hasPic_car = true;
            }
        }
    }

    public static Bitmap rotaingImageView(int angle, Bitmap bitmap) {
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

    private void setData() {
        //设置外层数据
        user_name.setText(userName);
        user_phone.setText(userPhone);
        car_num.setText(platNumber);
        store_name.setText(storeName);
        order_num.setText(orderNo);
        //设置内层数据shoeFlag
        setShoeFlagData();
        //设置条形码newShoeList
        setShoeBarCodeData();

    }

    private void setShoeBarCodeData() {
        //初始化 全部隐藏
        fl_code_a.setVisibility(View.GONE);
        fl_code_b.setVisibility(View.GONE);
        fl_code_c.setVisibility(View.GONE);
        fl_code_d.setVisibility(View.GONE);

        //根据个数显示
        if (newShoeList.size() >= 1) {
            fl_code_a.setVisibility(View.VISIBLE);
            code_a.setText(newShoeList.get(0).getBarCode());
        }
        if (newShoeList.size() >= 2) {
            fl_code_b.setVisibility(View.VISIBLE);
            code_b.setText(newShoeList.get(1).getBarCode());
        }
        if (newShoeList.size() >= 3) {
            fl_code_c.setVisibility(View.VISIBLE);
            code_c.setText(newShoeList.get(2).getBarCode());
        }
        if (newShoeList.size() >= 4) {
            fl_code_d.setVisibility(View.VISIBLE);
            code_d.setText(newShoeList.get(3).getBarCode());
        }
    }

    /*  onDestroy()
* 判断删除带有条形码的图片
* */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //根据Path删除转换的照片
        if (path_licenseBitmap != null && path_licenseBitmap.length() != 0) {
            UtilsRY.deleteImage(path_licenseBitmap, getApplicationContext());
            Log.e(TAG, "onDestroy: path_licenseBitmap");
        }
        if (path_carBitmap != null && path_carBitmap.length() != 0) {
            UtilsRY.deleteImage(path_carBitmap, getApplicationContext());
            Log.e(TAG, "onDestroy: path_carBitmap");
        }
    }


    private void setShoeFlagData() {
        if (shoeFlagList.size() == 2) {
            ll_shoe_font.setVisibility(View.VISIBLE);
            ll_shoe_rear.setVisibility(View.VISIBLE);
            ll_shoe_consistent.setVisibility(View.GONE);
            for (int i = 0; i < shoeFlagList.size(); i++) {
                PublicShoeFlag bean = shoeFlagList.get(i);
                if (shoeFlagList.get(i).getShoeFlag().equals("1")) {
                    shoe_name_font.setText(bean.getShoeName());
                    shoe_amount_font.setText(bean.getShoeAmount());
                    Glide.with(getApplicationContext()).load(bean.getShoeImgUrl()).into(shoe_img_font);
                }
                if (shoeFlagList.get(i).getShoeFlag().equals("2")) {
                    shoe_name_rear.setText(bean.getShoeName());
                    shoe_amount_rear.setText(bean.getShoeAmount());
                    Glide.with(getApplicationContext()).load(bean.getShoeImgUrl()).into(shoe_img_rear);
                }
            }
        }
        if (shoeFlagList.size() == 1) {
            PublicShoeFlag bean = shoeFlagList.get(0);
            if (bean.getShoeFlag().equals("0")) {
                ll_shoe_font.setVisibility(View.GONE);
                ll_shoe_rear.setVisibility(View.GONE);
                ll_shoe_consistent.setVisibility(View.VISIBLE);
                shoe_name_consistent.setText(bean.getShoeName());
                shoe_amount_consistent.setText(bean.getShoeAmount());
                Glide.with(getApplicationContext()).load(bean.getShoeImgUrl()).into(shoe_img_consistent);
            }
            if (bean.getShoeFlag().equals("1")) {
                ll_shoe_font.setVisibility(View.VISIBLE);
                ll_shoe_rear.setVisibility(View.GONE);
                ll_shoe_consistent.setVisibility(View.GONE);
                shoe_name_font.setText(bean.getShoeName());
                shoe_amount_font.setText(bean.getShoeAmount());
                Glide.with(getApplicationContext()).load(bean.getShoeImgUrl()).into(shoe_img_font);
            }
            if (bean.getShoeFlag().equals("2")) {
                ll_shoe_font.setVisibility(View.GONE);
                ll_shoe_rear.setVisibility(View.VISIBLE);
                ll_shoe_consistent.setVisibility(View.GONE);
                shoe_name_rear.setText(bean.getShoeName());
                shoe_amount_rear.setText(bean.getShoeAmount());
                Glide.with(getApplicationContext()).load(bean.getShoeImgUrl()).into(shoe_img_rear);
            }

        }
    }

    private void initView() {
        user_name = findViewById(R.id.user_name);
        user_phone = findViewById(R.id.user_phone);
        car_num = findViewById(R.id.car_num);
        store_name = findViewById(R.id.store_name);
        order_num = findViewById(R.id.order_num);
        user_phone_img = findViewById(R.id.user_phone_img);
        //拍照示例
        tv_carpic_sample = findViewById(R.id.tv_carpic_sample);
        //底部接单控件
        tv_bottom_a = findViewById(R.id.tv_bottom_a);
        tv_bottom_b = findViewById(R.id.tv_bottom_b);
        tv_bottom_c = findViewById(R.id.tv_bottom_c);
        //shoeFlag
        ll_shoe_font = findViewById(R.id.ll_shoe_font);
        ll_shoe_rear = findViewById(R.id.ll_shoe_rear);
        ll_shoe_consistent = findViewById(R.id.ll_shoe_consistent);
        shoe_img_consistent = findViewById(R.id.shoe_img_consistent);
        shoe_name_consistent = findViewById(R.id.shoe_name_consistent);
        shoe_amount_consistent = findViewById(R.id.shoe_amount_consistent);
        shoe_img_font = findViewById(R.id.shoe_img_font);
        shoe_name_font = findViewById(R.id.shoe_name_font);
        shoe_amount_font = findViewById(R.id.shoe_amount_font);
        shoe_img_rear = findViewById(R.id.shoe_img_rear);
        shoe_name_rear = findViewById(R.id.shoe_name_rear);
        shoe_amount_rear = findViewById(R.id.shoe_amount_rear);
        //行驶证照片和车辆照片
        pic_xingshizheng = findViewById(R.id.pic_xingshizheng);
        pic_xingshizheng_delete = findViewById(R.id.pic_xingshizheng_delete);
        pic_xingshizheng_center = findViewById(R.id.pic_xingshizheng_center);
        pic_car = findViewById(R.id.pic_car);
        pic_car_delete = findViewById(R.id.pic_car_delete);
        pic_car_center = findViewById(R.id.pic_car_center);
        //条形码
        fl_code_a = findViewById(R.id.fl_code_a);
        code_a = findViewById(R.id.code_a);
        fl_code_b = findViewById(R.id.fl_code_b);
        code_b = findViewById(R.id.code_b);
        fl_code_c = findViewById(R.id.fl_code_c);
        code_c = findViewById(R.id.code_c);
        fl_code_d = findViewById(R.id.fl_code_d);
        code_d = findViewById(R.id.code_d);


        shoeFlagList = new ArrayList<>();
        newShoeList = new ArrayList<>();
        progressDialog = new ProgressDialog(OrderConfirmFirstChangeActivity.this);
    }


    /*
    * 重写回退键监听
    * */
    @Override
    public void onBackPressed() {
        Log.e(TAG, "   before  onBackPressed: ");
        Intent intent = new Intent();
        Bundle bundle = new Bundle();

        if (whereIn.equals("MainOrderItem")) {
            intent.setClass(getApplicationContext(), MainActivity.class);
            Log.e(TAG, "onBackPressed: MainOrderItem");
            bundle.putString("page", "order");
        }
        if (whereIn.equals("MyOrderItem")) {
            intent.setClass(getApplicationContext(), MyOrderActivity.class);
            Log.e(TAG, "onBackPressed: MyOrderItem");
            intent.putExtra("page", select);
            intent.putExtra("typestate", "all");
        }
        if (whereIn.equals("MainOrderTop")) {
            intent.setClass(getApplicationContext(), MyOrderActivity.class);
            Log.e(TAG, "onBackPressed: MainOrderTop");
            intent.putExtra("page", select);
            intent.putExtra("typestate", "pingtai");
        }


        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }
}
