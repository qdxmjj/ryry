package com.ruyiruyi.ruyiruyi.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.ruyiruyi.ruyiruyi.MainActivity;
import com.ruyiruyi.ruyiruyi.R;
import com.ruyiruyi.ruyiruyi.db.DbConfig;
import com.ruyiruyi.ruyiruyi.db.model.User;
import com.ruyiruyi.ruyiruyi.ui.activity.CarManagerActivity;
import com.ruyiruyi.ruyiruyi.ui.activity.CreditLimitActivity;
import com.ruyiruyi.ruyiruyi.ui.activity.CxwyActivity;
import com.ruyiruyi.ruyiruyi.ui.activity.MyLimitActivity;
import com.ruyiruyi.ruyiruyi.ui.activity.OrderActivity;
import com.ruyiruyi.ruyiruyi.ui.activity.ShopEvaluateActivity;
import com.ruyiruyi.ruyiruyi.ui.activity.TestActivity;
import com.ruyiruyi.ruyiruyi.ui.activity.TireWaitChangeActivity;
import com.ruyiruyi.ruyiruyi.ui.activity.UserInfoActivity;
import com.ruyiruyi.ruyiruyi.utils.UIOpenHelper;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;
import com.ruyiruyi.rylibrary.utils.glide.GlideCircleTransform;

import org.xutils.DbManager;
import org.xutils.ex.DbException;

import java.util.List;

import rx.functions.Action1;

public class MyFragment extends Fragment {

    private TextView myButton;
    private TextView nologin;
    private ImageView myImage;
    private TextView userNameView;
    private LinearLayout userInfoLayout;
    private LinearLayout noLoginLayout;
    private Boolean isLogin;
    private LinearLayout shezhiLayout;
    private RequestManager glideRequest;
    private LinearLayout myCarLayout;
    private LinearLayout tireWaitLayout;
    private LinearLayout testLayout;
    private TextView orderAllText;
    private LinearLayout dzfLayout;
    private LinearLayout dfhLayout;
    private LinearLayout dfwLayout;
    private LinearLayout ywcLayout;
    public static String FROM_FRAGMENT = "FROM_FRAGMENT";
    private LinearLayout evaluateLayout;
    private LinearLayout creditLimitLayout;
    private TextView creditLimitText;
    private LinearLayout myLimitLayout;
    private TextView myLimitTesxt;
    private LinearLayout cxwyLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();

        if (isLogin) {
            initDataFromDb();
        }

      /*  myButton = (TextView) getView().findViewById(R.id.my_button);
        nologin = (TextView)  getView().findViewById(R.id.my_nologin);

        RxViewAction.clickNoDouble(myButton)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        UIOpenHelper.openLogin(getContext());
                    }
                });
        RxViewAction.clickNoDouble(nologin)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {

                        DbConfig dbConfig = new DbConfig();
                        User user = dbConfig.getUser();
                        user.setIsLogin("0");
                        DbManager db = dbConfig.getDbManager();

                        try {
                            db.saveOrUpdate(user);
                        } catch (DbException e) {

                        }
                        UIOpenHelper.openLogin(getContext());
                        initView();

                    }
                });


*/


    }

    private void initDataFromDb() {
        DbConfig dbConfig = new DbConfig();
        User user = dbConfig.getUser();
        String headimgurl = user.getHeadimgurl();
        String userName = user.getNick();
        glideRequest = Glide.with(this);
        glideRequest.load(headimgurl).transform(new GlideCircleTransform(getContext())).into(myImage);
        userNameView.setText(userName);

    }

    private void initView() {
        myImage = (ImageView) getView().findViewById(R.id.my_touxiang);
        userNameView = (TextView) getView().findViewById(R.id.my_username);
        userInfoLayout = ((LinearLayout) getView().findViewById(R.id.user_info_layout));
        noLoginLayout = ((LinearLayout) getView().findViewById(R.id.no_login_layout));
        shezhiLayout = (LinearLayout) getView().findViewById(R.id.shezhi_layout);
        myCarLayout = ((LinearLayout) getView().findViewById(R.id.my_car_layout));
        tireWaitLayout = (LinearLayout) getView().findViewById(R.id.tire_wait_layout);
        testLayout = (LinearLayout) getView().findViewById(R.id.test_layout);
        orderAllText = (TextView) getView().findViewById(R.id.order_all_text);
        dzfLayout = (LinearLayout) getView().findViewById(R.id.dzf_layout);
        dfhLayout = (LinearLayout) getView().findViewById(R.id.dfh_layout);
        dfwLayout = (LinearLayout) getView().findViewById(R.id.dfw_layout);
        ywcLayout = (LinearLayout) getView().findViewById(R.id.ywc_layout);
        evaluateLayout = (LinearLayout) getView().findViewById(R.id.evaluate_layout);
        creditLimitLayout = (LinearLayout) getView().findViewById(R.id.credit_limit_layout);
        creditLimitText = ((TextView) getView().findViewById(R.id.credit_limit_my_text));
        myLimitLayout = ((LinearLayout) getView().findViewById(R.id.my_limit_layout));
        myLimitTesxt = ((TextView) getView().findViewById(R.id.my_limit_text));
        cxwyLayout = (LinearLayout) getView().findViewById(R.id.cxwy_layout);

        //畅行无忧
        RxViewAction.clickNoDouble(cxwyLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        startActivity(new Intent(getContext(), CxwyActivity.class));
                    }
                });
        //我的额度
        RxViewAction.clickNoDouble(myLimitLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        startActivity(new Intent(getContext(), MyLimitActivity.class));
                    }
                });
        //信用额度
        RxViewAction.clickNoDouble(creditLimitLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        startActivity(new Intent(getContext(), CreditLimitActivity.class));
                    }
                });
        //测试类
        RxViewAction.clickNoDouble(myImage).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Intent intent = new Intent(getActivity(), UserInfoActivity.class);
                startActivity(intent);
            }
        });

        RxViewAction.clickNoDouble(testLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        startActivity(new Intent(getContext(), TestActivity.class));
                    }
                });
        //所有订单
        RxViewAction.clickNoDouble(orderAllText)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Intent intent = new Intent(getContext(), OrderActivity.class);
                        intent.putExtra(OrderFragment.ORDER_TYPE, "ALL");
                        startActivity(intent);
                    }
                });
        //待支付订单
        RxViewAction.clickNoDouble(dzfLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Intent intent = new Intent(getContext(), OrderActivity.class);
                        intent.putExtra(OrderFragment.ORDER_TYPE, "DZF");
                        startActivity(intent);
                    }
                });
        //待支付
        RxViewAction.clickNoDouble(dfhLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Intent intent = new Intent(getContext(), OrderActivity.class);
                        intent.putExtra(OrderFragment.ORDER_TYPE, "DFH");
                        startActivity(intent);
                    }
                });
        //带服务
        RxViewAction.clickNoDouble(dfwLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Intent intent = new Intent(getContext(), OrderActivity.class);
                        intent.putExtra(OrderFragment.ORDER_TYPE, "DFW");
                        startActivity(intent);
                    }
                });
        //已完成
        RxViewAction.clickNoDouble(ywcLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Intent intent = new Intent(getContext(), OrderActivity.class);
                        intent.putExtra(OrderFragment.ORDER_TYPE, "YWC");
                        startActivity(intent);
                    }
                });


        DbConfig dbConfig = new DbConfig();
        isLogin = dbConfig.getIsLogin();
        userInfoLayout.setVisibility(isLogin ? View.VISIBLE : View.GONE);
        noLoginLayout.setVisibility(isLogin ? View.GONE : View.VISIBLE);
        RxViewAction.clickNoDouble(noLoginLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        UIOpenHelper.openLogin(getContext());
                    }
                });
        //设置
        RxViewAction.clickNoDouble(shezhiLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (isLogin) {
                            DbConfig dbConfig = new DbConfig();
                            User user = dbConfig.getUser();
                            user.setIsLogin("0");
                            DbManager db = dbConfig.getDbManager();

                            try {
                                db.saveOrUpdate(user);
                            } catch (DbException e) {

                            }
                            initView();
                        }

                        UIOpenHelper.openLogin(getContext());

                    }
                });
        //我的宝驹的点击
        RxViewAction.clickNoDouble(myCarLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Intent intent = new Intent(getContext(), CarManagerActivity.class);
                        intent.putExtra(FROM_FRAGMENT, "MYFRAGMENT");
                        startActivityForResult(intent, MainActivity.MYFRAGMENT_RESULT);
                    }
                });
        //待更换轮胎的点击
        RxViewAction.clickNoDouble(tireWaitLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Intent intent = new Intent(getContext(), TireWaitChangeActivity.class);
                        intent.putExtra(FROM_FRAGMENT,"MYFRAGMENT");
                        startActivity(intent);
                    }
                });
        RxViewAction.clickNoDouble(evaluateLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        int userId = new DbConfig().getId();
                        Intent intent = new Intent(getContext(), ShopEvaluateActivity.class);
                        intent.putExtra("USERID",userId);
                        intent.putExtra(ShopEvaluateActivity.EVALUATE_TYPE,1);
                        startActivity(intent);
                    }
                });

      /*  DbConfig dbConfig = new DbConfig();
        User user = dbConfig.getUser();
        if (user == null){
            myButton.setText("未登录");
            myButton.setClickable(true);
            nologin.setVisibility(View.GONE);
        }else {
            myButton.setText("已登陆");
            myButton.setClickable(false);
            nologin.setVisibility(View.VISIBLE);
        }*/
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


}