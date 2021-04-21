package org.jzs.skutils.dialog

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.app.DialogFragment
import androidx.databinding.DataBindingUtil
import android.os.Bundle
import android.view.View
import org.jzs.skutils.R
import org.jzs.skutils.databinding.SkDialogNotitleBinding

/**
 * 无标题的 dialog
 */
@SuppressLint("ValidFragment")
class NoTitleDialog(text: String) : DialogFragment() {

    private var mCallBack: DialogCallBack? = null
    lateinit var bind: SkDialogNotitleBinding
    var okText: String = "确定"
    var cancelText: String = "取消"
    var contentText: String = text
    var showOk: Boolean = true


    /**
     * @param text 提示内容
     * @param showOk 是否显示确定按钮
     */
    constructor(text: String, showOk: Boolean) : this(text) {
        this.showOk = showOk
    }

    /**
     * @param text 提示内容
     * @param showOk 是否显示确定按钮
     * @param okText 确定按钮文字
     */
    constructor(text: String, showOk: Boolean, okText: String) : this(text, showOk) {
        this.showOk = showOk
        this.okText = okText
    }

    /**
     * @param text 提示内容
     * @param showOk 是否显示确定按钮
     * @param okText 确定按钮文字
     * @param cancelText 取消按钮文字
     */
    constructor(text: String, showOk: Boolean, okText: String, cancelText: String) : this(text, showOk, okText) {
        this.cancelText = cancelText
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)
        val view = activity.layoutInflater.inflate(R.layout.sk_dialog_notitle, null)
        bind = DataBindingUtil.bind(view)!!
        //传参方法
        //val args = Bundle()
        //args.putSerializable(Constant.EXTRA.EXTRA_ITEM, mList[position])
        //dialog.setArguments(args)

        //获得参数
        //val entity = arguments.getSerializable(Constant.EXTRA.EXTRA_ITEM) as BaseEntity
        //bind.setEntity(entity)
        bind.tvContent.text = contentText

        bind.btnOk.setOnClickListener {
            if (mCallBack != null) {
                mCallBack!!.sure()
            }
            dismiss()
        }
        bind.btnCancel.setOnClickListener {
            if (mCallBack != null) {
                mCallBack!!.cancel()
            }
            dismiss()
        }

        if (!showOk) {
            bind.btnCancel.visibility = View.GONE
        }

        bind.btnOk.text = okText
        bind.btnCancel.text = cancelText

        builder.setView(view)
        return builder.create()
    }

    fun setCallBack(dialogCallBack: DialogCallBack) {
        this.mCallBack = dialogCallBack
    }

    interface DialogCallBack {
        fun sure()

        fun cancel()
    }
}
