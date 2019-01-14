package com.ruyiruyi.ruyiruyi.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.ruyiruyi.ruyiruyi.ui.multiType.EmptyBig;
import com.ruyiruyi.ruyiruyi.ui.multiType.EmptyBigViewBinder;
import com.ruyiruyi.ruyiruyi.ui.multiType.ExchangeCoupon;
import com.ruyiruyi.ruyiruyi.ui.multiType.ExchangeCouponViewBinder;
import com.ruyiruyi.ruyiruyi.ui.multiType.ExchangeGoodsOrder;
import com.ruyiruyi.ruyiruyi.utils.RequestUtils;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;
import com.ruyiruyi.rylibrary.cell.GradationScrollView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import me.drakeet.multitype.MultiTypeAdapter;
import rx.functions.Action1;

import static me.drakeet.multitype.MultiTypeAsserts.assertAllRegistered;
import static me.drakeet.multitype.MultiTypeAsserts.assertHasTheSameAdapter;

public class ExchangeCouponActivity extends RyBase1Activity implements GradationScrollView.ScrollViewListener,ExchangeCouponViewBinder.OnCouponItemClick {

    private static final String TAG = ExchangeCoupon.class.getSimpleName();
    private RecyclerView listView;
    private List<Object> items = new ArrayList<>();
    private MultiTypeAdapter adapter;
    private FrameLayout actionBarView;
    private int height;
    private ImageView ivBanner;

    private GradationScrollView scrollView;
    private ImageView backView;

    public List<ExchangeCoupon> couponList;
    private String totalScore;
    private TextView scoreTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange_coupon);

        couponList = new ArrayList<>();
        Intent intent = getIntent();
        totalScore = intent.getStringExtra("TOTAL_SCORE");

        initView();
        initListeners();

        initDataFromService();
    }

    private void initDataFromService() {
        int userId = new DbConfig(getApplicationContext()).getId();

        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL_JIFEN + "score/sku");
        params.addBodyParameter("skuType",1 + "");

        String token = new DbConfig(this).getToken();
        params.addParameter("token",token);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                JSONObject jsonObject1 = null;
                try {
                    jsonObject1 = new JSONObject(result);
                    String status = jsonObject1.getString("status");
                    String msg = jsonObject1.getString("msg");

                    if (status.equals("1")){
                        JSONArray data = jsonObject1.getJSONArray("data");
                        couponList.clear();
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject object = data.getJSONObject(i);
                            int id = object.getInt("id");
                            String name = object.getString("name");
                            String amount = object.getString("amount");
                            String price = object.getString("price");
                            String score = object.getString("score");
                            couponList.add(new ExchangeCoupon(id,name,price,amount,score));
                        }


                        initData();
                    }else if (status.equals("-999")) {
                        showUserTokenDialog("您的账号在其它设备登录,请重新登录");
                    }else {
                        Toast.makeText(ExchangeCouponActivity.this, msg, Toast.LENGTH_SHORT).show();
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

                scrollView.setScrollViewListener(ExchangeCouponActivity.this);
            }
        });
    }


    private void initData() {
        scoreTextView.setText(totalScore);
        items.clear();
        if (couponList.size() == 0){
            items.add(new EmptyBig());
        }else {
            for (int i = 0; i < couponList.size(); i++) {
                items.add(couponList.get(i));
            }
        }

        assertAllRegistered(adapter, items);
        adapter.notifyDataSetChanged();
    }

    private void initView() {
        scoreTextView = (TextView) findViewById(R.id.score_text_view);
        backView = (ImageView) findViewById(R.id.back_image_view);
        scrollView = (GradationScrollView) findViewById(R.id.scrollview);
        ivBanner = (ImageView) findViewById(R.id.iv_banber);
        actionBarView = (FrameLayout) findViewById(R.id.action_bar_view);
        listView = (RecyclerView) findViewById(R.id.jifen_listview);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        listView.setLayoutManager(linearLayoutManager);
        adapter = new MultiTypeAdapter(items);
        register();

        listView.setAdapter(adapter);
        assertHasTheSameAdapter(listView, adapter);

        RxViewAction.clickNoDouble(backView)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        onBackPressed();
                    }
                });

    }

    private void register() {
        ExchangeCouponViewBinder exchangeCouponViewBinder = new ExchangeCouponViewBinder();
        exchangeCouponViewBinder.setListener(this);
        adapter.register(ExchangeCoupon.class, exchangeCouponViewBinder);
        adapter.register(EmptyBig.class,new EmptyBigViewBinder());
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


    /**
     * 兑换券
     * @param id
     * @param score
     */
    @Override
    public void onCoupnItemClikcListener(int id, String score) {
        if (Integer.parseInt(totalScore) > Integer.parseInt(score)) {
            postCouponOrder(id,score);
        }else {
            Toast.makeText(this, "您的积分不足，请前往获取积分", Toast.LENGTH_SHORT).show();
        }
    }

    /**\
     * 提交积分订单
     * @param id
     * @param score
     */
    private void postCouponOrder(int id, String score) {
        int userId = new DbConfig(getApplicationContext()).getId();
        String token = new DbConfig(this).getToken();

        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL_JIFEN + "score/order");
        params.addBodyParameter("userId",userId + "");
        params.addBodyParameter("orderType",2 + "");
        params.addBodyParameter("skuId",id + "");
        params.addBodyParameter("score",score + "");
        params.addBodyParameter("token",token + "");
        Log.e(TAG, "postCouponOrder:-- " + params);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                JSONObject jsonObject1 = null;
                try {
                    jsonObject1 = new JSONObject(result);
                    String status = jsonObject1.getString("status");
                    String msg = jsonObject1.getString("msg");
                    if (status.equals("1")){
                        Toast.makeText(ExchangeCouponActivity.this, "兑换成功", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(),IntegralShopActivity.class));
                    }else {
                        Toast.makeText(ExchangeCouponActivity.this, msg, Toast.LENGTH_SHORT).show();
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
}
