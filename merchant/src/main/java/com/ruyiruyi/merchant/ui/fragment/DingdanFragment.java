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
import android.widget.Toast;

import com.ruyiruyi.merchant.R;
import com.ruyiruyi.merchant.ui.multiType.DingdanItemViewProvider;
import com.ruyiruyi.merchant.ui.multiType.listener.OnMyItemTouchListener;
import com.ruyiruyi.merchant.ui.multiType.modle.Dingdan;

import java.util.ArrayList;
import java.util.List;

import me.drakeet.multitype.MultiTypeAdapter;

import static me.drakeet.multitype.MultiTypeAsserts.assertAllRegistered;
import static me.drakeet.multitype.MultiTypeAsserts.assertHasTheSameAdapter;


public class DingdanFragment extends Fragment {
    private static final String TAG = DingdanFragment.class.getSimpleName();
    private RecyclerView mRecyclerView;
    private MultiTypeAdapter multiTypeAdapter;
    private List<Object> items = new ArrayList<>();
    private List<Dingdan> list;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dingdan_fg,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initView();

        initDataAndRfTest();

        bindView();
//        mRecyclerView.set


    }

    private void bindView() {
        //设置item监听
        mRecyclerView.addOnItemTouchListener(new OnMyItemTouchListener(mRecyclerView) {
            @Override
            public void onItemClick(RecyclerView.ViewHolder vh) {
                //操作
                Log.e(TAG, "Recycler-->onItemClick: "+"AdapterPosition:"+vh.getAdapterPosition());
                Toast.makeText(getActivity(),"AdapterPosition:"+vh.getAdapterPosition(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initView() {
        mRecyclerView = (RecyclerView) getView().findViewById(R.id.rlv_dingdan);
        LinearLayoutManager manager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        mRecyclerView.setLayoutManager(manager);
        multiTypeAdapter = new MultiTypeAdapter(items);
        /* 注册类型和 View 的对应关系 */
        multiTypeAdapter.register(Dingdan.class,new DingdanItemViewProvider());
        /* 绑定  模拟加载数据，也可以稍后再加载，然后使用
         * adapter.notifyDataSetChanged() 刷新列表 */
        mRecyclerView.setAdapter(multiTypeAdapter);
        //测试绑定
        assertHasTheSameAdapter(mRecyclerView,multiTypeAdapter);
    }


    private void initDataAndRfTest() {
        list = new ArrayList<Dingdan>();
        list.add(new Dingdan("汽车保养","鲁 B 12345","1"));
        list.add(new Dingdan("综合订单","鲁 B 12345","1"));
        list.add(new Dingdan("安装","鲁 B 12345","1"));
        for (int i = 0; i < 4; i++) {
            list.add(new Dingdan("汽车保养","鲁 B 12345","0"));
            list.add(new Dingdan("综合订单","鲁 B 12345","0"));
            list.add(new Dingdan("安装","鲁 B 12345","0"));
        }
        items.clear();
        items.addAll(list);
        //测试绑定
        assertAllRegistered(multiTypeAdapter,items);
        multiTypeAdapter.notifyDataSetChanged();

    }
}