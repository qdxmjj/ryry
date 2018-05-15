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
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ruyiruyi.merchant.R;
import com.ruyiruyi.merchant.db.DbConfig;
import com.ruyiruyi.merchant.db.model.User;
import com.ruyiruyi.merchant.ui.multiType.DianpuItemViewProvider;
import com.ruyiruyi.merchant.ui.multiType.listener.OnMyItemTouchListener;
import com.ruyiruyi.merchant.ui.multiType.modle.Dianpu;
import com.ruyiruyi.rylibrary.utils.glide.GlideCircleTransform;

import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import me.drakeet.multitype.MultiTypeAdapter;

import static me.drakeet.multitype.MultiTypeAsserts.assertAllRegistered;
import static me.drakeet.multitype.MultiTypeAsserts.assertHasTheSameAdapter;


public class DianpuFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private ImageView storeImage;
    private TextView storeName;
    private MultiTypeAdapter multiTypeAdapter;
    private List<Object> items = new ArrayList<>();//Object!!!!!!!!!!
    private List<Dianpu> list;
    private String TAG = DianpuFragment.class.getSimpleName();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dianpu_fg, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mRecyclerView = (RecyclerView) getView().findViewById(R.id.dianpu_rlv);
        LinearLayoutManager manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(manager);
        multiTypeAdapter = new MultiTypeAdapter(items);
        multiTypeAdapter.register(Dianpu.class, new DianpuItemViewProvider());
        mRecyclerView.setAdapter(multiTypeAdapter);
        assertHasTheSameAdapter(mRecyclerView, multiTypeAdapter);
        initDataAndRfTest();

        mRecyclerView.addOnItemTouchListener(new OnMyItemTouchListener(mRecyclerView) {
            @Override
            public void onItemClick(RecyclerView.ViewHolder vh) {
                //操作
                Log.e(TAG, "Recycler-->onItemClick: " + "AdapterPosition:" + vh.getAdapterPosition());
                Toast.makeText(getActivity(), "AdapterPosition:" + vh.getAdapterPosition(), Toast.LENGTH_SHORT).show();
            }
        });

        initView();
    }

    private void initView() {
        storeImage = getView().findViewById(R.id.img_user);
        storeName = getView().findViewById(R.id.tv_username);
        //头像 店铺名称
        DbConfig config = new DbConfig();
        User user = config.getUser();
        storeName.setText(user.getStoreName());
        Glide.with(this).load(user.getStoreImgUrl()).
                transform(new GlideCircleTransform(getContext()))
                .into(storeImage);
    }

    private void initDataAndRfTest() {
        list = new ArrayList<Dianpu>();
        for (int i = 0; i < 3; i++) {
            list.add(new Dianpu("汽车保养", "07-08", "+800.00", "已完成"));
            list.add(new Dianpu("美容清洗", "07-08", "+8000.00", "已完成"));
            list.add(new Dianpu("安装", "07-08", "+6000.00", "已完成"));
            list.add(new Dianpu("安装", "07-08", "+6600.00", "已完成"));
        }
        items.clear();
        items.addAll(list);
        assertAllRegistered(multiTypeAdapter, items);
        multiTypeAdapter.notifyDataSetChanged();

    }
}