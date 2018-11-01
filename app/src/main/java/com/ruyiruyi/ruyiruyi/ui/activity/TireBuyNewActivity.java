package com.ruyiruyi.ruyiruyi.ui.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ruyiruyi.ruyiruyi.R;
import com.ruyiruyi.ruyiruyi.db.DbConfig;
import com.ruyiruyi.ruyiruyi.db.model.User;
import com.ruyiruyi.ruyiruyi.ui.activity.base.RyBaseActivity;
import com.ruyiruyi.ruyiruyi.ui.cell.TSeekBar;
import com.ruyiruyi.ruyiruyi.ui.model.CxwyPrice;
import com.ruyiruyi.ruyiruyi.ui.model.CxwyTimesPrice;
import com.ruyiruyi.ruyiruyi.ui.model.CxwyYear;
import com.ruyiruyi.ruyiruyi.ui.model.FlowModel;
import com.ruyiruyi.ruyiruyi.ui.model.ServiceType;
import com.ruyiruyi.ruyiruyi.ui.model.TireInfo;
import com.ruyiruyi.ruyiruyi.ui.model.TirePrice;
import com.ruyiruyi.ruyiruyi.ui.model.TireRank;
import com.ruyiruyi.ruyiruyi.utils.RequestUtils;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;
import com.ruyiruyi.rylibrary.cell.ActionBar;
import com.ruyiruyi.rylibrary.cell.AmountView;
import com.ruyiruyi.rylibrary.cell.flowlayout.FlowLayout;
import com.ruyiruyi.rylibrary.cell.flowlayout.TagAdapter;
import com.ruyiruyi.rylibrary.cell.flowlayout.TagFlowLayout;
import com.ruyiruyi.rylibrary.ui.viewpager.CustomBanner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import rx.functions.Action1;

public class TireBuyNewActivity extends RyBaseActivity {
    private static final String TAG = TireBuyNewActivity.class.getSimpleName();
    private ActionBar actionBar;
    private FrameLayout liuchengLayout;
    private Dialog dialog;
    private View inflate;
    private FrameLayout fuwuLayout;
    private LinearLayout weixinShreLayout;
    private LinearLayout pengyouquanLayout;
    private LinearLayout dingweiLayout;
    private LinearLayout butaiLayout;
    private LinearLayout huanxinLayout;
    private LinearLayout fuwuContentLayout;
    private Dialog liuchengDialog;
    private View liuchengInflate;
    private ImageView liuchengImage;
    private ImageView liuchengContentLayout;
    private String tiresize;
    private Dialog shopDialog;
    private View shopInflate;

    public List<TireInfo> tireInfoList ;
    private CustomBanner mBanner;
    public int currentTirePostition = 0;    //当前选择的花纹轮胎
    public int currentRankPostition = 0;    //当前选择的速度级别轮胎
    public int currentServiceYear = 1;      //当前选择的服务年限
    public int currentTireChangePostition = 0;    //当前改变的花纹轮胎
    public int currentRankChangePostition = 0;    //当前改变的速度级别轮胎
    public int currentServiceYearChange = 1;      //当前改变的服务年限
    public int tireCount = 0;               //购买的轮胎数量
    public int cxwwyCount = 0;               //购买的畅行无忧数量


    public List<String> imageList;
    public boolean isChoose = false;  //是否进行选择
    public boolean isChooseRank = false;  //是否进行选择速度级别
    private TextView tirePriceText;
    private TextView shoeTitleText;
    private LinearLayout cxwyChooseLayout;
    private TextView tireRankCountText;
    private TextView cxwyCountText;
    private TextView cxwyPriceText;
    private String cxwyAllPrice;

    private ImageView tireImageView;
    private TextView tirePriceDialogText;
    private TextView tireFigureRankDialgText;
    private TagFlowLayout tireRankFlow;
    private TagFlowLayout tireFigureFlow;

    public List<FlowModel> figureFlowMode;
    public List<FlowModel> rankFlowMode;
    private FrameLayout tireRankChooseLayout;
    private LinearLayout liuchengLayout11;
    private TSeekBar yearChooseSeekBar;
    private boolean isChooseServiceYear;
    private String serviceYear;
    private String serviceYearMax;
    private TextView serviceYearText;
    private View serviceYearView;
    private AmountView tireAmountView;
    private AmountView cxwyAmountView;

    public static int maxTireCount = 4;     //最大轮胎数
    public static int maxCount = 7;         //畅行无忧最大水
    private TextView cxwyPriceDIalogText;
    private TextView tireChooseButton;
    private TextView tirePostButton;
    private int tirePrice;
    private String shoeTitle;
    private String fontrearflag;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tire_buy_new);
        actionBar = (ActionBar) findViewById(R.id.my_action);
        actionBar.setTitle("轮胎购买");
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
        progressDialog = new ProgressDialog(this);
        Intent intent = getIntent();
        tiresize = intent.getStringExtra("TIRESIZE");
        isChooseServiceYear = intent.getBooleanExtra("CHOOSE_SERVICE_YEAR",false);
        serviceYear = intent.getStringExtra("SERVICE_YEAR");
        serviceYearMax = intent.getStringExtra("SERVICE_YEAR_MAX");
        fontrearflag = intent.getStringExtra("FONTREARFLAG");


        Log.e(TAG, "onCreate: ---" + serviceYear);
        Log.e(TAG, "onCreate: ---" + serviceYearMax);

        tireInfoList = new ArrayList<>();
        currentTirePostition = 0;
        imageList = new ArrayList<>();
        figureFlowMode = new ArrayList<>();
        rankFlowMode = new ArrayList<>();

        initView();

        initDataFromService();



    }

    private void initData() {

        TireInfo tireInfo = tireInfoList.get(currentTirePostition);
        String shoeDownImg = tireInfo.getShoeDownImg();
        String shoeLeftImg = tireInfo.getShoeLeftImg();
        String shoeMiddleImg = tireInfo.getShoeMiddleImg();
        String shoeRightImg = tireInfo.getShoeRightImg();
        String shoeUpImg = tireInfo.getShoeUpImg();
        //设置轮播图
        imageList.clear();
        imageList.add(shoeDownImg);
        imageList.add(shoeLeftImg);
        imageList.add(shoeMiddleImg);
        imageList.add(shoeRightImg);
        imageList.add(shoeUpImg);
        mBanner.setPages(new CustomBanner.ViewCreator<String>() {
            @Override
            public View createView(Context context, int position) {
                ImageView imageView = new ImageView(context);
                imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                return imageView;
            }

            @Override
            public void updateUI(Context context, View view, int position, String entity) {
                Glide.with(context).load(entity).into((ImageView) view);
            }
        }, imageList)
                //设置自动翻页
                .startTurning(5000);

        //设置价格
        if (isChoose){
            List<TirePrice> tirePriceList = tireInfo.getTireRankList().get(currentRankPostition).getTirePriceList();
            for (int i = 0; i < tirePriceList.size(); i++) {
                if (tirePriceList.get(i).getServiceYear() == currentServiceYear) {
                    tirePrice = tirePriceList.get(i).getTirePrice();
                    tirePriceText.setText("￥" + tirePrice);
                }
            }
        }else {
            List<TirePrice> tirePriceList = tireInfo.getTireRankList().get(0).getTirePriceList();
            int tireMinPrice = 0;
            int tireMaxPrice = 0;
            for (int i = 0; i < tirePriceList.size(); i++) {
                if (tirePriceList.get(i).getServiceYear() == 1) {
                    tireMinPrice = tirePriceList.get(i).getTirePrice();
                }else if (tirePriceList.get(i).getServiceYear() == 15){
                    tireMaxPrice = tirePriceList.get(i).getTirePrice();
                }
            }
            tirePriceText.setText("￥" + tireMinPrice + " ~ " + tireMaxPrice);
        }
        //设置标题
        shoeTitle = tireInfo.getDetailStr();
        shoeTitleText.setText(shoeTitle);
        //设置规格  选择的轮胎数量跟畅行无忧数量
        if (isChoose){

            String figure = tireInfo.getFigure();
            TireRank tireRank = tireInfo.getTireRankList().get(currentRankPostition);
            String rankName = tireRank.getRankName();
            tireRankCountText.setText("已选 " + figure + "," + rankName + "," + tireCount + "条" );
            if (cxwwyCount > 0){
                cxwyChooseLayout.setVisibility(View.VISIBLE);
              /*  cxwyCountText.setText("畅行无忧" + cxwwyCount + "次");
                List<CxwyTimesPrice> cxwyTimesPriceList = tireInfo.getCxwyTimesPriceList();
                String shoeBasePrice = tireInfo.getShoeBasePrice();
                for (int i = 0; i < cxwyTimesPriceList.size(); i++) {
                    if (cxwyTimesPriceList.get(i).getTimes() == cxwwyCount){
                        double price = Double.parseDouble(cxwyTimesPriceList.get(i).getRate()) * Double.parseDouble(shoeBasePrice) / 100;
                        String format = new DecimalFormat("0").format(price);
                        cxwyAllPrice = format + "";
                        cxwyPriceText.setText(" ￥" + cxwyAllPrice);
                    }
                }*/
                cxwyPriceText.setText(" ￥" + cxwyAllPrice);
            }else {
                cxwyChooseLayout.setVisibility(View.GONE);
            }

        }else {
            cxwyChooseLayout.setVisibility(View.GONE);
            tireRankCountText.setText("请选择轮胎规格");
        }

        Glide.with(this).load(tireInfo.getImgMiddleUrl()).into(tireImageView);
    }

    public void initFigureFlow(){
        TireInfo tireInfo = tireInfoList.get(currentTireChangePostition);
        figureFlowMode.clear();
        for (int i = 0; i < tireInfoList.size(); i++) {
            String figure = tireInfoList.get(i).getFigure();
            figureFlowMode.add(new FlowModel(figure));
        }
        tireFigureFlow.setAdapter(new TagAdapter<FlowModel>(figureFlowMode) {
            @Override
            public View getView(FlowLayout parent, int position, FlowModel flowModel) {
                LayoutInflater mInflater = LayoutInflater.from(getApplicationContext());
                TextView tv = (TextView) mInflater.inflate(R.layout.item_tire, tireFigureFlow, false);
                tv.setText(flowModel.getFlowName());
                return tv;
            }
        });
    }
    public void initRangkFlow(){
        TireInfo tireInfo = tireInfoList.get(currentTireChangePostition);
        rankFlowMode.clear();
        List<TireRank> tireRankList = tireInfo.getTireRankList();
        for (int i = 0; i < tireRankList.size(); i++) {
            String rankName = tireRankList.get(i).getRankName();
            rankFlowMode.add(new FlowModel(rankName));
        }

        tireRankFlow.setAdapter(new TagAdapter<FlowModel>(rankFlowMode) {
            @Override
            public View getView(FlowLayout parent, int position, FlowModel flowModel) {
                LayoutInflater mInflater = LayoutInflater.from(getApplicationContext());
                TextView tv = (TextView) mInflater.inflate(R.layout.item_tire, tireFigureFlow, false);
                tv.setText(flowModel.getFlowName());
                return tv;
            }
        });
    }

    public void initDataTireDialog(){
        TireInfo tireInfo = tireInfoList.get(currentTireChangePostition);

        //更改规格  与 价格
        initDialogRank();

        String imgMiddleUrl = tireInfo.getImgMiddleUrl();
        Glide.with(this).load(imgMiddleUrl).into(tireImageView);
               // .skipMemoryCache(true)
             //   .diskCacheStrategy(DiskCacheStrategy.NONE)




    }
    /**
     * 更改轮胎规格
     */
    public void initDialogRank(){
        //更改价格
        initDialogTirePrice();

        TireInfo tireInfo = tireInfoList.get(currentTireChangePostition);
        if (isChooseRank){
            String figure = tireInfo.getFigure();
            TireRank tireRank = tireInfo.getTireRankList().get(currentRankChangePostition);
            String rankName = tireRank.getRankName();
            tireFigureRankDialgText.setText("已选 " + figure + "," + rankName);
        }else {
            tireFigureRankDialgText.setText("请选择轮胎规格");
        }
    }

    /**
     * 更改D轮胎价格
     */
    public void initDialogTirePrice(){
        TireInfo tireInfo = tireInfoList.get(currentTireChangePostition);
        List<TirePrice> tirePriceList = tireInfo.getTireRankList().get(currentRankChangePostition).getTirePriceList();
        if (isChooseRank){
            for (int i = 0; i < tirePriceList.size(); i++) {
                if (tirePriceList.get(i).getServiceYear() == currentServiceYearChange){
                    int tirePrice = tirePriceList.get(i).getTirePrice();
                    tirePriceDialogText.setText("￥" + tirePrice);
                }
            }
        }else {
            int tireMinPrice = 0;
            int tireMaxPrice = 0;
            for (int i = 0; i < tirePriceList.size(); i++) {
                if (tirePriceList.get(i).getServiceYear() == 1) {
                    tireMinPrice = tirePriceList.get(i).getTirePrice();
                }else if (tirePriceList.get(i).getServiceYear() == 15){
                    tireMaxPrice = tirePriceList.get(i).getTirePrice();
                }
            }
            tirePriceDialogText.setText("￥" + tireMinPrice + " ~ " + tireMaxPrice);
        }

    }

    private void initDataFromService() {
        showDialogProgress(progressDialog,"加载中...");
        User user = new DbConfig(this).getUser();
        int carId = user.getCarId();
        int userId = user.getId();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("shoeSize",tiresize);
            jsonObject.put("userId", userId);
            jsonObject.put("userCarId", carId);
        } catch (JSONException e) {
        }
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "order/getShoeBySize");
        params.addBodyParameter("reqJson", jsonObject.toString());
        String token = new DbConfig(this).getToken();
        params.addParameter("token", token);
        params.setConnectTimeout(10000);
        Log.e(TAG, "initDataFromService:------- " + params.toString());
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess: ------" +  result);

                try {
                    JSONObject jsonObject1 = new JSONObject(result);
                    String status = jsonObject1.getString("status");
                    String msg = jsonObject1.getString("msg");
                    if (status.equals("1")){
                        tireInfoList.clear();
                        JSONArray data = jsonObject1.getJSONArray("data");
                        if (data.length() == 0){
                            Toast.makeText(TireBuyNewActivity.this, "您所需要的规格正在上架当中，请您耐心等待！", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject tireObject = data.getJSONObject(i);
                            String description = tireObject.getString("description");
                            String imgLeftUrl = tireObject.getString("imgLeftUrl");
                            String imgMiddleUrl = tireObject.getString("imgMiddleUrl");
                            String imgRightUrl = tireObject.getString("imgRightUrl");
                            String shoeFlgureName = tireObject.getString("shoeFlgureName");
                            JSONObject shoeDetailResult = tireObject.getJSONObject("shoeDetailResult");
                            String detailStr = shoeDetailResult.getString("detailStr");
                            String figure = shoeDetailResult.getString("figure");
                            String shoeBasePrice = shoeDetailResult.getString("shoeBasePrice");
                            String shoeDownImg = shoeDetailResult.getString("shoeDownImg");
                            String shoeLeftImg = shoeDetailResult.getString("shoeLeftImg");
                            String shoeMiddleImg = shoeDetailResult.getString("shoeMiddleImg");
                            String shoeRightImg = shoeDetailResult.getString("shoeRightImg");
                            String shoeUpImg = shoeDetailResult.getString("shoeUpImg");
                            String platNumber = shoeDetailResult.getString("platNumber");

                         /*   //获取畅行无忧价格  随着年限改变
                            List<CxwyYear> cxwyYearList = new ArrayList<CxwyYear>();

                            JSONObject cxwyPriceMap = shoeDetailResult.getJSONObject("cxwyPriceMap");
                            for (int j = 1; j < 16; j++) {
                                JSONObject cxwyYearObject = cxwyPriceMap.getJSONObject(j + "");
                                List<CxwyPrice> cxwyPriceList = new ArrayList<CxwyPrice>();
                                Log.e(TAG, "onSuccess:cxwyYear--------------- " + j);
                                for (int k = 1; k < 8; k++) {
                                    String cxwyTimesPrice = cxwyYearObject.getString(k + "");
                                    Log.e(TAG, "onSuccess: cxwyTimesPrice ---" + cxwyTimesPrice );
                                    cxwyPriceList.add(new CxwyPrice(k,cxwyTimesPrice));
                                }
                                cxwyYearList.add(new CxwyYear(j,cxwyPriceList));
                            }*/
                            //畅行无忧
                            JSONArray cxwyPriceParamList = shoeDetailResult.getJSONArray("cxwyPriceParamList");
                            List<CxwyTimesPrice> cxwyTimesPriceList = new ArrayList<CxwyTimesPrice>();
                            for (int j = 0; j < cxwyPriceParamList.length(); j++) {
                                JSONObject cxwyObject = cxwyPriceParamList.getJSONObject(j);
                                String rate = cxwyObject.getString("rate");
                                int id = cxwyObject.getInt("id");
                                int times = cxwyObject.getInt("times");
                                cxwyTimesPriceList.add(new CxwyTimesPrice(id,rate,times));
                            }
                            //
                            JSONArray shoeSpeedLoadResultList = tireObject.getJSONArray("shoeSpeedLoadResultList");
                            List<TireRank> tireRankList = new ArrayList<TireRank>();
                            for (int j = 0; j < shoeSpeedLoadResultList.length(); j++) {
                                JSONObject object = shoeSpeedLoadResultList.getJSONObject(j);
                                String speedLoadStr = object.getString("speedLoadStr");
                                String rankName = speedLoadStr.substring(0, speedLoadStr.indexOf("￥")-1);
                                String shoeId = object.getString("shoeId");
                                JSONObject priceMap = object.getJSONObject("priceMap");
                                List<TirePrice> tirePriceList = new ArrayList<TirePrice>();
                                for (int k = 1; k <= 15; k++) {
                                    String price = priceMap.getString(k + "");
                                    TirePrice tirePrice = new TirePrice(k, Integer.parseInt(price));
                                    tirePriceList.add(tirePrice);
                                }

                                //获取畅行无忧价格  随着年限改变
                                List<CxwyYear> cxwyYearList = new ArrayList<CxwyYear>();

                                JSONObject cxwyPriceMap = object.getJSONObject("cxwyPriceMap");
                                for (int f = 1; f < 16; f++) {
                                    JSONObject cxwyYearObject = cxwyPriceMap.getJSONObject(f + "");
                                    List<CxwyPrice> cxwyPriceList = new ArrayList<CxwyPrice>();
                                    Log.e(TAG, "onSuccess:cxwyYear--------------- " + f);
                                    for (int g = 1; g < 8; g++) {
                                        String cxwyTimesPrice = cxwyYearObject.getString(g + "");
                                        Log.e(TAG, "onSuccess: cxwyTimesPrice ---" + cxwyTimesPrice );
                                        cxwyPriceList.add(new CxwyPrice(g,cxwyTimesPrice));
                                    }
                                    cxwyYearList.add(new CxwyYear(f,cxwyPriceList));
                                }
                                tireRankList.add(new TireRank(shoeId,rankName,tirePriceList,cxwyYearList));
                            }
                            tireInfoList.add(new TireInfo(description,imgLeftUrl,imgMiddleUrl,imgRightUrl,shoeDownImg,shoeLeftImg,shoeMiddleImg,shoeRightImg,shoeUpImg,detailStr,
                                    figure,shoeBasePrice,shoeFlgureName,cxwyTimesPriceList,tireRankList,platNumber));

                            initData();
                            initFigureFlow();
                            initRangkFlow();

                        }
                    }else if (status.equals("-999")) {
                        showUserTokenDialog("您的账号在其它设备登录,请重新登录");
                    } else {
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(TireBuyNewActivity.this, "网络连接失败，请检查网络链接", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                hideDialogProgress(progressDialog);
            }
        });
    }

    private void initView() {
        tirePostButton = (TextView) findViewById(R.id.tire_count_new_button);
        tireRankChooseLayout = (FrameLayout) findViewById(R.id.tire_rank_choose_layout);
        cxwyCountText = (TextView) findViewById(R.id.cxwy_count_text);
        cxwyPriceText = (TextView) findViewById(R.id.cxwy_price_text);
        tireRankCountText = (TextView) findViewById(R.id.tire_choose_rank_count_text);
        cxwyChooseLayout = (LinearLayout) findViewById(R.id.cxwy_choose_count_new_layout);
        mBanner = (CustomBanner) findViewById(R.id.car_count_banner_new);
        shoeTitleText = (TextView) findViewById(R.id.tire_name_new_text);
        tirePriceText = (TextView) findViewById(R.id.tire_price_new_text);
        liuchengLayout = (FrameLayout) findViewById(R.id.tire_liucheng_new_layout);
        liuchengContentLayout = (ImageView) findViewById(R.id.liucheng_content_layout);
        fuwuLayout = (FrameLayout) findViewById(R.id.fuwu_layout);
        fuwuContentLayout = (LinearLayout) findViewById(R.id.fuwu_content_layout);

        RxViewAction.clickNoDouble(tirePostButton)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (tireCount == 0 || !isChooseRank ||!isChoose){
                           // Toast.makeText(TireBuyNewActivity.this, "请选择轮胎规格", Toast.LENGTH_SHORT).show();
                            initDataTireDialog();
                            shopDialog.show();
                            return;
                        }
                        User user = new DbConfig(getApplicationContext()).getUser();
                        TireInfo tireInfo = tireInfoList.get(currentTirePostition);
                        String platNumber = tireInfo.getPlatNumber();
                        String imgMiddleUrl = tireInfo.getImgMiddleUrl();
                        String shoeId = tireInfo.getTireRankList().get(currentRankPostition).getShoeId();

                        Intent intent = new Intent(getApplicationContext(), OrderAffirmActivity.class);
                        intent.putExtra("FONTREARFLAG", fontrearflag);   //前后轮标识
                        intent.putExtra("TIRECOUNT", tireCount);//轮胎数量
                        intent.putExtra("TIREPRICE", tirePrice+"");     //轮胎单价
                        intent.putExtra("TIREPNAME", shoeTitle);  //轮胎名称
                        intent.putExtra("CXWYCOUNT", cxwwyCount);  //畅行无忧数量
                        if (cxwyAllPrice!=null){
                            intent.putExtra("CXWYPRICE", cxwyAllPrice);  //畅行无忧名称
                        }else {
                            intent.putExtra("CXWYPRICE", "0.0");  //畅行无忧名称
                        }

                        intent.putExtra("USERNAME", user.getNick());  //用户名
                        intent.putExtra("USERPHONE", user.getPhone());  //手机号
                        intent.putExtra("CARNUMBER", platNumber);  //车牌号
                        intent.putExtra("TIREIMAGE", imgMiddleUrl);  //轮胎图片
                        intent.putExtra("SHOEID", Integer.parseInt(shoeId));  //轮胎id
                        intent.putExtra("SERVICE_YEAR", currentServiceYear+"");  //轮胎id
                        startActivity(intent);

                    }
                });

        RxViewAction.clickNoDouble(liuchengLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        liuchengDialog.show();
                    }
                });

        RxViewAction.clickNoDouble(liuchengContentLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        liuchengDialog.show();
                    }
                });

        RxViewAction.clickNoDouble(tireRankChooseLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        initDataTireDialog();
                        shopDialog.show();
                    }
                });

        RxViewAction.clickNoDouble(fuwuLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        dialog.show();
                    }
                });

        RxViewAction.clickNoDouble(fuwuContentLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        dialog.show();
                    }
                });



        /**
         * 品质服务dialog
         */
        dialog = new Dialog(this, R.style.ActionSheetDialogStyle);
        inflate = LayoutInflater.from(this).inflate(R.layout.dialog_fuwu, null);
        inflate.setMinimumWidth(10000);
        huanxinLayout = ((LinearLayout) inflate.findViewById(R.id.huanxin_layout));
        dingweiLayout = ((LinearLayout) inflate.findViewById(R.id.dingwei_layout));
        butaiLayout = ((LinearLayout) inflate.findViewById(R.id.butai_layout));
        dialog.setContentView(inflate);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setAttributes(lp);
        dialog.setCanceledOnTouchOutside(true);

        /**
         * 更换流程ialog
         */
        liuchengDialog = new Dialog(this, R.style.ActionSheetDialogStyle);
        liuchengInflate = LayoutInflater.from(this).inflate(R.layout.dialog_liucheng,null);
        liuchengInflate.setMinimumWidth(10000);
        liuchengImage = ((ImageView) liuchengInflate.findViewById(R.id.liucheng_image_dialog));
        liuchengLayout11 = ((LinearLayout) liuchengInflate.findViewById(R.id.liucheng_layout));
        liuchengDialog.setContentView(liuchengInflate);
        Window liuchengDialogWindow = liuchengDialog.getWindow();
        liuchengDialogWindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams liuchengLp = liuchengDialogWindow.getAttributes();
        liuchengDialogWindow.setAttributes(liuchengLp);
        liuchengDialog.setCanceledOnTouchOutside(true);


        /**
         * 商品的dialog
         */
        shopDialog = new Dialog(this, R.style.ActionSheetDialogStyle);
        shopInflate = LayoutInflater.from(this).inflate(R.layout.dialog_shop,null);
        shopInflate.setMinimumWidth(10000);
        tireImageView = ((ImageView) shopInflate.findViewById(R.id.tire_image_view));
        tirePriceDialogText = ((TextView) shopInflate.findViewById(R.id.tire_price_dialog_text));
        tireFigureRankDialgText = ((TextView) shopInflate.findViewById(R.id.tire_figure_rank_dialog_text));
        tireRankFlow = ((TagFlowLayout) shopInflate.findViewById(R.id.tire_rank_flow));
        tireFigureFlow = ((TagFlowLayout) shopInflate.findViewById(R.id.tire_figure_flow));
        yearChooseSeekBar = ((TSeekBar) shopInflate.findViewById(R.id.seekbar_year_choose));
        serviceYearText = (TextView) shopInflate.findViewById(R.id.service_year_text);
        tireAmountView = (AmountView) shopInflate.findViewById(R.id.amount_view);
        cxwyAmountView = (AmountView) shopInflate.findViewById(R.id.changxingwuyou_count);
        cxwyPriceDIalogText = (TextView) shopInflate.findViewById(R.id.cxwy_price__dialog_text);
        tireChooseButton = (TextView) shopInflate.findViewById(R.id.tire_choose_button);
        serviceYearView = shopInflate.findViewById(R.id.service_year_view);
        if (isChooseServiceYear){
            currentServiceYear = Integer.parseInt(serviceYear);
            currentServiceYearChange = Integer.parseInt(serviceYear);
            yearChooseSeekBar.setVisibility(View.GONE);
            serviceYearView.setVisibility(View.GONE);
            serviceYearText.setVisibility(View.GONE);
        }else {
          //  currentServiceYearChange = Integer.parseInt(serviceYearMax) + 1;
            yearChooseSeekBar.setVisibility(View.VISIBLE);
            serviceYearView.setVisibility(View.VISIBLE);                                   
            serviceYearText.setVisibility(View.VISIBLE);

            yearChooseSeekBar.setMax(Integer.parseInt(serviceYearMax));
           // yearChooseSeekBar.setProgress(100);
        }
        tireFigureFlow.setMaxSelectCount(1);
        tireRankFlow.setMaxSelectCount(1);
        shopDialog.setContentView(shopInflate);
        Window shopDialogWindow = shopDialog.getWindow();
        shopDialogWindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams shopLp = shopDialogWindow.getAttributes();
        WindowManager wm = (WindowManager) this
                .getSystemService(Context.WINDOW_SERVICE);
        int height = wm.getDefaultDisplay().getHeight();
        shopLp.height = (int) (height * 0.8);
        shopDialogWindow.setAttributes(shopLp);
        shopDialog.setCanceledOnTouchOutside(true);

        tireFigureFlow.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                return true;
            }
        });

        tireFigureFlow.setOnSelectListener(new TagFlowLayout.OnSelectListener() {
            @Override
            public void onSelected(Set<Integer> selectPosSet) {
                for (Integer str : selectPosSet) {
                    currentTireChangePostition = str.intValue();
                    currentRankChangePostition = 0;
                    isChooseRank = false;
                    isChoose = false;
                    Log.e(TAG, "onSelected:currentTireChangePostition---- " + currentTireChangePostition);
                    initDataTireDialog();
                    initRangkFlow();
                }
            }
        });

        tireRankFlow.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                return true;
            }
        });

        /**
         * 速度级别选择
         */
        tireRankFlow.setOnSelectListener(new TagFlowLayout.OnSelectListener() {
            @Override
            public void onSelected(Set<Integer> selectPosSet) {
                for (Integer str : selectPosSet) {
                    currentRankChangePostition = str.intValue();
                    isChooseRank = true;
                    Log.e(TAG, "onSelected: currentRankChangePostition" + currentRankChangePostition);
                    initDialogRank();

                    initCxwyPrice();
                }
            }
        });

        yearChooseSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            /**
             * 拖动条进度改变的时候调用
             * @param seekBar
             * @param i
             * @param b
             */
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            /**
             * 拖动条开始时调用
             * @param seekBar
             */
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            /**
             * 拖动条停止时调用
             * @param seekBar
             */
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                currentServiceYearChange = yearChooseSeekBar.currentYear();
                initDialogTirePrice();
                initCxwyPrice();
            }
        });


        tireAmountView.setGoods_storage(maxTireCount);
        tireAmountView.setOnAmountChangeListener(new AmountView.OnAmountChangeListener() {
            @Override
            public void onAmountChange(View view, int amount) {
                if (amount == maxTireCount) {
                    Toast.makeText(TireBuyNewActivity.this, "轮胎数量已达到购买上限", Toast.LENGTH_SHORT).show();
                }
                tireCount = amount;
            }
        });

        cxwyAmountView.setGoods_storage(maxCount);
        cxwyAmountView.setOnAmountChangeListener(new AmountView.OnAmountChangeListener() {
            @Override
            public void onAmountChange(View view, int amount) {
                TireInfo tireInfo = tireInfoList.get(currentTireChangePostition);
                List<CxwyYear> cxwyYearList = tireInfo.getTireRankList().get(currentRankPostition).getCxwyYearList();
             //   List<CxwyYear> cxwyYearList = tireInfo.getCxwyYearList();

                for (int i = 0; i < cxwyYearList.size(); i++) {
                    int serviceYear = cxwyYearList.get(i).getServiceYear();
                    Log.e(TAG, "onAmountChange:+++++++++ " + serviceYear);
                    List<CxwyPrice> cxwyPriceList = cxwyYearList.get(i).getCxwyPriceList();
                    for (int j = 0; j < cxwyPriceList.size(); j++) {

                        Log.e(TAG, "onAmountChange: +++" + cxwyPriceList.get(j).getCxwyPrice() );
                    }
                }
                List<CxwyPrice> cxwyPriceList = new ArrayList<CxwyPrice>();
                Log.e(TAG, "onAmountChange:currentServiceYearChange ---" + currentServiceYearChange);
                Log.e(TAG, "onAmountChange:currentServiceYear--- " + currentServiceYear);
                cxwyPriceList.clear();
                for (int i = 0; i < cxwyYearList.size(); i++) {
                    if (cxwyYearList.get(i).getServiceYear() == currentServiceYearChange) {
                        cxwyPriceList = cxwyYearList.get(i).getCxwyPriceList();
                    }
                }

                if (amount == maxCount) {
                    Toast.makeText(TireBuyNewActivity.this, "畅行无忧已达到购买上限", Toast.LENGTH_SHORT).show();
                }
                cxwwyCount = amount;
                if (amount == 0){
                    cxwyAllPrice = 0 + "";
                }else {
                    for (int i = 0; i < cxwyPriceList.size(); i++) {
                        Log.e(TAG, "onAmountChange:--- " + cxwyPriceList.get(i).getCxwyPrice() );
                        if (cxwyPriceList.get(i).getTimes() == cxwwyCount) {
                            String cxwyPrice = cxwyPriceList.get(i).getCxwyPrice();
                            cxwyAllPrice = cxwyPrice;
                        }
                    }
                }
                cxwyPriceDIalogText.setText("￥" + cxwyAllPrice);

                //老畅行无忧计算方法
                /*TireInfo tireInfo = tireInfoList.get(currentTireChangePostition);
                String shoeBasePrice = tireInfo.getShoeBasePrice();
                List<CxwyTimesPrice> cxwyList = tireInfo.getCxwyTimesPriceList();
                if (amount == maxCount) {
                    Toast.makeText(TireBuyNewActivity.this, "畅行无忧已达到购买上限", Toast.LENGTH_SHORT).show();
                }
                cxwwyCount = amount;
                if (amount == 0){
                    cxwyAllPrice = 0 + "";
                }else {

                    for (int i = 0; i < cxwyList.size(); i++) {
                        if (cxwyList.get(i).getTimes() == cxwwyCount) {
                            double price = Double.parseDouble(cxwyList.get(i).getRate()) * Double.parseDouble(shoeBasePrice) / 100;
                            String format = new DecimalFormat("0").format(price);
                            cxwyAllPrice = format + "";
                        }
                    }
                }

                cxwyPriceDIalogText.setText("￥" + cxwyAllPrice);*/

            }
        });

        //提交选择轮胎数据
        RxViewAction.clickNoDouble(tireChooseButton)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (!isChooseRank){
                         /*   initDataTireDialog();
                            shopDialog.show();*/
                           Toast.makeText(TireBuyNewActivity.this, "请选择轮胎规格", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (tireCount == 0){
                            /*initDataTireDialog();
                            shopDialog.show();*/
                           Toast.makeText(TireBuyNewActivity.this, "请选择轮胎数量", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        currentTirePostition =  currentTireChangePostition;
                        currentRankPostition = currentRankChangePostition;
                        currentServiceYear = currentServiceYearChange;
                        Log.e(TAG, "call: ------------" + currentServiceYear);

                        shopDialog.dismiss();
                        isChoose = true;
                        initData();
                    }
                });

    }

    /**
     *  选择完年限  速度级别时更改畅行无忧价格
     */
    private void initCxwyPrice() {
        TireInfo tireInfo = tireInfoList.get(currentTireChangePostition);
       // List<CxwyYear> cxwyYearList = tireInfo.getCxwyYearList();
        List<CxwyYear> cxwyYearList = tireInfo.getTireRankList().get(currentRankPostition).getCxwyYearList();

        for (int i = 0; i < cxwyYearList.size(); i++) {
            int serviceYear = cxwyYearList.get(i).getServiceYear();
            Log.e(TAG, "onAmountChange:+++++++++ " + serviceYear);
            List<CxwyPrice> cxwyPriceList = cxwyYearList.get(i).getCxwyPriceList();
            for (int j = 0; j < cxwyPriceList.size(); j++) {

                Log.e(TAG, "onAmountChange: +++" + cxwyPriceList.get(j).getCxwyPrice() );
            }
        }
        List<CxwyPrice> cxwyPriceList = new ArrayList<CxwyPrice>();
        Log.e(TAG, "onAmountChange:currentServiceYearChange ---" + currentServiceYearChange);
        Log.e(TAG, "onAmountChange:currentServiceYear--- " + currentServiceYear);
        cxwyPriceList.clear();
        for (int i = 0; i < cxwyYearList.size(); i++) {
            if (cxwyYearList.get(i).getServiceYear() == currentServiceYearChange) {
                cxwyPriceList = cxwyYearList.get(i).getCxwyPriceList();
            }
        }

        if (cxwwyCount == 0) {
            cxwyAllPrice = "0";
        }else {
            for (int i = 0; i < cxwyPriceList.size(); i++) {
                Log.e(TAG, "onAmountChange:--- " + cxwyPriceList.get(i).getCxwyPrice() );
                if (cxwyPriceList.get(i).getTimes() == cxwwyCount) {
                    String cxwyPrice = cxwyPriceList.get(i).getCxwyPrice();
                    cxwyAllPrice = cxwyPrice;
                }
            }
        }


        cxwyPriceDIalogText.setText("￥" + cxwyAllPrice);
    }
}
