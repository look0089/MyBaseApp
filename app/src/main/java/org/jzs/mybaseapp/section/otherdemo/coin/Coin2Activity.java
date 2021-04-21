package org.jzs.mybaseapp.section.otherdemo.coin;

import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

import org.jzs.mybaseapp.R;
import org.jzs.mybaseapp.common.system.SharedPreferencesUtil;
import org.jzs.mybaseapp.databinding.ActivityCoin2Binding;

import java.math.BigDecimal;

/**
 * 抛硬币
 *
 * @author Jzs created 2017/11/11 0011
 */
public class Coin2Activity extends AppCompatActivity {
    public static final String TAG = "Coin2Activity";

    private ActivityCoin2Binding mBinding;

    private ScaleAnimation sato0 = new ScaleAnimation(1, 1, 1, 0,
            Animation.RELATIVE_TO_PARENT, 0.5f, Animation.RELATIVE_TO_PARENT, 0.5f);

    private ScaleAnimation sato1 = new ScaleAnimation(1, 1, 0, 1,
            Animation.RELATIVE_TO_PARENT, 0.5f, Animation.RELATIVE_TO_PARENT, 0.5f);

    private ScaleAnimation sato3 = new ScaleAnimation(1, 1, 1, 0,
            Animation.RELATIVE_TO_PARENT, 0.5f, Animation.RELATIVE_TO_PARENT, 0.5f);

    private int mCount = 30;
    private int mTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_coin2);

        refreshTimeInfo();
        initView();
    }

    private void showImage1() {
        mBinding.image1.setVisibility(View.VISIBLE);
        mBinding.image2.setVisibility(View.GONE);
    }

    private void showImage2() {
        mBinding.image1.setVisibility(View.GONE);
        mBinding.image2.setVisibility(View.VISIBLE);
    }

    private void initView() {

        /**
         * 抛硬币
         */
        mBinding.setCoinClickable(true);
        mBinding.setTipVisibility(View.INVISIBLE);
        mBinding.root.setOnClickListener(v -> {
            mBinding.setTipVisibility(View.INVISIBLE);
            mBinding.setCoinClickable(false);
            mBinding.tvTip.setVisibility(View.INVISIBLE);

            TranslateAnimation translateAnimation = (TranslateAnimation) AnimationUtils.loadAnimation(this, R.anim.translate);
            mBinding.root.startAnimation(translateAnimation);
            mCount = 30;
            mTime = 0;
            mCount = mCount + (int) (Math.random() * 2);
            if (mBinding.image1.getVisibility() == View.VISIBLE) {
                mBinding.image1.startAnimation(sato0);
            } else {
                mBinding.image2.startAnimation(sato0);
            }
        });

        /**
         * 清空计数
         */
        mBinding.layoutTime.btnClean.setOnClickListener(v -> {
            SharedPreferencesUtil.saveInt("coin_time", 0);
            SharedPreferencesUtil.saveInt("coin_front", 0);
            SharedPreferencesUtil.saveInt("coin_back", 0);
            refreshTimeInfo();

        });

        showImage1();
        sato0.setDuration(30);
        sato1.setDuration(30);

        sato0.setAnimationListener(new MyAnimationListener() {

            @Override
            public void onAnimationEnd(Animation animation) {
                mTime++;
                if (mTime > mCount) {
                    mBinding.image1.setAnimation(null);
                    mBinding.image2.setAnimation(null);
                    showImage2();
                    mBinding.image2.startAnimation(sato3);
                    return;
                }
                if (mBinding.image1.getVisibility() == View.VISIBLE) {
                    mBinding.image1.setAnimation(null);
                    showImage2();
                    mBinding.image2.startAnimation(sato1);
                } else {
                    mBinding.image2.setAnimation(null);
                    showImage1();
                    mBinding.image1.startAnimation(sato1);
                }
            }

        });
        sato1.setAnimationListener(new MyAnimationListener() {

            @Override
            public void onAnimationEnd(Animation animation) {
                mTime++;
                if (mTime > mCount) {
                    mBinding.image2.setAnimation(null);
                    mBinding.image1.setAnimation(null);
                    showImage1();
                    mBinding.image1.startAnimation(sato3);
                    return;
                }
                if (mBinding.image1.getVisibility() == View.VISIBLE) {
                    mBinding.image2.setAnimation(null);
                    showImage1();
                    mBinding.image1.startAnimation(sato0);
                } else {
                    mBinding.image1.setAnimation(null);
                    showImage2();
                    mBinding.image2.startAnimation(sato0);
                }
            }
        });
        sato3.setAnimationListener(new MyAnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                mBinding.setCoinClickable(true);
                mBinding.tvTip.setVisibility(View.VISIBLE);

                int coin_time = SharedPreferencesUtil.getInt("coin_time", 0);
                SharedPreferencesUtil.saveInt("coin_time", coin_time + 1);

                if (mCount % 2 == 0) {
                    mBinding.tvTip.setText("正面！");
                    int coin_front = SharedPreferencesUtil.getInt("coin_front", 0);
                    SharedPreferencesUtil.saveInt("coin_front", coin_front + 1);
                } else {
                    mBinding.tvTip.setText("反面！");
                    int coin_back = SharedPreferencesUtil.getInt("coin_back", 0);
                    SharedPreferencesUtil.saveInt("coin_back", coin_back + 1);
                }
                mBinding.setTipVisibility(View.VISIBLE);

                refreshTimeInfo();
            }
        });
    }

    /**
     * 设置计数
     */
    private void refreshTimeInfo() {
        mBinding.layoutTime.tvTime.setText("次数：" + SharedPreferencesUtil.getInt("coin_time", 0));
        mBinding.layoutTime.tvFront.setText("正面：" + SharedPreferencesUtil.getInt("coin_front", 0));
        mBinding.layoutTime.tvBack.setText("反面：" + SharedPreferencesUtil.getInt("coin_back", 0));
        BigDecimal bigDecimal;
        try {
            bigDecimal = new BigDecimal((double) SharedPreferencesUtil.getInt("coin_front", 0) / (double) SharedPreferencesUtil.getInt("coin_time", 0) * 100).setScale(2, BigDecimal.ROUND_HALF_UP);
        } catch (Exception e) {
            bigDecimal = new BigDecimal(0);
        }
        mBinding.layoutTime.tvFrontRate.setText(bigDecimal + "%");
    }
}
