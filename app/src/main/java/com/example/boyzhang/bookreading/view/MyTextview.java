package com.example.boyzhang.bookreading.view;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewTreeObserver;

/**
 * Created by boyzhang on 2018/6/4.
 */

public class MyTextview extends AppCompatTextView {

    private float textSize;
    private float textLineHeight;
    private int top;
    private int y;
    private int lines;
    private int bottom;
    private int right;
    private int left;
    private int lineDrawWords;
    private char[] textCharArray = new char[1];
    private float singleWordWidth;
    private boolean first = true;
    private float lineSpacingExtra;
    private Paint paint;
    int maxLine;

    public MyTextview(Context context){
        this(context, null);
    }
    public MyTextview(Context context, AttributeSet attrs){
        this(context, attrs, 0);

    }
    public MyTextview(Context context, AttributeSet attrs, int defStyleAttr){
        super(context, attrs, defStyleAttr);
        Log.e("log","start");
        final ViewTreeObserver observer= getViewTreeObserver();

        /*observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                if(observer.isAlive())
                    observer.removeOnPreDrawListener(this);
                initTextInfo();
                return false;
            }
        });*/

    }

    public void initTextInfo() {
        textSize = getTextSize();
        textLineHeight = getLineHeight();
        left = 0;
        right = getRight();
        y = getTop();
        // 要画的宽度
        int drawTotalWidth = right - left;
        String text = getText().toString();
        if (!TextUtils.isEmpty(text)) {
            text = ToDBC(text);
            textCharArray = text.toCharArray();
            paint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
            paint.setTextSize(textSize);
            // 一个单词的的宽度
            singleWordWidth = paint.measureText("一") + getLineSpacingExtra();
            // 一行可以放多少个字符
            lineDrawWords = (int) (drawTotalWidth / singleWordWidth);
            int length = textCharArray.length;
            lines = length / lineDrawWords;
            if ((length % lineDrawWords) > 0) {
                lines = lines + 1;
            }
            //ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) getLayoutParams();
            //int totalHeight = (int) (lines*textLineHeight+textLineHeight*2 + getPaddingBottom()+getPaddingTop()+layoutParams.bottomMargin+layoutParams.topMargin);
            //setHeight(totalHeight);
        }
    }



    @Override
    protected void onDraw(Canvas canvas) {
        //super.onDraw(canvas);
        initTextInfo();

        /*for (int i = 0; i < drawTotalLine; i++) {
            try {
                int length = textCharArray.length;
                int mLeft = left;
                // 第i+1行开始的字符index
                int startIndex = i * lineDrawWords;
                // 第i+1行结束的字符index
                int endTextIndex = startIndex + lineDrawWords;
                if (endTextIndex > length) {
                    endTextIndex = length;
                }
                y += textLineHeight;
                for (; startIndex < endTextIndex; startIndex++) {
                    char c = textCharArray[startIndex];
//                  if (c == ' ') {
//                      c = '\u3000';
//                  } else if (c < '\177') {
//                      c = (char) (c + 65248);
//                  }
                    canvas.drawText(String.valueOf(c), mLeft, y, getPaint());
                    mLeft += singleWordWidth;
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.w("err","happen");
            }
        }*/
        int len = textCharArray.length;
        for(int startIndex = 0; startIndex < len; ){
            int mLeft = left;
            y += textLineHeight;
            int endTextIndex = startIndex + lineDrawWords;
            for(; startIndex < len && startIndex < endTextIndex; startIndex++){
                char c = textCharArray[startIndex];
                if(c == '\n'){
                    startIndex++;
                    break;
                }
                canvas.drawText(String.valueOf(c), mLeft, y, getPaint());
                mLeft += singleWordWidth;
            }

        }
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);

    }

    public void setMaxLines(int max){
        this.maxLine = max;
    }

    public void setLineSpacingExtra(int lineSpacingExtra){
        this.lineSpacingExtra = lineSpacingExtra;
    }

    public static String ToDBC(String input) {
        // 导致TextView异常换行的原因：安卓默认数字、字母不能为第一行以后每行的开头字符，因为数字、字母为半角字符
        // 所以我们只需要将半角字符转换为全角字符即可
        char c[] = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == ' ') {
                c[i] = '\u3000';
            }
            else if(c[i] == '\n'){
                continue;
            }
            else if (c[i] < '\177') {
                c[i] = (char) (c[i] + 65248);
            }
        }
        return new String(c);
    }
}
