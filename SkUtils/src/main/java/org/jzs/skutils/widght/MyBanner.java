package org.jzs.skutils.widght;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import org.jzs.skutils.AppContext.Applications;
import org.jzs.skutils.R;
import org.jzs.skutils.utils.glide.GlideUtils;

import java.util.ArrayList;

public class MyBanner extends RelativeLayout {
    private ViewPager mViewPager;
    private ViewPagerAdapter mAdapter;
    private int position;

    //图片的个数
    private int itemCount;
    private ArrayList<String> imgUrlList;
    private ArrayList<View> imgViewList;

    private ArrayList<View> dotViewList;
    private int dotWidth = 20;

    private int dotBackgroundColor = Color.LTGRAY;
    private int dotselectedColor = Applications.context().getResources().getColor(R.color.theme);

    public MyBanner(Context context) {
        super(context);
        init();
    }

    public MyBanner(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyBanner(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        imgUrlList = new ArrayList<String>();
        imgViewList = new ArrayList<View>();
        mViewPager = new ViewPager(getContext());
        LayoutParams _layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        mViewPager.setLayoutParams(_layoutParams);
        mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int pos) {
                if (pos == 0) {
                    position = mAdapter.getCount() - 1;
                } else {
                    position = pos - 1;
                }
                switchDot();
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
                switch (arg0) {
                    case 1:// 手势滑动
                        break;
                    case 2:// 界面切换中
                        break;
                    case 0:// 滑动结束，即切换完毕或者加载完毕
                        // 当前为最后一张，此时从右向左滑，则切换到第一张
                        if (mViewPager.getCurrentItem() == mViewPager.getAdapter().getCount() - 1) {
                            mViewPager.setCurrentItem(1, false);
                        } else if (mViewPager.getCurrentItem() == 0) {
                            // 当前为第一张，此时从左向右滑，则切换到最后一张
                            mViewPager.setCurrentItem(mViewPager.getAdapter().getCount() - 2, false);
                        }
                        break;
                }
            }
        });
        addView(mViewPager);
    }

    private View getDotView() {
        if (itemCount < 1) {
            return null;
        }

        LinearLayout _lLay = new LinearLayout(getContext());
        _lLay.setPadding(0, 0, 8, 8);
        _lLay.setOrientation(LinearLayout.HORIZONTAL);
        LayoutParams _layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        //设置dot位置
        _layoutParams.addRule(ALIGN_PARENT_BOTTOM);
        _layoutParams.addRule(CENTER_IN_PARENT);
        _lLay.setLayoutParams(_layoutParams);

        dotViewList = new ArrayList<View>();
        for (int i = 0; i < imgUrlList.size(); i++) {
            View _dotView = new View(getContext());
            _dotView.setBackgroundDrawable(setViewShape(i));
            LinearLayout.LayoutParams _linearLayoutParams = new LinearLayout.LayoutParams(dotWidth, dotWidth);
            if (i > 0) {
                _linearLayoutParams.setMargins(dotWidth / 2, 0, 0, 0);
            }
            _dotView.setLayoutParams(_linearLayoutParams);
            _lLay.addView(_dotView);
            dotViewList.add(_dotView);
        }
        return _lLay;
    }

    /**
     * 动态添加shape(圆点)
     **/
    private GradientDrawable setViewShape(int position) {
        int strokeWidth = 1; //边框宽度
        int roundRadius = dotWidth / 2; // 圆角半径
        int strokeColor = dotBackgroundColor;// 边框颜色
        int fillColor = this.position == position ? dotselectedColor : dotBackgroundColor;// 内部填充颜色

        GradientDrawable _gd = new GradientDrawable();//创建drawable
        _gd.setColor(fillColor);
        _gd.setCornerRadius(roundRadius);
        _gd.setStroke(strokeWidth, strokeColor);

        return _gd;
    }

    /**
     * 添加轮播图片和下面的点
     **/
    public void setImgUrl(final ArrayList<String> imgUrlList) {
        if (null == imgUrlList || imgUrlList.size() == 0) {
            return;
        }

        position = 0;
        this.imgUrlList = imgUrlList;
        itemCount = imgUrlList.size();

        if (null != imgViewList && imgViewList.size() > 0) {
            imgViewList.clear();
        }

        if (itemCount > 1) {
            final int _position = itemCount - 1;
            imgViewList.add(newImageView(_position));
        }
        for (int i = 0; i < itemCount; i++) {
            final int _position = i;
            imgViewList.add(newImageView(_position));
        }
        if (itemCount > 1) {
            imgViewList.add(newImageView(0));
        }
        itemCount = imgViewList.size();
        //右下角的 dot 数
        addView(getDotView());

        mAdapter = new ViewPagerAdapter(imgViewList);
        mViewPager.setAdapter(mAdapter);

        mViewPager.setOffscreenPageLimit(itemCount);
        mViewPager.setCurrentItem(itemCount > 1 ? position + 1 : position);
        startSwitchImg();
    }

    private ImageView newImageView(final int position) {
        ImageView _img = new ImageView(getContext());
        _img.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        _img.setScaleType(ImageView.ScaleType.FIT_XY);
        _img.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.setOnImgClickListener(imgUrlList.get(position), position);
                }
            }
        });

        String _url = imgUrlList.get(position);
        GlideUtils.setImage(_url, _img);
        return _img;
    }

    /**
     * 开始切换图片
     **/
    private void startSwitchImg() {
        if (imgUrlList.size() <= 1) {
            return;
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                position++;
                if (position >= mAdapter.getCount() - 1) {
                    position = 0;
                    mViewPager.setCurrentItem(position + 1);
                } else {
                    mViewPager.setCurrentItem(position);
                }
                startSwitchImg();
            }
        }, 1500);
    }

    private void switchDot() {
        int _len = dotViewList.size();
        for (int i = 0; i < _len; i++) {
            View _view = dotViewList.get(i);
            _view.setBackgroundDrawable(setViewShape(i));
        }
    }

    private OnImgClickListener mListener;

    public interface OnImgClickListener {
        void setOnImgClickListener(String imgUrl, int position);
    }

    public void setOnImgClickListener(OnImgClickListener listener) {
        mListener = listener;
    }

    /******************  Adapter *********************************/
    /**
     * @author yangyu 功能描述：ViewPager适配器，用来绑定数据和view
     */
    private class ViewPagerAdapter extends PagerAdapter {

        // 界面列表
        private ArrayList<View> views;

        public ViewPagerAdapter(ArrayList<View> views) {
            this.views = views;
        }

        public void AddItems(ArrayList<View> viewList) {
            if (null == viewList) {
                return;
            }
            views.clear();
            views = viewList;
            notifyDataSetChanged();
        }

        //获得当前界面数
        @Override
        public int getCount() {
            if (views != null) {
                return views.size();
            }
            return 0;
        }

        // 初始化position位置的界面
        @Override
        public Object instantiateItem(View view, int position) {
            ((ViewPager) view).addView(views.get(position), 0);
            return views.get(position);
        }

        // 判断是否由对象生成界面
        @Override
        public boolean isViewFromObject(View view, Object arg1) {
            return (view == arg1);
        }

        //销毁position位置的界面
        @Override
        public void destroyItem(View view, int position, Object arg2) {
            ((ViewPager) view).removeView(views.get(position));
        }
    }
}


