package org.jzs.mybaseapp.common.base;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;


import org.jzs.mybaseapp.MyApplaciton;
import org.jzs.mybaseapp.R;
import org.jzs.mybaseapp.common.system.Constant;
import org.jzs.mybaseapp.common.utils.AppLog;
import org.jzs.mybaseapp.common.utils.AppManager;
import org.jzs.mybaseapp.common.widget.swipebacklayout.SwipeBackActivity;

/**
 * 所有的Activity的基类，前期先增加一个全局异常捕获，后续增加响应的接口方法来规范代码编写
 */
public class BaseActivity extends SwipeBackActivity {

    private static BaseActivity instance;

    @SuppressLint("InflateParams")
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 保存Activity到List中
        AppManager.getInstance().addActivity(this);
        // 默认开启侧滑，不需要侧滑的页面设置关闭
        setSwipeBackEnable(true);
    }

    public static BaseActivity getInstance() {
        if (instance == null) {
            instance = new BaseActivity();
        }
        return instance;
    }


    @Override
    protected void onResume() {
        super.onResume();
//        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
//        MobclickAgent.onPause(this);
    }

    /**
     * 回退事件
     */
    public void onClickLeft(View view) {
        finish();
    }

    /**
     * 判断是否是快速点击
     */
    private static long lastClickTime;

    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < 800) {
            AppLog.redLog("isFastDoubleClick", "true");
            return true;
        }
        lastClickTime = time;
        AppLog.redLog("isFastDoubleClick", "false");
        return false;

    }

    /**
     * 界面过渡动画----------start------------
     */
    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        if (isFastDoubleClick()) {
            return;
        }
        super.startActivityForResult(intent, requestCode);
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    @Override
    public void finish() {
        super.finish();
        // 移除List中的Activity
        AppManager.getInstance().finishActivity(this);
        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }

    /**
     * 界面过渡动画----------end------------
     */


    protected String getItemId() {
        return getIntent().getStringExtra(Constant.EXTRA.EXTRA_ITEM_ID);
    }

}
