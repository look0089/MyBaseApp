package org.jzs.mybaseapp.section.otherdemo.waterpic;

import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import org.jzs.mybaseapp.common.widget.swipebacklayout.ViewDragHelper;

import androidx.core.view.MotionEventCompat;

/**
 * 用ViewDraghelper实现可拖动的viewGroup
 * 资料：http://blog.csdn.net/lmj623565791/article/details/46858663
 */


public class VDHLayout extends LinearLayout {

    private ViewDragHelper viewDragHelper;
    private View view3;//仅内部自由滑动

    private Point mAutoBackOriginPos = new Point();

    public VDHLayout(Context context) {
        this(context, null, 0);
    }

    public VDHLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VDHLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        viewDragHelper = ViewDragHelper.create(this, 1.0f, new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                return true;//true表示可以拖动
            }

            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) {
                if (child == view3) {
                    int leftBound = getPaddingLeft();
                    int rightBound = getWidth() - view3.getWidth() - leftBound;
                    int newLeft = Math.min(Math.max(left, leftBound), rightBound);
                    return newLeft;
                }
                return left;
            }

            @Override
            public int clampViewPositionVertical(View child, int top, int dy) {
                if (child == view3) {
                    int topBound = getPaddingTop();
                    int bottomBound = getHeight() - view3.getHeight() - topBound;
                    int newTop = Math.min(Math.max(top, topBound), bottomBound);
                    return newTop;
                }
                return top;
            }


            @Override
            public void onEdgeTouched(int edgeFlags, int pointerId) {
                super.onEdgeTouched(edgeFlags, pointerId);
            }

            //如果有view的clickable为true，必须要重写下面这两个方法,具体什么用还不清楚，貌似只要返回值大于0就好了...
            @Override
            public int getViewHorizontalDragRange(View child) {
                return getWidth() - child.getMeasuredHeight();
            }

            @Override
            public int getViewVerticalDragRange(View child) {
                return getMeasuredHeight() - child.getMeasuredHeight();
            }
        });
        viewDragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_LEFT);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        //保存这个view左上角的坐标信息
    }

    @Override
    public void computeScroll() {
        if (viewDragHelper.continueSettling(true)) {
            invalidate();
        }
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        view3 = getChildAt(0);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        //决定是否拦截
        int action = MotionEventCompat.getActionMasked(ev);
        if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
            viewDragHelper.cancel();
            return false;
        }
        return viewDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //交给viewdraghelper处理
        viewDragHelper.processTouchEvent(event);
        return true;
    }
}
