package com.wf;

import android.content.SharedPreferences;

import org.jzs.mybaseapp.common.system.SharedPreferencesUtil;

/**
 * Created by Jzs on 2020/10/29.
 * 系统参数
 */
public class SystemParams {
    private static SharedPreferences sp = SharedPreferencesUtil.getSP();

    public static final String API_IP = "api_ip";
    public static final String LIVE = "live";
    public static final String IDCARD = "idcard";
    public static final String IS_TEMPERATURE = "ISTEMPERATURE";
    public static final String IS_ENROLL = "isEnroll";
    public static final String PASSWORDSTR = "passwordStr";

    /**
     * 是否开启雷达监测
     */
    public static boolean IS_RADAR = true;

    /**
     * 获取设置的局域网ip
     *
     * @return
     */
    public static String getLanIp() {
        return sp.getString(API_IP, "");
    }

    /**
     * 获取是否活体检测
     */
    public static boolean getLive() {
        return sp.getBoolean(LIVE, false);
    }

    /**
     * 是否连接身份证读卡器
     */
    public static boolean getIdCard() {
        return sp.getBoolean(IDCARD, false);
    }

    public static void saveIdCard(boolean value) {
        SharedPreferencesUtil.saveBoolean(SystemParams.IDCARD, value);
    }


    /**
     * 是否连接测温设备
     */
    public static boolean getIsTemperature() {
        return sp.getBoolean(IS_TEMPERATURE, false);
    }

    public static void saveIsTemperature(boolean value) {
        SharedPreferencesUtil.saveBoolean(SystemParams.IS_TEMPERATURE, value);
    }


    /**
     * 是否录入模式
     */
    public static boolean getIsEnroll() {
        return sp.getBoolean(IS_ENROLL, false);
    }

    public static void setIsEnroll(boolean value) {
        SharedPreferencesUtil.saveBoolean(SystemParams.IS_ENROLL, value);
    }


    /**
     * 进入设置的密码
     */
    public static String getPasswordstr() {
        return sp.getString(PASSWORDSTR, "");
    }
}
