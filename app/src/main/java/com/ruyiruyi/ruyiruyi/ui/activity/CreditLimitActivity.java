package com.ruyiruyi.ruyiruyi.ui.activity;

import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.ruyiruyi.ruyiruyi.R;
import com.ruyiruyi.ruyiruyi.db.DbConfig;
import com.ruyiruyi.ruyiruyi.ui.activity.base.RyBaseActivity;
import com.ruyiruyi.ruyiruyi.ui.multiType.CreditLimit;
import com.ruyiruyi.ruyiruyi.ui.multiType.CreditLimitViewBinder;
import com.ruyiruyi.ruyiruyi.ui.multiType.EmptyBig;
import com.ruyiruyi.ruyiruyi.ui.multiType.EmptyBigViewBinder;
import com.ruyiruyi.ruyiruyi.ui.multiType.LoadMore;
import com.ruyiruyi.ruyiruyi.ui.multiType.LoadMoreViewBinder;
import com.ruyiruyi.ruyiruyi.utils.RequestUtils;
import com.ruyiruyi.rylibrary.cell.ActionBar;

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

public class CreditLimitActivity extends RyBaseActivity {
    private ActionBar actionBar;
    private RecyclerView listView;
    private SwipeRefreshLayout mSwipeLayout;
    private List<Object> items = new ArrayList<>();
    private MultiTypeAdapter adapter;
    public List<CreditLimit> creditLimitList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit_limit, R.id.my_action);
        actionBar = (ActionBar) findViewById(R.id.my_action);
        actionBar.setTitle("信用额度");
        actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(int var1) {
                switch ((var1)) {
                    case -1:
                        onBackPressed();
                        break;
                }
            }
        });
        creditLimitList = new ArrayList<>();


        initView();
        initData();
        initSwipeLayout();
    }

    private void initData() {
        requestFromServer();
    }

    private void requestFromServer() {
        creditLimitList.clear();

        JSONObject object = new JSONObject();
        try {
            object.put("userId", new DbConfig().getId());

        } catch (JSONException e) {
        }
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "userCarInfo/queryCarCreditInfo");
        params.addBodyParameter("reqJson", object.toString());
        params.addBodyParameter("token", new DbConfig().getToken());
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    int status = jsonObject.getInt("status");
                    String msg = jsonObject.getString("msg");
                    if (status == 1) {
                        JSONArray data = jsonObject.getJSONArray("data");
                        for (int i = 0; i < data.length(); i++) {
                            CreditLimit creditLimit = new CreditLimit();
                            JSONObject objBean = (JSONObject) data.get(i);
                            creditLimit.setCarImage(objBean.getString("logoUrl"));
                            creditLimit.setCarName(objBean.getString("carName"));
                            creditLimit.setCarNumber(objBean.getString("platNumber"));
                            creditLimit.setCreditLimit(objBean.getString("credit"));
                            creditLimit.setCreditLimitRemain(objBean.getString("remain"));
                            creditLimitList.add(creditLimit);
                        }
                    } else if (status == -999) {
                        showUserTokenDialog("您的账号在其它设备登录,请重新登录");

                    } else {
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                    }


                    //更新数据
                    updataData();

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


    /*
    * 更新数据
    * */
    private void updataData() {
        items.clear();
        if (creditLimitList == null || creditLimitList.size() == 0) {
            items.add(new EmptyBig());
        } else {
            for (int i = 0; i < creditLimitList.size(); i++) {
                items.add(creditLimitList.get(i));
            }
        }
        assertAllRegistered(adapter, items);
        adapter.notifyDataSetChanged();
    }

    //初始化下拉上拉
    private void initSwipeLayout() {
        mSwipeLayout.setColorSchemeResources(//下拉刷新圆圈颜色
                R.color.theme_primary,
                R.color.c5,
                R.color.c6,
                R.color.c7
        );
        mSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //加载最新数据并更新adapter数据

                myDownRefreshByServer();

                mSwipeLayout.setRefreshing(false);
            }
        });
    }

    /*
    * 下拉刷新
    * */
    private void myDownRefreshByServer() {
        requestFromServer();
    }


    private void initView() {
        listView = (RecyclerView) findViewById(R.id.credit_limit_listview);
        mSwipeLayout = (SwipeRefreshLayout) findViewById(R.id.credit_limit_swip);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        listView.setLayoutManager(linearLayoutManager);
        adapter = new MultiTypeAdapter(items);
        register();

        listView.setAdapter(adapter);
        assertHasTheSameAdapter(listView, adapter);
    }

    private void register() {
        adapter.register(CreditLimit.class, new CreditLimitViewBinder(this));
        adapter.register(EmptyBig.class, new EmptyBigViewBinder());
        adapter.register(LoadMore.class, new LoadMoreViewBinder());
    }
}
