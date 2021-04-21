package org.jzs.mybaseapp.common.widget

import android.app.AlertDialog
import android.app.Dialog
import android.app.DialogFragment
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import org.jzs.mybaseapp.R
import org.jzs.mybaseapp.common.base.BaseEntity
import org.jzs.mybaseapp.common.system.Constant
import org.jzs.mybaseapp.databinding.FragmentTestDialogBinding

class TestDialogFragment : DialogFragment() {
    lateinit var mBinding: FragmentTestDialogBinding
    lateinit var orderItem: BaseEntity

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)
        val view = activity.layoutInflater.inflate(R.layout.fragment_test_dialog, null)
        mBinding = DataBindingUtil.bind(view)!!
        builder.setView(view)

        //获得参数
        orderItem = arguments.getSerializable(Constant.EXTRA.EXTRA_ITEM) as BaseEntity

        mBinding.ivClose.setOnClickListener {
            dismiss()
        }
        mBinding.btnSure.setOnClickListener {
            dismiss()
        }
        return builder.create()
    }

}