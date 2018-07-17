package com.ruyiruyi.ruyiruyi.ui.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
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

import com.bumptech.glide.Glide;
import com.ruyiruyi.ruyiruyi.R;
import com.ruyiruyi.ruyiruyi.db.DbConfig;
import com.ruyiruyi.ruyiruyi.db.model.Location;
import com.ruyiruyi.ruyiruyi.db.model.User;
import com.ruyiruyi.ruyiruyi.ui.activity.base.RyBaseActivity;
import com.ruyiruyi.ruyiruyi.ui.cell.ShopChooseCell;
import com.ruyiruyi.ruyiruyi.ui.fragment.MerchantFragment;
import com.ruyiruyi.ruyiruyi.ui.fragment.OrderFragment;
import com.ruyiruyi.ruyiruyi.ui.model.ServiceType;
import com.ruyiruyi.ruyiruyi.ui.multiType.RoadChoose;
import com.ruyiruyi.ruyiruyi.ui.multiType.Shop;
import com.ruyiruyi.ruyiruyi.utils.RequestUtils;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;
import com.ruyiruyi.rylibrary.cell.ActionBar;
import com.ruyiruyi.rylibrary.cell.AmountView;
import com.ruyiruyi.rylibrary.image.ImageUtils;
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

public class TireFreeChangeActivity extends RyBaseActivity {

    private static final String TAG = TireFreeChangeActivity.class.getSimpleName();
    private ActionBar actionBar;
    private TextView userNameText;
    private TextView userPhoneText;
    private TextView carNumberText;
    private TextView catFontText;
    private TextView carRearText;
    private AmountView fontAmountView;
    private AmountView rearAmountView;


    private ShopChooseCell shopChooseView;
    private Shop shop;
    private LayoutInflater mInflater;
    private int isReach5Years;   //`0不满 1是满五年
    private int rearFreeAmount = 0;
    private int fontFreeAmount = 0;
    private int fontRearFlag;       //fontRearFlag：0：前后轮一致   非0:前后轮不一致

    private int fontMaxCount = 0;
    private int rearMaxCount = 0;

    public int currentFontCount = 0;
    public int currentRearCount = 0;

    private int currentImage = 10; // 11 20  21  30 31  40  41

    protected static final int CHOOSE_PICTURE = 0;
    protected static final int TAKE_PICTURE = 1;
    private String path;
    private Uri tempUri;

    private LinearLayout tireImageLayout1;
    private LinearLayout tireImageLayout2;
    private LinearLayout tireImageLayout3;
    private LinearLayout tireImageLayout4;
    private ImageView codeImage1;
    private ImageView codeImageDelete1;
    private LinearLayout addCodeImage1;
    private ImageView tireImage1;
    private ImageView tireImageDelete1;
    private LinearLayout addTireImage1;

    private ImageView codeImage2;
    private ImageView codeImageDelete2;
    private LinearLayout addCodeImage2;
    private ImageView tireImage2;
    private ImageView tireImageDelete2;
    private LinearLayout addTireImage2;

    private ImageView codeImage3;
    private ImageView codeImageDelete3;
    private LinearLayout addCodeImage3;
    private ImageView tireImage3;
    private ImageView tireImageDelete3;
    private LinearLayout addTireImage3;

    private ImageView codeImage4;
    private ImageView codeImageDelete4;
    private LinearLayout addCodeImage4;
    private ImageView tireImage4;
    private ImageView tireImageDelete4;
    private LinearLayout addTireImage4;



    private Bitmap imgBitmap;
    private Bitmap codeBitmap1;
    private boolean hasCode1 = false;
    private Bitmap tireBitmap1;
    private boolean hasTire1 = false;

    private Bitmap codeBitmap2;
    private boolean hasCode2 = false;
    private Bitmap tireBitmap2;
    private boolean hasTire2 = false;

    private Bitmap codeBitmap3;
    private boolean hasCode3 = false;
    private Bitmap tireBitmap3;
    private boolean hasTire3 = false;

    private Bitmap codeBitmap4;
    private boolean hasCode4 = false;
    private Bitmap tireBitmap4;
    private boolean hasTire4 = false;
    private FrameLayout imageTopLayout;
    private FrameLayout tireCountLayout;
    private FrameLayout fontTireCountLayout;
    private FrameLayout rearTireCountLayout;
    private TextView tireCountText;
    private TextView postOrder;
    private TextView tv_sample;
    private ProgressDialog progressDialog;
    private View tireImageLayoutView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tire_free_change);

        actionBar = (ActionBar) findViewById(R.id.my_action);
        actionBar.setTitle("免费再换");
        ;
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

        progressDialog = new ProgressDialog(this);
        initView();
        initData();
        initShop();
        initAmountView();
        initFreeTireFromService();
        initImageView();
    }

    private void initImageView() {
        if (currentFontCount + currentRearCount == 0) {
            hasTire1 = false;
            hasCode1 = false;
            hasTire2 = false;
            hasCode2 = false;
            hasTire3 = false;
            hasCode3 = false;
            hasTire4 = false;
            hasCode4 = false;
            initTire1Layou();
            initTire2Layou();
            initTire3Layou();
            initTire4Layou();
            initCode1Layout();
            initCode2Layout();
            initCode3Layout();
            initCode4Layout();
            tireImageLayoutView.setVisibility(View.GONE);
            imageTopLayout.setVisibility(View.GONE);
            tireImageLayout1.setVisibility(View.GONE);
            tireImageLayout2.setVisibility(View.GONE);
            tireImageLayout3.setVisibility(View.GONE);
            tireImageLayout4.setVisibility(View.GONE);
        } else if (currentFontCount + currentRearCount == 1) {
            hasTire2 = false;
            hasCode2 = false;
            hasTire3 = false;
            hasCode3 = false;
            hasTire4 = false;
            hasCode4 = false;
            initTire2Layou();
            initTire3Layou();
            initTire4Layou();
            initCode2Layout();
            initCode3Layout();
            initCode4Layout();
            tireImageLayoutView.setVisibility(View.VISIBLE);
            imageTopLayout.setVisibility(View.VISIBLE);
            tireImageLayout1.setVisibility(View.VISIBLE);
            tireImageLayout2.setVisibility(View.GONE);
            tireImageLayout3.setVisibility(View.GONE);
            tireImageLayout4.setVisibility(View.GONE);
        } else if (currentFontCount + currentRearCount == 2) {
            hasTire3 = false;
            hasCode3 = false;
            hasTire4 = false;
            hasCode4 = false;
            initTire3Layou();
            initTire4Layou();
            initCode3Layout();
            initCode4Layout();
            imageTopLayout.setVisibility(View.VISIBLE);
            tireImageLayout1.setVisibility(View.VISIBLE);
            tireImageLayout2.setVisibility(View.VISIBLE);
            tireImageLayout3.setVisibility(View.GONE);
            tireImageLayout4.setVisibility(View.GONE);
        } else if (currentFontCount + currentRearCount == 3) {
            hasTire4 = false;
            hasCode4 = false;
            initTire4Layou();
            initCode4Layout();
            tireImageLayoutView.setVisibility(View.VISIBLE);
            imageTopLayout.setVisibility(View.VISIBLE);
            tireImageLayout1.setVisibility(View.VISIBLE);
            tireImageLayout2.setVisibility(View.VISIBLE);
            tireImageLayout3.setVisibility(View.VISIBLE);
            tireImageLayout4.setVisibility(View.GONE);
        } else if (currentFontCount + currentRearCount == 4) {

            tireImageLayoutView.setVisibility(View.VISIBLE);
            imageTopLayout.setVisibility(View.VISIBLE);
            tireImageLayout1.setVisibility(View.VISIBLE);
            tireImageLayout2.setVisibility(View.VISIBLE);
            tireImageLayout3.setVisibility(View.VISIBLE);
            tireImageLayout4.setVisibility(View.VISIBLE);
        }
    }

    private void initView() {
        mInflater = LayoutInflater.from(this);
        shopChooseView = (ShopChooseCell) findViewById(R.id.shop_choose_cell);
        userNameText = (TextView) findViewById(R.id.user_name_text);
        userPhoneText = (TextView) findViewById(R.id.user_phone_text);
        carNumberText = (TextView) findViewById(R.id.car_number_text);
        catFontText = (TextView) findViewById(R.id.font_tire_count_text);
        carRearText = (TextView) findViewById(R.id.rear_tire_count_text);
        fontAmountView = (AmountView) findViewById(R.id.font_amount_view);
        rearAmountView = (AmountView) findViewById(R.id.area_amount_view);
        tireImageLayoutView = (View) findViewById(R.id.tire_image_layout_view);
        //图片
        tireImageLayout1 = (LinearLayout) findViewById(R.id.tire_image_layout1);

        codeImage1 = (ImageView) findViewById(R.id.code_image1);
        codeImageDelete1 = (ImageView) findViewById(R.id.code_image_delete1);
        addCodeImage1 = (LinearLayout) findViewById(R.id.add_code_image1);

        tireImage1 = (ImageView) findViewById(R.id.tire_image1);
        tireImageDelete1 = (ImageView) findViewById(R.id.tire_image_delete1);
        addTireImage1 = (LinearLayout) findViewById(R.id.add_tire_image1);

        tireImageLayout2 = (LinearLayout) findViewById(R.id.tire_image_layout2);
        codeImage2 = (ImageView) findViewById(R.id.code_image2);
        codeImageDelete2 = (ImageView) findViewById(R.id.code_image_delete2);
        addCodeImage2 = (LinearLayout) findViewById(R.id.add_code_image2);

        tireImage2 = (ImageView) findViewById(R.id.tire_image2);
        tireImageDelete2 = (ImageView) findViewById(R.id.tire_image_delete2);
        addTireImage2 = (LinearLayout) findViewById(R.id.add_tire_image2);

        tireImageLayout3 = (LinearLayout) findViewById(R.id.tire_image_layout3);
        codeImage3 = (ImageView) findViewById(R.id.code_image3);
        codeImageDelete3 = (ImageView) findViewById(R.id.code_image_delete3);
        addCodeImage3 = (LinearLayout) findViewById(R.id.add_code_image3);

        tireImage3 = (ImageView) findViewById(R.id.tire_image3);
        tireImageDelete3 = (ImageView) findViewById(R.id.tire_image_delete3);
        addTireImage3 = (LinearLayout) findViewById(R.id.add_tire_image3);

        tireImageLayout4 = (LinearLayout) findViewById(R.id.tire_image_layout4);
        codeImage4 = (ImageView) findViewById(R.id.code_image4);
        codeImageDelete4 = (ImageView) findViewById(R.id.code_image_delete4);
        addCodeImage4 = (LinearLayout) findViewById(R.id.add_code_image4);

        tireImage4 = (ImageView) findViewById(R.id.tire_image4);
        tireImageDelete4 = (ImageView) findViewById(R.id.tire_image_delete4);
        addTireImage4 = (LinearLayout) findViewById(R.id.add_tire_image4);

        imageTopLayout = (FrameLayout) findViewById(R.id.image_top_layout);

        tireCountLayout = (FrameLayout) findViewById(R.id.tire_count_layout);
        fontTireCountLayout = (FrameLayout) findViewById(R.id.font_tire_count_layout);
        rearTireCountLayout = (FrameLayout) findViewById(R.id.rear_tire_count_layout);
        tireCountText = (TextView) findViewById(R.id.tire_count_text);

        postOrder = (TextView) findViewById(R.id.tire_repair_button);
        tv_sample = (TextView) findViewById(R.id.tv_sample);


        //拍照示例
        RxViewAction.clickNoDouble(tv_sample).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Intent intent = new Intent(getApplicationContext(), PhotoSampleActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("type", "change");
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        RxViewAction.clickNoDouble(postOrder)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (currentRearCount == 0 && currentFontCount == 0) {
                            Toast.makeText(TireFreeChangeActivity.this, "请选择轮胎数量", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (currentRearCount + currentFontCount == 0) {
                            Toast.makeText(TireFreeChangeActivity.this, "请选择更换轮胎数", Toast.LENGTH_SHORT).show();
                            return;
                        } else if (currentRearCount + currentFontCount == 1) {
                            if (!hasTire1 || !hasCode1) {
                                Toast.makeText(TireFreeChangeActivity.this, "请上传图片", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        } else if (currentRearCount + currentFontCount == 2) {
                            if (!hasTire1 || !hasCode1 || !hasCode2 || !hasTire2) {
                                Toast.makeText(TireFreeChangeActivity.this, "请上传图片", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        } else if (currentRearCount + currentFontCount == 3) {
                            if (!hasTire1 || !hasCode1 || !hasCode2 || !hasTire2 || !hasCode3 || !hasTire3) {
                                Toast.makeText(TireFreeChangeActivity.this, "请上传图片", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        } else if (currentRearCount + currentFontCount == 4) {
                            if (!hasTire1 || !hasCode1 || !hasCode2 || !hasTire2 || !hasCode3 || !hasTire3 || !hasCode4 || !hasTire4) {
                                Toast.makeText(TireFreeChangeActivity.this, "请上传图片", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }

                        freeChangeOrder();


                    }
                });
        //数量选择控件
        initAmountView();
        fontAmountView.setOnAmountChangeListener(new AmountView.OnAmountChangeListener() {
            @Override
            public void onAmountChange(View view, int amount) {
                currentFontCount = amount;
                initAmountView();

                if (amount == fontMaxCount) {
                    Toast.makeText(TireFreeChangeActivity.this, "轮胎数量达到购买上限", Toast.LENGTH_SHORT).show();
                }

                initImageView();
            }
        });
        // rearAmountView.setGoods_storage(2);
        rearAmountView.setOnAmountChangeListener(new AmountView.OnAmountChangeListener() {
            @Override
            public void onAmountChange(View view, int amount) {
                currentRearCount = amount;
                initAmountView();

                if (amount == rearMaxCount) {
                    Toast.makeText(TireFreeChangeActivity.this, "轮胎数量达到购买上限", Toast.LENGTH_SHORT).show();
                }
                initImageView();
            }
        });

        //初始化数据信息
        User user = new DbConfig(this).getUser();
        String userNick = user.getNick();
        String userPhone = user.getPhone();
        userNameText.setText(userNick);
        userPhoneText.setText(userPhone);
        //门店选择
        RxViewAction.clickNoDouble(shopChooseView)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Intent intent = new Intent(getApplicationContext(), ShopChooseActivity.class);
                        intent.putExtra(MerchantFragment.SHOP_TYPE, 5);
                        startActivityForResult(intent, TireChangeActivity.CHOOSE_SHOP);
                    }
                });

        /**
         * 添加条形码
         */
        RxViewAction.clickNoDouble(addCodeImage1)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {


                        currentImage = 10;
                        showChoosePicDialog();
                    }
                });
        RxViewAction.clickNoDouble(addTireImage1)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        currentImage = 11;
                        showChoosePicDialog();
                    }
                });
        RxViewAction.clickNoDouble(codeImageDelete1)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        hasCode1 = false;
                        initCode1Layout();
                    }
                });
        RxViewAction.clickNoDouble(tireImageDelete1)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        hasTire1 = false;
                        initTire1Layou();
                    }
                });

        //2
        RxViewAction.clickNoDouble(addCodeImage2)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {


                        currentImage = 20;
                        showChoosePicDialog();
                    }
                });
        RxViewAction.clickNoDouble(addTireImage2)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        currentImage = 21;
                        showChoosePicDialog();
                    }
                });
        RxViewAction.clickNoDouble(codeImageDelete2)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        hasCode2 = false;
                        initCode2Layout();
                    }
                });
        RxViewAction.clickNoDouble(tireImageDelete2)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        hasTire2 = false;
                        initTire2Layou();
                    }
                });

        //3
        RxViewAction.clickNoDouble(addCodeImage3)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {


                        currentImage = 30;
                        showChoosePicDialog();
                    }
                });
        RxViewAction.clickNoDouble(addTireImage3)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        currentImage = 31;
                        showChoosePicDialog();
                    }
                });
        RxViewAction.clickNoDouble(codeImageDelete3)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        hasCode3 = false;
                        initCode3Layout();
                    }
                });
        RxViewAction.clickNoDouble(tireImageDelete3)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        hasTire3 = false;
                        initTire3Layou();
                    }
                });

        //4
        RxViewAction.clickNoDouble(addCodeImage4)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {


                        currentImage = 40;
                        showChoosePicDialog();
                    }
                });
        RxViewAction.clickNoDouble(addTireImage4)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        currentImage = 41;
                        showChoosePicDialog();
                    }
                });
        RxViewAction.clickNoDouble(codeImageDelete4)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        hasCode4 = false;
                        initCode4Layout();
                    }
                });
        RxViewAction.clickNoDouble(tireImageDelete4)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        hasTire4 = false;
                        initTire4Layou();
                    }
                });
    }

    private void freeChangeOrder() {
        showDialogProgress(progressDialog, "免费再换订单提交中...");


        User user = new DbConfig(this).getUser();
        int userId = user.getId();
        int userCarId = user.getCarId();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("storeId", shop.getStoreId());
            jsonObject.put("userCarId", userCarId);
            jsonObject.put("userId", userId);
            if (fontRearFlag == 0) {
                jsonObject.put("fontAmount", currentFontCount + currentRearCount);
                jsonObject.put("rearAmount", 0);
            } else {
                jsonObject.put("fontAmount", currentFontCount);
                jsonObject.put("rearAmount", currentRearCount);
            }
            jsonObject.put("fontRearFlag", fontRearFlag);
            jsonObject.put("orderType", 3);

        } catch (JSONException e) {
        }
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "addUserFreeChangeOrder");
        Log.e(TAG, "initOrderFromService: -++-" + jsonObject.toString());
        params.addBodyParameter("reqJson", jsonObject.toString());
        try {
            if (hasCode1){
                String code1 = ImageUtils.savePhoto(codeBitmap1, this.getObbDir().getAbsolutePath(), "code1");
                File file = new Compressor(getApplicationContext()).compressToFile(new File(code1));
                params.addBodyParameter("shoe1BarCodeImg" ,file);
            }
            if (hasTire1){
                String tire1 = ImageUtils.savePhoto(tireBitmap1, this.getObbDir().getAbsolutePath(), "tire1");
                File file = new Compressor(getApplicationContext()).compressToFile(new File(tire1));
                params.addBodyParameter("shoe1Img" ,file);
            }
            if (hasCode2){
                String code2 = ImageUtils.savePhoto(codeBitmap2, this.getObbDir().getAbsolutePath(), "code2");
                File file = new Compressor(getApplicationContext()).compressToFile(new File(code2));
                params.addBodyParameter("shoe2BarCodeImg" ,file );
            }
            if (hasTire2){
                String tire2 = ImageUtils.savePhoto(tireBitmap2, this.getObbDir().getAbsolutePath(), "tire2");
                File file = new Compressor(getApplicationContext()).compressToFile(new File(tire2));
                params.addBodyParameter("shoe2Img" ,file );
            }
            if (hasCode3){
                String code3 = ImageUtils.savePhoto(codeBitmap3, this.getObbDir().getAbsolutePath(), "code3");
                File file = new Compressor(getApplicationContext()).compressToFile(new File(code3));
                params.addBodyParameter("shoe3BarCodeImg" ,file );
            }
            if (hasTire3){
                String tire3 = ImageUtils.savePhoto(tireBitmap3, this.getObbDir().getAbsolutePath(), "tire3");
                File file = new Compressor(getApplicationContext()).compressToFile(new File(tire3));
                params.addBodyParameter("shoe3Img" ,file );
            }
            if (hasCode4){
                String code4 = ImageUtils.savePhoto(codeBitmap4, this.getObbDir().getAbsolutePath(), "code4");
                File file = new Compressor(getApplicationContext()).compressToFile(new File(code4));
                params.addBodyParameter("shoe4BarCodeImg" ,file );
            }
            if (hasTire4){
                String tire4 = ImageUtils.savePhoto(tireBitmap4, this.getObbDir().getAbsolutePath(), "tire4");
                File file = new Compressor(getApplicationContext()).compressToFile(new File(tire4));
                params.addBodyParameter("shoe4Img" ,file );
            }
        } catch (IOException e) {

        }
        String token = new DbConfig(this).getToken();
        params.addParameter("token", token);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess:---- " + result);
                JSONObject jsonObject1 = null;
                try {
                    jsonObject1 = new JSONObject(result);
                    String status = jsonObject1.getString("status");
                    String msg = jsonObject1.getString("msg");
                    if (status.equals("1")) {
                        Intent intent = new Intent(getApplicationContext(), OrderActivity.class);
                        intent.putExtra(OrderFragment.ORDER_TYPE, "DFH");
                        intent.putExtra(OrderActivity.ORDER_FROM, 1);
                        startActivity(intent);
                        finish();
                    }else {
                        Toast.makeText(TireFreeChangeActivity.this, msg, Toast.LENGTH_SHORT).show();
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
                hideDialogProgress(progressDialog);
            }
        });
    }

    private void initTire4Layou() {
        tireImage4.setVisibility(hasTire4 ? View.VISIBLE : View.GONE);
        addTireImage4.setVisibility(hasTire4 ? View.GONE :View.VISIBLE);
        tireImageDelete4.setVisibility(hasTire4?View.VISIBLE : View.GONE);
    }

    private void initCode4Layout() {
        codeImage4.setVisibility(hasCode4 ? View.VISIBLE : View.GONE);
        addCodeImage4.setVisibility(hasCode4 ? View.GONE :View.VISIBLE);
        codeImageDelete4.setVisibility(hasCode4 ?View.VISIBLE : View.GONE);
    }

    private void initTire3Layou() {
        tireImage3.setVisibility(hasTire3 ? View.VISIBLE : View.GONE);
        addTireImage3.setVisibility(hasTire3 ? View.GONE :View.VISIBLE);
        tireImageDelete3.setVisibility(hasTire3?View.VISIBLE : View.GONE);
    }

    private void initCode3Layout() {
        codeImage3.setVisibility(hasCode3 ? View.VISIBLE : View.GONE);
        addCodeImage3.setVisibility(hasCode3 ? View.GONE :View.VISIBLE);
        codeImageDelete3.setVisibility(hasCode3?View.VISIBLE : View.GONE);
    }

    private void initTire2Layou() {
        tireImage2.setVisibility(hasTire2 ? View.VISIBLE : View.GONE);
        addTireImage2.setVisibility(hasTire2 ? View.GONE :View.VISIBLE);
        tireImageDelete2.setVisibility(hasTire2?View.VISIBLE : View.GONE);
    }

    private void initCode2Layout() {
        codeImage2.setVisibility(hasCode2 ? View.VISIBLE : View.GONE);
        addCodeImage2.setVisibility(hasCode2 ? View.GONE :View.VISIBLE);
        codeImageDelete2.setVisibility(hasCode2?View.VISIBLE : View.GONE);
    }

    private void initTire1Layou() {
        tireImage1.setVisibility(hasTire1 ? View.VISIBLE : View.GONE);
        addTireImage1.setVisibility(hasTire1 ? View.GONE :View.VISIBLE);
        tireImageDelete1.setVisibility(hasTire1?View.VISIBLE : View.GONE);
    }

    private void initCode1Layout() {
        codeImage1.setVisibility(hasCode1 ? View.VISIBLE : View.GONE);
        addCodeImage1.setVisibility(hasCode1 ? View.GONE :View.VISIBLE);
        codeImageDelete1.setVisibility(hasCode1?View.VISIBLE : View.GONE);
    }

    private void showChoosePicDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("设置头像");
        String[] items = {"选择本地照片", "拍照"};
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
        if (currentImage == 10){
            file = new File(this.getObbDir().getAbsolutePath(), "codeimage1.jpg");
        }else if (currentImage == 11){
            file = new File(this.getObbDir().getAbsolutePath(), "tireimage1.jpg");
        }else if (currentImage == 20){
            file = new File(this.getObbDir().getAbsolutePath(), "codeimage2.jpg");
        }else if (currentImage == 21){
            file = new File(this.getObbDir().getAbsolutePath(), "tireimage2.jpg");
        }else if (currentImage == 30){
            file = new File(this.getObbDir().getAbsolutePath(), "codeimage3.jpg");
        }else if (currentImage == 31){
            file = new File(this.getObbDir().getAbsolutePath(), "tireimage3.jpg");
        }else if (currentImage == 40){
            file = new File(this.getObbDir().getAbsolutePath(), "codeimage4.jpg");
        }else if (currentImage == 41){
            file = new File(this.getObbDir().getAbsolutePath(), "tireimage4.jpg");
        }

        path = file.getPath();
        //判断是否是AndroidN以及更高的版本
        if (Build.VERSION.SDK_INT >= 24) {
            openCameraIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            tempUri = FileProvider.getUriForFile(TireFreeChangeActivity.this, "com.ruyiruyi.ruyiruyi.fileProvider", file);
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
        super.onActivityResult(requestCode, resultCode, data);
        Log.e(TAG, "onActivityResult: ------------" + data);
        Log.e(TAG, "onActivityResult: ------------" + requestCode);
        Log.e(TAG, "onActivityResult: ------------" + resultCode);
        if (resultCode == RESULT_OK) { // 如果返回码是可以用的
            switch (requestCode) {
                case TAKE_PICTURE:
                    setImageToViewFromPhone(tempUri,0);
                    //  startPhotoZoom(tempUri); // 开始对图片进行裁剪处理
                    break;
                case CHOOSE_PICTURE:        //从相册选择
                    Uri uri = data.getData();
                    Log.e(TAG, "onActivityResult:+++++++++++++ " + uri);
                    setImageToViewFromPhone(uri,1);
                    //startPhotoZoom(data.getData()); // 开始对图片进行裁剪处理
                    break;

            }
        }else if (resultCode == TireChangeActivity.CHOOSE_SHOP) {
            Bundle bundle = data.getExtras();
            shop = ((Shop) bundle.getSerializable("shop"));
            shopChooseView.setValue(shop.getStoreName(), shop.getStoreImage(), shop.getStoreAddress(), shop.getStoreDistence(), shop.getServiceTypeList(), mInflater);
        }
    }


    /**
     * 未裁剪之后的图片数据
     *setImageToViewFromPhone
     * @param
     */
    protected void setImageToViewFromPhone(Uri uri,int type) { //0是拍  1是选
        int degree = 0;
        if (type == 0){ //拍照
            degree = ImageUtils.readPictureDegree(path);
        }else {     //从相册选择
            degree = ImageUtils.readPictureDegree(uri.toString());
            Log.w(TAG, "setImageToViewFromPhone: " + degree);
        }


        if (uri != null) {
            Bitmap photo = null;
            try {
                photo = ImageUtils.getBitmapFormUri(getApplicationContext(),uri);
            } catch (IOException e) {

            }
            Log.d(TAG,"setImageToView:"+photo);
            if (currentImage == 10 ){
                codeBitmap1 = rotaingImageView(degree, photo);
                hasCode1 = true;
                initCode1Layout();
                codeImage1.setImageBitmap(codeBitmap1);
            }else if (currentImage == 11){
                tireBitmap1 = rotaingImageView(degree,photo);
                hasTire1 = true;
                initTire1Layou();
                tireImage1.setImageBitmap(tireBitmap1);
            }else if (currentImage == 20){
                codeBitmap2 = rotaingImageView(degree, photo);
                hasCode2 = true;
                initCode2Layout();
                codeImage2.setImageBitmap(codeBitmap2);
            }else if (currentImage == 21){
                tireBitmap2 = rotaingImageView(degree,photo);
                hasTire2 = true;
                initTire2Layou();
                tireImage2.setImageBitmap(tireBitmap2);
            }else if (currentImage == 30){
                codeBitmap3 = rotaingImageView(degree, photo);
                hasCode3 = true;
                initCode3Layout();
                codeImage3.setImageBitmap(codeBitmap3);
            }else if (currentImage == 31){
                tireBitmap3 = rotaingImageView(degree,photo);
                hasTire3 = true;
                initTire3Layou();
                tireImage3.setImageBitmap(tireBitmap3);
            }else if (currentImage == 40){
                codeBitmap4 = rotaingImageView(degree, photo);
                hasCode4 = true;
                initCode4Layout();
                codeImage4.setImageBitmap(codeBitmap4);
            }else if (currentImage == 41){
                tireBitmap4 = rotaingImageView(degree,photo);
                hasTire4 = true;
                initTire4Layou();
                tireImage4.setImageBitmap(tireBitmap4);
            }
            //uploadPic(photo);
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

    private void initFreeTireFromService() {
        User user = new DbConfig(this).getUser();
        int carId = user.getCarId();

        int userId = user.getId();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userId", userId);
            jsonObject.put("userCarId", carId);
        } catch (JSONException e) {

        }
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "getUserChangedShoeNumAnd5Year");
        params.addBodyParameter("reqJson", jsonObject.toString());
        String token = new DbConfig(this).getToken();
        params.addParameter("token", token);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess: --" +  result);
                JSONObject jsonObject1 = null;
                JSONObject data = null;
                try {
                    jsonObject1 = new JSONObject(result);
                    String status = jsonObject1.getString("status");
                    String msg = jsonObject1.getString("msg");
                    if (status.equals("1")) {
                        data = jsonObject1.getJSONObject("data");
                        fontFreeAmount = data.getInt("fontAmount");
                        rearFreeAmount = data.getInt("rearAmount");
                        fontRearFlag = data.getInt("fontRearFlag");
                        isReach5Years = data.getInt("isReach5Years");
                        if (fontRearFlag == 0){
                            tireCountLayout.setVisibility(View.VISIBLE);
                            fontTireCountLayout.setVisibility(View.GONE);
                            rearTireCountLayout.setVisibility(View.GONE);
                            tireCountText.setText(fontFreeAmount+"");
                        }else if(fontRearFlag == 1){
                            tireCountLayout.setVisibility(View.GONE);
                            fontTireCountLayout.setVisibility(View.VISIBLE);
                            rearTireCountLayout.setVisibility(View.VISIBLE);
                            catFontText.setText(fontFreeAmount +"");
                            carRearText.setText(rearFreeAmount +"");
                        }else {
                            tireCountLayout.setVisibility(View.VISIBLE);
                            fontTireCountLayout.setVisibility(View.GONE);
                            rearTireCountLayout.setVisibility(View.GONE);
                            tireCountText.setText(0+"");
                        }



                        if (fontFreeAmount == 0 && rearFreeAmount == 0) {
                            Toast.makeText(TireFreeChangeActivity.this, "暂无可更换轮胎", Toast.LENGTH_SHORT).show();
                        }
                        initAmountView();
                    } else if (status.equals("-999")) {
                        showUserTokenDialog("您的账号在其它设备登录,请重新登录");
                    } else {
                        Toast.makeText(TireFreeChangeActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    if (data == null){
                        Toast.makeText(TireFreeChangeActivity.this, "您未有可免费更换得轮胎，快去购买换胎吧", Toast.LENGTH_SHORT).show();
                        fontRearFlag = 0;
                        fontFreeAmount = 0;
                        rearFreeAmount = 0;
                        if (fontRearFlag == 0){
                            tireCountLayout.setVisibility(View.VISIBLE);
                            fontTireCountLayout.setVisibility(View.GONE);
                            rearTireCountLayout.setVisibility(View.GONE);
                            tireCountText.setText(fontFreeAmount+"");
                        }else if(fontRearFlag == 1){
                            tireCountLayout.setVisibility(View.GONE);
                            fontTireCountLayout.setVisibility(View.VISIBLE);
                            rearTireCountLayout.setVisibility(View.VISIBLE);
                            catFontText.setText(fontFreeAmount +"");
                            carRearText.setText(rearFreeAmount +"");
                        }else {
                            tireCountLayout.setVisibility(View.VISIBLE);
                            fontTireCountLayout.setVisibility(View.GONE);
                            rearTireCountLayout.setVisibility(View.GONE);
                            tireCountText.setText(0+"");
                        }
                    }
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

    private void initAmountView() {
      //免费再换
        if (fontRearFlag == 0) { //前后轮一致
            fontMaxCount = 2;
            rearMaxCount = 2;
            if (fontFreeAmount == 4) {   //一共四条轮胎
                fontAmountView.setGoods_storage(2);
                rearAmountView.setGoods_storage(2);
            } else {     //不足四条轮胎
                fontAmountView.setGoods_storage(2);
                rearAmountView.setGoods_storage(2);
                if (currentFontCount + currentRearCount == fontFreeAmount) {        //达到最大轮胎数时 设置为最大数
                    fontAmountView.setGoods_storage(currentFontCount);
                    rearAmountView.setGoods_storage(currentRearCount);
                    fontMaxCount = currentFontCount;
                    rearMaxCount = currentRearCount;
                }
            }
        } else {     //前后轮不一致
            fontMaxCount = fontFreeAmount;
            rearMaxCount = rearFreeAmount;
            fontAmountView.setGoods_storage(fontFreeAmount);
            rearAmountView.setGoods_storage(rearFreeAmount);
        }


    }

    private void initShop() {
        Location location = new DbConfig(this).getLocation();
        String city = location.getCity();
        Double jingdu = location.getJingdu();
        Double weidu = location.getWeidu();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("page", 1);
            jsonObject.put("rows", 1);
            jsonObject.put("cityName", city);
            jsonObject.put("storeName", "");
            jsonObject.put("storeType", "");//门店类型  1:4S店   2:快修店  3:维修厂  4:美容店

            jsonObject.put("rankType", 1);//排序方式  0:默认排序  1：附近优先
            //门店服务类型 2:汽车保养  3:美容清洗  4:改装  5:轮胎服务
            jsonObject.put("serviceType", 5); //针对性门店

            jsonObject.put("longitude", jingdu + "");
            jsonObject.put("latitude", weidu + "");
        } catch (JSONException e) {
        }
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "selectStoreByCondition");
        params.addBodyParameter("reqJson", jsonObject.toString());
        String token = new DbConfig(this).getToken();
        params.addParameter("token", token);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject1 = new JSONObject(result);
                    String status = jsonObject1.getString("status");
                    String msg = jsonObject1.getString("msg");

                    if (status.equals("1")) {
                        JSONObject data = jsonObject1.getJSONObject("data");
                        JSONArray storeQuaryResVos = data.getJSONArray("storeQuaryResVos");

                        JSONObject storeObjecct = storeQuaryResVos.getJSONObject(0);
                        String distance = storeObjecct.getString("distance");
                        String storeAddress = storeObjecct.getString("storeAddress");
                        String storeId = storeObjecct.getString("storeId");
                        int storeIdInt = Integer.parseInt(storeId);
                        String storeImg = storeObjecct.getString("storeImg");
                        String storeName = storeObjecct.getString("storeName");
                        String storeType = storeObjecct.getString("storeType");
                        String storeTypeColor = storeObjecct.getString("storeTypeColor");
                        JSONArray serviceArray = storeObjecct.getJSONArray("storeServcieList");
                        List<ServiceType> serviceTypeList = new ArrayList<ServiceType>();
                        for (int j = 0; j < serviceArray.length(); j++) {
                            JSONObject serviceObject = serviceArray.getJSONObject(j);
                            JSONObject service = serviceObject.getJSONObject("service");
                            String color = service.getString("color");
                            String name = service.getString("name");
                            serviceTypeList.add(new ServiceType(name, color));
                        }
                        shop = new Shop(storeIdInt, storeType, storeTypeColor, storeName, storeImg, storeAddress, distance);
                        shop.setServiceTypeList(serviceTypeList);
                        shopChooseView.setValue(shop.getStoreName(), shop.getStoreImage(), shop.getStoreAddress(), shop.getStoreDistence(), shop.getServiceTypeList(), mInflater);

                    } else if (status.equals("-999")) {
                        showUserTokenDialog("您的账号在其它设备登录,请重新登录");
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

    private void initData() {
        User user = new DbConfig(this).getUser();
        int carId = user.getCarId();
        int userId = user.getId();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userId", userId);
            jsonObject.put("userCarId", carId);
        } catch (JSONException e) {
        }
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "getCarByUserIdAndCarId");
        params.addBodyParameter("reqJson", jsonObject.toString());
        String token = new DbConfig(this).getToken();
        params.addParameter("token", token);
        x.http().post(params, new Callback.CommonCallback<String>() {
            private String carNumber;

            @Override
            public void onSuccess(String result) {
                JSONObject jsonObject1 = null;
                try {
                    jsonObject1 = new JSONObject(result);
                    String status = jsonObject1.getString("status");
                    String msg = jsonObject1.getString("msg");
                    if (status.equals("1")){
                        JSONObject data = jsonObject1.getJSONObject("data");
                        carNumber = data.getString("platNumber");
                        carNumberText.setText(carNumber);

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

}
