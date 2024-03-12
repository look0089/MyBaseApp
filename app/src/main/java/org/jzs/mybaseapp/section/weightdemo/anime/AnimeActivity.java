package org.jzs.mybaseapp.section.weightdemo.anime;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.IntEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jzs.mybaseapp.R;
import org.jzs.mybaseapp.common.base.EventBusEntity;
import org.jzs.mybaseapp.databinding.ActivityAnimeBinding;
import org.jzs.mybaseapp.section.weightdemo.anime.view.CursorView;
import org.jzs.mybaseapp.section.weightdemo.anime.view.LoadingButton;
import org.jzs.mybaseapp.section.weightdemo.anime.view.RemoteControlMenu;
import org.jzs.mybaseapp.section.weightdemo.anime.view.WaveHelper;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

/**
 * @author Jzs created 2017/8/2
 */

public class AnimeActivity extends AppCompatActivity {
    public static final String TAG = "AnimeActivity";

    private CursorView view;
    private RemoteControlMenu menu;
    private boolean isOpen = false;
    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };
    private TextView tv;
    int countDownTime = 1000;
    private ActivityAnimeBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_anime);
        view = (CursorView) findViewById(R.id.cursorview);
        //遥控器
        menu = (RemoteControlMenu) findViewById(R.id.menu);
        EventBus.getDefault().register(this);
        initPlayBtn();
        initWave(0.5f);
        LoadingButton viewById = findViewById(R.id.btn_load);
        viewById.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewById.startLoading();
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onMessageEvent(EventBusEntity entity) {
        if (entity.isMeun == true) {
            menu.setVisibility(View.VISIBLE);
            EventBus.getDefault().removeAllStickyEvents();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void initPlayBtn() {
        tv = (TextView) findViewById(R.id.tv);
        tv.setOnClickListener(v -> {
            if (isOpen) {
                performAnimate(tv, 220, 100);
//                videoPlayer.onVideoPause();
                mHandler.removeCallbacks(runnable);
            } else {
                performAnimate(tv, 100, 220);
//                videoPlayer.startPlayLogic();
                mHandler.postDelayed(runnable, 1000);
            }
            isOpen = !isOpen;
        });
    }

    //倒计时
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
//            int countDownTime = (videoPlayer.getDuration() - videoPlayer.getCurrentPositionWhenPlaying()) / 1000;
            countDownTime--;
            tv.setText("\"" + countDownTime + "\"" + "     ■");
            mHandler.postDelayed(this, 1000);
        }
    };

    //textview变形动画
    private void performAnimate(final View target, final int start, final int end) {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(1, 100);

        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            //持有一个IntEvaluator对象，方便下面估值的时候使用
            private IntEvaluator mEvaluator = new IntEvaluator();

            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                //获得当前动画的进度值，整型，1-100之间
                int currentValue = (Integer) animator.getAnimatedValue();

                //计算当前进度占整个动画过程的比例，浮点型，0-1之间
                float fraction = currentValue / 100f;

                //这里我偷懒了，不过有现成的干吗不用呢
                //直接调用整型估值器通过比例计算出宽度，然后再设给Button
                target.getLayoutParams().width = mEvaluator.evaluate(fraction, start, end);
                target.requestLayout();
            }
        });
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                tv.setText("");
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (isOpen) {
                    tv.setText("\"" + countDownTime + "\"" + "     ■");
                } else {
                    tv.setText("►");
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        valueAnimator.setDuration(150).start();
    }


    public void setFloat(float pos) {
        List<Animator> animators = new ArrayList<>();

        ObjectAnimator movePosAnim = ObjectAnimator.ofFloat(view, "movePos", view.getMovePos(), pos);
        movePosAnim.setRepeatCount(0);
        movePosAnim.setDuration(200);
        movePosAnim.setInterpolator(new AccelerateDecelerateInterpolator());
        animators.add(movePosAnim);

        int i = (int) (Math.random() * 100);
        ObjectAnimator roundAnim = ObjectAnimator.ofFloat(view, "round", view.getRound(), i);
        roundAnim.setRepeatCount(0);
        roundAnim.setDuration(200);
        roundAnim.setInterpolator(new AccelerateInterpolator());
        animators.add(roundAnim);

        AnimatorSet mAnimatorSet = new AnimatorSet();
        mAnimatorSet.playTogether(animators);
        mAnimatorSet.start();
    }

    public float getX(View v) {
        Log.e(TAG, "getX: " + v.getRight());
        Log.e(TAG, "getX: " + v.getWidth());
        Log.e(TAG, "getX: " + (v.getRight() - v.getWidth() / 2));
        return v.getRight() - v.getWidth() / 2;
    }

    public void btn1(View v) {
        setFloat(getX(v));
    }

    public void btn2(View v) {
        setFloat(getX(v));
    }

    public void btn3(View v) {
        setFloat(getX(v));
    }

    public void btn4(View v) {
        setFloat(getX(v));
    }


    private void initWave(float level) {
        WaveHelper mWaveHelper = new WaveHelper(mBinding.wave, level);
        mBinding.wave.setBorder(5, Color.parseColor("#2798BA"));
        mBinding.wave.setWaveColor(
                Color.parseColor("#52BADD"), Color.parseColor("#82D3EE")
        );
        mBinding.wave.setWaterLevelRatio(level);
        mWaveHelper.start();
    }
}
