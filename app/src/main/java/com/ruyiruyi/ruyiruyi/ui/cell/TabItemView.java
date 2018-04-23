package com.ruyiruyi.ruyiruyi.ui.cell;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ruyiruyi.ruyiruyi.R;

public class TabItemView extends LinearLayout {

    /**
     * Item 的标题
     */
    public String title;

    public int textColor;
    public int iconDrawable;
    public int bgDrawable;

    public TextView tvTitle;
    public ImageView ivIcon;

    public TabItemView(Context context, String title, int textColor, int iconDrawable,
                       int bgDrawable) {
        super(context);
        this.title = title;
        this.iconDrawable = iconDrawable;
        this.textColor = textColor;
        this.bgDrawable = bgDrawable;
        init();
    }

    /**
     * 初始化
     */
    public void init(){
        View view = LayoutInflater.from(super.getContext()).inflate(R.layout.view_tab_item, this);
        tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        ivIcon = (ImageView) view.findViewById(R.id.ivIcon);

        LinearLayout.LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.weight = 1;
        view.setLayoutParams(layoutParams);

        if (iconDrawable!=0){
            ivIcon.setImageResource(iconDrawable);
        }
        if (bgDrawable!=0) {
            this.setBackgroundResource(bgDrawable);
        }
        if (textColor!=0){
            tvTitle.setTextColor(textColor);
        }
        tvTitle.setText(title);

    }
}