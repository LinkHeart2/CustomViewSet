package com.hjx.android.customviewset.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

/**
 * Created by hjx on 2017/10/11.
 * You can make it better
 *
 * 拖动选择图片裁剪矩形，选中后选中区域不可点击，选中后可以通过setClip设置不可再裁剪。
 */

public class ClipImageView extends ImageView {

//    private int MIN_WIDTH = 50;//最小边长
    private boolean isClip = true;//true = 可以拉矩形裁剪
    private boolean isOnClip; // 是否在裁剪中

    private boolean isCustom = false;
    private float xProportion = 0.5f;
    private float yProportion = 0.5f;
    private float widthProportion = 0.3f;


    private Rect rect;
    private float downX;
    private float downY;
    private float moveX;
    private float moveY;
    private Paint paint;


    public ClipImageView(Context context) {
        this(context,null);
    }

    public ClipImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ClipImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setClickable(true);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);

        rect = new Rect(0,0,0,0);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        moveX = event.getX();
        moveY = event.getY();
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                downY = event.getY();
                downX = event.getX();
                return !checkInRect(downX,downY,rect)&&isClip;//在范围内不拦截
            case MotionEvent.ACTION_MOVE:
//                if(positionOk(downX,downY,moveX,moveY)){
                    isOnClip = true;
                    invalidate();
//                }
                break;
            case MotionEvent.ACTION_UP:
                if(isClip
//                        && positionOk(downX,downY,moveX,moveY)
                        ){
                    rect = sortRect(downX,downY,moveX,moveY);

                }
                isOnClip = false;
                invalidate();
                break;
            default:
            break;
            }
        return isClip;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Path clipPath = new Path();
        int width = getWidth();
        int height = getHeight();

        if(isCustom){
            rect = new Rect((int)(xProportion*width),(int)(yProportion*height),(int)(xProportion*width+widthProportion*width),(int)(yProportion*height+widthProportion*height));
        }

        clipPath.addRect(0, 0 ,width, rect.top, Path.Direction.CW);
        clipPath.addRect(0, 0 , rect.left,height, Path.Direction.CW);
        clipPath.addRect(rect.right, 0 ,width, height, Path.Direction.CW);
        clipPath.addRect(0, rect.bottom ,width,height, Path.Direction.CW);
        int save = canvas.save();
        canvas.clipPath(clipPath);
        super.onDraw(canvas);

        canvas.restoreToCount(save);
        if(isOnClip){
            Path path = new Path();
            Rect pathRect = sortRect(downX, downY, moveX, moveY);
            path.addRect(new RectF(pathRect), Path.Direction.CW);
            paint.setPathEffect(new DashPathEffect(new float[]{5f,2f},0));
            canvas.drawPath(path,paint);
        }
    }

    //重置
    public void reset(){
        rect = new Rect(0,0,0,0);
        postInvalidate();
    }

//    private boolean positionOk(float x,float y,float x1,float y1){
//        return Math.abs(x1 - x) > MIN_WIDTH && Math.abs(y1 - y) > MIN_WIDTH;
//    }

    public void setClip(boolean clip) {
        isClip = clip;
    }

    private Rect sortRect(float x1, float y1, float x2, float y2){
        Rect result = new Rect(0,0,0,0);
        result.left = (int) Math.max(Math.min(x1, x2),0);
        result.top = (int) Math.max(Math.min(y1, y2),0);
        result.right = (int) Math.min(Math.max(x1, x2),getWidth());
        result.bottom = (int) Math.min(Math.max(y1, y2),getHeight());
        return result;
    }

    public boolean checkInRect(float x,float y, Rect rect) {
        return (x>rect.left && x<rect.right) && (y>rect.top && y<rect.bottom);
    }

}
