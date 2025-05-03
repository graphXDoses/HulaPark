package com.parkingapp.hulapark.Views;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;

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
            mPaint.setColor(typedArray.getColor(R.styleable.BottomNavMenuHolderView_exampleColor, resources.getColor(R.color.green_leaf)));

            typedArray.recycle();
        }

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
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPath.reset();
        mPath.moveTo(0, 0);
        mPath.lineTo(mNavigationBarWidth, 0);
        mPath.lineTo(mNavigationBarWidth, mNavigationBarHeight);
        mPath.lineTo(0, mNavigationBarHeight);
        mPath.close();
        canvas.drawPath(mPath, mPaint);
    }
}