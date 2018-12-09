package com.example.warehouse.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.warehouse.R;
import com.example.warehouse.db.DbConfig;
import com.example.warehouse.db.TireType;
import com.example.warehouse.ui.cell.WareActionBar;
import com.example.warehouse.ui.mulit.Bian;
import com.example.warehouse.ui.mulit.BianViewBinder;
import com.example.warehouse.ui.mulit.Huawen;
import com.example.warehouse.ui.mulit.HuawenViewBinder;
import com.example.warehouse.ui.mulit.Kuan;
import com.example.warehouse.ui.mulit.KuanViewBinder;
import com.example.warehouse.ui.mulit.Zhi;
import com.example.warehouse.ui.mulit.ZhiViewBinder;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;
import com.ruyiruyi.rylibrary.cell.ActionBar;

import org.xutils.DbManager;
import org.xutils.ex.DbException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

import me.drakeet.multitype.MultiTypeAdapter;
import rx.functions.Action1;

import static me.drakeet.multitype.MultiTypeAsserts.assertAllRegistered;
import static me.drakeet.multitype.MultiTypeAsserts.assertHasTheSameAdapter;

public class TireSizeChooseActivity extends AppCompatActivity implements KuanViewBinder.OnKuanClickListener,BianViewBinder.OnBianClikcListener
                    ,ZhiViewBinder.OnZhiClickListener,HuawenViewBinder.OnHuawenClickListener {
    private static final String TAG = TireSizeChooseActivity.class.getSimpleName();
    private WareActionBar actionBar;
    private RecyclerView kuanListView;
    private RecyclerView bianListView;
    private RecyclerView zhiListView;
    private RecyclerView huawenListView;
    private List<Object> kuanItems = new ArrayList<>();
    private MultiTypeAdapter kuanAdapter;

    private List<Object> bianItems = new ArrayList<>();
    private MultiTypeAdapter bianAdapter;

    private List<Object> zhiItems = new ArrayList<>();
    private MultiTypeAdapter zhiAdapter;

    private List<Object> huawenItems = new ArrayList<>();
    private MultiTypeAdapter huawenAdapter;
    private String pinPai;
    public List<TireType> pinpaiList;
    public List<Integer> kuanList;
    public List<Integer> zhiList;
    public List<Integer> bianList;
    public List<Huawen> huawenList;
    public int currentClick = 0 ; //0胎面宽  1扁平比 2 直径 3花纹
    public int currentKuan = 0;
    public int currentBian = 0;
    public int currentZhi = 0;
    private TextView chooseTireButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tire_size);

        actionBar = (WareActionBar) findViewById(R.id.my_action);
        actionBar.setTitle("选择轮胎规格");
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
        pinpaiList = new ArrayList<>();
        kuanList = new ArrayList<>();
        zhiList = new ArrayList<>();
        bianList = new ArrayList<>();
        huawenList = new ArrayList<>();
        Intent intent = getIntent();
        pinPai = intent.getStringExtra("PIN_PAI");

        getPinPaiData();

        initView();

        initKuanData();
    }


    /**
     * 获取品牌数据
     */
    private void getPinPaiData() {
        pinpaiList.clear();
        DbManager db = new DbConfig(this).getDbManager();
        try {
            pinpaiList = db.selector(TireType.class)
                    .where("brand","=",pinPai)
                    .findAll();
            Log.e(TAG, "getPinPaiData: pinpaiList---2----" + pinpaiList.size());
            Log.e(TAG, "initKuanData:pinpaiList ------" + pinpaiList.get(0).toString());
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    private void initView() {
        kuanListView = (RecyclerView) findViewById(R.id.kuan_listview);
        bianListView = (RecyclerView) findViewById(R.id.bian_listview);
        zhiListView = (RecyclerView) findViewById(R.id.zhi_listview);
        huawenListView = (RecyclerView) findViewById(R.id.huawen_listview);
        chooseTireButton = (TextView) findViewById(R.id.choose_tire_button);

        RxViewAction.clickNoDouble(chooseTireButton)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        List<TireType> chooseTire = new ArrayList<TireType>();
                        for (int i = 0; i < pinpaiList.size(); i++) {
                            if (pinpaiList.get(i).isclick()) {      //已被选中的轮胎xinghao
                                chooseTire.add(pinpaiList.get(i));
                            }
                        }

                        Intent intent = new Intent();
                        intent.putExtra("TIRE_LIST", (Serializable) chooseTire);
                        setResult(PurchaseActivity.TIRE_CHOOSE_REQUSET,intent);
                        finish();

                    }
                });

        //胎面宽的适配器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        kuanListView.setLayoutManager(linearLayoutManager);
        kuanAdapter = new MultiTypeAdapter(kuanItems);
        kuanRegister();
        kuanListView.setAdapter(kuanAdapter);
        assertHasTheSameAdapter(kuanListView, kuanAdapter);

        //扁平比的适配器
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        bianListView.setLayoutManager(linearLayoutManager2);
        bianAdapter = new MultiTypeAdapter(bianItems);
        bianRegister();
        bianListView.setAdapter(bianAdapter);
        assertHasTheSameAdapter(bianListView, bianAdapter);

        //直径的适配器
        LinearLayoutManager linearLayoutManager3 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        zhiListView.setLayoutManager(linearLayoutManager3);
        zhiAdapter = new MultiTypeAdapter(zhiItems);
        zhiRegister();
        zhiListView.setAdapter(zhiAdapter);
        assertHasTheSameAdapter(zhiListView, zhiAdapter);

        //花纹的适配器
        LinearLayoutManager linearLayoutManager4 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        huawenListView.setLayoutManager(linearLayoutManager4);
        huawenAdapter = new MultiTypeAdapter(huawenItems);
        huawenRegister();
        huawenListView.setAdapter(huawenAdapter);
        assertHasTheSameAdapter(huawenListView, huawenAdapter);


    }

    private void huawenRegister() {
        HuawenViewBinder huawenViewBinder = new HuawenViewBinder();
        huawenViewBinder.setListener(this);
        huawenAdapter.register(Huawen.class, huawenViewBinder);
    }

    private void zhiRegister() {
        ZhiViewBinder zhiViewBinder = new ZhiViewBinder();
        zhiViewBinder.setListener(this);
        zhiAdapter.register(Zhi.class, zhiViewBinder);
    }

    private void bianRegister() {
        BianViewBinder bianViewBinder = new BianViewBinder();
        bianViewBinder.setListener(this);
        bianAdapter.register(Bian.class, bianViewBinder);
    }

    private void kuanRegister() {
        KuanViewBinder kuanViewBinder = new KuanViewBinder();
        kuanViewBinder.setListener(this);
        kuanAdapter.register(Kuan.class, kuanViewBinder);
    }

    /**
     * 胎面宽 回调
     * @param kuanName
     */
    @Override
    public void onKuanItemClikcListener(int kuanName) {
        currentKuan = kuanName;
        currentBian = 0;
        currentZhi = 0;
        initKuanData();
        initBianData();
        initZhiData();
        initHuawenData();
    }
    /**
     * 扁平比 回调
     * @param bianName
     */
    @Override
    public void onBianItemClickListener(int bianName) {
        currentBian = bianName;
        currentZhi = 0;
        initBianData();
        initZhiData();
        initHuawenData();
    }

    /**
     * 直径的选择回调
     * @param name
     */
    @Override
    public void onZhiItemClikcListener(int name) {
        currentZhi = name;
        initZhiData();
        initHuawenData();
    }
    /**
     * 花纹 回调
     * @param id
     */
    @Override
    public void onHuawenItemClickListener(int id) {
        for (int i = 0; i < pinpaiList.size(); i++) {
            if (pinpaiList.get(i).getId() == id) {
                if (pinpaiList.get(i).isclick()){
                    pinpaiList.get(i).setIsclick(false);
                }else {
                    pinpaiList.get(i).setIsclick(true);
                }
            }
        }
        initHuawenData();
    }


    /**
     * 初始化花纹数据
     */
    private void initHuawenData() {
        huawenList.clear();
        for (int i = 0; i < pinpaiList.size(); i++) {
            if (pinpaiList.get(i).getInchmm().equals(currentKuan +"") && pinpaiList.get(i).getInch().equals(currentBian +"")
                    && pinpaiList.get(i).getDiameter().equals(currentZhi +"")){
                String flgureName = pinpaiList.get(i).getFlgureName();
                int id = pinpaiList.get(i).getId();
                boolean isclick = pinpaiList.get(i).isclick();
                huawenList.add(new Huawen(id,flgureName,isclick));
            }
        }

        huawenItems.clear();
        for (int i = 0; i < huawenList.size(); i++) {
            huawenItems.add(huawenList.get(i));
        }
        if (huawenItems.size() == 0){
            huawenListView.setVisibility(View.INVISIBLE);
        }else {
            huawenListView.setVisibility(View.VISIBLE);
            assertAllRegistered(huawenAdapter,huawenItems);
            huawenAdapter.notifyDataSetChanged();
        }

    }


    /**
     * 初始化胎面宽数据  将胎面宽去重 排序
     */
    private void initKuanData() {
        kuanList.clear();
        List<Integer> kuanAllList = new ArrayList<>();
        for (int i = 0; i < pinpaiList.size(); i++) {
            String inchmm = pinpaiList.get(i).getInchmm(); //胎面宽
            kuanAllList.add(Integer.parseInt(inchmm));
        }

        HashSet<Integer> set = new HashSet<>();
        set.addAll(kuanAllList);
        kuanList.addAll(set);
        Log.e(TAG, "initKuanData:kuanList ------" + kuanList.size());
        Log.e(TAG, "initKuanData:kuanList ------" + kuanList.get(0).toString());

        Collections.sort(kuanList, new Comparator<Integer>() {
            @Override
            public int compare(Integer t0, Integer t1) {

                return t0.compareTo(t1);
            }
        });


        kuanItems.clear();
        for (int i = 0; i < kuanList.size(); i++) {
            if (kuanList.get(i).toString().equals(currentKuan+"")){
                kuanItems.add(new Kuan(kuanList.get(i).toString(),true));
            }else {
                kuanItems.add(new Kuan(kuanList.get(i).toString(),false));
            }

        }


        assertAllRegistered(kuanAdapter,kuanItems);
        kuanAdapter.notifyDataSetChanged();


    }

    /**
     * 初始化扁平比数据  根据选的胎面宽 查扁平比 去重 排序
     */
    private void initBianData() {
        bianList.clear();
        List<Integer> bianAllList = new ArrayList<>();
        for (int i = 0; i < pinpaiList.size(); i++) {
            if (pinpaiList.get(i).getInchmm().equals(currentKuan+"")){
                String inch = pinpaiList.get(i).getInch();
                bianAllList.add(Integer.parseInt(inch));
            }
        }

        HashSet<Integer> set = new HashSet<>();
        set.addAll(bianAllList);
        bianList.addAll(set);
        Log.e(TAG, "initBianData:bianList--- " + bianList.size() );
        Collections.sort(bianList, new Comparator<Integer>() {
            @Override
            public int compare(Integer t0, Integer t1) {
                return t0.compareTo(t1);
            }
        });

        bianItems.clear();
        for (int i = 0; i < bianList.size(); i++) {
            if (bianList.get(i).toString().equals(currentBian + "")){
                bianItems.add(new Bian(bianList.get(i).toString(),true));
            }else {
                bianItems.add(new Bian(bianList.get(i).toString(),false));
            }

        }

        if (bianItems.size() == 0){
            bianListView.setVisibility(View.INVISIBLE);
        }else {
            bianListView.setVisibility(View.VISIBLE);
            assertAllRegistered(bianAdapter,bianItems);
            bianAdapter.notifyDataSetChanged();
        }


    }



    /**
     * 初始化直径数据
     */
    private void initZhiData() {
        zhiList.clear();
        List<Integer> zhiAllList = new ArrayList<>();
        for (int i = 0; i < pinpaiList.size(); i++) {
            if (pinpaiList.get(i).getInchmm().equals(currentKuan +"") && pinpaiList.get(i).getInch().equals(currentBian +"")){
                String zhi = pinpaiList.get(i).getDiameter();
                zhiAllList.add(Integer.parseInt(zhi));
            }
        }

        HashSet<Integer> set = new HashSet<>();
        set.addAll(zhiAllList);
        zhiList.addAll(set);

        Collections.sort(zhiList, new Comparator<Integer>() {
            @Override
            public int compare(Integer t0, Integer t1) {
                return t0.compareTo(t1);
            }
        });
        zhiItems.clear();
        for (int i = 0; i < zhiList.size(); i++) {
            if (zhiList.get(i).toString().equals(currentZhi + "")){
                zhiItems.add(new Zhi(zhiList.get(i).toString(),true));
            }else {
                zhiItems.add(new Zhi(zhiList.get(i).toString(),false));
            }
        }
        if (zhiItems.size() == 0){
            zhiListView.setVisibility(View.INVISIBLE);
        }else {
            zhiListView.setVisibility(View.VISIBLE);
            assertAllRegistered(zhiAdapter,zhiItems);
            zhiAdapter.notifyDataSetChanged();
        }


    }


}
