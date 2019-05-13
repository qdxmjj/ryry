package com.ruyiruyi.ruyiruyi.ui.cell;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
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
    private LinearLayout proveLayout;
    private TextView titleText;
    private LinearLayout proveInLayout;
    private ImageView proveIcon;
    private TextView proveText;
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
        addView(contont, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, 300));

        imageView = new ImageView(context);
        imageView.setImageResource(R.mipmap.ic_logo);
        contont.addView(imageView, LayoutHelper.createFrame(200, 200, Gravity.CENTER_VERTICAL, 40, 0, 0, 0));

        textLayout = new LinearLayout(context);
        textLayout.setOrientation(LinearLayout.VERTICAL);
        contont.addView(textLayout, LayoutHelper.createFrame(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.CENTER_VERTICAL, 300, 0, 0, 0));

        proveLayout = new LinearLayout(context);
        proveLayout.setOrientation(LinearLayout.HORIZONTAL);
        textLayout.addView(proveLayout, LayoutHelper.createLinear(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT));


        titleText = new TextView(context);
        titleText.setTextColor(getResources().getColor(R.color.c6));
        titleText.setTextSize(16);
        titleText.setText("请选择车型");
        proveLayout.addView(titleText, LayoutHelper.createLinear(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.CENTER_VERTICAL));


        proveInLayout = new LinearLayout(context);
        proveInLayout.setOrientation(LinearLayout.HORIZONTAL);
        proveLayout.addView(proveInLayout, LayoutHelper.createLinear(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.CENTER_VERTICAL, 10, 0, 0, 0));

        proveIcon = new ImageView(context);
        proveInLayout.addView(proveIcon, LayoutHelper.createLinear(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.CENTER_VERTICAL, 16, 6, 0, 6));
        proveText = new TextView(context);
        proveText.setTextSize(12);
        proveInLayout.addView(proveText, LayoutHelper.createLinear(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.CENTER_VERTICAL, 3, 0, 16, 0));

        carNumberText = new TextView(context);
        carNumberText.setTextColor(getResources().getColor(R.color.c5));
        carNumberText.setTextSize(15);
        textLayout.addView(carNumberText, LayoutHelper.createLinear(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, 0, 10, 0, 0));

        morenView = new ImageView(context);
        morenView.setImageResource(R.drawable.ic_moren);
        contont.addView(morenView, LayoutHelper.createFrame(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.RIGHT | Gravity.TOP));
    }

    public void setValue(String icon, String title, String carBumber, int morenCar, int proveStatus) {
        if (icon.isEmpty()) {
            imageView.setImageResource(R.drawable.ic_dairenzheng);
        } else {
            Glide.with(getContext()).load(icon).into(imageView);
        }
        if (title.isEmpty()) {
            title = "待选择车型";
        }
        titleText.setText(title);
        carNumberText.setText(carBumber);
        if (morenCar == 1) {
            morenView.setVisibility(VISIBLE);
        } else {
            morenView.setVisibility(GONE);
        }
        Log.e("check_proveStatus", "setValue: " + proveStatus);
        if (proveStatus == 2) { //是否进行车主认证 (1 已认证 2 未认证)
            proveIcon.setImageResource(R.drawable.ic_weirz);
            proveInLayout.setBackgroundResource(R.drawable.bg_noprove);
            proveText.setTextColor(getResources().getColor(R.color.c6));
            proveText.setText("未认证");
        } else {
            proveIcon.setImageResource(R.drawable.ic_yirz);
            proveInLayout.setBackgroundResource(R.drawable.bg_hasprove);
            proveText.setTextColor(getResources().getColor(R.color.theme_primary));
            proveText.setText("已认证");
        }
    }


}