package org.jzs.skutils.recycle;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;


/**
 * Created by susion on 17/1/19.
 */
public class RVUtils {

    public static LinearLayoutManager getLayoutManager(Context context, int orientation) {
        LinearLayoutManager manager = new LinearLayoutManager(context);
        manager.setOrientation(orientation);
        return manager;
    }

    public static StaggeredGridLayoutManager getStaggeredGridLayoutManager(int spanCount) {
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(spanCount, StaggeredGridLayoutManager.VERTICAL) {
            @Override
            public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
                try {
                    super.onLayoutChildren(recycler, state);
                } catch (IndexOutOfBoundsException e) {
                }
            }
        };
        return staggeredGridLayoutManager;
    }

    public static StaggeredGridLayoutManager getStaggeredGridLayoutHorizontalManager(int spanCount) {
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(spanCount, StaggeredGridLayoutManager.HORIZONTAL) {
            @Override
            public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
                try {
                    super.onLayoutChildren(recycler, state);
                } catch (IndexOutOfBoundsException e) {
                }
            }
        };
        return staggeredGridLayoutManager;
    }

    /**
     * 滚动到底部的监听.
     */
    public static void setOnLastItemVisibleListener(RecyclerView rv, final OnLastItemVisibleListener onLastItemVisibleListener) {

        rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && recyclerView.getAdapter() != null) {
                    RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();

                    if (layoutManager instanceof LinearLayoutManager) {
                        int lastVisiblePosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
                        if (lastVisiblePosition >= recyclerView.getAdapter().getItemCount() - 3) {
                            onLastItemVisibleListener.onLastItemVisible();
                        }
                    } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                        StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;
                        int last[] = new int[staggeredGridLayoutManager.getSpanCount()];
                        staggeredGridLayoutManager.findLastVisibleItemPositions(last);
                        for (int i = 0; i < last.length; i++) {
                            if (last[i] >= recyclerView.getAdapter().getItemCount() - 3) {
                                onLastItemVisibleListener.onLastItemVisible();
                                break;
                            }
                        }

                    } else if (layoutManager instanceof GridLayoutManager) {
                        GridLayoutManager manager = (GridLayoutManager) layoutManager;
                        int last[] = new int[manager.getSpanCount()];
                        if (last[0] >= recyclerView.getAdapter().getItemCount() - 3) {
                            onLastItemVisibleListener.onLastItemVisible();
                        }
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    //不画最后一条线
    public static class NoLastDividerDecoration extends RecyclerView.ItemDecoration {
        public Paint mDividerPaint;
        int mDividerHeight;
        Rect mMargin;

        public NoLastDividerDecoration(Context context, int colorId, int dividerHeight, Rect margin) {
            init(context, colorId, dividerHeight);
            mMargin = margin;
        }

        private void init(Context context, int colorId, int dividerHeight) {
            mDividerPaint = new Paint();
            mDividerPaint.setColor(context.getResources().getColor(colorId));
            mDividerHeight = dividerHeight;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            outRect.bottom = mDividerHeight;
        }

        @Override
        public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
            int childCount = parent.getChildCount();
            int left, right;

            if (mMargin.left > 0) {
                left = parent.getPaddingLeft() + mMargin.left;
            } else {
                left = parent.getPaddingLeft();
            }

            if (mMargin.right > 0) {
                right = parent.getWidth() - parent.getPaddingRight() - mMargin.right;
            } else {
                right = parent.getWidth() - parent.getPaddingRight();
            }

            for (int i = 0; i < childCount - 1; i++) {
                View view = parent.getChildAt(i);
                float top = view.getBottom();
                float bottom = view.getBottom() + mDividerHeight;
                c.drawRect(left, top, right, bottom, mDividerPaint);
            }
        }
    }
}
