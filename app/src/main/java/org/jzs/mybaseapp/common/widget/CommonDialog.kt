package org.jzs.mybaseapp.common.widget

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.app.DialogFragment
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import org.jzs.mybaseapp.R
import org.jzs.mybaseapp.databinding.DialogCommonBinding

/**
 * 通用dialog
 */
@SuppressLint("ValidFragment")
class CommonDialog(text: String) : DialogFragment() {

    private var mCallBack: DialogCallBack? = null
    var bind: DialogCommonBinding? = null
    var text: String = text
    var okText: String = "确定"
    var showOk: Boolean = false

    constructor(text: String, showOk: Boolean) : this(text) {
        this.showOk = showOk
    }

    constructor(text: String, showOk: Boolean, okText: String) : this(text, showOk) {
        this.showOk = showOk
        this.okText = okText
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)
        val view = activity.layoutInflater.inflate(R.layout.dialog_common, null)
        bind = DataBindingUtil.bind(view)
        //传参方法
        //val args = Bundle()
        //args.putSerializable(Constant.EXTRA.EXTRA_ITEM, mList[position])
        //dialog.setArguments(args)

        //获得参数
        //val entity = arguments.getSerializable(Constant.EXTRA.EXTRA_ITEM) as BaseEntity
        //bind.setEntity(entity)
        bind?.tvContent?.text = text
        bind!!.btnOk.setOnClickListener {
            if (mCallBack != null) {
                mCallBack!!.sure()
            }
            dismiss()
        }
        bind!!.btnCancel.setOnClickListener {
            if (mCallBack != null) {
                mCallBack!!.cancel()
            }
            dismiss()
        }

        if (!showOk) {
            bind!!.btnCancel.visibility = View.GONE
        }

        bind!!.btnOk.text = okText

        builder.setView(view)
        return builder.create()
    }

    fun setCallBack(dialogCallBack: DialogCallBack) {
        this.mCallBack = dialogCallBack
    }


    fun setSureText(str: String) {
        bind!!.btnOk.text = str
    }


    interface DialogCallBack {
        fun sure()
        fun cancel()
    }
}
