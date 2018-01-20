package com.hjx.android.customviewset.widget;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

import com.hjx.android.customviewset.R;

/**
 * Created by hjx on 2017/10/31.
 * You can make it better
 */

public class FlipboardView extends View {


    private Bitmap bitmap;
    private Paint paint;
    private int width;
    private int height;

    private int rotaDegree = 0;//旋转角度
    private int upDegree = 0;//右边掀起来角度
    private int leftDegree = 0;//左边掀起来角度

    private int centerX;
    private int centerY;

    private Camera camera = new Camera();

    public FlipboardView(Context context) {
        this(context,null);
    }

    public FlipboardView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public FlipboardView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);


        setClickable(true);

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);

        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.flipboard);

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        float newZ = - displayMetrics.density * 6;
        camera.setLocation(0, 0, newZ);

        ActivityManager service = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

    }

    public void reset(){
        rotaDegree = 0;
        upDegree = 0;
        leftDegree = 0;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        width = getWidth();
        height = getHeight();
        centerX = width / 2;
        centerY = height / 2;

        int x = centerX - bitmap.getWidth()/2;
        int y = centerY - bitmap.getHeight()/2;

        camera.save();

        canvas.save();

        camera.rotateY(-upDegree);

        canvas.translate(centerX,centerY);
        canvas.rotate(-rotaDegree);

        camera.applyToCanvas(canvas);

        canvas.clipRect(0,-centerY,centerX,centerY);
        canvas.rotate(rotaDegree);
        canvas.translate(-centerX,-centerY);

        canvas.drawBitmap(bitmap,x,y,paint);

        camera.restore();

        canvas.restore();


        camera.save();

        canvas.save();

        camera.rotateY(leftDegree);

        canvas.translate(centerX,centerY);
        canvas.rotate(-rotaDegree);

        camera.applyToCanvas(canvas);
        canvas.clipRect(-centerX,-centerY,0,centerY);
        canvas.rotate(rotaDegree);

        canvas.translate(-centerX,-centerY);

        canvas.drawBitmap(bitmap,x,y,paint);

        camera.restore();

        canvas.restore();

    }


    public int getRotaDegree() {
        return rotaDegree;
    }

    public void setRotaDegree(int rotaDegree) {
        this.rotaDegree = rotaDegree;
        postInvalidate();
    }

    public int getUpDegree() {
        return upDegree;
    }

    public void setUpDegree(int upDegree) {
        this.upDegree = upDegree;
        invalidate();
    }

    public int getLeftDegree() {
        return leftDegree;
    }

    public void setLeftDegree(int leftDegree) {
        this.leftDegree = leftDegree;
        invalidate();
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
        invalidate();
    }
}
