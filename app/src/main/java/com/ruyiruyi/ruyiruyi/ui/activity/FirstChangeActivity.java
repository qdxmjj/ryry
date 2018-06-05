package com.ruyiruyi.ruyiruyi.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ruyiruyi.ruyiruyi.R;
import com.ruyiruyi.ruyiruyi.ui.activity.base.RyBaseActivity;
import com.ruyiruyi.ruyiruyi.ui.model.StoreType;
import com.ruyiruyi.rylibrary.cell.ActionBar;
import com.ruyiruyi.rylibrary.cell.flowlayout.FlowLayout;
import com.ruyiruyi.rylibrary.cell.flowlayout.TagAdapter;
import com.ruyiruyi.rylibrary.cell.flowlayout.TagFlowLayout;
import com.ruyiruyi.rylibrary.ui.viewpager.CustomBanner;

import java.util.ArrayList;
import java.util.List;

public class FirstChangeActivity extends RyBaseActivity {
    private ActionBar actionBar;
    private TagFlowLayout typeFlowLayout;
    private String[] mVals = new String[]
            {"快修店", "4S", "美容 ", "清洗", "轮胎"};

    public List<StoreType> typeList;
    private CustomBanner mBanner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_change,R.id.my_action);
        actionBar = (ActionBar) findViewById(R.id.my_action);
        actionBar.setTitle("首次更换");;
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

    }
}
