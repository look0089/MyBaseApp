package org.jzs.mybaseapp.common.system;

import android.content.Context;
import android.content.SharedPreferences;

import org.jzs.mybaseapp.MyApplaciton;

/**
 * Created by Jzs on 2017/7/28.
 */

public class SharedPreferencesUtil {

    /**
     * 第一次启动
     */
    public static final String IS_FIRST = "is_first_open";

    /**
     * 是否登录
     */
    public static final String IS_LOGIN = "is_login";



    public static SharedPreferences getSP() {
        return MyApplaciton.getInstance().getSharedPreferences(Constant.SP_NAME, Context.MODE_PRIVATE);
    }

    public static void saveBoolean(String key, boolean value) {

        SharedPreferences sp = getSP();
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static boolean getBoolean(String key, boolean defValue) {

        SharedPreferences sp = getSP();
        return sp.getBoolean(key, defValue);
    }

    public static void saveString(String key, String value) {

        SharedPreferences sp = getSP();
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getString(String key, String defValue) {

        SharedPreferences sp = getSP();
        return sp.getString(key, defValue);
    }

    public static void saveLong(String key, long value) {

        SharedPreferences sp = getSP();
        SharedPreferences.Editor editor = sp.edit();
        editor.putLong(key, value);
        editor.commit();
    }

    public static long getLong(String key, long defValue) {

        SharedPreferences sp = getSP();
        return sp.getLong(key, defValue);
    }

    public static void saveInt(String key, int value) {

        SharedPreferences sp = getSP();
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public static int getInt(String key, int defValue) {

        SharedPreferences sp = getSP();
        return sp.getInt(key, defValue);
    }

    public static void saveFloat(String key, Float defValue) {

        SharedPreferences sp = getSP();
        SharedPreferences.Editor editor = sp.edit();
        editor.putFloat(key, defValue);
        editor.commit();
    }

    public static double getFloat(String key, Float defValue) {

        SharedPreferences sp = getSP();
        return sp.getFloat(key, defValue);
    }
}
