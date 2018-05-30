package com.ruyiruyi.ruyiruyi.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ruyiruyi.ruyiruyi.R;
import com.ruyiruyi.ruyiruyi.ui.activity.GoodsShopActivity;
import com.ruyiruyi.ruyiruyi.ui.fragment.base.RYBaseFragment;
import com.ruyiruyi.ruyiruyi.ui.multiType.BigClass;
import com.ruyiruyi.ruyiruyi.ui.multiType.BigClassViewBinder;
import com.ruyiruyi.ruyiruyi.ui.multiType.EvaluateImage;
import com.ruyiruyi.ruyiruyi.ui.multiType.EvaluateImageViewBinder;
import com.ruyiruyi.ruyiruyi.ui.multiType.GoodsClass;
import com.ruyiruyi.ruyiruyi.ui.multiType.GoodsClassViewBinder;

import java.util.ArrayList;
import java.util.List;

import me.drakeet.multitype.MultiTypeAdapter;

import static me.drakeet.multitype.MultiTypeAsserts.assertAllRegistered;
import static me.drakeet.multitype.MultiTypeAsserts.assertHasTheSameAdapter;

public class GoodsClassFragment extends RYBaseFragment implements BigClassViewBinder.OnBigClassItemClick ,GoodsClassViewBinder.OnClassItemClick {
    private RecyclerView bigClassliserView;
    private RecyclerView classListView;
    private List<Object> bigClassItems = new ArrayList<>();
    private MultiTypeAdapter bigClassAdapter;
    public List<BigClass> bigClassLists;

    private List<Object> classItems = new ArrayList<>();
    private MultiTypeAdapter classAdapter;
    public List<GoodsClass> goodsClassList;
    public int serviceType = 2; //2汽车保养  3美容清洗  4安装改装  5轮胎服务


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_goods_class,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        bigClassLists = new ArrayList<>();
        goodsClassList = new ArrayList<>();

        bigClassLists.add(new BigClass("汽车保养",true));
        bigClassLists.add(new BigClass("美容清洗",false));
        bigClassLists.add(new BigClass("安装改装",false));
        bigClassLists.add(new BigClass("轮胎服务",false));
        initView();
        initData();
        initClassData();

    }

    private void initClassData() {
        goodsClassList.clear();
        if (serviceType == 2){//2汽车保养  3美容清洗  4安装改装  5轮胎服务
            for (int i = 0; i < 20; i++) {
                goodsClassList.add(new GoodsClass(i,"防冻液" + i,""));
            }
        }else if (serviceType == 3){
            for (int i = 0; i < 20; i++) {
                goodsClassList.add(new GoodsClass(i,"美容" + i,""));
            }
        }else if (serviceType == 4){
            for (int i = 0; i < 20; i++) {
                goodsClassList.add(new GoodsClass(i,"安装" + i,""));
            }
        }else if (serviceType == 5){
            for (int i = 0; i < 20; i++) {
                goodsClassList.add(new GoodsClass(i,"轮胎" + i,""));
            }
        }
        initClass();
    }

    private void initClass() {
        classItems.clear();
        for (int i = 0; i < goodsClassList.size(); i++) {
            classItems.add(goodsClassList.get(i));
        }
        assertAllRegistered(classAdapter,classItems);
        classAdapter.notifyDataSetChanged();
    }

    private void initData() {
        bigClassItems.clear();
        for (int i = 0; i < bigClassLists.size(); i++) {
            bigClassItems.add(bigClassLists.get(i));
        }
        assertAllRegistered(bigClassAdapter,bigClassItems);
        bigClassAdapter.notifyDataSetChanged();
    }

    private void initView() {
        bigClassliserView = ((RecyclerView) getView().findViewById(R.id.goods_big_class_listview));
        classListView = ((RecyclerView) getView().findViewById(R.id.goods_class_listview));

        //服务大类的listview
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        bigClassliserView.setLayoutManager(linearLayoutManager);
        bigClassAdapter = new MultiTypeAdapter(bigClassItems);
        bigClassRegister();
        bigClassliserView.setAdapter(bigClassAdapter);
        assertHasTheSameAdapter(bigClassliserView, bigClassAdapter);
        //服务小类的listView
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3);
        classListView.setLayoutManager(gridLayoutManager);
        classAdapter = new MultiTypeAdapter(classItems);
        classRegister();
        classListView.setAdapter(classAdapter);
        assertHasTheSameAdapter(classListView, classAdapter);

    }

    private void classRegister() {
        GoodsClassViewBinder goodsClassViewBinder = new GoodsClassViewBinder();
        goodsClassViewBinder.setListener(this);
        classAdapter.register(GoodsClass.class, goodsClassViewBinder);
    }

    private void bigClassRegister() {
        BigClassViewBinder bigClassViewBinder = new BigClassViewBinder();
        bigClassViewBinder.setListener(this);
        bigClassAdapter.register(BigClass.class, bigClassViewBinder);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBigClassItemClikcListener(String className) {
        if(className.equals("汽车保养")){
            serviceType = 2;
        }else if (className.equals("美容清洗")){
            serviceType = 3;
        }else if (className.equals("安装改装")){
            serviceType = 4;
        }else if (className.equals("轮胎服务")){
            serviceType = 5;
        }
        for (int i = 0; i < bigClassLists.size(); i++) {
            if (bigClassLists.get(i).getBigClassName().equals(className)) {
                bigClassLists.get(i).setCheck(true);
            }else {
                bigClassLists.get(i).setCheck(false);
            }
        }
        initData();
        initClassData();
    }

    @Override
    public void onClassItemClikcListener(int classId, String className) {
        Intent intent = new Intent(getContext(), GoodsShopActivity.class);
        intent.putExtra(GoodsShopActivity.CLASS_ID,classId);
        intent.putExtra(GoodsShopActivity.CLASS_NAME,className);
        startActivity(intent);
    }
}