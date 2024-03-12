package org.jzs.mybaseapp.section.weightdemo.anime.view;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;


import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

/**
 * Created by allen on 2017/5/23.
 * <p>
 * loading按钮
 */

public class LoadingButton extends View {

    public int mWidth;
    public int mHeight;

    private Paint circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);


//    private static final int POINT_COLOR_1 = 0x4CFFFFFF;
//    private static final int POINT_COLOR_2 = 0x7FFFFFFF;
//    private static final int POINT_COLOR_3 = 0xFFFFFFFF;

    private static final int POINT_COLOR_1 = Color.BLACK;
    private static final int POINT_COLOR_2 = Color.BLUE;
    private static final int POINT_COLOR_3 = Color.BLUE;

    private int duration = 300;

    private float centerX;
    private float centerY;

    private float radius;


    private boolean isLoading = false;

    private int mLoadingIndex = 0;
    private Runnable mRunnable;


    public LoadingButton(Context context) {
        super(context);
        initPaint();
    }

    public LoadingButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }

    public LoadingButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public LoadingButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initPaint();
    }


    private void initPaint() {
        circlePaint = getPaint(dp2px(1), Paint.Style.FILL);
        mRunnable = new Runnable() {
            @Override
            public void run() {
                invalidate();
            }
        };
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        mWidth = width;
        mHeight = height;

        centerX = mWidth / 2;
        centerY = mHeight / 2;

        radius = mHeight / 8;
        Log.d("allen", "onMeasure: " + centerX);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        circlePaint.setColor(POINT_COLOR_1);
        canvas.drawCircle(centerX, centerY, radius, circlePaint);
    }

    private void drawLoading(Canvas canvas, int index) {
        if (index < 0 || index > 2) return;
        switch (index) {
            case 0:
                circlePaint.setColor(POINT_COLOR_1);
                canvas.drawCircle(centerX - radius * 4, centerY, radius, circlePaint);
                circlePaint.setColor(POINT_COLOR_2);
                canvas.drawCircle(centerX, centerY, radius, circlePaint);
                circlePaint.setColor(POINT_COLOR_3);
                canvas.drawCircle(centerX + radius * 4, centerY, radius, circlePaint);
                break;
            case 1:
                circlePaint.setColor(POINT_COLOR_3);
                canvas.drawCircle(centerX - radius * 4, centerY, radius, circlePaint);
                circlePaint.setColor(POINT_COLOR_1);
                canvas.drawCircle(centerX, centerY, radius, circlePaint);
                circlePaint.setColor(POINT_COLOR_2);
                canvas.drawCircle(centerX + radius * 4, centerY, radius, circlePaint);
                break;
            case 2:
                circlePaint.setColor(POINT_COLOR_2);
                canvas.drawCircle(centerX - radius * 4, centerY, radius, circlePaint);
                circlePaint.setColor(POINT_COLOR_3);
                canvas.drawCircle(centerX, centerY, radius, circlePaint);
                circlePaint.setColor(POINT_COLOR_1);
                canvas.drawCircle(centerX + radius * 4, centerY, radius, circlePaint);
                break;
        }

    }


    public void startLoading() {
        if (isLoading) return;
        isLoading = true;
        mLoadingIndex = 0;
        invalidate();
    }

    public void stopLoading() {
        if (!isLoading) return;
        isLoading = false;
    }

    public boolean isLoading() {
        return isLoading;
    }

    /**
     * 统一处理paint
     *
     * @param strokeWidth
     * @param style
     * @return
     */
    public Paint getPaint(int strokeWidth, Paint.Style style) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStrokeWidth(5f);
        paint.setAntiAlias(true);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStyle(style);
        return paint;
    }


    /**
     * dp 2 px
     *
     * @param dpVal
     */
    protected int dp2px(int dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal, getResources().getDisplayMetrics());
    }

    /**
     * sp 2 px
     *
     * @param spVal
     * @return
     */
    protected int sp2px(int spVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spVal, getResources().getDisplayMetrics());

    }

}
