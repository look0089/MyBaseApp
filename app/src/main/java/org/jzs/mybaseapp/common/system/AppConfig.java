package org.jzs.mybaseapp.common.system;

import android.content.Context;
import android.os.Environment;

/**
 * Created by Jzs on 2017/7/24.
 * 用于存放系统的一些常量参数
 * 如基地址、文件存放地址、是否开启log打印、SP储存名
 */

public class AppConfig {
    
    // 是否开启Log打印
    public static Boolean IS_LOG;

    public static Boolean GLOBAL_EXCEPTION_ENABLE; // 是否开启全部异常捕获
    public static String GLOBAL_EXCEPTION_ACTION;
    public static String GLOBAL_EXCEPTION_SDCARD_DIR; // 异常日志存放路径

    // 图片缓存最大容量，150M，根据自己的需求进行修改
    public static final int GLIDE_CATCH_SIZE = 150 * 1000 * 1000;
    // 图片缓存子目录
    public static final String GLIDE_CARCH_DIR = "image_catch";
    // 应用储存路径
    public static String APP_SDCARD_DIR;

    public static String BASE_URL;

    public static void init(Context context) {
//        BASE_URL = context.getString(R.string.base_url);
        BASE_URL = "http://www.yigongkeji.cc:8010";
        IS_LOG = true;

        APP_SDCARD_DIR = Environment.getExternalStorageDirectory()+ "/MyBaseApp/";
        initExceptionConfig();
    }

    /**
     * 全局异常捕获设置
     */
    public static void initExceptionConfig() {
        GLOBAL_EXCEPTION_ENABLE = true;
        GLOBAL_EXCEPTION_ACTION = "org.jzs.mybaseapp.common.utils.GlobalExceptionHanlder";
        GLOBAL_EXCEPTION_SDCARD_DIR = APP_SDCARD_DIR + "log/";
    }


    /**
     * 登录
     */
    public static final String LOGIN = "/mobile/weixiu/view/member/login";

}
