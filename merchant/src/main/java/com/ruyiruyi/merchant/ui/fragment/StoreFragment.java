package com.ruyiruyi.merchant.ui.fragment;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
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
import android.support.v4.widget.SwipeRefreshLayout;
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
import com.facebook.common.file.FileUtils;
import com.ruyiruyi.merchant.MainActivity;
import com.ruyiruyi.merchant.R;
import com.ruyiruyi.merchant.bean.ItemBottomBean;
import com.ruyiruyi.merchant.bean.ItemNullBean;
import com.ruyiruyi.merchant.db.DbConfig;
import com.ruyiruyi.merchant.db.model.User;
import com.ruyiruyi.merchant.ui.multiType.DianpuItemViewProvider;
import com.ruyiruyi.merchant.ui.multiType.ItemBottomProvider;
import com.ruyiruyi.merchant.ui.multiType.ItemNullProvider;
import com.ruyiruyi.merchant.ui.multiType.listener.OnLoadMoreListener;
import com.ruyiruyi.merchant.ui.multiType.listener.OnMyItemTouchListener;
import com.ruyiruyi.merchant.ui.multiType.modle.Dianpu;
import com.ruyiruyi.merchant.ui.multiType.modle.Dingdan;
import com.ruyiruyi.merchant.utils.UtilsRY;
import com.ruyiruyi.merchant.utils.UtilsURL;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;
import com.ruyiruyi.rylibrary.base.BaseFragment;
import com.ruyiruyi.rylibrary.image.ImageUtils;
import com.ruyiruyi.rylibrary.utils.glide.GlideCircleTransform;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.DbManager;
import org.xutils.common.Callback;
import org.xutils.ex.DbException;
import org.xutils.http.RequestParams;
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


public class StoreFragment extends BaseFragment {
    private RecyclerView mRecyclerView;
    private ImageView storeImage;
    private TextView storeName;
    private MultiTypeAdapter multiTypeAdapter;
    private List<Object> items = new ArrayList<>();//Object!!!
    private List<Dianpu> list;
    private String TAG = StoreFragment.class.getSimpleName();
    private final int CHOOSE_PICTURE = 0;
    private final int TAKE_PICTURE = 1;
    private Uri tempUri;
    private Bitmap imgBitmap;
    private String img_Path;
    private int total_all_page;
    private int mRows = 10;  // 设置默认一页加载10条数据
    private int current_page;
    private boolean isLoadMore = false;
    private boolean isLoadOver = false;
    private boolean isLoadMoreSingle = false;//上拉单次标志位
    private boolean isFirstLoad = true;
    private SwipeRefreshLayout mSwipeLayout;
    private String monthIncome;
    private String totalIncome;
    private TextView tv_bysy_num;
    private TextView tv_zsy_num;
    private ProgressDialog progressDialog;
    private ProgressDialog startDialog;
    private ForRefreshStore listener;
    private String path_takepic;
    private boolean isCamera = false;
    private Context mContext;

    public StoreFragment(Context mContext) {
        this.mContext = mContext;
    }

    public void setListener(ForRefreshStore listener) {
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dianpu_fg, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        list = new ArrayList<>();
        progressDialog = new ProgressDialog(getContext());

        initView();
        initData();
        initSwipeLayout();
        bindView();
    }

    private void initData() {
        requestFromServer();
    }

    private void requestFromServer() {
        //数据加载完成前显示加载动画
        startDialog = new ProgressDialog(getContext());
        if (isFirstLoad) {
            showDialogProgress(startDialog, "信息加载中...");
        }

        isLoadOver = false;

        if (!isLoadMore) {//只有加载更多(不清空原数据)
            list.clear();
            current_page = 1;
        }

        //下载数据
        JSONObject object = new JSONObject();
        try {
            object.put("page", current_page);
            object.put("rows", mRows);
            object.put("storeId", new DbConfig().getId() + "");
            object.put("type", "1");//type: 1:商品订单 2:如意如意平台订单

        } catch (JSONException e) {
        }
        RequestParams params = new RequestParams(UtilsURL.REQUEST_URL + "getStoreGeneralOrderByType");
        params.addBodyParameter("reqJson", object.toString());
        params.addBodyParameter("token", new DbConfig().getToken());
        params.setConnectTimeout(6000);
        Log.e(TAG, "requestFromServer: oov" + params.toString());
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess: result oov= " + result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONObject data = jsonObject.getJSONObject("data");
                    monthIncome = data.getString("monthIncome");
                    totalIncome = data.getString("totalIncome");
                    int total = data.getInt("total");
                    total_all_page = total / mRows;
                    if (total % mRows > 0) {
                        total_all_page++;
                    }
                    JSONArray rows = data.getJSONArray("rows");
                    for (int i = 0; i < rows.length(); i++) {
                        Dianpu dianpu = new Dianpu();
                        JSONObject beanObj = (JSONObject) rows.get(i);
                        dianpu.setOrderImage(beanObj.getString("orderImage"));
                        dianpu.setOrderPrice(beanObj.getString("orderPrice"));
                        dianpu.setOrderTime(beanObj.getLong("orderTime"));
                        dianpu.setOrderState(beanObj.getString("orderState"));
                        String orderServcieTypeName = "";
                        String orderServcieTypeName_First = "";
                        JSONArray orderServcieTypeNameList = beanObj.getJSONArray("orderServcieTypeNameList");
                        for (int j = 0; j < orderServcieTypeNameList.length(); j++) {
                            String str = (String) orderServcieTypeNameList.get(j);
                            if (j == 0) {
                                orderServcieTypeName = str;
                                orderServcieTypeName_First = str;
                            } else {
                                orderServcieTypeName = orderServcieTypeName + "/" + str;
                            }
                        }
                        dianpu.setOrderServcieTypeName(orderServcieTypeName);
                        dianpu.setOrderServcieTypeName_First(orderServcieTypeName_First);
                        dianpu.setOrderNo(beanObj.getString("orderNo"));
                        dianpu.setOrderType(beanObj.getString("orderType"));

                        list.add(dianpu);
                    }

                    //更新数据
                    updataData();

                    //设置固定数据
                    setData();

                    isLoadMoreSingle = false;//重置加载更多单次标志位


                } catch (JSONException e) {
                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(getContext(), "信息加载失败,请检查网络", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                //加载完成 隐藏加载动画
                if (isFirstLoad) {
                    hideDialogProgress(startDialog);
                    isFirstLoad = false;
                }
            }
        });
    }

    private void setData() {
        tv_zsy_num.setText(totalIncome);
        tv_bysy_num.setText(monthIncome);
    }

    //初始化下拉上拉
    private void initSwipeLayout() {
        mSwipeLayout.setColorSchemeResources(//下拉刷新圆圈颜色
                R.color.theme_primary,
                R.color.c5,
                R.color.c6,
                R.color.c7
        );
        mSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //加载最新数据并更新adapter数据

                isLoadMore = false;
                myDownRefreshByServer();

                mSwipeLayout.setRefreshing(false);
            }
        });
        //加载更多
        mRecyclerView.setOnScrollListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                //根据上拉单次标志位判断是否执行加载更多（防止多次加载）
                if (isLoadMoreSingle) {
                    return;
                }
                isLoadMoreSingle = true;//上拉单次标志位

                if (total_all_page > current_page) {
                    current_page++;
                    items.add(new ItemBottomBean("加载更多..."));

                    isLoadMore = true;
                    requestFromServer();
                } else {
                    if (!isLoadOver && (total_all_page > 1)) {//用于判断是否加  加载完成底部
                        items.add(new ItemBottomBean("全部加载完毕!"));
                        isLoadOver = true;
                    }
                }
                assertAllRegistered(multiTypeAdapter, items);
                multiTypeAdapter.notifyDataSetChanged();
            }
        });
    }

    //下拉刷新
    private void myDownRefreshByServer() {//下拉刷新
        requestFromServer();
    }


    private void updataData() {
        items.clear();
        if (list == null || list.size() == 0) {
            items.add(new ItemNullBean("暂无数据"));
        } else {
            for (int i = 0; i < list.size(); i++) {
                items.add(list.get(i));
            }
        }
        assertAllRegistered(multiTypeAdapter, items);
        multiTypeAdapter.notifyDataSetChanged();
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

        //初始化其他
        tv_zsy_num = getView().findViewById(R.id.tv_zsy_num);
        tv_bysy_num = getView().findViewById(R.id.tv_bysy_num);
        mSwipeLayout = getView().findViewById(R.id.my_swp_);
        mRecyclerView = (RecyclerView) getView().findViewById(R.id.dianpu_rlv);
        LinearLayoutManager manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(manager);
        multiTypeAdapter = new MultiTypeAdapter(items);
        multiTypeAdapter.register(Dianpu.class, new DianpuItemViewProvider(getContext()));
        multiTypeAdapter.register(ItemNullBean.class, new ItemNullProvider());
        multiTypeAdapter.register(ItemBottomBean.class, new ItemBottomProvider());
        mRecyclerView.setAdapter(multiTypeAdapter);
        assertHasTheSameAdapter(mRecyclerView, multiTypeAdapter);

    }


    private void showPicInputDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
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
            int check = ContextCompat.checkSelfPermission(mContext, permissions[0]);
            // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝
            if (check != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }
        Intent openCameraIntent = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE);
        File file = null;
        file = new File(Environment
                .getExternalStorageDirectory(), "newstoreheadimg.jpg");
        path_takepic = file.getPath();

        //判断是否是AndroidN以及更高的版本
        if (Build.VERSION.SDK_INT >= 24) {
            openCameraIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            tempUri = FileProvider.getUriForFile(mContext, "com.ruyiruyi.merchant.fileProvider", file);
        } else {
            tempUri = Uri.fromFile(new File(Environment
                    .getExternalStorageDirectory(), "newstoreheadimg.jpg"));
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
                    setImageToViewFromPhone(uri, uri.toString());
                }
                break;
            case TAKE_PICTURE:
                if (tempUri != null) {
                    isCamera = true;
                    setImageToViewFromPhone(tempUri, path_takepic);
                }
                break;

        }
    }

    //未剪辑照片
    private void setImageToViewFromPhone(Uri uri, String paths) {
        int degree = ImageUtils.readPictureDegree(paths);
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
         /**/           //请求修改头像
        showDialogProgress(progressDialog, "头像修改中...");

        img_Path = ImageUtils.savePhoto(imgBitmap, Environment
                .getExternalStorageDirectory().getAbsolutePath(), "forpoststoreheadimg");//为提交请求所生成图片 每次提交被替换
        JSONObject object = new JSONObject();
        try {
            object.put("storeId", new DbConfig().getId() + "");
            User user = new DbConfig().getUser();
            object.put("headImgUrl", user.getStoreImgUrl());

        } catch (JSONException e) {
        }
        RequestParams params = new RequestParams(UtilsURL.REQUEST_URL + "updateStoreHeadImgByStoreId");
        params.addBodyParameter("reqJson", object.toString());
        params.addBodyParameter("token", new DbConfig().getToken());
        params.addBodyParameter("store_head_img", new File(img_Path));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess: xmjj store"  );
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
                                    .into(storeImage);*/
                        //修改本地用户信息后跳转
                        User user = new DbConfig().getUser();
                        user.setStoreImgUrl(url);
                        DbConfig dbConfig = new DbConfig();
                        DbManager db = dbConfig.getDbManager();
                        try {
                            db.saveOrUpdate(user);

                        } catch (DbException e) {
                        }
//                        Intent intent = new Intent(getContext(), MainActivity.class);
//                        Bundle bundle = new Bundle();
//                        bundle.putString("page", "store");
//                        intent.putExtras(bundle);
//                        getActivity().finish();
//                        startActivity(intent);
                        listener.forRefreshStoreListener();//通知MainActivity刷新数据
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
                Log.e(TAG, "onError: xmjj store"  );
//                Toast.makeText(mContext, "头像修改失败,请检查网络", Toast.LENGTH_SHORT).show();
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

    /*
    * 头像修改完毕 刷新MainActivity中所有数据 接口   fg-->activity
    * */
    public interface ForRefreshStore {
        void forRefreshStoreListener();//头像修改完毕 通知MainActivity刷新所有数据

    }


}