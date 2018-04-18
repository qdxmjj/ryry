package com.ruyiruyi.ruyiruyi.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ruyiruyi.ruyiruyi.R;
import com.ruyiruyi.ruyiruyi.ui.model.StoreType;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;
import com.ruyiruyi.rylibrary.base.BaseActivity;
import com.ruyiruyi.rylibrary.cell.ActionBar;
import com.ruyiruyi.rylibrary.cell.AmountView;
import com.ruyiruyi.rylibrary.cell.flowlayout.FlowLayout;
import com.ruyiruyi.rylibrary.cell.flowlayout.TagAdapter;
import com.ruyiruyi.rylibrary.cell.flowlayout.TagFlowLayout;
import com.ruyiruyi.rylibrary.ui.viewpager.CustomBanner;

import java.util.ArrayList;
import java.util.List;

import rx.functions.Action1;

public class TireChangeActivity extends BaseActivity {
    public final static String CHANGE_TIRE = "CHANGE_TIRE";
    public int currentChangeType = 0;  //0是首次更换  1是免费更换

    private ActionBar actionBar;
    private TagFlowLayout typeFlowLayout;
    private String[] mVals = new String[]
            {"快修店", "4S", "美容 ", "清洗", "轮胎"};

    public List<StoreType> typeList;
    private CustomBanner mBanner;
    private LinearLayout freeChangeLayout;
    public int currentFontCount = 0;
    public int currentRearCount = 0;
    public int hasFontCount = 0;
    public int hasRearCount = 0;

    private AmountView fontAmountView;
    private AmountView rearAmountView;
    private FrameLayout tireLiuchengLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tire_change,R.id.my_action);
        actionBar = (ActionBar) findViewById(R.id.my_action);

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
        currentChangeType = intent.getIntExtra(CHANGE_TIRE,0);

        if (currentChangeType == 0){
            actionBar.setTitle("首次更换");;
        }else if (currentChangeType == 1){
            actionBar.setTitle("免费再换");;
        }



        initData();

        initView();
    }
    private void initData() {
        typeList = new ArrayList<>();
        typeList.add(new StoreType(1,"快修店"));
        typeList.add(new StoreType(2,"4s"));
        typeList.add(new StoreType(3,"轮胎"));
        typeList.add(new StoreType(4,"轮胎"));
    }

    private void initView() {
        final LayoutInflater mInflater = LayoutInflater.from(this);
        typeFlowLayout = (TagFlowLayout) findViewById(R.id.type_flowlayout);
        mBanner = (CustomBanner) findViewById(R.id.tire_change_banner);
        freeChangeLayout = (LinearLayout) findViewById(R.id.free_change_layout);
        fontAmountView = (AmountView) findViewById(R.id.font_amount_view);
        rearAmountView = (AmountView) findViewById(R.id.area_amount_view);
        tireLiuchengLayout = (FrameLayout) findViewById(R.id.tire_liucheng_layout);

        freeChangeLayout.setVisibility(currentChangeType == 0? View.GONE:View.VISIBLE);

        fontAmountView.setGoods_storage(2);
        fontAmountView.setOnAmountChangeListener(new AmountView.OnAmountChangeListener() {
            @Override
            public void onAmountChange(View view, int amount) {
                if (currentChangeType == 0){//首次
                    if (amount == hasFontCount){
                        Toast.makeText(TireChangeActivity.this,"轮胎数量已达到购买上限", Toast.LENGTH_SHORT).show();
                    }
                    currentFontCount =amount;
                }else if(currentChangeType == 1){//mianfei
                    if (amount == 2){
                        Toast.makeText(TireChangeActivity.this,"轮胎数量已达到购买上限", Toast.LENGTH_SHORT).show();
                    }
                    currentFontCount =amount;
                }


            }
        });
        rearAmountView.setGoods_storage(2);
        rearAmountView.setOnAmountChangeListener(new AmountView.OnAmountChangeListener() {
            @Override
            public void onAmountChange(View view, int amount) {
                if (currentChangeType == 0){//首次
                    if (amount == hasRearCount){
                        Toast.makeText(TireChangeActivity.this,"轮胎数量已达到购买上限", Toast.LENGTH_SHORT).show();
                    }
                    currentRearCount =amount;
                }else if(currentChangeType == 1){//mianfei
                    if (amount == 2){
                        Toast.makeText(TireChangeActivity.this,"轮胎数量已达到购买上限", Toast.LENGTH_SHORT).show();
                    }
                    currentRearCount =amount;
                }


            }
        });



        List<String> imageList = new ArrayList<>();

        imageList.add("http://180.76.243.205:8111/images/Advertisement/cxwy.png");
        imageList.add("http://180.76.243.205:8111/images/Advertisement/cxwy1000.png");
        imageList.add("http://180.76.243.205:8111/images/Advertisement/cxwy1000.png");


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
        },imageList)//                //设置指示器为普通指示器
//                .setIndicatorStyle(CustomBanner.IndicatorStyle.ORDINARY)
//                //设置两个点图片作为翻页指示器，不设置则没有指示器，可以根据自己需求自行配合自己的指示器,不需要圆点指示器可用不设
//                .setIndicatorRes(R.drawable.shape_point_select, R.drawable.shape_point_unselect)
//                //设置指示器的方向
//                .setIndicatorGravity(CustomBanner.IndicatorGravity.CENTER)
//                //设置指示器的指示点间隔
//                .setIndicatorInterval(20)
                //设置自动翻页
                .startTurning(5000);


        typeFlowLayout.setAdapter(new TagAdapter<StoreType>(typeList) {


            @Override
            public View getView(FlowLayout parent, int position, StoreType storeType) {
                TextView tv = (TextView) mInflater.inflate(R.layout.item_type,
                        typeFlowLayout, false);
                tv.setText(storeType.getStoreName());
                if (storeType.getType() == 1){
                    tv.setBackgroundResource(R.drawable.tag_bg_hong);
                } else if (storeType.getType() == 2) {
                    tv.setBackgroundResource(R.drawable.tag_bg_huang);
                }else if (storeType.getType() == 3){
                    tv.setBackgroundResource(R.drawable.tag_bg_qing);
                }else if (storeType.getType() == 4){
                    tv.setBackgroundResource(R.drawable.tag_bg_lv);
                }
                return tv;
            }
        });
        typeFlowLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getActivity(), "FlowLayout Clicked", Toast.LENGTH_SHORT).show();
            }
        });

        RxViewAction.clickNoDouble(tireLiuchengLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        startActivity(new Intent(getApplicationContext(),TireLiuchengActivity.class));
                    }
                });

    }
}
