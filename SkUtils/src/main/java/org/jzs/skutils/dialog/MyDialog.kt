package org.jzs.skutils.dialog

import android.app.Dialog
import android.content.Context
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.Button
import android.widget.TextView
import org.jzs.skutils.R


/**
 * 通用dialog
 */
class MyDialog : View.OnClickListener {

    private var dialog: Dialog? = null

    private var context: Context? = null

    private var mCallBack: DialogCallBack? = null

    fun showDialog(context: Context): Dialog {
        this.context = context
        dialog = Dialog(context, R.style.custom_dialog)
        dialog!!.setContentView(R.layout.dialog_my)
        dialog!!.findViewById<View>(R.id.btn_cancel).setOnClickListener(this)
        dialog!!.findViewById<View>(R.id.btn_ok).setOnClickListener(this)
        dialog!!.show()
        return dialog as Dialog
    }

    fun setContent(str: String) {
        (dialog!!.findViewById<View>(R.id.tv_content) as TextView).text = str
    }

    fun setTitle(str: String) {
        (dialog!!.findViewById<View>(R.id.tv_title) as TextView).text = str
    }

    fun setCancleGone() {
        dialog!!.findViewById<View>(R.id.btn_cancel).visibility = View.GONE
    }

    fun setSureGone() {
        dialog!!.findViewById<View>(R.id.btn_ok).visibility = View.GONE
    }

    fun setSureText(str: String) {
        (dialog!!.findViewById<View>(R.id.btn_ok) as Button).text = str
    }

    fun setCancleText(str: String) {
        (dialog!!.findViewById<Button>(R.id.btn_cancel))!!.text = str
    }

    fun setCancleBackground(res: Int) {
        dialog!!.findViewById<Button>(R.id.btn_cancel).setBackgroundColor(ContextCompat.getColor(context!!, res))
    }

    fun setCallBack(dialogCallBack: DialogCallBack) {
        this.mCallBack = dialogCallBack
    }

    override fun onClick(v: View) {
        val i = v.id
        if (i == R.id.btn_cancel) {
            if (mCallBack != null) {
                mCallBack!!.cancel()
            }
            dialog!!.dismiss()

        } else if (i == R.id.btn_ok) {
            if (mCallBack != null) {
                mCallBack!!.sure()
            }
            dialog!!.dismiss()

        }
    }

    interface DialogCallBack {
        fun sure()

        fun cancel()
    }
}
