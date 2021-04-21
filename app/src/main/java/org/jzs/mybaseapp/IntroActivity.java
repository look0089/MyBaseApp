package org.jzs.mybaseapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import org.jzs.mybaseapp.common.system.SharedPreferencesUtil;

import java.util.ArrayList;
import java.util.List;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

/**
 * 引导页及闪屏页
 *
 * @author Jzs created 2017/7/20
 */
public class IntroActivity extends Activity {

    static final String TAG = "IntroActivity";

    ImageView iv_intro;

    ViewPager vp_intro;

    // 引导图片资源
    private final int[] pics = {
//            R.drawable.intro1,
//            R.drawable.intro2,
//            R.drawable.intro3,
//            R.drawable.intro4
    };

    // 记录当前选中位置
    int currentIndex;

    private List<View> views;

    public void bindView() {

        iv_intro = (ImageView) findViewById(R.id.iv_intro);
        vp_intro = (ViewPager) findViewById(R.id.vp_intro);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        bindView();
        AfterViews();
    }

    public void AfterViews() {

        if (SharedPreferencesUtil.getBoolean(SharedPreferencesUtil.IS_FIRST, true)) {// 该应用是否第一次安装
//            iv_intro.setBackgroundResource(R.drawable.intro);
            showGuideView();
        } else {
//            iv_intro.setBackgroundResource(R.drawable.intro);
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    intentHome();
                }
            }, 2000);
        }
    }

    /**
     * 显示引导页
     */
    public void showGuideView() {

        iv_intro.postDelayed(new Runnable() {

            @Override
            public void run() {
                iv_intro.setAnimation(AnimationUtils.loadAnimation(IntroActivity.this, R.anim.alpha_dismiss));
                iv_intro.setVisibility(View.GONE);
            }
        }, 2000);
        views = new ArrayList<View>();
        for (int i = 0; i < pics.length; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setScaleType(ScaleType.FIT_XY);
            imageView.setBackgroundResource(pics[i]);
            if (i == pics.length - 1) {
                imageView.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        intentHome();
                        SharedPreferencesUtil.saveBoolean(SharedPreferencesUtil.IS_FIRST, false);
                    }
                });
            }
            views.add(imageView);
        }
        vp_intro.setAdapter(new ViewPagerAdapter());
        vp_intro.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {

            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });

    }

    /**
     * 初始化数据
     */
    private void intentHome() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.alpha_into, R.anim.alpha_bigger_dismiss);
        finish();
    }

    public class ViewPagerAdapter extends PagerAdapter {

        // 销毁arg1位置的界面
        @Override
        public void destroyItem(View arg0, int arg1, Object arg2) {

            ((ViewPager) arg0).removeView(views.get(arg1));
        }

        @Override
        public void finishUpdate(View arg0) {

        }

        // 获得当前界面数
        @Override
        public int getCount() {

            if (views != null) {
                return views.size();
            }

            return 0;
        }

        // 初始化arg1位置的界面
        @Override
        public Object instantiateItem(View arg0, int arg1) {

            ((ViewPager) arg0).addView(views.get(arg1), 0);

            return views.get(arg1);
        }

        // 判断是否由对象生成界面
        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {

            return (arg0 == arg1);
        }

        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {

        }

        @Override
        public Parcelable saveState() {

            return null;
        }

        @Override
        public void startUpdate(View arg0) {

        }

    }

}
