package com.ruyiruyi.merchant.ui.fragment;


import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ruyiruyi.merchant.R;
import com.ruyiruyi.merchant.bean.XiangmusBean;
import com.ruyiruyi.merchant.db.DbConfig;
import com.ruyiruyi.merchant.db.model.ServiceType;
import com.ruyiruyi.merchant.ui.activity.MyPicDialogActivity;
import com.ruyiruyi.merchant.utils.UtilsRY;
import com.ruyiruyi.merchant.utils.UtilsURL;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;
import com.ruyiruyi.rylibrary.base.BaseActivity;
import com.ruyiruyi.rylibrary.base.BaseFragment;
import com.ruyiruyi.rylibrary.image.ImageUtils;
import com.ruyiruyi.rylibrary.ui.cell.WheelView;
import com.ruyiruyi.rylibrary.utils.glide.GlideRoundTransform;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.common.util.DensityUtil;
import org.xutils.ex.DbException;
import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import rx.functions.Action1;


public class StoreXiangQingFragment extends BaseFragment implements CompoundButton.OnCheckedChangeListener {

    private String TAG = StoreXiangQingFragment.class.getSimpleName();


    private TextView tv_shoptime;
    private LinearLayout ll_xiangmus;
    private TextView tv_save;

    private TextView tv_shopname;
    private TextView tv_shopcategory;
    private TextView tv_shopphone;
    private TextView tv_shopcity;
    private TextView tv_shoplocation;
    private ImageView img_mdpic_a;
    private ImageView img_mdpic_b;
    private ImageView img_mdpic_c;
    private ImageView img_mdpic_a_delete;
    private ImageView img_mdpic_b_delete;
    private ImageView img_mdpic_c_delete;
    private ImageView img_mdpic_a_center;
    private ImageView img_mdpic_b_center;
    private ImageView img_mdpic_c_center;
    private boolean hasPicA = true;
    private boolean hasPicB = true;
    private boolean hasPicC = true;
    private boolean isNewPicA = false;
    private boolean isNewPicB = false;
    private boolean isNewPicC = false;
    private Switch mSwitch;

    /*
    * 照片对应 :  mdPic_a_url = data.getString("locationImgUrl");
                    mdPic_b_url = data.getString("indoorImgUrl");
                    mdPic_c_url = data.getString("factoryImgUrl");
    * */

    private WheelView whv_lTime, whv_rTime;

    public String currentlTime = "00:00:00";
    public String currentrTime = "00:00:00";
    public int currentlTimePosition = 0;
    public int currentrTimePosition = 0;
    private List<String> lTime_list;
    private List<String> rTime_list;
    private List<XiangmusBean> list_xms = new ArrayList<>();
    private List<String> serviceTypeList = new ArrayList<>();
    private String serviceTypeListString;

    private String shopTimeL = "00:00:00";
    private String shopTimeR = "00:00:00";

    private String storeTimeL_old;
    private String storeTimeR_old;
    private List<String> storeServiceList_old = new ArrayList<>();
    private String storeServiceList_old_String;
    private String storeName;
    private String storeCategory;
    private String storePhone;
    private String storeCity;
    private String storeLocation;
    private String mdPic_a_url;
    private String mdPic_b_url;
    private String mdPic_c_url;
    private boolean isClicked = false;
    private int isOpen = 1; //默认开店营业 2 不营业 1 营业
    private ProgressDialog progressDialog;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 111:
                    bindView();
                    getData();
                    break;
                case 222:
                    textAddXMView();//设置项目控件（此时含用户选择状态）
                    setData();
                    break;
            }
        }
    };
    private final int CHOOSE_PICTURE = 0;
    private final int TAKE_PICTURE = 1;
    private int currentImage = 0;// 0  1  2
    private String path_;
    protected static Uri tempUri;
    private Bitmap mdPicaBitmap;
    private Bitmap mdPicbBitmap;
    private Bitmap mdPiccBitmap;
    private String mdpicaPath;
    private String mdpicbPath;
    private String mdpiccPath;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.store_xiangqing_fg, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        progressDialog = new ProgressDialog(getActivity());

        initView();
        initRegisterServiceTypeData();
        /*bindView();
        getData();       已移至handler 中
        setData();*/

    }

    private void setData() {
        //控件预设数据
        tv_shoptime.setText(storeTimeL_old + " 至 " + storeTimeR_old);
        tv_shopname.setText(storeName);
        tv_shopcategory.setText(storeCategory);
        tv_shopphone.setText(storePhone);
        tv_shopcity.setText(storeCity);
        tv_shoplocation.setText(storeLocation);
/*        ImageOptions myOptions = new ImageOptions.Builder()
                .setRadius(DensityUtil.dip2px(2))
                .setCircular(false)
                .setPlaceholderScaleType(ImageView.ScaleType.FIT_XY)
                .setSquare(true)// setSquare和build为必须设置
                .build();
        x.image().bind(img_mdpic_a, mdPic_a_url, myOptions);
        x.image().bind(img_mdpic_b, mdPic_b_url, myOptions);
        x.image().bind(img_mdpic_c, mdPic_c_url, myOptions);*/
        Glide.with(getActivity()).load(mdPic_a_url)
                .transform(new GlideRoundTransform(getActivity(), 5))
                .diskCacheStrategy(DiskCacheStrategy.NONE)//跳过硬盘缓存
                .skipMemoryCache(true)//跳过内存缓存
                .into(img_mdpic_a);
        Glide.with(getActivity()).load(mdPic_b_url)
                .transform(new GlideRoundTransform(getActivity(), 5))
                .diskCacheStrategy(DiskCacheStrategy.NONE)//跳过硬盘缓存
                .skipMemoryCache(true)//跳过内存缓存
                .into(img_mdpic_b);
        Glide.with(getActivity()).load(mdPic_c_url)
                .transform(new GlideRoundTransform(getActivity(), 5))
                .diskCacheStrategy(DiskCacheStrategy.NONE)//跳过硬盘缓存
                .skipMemoryCache(true)//跳过内存缓存
                .into(img_mdpic_c);
        if (isOpen == 2) {
            mSwitch.setChecked(false);
        } else {
            mSwitch.setChecked(true);
        }

        //mSwitch 绑定点击事件
        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    isOpen = 1;//2 不营业  1 营业
                } else {
                    isOpen = 2;
                }
            }
        });

    }

    private void getData() {
        String id_ = new DbConfig(getActivity()).getId() + "";
        JSONObject object = new JSONObject();
        try {
            object.put("storeId", id_);
        } catch (JSONException e) {
        }
        RequestParams params = new RequestParams(UtilsURL.REQUEST_URL + "getStoreInfoByStoreId");
        params.addBodyParameter("reqJson", object.toString());
        Log.e(TAG, "getData: 10086 params.toString()" + params.toString());
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONObject data = jsonObject.getJSONObject("data");
                    storeName = data.getString("storeName");
                    JSONArray storeServcieList = data.getJSONArray("storeServcieList");
                    for (int i = 0; i < storeServcieList.length(); i++) {
                        JSONObject storeServcie = (JSONObject) storeServcieList.get(i);
                        storeServiceList_old.add(storeServcie.getInt("serviceType") + "");
                    }
                    storeServiceList_old_String = "";
                    for (int i = 0; i < storeServiceList_old.size(); i++) {//顺便保存原来所选 转换为String
                        if (i == storeServiceList_old.size() - 1) {
                            storeServiceList_old_String = storeServiceList_old_String + storeServiceList_old.get(i);
                        } else {
                            storeServiceList_old_String = storeServiceList_old_String + storeServiceList_old.get(i) + ",";
                        }
                    }
                    serviceTypeList.clear();
                    for (int i = 0; i < storeServiceList_old.size(); i++) {//原始值 转给 要提交的参数list 点击之前
                        serviceTypeList.add(storeServiceList_old.get(i));
                    }

                    storeCategory = data.getString("storeType");
                    storePhone = data.getString("storePhone");
                    isOpen = Integer.parseInt(data.getString("status"));
                    Log.e(TAG, "onSuccess:request10010 isOpen = " + isOpen);
                    Log.e(TAG, "onSuccess:request10010 data.getstatus= " + data.getString("status"));
                    storeTimeL_old = data.getString("storeStartTime").substring(11, 19);//"storeStartTime": "2000-01-01 08:00:00.0",
                    storeTimeR_old = data.getString("storeEndTime").substring(11, 19);
                    storeCity = data.getString("storeLocation");
                    storeLocation = data.getString("storeAddress");
                    mdPic_a_url = data.getString("locationImgUrl");
                    mdPic_b_url = data.getString("indoorImgUrl");
                    mdPic_c_url = data.getString("factoryImgUrl");
                    shopTimeL = storeTimeL_old;//设置默认提交时间与原时间一致
                    shopTimeR = storeTimeR_old;

                    Log.e(TAG, "onSuccess:10086 list_xms.size()" + list_xms.size());
                    for (int i = 0; i < list_xms.size(); i++) {
                        for (int y = 0; y < storeServiceList_old.size(); y++) {
                            if ((list_xms.get(i).getId() + "").equals(storeServiceList_old.get(y))) {
                                list_xms.get(i).setIsChecked(1);
                            } else {
                            }
                        }
                    }
                    Log.e(TAG, "onSuccess:10086 list_xms.size()" + list_xms.size());

                    Message message = new Message();
                    message.what = 222;// 第二次  （这里可以不用Handler 只是便于修改）
                    mHandler.sendMessage(message);
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
        //修改照片 删除点击事件
        RxViewAction.clickNoDouble(img_mdpic_a_delete).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                img_mdpic_a.setImageResource(R.drawable.img_bg_dark);
                img_mdpic_a_delete.setVisibility(View.GONE);
                img_mdpic_a_center.setVisibility(View.VISIBLE);
                hasPicA = false;
                isNewPicA = true;
            }
        });
        RxViewAction.clickNoDouble(img_mdpic_b_delete).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                img_mdpic_b.setImageResource(R.drawable.img_bg_dark);
                img_mdpic_b_delete.setVisibility(View.GONE);
                img_mdpic_b_center.setVisibility(View.VISIBLE);
                hasPicB = false;
                isNewPicB = true;
            }
        });
        RxViewAction.clickNoDouble(img_mdpic_c_delete).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                img_mdpic_c.setImageResource(R.drawable.img_bg_dark);
                img_mdpic_c_delete.setVisibility(View.GONE);
                img_mdpic_c_center.setVisibility(View.VISIBLE);
                hasPicC = false;
                isNewPicC = true;
            }
        });
        //修改照片 添加照片点击事件
        RxViewAction.clickNoDouble(img_mdpic_a_center).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                currentImage = 0;
                showPicInputDialog();
            }
        });
        RxViewAction.clickNoDouble(img_mdpic_b_center).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                currentImage = 1;
                showPicInputDialog();
            }
        });
        RxViewAction.clickNoDouble(img_mdpic_c_center).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                currentImage = 2;
                showPicInputDialog();
            }
        });

        //mSwitch 点击事件在数据获取之后 绑定 （在setData方法中）

        //shopTime
        RxViewAction.clickNoDouble(tv_shoptime).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                currentlTime = "00:00:00";//每次进入dialog初始化
                currentrTime = "00:00:00";//每次进入dialog初始化
                View v_shoptime = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_shoptime_view, null);
                whv_lTime = (WheelView) v_shoptime.findViewById(R.id.whv_ltime);
                whv_rTime = (WheelView) v_shoptime.findViewById(R.id.whv_rtime);
                whv_lTime.setItems(getStrLTime(), 0);
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
                whv_rTime.setItems(rTime_list, 0);
                whv_rTime.setOnItemSelectedListener(new WheelView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(int selectedIndex, String item) {
                        currentrTimePosition = whv_rTime.getSelectedPosition();
                        currentrTime = item;
                    }
                });
                new AlertDialog.Builder(getActivity())
                        .setTitle("选择营业时间")
                        .setView(v_shoptime)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                shopTimeL = currentlTime;//点击确定再更新值
                                shopTimeR = currentrTime;//点击确定再更新值
                                tv_shoptime.setText(currentlTime + " 至 " + currentrTime);
                            }
                        }).show();
            }
        });


        //提交
        RxViewAction.clickNoDouble(tv_save).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
        /* isOpen
        shoptimeL    shoptimeR
        storeServiceList_old_String --> serviceTypeListString */
                if (tv_shoptime.getText() == null || tv_shoptime.length() == 0) {
                    Toast.makeText(getActivity(), "请选择营业时间", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (serviceTypeList == null || serviceTypeList.size() == 0) {//为空 一定点击过XM  为全部未选  则提示
                    Toast.makeText(getActivity(), "请选择合作项目", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    serviceTypeListString = "";
                    for (int i = 0; i < serviceTypeList.size(); i++) {
                        if (i == serviceTypeList.size() - 1) {
                            serviceTypeListString = serviceTypeListString + serviceTypeList.get(i);
                        } else {
                            serviceTypeListString = serviceTypeListString + serviceTypeList.get(i) + ",";
                        }
                    }
                    Log.e(TAG, "call: XiangQing serviceTypeListString" + serviceTypeListString);
                }
                if (isNewPicA && !hasPicA) {
                    Toast.makeText(getActivity(), "请补全门店照片", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (isNewPicB && !hasPicB) {
                    Toast.makeText(getActivity(), "请补全门店照片", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (isNewPicC && !hasPicC) {
                    Toast.makeText(getActivity(), "请补全门店照片", Toast.LENGTH_SHORT).show();
                    return;
                }


                showSaveDialog();


            }
        });
        //门店照片 a
        RxViewAction.clickNoDouble(img_mdpic_a).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                /*showPicDialog(mdPic_a_url);*/
                if (!hasPicA) {
                    return;
                }
                Intent intent = new Intent(getActivity(), MyPicDialogActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("imgUrl", mdPic_a_url);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        //门店照片 b
        RxViewAction.clickNoDouble(img_mdpic_b).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                /*showPicDialog(mdPic_b_url);*/
                if (!hasPicB) {
                    return;
                }
                Intent intent = new Intent(getActivity(), MyPicDialogActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("imgUrl", mdPic_b_url);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        //门店照片 c
        RxViewAction.clickNoDouble(img_mdpic_c).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
              /*  showPicDialog(mdPic_c_url);*/
                if (!hasPicC) {
                    return;
                }
                Intent intent = new Intent(getActivity(), MyPicDialogActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("imgUrl", mdPic_c_url);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    private void showPicInputDialog() {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getActivity());
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
        final android.support.v7.app.AlertDialog dialog = builder.create();
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialog.dismiss();
            }
        });
        dialog.show();
        //设置按钮颜色
        dialog.getButton(android.support.v7.app.AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.theme_primary));
    }

    private void takePicture() {
        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (Build.VERSION.SDK_INT >= 23) {
            // 需要申请动态权限
            int check = ContextCompat.checkSelfPermission(getActivity(), permissions[0]);
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
        }

        //判断是否是AndroidN以及更高的版本
        if (Build.VERSION.SDK_INT >= 24) {
            openCameraIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            tempUri = FileProvider.getUriForFile(getActivity(), "com.ruyiruyi.merchant.fileProvider", file);
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

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

    //未剪辑照片
    private void setImageToViewFromPhone(Uri uri, boolean isCamera) {
        int degree = 0;
        if (isCamera) {
            degree = ImageUtils.readPictureDegree(path_);
        }
        if (uri != null) {
            Bitmap photo = null;
            try {
                photo = ImageUtils.getBitmapFormUri(getActivity(), uri);
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
                hasPicA = true;
            } else if (currentImage == 1) {
                if (isCamera) {
                    mdPicbBitmap = rotaingImageView(degree, photo);
                } else {
                    mdPicbBitmap = photo;
                }
                img_mdpic_b.setImageBitmap(mdPicbBitmap);
                img_mdpic_b_delete.setVisibility(View.VISIBLE);
                img_mdpic_b_center.setVisibility(View.GONE);
                hasPicB = true;
            } else if (currentImage == 2) {
                if (isCamera) {
                    mdPiccBitmap = rotaingImageView(degree, photo);
                } else {
                    mdPiccBitmap = photo;
                }
                img_mdpic_c.setImageBitmap(mdPiccBitmap);
                img_mdpic_c_delete.setVisibility(View.VISIBLE);
                img_mdpic_c_center.setVisibility(View.GONE);
                hasPicC = true;
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


    private void showSaveDialog() {
        android.support.v7.app.AlertDialog dialog = new android.support.v7.app.AlertDialog.Builder(getActivity()).create();
        View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_save_commit, null);
        TextView error_text = (TextView) dialogView.findViewById(R.id.save_text);
        error_text.setText("确定提交保存吗");
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
                showDialogProgress(progressDialog, "店铺信息保存中...");
                if (isNewPicA) {
                    mdpicaPath = ImageUtils.savePhoto(mdPicaBitmap, Environment
                            .getExternalStorageDirectory().getAbsolutePath(), "mdPica");
                }
                if (isNewPicB) {
                    mdpicbPath = ImageUtils.savePhoto(mdPicbBitmap, Environment
                            .getExternalStorageDirectory().getAbsolutePath(), "mdPicb");
                }
                if (isNewPicC) {
                    mdpiccPath = ImageUtils.savePhoto(mdPiccBitmap, Environment
                            .getExternalStorageDirectory().getAbsolutePath(), "mdPicc");
                }
                String storeId = new DbConfig(getActivity()).getId() + "";
                JSONObject object = new JSONObject();
                try {
                    object.put("id", storeId);
                    object.put("status", isOpen + "");
                    object.put("phone", new DbConfig(getActivity()).getUser().getPhone());
                    Log.e(TAG, "showd:10010 isOpen = " + isOpen);
                    object.put("startTime", "2000-01-01T" + shopTimeL + ".000+0800");
                    object.put("endTime", "2000-01-01T" + shopTimeR + ".000+0800");

                } catch (JSONException e) {
                }
                RequestParams params = new RequestParams(UtilsURL.REQUEST_URL + "updateStoreInfoByStoreId");
                params.addBodyParameter("reqJson", object.toString());
                params.addBodyParameter("serviceTypeList", serviceTypeListString);
                if (isNewPicA) {
                    params.addBodyParameter("location_img", new File(mdpicaPath));
                }
                if (isNewPicB) {
                    params.addBodyParameter("indoor_img", new File(mdpicbPath));
                }
                if (isNewPicC) {
                    params.addBodyParameter("factory_img", new File(mdpiccPath));
                }
                params.setConnectTimeout(6000);
                Log.e(TAG, "onClick:110 serviceTypeListString = " + serviceTypeListString);
                x.http().post(params, new Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(String result) {//提交请求
                        try {
                            JSONObject object1 = new JSONObject(result);
                            int status = object1.getInt("status");
                            if (status == 1) {
                                Toast.makeText(getActivity(), "更新店铺成功", Toast.LENGTH_SHORT).show();
                                getActivity().finish();
                            } else {
                                Toast.makeText(getActivity(), "更新店铺失败", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                        }
                    }

                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {
                        Toast.makeText(getActivity(), "更新店铺失败,请检查网络", Toast.LENGTH_SHORT).show();
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
        dialog.getButton(android.support.v7.app.AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.theme_primary));
        dialog.getButton(android.support.v7.app.AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.theme_primary));


    }

    private void initView() {
        //可修改的控件
        tv_shoptime = (TextView) getView().findViewById(R.id.tv_shoptime);
        tv_save = (TextView) getView().findViewById(R.id.tv_save);
        ll_xiangmus = (LinearLayout) getView().findViewById(R.id.ll_xiangmus);
        //不可修改的控件
        tv_shopname = (TextView) getView().findViewById(R.id.tv_shopname);
        tv_shopcategory = (TextView) getView().findViewById(R.id.tv_shopcategory);
        tv_shopphone = (TextView) getView().findViewById(R.id.tv_shopphone);
        tv_shopcity = (TextView) getView().findViewById(R.id.tv_shopcity);
        tv_shoplocation = (TextView) getView().findViewById(R.id.tv_shoplocation);
        img_mdpic_a = (ImageView) getView().findViewById(R.id.img_mdpic_a);
        img_mdpic_b = (ImageView) getView().findViewById(R.id.img_mdpic_b);
        img_mdpic_c = (ImageView) getView().findViewById(R.id.img_mdpic_c);
        img_mdpic_a_delete = (ImageView) getView().findViewById(R.id.img_mdpic_a_delete);
        img_mdpic_b_delete = (ImageView) getView().findViewById(R.id.img_mdpic_b_delete);
        img_mdpic_c_delete = (ImageView) getView().findViewById(R.id.img_mdpic_c_delete);
        img_mdpic_a_center = (ImageView) getView().findViewById(R.id.img_mdpic_a_center);
        img_mdpic_b_center = (ImageView) getView().findViewById(R.id.img_mdpic_b_center);
        img_mdpic_c_center = (ImageView) getView().findViewById(R.id.img_mdpic_c_center);
        mSwitch = (Switch) getView().findViewById(R.id.swch_isopen);

    }

    private void textAddXMView() {

        for (int i = 0; i < list_xms.size(); i++) {
            Log.e(TAG, "textAddXMView: 10086  IsChecked() " + "i=" + i + "  " + list_xms.get(i).getIsChecked());
        }

        if (list_xms == null) {
            Toast.makeText(getActivity(), "请检查网络", Toast.LENGTH_SHORT).show();
        } else {
            for (int i = 0; i < (list_xms.size() % 3 == 0 ? (list_xms.size() / 3) : (list_xms.size() / 3 + 1)); i++) {
                View view = LayoutInflater.from(getActivity()).inflate(R.layout.register_xm_add_item, null);
                CheckBox checkBoxa = (CheckBox) view.findViewById(R.id.checkbox_a);
                CheckBox checkBoxb = (CheckBox) view.findViewById(R.id.checkbox_b);
                CheckBox checkBoxc = (CheckBox) view.findViewById(R.id.checkbox_c);
                checkBoxa.setText(list_xms.get(i * 3 + 0).getName());
                Log.e(TAG, "textAddXMView:10086 1");
                Log.e(TAG, "textAddXMView: a10086 getIsChecked()" + list_xms.get(i * 3 + 0).getIsChecked());
                checkBoxa.setChecked(list_xms.get(i * 3 + 0).getIsChecked() == 1 ? true : false);
                checkBoxa.setTag(list_xms.get(i * 3 + 0));
                checkBoxa.setOnCheckedChangeListener(this);

                checkBoxb.setText((i * 3 + 1 >= list_xms.size()) ? "" : list_xms.get(i * 3 + 1).getName());
                Log.e(TAG, "textAddXMView:10086 2");
                if (!(i * 3 + 1 >= list_xms.size())) {
                    checkBoxb.setChecked(list_xms.get(i * 3 + 1).getIsChecked() == 1 ? true : false);
                }
                checkBoxb.setVisibility((i * 3 + 1 >= list_xms.size()) ? View.GONE : View.VISIBLE);
                checkBoxb.setTag((i * 3 + 1 >= list_xms.size()) ? null : list_xms.get(i * 3 + 1));
                checkBoxb.setOnCheckedChangeListener(this);

                checkBoxc.setText((i * 3 + 2 >= list_xms.size()) ? "" : list_xms.get(i * 3 + 2).getName());
                Log.e(TAG, "textAddXMView:10086 3");
                if (!(i * 3 + 2 >= list_xms.size())) {
                    checkBoxc.setChecked(list_xms.get(i * 3 + 2).getIsChecked() == 1 ? true : false);
                }

                checkBoxc.setVisibility((i * 3 + 2 >= list_xms.size()) ? View.GONE : View.VISIBLE);
                checkBoxc.setTag((i * 3 + 2 >= list_xms.size()) ? null : list_xms.get(i * 3 + 2));
                checkBoxc.setOnCheckedChangeListener(this);

                ll_xiangmus.addView(view);
            }
        }

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

    public List<String> getRTimeList(int selectedIndex) {
        rTime_list = new ArrayList<String>();
        rTime_list.addAll(lTime_list.subList(selectedIndex, lTime_list.size()));
        return rTime_list;
    }


    //展示图片dialog 效果不佳 未使用
    private void showPicDialog(String pic_url) {

        AlertDialog dialog = new AlertDialog.Builder(getActivity()).create();
        View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_picture, null);
        ImageView img_dialog_pic = (ImageView) dialogView.findViewById(R.id.img_dialog_pic);
/*        //设置imageView 宽高
        WindowManager manager = getActivity().getWindowManager();
        Display display = manager.getDefaultDisplay();//为获取屏幕宽高
        LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) img_dialog_pic.getLayoutParams();
        linearParams.width = (int) (display.getWidth() * 0.9);
        linearParams.height = (int) (display.getHeight() * 0.9);*/

        ImageOptions myOptions = new ImageOptions.Builder()
                .setCircular(false)
                .setPlaceholderScaleType(ImageView.ScaleType.FIT_XY)
                .setSquare(true)// setSquare和build为必须设置
                .build();
        x.image().bind(img_dialog_pic, pic_url, myOptions);
        Log.e(TAG, "showPicDialog: " + pic_url);
        dialog.setView(dialogView);
        //Glide.with(getActivity()).load(pic_url).into(img_dialog_pic);

        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "返回", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.theme_primary));
/*        //设置Dialog大小
        WindowManager manager = getActivity().getWindowManager();
        Display display = manager.getDefaultDisplay();//为获取屏幕宽高
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();//获取对话框当前的参数值
        params.height = (int) (display.getHeight()*0.9);//设置高度未屏幕的0.8
        params.width = (int) (display.getWidth()*0.9);
        dialog.getWindow().setAttributes(params);//设置参数生效*/

    }

    private void initRegisterServiceTypeData() {
        JSONObject object = new JSONObject();
        try {
            object.put("time", "2000-00-00 00:00:00");

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
                xiangmusBean.setIsChecked(0);//0为false （此为总列表 默认全部未选）
                xiangmusBean.setName(obj.getString("name"));
//                Log.e(TAG, "saveServiceType: xiangmusBean.getName() = "+xiangmusBean.getName() );
                list_xms.add(xiangmusBean);
//                Log.e(TAG, "saveServiceType: list_xms.toString()0 = " +list_xms.toString() );
            } catch (JSONException e) {
            }
        }
        //2 下载数据完成后再动态添加View
//    2  textAddXMView();
        Message msg = new Message();
        msg.what = 111;//第一次
        mHandler.sendMessage(msg);   //下载完默认ServiceType列表之后再进行接下来操作
    }

    private void showMyDialog(String error) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_error, null);
        TextView error_text = (TextView) dialogView.findViewById(R.id.error_text);
        error_text.setText(error);
        dialog.setTitle("如意如驿商家版");
        dialog.setIcon(R.drawable.ic_logo_huise);
        dialog.setView(dialogView);
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        XiangmusBean bean = (XiangmusBean) buttonView.getTag();
        boolean isRemove = false;

        if (isChecked) { //isChecked   添加
            if (serviceTypeList == null || serviceTypeList.size() == 0) {
                serviceTypeList.add(bean.getId() + "");
            } else {
                for (int i = 0; i < serviceTypeList.size(); i++) {
                    if (serviceTypeList.get(i).equals(bean.getId() + "")) {
                        isRemove = true;
                    }
                }
                if (isRemove) {
                    //点击完为选中状态时有相同的则不添加
                    isRemove = false;
                } else {
                    serviceTypeList.add(bean.getId() + "");
                }
            }
        } else { //noChecked  清除
            if (serviceTypeList == null || serviceTypeList.size() == 0) {

            } else {
                for (int i = 0; i < serviceTypeList.size(); i++) {
                    if (serviceTypeList.get(i).equals(bean.getId() + "")) {
                        isRemove = true;
                    }
                }
                if (isRemove) {
                    serviceTypeList.remove(bean.getId() + "");
                    isRemove = false;
                } else {
                    //点击完为未选中状态时没有相同的则不清除
                }
            }
        }
    }
}