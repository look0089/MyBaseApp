package org.jzs.mybaseapp.section.weightdemo.anime.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * 移动的三角形
 * <p>
 * Created by Jzs on 2017/10/16 0016.
 */

public class CursorView extends View {
    public static final String TAG = "CursorView";

    private float mRound = 35;
    private float trigonTop = 30;
    private float mMovePos = 60;

    public CursorView(Context context) {
        super(context);
        initPaint();
    }

    public CursorView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }

    public CursorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }

    public CursorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initPaint();
    }

    // 1.创建一个画笔
    private Paint mPaint = new Paint();

    // 2.初始化画笔
    private void initPaint() {
        mPaint.setColor(Color.BLACK);       //设置画笔颜色
        mPaint.setStyle(Paint.Style.FILL);  //设置画笔模式为填充
        mPaint.setStrokeWidth(10f);         //设置画笔宽度为10px
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        RectF rectF = new RectF(0, 0 + 30, getWidth(), getHeight());
        canvas.drawRoundRect(rectF, mRound, mRound, mPaint);

        //实例化路径
        Path path = new Path();
        path.moveTo(mMovePos - trigonTop, 0);// 此点为多边形的起点
        path.lineTo(mMovePos - (trigonTop * 2), 60);
        path.lineTo(mMovePos, 60);
        path.close(); // 使这些点构成封闭的多边形
        canvas.drawPath(path, mPaint);

    }

    public void setRound(float round) {
        if (mRound != round) {
            mRound = round;
            invalidate();
        }
    }

    public void setMovePos(float movePos) {
        if (mMovePos != movePos) {
            mMovePos = movePos;
            invalidate();
        }
    }

    public float getMovePos() {
        return mMovePos;
    }

    public float getRound() {
        return mRound;
    }
}
