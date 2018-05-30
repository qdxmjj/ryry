package com.ruyiruyi.ruyiruyi.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.ruyiruyi.ruyiruyi.db.DbConfig;
import com.ruyiruyi.ruyiruyi.db.model.Newest;
import com.ruyiruyi.ruyiruyi.db.model.UserTest;
import com.ruyiruyi.ruyiruyi.ui.activity.base.RYBaseActivity;
import com.ruyiruyi.ruyiruyi.ui.service.CodeTimerService;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;
import com.ruyiruyi.rylibrary.utils.LayoutHelper;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import rx.functions.Action1;

public class TestActivity extends RYBaseActivity {

    private static final String TAG = TestActivity.class.getSimpleName();
    private LinearLayout content;
    private Button btnCountdown;
    private Button dituButton;
    private Button evaluateButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        content = new LinearLayout(this);

        setContentView(content);
        content.setOrientation(LinearLayout.VERTICAL);

        btnCountdown = new Button(this);
        btnCountdown.setText("时间测试");
        content.addView(btnCountdown, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT,LayoutHelper.WRAP_CONTENT));

        btnCountdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startService(new Intent(getApplication(),CodeTimerService.class));
            }
        });

        dituButton = new Button(this);
        dituButton.setText("百度地图");
        content.addView(dituButton, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT,LayoutHelper.WRAP_CONTENT));
        RxViewAction.clickNoDouble(dituButton)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        startActivity(new Intent(getApplicationContext(),BaiduActivity.class));
                    }
                });

        evaluateButton = new Button(this);
        evaluateButton.setText("评价");
        content.addView(evaluateButton, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT,LayoutHelper.WRAP_CONTENT));
        RxViewAction.clickNoDouble(evaluateButton)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        startActivity(new Intent(getApplicationContext(),EvaluateActivity.class));
                    }
                });




    }

    // 注册广播
    private static IntentFilter updateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(CodeTimerService.IN_RUNNING);
        intentFilter.addAction(CodeTimerService.END_RUNNING);
        return intentFilter;
    }

    // 广播接收者
    private final BroadcastReceiver mUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            switch (action) {
                case CodeTimerService.IN_RUNNING:
                    if (btnCountdown.isEnabled())
                        btnCountdown.setEnabled(false);
                    // 正在倒计时
                    btnCountdown.setText("倒计时中(" + intent.getStringExtra("time") + ")");
                    Log.e(TAG, "倒计时中(" + intent.getStringExtra("time") + ")");
                    break;
                case CodeTimerService.END_RUNNING:
                    // 完成倒计时
                    btnCountdown.setEnabled(true);
                    btnCountdown.setText("倒计时");

                    break;
            }
        }
    };


    @Override
    protected void onResume() {
        super.onResume();
        // 注册广播
      //  registerReceiver(mUpdateReceiver, updateIntentFilter());
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 移除注册
      //  unregisterReceiver(mUpdateReceiver);
    }

    private void initDataFromDb() {
        DbConfig dbConfig = new DbConfig();
        DbManager.DaoConfig daoConfig = dbConfig.getDaoConfig();

        DbManager db = x.getDb(daoConfig);

        List<Newest> data = new ArrayList<>();
        Newest newest = new Newest();
        newest.setId(1);
        newest.setTitle("122");
        data.add(newest);
        for (Newest newest1 : data){
            try {
                db.saveOrUpdate(data);
            } catch (DbException e) {

            }
        }

        List<UserTest> userTestList = new ArrayList<>();
        UserTest test = new UserTest(2,"bbb");
        userTestList.add(test);
        try {
            db.save(userTestList);
        } catch (DbException e) {

        }

    }

}
