package com.example.xiaoxiao.materialdesign;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by xiaoxiao on 2018/1/25.
 */

public class ButtonView extends View{

    private float radius = 40;
    private float width;
    private float height;
    private Paint mPaint;
    private float lastX;
    private float lastY;
    int degree = 0;
    int count;
    boolean isClick =false;

    public ButtonView(Context context) {
        super(context);
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = getMeasuredWidth() / 2;
        height = getMeasuredHeight() / 2;
    }


    public void onDraw(Canvas c) {
        Log.d("xx/button","onDraw" +count);
        count ++;
        mPaint.setColor(Color.RED);
        if(!isClick) {
            c.drawCircle(width, height, radius, mPaint);
            for(int i=0;i<60;i++){
                c.drawLine(width, height - radius - 4, width, height - radius - 12, mPaint);
                c.rotate(6, width, height);
            }
          //  width-10,height-10,width+10,height+10
        }else{
            c.drawRect(new RectF(width-10,height-10,width+10,height+10),mPaint);
            mPaint.setColor(Color.GRAY);
            for (int i = 0; i < 13; i++) {
                c.drawLine(width, height - radius - 4, width, height - radius - 12, mPaint);
                c.rotate(30, width, height);
            }
            if(count>59){
                count =0;
            }
            for(int i=0;i<count;i++){
                c.drawLine(width, height - radius - 4, width, height - radius - 12, mPaint);
                c.rotate(6, width, height);
            }
            invalidate();
        }


    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d("xx/button","onTouchEvent");
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                float x =event.getRawX();
                float y =event.getRawY();
                Log.d("xx/button","x"+x+" width: "+width +"y"+y+" height: "+height);
               // if(x<width+radius && x>width-radius && y>height+radius && y<height-radius){
                    if(isClick){
                        isClick =false;
                    }else{
                        isClick =true;
                    }
                    invalidate();
              //  }
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return true;
    }

}

