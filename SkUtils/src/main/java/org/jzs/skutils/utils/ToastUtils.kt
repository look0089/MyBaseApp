package org.jzs.skutils.utils

import android.content.Context
import android.support.design.widget.Snackbar
import android.view.View
import android.widget.Toast

/**
 * Created by Jzs on 2018/3/15.
 */

object ToastUtils {

    /**
     * 之前显示的内容
     */
    private var oldMsg: String? = null
    /**
     * Toast对象
     */
    private var toast: Toast? = null
    /**
     * 第一次时间
     */
    private var oneTime: Long = 0
    /**
     * 第二次时间
     */
    private var twoTime: Long = 0

    /**
     * 显示Toast
     *
     * @param message
     */
    @JvmStatic
    fun showToast(context: Context, message: String) {
        if (toast == null) {
            toast = Toast.makeText(context, message, Toast.LENGTH_SHORT)
            toast!!.show()
            oneTime = System.currentTimeMillis()
        } else {
            twoTime = System.currentTimeMillis()
            if (message == oldMsg) {
                if (twoTime - oneTime > Toast.LENGTH_SHORT) {
                    toast!!.show()
                }
            } else {
                oldMsg = message
                toast!!.setText(message)
                toast!!.show()
            }
        }
        oneTime = twoTime
    }

    fun showSnackbar(v: View, msg: String) {
        val snackbar = Snackbar.make(v, msg, Snackbar
                .LENGTH_LONG)
        snackbar.show()
        snackbar.setAction("确定") { view -> snackbar.dismiss() }
    }
}