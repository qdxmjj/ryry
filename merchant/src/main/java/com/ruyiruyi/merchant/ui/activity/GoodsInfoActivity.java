package com.ruyiruyi.merchant.ui.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ruyiruyi.merchant.R;
import com.ruyiruyi.merchant.db.DbConfig;
import com.ruyiruyi.merchant.ui.multiType.modle.Service;
import com.ruyiruyi.merchant.ui.multiType.modle.XiaoService;
import com.ruyiruyi.merchant.utils.UtilsURL;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;
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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import rx.functions.Action1;

public class GoodsInfoActivity extends AppCompatActivity {
    private final int CHOOSE_PICTURE = 0;
    private final int TAKE_PICTURE = 1;

    private ImageView mGoodsImg;
    private EditText mGoodsName;
    private EditText mGoodsPrice;
    private TextView mGoodsType;
    private EditText mGoodsKucun;
    private EditText mGoodsStatus;
    private Bitmap imgBitmap;
    private Uri tempUri;
    private String TAG = GoodsInfoActivity.class.getSimpleName();
    public List<Service> serviceList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_goods_info);

        serviceList = new ArrayList<>();
        initView();
        bindView();
        initDataFromService();
    }

    private void initDataFromService() {
        int shopId = new DbConfig().getId();
        final JSONObject object = new JSONObject();
        try {
            object.put("storeId", shopId);
        } catch (JSONException e) {
        }
        RequestParams params = new RequestParams(UtilsURL.REQUEST_URL + "getStoreAddedServices");
        params.addBodyParameter("reqJson", object.toString());

        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess: -----" + result);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(result);
                    JSONObject data = jsonObject.getJSONObject("data");

                    String msg = jsonObject.getString("msg");
                    String status = jsonObject.getString("status");
                    if (status.equals("1")){
                        Iterator<String> keys = data.keys();
                        while (keys.hasNext()){
                            String next = keys.next();
                            Log.e(TAG, "onSuccess: " + next);
                            Service service = new Service();
                            String serviceName1 = next;
                            service.setServiceName(serviceName1);
                            JSONArray jsonArray = data.getJSONArray(next);
                            List<XiaoService> xiaoServiceList = new ArrayList<XiaoService>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object1 = jsonArray.getJSONObject(i);
                                String serviceId = object1.getString("serviceId");
                                String serviceName = object1.getString("serviceName");
                                String serviceTypeId = object1.getString("serviceTypeId");
                                String serviceTypeName = object1.getString("serviceTypeName");
                                XiaoService xiaoService = new XiaoService(serviceId, serviceName, serviceTypeId, serviceTypeName);
                                xiaoServiceList.add(xiaoService);
                            }
                            service.setXiaoServiceList(xiaoServiceList);
                            serviceList.add(service);
                        }


                      /*  Map map = (Map) data;
                        Log.e(TAG, "onSuccess: " + map.toString() );
                        Set<String> string = map.keySet();
                        serviceList.clear();
                        for (String strings : string){

                        }*/
                        Log.e(TAG, "onSuccess: " + serviceList.size());
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

    private void bindView() {
        //头像选择
        RxViewAction.clickNoDouble(mGoodsImg).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                showPicInputDialog();
            }
        });
        RxViewAction.clickNoDouble(mGoodsType).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {

            }
        });
    }

    private void showPicInputDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
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
            int check = ContextCompat.checkSelfPermission(this, permissions[0]);
            // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝
            if (check != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }
        Intent openCameraIntent = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE);
        File file = null;
        file = new File(Environment
                .getExternalStorageDirectory(), "goodsinfoimg.jpg");

        //判断是否是AndroidN以及更高的版本
        if (Build.VERSION.SDK_INT >= 24) {
            openCameraIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            tempUri = FileProvider.getUriForFile(GoodsInfoActivity.this, "com.ruyiruyi.merchant.fileProvider", file);
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
                case CHOOSE_PICTURE:
                    Uri uri = data.getData();
                    setImageToViewFromPhone(uri);
                    break;
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
            imgBitmap = rotaingImageView(degree, photo);
//   2          mGoodsImg.setImageBitmap(imgBitmap);
            //Glide 加载BitMap需要先将bitmap对象转换为字节,在加载;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            imgBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] bytes = baos.toByteArray();
            Glide.with(this).load(bytes)
                    .transform(new GlideCircleTransform(this))
                    .into(mGoodsImg);
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

    private void initView() {
        mGoodsImg = (ImageView) findViewById(R.id.img_goodsimg);
        mGoodsName = (EditText) findViewById(R.id.et_goods_name);
        mGoodsPrice = (EditText) findViewById(R.id.et_goods_price);
        mGoodsType = (TextView) findViewById(R.id.tv_goods_type);
        mGoodsKucun = (EditText) findViewById(R.id.et_goods_kucun);
        mGoodsStatus = (EditText) findViewById(R.id.et_goods_status);
    }
}
