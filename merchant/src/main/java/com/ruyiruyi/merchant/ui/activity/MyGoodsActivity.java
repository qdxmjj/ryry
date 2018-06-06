package com.ruyiruyi.merchant.ui.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ruyiruyi.merchant.R;
import com.ruyiruyi.merchant.bean.ServicesBean;
import com.ruyiruyi.merchant.db.DbConfig;
import com.ruyiruyi.merchant.ui.adapter.MyPagerAdapter;
import com.ruyiruyi.merchant.ui.fragment.MyGoodsFragment;
import com.ruyiruyi.merchant.utils.UtilsURL;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;
import com.ruyiruyi.rylibrary.cell.ActionBar;
import com.ruyiruyi.rylibrary.ui.cell.WheelView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import rx.functions.Action1;

public class MyGoodsActivity extends FragmentActivity {
    private ActionBar mActionBar;
    private TabLayout mTab;
    private ViewPager mVPager;
    private List<Fragment> fragments;
    private List<String> title_list;
    private MyPagerAdapter pagerAdapter;

    private LinearLayout ll_bottom;
    private TextView tv_addgoods;
    private TextView tv_type;
    private TextView tv_all_type;
    private ImageView img_bottom;
    private WheelView whvL;
    private WheelView whvR;
    private int currentLeftPosition = 0;
    private int currentRightPosition = 0;
    private int currentForId = 0;
    private String currentLeftString = "请选择";
    private String currentRightString = "请选择";
    private List<String> leftTypeList;
    private List<ServicesBean> servicesBean2;
    private List<ServicesBean> servicesBean3;
    private List<ServicesBean> servicesBean4;
    private List<ServicesBean> servicesBean5;
    private List<ServicesBean> servicesBean2a;
    private List<ServicesBean> servicesBean3a;
    private List<ServicesBean> servicesBean4a;
    private List<ServicesBean> servicesBean5a;
    private List<ServicesBean> servicesBeanAllType;
    private String leftTypeId = "";
    private String rightTypeId = "";


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    break;
                case 2:
                    myRequestPostForDataBy("3");
                    break;
                case 3:
                    myRequestPostForDataBy("4");
                    break;
                case 4:
                    myRequestPostForDataBy("5");
                    break;
                case 5:
                    //以下操作
                    if (servicesBean2a.size() != 0) {
                        leftTypeList.add("汽车保养");
                    }
                    if (servicesBean3a.size() != 0) {
                        leftTypeList.add("美容清洗");
                    }
                    if (servicesBean4a.size() != 0) {
                        leftTypeList.add("安装");
                    }
                    if (servicesBean5a.size() != 0) {
                        leftTypeList.add("轮胎服务");
                    }
                    leftTypeList.add("全部分类");
                    bindView();
                    break;
            }
        }
    };
    private String TAG = MyGoodsActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_goods);
        mActionBar = (ActionBar) findViewById(R.id.mygoods_acbar);
        mActionBar.setTitle("商品管理");
        mActionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(int var1) {
                switch (var1) {
                    case -1:
                        onBackPressed();
                        break;
                }
            }
        });

        initView();
        initData();


    }

    private void initData() {
        myRequestPostForDataBy("2");
    }

    private void bindView() {
        //添加商品 跳转
        RxViewAction.clickNoDouble(tv_addgoods).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Intent intent = new Intent(MyGoodsActivity.this, GoodsInfoActivity.class);
                startActivity(intent);
            }
        });

        //分类
        RxViewAction.clickNoDouble(tv_all_type).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                //选择弹框
                showSelectDialog();
            }
        });
    }

    //选择弹框
    private void showSelectDialog() {

        img_bottom.setImageResource(R.drawable.ic_upwhite);//箭头

        currentLeftPosition = 0;//每次弹Dialog 初始化
        currentRightPosition = 0;//每次弹Dialog 初始化
        currentForId = 0;//每次弹Dialog 初始化

        View v_select = LayoutInflater.from(this).inflate(R.layout.dialog_two_horizontal_wheel_view, null);
        whvL = v_select.findViewById(R.id.whv_left);
        whvR = v_select.findViewById(R.id.whv_right);
        whvL.setItems(leftTypeList, currentLeftPosition);
        String s = leftTypeList.get(0);
        List<String> strings = getRightStringList(s);
        whvR.setItems(strings, currentRightPosition);
        currentLeftString = leftTypeList.get(0);//每次弹Dialog 初始化
        currentRightString = strings.get(currentRightPosition);//每次弹Dialog 初始化
        whvL.setOnItemSelectedListener(new WheelView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int selectedIndex, String item) {
                currentRightPosition = 0;
                currentLeftPosition = whvL.getSelectedPosition();
                currentLeftString = whvL.getSelectedItem();
                List<String> strings2 = getRightStringList(currentLeftString);
                whvR.setItems(strings2, currentRightPosition);
                currentRightString = strings2.get(currentRightPosition);//每次点击 初始化
            }
        });
        whvR.setOnItemSelectedListener(new WheelView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int selectedIndex, String item) {
                currentRightPosition = whvR.getSelectedPosition();
                currentRightString = whvR.getSelectedItem();
            }
        });
        whvL.setIsLoop(false);
        whvR.setIsLoop(false);
        new AlertDialog.Builder(this)
                .setTitle("选择排列方式")
                .setView(v_select)
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        img_bottom.setImageResource(R.drawable.ic_downwhite);//箭头
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        img_bottom.setImageResource(R.drawable.ic_downwhite);//箭头

                        tv_all_type.setText(currentRightString);
                        tv_type.setVisibility(View.GONE);
                        currentForId = currentRightPosition;
                        //获取ID
                        switch (currentLeftString) {
                            case "汽车保养":
                                leftTypeId = "2";
                                rightTypeId = servicesBean2a.get(currentForId).getService_id() + "";
                                break;
                            case "美容清洗":
                                leftTypeId = "3";
                                rightTypeId = servicesBean3a.get(currentForId).getService_id() + "";
                                break;
                            case "安装":
                                leftTypeId = "4";
                                rightTypeId = servicesBean4a.get(currentForId).getService_id() + "";
                                break;
                            case "轮胎服务":
                                leftTypeId = "5";
                                rightTypeId = servicesBean5a.get(currentForId).getService_id() + "";
                                break;
                            case "全部分类":
                                leftTypeId = "";//所有类型
                                rightTypeId = "";//所有类型
                                break;
                        }
                        //接下来操作 （向Fragment传值）
                        Log.e(TAG, "onClick: 666 leftTypeId = " + leftTypeId);
                        Log.e(TAG, "onClick: 666 rightTypeId = " + rightTypeId);
//        2                listener.passToEnterFragment(leftTypeId,rightTypeId);  //无法实现activity一对多个Fragment回调...
                        fragments.clear();
                        fragments = getFragments(leftTypeId, rightTypeId);
                        title_list.clear();
                        title_list = getTitles();
                        pagerAdapter.UpdataNewData(title_list, fragments);

                    }
                }).show();

    }

    private void initView() {
        servicesBean2 = new ArrayList<>();
        servicesBean3 = new ArrayList<>();
        servicesBean4 = new ArrayList<>();
        servicesBean5 = new ArrayList<>();
        servicesBean2a = new ArrayList<>();
        servicesBean3a = new ArrayList<>();
        servicesBean4a = new ArrayList<>();
        servicesBean5a = new ArrayList<>();
        leftTypeList = new ArrayList<>();
        servicesBeanAllType = new ArrayList<>();
        mTab = (TabLayout) findViewById(R.id.mygoods_tab);
        mVPager = (ViewPager) findViewById(R.id.mygoods_vpager);
        ll_bottom = (LinearLayout) findViewById(R.id.ll_bottom);
        tv_addgoods = (TextView) findViewById(R.id.tv_addgoods);
        tv_type = (TextView) findViewById(R.id.tv_type);
        tv_all_type = (TextView) findViewById(R.id.tv_all_type);
        img_bottom = (ImageView) findViewById(R.id.img_bottom);
        fragments = new ArrayList<>();
        fragments = getFragments(leftTypeId, rightTypeId);
        title_list = new ArrayList<>();
        title_list = getTitles();
        for (int i = 0; i < title_list.size(); i++) {
            TabLayout.Tab tab = mTab.newTab();
            tab.setText(title_list.get(i));
            mTab.addTab(tab);
        }
        pagerAdapter = new MyPagerAdapter(getSupportFragmentManager(), title_list, fragments);
        mVPager.setAdapter(pagerAdapter);
        mTab.setupWithViewPager(mVPager);

    }

    private List<String> getRightStringList(String s) {
        ArrayList<String> strings = new ArrayList<>();
        switch (s) {
            case "汽车保养":
                for (int i = 0; i < servicesBean2a.size(); i++) {
                    strings.add(servicesBean2a.get(i).getServiceInfo());
                }
                break;
            case "美容清洗":
                for (int i = 0; i < servicesBean3a.size(); i++) {
                    strings.add(servicesBean3a.get(i).getServiceInfo());
                }
                break;
            case "安装":
                for (int i = 0; i < servicesBean4a.size(); i++) {
                    strings.add(servicesBean4a.get(i).getServiceInfo());
                }
                break;
            case "轮胎服务":
                for (int i = 0; i < servicesBean5a.size(); i++) {
                    strings.add(servicesBean5a.get(i).getServiceInfo());
                }
                break;
            case "全部分类":
                ServicesBean bean = new ServicesBean();
                bean.setIsChecked(1);
                bean.setServiceInfo("全部分类");
                servicesBeanAllType.add(bean);
                strings.add(servicesBeanAllType.get(0).getServiceInfo());
                break;
        }
        return strings;
    }

    private void myRequestPostForDataBy(final String s) {
        final List<ServicesBean> servicesBean = new ArrayList<>();
        JSONObject jsonObject = new JSONObject();
        try {
            String storeId = new DbConfig().getId() + "";
            jsonObject.put("storeId", storeId);
            jsonObject.put("serviceTypeId", s);
        } catch (JSONException e) {
        }
        RequestParams params = new RequestParams(UtilsURL.REQUEST_URL + "getStoreServicesAndState");
        params.addBodyParameter("reqJson", jsonObject.toString());
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject object = new JSONObject(result);
                    JSONArray data = object.getJSONArray("data");
                    String msg = object.getString("msg");
                    int status = object.getInt("status");
                    if (data != null && data.length() != 0) {
                        for (int i = 0; i < data.length(); i++) {
                            ServicesBean bean = new ServicesBean();
                            JSONObject obj = (JSONObject) data.get(i);
                            bean.setService_id(obj.getInt("id"));
                            bean.setIsChecked(Integer.parseInt(obj.getString("selectState")));
                            bean.setServiceInfo(obj.getString("name"));
                            servicesBean.add(bean);

                        }
                    }
                    switch (s) {
                        case "2":
                            for (int i = 0; i < servicesBean.size(); i++) {
                                servicesBean2.add(servicesBean.get(i));
                            }
                            for (int i = 0; i < servicesBean2.size(); i++) {
                                if (servicesBean2.get(i).getIsChecked() == 1) {
                                    servicesBean2a.add(servicesBean2.get(i));
                                }
                            }
                            break;
                        case "3":
                            for (int i = 0; i < servicesBean.size(); i++) {
                                servicesBean3.add(servicesBean.get(i));
                            }
                            for (int i = 0; i < servicesBean3.size(); i++) {
                                if (servicesBean3.get(i).getIsChecked() == 1) {
                                    servicesBean3a.add(servicesBean3.get(i));
                                }
                            }
                            break;
                        case "4":
                            for (int i = 0; i < servicesBean.size(); i++) {
                                servicesBean4.add(servicesBean.get(i));
                            }
                            for (int i = 0; i < servicesBean4.size(); i++) {
                                if (servicesBean4.get(i).getIsChecked() == 1) {
                                    servicesBean4a.add(servicesBean4.get(i));
                                }
                            }
                            break;
                        case "5":
                            for (int i = 0; i < servicesBean.size(); i++) {
                                servicesBean5.add(servicesBean.get(i));
                            }
                            for (int i = 0; i < servicesBean5.size(); i++) {
                                if (servicesBean5.get(i).getIsChecked() == 1) {
                                    servicesBean5a.add(servicesBean5.get(i));
                                }
                            }
                            break;

                    }
                    Message message = new Message();
                    message.what = Integer.parseInt(s);
                    mHandler.sendMessage(message);
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

            }
        });

    }

    private List<String> getTitles() {
        List<String> stringList = new ArrayList<>();
        stringList.add("出售中");
        stringList.add("已下架");
        return stringList;
    }

    private List<Fragment> getFragments(String leftTypeId, String rightTypeId) {

        ArrayList<Fragment> list_fg = new ArrayList<>();
        MyGoodsFragment onsale_fragment = new MyGoodsFragment();
        Bundle bundle_onSale2 = new Bundle();
        bundle_onSale2.putString(MyGoodsFragment.SALE_TYPE, "ONSALE");
        bundle_onSale2.putString(MyGoodsFragment.LEFT_ID, leftTypeId);
        bundle_onSale2.putString(MyGoodsFragment.RIGHT_ID, rightTypeId);//OMG
        onsale_fragment.setArguments(bundle_onSale2);
        list_fg.add(onsale_fragment);

        MyGoodsFragment nosale_fragment = new MyGoodsFragment();
        Bundle bundle_noSale2 = new Bundle();
        bundle_noSale2.putString(MyGoodsFragment.SALE_TYPE, "NOSALE");
        bundle_noSale2.putString(MyGoodsFragment.LEFT_ID, leftTypeId);
        bundle_noSale2.putString(MyGoodsFragment.RIGHT_ID, rightTypeId);
        nosale_fragment.setArguments(bundle_noSale2);
        list_fg.add(nosale_fragment);

        return list_fg;
    }


}
