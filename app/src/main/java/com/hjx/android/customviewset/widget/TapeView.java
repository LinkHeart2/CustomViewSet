package com.hjx.android.customviewset.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.Scroller;

import com.hjx.android.customviewset.R;
import com.hjx.android.customviewset.util.UiUtil;

/**
 * Created by hjx on 2017/11/23.
 * You can make it better
 */

public class TapeView extends View{

    private static final String TAG = "TapeView";

    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private Scroller mScroller;
    private VelocityTracker mVelocityTracker;

    private float lastX;
    private float lastY;


    //单位长度
    private int companyLength = 150;
    //分段数量
    private int subsectionCount = 10;
    //最大值
    private int max = 100;

    //卷尺顶部
    private int tapeTop = 0;
    private int topLineSize = 1;
    private int topLineColor = Color.BLACK;

    private int indexLength = 100;
    private int indexSize = 8;
    private int indexColor = Color.GREEN;

    private int miniLineSize = 2;
    private int miniLineColor = Color.BLACK;
    private int miniLineLength = 40;

    private int bigiLineSize = 4;
    private int bigLineColor = Color.BLACK;
    private int bigLineLength = 80;

    private int textColor = Color.BLACK;
    private int textSize = 30;

    private double startNumber = 0.6;

    private boolean smoothed = false;



    private final int defaultWidth = 500;
    private final int defaultHeight = 300;

    private OnIndexChangeListener mListener;

    public TapeView(Context context) {
        super(context);
        init();
    }


    public TapeView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);

    }

    public TapeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TapeView);
        miniLineColor = a.getColor(R.styleable.TapeView_miniLine_color,getResources().getColor(R.color.divideColor));
        bigLineColor = a.getColor(R.styleable.TapeView_bigLine_color,getResources().getColor(R.color.divideColor));
        topLineColor = a.getColor(R.styleable.TapeView_topLine_color,getResources().getColor(R.color.divideColor));
        indexColor = a.getColor(R.styleable.TapeView_index_color,getResources().getColor(R.color.divideColor));

        a.recycle();

        init();
    }


    private void init(){
        textSize = (int) UiUtil.sp2px(getContext(),12);
        companyLength = (int) UiUtil.dp2px(getContext(),100);
        if(mScroller == null){
            mScroller = new Scroller(getContext());
            mVelocityTracker = VelocityTracker.obtain();

            textPaint.setColor(textColor);
            textPaint.setTextAlign(Paint.Align.CENTER);
            textPaint.setTextSize(textSize);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mVelocityTracker.addMovement(event);
        float x = event.getX();
        float y = event.getY();
        boolean result = false;

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN: {
                if(!mScroller.isFinished()){
                    smoothed = false;
                    mScroller.abortAnimation();
                }
                result = true;
            }
            break;
            case MotionEvent.ACTION_MOVE:{
                float deltaX = x - lastX;
                if(getScrollX()-deltaX>=-getWidth()/2 && getScrollX()-deltaX<=max*companyLength-getWidth()/2) {
                    scrollBy((int) -deltaX, 0);
                }
                result = true;
            }
            break;
            case MotionEvent.ACTION_UP:{
                mVelocityTracker.computeCurrentVelocity(1000);
                float xVelocity = mVelocityTracker.getXVelocity();
                float detalX = x - lastX;
                float desX = getScrollX() + (detalX > 0 ? 1 : -1) * xVelocity/5;

                smoothScrollTo(Math.min(max*companyLength-getWidth()/2,Math.max(-getWidth()/2,(int) desX)));
                mVelocityTracker.clear();
            }
                break;
        }
        lastX = x;
        lastY = y;
        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int width = getWidth();
        int height = getHeight();

        int subsectLength = companyLength / subsectionCount;

        int scrollX = getScrollX();
        startNumber = scrollX*1f/companyLength;
        if(mListener != null){
            mListener.changeIndex(startNumber+width*1f/2/companyLength);
        }


        double firstInt = (Math.ceil(startNumber) - startNumber)*companyLength;
        double preCount = firstInt / subsectLength;
        int count = width / companyLength;

        mPaint.setStrokeWidth(topLineSize);

        canvas.drawLine(Math.max(0,scrollX),tapeTop,scrollX+width,tapeTop,mPaint);

        mPaint.setColor(miniLineColor);

        drawPreLine(canvas, subsectLength, firstInt, preCount);

        drawShowLine(canvas, subsectLength, count, firstInt,startNumber);

        mPaint.setStrokeWidth(indexSize);
        mPaint.setColor(indexColor);
        canvas.drawLine(scrollX + (width>>1),tapeTop,scrollX + (width>>1),tapeTop+indexLength,mPaint);


    }

    private void drawShowLine(Canvas canvas, int subsectLength, int count, double firstInt,double startNumber) {
        for (int i = 0; i <= count; i++) {
            mPaint.setStrokeWidth(bigiLineSize);
            float bigLineX = (float) (firstInt + i * companyLength);
            if(getScrollX()+bigLineX<-firstInt)continue;
            canvas.drawLine(getScrollX()+bigLineX,tapeTop,getScrollX()+bigLineX,tapeTop+bigLineLength,mPaint);

            canvas.drawText(String.valueOf((int)((getScrollX()+bigLineX)/companyLength+0.1)),getScrollX()+bigLineX,tapeTop+bigLineLength+textSize+UiUtil.dp2px(getContext(),16),textPaint);
            mPaint.setStrokeWidth(miniLineSize);
            for (int j = 1; j < subsectionCount ; j++) {
                float miniLineX = bigLineX + j * subsectLength;
                if(getScrollX()+miniLineX<0)continue;
                canvas.drawLine(getScrollX()+miniLineX,tapeTop,getScrollX()+miniLineX,tapeTop+miniLineLength,mPaint);
            }
        }
    }

    private void drawPreLine(Canvas canvas, int subsectLength, double firstInt, double preCount) {
        if(getScrollX()<0)return;
        mPaint.setStrokeWidth(miniLineSize);
        for (int i = 0; i < preCount; i++) {
            float x = (float) (firstInt - i * subsectLength);
            canvas.drawLine(getScrollX()+x,tapeTop,getScrollX()+x,tapeTop+miniLineLength,mPaint);
        }
    }

    private void smoothScrollTo(int desX){
        int scrollX = getScrollX();
        int deltaX = desX - scrollX;
        mScroller.startScroll(scrollX,0,deltaX,0,1000);
        smoothed = true;
        invalidate();
    }

    @Override
    public void computeScroll() {
        if(mScroller.computeScrollOffset()){
            int currX = mScroller.getCurrX();
            int currY = mScroller.getCurrY();
            scrollTo(currX,currY);
            postInvalidate();
        } else if(smoothed){
            smoothed = false;
            double v = (startNumber + getWidth() * 1f / 2 / companyLength)*10;
            if(v != (int)v){
                long l = Math.round(v);
                smoothScrollTo((int) ((double)l/10*companyLength - getWidth()/2));
            }
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        mVelocityTracker.recycle();
        super.onDetachedFromWindow();
    }

    public interface OnIndexChangeListener{
        void changeIndex(double index);
    }

    public void setListener(OnIndexChangeListener listener) {
        mListener = listener;
    }

    public void setMax(int max) {
        this.max = max;
    }
}
