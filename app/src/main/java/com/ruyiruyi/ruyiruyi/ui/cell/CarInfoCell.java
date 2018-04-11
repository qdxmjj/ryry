package com.ruyiruyi.ruyiruyi.ui.cell;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ruyiruyi.ruyiruyi.R;
import com.ruyiruyi.rylibrary.utils.LayoutHelper;

public class CarInfoCell extends FrameLayout {
    private ImageView imageView;
    private FrameLayout contont;
    private LinearLayout textLayout;
    private TextView titleText;
    private TextView carNumberText;
    private ImageView morenView;

    public CarInfoCell(Context context) {
        super(context);
        initView(context);
    }


    public CarInfoCell(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public CarInfoCell(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }
    private void initView(Context context) {

        contont = new FrameLayout(context);
        addView(contont,LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT,300));

        imageView = new ImageView(context);
        contont.addView(imageView, LayoutHelper.createFrame(200,200, Gravity.CENTER_VERTICAL,40,0,0,0));

        textLayout = new LinearLayout(context);
        textLayout.setOrientation(LinearLayout.VERTICAL);
        contont.addView(textLayout,LayoutHelper.createFrame(LayoutHelper.WRAP_CONTENT,LayoutHelper.WRAP_CONTENT,Gravity.CENTER_VERTICAL,300 ,0,0,0));

        titleText = new TextView(context);
        titleText.setTextColor(getResources().getColor(R.color.c6));
        titleText.setTextSize(16);
        textLayout.addView(titleText,LayoutHelper.createLinear(LayoutHelper.WRAP_CONTENT,LayoutHelper.WRAP_CONTENT));

        carNumberText = new TextView(context);
        carNumberText.setTextColor(getResources().getColor(R.color.c5));
        carNumberText.setTextSize(15);
        textLayout.addView(carNumberText,LayoutHelper.createLinear(LayoutHelper.WRAP_CONTENT,LayoutHelper.WRAP_CONTENT,0,10,0,0));

        morenView = new ImageView(context);
        morenView.setImageResource(R.drawable.ic_moren);
        contont.addView(morenView,LayoutHelper.createFrame(LayoutHelper.WRAP_CONTENT,LayoutHelper.WRAP_CONTENT,Gravity.RIGHT|Gravity.TOP));
    }

    public void setValue(String icon,String title,String carBumber,int morenCar){
        Glide.with(getContext()).load(icon).into(imageView);
        titleText.setText(title);
        carNumberText.setText(carBumber);
        if (morenCar == 1){
            morenView.setVisibility(VISIBLE);
        }else {
            morenView.setVisibility(GONE);
        }
    }


}