package org.jzs.mybaseapp.common.widget

import android.app.DialogFragment
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.TextUtils
import android.view.*
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import org.greenrobot.eventbus.EventBus
import org.jzs.mybaseapp.R
import org.jzs.mybaseapp.common.base.EventBusEntity

/**
 *
 */
class DatePickerDialog : DialogFragment() {
    lateinit var mBinding: org.jzs.mybaseapp.databinding.DialogDatepickerBinding
    override fun onStart() {
        super.onStart()
        setDialogStyle()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = activity.layoutInflater.inflate(R.layout.dialog_datepicker, null)
        mBinding = DataBindingUtil.bind(view)!!
        mBinding.tvCancel.setOnClickListener {
            dismiss()
        }

        mBinding.tvSure.setOnClickListener {
            var sleek = ""
            var workday = ""
            if (mBinding.tvMonday.cbisChecked) {
                sleek = sleek + "星期一|"
                workday = workday + "一、"
            }
            if (mBinding.tvTuesday.cbisChecked) {
                sleek = sleek + "星期二|"
                workday = workday + "二、"
            }
            if (mBinding.tvWednesday.cbisChecked) {
                sleek = sleek + "星期三|"
                workday = workday + "三、"
            }
            if (mBinding.tvThursday.cbisChecked) {
                sleek = sleek + "星期四|"
                workday = workday + "四、"
            }
            if (mBinding.tvFriday.cbisChecked) {
                sleek = sleek + "星期五|"
                workday = workday + "五、"
            }
            if (mBinding.tvSaturday.cbisChecked) {
                sleek = sleek + "星期六|"
                workday = workday + "六、"
            }
            if (mBinding.tvSunday.cbisChecked) {
                sleek = sleek + "星期日|"
                workday = workday + "日、"
            }
            if (TextUtils.isEmpty(workday)) {
                Toast.makeText(activity, "请选择天数", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            workday = workday.substring(0, workday.length - 1)
            sleek = sleek.substring(0, sleek.length - 1)
            if (mBinding.tvMonday.cbisChecked
                    && mBinding.tvTuesday.cbisChecked
                    && mBinding.tvWednesday.cbisChecked
                    && mBinding.tvThursday.cbisChecked
                    && mBinding.tvFriday.cbisChecked
                    && !mBinding.tvSaturday.cbisChecked
                    && !mBinding.tvSunday.cbisChecked) {
                workday = "工作日"
            }
            if (mBinding.tvMonday.cbisChecked
                    && mBinding.tvTuesday.cbisChecked
                    && mBinding.tvWednesday.cbisChecked
                    && mBinding.tvThursday.cbisChecked
                    && mBinding.tvFriday.cbisChecked
                    && mBinding.tvSaturday.cbisChecked
                    && mBinding.tvSunday.cbisChecked) {
                workday = "每天"
            }
            var item = EventBusEntity()
            item.workDay = workday
            item.sleek = sleek
            EventBus.getDefault().post(item)
            dismiss()
        }
        return mBinding.root
    }


    fun setDialogStyle() {
//        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Light_Dialog_MinWidth);
        //去掉dialog默认的padding
        dialog?.let {
            it.window!!.decorView.setPadding(20, 0, 20, 0)
            it.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val lp = it.window!!.attributes
            lp.width = WindowManager.LayoutParams.MATCH_PARENT
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT
            //设置dialog的位置在底部
            lp.gravity = Gravity.BOTTOM
            //设置dialog的动画
//        lp.windowAnimations = R.style.BottomDialogAnimation
            it.window!!.attributes = lp
        }
    }
}