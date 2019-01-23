package com.ruyiruyi.ruyiruyi.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ruyiruyi.ruyiruyi.R;
import com.ruyiruyi.ruyiruyi.db.DbConfig;
import com.ruyiruyi.ruyiruyi.ui.activity.base.RyBaseActivity;
import com.ruyiruyi.ruyiruyi.ui.multiType.ItemNullProvider;
import com.ruyiruyi.ruyiruyi.ui.multiType.MyAddress;
import com.ruyiruyi.ruyiruyi.ui.multiType.MyAddressViewBinder;
import com.ruyiruyi.ruyiruyi.ui.multiType.NoAddressBean;
import com.ruyiruyi.ruyiruyi.ui.multiType.NoAddressBeanViewBinder;
import com.ruyiruyi.ruyiruyi.ui.multiType.bean.ItemNullBean;
import com.ruyiruyi.ruyiruyi.utils.RequestUtils;
import com.ruyiruyi.rylibrary.cell.ActionBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import me.drakeet.multitype.MultiTypeAdapter;

import static me.drakeet.multitype.MultiTypeAsserts.assertAllRegistered;
import static me.drakeet.multitype.MultiTypeAsserts.assertHasTheSameAdapter;

public class MyAddressActivity extends RyBaseActivity implements MyAddressViewBinder.ForRefreshAddress {
    private final String TAG = MyAddressActivity.class.getSimpleName();
    private ActionBar actionBar;
    private boolean firstIn = true;

    private RecyclerView mRecyclerView;
    private MultiTypeAdapter multiTypeAdapter;
    private List<Object> items = new ArrayList<>();
    private List<MyAddress> orderBeanList;
    private boolean canChoose;
    private int addressId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_address);
        actionBar = (ActionBar) findViewById(R.id.acbar);
        actionBar.setTitle("我的地址");
        actionBar.setRightImage(R.drawable.ic_add1);
        actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(int var1) {
                switch ((var1)) {
                    case -1:
                        onBackPressed();
                        break;
                    case -2:
                        //添加我的地址
                        Intent intent = new Intent(getApplicationContext(), AddAddressActivity.class);
                        intent.putExtra("whereIn", "listAdd");
                        startActivity(intent);
                        break;
                }
            }
        });

        Intent intent = getIntent();
        canChoose = intent.getBooleanExtra("canChoose", false);
        addressId = intent.getIntExtra("addressId", -1);

        initView();
        initData();

    }

    private void initData() {
        orderBeanList = new ArrayList<>();
        orderBeanList.clear();
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL_JIFEN + "score/address");
        params.addBodyParameter("userId", new DbConfig(getApplicationContext()).getId() + "");
        Log.e(TAG, "initData:  params.toString() = " + params.toString());
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    Log.e(TAG, "onSuccess: result = " + result);
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray data = jsonObject.getJSONArray("data");
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject bean = (JSONObject) data.get(i);
                        MyAddress address = new MyAddress();
                        address.setId(bean.getInt("id"));
                        address.setName(bean.getString("name"));
                        address.setPhone(bean.getString("phone"));
                        address.setAddress(bean.getString("address"));
                        address.setSheng(bean.getString("province"));
                        address.setShi(bean.getString("city"));
                        address.setQu(bean.getString("county"));
                        address.setIsDefault(Integer.parseInt(bean.getString("isDefault")));
                        orderBeanList.add(address);
                    }
                    updataData();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                updataNetError();
                Toast.makeText(MyAddressActivity.this, "网络异常，请检查网络", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private void initFackData() {
        orderBeanList = new ArrayList<>();
        for (int i = 0; i < 66; i++) {
            MyAddress bean = new MyAddress();
            bean.setName("小马驾驾" + i);
            bean.setPhone("18888888888");
            bean.setId(i);
            bean.setAddress("山东省 青岛市 城阳区 天安数码城23号楼1楼青岛小马驾驾信息科技有限公司");
            bean.setIsDefault(i == 0 ? 1 : 0);
            orderBeanList.add(bean);
        }
        updataData();

        /*updataNetError()*/

    }


    private void updataData() {
        items.clear();
        if (orderBeanList == null || orderBeanList.size() == 0) {
            items.add(new NoAddressBean());
        } else {
            for (int i = 0; i < orderBeanList.size(); i++) {
                items.add(orderBeanList.get(i));
            }
        }
        assertAllRegistered(multiTypeAdapter, items);
        multiTypeAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {//刷新
        super.onResume();

        if (!firstIn) {
            initData();
        }
        firstIn = false;
        Log.e(TAG, "onResume: ");
    }

    private void updataNetError() {
        items.clear();
        items.add(new ItemNullBean(R.drawable.ic_net_error));
        assertAllRegistered(multiTypeAdapter, items);
        multiTypeAdapter.notifyDataSetChanged();

    }

    private void initView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.rlv);

        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(manager);
        multiTypeAdapter = new MultiTypeAdapter(items);
        /* 注册 */
        MyAddressViewBinder provider = new MyAddressViewBinder(getApplicationContext());
        provider.setListener(this);
        multiTypeAdapter.register(MyAddress.class, provider);
        multiTypeAdapter.register(NoAddressBean.class, new NoAddressBeanViewBinder(getApplicationContext()));
        multiTypeAdapter.register(ItemNullBean.class, new ItemNullProvider());
        mRecyclerView.setAdapter(multiTypeAdapter);
        //测试绑定
        assertHasTheSameAdapter(mRecyclerView, multiTypeAdapter);

    }

    @Override
    public void gotoRefresh(int id) {
        if (id == addressId) {
            Toast.makeText(this, "您不可删除当前订单所选地址!", Toast.LENGTH_SHORT).show();
        } else {
            showDeleteDialog(id);
        }
    }

    /*
    * 条目选择
    * */
    @Override
    public void gotoChoose(MyAddress myAddress) {
        if (!canChoose) {//不可选择时返回
            return;
        }
        Intent data = new Intent();
        data.putExtra("id", myAddress.getId());
        data.putExtra("name", myAddress.getName());
        data.putExtra("phone", myAddress.getPhone());
        data.putExtra("sheng", myAddress.getSheng());
        data.putExtra("shi", myAddress.getShi());
        data.putExtra("xian", myAddress.getQu());
        data.putExtra("addressTxt", myAddress.getAddress().replace(myAddress.getSheng() + myAddress.getShi() + myAddress.getQu(), ""));
        data.putExtra("isDefault", myAddress.getIsDefault());
        MyAddressActivity.this.setResult(RESULT_OK, data);
        finish();
    }


    private void showDeleteDialog(final int id) {
        AlertDialog dialog = new AlertDialog.Builder(MyAddressActivity.this).create();
        View dialogView = LayoutInflater.from(MyAddressActivity.this).inflate(R.layout.dialog_error, null);
        TextView error_text = (TextView) dialogView.findViewById(R.id.error_text);
        error_text.setText("确认删除此地址吗?");
        dialog.setTitle("如意如驿");
        dialog.setIcon(R.mipmap.ic_logo);
        dialog.setView(dialogView);
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "确 定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //删除地址
                RequestParams params = new RequestParams(RequestUtils.REQUEST_URL_JIFEN + "score/address");
                params.addBodyParameter("id", id + "");
                Log.e(TAG, "onClick: params.toString() = " + params.toString());
                x.http().request(HttpMethod.DELETE, params, new Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        Log.e(TAG, "onSuccess: result = " + result);
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            int status = jsonObject.getInt("status");
                            String msg = jsonObject.getString("msg");
                            if (status == 1) {
                                //刷新
                                onResume();
                            }
                            Toast.makeText(MyAddressActivity.this, msg, Toast.LENGTH_SHORT).show();

                        } catch (JSONException e) {
                            e.printStackTrace();
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
        });
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取 消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dialog.show();
        //设置按钮颜色
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.theme_primary));
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.theme_primary));

    }


}
