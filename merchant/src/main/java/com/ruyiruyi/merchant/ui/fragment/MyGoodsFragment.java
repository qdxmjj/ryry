package com.ruyiruyi.merchant.ui.fragment;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ruyiruyi.merchant.R;
import com.ruyiruyi.merchant.bean.GoodsItemBean;
import com.ruyiruyi.merchant.db.DbConfig;
import com.ruyiruyi.merchant.ui.multiType.GoodsItemProvider;
import com.ruyiruyi.merchant.utils.UtilsURL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import me.drakeet.multitype.MultiTypeAdapter;

import static me.drakeet.multitype.MultiTypeAsserts.assertAllRegistered;
import static me.drakeet.multitype.MultiTypeAsserts.assertHasTheSameAdapter;

public class MyGoodsFragment extends Fragment {
    public static String SALE_TYPE = "SALE_TYPE";
    private String sale_type;
    private RecyclerView mRlv;
    private MultiTypeAdapter multiTypeAdapter;
    private List<Object> items = new ArrayList<>();
    private String TAG = MyGoodsFragment.class.getSimpleName();
    private List<GoodsItemBean> itemBeanList;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    if (itemBeanList==null||itemBeanList.size()==0) {
                        items.clear();
                        ArrayList<GoodsItemBean> listaa = new ArrayList<>();
                        GoodsItemBean beanaa = new GoodsItemBean();
                        beanaa.setName("暂无数据");
                        listaa.add(beanaa);
                        for (int i = 0; i < listaa.size(); i++) {
                            items.add(listaa.get(i));
                        }
                        assertAllRegistered(multiTypeAdapter, items);
                        multiTypeAdapter.notifyDataSetChanged();
                    }else {
                        items.clear();
                        for (int i = 0; i < itemBeanList.size(); i++) {
                            items.add(itemBeanList.get(i));
                        }
                        assertAllRegistered(multiTypeAdapter, items);
                        multiTypeAdapter.notifyDataSetChanged();
                    }

                    break;
            }
        }
    };

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
        itemBeanList = new ArrayList<>();

        initView();
        initData();

    }

    private void initData() {
        String storeId = new DbConfig().getId() + "";
        JSONObject object = new JSONObject();
        switch (sale_type) {
            case "ONSALE":
                try {
                    object.put("storeId", storeId);
                    object.put("serviceTypeId", "");//空为查询所有数据
                    object.put("servicesId", "");
                    object.put("page", "1");
                    object.put("rows", "10");
                    object.put("stockStatus", "1");//上架下架状态 1 yes    2 no
                } catch (JSONException e) {
                }
                RequestParams params1 = new RequestParams(UtilsURL.REQUEST_URL + "getStockByCondition");
                params1.addBodyParameter("reqJson", object.toString());
                Log.e(TAG, "initData: 999 params.toString()" + params1.toString());
                x.http().post(params1, new Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        Log.e(TAG, "onSuccess:999 up  1 ");
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            JSONObject data = jsonObject.getJSONObject("data");
                            JSONArray rows = data.getJSONArray("rows");
                            int total = data.getInt("total");
                            for (int i = 0; i < rows.length(); i++) {
                                JSONObject one = (JSONObject) rows.get(i);
                                GoodsItemBean itemBean = new GoodsItemBean();
                                itemBean.setAmount(one.getInt("amount"));
                                itemBean.setId(one.getInt("id"));
                                itemBean.setImgUrl(one.getString("imgUrl"));
                                itemBean.setName(one.getString("name"));
                                itemBean.setPrice(one.getDouble("price"));
                                itemBean.setServiceId(one.getInt("serviceId"));
                                itemBean.setServiceTypeId(one.getInt("serviceTypeId"));
                                itemBean.setSoldNo(one.getInt("soldNo"));
                                itemBean.setStatus(one.getInt("status"));
                                itemBean.setStockTypeId(one.getInt("stockTypeId"));
                                itemBean.setStoreId(one.getInt("storeId"));
                                itemBean.setTime(one.getLong("time"));
                                Log.e(TAG, "onSuccess:999 1 itemBean.getName() = " + itemBean.getName());

                                itemBeanList.add(itemBean);
                            }
                            Log.e(TAG, "onSuccess:999 1 itemBeanList.size() = " + itemBeanList.size());
                            Message message = new Message();
                            message.what = 1;
                            mHandler.sendMessage(message);

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
                break;
            case "NOSALE":
                try {
                    object.put("storeId", storeId);
                    object.put("serviceTypeId", "");//空为查询所有数据
                    object.put("servicesId", "");
                    object.put("page", "1");
                    object.put("rows", "10");
                    object.put("stockStatus", "2");//上架下架状态 1 yes    2 no
                } catch (JSONException e) {
                }
                RequestParams params2 = new RequestParams(UtilsURL.REQUEST_URL + "getStockByCondition");
                params2.addBodyParameter("reqJson", object.toString());
                Log.e(TAG, "initData: 999 params2.toString()" + params2.toString());
                x.http().post(params2, new Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        Log.e(TAG, "onSuccess:999 up  2 ");
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            JSONObject data = jsonObject.getJSONObject("data");
                            JSONArray rows = data.getJSONArray("rows");
                            int total = data.getInt("total");
                            for (int i = 0; i < rows.length(); i++) {
                                JSONObject one = (JSONObject) rows.get(i);
                                GoodsItemBean itemBean = new GoodsItemBean();
                                itemBean.setAmount(one.getInt("amount"));
                                itemBean.setId(one.getInt("id"));
                                itemBean.setImgUrl(one.getString("imgUrl"));
                                itemBean.setName(one.getString("name"));
                                itemBean.setPrice(one.getDouble("price"));
                                itemBean.setServiceId(one.getInt("serviceId"));
                                itemBean.setServiceTypeId(one.getInt("serviceTypeId"));
                                itemBean.setSoldNo(one.getInt("soldNo"));
                                itemBean.setStatus(one.getInt("status"));
                                itemBean.setStockTypeId(one.getInt("stockTypeId"));
                                itemBean.setStoreId(one.getInt("storeId"));
                                itemBean.setTime(one.getLong("time"));
                                Log.e(TAG, "onSuccess:999 2 itemBean.getName() = " + itemBean.getName());

                                itemBeanList.add(itemBean);
                            }
                            Log.e(TAG, "onSuccess:999 2 itemBeanList.size() = " + itemBeanList.size());
                            Message message = new Message();
                            message.what = 1;
                            mHandler.sendMessage(message);

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
                break;
        }
    }

    private void initView() {
        mRlv = (RecyclerView) getView().findViewById(R.id.mygoods_rlv);

        LinearLayoutManager manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mRlv.setLayoutManager(manager);
        multiTypeAdapter = new MultiTypeAdapter(items);
        multiTypeAdapter.register(GoodsItemBean.class, new GoodsItemProvider());
        mRlv.setAdapter(multiTypeAdapter);
        assertHasTheSameAdapter(mRlv, multiTypeAdapter);
    }
}