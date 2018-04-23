package com.ruyiruyi.ruyiruyi.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.ruyiruyi.ruyiruyi.R;
import com.ruyiruyi.ruyiruyi.db.DbConfig;
import com.ruyiruyi.ruyiruyi.ui.listener.OnFigureItemInterface;
import com.ruyiruyi.ruyiruyi.ui.multiType.TireFigure;
import com.ruyiruyi.ruyiruyi.ui.multiType.TireFigureViewBinder;
import com.ruyiruyi.ruyiruyi.ui.multiType.TireRank;
import com.ruyiruyi.ruyiruyi.ui.multiType.TitleStr;
import com.ruyiruyi.ruyiruyi.ui.multiType.TitleStrViewBinder;
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

public class CarFigureActivity extends BaseActivity implements OnFigureItemInterface {
    private static final String TAG = CarFigureActivity.class.getSimpleName();
    private ActionBar actionBar;
    private RecyclerView listView;
    private TextView nextButton;
    private List<Object> items = new ArrayList<>();
    private MultiTypeAdapter adapter;
    private List<TireFigure> tireFigureList ;
    private String tiresize;
    private String fontrearflag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_figure,R.id.my_action);
        actionBar = (ActionBar) findViewById(R.id.my_action);
        actionBar.setTitle("选择花纹");;
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
        tireFigureList = new ArrayList<>();

        Intent intent = getIntent();
        tiresize = intent.getStringExtra("TIRESIZE");
        fontrearflag = intent.getStringExtra("FONTREARFLAG");
        //getData();
        getDataFromService();

        initView();

    }

    private void getDataFromService() {
        tireFigureList.clear();
        int uesrId = new DbConfig().getId();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("shoeSize",tiresize);
            jsonObject.put("userId",uesrId);
        } catch (JSONException e) {

        }
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "getShoeBySize");
        params.addBodyParameter("reqJson",jsonObject.toString());
        String token = new DbConfig().getToken();
        params.addParameter("token",token);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess:------ " + result );
                JSONObject jsonObject1 = null;
                try {
                    jsonObject1 = new JSONObject(result);
                    String status = jsonObject1.getString("status");
                    String msg = jsonObject1.getString("msg");
                    if (status.equals("1")){
                        JSONArray data = jsonObject1.getJSONArray("data");


                        for (int i = 0; i < data.length(); i++) {
                            String description = data.getJSONObject(i).getString("description");
                            String imgLeftUrl = data.getJSONObject(i).getString("imgLeftUrl");
                            String imgMiddleUrl = data.getJSONObject(i).getString("imgMiddleUrl");
                            String imgRightUrl = data.getJSONObject(i).getString("imgRightUrl");
                            String shoeFlgureName = data.getJSONObject(i).getString("shoeFlgureName");
                            JSONArray shoeSpeedLoadResultList = data.getJSONObject(i).getJSONArray("shoeSpeedLoadResultList");
                            List<TireRank> tireRankList = new ArrayList<TireRank>();
                            for (int j = 0; j < shoeSpeedLoadResultList.length(); j++) {
                                String price = shoeSpeedLoadResultList.getJSONObject(j).getString("price");
                                String shoeId = shoeSpeedLoadResultList.getJSONObject(j).getString("shoeId");
                                int shoeIdInt = Integer.parseInt(shoeId);
                                String speedLoadStr = shoeSpeedLoadResultList.getJSONObject(j).getString("speedLoadStr");
                                tireRankList.add(new TireRank(shoeIdInt,speedLoadStr,shoeFlgureName,false,price));
                            }
                            tireFigureList.add(new TireFigure(false,0,shoeFlgureName,imgLeftUrl,imgMiddleUrl,imgRightUrl,description,tireRankList));
                        }
                        Log.e(TAG, "onSuccess: " + tireFigureList.size());
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

    private void getData() {
        tireFigureList = new ArrayList<>();
        tireFigureList.clear();
        List<TireRank> tireRankList = new ArrayList<>();
        for (int i = 0; i < 1; i++) {
            tireRankList.add(new TireRank(i,"载重1000/200K/￥200","经济运动型",false,"￥200"));
        }
        TireFigure tireFigure = new TireFigure(false, 0,"经济运动型", "http://180.76.243.205:8111/images/flgure/9F5CD167-866A-C9B3-4406-7E0E36A4D003.jpg",
                "http://180.76.243.205:8111/images/flgure/9F5CD167-866A-C9B3-4406-7E0E36A4D003.jpg",
                "http://180.76.243.205:8111/images/flgure/112E28A5-68FD-B758-16CA-E1C7F67939C6.jpg",
                "路酷泽品牌轮胎是山东新大陆橡胶科技有限公司针对高端乘用车推出的拳头产品！如驿如意平台结合中国车主用车习惯独家推出“一次换轮胎 终身免费开”的全新升级服务！",
                tireRankList);
        tireFigureList.add(tireFigure);
        for (int i = 0; i < 6; i++) {
            String gifureStr = "经济运动型" + i;
            List<TireRank> tireRankList1 = new ArrayList<>();
            for (int j = 0; j < 3; j++) {
                tireRankList1.add(new TireRank(j,"载重1000/200K/￥200/" + j,gifureStr,false,"￥200"));
            }
            TireFigure tireFigure1 = new TireFigure(false, 0,gifureStr, "http://180.76.243.205:8111/images/flgure/9F5CD167-866A-C9B3-4406-7E0E36A4D003.jpg",
                    "http://180.76.243.205:8111/images/flgure/9F5CD167-866A-C9B3-4406-7E0E36A4D003.jpg",
                    "http://180.76.243.205:8111/images/flgure/112E28A5-68FD-B758-16CA-E1C7F67939C6.jpg",
                    "路酷泽品牌轮胎是山东新大陆橡胶科技有限公司针对高端乘用车推出的拳头产品！如驿如意平台结合中国车主用车习惯独家推出“一次换轮胎 终身免费开”的全新升级服务！",
                    tireRankList1);


            tireFigureList.add(tireFigure1);
        }
    }

    private void initData() {
        items.clear();
        items.add(new TitleStr("轮胎花纹类别选择"));

        for (int i = 0; i < tireFigureList.size(); i++) {
            items.add(tireFigureList.get(i));
        }
        assertAllRegistered(adapter,items);
        adapter.notifyDataSetChanged();
    }

    private void initView() {
        listView = (RecyclerView) findViewById(R.id.car_figure_list);
        nextButton = (TextView) findViewById(R.id.car_figure_button);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        listView.setLayoutManager(linearLayoutManager);
        adapter = new MultiTypeAdapter(items);
        register();

        listView.setAdapter(adapter);
        assertHasTheSameAdapter(listView, adapter);

        RxViewAction.clickNoDouble(nextButton)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        for (int i = 0; i < tireFigureList.size(); i++) {
                            if (tireFigureList.get(i).isCheck) {
                                List<TireRank> tireRankList = tireFigureList.get(i).getTireRankList();
                                for (int j = 0; j < tireRankList.size(); j++) {
                                    if (tireRankList.get(j).isCheck) {
                                        int shoeId = tireRankList.get(j).getId();
                                        String price = tireRankList.get(j).getPrice();
                                        Log.e(TAG, "call: ---------*--------" + shoeId);
                                        Intent intent = new Intent(getApplicationContext(), TireCountActivity.class);
                                        intent.putExtra("SHOEID",shoeId);
                                        intent.putExtra("PRICE",price);
                                        intent.putExtra("FONTREARFLAG",fontrearflag);
                                        startActivity(intent);
                                    }
                                }
                            }
                        }
                    }
                });
    }

    private void register() {
        adapter.register(TitleStr.class,new TitleStrViewBinder());
        TireFigureViewBinder tireFigureViewBinder = new TireFigureViewBinder(this);
        tireFigureViewBinder.setListener(this);
       // tireFigureViewBinder.setRanklistener(this);
        adapter.register(TireFigure.class, tireFigureViewBinder);
    }

    @Override
    public void onFigureClickListener(String name) {
        for (int i = 0; i < tireFigureList.size(); i++) {
            if (tireFigureList.get(i).getTitleStr().equals(name)) {

                tireFigureList.get(i).setCheck(!tireFigureList.get(i).isCheck);
            }else {
                tireFigureList.get(i).setCheck(false);
            }
        }
        initData();
    }

    @Override
    public void onRankClickListener(String rankName,String figureName) {
        for (int i = 0; i < tireFigureList.size(); i++) {
            if (tireFigureList.get(i).getTitleStr().equals(figureName)) {

                for (int h = 0; h < tireFigureList.get(i).getTireRankList().size(); h++) {
                    if (tireFigureList.get(i).getTireRankList().get(h).getRankName().equals(rankName)){
                        tireFigureList.get(i).getTireRankList().get(h).setCheck(true);
                    }else {
                        tireFigureList.get(i).getTireRankList().get(h).setCheck(false);
                    }
                }
            }else {
                for (int h = 0; h < tireFigureList.get(i).getTireRankList().size(); h++) {
                    tireFigureList.get(i).getTireRankList().get(h).setCheck(false);
                }
            }
        }
        initData();
    }

 /*   @Override
    public void onTireRankClickListener(String name) {
        for (int i = 0; i < tireFigureList.size(); i++) {
            List<TireRank> tireRankList = tireFigureList.get(i).getTireRankList();
            for (int j = 0; j < tireRankList.size(); j++) {
                if (tireRankList.get(j).getRankName().equals(name)) {
                    tireFigureList.get(i).getTireRankList().get(j).setCheck(true);
                }else {
                    tireFigureList.get(i).getTireRankList().get(j).setCheck(false);
                }
            }
        }
        initData();
    }*/

   /* @Override
    public void onRankClickListener(String rankName) {
        for (int i = 0; i < tireFigureList.size(); i++) {
            List<TireRank> tireRankList = tireFigureList.get(i).getTireRankList();
            for (int j = 0; j < tireRankList.size(); j++) {
                if (tireRankList.get(j).getRankName().equals(rankName)) {
                    tireFigureList.get(i).getTireRankList().get(j).setCheck(true);
                }else {
                    tireFigureList.get(i).getTireRankList().get(j).setCheck(false);
                }
            }
        }
        initData();
    }*/
}
