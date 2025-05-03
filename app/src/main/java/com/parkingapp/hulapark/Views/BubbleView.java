package com.parkingapp.hulapark.Views;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.RelativeLayout;

import com.parkingapp.hulapark.R;

public class BubbleView extends RelativeLayout
{
    private Paint paint;
    private Path path;
    private int bezierWidth, bezierHeight;
    private boolean isLinear=false;
    private Context context;

    public BubbleView(Context context) { super(context); }

    public BubbleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init(attrs);
    }
    public BubbleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        path = new Path();
        paint.setStrokeWidth(0);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);

        if (attrs != null)
        {
            Resources resources = getResources();

            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.BubbleView);
            paint.setColor(typedArray.getColor(R.styleable.BubbleView_color, resources.getColor(R.color.green_leaf)));

            typedArray.recycle();
        }

        setBackgroundColor(Color.TRANSPARENT);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setBackgroundColor(Color.TRANSPARENT);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        bezierHeight = getHeight();
        bezierWidth = getWidth();
    }

    @Override
    protected void onDraw(Canvas canvas)
    {

        /**
         * Reset path before drawing
         */
        path.reset();

        /**
         * Start point for drawing
         */
        path.moveTo(0, bezierHeight);

        if(!isLinear){
            /**
             * Seth half path of bezier view
             */
            path.cubicTo(bezierWidth / 4, bezierHeight, bezierWidth / 4, 0, bezierWidth / 2, 0);
            /**
             * Seth second part of bezier view
             */
            path.cubicTo((bezierWidth / 4) * 3, 0, (bezierWidth / 4) * 3, bezierHeight, bezierWidth, bezierHeight);
        }

        /**
         * Draw our bezier view
         */
        canvas.drawPath(path, paint);
    }

    /**
     * Build bezier view with given width and height
     *
     * @param bezierWidth  Given width
     * @param bezierHeight Given height
     * @param isLinear True, if curves are not needed
     */
    void build(int bezierWidth, int bezierHeight,boolean isLinear) {
        this.bezierWidth = bezierWidth;
        this.bezierHeight = bezierHeight;
        this.isLinear=isLinear;
    }
}
