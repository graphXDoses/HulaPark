package com.parkingapp.hulapark.Views;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;

import androidx.annotation.Dimension;

import com.airbnb.lottie.utils.Utils;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parkingapp.hulapark.R;
public class BottomNavMenuHolderView extends BottomNavigationView
{
    private Path mPath;
    private Paint mPaint;
    /** the CURVE_CIRCLE_RADIUS represent the radius of the fab button */
//    public final int CURVE_CIRCLE_RADIUS = 256 / 3;
//    // the coordinates of the first curve
//    public Point mFirstCurveStartPoint = new Point();
//    public Point mFirstCurveEndPoint = new Point();
//    public Point mFirstCurveControlPoint2 = new Point();
//    public Point mFirstCurveControlPoint1 = new Point();
//
//    //the coordinates of the second curve
//    public Point mSecondCurveStartPoint = new Point();
//    public Point mSecondCurveEndPoint = new Point();
//    public Point mSecondCurveControlPoint1 = new Point();
//    public Point mSecondCurveControlPoint2 = new Point();
    public int mNavigationBarWidth;
    public int mNavigationBarHeight;
    private Context context;
    private int itemCount = 1;
    private int bezierWidth, bezierHeight;
    private float Xoffset = 0.0f;

    final float scale = getContext().getResources().getDisplayMetrics().density;

    public BottomNavMenuHolderView(Context context) {
        super(context);
        this.context = context;
        init(null);
    }
    public BottomNavMenuHolderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init(attrs);
    }
    public BottomNavMenuHolderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        mPath = new Path();
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        if (attrs != null)
        {
            Resources resources = getResources();

            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.BottomNavMenuHolderView);
            mPaint.setColor(typedArray.getColor(R.styleable.BottomNavMenuHolderView_menuColor, resources.getColor(R.color.green_leaf)));
            bezierHeight = (int)(typedArray.getDimension(R.styleable.BottomNavMenuHolderView_bubbleWaveHeight, 50.0f));

            typedArray.recycle();
        }

        itemCount = getMenu().size();
        itemCount = itemCount <= 0 ? 1 : itemCount;
        bezierHeight = bezierHeight == 50 ? toPX(50) : bezierHeight;
        bezierWidth = bezierHeight * 4;
        setBackgroundColor(Color.TRANSPARENT);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mNavigationBarWidth = getWidth();
        mNavigationBarHeight = getHeight();
        bezierWidth = mNavigationBarWidth / itemCount;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPath.reset();
        mPath.moveTo(0, bezierHeight);
        mPath.lineTo(Xoffset, bezierHeight);
        mPath.cubicTo(
                Xoffset + (bezierWidth / 4), bezierHeight,
                Xoffset + (bezierWidth / 4), 0,
                Xoffset + (bezierWidth / 2), 0);
        mPath.cubicTo(
                Xoffset + ((bezierWidth / 4) * 3), 0,
                Xoffset + ((bezierWidth / 4) * 3), bezierHeight,
                Xoffset + bezierWidth, bezierHeight);
        mPath.lineTo(mNavigationBarWidth, bezierHeight);
        mPath.lineTo(mNavigationBarWidth, mNavigationBarHeight);
        mPath.lineTo(0, mNavigationBarHeight);
        mPath.close();
        canvas.drawPath(mPath, mPaint);
    }

    protected int toPX(float dps) { return (int)(dps * scale + 0.5f); }

    public void setBubbleX(float x)
    {
        Xoffset = x;
        invalidate();
        requestLayout();
    }
}