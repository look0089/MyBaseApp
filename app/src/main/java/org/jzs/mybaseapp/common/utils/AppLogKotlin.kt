package org.jzs.mybaseapp.common.utils

import android.util.Log

import org.jzs.mybaseapp.common.system.AppConfig

/**
 * 打印日志调用
 *
 * @author Jzs created 2017/7/24
 */
class AppLogKotlin {

    companion object {

        val TAG = "AppLog"

        fun e(aLogInfo: String) {
            Log.e(TAG, aLogInfo)
        }

        fun e(aTag: String?, aLogInfo: String?) {
            if (AppConfig.IS_LOG && aLogInfo != null) {
                Log.e(aTag, aLogInfo)
            } else if (AppConfig.IS_LOG && aLogInfo == null) {
                Log.e(aTag, "null error")
            }
        }

        fun redLog(aTag: String?, aLogInfo: String?) {
            if (AppConfig.IS_LOG && aLogInfo != null) {
                Log.e(aTag, aLogInfo)
            } else if (AppConfig.IS_LOG && aLogInfo == null) {
                Log.e(aTag, "null error")
            }
        }

        fun greenLog(aTag: String?, aLogInfo: String?) {
            if (AppConfig.IS_LOG && aLogInfo != null) {
                Log.i(aTag, aLogInfo)
            } else if (AppConfig.IS_LOG && aLogInfo == null) {
                Log.e(aTag, "null error")
            }
        }

        fun yellowLog(aTag: String?, aLogInfo: String?) {
            if (AppConfig.IS_LOG && aLogInfo != null) {
                Log.w(aTag, aLogInfo)
            } else if (AppConfig.IS_LOG && aLogInfo == null) {
                Log.e(aTag, "null error")
            }
        }

        fun blackLog(aTag: String?, aLogInfo: String?) {
            if (AppConfig.IS_LOG && aLogInfo != null) {
                Log.d(aTag, aLogInfo)
            } else if (AppConfig.IS_LOG && aLogInfo == null) {
                Log.e(aTag, "null error")
            }
        }

        fun debug(aTag: String?, aLogInfo: String?) {
            if (AppConfig.IS_LOG && aLogInfo != null) {
                Log.e(aTag, aLogInfo)
            } else if (AppConfig.IS_LOG && aLogInfo == null) {
                Log.e(aTag, "null error")
            }

        }

    }
}
