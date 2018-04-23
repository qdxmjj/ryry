package com.ruyiruyi.ruyiruyi.ui.cell;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ruyiruyi.ruyiruyi.R;
import com.ruyiruyi.ruyiruyi.ui.model.StoreType;
import com.ruyiruyi.rylibrary.cell.flowlayout.FlowLayout;
import com.ruyiruyi.rylibrary.cell.flowlayout.TagAdapter;
import com.ruyiruyi.rylibrary.cell.flowlayout.TagFlowLayout;
import com.ruyiruyi.rylibrary.utils.AndroidUtilities;
import com.ruyiruyi.rylibrary.utils.LayoutHelper;

import java.util.List;

public class ShopChooseCell extends LinearLayout{
    private LinearLayout content;
    private FrameLayout topLayout;
    private TextView titleView;
    private ImageView goView;
    private LinearLayout shopLayout;
    private ImageView shopImage;
    private LinearLayout rightLayout;
    private TextView shopNameView;
    private TagFlowLayout tagFlowLayout;
    private FrameLayout locationLayout;
    private TextView addressView;
    private TextView distenceView;
    public Context context;

    public ShopChooseCell(Context context) {
        super(context);
        this.context = context;
        initView(context);
    }

    public ShopChooseCell(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView(context);
    }

    public ShopChooseCell(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initView(context);
    }

    private void initView(Context context) {
        content = new LinearLayout(context);
        content.setOrientation(VERTICAL);
        addView(content, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT,LayoutHelper.WRAP_CONTENT,0,20,0,0));

        topLayout = new FrameLayout(context);
        content.addView(topLayout,LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT,LayoutHelper.WRAP_CONTENT));
        titleView = new TextView(context);
        titleView.setText("安装门店");
        titleView.setTextSize(17);
        titleView.setTextColor(getResources().getColor(R.color.c7));
        topLayout.addView(titleView,LayoutHelper.createFrame(LayoutHelper.WRAP_CONTENT,LayoutHelper.WRAP_CONTENT, Gravity.CENTER_VERTICAL,60,0,0,0));

        goView = new ImageView(context);
        goView.setImageResource(R.drawable.ic_go);
        topLayout.addView(goView,LayoutHelper.createFrame(LayoutHelper.WRAP_CONTENT,LayoutHelper.WRAP_CONTENT,Gravity.RIGHT|Gravity.CENTER_VERTICAL,0,0,60,0));

        shopLayout = new LinearLayout(context);
        shopLayout.setPadding(50,60,50,60);
        shopLayout.setOrientation(HORIZONTAL);
        content.addView(shopLayout,LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT,LayoutHelper.WRAP_CONTENT));

        shopImage = new ImageView(context);
        shopLayout.addView(shopImage,LayoutHelper.createLinear(AndroidUtilities.dp(300),AndroidUtilities.dp(300)));

        rightLayout = new LinearLayout(context);
        rightLayout.setOrientation(VERTICAL);
        shopLayout.addView(rightLayout,LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT,LayoutHelper.WRAP_CONTENT,40,0,0,0));

        shopNameView = new TextView(context);
        shopNameView.setTextSize(16);
        shopNameView.setSingleLine();
        shopNameView.setEllipsize(TextUtils.TruncateAt.END);
        shopNameView.setTextColor(getResources().getColor(R.color.c7));
        rightLayout.addView(shopNameView,LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT,LayoutHelper.WRAP_CONTENT));

        tagFlowLayout = new TagFlowLayout(context);
        rightLayout.addView(tagFlowLayout,LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT,LayoutHelper.WRAP_CONTENT,0,20,0,0));

        locationLayout = new FrameLayout(context);
        rightLayout.addView(locationLayout,LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT,LayoutHelper.WRAP_CONTENT,0,20,0,0));
        addressView = new TextView(context);
        addressView.setTextSize(14);
        addressView.setTextColor(getResources().getColor(R.color.c5));
        addressView.setSingleLine();
        addressView.setEllipsize(TextUtils.TruncateAt.END);
        locationLayout.addView(addressView,LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT,LayoutHelper.WRAP_CONTENT,Gravity.CENTER_VERTICAL,0,0,70,0));

        distenceView = new TextView(context);
        distenceView.setTextSize(14);
        distenceView.setTextColor(getResources().getColor(R.color.c5));
        locationLayout.addView(distenceView,LayoutHelper.createFrame(LayoutHelper.WRAP_CONTENT,LayoutHelper.WRAP_CONTENT,Gravity.CENTER_VERTICAL|Gravity.RIGHT));
    }

    public void setValue(String shopNameStr, String shopImageStr, String shopAddress, String shopDistence, List<StoreType> typeList, final LayoutInflater mInflater){
        shopNameView.setText(shopNameStr);
        Glide.with(context).load(shopImageStr).into(shopImage);
        addressView.setText(shopAddress);
        distenceView.setText(shopDistence);

        tagFlowLayout.setAdapter(new TagAdapter<StoreType>(typeList) {
            @Override
            public View getView(FlowLayout parent, int position, StoreType storeType) {
                TextView tv = (TextView) mInflater.inflate(R.layout.item_type,
                        tagFlowLayout, false);
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
    }


}