package com.ruyiruyi.merchant.ui.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ruyiruyi.merchant.R;
import com.ruyiruyi.merchant.db.DbConfig;
import com.ruyiruyi.merchant.ui.adapter.IncomeFragmentPagerAdapter;
import com.ruyiruyi.merchant.ui.fragment.base.MerchantBaseFragment;
import com.ruyiruyi.merchant.utils.DatePickerDialog;
import com.ruyiruyi.merchant.utils.UtilsRY;
import com.ruyiruyi.merchant.utils.UtilsURL;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;
import com.ruyiruyi.rylibrary.utils.FormatDateUtil;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import rx.functions.Action1;

public class IncomeFragment extends MerchantBaseFragment implements ServiceIncomeFragment.ServiceForMainIncome, SaleIncomeFragment.SaleForMainIncome, GoodsIncomeFragment.GoodsForMainIncome, ExtraIncomeFragment.ExtraForMainIncome {

    private String TAG = IncomeFragment.class.getSimpleName();
    private View parentView;

    private TabLayout income_tab;
    private ViewPager income_pager;
    private TextView year;
    private TextView month;
    private LinearLayout ll_time;
    private TextView all_income;

    private IncomeFragmentPagerAdapter mMainFgPagerAdapter;
    private List<Fragment> fragmentList;

    private double allIncome;
    private double serviceIncome;
    private double goodsIncome;
    private double saleIncome;
    private double extraIncome;
    private int currentYear;
    private int currentMonth;
    private List<String> titlelist;
    private List<String> incomelist;
    private boolean isFirstorChoose = true;
    private boolean isChoose = false;

    private int currentPager = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (parentView == null) {
            parentView = inflater.inflate(R.layout.income_fg, container, false);
        } else {
            ViewGroup viewGroup = (ViewGroup) parentView.getParent();
            if (viewGroup != null)
                viewGroup.removeView(parentView);
        }
        return parentView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initView();
        bindView();

        getCurentYearandMonth();

        initData();
    }

    private void bindView() {
        //选择时间
        RxViewAction.clickNoDouble(ll_time).subscribe(new Action1<Void>() {
            Calendar c = Calendar.getInstance();

            @Override
            public void call(Void aVoid) {

                new DatePickerDialog(getActivity(), 0, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker startDatePicker, int startYear, int startMonthOfYear,
                                          int startDayOfMonth) {
                        currentYear = startYear;
                        currentMonth = startMonthOfYear + 1;

                        //刷新数据
                        isFirstorChoose = true;
                        isChoose = true;
                        initData();

                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE)).show();

            }
        });
    }

    /**
     * 获取并设置当前年月
     */
    private void getCurentYearandMonth() {
        Date date = new Date();
        long time = date.getTime();
        String timestampToString = new UtilsRY().getTimestampToString(time);
        currentYear = Integer.parseInt(timestampToString.substring(0, 4));
        currentMonth = Integer.parseInt(timestampToString.substring(5, 7));
        year.setText(currentYear + "");
        String formatMonth = new FormatDateUtil().formatMonthOrDay(currentMonth);
        month.setText(formatMonth);
    }

    /**
     * post 获取数据 (内含刷新)
     */
    private void initData() {
        //post 获取数据
        JSONObject object = new JSONObject();
        try {
            object.put("storeId", new DbConfig(getContext()).getId());
            String formatMonth = new FormatDateUtil().formatMonthOrDay(currentMonth);
            object.put("date", currentYear + "-" + formatMonth + "-01 00:00:00");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestParams params = new RequestParams(UtilsURL.REQUEST_URL + "getStoreEarningsCountByMonth");
        params.addBodyParameter("reqJson", object.toString());
        Log.e(TAG, "initData:  income resultparams.toString() = " + params.toString());
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess: income result = " + result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONObject data = jsonObject.getJSONObject("data");
                    serviceIncome = data.getDouble("store_service_earnings");
                    goodsIncome = data.getDouble("store_goods_earnings");
                    saleIncome = data.getDouble("store_sale_earnings");
                    extraIncome = data.getDouble("store_app_install_earnings");
                    allIncome = serviceIncome + goodsIncome + saleIncome + extraIncome;

                    //绑定数据
                    bindData();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(getContext(), "信息加载失败,请检查网络", Toast.LENGTH_SHORT).show();
                //网络异常假数据
                serviceIncome = 0;
                goodsIncome = 0;
                saleIncome = 0;
                extraIncome = 0;
                allIncome = serviceIncome + goodsIncome + saleIncome + extraIncome;
                //绑定数据
                bindData();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });

    }

    private void bindData() {
        titlelist = new ArrayList<>();
        titlelist.add("服务收益");
        titlelist.add("商品收益");
        titlelist.add("销售收益");
        titlelist.add("额外收益");
        incomelist = new ArrayList<>();
        incomelist.add(serviceIncome + "");
        incomelist.add(goodsIncome + "");
        incomelist.add(saleIncome + "");
        incomelist.add(extraIncome + "");

        all_income.setText(allIncome + "");

        year.setText(currentYear + "");
        String formatMonth = new FormatDateUtil().formatMonthOrDay(currentMonth);
        month.setText(formatMonth);

        //设置主页tab等
        if (isFirstorChoose) {
            initMain();
        }
    }

    private void initMain() {
        Bundle incomeBundle = new Bundle();
        incomeBundle.putString("year", currentYear + "");
        incomeBundle.putString("month", currentMonth + "");
        Log.e(TAG, "initMain: " + "currentYear = " + currentYear + "currentMonth = " + currentMonth);

        fragmentList.clear();

        ServiceIncomeFragment serviceIncomeFragment = new ServiceIncomeFragment();
        serviceIncomeFragment.setArguments(incomeBundle);
        serviceIncomeFragment.setListener(this);
        fragmentList.add(serviceIncomeFragment);

        GoodsIncomeFragment goodsIncomeFragment = new GoodsIncomeFragment();
        goodsIncomeFragment.setArguments(incomeBundle);
        goodsIncomeFragment.setListener(this);
        fragmentList.add(goodsIncomeFragment);

        SaleIncomeFragment saleIncomeFragment = new SaleIncomeFragment();
        saleIncomeFragment.setArguments(incomeBundle);
        saleIncomeFragment.setListener(this);
        fragmentList.add(saleIncomeFragment);

        ExtraIncomeFragment extraIncomeFragment = new ExtraIncomeFragment();
        extraIncomeFragment.setArguments(incomeBundle);
        extraIncomeFragment.setListener(this);
        fragmentList.add(extraIncomeFragment);

        if (isChoose) { //时间选择刷新 需要刷新适配器
            mMainFgPagerAdapter.UpdataNewData(fragmentList);

            isChoose = false;
        } else {//初次加载 新建适配器并绑定
            mMainFgPagerAdapter = null;
            mMainFgPagerAdapter = new IncomeFragmentPagerAdapter(getChildFragmentManager(), fragmentList);
            income_pager.setAdapter(mMainFgPagerAdapter);
            income_tab.setupWithViewPager(income_pager);
            income_pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    currentPager = position;
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        }


        //Tablayout数据绑定
        setTabData();

        income_pager.setCurrentItem(currentPager);
    }

    /**
     * 初始化控件
     */
    private void initView() {
        income_tab = getView().findViewById(R.id.income_tab);
        income_pager = getView().findViewById(R.id.income_pager);
        year = getView().findViewById(R.id.year);
        month = getView().findViewById(R.id.month);
        ll_time = getView().findViewById(R.id.ll_time);
        all_income = getView().findViewById(R.id.all_income);

        fragmentList = new ArrayList<>();
    }

    /**
     * Tablayout 数据绑定
     */
    private void setTabData() {
        for (int i = 0; i < mMainFgPagerAdapter.getCount(); i++) {
            TabLayout.Tab tab = income_tab.getTabAt(i);
            tab.setCustomView(R.layout.income_tab);
            TextView tv_tab = (TextView) tab.getCustomView().findViewById(R.id.tv_tab);
            TextView tv_income = (TextView) tab.getCustomView().findViewById(R.id.tv_income);
            tv_tab.setText(titlelist.get(i));
            tv_income.setText(incomelist.get(i));
        }
    }

    /**
     * from   ServiceIncomeFragment 接口回调
     */
    @Override
    public void serviceforMianIncome() {
        isFirstorChoose = false;
        //刷新收益信息
        initData();
        //绑定Tablayout数据

        mMainFgPagerAdapter.notifyDataSetChanged();
        setTabData();

        currentPager = 0;
        income_pager.setCurrentItem(currentPager);

    }

    @Override
    public void goodsforMianIncome() {
        isFirstorChoose = false;
        //刷新收益信息
        initData();
        //绑定Tablayout数据

        mMainFgPagerAdapter.notifyDataSetChanged();
        setTabData();

        currentPager = 1;
        income_pager.setCurrentItem(currentPager);
    }

    /**
     * from   SaleIncomeFragment 接口回调
     */
    @Override
    public void saleforMianIncome() {
        isFirstorChoose = false;
        //刷新收益信息
        initData();
        //绑定Tablayout数据

        mMainFgPagerAdapter.notifyDataSetChanged();
        setTabData();

        currentPager = 2;
        income_pager.setCurrentItem(currentPager);

    }

    @Override
    public void extraforMianIncome() {
        isFirstorChoose = false;
        //刷新收益信息
        initData();
        //绑定Tablayout数据

        mMainFgPagerAdapter.notifyDataSetChanged();
        setTabData();

        currentPager = 3;
        income_pager.setCurrentItem(currentPager);
    }

}