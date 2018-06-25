package com.ruyiruyi.ruyiruyi.ui.activity;

import android.animation.ObjectAnimator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.ruyiruyi.ruyiruyi.MainActivity;
import com.ruyiruyi.ruyiruyi.R;
import com.ruyiruyi.ruyiruyi.ui.activity.base.RyBaseActivity;
import com.ruyiruyi.ruyiruyi.ui.service.LuncherDownlodeService;

import java.util.ArrayList;
import java.util.List;

public class GuideActivity extends RyBaseActivity {
    private ViewPager viewPager;//需要ViewPaeger
    private PagerAdapter mAdapter;//需要PagerAdapter适配器
    private List<View> mViews = new ArrayList<>();//准备数据源
    private TextView bt_home;//在ViewPager的最后一个页面设置一个按钮，用于点击跳转到MainActivity
    private TextView tv_num;
    private TextView tv_welcome;
    private TextView tv_txt;
    private boolean isFirstToThree = true; //只有第一次进入第三页才播放倒计时动画标记
    private TimeCount mTimeCount;
    private int progress = 0;
    private int fuckProgress = 0;
    private boolean isfuckFirst = true;
    private MyReceiver receiver = null;
    private String TAG = GuideActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        //开启服务下载
        StartDownlodeService();

        initView();//初始化view
    }


    public void StartDownlodeService() {
        //启动服务
        Intent intent = new Intent(this, LuncherDownlodeService.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("Key", LuncherDownlodeService.Control.PLAY);
        intent.putExtras(bundle);
        startService(intent);
        //注册广播接收器
        receiver = new MyReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.ruyiruyi.ruyiruyi.ui.service.LuncherDownlodeService");
        GuideActivity.this.registerReceiver(receiver, filter);

    }

    public class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            fuckProgress = 0;
            if (!isfuckFirst) {
                mTimeCount.cancel();
                mTimeCount.onFinish();
            }
            isfuckFirst = false;
            Bundle bundle = intent.getExtras();
            int count = bundle.getInt("count");
            progress = progress + count;
            tv_num.setText(progress + "%");
            if (fuckProgress == 0) {
                mTimeCount = new TimeCount(9000, 600);//
                mTimeCount.start();
            }

            if (progress == 100) {
                //修改判断是否是第一次进入的标志位
                SharedPreferences sf = getSharedPreferences("data", MODE_PRIVATE);//判断是否是第一次进入
                SharedPreferences.Editor editor = sf.edit();
                editor.putBoolean("isFirstIn", false);
                editor.commit();

                tv_num.setVisibility(View.GONE);
                tv_welcome.setVisibility(View.VISIBLE);
                tv_txt.setVisibility(View.GONE);
                openAnimator();
            }
        }
    }

    @Override
    protected void onDestroy() {
        //结束服务
        stopService(new Intent(GuideActivity.this, LuncherDownlodeService.class));
        super.onDestroy();
    }

    private void initView() {
        viewPager = (ViewPager) findViewById(R.id.view_pager);

        LayoutInflater inflater = LayoutInflater.from(this);//将每个xml文件转化为View
        View guideOne = inflater.inflate(R.layout.guide_a_layout, null);//每个xml中就放置一个imageView
        View guideTwo = inflater.inflate(R.layout.guide_b_layout, null);
        View guideThree = inflater.inflate(R.layout.guide_c_layout, null);

        mViews.add(guideOne);//将view加入到list中
        mViews.add(guideTwo);
        mViews.add(guideThree);

        mAdapter = new PagerAdapter() {
            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                View view = mViews.get(position);//初始化适配器，将view加到container中
                container.addView(view);
                return view;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                View view = mViews.get(position);
                container.removeView(view);//将view从container中移除
            }

            @Override
            public int getCount() {
                return mViews.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;//判断当前的view是我们需要的对象
            }
        };

        viewPager.setAdapter(mAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 2 && isFirstToThree) {
//                    actionAnimator();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        bt_home = (TextView) guideThree.findViewById(R.id.to_Main);
        tv_num = (TextView) guideThree.findViewById(R.id.tv_num);
        tv_welcome = (TextView) guideThree.findViewById(R.id.tv_welcome);
        tv_txt = (TextView) guideThree.findViewById(R.id.tv_txt);
        bt_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GuideActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
        bt_home.setAlpha(0);
        bt_home.setClickable(false);
    }

    class TimeCount extends CountDownTimer {

        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            //进行中
            tv_num.setText(progress + (14 - millisUntilFinished / 600) + "%");
        }

        @Override
        public void onFinish() {
            //结束
            Log.e(TAG, "onFinish: ~~~~~~");

        }
    }

    /*
    * 进入主页出现动画
    * */
    private void openAnimator() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(bt_home, View.ALPHA, 1);
        animator.start();
        bt_home.setClickable(true);
    }

    /*
    * 进入主页隐藏动画
    * */
    private void closeAnimator() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(bt_home, View.ALPHA, 0);
        animator.start();
        bt_home.setClickable(true);
    }

}
