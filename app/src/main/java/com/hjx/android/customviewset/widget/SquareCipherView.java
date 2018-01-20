package com.hjx.android.customviewset.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by hjx on 0021 9-21.
 * You can make it better
 *
 * 自定义图案解锁
 *
 */

public class SquareCipherView extends View {

    private Paint mPaint;
    private Paint linePaint;

    private static int MIN_COUNT = 4;

    private List<Point> points;//基本点
    private List<Point> line;//连线点
    private int mWidth;//整个view宽度
    private int offset;//点的水平方向距离
    private long obliqueOffset;//45度角上的偏移
    private int radius;//点的半径

    private int color;//未选中颜色
    private int selectColor;//选中后颜色
    private int errorColor;//连线错误时的颜色

    private boolean isError;//是否连线错误

    private float downX;
    private float downY;//按下的坐标
    private float moveX;
    private float moveY;//当前的坐标

    private boolean isAction;//是否在动作中

    private Handler mHandler = new Handler();

    private LineListener mListener;

    private int state;
    public static final int INPUT_ERROR = 0;//录入错误
    public static final int INPUT_SUCCESS = 1;//录入成功
    public static final int CHECK_ERROR = 2;//对照错误
    public static final int CHECK_SUCCESS = 3;//对照成功
    public static final int TOO_LITTLE = 4;//连线点少于下限
    public static final int LINING = 5;//连线开始
    public static final int PREPARE = 6;//准备状态


    public SquareCipherView(Context context) {
        this(context,null);
    }

    public SquareCipherView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SquareCipherView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }


    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setColor(color);

        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setColor(color);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeCap(Paint.Cap.ROUND);

        color = Color.BLUE;
        selectColor = Color.GREEN;
        errorColor = Color.RED;

    }

    //重置点集合
    private void reset(){
        initPoints();
    }

    //初始化点集合
    private void initPoints(){
        state = PREPARE;
        if(points == null) {
            points = new LinkedList<>();
            for (int i = 1; i <= 3; i++) {
                for (int j = 1; j <= 3; j++) {
                    int id = (i - 1) * 3 + j;
                    Log.d("TAG", "initPoints: "+id);
                    points.add(new Point(offset * j, offset * i, id));
                }
            }
        }else{
            for (Point point : points) {
                point.isSelect = false;
            }
        }

        if(line == null)
            line = new ArrayList<>();
        else{
            line.clear();
        }

    }



    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mWidth = getMeasuredWidth();
        offset = mWidth/4;
        radius = offset /8;
        mPaint.setStrokeWidth(radius*2);
        linePaint.setStrokeWidth(radius/4);

        obliqueOffset = (long) Math.ceil(Math.sqrt(offset*offset*2));

        initPoints();

    }



    @Override
    protected void onDraw(Canvas canvas) {

        drawPoints(canvas);

        drawLine(canvas);

        if(isAction){
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    invalidate();
                }
            },16);
        }

    }

    //绘制连线
    private void drawLine(Canvas canvas) {
        if(line.size() == 0)
            return;
        linePaint.setColor(isError ? errorColor : color);
        if(line.size() > 1){
            Path path = new Path();
            path.moveTo(line.get(0).x,line.get(0).y);
            for (int i = 1; i < line.size() ; i++) {
                Point point = line.get(i);
                path.lineTo(point.x,point.y);
            }
            canvas.drawPath(path,linePaint);
        }
        if(isAction && line.size()<points.size()){
            Point point = line.get(line.size() - 1);
            canvas.drawLine(point.x,point.y,moveX,moveY,linePaint);
        }

    }

    /**
     * 绘制点
     * @param canvas
     */
    private void drawPoints(Canvas canvas) {
        if(isError){
            mPaint.setColor(errorColor);
        }
        for (Point point : points) {
            if(!isError){
                mPaint.setColor(point.isSelect?selectColor:color);
            }
            canvas.drawPoint(point.x, point.y, mPaint);
        }
    }

    public static class Point{
        private int x,y;
        private int id;
        private boolean isSelect;//是否选中


        public Point(int x, int y,int id) {
            this.x = x;
            this.y = y;
            this.id = id;
        }

        public static double getInstance(Point a,Point b){
            return Math.sqrt(Math.pow((a.x - b.x),2)+Math.pow((a.y - b.y),2));
        }

        public static boolean isInRange(Point a, int radius, float x,float y){
            return Math.sqrt(Math.pow((a.x - x),2)+Math.pow((a.y - y),2))<radius;
        }

    }

    /**
     *
     * @param x
     * @param y
     * @return  是否有变化 true = 有变化
     */
    private boolean checkSelect(float x,float y){
        for (Point point : points) {
            if(Point.isInRange(point,radius,x,y)&&!point.isSelect){
                if(line.size()>0 && line.size()<8) {
                    Point betweenPoint = checkBetween(line.get(line.size() - 1), point);
                    if(betweenPoint!=null) {
                        betweenPoint.isSelect = true;
                        line.add(betweenPoint);
                        if (line.size() == 1) {
                            sendEvent(LINING);
                        }
                    }
                }
                point.isSelect = true;
                line.add(point);
                if(line.size() == 1){
                    sendEvent(LINING);
                }
                Log.d("TAG", "checkSelect: add("+point.x+","+point.y+")");
                return true;
            }
        }
        return false;
    }


    /**
     * 检查两个点中间有没有未连接的点
     * @param point
     * @param point1
     */
    private Point checkBetween(Point point, Point point1) {
        Point result = null;
        if(Point.getInstance(point,point1)>obliqueOffset&&(point.x==point1.x || point.y == point1.y ||Math.abs(point.x - point1.x) == Math.abs(point.y - point1.y))){
            result = points.get((point.id+point1.id)/2-1);
        }
        return result;
    }


    //回调监听
    private void sendEvent(int event) {
        state = event;
        if(mListener!=null){
            mListener.onEvent(event);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(state == INPUT_SUCCESS)
            return false;
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                downX = event.getX();
                downY = event.getY();
                moveX = event.getX();
                moveY = event.getY();
                isAction = true;
                checkSelect(downX,downY);
                invalidate();

            break;
            case MotionEvent.ACTION_MOVE:
                moveX = event.getX();
                moveY = event.getY();
                checkSelect(moveX,moveY);

                break;
            case MotionEvent.ACTION_UP:
                isAction = false;
                if(line.size()<MIN_COUNT){
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            sendEvent(TOO_LITTLE);
                            reset();
                            postInvalidate();
                        }
                    },100);

                }else{
                    sendEvent(INPUT_SUCCESS);
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            reset();
                            postInvalidate();
                        }
                    },1000);
                }
                postInvalidate();
                break;
            default:
            break;
            }
            return true;
    }

    public interface LineListener{
        void onEvent(int event);
    }

    public void setListener(LineListener listener) {
        mListener = listener;
    }
}
