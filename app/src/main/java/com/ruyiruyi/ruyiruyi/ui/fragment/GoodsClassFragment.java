package com.ruyiruyi.ruyiruyi.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.ruyiruyi.ruyiruyi.R;
import com.ruyiruyi.ruyiruyi.db.DbConfig;
import com.ruyiruyi.ruyiruyi.ui.activity.GoodsShopActivity;
import com.ruyiruyi.ruyiruyi.ui.fragment.base.RyBaseFragment;
import com.ruyiruyi.ruyiruyi.ui.activity.SearchActivity;
import com.ruyiruyi.ruyiruyi.ui.multiType.Left;
import com.ruyiruyi.ruyiruyi.ui.multiType.LeftViewBinder;
import com.ruyiruyi.ruyiruyi.ui.multiType.GoodsClass;
import com.ruyiruyi.ruyiruyi.ui.multiType.GoodsClassViewBinder;
import com.ruyiruyi.ruyiruyi.utils.RequestUtils;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import me.drakeet.multitype.MultiTypeAdapter;
import rx.functions.Action1;

import static me.drakeet.multitype.MultiTypeAsserts.assertAllRegistered;
import static me.drakeet.multitype.MultiTypeAsserts.assertHasTheSameAdapter;

public class GoodsClassFragment extends RyBaseFragment implements LeftViewBinder.OnBigClassItemClick ,GoodsClassViewBinder.OnClassItemClick {
    private static final String TAG = GoodsClassFragment.class.getSimpleName();
    private RecyclerView bigClassliserView;
    private RecyclerView classListView;
    private List<Object> bigClassItems = new ArrayList<>();
    private MultiTypeAdapter bigClassAdapter;
    public List<Left> leftLists;

    private List<Object> classItems = new ArrayList<>();
    private MultiTypeAdapter classAdapter;
    public List<GoodsClass> goodsClassList;
    public int serviceType = 2; //2汽车保养  3美容清洗  4安装改装  5轮胎服务
    private LinearLayout goodsSearchLayout;
    private int classId;
    private String className;
    private String classImage;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_goods_class,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        leftLists = new ArrayList<>();
        goodsClassList = new ArrayList<>();

        leftLists.add(new Left("汽车保养",true));
        leftLists.add(new Left("美容清洗",false));
        leftLists.add(new Left("安装改装",false));
        leftLists.add(new Left("轮胎服务",false));
        initView();

        initData();
        initClassData();

    }

    private void initClassData() {
       /* User user = new DbConfig().getUser();
        int userId = user.getId();*/
        int userId = new DbConfig(getContext()).getId();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userId",userId);
            jsonObject.put("serviceTypeId",serviceType);
        } catch (JSONException e) {

        }
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "serviceInfo/showServicesList");
        params.addBodyParameter("reqJson",jsonObject.toString());
        String token = new DbConfig(getContext()).getToken();
        params.addParameter("token",token);
        Log.e(TAG, "initClassData: " + params.toString());
        x.http().post(params, new Callback.CommonCallback<String>() {


            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess: " + result);
                JSONObject jsonObject1 = null;
                try {
                    jsonObject1 = new JSONObject(result);
                    String status = jsonObject1.getString("status");
                    String msg = jsonObject1.getString("msg");
                    if (status.equals("1")){
                        JSONArray data = jsonObject1.getJSONArray("data");
                        goodsClassList.clear();
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject object = data.getJSONObject(i);
                            classImage = object.getString("img");
                            className = object.getString("name");
                            classId = object.getInt("id");
                            goodsClassList.add(new GoodsClass(classId,className,classImage));
                        }
                        initClass();
                    } else if (status.equals("-999")) {
                        showUserTokenDialog("您的账号在其它设备登录,请重新登录");
                    }
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
        for (int i = 0; i < leftLists.size(); i++) {
            bigClassItems.add(leftLists.get(i));
        }
        assertAllRegistered(bigClassAdapter,bigClassItems);
        bigClassAdapter.notifyDataSetChanged();
    }

    private void initView() {
        bigClassliserView = ((RecyclerView) getView().findViewById(R.id.goods_big_class_listview));
        classListView = ((RecyclerView) getView().findViewById(R.id.goods_class_listview));
        goodsSearchLayout = (LinearLayout) getView().findViewById(R.id.goods_search_layout);


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

        //搜索商品
        RxViewAction.clickNoDouble(goodsSearchLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Intent intent = new Intent(getContext(), SearchActivity.class);
                        intent.putExtra(SearchActivity.TYPE,1);
                        startActivity(intent);
                    }
                });

    }

    private void classRegister() {
        GoodsClassViewBinder goodsClassViewBinder = new GoodsClassViewBinder(getContext());
        goodsClassViewBinder.setListener(this);
        classAdapter.register(GoodsClass.class, goodsClassViewBinder);
    }

    private void bigClassRegister() {
        LeftViewBinder leftViewBinder = new LeftViewBinder();
        leftViewBinder.setListener(this);
        bigClassAdapter.register(Left.class, leftViewBinder);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onLeftItemClikcListener(String className,String classId) {
        if(className.equals("汽车保养")){
            serviceType = 2;
        }else if (className.equals("美容清洗")){
            serviceType = 3;
        }else if (className.equals("安装改装")){
            serviceType = 4;
        }else if (className.equals("轮胎服务")){
            serviceType = 5;
        }
        for (int i = 0; i < leftLists.size(); i++) {
            if (leftLists.get(i).getBigClassName().equals(className)) {
                leftLists.get(i).setCheck(true);
            }else {
                leftLists.get(i).setCheck(false);
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
        intent.putExtra(GoodsShopActivity.FROMTYPE,0);
        startActivity(intent);
    }
}