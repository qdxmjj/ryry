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

import com.baidu.ocr.sdk.OCR;
import com.baidu.ocr.sdk.OnResultListener;
import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.sdk.model.AccessToken;
import com.baidu.ocr.ui.camera.CameraActivity;
import com.ruyiruyi.merchant.MainActivity;
import com.ruyiruyi.merchant.R;
import com.ruyiruyi.merchant.bean.RepairAmount;
import com.ruyiruyi.merchant.bean.ShoeRepair;
import com.ruyiruyi.merchant.cell.CircleImageView;
import com.ruyiruyi.merchant.db.DbConfig;
import com.ruyiruyi.merchant.ui.activity.base.MerchantBaseActivity;
import com.ruyiruyi.merchant.ui.multiType.PublicShoeFlag;
import com.ruyiruyi.merchant.ui.service.RecognizeService;
import com.ruyiruyi.merchant.utils.FileUtil;
import com.ruyiruyi.merchant.utils.UtilsRY;
import com.ruyiruyi.merchant.utils.UtilsURL;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;
import com.ruyiruyi.rylibrary.cell.ActionBar;
import com.ruyiruyi.rylibrary.cell.AmountView;
import com.ruyiruyi.rylibrary.image.ImageUtils;
import com.ruyiruyi.rylibrary.utils.RyLiaTransparentDialog;

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


public class OrderConfirmTireRepairActivity extends MerchantBaseActivity {
    private final int CHOOSE_PICTURE = 0;
    private final int TAKE_PICTURE = 1;
    //底部接单控件
    private TextView tv_bottom_a;
    private TextView tv_bottom_c;
    // 外层数据控件
    private TextView user_name;
    private TextView user_phone;
    private TextView car_num;
    private TextView store_name;
    private TextView order_num;
    private ImageView user_phone_img;
    // shoeFlag(删)
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
    //  轮胎照片 上方BarCode
    private TextView pic_a_titleno;
    private TextView pic_b_titleno;
    private TextView pic_c_titleno;
    private TextView pic_d_titleno;
    //  轮胎照片
    private LinearLayout ll_pic_a;
    private CircleImageView pic_a_left;
    private ImageView pic_a_left_delete;
    private LinearLayout pic_a_left_center;
    private CircleImageView pic_a_right;
    private ImageView pic_a_right_delete;
    private LinearLayout pic_a_right_center;

    private LinearLayout ll_pic_b;
    private CircleImageView pic_b_left;
    private ImageView pic_b_left_delete;
    private LinearLayout pic_b_left_center;
    private CircleImageView pic_b_right;
    private ImageView pic_b_right_delete;
    private LinearLayout pic_b_right_center;

    private LinearLayout ll_pic_c;
    private CircleImageView pic_c_left;
    private ImageView pic_c_left_delete;
    private LinearLayout pic_c_left_center;
    private CircleImageView pic_c_right;
    private ImageView pic_c_right_delete;
    private LinearLayout pic_c_right_center;

    private LinearLayout ll_pic_d;
    private CircleImageView pic_d_left;
    private ImageView pic_d_left_delete;
    private LinearLayout pic_d_left_center;
    private CircleImageView pic_d_right;
    private ImageView pic_d_right_delete;
    private LinearLayout pic_d_right_center;
    //行驶证照片和车辆照片
    private CircleImageView pic_xingshizheng;
    private ImageView pic_xingshizheng_delete;
    private LinearLayout pic_xingshizheng_center;

    private CircleImageView pic_car;
    private ImageView pic_car_delete;
    private LinearLayout pic_car_center;

    //拍照示例
    private TextView tv_shoepic_sample;
    private TextView tv_licencepic_sample;
    private TextView tv_carpic_sample;

    //补胎编号以及修补个数
    private FrameLayout fl_repair_a_;
    private TextView code_repair_a_;
    private AmountView repair_num_a_;

    private FrameLayout fl_repair_b_;
    private TextView code_repair_b_;
    private AmountView repair_num_b_;

    private FrameLayout fl_repair_c_;
    private TextView code_repair_c_;
    private AmountView repair_num_c_;

    private FrameLayout fl_repair_d_;
    private TextView code_repair_d_;
    private AmountView repair_num_d_;


    private ActionBar actionBar;
    private String storeId;
    private String orderType;
    private String whereIn;
    private String select;
    private String orderNo;
    private String userName;
    private String userPhone;
    private String platNumber;
    private String storeName;
    private int userCarId;

    private List<PublicShoeFlag> shoeFlagList; //shoeFlagListlist(删)
    private List<ShoeRepair> shoeRepairList;
    protected static Uri tempUri;//公共pic Uri
    private boolean hasPic_a_left = false;//已拍照拍不可点击标志位
    private boolean hasPic_a_right = false;//已拍照拍不可点击标志位
    private boolean hasPic_b_left = false;//已拍照拍不可点击标志位
    private boolean hasPic_b_right = false;//已拍照拍不可点击标志位
    private boolean hasPic_c_left = false;//已拍照拍不可点击标志位
    private boolean hasPic_c_right = false;//已拍照拍不可点击标志位
    private boolean hasPic_d_left = false;//已拍照拍不可点击标志位
    private boolean hasPic_d_right = false;//已拍照拍不可点击标志位
    private boolean hasPic_license = false;//已拍照拍不可点击标志位
    private boolean hasPic_car = false;//已拍照拍不可点击标志位
    private String TAG = OrderConfirmTireRepairActivity.class.getSimpleName();
    private ProgressDialog progressDialog;
    private ProgressDialog mainDialog;
    private ScrollView scrollView_;
    private final int maxRepairNum = 3;  //预设轮胎最大修补次数
    private String maxErrorMsg = "此轮胎修补次数已达上限!";//预设轮胎最大修补次数提示
    private int currentCount_a_ = 0;// 默认0商家未增加补胎数量  >0已增加修补数
    private int currentCount_b_ = 0;
    private int currentCount_c_ = 0;
    private int currentCount_d_ = 0;
    private int oldCount_a_ = 0;
    private int oldCount_b_ = 0;
    private int oldCount_c_ = 0;
    private int oldCount_d_ = 0;
    private int currentImage = 0;

    private String path_ = "";
    /*提交参数*/
    private Bitmap shoeAbitmap;//10张照片
    private Bitmap shoeACodebitmap;
    private Bitmap shoeBbitmap;
    private Bitmap shoeBCodebitmap;
    private Bitmap shoeCbitmap;
    private Bitmap shoeCCodebitmap;
    private Bitmap shoeDbitmap;
    private Bitmap shoeDCodebitmap;
    private Bitmap licenseBitmap;
    private Bitmap carBitmap;
    private String path_licenseBitmap = "";
    private String path_carBitmap = "";
    private String path_shoeABitmap = "";
    private String path_shoeABarcodeBitmap = "";
    private String path_shoeBBitmap = "";
    private String path_shoeBBarcodeBitmap = "";
    private String path_shoeCBitmap = "";
    private String path_shoeCBarcodeBitmap = "";
    private String path_shoeDBitmap = "";
    private String path_shoeDBarcodeBitmap = "";
    private List<RepairAmount> repairAmountList;

    private static final int REQUEST_CODE_VEHICLE_LICENSE = 120;  //行驶证证识别 //TODO
    private FrameLayout xsz_prove_layout;
    private ImageView prove_icon;
    private TextView xsz_prove;
    private int proveStatus = 2; // 1 已认证 2 未认证（默认）
    private RyLiaTransparentDialog ryTransparentDialog;
    private String drivingLicenseTime = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirm_tire_repair);
        actionBar = findViewById(R.id.open_order_freechange_acbar);
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

        ryTransparentDialog = new RyLiaTransparentDialog(this, "      未认证用户不影响轮胎和商品的购买，免费认证后可以享受特价洗车和一分钱补胎服务以及如驿如意赠送的其他服务");
        //TODO 行驶证识别调用initAccessToken方法，初始化OCR单例
        OCR.getInstance(this).initAccessToken(new OnResultListener<AccessToken>() {
            @Override
            public void onResult(AccessToken result) {
                // 调用成功，返回AccessToken对象
                String token = result.getAccessToken();
            }

            @Override
            public void onError(OCRError error) {
                // 调用失败，返回OCRError子类SDKError对象
            }
        }, getApplicationContext());

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
        Log.e(TAG, "initData: params.toString() = " + params.toString());
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
                        userCarId = data.getInt("userCarId");
                        proveStatus = data.getInt("authenticatedState");//TODO
                        JSONArray userCarShoeOldBarCodeList = data.getJSONArray("userCarShoeOldBarCodeList");
                        for (int i = 0; i < userCarShoeOldBarCodeList.length(); i++) {
                            JSONObject objBean = (JSONObject) userCarShoeOldBarCodeList.get(i);
                            ShoeRepair bean = new ShoeRepair();
                            bean.setBarCode(objBean.getString("barCode"));
                            bean.setFontRearFlag(objBean.getInt("fontRearFlag") + "");
                            bean.setId(objBean.getInt("id") + "");
                            bean.setIsReach5Years(objBean.getInt("isReach5Years") + "");
                            bean.setOrderNo(objBean.getString("orderNo"));
                            bean.setPrice(objBean.getInt("price") + "");
                            bean.setRepairAmount(objBean.getInt("repairAmount") + "");
                            bean.setShoeId(objBean.getInt("shoeId") + "");
                            bean.setShoeImgUrl(objBean.getString("shoeImgUrl"));
                            bean.setShoeName(objBean.getString("shoeName"));
                            bean.setStage(objBean.getInt("stage") + "");
                            bean.setUserCarId(objBean.getInt("userCarId") + "");
                            bean.setUserId(objBean.getInt("userId") + "");
                            bean.setTime(objBean.getLong("time"));
                            shoeRepairList.add(bean);
                        }

                        //设置数据
                        setData();
                        //绑定监听
                        bindView();

                        hideDialogProgress(mainDialog);
                        scrollView_.setVisibility(View.VISIBLE);


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

        //行驶证认证  //TODO
        RxViewAction.clickNoDouble(xsz_prove).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (proveStatus == 1) {
                    Toast.makeText(OrderConfirmTireRepairActivity.this, "该车辆已认证", Toast.LENGTH_SHORT).show();
                    return;
                }

                //TODO 行驶证识别拍摄
                // 行驶证识别参数设置
                Intent intent = new Intent(getApplicationContext(), CameraActivity.class);
                // 设置临时存储
                intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH,
                        FileUtil.getSaveFile(getApplication()).getAbsolutePath());
                intent.putExtra(CameraActivity.KEY_CONTENT_TYPE,
                        CameraActivity.CONTENT_TYPE_GENERAL);
                startActivityForResult(intent, REQUEST_CODE_VEHICLE_LICENSE);

            }
        });
        //认证问号提示//TODO
        RxViewAction.clickNoDouble(prove_icon).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {

                showDialogProgress(ryTransparentDialog, "");
            }
        });

        /* <--轮胎照片拍照监听 */
        RxViewAction.clickNoDouble(pic_a_left_center).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (hasPic_a_left) {
                    return;
                }
                currentImage = 1;
                showPicInputDialog();
            }
        });
        RxViewAction.clickNoDouble(pic_a_right_center).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (hasPic_a_right) {
                    return;
                }
                currentImage = 2;
                showPicInputDialog();
            }
        });
        RxViewAction.clickNoDouble(pic_b_left_center).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (hasPic_b_left) {
                    return;
                }
                currentImage = 3;
                showPicInputDialog();
            }
        });
        RxViewAction.clickNoDouble(pic_b_right_center).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (hasPic_b_right) {
                    return;
                }
                currentImage = 4;
                showPicInputDialog();
            }
        });
        RxViewAction.clickNoDouble(pic_c_left_center).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (hasPic_c_left) {
                    return;
                }
                currentImage = 5;
                showPicInputDialog();
            }
        });
        RxViewAction.clickNoDouble(pic_c_right_center).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (hasPic_c_right) {
                    return;
                }
                currentImage = 6;
                showPicInputDialog();
            }
        });
        RxViewAction.clickNoDouble(pic_d_left_center).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (hasPic_d_left) {
                    return;
                }
                currentImage = 7;
                showPicInputDialog();
            }
        });
        RxViewAction.clickNoDouble(pic_d_right_center).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (hasPic_d_right) {
                    return;
                }
                currentImage = 8;
                showPicInputDialog();
            }
        });
        //车辆行驶证照片监听
        RxViewAction.clickNoDouble(pic_xingshizheng_center).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (hasPic_license) {
                    return;
                }
                currentImage = 9;
                showPicInputDialog();
            }
        });
        //车辆拍照监听
        RxViewAction.clickNoDouble(pic_car_center).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (hasPic_car) {
                    return;
                }
                currentImage = 10;
                showPicInputDialog();
            }
        });
        //删除照片
        RxViewAction.clickNoDouble(pic_a_left_delete).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                pic_a_left_delete.setVisibility(View.GONE);
                pic_a_left_center.setVisibility(View.VISIBLE);
                pic_a_left.setImageResource(R.drawable.img_bg_dark);
                hasPic_a_left = false;
            }
        });
        RxViewAction.clickNoDouble(pic_a_right_delete).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                pic_a_right_delete.setVisibility(View.GONE);
                pic_a_right_center.setVisibility(View.VISIBLE);
                pic_a_right.setImageResource(R.drawable.img_bg_dark);
                hasPic_a_right = false;
            }
        });
        RxViewAction.clickNoDouble(pic_b_left_delete).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                pic_b_left_delete.setVisibility(View.GONE);
                pic_b_left_center.setVisibility(View.VISIBLE);
                pic_b_left.setImageResource(R.drawable.img_bg_dark);
                hasPic_b_left = false;
            }
        });
        RxViewAction.clickNoDouble(pic_b_right_delete).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                pic_b_right_delete.setVisibility(View.GONE);
                pic_b_right_center.setVisibility(View.VISIBLE);
                pic_b_right.setImageResource(R.drawable.img_bg_dark);
                hasPic_b_right = false;
            }
        });
        RxViewAction.clickNoDouble(pic_c_left_delete).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                pic_c_left_delete.setVisibility(View.GONE);
                pic_c_left_center.setVisibility(View.VISIBLE);
                pic_c_left.setImageResource(R.drawable.img_bg_dark);
                hasPic_c_left = false;
            }
        });
        RxViewAction.clickNoDouble(pic_c_right_delete).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                pic_c_right_delete.setVisibility(View.GONE);
                pic_c_right_center.setVisibility(View.VISIBLE);
                pic_c_right.setImageResource(R.drawable.img_bg_dark);
                hasPic_c_right = false;
            }
        });
        RxViewAction.clickNoDouble(pic_d_left_delete).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                pic_d_left_delete.setVisibility(View.GONE);
                pic_d_left_center.setVisibility(View.VISIBLE);
                pic_d_left.setImageResource(R.drawable.img_bg_dark);
                hasPic_d_left = false;
            }
        });
        RxViewAction.clickNoDouble(pic_d_right_delete).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                pic_d_right_delete.setVisibility(View.GONE);
                pic_d_right_center.setVisibility(View.VISIBLE);
                pic_d_right.setImageResource(R.drawable.img_bg_dark);
                hasPic_d_right = false;
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
        //拍照示例
        RxViewAction.clickNoDouble(tv_shoepic_sample).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Intent intent = new Intent(getApplicationContext(), PhotoSampleActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("type", "xiubu");
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
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
          /*底部订单处理按钮监听*/
        //确认服务
        RxViewAction.clickNoDouble(tv_bottom_a).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (judgeBeforeSave("1")) {
                    if (proveStatus == 2) {//TODO
                        Toast.makeText(OrderConfirmTireRepairActivity.this, "请先进行车辆认证", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    showOrderDialog("1");
                }
            }
        });
        //拒绝服务 (已删)
        RxViewAction.clickNoDouble(tv_bottom_c).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (judgeBeforeSave("3")) {
                    showOrderDialog("3");
                }
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
            case "3":
                error_text.setText("确定拒绝服务吗?");
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
                        RequestParams params = new RequestParams(UtilsURL.REQUEST_URL + "storeSelectShoeRepairOrderType");
                        params.addBodyParameter("reqJson", object.toString());
                        params.addBodyParameter("repairBarCodeList", repairAmountList.toString());
                        params.addBodyParameter("token", new DbConfig(getApplicationContext()).getToken());
                        /*params.addBodyParameter("drivingLicenseImg", new File(path_licenseBitmap));*///TODO
                        params.addBodyParameter("drivingLicenseTime", drivingLicenseTime);//TODO
                        params.addBodyParameter("carImg", new File(path_carBitmap));
                        if (hasPic_a_left) {
                            params.addBodyParameter("shoe1Img", new File(path_shoeABitmap));
                        }
                        if (hasPic_a_right) {
                            params.addBodyParameter("shoe1BarCodeImg", new File(path_shoeABarcodeBitmap));
                        }
                        if (hasPic_b_left) {
                            params.addBodyParameter("shoe2Img", new File(path_shoeBBitmap));
                        }
                        if (hasPic_b_right) {
                            params.addBodyParameter("shoe2BarCodeImg", new File(path_shoeBBarcodeBitmap));
                        }
                        if (hasPic_c_left) {
                            params.addBodyParameter("shoe3Img", new File(path_shoeCBitmap));
                        }
                        if (hasPic_c_right) {
                            params.addBodyParameter("shoe3BarCodeImg", new File(path_shoeCBarcodeBitmap));
                        }
                        if (hasPic_d_left) {
                            params.addBodyParameter("shoe4Img", new File(path_shoeDBitmap));
                        }
                        if (hasPic_d_right) {
                            params.addBodyParameter("shoe4BarCodeImg", new File(path_shoeDBarcodeBitmap));
                        }
                        Log.e(TAG, "onClick:  submit params.toString() = " + params.toString());
                        x.http().post(params, new Callback.CommonCallback<String>() {
                            @Override
                            public void onSuccess(String result) {
                                try {
                                    JSONObject jsonObject = new JSONObject(result);
                                    int status = jsonObject.getInt("status");
                                    String msg = jsonObject.getString("msg");
                                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
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
//                                Toast.makeText(getApplicationContext(), "网络异常,请检查网络", Toast.LENGTH_SHORT).show();
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
                        //拒绝
                        JSONObject object3 = new JSONObject();
                        try {
                            object3.put("orderNo", orderNo);
                            object3.put("serviceType", "3");
                            object3.put("orderType", orderType);

                        } catch (JSONException e) {
                        }
                        RequestParams params3 = new RequestParams(UtilsURL.REQUEST_URL + "storeSelectShoeRepairOrderType");
                        params3.addBodyParameter("reqJson", object3.toString());
                        params3.addBodyParameter("token", new DbConfig(getApplicationContext()).getToken());
                        x.http().post(params3, new Callback.CommonCallback<String>() {
                            @Override
                            public void onSuccess(String result) {
                                try {
                                    JSONObject jsonObject = new JSONObject(result);
                                    int status = jsonObject.getInt("status");
                                    String msg = jsonObject.getString("msg");
                                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
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
//                                Toast.makeText(getApplicationContext(), "网络异常,请检查网络", Toast.LENGTH_SHORT).show();
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

    private boolean judgeBeforeSave(String type) {
        switch (type) {
            case "1"://确认服务
                if (currentCount_a_ == 0 && currentCount_b_ == 0 && currentCount_c_ == 0 && currentCount_d_ == 0) {
                    showErrorDialog("您还没有添加补胎数量!");
                    return false;
                }
                if ((currentCount_a_ > 0 && !hasPic_a_left) || (currentCount_b_ > 0 && !hasPic_b_left) || (currentCount_c_ > 0 && !hasPic_c_left) || (currentCount_d_ > 0 && !hasPic_d_left)) {
                    showErrorDialog("请补全轮胎条正面照!");
                    return false;
                }
                if ((currentCount_a_ > 0 && !hasPic_a_right) || (currentCount_b_ > 0 && !hasPic_b_right) || (currentCount_c_ > 0 && !hasPic_c_right) || (currentCount_d_ > 0 && !hasPic_d_right)) {
                    showErrorDialog("请补全轮胎条形码特写照!");
                    return false;
                }
                /*if (!hasPic_license) {//TODO
                    showErrorDialog("请上传行驶证照片!");
                    return false;
                }*/
                if (!hasPic_car) {
                    showErrorDialog("请上传车辆照片!");
                    return false;
                }
                break;
            case "2"://补差服务
                /*暂无*/
                break;
            case "3"://拒绝服务
                //(无需判断)
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

    private void showPicInputDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("上传照片");
        String[] items = {"选择本地照片", "拍照"};
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                 /* takePicture();*/

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
        if (currentImage == 1) {
            file = new File(this.getObbDir().getAbsolutePath(), "shoea.jpg");
            path_ = file.getPath();
        } else if (currentImage == 2) {
            file = new File(this.getObbDir().getAbsolutePath(), "shoeacode.jpg");
            path_ = file.getPath();
        } else if (currentImage == 3) {
            file = new File(this.getObbDir().getAbsolutePath(), "shoeb.jpg");
            path_ = file.getPath();
        } else if (currentImage == 4) {
            file = new File(this.getObbDir().getAbsolutePath(), "shoebcode.jpg");
            path_ = file.getPath();
        } else if (currentImage == 5) {
            file = new File(this.getObbDir().getAbsolutePath(), "shoec.jpg");
            path_ = file.getPath();
        } else if (currentImage == 6) {
            file = new File(this.getObbDir().getAbsolutePath(), "shoeccode.jpg");
            path_ = file.getPath();
        } else if (currentImage == 7) {
            file = new File(this.getObbDir().getAbsolutePath(), "shoed.jpg");
            path_ = file.getPath();
        } else if (currentImage == 8) {
            file = new File(this.getObbDir().getAbsolutePath(), "shoedcode.jpg");
            path_ = file.getPath();
        } else if (currentImage == 9) {
            file = new File(this.getObbDir().getAbsolutePath(), "license.jpg");
            path_ = file.getPath();
        } else if (currentImage == 10) {
            file = new File(this.getObbDir().getAbsolutePath(), "car.jpg");
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
                case REQUEST_CODE_VEHICLE_LICENSE://TODO
                    RecognizeService.recVehicleLicense(this, FileUtil.getSaveFile(getApplicationContext()).getAbsolutePath(),
                            new RecognizeService.ServiceListener() {
                                @Override
                                public void onResult(String result) {
                                    Log.e(TAG, "onResult: 行驶证认证回调" + result);
                                    try {
                                        JSONObject tokenObj = new JSONObject(result);
                                        JSONObject words_result = tokenObj.getJSONObject("words_result");
                                        JSONObject obj = words_result.getJSONObject("号牌号码");
                                        String provePlantnumber = obj.getString("words");

                                        JSONObject obj_date = words_result.getJSONObject("注册日期");
                                        String register_date = obj_date.getString("words");
                                        if (register_date == null || register_date.length() == 0 || register_date.length() < 8) {
                                            Toast.makeText(OrderConfirmTireRepairActivity.this, "行驶证认证异常，请保证行驶证信息拍摄完整", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                        drivingLicenseTime = register_date.substring(0, 4) + "-" + register_date.substring(4, 6) + "-" + register_date.substring(6, 8);
                                        //TODO 用户比对确认处理
                                        Log.e(TAG, "onResult: provePlantnumber = " + provePlantnumber + " register_date = " + register_date);
                                        if (platNumber != null && platNumber.equals(provePlantnumber)) {
                                            //TODO POST
                                            RequestParams params = new RequestParams(UtilsURL.REQUEST_URL + "userCarInfo/updateAuthenticatedState");// TODO POST
                                            params.addBodyParameter("id", userCarId + "");
                                            params.addBodyParameter("drivingLicenseDateStr", drivingLicenseTime);
                                            params.addBodyParameter("authenticatedState", "1");
                                            Log.e(TAG, "onResult: 认证 params.toString() = " + params.toString());
                                            x.http().post(params, new Callback.CommonCallback<String>() {
                                                @Override
                                                public void onSuccess(String result) {
                                                    Log.e(TAG, "onSuccess: 认证 result = " + result);
                                                    try {
                                                        JSONObject jsonObject = new JSONObject(result);
                                                        int status = jsonObject.getInt("status");
                                                        String msg = jsonObject.getString("msg");
                                                        Toast.makeText(OrderConfirmTireRepairActivity.this, msg, Toast.LENGTH_SHORT).show();
                                                        if (status == 1) {//成功
                                                            //认证成功
                                                            Toast.makeText(OrderConfirmTireRepairActivity.this, "认证成功!", Toast.LENGTH_SHORT).show();
                                                            ///切换成已认证状态
                                                            proveStatus = 1;//是否进行车主认证 (1 已认证 2 未认证)
                                                            xsz_prove.setText("已认证");
                                                            xsz_prove.setTextColor(getResources().getColor(R.color.c22));
                                                            xsz_prove.setClickable(false);//本次不可再次认证
                                                        }

                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                }

                                                @Override
                                                public void onError(Throwable ex, boolean isOnCallback) {
                                                    Log.e(TAG, "onError: 认证 ex.toString() = " + ex.toString());
                                                    Toast.makeText(OrderConfirmTireRepairActivity.this, "认证失败，请检查网络", Toast.LENGTH_SHORT).show();
                                                }

                                                @Override
                                                public void onCancelled(CancelledException cex) {

                                                }

                                                @Override
                                                public void onFinished() {

                                                }
                                            });

                                        } else {
                                            //认证失败
                                            Toast.makeText(OrderConfirmTireRepairActivity.this, "行驶证认证失败，请检查车牌号", Toast.LENGTH_SHORT).show();
                                        }

                                    } catch (JSONException e) {
                                        Toast.makeText(OrderConfirmTireRepairActivity.this, "行驶证认证异常，请保证行驶证信息拍摄完整", Toast.LENGTH_SHORT).show();
                                        e.printStackTrace();
                                    }
                                }
                            });
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
                Log.e(TAG, "setImageToViewFromPhone  shoeAbitmap: ");
                shoeAbitmap = rotaingImageView(degree, photo);
                pic_a_left.setImageBitmap(shoeAbitmap);
                pic_a_left_delete.setVisibility(View.VISIBLE);
                pic_a_left_center.setVisibility(View.GONE);
                hasPic_a_left = true;
            } else if (currentImage == 2) {
                Log.e(TAG, "setImageToViewFromPhone  shoeACodebitmap: ");
                shoeACodebitmap = rotaingImageView(degree, photo);
                pic_a_right.setImageBitmap(shoeACodebitmap);
                pic_a_right_delete.setVisibility(View.VISIBLE);
                pic_a_right_center.setVisibility(View.GONE);
                hasPic_a_right = true;
            } else if (currentImage == 3) {
                Log.e(TAG, "setImageToViewFromPhone  shoeBbitmap: ");
                shoeBbitmap = rotaingImageView(degree, photo);
                pic_b_left.setImageBitmap(shoeBbitmap);
                pic_b_left_delete.setVisibility(View.VISIBLE);
                pic_b_left_center.setVisibility(View.GONE);
                hasPic_b_left = true;
            } else if (currentImage == 4) {
                Log.e(TAG, "setImageToViewFromPhone  shoeBCodebitmap: ");
                shoeBCodebitmap = rotaingImageView(degree, photo);
                pic_b_right.setImageBitmap(shoeBCodebitmap);
                pic_b_right_delete.setVisibility(View.VISIBLE);
                pic_b_right_center.setVisibility(View.GONE);
                hasPic_b_right = true;
            } else if (currentImage == 5) {
                Log.e(TAG, "setImageToViewFromPhone  shoeCbitmap: ");
                shoeCbitmap = rotaingImageView(degree, photo);
                pic_c_left.setImageBitmap(shoeCbitmap);
                pic_c_left_delete.setVisibility(View.VISIBLE);
                pic_c_left_center.setVisibility(View.GONE);
                hasPic_c_left = true;
            } else if (currentImage == 6) {
                Log.e(TAG, "setImageToViewFromPhone  shoeCCodebitmap: ");
                shoeCCodebitmap = rotaingImageView(degree, photo);
                pic_c_right.setImageBitmap(shoeCCodebitmap);
                pic_c_right_delete.setVisibility(View.VISIBLE);
                pic_c_right_center.setVisibility(View.GONE);
                hasPic_c_right = true;
            } else if (currentImage == 7) {
                Log.e(TAG, "setImageToViewFromPhone  shoeDbitmap: ");
                shoeDbitmap = rotaingImageView(degree, photo);
                pic_d_left.setImageBitmap(shoeDbitmap);
                pic_d_left_delete.setVisibility(View.VISIBLE);
                pic_d_left_center.setVisibility(View.GONE);
                hasPic_d_left = true;
            } else if (currentImage == 8) {
                Log.e(TAG, "setImageToViewFromPhone  shoeDCodebitmap: ");
                shoeDCodebitmap = rotaingImageView(degree, photo);
                pic_d_right.setImageBitmap(shoeDCodebitmap);
                pic_d_right_delete.setVisibility(View.VISIBLE);
                pic_d_right_center.setVisibility(View.GONE);
                hasPic_d_right = true;
            } else if (currentImage == 9) {
                Log.e(TAG, "setImageToViewFromPhone  licenseBitmap: ");
                licenseBitmap = rotaingImageView(degree, photo);
                pic_xingshizheng.setImageBitmap(licenseBitmap);
                pic_xingshizheng_delete.setVisibility(View.VISIBLE);
                pic_xingshizheng_center.setVisibility(View.GONE);
                hasPic_license = true;
            } else if (currentImage == 10) {
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
        //设置认证状态 //TODO
        if (proveStatus == 1) {
            //是否进行车主认证 (1 已认证 2 未认证)
            xsz_prove.setText("已认证");
            xsz_prove.setTextColor(getResources().getColor(R.color.c22));
            xsz_prove.setClickable(false);//本次不可再次认证
        } else {
            xsz_prove.setText("未认证");
            xsz_prove.setTextColor(getResources().getColor(R.color.c19));
            xsz_prove.setClickable(true);//本次可认证
        }
        //设置外层数据
        user_name.setText(userName);
        user_phone.setText(userPhone);
        car_num.setText(platNumber);
        store_name.setText(storeName);
        order_num.setText(orderNo);
        //设置内层数据
        fl_repair_a_.setVisibility(View.GONE);
        fl_repair_b_.setVisibility(View.GONE);
        fl_repair_c_.setVisibility(View.GONE);
        fl_repair_d_.setVisibility(View.GONE);
        pic_a_titleno.setVisibility(View.GONE);
        pic_b_titleno.setVisibility(View.GONE);
        pic_c_titleno.setVisibility(View.GONE);
        pic_d_titleno.setVisibility(View.GONE);
        ll_pic_a.setVisibility(View.GONE);
        ll_pic_b.setVisibility(View.GONE);
        ll_pic_c.setVisibility(View.GONE);
        ll_pic_d.setVisibility(View.GONE);
        for (int i = 0; i < shoeRepairList.size(); i++) {
            final ShoeRepair bean = shoeRepairList.get(i);
            if (i == 0) {
                fl_repair_a_.setVisibility(View.VISIBLE);
                code_repair_a_.setText(bean.getBarCode());
                pic_a_titleno.setText(bean.getBarCode());//照片标题
//                pic_a_titleno.setVisibility(View.VISIBLE);//照片标题
//                ll_pic_a.setVisibility(View.VISIBLE);//照片
                oldCount_a_ = Integer.parseInt(bean.getRepairAmount());
                repair_num_a_.setGoods_storage(maxRepairNum - oldCount_a_ > 0 ? maxRepairNum - oldCount_a_ : 1);//必须先设置good_storege 否则默认为1
                repair_num_a_.setAmount(0);
                repair_num_a_.setOnAmountChangeListener(new AmountView.OnAmountChangeListener() {
                    @Override
                    public void onAmountChange(View view, int amount) {
                        if (amount > maxRepairNum - oldCount_a_) {
                            Toast.makeText(getApplicationContext(), maxErrorMsg, Toast.LENGTH_SHORT).show();
                            repair_num_a_.setAmount(amount - 1);
                            Log.e(TAG, "onAmountChange: submit outofmax");
                            return;
                        }
                        if (amount == maxRepairNum - oldCount_a_) {
                            Toast.makeText(getApplicationContext(), "此轮胎历史修补" + oldCount_a_ + "次，每条轮胎最多免费修补" + maxRepairNum + "次，店主请留意!", Toast.LENGTH_SHORT).show();
                        }
                        if (amount < 0) {
                            repair_num_a_.setAmount(amount + 1);
                            return;
                        }
                        if (amount == 0) {
                            Log.e(TAG, "onAmountChange:  amount == oldCount_a_");
                            //减到初始值时 先隐藏
                            pic_a_titleno.setVisibility(View.GONE);//照片标题
                            ll_pic_a.setVisibility(View.GONE);//照片
                            //再删除照片显示
                            pic_a_left_delete.setVisibility(View.GONE);
                            pic_a_left_center.setVisibility(View.VISIBLE);
                            pic_a_left.setImageResource(R.drawable.img_bg_dark);
                            hasPic_a_left = false;
                            pic_a_right_delete.setVisibility(View.GONE);
                            pic_a_right_center.setVisibility(View.VISIBLE);
                            pic_a_right.setImageResource(R.drawable.img_bg_dark);
                            hasPic_a_right = false;
                            currentCount_a_ = amount;
                            return;
                        }
                        pic_a_titleno.setVisibility(View.VISIBLE);//照片标题
                        ll_pic_a.setVisibility(View.VISIBLE);//照片
                        currentCount_a_ = amount;
                    }
                });
            }
            if (i == 1) {
                fl_repair_b_.setVisibility(View.VISIBLE);
                code_repair_b_.setText(bean.getBarCode());
                pic_b_titleno.setText(bean.getBarCode());//照片标题
//                pic_b_titleno.setVisibility(View.VISIBLE);//照片标题
//                ll_pic_b.setVisibility(View.VISIBLE);
                oldCount_b_ = Integer.parseInt(bean.getRepairAmount());
                repair_num_b_.setGoods_storage(maxRepairNum - oldCount_b_ > 0 ? maxRepairNum - oldCount_b_ : 1);
                repair_num_b_.setAmount(0);
                repair_num_b_.setOnAmountChangeListener(new AmountView.OnAmountChangeListener() {
                    @Override
                    public void onAmountChange(View view, int amount) {
                        if (amount > maxRepairNum - oldCount_b_) {
                            Toast.makeText(getApplicationContext(), maxErrorMsg, Toast.LENGTH_SHORT).show();
                            repair_num_b_.setAmount(amount - 1);
                            return;
                        }
                        if (amount == maxRepairNum - oldCount_b_) {
                            Toast.makeText(getApplicationContext(), "此轮胎历史修补" + oldCount_b_ + "次，每条轮胎最多免费修补" + maxRepairNum + "次，店主请留意!", Toast.LENGTH_SHORT).show();
                        }
                        if (amount < 0) {
                            repair_num_b_.setAmount(amount + 1);
                            return;
                        }
                        if (amount == 0) {
                            //减到初始值时 先隐藏
                            pic_b_titleno.setVisibility(View.GONE);//照片标题
                            ll_pic_b.setVisibility(View.GONE);//照片
                            //再删除照片显示
                            pic_b_left_delete.setVisibility(View.GONE);
                            pic_b_left_center.setVisibility(View.VISIBLE);
                            pic_b_left.setImageResource(R.drawable.img_bg_dark);
                            hasPic_b_left = false;
                            pic_b_right_delete.setVisibility(View.GONE);
                            pic_b_right_center.setVisibility(View.VISIBLE);
                            pic_b_right.setImageResource(R.drawable.img_bg_dark);
                            hasPic_b_right = false;
                            currentCount_b_ = amount;
                            return;
                        }
                        pic_b_titleno.setVisibility(View.VISIBLE);//照片标题
                        ll_pic_b.setVisibility(View.VISIBLE);//照片
                        currentCount_b_ = amount;
                    }
                });
            }
            if (i == 2) {
                fl_repair_c_.setVisibility(View.VISIBLE);
                code_repair_c_.setText(bean.getBarCode());
                pic_c_titleno.setText(bean.getBarCode());//照片标题
//                pic_c_titleno.setVisibility(View.VISIBLE);//照片标题
//                ll_pic_c.setVisibility(View.VISIBLE);
                oldCount_c_ = Integer.parseInt(bean.getRepairAmount());
                repair_num_c_.setGoods_storage(maxRepairNum - oldCount_c_ > 0 ? maxRepairNum - oldCount_c_ : 1);
                repair_num_c_.setAmount(0);
                repair_num_c_.setOnAmountChangeListener(new AmountView.OnAmountChangeListener() {
                    @Override
                    public void onAmountChange(View view, int amount) {
                        if (amount > maxRepairNum - oldCount_c_) {
                            Toast.makeText(getApplicationContext(), maxErrorMsg, Toast.LENGTH_SHORT).show();
                            repair_num_c_.setAmount(amount - 1);
                            return;
                        }
                        if (amount == maxRepairNum - oldCount_c_) {
                            Toast.makeText(getApplicationContext(), "此轮胎历史修补" + oldCount_c_ + "次，每条轮胎最多免费修补" + maxRepairNum + "次，店主请留意!", Toast.LENGTH_SHORT).show();
                        }
                        if (amount < 0) {
                            repair_num_c_.setAmount(amount + 1);
                            return;
                        }
                        if (amount == 0) {
                            //减到初始值时 先隐藏
                            pic_c_titleno.setVisibility(View.GONE);//照片标题
                            ll_pic_c.setVisibility(View.GONE);//照片
                            //再删除照片显示
                            pic_c_left_delete.setVisibility(View.GONE);
                            pic_c_left_center.setVisibility(View.VISIBLE);
                            pic_c_left.setImageResource(R.drawable.img_bg_dark);
                            hasPic_c_left = false;
                            pic_c_right_delete.setVisibility(View.GONE);
                            pic_c_right_center.setVisibility(View.VISIBLE);
                            pic_c_right.setImageResource(R.drawable.img_bg_dark);
                            hasPic_c_right = false;
                            currentCount_c_ = amount;
                            return;
                        }
                        pic_c_titleno.setVisibility(View.VISIBLE);//照片标题
                        ll_pic_c.setVisibility(View.VISIBLE);//照片
                        currentCount_c_ = amount;
                    }
                });
            }
            if (i == 3) {
                fl_repair_d_.setVisibility(View.VISIBLE);
                code_repair_d_.setText(bean.getBarCode());
                pic_d_titleno.setText(bean.getBarCode());//照片标题
//                pic_d_titleno.setVisibility(View.VISIBLE);//照片标题
//                ll_pic_d.setVisibility(View.VISIBLE);
                oldCount_d_ = Integer.parseInt(bean.getRepairAmount());
                repair_num_d_.setGoods_storage(maxRepairNum - oldCount_d_ > 0 ? maxRepairNum - oldCount_d_ : 1);
                repair_num_d_.setAmount(0);
                repair_num_d_.setOnAmountChangeListener(new AmountView.OnAmountChangeListener() {
                    @Override
                    public void onAmountChange(View view, int amount) {
                        if (amount > maxRepairNum - oldCount_d_) {
                            Toast.makeText(getApplicationContext(), maxErrorMsg, Toast.LENGTH_SHORT).show();
                            repair_num_d_.setAmount(amount - 1);
                            return;
                        }
                        if (amount == maxRepairNum - oldCount_d_) {
                            Toast.makeText(getApplicationContext(), "此轮胎历史修补" + oldCount_d_ + "次，每条轮胎最多免费修补" + maxRepairNum + "次，店主请留意!", Toast.LENGTH_SHORT).show();
                        }
                        if (amount < 0) {
                            repair_num_d_.setAmount(amount + 1);
                            return;
                        }
                        if (amount == 0) {
                            //减到初始值时 先隐藏
                            pic_d_titleno.setVisibility(View.GONE);//照片标题
                            ll_pic_d.setVisibility(View.GONE);//照片
                            //再删除照片显示
                            pic_d_left_delete.setVisibility(View.GONE);
                            pic_d_left_center.setVisibility(View.VISIBLE);
                            pic_d_left.setImageResource(R.drawable.img_bg_dark);
                            hasPic_d_left = false;
                            pic_d_right_delete.setVisibility(View.GONE);
                            pic_d_right_center.setVisibility(View.VISIBLE);
                            pic_d_right.setImageResource(R.drawable.img_bg_dark);
                            hasPic_d_right = false;
                            currentCount_d_ = amount;
                            return;
                        }
                        pic_d_titleno.setVisibility(View.VISIBLE);//照片标题
                        ll_pic_d.setVisibility(View.VISIBLE);//照片
                        currentCount_d_ = amount;
                    }
                });
            }
        }

    }

    private void initBeforePost() {
        //1.数据
        for (int i = 0; i < shoeRepairList.size(); i++) {
            Log.e(TAG, "initBeforePost: submit oldCount_a_ = " + oldCount_a_ + " " + oldCount_b_ + " " + oldCount_c_ + " " + oldCount_d_);
            if (i == 0 && currentCount_a_ > 0) {
                repairAmountList.add(new RepairAmount(code_repair_a_.getText().toString(), oldCount_a_ + currentCount_a_ + ""));
            }
            if (i == 1 && currentCount_b_ > 0) {
                repairAmountList.add(new RepairAmount(code_repair_b_.getText().toString(), oldCount_b_ + currentCount_b_ + ""));
            }
            if (i == 2 && currentCount_c_ > 0) {
                repairAmountList.add(new RepairAmount(code_repair_c_.getText().toString(), oldCount_c_ + currentCount_c_ + ""));
            }
            if (i == 3 && currentCount_d_ > 0) {
                repairAmountList.add(new RepairAmount(code_repair_d_.getText().toString(), oldCount_d_ + currentCount_d_ + ""));
            }
        }

        //2.照片   最后在onDestroy中根据图片Path删除照片
        /*path_licenseBitmap = ImageUtils.savePhoto(licenseBitmap, this.getObbDir().getAbsolutePath(), "licensePic");*///TODO
        path_carBitmap = ImageUtils.savePhoto(carBitmap, this.getObbDir().getAbsolutePath(), "carPic");
        if (hasPic_a_left) {
            path_shoeABitmap = ImageUtils.savePhoto(shoeAbitmap, this.getObbDir().getAbsolutePath(), "shoeAPic" + pic_a_titleno.getText().toString());
        }
        if (hasPic_a_right) {
            path_shoeABarcodeBitmap = ImageUtils.savePhoto(shoeACodebitmap, this.getObbDir().getAbsolutePath(), "shoeACodePic" + pic_a_titleno.getText().toString());
        }
        if (hasPic_b_left) {
            path_shoeBBitmap = ImageUtils.savePhoto(shoeBbitmap, this.getObbDir().getAbsolutePath(), "shoeBPic" + pic_b_titleno.getText().toString());
        }
        if (hasPic_b_right) {
            path_shoeBBarcodeBitmap = ImageUtils.savePhoto(shoeBCodebitmap, this.getObbDir().getAbsolutePath(), "shoeBCodePic" + pic_b_titleno.getText().toString());
        }
        if (hasPic_c_left) {
            path_shoeCBitmap = ImageUtils.savePhoto(shoeCbitmap, this.getObbDir().getAbsolutePath(), "shoeCPic" + pic_c_titleno.getText().toString());
        }
        if (hasPic_c_right) {
            path_shoeCBarcodeBitmap = ImageUtils.savePhoto(shoeCCodebitmap, this.getObbDir().getAbsolutePath(), "shoeCCodePic" + pic_c_titleno.getText().toString());
        }
        if (hasPic_d_left) {
            path_shoeDBitmap = ImageUtils.savePhoto(shoeDbitmap, this.getObbDir().getAbsolutePath(), "shoeDPic" + pic_d_titleno.getText().toString());
        }
        if (hasPic_d_right) {
            path_shoeDBarcodeBitmap = ImageUtils.savePhoto(shoeDCodebitmap, this.getObbDir().getAbsolutePath(), "shoeDCodePic" + pic_d_titleno.getText().toString());
        }

    }


    private void initView() {
        tv_bottom_a = findViewById(R.id.tv_bottom_a);
        tv_bottom_c = findViewById(R.id.tv_bottom_c);
        user_name = findViewById(R.id.user_name);
        user_phone = findViewById(R.id.user_phone);
        car_num = findViewById(R.id.car_num);
        store_name = findViewById(R.id.store_name);
        order_num = findViewById(R.id.order_num);
        user_phone_img = findViewById(R.id.user_phone_img);
        //shoeFlag(删)
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
        //轮胎照片 标题 barcode
        pic_a_titleno = findViewById(R.id.pic_a_titleno);
        pic_b_titleno = findViewById(R.id.pic_b_titleno);
        pic_c_titleno = findViewById(R.id.pic_c_titleno);
        pic_d_titleno = findViewById(R.id.pic_d_titleno);
        //轮胎照片
        ll_pic_a = findViewById(R.id.ll_pic_a);
        pic_a_left = findViewById(R.id.pic_a_left);
        pic_a_left_delete = findViewById(R.id.pic_a_left_delete);
        pic_a_left_center = findViewById(R.id.pic_a_left_center);
        pic_a_right = findViewById(R.id.pic_a_right);
        pic_a_right_delete = findViewById(R.id.pic_a_right_delete);
        pic_a_right_center = findViewById(R.id.pic_a_right_center);
        ll_pic_b = findViewById(R.id.ll_pic_b);
        pic_b_left = findViewById(R.id.pic_b_left);
        pic_b_left_delete = findViewById(R.id.pic_b_left_delete);
        pic_b_left_center = findViewById(R.id.pic_b_left_center);
        pic_b_right = findViewById(R.id.pic_b_right);
        pic_b_right_delete = findViewById(R.id.pic_b_right_delete);
        pic_b_right_center = findViewById(R.id.pic_b_right_center);
        ll_pic_c = findViewById(R.id.ll_pic_c);
        pic_c_left = findViewById(R.id.pic_c_left);
        pic_c_left_delete = findViewById(R.id.pic_c_left_delete);
        pic_c_left_center = findViewById(R.id.pic_c_left_center);
        pic_c_right = findViewById(R.id.pic_c_right);
        pic_c_right_delete = findViewById(R.id.pic_c_right_delete);
        pic_c_right_center = findViewById(R.id.pic_c_right_center);
        ll_pic_d = findViewById(R.id.ll_pic_d);
        pic_d_left = findViewById(R.id.pic_d_left);
        pic_d_left_delete = findViewById(R.id.pic_d_left_delete);
        pic_d_left_center = findViewById(R.id.pic_d_left_center);
        pic_d_right = findViewById(R.id.pic_d_right);
        pic_d_right_delete = findViewById(R.id.pic_d_right_delete);
        pic_d_right_center = findViewById(R.id.pic_d_right_center);
        //行驶证和车辆照片
        pic_xingshizheng = findViewById(R.id.pic_xingshizheng);
        pic_xingshizheng_delete = findViewById(R.id.pic_xingshizheng_delete);
        pic_xingshizheng_center = findViewById(R.id.pic_xingshizheng_center);

        pic_car = findViewById(R.id.pic_car);
        pic_car_delete = findViewById(R.id.pic_car_delete);
        pic_car_center = findViewById(R.id.pic_car_center);

        //拍照示例
        tv_shoepic_sample = findViewById(R.id.tv_shoepic_sample);
        tv_licencepic_sample = findViewById(R.id.tv_licencepic_sample);
        tv_carpic_sample = findViewById(R.id.tv_carpic_sample);
        //补胎编号以及修补个数
        fl_repair_a_ = findViewById(R.id.fl_repair_a_);
        code_repair_a_ = findViewById(R.id.code_repair_a_);
        repair_num_a_ = findViewById(R.id.repair_num_a_);
        fl_repair_b_ = findViewById(R.id.fl_repair_b_);
        code_repair_b_ = findViewById(R.id.code_repair_b_);
        repair_num_b_ = findViewById(R.id.repair_num_b_);
        fl_repair_c_ = findViewById(R.id.fl_repair_c_);
        code_repair_c_ = findViewById(R.id.code_repair_c_);
        repair_num_c_ = findViewById(R.id.repair_num_c_);
        fl_repair_d_ = findViewById(R.id.fl_repair_d_);
        code_repair_d_ = findViewById(R.id.code_repair_d_);
        repair_num_d_ = findViewById(R.id.repair_num_d_);

        xsz_prove_layout = findViewById(R.id.xsz_prove_layout);//TODO
        prove_icon = findViewById(R.id.prove_icon);
        xsz_prove = findViewById(R.id.xsz_prove);


        shoeFlagList = new ArrayList<>();
        shoeRepairList = new ArrayList<>();
        repairAmountList = new ArrayList<>();
        progressDialog = new ProgressDialog(OrderConfirmTireRepairActivity.this);
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
        if (path_shoeABitmap != null && path_shoeABitmap.length() != 0) {
            UtilsRY.deleteImage(path_shoeABitmap, getApplicationContext());
            Log.e(TAG, "onDestroy: path_shoeABitmap");
        }
        if (path_shoeABarcodeBitmap != null && path_shoeABarcodeBitmap.length() != 0) {
            UtilsRY.deleteImage(path_shoeABarcodeBitmap, getApplicationContext());
            Log.e(TAG, "onDestroy: path_shoeABarcodeBitmap");
        }
        if (path_shoeBBitmap != null && path_shoeBBitmap.length() != 0) {
            UtilsRY.deleteImage(path_shoeBBitmap, getApplicationContext());
            Log.e(TAG, "onDestroy: path_shoeBBitmap");
        }
        if (path_shoeBBarcodeBitmap != null && path_shoeBBarcodeBitmap.length() != 0) {
            UtilsRY.deleteImage(path_shoeBBarcodeBitmap, getApplicationContext());
            Log.e(TAG, "onDestroy: path_shoeBBarcodeBitmap");
        }
        if (path_shoeCBitmap != null && path_shoeCBitmap.length() != 0) {
            UtilsRY.deleteImage(path_shoeCBitmap, getApplicationContext());
            Log.e(TAG, "onDestroy: path_shoeCBitmap");
        }
        if (path_shoeCBarcodeBitmap != null && path_shoeCBarcodeBitmap.length() != 0) {
            UtilsRY.deleteImage(path_shoeCBarcodeBitmap, getApplicationContext());
            Log.e(TAG, "onDestroy: path_shoeCBarcodeBitmap");
        }
        if (path_shoeDBitmap != null && path_shoeDBitmap.length() != 0) {
            UtilsRY.deleteImage(path_shoeDBitmap, getApplicationContext());
            Log.e(TAG, "onDestroy: path_shoeDBitmap");
        }
        if (path_shoeDBarcodeBitmap != null && path_shoeDBarcodeBitmap.length() != 0) {
            UtilsRY.deleteImage(path_shoeDBarcodeBitmap, getApplicationContext());
            Log.e(TAG, "onDestroy: path_shoeDBarcodeBitmap");
        }
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
