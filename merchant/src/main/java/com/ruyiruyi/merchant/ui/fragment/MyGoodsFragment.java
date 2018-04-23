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

import com.ruyiruyi.merchant.R;
import com.ruyiruyi.merchant.bean.GoodsBean;
import com.ruyiruyi.merchant.ui.multiType.modle.GoodsItemProvider;

import java.util.ArrayList;
import java.util.List;

import me.drakeet.multitype.MultiTypeAdapter;

import static me.drakeet.multitype.MultiTypeAsserts.assertAllRegistered;
import static me.drakeet.multitype.MultiTypeAsserts.assertHasTheSameAdapter;

public class MyGoodsFragment extends Fragment {
    public static String SALE_TYPE = "SALE_TYPE";
    private String sale_type;
    private RecyclerView mRlv;
    private List<GoodsBean> goodsBeenList;
    private MultiTypeAdapter multiTypeAdapter;
    private List<Object> items = new ArrayList<>();
    private String TAG = MyGoodsFragment.class.getSimpleName();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.mygoods_list_fg, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Bundle bundle = getArguments();
        sale_type = bundle.getString(SALE_TYPE);
        Log.e(TAG, "onActivityCreated: " + sale_type);
        goodsBeenList = new ArrayList<>();

        initView();
        initData();

    }

    private void initData() {
        switch (sale_type) {
            case "ONSALE":
                for (int i = 0; i < 10; i++) {
                    GoodsBean bean = new GoodsBean();
                    bean.setGoods_id(i);
                    bean.setImg_url("http://180.76.243.205:8111/images/userHeadimgurl/defaultHead.png");
                    bean.setKucun(i + "");
                    bean.setTxt("清仓大甩卖" + i);
                    bean.setYishou(i + "");
                    bean.setMoney(888 + "");
                    goodsBeenList.add(bean);
                }
                break;
            case "NOSALE":
                for (int i = 0; i < 10; i++) {
                    GoodsBean bean = new GoodsBean();
                    bean.setGoods_id(i);
                    bean.setImg_url("http://180.76.243.205:8111/images/userHeadimgurl/defaultHead.png");
                    bean.setKucun(i + "");
                    bean.setTxt("已下架" + i);
                    bean.setYishou(i + "");
                    bean.setMoney(888 + "");
                    goodsBeenList.add(bean);
                }
                break;
        }

        items.clear();
        for (int i = 0; i <goodsBeenList.size() ; i++) {
            items.add(goodsBeenList.get(i));
        }
        assertAllRegistered(multiTypeAdapter,items);
        multiTypeAdapter.notifyDataSetChanged();
    }

    private void initView() {
        mRlv = (RecyclerView) getView().findViewById(R.id.mygoods_rlv);

        LinearLayoutManager manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mRlv.setLayoutManager(manager);
        multiTypeAdapter = new MultiTypeAdapter(items);
        multiTypeAdapter.register(GoodsBean.class, new GoodsItemProvider());
        mRlv.setAdapter(multiTypeAdapter);
        assertHasTheSameAdapter(mRlv, multiTypeAdapter);
    }
}