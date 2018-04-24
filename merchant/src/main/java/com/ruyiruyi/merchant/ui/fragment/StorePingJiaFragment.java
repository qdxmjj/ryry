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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ruyiruyi.merchant.R;
import com.ruyiruyi.merchant.bean.GoodsBean;
import com.ruyiruyi.merchant.bean.StorePingJiaBean;
import com.ruyiruyi.merchant.ui.multiType.GoodsItemProvider;
import com.ruyiruyi.merchant.ui.multiType.StorePingJiaItemProvider;

import java.util.ArrayList;
import java.util.List;

import me.drakeet.multitype.MultiTypeAdapter;

import static me.drakeet.multitype.MultiTypeAsserts.assertAllRegistered;
import static me.drakeet.multitype.MultiTypeAsserts.assertHasTheSameAdapter;

public class StorePingJiaFragment extends Fragment {
    private RecyclerView mRlv;
    private List<StorePingJiaBean> pingjiaBeanList;
    private MultiTypeAdapter multiTypeAdapter;
    private List<Object> items = new ArrayList<>();
    private String TAG = StorePingJiaFragment.class.getSimpleName();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.store_pingjia_fg, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        pingjiaBeanList = new ArrayList<>();

        initView();
        initData();

    }

    private void initData() {

        for (int i = 0; i < 10; i++) {
            StorePingJiaBean bean = new StorePingJiaBean();
            bean.setUser_id(i);
            bean.setUser_img_url("http://192.168.0.167/images/store/storeImg/18254262176/mdPicb.png");
            bean.setPingjia_pica_url("http://192.168.0.167/images/store/storeImg/18254262176/mdPicb.png");
            bean.setPingjia_picb_url("http://192.168.0.167/images/store/storeImg/18254262176/mdPicb.png");
            bean.setUser_name(i + "号测试员");
            bean.setStar("3");
            bean.setPj_time("2018-04-24");
            bean.setPj_txt("好评！" + i);
            pingjiaBeanList.add(bean);
        }
        items.clear();
        for (int i = 0; i < pingjiaBeanList.size(); i++) {
            items.add(pingjiaBeanList.get(i));
        }
        assertAllRegistered(multiTypeAdapter, items);
        multiTypeAdapter.notifyDataSetChanged();
    }

    private void initView() {
        mRlv = (RecyclerView) getView().findViewById(R.id.store_pingjia_rlv);

        LinearLayoutManager manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mRlv.setLayoutManager(manager);
        multiTypeAdapter = new MultiTypeAdapter(items);
        multiTypeAdapter.register(StorePingJiaBean.class, new StorePingJiaItemProvider(getContext()));
        mRlv.setAdapter(multiTypeAdapter);
        assertHasTheSameAdapter(mRlv, multiTypeAdapter);
    }
}