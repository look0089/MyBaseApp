package org.jzs.skutils

import android.util.Log
import org.jzs.skutils.AppContext.Applications
import android.R.attr.tag


/**
 * 打印日志调用
 *
 * @author Jzs created 2017/7/24
 */
object AppLog {

    val TAG = "AppLog"

    var IS_LOG = java.lang.Boolean.parseBoolean(Applications.context().getString(R.string.print_log))

    @JvmStatic
    fun e(aLogInfo: String) {
        var msg = aLogInfo
        if (IS_LOG) {
            if (msg.length > 2001) {
                while (msg.length > 2001) {
                    Log.e(TAG, msg.substring(0, 2001))
                    msg = msg.substring(2001)
                }
            }
            //剩余部分
            Log.e(TAG, msg)
        }
    }

    @JvmStatic
    fun e(aTag: String?, aLogInfo: String?) {
        if (IS_LOG && aLogInfo != null) {
            e(aLogInfo)
        } else if (IS_LOG && aLogInfo == null) {
            Log.e(aTag, "null error")
        }
    }

    @JvmStatic
    fun redLog(aTag: String?, aLogInfo: String?) {
        if (IS_LOG && aLogInfo != null) {
            Log.e(aTag, aLogInfo)
        } else if (IS_LOG && aLogInfo == null) {
            Log.e(aTag, "null error")
        }
    }

    @JvmStatic
    fun greenLog(aTag: String?, aLogInfo: String?) {
        if (IS_LOG && aLogInfo != null) {
            Log.i(aTag, aLogInfo)
        } else if (IS_LOG && aLogInfo == null) {
            Log.e(aTag, "null error")
        }
    }

    @JvmStatic
    fun yellowLog(aTag: String?, aLogInfo: String?) {
        if (IS_LOG && aLogInfo != null) {
            Log.w(aTag, aLogInfo)
        } else if (IS_LOG && aLogInfo == null) {
            Log.e(aTag, "null error")
        }
    }

    @JvmStatic
    fun blackLog(aTag: String?, aLogInfo: String?) {
        if (IS_LOG && aLogInfo != null) {
            Log.d(aTag, aLogInfo)
        } else if (IS_LOG && aLogInfo == null) {
            Log.e(aTag, "null error")
        }
    }

    @JvmStatic
    fun debug(aTag: String?, aLogInfo: String?) {
        if (IS_LOG && aLogInfo != null) {
            Log.e(aTag, aLogInfo)
        } else if (IS_LOG && aLogInfo == null) {
            Log.e(aTag, "null error")
        }

    }


}
