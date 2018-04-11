package com.ruyiruyi.ruyiruyi.ui.cell;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.ruyiruyi.rylibrary.R;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;
import com.ruyiruyi.rylibrary.utils.LayoutHelper;

import rx.functions.Action1;

public class ActionBar extends FrameLayout {

    private  FrameLayout content;
    private  TextView titleView;
    private ImageView backButtonImageView;
    private TextView titleTextView;
    private TextView subTitleTextView;
    private View actionModeTop;
 //   private ActionBarMenu menu;
   // private ActionBarMenu actionMode;
    private boolean occupyStatusBar;
    private boolean allowOverlayTitle;
    private CharSequence lastTitle;
    private boolean castShadows;
    protected boolean isSearchFieldVisible;
    protected int itemsBackgroundResourceId;
    private boolean isBackOverlayVisible;
    public ActionBar.ActionBarMenuOnItemClick actionBarMenuOnItemClick;
    private int extraHeight;
    private static Paint paint;
    private boolean needDivider;
    private boolean isSmallStyle;



    public ActionBar(Context context) {
        super(context);
        initView(context);
    }

    public ActionBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ActionBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    private void initView(Context context) {
        content = new FrameLayout(context);
        addView(content,LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT,100));

        backButtonImageView = new ImageView(context);
        backButtonImageView.setScaleType(ImageView.ScaleType.CENTER);
        backButtonImageView.setImageResource(R.drawable.ic_back);
        content.addView(backButtonImageView, LayoutHelper.createFrame(54,54,Gravity.CENTER_VERTICAL,10,0,0,0));
        RxViewAction.clickNoDouble(backButtonImageView)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        ActionBar.this.actionBarMenuOnItemClick.onItemClick(-1);
                    }
                });

        titleView = new TextView(context);
        titleView.setTextSize(20);
        titleView.setTextColor(Color.WHITE);
        content.addView(titleView,LayoutHelper.createFrame(LayoutHelper.WRAP_CONTENT,LayoutHelper.WRAP_CONTENT,Gravity.CENTER));
    }

    public void setBackground(int color){
        content.setBackgroundColor(color);
    }

    public void setTitle(String title){
        titleView.setText(title);
    }

    public static class ActionBarMenuOnItemClick{
        public void onItemClick(int var1){}
    }




}