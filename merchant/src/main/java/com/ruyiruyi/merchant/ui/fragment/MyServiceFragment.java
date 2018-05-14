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
import android.widget.TextView;

import com.ruyiruyi.merchant.R;
import com.ruyiruyi.merchant.bean.ServicesBean;
import com.ruyiruyi.merchant.db.DbConfig;
import com.ruyiruyi.merchant.ui.activity.MyServiceActivity;
import com.ruyiruyi.merchant.ui.multiType.ServiceItemProvider;
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

public class MyServiceFragment extends Fragment implements ServiceItemProvider.OnServiceItemClick {
    public static String SALE_TYPE = "SALE_TYPE";
    private String sale_type;
    private RecyclerView mRlv;
    private List<ServicesBean> servicesBean;
    private MultiTypeAdapter multiTypeAdapter;
    private List<Object> items = new ArrayList<>();
    private String TAG = MyServiceFragment.class.getSimpleName();
    private View parentView;
    public StartFragmentPasstoActivity listener;
    private List<String> checkedList = new ArrayList<>();

    public void setListener(StartFragmentPasstoActivity listener) {
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (parentView == null) {
            parentView = inflater.inflate(R.layout.myservice_list_fg, container, false);
        } else {
            ViewGroup viewGroup = (ViewGroup) parentView.getParent();
            if (viewGroup != null)
                viewGroup.removeView(parentView);
        }
        return parentView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Bundle bundle = getArguments();
        sale_type = bundle.getString(SALE_TYPE);
        Log.e(TAG, "onActivityCreated: " + sale_type);
        servicesBean = new ArrayList<>();

        initView();
        initData();

    }

    private void initData() {
        switch (sale_type) {
            case "QCBY"://id 2
                myRequestPostForDataBy("2");
                break;
            case "MRQX"://id 3
                myRequestPostForDataBy("3");
                break;
            case "AZ"://id 4
                myRequestPostForDataBy("4");
                break;
            case "LTFW"://id 5
                myRequestPostForDataBy("5");
                break;
        }

    }

    //封装的四个Fragment 请求 获取servicesBean 这个list ；
    private void myRequestPostForDataBy(final String s) {
        JSONObject jsonObject = new JSONObject();
        try {
            String storeId = new DbConfig().getId() + "";
            jsonObject.put("storeId", storeId);
            jsonObject.put("serviceTypeId", s);
        } catch (JSONException e) {
        }
        RequestParams params = new RequestParams(UtilsURL.REQUEST_URL + "getStoreServicesAndState");
        params.addBodyParameter("reqJson", jsonObject.toString());
        Log.e(TAG, "myRequestPostForDataBy bugs: params.toString()==>" + params.toString());
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess: bugs result = " + result);
                try {
                    JSONObject object = new JSONObject(result);
                    JSONArray data = object.getJSONArray("data");
                    String msg = object.getString("msg");
                    int status = object.getInt("status");
                    if (data != null && data.length() != 0) {
                        for (int i = 0; i < data.length(); i++) {
                            ServicesBean bean = new ServicesBean();
                            JSONObject obj = (JSONObject) data.get(i);
                            bean.setService_id(obj.getInt("id"));
                            bean.setIsChecked(Integer.parseInt(obj.getString("selectState")));
                            bean.setServiceInfo(obj.getString("name"));
                            servicesBean.add(bean);
                        }
                    }

                    if (servicesBean == null || servicesBean.size() == 0) {
                        Log.e(TAG, "initData: servicesBean 是空的？=00=》" + servicesBean);
                    } else {
                        updataAdapter();
                    }

                    //循环列出已选中的服务选项的id列表（String型list）
                    checkedList.clear();
                    for (int i = 0; i < servicesBean.size(); i++) {
                        if (servicesBean.get(i).getIsChecked() == 1) {
                            checkedList.add(servicesBean.get(i).getService_id() + "");
                        }
                    }

                    listener.startFragmentPasstoActivityListener(s, checkedList); //此时传递数据 原始
                    Log.e(TAG, "onSuccess: 000servicesBean.size()-->" + s + "<--" + servicesBean.size());
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

    private void updataAdapter() {
        items.clear();
        for (int i = 0; i < servicesBean.size(); i++) {
            items.add(servicesBean.get(i));
        }
        assertAllRegistered(multiTypeAdapter, items);
        multiTypeAdapter.notifyDataSetChanged();
    }

    private void initView() {
        mRlv = (RecyclerView) getView().findViewById(R.id.myservice_rlv);

        LinearLayoutManager manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mRlv.setLayoutManager(manager);
        multiTypeAdapter = new MultiTypeAdapter(items);
        ServiceItemProvider serviceItemProvider = new ServiceItemProvider();
        serviceItemProvider.setListener(this);// 绑定adapter-->fragment接口
        multiTypeAdapter.register(ServicesBean.class, serviceItemProvider);
        mRlv.setAdapter(multiTypeAdapter);
        assertHasTheSameAdapter(mRlv, multiTypeAdapter);
    }

    @Override
    public void onServiceItemClickListener(int id) {
        for (int i = 0; i < servicesBean.size(); i++) {
            if (servicesBean.get(i).getService_id() == id) {
                int isChecked = servicesBean.get(i).getIsChecked();
                if (isChecked == 0) {
                    servicesBean.get(i).setIsChecked(1);
                } else {
                    servicesBean.get(i).setIsChecked(0);
                }
            }
        }
        updataAdapter();
        listener.onServiceItemClickToActivityListener(id);//传递给Activity 后选
    }

    public interface StartFragmentPasstoActivity {
        void startFragmentPasstoActivityListener(String s, List<String> checkedList);//传至Activity 原始

        void onServiceItemClickToActivityListener(int id);//传至Activity 后选
    }
}