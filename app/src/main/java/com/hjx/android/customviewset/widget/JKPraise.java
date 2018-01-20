package com.hjx.android.customviewset.widget;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.CycleInterpolator;

import com.hjx.android.customviewset.R;

/**
 * Created by hjx on 2017/10/17.
 * You can make it better
 */

public class JKPraise extends View {

    private int count = 123;
    private Paint mPaint;

    private boolean isAnim;
    private boolean isCheck = false;


    private float praiseSize = 1;
    private float ovalSize;

    private Handler mHandler = new Handler();
    private boolean isShowOval;
    private Paint ovalPaint;
    private Paint textPaint;
    private Paint changePaint;
    private Paint linePaint;

    private float textAnimOffset;//动画偏移
    private int newIndex;//发生改变的位置
    private boolean isUp;//升还是降
    private String keepText;//不变的部分
    private String newText;//进来的字符串
    private String oldText;//出去的字符串

    private int duration =300;

    private int textSize = 48;

    public JKPraise(Context context) {
        this(context,null);
    }

    public JKPraise(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public JKPraise(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setLayerType(LAYER_TYPE_HARDWARE,null);

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        ovalPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        ovalPaint.setStyle(Paint.Style.STROKE);
        ovalPaint.setStrokeWidth(8);
        ovalPaint.setARGB(16,255,0,0);

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(Color.GRAY);
        textPaint.setTextSize(textSize);

        changePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        changePaint.setColor(Color.GRAY);
        changePaint.setTextSize(textSize);
        changePaint.setTextAlign(Paint.Align.CENTER);

        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setStrokeCap(Paint.Cap.ROUND);
        linePaint.setColor(Color.RED);
        linePaint.setStrokeWidth(5);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        int width = getWidth();
        int height = getHeight();

        Bitmap bitmap = decodeBitmap(isCheck ? R.drawable.zan : R.drawable.un_zan, (int) (width / 4 * praiseSize), (int) (height / 2 * praiseSize));
        Rect dst = new Rect((int)(width/4 - (width / 8 * praiseSize)),
                (int) (height/2 - height / 4 * praiseSize),
                (int)(width/4 + (width / 8 * praiseSize)),
                (int)(height/2 + height / 4 * praiseSize));
        Rect src = new Rect(0,0, bitmap.getWidth(),bitmap.getHeight());

        if(isShowOval){
            Rect ovalRect = new Rect((int)(width/4 - (width / 8 * ovalSize)),
                    (int) (height/2 - height / 4 * ovalSize),
                    (int)(width/4 + (width / 8 * ovalSize)),
                    (int)(height/2 + height / 4 * ovalSize));
            canvas.drawOval(new RectF(ovalRect),ovalPaint);
        }

        canvas.drawBitmap(bitmap, src,dst ,mPaint);

        if(isCheck) {
            int yOffset = -Math.abs(dst.right-dst.left)/4;
            int centerX = dst.centerX();
            int centerY = dst.centerY();
            int half = Math.abs(dst.left - dst.right) / 2;
            half = (int) (half*0.8);
            centerY += yOffset;
            float lineShowOffset = isAnim?isShowOval ? textAnimOffset : 1 - textAnimOffset:1;
            Rect lineRect = new Rect((int) (centerX - lineShowOffset*half),
                    (int) (centerY - lineShowOffset*half),
                    (int) (centerX+lineShowOffset*half),
                    (int) (centerY+lineShowOffset*half));
            linePaint.setAlpha((int) (255*(lineShowOffset - 0.5))*2);

            Point leftCenterPoint = getPointCenter(centerX, centerY, lineRect.left, Math.abs(lineRect.top+lineRect.bottom)/2);//中上与中心中点
            leftCenterPoint = getPointCenter(leftCenterPoint.x,leftCenterPoint.y,lineRect.left,Math.abs(lineRect.top+lineRect.bottom)/2);
            Point leftTopPoint = getPointCenter(centerX, centerY, lineRect.left, lineRect.top);//左上角与中心中点
            leftTopPoint = getPointCenter(leftTopPoint.x,leftTopPoint.y,lineRect.left,lineRect.top);
            Point centerTopPoint = getPointCenter(centerX, centerY, Math.abs(lineRect.left + lineRect.right) / 2, lineRect.top);//中上与中心中点
            centerTopPoint = getPointCenter(centerTopPoint.x,centerTopPoint.y,Math.abs(lineRect.left + lineRect.right) / 2,lineRect.top);
            Point rightTopPoint = getPointCenter(centerX, centerY, lineRect.right, lineRect.top);//右上角与中心中点
            rightTopPoint = getPointCenter(rightTopPoint.x,rightTopPoint.y,lineRect.right,lineRect.top);

            canvas.drawLine(leftCenterPoint.x, leftCenterPoint.y, lineRect.left-half/6, Math.abs(lineRect.top+lineRect.bottom)/2,linePaint);
            canvas.drawLine(leftTopPoint.x, leftTopPoint.y, lineRect.left, lineRect.top,linePaint);
            canvas.drawLine(centerTopPoint.x,centerTopPoint.y, Math.abs(lineRect.left + lineRect.right) / 2 , lineRect.top-half/6,linePaint);
            canvas.drawLine(rightTopPoint.x,rightTopPoint.y, lineRect.right, lineRect.top,linePaint);
        }



        if(!isAnim) {
            textPaint.setAlpha(255);
            Rect textRect = new Rect();
            textPaint.getTextBounds(count + "", 0, (count + "").length(), textRect);
            canvas.drawText(count + "", width / 2, (height / 2 + (textRect.bottom - textRect.top) / 2), textPaint);
        }else{
            textPaint.setAlpha(255);
            Rect textRect = new Rect();
            textPaint.getTextBounds(keepText, 0, keepText.length(), textRect);

            int baseHight = height / 2 + (textRect.bottom - textRect.top) / 2;

            canvas.drawText(keepText, width / 2, baseHight, textPaint);

            float measureText = textPaint.measureText(keepText);

            //old
            changePaint.setTextSize(textSize+textAnimOffset*-10);
            changePaint.setAlpha(255 - (int) (textAnimOffset*255));
            canvas.drawText(oldText, width / 2 + measureText + textPaint.measureText(oldText)/2,
                    textAnimOffset * (isUp?-baseHight:baseHight)+baseHight
                    ,changePaint);

            //new
            changePaint.setTextSize(textSize-(1-textAnimOffset)*10);
            changePaint.setAlpha((int) (textAnimOffset*255));
            canvas.drawText(newText, width / 2 + measureText+ textPaint.measureText(newText)/2,
                    (1-textAnimOffset)*(isUp?baseHight:-baseHight)+baseHight
                    ,changePaint);

        }

    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
        invalidate();
    }

    private Bitmap decodeBitmap(int id, int width,int height){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(getResources(),id,options);
        int sampleSize = 1;
        while ( (options.outWidth / sampleSize>width) || (options.outHeight / sampleSize > height)){
            sampleSize *=2;
        }
        options.inSampleSize = sampleSize;
        options.inJustDecodeBounds = false;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        return BitmapFactory.decodeResource(getResources(),id,options);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(isAnim)return false;
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                animatePraise();
                return true;
            default:
            break;
            }
        return super.onTouchEvent(event);
    }

    private void animatePraise() {
            isAnim = true;
        isUp = !isCheck;
        String oldString = String.valueOf(count);
        String newString = String.valueOf(isUp?count+1:count-1);
        newIndex = calculateText(oldString,newString);
        keepText = oldString.substring(0,newIndex);
        oldText = oldString.substring(newIndex);
        newText = newString.substring(newIndex);
        ObjectAnimator.ofFloat(this,"textAnimOffset",0,1).setDuration(duration).start();

        ObjectAnimator praiseAnim = ObjectAnimator.ofFloat(this, "praiseSize", 0.7f).setDuration(duration);
        praiseAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                invalidate();
            }
        });
        praiseAnim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isShowOval = false;
                isAnim = false;
                if(isCheck)count++;
                else count--;
                invalidate();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        praiseAnim.setInterpolator(new CycleInterpolator(1));
        praiseAnim.start();


        if(!isCheck){
            isShowOval = true;
            ObjectAnimator.ofFloat(this,"ovalSize",0,1.8f).setDuration(duration).start();
        }

        mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    isCheck = !isCheck;
                    if(isCheck)
                        isShowOval = true;
                    invalidate();
                }
            },duration/2);

    }

    public float getPraiseSize() {
        return praiseSize;
    }

    public void setPraiseSize(float praiseSize) {
        this.praiseSize = praiseSize;
    }

    public float getOvalSize() {
        return ovalSize;
    }

    public void setOvalSize(float ovalSize) {
        this.ovalSize = ovalSize;
    }

    public float getTextAnimOffset() {
        return textAnimOffset;
    }

    public void setTextAnimOffset(float textAnimOffset) {
        this.textAnimOffset = textAnimOffset;
    }

    private int calculateText(String oldString ,String newString ){
        if(newString.length()!=oldString.length()){
            return 0;
        }else{
            for (int i = 0; i < oldString.length(); i++) {
                if(oldString.charAt(i) != newString.charAt(i)){
                    return i;
                }
            }
        }
        return -1;
    }

    private Point getPointCenter(int x1, int y1, int x2, int y2){
        return new Point(Math.abs(x1+x2)/2,Math.abs(y1+y2)/2);
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }
}
