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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ruyiruyi.merchant.R;
import com.ruyiruyi.merchant.db.DbConfig;
import com.ruyiruyi.merchant.db.model.User;
import com.ruyiruyi.merchant.ui.multiType.DianpuItemViewProvider;
import com.ruyiruyi.merchant.ui.multiType.listener.OnMyItemTouchListener;
import com.ruyiruyi.merchant.ui.multiType.modle.Dianpu;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;
import com.ruyiruyi.rylibrary.image.ImageUtils;
import com.ruyiruyi.rylibrary.utils.glide.GlideCircleTransform;

import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import me.drakeet.multitype.MultiTypeAdapter;
import rx.functions.Action1;

import static me.drakeet.multitype.MultiTypeAsserts.assertAllRegistered;
import static me.drakeet.multitype.MultiTypeAsserts.assertHasTheSameAdapter;


public class DianpuFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private ImageView storeImage;
    private TextView storeName;
    private MultiTypeAdapter multiTypeAdapter;
    private List<Object> items = new ArrayList<>();//Object!!!
    private List<Dianpu> list;
    private String TAG = DianpuFragment.class.getSimpleName();
    private final int CHOOSE_PICTURE = 0;
    private final int TAKE_PICTURE = 1;
    private Uri tempUri;
    private Bitmap imgBitmap;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dianpu_fg, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mRecyclerView = (RecyclerView) getView().findViewById(R.id.dianpu_rlv);
        LinearLayoutManager manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(manager);
        multiTypeAdapter = new MultiTypeAdapter(items);
        multiTypeAdapter.register(Dianpu.class, new DianpuItemViewProvider());
        mRecyclerView.setAdapter(multiTypeAdapter);
        assertHasTheSameAdapter(mRecyclerView, multiTypeAdapter);
        initDataAndRfTest();

        mRecyclerView.addOnItemTouchListener(new OnMyItemTouchListener(mRecyclerView) {
            @Override
            public void onItemClick(RecyclerView.ViewHolder vh) {
                //操作
                Log.e(TAG, "Recycler-->onItemClick: " + "AdapterPosition:" + vh.getAdapterPosition());
                Toast.makeText(getActivity(), "AdapterPosition:" + vh.getAdapterPosition(), Toast.LENGTH_SHORT).show();
            }
        });

        initView();
        bindView();
    }

    private void bindView() {
        RxViewAction.clickNoDouble(storeImage).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                showPicInputDialog();
            }
        });
    }

    private void initView() {
        storeImage = getView().findViewById(R.id.img_user);
        storeName = getView().findViewById(R.id.tv_username);
        //头像 店铺名称
        DbConfig config = new DbConfig();
        User user = config.getUser();
        storeName.setText(user.getStoreName());
        Glide.with(this).load(user.getStoreImgUrl()).
                transform(new GlideCircleTransform(getContext()))
                .diskCacheStrategy(DiskCacheStrategy.NONE)//跳过硬盘缓存
                .skipMemoryCache(true)//跳过内存缓存
                .into(storeImage);
    }

    private void initDataAndRfTest() {
        list = new ArrayList<Dianpu>();
        for (int i = 0; i < 3; i++) {
            list.add(new Dianpu("汽车保养", "07-08", "+800.00", "已完成"));
            list.add(new Dianpu("美容清洗", "07-08", "+8000.00", "已完成"));
            list.add(new Dianpu("安装", "07-08", "+6000.00", "已完成"));
            list.add(new Dianpu("安装", "07-08", "+6600.00", "已完成"));
        }
        items.clear();
        items.addAll(list);
        assertAllRegistered(multiTypeAdapter, items);
        multiTypeAdapter.notifyDataSetChanged();

    }

    private void showPicInputDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
                .getExternalStorageDirectory(), "dianputopimg.jpg");

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
                if (data != null) {
                    Uri uri = data.getData();
                    setImageToViewFromPhone(uri);
                }
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
                    .into(storeImage);
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


}