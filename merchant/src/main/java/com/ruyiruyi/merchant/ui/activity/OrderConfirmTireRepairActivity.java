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
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ruyiruyi.merchant.MainActivity;
import com.ruyiruyi.merchant.R;
import com.ruyiruyi.merchant.bean.FreeChangeNewShoeBean;
import com.ruyiruyi.merchant.bean.FreeChangeOldShoeBean;
import com.ruyiruyi.merchant.bean.OldNewBarCode;
import com.ruyiruyi.merchant.bean.ShoeRepair;
import com.ruyiruyi.merchant.db.DbConfig;
import com.ruyiruyi.merchant.ui.activity.base.MerchantBaseActivity;
import com.ruyiruyi.merchant.ui.multiType.PublicShoeFlag;
import com.ruyiruyi.merchant.utils.UtilsRY;
import com.ruyiruyi.merchant.utils.UtilsURL;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;
import com.ruyiruyi.rylibrary.cell.ActionBar;
import com.ruyiruyi.rylibrary.cell.AmountView;
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

public class OrderConfirmTireRepairActivity extends MerchantBaseActivity {
    private final int TAKE_PICTURE = 0;
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
    private ImageView pic_a_left;
    private ImageView pic_a_left_delete;
    private LinearLayout pic_a_left_center;
    private ImageView pic_a_right;
    private ImageView pic_a_right_delete;
    private LinearLayout pic_a_right_center;

    private LinearLayout ll_pic_b;
    private ImageView pic_b_left;
    private ImageView pic_b_left_delete;
    private LinearLayout pic_b_left_center;
    private ImageView pic_b_right;
    private ImageView pic_b_right_delete;
    private LinearLayout pic_b_right_center;

    private LinearLayout ll_pic_c;
    private ImageView pic_c_left;
    private ImageView pic_c_left_delete;
    private LinearLayout pic_c_left_center;
    private ImageView pic_c_right;
    private ImageView pic_c_right_delete;
    private LinearLayout pic_c_right_center;

    private LinearLayout ll_pic_d;
    private ImageView pic_d_left;
    private ImageView pic_d_left_delete;
    private LinearLayout pic_d_left_center;
    private ImageView pic_d_right;
    private ImageView pic_d_right_delete;
    private LinearLayout pic_d_right_center;
    //行驶证照片和车辆照片
    private ImageView pic_xingshizheng;
    private ImageView pic_xingshizheng_delete;
    private LinearLayout pic_xingshizheng_center;

    private ImageView pic_car;
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
    private String orderNo;
    private String userName;
    private String userPhone;
    private String platNumber;
    private String storeName;

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
    private final int maxRepairNum = 3;  //预设轮胎最大修补次数
    private int currentCount_a_ = 0;
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
        storeId = new DbConfig().getId() + "";

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
        params.addBodyParameter("token", new DbConfig().getToken());
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
                if (hasPic_a_left) {
                    return;
                }
                currentImage = 3;
                showPicInputDialog();
            }
        });
        RxViewAction.clickNoDouble(pic_b_right_center).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (hasPic_a_right) {
                    return;
                }
                currentImage = 4;
                showPicInputDialog();
            }
        });
        RxViewAction.clickNoDouble(pic_c_left_center).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (hasPic_a_left) {
                    return;
                }
                currentImage = 5;
                showPicInputDialog();
            }
        });
        RxViewAction.clickNoDouble(pic_c_right_center).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (hasPic_a_right) {
                    return;
                }
                currentImage = 6;
                showPicInputDialog();
            }
        });
        RxViewAction.clickNoDouble(pic_d_left_center).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (hasPic_a_left) {
                    return;
                }
                currentImage = 7;
                showPicInputDialog();
            }
        });
        RxViewAction.clickNoDouble(pic_d_right_center).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (hasPic_a_right) {
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
                bundle.putString("type", "change");
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
                    showOrderDialog("1");
                }
            }
        });
        //拒绝服务
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
        dialog.setIcon(R.drawable.ic_logo_huise);
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
                        RequestParams params = new RequestParams(UtilsURL.REQUEST_URL + "");
                        params.addBodyParameter("reqJson", object.toString());
//                        params.addBodyParameter("changeBarCodeVoList", oldNewBarCodeList.toString());
                        params.addBodyParameter("token", new DbConfig().getToken());
                        params.addBodyParameter("drivingLicenseImg", new File(path_licenseBitmap));
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
                        Log.e(TAG, "onClick:  params.toString() = " + params.toString());
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
                                Toast.makeText(getApplicationContext(), "网络异常,请检查网络", Toast.LENGTH_SHORT).show();
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
                        RequestParams params3 = new RequestParams(UtilsURL.REQUEST_URL + "");
                        params3.addBodyParameter("reqJson", object3.toString());
//                        params3.addBodyParameter("changeBarCodeVoList", oldNewBarCodeList.toString());
                        params3.addBodyParameter("token", new DbConfig().getToken());
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
                                Toast.makeText(getApplicationContext(), "网络异常,请检查网络", Toast.LENGTH_SHORT).show();
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
                int picNumShoe = 0;
                int picNumCode = 0;
                if (hasPic_a_left) {
                    picNumShoe++;
                }
                if (hasPic_b_left) {
                    picNumShoe++;
                }
                if (hasPic_c_left) {
                    picNumShoe++;
                }
                if (hasPic_d_left) {
                    picNumShoe++;
                }
                if (picNumShoe < shoeRepairList.size()) {
                    showErrorDialog("请补全轮胎正面照!");
                    return false;
                }
                if (hasPic_a_right) {
                    picNumCode++;
                }
                if (hasPic_b_right) {
                    picNumCode++;
                }
                if (hasPic_c_right) {
                    picNumCode++;
                }
                if (hasPic_d_right) {
                    picNumCode++;
                }
                if (picNumCode < shoeRepairList.size()) {
                    showErrorDialog("请补全轮胎条形码特写照!");
                    return false;
                }
                if (!hasPic_license) {
                    showErrorDialog("请上传行驶证照片!");
                    return false;
                }
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
        dialog.setIcon(R.drawable.ic_logo_huise);
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
        String[] items = {"拍照"};
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                takePicture();
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
            file = new File(Environment
                    .getExternalStorageDirectory(), "shoea.jpg");
            path_ = file.getPath();
        } else if (currentImage == 2) {
            file = new File(Environment
                    .getExternalStorageDirectory(), "shoeacode.jpg");
            path_ = file.getPath();
        } else if (currentImage == 3) {
            file = new File(Environment
                    .getExternalStorageDirectory(), "shoeb.jpg");
            path_ = file.getPath();
        } else if (currentImage == 4) {
            file = new File(Environment
                    .getExternalStorageDirectory(), "shoebcode.jpg");
            path_ = file.getPath();
        } else if (currentImage == 5) {
            file = new File(Environment
                    .getExternalStorageDirectory(), "shoec.jpg");
            path_ = file.getPath();
        } else if (currentImage == 6) {
            file = new File(Environment
                    .getExternalStorageDirectory(), "shoeccode.jpg");
            path_ = file.getPath();
        } else if (currentImage == 7) {
            file = new File(Environment
                    .getExternalStorageDirectory(), "shoed.jpg");
            path_ = file.getPath();
        } else if (currentImage == 8) {
            file = new File(Environment
                    .getExternalStorageDirectory(), "shoedcode.jpg");
            path_ = file.getPath();
        } else if (currentImage == 9) {
            file = new File(Environment
                    .getExternalStorageDirectory(), "license.jpg");
            path_ = file.getPath();
        } else if (currentImage == 10) {
            file = new File(Environment
                    .getExternalStorageDirectory(), "car.jpg");
            path_ = file.getPath();
        }

        //判断是否是AndroidN以及更高的版本
        if (Build.VERSION.SDK_INT >= 24) {
            openCameraIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            tempUri = FileProvider.getUriForFile(getApplicationContext(), "com.ruyiruyi.merchant.fileProvider", file);
        } else {
            tempUri = Uri.fromFile(new File(Environment
                    .getExternalStorageDirectory(), "image.jpg"));
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
                case TAKE_PICTURE:
                    setImageToViewFromPhone(tempUri);
                    break;
            }
        }

    }

    //未剪辑照片
    private void setImageToViewFromPhone(Uri uri) {
        int degree = ImageUtils.readPictureDegree(path_);
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
        for (int i = 0; i < shoeRepairList.size(); i++) {
            final ShoeRepair bean = shoeRepairList.get(i);
            if (i == 0) {
                fl_repair_a_.setVisibility(View.VISIBLE);
                code_repair_a_.setText(bean.getBarCode());
                pic_a_titleno.setText(bean.getBarCode());//照片标题
                pic_a_titleno.setVisibility(View.VISIBLE);//照片标题
                oldCount_a_ = Integer.parseInt(bean.getRepairAmount());
                repair_num_a_.setAmount(oldCount_a_);
                repair_num_a_.setGoods_storage(maxRepairNum);
                repair_num_a_.setOnAmountChangeListener(new AmountView.OnAmountChangeListener() {
                    @Override
                    public void onAmountChange(View view, int amount) {
                        if (amount == maxRepairNum) {
                            Toast.makeText(getApplicationContext(), "每条轮胎最多修补三次!", Toast.LENGTH_SHORT).show();
                        }
                        if (amount < oldCount_a_) {
                            repair_num_a_.setAmount(amount + 1);
                            return;
                        }
                        currentCount_a_ = amount;
                        Log.e(TAG, "onAmountChange: currentCount_a_ = " + currentCount_a_);
                    }
                });
            }
            if (i == 1) {
                fl_repair_b_.setVisibility(View.VISIBLE);
                code_repair_b_.setText(bean.getBarCode());
                pic_b_titleno.setText(bean.getBarCode());//照片标题
                pic_b_titleno.setVisibility(View.VISIBLE);//照片标题
                oldCount_b_ = Integer.parseInt(bean.getRepairAmount());
                repair_num_b_.setAmount(oldCount_b_);
                repair_num_b_.setGoods_storage(maxRepairNum);
                repair_num_b_.setOnAmountChangeListener(new AmountView.OnAmountChangeListener() {
                    @Override
                    public void onAmountChange(View view, int amount) {
                        if (amount == maxRepairNum) {
                            Toast.makeText(getApplicationContext(), "每条轮胎最多修补三次!", Toast.LENGTH_SHORT).show();
                        }
                        if (amount < oldCount_b_) {
                            repair_num_b_.setAmount(amount + 1);
                            return;
                        }
                        currentCount_b_ = amount;
                    }
                });
            }
            if (i == 2) {
                fl_repair_c_.setVisibility(View.VISIBLE);
                code_repair_c_.setText(bean.getBarCode());
                pic_c_titleno.setText(bean.getBarCode());//照片标题
                pic_c_titleno.setVisibility(View.VISIBLE);//照片标题
                oldCount_c_ = Integer.parseInt(bean.getRepairAmount());
                repair_num_c_.setAmount(oldCount_c_);
                repair_num_c_.setGoods_storage(maxRepairNum);
                repair_num_c_.setOnAmountChangeListener(new AmountView.OnAmountChangeListener() {
                    @Override
                    public void onAmountChange(View view, int amount) {
                        if (amount == maxRepairNum) {
                            Toast.makeText(getApplicationContext(), "每条轮胎最多修补三次!", Toast.LENGTH_SHORT).show();
                        }
                        if (amount < oldCount_c_) {
                            repair_num_c_.setAmount(amount + 1);
                            return;
                        }
                        currentCount_c_ = amount;
                    }
                });
            }
            if (i == 3) {
                fl_repair_d_.setVisibility(View.VISIBLE);
                code_repair_d_.setText(bean.getBarCode());
                pic_d_titleno.setText(bean.getBarCode());//照片标题
                pic_d_titleno.setVisibility(View.VISIBLE);//照片标题
                oldCount_d_ = Integer.parseInt(bean.getRepairAmount());
                repair_num_d_.setAmount(oldCount_d_);
                repair_num_d_.setGoods_storage(maxRepairNum);
                repair_num_d_.setOnAmountChangeListener(new AmountView.OnAmountChangeListener() {
                    @Override
                    public void onAmountChange(View view, int amount) {
                        if (amount == maxRepairNum) {
                            Toast.makeText(getApplicationContext(), "每条轮胎最多修补三次!", Toast.LENGTH_SHORT).show();
                        }
                        if (amount < oldCount_d_) {
                            repair_num_d_.setAmount(amount + 1);
                            return;
                        }
                        currentCount_d_ = amount;
                    }
                });
            }
        }

    }

    private void initBeforePost() {
        //1.数据

        //2.照片   最后在onDestroy中根据图片Path删除照片
        path_licenseBitmap = ImageUtils.savePhoto(licenseBitmap, Environment
                .getExternalStorageDirectory().getAbsolutePath(), "licensePic");
        path_carBitmap = ImageUtils.savePhoto(carBitmap, Environment
                .getExternalStorageDirectory().getAbsolutePath(), "carPic");
        if (hasPic_a_left) {
            path_shoeABitmap = ImageUtils.savePhoto(shoeAbitmap, Environment
                    .getExternalStorageDirectory().getAbsolutePath(), "shoeAPic" + pic_a_titleno.getText().toString());
        }
        if (hasPic_a_right) {
            path_shoeABarcodeBitmap = ImageUtils.savePhoto(shoeACodebitmap, Environment
                    .getExternalStorageDirectory().getAbsolutePath(), "shoeACodePic" + pic_a_titleno.getText().toString());
        }
        if (hasPic_b_left) {
            path_shoeBBitmap = ImageUtils.savePhoto(shoeBbitmap, Environment
                    .getExternalStorageDirectory().getAbsolutePath(), "shoeBPic" + pic_b_titleno.getText().toString());
        }
        if (hasPic_b_right) {
            path_shoeBBarcodeBitmap = ImageUtils.savePhoto(shoeBCodebitmap, Environment
                    .getExternalStorageDirectory().getAbsolutePath(), "shoeBCodePic" + pic_b_titleno.getText().toString());
        }
        if (hasPic_c_left) {
            path_shoeCBitmap = ImageUtils.savePhoto(shoeCbitmap, Environment
                    .getExternalStorageDirectory().getAbsolutePath(), "shoeCPic" + pic_c_titleno.getText().toString());
        }
        if (hasPic_c_right) {
            path_shoeCBarcodeBitmap = ImageUtils.savePhoto(shoeCCodebitmap, Environment
                    .getExternalStorageDirectory().getAbsolutePath(), "shoeCCodePic" + pic_c_titleno.getText().toString());
        }
        if (hasPic_d_left) {
            path_shoeDBitmap = ImageUtils.savePhoto(shoeDbitmap, Environment
                    .getExternalStorageDirectory().getAbsolutePath(), "shoeDPic" + pic_d_titleno.getText().toString());
        }
        if (hasPic_d_right) {
            path_shoeDBarcodeBitmap = ImageUtils.savePhoto(shoeDCodebitmap, Environment
                    .getExternalStorageDirectory().getAbsolutePath(), "shoeDCodePic" + pic_d_titleno.getText().toString());
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

        shoeFlagList = new ArrayList<>();
        shoeRepairList = new ArrayList<>();
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
}
