package com.parkingapp.hulapark.Views;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parkingapp.hulapark.R;

public class TopNavMenuHolderView extends BottomNavigationView
{
    private Path mPath;
    private Paint mPaint;
    private Context context;
    private int itemCount = 1;
    private int indicatorHeight;

    private int selectedIndex = 0;
    private float indicatorLeft = 0;
    private float indicatorRight = 0;

    final float scale = getContext().getResources().getDisplayMetrics().density;

    public TopNavMenuHolderView(Context context) {
        super(context);
        this.context = context;
        init(null);
    }
    public TopNavMenuHolderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init(attrs);
    }
    public TopNavMenuHolderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        mPath = new Path();
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
//        setWillNotDraw(false);

        if (attrs != null)
        {
            Resources resources = getResources();

            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TopNavMenuHolderView);
            mPaint.setColor(resources.getColor(R.color.orange));
//            mPaint.setColor(typedArray.getColor(R., resources.getColor(R.color.green_leaf)));
            indicatorHeight = (int)(typedArray.getDimension(R.styleable.TopNavMenuHolderView_customIndicatorHeight, toPX(10)));

            typedArray.recycle();
        }

        setBackgroundColor(Color.TRANSPARENT);
    }

    private void updateIndicatorBounds()
    {
        View selectedChild = getChildAt(selectedIndex);
        if (selectedChild != null) {
            MarginLayoutParams lp = (MarginLayoutParams) selectedChild.getLayoutParams();
            indicatorLeft = selectedChild.getLeft() + lp.leftMargin;
            indicatorRight = selectedChild.getRight() - lp.rightMargin;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float top = getHeight() - indicatorHeight;
        canvas.drawRect(indicatorLeft, top, indicatorRight, getHeight(), mPaint);
    }

    public void animateIndicatorToIndex(int newIndex) {
        ViewGroup menuView = (ViewGroup) getChildAt(0);
        if (menuView == null || newIndex < 0 || newIndex >= menuView.getChildCount()) return;

        View newView = menuView.getChildAt(newIndex);
        if (newView == null) return;

        newView.post(() -> {
            int[] parentLoc = new int[2];
            int[] newLoc = new int[2];

            this.getLocationOnScreen(parentLoc);
            newView.getLocationOnScreen(newLoc);

            float newLeft = newLoc[0] - parentLoc[0];
            float newRight = newLeft + newView.getWidth();

            float oldLeft = indicatorLeft;
            float oldRight = indicatorRight;

            ValueAnimator animator = ValueAnimator.ofFloat(0f, 1f);
            animator.setDuration(250);
            animator.addUpdateListener(animation -> {
                float fraction = animation.getAnimatedFraction();
                indicatorLeft = oldLeft + (newLeft - oldLeft) * fraction;
                indicatorRight = oldRight + (newRight - oldRight) * fraction;
                invalidate();
            });
            animator.start();

            selectedIndex = newIndex;
        });
    }

    public void attachIndicatorToSelection() {
        if (isInEditMode()) return;

        post(() -> {
            ViewGroup menuView = (ViewGroup) getChildAt(0);
            if (menuView != null && selectedIndex < menuView.getChildCount()) {
                View selectedView = menuView.getChildAt(selectedIndex);
                if (selectedView != null) {
                    int[] parentLocation = new int[2];
                    int[] childLocation = new int[2];

                    this.getLocationOnScreen(parentLocation);
                    selectedView.getLocationOnScreen(childLocation);

                    int offsetLeft = childLocation[0] - parentLocation[0];
                    int offsetRight = offsetLeft + selectedView.getWidth();

                    indicatorLeft = offsetLeft;
                    indicatorRight = offsetRight;

                    invalidate();
                }
            }
        });
    }

    protected int toPX(float dps) { return (int)(dps * scale + 0.5f); }
}