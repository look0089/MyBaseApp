package org.jzs.mybaseapp.section.otherdemo.ble;

import android.os.Handler;

/**
 * Created by Jzs on 2022/1/21.
 */
public class HandleUtils {
    private static Handler handler = null;

    public static Handler getInstace() {
        if (handler == null)
            handler = new Handler();
        return handler;
    }
}