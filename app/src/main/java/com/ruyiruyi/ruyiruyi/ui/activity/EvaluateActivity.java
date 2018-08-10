package com.ruyiruyi.ruyiruyi.ui.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.ruyiruyi.ruyiruyi.R;
import com.ruyiruyi.ruyiruyi.db.DbConfig;
import com.ruyiruyi.ruyiruyi.db.model.User;
import com.ruyiruyi.ruyiruyi.ui.activity.base.RyBaseActivity;
import com.ruyiruyi.ruyiruyi.ui.cell.MessagePicturesLayout;
import com.ruyiruyi.ruyiruyi.ui.multiType.EvaluateImage;
import com.ruyiruyi.ruyiruyi.ui.multiType.EvaluateImageViewBinder;
import com.ruyiruyi.ruyiruyi.utils.GifSizeFilter;
import com.ruyiruyi.ruyiruyi.utils.ImagPagerUtil;
import com.ruyiruyi.ruyiruyi.utils.RequestUtils;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;
import com.ruyiruyi.rylibrary.cell.ActionBar;
import com.ruyiruyi.rylibrary.cell.CustomEditText;
import com.ruyiruyi.rylibrary.cell.NewRatingBar;
import com.ruyiruyi.rylibrary.image.ImageUtils;
import com.ruyiruyi.rylibrary.utils.glide.GlideCircleTransform;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.filter.Filter;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ch.ielse.view.imagewatcher.ImageWatcher;
import id.zelory.compressor.Compressor;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import me.drakeet.multitype.MultiTypeAdapter;
import rx.functions.Action1;

import static me.drakeet.multitype.MultiTypeAsserts.assertAllRegistered;
import static me.drakeet.multitype.MultiTypeAsserts.assertHasTheSameAdapter;

public class EvaluateActivity extends RyBaseActivity implements EvaluateImageViewBinder.OnEvaluateImageClickListener ,MessagePicturesLayout.Callback{
    private static final String TAG = EvaluateActivity.class.getSimpleName();
    private ActionBar actionBar;
    private RecyclerView listView;
    private List<Object> items = new ArrayList<>();
    private MultiTypeAdapter adapter;
    private EvaluateImageViewBinder evaluateImageProvicer;
    private List<EvaluateImage> list = new ArrayList<>();
    private CustomEditText evaluateEditText;
    private static final int REQUEST_CODE_CHOOSE = 23;
    private ImageWatcher vImageWatcher;
    public boolean isTranslucentStatus = false;
    private final List<ImageView> mVisiblePictureList = new ArrayList<>();
    private String headimgurl;
    private RequestManager glideRequest;
    private ImageView userImage;
    private TextView evaluateButton;
    private Bitmap evaluateOne;
    private Bitmap evaluateTwo;
    private Bitmap evaluateThree;
    private Bitmap evaluateFour;
    private Bitmap evaluateFive;
    private NewRatingBar ratingBar;
    private String orderNo;
    private String storeId;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluate,R.id.my_action);
/*        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.setNavigationBarColor(Color.TRANSPARENT);
            isTranslucentStatus = true;
        }*/
        actionBar = (ActionBar) findViewById(R.id.my_action);
        actionBar.setTitle("评价");;
        actionBar.setRightView("提交");
        actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick(){
            @Override
            public void onItemClick(int var1) {
                switch ((var1)){
                    case -1:
                        onBackPressed();
                        break;
                    case -3:
                        postEvaluate();
                        break;
                }
            }
        });
        Intent intent = getIntent();
        orderNo = intent.getStringExtra(PaymentActivity.ORDERNO);
        storeId = intent.getStringExtra(PaymentActivity.STOREID);

        progressDialog = new ProgressDialog(this);
        //配置点击查看大图
        initImageLoader();
        initView();

    }

    private void initView() {
        listView = (RecyclerView) findViewById(R.id.evaluate_phote_recycle);
        evaluateButton = (TextView) findViewById(R.id.save_evaluate);
        ratingBar = (NewRatingBar) findViewById(R.id.rating_bar);
        userImage = (ImageView) findViewById(R.id.user_image);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        listView.setLayoutManager(gridLayoutManager);
        adapter = new MultiTypeAdapter(items);

        evaluateImageProvicer = new EvaluateImageViewBinder(this);
        evaluateImageProvicer.setListener(this);
        adapter.register(EvaluateImage.class, evaluateImageProvicer);
        listView.setAdapter(adapter);
        assertHasTheSameAdapter(listView, adapter);

        updateData();

        evaluateEditText = (CustomEditText) findViewById(R.id.evaluate_edittext);
        evaluateEditText.clearFocus();
        User user = new DbConfig(this).getUser();
        headimgurl = user.getHeadimgurl();
        glideRequest = Glide.with(this);
        glideRequest.load(headimgurl).transform(new GlideCircleTransform(this)).into(userImage);
        RxViewAction.clickNoDouble(evaluateButton)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                       // float starStep = ratingBar.starStep;
                        //Log.e(TAG, "call: -----" + starStep);
                         postEvaluate();
                    }
                });
    }

    /**
     * 提交评价
     */
    private void postEvaluate() {
        showDialogProgress(progressDialog,"评价提交中...");
        Log.e(TAG, "postEvaluate: -*-" + list.size());
        for (int i = 0; i < list.size(); i++) {
            try {
                Uri uri = list.get(i).getUri();
                int degree = ImageUtils.readPictureDegree(uri.toString());
                Bitmap photo = ImageUtils.getBitmapFormUri(getApplicationContext(), uri);
                if (i == 0){
                    Log.e(TAG, "postEvaluate: 0");
                    evaluateOne = rotaingImageView(degree, photo);
                }else if (i == 1){
                    Log.e(TAG, "postEvaluate: 1");
                    evaluateTwo = rotaingImageView(degree, photo);
                }else if (i == 2){
                    Log.e(TAG, "postEvaluate: 2");
                    evaluateThree = rotaingImageView(degree, photo);
                }else if (i == 3){
                    Log.e(TAG, "postEvaluate: 3");
                    evaluateFour = rotaingImageView(degree, photo);
                }else if (i == 4){
                    Log.e(TAG, "postEvaluate: 4");
                    evaluateFive = rotaingImageView(degree, photo);
                }
            } catch (IOException e) {

            }
        }



        final int userId = new DbConfig(this).getId();
        float starStep = ratingBar.starStep;
        String content = evaluateEditText.getText().toString();
        Log.e(TAG, "postEvaluate: " + starStep);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userId",userId);
            jsonObject.put("orderNo",orderNo);
            jsonObject.put("storeId",storeId);
            jsonObject.put("content",content);
            jsonObject.put("starNo",starStep);
        } catch (JSONException e) {
        }
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "userCommitComment");
        params.addBodyParameter("reqJson",jsonObject.toString());
        params.setConnectTimeout(10000);
        try {
            if (evaluateOne!=null){
                String evaluateOne = ImageUtils.savePhoto(this.evaluateOne, this.getObbDir().getAbsolutePath()
                        , "evaluateOne");

                params.addBodyParameter("img1" ,new Compressor(getApplicationContext()).compressToFile(new File(evaluateOne)));
            }
            if (evaluateTwo!=null){
                String evaluateTwo = ImageUtils.savePhoto(this.evaluateTwo, this.getObbDir().getAbsolutePath()
                        , "evaluateTwo");
                params.addBodyParameter("img2" ,new Compressor(getApplicationContext()).compressToFile(new File(evaluateTwo)) );
            }
            if (evaluateThree!=null){
                String evaluateThree = ImageUtils.savePhoto(this.evaluateThree, this.getObbDir().getAbsolutePath()
                        , "evaluateThree");
                params.addBodyParameter("img3" ,new Compressor(getApplicationContext()).compressToFile(new File(evaluateThree)) );
            }
            if (evaluateFour!=null){
                String evaluateFour = ImageUtils.savePhoto(this.evaluateFour, this.getObbDir().getAbsolutePath()
                        , "evaluateFour");
                params.addBodyParameter("img4" ,new Compressor(getApplicationContext()).compressToFile(new File(evaluateFour)) );
            }
            if (evaluateFive!=null){
                String evaluateFive = ImageUtils.savePhoto(this.evaluateFive, this.getObbDir().getAbsolutePath()
                        , "evaluateFive");
                params.addBodyParameter("img5" ,new Compressor(getApplicationContext()).compressToFile(new File(evaluateFive)) );
            }
        } catch (IOException e) {

        }
        String token = new DbConfig(this).getToken();
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
                        Intent intent = new Intent(getApplicationContext(), ShopEvaluateActivity.class);
                        intent.putExtra(ShopEvaluateActivity.EVALUATE_TYPE,1);
                        intent.putExtra("USERID",userId);
                        startActivity(intent);
                        finish();
                    } else if (status.equals("-999")) {
                        showUserTokenDialog("您的账号在其它设备登录,请重新登录");
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


    private void updateData() {
        items.clear();
        if (list==null){
            EvaluateImage evaluateImage = new EvaluateImage();
            evaluateImage.setAdd(true);
            items.add(evaluateImage);
        }else {
            if (list.size()<5){
                for (int i = 0; i < list.size(); i++) {
                    items.add(list.get(i));
                }
                EvaluateImage evaluateImage = new EvaluateImage();
                evaluateImage.setAdd(true);
                items.add(evaluateImage);
            }else {
                for (int i = 0; i < list.size(); i++) {
                    items.add(list.get(i));
                }
            }

            assertAllRegistered(adapter,items);
            adapter.notifyDataSetChanged();

        }
    }


    private void initImageLoader() {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                getApplicationContext()).threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .writeDebugLogs() // Remove for release app
                .build();
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);

    }

    @Override
    public void onImageAddClickListener(boolean add, Uri uri) {
        if (add){
            RxPermissions rxPermissions = new RxPermissions(this);
            rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .subscribe(new Observer<Boolean>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(Boolean aBoolean) {
                            int size = 5 - list.size();
                            Matisse.from(EvaluateActivity.this)
                                    .choose(MimeType.allOf())
                                    .countable(true)
                                    .capture(true)
                                    .captureStrategy(
                                            new CaptureStrategy(true,"com.ruyiruyi.ruyiruyi.fileProvider")
                                    )
                                    .maxSelectable(size)
                                    .addFilter(new GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
                                    .gridExpectedSize(
                                            getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                                    .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                                    .thumbnailScale(0.85f)
                                    .imageEngine(new GlideEngine())
                                    .forResult(REQUEST_CODE_CHOOSE);
                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        }else {
            //点击查看大图
            ArrayList<String> picList = new ArrayList<>();
            String oneUri = uri.toString();
            picList.add(oneUri); //点击哪张 把哪张放第一个
            for (int i = 0; i < list.size(); i++) {     //除去点击那张  其他放进去
                if (!oneUri.equals(list.get(i).getUri().toString())){
                    picList.add(list.get(i).getUri().toString());
                }
            };
            String content = evaluateEditText.getText().toString();     //放评论
            ImagPagerUtil imagPagerUtil = new ImagPagerUtil(EvaluateActivity.this, picList);
            imagPagerUtil.setContentText(content);
            imagPagerUtil.show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {

            List<Uri> uriList = Matisse.obtainResult(data);

            //  Log.e(TAG, "onActivityResult: " + uriList.get(0).toString());
            //去掉重复图片
            int uriSize = uriList.size();
            int listSize = list.size();
            int index = 0;
            for (int i = 0; i < uriList.size(); i++) {
                for (int j = 0; j < list.size(); j++) {
                    if (uriList.get(i).toString().equals(list.get(j).getUri().toString())) {

                        Toast.makeText(this, "不可添加重复图片！", Toast.LENGTH_SHORT).show();
                        uriList.remove(i);
                        if (uriList.size() == 0) {
                            return;
                        }

                    }
                }
            }


            // 判断只能添加五张图片
            if ( (uriList.size() + list.size()) > 5){
                Toast.makeText(this, "最多只能添加5张", Toast.LENGTH_SHORT).show();
                int size =  5 - list.size();
                for (int i = 0; i < size; i++) {
                    EvaluateImage evaluateImage = new EvaluateImage();
                    evaluateImage.setUri(uriList.get(i));
                    evaluateImage.setAdd(false);
                    list.add(evaluateImage);
                    // items.add(evaluateImage);
                }
            }else {
                //不足5张的添加  添加图片按钮
                int size = uriList.size();
                for (int i = 0; i < size; i++) {
                    EvaluateImage evaluateImage = new EvaluateImage();
                    evaluateImage.setUri(uriList.get(i));
                    evaluateImage.setAdd(false);
                    list.add(evaluateImage);
                    // items.add(evaluateImage);
                }
                EvaluateImage evaluateImage = new EvaluateImage();
                evaluateImage.setAdd(true);
                // items.add(evaluateImage);
            }
            // assertAllRegistered(adapter,items);;
            // adapter.notifyDataSetChanged();
            updateData();
        }


    }

    @Override
    public void onImageDelete(Uri uri) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getUri().equals(uri)) {
                list.remove(i);
            }
        }
        updateData();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onThumbPictureClick(ImageView i, List<ImageView> imageGroupList, List<String> urlList) {
        vImageWatcher.show(i, imageGroupList, urlList);
    }

}
