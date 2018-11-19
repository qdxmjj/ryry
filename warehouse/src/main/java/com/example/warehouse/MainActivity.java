package com.example.warehouse;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.warehouse.R;
import com.example.warehouse.db.DbConfig;
import com.example.warehouse.db.TireType;
import com.example.warehouse.db.User;
import com.example.warehouse.ui.activity.PurchaseActivity;
import com.example.warehouse.ui.activity.base.WareHouseBaseActivity;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;

import org.xutils.DbManager;
import org.xutils.ex.DbException;

import java.util.List;

import rx.functions.Action1;

public class MainActivity extends WareHouseBaseActivity {

    private ImageView caigouView;
    private ImageView caigouJiluView;
    private ImageView diaoboView;
    private ImageView diaoboJiluView;
    private ImageView diaoboShenqingView;
    private ImageView kucunView;
    private ImageView touxiangView;
    private TextView userNameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //导航栏沉浸式
        fullScreen(this);

        initView();

        initData();
    }

    private void initData() {
        DbConfig dbConfig = new DbConfig(this);
        DbManager db = dbConfig.getDbManager();
        List<TireType> tireTypeList = null;
        try {
            tireTypeList = db.selector(TireType.class)
                    .orderBy("time")
                    .findAll();
        } catch (DbException e) {
        }
        if (tireTypeList == null){
            Toast.makeText(this, "kongde ", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this, tireTypeList.size() +" ", Toast.LENGTH_SHORT).show();
        }


        User user = new DbConfig(this).getUser();
        if (user != null){
            String username = user.getNick();
            String headimgurl = user.getHeadimgurl();
            Glide.with(this).load(headimgurl).into(touxiangView);
            userNameView.setText(username);
        }else {
            Toast.makeText(this, "用户信息是空", Toast.LENGTH_SHORT).show();
        }

    }

    private void initView() {
        touxiangView = (ImageView) findViewById(R.id.user_touxiang_image);
        userNameView = (TextView) findViewById(R.id.user_name_text);

        caigouView = (ImageView) findViewById(R.id.caigou_image);
        caigouJiluView = (ImageView) findViewById(R.id.caigou_jilu_image);
        diaoboView = (ImageView) findViewById(R.id.diaobo_image);
        diaoboJiluView = (ImageView) findViewById(R.id.diaobo_jilu_image);
        diaoboShenqingView = (ImageView) findViewById(R.id.diaobo_shenqing_image);
        kucunView = (ImageView) findViewById(R.id.kucun_image);

        //头像点击
        RxViewAction.clickNoDouble(touxiangView)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {

                    }
                });

        //采购
        RxViewAction.clickNoDouble(caigouView)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        startActivity(new Intent(getApplicationContext(),PurchaseActivity.class));
                    }
                });
        //采购记录
        RxViewAction.clickNoDouble(caigouJiluView)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        startActivity(new Intent(getApplicationContext(), PurchaseActivity.class));
                    }
                });
        //调拨
        RxViewAction.clickNoDouble(diaoboView)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {

                    }
                });
        //调拨记录
        RxViewAction.clickNoDouble(diaoboJiluView)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {

                    }
                });
        //调拨申请
        RxViewAction.clickNoDouble(diaoboShenqingView)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {

                    }
                });
        //库存
        RxViewAction.clickNoDouble(kucunView)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {

                    }
                });
    }

    private void fullScreen(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                //5.x开始需要把颜色设置透明，否则导航栏会呈现系统默认的浅灰色
                Window window = activity.getWindow();
                View decorView = window.getDecorView();
                //两个 flag 要结合使用，表示让应用的主体内容占用系统状态栏的空间
                int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
                decorView.setSystemUiVisibility(option);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.TRANSPARENT);
                //导航栏颜色也可以正常设置
//                window.setNavigationBarColor(Color.TRANSPARENT);
            } else {
                Window window = activity.getWindow();
                WindowManager.LayoutParams attributes = window.getAttributes();
                int flagTranslucentStatus = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
                int flagTranslucentNavigation = WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION;
                attributes.flags |= flagTranslucentStatus;
//                attributes.flags |= flagTranslucentNavigation;
                window.setAttributes(attributes);
            }
        }
    }
}
