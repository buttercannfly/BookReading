package com.example.boyzhang.bookreading.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by boyzhang on 2018/5/15.
 */

public class View_showbook extends LinearLayout {

    public View_showbook(Context context){
        this(context, null, 0);
    }
    public View_showbook(Context context, AttributeSet attrs){
        this(context, null, 0);
    }
    public View_showbook(Context context, AttributeSet attrs, int defStyleAttr){
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int wide_size = MeasureSpec.getSize(widthMeasureSpec);
        int wide_mode = MeasureSpec.getMode(widthMeasureSpec);
        int long_size = MeasureSpec.getSize(heightMeasureSpec);
        int long_mode = MeasureSpec.getMode(heightMeasureSpec);

        setMeasuredDimension(wide_size, long_size);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        int count = getChildCount();
        //记录当前的高度位置
        int curHeight = t;
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            int height = child.getMeasuredHeight();
            int width = child.getMeasuredWidth();
            child.layout(l, curHeight, l + width, curHeight + height);
            curHeight += height;
        }
    }
}
