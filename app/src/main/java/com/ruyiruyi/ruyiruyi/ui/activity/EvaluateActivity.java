package com.ruyiruyi.ruyiruyi.ui.activity;

import android.Manifest;
import android.app.FragmentBreadCrumbs;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.ruyiruyi.ruyiruyi.R;
import com.ruyiruyi.ruyiruyi.ui.cell.MessagePicturesLayout;
import com.ruyiruyi.ruyiruyi.ui.multiType.EvaluateImage;
import com.ruyiruyi.ruyiruyi.ui.multiType.EvaluateImageViewBinder;
import com.ruyiruyi.ruyiruyi.utils.GifSizeFilter;
import com.ruyiruyi.ruyiruyi.utils.ImagPagerUtil;
import com.ruyiruyi.ruyiruyi.utils.Utils;
import com.ruyiruyi.rylibrary.base.BaseActivity;
import com.ruyiruyi.rylibrary.cell.ActionBar;
import com.ruyiruyi.rylibrary.cell.CustomEditText;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.filter.Filter;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import java.util.ArrayList;
import java.util.List;

import ch.ielse.view.imagewatcher.ImageWatcher;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import me.drakeet.multitype.MultiTypeAdapter;
import rx.android.schedulers.AndroidSchedulers;

import static me.drakeet.multitype.MultiTypeAsserts.assertAllRegistered;
import static me.drakeet.multitype.MultiTypeAsserts.assertHasTheSameAdapter;

public class EvaluateActivity extends BaseActivity implements EvaluateImageViewBinder.OnEvaluateImageClickListener ,MessagePicturesLayout.Callback{
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluate,R.id.my_action);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.setNavigationBarColor(Color.TRANSPARENT);
            isTranslucentStatus = true;
        }
        actionBar = (ActionBar) findViewById(R.id.my_action);
        actionBar.setTitle("评价");;
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

        //配置点击查看大图
        initImageLoader();
        initView();

    }

    private void initView() {
        listView = (RecyclerView) findViewById(R.id.evaluate_phote_recycle);
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
