package org.jzs.mybaseapp.common.base;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.jzs.mybaseapp.R;
import org.jzs.mybaseapp.common.utils.AppLog;

import androidx.fragment.app.Fragment;

/**
 * 所有的Fragment的基类
 *
 * @author Jzs created 2017/7/24
 */
public abstract class BaseFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        if (!isFastDoubleClick()) {
            super.startActivityForResult(intent, requestCode);
            getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        }
    }

    @Override
    public void startActivity(Intent intent) {
        if (!isFastDoubleClick()) {
            super.startActivity(intent);
            getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }


    /**
     * 判断是否是快速点击
     */
    private static long lastClickTime;

    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < 500) {
            AppLog.redLog("isFastDoubleClick", "true");
            return true;
        }
        lastClickTime = time;
        AppLog.redLog("isFastDoubleClick", "false");
        return false;

    }
}
