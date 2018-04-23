package com.ruyiruyi.ruyiruyi.ui.fragment;

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

import com.ruyiruyi.ruyiruyi.R;
import com.ruyiruyi.ruyiruyi.ui.multiType.GoodsItem;
import com.ruyiruyi.ruyiruyi.ui.multiType.GoodsItemViewBinder;
import com.ruyiruyi.ruyiruyi.utils.FullyLinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import me.drakeet.multitype.MultiTypeAdapter;

import static me.drakeet.multitype.MultiTypeAsserts.assertAllRegistered;
import static me.drakeet.multitype.MultiTypeAsserts.assertHasTheSameAdapter;

public class GoodsListFragment extends Fragment {
    private static final String TAG = GoodsListFragment.class.getSimpleName();
    public static String SHOP_SERVICE_TYPE; //MRQX  QCBY  AZ  LTFW
    private String shopServiceType;
    private TextView text;
    private RecyclerView listView;
    private List<Object> items = new ArrayList<>();
    private MultiTypeAdapter adapter;
    public List<GoodsItem> goodsItemList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_gooods_list,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle arguments = getArguments();
        shopServiceType = arguments.getString(SHOP_SERVICE_TYPE);
        Log.e(TAG, "onActivityCreated: -" + shopServiceType);
        goodsItemList = new ArrayList<>();

        initView();

        initData();

    }

    private void initData() {
        goodsItemList.clear();
        if (shopServiceType.equals("QCBY")){
            goodsItemList.add( new GoodsItem("汽车保养" ,true));
            for (int i = 0; i < 10; i++) {
                goodsItemList.add( new GoodsItem("汽车保养" + i,false));
            }
        }else if (shopServiceType.equals("MRQX")){
            for (int i = 0; i < 20; i++) {
                goodsItemList.add( new GoodsItem("美容清洗" + i,false));
            }
        }else if (shopServiceType.equals("AZ")){
            for (int i = 0; i < 2; i++) {
                goodsItemList.add( new GoodsItem("安装" + i,false));
            }
        }else if (shopServiceType.equals("LTFW")){
            goodsItemList.add( new GoodsItem("轮胎服务" ,true));
            for (int i = 0; i < 6; i++) {
                goodsItemList.add( new GoodsItem("轮胎服务" + i,false));
            }
        }

        items.clear();
        for (int i = 0; i <goodsItemList.size() ; i++) {
            items.add(goodsItemList.get(i));
        }
        assertAllRegistered(adapter,items);
        adapter.notifyDataSetChanged();
    }

    private void initView() {
        listView = (RecyclerView) getView().findViewById(R.id.goods_listview);
        FullyLinearLayoutManager linearLayoutManager = new FullyLinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        listView.setLayoutManager(linearLayoutManager);
        adapter = new MultiTypeAdapter(items);
        register();

        listView.setAdapter(adapter);
        assertHasTheSameAdapter(listView, adapter);

    }

    private void register() {
        adapter.register(GoodsItem.class,new GoodsItemViewBinder());
    }
}