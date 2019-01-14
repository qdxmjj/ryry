package com.ruyiruyi.ruyiruyi.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ruyiruyi.ruyiruyi.R;
import com.ruyiruyi.ruyiruyi.db.DbConfig;
import com.ruyiruyi.ruyiruyi.ui.activity.base.RyBaseActivity;
import com.ruyiruyi.ruyiruyi.ui.multiType.ExchangeGoodsOrder;
import com.ruyiruyi.ruyiruyi.ui.multiType.WuLiuTop;
import com.ruyiruyi.ruyiruyi.ui.multiType.WuLiuTopViewBinder;
import com.ruyiruyi.ruyiruyi.ui.multiType.WuliuItem;
import com.ruyiruyi.ruyiruyi.ui.multiType.WuliuItemViewBinder;
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

public class WuLiuActivity extends RyBaseActivity {
    private static final String TAG = WuLiuActivity.class.getSimpleName();
    private ActionBar actionBar;
    private String goodsName;
    private String goodsImage;
    private String orderNo;
/*    private ImageView goodsImageView;
    private TextView goodsNameView;
    private TextView wuliuNoView;
    private TextView wuliuPhoneView;*/
    public String wuliuNo;
    public String wuliuName;
    public String wuliuPhone;
    private RecyclerView listView;
    private List<Object> items = new ArrayList<>();
    private MultiTypeAdapter adapter;
    public List<WuliuItem> wuliuList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wu_liu);
        actionBar = (ActionBar) findViewById(R.id.my_action);
        actionBar.setTitle("订单列表");
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
        wuliuList = new ArrayList<>();

        Intent intent = getIntent();
        goodsName = intent.getStringExtra("GOODS_NAME");
        goodsImage = intent.getStringExtra("GOODS_IMAGE");
        orderNo = intent.getStringExtra("ORDER_NO");

        initView();
        initDataFromService();
    }

    private void initDataFromService() {
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL_JIFEN + "express");
        String token = new DbConfig(this).getToken();
        params.addParameter("orderNo", orderNo);
        params.addParameter("token", token);
        Log.e(TAG, "initDataFromService:--- " + params);
        x.http().get(params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess:---- " + result);
                JSONObject jsonObject1 = null;
                try {
                    jsonObject1 = new JSONObject(result);
                    String state = jsonObject1.getString("state");        //-1单号或代码错误  0 暂无轨迹 1快递收件 2在途中 3签收 4问题件
                    Log.e(TAG, "onSuccess: -" + state);
                    String msg = jsonObject1.getString("msg");
                    if (state.equals("1") || state.equals("2") || state.equals("3") || state.equals("4")) {
                        wuliuNo = jsonObject1.getString("no");
                        wuliuName = jsonObject1.getString("name");
                        wuliuPhone = jsonObject1.getString("phone");
                        Log.e(TAG, "onSuccess: --" + wuliuName);

                        wuliuList.clear();
                        JSONArray list = jsonObject1.getJSONArray("list");
                        for (int i = 0; i < list.length(); i++) {
                            JSONObject object = list.getJSONObject(i);
                            String time = object.getString("time");
                            String content = object.getString("content");
                            if ( i == 0){
                                wuliuList.add(new WuliuItem(time,content,false,true));  //第一条没上线
                            }else if (i == list.length()-1){
                                wuliuList.add(new WuliuItem(time,content,true,false));  //最后一条没下线
                            }else {
                                wuliuList.add(new WuliuItem(time,content,true,true));
                            }

                        }


                        initdata();
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

    private void initdata() {
  /*      wuliuNoView.setText(wuliuName+"  " + wuliuNo);
        wuliuPhoneView.setText("官方电话:" +  wuliuPhone);*/

        items.add(new WuLiuTop(goodsImage,goodsName,wuliuName,wuliuNo,wuliuPhone));

        for (int i = 0; i < wuliuList.size(); i++) {
            items.add(wuliuList.get(i));
        }

        assertAllRegistered(adapter, items);
        adapter.notifyDataSetChanged();
    }

    private void initView() {
     /*   goodsImageView = (ImageView) findViewById(R.id.goods_image_view);
        goodsNameView = (TextView) findViewById(R.id.goods_name_view);
        wuliuNoView = (TextView) findViewById(R.id.wuliu_no_view);
        wuliuPhoneView = (TextView) findViewById(R.id.wuliu_phone_view);*/
        listView = (RecyclerView) findViewById(R.id.wuliu_listview);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        listView.setLayoutManager(linearLayoutManager);
        adapter = new MultiTypeAdapter(items);
        register();

        listView.setAdapter(adapter);
        assertHasTheSameAdapter(listView, adapter);


       /* Glide.with(this).load(goodsImage).into(goodsImageView);
        goodsNameView.setText(goodsName);*/
    }

    private void register() {
        adapter.register(WuliuItem.class,new WuliuItemViewBinder());

        adapter.register(WuLiuTop.class,new WuLiuTopViewBinder(this));
    }
}
