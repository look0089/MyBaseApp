package org.jzs.skutils.dialog

import android.annotation.SuppressLint
import android.app.DialogFragment
import androidx.databinding.DataBindingUtil
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.TypedValue
import android.view.*
import android.widget.LinearLayout
import android.widget.TextView
import org.jzs.skutils.AppContext.Applications
import org.jzs.skutils.R

@SuppressLint("ValidFragment")
class IosDialog(list: ArrayList<String>) : DialogFragment(), View.OnClickListener {

    lateinit var mBinding: org.jzs.skutils.databinding.IosDialogLayoutBinding

    private var mList = list

    private var listener: OnDialogItemClickListener? = null

    private var sp = 16

    private var colorRes: Int = Applications.context().resources.getColor(R.color.velvet)

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = activity.layoutInflater.inflate(R.layout.ios_dialog_layout, null)
        mBinding = DataBindingUtil.bind(view)!!
        setDialogStyle()
        addTextView()
        mBinding.parent.setOnClickListener(this)
        return mBinding.root
    }

    override fun onClick(v: View) {
        for (i in mList.indices) {
            if (v.id == i) {
                if (listener != null) {
                    listener?.onItemClick(i, (v as TextView).text.toString())
                    dialog.dismiss()
                    break
                }
            }
        }

        if (v.id == R.id.parent) {
            dialog.dismiss()
        }
    }

    fun setOnItemClickListener(listener: OnDialogItemClickListener): IosDialog {
        this.listener = listener
        return this
    }

    fun setTextColor(colorRes: Int): IosDialog {
        this.colorRes = colorRes
        return this
    }

    @SuppressLint("NewApi")
    private fun addTextView() {
        val linear = mBinding.linear
        for (i in mList.indices) {
            val tv = TextView(context)
            tv.id = i
            tv.gravity = Gravity.CENTER
            val padding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16f, context.resources.displayMetrics).toInt()
            tv.setPadding(padding, padding, padding, padding)
            tv.setTextColor(colorRes)
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, sp.toFloat())
            tv.text = mList.get(i)
            tv.background = context.resources.getDrawable(R.drawable.shape_rec_coner)
            tv.setOnClickListener(this)
            val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            params.weight = 1f
            tv.layoutParams = params
            linear.addView(tv)

            if (i < mList.size - 1) {
                val line = View(context)
                line.setBackgroundColor(context.resources.getColor(R.color.line))
                val px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, 1f, context.resources.displayMetrics).toInt()
                val param = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, px)
                param.weight = 0f
                line.layoutParams = param
                linear.addView(line)
            }
        }
    }


    fun setDialogStyle() {
        this.dialog.setCanceledOnTouchOutside(true)
        //去掉dialog默认的padding
        dialog.window.decorView.setPadding(0, 0, 0, 0)
        val lp = dialog.window.attributes
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        //设置dialog的位置在底部
        lp.gravity = Gravity.BOTTOM
        //设置dialog的动画 从底部出现
        lp.windowAnimations = R.style.BottomDialogAnimation
        dialog.window.attributes = lp
        dialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }


}