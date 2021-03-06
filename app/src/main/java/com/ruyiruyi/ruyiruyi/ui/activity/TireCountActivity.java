package com.ruyiruyi.ruyiruyi.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ruyiruyi.ruyiruyi.R;
import com.ruyiruyi.ruyiruyi.db.DbConfig;
import com.ruyiruyi.ruyiruyi.ui.activity.base.RyBaseActivity;
import com.ruyiruyi.ruyiruyi.ui.model.CxwyTimesPrice;
import com.ruyiruyi.ruyiruyi.utils.RequestUtils;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;
import com.ruyiruyi.rylibrary.cell.ActionBar;
import com.ruyiruyi.rylibrary.cell.AmountView;
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

import rx.functions.Action1;

public class TireCountActivity extends RyBaseActivity {
    private static final String TAG = TireCountActivity.class.getSimpleName();
    private ActionBar actionBar;
    private CustomBanner mBanner;
    private String price;
    private AmountView tireAmountView;
    public static int maxCount = 7;
    public static int maxTireCount = 4;
    public int tireCurrentCount = 0;
    public int cxwyCurrentCount = 0;
    private AmountView cxwyAmountView;
    private TextView tireCountButton;
    private TextView tirePriceText;
    private TextView tireNameText;
    private String fontrearflag;//0是一致  1前轮 2后轮
    private String tireSize;
    private String cxwyPrice = 0+"";
    private int shoeId;
    private String tireName;
    private String tireImage;
    private String carNumber;
    private String userPhone;
    private String userName;
    private TextView tireLocationText;
    private TextView cxwyPriceText;
    public List<CxwyTimesPrice> cxwyList;
    private String shoeBasePrice;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tire_count, R.id.my_action);
        actionBar = (ActionBar) findViewById(R.id.my_action);
        actionBar.setTitle("轮胎购买");
        ;
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

        cxwyList = new ArrayList<>();
        Intent intent = getIntent();
        shoeId = intent.getIntExtra("SHOEID", 0);
        price = intent.getStringExtra("PRICE");
        fontrearflag = intent.getStringExtra("FONTREARFLAG");
        if (fontrearflag.equals("0")){
            maxTireCount = 4;
        }else {
            maxTireCount = 2;
        }
        Log.e(TAG, "onCreate: --------*---------" + shoeId);

        initView();

        initDataFromService();
    }

    private void initDataFromService() {
        int userId = new DbConfig(this).getId();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("shoeId", shoeId);
            jsonObject.put("userId", userId);
        } catch (JSONException e) {
        }
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "getShoeDetailByShoeId");
        params.addBodyParameter("reqJson", jsonObject.toString());
        String token = new DbConfig(this).getToken();
        params.addParameter("token", token);
        Log.e(TAG, "initDataFromService:------- " + params.toString());
        x.http().post(params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess:------ " + result);
                try {
                    JSONObject jsonObject1 = new JSONObject(result);
                    jsonObject1 = new JSONObject(result);
                    String status = jsonObject1.getString("status");
                    String msg = jsonObject1.getString("msg");
                    if (status.equals("1")) {
                        JSONObject data = jsonObject1.getJSONObject("data");
                        tireName = data.getString("detailStr");
                      //  cxwyPrice = data.getString("finalCxwyPrice");
                        userName = data.getString("userName");
                        userPhone = data.getString("userPhone");
                        carNumber = data.getString("platNumber");
                        tireImage = data.getString("shoeDownImg");
                        shoeBasePrice = data.getString("shoeBasePrice");
                        String image2 = data.getString("shoeLeftImg");
                        String image3 = data.getString("shoeMiddleImg");
                        String image4 = data.getString("shoeRightImg");
                        String image5 = data.getString("shoeUpImg");
                        tireSize = data.getString("size");
                        List<String> imageList = new ArrayList<>();
                        imageList.add(tireImage);
                        imageList.add(image2);
                        imageList.add(image3);
                        imageList.add(image4);
                        imageList.add(image5);
                        //获取畅行无忧的价格
                        cxwyList.clear();
                        JSONArray cxwyPriceParamList = data.getJSONArray("cxwyPriceParamList");
                        for (int i = 0; i < cxwyPriceParamList.length(); i++) {
                            JSONObject object = cxwyPriceParamList.getJSONObject(i);
                            int id = object.getInt("id");
                            int times = object.getInt("times");
                            String rate = object.getString("rate");
                            CxwyTimesPrice cxwyTimesPrice = new CxwyTimesPrice(id, rate, times);
                            cxwyList.add(cxwyTimesPrice);
                        }
                        mBanner.setPages(new CustomBanner.ViewCreator<String>() {
                            @Override
                            public View createView(Context context, int position) {
                                ImageView imageView = new ImageView(context);
                                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                                return imageView;
                            }

                            @Override
                            public void updateUI(Context context, View view, int position, String entity) {
                                Glide.with(context).load(entity).into((ImageView) view);
                            }
                        }, imageList)
                                //设置自动翻页
                                .startTurning(5000);
                        tireNameText.setText(tireName);

                        cxwyPriceText.setText("￥" + cxwyPrice);

                    } else if (status.equals("-999")) {
                        showUserTokenDialog("您的账号在其它设备登录,请重新登录");
                    } else {
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
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

    private void initView() {
        mBanner = (CustomBanner) findViewById(R.id.car_count_banner);
        tireAmountView = (AmountView) findViewById(R.id.amount_view);
        cxwyAmountView = (AmountView) findViewById(R.id.changxingwuyou_count);
        tireCountButton = (TextView) findViewById(R.id.tire_count_button);
        tirePriceText = (TextView) findViewById(R.id.tire_price_text);
        tireNameText = (TextView) findViewById(R.id.tire_name_text);
        tireLocationText = (TextView) findViewById(R.id.tire_location_text);
        cxwyPriceText = (TextView) findViewById(R.id.cxwy_price_text);

        if (fontrearflag.equals("0")) {
            tireLocationText.setText("轮胎数量");
        } else if (fontrearflag.equals("1")) {
            tireLocationText.setText("前轮数量");
        } else {
            tireLocationText.setText("后轮数量");
        }

        tirePriceText.setText(price);

        tireAmountView.setGoods_storage(maxTireCount);
        tireAmountView.setOnAmountChangeListener(new AmountView.OnAmountChangeListener() {
            @Override
            public void onAmountChange(View view, int amount) {
                if (amount == maxTireCount) {
                    Toast.makeText(TireCountActivity.this, "轮胎数量已达到购买上限", Toast.LENGTH_SHORT).show();
                }
                tireCurrentCount = amount;
            }
        });

        cxwyAmountView.setGoods_storage(maxCount);
        cxwyAmountView.setOnAmountChangeListener(new AmountView.OnAmountChangeListener() {
            @Override
            public void onAmountChange(View view, int amount) {
                if (amount == maxCount) {
                    Toast.makeText(TireCountActivity.this, "畅行无忧已达到购买上限", Toast.LENGTH_SHORT).show();
                }
                cxwyCurrentCount = amount;
                if (amount == 0){
                    cxwyPrice = 0 + "";
                }else {

                    for (int i = 0; i < cxwyList.size(); i++) {
                        if (cxwyList.get(i).getTimes() == cxwyCurrentCount) {
                            double price = Double.parseDouble(cxwyList.get(i).getRate()) * Double.parseDouble(shoeBasePrice) / 100;
                            String format = new DecimalFormat("0").format(price);
                            cxwyPrice = format + "";
                        }
                    }
                }

                cxwyPriceText.setText("￥" + cxwyPrice);

            }
        });

        RxViewAction.clickNoDouble(tireCountButton)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (tireCurrentCount == 0) {
                            Toast.makeText(TireCountActivity.this, "你选择轮胎购买数量", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Intent intent = new Intent(getApplicationContext(), QcActivity.class);
                        intent.putExtra("FONTREARFLAG", fontrearflag);   //前后轮标识
                        intent.putExtra("TIRECOUNT", tireCurrentCount);//轮胎数量
                        intent.putExtra("TIREPRICE", price);     //轮胎单价
                        intent.putExtra("TIREPNAME", tireName);  //轮胎名称
                        intent.putExtra("CXWYCOUNT", cxwyCurrentCount);  //畅行无忧数量
                        intent.putExtra("CXWYPRICE", cxwyPrice);  //畅行无忧名称
                        intent.putExtra("USERNAME", userName);  //用户名
                        intent.putExtra("USERPHONE", userPhone);  //手机号
                        intent.putExtra("CARNUMBER", carNumber);  //车牌号
                        intent.putExtra("TIREIMAGE", tireImage);  //轮胎图片
                        intent.putExtra("SHOEID", shoeId);  //轮胎id
                        Log.e(TAG, "call: " + carNumber);


                        startActivity(intent);
                    }
                });


    }
}
