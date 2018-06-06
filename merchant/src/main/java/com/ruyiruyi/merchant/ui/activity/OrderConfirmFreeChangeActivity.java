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
import android.os.Environment;
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
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ruyiruyi.merchant.MainActivity;
import com.ruyiruyi.merchant.R;
import com.ruyiruyi.merchant.bean.FreeChangeNewShoeBean;
import com.ruyiruyi.merchant.bean.FreeChangeOldShoeBean;
import com.ruyiruyi.merchant.bean.OldNewBarCode;
import com.ruyiruyi.merchant.db.DbConfig;
import com.ruyiruyi.merchant.ui.activity.base.MerchantBaseActivity;
import com.ruyiruyi.merchant.ui.multiType.PublicShoeFlag;
import com.ruyiruyi.merchant.utils.UtilsURL;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;
import com.ruyiruyi.rylibrary.cell.ActionBar;
import com.ruyiruyi.rylibrary.image.ImageUtils;
import com.ruyiruyi.rylibrary.ui.cell.WheelView;

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

public class OrderConfirmFreeChangeActivity extends MerchantBaseActivity {
    private final int TAKE_PICTURE = 0;
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
    //  满五年
    private FrameLayout fl_full_change_title;//满五年标题布局
    private LinearLayout ll_full_change_content;//满五年content布局
    private FrameLayout fl_full_barcode_a_;//整条
    private TextView full_code_a;//左 No.12346
    private TextView full_code_a_change;//替换 (1.)
    private LinearLayout ll_full_code_a_new;//右 布局  带x  (2.)
    private TextView full_code_a_new;//右 No.12346
    private ImageView full_code_a_new_delete;  //img x
    private FrameLayout fl_full_barcode_b_;//整条
    private TextView full_code_b;//左 No.12346
    private TextView full_code_b_change;//替换 (1.)
    private LinearLayout ll_full_code_b_new;//右 布局  带x  (2.)
    private TextView full_code_b_new;//右 No.12346
    private ImageView full_code_b_new_delete;  //img x
    private FrameLayout fl_full_barcode_c_;//整条
    private TextView full_code_c;//左 No.12346
    private TextView full_code_c_change;//替换 (1.)
    private LinearLayout ll_full_code_c_new;//右 布局  带x  (2.)
    private TextView full_code_c_new;//右 No.12346
    private ImageView full_code_c_new_delete;  //img x
    private FrameLayout fl_full_barcode_d_;//整条
    private TextView full_code_d;//左 No.12346
    private TextView full_code_d_change;//替换 (1.)
    private LinearLayout ll_full_code_d_new;//右 布局  带x  (2.)
    private TextView full_code_d_new;//右 No.12346
    private ImageView full_code_d_new_delete;  //img x
    // 达到磨损程度
    private FrameLayout fl_broken_change_title;//达到磨损程度标题布局
    private LinearLayout ll_broken_change_content;//达到磨损程度content布局
    private FrameLayout fl_broken_barcode_a_;//整条
    private TextView broken_code_a;//左 No.12346
    private TextView broken_code_a_change;//替换 (1.)
    private LinearLayout ll_broken_code_a_new;//右 布局  带x  (2.)
    private TextView broken_code_a_new;//右 No.12346
    private ImageView broken_code_a_new_delete;  //img x
    private FrameLayout fl_broken_barcode_b_;//整条
    private TextView broken_code_b;//左 No.12346
    private TextView broken_code_b_change;//替换 (1.)
    private LinearLayout ll_broken_code_b_new;//右 布局  带x  (2.)
    private TextView broken_code_b_new;//右 No.12346
    private ImageView broken_code_b_new_delete;  //img x
    private FrameLayout fl_broken_barcode_c_;//整条
    private TextView broken_code_c;//左 No.12346
    private TextView broken_code_c_change;//替换 (1.)
    private LinearLayout ll_broken_code_c_new;//右 布局  带x  (2.)
    private TextView broken_code_c_new;//右 No.12346
    private ImageView broken_code_c_new_delete;  //img x
    private FrameLayout fl_broken_barcode_d_;//整条
    private TextView broken_code_d;//左 No.12346
    private TextView broken_code_d_change;//替换 (1.)
    private LinearLayout ll_broken_code_d_new;//右 布局  带x  (2.)
    private TextView broken_code_d_new;//右 No.12346
    private ImageView broken_code_d_new_delete;  //img x
    //  替换Dialog  内部控件
    private WheelView whv_change;
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


    private ActionBar actionBar;
    private String orderNo;
    private String orderType;
    private String storeId;
    private String userName;
    private String userPhone;
    private String platNumber;
    private String storeName;
    private boolean isShoeConsistent = false;  //轮胎是否一致标记 默认false不一致
    private int fiveYearsShoeAmount = 0;// 满五年更换的轮胎数 默认0
    private int noFiveYearsShoeAmount = 0;// 总轮胎数 默认0
    private List<PublicShoeFlag> shoeFlagList; //shoeFlagListlist
    private List<FreeChangeOldShoeBean> oldShoeList; //旧轮胎list
    private List<FreeChangeOldShoeBean> oldShoeReachFiveList; //旧轮胎满五年list
    private List<FreeChangeOldShoeBean> oldShoeNoFiveList; //旧轮胎达到磨损list
    private List<FreeChangeNewShoeBean> newShoeList; //新轮胎list
    private List<String> list; //新轮胎list --> StrList
    private int currentSelect_a_ = 0;//替换dialog 选择器当前选择位置 默认0 （第一个）
    private int currentSelect_b_ = 0;//替换dialog 选择器当前选择位置 默认0 （第一个）
    private int currentSelect_c_ = 0;//替换dialog 选择器当前选择位置 默认0 （第一个）
    private int currentSelect_d_ = 0;//替换dialog 选择器当前选择位置 默认0 （第一个）
    private int currentImage = 0; //区分拍照flag 1 轮胎a正面照片 2 轮胎a条形码照片 ； 3 轮胎b正面照片 4 轮胎b条形码照片 ； 5..6..；7..8..；9行驶证照片；10 车辆照片；
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
    private String TAG = OrderConfirmFreeChangeActivity.class.getSimpleName();
    private boolean isBucha_a_ = false;//补差dialog是否选择标志位默认未选
    private boolean isBucha_b_ = false;//补差dialog是否选择标志位
    private boolean isBucha_c_ = false;//补差dialog是否选择标志位
    private boolean isBucha_d_ = false;//补差dialog是否选择标志位
    private String buchaBarCode_a_ = "";//补差提交的条形码
    private String buchaBarCode_b_ = "";
    private String buchaBarCode_c_ = "";
    private String buchaBarCode_d_ = "";

    private ProgressDialog progressDialog;
    /*提交参数*/
    private List<OldNewBarCode> oldNewBarCodeList; //新旧BarCode list
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
        setContentView(R.layout.activity_order_confirm_free_change);
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
        /* <--替换按钮监听 */
        RxViewAction.clickNoDouble(full_code_a_change).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (full_code_a_change.getTag() == null || full_code_a_change.getTag().toString().length() == 0) {
                    return;
                }
                String tag = full_code_a_change.getTag().toString();
                showChangeDialog(tag);
            }
        });
        RxViewAction.clickNoDouble(full_code_b_change).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (full_code_b_change.getTag() == null || full_code_b_change.getTag().toString().length() == 0) {
                    return;
                }
                String tag = full_code_b_change.getTag().toString();
                showChangeDialog(tag);
            }
        });
        RxViewAction.clickNoDouble(full_code_c_change).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (full_code_c_change.getTag() == null || full_code_c_change.getTag().toString().length() == 0) {
                    return;
                }
                String tag = full_code_c_change.getTag().toString();
                showChangeDialog(tag);
            }
        });
        RxViewAction.clickNoDouble(full_code_d_change).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (full_code_d_change.getTag() == null || full_code_d_change.getTag().toString().length() == 0) {
                    return;
                }
                String tag = full_code_d_change.getTag().toString();
                showChangeDialog(tag);
            }
        });
        RxViewAction.clickNoDouble(broken_code_a_change).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (broken_code_a_change.getTag() == null || broken_code_a_change.getTag().toString().length() == 0) {
                    return;
                }
                String tag = broken_code_a_change.getTag().toString();
                showChangeDialog(tag);
            }
        });
        RxViewAction.clickNoDouble(broken_code_b_change).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (broken_code_b_change.getTag() == null || broken_code_b_change.getTag().toString().length() == 0) {
                    return;
                }
                String tag = broken_code_b_change.getTag().toString();
                showChangeDialog(tag);
            }
        });
        RxViewAction.clickNoDouble(broken_code_c_change).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (broken_code_c_change.getTag() == null || broken_code_c_change.getTag().toString().length() == 0) {
                    return;
                }
                String tag = broken_code_c_change.getTag().toString();
                showChangeDialog(tag);
            }
        });
        RxViewAction.clickNoDouble(broken_code_d_change).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (broken_code_d_change.getTag() == null || broken_code_d_change.getTag().toString().length() == 0) {
                    return;
                }
                String tag = broken_code_d_change.getTag().toString();
                showChangeDialog(tag);
            }
        });
        //替换 隐藏后 出现的x 取消code 控件监听

        /* 替换按钮监听--> */



        /* <--轮胎照片拍照监听 */
        RxViewAction.clickNoDouble(pic_a_left_center).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (pic_a_titleno.getText() == null || pic_a_titleno.getText().toString().length() == 0) {
                    Toast.makeText(OrderConfirmFreeChangeActivity.this, "请先选择替换轮胎", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (hasPic_a_left) {
                    return;
                }
                String code = pic_a_titleno.getText().toString();
                currentImage = 1;
                showPicInputDialog(code);
            }
        });
        RxViewAction.clickNoDouble(pic_a_right_center).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (pic_a_titleno.getText() == null || pic_a_titleno.getText().toString().length() == 0) {
                    Toast.makeText(OrderConfirmFreeChangeActivity.this, "请先选择替换轮胎", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (hasPic_a_right) {
                    return;
                }
                String code = pic_a_titleno.getText().toString();
                currentImage = 2;
                showPicInputDialog(code);
            }
        });
        RxViewAction.clickNoDouble(pic_b_left_center).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (pic_b_titleno.getText() == null || pic_b_titleno.getText().toString().length() == 0) {
                    Toast.makeText(OrderConfirmFreeChangeActivity.this, "请先选择替换轮胎", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (hasPic_b_left) {
                    return;
                }
                String code = pic_b_titleno.getText().toString();
                currentImage = 3;
                showPicInputDialog(code);
            }
        });
        RxViewAction.clickNoDouble(pic_b_right_center).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (pic_b_titleno.getText() == null || pic_b_titleno.getText().toString().length() == 0) {
                    Toast.makeText(OrderConfirmFreeChangeActivity.this, "请先选择替换轮胎", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (hasPic_b_right) {
                    return;
                }
                String code = pic_b_titleno.getText().toString();
                currentImage = 4;
                showPicInputDialog(code);
            }
        });
        RxViewAction.clickNoDouble(pic_c_left_center).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (pic_c_titleno.getText() == null || pic_c_titleno.getText().toString().length() == 0) {
                    Toast.makeText(OrderConfirmFreeChangeActivity.this, "请先选择替换轮胎", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (hasPic_c_left) {
                    return;
                }
                String code = pic_c_titleno.getText().toString();
                currentImage = 5;
                showPicInputDialog(code);
            }
        });
        RxViewAction.clickNoDouble(pic_c_right_center).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (pic_c_titleno.getText() == null || pic_c_titleno.getText().toString().length() == 0) {
                    Toast.makeText(OrderConfirmFreeChangeActivity.this, "请先选择替换轮胎", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (hasPic_c_right) {
                    return;
                }
                String code = pic_c_titleno.getText().toString();
                currentImage = 6;
                showPicInputDialog(code);
            }
        });
        RxViewAction.clickNoDouble(pic_d_left_center).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (pic_d_titleno.getText() == null || pic_d_titleno.getText().toString().length() == 0) {
                    Toast.makeText(OrderConfirmFreeChangeActivity.this, "请先选择替换轮胎", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (hasPic_d_left) {
                    return;
                }
                String code = pic_d_titleno.getText().toString();
                currentImage = 7;
                showPicInputDialog(code);
            }
        });
        RxViewAction.clickNoDouble(pic_d_right_center).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (pic_d_titleno.getText() == null || pic_d_titleno.getText().toString().length() == 0) {
                    Toast.makeText(OrderConfirmFreeChangeActivity.this, "请先选择替换轮胎", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (hasPic_d_right) {
                    return;
                }
                String code = pic_d_titleno.getText().toString();
                currentImage = 8;
                showPicInputDialog(code);
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
                currentImage = 9;
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
                currentImage = 10;
                showPicInputDialog(code);
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
        });/* 轮胎照片拍照监听--> */


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
        //补差服务
        RxViewAction.clickNoDouble(tv_bottom_b).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (judgeBeforeSave("2")) {
                    showBuchaDialog();
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

    private void showBuchaDialog() {
        AlertDialog dialog = new AlertDialog.Builder(this).create();
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_bucha, null);
        LinearLayout ll_bucha_a_ = (LinearLayout) dialogView.findViewById(R.id.ll_bucha_a_);
        final ImageView img_bucha_a_ = (ImageView) dialogView.findViewById(R.id.img_bucha_a_);
        TextView tv_bucha_a_ = (TextView) dialogView.findViewById(R.id.tv_bucha_a_);
        LinearLayout ll_bucha_b_ = (LinearLayout) dialogView.findViewById(R.id.ll_bucha_b_);
        final ImageView img_bucha_b_ = (ImageView) dialogView.findViewById(R.id.img_bucha_b_);
        TextView tv_bucha_b_ = (TextView) dialogView.findViewById(R.id.tv_bucha_b_);
        LinearLayout ll_bucha_c_ = (LinearLayout) dialogView.findViewById(R.id.ll_bucha_c_);
        final ImageView img_bucha_c_ = (ImageView) dialogView.findViewById(R.id.img_bucha_c_);
        TextView tv_bucha_c_ = (TextView) dialogView.findViewById(R.id.tv_bucha_c_);
        LinearLayout ll_bucha_d_ = (LinearLayout) dialogView.findViewById(R.id.ll_bucha_d_);
        final ImageView img_bucha_d_ = (ImageView) dialogView.findViewById(R.id.img_bucha_d_);
        TextView tv_bucha_d_ = (TextView) dialogView.findViewById(R.id.tv_bucha_d_);
        dialog.setTitle("选择需要补差的轮胎");
        dialog.setIcon(R.drawable.ic_logo_huise);
        ll_bucha_a_.setVisibility(View.GONE);//初始化全部隐藏
        ll_bucha_b_.setVisibility(View.GONE);
        ll_bucha_c_.setVisibility(View.GONE);
        ll_bucha_d_.setVisibility(View.GONE);
        if (noFiveYearsShoeAmount >= 1) {//根据需要补差轮胎的个数显示
            ll_bucha_a_.setVisibility(View.VISIBLE);
            buchaBarCode_a_ = oldShoeNoFiveList.get(0).getBarCode();//提交所需的条形码
            tv_bucha_a_.setText(buchaBarCode_a_);
            //记录dismiss前点击选择的补差条形码
            if (isBucha_a_) {
                img_bucha_a_.setImageResource(R.drawable.ic_check);
            } else {
                img_bucha_a_.setImageResource(R.drawable.ic_notcheck);
            }
            //监听
            RxViewAction.clickNoDouble(img_bucha_a_).subscribe(new Action1<Void>() {
                @Override
                public void call(Void aVoid) {
                    if (!isBucha_a_) {
                        img_bucha_a_.setImageResource(R.drawable.ic_check);
                        isBucha_a_ = true;
                    } else {
                        img_bucha_a_.setImageResource(R.drawable.ic_notcheck);
                        isBucha_a_ = false;
                    }
                }
            });
        }
        if (noFiveYearsShoeAmount >= 2) {
            ll_bucha_b_.setVisibility(View.VISIBLE);
            buchaBarCode_b_ = oldShoeNoFiveList.get(1).getBarCode();
            tv_bucha_b_.setText(buchaBarCode_b_);
            //记录dismiss前点击选择的补差条形码
            if (isBucha_b_) {
                img_bucha_b_.setImageResource(R.drawable.ic_check);
            } else {
                img_bucha_b_.setImageResource(R.drawable.ic_notcheck);
            }
            //监听
            RxViewAction.clickNoDouble(img_bucha_b_).subscribe(new Action1<Void>() {
                @Override
                public void call(Void aVoid) {
                    if (!isBucha_b_) {
                        img_bucha_b_.setImageResource(R.drawable.ic_check);
                        isBucha_b_ = true;
                    } else {
                        img_bucha_b_.setImageResource(R.drawable.ic_notcheck);
                        isBucha_b_ = false;
                    }
                }
            });
        }
        if (noFiveYearsShoeAmount >= 3) {
            ll_bucha_c_.setVisibility(View.VISIBLE);
            buchaBarCode_c_ = oldShoeNoFiveList.get(2).getBarCode();
            tv_bucha_c_.setText(buchaBarCode_c_);
            //记录dismiss前点击选择的补差条形码
            if (isBucha_c_) {
                img_bucha_c_.setImageResource(R.drawable.ic_check);
            } else {
                img_bucha_c_.setImageResource(R.drawable.ic_notcheck);
            }
            //监听
            RxViewAction.clickNoDouble(img_bucha_c_).subscribe(new Action1<Void>() {
                @Override
                public void call(Void aVoid) {
                    if (!isBucha_c_) {
                        img_bucha_c_.setImageResource(R.drawable.ic_check);
                        isBucha_c_ = true;
                    } else {
                        img_bucha_c_.setImageResource(R.drawable.ic_notcheck);
                        isBucha_c_ = false;
                    }
                }
            });
        }
        if (noFiveYearsShoeAmount >= 4) {
            ll_bucha_d_.setVisibility(View.VISIBLE);
            buchaBarCode_d_ = oldShoeNoFiveList.get(3).getBarCode();
            tv_bucha_d_.setText(buchaBarCode_d_);
            //记录dismiss前点击选择的补差条形码
            if (isBucha_d_) {
                img_bucha_d_.setImageResource(R.drawable.ic_check);
            } else {
                img_bucha_d_.setImageResource(R.drawable.ic_notcheck);
            }
            //监听
            RxViewAction.clickNoDouble(img_bucha_d_).subscribe(new Action1<Void>() {
                @Override
                public void call(Void aVoid) {
                    if (!isBucha_d_) {
                        img_bucha_d_.setImageResource(R.drawable.ic_check);
                        isBucha_d_ = true;
                    } else {
                        img_bucha_d_.setImageResource(R.drawable.ic_notcheck);
                        isBucha_d_ = false;
                    }
                }
            });
        }
        dialog.setView(dialogView);
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //先判断是否选择补差轮胎
                if (!isBucha_a_ && !isBucha_b_ && !isBucha_c_ && isBucha_d_) {
                    Toast.makeText(OrderConfirmFreeChangeActivity.this, "请选择需要补差的轮胎!", Toast.LENGTH_SHORT).show();
                    return;
                }
                //再提交
                JSONObject object = new JSONObject();
                try {
                    object.put("orderNo", orderNo);
                    object.put("serviceType", "2");
                    object.put("orderType", orderType);

                } catch (JSONException e) {
                }
                RequestParams params = new RequestParams(UtilsURL.REQUEST_URL + "storeSelectChangeShoeOrderType");
                params.addBodyParameter("reqJson", object.toString());
                params.addBodyParameter("token", new DbConfig().getToken());
                List<OldNewBarCode> c_list = new ArrayList<OldNewBarCode>();
                if (isBucha_a_) {
                    c_list.add(new OldNewBarCode(buchaBarCode_a_, ""));
                }
                if (isBucha_b_) {
                    c_list.add(new OldNewBarCode(buchaBarCode_b_, ""));
                }
                if (isBucha_c_) {
                    c_list.add(new OldNewBarCode(buchaBarCode_c_, ""));
                }
                if (isBucha_d_) {
                    c_list.add(new OldNewBarCode(buchaBarCode_d_, ""));
                }
                params.addBodyParameter("changeBarCodeVoList", c_list.toString());
                x.http().post(params, new Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            String msg = jsonObject.getString("msg");
                            int status = jsonObject.getInt("status");
                            Toast.makeText(OrderConfirmFreeChangeActivity.this, msg, Toast.LENGTH_SHORT).show();
                            if (status == 1) {
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("page", "order");
                                intent.putExtras(bundle);
                                startActivity(intent);
                                finish();
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
        });
        dialog.show();
        //设置按钮颜色
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.theme_primary));
    }

    private boolean judgeBeforeSave(String type) {
        switch (type) {
            case "1"://确认服务
                if (!hasPic_a_left && !hasPic_b_left && !hasPic_c_left && !hasPic_d_left) {
                    showErrorDialog("请补全轮胎正面照!");
                    return false;
                }
                if (!hasPic_a_right && !hasPic_b_right && !hasPic_c_right && !hasPic_d_right) {
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
                //判断补差  --》 dialog--》post
                if (noFiveYearsShoeAmount == 0) {//达到磨损程度的轮胎数量为零则不能补差
                    showErrorDialog("没有可补差的轮胎(达到磨损程度且未满五年的轮胎)!");
                    return false;
                }
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


    private void showOrderDialog(final String type) {
        AlertDialog dialog = new AlertDialog.Builder(this).create();
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_error, null);
        final TextView error_text = (TextView) dialogView.findViewById(R.id.error_text);
        switch (type) {
            case "1":
                error_text.setText("确定确认服务吗?");
                break;
            case "2":
                error_text.setText("确定补差服务吗?");
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
                        RequestParams params = new RequestParams(UtilsURL.REQUEST_URL + "storeSelectChangeShoeOrderType");
                        params.addBodyParameter("reqJson", object.toString());
                        params.addBodyParameter("changeBarCodeVoList", oldNewBarCodeList.toString());
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
                                    Toast.makeText(OrderConfirmFreeChangeActivity.this, msg, Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(OrderConfirmFreeChangeActivity.this, "网络异常,请检查网络", Toast.LENGTH_SHORT).show();
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
                        //补差
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
                        RequestParams params3 = new RequestParams(UtilsURL.REQUEST_URL + "storeSelectChangeShoeOrderType");
                        params3.addBodyParameter("reqJson", object3.toString());
                        params3.addBodyParameter("changeBarCodeVoList", oldNewBarCodeList.toString());
                        params3.addBodyParameter("token", new DbConfig().getToken());
                        x.http().post(params3, new Callback.CommonCallback<String>() {
                            @Override
                            public void onSuccess(String result) {
                                try {
                                    JSONObject jsonObject = new JSONObject(result);
                                    int status = jsonObject.getInt("status");
                                    String msg = jsonObject.getString("msg");
                                    Toast.makeText(OrderConfirmFreeChangeActivity.this, msg, Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(OrderConfirmFreeChangeActivity.this, "网络异常,请检查网络", Toast.LENGTH_SHORT).show();
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
        //1.changeBarCodeVoList
        oldNewBarCodeList.clear();
        if (full_code_a_new.getText() != null && full_code_a_new.getText().length() != 0) {
            String oldCode = full_code_a.getText().toString();
            String newCode = full_code_a_new.getText().toString();
            OldNewBarCode oldNewBarCode = new OldNewBarCode(oldCode, newCode);
            oldNewBarCodeList.add(oldNewBarCode);
        }
        if (full_code_b_new.getText() != null && full_code_b_new.getText().length() != 0) {
            String oldCode = full_code_b.getText().toString();
            String newCode = full_code_b_new.getText().toString();
            OldNewBarCode oldNewBarCode = new OldNewBarCode(oldCode, newCode);
            oldNewBarCodeList.add(oldNewBarCode);
        }
        if (full_code_c_new.getText() != null && full_code_c_new.getText().length() != 0) {
            String oldCode = full_code_c.getText().toString();
            String newCode = full_code_c_new.getText().toString();
            OldNewBarCode oldNewBarCode = new OldNewBarCode(oldCode, newCode);
            oldNewBarCodeList.add(oldNewBarCode);
        }
        if (full_code_d_new.getText() != null && full_code_d_new.getText().length() != 0) {
            String oldCode = full_code_d.getText().toString();
            String newCode = full_code_d_new.getText().toString();
            OldNewBarCode oldNewBarCode = new OldNewBarCode(oldCode, newCode);
            oldNewBarCodeList.add(oldNewBarCode);
        }
        if (broken_code_a_new.getText() != null && broken_code_a_new.getText().length() != 0) {
            String oldCode = broken_code_a.getText().toString();
            String newCode = broken_code_a_new.getText().toString();
            OldNewBarCode oldNewBarCode = new OldNewBarCode(oldCode, newCode);
            oldNewBarCodeList.add(oldNewBarCode);
        }
        if (broken_code_b_new.getText() != null && broken_code_b_new.getText().length() != 0) {
            String oldCode = broken_code_b.getText().toString();
            String newCode = broken_code_b_new.getText().toString();
            OldNewBarCode oldNewBarCode = new OldNewBarCode(oldCode, newCode);
            oldNewBarCodeList.add(oldNewBarCode);
        }
        if (broken_code_c_new.getText() != null && broken_code_c_new.getText().length() != 0) {
            String oldCode = broken_code_c.getText().toString();
            String newCode = broken_code_c_new.getText().toString();
            OldNewBarCode oldNewBarCode = new OldNewBarCode(oldCode, newCode);
            oldNewBarCodeList.add(oldNewBarCode);
        }
        if (broken_code_d_new.getText() != null && broken_code_d_new.getText().length() != 0) {
            String oldCode = broken_code_d.getText().toString();
            String newCode = broken_code_d_new.getText().toString();
            OldNewBarCode oldNewBarCode = new OldNewBarCode(oldCode, newCode);
            oldNewBarCodeList.add(oldNewBarCode);
        }
        //2.照片
        path_licenseBitmap = ImageUtils.savePhoto(licenseBitmap, Environment
                .getExternalStorageDirectory().getAbsolutePath(), "licensePic");
        path_carBitmap = ImageUtils.savePhoto(carBitmap, Environment
                .getExternalStorageDirectory().getAbsolutePath(), "licensePic");
        if (hasPic_a_left) {
            path_shoeABitmap = ImageUtils.savePhoto(shoeAbitmap, Environment
                    .getExternalStorageDirectory().getAbsolutePath(), "shoeAPic");
        }
        if (hasPic_a_right) {
            path_shoeABarcodeBitmap = ImageUtils.savePhoto(shoeACodebitmap, Environment
                    .getExternalStorageDirectory().getAbsolutePath(), "shoeACodePic");
        }
        if (hasPic_b_left) {
            path_shoeBBitmap = ImageUtils.savePhoto(shoeBbitmap, Environment
                    .getExternalStorageDirectory().getAbsolutePath(), "shoeBPic");
        }
        if (hasPic_b_right) {
            path_shoeBBarcodeBitmap = ImageUtils.savePhoto(shoeBCodebitmap, Environment
                    .getExternalStorageDirectory().getAbsolutePath(), "shoeBCodePic");
        }
        if (hasPic_c_left) {
            path_shoeCBitmap = ImageUtils.savePhoto(shoeCbitmap, Environment
                    .getExternalStorageDirectory().getAbsolutePath(), "shoeCPic");
        }
        if (hasPic_c_right) {
            path_shoeCBarcodeBitmap = ImageUtils.savePhoto(shoeCCodebitmap, Environment
                    .getExternalStorageDirectory().getAbsolutePath(), "shoeCCodePic");
        }
        if (hasPic_d_left) {
            path_shoeDBitmap = ImageUtils.savePhoto(shoeDbitmap, Environment
                    .getExternalStorageDirectory().getAbsolutePath(), "shoeDPic");
        }
        if (hasPic_d_right) {
            path_shoeDBarcodeBitmap = ImageUtils.savePhoto(shoeDCodebitmap, Environment
                    .getExternalStorageDirectory().getAbsolutePath(), "shoeDCodePic");
        }

    }

    private void showPicInputDialog(final String code) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("上传照片");
        String[] items = {"拍照"};
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                takePicture(code);
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
            file = new File(Environment
                    .getExternalStorageDirectory(), "shoea" + code + ".jpg");
        } else if (currentImage == 2) {
            file = new File(Environment
                    .getExternalStorageDirectory(), "shoeacode" + code + ".jpg");
        } else if (currentImage == 3) {
            file = new File(Environment
                    .getExternalStorageDirectory(), "shoeb" + code + ".jpg");
        } else if (currentImage == 4) {
            file = new File(Environment
                    .getExternalStorageDirectory(), "shoebcode" + code + ".jpg");
        } else if (currentImage == 5) {
            file = new File(Environment
                    .getExternalStorageDirectory(), "shoec" + code + ".jpg");
        } else if (currentImage == 6) {
            file = new File(Environment
                    .getExternalStorageDirectory(), "shoeccode" + code + ".jpg");
        } else if (currentImage == 7) {
            file = new File(Environment
                    .getExternalStorageDirectory(), "shoed" + code + ".jpg");
        } else if (currentImage == 8) {
            file = new File(Environment
                    .getExternalStorageDirectory(), "shoedcode" + code + ".jpg");
        } else if (currentImage == 9) {
            file = new File(Environment
                    .getExternalStorageDirectory(), "license" + code + ".jpg");
        } else if (currentImage == 10) {
            file = new File(Environment
                    .getExternalStorageDirectory(), "car" + code + ".jpg");
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
        int degree = ImageUtils.readPictureDegree(uri.toString());
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

    private void showChangeDialog(final String tag) {
        View v_category = LayoutInflater.from(this).inflate(R.layout.dialog_category_view, null);
        whv_change = (WheelView) v_category.findViewById(R.id.whv_category);
        whv_change.setItems(getNewShoeCodeList(), 0);
        whv_change.setOnItemSelectedListener(new WheelView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int selectedIndex, String item) {
                switch (tag) {
                    case "1":
                        currentSelect_a_ = selectedIndex;
                        break;
                    case "2":
                        currentSelect_b_ = selectedIndex;
                        break;
                    case "3":
                        currentSelect_c_ = selectedIndex;
                        break;
                    case "4":
                        currentSelect_d_ = selectedIndex;
                        break;
                }
            }
        });
        whv_change.setIsLoop(false);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("选择替换轮胎条形码")
                .setView(v_category);
        final AlertDialog dialog = builder.create();
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (tag) {
                    case "1":
                        pic_a_titleno.setVisibility(View.VISIBLE);
                        pic_a_titleno.setText(list.get(currentSelect_a_));
                        //只有full_a 和 broken_a 可能是1
                        if (full_code_a_change.getTag() != null && full_code_a_change.getTag().toString().equals("1")) {
                            full_code_a_change.setVisibility(View.GONE);
                            ll_full_code_a_new.setVisibility(View.VISIBLE);
                            full_code_a_new.setText(list.get(currentSelect_a_));//新
                            RxViewAction.clickNoDouble(full_code_a_new_delete).subscribe(new Action1<Void>() {
                                @Override
                                public void call(Void aVoid) {
                                    full_code_a_change.setVisibility(View.VISIBLE);
                                    ll_full_code_a_new.setVisibility(View.GONE);
                                    full_code_a_new.setText("");//新重置
                                    pic_a_titleno.setText("");//图片title code重置并隐藏
                                    pic_a_titleno.setVisibility(View.GONE);
                                }
                            });
                        }
                        if (broken_code_a_change.getTag() != null && broken_code_a_change.getTag().toString().equals("1")) {
                            broken_code_a_change.setVisibility(View.GONE);
                            ll_broken_code_a_new.setVisibility(View.VISIBLE);
                            broken_code_a_new.setText(list.get(currentSelect_a_));//新
                            RxViewAction.clickNoDouble(broken_code_a_new_delete).subscribe(new Action1<Void>() {
                                @Override
                                public void call(Void aVoid) {
                                    broken_code_a_change.setVisibility(View.VISIBLE);
                                    ll_broken_code_a_new.setVisibility(View.GONE);
                                    broken_code_a_new.setText("");//新重置
                                    pic_a_titleno.setText("");//图片title code重置并隐藏
                                    pic_a_titleno.setVisibility(View.GONE);
                                }
                            });
                        }
                        break;
                    case "2":
                        pic_b_titleno.setVisibility(View.VISIBLE);
                        pic_b_titleno.setText(list.get(currentSelect_b_));
                        //只有full_b 和 broken_a 和 broken_b可能是2
                        if (full_code_b_change.getTag() != null && full_code_b_change.getTag().toString().equals("2")) {
                            full_code_b_change.setVisibility(View.GONE);
                            ll_full_code_b_new.setVisibility(View.VISIBLE);
                            full_code_b_new.setText(list.get(currentSelect_b_));//新
                            RxViewAction.clickNoDouble(full_code_b_new_delete).subscribe(new Action1<Void>() {
                                @Override
                                public void call(Void aVoid) {
                                    full_code_b_change.setVisibility(View.VISIBLE);
                                    ll_full_code_b_new.setVisibility(View.GONE);
                                    full_code_b_new.setText("");//新重置
                                    pic_b_titleno.setText("");//图片title code重置并隐藏
                                    pic_b_titleno.setVisibility(View.GONE);
                                }
                            });
                        }
                        if (broken_code_a_change.getTag() != null && broken_code_a_change.getTag().toString().equals("2")) {
                            broken_code_a_change.setVisibility(View.GONE);
                            ll_broken_code_a_new.setVisibility(View.VISIBLE);
                            broken_code_a_new.setText(list.get(currentSelect_b_));//新
                            RxViewAction.clickNoDouble(broken_code_a_new_delete).subscribe(new Action1<Void>() {
                                @Override
                                public void call(Void aVoid) {
                                    broken_code_a_change.setVisibility(View.VISIBLE);
                                    ll_broken_code_a_new.setVisibility(View.GONE);
                                    broken_code_a_new.setText("");//新重置
                                    pic_b_titleno.setText("");//图片title code重置并隐藏
                                    pic_b_titleno.setVisibility(View.GONE);
                                }
                            });
                        }
                        if (broken_code_b_change.getTag() != null && broken_code_b_change.getTag().toString().equals("2")) {
                            broken_code_b_change.setVisibility(View.GONE);
                            ll_broken_code_b_new.setVisibility(View.VISIBLE);
                            broken_code_b_new.setText(list.get(currentSelect_b_));//新
                            RxViewAction.clickNoDouble(broken_code_b_new_delete).subscribe(new Action1<Void>() {
                                @Override
                                public void call(Void aVoid) {
                                    broken_code_b_change.setVisibility(View.VISIBLE);
                                    ll_broken_code_b_new.setVisibility(View.GONE);
                                    broken_code_b_new.setText("");//新重置
                                    pic_b_titleno.setText("");//图片title code重置并隐藏
                                    pic_b_titleno.setVisibility(View.GONE);
                                }
                            });
                        }

                        break;
                    case "3":
                        pic_c_titleno.setVisibility(View.VISIBLE);
                        pic_c_titleno.setText(list.get(currentSelect_c_));
                        //只有full_c 和 broken_a 和 broken_b和 broken_c可能是3
                        if (full_code_c_change.getTag() != null && full_code_c_change.getTag().toString().equals("3")) {
                            full_code_c_change.setVisibility(View.GONE);
                            ll_full_code_c_new.setVisibility(View.VISIBLE);
                            full_code_c_new.setText(list.get(currentSelect_c_));//新
                            RxViewAction.clickNoDouble(full_code_c_new_delete).subscribe(new Action1<Void>() {
                                @Override
                                public void call(Void aVoid) {
                                    full_code_c_change.setVisibility(View.VISIBLE);
                                    ll_full_code_c_new.setVisibility(View.GONE);
                                    full_code_c_new.setText("");//新重置
                                    pic_c_titleno.setText("");//图片title code重置并隐藏
                                    pic_c_titleno.setVisibility(View.GONE);
                                }
                            });
                        }
                        if (broken_code_a_change.getTag() != null && broken_code_a_change.getTag().toString().equals("3")) {
                            broken_code_a_change.setVisibility(View.GONE);
                            ll_broken_code_a_new.setVisibility(View.VISIBLE);
                            broken_code_a_new.setText(list.get(currentSelect_c_));//新
                            RxViewAction.clickNoDouble(broken_code_a_new_delete).subscribe(new Action1<Void>() {
                                @Override
                                public void call(Void aVoid) {
                                    broken_code_a_change.setVisibility(View.VISIBLE);
                                    ll_broken_code_a_new.setVisibility(View.GONE);
                                    broken_code_a_new.setText("");//新重置
                                    pic_c_titleno.setText("");//图片title code重置并隐藏
                                    pic_c_titleno.setVisibility(View.GONE);
                                }
                            });
                        }
                        if (broken_code_b_change.getTag() != null && broken_code_b_change.getTag().toString().equals("3")) {
                            broken_code_b_change.setVisibility(View.GONE);
                            ll_broken_code_b_new.setVisibility(View.VISIBLE);
                            broken_code_b_new.setText(list.get(currentSelect_c_));//新
                            RxViewAction.clickNoDouble(broken_code_b_new_delete).subscribe(new Action1<Void>() {
                                @Override
                                public void call(Void aVoid) {
                                    broken_code_b_change.setVisibility(View.VISIBLE);
                                    ll_broken_code_b_new.setVisibility(View.GONE);
                                    broken_code_b_new.setText("");//新重置
                                    pic_c_titleno.setText("");//图片title code重置并隐藏
                                    pic_c_titleno.setVisibility(View.GONE);
                                }
                            });
                        }
                        if (broken_code_c_change.getTag() != null && broken_code_c_change.getTag().toString().equals("3")) {
                            broken_code_c_change.setVisibility(View.GONE);
                            ll_broken_code_c_new.setVisibility(View.VISIBLE);
                            broken_code_c_new.setText(list.get(currentSelect_c_));//新
                            RxViewAction.clickNoDouble(broken_code_c_new_delete).subscribe(new Action1<Void>() {
                                @Override
                                public void call(Void aVoid) {
                                    broken_code_c_change.setVisibility(View.VISIBLE);
                                    ll_broken_code_c_new.setVisibility(View.GONE);
                                    broken_code_c_new.setText("");//新重置
                                    pic_c_titleno.setText("");//图片title code重置并隐藏
                                    pic_c_titleno.setVisibility(View.GONE);
                                }
                            });
                        }
                        break;
                    case "4":
                        pic_d_titleno.setVisibility(View.VISIBLE);
                        pic_d_titleno.setText(list.get(currentSelect_d_));
                        //只有full_d 和 broken_a 和 broken_b和 broken_c和 broken_d可能是4
                        if (full_code_d_change.getTag() != null && full_code_d_change.getTag().toString().equals("4")) {
                            full_code_d_change.setVisibility(View.GONE);
                            ll_full_code_d_new.setVisibility(View.VISIBLE);
                            full_code_d_new.setText(list.get(currentSelect_d_));//新
                            RxViewAction.clickNoDouble(full_code_d_new_delete).subscribe(new Action1<Void>() {
                                @Override
                                public void call(Void aVoid) {
                                    full_code_d_change.setVisibility(View.VISIBLE);
                                    ll_full_code_d_new.setVisibility(View.GONE);
                                    full_code_d_new.setText("");//新重置
                                    pic_d_titleno.setText("");//图片title code重置并隐藏
                                    pic_d_titleno.setVisibility(View.GONE);
                                }
                            });
                        }
                        if (broken_code_a_change.getTag() != null && broken_code_a_change.getTag().toString().equals("4")) {
                            broken_code_a_change.setVisibility(View.GONE);
                            ll_broken_code_a_new.setVisibility(View.VISIBLE);
                            broken_code_a_new.setText(list.get(currentSelect_d_));//新
                            RxViewAction.clickNoDouble(broken_code_a_new_delete).subscribe(new Action1<Void>() {
                                @Override
                                public void call(Void aVoid) {
                                    broken_code_a_change.setVisibility(View.VISIBLE);
                                    ll_broken_code_a_new.setVisibility(View.GONE);
                                    broken_code_a_new.setText("");//新重置
                                    pic_d_titleno.setText("");//图片title code重置并隐藏
                                    pic_d_titleno.setVisibility(View.GONE);
                                }
                            });
                        }
                        if (broken_code_b_change.getTag() != null && broken_code_b_change.getTag().toString().equals("4")) {
                            broken_code_b_change.setVisibility(View.GONE);
                            ll_broken_code_b_new.setVisibility(View.VISIBLE);
                            broken_code_b_new.setText(list.get(currentSelect_d_));//新
                            RxViewAction.clickNoDouble(broken_code_b_new_delete).subscribe(new Action1<Void>() {
                                @Override
                                public void call(Void aVoid) {
                                    broken_code_b_change.setVisibility(View.VISIBLE);
                                    ll_broken_code_b_new.setVisibility(View.GONE);
                                    broken_code_b_new.setText("");//新重置
                                    pic_d_titleno.setText("");//图片title code重置并隐藏
                                    pic_d_titleno.setVisibility(View.GONE);
                                }
                            });
                        }
                        if (broken_code_c_change.getTag() != null && broken_code_c_change.getTag().toString().equals("4")) {
                            broken_code_c_change.setVisibility(View.GONE);
                            ll_broken_code_c_new.setVisibility(View.VISIBLE);
                            broken_code_c_new.setText(list.get(currentSelect_d_));//新
                            RxViewAction.clickNoDouble(broken_code_c_new_delete).subscribe(new Action1<Void>() {
                                @Override
                                public void call(Void aVoid) {
                                    broken_code_c_change.setVisibility(View.VISIBLE);
                                    ll_broken_code_c_new.setVisibility(View.GONE);
                                    broken_code_c_new.setText("");//新重置
                                    pic_d_titleno.setText("");//图片title code重置并隐藏
                                    pic_d_titleno.setVisibility(View.GONE);
                                }
                            });
                        }
                        if (broken_code_d_change.getTag() != null && broken_code_d_change.getTag().toString().equals("4")) {
                            broken_code_d_change.setVisibility(View.GONE);
                            ll_broken_code_d_new.setVisibility(View.VISIBLE);
                            broken_code_d_new.setText(list.get(currentSelect_d_));//新
                            RxViewAction.clickNoDouble(broken_code_d_new_delete).subscribe(new Action1<Void>() {
                                @Override
                                public void call(Void aVoid) {
                                    broken_code_d_change.setVisibility(View.VISIBLE);
                                    ll_broken_code_d_new.setVisibility(View.GONE);
                                    broken_code_d_new.setText("");//新重置
                                    pic_d_titleno.setText("");//图片title code重置并隐藏
                                    pic_d_titleno.setVisibility(View.GONE);
                                }
                            });
                        }
                        break;
                }
            }
        });
        dialog.show();
        //设置按钮颜色
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.theme_primary));

    }


    /*
    * 获取newShoeList --> StrList
    * */
    private List<String> getNewShoeCodeList() {
        list = new ArrayList<>();
        for (int i = 0; i < newShoeList.size(); i++) {
            FreeChangeNewShoeBean bean = newShoeList.get(i);
            list.add(bean.getBarCode());
        }
        return list;
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
                        JSONArray freeChangeOrderVoList = data.getJSONArray("freeChangeOrderVoList");
                        for (int i = 0; i < freeChangeOrderVoList.length(); i++) {//存取前后胎更换位置数量list数据
                            PublicShoeFlag bean = null;
                            JSONObject objBean = (JSONObject) freeChangeOrderVoList.get(i);
                            String fontRearFlag = objBean.getString("fontRearFlag");
                            if (fontRearFlag.equals("0")) {
                                bean = new PublicShoeFlag();
                                isShoeConsistent = true;
                                bean.setShoeImgUrl(objBean.getString("shoeImg"));
                                bean.setShoeName(objBean.getString("shoeName"));
                                bean.setOrderType(orderType);
                                bean.setShoeFlag(objBean.getString("fontRearFlag"));
                                bean.setShoeAmount(objBean.getInt("fontAmount") + "");
                                shoeFlagList.add(bean);
                            }
                            if (fontRearFlag.equals("1")) {
                                bean = new PublicShoeFlag();
                                isShoeConsistent = false;
                                bean.setShoeImgUrl(objBean.getString("shoeImg"));
                                bean.setShoeName(objBean.getString("shoeName"));
                                bean.setOrderType(orderType);
                                bean.setShoeFlag(objBean.getString("fontRearFlag"));
                                bean.setShoeAmount(objBean.getInt("fontAmount") + "");
                                shoeFlagList.add(bean);
                            }
                            if (fontRearFlag.equals("2")) {
                                bean = new PublicShoeFlag();
                                isShoeConsistent = false;
                                bean.setShoeImgUrl(objBean.getString("shoeImg"));
                                bean.setShoeName(objBean.getString("shoeName"));
                                bean.setOrderType(orderType);
                                bean.setShoeFlag(objBean.getString("fontRearFlag"));
                                bean.setShoeAmount(objBean.getInt("rearAmount") + "");
                                shoeFlagList.add(bean);
                            }
                        }
                        JSONArray userCarShoeOldBarCodeList = data.getJSONArray("userCarShoeOldBarCodeList");
                        for (int i = 0; i < userCarShoeOldBarCodeList.length(); i++) {//存取旧轮胎数据
                            FreeChangeOldShoeBean bean = new FreeChangeOldShoeBean();
                            JSONObject objBean = (JSONObject) userCarShoeOldBarCodeList.get(i);
                            bean.setShoeImgUrl(objBean.getString("shoeImgUrl"));
                            bean.setShoeName(objBean.getString("shoeName"));
                            bean.setOrderType(orderType);
                            bean.setFontRearFlag(objBean.getInt("fontRearFlag") + "");
                            bean.setId(objBean.getInt("id") + "");
                            bean.setBarCode(objBean.getString("barCode"));
                            bean.setIsReach5Years(objBean.getInt("isReach5Years") + "");
                            bean.setOrderNo(objBean.getString("orderNo"));
                            bean.setTime(objBean.getLong("time"));
                            bean.setUserCarId(objBean.getInt("userCarId") + "");
                            bean.setUserId(objBean.getInt("userId") + "");
                            oldShoeList.add(bean);
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
                            bean.setTime(objBean.getLong("time"));
                            bean.setStatus(objBean.getInt("status") + "");
                            newShoeList.add(bean);
                        }
                        Log.e(TAG, "onSuccess:   bug在哪里 4");
                        Log.e(TAG, "onSuccess: " + "onSuccess");

                        //设置数据
                        setData();
                        //绑定监听
                        bindView();


                    } else {
                        Toast.makeText(OrderConfirmFreeChangeActivity.this, msg, Toast.LENGTH_SHORT).show();
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

    private void setData() {
        //设置外层数据
        user_name.setText(userName);
        user_phone.setText(userPhone);
        car_num.setText(platNumber);
        store_name.setText(storeName);
        order_num.setText(orderNo);
        //设置内层数据
        /*1.带图前后胎、数量数据(shoeFlagList)*/
        setShoeFlagData();
        /*2.满五年、达到磨损程度数据(oldShoeList)*/
        setOldShoeData();
        /*3.轮胎照片个数设置*/
        if (fiveYearsShoeAmount + noFiveYearsShoeAmount == 1) {
            ll_pic_a.setVisibility(View.VISIBLE);
            ll_pic_b.setVisibility(View.GONE);
            ll_pic_c.setVisibility(View.GONE);
            ll_pic_d.setVisibility(View.GONE);
        }
        if (fiveYearsShoeAmount + noFiveYearsShoeAmount == 2) {
            ll_pic_a.setVisibility(View.VISIBLE);
            ll_pic_b.setVisibility(View.VISIBLE);
            ll_pic_c.setVisibility(View.GONE);
            ll_pic_d.setVisibility(View.GONE);
        }
        if (fiveYearsShoeAmount + noFiveYearsShoeAmount == 3) {
            ll_pic_a.setVisibility(View.VISIBLE);
            ll_pic_b.setVisibility(View.VISIBLE);
            ll_pic_c.setVisibility(View.VISIBLE);
            ll_pic_d.setVisibility(View.GONE);
        }
        if (fiveYearsShoeAmount + noFiveYearsShoeAmount == 4) {
            ll_pic_a.setVisibility(View.VISIBLE);
            ll_pic_b.setVisibility(View.VISIBLE);
            ll_pic_c.setVisibility(View.VISIBLE);
            ll_pic_d.setVisibility(View.VISIBLE);
        }

    }

    private void setOldShoeData() {
        //分离数据
        for (int i = 0; i < oldShoeList.size(); i++) {
            FreeChangeOldShoeBean bean = oldShoeList.get(i);
            if (bean.getIsReach5Years().equals("1")) {
                oldShoeReachFiveList.add(bean);
            }
            if (bean.getIsReach5Years().equals("0")) {
                oldShoeNoFiveList.add(bean);
            }
        }
        //分别设置数据
        if (oldShoeReachFiveList != null) {
            fiveYearsShoeAmount = oldShoeReachFiveList.size();/*轮胎满五年条数*/
        }
        if (oldShoeNoFiveList != null) {
            noFiveYearsShoeAmount = oldShoeNoFiveList.size();/*轮胎不满五年条数*/
        }
        if (oldShoeReachFiveList.size() == 0) {//满五年
            fl_full_change_title.setVisibility(View.GONE);
            ll_full_change_content.setVisibility(View.GONE);
        }
        if (oldShoeReachFiveList.size() == 1) {
            fl_full_change_title.setVisibility(View.VISIBLE);
            ll_full_change_content.setVisibility(View.VISIBLE);
            fl_full_barcode_a_.setVisibility(View.VISIBLE);
            fl_full_barcode_b_.setVisibility(View.GONE);
            fl_full_barcode_c_.setVisibility(View.GONE);
            fl_full_barcode_d_.setVisibility(View.GONE);
            full_code_a.setText(oldShoeReachFiveList.get(0).getBarCode());
            full_code_a_change.setTag("1");
            /*private TextView full_code_a;//左 No.12346
             private TextView full_code_a_change;//替换 (1.)
             private LinearLayout ll_full_code_a_new;//右 布局  带x  (2.)
             private TextView full_code_a_new;//右 No.12346
             private ImageView full_code_a_new_delete;  //img x*/

        }
        if (oldShoeReachFiveList.size() == 2) {
            fl_full_change_title.setVisibility(View.VISIBLE);
            ll_full_change_content.setVisibility(View.VISIBLE);
            fl_full_barcode_a_.setVisibility(View.VISIBLE);
            fl_full_barcode_b_.setVisibility(View.VISIBLE);
            fl_full_barcode_c_.setVisibility(View.GONE);
            fl_full_barcode_d_.setVisibility(View.GONE);
            full_code_a.setText(oldShoeReachFiveList.get(0).getBarCode());
            full_code_b.setText(oldShoeReachFiveList.get(1).getBarCode());
            full_code_a_change.setTag("1");
            full_code_b_change.setTag("2");
        }
        if (oldShoeReachFiveList.size() == 3) {
            fl_full_change_title.setVisibility(View.VISIBLE);
            ll_full_change_content.setVisibility(View.VISIBLE);
            fl_full_barcode_a_.setVisibility(View.VISIBLE);
            fl_full_barcode_b_.setVisibility(View.VISIBLE);
            fl_full_barcode_c_.setVisibility(View.VISIBLE);
            fl_full_barcode_d_.setVisibility(View.GONE);
            full_code_a.setText(oldShoeReachFiveList.get(0).getBarCode());
            full_code_b.setText(oldShoeReachFiveList.get(1).getBarCode());
            full_code_c.setText(oldShoeReachFiveList.get(2).getBarCode());
            full_code_a_change.setTag("1");
            full_code_b_change.setTag("2");
            full_code_c_change.setTag("3");
        }
        if (oldShoeReachFiveList.size() == 4) {
            fl_full_change_title.setVisibility(View.VISIBLE);
            ll_full_change_content.setVisibility(View.VISIBLE);
            fl_full_barcode_a_.setVisibility(View.VISIBLE);
            fl_full_barcode_b_.setVisibility(View.VISIBLE);
            fl_full_barcode_c_.setVisibility(View.VISIBLE);
            fl_full_barcode_d_.setVisibility(View.VISIBLE);
            full_code_a.setText(oldShoeReachFiveList.get(0).getBarCode());
            full_code_b.setText(oldShoeReachFiveList.get(1).getBarCode());
            full_code_c.setText(oldShoeReachFiveList.get(2).getBarCode());
            full_code_d.setText(oldShoeReachFiveList.get(3).getBarCode());
            full_code_a_change.setTag("1");
            full_code_b_change.setTag("2");
            full_code_c_change.setTag("3");
            full_code_d_change.setTag("4");
        }
        if (oldShoeNoFiveList.size() == 0) {//达到破损程度
            fl_broken_change_title.setVisibility(View.GONE);
            ll_broken_change_content.setVisibility(View.GONE);
        }
        if (oldShoeNoFiveList.size() == 1) {
            fl_broken_change_title.setVisibility(View.VISIBLE);
            ll_broken_change_content.setVisibility(View.VISIBLE);
            fl_broken_barcode_a_.setVisibility(View.VISIBLE);
            fl_broken_barcode_b_.setVisibility(View.GONE);
            fl_broken_barcode_c_.setVisibility(View.GONE);
            fl_broken_barcode_d_.setVisibility(View.GONE);
            broken_code_a.setText(oldShoeNoFiveList.get(0).getBarCode());
            broken_code_a_change.setTag(fiveYearsShoeAmount + 1 + "");

        }
        if (oldShoeNoFiveList.size() == 2) {
            fl_broken_change_title.setVisibility(View.VISIBLE);
            ll_broken_change_content.setVisibility(View.VISIBLE);
            fl_broken_barcode_a_.setVisibility(View.VISIBLE);
            fl_broken_barcode_b_.setVisibility(View.VISIBLE);
            fl_broken_barcode_c_.setVisibility(View.GONE);
            fl_broken_barcode_d_.setVisibility(View.GONE);
            broken_code_a.setText(oldShoeNoFiveList.get(0).getBarCode());
            broken_code_b.setText(oldShoeNoFiveList.get(1).getBarCode());
            broken_code_a_change.setTag(fiveYearsShoeAmount + 1 + "");
            broken_code_b_change.setTag(fiveYearsShoeAmount + 2 + "");
        }
        if (oldShoeNoFiveList.size() == 3) {
            fl_broken_change_title.setVisibility(View.VISIBLE);
            ll_broken_change_content.setVisibility(View.VISIBLE);
            fl_broken_barcode_a_.setVisibility(View.VISIBLE);
            fl_broken_barcode_b_.setVisibility(View.VISIBLE);
            fl_broken_barcode_c_.setVisibility(View.VISIBLE);
            fl_broken_barcode_d_.setVisibility(View.GONE);
            broken_code_a.setText(oldShoeNoFiveList.get(0).getBarCode());
            broken_code_b.setText(oldShoeNoFiveList.get(1).getBarCode());
            broken_code_c.setText(oldShoeNoFiveList.get(2).getBarCode());
            broken_code_a_change.setTag(fiveYearsShoeAmount + 1 + "");
            broken_code_b_change.setTag(fiveYearsShoeAmount + 2 + "");
            broken_code_c_change.setTag(fiveYearsShoeAmount + 3 + "");
        }
        if (oldShoeNoFiveList.size() == 4) {
            fl_broken_change_title.setVisibility(View.VISIBLE);
            ll_broken_change_content.setVisibility(View.VISIBLE);
            fl_broken_barcode_a_.setVisibility(View.VISIBLE);
            fl_broken_barcode_b_.setVisibility(View.VISIBLE);
            fl_broken_barcode_c_.setVisibility(View.VISIBLE);
            fl_broken_barcode_d_.setVisibility(View.VISIBLE);
            broken_code_a.setText(oldShoeNoFiveList.get(0).getBarCode());
            broken_code_b.setText(oldShoeNoFiveList.get(1).getBarCode());
            broken_code_c.setText(oldShoeNoFiveList.get(2).getBarCode());
            broken_code_d.setText(oldShoeNoFiveList.get(3).getBarCode());
            broken_code_a_change.setTag(fiveYearsShoeAmount + 1 + "");
            broken_code_b_change.setTag(fiveYearsShoeAmount + 2 + "");
            broken_code_c_change.setTag(fiveYearsShoeAmount + 3 + "");
            broken_code_d_change.setTag(fiveYearsShoeAmount + 4 + "");
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
                    ll_shoe_font.setVisibility(View.VISIBLE);
                    ll_shoe_rear.setVisibility(View.GONE);
                    ll_shoe_consistent.setVisibility(View.GONE);
                    shoe_name_font.setText(bean.getShoeName());
                    shoe_amount_font.setText(bean.getShoeAmount());
                    Glide.with(getApplicationContext()).load(bean.getShoeImgUrl()).into(shoe_img_font);
                }
                if (shoeFlagList.get(i).getShoeFlag().equals("2")) {
                    ll_shoe_font.setVisibility(View.GONE);
                    ll_shoe_rear.setVisibility(View.VISIBLE);
                    ll_shoe_consistent.setVisibility(View.GONE);
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
        tv_bottom_a = findViewById(R.id.tv_bottom_a);
        tv_bottom_b = findViewById(R.id.tv_bottom_b);
        tv_bottom_c = findViewById(R.id.tv_bottom_c);
        user_name = findViewById(R.id.user_name);
        user_phone = findViewById(R.id.user_phone);
        car_num = findViewById(R.id.car_num);
        store_name = findViewById(R.id.store_name);
        order_num = findViewById(R.id.order_num);
        user_phone_img = findViewById(R.id.user_phone_img);
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
        //满五年
        fl_full_change_title = findViewById(R.id.fl_full_change_title);
        ll_full_change_content = findViewById(R.id.ll_full_change_content);
        fl_full_barcode_a_ = findViewById(R.id.fl_full_barcode_a_);
        full_code_a = findViewById(R.id.full_code_a);
        full_code_a_change = findViewById(R.id.full_code_a_change);
        ll_full_code_a_new = findViewById(R.id.ll_full_code_a_new);
        full_code_a_new = findViewById(R.id.full_code_a_new);
        full_code_a_new_delete = findViewById(R.id.full_code_a_new_delete);
        fl_full_barcode_b_ = findViewById(R.id.fl_full_barcode_b_);
        full_code_b = findViewById(R.id.full_code_b);
        full_code_b_change = findViewById(R.id.full_code_b_change);
        ll_full_code_b_new = findViewById(R.id.ll_full_code_b_new);
        full_code_b_new = findViewById(R.id.full_code_b_new);
        full_code_b_new_delete = findViewById(R.id.full_code_b_new_delete);
        fl_full_barcode_c_ = findViewById(R.id.fl_full_barcode_c_);
        full_code_c = findViewById(R.id.full_code_c);
        full_code_c_change = findViewById(R.id.full_code_c_change);
        ll_full_code_c_new = findViewById(R.id.ll_full_code_c_new);
        full_code_c_new = findViewById(R.id.full_code_c_new);
        full_code_c_new_delete = findViewById(R.id.full_code_c_new_delete);
        fl_full_barcode_d_ = findViewById(R.id.fl_full_barcode_d_);
        full_code_d = findViewById(R.id.full_code_d);
        full_code_d_change = findViewById(R.id.full_code_d_change);
        ll_full_code_d_new = findViewById(R.id.ll_full_code_d_new);
        full_code_d_new = findViewById(R.id.full_code_d_new);
        full_code_d_new_delete = findViewById(R.id.full_code_d_new_delete);
        //达到磨损程度
        fl_broken_change_title = findViewById(R.id.fl_broken_change_title);
        ll_broken_change_content = findViewById(R.id.ll_broken_change_content);
        fl_broken_barcode_a_ = findViewById(R.id.fl_broken_barcode_a_);
        broken_code_a = findViewById(R.id.broken_code_a);
        broken_code_a_change = findViewById(R.id.broken_code_a_change);
        ll_broken_code_a_new = findViewById(R.id.ll_broken_code_a_new);
        broken_code_a_new = findViewById(R.id.broken_code_a_new);
        broken_code_a_new_delete = findViewById(R.id.broken_code_a_new_delete);
        fl_broken_barcode_b_ = findViewById(R.id.fl_broken_barcode_b_);
        broken_code_b = findViewById(R.id.broken_code_b);
        broken_code_b_change = findViewById(R.id.broken_code_b_change);
        ll_broken_code_b_new = findViewById(R.id.ll_broken_code_b_new);
        broken_code_b_new = findViewById(R.id.broken_code_b_new);
        broken_code_b_new_delete = findViewById(R.id.broken_code_b_new_delete);
        fl_broken_barcode_c_ = findViewById(R.id.fl_broken_barcode_c_);
        broken_code_c = findViewById(R.id.broken_code_c);
        broken_code_c_change = findViewById(R.id.broken_code_c_change);
        ll_broken_code_c_new = findViewById(R.id.ll_broken_code_c_new);
        broken_code_c_new = findViewById(R.id.broken_code_c_new);
        broken_code_c_new_delete = findViewById(R.id.broken_code_c_new_delete);
        fl_broken_barcode_d_ = findViewById(R.id.fl_broken_barcode_d_);
        broken_code_d = findViewById(R.id.broken_code_d);
        broken_code_d_change = findViewById(R.id.broken_code_d_change);
        ll_broken_code_d_new = findViewById(R.id.ll_broken_code_d_new);
        broken_code_d_new = findViewById(R.id.broken_code_d_new);
        broken_code_d_new_delete = findViewById(R.id.broken_code_d_new_delete);
        //change 标题 barcode
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

        shoeFlagList = new ArrayList<>();
        oldShoeList = new ArrayList<>();
        oldShoeReachFiveList = new ArrayList<>();
        oldShoeNoFiveList = new ArrayList<>();
        newShoeList = new ArrayList<>();
        oldNewBarCodeList = new ArrayList<>();

        progressDialog = new ProgressDialog(OrderConfirmFreeChangeActivity.this);
    }
}
