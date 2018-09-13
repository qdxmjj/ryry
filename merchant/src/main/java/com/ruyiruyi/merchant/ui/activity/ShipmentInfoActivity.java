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
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.ruyiruyi.merchant.R;
import com.ruyiruyi.merchant.db.DbConfig;
import com.ruyiruyi.merchant.db.model.User;
import com.ruyiruyi.merchant.utils.CircleImageView;
import com.ruyiruyi.merchant.utils.UtilsRY;
import com.ruyiruyi.merchant.utils.UtilsURL;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;
import com.ruyiruyi.rylibrary.base.BaseActivity;
import com.ruyiruyi.rylibrary.cell.ActionBar;
import com.ruyiruyi.rylibrary.image.ImageUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.io.IOException;

import id.zelory.compressor.Compressor;
import rx.functions.Action1;


public class ShipmentInfoActivity extends BaseActivity {

    private String orderNo;
    private String platNumber;
    private long orderTime;
    private String userPhone;
    private boolean isConsistent;
    private String shoeTitle_front;
    private String shoeTitle_rear;
    private int shoeNum_front;
    private int shoeNum_rear;
    private String orderImg_front;
    private String orderImg_rear;
    private int tyreId_front;
    private int tyreId_rear;
    private int fontRearFlag_front;
    private int fontRearFlag_rear;
    private double shoePrice_front;
    private double shoePrice_rear;
    private int orderId;
    private ActionBar mActionBar;
    private ProgressDialog mDialog;
    /*初始数据*/
    private TextView orderno;
    private TextView carnum;
    private TextView ordertime;
    private TextView userphone;

    private TextView tv_front_type;
    private TextView tv_front_num;

    private TextView shoetype_front;
    private TextView shoenum_front;
    private TextView shoetype_rear;
    private TextView shoenum_rear;

    //轮胎型号和数量
    private LinearLayout ll_front_numandtype;
    private LinearLayout ll_rear_numandtype;

    /*图片条形码*/
    private LinearLayout ll_pic_front1;
    private LinearLayout ll_pic_front2;
    private LinearLayout ll_pic_rear1;
    private LinearLayout ll_pic_rear2;

    private CircleImageView shoepic_front1;
    private ImageView add_front1;
    private ImageView del_front1;
    private EditText et_tiaoma_front1;
    private ImageView img_saoma_front1;

    private CircleImageView shoepic_front2;
    private ImageView add_front2;
    private ImageView del_front2;
    private EditText et_tiaoma_front2;
    private ImageView img_saoma_front2;

    private CircleImageView shoepic_rear1;
    private ImageView add_rear1;
    private ImageView del_rear1;
    private EditText et_tiaoma_rear1;
    private ImageView img_saoma_rear1;

    private CircleImageView shoepic_rear2;
    private ImageView add_rear2;
    private ImageView del_rear2;
    private EditText et_tiaoma_rear2;
    private ImageView img_saoma_rear2;

    private TextView shipment_save;

    private boolean hasPicFront1;
    private boolean hasPicFront2;
    private boolean hasPicRear1;
    private boolean hasPicRear2;
    private int currentImage; // 1-->front1   2 -->front2   3 -->rear1   4 -->rear2
    private int currentScan; // 1-->front1   2 -->front2   3 -->rear1   4 -->rear2
    private final int CHOOSE_PICTURE = 0;
    private final int TAKE_PICTURE = 1;
    private final String TAG = ShipmentInfoActivity.class.getSimpleName();
    protected static Uri tempUri;
    private String path_;
    private Bitmap shoeFrontBitmap1;
    private Bitmap shoeFrontBitmap2;
    private Bitmap shoeRearBitmap1;
    private Bitmap shoeRearBitmap2;

    private String shoeFront1Path;
    private String shoeFront2Path;
    private String shoeRear1Path;
    private String shoeRear2Path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_shipment_info);
        mActionBar = (ActionBar) findViewById(R.id.m_acbar);
        mActionBar.setTitle("发货订单详情");
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
        //获取传递数据
        Intent intent = getIntent();
        orderNo = intent.getStringExtra("orderNo");
        platNumber = intent.getStringExtra("platNumber");
        orderTime = intent.getLongExtra("orderTime", 0);
        userPhone = intent.getStringExtra("userPhone");
        isConsistent = intent.getBooleanExtra("isConsistent", false);
        shoeTitle_front = intent.getStringExtra("shoeTitle_front");
        shoeTitle_rear = intent.getStringExtra("shoeTitle_rear");
        shoeNum_front = intent.getIntExtra("shoeNum_front", 0);
        shoeNum_rear = intent.getIntExtra("shoeNum_rear", 0);
        orderImg_front = intent.getStringExtra("orderImg_front");
        orderImg_rear = intent.getStringExtra("orderImg_rear");
        tyreId_front = intent.getIntExtra("tyreId_front", -1);
        tyreId_rear = intent.getIntExtra("tyreId_rear", -1);
        fontRearFlag_front = intent.getIntExtra("fontRearFlag_front", -1);
        fontRearFlag_rear = intent.getIntExtra("fontRearFlag_rear", -1);
        shoePrice_front = intent.getDoubleExtra("shoePrice_front", -1);
        shoePrice_rear = intent.getDoubleExtra("shoePrice_rear", -1);
        orderId = intent.getIntExtra("orderId", -1);

        initView();
        bindView();
        initData();
        bindData();
    }

    private void bindData() {
        orderno.setText(orderNo);
        carnum.setText(platNumber);
        String timestampToStringAll = new UtilsRY().getTimestampToStringAll(orderTime);
        ordertime.setText(timestampToStringAll);
        userphone.setText(userPhone);
        if (isConsistent) {//前后一致情况
            //1.型号和数量
            ll_front_numandtype.setVisibility(View.VISIBLE);
            tv_front_type.setText("轮胎型号");
            tv_front_num.setText("轮胎数量");
            shoetype_front.setText(shoeTitle_front);
            shoenum_front.setText(shoeNum_front + "");

        } else {//前后一不致情况
            if (shoeNum_front > 0) {
                ll_front_numandtype.setVisibility(View.VISIBLE);
                shoetype_front.setText(shoeTitle_front);
                shoenum_front.setText(shoeNum_front + "");
            }
            if (shoeNum_rear > 0) {
                ll_rear_numandtype.setVisibility(View.VISIBLE);
                shoetype_rear.setText(shoeTitle_rear);
                shoenum_rear.setText(shoeNum_rear + "");
            }
        }
        //2.照片和条形码
        for (int i = 0; i < shoeNum_front; i++) {
            if (i == 0) {
                Log.e(TAG, "bindData: " + i);
                ll_pic_front1.setVisibility(View.VISIBLE);
            }
            if (i == 1) {
                Log.e(TAG, "bindData: " + i);
                ll_pic_front2.setVisibility(View.VISIBLE);
            }
            if (i == 2) {
                Log.e(TAG, "bindData: " + i);
                ll_pic_rear1.setVisibility(View.VISIBLE);
            }
            if (i == 3) {
                Log.e(TAG, "bindData: " + i);
                ll_pic_rear2.setVisibility(View.VISIBLE);
            }
        }
        for (int i = 0; i < shoeNum_rear; i++) {
            if (i == 0) {
                Log.e(TAG, "bindData: " + i);
                ll_pic_rear1.setVisibility(View.VISIBLE);
            }
            if (i == 1) {
                Log.e(TAG, "bindData: " + i);
                ll_pic_rear2.setVisibility(View.VISIBLE);
            }
        }
    }

    private void bindView() {
        /*轮胎条形码拍照*/
        RxViewAction.clickNoDouble(add_front1).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (hasPicFront1) {
                    return;
                }
                //拍照
                currentImage = 1;
                showPicInputDialog();
            }
        });
        RxViewAction.clickNoDouble(add_front2).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (hasPicFront2) {
                    return;
                }
                //拍照
                currentImage = 2;
                showPicInputDialog();
            }
        });
        RxViewAction.clickNoDouble(add_rear1).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (hasPicRear1) {
                    return;
                }
                //拍照
                currentImage = 3;
                showPicInputDialog();
            }
        });
        RxViewAction.clickNoDouble(add_rear2).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (hasPicRear2) {
                    return;
                }
                //拍照
                currentImage = 4;
                showPicInputDialog();
            }
        });
        /*删除*/
        RxViewAction.clickNoDouble(del_front1).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                shoepic_front1.setImageResource(R.drawable.tv_dashline);
                del_front1.setVisibility(View.GONE);
                add_front1.setVisibility(View.VISIBLE);
                hasPicFront1 = false;
            }
        });
        RxViewAction.clickNoDouble(del_front2).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                shoepic_front2.setImageResource(R.drawable.tv_dashline);
                del_front2.setVisibility(View.GONE);
                add_front2.setVisibility(View.VISIBLE);
                hasPicFront2 = false;
            }
        });
        RxViewAction.clickNoDouble(del_rear1).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                shoepic_rear1.setImageResource(R.drawable.tv_dashline);
                del_rear1.setVisibility(View.GONE);
                add_rear1.setVisibility(View.VISIBLE);
                hasPicRear1 = false;
            }
        });
        RxViewAction.clickNoDouble(del_rear2).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                shoepic_rear2.setImageResource(R.drawable.tv_dashline);
                del_rear2.setVisibility(View.GONE);
                add_rear2.setVisibility(View.VISIBLE);
                hasPicRear2 = false;
            }
        });
        /*扫码*/
        RxViewAction.clickNoDouble(img_saoma_front1).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                //扫码
                currentScan = 1;
                scanCode();
            }
        });
        RxViewAction.clickNoDouble(img_saoma_front2).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                //扫码
                currentScan = 2;
                scanCode();
            }
        });
        RxViewAction.clickNoDouble(img_saoma_rear1).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                //扫码
                currentScan = 3;
                scanCode();
            }
        });
        RxViewAction.clickNoDouble(img_saoma_rear2).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                //扫码
                currentScan = 4;
                scanCode();
            }
        });
        /*保存*/
        RxViewAction.clickNoDouble(shipment_save).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                //确认操作
                if (!isPassjudgebeforesave()) {
                    return;
                }
                showSaveDialog("确认提交发货信息吗?");
            }
        });
    }

    /*
    * 提交前判断
    * */
    private boolean isPassjudgebeforesave() {
        if ((hasPicFront1 && et_tiaoma_front1.getText().length() == 0) || (hasPicFront2 && et_tiaoma_front2.getText().length() == 0) || (hasPicRear1 && et_tiaoma_rear1.getText().length() == 0) || (hasPicRear2 && et_tiaoma_rear2.getText().length() == 0)) {
            Toast.makeText(this, "请补全轮胎条形码信息", Toast.LENGTH_SHORT).show();
            return false;
        }
        int currentPicNum = 0;
        if (hasPicFront1) {
            currentPicNum++;
        }
        if (hasPicFront2) {
            currentPicNum++;
        }
        if (hasPicRear1) {
            currentPicNum++;
        }
        if (hasPicRear2) {
            currentPicNum++;
        }
        if (currentPicNum < (shoeNum_front + shoeNum_rear)) {
            Toast.makeText(this, "请补全轮胎照片信息", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    /**
     * 扫描条形码
     */
    private void scanCode() {
        IntentIntegrator integrator = new IntentIntegrator(ShipmentInfoActivity.this);
        // 设置要扫描的条码类型，ONE_D_CODE_TYPES：一维码，QR_CODE_TYPES-二维码
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
        integrator.setCaptureActivity(ShipmentScanActivity.class); //设置打开摄像头的Activity
        integrator.setPrompt("请扫描轮胎条形码"); //底部的提示文字，设为""可以置空
        integrator.setCameraId(0); //前置或者后置摄像头
        integrator.setBeepEnabled(true); //扫描成功的「哔哔」声，默认开启
        integrator.setBarcodeImageEnabled(true);
        integrator.initiateScan();
    }

    private void initData() {

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
        if (currentImage == 1) {
            file = new File(
                    this.getObbDir().getAbsolutePath()
                    , "tyre1.jpg");
            path_ = file.getPath();
        } else if (currentImage == 2) {
            file = new File(
                    this.getObbDir().getAbsolutePath()
                    , "tyre2.jpg");
            path_ = file.getPath();
        } else if (currentImage == 3) {
            file = new File(
                    this.getObbDir().getAbsolutePath()
                    , "tyre3.jpg");
            path_ = file.getPath();
        } else if (currentImage == 4) {
            file = new File(
                    this.getObbDir().getAbsolutePath()
                    , "tyre4.jpg");
            path_ = file.getPath();
        }

        //判断是否是AndroidN以及更高的版本
        if (Build.VERSION.SDK_INT >= 24) {
            openCameraIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            tempUri = FileProvider.getUriForFile(ShipmentInfoActivity.this, "com.ruyiruyi.merchant.fileProvider", file);
        } else {
            tempUri = Uri.fromFile(new File(
                    this.getObbDir().getAbsolutePath()
                    , "image.jpg"));
        }
        Log.e(TAG, "takePicture: " + tempUri);
        // 指定照片保存路径（SD卡），image.jpg为一个临时文件，每次拍照后这个图片都会被替换
        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);
        startActivityForResult(openCameraIntent, TAKE_PICTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e(TAG, "onActivityResult:resultCode " + resultCode + "requestcode" + requestCode);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CHOOSE_PICTURE:
                    Uri uri = data.getData();
                    setImageToViewFromPhone(uri, false);
                    break;
                case TAKE_PICTURE:
                    setImageToViewFromPhone(tempUri, true);
                    break;
                default://条形码扫描后
                    IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
                    if (scanResult != null) {
                        String result = scanResult.getContents();
                        if (currentScan == 1) {
                            et_tiaoma_front1.setText(result);
                        } else if (currentScan == 2) {
                            et_tiaoma_front2.setText(result);
                        } else if (currentScan == 3) {
                            et_tiaoma_rear1.setText(result);
                        } else if (currentScan == 4) {
                            et_tiaoma_rear2.setText(result);
                        }
                    }
                    break;

            }
        }
    }

    //未剪辑照片
    private void setImageToViewFromPhone(Uri uri, boolean isCamera) {
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

                shoeFrontBitmap1 = rotaingImageView(degree, photo);

                shoepic_front1.setImageBitmap(shoeFrontBitmap1);
                del_front1.setVisibility(View.VISIBLE);
                add_front1.setVisibility(View.GONE);
                hasPicFront1 = true;
            } else if (currentImage == 2) {

                shoeFrontBitmap2 = rotaingImageView(degree, photo);

                shoepic_front2.setImageBitmap(shoeFrontBitmap2);
                del_front2.setVisibility(View.VISIBLE);
                add_front2.setVisibility(View.GONE);
                hasPicFront2 = true;
            } else if (currentImage == 3) {

                shoeRearBitmap1 = rotaingImageView(degree, photo);

                shoepic_rear1.setImageBitmap(shoeRearBitmap1);
                del_rear1.setVisibility(View.VISIBLE);
                add_rear1.setVisibility(View.GONE);
                hasPicRear1 = true;
            } else if (currentImage == 4) {

                shoeRearBitmap2 = rotaingImageView(degree, photo);

                shoepic_rear2.setImageBitmap(shoeRearBitmap2);
                del_rear2.setVisibility(View.VISIBLE);
                add_rear2.setVisibility(View.GONE);
                hasPicRear2 = true;
            }
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
         /*初始数据*/
        orderno = findViewById(R.id.orderno);
        carnum = findViewById(R.id.carnum);
        ordertime = findViewById(R.id.ordertime);
        userphone = findViewById(R.id.userphone);

        tv_front_type = findViewById(R.id.tv_front_type);
        tv_front_num = findViewById(R.id.tv_front_num);

        shoetype_front = findViewById(R.id.shoetype_front);
        shoenum_front = findViewById(R.id.shoenum_front);
        shoetype_rear = findViewById(R.id.shoetype_rear);
        shoenum_rear = findViewById(R.id.shoenum_rear);

        /*轮胎型号和数量*/
        ll_front_numandtype = findViewById(R.id.ll_front_numandtype);
        ll_rear_numandtype = findViewById(R.id.ll_rear_numandtype);

        /*图片条形码*/
        ll_pic_front1 = findViewById(R.id.ll_pic_front1);
        ll_pic_front2 = findViewById(R.id.ll_pic_front2);
        ll_pic_rear1 = findViewById(R.id.ll_pic_rear1);
        ll_pic_rear2 = findViewById(R.id.ll_pic_rear2);

        shoepic_front1 = findViewById(R.id.shoepic_front1);
        add_front1 = findViewById(R.id.add_front1);
        del_front1 = findViewById(R.id.del_front1);
        et_tiaoma_front1 = findViewById(R.id.et_tiaoma_front1);
        img_saoma_front1 = findViewById(R.id.img_saoma_front1);

        shoepic_front2 = findViewById(R.id.shoepic_front2);
        add_front2 = findViewById(R.id.add_front2);
        del_front2 = findViewById(R.id.del_front2);
        et_tiaoma_front2 = findViewById(R.id.et_tiaoma_front2);
        img_saoma_front2 = findViewById(R.id.img_saoma_front2);

        shoepic_rear1 = findViewById(R.id.shoepic_rear1);
        add_rear1 = findViewById(R.id.add_rear1);
        del_rear1 = findViewById(R.id.del_rear1);
        et_tiaoma_rear1 = findViewById(R.id.et_tiaoma_rear1);
        img_saoma_rear1 = findViewById(R.id.img_saoma_rear1);

        shoepic_rear2 = findViewById(R.id.shoepic_rear2);
        add_rear2 = findViewById(R.id.add_rear2);
        del_rear2 = findViewById(R.id.del_rear2);
        et_tiaoma_rear2 = findViewById(R.id.et_tiaoma_rear2);
        img_saoma_rear2 = findViewById(R.id.img_saoma_rear2);

        shipment_save = findViewById(R.id.shipment_save);

        mDialog = new ProgressDialog(this);

    }


    private void showSaveDialog(String error) {
        final AlertDialog dialog = new AlertDialog.Builder(this).create();
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_error, null);
        TextView error_text = (TextView) dialogView.findViewById(R.id.error_text);
        error_text.setText(error);
        dialog.setTitle("如意如驿商家版");
        dialog.setIcon(R.drawable.ic_launcher);
        dialog.setView(dialogView);
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "再看看", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialog.dismiss();
            }
        });
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "是的", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                showDialogProgress(mDialog, "信息提交中...");

                File file_front1 = null;
                File file_front2 = null;
                File file_rear1 = null;
                File file_rear2 = null;
                try {
                    if (hasPicFront1) {
                        shoeFront1Path = ImageUtils.savePhoto(shoeFrontBitmap1,
                                getApplicationContext().getObbDir().getAbsolutePath()
                                , "shoeFront1");
                        file_front1 = new Compressor(getApplicationContext()).compressToFile(new File(shoeFront1Path));
                    }
                    if (hasPicFront2) {
                        shoeFront2Path = ImageUtils.savePhoto(shoeFrontBitmap2,
                                getApplicationContext().getObbDir().getAbsolutePath()
                                , "shoeFront2");
                        file_front2 = new Compressor(getApplicationContext()).compressToFile(new File(shoeFront2Path));
                    }
                    if (hasPicRear1) {
                        shoeRear1Path = ImageUtils.savePhoto(shoeRearBitmap1,
                                getApplicationContext().getObbDir().getAbsolutePath()
                                , "shoeRear1");
                        file_rear1 = new Compressor(getApplicationContext()).compressToFile(new File(shoeRear1Path));
                    }
                    if (hasPicRear2) {
                        shoeRear2Path = ImageUtils.savePhoto(shoeRearBitmap2,
                                getApplicationContext().getObbDir().getAbsolutePath()
                                , "shoeRear2");
                        file_rear2 = new Compressor(getApplicationContext()).compressToFile(new File(shoeRear2Path));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                //提交发货信息post
                RequestParams params = new RequestParams(UtilsURL.REQUEST_URL_FAHUO + "orderInfo/updateOrderPostStatusByStore");
                User user = new DbConfig(ShipmentInfoActivity.this).getUser();
                String storeName = user.getStoreName();
                int storeId = user.getId();
                params.addBodyParameter("storeName", storeName);
                params.addBodyParameter("storeId", storeId + "");
                params.addBodyParameter("id", orderId + "");
                params.addBodyParameter("no", orderNo);
                params.addBodyParameter("phone", userPhone);
                params.addBodyParameter("imgUrl", file_front1);
                params.addBodyParameter("imgUrl2", file_front2);
                params.addBodyParameter("imgUrl3", file_rear1);
                params.addBodyParameter("imgUrl4", file_rear2);
                if (hasPicFront1) {
                    /*params.addBodyParameter("imgUrl", file_front1);*/
                    params.addBodyParameter("barcode1", et_tiaoma_front1.getText().toString());
                    params.addBodyParameter("tyreName1", shoeTitle_front);
                    params.addBodyParameter("shoeId1", tyreId_front + "");
                    params.addBodyParameter("flag1", fontRearFlag_front + "");
                    params.addBodyParameter("tyrePrice1", shoePrice_front + "");
                    params.addBodyParameter("tyreImgUrl1", orderImg_front);
                }
                if (hasPicFront2) {
                    /*params.addBodyParameter("imgUrl2", file_front2);*/
                    params.addBodyParameter("barcode2", et_tiaoma_front2.getText().toString());
                    params.addBodyParameter("tyreName2", shoeTitle_front);
                    params.addBodyParameter("shoeId2", tyreId_front + "");
                    params.addBodyParameter("flag2", fontRearFlag_front + "");
                    params.addBodyParameter("tyrePrice2", shoePrice_front + "");
                    params.addBodyParameter("tyreImgUrl2", orderImg_front);
                }
                if (hasPicRear1) {
                    if (fontRearFlag_rear > 0) {//有后轮
                       /* params.addBodyParameter("imgUrl3", file_rear1);*/
                        params.addBodyParameter("barcode3", et_tiaoma_rear1.getText().toString());
                        params.addBodyParameter("tyreName3", shoeTitle_rear);
                        params.addBodyParameter("shoeId3", tyreId_rear + "");
                        params.addBodyParameter("flag3", fontRearFlag_rear + "");
                        params.addBodyParameter("tyrePrice3", shoePrice_rear + "");
                        params.addBodyParameter("tyreImgUrl3", orderImg_rear);
                    } else {//没有后轮则前后轮一致用前轮数据
                        /*params.addBodyParameter("imgUrl3", file_rear1);*/
                        params.addBodyParameter("barcode3", et_tiaoma_rear1.getText().toString());
                        params.addBodyParameter("tyreName3", shoeTitle_front);
                        params.addBodyParameter("shoeId3", tyreId_front + "");
                        params.addBodyParameter("flag3", fontRearFlag_front + "");
                        params.addBodyParameter("tyrePrice3", shoePrice_front + "");
                        params.addBodyParameter("tyreImgUrl3", orderImg_front);
                    }
                }
                if (hasPicRear2) {
                    if (fontRearFlag_rear > 0) {//有后轮
                        /*params.addBodyParameter("imgUrl4", file_rear2);*/
                        params.addBodyParameter("barcode4", et_tiaoma_rear2.getText().toString());
                        params.addBodyParameter("tyreName4", shoeTitle_rear);
                        params.addBodyParameter("shoeId4", tyreId_rear + "");
                        params.addBodyParameter("flag4", fontRearFlag_rear + "");
                        params.addBodyParameter("tyrePrice4", shoePrice_rear + "");
                        params.addBodyParameter("tyreImgUrl4", orderImg_rear);
                    } else {//没有后轮则前后轮一致用前轮数据
                        /*params.addBodyParameter("imgUrl4", file_rear2);*/
                        params.addBodyParameter("barcode4", et_tiaoma_rear2.getText().toString());
                        params.addBodyParameter("tyreName4", shoeTitle_front);
                        params.addBodyParameter("shoeId4", tyreId_front + "");
                        params.addBodyParameter("flag4", fontRearFlag_front + "");
                        params.addBodyParameter("tyrePrice4", shoePrice_front + "");
                        params.addBodyParameter("tyreImgUrl4", orderImg_front);
                    }
                }
                Log.e(TAG, "onClick:  params =  " + params.toString());
                x.http().post(params, new Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        try {
                            Log.e(TAG, "onSuccess: result2 = " + result);
                            JSONObject object = new JSONObject(result);
                            String msg = object.getString("msg");
                            boolean isSuccess = object.getBoolean("isSuccess");
                            Toast.makeText(ShipmentInfoActivity.this, msg, Toast.LENGTH_SHORT).show();
                            if (isSuccess) {
                                //发货成功
                                finish();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {
                        Toast.makeText(ShipmentInfoActivity.this, "请检查网络", Toast.LENGTH_SHORT).show();
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
        dialog.show();
        //设置按钮颜色
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.theme_primary));
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.theme_primary));
    }


    @Override
    public void onBackPressed() {
        finish();
    }
}
