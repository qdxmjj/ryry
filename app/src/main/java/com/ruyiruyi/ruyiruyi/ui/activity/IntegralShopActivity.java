package com.ruyiruyi.ruyiruyi.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ruyiruyi.ruyiruyi.R;
import com.ruyiruyi.ruyiruyi.db.DbConfig;
import com.ruyiruyi.ruyiruyi.ui.activity.base.RyBase1Activity;
import com.ruyiruyi.ruyiruyi.ui.activity.base.RyBaseActivity;
import com.ruyiruyi.ruyiruyi.utils.RequestUtils;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;
import com.ruyiruyi.rylibrary.cell.GradationScrollView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import rx.functions.Action1;

public class IntegralShopActivity extends RyBase1Activity implements GradationScrollView.ScrollViewListener{

    private static final String TAG = IntegralShopActivity.class.getSimpleName();
    private ImageView backView;
    private TextView dengluButton;
    private FrameLayout actionBarView;
    private GradationScrollView scrollView;
    private ImageView ivBanner;
    private int height;
    private String yaoqingRule;
    private String xiaofeiRule;
    private String loginRule;
    private String totalScore;
    private TextView scoreTextView;
    private TextView dengluRuleText;
    private TextView xiaofeiRuleText;
    private TextView yaoqingRuleText;
    private ImageView exchangeCouponView;
    private TextView duihuanJiluView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_integral_shop);

        //导航栏沉浸式
      //  fullScreen(this);

        initView();
        initListeners();

        initDataFromService();
    }

    private void initDataFromService() {
        int userId = new DbConfig(getApplicationContext()).getId();

        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL_JIFEN + "score/info");
        params.addBodyParameter("userId",userId + "");

        String token = new DbConfig(this).getToken();
        params.addParameter("token",token);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess: ---" + result);
                JSONObject jsonObject1 = null;
                try {
                    jsonObject1 = new JSONObject(result);
                    String status = jsonObject1.getString("status");
                    String msg = jsonObject1.getString("msg");

                    if (status.equals("1")){
                        JSONObject data = jsonObject1.getJSONObject("data");
                        totalScore = data.getString("totalScore");
                        JSONArray ruleList = data.getJSONArray("ruleList");

                        loginRule = ruleList.get(0).toString();
                        xiaofeiRule = ruleList.get(1).toString();
                        yaoqingRule = ruleList.get(2).toString();

                        initData();
                    }else {
                        Toast.makeText(IntegralShopActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
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

    private void initData() {
        scoreTextView.setText(totalScore);
        dengluRuleText.setText(loginRule);
        xiaofeiRuleText.setText(xiaofeiRule);
        yaoqingRuleText.setText(yaoqingRule);
    }

    /**
     * 获取顶部图片高度后，设置滚动监听
     */
    private void initListeners() {

        ViewTreeObserver vto = ivBanner.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                actionBarView.getViewTreeObserver().removeGlobalOnLayoutListener(
                        this);
                height = ivBanner.getHeight();

                scrollView.setScrollViewListener(IntegralShopActivity.this);
            }
        });
    }


    private void initView() {
        duihuanJiluView = (TextView) findViewById(R.id.duihuan_jilu_view);
        backView = (ImageView) findViewById(R.id.back_image_view);
        dengluButton = (TextView) findViewById(R.id.denglu_button);
        actionBarView = (FrameLayout) findViewById(R.id.action_bar_view);
        scrollView = (GradationScrollView) findViewById(R.id.scrollview);
        ivBanner = (ImageView) findViewById(R.id.iv_banber);
        scoreTextView = (TextView) findViewById(R.id.score_text_view);
        dengluRuleText = (TextView) findViewById(R.id.denglu_rule_text);
        xiaofeiRuleText = (TextView) findViewById(R.id.xiaofei_rule_text);
        yaoqingRuleText = (TextView) findViewById(R.id.yaoqing_rule_text);

        exchangeCouponView = (ImageView) findViewById(R.id.coupon_exchang_view);

        //兑换记录
        RxViewAction.clickNoDouble(duihuanJiluView)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        startActivity(new Intent(getApplicationContext(),ChangeOrderActivity.class));
                    }
                });

        /**
         * 优惠券兑换
         */
        RxViewAction.clickNoDouble(exchangeCouponView)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Intent intent = new Intent(getApplicationContext(), ExchangeCouponActivity.class);
                        intent.putExtra("TOTAL_SCORE",totalScore);
                        startActivity(intent);
                    }
                });


        RxViewAction.clickNoDouble(backView)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        onBackPressed();
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

    /**
     * 滑动监听
     * @param scrollView
     * @param x
     * @param y
     * @param oldx
     * @param oldy
     */
    @Override
    public void onScrollChanged(GradationScrollView scrollView, int x, int y,
                                int oldx, int oldy) {
        // TODO Auto-generated method stub
        if (y <= 0) {   //设置标题的背景颜色
            actionBarView.setBackgroundColor(Color.argb((int) 0, 144,151,166));
        } else if (y > 0 && y <= height) { //滑动距离小于banner图的高度时，设置背景和字体颜色颜色透明度渐变
            float scale = (float) y / height;
            float alpha = (255 * scale);
        //    actionBarView.setTextColor(Color.argb((int) alpha, 255,255,255));
            actionBarView.setBackgroundColor(Color.argb((int) alpha, 255,102,35));
        } else {    //滑动到banner下面设置普通颜色
            actionBarView.setBackgroundColor(Color.argb((int) 255, 255,102,35));
        }
    }


}
