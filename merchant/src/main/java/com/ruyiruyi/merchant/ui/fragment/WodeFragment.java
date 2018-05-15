package com.ruyiruyi.merchant.ui.fragment;

import android.Manifest;
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
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ruyiruyi.merchant.R;
import com.ruyiruyi.merchant.db.DbConfig;
import com.ruyiruyi.merchant.db.model.User;
import com.ruyiruyi.merchant.ui.activity.BugTestActivity;
import com.ruyiruyi.merchant.ui.activity.LoginActivity;
import com.ruyiruyi.merchant.ui.activity.MyGoodsActivity;
import com.ruyiruyi.merchant.ui.activity.MyOrderActivity;
import com.ruyiruyi.merchant.ui.activity.MyServiceActivity;
import com.ruyiruyi.merchant.ui.activity.OrderXiangqingActivity;
import com.ruyiruyi.merchant.ui.activity.ServiceRecordActivity;
import com.ruyiruyi.merchant.ui.activity.SheZhiActivity;
import com.ruyiruyi.merchant.ui.activity.StoreManageActivity;
import com.ruyiruyi.merchant.utils.UtilsURL;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;
import com.ruyiruyi.rylibrary.image.ImageUtils;
import com.ruyiruyi.rylibrary.utils.glide.GlideCircleTransform;

import org.xutils.DbManager;
import org.xutils.ex.DbException;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import rx.functions.Action1;


public class WodeFragment extends Fragment {

    private static final String TAG = WodeFragment.class.getSimpleName();
    private TextView tv_username;
    private TextView tv_mid_wddd;
    private TextView tv_mid_gldp;
    private RelativeLayout rl_wdfw;
    private RelativeLayout rl_wdsp;
    private RelativeLayout rl_tgjl;
    private RelativeLayout rl_dzyhs;
    private RelativeLayout rl_shezhi;
    private ImageView img_user_top;
    private RequestManager glideReq;
    private Boolean isLogin;

    private final int CHOOSE_PICTURE = 0;
    private final int TAKE_PICTURE = 1;
    private Uri tempUri;
    private Bitmap imgBitmap;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.wode_fg, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();

        initDataFromDbAndSetView();
    }

    private void initView() {
        tv_username = (TextView) getView().findViewById(R.id.tv_username);
        tv_mid_wddd = (TextView) getView().findViewById(R.id.tv_mid_wddd);
        tv_mid_gldp = (TextView) getView().findViewById(R.id.tv_mid_gldp);
        rl_wdfw = (RelativeLayout) getView().findViewById(R.id.rl_wdfw);
        rl_wdsp = (RelativeLayout) getView().findViewById(R.id.rl_wdsp);
        rl_tgjl = (RelativeLayout) getView().findViewById(R.id.rl_tgjl);
        rl_dzyhs = (RelativeLayout) getView().findViewById(R.id.rl_dzyhs);
        rl_shezhi = (RelativeLayout) getView().findViewById(R.id.rl_shezhi);
        img_user_top = (ImageView) getView().findViewById(R.id.img_user_top);


        RxViewAction.clickNoDouble(rl_wdsp).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), MyGoodsActivity.class);
                startActivity(intent);
            }
        });
        RxViewAction.clickNoDouble(rl_wdfw).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), MyServiceActivity.class);
                startActivity(intent);
            }
        });
        RxViewAction.clickNoDouble(tv_mid_gldp).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), StoreManageActivity.class);
                startActivity(intent);
            }
        });
        RxViewAction.clickNoDouble(tv_mid_wddd).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), MyOrderActivity.class);
                startActivity(intent);
            }
        });
        RxViewAction.clickNoDouble(rl_shezhi).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Intent intent = new Intent(getActivity(), SheZhiActivity.class);
                startActivity(intent);
            }
        });
        RxViewAction.clickNoDouble(img_user_top).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                showPicInputDialog();
            }
        });


        //测试用 测试栏
        TextView main_test = getView().findViewById(R.id.main_test);
        RxViewAction.clickNoDouble(main_test).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Intent intent = new Intent(getActivity(), BugTestActivity.class);
                startActivity(intent);
            }
        });
    }


    private void showPicInputDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("上传照片");
        String[] items = {"选择本地照片", "拍照"};
        builder.setNegativeButton("取消", null);
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
        builder.create().show();
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
        file = new File(Environment
                .getExternalStorageDirectory(), "wodetopimg.jpg");

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
        //在onCreateView的下面重写onActivityResult这个方法，跳转到系统的相册是仅仅有requeestCode系统相册是没有给我们加一个ResultCode这个参数！！！
        switch (requestCode) {
            case CHOOSE_PICTURE:
                Uri uri = data.getData();
                setImageToViewFromPhone(uri);
                break;
            case TAKE_PICTURE:
                setImageToViewFromPhone(tempUri);
                break;

        }
    }

    //未剪辑照片
    private void setImageToViewFromPhone(Uri uri) {
        int degree = ImageUtils.readPictureDegree(uri.toString());
        if (uri != null) {
            Bitmap photo = null;
            try {
                photo = ImageUtils.getBitmapFormUri(getContext(), uri);
            } catch (IOException e) {
            }
            imgBitmap = rotaingImageView(degree, photo);
//   2          mGoodsImg.setImageBitmap(imgBitmap);
            //Glide 加载BitMap需要先将bitmap对象转换为字节,在加载;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            imgBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] bytes = baos.toByteArray();
            Glide.with(this).load(bytes)
                    .transform(new GlideCircleTransform(getActivity()))
                    .into(img_user_top);
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

    private void initDataFromDbAndSetView() {
        DbConfig dbConfig = new DbConfig();
        User user = dbConfig.getUser();
        String topimgurl = user.getStoreImgUrl();
        String storeName = user.getStoreName();
        //glide 转换圆形图片
        glideReq = Glide.with(this);
        glideReq.load(topimgurl)
                .transform(new GlideCircleTransform(getContext()))
                .diskCacheStrategy(DiskCacheStrategy.NONE)//跳过硬盘缓存
                .skipMemoryCache(true)//跳过内存缓存
                .into(img_user_top);
        tv_username.setText(storeName);

    }
}