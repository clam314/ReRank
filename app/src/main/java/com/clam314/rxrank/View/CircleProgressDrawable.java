package com.clam314.rxrank.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.text.TextPaint;
import android.util.DisplayMetrics;
import android.util.TypedValue;

import com.clam314.rxrank.R;
import com.clam314.rxrank.util.DeBugLog;

import java.util.Locale;

/**
 * Created by clam314 on 2017/4/9
 */

public class CircleProgressDrawable extends Drawable {
    private static final String TAG = CircleProgressDrawable.class.getSimpleName();
    private static final int MAX_PROGRESS = 10000;
    private int progress;
    private Paint mPaint;
    private Paint mTextPaint;
    private float circleRadius;
    private float ringWidth;
    private float textSize;


    public static CircleProgressDrawable newDefaultInstance(Context context){
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int textColor,circleColor;
        if(Build.VERSION.SDK_INT < 23){
            textColor = context.getResources().getColor(R.color.item_category_title_color);
            circleColor = context.getResources().getColor(R.color.drawable_circle_progress);
        }else {
            textColor = context.getColor(R.color.item_category_title_color);
            circleColor = context.getColor(R.color.drawable_circle_progress);
        }
        return new CircleProgressDrawable(dm,textColor,circleColor);
    }

    public CircleProgressDrawable(DisplayMetrics dm, int textColor, int circleColor) {
        progress = 0;
        textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,12,dm);
        circleRadius = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,20,dm);
        ringWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,1,dm);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(circleColor);
        mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(textColor);
        mTextPaint.setTextSize(textSize);
    }

    @Override
    protected boolean onLevelChange(int level) {
        progress = level;
        DeBugLog.logError(TAG,"onLevelChange:"+level);
        return true;
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        DeBugLog.logError(TAG,"draw:"+progress);
        canvas.translate(canvas.getWidth()/2,canvas.getHeight()/2);
        mPaint.setStrokeWidth(ringWidth);
        mPaint.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(0,0, circleRadius,mPaint);
        mPaint.setStyle(Paint.Style.FILL);
        RectF rectF = new RectF(-circleRadius,-circleRadius,circleRadius,circleRadius);
        canvas.drawArc(rectF,270,progress/MAX_PROGRESS*360,true,mPaint);
        String text = String.format(Locale.CHINA,"%d%%",progress/MAX_PROGRESS*100);
        float x = text.length() * textSize/4;
        float y = textSize/2;
        canvas.drawText(text,-x,y,mTextPaint);
    }

    @Override
    public void setAlpha(int alpha) {
        mPaint.setAlpha(alpha);
        mTextPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {
        mPaint.setColorFilter(colorFilter);
        mTextPaint.setColorFilter(colorFilter);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSPARENT;
    }
}
