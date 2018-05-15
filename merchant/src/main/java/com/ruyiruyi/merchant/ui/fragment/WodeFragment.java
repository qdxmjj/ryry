package com.ruyiruyi.merchant.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
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
import com.ruyiruyi.merchant.ui.activity.StoreManageActivity;
import com.ruyiruyi.merchant.utils.UtilsURL;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;
import com.ruyiruyi.rylibrary.utils.glide.GlideCircleTransform;

import org.xutils.DbManager;
import org.xutils.ex.DbException;

import rx.functions.Action1;


public class WodeFragment extends Fragment {

    private static final String TAG = WodeFragment.class.getSimpleName();
    private ImageView img_user;
    private TextView tv_username;
    private TextView tv_mid_wddd;
    private TextView tv_mid_gldp;
    private RelativeLayout rl_wdfw;
    private RelativeLayout rl_wdsp;
    private RelativeLayout rl_tgjl;
    private RelativeLayout rl_dzyhs;
    private RequestManager glideReq;
    private Boolean isLogin;

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
        img_user = (ImageView) getView().findViewById(R.id.img_user_top);
        tv_username = (TextView) getView().findViewById(R.id.tv_username);
        tv_mid_wddd = (TextView) getView().findViewById(R.id.tv_mid_wddd);
        tv_mid_gldp = (TextView) getView().findViewById(R.id.tv_mid_gldp);
        rl_wdfw = (RelativeLayout) getView().findViewById(R.id.rl_wdfw);
        rl_wdsp = (RelativeLayout) getView().findViewById(R.id.rl_wdsp);
        rl_tgjl = (RelativeLayout) getView().findViewById(R.id.rl_tgjl);
        rl_dzyhs = (RelativeLayout) getView().findViewById(R.id.rl_dzyhs);

        RxViewAction.clickNoDouble(tv_username).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                //Test退出登录
                DbConfig dbConfig = new DbConfig();
                User user = dbConfig.getUser();
                Log.e(TAG, "call:   user ==>id" + user.getId() + "is" + user.getIsLogin());
                if (user != null) {
                    user.setIsLogin("0");
                    DbManager dbManager = dbConfig.getDbManager();
                    try {
                        dbManager.saveOrUpdate(user);
                    } catch (DbException e) {
                    }
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                }
            }
        });

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


        //测试用
        TextView main_test = getView().findViewById(R.id.main_test);
        RxViewAction.clickNoDouble(main_test).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Intent intent = new Intent(getActivity(), BugTestActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initDataFromDbAndSetView() {
        DbConfig dbConfig = new DbConfig();
        User user = dbConfig.getUser();
        Log.e(TAG, "initDataFromDbAndSetView: user.getId() ==>" + user.getId());
        String topimgurl = user.getStoreImgUrl();
        Log.e(TAG, "initDataFromDbAndSetView: topimgurl ==>" + topimgurl);
        String storeName = user.getStoreName();
        //glide 转换圆形图片
        glideReq = Glide.with(this);
        glideReq.load(topimgurl).transform(new GlideCircleTransform(getContext())).into(img_user);
        tv_username.setText(storeName);

    }
}