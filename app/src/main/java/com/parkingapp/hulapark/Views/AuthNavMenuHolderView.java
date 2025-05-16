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

public class AuthNavMenuHolderView extends BottomNavigationView
{
    private Path mPath;
    private Paint mPaint;
    public int mNavigationBarWidth;
    public int mNavigationBarHeight;
    private Context context;
    private int itemCount = 1;
    private int indicatorWidth, indicatorHeight;
    private float Xoffset = 0.0f;

    final float scale = getContext().getResources().getDisplayMetrics().density;

    public AuthNavMenuHolderView(Context context) {
        super(context);
        this.context = context;
        init(null);
    }
    public AuthNavMenuHolderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init(attrs);
    }
    public AuthNavMenuHolderView(Context context, AttributeSet attrs, int defStyleAttr) {
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
            mPaint.setColor(resources.getColor(R.color.yellow));
//            mPaint.setColor(typedArray.getColor(R., resources.getColor(R.color.green_leaf)));
            indicatorHeight = (int)(typedArray.getDimension(R.styleable.AuthNavMenuHolderView_customIndicatorHeight, toPX(12)));

            typedArray.recycle();
        }

        itemCount = getMenu().size();
        itemCount = itemCount <= 0 ? 1 : itemCount;
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
        indicatorWidth = mNavigationBarWidth / itemCount;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPath.reset();
        mPath.moveTo(0, mNavigationBarHeight);
        mPath.lineTo(mNavigationBarWidth / 2, mNavigationBarHeight);
        mPath.lineTo(mNavigationBarWidth / 2, mNavigationBarHeight - indicatorHeight);
        mPath.lineTo((mNavigationBarWidth / 2) - indicatorWidth, mNavigationBarHeight - indicatorHeight);
        mPath.close();
        canvas.drawPath(mPath, mPaint);
    }

    protected int toPX(float dps) { return (int)(dps * scale + 0.5f); }
}