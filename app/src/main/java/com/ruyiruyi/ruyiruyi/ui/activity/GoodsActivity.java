package com.ruyiruyi.ruyiruyi.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.ruyiruyi.ruyiruyi.R;
import com.ruyiruyi.ruyiruyi.db.DbConfig;
import com.ruyiruyi.ruyiruyi.ui.fragment.GoodsListFragment;
import com.ruyiruyi.ruyiruyi.ui.multiType.GoodsVertical;
import com.ruyiruyi.ruyiruyi.ui.multiType.GoodsVerticalViewBinder;
import com.ruyiruyi.ruyiruyi.utils.RequestUtils;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;
import com.ruyiruyi.rylibrary.base.BaseActivity;
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
import rx.functions.Action1;

import static me.drakeet.multitype.MultiTypeAsserts.assertAllRegistered;
import static me.drakeet.multitype.MultiTypeAsserts.assertHasTheSameAdapter;

public class GoodsActivity extends BaseActivity {
    private static final String TAG = GoodsActivity.class.getSimpleName();
    private ActionBar actionBar;
    private RecyclerView listView;
    private List<Object> items = new ArrayList<>();
    private MultiTypeAdapter adapter;
    private int goodsClassId;
    private TextView buyButton;
    public List<GoodsVertical> goodsVerticalList ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods,R.id.my_action);
        actionBar = (ActionBar) findViewById(R.id.my_action);
        actionBar.setTitle("商品列表");;
        actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick(){
            @Override
            public void onItemClick(int var1) {
                switch ((var1)){
                    case -1:
                        onBackPressed();
                        break;
                }
            }
        });
        Intent intent = getIntent();
        goodsClassId = intent.getIntExtra(GoodsListFragment.GOODS_CLASS_ID,0);

        goodsVerticalList = new ArrayList<>();
        initView();
        initDataFromService();
       // initData();
    }

    private void initDataFromService() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("storeId","");
            jsonObject.put("serviceTypeId","");
            jsonObject.put("servicesId",goodsClassId);
            jsonObject.put("stockStatus","");
        } catch (JSONException e) {
        }
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "getStockByCondition");
        params.addBodyParameter("reqJson",jsonObject.toString());
        String token = new DbConfig().getToken();
        params.addParameter("token",token);
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
                        goodsVerticalList.clear();
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject object = data.getJSONObject(i);
                            int id = object.getInt("id");
                            String imgUrl = object.getString("imgUrl");
                            String name = object.getString("name");
                            int amount = object.getInt("amount");
                            long price = object.getLong("price");
                            GoodsVertical goodsVertical = new GoodsVertical(id, imgUrl, name, price +"", amount);
                            goodsVerticalList.add(goodsVertical);
                        }
                        initData();
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
        for (int i = 0; i < goodsVerticalList.size(); i++) {
            items.add(goodsVerticalList.get(i));
        }
        assertAllRegistered(adapter,items);
        adapter.notifyDataSetChanged();
    }

    private void initView() {
        listView = (RecyclerView) findViewById(R.id.goods_listview);

        buyButton = (TextView) findViewById(R.id.tire_buy_button);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        listView.setLayoutManager(linearLayoutManager);
        adapter = new MultiTypeAdapter(items);
        register();

        listView.setAdapter(adapter);
        assertHasTheSameAdapter(listView, adapter);
        RxViewAction.clickNoDouble(buyButton)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Intent intent = new Intent();
                        setResult(GoodsListFragment.GOODS_FRAGMENT_RESULT,intent);
                        finish();
                    }
                });
    }

    private void register() {
        GoodsVerticalViewBinder goodsVerticalViewBinder = new GoodsVerticalViewBinder(this);
        adapter.register(GoodsVertical.class, goodsVerticalViewBinder);
    }
}
