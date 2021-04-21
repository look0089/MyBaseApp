package org.jzs.mybaseapp.section.weightdemo.anime.view;

/**
 * Created by Jzs on 2017/10/16 0016.
 */

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import org.jzs.mybaseapp.R;

import androidx.annotation.Nullable;


public class YuanView extends View {
    public static final String TAG = "CursorView";
    private Context mContext;


    public YuanView(Context context) {
        super(context);
        initPaint(context);
    }

    public YuanView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initPaint(context);
    }

    public YuanView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint(context);
    }

    public YuanView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initPaint(context);
    }

    // 1.创建一个画笔
    private Paint mPaint = new Paint();

    // 2.初始化画笔
    private void initPaint(Context context) {
        this.mContext = context;
        mPaint.setColor(mContext.getResources().getColor(R.color.orange_yellow));       //设置画笔颜色
        mPaint.setStyle(Paint.Style.FILL);  //设置画笔模式为填充
        mPaint.setStrokeWidth(20f);         //设置画笔宽度为10px
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mPaint.setStyle(Paint.Style.STROKE);
        //圆心，圆心，半径，paint
        canvas.drawCircle(getWidth() / 2, getWidth() / 2, (getWidth() - mPaint.getStrokeWidth()) / 2, mPaint);
    }

}

