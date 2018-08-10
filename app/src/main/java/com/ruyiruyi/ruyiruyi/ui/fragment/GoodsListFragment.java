package com.ruyiruyi.ruyiruyi.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ruyiruyi.ruyiruyi.R;
import com.ruyiruyi.ruyiruyi.db.DbConfig;
import com.ruyiruyi.ruyiruyi.ui.activity.GoodsActivity;
import com.ruyiruyi.ruyiruyi.ui.fragment.base.RyBaseFragment;
import com.ruyiruyi.ruyiruyi.ui.multiType.GoodsHorizontal;
import com.ruyiruyi.ruyiruyi.ui.multiType.GoodsItem;
import com.ruyiruyi.ruyiruyi.ui.multiType.GoodsItemViewBinder;
import com.ruyiruyi.ruyiruyi.ui.multiType.GoodsVertical;
import com.ruyiruyi.ruyiruyi.utils.RequestUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import me.drakeet.multitype.MultiTypeAdapter;

import static me.drakeet.multitype.MultiTypeAsserts.assertAllRegistered;
import static me.drakeet.multitype.MultiTypeAsserts.assertHasTheSameAdapter;

public class GoodsListFragment extends RyBaseFragment implements GoodsItemViewBinder.OnGoodsItemClick {
    private static final String TAG = GoodsListFragment.class.getSimpleName();
    public static final int GOODS_FRAGMENT_RESULT = 2;
    public static String GOODS_CLASS_ID = "GOODS_CLASS_ID";
    public static String STORE_ID = "STORE_ID";
    public static String SHOP_SERVICE_TYPE; //MRQX  QCBY  GZ  LTFW
    private String shopServiceType;
    private TextView text;
    private RecyclerView listView;
    private List<Object> items = new ArrayList<>();
    private MultiTypeAdapter adapter;
    public List<GoodsItem> goodsItemList;
    private int storeId;
    private List<GoodsVertical> goodslist;
    private double allprice;
    public OnGoodsListSend listener;

    public void setListener(OnGoodsListSend listener) {
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_gooods_list, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle arguments = getArguments();
        shopServiceType = arguments.getString(SHOP_SERVICE_TYPE);
        storeId = arguments.getInt(STORE_ID);
        Log.e(TAG, "onActivityCreated: -" + shopServiceType);
        goodsItemList = new ArrayList<>();

        goodslist = new ArrayList<>();

        // createData();


        initView();
        initDataFromService();

        // initData();

    }

    private void initDataFromService() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("storeId", storeId);
        } catch (JSONException e) {
        }
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "getStoreAddedServices");
        params.addBodyParameter("reqJson", jsonObject.toString());
        String token = new DbConfig(getContext()).getToken();
        params.addParameter("token", token);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    Log.e(TAG, "onSuccess: " + result);
                    JSONObject jsonObject1 = new JSONObject(result);
                    String status = jsonObject1.getString("status");
                    String msg = jsonObject1.getString("msg");
                    List<GoodsHorizontal> goodsHorizontalList = new ArrayList<GoodsHorizontal>();
                    if (status.equals("1")) {
                        JSONObject data = jsonObject1.getJSONObject("data");
                        JSONArray object = null; //MRQX  QCBY  GZ  LTFW
                        if (shopServiceType.equals("MRQX")) {
                            object = data.getJSONArray("美容清洗");
                        } else if (shopServiceType.equals("QCBY")) {
                            object = data.getJSONArray("汽车保养");
                        } else if (shopServiceType.equals("GZ")) {
                            object = data.getJSONArray("安装");
                        } else if (shopServiceType.equals("LTFW")) {
                            object = data.getJSONArray("轮胎服务");
                        }
                        for (int i = 0; i < object.length(); i++) {
                            JSONObject objectJSONObject = object.getJSONObject(i);
                            String serviceId = objectJSONObject.getString("serviceId");
                            int serviceIdInt = Integer.parseInt(serviceId);
                            String serviceName = objectJSONObject.getString("serviceName");
                            String serviceTypeId = objectJSONObject.getString("serviceTypeId");
                            String serviceTypeName = objectJSONObject.getString("serviceTypeName");
                            goodsItemList.add(new GoodsItem(serviceIdInt, serviceName, false));
                        }
                        initData();
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


    private void initData() {
        items.clear();
        for (int i = 0; i < goodsItemList.size(); i++) {
            items.add(goodsItemList.get(i));
        }
        assertAllRegistered(adapter, items);
        adapter.notifyDataSetChanged();
    }

    private void initView() {
        listView = (RecyclerView) getView().findViewById(R.id.goods_listview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        listView.setLayoutManager(linearLayoutManager);
        adapter = new MultiTypeAdapter(items);
        register();

        listView.setAdapter(adapter);
        assertHasTheSameAdapter(listView, adapter);

    }

    private void register() {
        GoodsItemViewBinder goodsItemViewBinder = new GoodsItemViewBinder(getContext());
        goodsItemViewBinder.setListener(this);
        adapter.register(GoodsItem.class, goodsItemViewBinder);
    }

    @Override
    public void onGoodsItemClickListenner(int goodsClassId, List<GoodsHorizontal> goodsHorizontalList) {

        Intent intent = new Intent(getContext(), GoodsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(GOODS_CLASS_ID, goodsClassId);
        bundle.putSerializable("GOODSLIST", (Serializable) goodsHorizontalList);
        intent.putExtras(bundle);
        startActivityForResult(intent, GOODS_FRAGMENT_RESULT);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == GOODS_FRAGMENT_RESULT) {
            Bundle bundle = data.getExtras();
            int classId = bundle.getInt(GOODS_CLASS_ID);
            goodslist.clear();
            goodslist = ((List<GoodsVertical>) bundle.getSerializable("GOODSLIST"));
            allprice = bundle.getDouble("ALLPRICE");
            Log.e(TAG, "onActivityResult: ---++++-----" + goodslist.size());
            List<GoodsHorizontal> goodsHorizontalList = new ArrayList<>();
            for (int i = 0; i < goodslist.size(); i++) {
                GoodsHorizontal goodsHorizontal = new GoodsHorizontal();
                goodsHorizontal.setGoodsName(goodslist.get(i).getGoodsName());
                goodsHorizontal.setGoodsImage(goodslist.get(i).getGoodsImage());
                Log.e(TAG, "onActivityResult: *******" + goodslist.get(i).getGoodsId());
                goodsHorizontal.setGoodsId(goodslist.get(i).getGoodsId());
                goodsHorizontal.setGoodsCount(goodslist.get(i).getGoodsAmount());
                goodsHorizontal.setGoodsPrice(goodslist.get(i).getGoodsPrice());
                goodsHorizontal.setCurrentCount(goodslist.get(i).getCurrentGoodsAmount());
                goodsHorizontal.setGoodsClassId(goodslist.get(i).getGoodsClassId());
                goodsHorizontal.setServiceTypeId(goodslist.get(i).getServiceTypeId());
                goodsHorizontalList.add(goodsHorizontal);
            }
            if (goodslist.size() > 0) {
                for (int i = 0; i < goodsItemList.size(); i++) {
                    if (goodsItemList.get(i).getGoodsClassId() == classId) {
                        goodsItemList.get(i).setGoodsList(goodsHorizontalList);
                        goodsItemList.get(i).setChooseGood(true);
                        goodsItemList.get(i).setPrice(allprice + "");
                    }
                }
            } else {
                for (int i = 0; i < goodsItemList.size(); i++) {
                    if (goodsItemList.get(i).getGoodsClassId() == classId) {
                        goodsItemList.get(i).setGoodsList(goodsHorizontalList);
                        goodsItemList.get(i).setChooseGood(false);
                    }
                }
            }
            listener.onGoodsListSend(classId, goodsHorizontalList);
            initData();


        }

    }

    public interface OnGoodsListSend {
        void onGoodsListSend(int classId, List<GoodsHorizontal> list);
    }
}