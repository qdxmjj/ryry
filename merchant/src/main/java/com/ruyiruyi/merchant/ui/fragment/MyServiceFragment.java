package com.ruyiruyi.merchant.ui.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ruyiruyi.merchant.R;
import com.ruyiruyi.merchant.bean.ServicesBean;
import com.ruyiruyi.merchant.ui.multiType.ServiceItemProvider;

import java.util.ArrayList;
import java.util.List;

import me.drakeet.multitype.MultiTypeAdapter;

import static me.drakeet.multitype.MultiTypeAsserts.assertAllRegistered;
import static me.drakeet.multitype.MultiTypeAsserts.assertHasTheSameAdapter;

public class MyServiceFragment extends Fragment {
    public static String SALE_TYPE = "SALE_TYPE";
    private String sale_type;
    private RecyclerView mRlv;
    private TextView tv_save;
    private List<ServicesBean> servicesBean;
    private MultiTypeAdapter multiTypeAdapter;
    private List<Object> items = new ArrayList<>();
    private String TAG = MyServiceFragment.class.getSimpleName();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.myservice_list_fg, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Bundle bundle = getArguments();
        sale_type = bundle.getString(SALE_TYPE);
        Log.e(TAG, "onActivityCreated: " + sale_type);
        servicesBean = new ArrayList<>();

        initView();
        initData();

    }

    private void initData() {
        switch (sale_type) {
            case "QCBY":
                for (int i = 0; i < 10; i++) {
                    ServicesBean bean = new ServicesBean();
                    bean.setService_id(i);
                    bean.setServiceInfo("汽车保养" + i);
                    if (i == 0) {
                        bean.setIsChecked(1);
                    } else {
                        bean.setIsChecked(0);
                    }
                    servicesBean.add(bean);
                }
                break;
            case "MRQX":
                for (int i = 0; i < 10; i++) {
                    ServicesBean bean = new ServicesBean();
                    bean.setService_id(i);
                    bean.setServiceInfo("美容清洗" + i);
                    if (i == 0) {
                        bean.setIsChecked(1);
                    } else {
                        bean.setIsChecked(0);
                    }
                    servicesBean.add(bean);
                }
                break;
            case "AZ":
                for (int i = 0; i < 10; i++) {
                    ServicesBean bean = new ServicesBean();
                    bean.setService_id(i);
                    bean.setServiceInfo("安装" + i);
                    if (i == 0) {
                        bean.setIsChecked(1);
                    } else {
                        bean.setIsChecked(0);
                    }
                    servicesBean.add(bean);
                }
                break;
            case "LTFW":
                for (int i = 0; i < 10; i++) {
                    ServicesBean bean = new ServicesBean();
                    bean.setService_id(i);
                    bean.setServiceInfo("轮胎服务" + i);
                    if (i == 0) {
                        bean.setIsChecked(1);
                    } else {
                        bean.setIsChecked(0);
                    }
                    servicesBean.add(bean);
                }
                break;
        }

        items.clear();
        for (int i = 0; i < servicesBean.size(); i++) {
            items.add(servicesBean.get(i));
        }
        assertAllRegistered(multiTypeAdapter, items);
        multiTypeAdapter.notifyDataSetChanged();
    }

    private void initView() {
        mRlv = (RecyclerView) getView().findViewById(R.id.myservice_rlv);
        tv_save = (TextView) getView().findViewById(R.id.tv_myservice_save);

        LinearLayoutManager manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mRlv.setLayoutManager(manager);
        multiTypeAdapter = new MultiTypeAdapter(items);
        multiTypeAdapter.register(ServicesBean.class, new ServiceItemProvider());
        mRlv.setAdapter(multiTypeAdapter);
        assertHasTheSameAdapter(mRlv, multiTypeAdapter);
    }
}