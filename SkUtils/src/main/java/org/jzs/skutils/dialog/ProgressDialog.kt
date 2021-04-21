package org.jzs.skutils.dialog

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.widget.TextView
import org.jzs.skutils.R


/**
 * 加载圈
 * @author Jzs created 2018/3/1
 * @email 355392668@qq.com
 */
@SuppressLint("ValidFragment")
object ProgressDialog {
    private var dialog: Dialog? = null
    /**
     * 通过 isShow == 0 来显示加载圈，重复创建时对 isShow 进行 +1，消失时 -1，当 isShow = 0 的时候再消失
     * 保证请求多接口时，所有接口都有响应后才会消失
     */
    var isShow = 0
    private val TAG = "ProgressDialog"
    private var canCancel = true
    private var loadingText = "加载中"

    /**
     * 得到自定义的progressDialog
     *
     * @param context
     * @return
     */
    @JvmStatic
    fun createLoadingDialog(context: Context?) {
        if (dialog != null && !dialog!!.isShowing()) {
            isShow = 0
        }
        if (isShow == 0) {
            isShow++
            if (context is Activity) {
                if (!context.isFinishing) {
                    dialog = Dialog(context, R.style.custom_dialog)
                    dialog!!.setCancelable(canCancel)// 不可以用“返回键”取消
                    dialog!!.setContentView(R.layout.sk_dialog_loading)// 设置布局
                    dialog!!.findViewById<TextView>(R.id.tv_loading).text = loadingText
                    dialog!!.show()
                }
            } else {
                dialog = Dialog(context!!, R.style.custom_dialog)
                dialog!!.setCancelable(canCancel)// 不可以用“返回键”取消
                dialog!!.setContentView(R.layout.sk_dialog_loading)// 设置布局
                dialog!!.findViewById<TextView>(R.id.tv_loading).text = loadingText
                try {
                    dialog!!.show()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    //是否能用返回键取消
    fun setCanCancel(canCancel: Boolean): ProgressDialog {
        this.canCancel = canCancel
        return ProgressDialog
    }

    fun setLoadingText(text: String): ProgressDialog {
        this.loadingText = text
        return ProgressDialog
    }


    @JvmStatic
    fun dismissDialog() {
        isShow -= 1
        if (isShow == 0) {
            try {
                if (dialog != null) {
                    dialog!!.dismiss()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    @JvmStatic
    fun forceDismissDialog() {
        isShow = 0
        try {
            if (dialog != null) {
                dialog!!.dismiss()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}