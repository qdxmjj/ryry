package com.ruyiruyi.ruyiruyi.ui.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.SyncStateContract;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ruyiruyi.ruyiruyi.R;
import com.ruyiruyi.ruyiruyi.db.DbConfig;
import com.ruyiruyi.ruyiruyi.db.model.CarTireInfo;
import com.ruyiruyi.ruyiruyi.db.model.Province;
import com.ruyiruyi.ruyiruyi.db.model.TireType;
import com.ruyiruyi.ruyiruyi.db.model.User;
import com.ruyiruyi.ruyiruyi.ui.multiType.RoadChoose;
import com.ruyiruyi.ruyiruyi.ui.multiType.Shop;
import com.ruyiruyi.ruyiruyi.utils.RequestUtils;
import com.ruyiruyi.ruyiruyi.utils.UtilsRY;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;
import com.ruyiruyi.rylibrary.base.BaseActivity;
import com.ruyiruyi.rylibrary.cell.ActionBar;
import com.ruyiruyi.rylibrary.image.ImageUtils;
import com.ruyiruyi.rylibrary.ui.cell.WheelView;
import com.ruyiruyi.rylibrary.ui.dialog.CustomDialog;
import com.ruyiruyi.rylibrary.utils.A2bigA;
import com.zhy.http.okhttp.https.HttpsUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.DbManager;
import org.xutils.common.Callback;
import org.xutils.common.util.KeyValue;
import org.xutils.ex.DbException;
import org.xutils.http.RequestParams;
import org.xutils.http.body.MultipartBody;
import org.xutils.x;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import rx.functions.Action1;

public class CarInfoActivity extends BaseActivity implements View.OnClickListener, DatePicker.OnDateChangedListener{

    public static final int TIRE_SIZE = 5; //前后轮选择
    private ActionBar actionBar;

    public static final int ROAD_CONDITITION = 4;//路况选择
    private static final String TAG = CarInfoActivity.class.getSimpleName();
    private TextView carNum;
    private String provinceShort[];
    private String letterAndDigit[];
    public static final int IMAGE_ITEM_ADD = -1;
    public static final int REQUEST_CODE_SELECT = 100;
    public static final int REQUEST_CODE_PREVIEW = 101;
    private LinearLayout addZhuyeLayout;
    protected static final int CHOOSE_PICTURE = 0;
    protected static final int TAKE_PICTURE = 1;

    private static final int CROP_SMALL_PICTURE = 2;
    protected static Uri tempUri;
    public static boolean hasZhuye = false;
    public static boolean hasFuye = false;
    public static boolean hasLcb = false;
    private ImageView zhuyeImage;
    private ImageView zhuyeImageDelete;
    private int currentImage = 0 ; //.=0 是主页  1是副业 2是里程表照片
    private LinearLayout addFuyeLayout;
    private ImageView fuyeImage;
    private ImageView fuyeImageDelete;
    private TextView saveCatButton;
    private Bitmap zhuyeBitmap;
    private Bitmap fuyeBitmap;
    private Bitmap lcbBitmap;
    private TextView carNumberText;
    private int currentCity = 0;
    private int currentProvicen = 0;
    private TextView carTypeChoose;
    private int carTiteInfoId = 0;
    private TextView carFontText;
    private TextView carRearText;
    private Switch isEnergySwich;
    public static boolean isEnergy = false; //是否是新能源
    private TextView xszRegisterTimeText;
    private FrameLayout xszRegidterTimeLayout;
    private StringBuffer date;
    private StringBuffer endDate;
    private int year;
    private int month;
    private int day;
    private TextView xszEndTimeText;
    private FrameLayout xszEndTimeLayout;
    private static boolean hasLichengbiao = true;
    private LinearLayout lichengbiaoLayout;
    private ImageView lichengbiaoImage;
    private LinearLayout lichengbiaoDataLayout;
    private FrameLayout roadConditionLayout;
    private TextView roadConditionText;
    private List<RoadChoose> jingchangList  ;
    private List<RoadChoose> ouerList ;
    private List<RoadChoose> bujingchangList;
    public StringBuffer roatStr;
    private LinearLayout addLichengbiaoLayout;
    private ImageView lcbImage;
    private ImageView lcbImageDelete;
    private FrameLayout yaoqingmaLayout;
    private EditText yaoqingmaText;
    private TextView saveCarButton;
    private FrameLayout carNumberLayout;
    private EditText lichengEdit;
    private int firstAddCar;
    private FrameLayout carTypeChooseLayout;
    private FrameLayout carFontLayout;
    private FrameLayout carRearLayout;
    private FrameLayout provinceLayout;
    private TextView provinceText;

    private List<String> shengList;
    private List<String> shiList;
    private List<String> xianList;
    public  String currentSheng = "北京市";
    public  String currentShi = "";
    public  String currentXian = "";
    public  int currentShengPosition = 0;
    public  int currentShiPosition = 0;
    public  int currentXianPosition = 0;
    private WheelView shengWv;
    private WheelView shiWv;
    private WheelView xianWv;
    private  int areaId = 0;
    private int userCarId;

    private  int canClick = 0;

    private WheelView whv_lTime, whv_rTime;
    private String endYear;
    private int currentRtime = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_info,R.id.my_action);
        actionBar = (ActionBar) findViewById(R.id.my_action);
        actionBar.setTitle("我的宝驹");;
        actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick(){
            @Override
            public void onItemClick(int var1) {
                switch ((var1)){
                    case -1:
                        onBackPressed();
                        break;
                }
            }
        });


        Intent intent = getIntent();
        canClick = intent.getIntExtra("CANCLICK", 0);
        Log.e(TAG, "onCreate: " +canClick);

        int from = intent.getIntExtra("FROM",0); // 0是车型选择返回  1是carManagetActivity返回
        if (from == 0){ //车型选择
            carTiteInfoId = intent.getIntExtra("CARTIREIINFO",0);
        }else if (from == 1){//查看车辆信息
            userCarId = intent.getIntExtra("USERCARID",0);
            initDataByUseridAndCarId();
        }



        initView();



        date = new StringBuffer();
        endDate = new StringBuffer();
        roatStr = new StringBuffer();
        jingchangList  = new ArrayList<>();
        ouerList  = new ArrayList<>();
        bujingchangList  = new ArrayList<>();
        shengList  = new ArrayList<>();
        shiList  = new ArrayList<>();
        xianList  = new ArrayList<>();

        initData();
        initDateTime();
        initLichengbiao();
        getSheng();
        getShi();
        getXian();
       // viewCanClick();
    }

  /*  private void viewCanClick() {
        saveCatButton.setClickable(canClick);
        carFontLayout.setClickable(canClick);
        carRearLayout.setClickable(canClick);
        roadConditionLayout.setClickable(canClick);
        lichengbiaoLayout.setClickable(canClick);
        xszRegidterTimeLayout.setClickable(canClick);
        carTypeChooseLayout.setClickable(canClick);
        addFuyeLayout.setClickable(canClick);
        addZhuyeLayout.setClickable(canClick);
        addLichengbiaoLayout.setClickable(canClick);
        zhuyeImageDelete.setClickable(canClick);
        fuyeImageDelete.setClickable(canClick);
        lcbImageDelete.setClickable(canClick);
        isEnergySwich.setClickable(canClick);



    }*/

    /**
     * 根据userId跟carId获取车辆信息
     */
    private void initDataByUseridAndCarId() {
        int uesrId = new DbConfig().getId();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userId",uesrId);
            jsonObject.put("userCarId",userCarId);
        } catch (JSONException e) {
        }
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "getCarByUserIdAndCarId");
        params.addBodyParameter("reqJson",jsonObject.toString());
        String token = new DbConfig().getToken();
        params.addParameter("token",token);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess: re" + result );
                JSONObject jsonObject1 = null;
                try {
                    jsonObject1 = new JSONObject(result);
                    String status = jsonObject1.getString("status");
                    String msg = jsonObject1.getString("msg");
                    if (status.equals("1")){
                        JSONObject data = jsonObject1.getJSONObject("data");
                        String carName = data.getString("carName");
                        int isNewenergy = data.getInt("isNewenergy");
                        String platNumber = data.getString("platNumber");
                        String proCityName = data.getString("proCityName");
                        String font = data.getString("font");
                        String rear = data.getString("rear");
                        Long drivingLicenseDate = data.getLong("drivingLicenseDate");
                        String xszRegisterTime = new UtilsRY().getTimestampToString(drivingLicenseDate);
                        Long serviceEndDate = data.getLong("serviceEndDate");
                        String xszEndTime = new UtilsRY().getTimestampToString(serviceEndDate);
                        String traveledImgInverse = data.getString("traveledImgInverse");
                        String traveledImgObverse = data.getString("traveledImgObverse");
                        String traveled = data.getString("traveled");
                        String maturityImg = data.getString("maturityImg");
                        String roadTxt = data.getString("roadTxt");
                        carTypeChoose.setText(carName);
                        if (isNewenergy == 0){//燃油
                            isEnergySwich.setChecked(false);
                        }else {//xin能源
                            isEnergySwich.setChecked(true);
                        }
                        carNumberText.setText(platNumber);
                        provinceText.setText(proCityName);
                        carFontText.setText(font);
                        carRearText.setText(rear);
                        xszRegisterTimeText.setText(xszRegisterTime);
                        xszEndTimeText.setText(xszEndTime);
                        hasZhuye = true;
                        initZhuyeLayou();
                        Glide.with(getApplicationContext()).load(traveledImgInverse).into(zhuyeImage);
                        hasFuye = true;
                        initFuyeLayou();;
                        Glide.with(getApplicationContext()).load(traveledImgObverse).into(fuyeImage);
                        if (traveled.equals("1000000")){
                            hasLichengbiao = false;
                            initLichengbiao();
                        }else {
                            hasLichengbiao = true;
                            initLichengbiao();
                            lichengEdit.setText(traveled);
                            hasLcb = true;
                            initLcbLayou();
                            Glide.with(getApplicationContext()).load(maturityImg).into(lcbImage);
                        }
                        roadConditionText.setText(roadTxt);
                        saveCatButton.setText("暂不可修改");

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

    private void getXian() {
        xianList.clear();
        DbManager db = new DbConfig().getDbManager();
        List<Province> provinceList = new ArrayList<>();
        try {
            provinceList  = db.selector(Province.class)
                    .where("name" , "=" , currentShi)
                    .findAll();
        } catch (DbException e) {
        }
        if (provinceList.size()==0){
            xianList.add("无");
        }else {
            int shiId = provinceList.get(0).getId();
            List<Province> xianAllList = new ArrayList<>();
            try {
                xianAllList  = db.selector(Province.class)
                        .where("fid" , "=" , shiId)
                        .findAll();
            } catch (DbException e) {
            }

            for (int i = 0; i < xianAllList.size(); i++) {
                xianList.add(xianAllList.get(i).getName());
            }
        }


    }

    private void getShi() {
        shiList.clear();
        DbManager db = new DbConfig().getDbManager();
        List<Province> provinceList = new ArrayList<>();
        Log.e(TAG, "getShi: "+  currentSheng );
        try {
            provinceList  = db.selector(Province.class)
                    .where("name" , "=" , currentSheng)
                    .findAll();
        } catch (DbException e) {
        }
        if (provinceList.size() == 0){
            shiList.add("无");
        }else {

        }     Log.e(TAG, "getShi: "+provinceList.size() );
        int shengId = provinceList.get(0).getId();
        List<Province> shiAllList = new ArrayList<>();
        try {
            shiAllList  = db.selector(Province.class)
                    .where("fid" , "=" , shengId)
                    .findAll();
        } catch (DbException e) {
        }
        for (int i = 0; i < shiAllList.size(); i++) {
            shiList.add(shiAllList.get(i).getName());
        }


    }

    private void getSheng() {
        shengList.clear();
        DbManager db = new DbConfig().getDbManager();
        List<Province> provinceList = new ArrayList<>();
        try {
            provinceList  = db.selector(Province.class)
                    .where("definition" , "=" , 1)
                    .findAll();
        } catch (DbException e) {

        }
        for (int i = 0; i < provinceList.size(); i++) {
            shengList.add(provinceList.get(i).getName());
        }

    }

    private void initData() {
        if (carTiteInfoId != 0){
            DbManager db = new DbConfig().getDbManager();
            try {
                List<CarTireInfo> tireInfoList = db.selector(CarTireInfo.class)
                        .where("id", "=", carTiteInfoId)
                        .findAll();
                CarTireInfo carTireInfo = tireInfoList.get(0);
                String brand = carTireInfo.getVerhicle();
                String font = carTireInfo.getFont();
                String rear = carTireInfo.getRear();

                carFontText.setText(font);
                carFontText.setText(font);
                carRearText.setText(rear);

                carTypeChoose.setText(brand);
            } catch (DbException e) {

            }

        }
    }

    private void initView() {

        addZhuyeLayout = (LinearLayout) findViewById(R.id.add_zhuye_jiashizheng);
        lcbImage = (ImageView) findViewById(R.id.lichengbiao_image);
        lcbImageDelete = (ImageView) findViewById(R.id.lichengbiao_image_delete);
        zhuyeImage = (ImageView) findViewById(R.id.zhuye_image);
        zhuyeImageDelete = (ImageView) findViewById(R.id.zhuye_image_delete);
        addFuyeLayout = (LinearLayout) findViewById(R.id.add_fuye_jiashizheng);
        fuyeImage = (ImageView) findViewById(R.id.fuye_image);
        fuyeImageDelete = (ImageView) findViewById(R.id.fuye_image_delete);
        addLichengbiaoLayout = (LinearLayout) findViewById(R.id.add_lichengbiao);
        saveCatButton = (TextView) findViewById(R.id.save_car);
        carNumberText = (TextView) findViewById(R.id.car_number);
        carNumberLayout = (FrameLayout) findViewById(R.id.car_number_layout);
        carTypeChoose = (TextView) findViewById(R.id.car_type_choose);
        carTypeChooseLayout = (FrameLayout) findViewById(R.id.car_type_choose_layout);
        carFontText = (TextView) findViewById(R.id.car_font);
        carRearText = (TextView) findViewById(R.id.car_rear);
        carFontLayout = (FrameLayout) findViewById(R.id.car_font_layout);
        carRearLayout = (FrameLayout) findViewById(R.id.car_rear_layout);
        isEnergySwich = (Switch) findViewById(R.id.car_is_energy);
        xszRegisterTimeText = (TextView) findViewById(R.id.xsz_register_time);
        xszRegidterTimeLayout = (FrameLayout) findViewById(R.id.xsz_register_time_layout);
        xszEndTimeText = (TextView) findViewById(R.id.xsz_end_time);
        xszEndTimeLayout = (FrameLayout) findViewById(R.id.xsz_end_time_layout);
        lichengbiaoLayout = (LinearLayout) findViewById(R.id.lichengbiao_is_use_layout);
        lichengbiaoImage = (ImageView) findViewById(R.id.has_lichengbiao_image);
        lichengbiaoDataLayout = (LinearLayout) findViewById(R.id.lichengbiao_data_layout);
        roadConditionLayout = (FrameLayout) findViewById(R.id.road_condition_choose_layout);
        roadConditionText = (TextView) findViewById(R.id.road_condition_choose);
        yaoqingmaLayout = (FrameLayout) findViewById(R.id.yaoqingma_layout);
        yaoqingmaText = (EditText) findViewById(R.id.yaoqingma_text);
        lichengEdit = (EditText) findViewById(R.id.licheng_edit);
        provinceLayout = (FrameLayout) findViewById(R.id.province_layout);
        provinceText = (TextView) findViewById(R.id.province_text);

        // xszRegidterTimeLayout.setOnClickListener(this);

        carNumberLayout.setOnClickListener(this);
        provinceLayout.setOnClickListener(this);
        lichengEdit.setClickable(canClick == 1 ?false : true);



        isEnergySwich.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (canClick == 1){
                    return;
                }
                if (isChecked){
                    isEnergy = true;
                }else {
                    isEnergy = false;
                }
            }
        });


        hasZhuye = false;
        initZhuyeLayou();
        hasFuye = false;
        initFuyeLayou();
        hasLcb = false;
        initLcbLayou() ;

        RxViewAction.clickNoDouble(saveCatButton)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (canClick == 0){
                            Log.e(TAG, "call: +-------------------true"  );
                        }else {
                            Log.e(TAG, "call: +-------------------false"  );
                        }

                        if (canClick == 1){
                            return;
                        }
                        if (carTiteInfoId == 0){
                            Toast.makeText(CarInfoActivity.this, "请选择车型", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (carNumberText.getText().toString().length() == 0 ){
                            Toast.makeText(CarInfoActivity.this, "请输入车牌号码", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (areaId == 0){
                            Toast.makeText(CarInfoActivity.this, "请选择常驻地区", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (xszRegisterTimeText.getText().length() == 0){
                            Toast.makeText(CarInfoActivity.this, "请选择行驶证日期", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (roadConditionText.getText().length() == 0){
                            Toast.makeText(CarInfoActivity.this, "请选择行驶路况", Toast.LENGTH_SHORT).show();

                        }

                        uploadPic();
                    }
                });

        RxViewAction.clickNoDouble(carFontLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (canClick == 1){
                            return;
                        }
                        if (carFontText.getText().toString().isEmpty()){
                            Toast.makeText(CarInfoActivity.this, "请先选择车型", Toast.LENGTH_SHORT).show();
                            return;
                        }else {
                            Intent intent = new Intent(getApplicationContext(), TireSizeActivity.class);
                            intent.putExtra("WEIZHI",0);
                            intent.putExtra("TIRE",carFontText.getText().toString());
                            startActivityForResult(intent,TIRE_SIZE);
                        }
                    }
                });
        RxViewAction.clickNoDouble(carRearLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (canClick == 1){
                            return;
                        }
                        if (carRearText.getText().toString().isEmpty()){
                            Toast.makeText(CarInfoActivity.this, "请先选择车型", Toast.LENGTH_SHORT).show();
                            return;
                        }else {
                            Intent intent = new Intent(getApplicationContext(), TireSizeActivity.class);
                            intent.putExtra("WEIZHI",1);
                            intent.putExtra("TIRE",carRearText.getText().toString());
                            startActivityForResult(intent,TIRE_SIZE);
                        }

                    }
                });

        RxViewAction.clickNoDouble(roadConditionLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (canClick == 1){
                            return;
                        }
                        startActivityForResult(new Intent(getApplicationContext(),RoadConditionActivity.class),ROAD_CONDITITION);
                    }
                });
        RxViewAction.clickNoDouble(lichengbiaoLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (canClick == 1){
                            return;
                        }
                        if (hasLichengbiao){
                            hasLichengbiao = false;
                        }else {
                            hasLichengbiao = true;
                        }
                        initLichengbiao();
                    }
                });

        RxViewAction.clickNoDouble(xszRegidterTimeLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (canClick == 1){
                            return;
                        }
                        showDataDialog();
                    }
                });



        RxViewAction.clickNoDouble(carTypeChooseLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (canClick == 1){
                            return;
                        }
                        startActivity(new Intent(getApplicationContext(),CarBrandActivity.class));
                    }
                });

        RxViewAction.clickNoDouble(addFuyeLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (canClick == 1){
                            return;
                        }
                        currentImage = 1;
                        showChoosePicDialog();
                    }
                });

        RxViewAction.clickNoDouble(addZhuyeLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (canClick == 1){
                            return;
                        }
                        Log.e(TAG, "call: 1");
                        currentImage = 0;
                        showChoosePicDialog();
                    }
                });
        RxViewAction.clickNoDouble(addLichengbiaoLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (canClick == 1){
                            return;
                        }
                        currentImage = 2;
                        showChoosePicDialog();
                    }
                });
        RxViewAction.clickNoDouble(zhuyeImageDelete)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (canClick == 1){
                            return;
                        }
                        hasZhuye = false;
                        initZhuyeLayou();
                    }
                });
        RxViewAction.clickNoDouble(fuyeImageDelete)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (canClick == 1){
                            return;
                        }
                        hasFuye = false;
                        initFuyeLayou(); ;
                    }
                });
        RxViewAction.clickNoDouble(lcbImageDelete)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (canClick == 1){
                            return;
                        }
                        hasLcb = false;
                        initLcbLayou() ;
                    }
                });

        RxViewAction.clickNoDouble(xszEndTimeLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        chooseServiceTime();
                    }
                });


      /*  RxViewAction.clickNoDouble(saveCatButton)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        uploadPic();
                    }
                });*/
        // carNum = (TextView) findViewById(R.id.car_number);
        //carNum.setOnClickListener(this);

     /*   RxViewAction.clickNoDouble(carNum)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        View outerView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.dialog_content_view, null);
                        final WheelView wv = (WheelView) outerView.findViewById(R.id.wheel_view_wv);
                        wv.setItems(getNumbers(),0);//init selected position is 0 初始选中位置为0
                        wv.setOnItemSelectedListener(new WheelView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(int selectedIndex, String item) {
                                Log.d(TAG, "[Dialog]selectedIndex: " + selectedIndex + ", item: " + item);
                            }
                        });

                        new AlertDialog.Builder(getApplicationContext())
                                .setTitle("WheelView in Dialog")
                                .setView(outerView)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Toast.makeText(CarInfoActivity.this,
                                                "selectedIndex: "+ wv.getSelectedPosition() +"  selectedItem: "+ wv.getSelectedItem(),
                                                Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .show();

                    }
                });*/

        User user = new DbConfig().getUser();
        firstAddCar = user.getFirstAddCar();
        if (firstAddCar == 0){
            yaoqingmaLayout.setVisibility(View.VISIBLE);
        }else {
            yaoqingmaLayout.setVisibility(View.GONE);
        }

    }

    private void chooseServiceTime() {
        View v_shoptime = LayoutInflater.from(this).inflate(R.layout.dialog_choose_time, null);
        whv_lTime = (WheelView) v_shoptime.findViewById(R.id.whv_ltime);
        whv_rTime = (WheelView) v_shoptime.findViewById(R.id.whv_rtime);
        whv_lTime.setItems(getStrLTime(), 0);
        whv_rTime.setItems(getRTimeList(), currentRtime);
        whv_lTime.setOnItemSelectedListener(new WheelView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int selectedIndex, String item) {

            }
        });

        whv_rTime.setOnItemSelectedListener(new WheelView.OnItemSelectedListener() {




            @Override
            public void onItemSelected(int selectedIndex, String item) {
                currentRtime = whv_rTime.getSelectedPosition();
                endYear = whv_rTime.getSelectedItem();
            }
        });
        new android.support.v7.app.AlertDialog.Builder(this)
                .setTitle("选择汽车服务时间")
                .setView(v_shoptime)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (endDate.length()>0){
                            endDate.delete(0,endDate.length());
                        }
                        xszEndTimeText.setText(endDate.append(endYear + "-12" + "-31"));
                    }
                }).show();
    }

    private List<String> getRTimeList() {
        List<String> rTime_list = new ArrayList<String>();
        int xszYear = Integer.parseInt(date.substring(0, 4));
        Log.e(TAG, "getRTimeList: ----" + xszYear);
        int xszEndYear = xszYear + 15;
        List<String> lTime_list = new ArrayList<>();
        Calendar date = Calendar.getInstance();
        String year = String.valueOf(date.get(Calendar.YEAR));
        int currentYear = Integer.parseInt(year);
        for (int i = 0; i < xszEndYear - currentYear; i++) {
            rTime_list.add(currentYear + i+"");
        }
        return rTime_list;
    }

    private List<String> getStrLTime() {
        List<String> lTime_list = new ArrayList<>();
        Calendar date = Calendar.getInstance();
        String year = String.valueOf(date.get(Calendar.YEAR));
        lTime_list.add(year);
        return lTime_list;
    }

    private void postData() {

    }

    /**
     * 初始化里程表
     */
    private void initLichengbiao() {
        if (hasLichengbiao){
            lichengbiaoImage.setImageResource(R.drawable.ic_check);
            lichengbiaoDataLayout.setVisibility(View.VISIBLE);
        }else {
            lichengbiaoImage.setImageResource(R.drawable.ic_check_no);
            lichengbiaoDataLayout.setVisibility(View.GONE);
        }
    }

    /**
     * 保存数据图片到服务器
     */
    private void uploadPic() {

        if (zhuyeBitmap == null){
            Toast.makeText(CarInfoActivity.this, "请上传驾驶证主页", Toast.LENGTH_SHORT).show();
            return;
        }
        if (fuyeBitmap == null){
            Toast.makeText(CarInfoActivity.this, "请上传驾驶证副页", Toast.LENGTH_SHORT).show();
            return;
        }
        String lcbPath = "";
        if (hasLcb){
            if (lcbBitmap == null){
                Toast.makeText(CarInfoActivity.this, "请上传驾驶证副页", Toast.LENGTH_SHORT).show();
                return;
            }
            lcbPath = ImageUtils.savePhoto(lcbBitmap, Environment
                    .getExternalStorageDirectory().getAbsolutePath(),"lichengbiao");
        }

       String zhuyePath = ImageUtils.savePhoto(zhuyeBitmap, Environment
                .getExternalStorageDirectory().getAbsolutePath(), "zhuye");

       String fuyePath = ImageUtils.savePhoto(fuyeBitmap, Environment
                .getExternalStorageDirectory().getAbsolutePath(), "fuye");

        int id = new DbConfig().getId();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userId",id);
            jsonObject.put("car_id",carTiteInfoId);
            jsonObject.put("car_name",carTypeChoose.getText());
            jsonObject.put("xinnengyuan",isEnergy);
            jsonObject.put("plat_number",carNumberText.getText().toString());
            jsonObject.put("pro_city_id",areaId);
            jsonObject.put("proCityName",provinceText.getText());
            jsonObject.put("font",carFontText.getText().toString());
            jsonObject.put("rear",carRearText.getText().toString());
            jsonObject.put("driving_license_date",xszRegisterTimeText.getText().toString());
            jsonObject.put("service_end_date",xszEndTimeText.getText().toString());
          //  ArrayList<Integer> jingchang = new ArrayList<>();
            StringBuffer jingchang = new StringBuffer();
            for (int i = 0; i < jingchangList.size(); i++) {
               // jingchang.add(jingchangList.get(i).getRoadId());
                if (i==jingchangList.size()-1){
                    jingchang.append(jingchangList.get(i).getRoadId());
                }else {
                    jingchang.append(jingchangList.get(i).getRoadId()+",");
                }

            }

          //  ArrayList<String> ouer = new ArrayList<>();
            StringBuffer ouer = new StringBuffer();
            for (int i = 0; i < ouerList.size(); i++) {
             //   ouer.add(ouerList.get(i).getRoadId()+"");
                if (i==ouerList.size()-1){
                    ouer.append(ouerList.get(i).getRoadId());
                }else {
                    ouer.append(ouerList.get(i).getRoadId()+",");
                }
            }

           // ArrayList<Integer> bujingchang = new ArrayList<>();
            StringBuffer bujingchang = new StringBuffer();
            for (int i = 0; i < bujingchangList.size(); i++) {
                //bujingchang.add(bujingchangList.get(i).getRoadId());
                if (i==bujingchangList.size()-1){
                    bujingchang.append(bujingchangList.get(i).getRoadId());
                }else {
                    bujingchang.append(bujingchangList.get(i).getRoadId()+",");
                }
            }
            jsonObject.put("road_txt",roadConditionText.getText().toString());
            Log.e(TAG, "uploadPic: " + jingchang );
            Log.e(TAG, "uploadPic: " + ouer );
            Log.e(TAG, "uploadPic: " + bujingchang );
            jsonObject.put("type_i_rate",jingchang);
            jsonObject.put("type_ii_rate",ouer);
            jsonObject.put("type_iii_rate",bujingchang);
            if (hasLichengbiao){
                jsonObject.put("traveled",lichengEdit.getText().toString());
            }else {
                jsonObject.put("traveled","");
            }
            if (firstAddCar == 0){
                jsonObject.put("invite_code",yaoqingmaText.getText().toString());
            }

        } catch (JSONException e) {

        }

        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "addUserCar");
        params.addBodyParameter("reqJson",jsonObject.toString());
        params.addBodyParameter("jiashizhengzhuye" ,new File(zhuyePath) );
        params.addBodyParameter("jiashizhengfuye" ,new File(fuyePath) );
        if (hasLichengbiao){
            params.addBodyParameter("lichengbiao" ,new File(lcbPath) );
        }
        String token = new DbConfig().getToken();
        params.addParameter("token",token);

        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess: " + result);
                JSONObject jsonObject1 = null;
                try {
                    jsonObject1 = new JSONObject(result);
                    String status = jsonObject1.getString("status");
                    String msg = jsonObject1.getString("msg");
                    if (status.equals("1")){
                        Toast.makeText(CarInfoActivity.this, msg, Toast.LENGTH_SHORT).show();
                        /*Intent intent = new Intent();
                        setResult(CarManagerActivity.CARMANAMGER_RESULT,intent);
                        finish();*/
                        startActivity(new Intent(getApplicationContext(),CarManagerActivity.class));


                    }else {
                        Toast.makeText(CarInfoActivity.this, msg, Toast.LENGTH_SHORT).show();
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


    /**
     * 读取照片旋转角度
     *
     * @param path 照片路径
     * @return 角度
     */
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
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



    private void initZhuyeLayou() {
        zhuyeImage.setVisibility(hasZhuye ? View.VISIBLE : View.GONE);
        addZhuyeLayout.setVisibility(hasZhuye ? View.GONE : View.VISIBLE);
        zhuyeImageDelete.setVisibility(hasZhuye ? View.VISIBLE : View.GONE);
    }
    private void initFuyeLayou() {
        fuyeImage.setVisibility(hasFuye ? View.VISIBLE : View.GONE);
        addFuyeLayout.setVisibility(hasFuye ? View.GONE : View.VISIBLE);
        fuyeImageDelete.setVisibility(hasFuye ? View.VISIBLE : View.GONE);
    }
    private void initLcbLayou() {
        lcbImage.setVisibility(hasLcb ? View.VISIBLE : View.GONE);
        addLichengbiaoLayout.setVisibility(hasLcb ? View.GONE : View.VISIBLE);
        lcbImageDelete.setVisibility(hasLcb ? View.VISIBLE : View.GONE);
    }



    private void showChoosePicDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("设置头像");
        String[] items = { "选择本地照片", "拍照" };
        builder.setNegativeButton("取消", null);
        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case CHOOSE_PICTURE: // 选择本地照片
                        Intent openAlbumIntent = new Intent(
                                Intent.ACTION_GET_CONTENT);
                        openAlbumIntent.setType("image/*");
                        startActivityForResult(openAlbumIntent, CHOOSE_PICTURE);
                        break;
                    case TAKE_PICTURE: // 拍照
                        takePicture();
                        break;
                }
            }
        });
        builder.create().show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e(TAG, "onActivityResult: ------------" + data);
        Log.e(TAG, "onActivityResult: ------------" + requestCode);
        Log.e(TAG, "onActivityResult: ------------" + resultCode);
        if (resultCode == RESULT_OK) { // 如果返回码是可以用的
            switch (requestCode) {
                case TAKE_PICTURE:
                    setImageToViewFromPhone(tempUri);
                  //  startPhotoZoom(tempUri); // 开始对图片进行裁剪处理
                    break;
                case CHOOSE_PICTURE:        //从相册选择
                    Uri uri = data.getData();
                    Log.e(TAG, "onActivityResult:+++++++++++++ " + uri);
                    setImageToViewFromPhone(uri);
                    //startPhotoZoom(data.getData()); // 开始对图片进行裁剪处理
                    break;
                case CROP_SMALL_PICTURE:
                    if (data != null) {
                        setImageToView(data); // 让刚才选择裁剪得到的图片显示在界面上
                    }
                    break;

            }
        }else if (resultCode == ROAD_CONDITITION){ //路况选择的回掉
            if (roatStr.length() > 0){
                roatStr.delete(0,roatStr.length());
            }

            jingchangList = ((List<RoadChoose>) data.getSerializableExtra("JINGCHANG"));
            ouerList = ((List<RoadChoose>) data.getSerializableExtra("OUER"));
            bujingchangList = ((List<RoadChoose>) data.getSerializableExtra("BUJINGCHANG"));
            for (int i = 0; i < jingchangList.size(); i++) {
                roatStr.append(jingchangList.get(i).getTitle()+ ";");
            }
            for (int i = 0; i < ouerList.size(); i++) {
                roatStr.append(ouerList.get(i).getTitle() + ";");
            }
            for (int i = 0; i < bujingchangList.size(); i++) {
                roatStr.append(bujingchangList.get(i).getTitle());
            }
            roadConditionText.setText(roatStr);
            Log.e(TAG, "onActivityResult:jingchangList " + jingchangList.size());
            Log.e(TAG, "onActivityResult:ouerList " + ouerList.size());
            Log.e(TAG, "onActivityResult:bujingchangList " + bujingchangList.size());
        }else if(resultCode == TIRE_SIZE){
            int weizhi = data.getIntExtra("weizhi",0);
            String tire = data.getStringExtra("tire");
            if(weizhi == 0){
                Toast.makeText(CarInfoActivity.this, "后轮轮胎默认与前轮一致", Toast.LENGTH_SHORT).show();
                carFontText.setText(tire);
                carRearText.setText(tire);
            }else {
                carRearText.setText(tire);
            }
        }
    }

    /**
     * 保存裁剪之后的图片数据
     *
     * @param
     */
    protected void setImageToView(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            Log.d(TAG,"setImageToView:"+photo);
            if (currentImage == 0 ){
                zhuyeBitmap = photo;
                hasZhuye = true;
                initZhuyeLayou();
                zhuyeImage.setImageBitmap(photo);
            }else if (currentImage == 1){
                fuyeBitmap = photo;
                hasFuye = true;
                initFuyeLayou();
                fuyeImage.setImageBitmap(photo);
            }else if (currentImage == 2){
                lcbBitmap = photo;
                hasLcb = true;
                initLcbLayou() ;
                lcbImage.setImageBitmap(photo);
            }
            //uploadPic(photo);
        }
    }


    /**
     * 未裁剪之后的图片数据
     *setImageToViewFromPhone
     * @param
     */
    protected void setImageToViewFromPhone(Uri uri) {

        int degree = ImageUtils.readPictureDegree(uri.toString());

        if (uri != null) {
            Bitmap photo = null;
            try {
                photo = ImageUtils.getBitmapFormUri(getApplicationContext(),uri);
            } catch (IOException e) {

            }
            Log.d(TAG,"setImageToView:"+photo);
            if (currentImage == 0 ){
                zhuyeBitmap = rotaingImageView(degree, photo);
                hasZhuye = true;
                initZhuyeLayou();
                zhuyeImage.setImageBitmap(zhuyeBitmap);
            }else if (currentImage == 1){
                fuyeBitmap = rotaingImageView(degree,photo);
                hasFuye = true;
                initFuyeLayou();
                fuyeImage.setImageBitmap(fuyeBitmap);
            }else if (currentImage == 2){
                lcbBitmap = rotaingImageView(degree,photo);
                hasLcb = true;
                initLcbLayou() ;
                lcbImage.setImageBitmap(lcbBitmap);

            }
            //uploadPic(photo);
        }
    }



    /**
     * 裁剪图片方法实现
     *
     * @param uri
     */
    protected void startPhotoZoom(Uri uri) {
        Log.e(TAG, "startPhotoZoom: " +  uri.toString() );
        if (uri == null) {
            Log.i("tag", "The uri is not exist.");
        }
        tempUri = uri;
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(tempUri, "image/*");
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 100);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, CROP_SMALL_PICTURE);
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
        if (currentImage == 0){
            file = new File(Environment
                    .getExternalStorageDirectory(), "zhashizhengzhuye.jpg");
        }else if (currentImage == 1){
            file = new File(Environment
                    .getExternalStorageDirectory(), "zhashizhengfuye.jpg");
        }

        //判断是否是AndroidN以及更高的版本
        if (Build.VERSION.SDK_INT >= 24) {
            openCameraIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            tempUri = FileProvider.getUriForFile(CarInfoActivity.this, "com.ruyiruyi.ruyiruyi.fileProvider", file);
        } else {
            tempUri = Uri.fromFile(new File(Environment
                    .getExternalStorageDirectory(), "image.jpg"));
        }
        Log.e(TAG, "takePicture: " + tempUri);
        // 指定照片保存路径（SD卡），image.jpg为一个临时文件，每次拍照后这个图片都会被替换
        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);
        startActivityForResult(openCameraIntent, TAKE_PICTURE);
    }

    private ArrayList getProvice() {
        String[] strings = {"京", "津", "冀", "鲁", "晋", "蒙", "辽", "吉", "黑"
                , "沪", "苏", "浙", "皖", "闽", "赣", "豫", "鄂", "湘"
                , "粤", "桂", "渝", "川", "贵", "云", "藏", "陕", "甘"
                , "青", "琼", "新", "港", "澳", "台", "宁"};
        ArrayList<String> stringArrayList = new ArrayList<>();
        for (int i = 0; i < strings.length; i++) {
            stringArrayList.add(strings[i]);
        }
        return  stringArrayList;
    }
    private ArrayList getCity(){
        String[] strings = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"
                , "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T"
                , "A", "S", "D", "F", "G", "H", "J", "K", "L"
                , "U", "V", "W", "X", "Y", "Z"};
        ArrayList<String> stringArrayList = new ArrayList<>();
        for (int i = 0; i < strings.length; i++) {
            stringArrayList.add(strings[i]);
        }
        return  stringArrayList;

    }



    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.car_number_layout:
                if (canClick == 1){
                    return;
                }
                View outerView = LayoutInflater.from(this).inflate(R.layout.dialog_content_view, null);
                final WheelView city = (WheelView) outerView.findViewById(R.id.wheel_view_city);
                city.setItems(getCity(),currentCity);//init selected position is 0 初始选中位置为0
                city.setOnItemSelectedListener(new WheelView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(int selectedIndex, String item) {
                        currentCity = city.getSelectedPosition();
                    }
                });
                final WheelView provice = (WheelView) outerView.findViewById(R.id.wheel_view_provice);
                provice.setItems(getProvice(),currentProvicen);//init selected position is 0 初始选中位置为0
                provice.setOnItemSelectedListener(new WheelView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(int selectedIndex, String item) {
                        currentProvicen = provice.getSelectedPosition();
                    }
                });

                final EditText carNumberEdit = (EditText) outerView.findViewById(R.id.car_numbe_dialog);
                String carStrNum = "";
                if (carNumberEdit.getText().toString().length() > 0){
                    carStrNum = carNumberText.getText().toString().substring(2);
                }


                 Log.e(TAG, "onClick: " + carStrNum);

                //  carNumberEdit.setText(carNumberText.getText().toString().substring(2,carNumberText.getText().length()));
                carNumberEdit.setTransformationMethod(new A2bigA());
                if (isEnergy){
                    carNumberEdit.setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)});
                }else {
                    carNumberEdit.setFilters(new InputFilter[]{new InputFilter.LengthFilter(5)});
                }

                carNumberEdit.setInputType(InputType.TYPE_CLASS_TEXT);

                carNumberEdit.setText(carStrNum);
                carNumberEdit.setSelection(carStrNum.length());



                new AlertDialog.Builder(this)
                        .setTitle("选择车牌号")
                        .setView(outerView)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String carStr = carNumberEdit.getText().toString();
                                if (isEnergy){
                                    if (carStr.length() != 6){
                                        Toast.makeText(CarInfoActivity.this, "请输入正确位数的新能源汽车车牌", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                }else {
                                    if (carStr.length() != 5){
                                        Toast.makeText(CarInfoActivity.this, "请输入正确位数的燃油汽车车牌", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                }
                                String carStrBig = carStr.toUpperCase();
                                Toast.makeText(CarInfoActivity.this,
                                        provice.getSelectedItem() + city.getSelectedItem() + carStrBig,
                                        Toast.LENGTH_SHORT).show();
                                carNumberText.setText(provice.getSelectedItem() + city.getSelectedItem() + carStrBig);
                            }
                        })
                        .show();

                break;
            case R.id.province_layout:
                if (canClick == 1){
                    return;
                }
                Log.e(TAG, "onClick: 1");
                View proView = LayoutInflater.from(this).inflate(R.layout.dialog_province_view, null);
                shengWv = (WheelView) proView.findViewById(R.id.wheel_view_sheng);
                shengWv.setItems(shengList,currentShengPosition);//init selected position is 0 初始选中位置为0
                shengWv.setOnItemSelectedListener(new WheelView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(int selectedIndex, String item) {
                      //  currentCity = city.getSelectedPosition();
                        currentShengPosition = shengWv.getSelectedPosition();
                        currentSheng =  shengWv.getSelectedItem();
                        getShi();;
                        shiWv.setItems(shiList,currentShiPosition);
                        currentShi = shiWv.getSelectedItem();
                        getXian();
                        xianWv.setItems(xianList,currentXianPosition);
                    }
                });

                shiWv = (WheelView) proView.findViewById(R.id.wheel_view_shi);
                shiWv.setItems(shiList,currentShiPosition);//init selected position is 0 初始选中位置为0
                shiWv.setOnItemSelectedListener(new WheelView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(int selectedIndex, String item) {
                        //  currentCity = city.getSelectedPosition();
                        currentShiPosition = shiWv.getSelectedPosition();
                        currentShi = shiWv.getSelectedItem();
                        getXian();
                        xianWv.setItems(xianList,currentXianPosition);
                    }
                });

                xianWv = (WheelView) proView.findViewById(R.id.wheel_view_xian);
                xianWv.setItems(xianList,currentXianPosition);//init selected position is 0 初始选中位置为0
                xianWv.setOnItemSelectedListener(new WheelView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(int selectedIndex, String item) {
                        //  currentCity = city.getSelectedPosition();
                        currentXianPosition = xianWv.getSelectedPosition();
                    }
                });
                new AlertDialog.Builder(this)
                        .setTitle("选择常驻地址")
                        .setView(proView)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String areStr = "";
                                String sheng = shengWv.getSelectedItem();
                                String shi = shiWv.getSelectedItem();
                                String xian = xianWv.getSelectedItem();
                                if (xian.equals("无")){
                                    areStr = sheng + shi;
                                }else {
                                    areStr = sheng + shi + xian;
                                }
                                provinceText.setText(areStr);

                                if (xian.equals("无") || xian.equals("")){
                                    if (shi.equals("无") || shi.equals("")){//没有市 没有县  获取省的Id
                                        DbManager db = new DbConfig().getDbManager();
                                        List<Province> provinceList = new ArrayList<>();
                                        try {
                                            provinceList  = db.selector(Province.class)
                                                    .where("name" , "=" , sheng)
                                                    .findAll();
                                        } catch (DbException e) {
                                        }
                                        areaId = provinceList.get(0).getId();
                                    }else {     //没有县获取市的id
                                        DbManager db = new DbConfig().getDbManager();
                                        List<Province> provinceList = new ArrayList<>();
                                        try {
                                            provinceList  = db.selector(Province.class)
                                                    .where("name" , "=" , shi)
                                                    .findAll();
                                        } catch (DbException e) {
                                        }
                                        areaId = provinceList.get(0).getId();
                                    }
                                }else {//获取县的id
                                    DbManager db = new DbConfig().getDbManager();
                                    List<Province> provinceList = new ArrayList<>();
                                    try {
                                        provinceList  = db.selector(Province.class)
                                                .where("name" , "=" , xian)
                                                .findAll();
                                    } catch (DbException e) {
                                    }
                                    areaId = provinceList.get(0).getId();
                                }
                                Log.e(TAG, "onClick: -----------------+--" + areaId);


                            }
                        })
                        .show();

                break;
            case R.id.xsz_register_time_layout:
                showDataDialog();
                break;

        }

    }

    /**
     * 日期选择控件
     */
    private void showDataDialog() {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setPositiveButton("设置", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (date.length() > 0) { //清除上次记录的日期
                    date.delete(0, date.length());
                }
                if (endDate.length() > 0) { //清除上次记录的日期
                    endDate.delete(0, endDate.length());
                }
                xszRegisterTimeText.setText(date.append(String.valueOf(year)).append("-").append(String.valueOf(month +1)).append("-").append(day));

               /* if (month == 1 && day == 29){
                    xszEndTimeText.setText(endDate.append(String.valueOf(year + 15)).append("-").append(String.valueOf(month +2)).append("-").append(1));
                }else {
                    xszEndTimeText.setText(endDate.append(String.valueOf(year + 15)).append("-").append(String.valueOf(month +1)).append("-").append(day));
                }*/
                dialog.dismiss();
                chooseServiceTime();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });


        final android.support.v7.app.AlertDialog dialog = builder.create();
        View dialogView = View.inflate(this, R.layout.dialog_date, null);
        final DatePicker datePicker = (DatePicker) dialogView.findViewById(R.id.datePicker);

        dialog.setTitle("设置日期");
        dialog.setView(dialogView);
        dialog.show();
        //初始化日期监听事件
        datePicker.init(year, month, day, this);
    }


    @Override
    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        this.year = year;
        this.month = monthOfYear;
        this.day = dayOfMonth;
    }
    /**
     * 获取当前的日期和时间
     */
    private void initDateTime() {
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

    }
}
