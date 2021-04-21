package org.jzs.mybaseapp.common.widget

import android.app.DialogFragment
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.NumberPicker
import androidx.databinding.DataBindingUtil
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jzs.mybaseapp.R
import org.jzs.mybaseapp.common.base.EventBusEntity
import org.jzs.mybaseapp.common.utils.ToastUtils
import java.util.ArrayList

/**
 *
 */
class TimePickerDialog : DialogFragment() {
    lateinit var mBinding: org.jzs.mybaseapp.databinding.DialogTimepickerBinding
    var startTime = "08:00"
    var endTime = "20:00"
    var startTimePos = 0
    var endTimePos = 0
    var sleek = ""

    override fun onStart() {
        super.onStart()
        setDialogStyle()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EventBus.getDefault().register(this)
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun getWorkDay(day: EventBusEntity) {
        mBinding.tvWorkday.text = day.workDay
        sleek = day.sleek
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = activity.layoutInflater.inflate(R.layout.dialog_timepicker, null)
        mBinding = DataBindingUtil.bind(view)!!


        setNumberPickerDividerColor(mBinding.npBefore)
        mBinding.npBefore.value = startTimePos
        mBinding.npBefore.setOnValueChangedListener { picker, oldVal, newVal ->
            startTime = picker.displayedValues[newVal]
            mBinding.tvTime.text = "$startTime-$endTime"
        }

        setNumberPickerDividerColor(mBinding.npAfter)
        mBinding.npAfter.value = endTimePos
        mBinding.npAfter.setOnValueChangedListener { picker, oldVal, newVal ->
            endTime = picker.displayedValues[newVal]
            mBinding.tvTime.text = "$startTime-$endTime"
        }
        mBinding.tvWorkday.setOnClickListener {
            var dialog = DatePickerDialog()
            dialog.show(this.fragmentManager, "tinker")
        }

        mBinding.tvCancel.setOnClickListener {
            dismiss()
        }

        mBinding.tvSure.setOnClickListener {
            submit()
        }
        return mBinding.root
    }


    fun getTimeList(): ArrayList<String> {
        val mEleList = ArrayList<String>()
        for (i in 0..23) {
            for (j in 0..11) {
                var createTime = ""
                if (i < 10) {
                    createTime = "0$i:"
                } else {
                    createTime = "$i:"
                }
                if (j < 2) {
                    createTime = createTime + "0" + j * 5
                } else {
                    createTime += j * 5
                }
                mEleList.add(createTime)
            }
        }
        return mEleList
    }

    fun setNumberPickerDividerColor(numberPicker: NumberPicker) {
        numberPicker.displayedValues = getTimeList().toTypedArray()
        numberPicker.minValue = 0
        numberPicker.maxValue = numberPicker.displayedValues.size - 1
        var pickerFields = NumberPicker::class.java.getDeclaredFields()
        pickerFields.forEach {
            if (it.name == "mSelectionDivider") {
                it.isAccessible = true
                try {
                    //设置分割线的颜色值
                    it.set(numberPicker, ColorDrawable(this.resources.getColor(R.color.transparent)))
                } catch (e: Exception) {
                    e.printStackTrace();
                }
            }
        }
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


    fun submit() {
        if (sleek.isEmpty()) {
            ToastUtils.showToast("请选择工作日")
            return
        }
        ToastUtils.showToast(sleek + "的" + "$startTime-$endTime")
    }

}