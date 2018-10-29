package com.ruyiruyi.ruyiruyi.ui.activity;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.Poi;
import com.ruyiruyi.ruyiruyi.MainActivity;
import com.ruyiruyi.ruyiruyi.MyApplication;
import com.ruyiruyi.ruyiruyi.R;
import com.ruyiruyi.ruyiruyi.db.DbConfig;
import com.ruyiruyi.ruyiruyi.db.model.Location;
import com.ruyiruyi.ruyiruyi.db.model.Province;
import com.ruyiruyi.ruyiruyi.ui.activity.base.RyBaseActivity;
import com.ruyiruyi.ruyiruyi.ui.model.HotCity;
import com.ruyiruyi.ruyiruyi.ui.service.LocationService;
import com.ruyiruyi.ruyiruyi.utils.RequestUtils;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;
import com.ruyiruyi.rylibrary.ui.adapter.CYBChangeCityGridViewAdapter;
import com.ruyiruyi.rylibrary.ui.adapter.ContactAdapter;
import com.ruyiruyi.rylibrary.ui.bean.UserEntity;
import com.ruyiruyi.rylibrary.ui.cell.QGridView;

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

import me.yokeyword.indexablerv.IndexableAdapter;
import me.yokeyword.indexablerv.IndexableHeaderAdapter;
import me.yokeyword.indexablerv.IndexableLayout;
import rx.functions.Action1;

public class CityChooseActivity extends RyBaseActivity {
    private static final String TAG = CityChooseActivity.class.getSimpleName();
    private ContactAdapter mAdapter;
    private BannerHeaderAdapter mBannerHeaderAdapter;
    private String[] city = {"青岛市","临沂市","潍坊市"};
    private IndexableLayout indexableLayout;
    private CYBChangeCityGridViewAdapter cybChangeCityGridViewAdapter;
    private ArrayList<String> list;
    private ArrayList<String> quList;
    private ImageView pic_contact_back;
    private Intent intent;
    private LocationService locationService;
    private TextView dingweiText;
    public String currentCity="";
    private double weidu;
    private double jingdu;
    private FrameLayout quLayout;
    private FrameLayout chooseQuLayout;
    public boolean isChooseQu = false;
    public List<HotCity> hotCityList ;
    private String currentCityName = "";
    private TextView quNameText;
    private QGridView quListView;
    private CYBChangeCityGridViewAdapter quGridViewAdapter;
    private int currentDefinition;
    private int currentFid;
    private int currentId;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_contact);

        initview();

//        SDKInitializer.initialize(getApplicationContext());
        locationService = ((MyApplication) getApplication()).locationService;
        //获取locationservice实例，建议应用中只初始化1个location实例，然后使用，可以参考其他示例的activity，都是通过此种方式获取locationservice实例的
        locationService.registerListener(mListener);
        locationService.setLocationOption(locationService.getDefaultLocationClientOption());
        locationService.start();// 定位SDK
        hotCityList = new ArrayList<>();
        quList = new ArrayList<>();
        Intent intent = getIntent();
       // currentCityName = intent.getStringExtra("CITY_NAME");

        initAdapter();
        onlisten();
        //获取热门地区
        initHotDataFromService();

        //获取位置
        Location location = new DbConfig(getApplicationContext()).getLocation();
        if (location!=null){
            currentCityName = location.getCity();
            jingdu = location.getJingdu();
            weidu = location.getWeidu();
        }
        //初始化区信息
        if (!currentCityName.equals("")){
            initQuData();
        }

    }

    private void initQuData() {
        quNameText.setText("您正在看：" + currentCityName);
        DbManager db = new DbConfig(this).getDbManager();
        List<Province> provinceList = new ArrayList<>();
        try {
            provinceList = db.selector(Province.class)
                    .where("definition", "=", "2")
                    .or("definition", "=", "3")
                    .findAll();
        } catch (DbException e) {
            e.printStackTrace();
        }
        if (provinceList.size()>0){
            for (int i = 0; i < provinceList.size(); i++) {
                if (provinceList.get(i).getName().equals(currentCityName)) {  //获取当前选择的地区的信息
                    currentDefinition = provinceList.get(i).getDefinition();
                    currentFid = provinceList.get(i).getFid();
                    currentId = provinceList.get(i).getId();
                }
            }
        }

        Log.e(TAG, "initQuData:currentDefinition--- " + currentDefinition);
        Log.e(TAG, "initQuData:currentFid--- " + currentFid);
        Log.e(TAG, "initQuData:currentId---" + currentId);
        List<Province> quAllList = new ArrayList<>();
        if (currentDefinition == 2){   // 2是市
            try {
                quAllList = db.selector(Province.class)
                        .where("fid","=",currentId)
                        .findAll();

            } catch (DbException e) {
                e.printStackTrace();
            }

        }else if (currentDefinition == 3){      //3是区  反像查市
            try {
                quAllList = db.selector(Province.class)
                        .where("fid","=",currentFid)
                        .findAll();
            } catch (DbException e) {
                e.printStackTrace();
            }
        }
        quList.clear();
        if (quAllList.size()>0){
            for (int i = 0; i < quAllList.size(); i++) {
                quList.add(quAllList.get(i).getName());
            }
        }
        quGridViewAdapter = new CYBChangeCityGridViewAdapter(CityChooseActivity.this, quList);
        quListView.setAdapter(quGridViewAdapter);
    }

    /**
     * 获取热门地区
     */
    private void initHotDataFromService() {
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "getHotPosition");
        x.http().get(params, new Callback.CacheCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess: -----" + result);
                JSONObject jsonObject1 = null;
                try {
                    jsonObject1 = new JSONObject(result);
                    String status = jsonObject1.getString("status");
                    String msg = jsonObject1.getString("msg");
                    if (status.equals("1")){
                        JSONArray data = jsonObject1.getJSONArray("data");
                        hotCityList.clear();
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject object = data.getJSONObject(i);
                            int definition = object.getInt("definition");
                            int fid = object.getInt("fid");
                            int id = object.getInt("id");
                            String icon = object.getString("icon");
                            String name = object.getString("name");
                            String time = object.getString("time");
                            hotCityList.add(new HotCity(definition,fid,id,icon,name,time));

                        }
                        indexableLayout.addHeaderAdapter(mBannerHeaderAdapter);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable throwable, boolean b) {

            }

            @Override
            public void onCancelled(CancelledException e) {

            }

            @Override
            public void onFinished() {

            }

            @Override
            public boolean onCache(String s) {
                return false;
            }
        });
    }

    private void initHotData() {

    }


    /***
     * Stop location service
     */
    @Override
    protected void onStop() {
        // TODO Auto-generated method stub

        super.onStop();
    }

    public void initAdapter(){
        mAdapter = new ContactAdapter(this);
        indexableLayout.setAdapter(mAdapter);
        indexableLayout.setOverlayStyle_Center();
        mAdapter.setDatas(initDatas());
//        indexableLayout.setOverlayStyle_MaterialDesign(Color.RED);
        // 全字母排序。  排序规则设置为：每个字母都会进行比较排序；速度较慢
        indexableLayout.setCompareMode(IndexableLayout.MODE_FAST);
//        indexableLayout.addHeaderAdapter(new SimpleHeaderAdapter<>(mAdapter, "☆",null, null));

//         构造函数里3个参数,分别对应 (IndexBar的字母索引, IndexTitle, 数据源), 不想显示哪个就传null, 数据源传null时,代表add一个普通的View
//        mMenuHeaderAdapter = new MenuHeaderAdapter("↑", null, initMenuDatas());
//        indexableLayout.addHeaderAdapter(mMenuHeaderAdapter);

        // 这里BannerView只有一个Item, 添加一个长度为1的任意List作为第三个参数
        List<String> bannerList = new ArrayList<>();
        bannerList.add("");
        mBannerHeaderAdapter = new BannerHeaderAdapter("↑", null, bannerList);

    }

    public void initview(){
        intent = getIntent();
        pic_contact_back = (ImageView) findViewById(R.id.pic_contact_back);
        indexableLayout = (IndexableLayout) findViewById(R.id.indexableLayout);
        indexableLayout.setLayoutManager(new LinearLayoutManager(this));
        dingweiText = (TextView) findViewById(R.id.dingwei_city_text);
        quLayout = (FrameLayout) findViewById(R.id.qu_layout);
        chooseQuLayout = (FrameLayout) findViewById(R.id.choose_qu_layout);
        quNameText = (TextView) findViewById(R.id.qu_name_text);
        quListView = (QGridView) findViewById(R.id.qu_list_view);

        quListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //  intent.putExtra("city", list.get(position));

                Location location1 = new Location(1, quList.get(position),jingdu, weidu);
                DbManager db = new DbConfig(getApplicationContext()).getDbManager();
                try {
                    db.saveOrUpdate(location1);
                } catch (DbException e) {

                }
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
//                    System.out.println("aaaaaayyyyyyyyy"+list.get(position));
//                    setResult(RESULT_OK, intent);
//                    finish();
            }
        });



        RxViewAction.clickNoDouble(quLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (currentCityName.equals("")){
                            return;
                        }
                        if (isChooseQu){
                            chooseQuLayout.setVisibility(View.GONE);
                            isChooseQu = false;
                        }else {
                            chooseQuLayout.setVisibility(View.VISIBLE);
                            isChooseQu = true;
                        }
                    }
                });

        /**
         * 定位点击
         */
        RxViewAction.clickNoDouble(dingweiText)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (currentCity.equals("")){
                            Toast.makeText(CityChooseActivity.this, "请选择区县", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Location location1 = new Location(1, currentCity, jingdu, weidu);
                        DbManager db = new DbConfig(getApplicationContext()).getDbManager();
                        try {
                            db.saveOrUpdate(location1);
                        } catch (DbException e) {

                        }
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        finish();
                     //   CityChooseActivity.this.intent.putExtra("CITY",currentCity);
                       // setResult(HomeFragment.CITY_CHOOSE, CityChooseActivity.this.intent);
                       // finish();
                    }
                });

    }

    public void onlisten(){

        pic_contact_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mAdapter.setOnItemContentClickListener(new IndexableAdapter.OnItemContentClickListener<UserEntity>() {
            @Override
            public void onItemClick(View v, int originalPosition, int currentPosition, UserEntity entity) {
                if (originalPosition >= 0) {
                   /* Location location1 = new Location(1, entity.getNick(),jingdu, weidu);
                    DbManager db = new DbConfig(getApplicationContext()).getDbManager();
                    try {
                        db.saveOrUpdate(location1);
                    } catch (DbException e) {

                    }*/
                    currentCityName = entity.getNick();
                    initQuData();
                    chooseQuLayout.setVisibility(View.VISIBLE);
                    Toast.makeText(CityChooseActivity.this, "请选择县区", Toast.LENGTH_SHORT).show();
                    
                  /*  Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);*/
                  /*  intent.putExtra("CITY", entity.getNick());
                    setResult(HomeFragment.CITY_CHOOSE, intent);
                    finish();*/
                } else {
                    Toast.makeText(CityChooseActivity.this, "选中Header/Footer:" + entity.getNick() + "  当前位置:" + currentPosition, Toast.LENGTH_SHORT).show();
                  //  ToastUtil.showShort(CityPickerActivity.this, "选中Header/Footer:" + entity.getNick() + "  当前位置:" + currentPosition);
                }
            }
        });
    }

  /*  @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            String city = data.getStringExtra("city");
            Log.e(TAG, "onActivityResult: ----------+---"+city);
        }
    }*/

    /**
     * 自定义的Banner Header
     */
    class BannerHeaderAdapter extends IndexableHeaderAdapter {
        private static final int TYPE = 1;

        public BannerHeaderAdapter(String index, String indexTitle, List datas) {
            super(index, indexTitle, datas);
        }

        @Override
        public int getItemViewType() {
            return TYPE;
        }

        @Override
        public RecyclerView.ViewHolder onCreateContentViewHolder(ViewGroup parent) {
            View view = LayoutInflater.from(CityChooseActivity.this).inflate(R.layout.item_city_header, parent, false);
            VH holder = new VH(view);
            return holder;
        }

        @Override
        public void onBindContentViewHolder(RecyclerView.ViewHolder holder, Object entity) {
            // 数据源为null时, 该方法不用实现
            final VH vh = (VH) holder;
            list=new ArrayList<>();
           /* for(int i = 0; i<city.length; i++){
                list.add(city[i]);
            }*/

            for (int i = 0; i < hotCityList.size(); i++) {
                list.add(hotCityList.get(i).getName());
            }
            System.out.println("------------city"+list);
            cybChangeCityGridViewAdapter=new CYBChangeCityGridViewAdapter(CityChooseActivity.this, list);
            vh.head_home_change_city_gridview.setAdapter(cybChangeCityGridViewAdapter);
            vh.head_home_change_city_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                 //  intent.putExtra("city", list.get(position));
                    Log.e(TAG, "onItemClick: --------" + list.get(position));
                    /*Location location1 = new Location(1, list.get(position),jingdu, weidu);
                    DbManager db = new DbConfig(getApplicationContext()).getDbManager();
                    try {
                        db.saveOrUpdate(location1);
                    } catch (DbException e) {

                    }*/
                    /*Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);*/
                    currentCityName =  list.get(position);
                    initQuData();
                    chooseQuLayout.setVisibility(View.VISIBLE);
                    Toast.makeText(CityChooseActivity.this, "请选择县区", Toast.LENGTH_SHORT).show();


//                    System.out.println("aaaaaayyyyyyyyy"+list.get(position));
//                    setResult(RESULT_OK, intent);
//                    finish();
                }
            });
            /*//此处有问题
            vh.item_header_city_dw.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   intent.putExtra("city", vh.item_header_city_dw.getText().toString());
                    setResult(RESULT_OK, intent);
                    finish();
                }
            });*/
        /*    RxViewAction.clickNoDouble(vh.item_header_city_dw)
                    .subscribe(new Action1<Void>() {
                        @Override
                        public void call(Void aVoid) {
                            *//*intent.putExtra("city", vh.item_header_city_dw.getText().toString());
                            setResult(RESULT_OK, intent);
                            finish();*//*
                        }
                    });*/

        }

        private class VH extends RecyclerView.ViewHolder {
            GridView head_home_change_city_gridview;
            TextView item_header_city_dw;
            public VH(View itemView) {
                super(itemView);
                head_home_change_city_gridview =(QGridView)itemView.findViewById(R.id.item_header_city_gridview);
            //   item_header_city_dw = (TextView) itemView.findViewById(R.id.item_header_city_text);
            }
        }
    }

    private List<UserEntity> initDatas() {
        DbManager db = new DbConfig(this).getDbManager();
        List<Province> provinceList = new ArrayList<>();
        try {
            provinceList  = db.selector(Province.class)
                    .where("definition" , "=" , "2")
                    .findAll();
        } catch (DbException e) {
        }
        List<String> contactStrings  = new ArrayList<>();
        for (int i = 0; i < provinceList.size(); i++) {
            String name = provinceList.get(i).getName();
            contactStrings.add(name);
        }
        List<UserEntity> list = new ArrayList<>();


       /* List<UserEntity> list = new ArrayList<>();
        // 初始化数据
        List<String> contactStrings = Arrays.asList(getResources().getStringArray(R.array.provinces));
        List<String> mobileStrings = Arrays.asList(getResources().getStringArray(R.array.provinces));*/
        for (int i = 0; i < contactStrings.size(); i++) {
            UserEntity contactEntity = new UserEntity(contactStrings.get(i), contactStrings.get(i));
            list.add(contactEntity);
        }
        return list;
    }

    /*****
     *
     * 定位结果回调，重写onReceiveLocation方法，可以直接拷贝如下代码到自己工程中修改
     *
     */
    private BDAbstractLocationListener mListener = new BDAbstractLocationListener() {



        @Override
        public void onReceiveLocation(BDLocation location) {
            // TODO Auto-generated method stub
            if (null != location && location.getLocType() != BDLocation.TypeServerError) {
                StringBuffer sb = new StringBuffer(256);
                sb.append("time : ");
                /**
                 * 时间也可以使用systemClock.elapsedRealtime()方法 获取的是自从开机以来，每次回调的时间；
                 * location.getTime() 是指服务端出本次结果的时间，如果位置不发生变化，则时间不变
                 */
                sb.append(location.getTime());
                sb.append("\nlocType : ");// 定位类型
                sb.append(location.getLocType());
                sb.append("\nlocType description : ");// *****对应的定位类型说明*****
                sb.append(location.getLocTypeDescription());
                sb.append("\nlatitude : ");// 纬度
                sb.append(location.getLatitude());
                sb.append("\nlontitude : ");// 经度
                sb.append(location.getLongitude());
                sb.append("\nradius : ");// 半径
                sb.append(location.getRadius());
                sb.append("\nCountryCode : ");// 国家码
                sb.append(location.getCountryCode());
                sb.append("\nCountry : ");// 国家名称
                sb.append(location.getCountry());
                sb.append("\ncitycode : ");// 城市编码
                sb.append(location.getCityCode());
                sb.append("\ncity : ");// 城市
                sb.append(location.getCity());
                sb.append("\nDistrict : ");// 区
                sb.append(location.getDistrict());
                sb.append("\nStreet : ");// 街道
                sb.append(location.getStreet());
                sb.append("\naddr : ");// 地址信息
                sb.append(location.getAddrStr());
                sb.append("\nUserIndoorState: ");// *****返回用户室内外判断结果*****
                sb.append(location.getUserIndoorState());
                sb.append("\nDirection(not all devices have value): ");
                sb.append(location.getDirection());// 方向
                sb.append("\nlocationdescribe: ");
                sb.append(location.getLocationDescribe());// 位置语义化信息
                sb.append("\nPoi: ");// POI信息
                if (location.getPoiList() != null && !location.getPoiList().isEmpty()) {
                    for (int i = 0; i < location.getPoiList().size(); i++) {
                        Poi poi = (Poi) location.getPoiList().get(i);
                        sb.append(poi.getName() + ";");
                    }
                }
                if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
                    sb.append("\nspeed : ");
                    sb.append(location.getSpeed());// 速度 单位：km/h
                    sb.append("\nsatellite : ");
                    sb.append(location.getSatelliteNumber());// 卫星数目
                    sb.append("\nheight : ");
                    sb.append(location.getAltitude());// 海拔高度 单位：米
                    sb.append("\ngps status : ");
                    sb.append(location.getGpsAccuracyStatus());// *****gps质量判断*****
                    sb.append("\ndescribe : ");
                    sb.append("gps定位成功");
                } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
                    // 运营商信息
                    if (location.hasAltitude()) {// *****如果有海拔高度*****
                        sb.append("\nheight : ");
                        sb.append(location.getAltitude());// 单位：米
                    }
                    sb.append("\noperationers : ");// 运营商信息
                    sb.append(location.getOperators());
                    sb.append("\ndescribe : ");
                    sb.append("网络定位成功");
                } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                    sb.append("\ndescribe : ");
                    sb.append("离线定位成功，离线定位结果也是有效的");
                } else if (location.getLocType() == BDLocation.TypeServerError) {
                    sb.append("\ndescribe : ");
                    sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
                } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                    sb.append("\ndescribe : ");
                    sb.append("网络不同导致定位失败，请检查网络是否通畅");
                } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                    sb.append("\ndescribe : ");
                    sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
                }

                Log.e(TAG, "onReceiveLocation: " + sb.toString());
                locationService.unregisterListener(mListener); //注销掉监听
                locationService.stop(); //停止定位服务
                currentCity = location.getDistrict();
                jingdu = location.getLongitude();
                weidu = location.getLatitude();
                dingweiText.setText(location.getDistrict());
            }
        }

    };


}
