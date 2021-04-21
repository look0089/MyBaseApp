package org.jzs.mybaseapp.common.utils;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期格式化类
 *
 * @author jzs
 * @version v1.0
 * @e-mail 355392668@qq.com
 * @create-time 2017年7月26日17:03:29
 */

@SuppressLint("SimpleDateFormat")
public class DateFormatUtils {
    /**
     * 时间戳转日期
     *
     * @param date   时间戳
     * @param format yyyy-MM-dd HH:mm:ss
     */
    public static String timestampToDate(long date, String format) {
        if (date == 0) {
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(date));
    }

    /**
     * 日期转时间戳
     *
     * @param date   日期
     * @param format 日期格式 yyyy-MM-dd HH:mm:ss
     */
    public static long DateToTimestamp(String date, String format) {
        try {
            if (TextUtils.isEmpty(date)) {
                return System.currentTimeMillis();
            }
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return sdf.parse(date).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return System.currentTimeMillis();
    }


    /**
     * 时间转换成毫秒
     *
     * @param expireDate 时间
     * @param format     时间格式  yyyy-MM-dd HH:mm:ss
     */
    public static long getSecondsFromDate(String expireDate, String format) {
        if (expireDate == null || expireDate.trim().equals(""))
            return 0;
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date date = null;
        try {
            date = sdf.parse(expireDate);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 转换成时间格式(类似音乐、视频计算时间)
     *
     * @param time 秒
     */
    public static String secToTime(int time) {
        String timeStr;
        int hour;
        int minute;
        int second;
        if (time <= 0)
            return "00:00";
        else {
            minute = time / 60;
            if (minute < 60) {
                second = time % 60;
                timeStr = unitFormat(minute) + ":" + unitFormat(second);
            } else {
                hour = minute / 60;
                if (hour > 99)
                    return "99:59:59";
                minute = minute % 60;
                second = time - hour * 3600 - minute * 60;
                timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second);
            }
        }
        return timeStr;
    }

    public static String unitFormat(int i) {
        String retStr;
        if (i >= 0 && i < 10)
            retStr = "0" + i;
        else retStr = "" + i;
        return retStr;
    }


    /**
     * 传入时间戳与当前时间差
     *
     * @param date 时间戳
     */
    public static String deltaT(long date) {
        long time = (System.currentTimeMillis() - date) / 1000;
        if (time < 60) {
            return "刚刚";
        } else if (time < 3600) {
            return time / 60 + "分钟前";
        } else if (time < 24 * 3600) {
            return time / 3600 + "小时前";
        } else {
            return time / 24 / 3600 + "天前";
        }
    }

    /**
     * 传入时间戳与当前时间差
     * 返回时间差超过1个月显示时间
     *
     * @param date 时间戳
     */
    public static String deltaTF(long date) {
        long time = (System.currentTimeMillis() - date) / 1000;
        if (time < 60) {
            return "刚刚";
        } else if (time < 3600) {
            return time / 60 + "分钟前";
        } else if (time < 24 * 3600) {
            return time / 3600 + "小时前";
        } else {
            return timestampToDate(date, "yyyy-MM-dd HH:mm");
        }
    }
}
