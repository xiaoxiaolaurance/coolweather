package com.example.xiaoxiao.materialdesign;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by xiaoxiao on 2018/1/24.
 */

public class MyDecoration extends RecyclerView.ItemDecoration {

    private static int ORITATION_VERTICAL =1;
    private static int ORITATION_HORIZONTAL=0;
    private int oritation =ORITATION_VERTICAL;
    private Context context;
    private Paint mPaint;
    Drawable mDivide;
    private static final int attr[] ={android.R.attr.listDivider};
    public MyDecoration(Context context,int oritation,int resourceId){
        this.oritation =oritation;
        this.context =context;
        TypedArray array = context.obtainStyledAttributes(attr);
        mDivide =context.getResources().getDrawable(resourceId);
        array.recycle();
        mPaint =new Paint();
        mPaint.setColor(Color.alpha(0));
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        if(oritation ==ORITATION_VERTICAL){
            drawVerticalLine(c,parent);
        }else if(oritation ==ORITATION_HORIZONTAL){
            drawHorizontalLine(c,parent);
        }

    }

    public void drawVerticalLine(Canvas c,RecyclerView parent){
        int left = parent.getPaddingLeft();
        int right =parent.getWidth()-parent.getPaddingRight();
        int count =parent.getChildCount();
        for(int i=0;i<count;i++){
            final View child =parent.getChildAt(i);
            RecyclerView.LayoutParams params =(RecyclerView.LayoutParams) child.getLayoutParams();
            int top =child.getBottom()+params.bottomMargin;
            int bottom =top+mDivide.getIntrinsicHeight();
            mDivide.setBounds(left,top,right,bottom);
            mDivide.draw(c);
        }
    }

    public void drawHorizontalLine(Canvas c,RecyclerView parent){
        int top =parent.getPaddingTop();
        int bottom =parent.getPaddingBottom()+parent.getHeight();
        int count =parent.getChildCount();
        for(int i=0;i<count;i++) {
            final View child = parent.getChildAt(i);
            RecyclerView.LayoutParams params =(RecyclerView.LayoutParams) child.getLayoutParams();
            int left =child.getLeft() +params.leftMargin;
            int right =left +mDivide.getIntrinsicWidth();
            mDivide.setBounds(left,top,right,bottom);
            mDivide.draw(c);
        }
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
    }
}
