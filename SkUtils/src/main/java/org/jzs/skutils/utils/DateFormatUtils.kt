package org.jzs.skutils.utils

import android.annotation.SuppressLint
import android.text.TextUtils
import org.jzs.skutils.AppLog

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

/**
 * 日期格式化类
 *
 * @author jzs
 * @version v1.0
 * @e-mail 355392668@qq.com
 * @create-time 2017年7月26日17:03:29
 */

@SuppressLint("SimpleDateFormat")
object DateFormatUtils {
    private val TAG = "DateFormatUtils"

    /**
     * 获取当前日期 yyyy-mm-dd
     */
    val getNowYMD: String
        get() {
            val ca = Calendar.getInstance()
            val mYear = ca.get(Calendar.YEAR)
            val mMonth = ca.get(Calendar.MONTH) + 1
            val mDay = ca.get(Calendar.DAY_OF_MONTH)
            return "$mYear-$mMonth-$mDay"
        }

    /**
     * 获取当前年月 yyyy-mm
     */
    val getNowYM: String
        get() {
            val ca = Calendar.getInstance()
            val mYear = ca.get(Calendar.YEAR)
            val mMonth = ca.get(Calendar.MONTH) + 1
            return "$mYear-$mMonth"
        }


    /**
     * 由yyyy-MM-dd HH:mm:ss -> 时间戳
     *
     * @param datetime
     * @return
     */
    fun parseToYMDHMS(datetime: String): Long {
        if (TextUtils.isEmpty(datetime)) {
            return System.currentTimeMillis()
        }
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        return sdf.parse(datetime).time
    }


    /**
     * 时间戳 -> yyyy-MM-dd HH:mm:ss
     *
     * @param date
     * @return
     */
    fun formatWithYMDHms(date: Long): String {
        if (date == 0L) {
            return ""
        }
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        return sdf.format(Date(date))
    }


    /**
     * 转为 yyyy-MM-dd HH:mm
     *
     * @param date
     * @return
     */
    fun formatWithYMDHm(date: Long): String {
        if (date == 0L) {
            return ""
        }
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm")
        return sdf.format(Date(date))
    }


    /**
     * 转为 yyyy年MM月dd日 HH:mm
     *
     * @param date
     * @return
     */
    fun formatWithYMDHmChinese(date: Long): String {
        if (date == 0L) {
            return ""
        }
        val sdf = SimpleDateFormat("yyyy年MM月dd日 HH:mm")
        return sdf.format(Date(date))
    }

    /**
     * 转为yyyy-MM-dd
     *
     * @param date
     * @return
     */
    fun formatWithYMD(date: Long): String {
        if (date == 0L) {
            return ""
        }
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        return sdf.format(Date(date))
    }


    /**
     * 转为yyyy-MM
     *
     * @param date
     * @return
     */
    fun formatWithYM(date: Long): String {
        if (date == 0L) {
            return ""
        }
        val sdf = SimpleDateFormat("yyyy-MM")
        return sdf.format(Date(date))
    }


    /**
     * 转为MM-dd
     *
     * @param date
     * @return
     */
    fun formatWithMD(date: Long): String {
        if (date == 0L) {
            return ""
        }
        val sdf = SimpleDateFormat("MM-dd")
        return sdf.format(Date(date))
    }


    /**
     * 转为HH:mm
     *
     * @param date
     * @return
     */
    fun formatWithHm(date: Long): String {
        if (date == 0L) {
            return ""
        }
        val sdf = SimpleDateFormat("HH:mm")
        return sdf.format(Date(date))
    }


    /**
     * 转为MM-dd HH:mm
     *
     * @param date
     * @return
     */
    fun formatWithMDHm(date: Long): String {
        if (date == 0L) {
            return ""
        }
        val sdf = SimpleDateFormat("MM-dd HH:mm")
        return sdf.format(Date(date))
    }

    /**
     * 转为HH:mm:ss
     *
     * @param date
     * @return
     */
    fun formatWithHms(date: Long): String {
        if (date == 0L) {
            return ""
        }
        val sdf = SimpleDateFormat("HH:mm:ss")
        return sdf.format(Date(date))
    }


    /**
     * 由yyyy-MM-dd HH:mm\ -> 时间戳
     *
     * @param datetime
     * @return
     */
    fun parseToLong(datetime: String): Long {
        try {
            if (TextUtils.isEmpty(datetime)) {
                return System.currentTimeMillis()
            }
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm")
            return sdf.parse(datetime).time
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return System.currentTimeMillis()
    }

    fun parseToDate(datetime: String): Date? {
        try {
            if (TextUtils.isEmpty(datetime)) {
                return null
            }
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            return sdf.parse(datetime)
        } catch (e: ParseException) {
            AppLog.redLog(TAG, e.toString() + "")
        }

        return null
    }

    /**
     * 将日期转化为年月日时分秒
     *
     * @param date
     * @return
     */
    fun parseDateToString(date: Date): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        return sdf.format(date)
    }

    /**
     * 由yyyy-MM-dd -> 时间戳
     *
     * @param datetime
     * @return
     */
    fun parseToYMD(datetime: String): Long {
        try {
            if (TextUtils.isEmpty(datetime)) {
                return System.currentTimeMillis()
            }
            val sdf = SimpleDateFormat("yyyy-MM-dd")
            return sdf.parse(datetime).time
        } catch (e: ParseException) {
            AppLog.redLog(TAG, e.toString() + "")
        }

        return System.currentTimeMillis()
    }


    /**
     * 时间转换成毫秒
     *
     * @param expireDate
     * @return
     */
    fun getSecondsFromDate(expireDate: String?): Long {
        if (TextUtils.isEmpty(expireDate)) {
            return 0
        }
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm")
        var date: Date? = null
        return try {
            date = sdf.parse(expireDate)
            date!!.time
        } catch (e: ParseException) {
            e.printStackTrace()
            0
        }

    }


    /**
     * 返回时间差
     *
     * @param date
     * @return
     */
    fun deltaT(date: Long): String {
        val time = (System.currentTimeMillis() - date) / 1000
        return if (time < 60) {
            "刚刚"
        } else if (time < 3600) {
            (time / 60).toString() + "分钟前"
        } else if (time < 24 * 3600) {
            (time / 3600).toString() + "小时前"
        } else {
            (time / 24 / 3600).toString() + "天前"
        }
    }

    /**
     * 返回时间差超过1个月显示时间
     *
     * @param date
     * @return
     */
    fun deltaTF(date: Long): String {
        val time = (System.currentTimeMillis() - date) / 1000
        return if (time < 60) {
            "刚刚"
        } else if (time < 3600) {
            (time / 60).toString() + "分钟前"
        } else if (time < 24 * 3600) {
            (time / 3600).toString() + "小时前"
        } else {
            formatWithYMDHm(date)
        }
    }


    /**
     * 转换成时间格式(类似音乐、视频计算时间)
     *
     * @param time
     * @return
     */
    fun secToTime(time: Int): String {
        var timeStr: String? = null
        var hour = 0
        var minute = 0
        var second = 0
        if (time <= 0)
            return "00:00"
        else {
            minute = time / 60
            if (minute < 60) {
                second = time % 60
                timeStr = unitFormat(minute) + ":" + unitFormat(second)
            } else {
                hour = minute / 60
                if (hour > 99)
                    return "99:59:59"
                minute = minute % 60
                second = time - hour * 3600 - minute * 60
                timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second)
            }
        }
        return timeStr
    }

    /**
     * 之上的附属方法
     *
     * @param i
     * @return
     */
    private fun unitFormat(i: Int): String {
        var retStr: String? = null
        retStr = if (i in 0..9)
            "0" + Integer.toString(i)
        else
            "" + i
        return retStr
    }
}
