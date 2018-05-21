package com.example.boyzhang.bookreading.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

import com.example.boyzhang.bookreading.R;

/**
 * Created by boyzhang on 2018/5/17.
 */

public class Circle extends View {
    int paintColor;
    Context context;

    public Circle(Context context){
        this(context, null, 0);
    }

    public Circle(Context context, AttributeSet attrs){
        this(context, attrs, 0);
    }
    public Circle(Context context, AttributeSet attrs, int defStyleAttr){
        super(context, attrs, defStyleAttr);
        this.context = context;

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.Circle);
        //第一个参数为属性集合里面的属性，R文件名称：R.styleable+属性集合名称+下划线+属性名称
        //第二个参数为，如果没有设置这个属性，则设置的默认的值
        paintColor = typedArray.getColor(R.styleable.Circle_Color, Color.GRAY);
        typedArray.recycle();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        DisplayMetrics outMetrics = new DisplayMetrics();
        outMetrics = context.getResources().getDisplayMetrics();
        int width = outMetrics.widthPixels;
        setMeasuredDimension(width/10, width/20);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //调用父View的onDraw函数，因为View这个类帮我们实现了一些
        // 基本的而绘制功能，比如绘制背景颜色、背景图片等
        super.onDraw(canvas);
        int r = getMeasuredHeight() / 2;//也可以是getMeasuredHeight()/2,本例中我们已经将宽高设置相等了
        //圆心的横坐标为当前的View的左边起始位置+半径
        int centerX = getLeft() + r;
        //圆心的纵坐标为当前的View的顶部起始位置+半径
        int centerY = getTop() + r;

        Paint paint = new Paint();
        paint.setColor(paintColor);
        //开始绘制
        canvas.drawCircle(centerX, centerY, r, paint);


    }
}
