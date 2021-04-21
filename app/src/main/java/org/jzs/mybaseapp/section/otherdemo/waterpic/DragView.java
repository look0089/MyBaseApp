package org.jzs.mybaseapp.section.otherdemo.waterpic;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import org.jzs.mybaseapp.common.utils.CommonUtils;
import org.jzs.mybaseapp.common.utils.ScreenUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;

/**
 * Created by Jzs on 2017/11/1 0001.
 */

public class DragView extends View {
    List<PhotoEntity> mList = new ArrayList<>();
    Context mContext;

    public DragView(Context context) {
        super(context);
        this.mContext = context;
        initPaint();
    }

    public DragView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        initPaint();
    }

    public DragView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        initPaint();
    }

    public DragView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mContext = context;
        initPaint();
    }

    // 1.创建一个画笔
    private Paint mPaint = new Paint();

    // 2.初始化画笔
    private void initPaint() {
        mPaint.setColor(Color.BLACK);       //设置画笔颜色
        mPaint.setStyle(Paint.Style.FILL);  //设置画笔模式为填充
        mPaint.setStrokeWidth(10f);         //设置画笔宽度为10px
        mPaint.setTextSize(CommonUtils.convertDipToPx(mContext, 16));

        for (int i = 0; i < 5; i++) {
            PhotoEntity photoEntity = new PhotoEntity();
            photoEntity.title = "项目" + i;
            photoEntity.content = "内容" + i;
            mList.add(photoEntity);
        }
    }

    public void setList(List l) {
        this.mList = l;
        invalidate();
    }

    public List getList() {
        return mList;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        for (int i = 0; i < mList.size(); i++) {
            canvas.drawText(mList.get(i).toString(), 0, (i + 1) * (int) (mPaint.getTextSize() * 1.5), mPaint);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(measureWidth(), measureHeight());
    }

    private int measureHeight() {
        Log.e("sk", "TextSize(): " + mPaint.getTextSize());
        Log.e("sk", "mList.size(): " + mList.size());
        int result = (int) (mList.size() * 1.8) * (int) (mPaint.getTextSize());
        Log.e("sk", "measureHeight: " + result);
        return result;
    }

    private int measureWidth() {
        int temp = 0;
        for (int i = 0; i < mList.size(); i++) {
            String s = mList.get(i).toString();
            if (s.length() > temp) {
                temp = s.length();
            }
        }
        Log.e("sk", "temp: " + temp);
        Log.e("sk", "measureWidth: " + (temp * mPaint.getTextSize()));
        return (int) (temp * mPaint.getTextSize());
    }

    public float leftPercentage() {
        float screenWidth = ScreenUtils.getScreenWidth(mContext);
        float left = getLeft();
        Log.e("posimage", "left: " + getLeft());
        Log.e("posimage", "screenWidth: " + screenWidth);
        Log.e("posimage", "Percentage: " + left / screenWidth);
        return left / screenWidth;
    }

    public static final String TAG = "DragView";

    public float topPercentage() {
        float screenHeight = ScreenUtils.getScreenHeight(mContext);
        float top = getTop();
        Log.e("posimage", "top: " + getTop());
        Log.e("posimage", "screenHeight: " + screenHeight);
        Log.e("posimage", "Percentage: " + top / screenHeight);
        return top / screenHeight;
    }

}
