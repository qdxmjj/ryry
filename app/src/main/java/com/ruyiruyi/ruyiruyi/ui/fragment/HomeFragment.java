package com.ruyiruyi.ruyiruyi.ui.fragment;

import android.content.Context;
import android.content.Intent;
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

import com.bumptech.glide.Glide;
import com.ruyiruyi.ruyiruyi.R;
import com.ruyiruyi.ruyiruyi.db.DbConfig;
import com.ruyiruyi.ruyiruyi.db.model.Lunbo;
import com.ruyiruyi.ruyiruyi.db.model.User;
import com.ruyiruyi.ruyiruyi.ui.activity.CarFigureActivity;
import com.ruyiruyi.ruyiruyi.ui.activity.CityChooseActivity;
import com.ruyiruyi.ruyiruyi.ui.multiType.Function;
import com.ruyiruyi.ruyiruyi.ui.multiType.FunctionViewBinder;
import com.ruyiruyi.ruyiruyi.ui.multiType.Hometop;
import com.ruyiruyi.ruyiruyi.ui.multiType.HometopViewBinder;
import com.ruyiruyi.ruyiruyi.ui.multiType.OneEvent;
import com.ruyiruyi.ruyiruyi.ui.multiType.OneEventViewBinder;
import com.ruyiruyi.ruyiruyi.ui.multiType.ThreeEvent;
import com.ruyiruyi.ruyiruyi.ui.multiType.ThreeEventViewBinder;
import com.ruyiruyi.ruyiruyi.utils.RequestUtils;
import com.ruyiruyi.rylibrary.ui.viewpager.CustomBanner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.DbManager;
import org.xutils.common.Callback;
import org.xutils.ex.DbException;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import javax.security.auth.login.LoginException;

import me.drakeet.multitype.MultiTypeAdapter;

import static me.drakeet.multitype.MultiTypeAsserts.assertAllRegistered;
import static me.drakeet.multitype.MultiTypeAsserts.assertHasTheSameAdapter;

public class HomeFragment extends Fragment implements HometopViewBinder.OnHomeTopItemClickListener,FunctionViewBinder.OnFunctionItemClick{

    private static final String TAG = HomeFragment.class.getSimpleName();
    private CustomBanner<String> mBanner;
    private ImageView imageView;
    private RecyclerView listView;
    private List<Object> items = new ArrayList<>();
    private MultiTypeAdapter adapter;
    private List<Lunbo> lunbos;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_home,container,false);
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        listView = (RecyclerView) getView().findViewById(R.id.home_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        listView.setLayoutManager(linearLayoutManager);
        adapter = new MultiTypeAdapter(items);

        register();

        listView.setAdapter(adapter);
        assertHasTheSameAdapter(listView, adapter);
        lunbos = new ArrayList<>();



       // initdata();

        initdataFromService();



    }

    private void initdataFromService() {
        DbConfig dbConfig = new DbConfig();
        int id = dbConfig.getId();

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userId",id);
        } catch (JSONException e) {
        }
        lunbos.clear();
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL_TEST + "getAndroidHomeDate");
        params.addBodyParameter("reqJson",jsonObject.toString());
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess: " + result);
                try {
                    JSONObject jsonObject1 = new JSONObject(result);
                    JSONObject data = jsonObject1.getJSONObject("data");
                    JSONArray lunboList = data.getJSONArray("lunbo_infos");
                    for (int i = 0; i < lunboList.length(); i++) {
                        JSONObject object = lunboList.getJSONObject(i);
                        String contentImageUrl = object.getString("contentImageUrl");
                        String contentText = object.getString("contentText");
                        int id1 = object.getInt("id");
                        Lunbo lunbo = new Lunbo(id1, contentImageUrl, contentText);
                        lunbos.add(lunbo);
                    }
                    saveLunboInToDb();

                } catch (JSONException e) {

                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e(TAG, "onError: " );
            }

            @Override
            public void onCancelled(CancelledException cex) {
                Log.e(TAG, "onCancelled: ");
            }

            @Override
            public void onFinished() {
                Log.e(TAG, "onFinished:");
            }
        });
    }

    private void saveLunboInToDb() {
        DbConfig dbConfig = new DbConfig();
        DbManager db = dbConfig.getDbManager();
        try {
            db.saveOrUpdate(lunbos);
        } catch (DbException e) {

        }
        initdata();
    }


    private void register() {
        HometopViewBinder hometopViewBinder = new HometopViewBinder();
        hometopViewBinder.setListener(this);
        adapter.register(Hometop.class, hometopViewBinder);
        FunctionViewBinder functionViewBinder = new FunctionViewBinder();
        functionViewBinder.setListener(this);
        adapter.register(Function.class, functionViewBinder);
        adapter.register(ThreeEvent.class,new ThreeEventViewBinder());
        adapter.register(OneEvent.class,new OneEventViewBinder());
    }

    private void initdata() {
        DbConfig dbConfig = new DbConfig();
        User user = new User();
        user = dbConfig.getUser();
        List<Lunbo> lunboList = new ArrayList<>();
        lunboList = dbConfig.getLunbo();


        items.clear();
        ArrayList<String> images = new ArrayList<>();
        for (int i = 0; i < lunboList.size(); i++) {
            images.add(lunboList.get(i).getContentImageUrl());
        }
        if (!(user == null)){
            int firstAddCar1 = user.getFirstAddCar();
            if (firstAddCar1 == 0){
                items.add(new Hometop(images,"添加我的宝驹","邀请好友绑定车辆可免费洗车",1));
            }else {
                items.add(new Hometop(images,"宝马X6","飞一般的感觉",2));
            }
        }else {//未登陆
            items.add(new Hometop(images,"新人注册享好礼","注册享受价格1000元大礼包",0));
        }



        items.add(new Function());
        items.add(new ThreeEvent());
        items.add(new OneEvent());
        assertAllRegistered(adapter,items);
        adapter.notifyDataSetChanged();
    }

    public void setBean(ArrayList beans) {
        mBanner.setPages(new CustomBanner.ViewCreator<String>() {
            @Override
            public View createView(Context context, int position) {
                ImageView imageView = new ImageView(context);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                return imageView;
            }

            @Override
            public void updateUI(Context context, View view, int position, String entity) {
                Glide.with(context).load(entity).into((ImageView) view);
            }
        }, beans)
//                //设置指示器为普通指示器
//                .setIndicatorStyle(CustomBanner.IndicatorStyle.ORDINARY)
//                //设置两个点图片作为翻页指示器，不设置则没有指示器，可以根据自己需求自行配合自己的指示器,不需要圆点指示器可用不设
//                .setIndicatorRes(R.drawable.shape_point_select, R.drawable.shape_point_unselect)
//                //设置指示器的方向
//                .setIndicatorGravity(CustomBanner.IndicatorGravity.CENTER)
//                //设置指示器的指示点间隔
//                .setIndicatorInterval(20)
                //设置自动翻页
                .startTurning(5000);
    }

    @Override
    public void onCityLayoutClickListener() {
        startActivity(new Intent(getContext(), CityChooseActivity.class));
    }

    @Override
    public void onFunctionClickListener(int type) {
        if (type == 0){
            startActivity(new Intent(getContext(), CarFigureActivity.class));
        }else if (type==1){

        }else if (type == 2){

        }else if (type ==3){

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}