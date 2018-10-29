package com.ruyiruyi.merchant.ui.fragment;

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
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ruyiruyi.merchant.R;
import com.ruyiruyi.merchant.db.DbConfig;
import com.ruyiruyi.merchant.db.model.User;
import com.ruyiruyi.merchant.ui.activity.BugTestActivity;
import com.ruyiruyi.merchant.ui.activity.MyGoodsActivity;
import com.ruyiruyi.merchant.ui.activity.MyOrderActivity;
import com.ruyiruyi.merchant.ui.activity.MyServiceActivity;
import com.ruyiruyi.merchant.ui.activity.OrdersForShipmentActivity;
import com.ruyiruyi.merchant.ui.activity.PromotionActivity;
import com.ruyiruyi.merchant.ui.activity.SettingActivity;
import com.ruyiruyi.merchant.ui.activity.StoreManageActivity;
import com.ruyiruyi.merchant.utils.UtilsRY;
import com.ruyiruyi.merchant.utils.UtilsURL;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;
import com.ruyiruyi.rylibrary.base.BaseFragment;
import com.ruyiruyi.rylibrary.image.ImageUtils;
import com.ruyiruyi.rylibrary.utils.glide.GlideCircleTransform;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.DbManager;
import org.xutils.common.Callback;
import org.xutils.ex.DbException;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.io.IOException;

import id.zelory.compressor.Compressor;
import rx.functions.Action1;


public class MyFragment extends BaseFragment {

    private static final String TAG = MyFragment.class.getSimpleName();
    private TextView tv_username;
    private LinearLayout tv_mid_wddd;
    private LinearLayout tv_mid_gldp;
    private RelativeLayout rl_wdfw; //我的服务
    private RelativeLayout rl_wdsp; //我的商品
    private RelativeLayout rl_tgjl; //推广奖励
    private RelativeLayout rl_wyzd; //我要置顶
    private RelativeLayout rl_dzyhs; //店主有话说
    private RelativeLayout rl_shezhi; //设置
    private RelativeLayout rl_ddfh; //订单发货
    private ImageView img_user_top;
    private RequestManager glideReq;
    private Boolean isLogin;

    private final int CHOOSE_PICTURE = 0;
    private final int TAKE_PICTURE = 1;
    private Uri tempUri;
    private Bitmap imgBitmap;
    private String img_Path;
    private ProgressDialog progressDialog;
    private ForRefreshMy listener;
    private String path_takepic;
    private boolean isCamera = false;
    /*private Context mContext;

    public MyFragment() {
    }

    public MyFragment(Context mContext) {
        this.mContext = mContext;
    }*/

    public void setListener(ForRefreshMy listener) {
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.wode_fg, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        progressDialog = new ProgressDialog(getContext());

        initView();

        initDataFromDbAndSetView();

        bindView();
    }

    private void bindView() {
        //订单发货
        RxViewAction.clickNoDouble(rl_ddfh).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                JSONObject object = new JSONObject();
                try {
                    object.put("storeId", new DbConfig(getContext()).getId());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                RequestParams params = new RequestParams(UtilsURL.REQUEST_URL + "checkStoreAuth");
                params.addBodyParameter("reqJson", object.toString());
                x.http().post(params, new Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        try {
                            Log.e(TAG, "onSuccess: checkStoreAuth result = " + result);
                            JSONObject jsonObject = new JSONObject(result);
                            int status = jsonObject.getInt("status");
                            String msg = jsonObject.getString("msg");
                            if (status == 1) {
                                boolean data = jsonObject.getBoolean("data");
                                if (data) {
                                    Intent intent = new Intent();
                                    intent.setClass(getContext(), OrdersForShipmentActivity.class);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(getContext(), "您还没有发货权限，如有需求请联系客服", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {
                        Toast.makeText(getContext(), "网络异常,请检查网络", Toast.LENGTH_SHORT).show();
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
        //我要置顶
        RxViewAction.clickNoDouble(rl_wyzd).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Toast.makeText(getActivity(), "敬请期待...", Toast.LENGTH_SHORT).show();
            }
        });
        //店主有话说
        RxViewAction.clickNoDouble(rl_dzyhs).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Toast.makeText(getActivity(), "敬请期待...", Toast.LENGTH_SHORT).show();
            }
        });
        //推广奖励
        RxViewAction.clickNoDouble(rl_tgjl).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Intent intent = new Intent();
                intent.setClass(getContext(), PromotionActivity.class);
                startActivity(intent);
            }
        });
        //我的商品
        RxViewAction.clickNoDouble(rl_wdsp).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Intent intent = new Intent();
                intent.setClass(getContext(), MyGoodsActivity.class);
                startActivity(intent);
            }
        });
        //我的服务
        RxViewAction.clickNoDouble(rl_wdfw).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Intent intent = new Intent();
                intent.setClass(getContext(), MyServiceActivity.class);
                startActivity(intent);
            }
        });
        //管理店铺
        RxViewAction.clickNoDouble(tv_mid_gldp).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Intent intent = new Intent();
                intent.setClass(getContext(), StoreManageActivity.class);
                startActivity(intent);
            }
        });
        //我的订单
        RxViewAction.clickNoDouble(tv_mid_wddd).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Intent intent = new Intent();
                intent.putExtra("page", "0");
                intent.putExtra("typestate", "all");
                intent.setClass(getContext(), MyOrderActivity.class);
                startActivity(intent);
            }
        });
        //设置
        RxViewAction.clickNoDouble(rl_shezhi).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Intent intent = new Intent(getContext(), SettingActivity.class);
                startActivity(intent);
            }
        });
        //头像
        RxViewAction.clickNoDouble(img_user_top).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                showPicInputDialog();
            }
        });


        //测试用 测试栏 <---
        boolean isOpen = false;//测试栏开关
        TextView main_test = getView().findViewById(R.id.main_test);
        if (!isOpen) {
            main_test.setVisibility(View.GONE);
        }
        RxViewAction.clickNoDouble(main_test).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Intent intent = new Intent(getContext(), BugTestActivity.class);
                startActivity(intent);
            }
        });   // 测试栏 --->
    }

    private void initView() {
        tv_username = (TextView) getView().findViewById(R.id.tv_username);
        tv_mid_wddd = (LinearLayout) getView().findViewById(R.id.tv_mid_wddd);
        tv_mid_gldp = (LinearLayout) getView().findViewById(R.id.tv_mid_gldp);
        rl_wdfw = (RelativeLayout) getView().findViewById(R.id.rl_wdfw);
        rl_wdsp = (RelativeLayout) getView().findViewById(R.id.rl_wdsp);
        rl_tgjl = (RelativeLayout) getView().findViewById(R.id.rl_tgjl);
        rl_wyzd = (RelativeLayout) getView().findViewById(R.id.rl_wyzd);
        rl_dzyhs = (RelativeLayout) getView().findViewById(R.id.rl_dzyhs);
        rl_shezhi = (RelativeLayout) getView().findViewById(R.id.rl_shezhi);
        rl_ddfh = (RelativeLayout) getView().findViewById(R.id.rl_ddfh);
        img_user_top = (ImageView) getView().findViewById(R.id.img_user_top);

    }


    private void showPicInputDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("修改头像");
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
            int check = ContextCompat.checkSelfPermission(getContext(), permissions[0]);
            // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝
            if (check != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }
        Intent openCameraIntent = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE);
        File file = null;
        file = new File(getActivity().getObbDir().getAbsolutePath(), "newwodetopimg.jpg");
        path_takepic = file.getPath();

        //判断是否是AndroidN以及更高的版本
        if (Build.VERSION.SDK_INT >= 24) {
            openCameraIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            tempUri = FileProvider.getUriForFile(getContext(), "com.ruyiruyi.merchant.fileProvider", file);
        } else {
            tempUri = Uri.fromFile(new File(getActivity().getObbDir().getAbsolutePath(), "newwodetopimg.jpg"));
        }
        Log.e(TAG, "takePicture: " + tempUri);
        // 指定照片保存路径（SD卡），image.jpg为一个临时文件，每次拍照后这个图片都会被替换(最后删除)
        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);
        startActivityForResult(openCameraIntent, TAKE_PICTURE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //在onCreateView的下面重写onActivityResult这个方法，跳转到系统的相册是仅仅有requeestCode系统相册是没有给我们加一个ResultCode这个参数！！！
        switch (requestCode) {
            case CHOOSE_PICTURE:
                if (data != null) {
                    isCamera = false;
                    Uri uri = data.getData();
                    setImageToViewFromPhone(uri, false);
                }
                break;
            case TAKE_PICTURE:
                isCamera = true;
                setImageToViewFromPhone(tempUri, true);
                break;

        }
    }

    //未剪辑照片
    private void setImageToViewFromPhone(Uri uri, boolean isCamera) {
        int degree = 0;
        if (isCamera) {
            degree = ImageUtils.readPictureDegree(path_takepic);
        } else {
            degree = ImageUtils.getOrientation(getActivity(), uri);
            Log.e(TAG, "setImageToViewFromPhone: degree = " + degree);
        }
        if (uri != null) {
            Bitmap photo = null;
            try {
                photo = ImageUtils.getBitmapFormUri(getContext(), uri);
            } catch (IOException e) {
            }

            if (photo != null) {
                imgBitmap = rotaingImageView(degree, photo);
                requestForChangePic(uri);//请求修改头像
            }
        }
    }


    /*
    * //请求修改头像
    * */
    private void requestForChangePic(final Uri uri) {
            /**/                      //请求修改头像
        showDialogProgress(progressDialog, "头像修改中...");

        img_Path = ImageUtils.savePhoto(imgBitmap, getActivity().getObbDir().getAbsolutePath(), "forpoststoreheadimg");//为提交请求所生成图片 每次提交被替换
        JSONObject object = new JSONObject();
        try {
            object.put("storeId", new DbConfig(getActivity()).getId() + "");
            User user = new DbConfig(getActivity()).getUser();
            object.put("headImgUrl", user.getStoreImgUrl());

        } catch (JSONException e) {
        }
        RequestParams params = new RequestParams(UtilsURL.REQUEST_URL + "updateStoreHeadImgByStoreId");
        params.addBodyParameter("reqJson", object.toString());
        params.addBodyParameter("token", new DbConfig(getActivity()).getToken());
        File file_s = null;
        try {
            file_s = new Compressor(getContext()).compressToFile(new File(img_Path));
        } catch (IOException e) {
        }
        params.addBodyParameter("store_head_img", file_s);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess: xmjj my");
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String msg = jsonObject.getString("msg");
                    String url = jsonObject.getString("data");
                    int status = jsonObject.getInt("status");
                    Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();

                    if (status == 1) {
/*                            //头像修改成功操作 (Glide 加载BitMap需要先将bitmap对象转换为字节,在加载;)
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            imgBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                            byte[] bytes = baos.toByteArray();
                            Glide.with(getContext()).load(bytes)
                                    .transform(new GlideCircleTransform(getActivity()))
                                    .into(img_user_top);*/
                        //修改本地用户信息后跳转
                        User user = new DbConfig(getActivity()).getUser();
                        user.setStoreImgUrl(url);

                        Log.e(TAG, "onSuccess: get0=" + url);
                        Log.e(TAG, "onSuccess: get=" + user.getStoreImgUrl());

                        DbConfig dbConfig = new DbConfig(getActivity());
                        DbManager db = dbConfig.getDbManager();
                        try {
                            db.saveOrUpdate(user);

                        } catch (DbException e) {
                        }

                        String storeImgUrl = new DbConfig(getActivity()).getUser().getStoreImgUrl();
                        Log.e(TAG, "onSuccess: get2=" + storeImgUrl);

                        listener.forRefreshMyListener();//通知MainActivity刷新数据
                        if (isCamera) {
                            UtilsRY.deleteUri(getContext(), uri);//删除照片
                        }
//                        getActivity().finish();
                    }

                } catch (JSONException e) {
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e(TAG, "onError: xmjj my");
//                Toast.makeText(getContext(), "头像修改失败,请检查网络", Toast.LENGTH_SHORT).show();
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

    private void initDataFromDbAndSetView() {
        DbConfig dbConfig = new DbConfig(getActivity());
        User user = dbConfig.getUser();
        String topimgurl = user.getStoreImgUrl();
        String storeName = user.getStoreName();
        Log.e(TAG, "initDataFromDbAndSetView:789789 topimgurl = " + topimgurl + "storeName = " + storeName);
        //glide 转换圆形图片
        glideReq = Glide.with(this);
        glideReq.load(topimgurl)
                .transform(new GlideCircleTransform(getContext()))
                .diskCacheStrategy(DiskCacheStrategy.NONE)//跳过硬盘缓存
                .skipMemoryCache(true)//跳过内存缓存
                .into(img_user_top);
        tv_username.setText(storeName);

    }

    /*
     * 头像修改完毕 刷新MainActivity中所有数据 接口   fg-->activity
     * */
    public interface ForRefreshMy {
        void forRefreshMyListener();//头像修改完毕 通知MainActivity刷新所有数据

    }

}