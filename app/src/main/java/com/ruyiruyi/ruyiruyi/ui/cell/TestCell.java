package com.ruyiruyi.ruyiruyi.ui.cell;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.TextView;

public class TestCell extends FrameLayout {

    private TextView textView;

    public TestCell(Context context) {
        super(context);
        initView(context);
    }


    public TestCell(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public TestCell(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }



    private void initView(Context context) {
        textView = new TextView(context);
        textView.setText("aa");
        addView(textView);
    }

}