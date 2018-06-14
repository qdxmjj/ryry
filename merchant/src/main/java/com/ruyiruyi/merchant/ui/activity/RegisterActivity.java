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
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.ruyiruyi.merchant.MyApplication;
import com.ruyiruyi.merchant.R;
import com.ruyiruyi.merchant.bean.XiangmusBean;
import com.ruyiruyi.merchant.db.DbConfig;
import com.ruyiruyi.merchant.db.model.Category;
import com.ruyiruyi.merchant.db.model.Province;
import com.ruyiruyi.merchant.db.model.ServiceType;
import com.ruyiruyi.merchant.utils.UtilsRY;
import com.ruyiruyi.merchant.utils.UtilsURL;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;
import com.ruyiruyi.rylibrary.base.BaseActivity;
import com.ruyiruyi.rylibrary.cell.ActionBar;
import com.ruyiruyi.rylibrary.image.ImageUtils;
import com.ruyiruyi.rylibrary.ui.cell.WheelView;
import com.ruyiruyi.rylibrary.utils.TripleDESUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.DbManager;
import org.xutils.common.Callback;
import org.xutils.ex.DbException;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import rx.functions.Action1;

public class RegisterActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener {
    private EditText et_userName;
    private EditText et_userPhone;
    private TextView tv_getCode;
    private EditText et_Code;
    private ImageView img_CodeRight;
    private EditText et_Passworda;
    private EditText et_Passwordb;
    private EditText et_Shopname;
    private TextView tv_ShopCategory;
    private EditText et_ShopPhone;
    private TextView tv_ShopTime;
    private TextView tv_ShopCity;
    private EditText et_ShopLocation;
    private TextView tv_ShopPoint;
    private ImageView img_yyzz;
    private ImageView img_yyzz_delete;
    private ImageView img_mdpic_a;
    private ImageView img_mdpic_a_delete;
    private ImageView img_mdpic_b;
    private ImageView img_mdpic_b_delete;
    private ImageView img_mdpic_c;
    private ImageView img_mdpic_c_delete;
    private ImageView img_shou_a;
    private ImageView img_shou_a_delete;
    private ImageView img_yyzz_center;
    private ImageView img_mdpic_a_center;
    private ImageView img_mdpic_b_center;
    private ImageView img_mdpic_c_center;
    private ImageView img_shou_a_center;
    private CheckBox ckbox_sl;
    private CheckBox ckbox_bsl;
    private TextView tv_save;
    private WheelView whv_sheng, whv_shi, whv_xian;
    private WheelView whv_category;
    private WheelView whv_lTime, whv_rTime;
    //add XMs  !!!
    private LinearLayout ll_xms;
    private List<XiangmusBean> list_xms = new ArrayList<>();

    private ActionBar mActionBar;
    private TimeCount mTimeCount;
    private String TAG = RegisterActivity.class.getSimpleName();
    private List<String> shengList;
    private List<Category> categoryList;
    private List<String> shiList;
    private List<String> xianList;
    public String currentSheng = "北京市";
    public String currentShi = "北京市";
    public String currentXian = "东城区";
    public int currentShengPosition = 0;
    public int currentShiPosition = 0;
    public int currentXianPosition = 0;
    public String currentlTime = "00:00:00";
    public String currentrTime = "00:00:00";
    public int currentlTimePosition = 0;
    public int currentrTimePosition = 0;
    private boolean isChecdXieyi = false;
    private int areaId = 9999;
    private int areaId_shi = 8888;
    private List<String> lTime_list;
    private List<String> rTime_list;
    private int currentImage = 0;//3-yyzz,0-mdpica,1-mdpicb,2-mdpicc,4-shou;
    private boolean hasPic_yyzz = false;
    private boolean hasPic_mdPic_a = false;
    private boolean hasPic_mdPic_b = false;
    private boolean hasPic_mdPic_c = false;
    private boolean hasPic_shou_a = false;
    private final int CHOOSE_PICTURE = 0;
    private final int TAKE_PICTURE = 1;
    private Bitmap mdPicaBitmap;
    private Bitmap mdPicbBitmap;
    private Bitmap mdPiccBitmap;
    private Bitmap yyzzPicBitmap;
    private Bitmap shouPicBitmap;
    private CheckBox checkbox_xieyi;
    protected static Uri tempUri;
    public static final int MAP_REUEST_CODE = 2;
    private double latitude_double;
    private double longitude_double;
    private LocationClient mLocationClient;
    private String cityAddress;
    private List<String> serviceTypeList = new ArrayList<String>();
    //提交参数---<
    private String persionName;
    private String persionPhone;
    private String code;
    private String passWord_a;
    private String passWord_b;
    private String shopName;
    private String shopCategoryId;
    private String shopPhone;
    private String shopTimeL = "00:00:00";
    private String shopTimeR = "00:00:00";
    private String positionId_xian;
    private String cityId;
    private String shopLocation;
    private String longitude;
    private String latitude;
    private String yyzzPath;
    private String mdpicaPath;
    private String mdpicbPath;
    private String mdpiccPath;
    private String shouPath;
    private String serviceTypeListString;
    private int isShoulian;//0 no 1 yes ;
    private int isRightCode = 0;// 0 wrong 1 right;
    //提交参数>---
    private ProgressDialog progressDialog;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    img_CodeRight.setVisibility(View.VISIBLE);
                    img_CodeRight.setImageResource(R.drawable.ic_right);
                    et_Code.setFocusable(false);
                    et_userPhone.setFocusable(false);
                    isRightCode = 1;
                    break;
            }
        }
    };
    private String areStr = "";
    private String path_;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_register);

        mActionBar = (ActionBar) findViewById(R.id.acbar_rigister);
        mActionBar.setTitle("门店注册");
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

        //下载数据
        initRegisterCategoryData();
        initRegisterServiceTypeData();
        initView();
        textAddXMView();
        shengList = new ArrayList<>();
        shiList = new ArrayList<>();
        xianList = new ArrayList<>();

        getSheng();
        getShi();
        getXian();
        bindView();
    }

    private void initRegisterCategoryData() {
// 0       date_category = new Date();
        List<Category> categoryList = null;
        try {
            categoryList = new DbConfig().getDbManager().selector(Category.class).orderBy("time").findAll();

        } catch (DbException e) {
        }
        JSONObject object = new JSONObject();

        try {
            if (categoryList == null) {
                object.put("time", "2000-00-00 00:00:00");
            } else {
                String time = categoryList.get(categoryList.size() - 1).getTime();
                object.put("time", time);

            }
        } catch (JSONException e) {
        }

        RequestParams params = new RequestParams(UtilsURL.REQUEST_URL + "getStoreType");
        params.addBodyParameter("reqJson", object.toString());
//        Log.e(TAG, "initRegisterCategoryData: ------------1");
        x.http().post(params, new Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
//                        Log.e(TAG, "initRegisterCategoryData: ------------2 result = " + result);
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(result);
                            String status = jsonObject.getString("status");
                            String msg = jsonObject.getString("msg");
                            JSONArray data = jsonObject.getJSONArray("data");
                            saveCategory(data);
//                            Log.e(TAG, "initRegisterCategoryData: ------------3 data = " + data);
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
                }

        );
    }

    private void saveCategory(JSONArray data) {
        categoryList = new ArrayList<>();
        Category category;
        for (int i = 0; i < data.length(); i++) {
            category = new Category();
            try {
                JSONObject obj = (JSONObject) data.get(i);
                long time = obj.getLong("time");
                String timestampToStringAll = new UtilsRY().getTimestampToStringAll(time);
                category.setTime(timestampToStringAll);
                category.setColor(obj.getString("color"));
                category.setId(obj.getInt("id"));
                category.setName(obj.getString("name"));
                categoryList.add(category);
            } catch (JSONException e) {
            }
        }
    }

    private void initRegisterServiceTypeData() {
//   0     date_serviceType = new Date();
        List<ServiceType> serviceTypeList = null;
        try {
            serviceTypeList = new DbConfig().getDbManager().selector(ServiceType.class).orderBy("time").findAll();

        } catch (DbException e) {
        }
        JSONObject object = new JSONObject();

        try {
            if (serviceTypeList == null) {
                object.put("time", "2000-00-00 00:00:00");
            } else {
                String time = serviceTypeList.get(serviceTypeList.size() - 1).getTime();
                object.put("time", time);

            }
        } catch (JSONException e) {
        }

        RequestParams params = new RequestParams(UtilsURL.REQUEST_URL + "getStoreServiceType");
        params.addBodyParameter("reqJson", object.toString());
        x.http().post(params, new Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(result);
                            String status = jsonObject.getString("status");
                            String msg = jsonObject.getString("msg");
                            JSONArray data = jsonObject.getJSONArray("data");
                            saveServiceType(data);
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
                }

        );
    }

    private void saveServiceType(JSONArray data) {
        Log.e(TAG, "saveServiceType: data.toString() 0= " + data.toString());
        XiangmusBean xiangmusBean;
        for (int i = 0; i < data.length(); i++) {
            xiangmusBean = new XiangmusBean();
            try {
                JSONObject obj = (JSONObject) data.get(i);
                long time = obj.getLong("time");
                String timestampToStringAll = new UtilsRY().getTimestampToStringAll(time);
                xiangmusBean.setTime(timestampToStringAll);
                xiangmusBean.setColor(obj.getString("color"));
                xiangmusBean.setId(obj.getInt("id"));
                xiangmusBean.setName(obj.getString("name"));
//                Log.e(TAG, "saveServiceType: xiangmusBean.getName() = "+xiangmusBean.getName() );
                list_xms.add(xiangmusBean);
//                Log.e(TAG, "saveServiceType: list_xms.toString()0 = " +list_xms.toString() );
            } catch (JSONException e) {
            }
        }
        //下载数据完成后再动态添加View
        textAddXMView();
    }

    private void textAddXMView() {

        if (list_xms == null) {
//            Log.e(TAG, "textAddXMView: list_xms.toString()3 = " +list_xms.toString() );
            Toast.makeText(RegisterActivity.this, "请检查网络", Toast.LENGTH_SHORT).show();
        } else {
//            Log.e(TAG, "textAddXMView: list_xms.toString()4 = " +list_xms.toString() );
            ll_xms = (LinearLayout) findViewById(R.id.ll_xiangmus);
            for (int i = 0; i < (list_xms.size() % 3 == 0 ? (list_xms.size() / 3) : (list_xms.size() / 3 + 1)); i++) {
                View view = LayoutInflater.from(RegisterActivity.this).inflate(R.layout.register_xm_add_item, null);
                CheckBox checkBoxa = (CheckBox) view.findViewById(R.id.checkbox_a);
                CheckBox checkBoxb = (CheckBox) view.findViewById(R.id.checkbox_b);
                CheckBox checkBoxc = (CheckBox) view.findViewById(R.id.checkbox_c);
                checkBoxa.setText(list_xms.get(i * 3 + 0).getName());
                checkBoxa.setTag(list_xms.get(i * 3 + 0));
                checkBoxa.setOnCheckedChangeListener(this);

                checkBoxb.setText((i * 3 + 1 >= list_xms.size()) ? "" : list_xms.get(i * 3 + 1).getName());
                checkBoxb.setVisibility((i * 3 + 1 >= list_xms.size()) ? View.GONE : View.VISIBLE);
                checkBoxb.setTag((i * 3 + 1 >= list_xms.size()) ? null : list_xms.get(i * 3 + 1));
                checkBoxb.setOnCheckedChangeListener(this);

                checkBoxc.setText((i * 3 + 2 >= list_xms.size()) ? "" : list_xms.get(i * 3 + 2).getName());
                checkBoxc.setVisibility((i * 3 + 2 >= list_xms.size()) ? View.GONE : View.VISIBLE);
                checkBoxc.setTag((i * 3 + 2 >= list_xms.size()) ? null : list_xms.get(i * 3 + 2));
                checkBoxc.setOnCheckedChangeListener(this);

                ll_xms.addView(view);
            }
        }

    }

    private void bindView() {
        RxViewAction.clickNoDouble(tv_getCode)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        String phone = et_userPhone.getText().toString();
                        if (phone.isEmpty()) {
                            showDialog("手机号不能为空");
                        } else if (!UtilsRY.isMobile(phone)) {
                            showDialog("手机号格式错误");
                        } else {
                            getCode(phone);
                        }
                    }
                });
        et_Code.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (et_Code.getText().length() == 6) {
                    persionPhone = et_userPhone.getText().toString();
                    code = et_Code.getText().toString();
                    Log.e(TAG, "afterTextChanged: code------------" + code + " persionPhone " + persionPhone);
                    JSONObject object = new JSONObject();
                    try {
                        object.put("phone", persionPhone);
                        object.put("code", code);
                    } catch (JSONException e) {
                    }
                    RequestParams params = new RequestParams(UtilsURL.REQUEST_URL + "verificationCode");
                    params.addBodyParameter("reqJson", object.toString());
                    Log.e(TAG, "afterTextChanged:---------------------------- " + params.toString());
                    x.http().post(params, new Callback.CommonCallback<String>() {
                        @Override
                        public void onSuccess(String result) {
                            Log.e(TAG, "onSuccess: -----------------------------------" + result);
                            JSONObject jsonObject = null;
                            try {
                                jsonObject = new JSONObject(result);
                            } catch (JSONException e) {
                            }
                            String status = null;
                            try {
                                status = jsonObject.getString("status");
                                String msg = jsonObject.getString("msg");
                                if (status.equals("1")) {
                                    Message message = Message.obtain();
                                    message.what = 0;
                                    mHandler.sendMessage(message);
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

                } else if (et_Code.getText().length() == 0) {
                    img_CodeRight.setVisibility(View.GONE);
                } else {
                    img_CodeRight.setImageResource(R.drawable.ic_wrong);
                    img_CodeRight.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    public void registerPicClick(View view) {
        switch (view.getId()) {
            case R.id.img_mdpic_a:
                if (hasPic_mdPic_a) {
                    return;
                }
                currentImage = 0;
                showPicInputDialog();
                break;
            case R.id.img_mdpic_b:
                if (hasPic_mdPic_b) {
                    return;
                }
                currentImage = 1;
                showPicInputDialog();
                break;
            case R.id.img_mdpic_c:
                if (hasPic_mdPic_c) {
                    return;
                }
                currentImage = 2;
                showPicInputDialog();
                break;
            case R.id.img_yyzz:
                if (hasPic_yyzz) {
                    return;
                }
                currentImage = 3;
                showPicInputDialog();
                break;
            case R.id.img_shou_a:
                if (hasPic_shou_a) {
                    return;
                }
                currentImage = 4;
                showPicInputDialogShou();
                break;


            case R.id.img_mdpic_a_delete:
                img_mdpic_a_delete.setVisibility(View.GONE);
                img_mdpic_a_center.setVisibility(View.VISIBLE);
                img_mdpic_a.setImageResource(R.drawable.img_bg_dark);
                hasPic_mdPic_a = false;
                break;
            case R.id.img_mdpic_b_delete:
                img_mdpic_b_delete.setVisibility(View.GONE);
                img_mdpic_b_center.setVisibility(View.VISIBLE);
                img_mdpic_b.setImageResource(R.drawable.img_bg_dark);
                hasPic_mdPic_b = false;
                break;
            case R.id.img_mdpic_c_delete:
                img_mdpic_c_delete.setVisibility(View.GONE);
                img_mdpic_c_center.setVisibility(View.VISIBLE);
                img_mdpic_c.setImageResource(R.drawable.img_bg_dark);
                hasPic_mdPic_c = false;
                break;
            case R.id.img_yyzz_delete:
                img_yyzz_delete.setVisibility(View.GONE);
                img_yyzz_center.setVisibility(View.VISIBLE);
                img_yyzz.setImageResource(R.drawable.img_bg_dark);
                hasPic_yyzz = false;
                break;
            case R.id.img_shou_a_delete:
                img_shou_a_delete.setVisibility(View.GONE);
                img_shou_a_center.setVisibility(View.VISIBLE);
                img_shou_a.setImageResource(R.drawable.img_bg_dark);
                hasPic_shou_a = false;
                break;
        }
    }

    private void showPicInputDialogShou() {
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
        if (currentImage == 0) {
            file = new File(Environment
                    .getExternalStorageDirectory(), "mdpicaaa.jpg");
            path_ = file.getPath();
        } else if (currentImage == 1) {
            file = new File(Environment
                    .getExternalStorageDirectory(), "mdpicbbb.jpg");
            path_ = file.getPath();
        } else if (currentImage == 2) {
            file = new File(Environment
                    .getExternalStorageDirectory(), "mdpicccc.jpg");
            path_ = file.getPath();
        } else if (currentImage == 3) {
            file = new File(Environment
                    .getExternalStorageDirectory(), "yyzz.jpg");
            path_ = file.getPath();
        } else if (currentImage == 4) {
            file = new File(Environment
                    .getExternalStorageDirectory(), "shou.jpg");
            path_ = file.getPath();
        }

        //判断是否是AndroidN以及更高的版本
        if (Build.VERSION.SDK_INT >= 24) {
            openCameraIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            tempUri = FileProvider.getUriForFile(RegisterActivity.this, "com.ruyiruyi.merchant.fileProvider", file);
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
        Log.e(TAG, "onActivityResult:resultCode " + resultCode + "requestcode" + requestCode);
        if (resultCode == MAP_REUEST_CODE) {
            longitude = data.getStringExtra("longitude");
            latitude = data.getStringExtra("latitude");
            cityAddress = data.getStringExtra("cityAddress");
            tv_ShopPoint.setText(cityAddress);
//            Toast.makeText(this, "经度=" + longitude + "纬度=" + latitude + "cityAddress=" + cityAddress, Toast.LENGTH_SHORT).show();
        }
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
            degree = ImageUtils.readPictureDegree(path_);
        }
        if (uri != null) {
            Bitmap photo = null;
            try {
                photo = ImageUtils.getBitmapFormUri(getApplicationContext(), uri);
            } catch (IOException e) {
            }
            if (currentImage == 0) {
                if (isCamera) {
                    mdPicaBitmap = rotaingImageView(degree, photo);
                } else {
                    mdPicaBitmap = photo;
                }
                img_mdpic_a.setImageBitmap(mdPicaBitmap);
                img_mdpic_a_delete.setVisibility(View.VISIBLE);
                img_mdpic_a_center.setVisibility(View.GONE);
                hasPic_mdPic_a = true;
            } else if (currentImage == 1) {
                if (isCamera) {
                    mdPicbBitmap = rotaingImageView(degree, photo);
                } else {
                    mdPicbBitmap = photo;
                }
                img_mdpic_b.setImageBitmap(mdPicbBitmap);
                img_mdpic_b_delete.setVisibility(View.VISIBLE);
                img_mdpic_b_center.setVisibility(View.GONE);
                hasPic_mdPic_b = true;
            } else if (currentImage == 2) {
                if (isCamera) {
                    mdPiccBitmap = rotaingImageView(degree, photo);
                } else {
                    mdPiccBitmap = photo;
                }
                img_mdpic_c.setImageBitmap(mdPiccBitmap);
                img_mdpic_c_delete.setVisibility(View.VISIBLE);
                img_mdpic_c_center.setVisibility(View.GONE);
                hasPic_mdPic_c = true;
            } else if (currentImage == 3) {
                if (isCamera) {
                    yyzzPicBitmap = rotaingImageView(degree, photo);
                } else {
                    yyzzPicBitmap = photo;
                }
                img_yyzz.setImageBitmap(yyzzPicBitmap);
                img_yyzz_delete.setVisibility(View.VISIBLE);
                img_yyzz_center.setVisibility(View.GONE);
                hasPic_yyzz = true;
            } else if (currentImage == 4) {
                if (isCamera) {
                    shouPicBitmap = rotaingImageView(degree, photo);
                } else {
                    shouPicBitmap = photo;
                }
                img_shou_a.setImageBitmap(shouPicBitmap);
                img_shou_a_delete.setVisibility(View.VISIBLE);
                img_shou_a_center.setVisibility(View.GONE);
                hasPic_shou_a = true;
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

    public void registerclick(View view) {
        switch (view.getId()) {
            case R.id.tv_shopcategory:
                View v_category = LayoutInflater.from(this).inflate(R.layout.dialog_category_view, null);
                whv_category = (WheelView) v_category.findViewById(R.id.whv_category);
                whv_category.setItems(categorylistToStringlist(categoryList), 0);
                whv_category.setOnItemSelectedListener(new WheelView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(int selectedIndex, String item) {
                    }
                });
                whv_category.setIsLoop(false);
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("选择门店类别")
                        .setView(v_category);
                final AlertDialog dialog = builder.create();
                dialog.setButton(DialogInterface.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        tv_ShopCategory.setText(whv_category.getSelectedItem());
                        shopCategoryId = (whv_category.getSelectedPosition() + 1) + "";
                    }
                });
                dialog.show();
                //设置按钮颜色
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.theme_primary));
                break;
            case R.id.tv_shoptime:
                currentlTime = "00:00:00";//每次进入dialog初始化
                currentrTime = "00:00:00";//每次进入dialog初始化
                View v_shoptime = LayoutInflater.from(this).inflate(R.layout.dialog_shoptime_view, null);
                whv_lTime = (WheelView) v_shoptime.findViewById(R.id.whv_ltime);
                whv_rTime = (WheelView) v_shoptime.findViewById(R.id.whv_rtime);
                whv_lTime.setItems(getStrLTime(), currentlTimePosition);
                whv_rTime.setItems(getRTimeList(0), 0);
                whv_lTime.setOnItemSelectedListener(new WheelView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(int selectedIndex, String item) {
                        currentlTimePosition = whv_lTime.getSelectedPosition();
                        rTime_list = getRTimeList(selectedIndex);
                        whv_rTime.setItems(rTime_list, 0);
                        currentlTime = item;
                        currentrTime = rTime_list.get(0);
                    }
                });
                whv_rTime.setItems(rTime_list, currentrTimePosition);
                whv_rTime.setOnItemSelectedListener(new WheelView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(int selectedIndex, String item) {
                        currentrTimePosition = whv_rTime.getSelectedPosition();
                        currentrTime = item;
                    }
                });
                whv_lTime.setIsLoop(false);
                whv_rTime.setIsLoop(false);
                AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
                builder2.setTitle("选择营业时间")
                        .setView(v_shoptime);
                final AlertDialog dialog2 = builder2.create();
                dialog2.setButton(DialogInterface.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        shopTimeL = currentlTime;//点击确定再更新值
                        shopTimeR = currentrTime;//点击确定再更新值
                        tv_ShopTime.setText(currentlTime + " 至 " + currentrTime);
                    }
                });
                dialog2.show();
                //设置按钮颜色
                dialog2.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.theme_primary));
                break;
            case R.id.tv_shopcity:
                View v_city = LayoutInflater.from(this).inflate(R.layout.dialog_city_view, null);
                whv_sheng = (WheelView) v_city.findViewById(R.id.whview_city_sheng);
                whv_shi = (WheelView) v_city.findViewById(R.id.whview_city_shi);
                whv_xian = (WheelView) v_city.findViewById(R.id.whview_city_xian);

                whv_sheng.setItems(shengList, currentShengPosition);//初始0
                whv_sheng.setOnItemSelectedListener(new WheelView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(int selectedIndex, String item) {
                        currentShengPosition = whv_sheng.getSelectedPosition();
                        currentSheng = whv_sheng.getSelectedItem();
                        getShi();
                        whv_shi.setItems(shiList, currentShiPosition);
                        currentShi = whv_shi.getSelectedItem();
                        getXian();
                        whv_xian.setItems(xianList, currentXianPosition);
                    }
                });

                whv_shi.setItems(shiList, currentShiPosition);
                whv_shi.setOnItemSelectedListener(new WheelView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(int selectedIndex, String item) {
                        currentShiPosition = whv_shi.getSelectedPosition();
                        currentShi = whv_shi.getSelectedItem();
                        getXian();
                        whv_xian.setItems(xianList, currentXianPosition);
                    }
                });
                whv_xian.setItems(xianList, currentXianPosition);
                whv_xian.setOnItemSelectedListener(new WheelView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(int selectedIndex, String item) {
                        currentXianPosition = whv_xian.getSelectedPosition();
                    }
                });
                whv_sheng.setIsLoop(false);
                whv_shi.setIsLoop(false);
                whv_xian.setIsLoop(false);
                AlertDialog.Builder builder3 = new AlertDialog.Builder(this);
                builder3.setTitle("选择所在城市")
                        .setView(v_city);
                final AlertDialog dialog3 = builder3.create();
                dialog3.setButton(DialogInterface.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String sheng = whv_sheng.getSelectedItem();
                        String shi = whv_shi.getSelectedItem();
                        String xian = whv_xian.getSelectedItem();
                        if (xian.equals("无")) {
                            areStr = sheng + shi;
                        } else {
                            areStr = sheng + shi + xian;
                        }
                        Log.e(TAG, "onClick: areStr = " + areStr);
                        tv_ShopCity.setText(areStr);

                        if (xian.equals("无") || xian.equals("")) {
                            if (shi.equals("无") || shi.equals("")) {  //没有县  没有市 获取省Id
                                       /* DbManager db = new DbConfig().getDbManager();
                                        List<Province> provinceList = new ArrayList<>();
                                        try {
                                            provinceList = db.selector(Province.class)
                                                    .where("name", "=", sheng)
                                                    .findAll();
                                        } catch (DbException e) {
                                        }
                                        if (provinceList != null) {
                                            areaId = provinceList.get(0).getId();
                                        }*/
                                areaId = 0;//没县 没市 全部默认0  省id 暂时忽略；
                                areaId_shi = 0;
                            } else {     //没有县 有市 获取市id
                                DbManager db = new DbConfig().getDbManager();
                                List<Province> provinceList = new ArrayList<>();
                                try {
                                    provinceList = db.selector(Province.class)
                                            .where("name", "=", shi)
                                            .findAll();
                                } catch (DbException e) {
                                }
                                if (provinceList != null) {
                                    areaId_shi = provinceList.get(0).getId();
                                }
                                areaId = 0;//没有县 默认0；

                            }
                        } else {//获取县id
                            DbManager db = new DbConfig().getDbManager();
                            List<Province> provinceList = new ArrayList<>();
                            List<Province> provinceListCity = new ArrayList<>();
                            try {
                                provinceList = db.selector(Province.class)
                                        .where("name", "=", xian)
                                        .findAll();
                                provinceListCity = db.selector(Province.class)
                                        .where("name", "=", shi)
                                        .findAll();

                            } catch (DbException e) {
                            }
                            if (provinceList != null) {
                                areaId = provinceList.get(0).getId();
                            }
                            if (provinceListCity != null) {
                                areaId_shi = provinceListCity.get(0).getId();
                            }
                        }
                    }
                });
                dialog3.show();
                //设置按钮颜色
                dialog3.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.theme_primary));
                break;
            case R.id.tv_shoppoint:
                Intent intent = new Intent(RegisterActivity.this, RegisterMapActivity.class);
                intent.putExtra("longitude_double", longitude_double);
                intent.putExtra("latitude_double", latitude_double);
                Log.e(TAG, "registerclick22222: " + "longitude_double" + longitude_double + "latitude_double" + latitude_double);
                startActivityForResult(intent, MAP_REUEST_CODE);
                break;
        }
    }

    private List<String> categorylistToStringlist(List<Category> categoryList) {
        List<String> categoryStringlist = new ArrayList<>();
        if (categoryList != null) {
            for (int i = 0; i < categoryList.size(); i++) {
                categoryStringlist.add(categoryList.get(i).getName());
            }
        }
        return categoryStringlist;
    }

    public List<String> getRTimeList(int selectedIndex) {
        rTime_list = new ArrayList<String>();
        rTime_list.addAll(lTime_list.subList(selectedIndex, lTime_list.size()));
        return rTime_list;
    }

    private List<String> getStrLTime() {
        lTime_list = new ArrayList<>();
        for (int i = 0; i < 25; i++) {
            if (i < 10) {
                lTime_list.add("0" + i + ":00:00");
            } else {
                lTime_list.add(i + ":00:00");
            }
        }
        return lTime_list;
    }

//    private List<String> getSpnList() {
//        categoryList = new ArrayList<>();
//        List<Category> categorydata = new ArrayList<>();
//
//        DbManager db = new DbConfig().getDbManager();
//        try {
//            categorydata = db.selector(Category.class)
//                    .findAll();
//        } catch (DbException e) {
//
//        }
//        if (categorydata != null) {
//            for (int i = 0; i < categorydata.size(); i++) {
//                categoryList.add(categorydata.get(i).getName());
//            }
//        }
//
//        return categoryList;
//    }

    private void getSheng() {
        shengList.clear();
        DbManager db = new DbConfig().getDbManager();
        List<Province> provinceList = new ArrayList<>();
        try {
            provinceList = db.selector(Province.class)
                    .where("definition", "=", 1)
                    .findAll();
        } catch (DbException e) {

        }
        if (provinceList != null) {
            for (int i = 0; i < provinceList.size(); i++) {
                shengList.add(provinceList.get(i).getName());
            }
        }
    }


    private void getShi() {
        shiList.clear();
        DbManager db = new DbConfig().getDbManager();
        List<Province> provinceList = new ArrayList<>();
        Log.e(TAG, "getShi: " + currentSheng);
        try {
            provinceList = db.selector(Province.class)
                    .where("name", "=", currentSheng)
                    .findAll();
        } catch (DbException e) {
        }
        if (provinceList != null) {
            if (provinceList.size() == 0) {
                shiList.add("无");
            } else {

            }
            Log.e(TAG, "getShi: " + provinceList.size());
            int shengId = provinceList.get(0).getId();
            List<Province> shiAllList = new ArrayList<>();
            try {
                shiAllList = db.selector(Province.class)
                        .where("fid", "=", shengId)
                        .findAll();
            } catch (DbException e) {
            }
            for (int i = 0; i < shiAllList.size(); i++) {
                shiList.add(shiAllList.get(i).getName());
            }
        }

    }

    private void getXian() {
        xianList.clear();
        DbManager db = new DbConfig().getDbManager();
        List<Province> provinceList = new ArrayList<>();
        try {
            provinceList = db.selector(Province.class)
                    .where("name", "=", currentShi)
                    .findAll();
        } catch (DbException e) {
        }
        if (provinceList != null) {
            if (provinceList.size() == 0) {
                xianList.add("无");
            } else {
                int shiId = provinceList.get(0).getId();
                List<Province> xianAllList = new ArrayList<>();
                try {
                    xianAllList = db.selector(Province.class)
                            .where("fid", "=", shiId)
                            .findAll();
                } catch (DbException e) {
                }

                for (int i = 0; i < xianAllList.size(); i++) {
                    xianList.add(xianAllList.get(i).getName());
                }
            }
        }
    }


    private void getCode(String phoneNumber) {
        showDialogProgress(progressDialog, "验证码发送中...");

        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("phone", phoneNumber);
        } catch (JSONException e) {
        }
        RequestParams params = new RequestParams(UtilsURL.REQUEST_URL + "sendMsg");
        params.addBodyParameter("reqJson", jsonObject.toString());
        params.setConnectTimeout(6000);
        x.http().post(params, new Callback.CommonCallback<String>() {
            private String status;

            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess: " + result);
                try {
                    JSONObject jsonObject1 = new JSONObject(result);
                    Log.e(TAG, "geyCode-->onSuccess: code_result ==>" + result.toString());
                    status = jsonObject1.getString("status");
                    if (status.equals("1")) {
                        Toast.makeText(RegisterActivity.this, "发送成功", Toast.LENGTH_SHORT).show();
                        mTimeCount.start();
                    } else {
                        Toast.makeText(RegisterActivity.this, "发送失败", Toast.LENGTH_SHORT).show();
                        tv_getCode.setText("重新发送");
                    }
                } catch (JSONException e) {
                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(getApplicationContext(), "验证码发送失败,请检查网络", Toast.LENGTH_SHORT).show();
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

    private void initView() {
        //获取经纬度
        mLocationClient = new LocationClient(this);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);                                //打开gps
        option.setCoorType("bd09ll");                           //设置坐标类型为bd09ll 百度需要的坐标，也可以返回其他type类型
        option.setPriority(LocationClientOption.NetWorkFirst);  //设置网络优先
//        option.setScanSpan(50000);                               //定时定位，每隔5秒钟定位一次。这个就看大家的需求了

        mLocationClient.setLocOption(option);
        mLocationClient.start();//这句代码百度api上给的没有，没有这个代码下面的回调方法不会执行的

        mLocationClient.registerLocationListener(new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                //  这里可以获取经纬度，这是回调方法
                longitude_double = bdLocation.getLongitude();
                latitude_double = bdLocation.getLatitude();
                Log.e(TAG, "registerclick11111: " + "longitude_double" + longitude_double + "latitude_double" + latitude_double);
            }
        });

        et_userName = (EditText) findViewById(R.id.et_person);
        et_userPhone = (EditText) findViewById(R.id.et_phone);
        tv_getCode = (TextView) findViewById(R.id.tv_getcode);
        et_Code = (EditText) findViewById(R.id.et_code);
        img_CodeRight = (ImageView) findViewById(R.id.img_code_right);
        et_Passworda = (EditText) findViewById(R.id.et_pass_a);
        et_Passwordb = (EditText) findViewById(R.id.et_pass_b);
        et_Shopname = (EditText) findViewById(R.id.et_shopname);
        tv_ShopCategory = (TextView) findViewById(R.id.tv_shopcategory);
        et_ShopPhone = (EditText) findViewById(R.id.et_shopphone);
        tv_ShopTime = (TextView) findViewById(R.id.tv_shoptime);
        tv_ShopCity = (TextView) findViewById(R.id.tv_shopcity);
        et_ShopLocation = (EditText) findViewById(R.id.et_shoplocation);
        tv_ShopPoint = (TextView) findViewById(R.id.tv_shoppoint);
        img_yyzz = (ImageView) findViewById(R.id.img_yyzz);
        img_yyzz_delete = (ImageView) findViewById(R.id.img_yyzz_delete);
        img_mdpic_a = (ImageView) findViewById(R.id.img_mdpic_a);
        img_mdpic_a_delete = (ImageView) findViewById(R.id.img_mdpic_a_delete);
        img_mdpic_b = (ImageView) findViewById(R.id.img_mdpic_b);
        img_mdpic_b_delete = (ImageView) findViewById(R.id.img_mdpic_b_delete);
        img_mdpic_c = (ImageView) findViewById(R.id.img_mdpic_c);
        img_mdpic_c_delete = (ImageView) findViewById(R.id.img_mdpic_c_delete);
        img_shou_a = (ImageView) findViewById(R.id.img_shou_a);
        img_shou_a_delete = (ImageView) findViewById(R.id.img_shou_a_delete);
        img_yyzz_center = (ImageView) findViewById(R.id.img_yyzz_center);
        img_mdpic_a_center = (ImageView) findViewById(R.id.img_mdpic_a_center);
        img_mdpic_b_center = (ImageView) findViewById(R.id.img_mdpic_b_center);
        img_mdpic_c_center = (ImageView) findViewById(R.id.img_mdpic_c_center);
        img_shou_a_center = (ImageView) findViewById(R.id.img_shou_a_center);
        ckbox_sl = (CheckBox) findViewById(R.id.checkbox_sl);
        ckbox_sl.setChecked(true);//默认熟练
        isShoulian = 1;
        ckbox_bsl = (CheckBox) findViewById(R.id.checkbox_bsl);
        checkbox_xieyi = (CheckBox) findViewById(R.id.ckbox_xieyi);
        CompoundButton.OnCheckedChangeListener listener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                switch (buttonView.getId()) {
                    case R.id.checkbox_sl:
                        if (isChecked) {
                            ckbox_bsl.setChecked(false);
                            isShoulian = 1;
                        } else {
                            ckbox_bsl.setChecked(true);
                            isShoulian = 0;
                        }
                        break;
                    case R.id.checkbox_bsl:
                        if (isChecked) {
                            ckbox_sl.setChecked(false);
                            isShoulian = 0;
                        } else {
                            ckbox_sl.setChecked(true);
                            isShoulian = 1;
                        }
                        break;
                    case R.id.ckbox_xieyi:
                        if (isChecked) {
                            ckbox_sl.setChecked(true);
                            isChecdXieyi = true;
                        } else {
                            ckbox_sl.setChecked(false);
                            isChecdXieyi = false;
                        }
                        break;
                }
            }
        };
        ckbox_sl.setOnCheckedChangeListener(listener);
        ckbox_bsl.setOnCheckedChangeListener(listener);
        checkbox_xieyi.setOnCheckedChangeListener(listener);
        tv_save = (TextView) findViewById(R.id.tv_save);
        mTimeCount = new TimeCount(60000, 1000);

        RxViewAction.clickNoDouble(tv_save).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Log.e(TAG, "call: et_userName.getText()" + et_userName.getText());
                if (et_userName.getText() == null || et_userName.getText().length() == 0) {
                    showDialog("请输入您的名字");
                    return;
                } else {
                    persionName = et_userName.getText().toString();
                }
                if (isRightCode == 0) {
                    showDialog("请验证手机号");
                    return;
                }
                if (et_Passworda.getText() == null || et_Passworda.getText().length() == 0) {
                    showDialog("请输入登录密码");
                    return;
                } else {
                    byte[] md5 = new byte[0];
                    try {
                        md5 = TripleDESUtil.MD5(et_Passworda.getText().toString());
                    } catch (NoSuchAlgorithmException e) {
                    } catch (UnsupportedEncodingException e) {
                    }
                    passWord_a = TripleDESUtil.bytes2HexString(md5);
                }
                if (et_Passwordb.getText() == null || et_Passwordb.getText().length() == 0) {
                    showDialog("请输入确认密码");
                    return;
                } else {
                    byte[] md5 = new byte[0];
                    try {
                        md5 = TripleDESUtil.MD5(et_Passwordb.getText().toString());
                    } catch (NoSuchAlgorithmException e) {
                    } catch (UnsupportedEncodingException e) {
                    }
                    passWord_b = TripleDESUtil.bytes2HexString(md5);
                }
                if (!passWord_a.equals(passWord_b)) {
                    showDialog("两次输入密码不一致");
                    return;
                }
                if (et_Shopname.getText() == null || et_Shopname.getText().length() == 0) {
                    showDialog("请输入门店名称");
                    return;
                } else {
                    shopName = et_Shopname.getText().toString();
                }
                if (shopCategoryId == null) {
                    showDialog("请选择您的门店类别");
                    return;
                }
                if (et_ShopPhone.getText() == null || et_ShopPhone.getText().length() == 0) {
                    showDialog("请输入您店里的电话");
                    return;
                } else {
                    shopPhone = et_ShopPhone.getText().toString();
                }
                if (shopTimeL == null || shopTimeR == null) {
                    showDialog("请选择您的营业时间");
                    return;
                }
                if (areaId == 9999) {
                    showDialog("请选择您的店铺所在城市");
                    return;
                } else {
                    positionId_xian = areaId + "";
                    cityId = areaId_shi + "";

                }
                if (et_ShopLocation.getText() == null || et_ShopLocation.getText().length() == 0) {
                    showDialog("请补充您的门店位置");
                    return;
                } else {
                    shopLocation = et_ShopLocation.getText().toString();
                }
                if (latitude == null || longitude == null) {
                    showDialog("请点击定位");
                    return;
                }
                if (yyzzPicBitmap == null || !hasPic_yyzz) {
                    showDialog("请上传营业执照");
                    return;
                } else {
                    yyzzPath = ImageUtils.savePhoto(yyzzPicBitmap, Environment
                            .getExternalStorageDirectory().getAbsolutePath(), "yyzzPic");
                }
                if (mdPicaBitmap == null || mdPicbBitmap == null || mdPiccBitmap == null || !hasPic_mdPic_a || !hasPic_mdPic_b || !hasPic_mdPic_c) {
                    showDialog("请上传门店照片");
                    return;
                } else {
                    mdpicaPath = ImageUtils.savePhoto(mdPicaBitmap, Environment
                            .getExternalStorageDirectory().getAbsolutePath(), "mdPica");
                    mdpicbPath = ImageUtils.savePhoto(mdPicbBitmap, Environment
                            .getExternalStorageDirectory().getAbsolutePath(), "mdPicb");
                    mdpiccPath = ImageUtils.savePhoto(mdPiccBitmap, Environment
                            .getExternalStorageDirectory().getAbsolutePath(), "mdPicc");
                }
                if (shouPicBitmap == null || !hasPic_shou_a) {
                    showDialog("请上传手持身份证照片");
                    return;
                } else {
                    shouPath = ImageUtils.savePhoto(shouPicBitmap, Environment
                            .getExternalStorageDirectory().getAbsolutePath(), "shouPic");
                }
                if (serviceTypeList == null || serviceTypeList.size() == 0) {
                    showDialog("请选择合作项目");
                    return;
                } else {
                    serviceTypeListString = "";
                    for (int i = 0; i < serviceTypeList.size(); i++) {
                        if (i == serviceTypeList.size() - 1) {
                            serviceTypeListString = serviceTypeListString + serviceTypeList.get(i);
                        } else {
                            serviceTypeListString = serviceTypeListString + serviceTypeList.get(i) + ",";
                        }
                        Log.e(TAG, "call:--++- " + serviceTypeListString);
                    }
                }
                if (!isChecdXieyi) {
                    showDialog("请同意《小马驾驾用户协议》");
                    return;
                }

                showSaveDialog("确定提交吗？");

            }
        });

    }

    private void showDialog(String error) {
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
                showDialogProgress(progressDialog, "注册信息提交中...");
                 /*    //提交参数---<
                private String persionName;
                private String persionPhone;
                private String code;
                private String passWord_a;
                private String passWord_b;
                private String shopName;
                private String shopCategoryId;
                private String shopPhone;
                private String shopTime;
                private String positionId_xian;
                private String shopLocation;
                private String longitude;
                private String latitude;
                private String serviceTypeListString;
                private boolean isShoulian;
                //提交参数>---*/
                JSONObject obj = new JSONObject();
                try {
                    obj.put("producerName", persionName);
                    obj.put("phone", persionPhone);
                    obj.put("password", passWord_a);
                    obj.put("storeName", shopName);
                    obj.put("storeTypeId", shopCategoryId);
                    obj.put("tel", shopPhone);
                    obj.put("startTime", "2000-01-01T" + shopTimeL + ".000+0800");
                    obj.put("endTime", "2000-01-01T" + shopTimeR + ".000+0800");
                    obj.put("positionId", positionId_xian);
                    obj.put("cityId", cityId);
                    obj.put("storeLocation", areStr);
                    obj.put("address", shopLocation);
                    obj.put("longitude", longitude);
                    obj.put("latitude", latitude);
                    // 00 obj.put("serviceTypeList",serviceTypeListString); 下面平级添加；
                    obj.put("appExpert", isShoulian);

                } catch (JSONException e) {
                }
                RequestParams pa = new RequestParams(UtilsURL.REQUEST_URL + "registerStore");
                pa.addBodyParameter("reqJson", obj.toString());
                pa.addBodyParameter("serviceTypeList", serviceTypeListString);//serviceTypeList
                pa.addBodyParameter("business_license_img", new File(yyzzPath));
                pa.addBodyParameter("location_img", new File(mdpicaPath));
                pa.addBodyParameter("indoor_img", new File(mdpicbPath));
                pa.addBodyParameter("factory_img", new File(mdpiccPath));
                pa.addBodyParameter("id_img", new File(shouPath));
                x.http().post(pa, new Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        try {
                            int status = new JSONObject(result).getInt("status");
                            String msg = new JSONObject(result).getString("msg");
                            if (status > 0) {
                                Toast.makeText(RegisterActivity.this, msg, Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                startActivity(intent);
                                RegisterActivity.this.finish();
                            } else {
                                Toast.makeText(RegisterActivity.this, msg, Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                        }
                    }

                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {
                        Toast.makeText(RegisterActivity.this, "注册信息提交失败,请检查网络", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        XiangmusBean bean = (XiangmusBean) buttonView.getTag();
//        Toast.makeText(this, " id= " + bean.getId() + " text = " + bean.getName() + " ", Toast.LENGTH_SHORT).show();
        if (isChecked) {
            if (serviceTypeList == null || serviceTypeList.size() == 0) {
                serviceTypeList.add(bean.getId() + "");
            } else {
                for (int i = 0; i < serviceTypeList.size(); i++) {
                    if (serviceTypeList.get(i).equals(bean.getId() + "")) {
                        serviceTypeList.remove(bean.getId() + "");
                    }
                }
                serviceTypeList.add(bean.getId() + "");
            }
        } else { //noChecked
            if (serviceTypeList == null || serviceTypeList.size() == 0) {

            } else {
                for (int i = 0; i < serviceTypeList.size(); i++) {
                    if (serviceTypeList.get(i).equals(bean.getId() + "")) {
                        serviceTypeList.remove(bean.getId() + "");
                    }
                }
            }
        }
    }

    class TimeCount extends CountDownTimer {

        /**
         * @param millisInFuture    The number of millis in the future from the call
         *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
         *                          is called.
         * @param countDownInterval The interval along the way to receive
         *                          {@link #onTick(long)} callbacks.
         */
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            tv_getCode.setBackgroundResource(R.drawable.btn_primary_enable);
            tv_getCode.setClickable(false);
            tv_getCode.setText("(" + millisUntilFinished / 1000 + "后重发)");
        }

        @Override
        public void onFinish() {
            tv_getCode.setText("重新获取");
            tv_getCode.setClickable(true);
            tv_getCode.setBackgroundResource(R.drawable.login_code_button);
        }
    }
}
