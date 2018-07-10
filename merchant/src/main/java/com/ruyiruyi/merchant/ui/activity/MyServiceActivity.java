package com.ruyiruyi.merchant.ui.activity;

import android.app.ProgressDialog;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ruyiruyi.merchant.R;
import com.ruyiruyi.merchant.bean.ServicesBean;
import com.ruyiruyi.merchant.db.DbConfig;
import com.ruyiruyi.merchant.ui.fragment.MyServiceFragment;
import com.ruyiruyi.merchant.ui.multiType.ServiceItemProvider;
import com.ruyiruyi.merchant.utils.UtilsURL;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;
import com.ruyiruyi.rylibrary.base.BaseActivity;
import com.ruyiruyi.rylibrary.base.BaseFragmentActivity;
import com.ruyiruyi.rylibrary.cell.ActionBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import rx.functions.Action1;

public class MyServiceActivity extends BaseFragmentActivity implements MyServiceFragment.StartFragmentPasstoActivity {
    private static final String TAG = MyServiceActivity.class.getSimpleName();
    private ActionBar mAcBar;
    private TabLayout mTab;
    private ViewPager mVp;
    private TextView tv_saveService;

    private List<Fragment> fragments;
    private List<String> title_list;

    private List<String> selectServicesList = new ArrayList<>();
    private String selectServicesListString = "";
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_service);
        mAcBar = (ActionBar) findViewById(R.id.myservice_acbar);
        mAcBar.setTitle("我的服务");
        mAcBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(int var1) {
                switch (var1) {
                    case -1:
                        onBackPressed();
                        break;
                }
            }
        });
        progressDialog = new ProgressDialog(this);


        initView();
        bindView();
    }

    private void bindView() {
        //保存提交
        RxViewAction.clickNoDouble(tv_saveService).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                showDialogProgress(progressDialog, "保存提交中...");
                Log.e(TAG, "call: 000servicesBeanList_all = >" + "all" + "<--" + selectServicesList.size());
                JSONObject object = new JSONObject();
                String storeId = new DbConfig(getApplicationContext()).getId() + "";
                for (int i = 0; i < selectServicesList.size(); i++) {
                    if (i == selectServicesList.size() - 1) {
                        selectServicesListString = selectServicesListString + selectServicesList.get(i);
                    } else {
                        selectServicesListString = selectServicesListString + selectServicesList.get(i) + ",";
                    }
                }

                Log.e(TAG, "call: selectServicesListString==>" + selectServicesListString);


                try {
                    object.put("storeId", storeId);
                    object.put("services", selectServicesListString);
                } catch (JSONException e) {
                }
                RequestParams params = new RequestParams(UtilsURL.REQUEST_URL + "addStoreServices");
                params.addBodyParameter("reqJson", object.toString());
                params.setConnectTimeout(6000);
                x.http().post(params, new Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        try {
                            JSONObject object1 = new JSONObject(result);
                            String msg = object1.getString("msg");
                            int status = object1.getInt("status");
                            Toast.makeText(MyServiceActivity.this, msg, Toast.LENGTH_SHORT).show();
                            finish();
                        } catch (JSONException e) {
                        }
                    }

                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {
                        Toast.makeText(getApplicationContext(), "信息保存失败,请检查网络", Toast.LENGTH_SHORT).show();
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
        });
    }

    private void initView() {
        mTab = (TabLayout) findViewById(R.id.myservice_tab);
        mVp = (ViewPager) findViewById(R.id.myservice_vp);
        tv_saveService = (TextView) findViewById(R.id.tv_myservice_save);

        getTitles();
        getFragments();

        for (int i = 0; i < title_list.size(); i++) {
            TabLayout.Tab tab = mTab.newTab();
            tab.setText(title_list.get(i));
            mTab.addTab(tab);
        }
        mVp.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public int getCount() {
                return fragments == null ? 0 : fragments.size();
            }

            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            //设置适配器（适配器中必须重写这一步，不然标题不会显示出来）
            @Override
            public CharSequence getPageTitle(int position) {
                return title_list.get(position);
            }
        });
        mTab.setupWithViewPager(mVp);
        mVp.setCurrentItem(0);
        mVp.setOffscreenPageLimit(3);

    }

    private void getTitles() {
        title_list = new ArrayList();
        title_list.add(MyServiceActivity.this.getString(R.string.service_type_a));
        title_list.add(MyServiceActivity.this.getString(R.string.service_type_b));
        title_list.add(MyServiceActivity.this.getString(R.string.service_type_c));
        title_list.add(MyServiceActivity.this.getString(R.string.service_type_d));

    }

    private void getFragments() {

        fragments = new ArrayList();
        MyServiceFragment qcby_fragment = new MyServiceFragment();
        qcby_fragment.setListener(this);
        Bundle bundle_qcby = new Bundle();
        bundle_qcby.putString(MyServiceFragment.SALE_TYPE, "QCBY");//id 2
        qcby_fragment.setArguments(bundle_qcby);
        fragments.add(qcby_fragment);

        MyServiceFragment mrqx_fragment = new MyServiceFragment();
        mrqx_fragment.setListener(this);
        Bundle bundle_mrqx = new Bundle();
        bundle_mrqx.putString(MyServiceFragment.SALE_TYPE, "MRQX");//id 3
        mrqx_fragment.setArguments(bundle_mrqx);
        fragments.add(mrqx_fragment);

        MyServiceFragment az_fragment = new MyServiceFragment();
        az_fragment.setListener(this);
        Bundle bundle_az = new Bundle();
        bundle_az.putString(MyServiceFragment.SALE_TYPE, "AZ");//id 4
        az_fragment.setArguments(bundle_az);
        fragments.add(az_fragment);

        MyServiceFragment ltfw_fragment = new MyServiceFragment();
        ltfw_fragment.setListener(this);
        Bundle bundle_ltfw = new Bundle();
        bundle_ltfw.putString(MyServiceFragment.SALE_TYPE, "LTFW");//id 5
        ltfw_fragment.setArguments(bundle_ltfw);
        fragments.add(ltfw_fragment);

    }

    //Test false 0.0
    public void onTestClick(View buttonView) {
        Toast.makeText(MyServiceActivity.this, "0000000000000000", Toast.LENGTH_SHORT);
        ServicesBean bean = (ServicesBean) buttonView.getTag();
        if (bean.getIsChecked() == 0) {//添加
            buttonView.setBackgroundResource(R.drawable.ic_check);
            if (selectServicesList == null || selectServicesList.size() == 0) {//原始为空 则添加
                selectServicesList.add(bean.getService_id() + "");
            } else {//原始不为空 则判断是否存在
                for (int i = 0; i < selectServicesList.size(); i++) {
                    if (selectServicesList.get(i).equals(bean.getServiceInfo())) {
                        //存在 不操作
                    } else {
                        //不存在 添加
                        selectServicesList.add(bean.getService_id() + "");
                    }
                }
            }
        } else {//移除
            buttonView.setBackgroundResource(R.drawable.ic_notcheck);
            if (selectServicesList == null || selectServicesList.size() == 0) {//原始为空 则不移除 不操作

            } else {//原始不为空 则判断是否存在
                for (int i = 0; i < selectServicesList.size(); i++) {
                    if (selectServicesList.get(i).equals(bean.getServiceInfo())) {
                        //存在 移除
                        selectServicesList.remove(bean.getService_id() + "");
                    } else {
                        //不存在 不操作
                    }
                }
            }
        }
    }


    @Override
    public void startFragmentPasstoActivityListener(String s, List<String> checkedList) {
        selectServicesList.addAll(checkedList);

    }

    @Override
    public void onServiceItemClickToActivityListener(int id) {
        if (selectServicesList == null || selectServicesList.size() == 0) {//原来选择的为空 则直接添加
            selectServicesList.add(id + "");
        } else {//原来选择的不为空 则判断id是否已存在
            int sizea = selectServicesList.size();
            boolean isadd = true;
            for (int i = 0; i < sizea; i++) {
                if (selectServicesList.get(i).equals(id + "")) {//如果该id已存在 则移除
                    isadd = false;
                 /*   Log.e(TAG, "onServiceItemClickToActivityListener000: ++++++++" );
                    selectServicesList.remove(i);
                    return;*/
                }/* else {//如果该id不存在 则添加
                    selectServicesList.add(id + "");
                }*/
            }
            if (isadd) {//添加
                selectServicesList.add(id + "");
            } else {
                selectServicesList.remove(id + "");
            }
        }
    }
}
