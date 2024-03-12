package org.jzs.mybaseapp.section.demo.curve

import android.graphics.Color
import org.jzs.mybaseapp.section.demo.curve.MPActivity.DataEntity
import com.github.mikephil.charting.data.LineData
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.github.mikephil.charting.data.Entry
import org.jzs.mybaseapp.R
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.IValueFormatter
import com.github.mikephil.charting.utils.ViewPortHandler
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import org.jzs.mybaseapp.databinding.ActivityMpBinding
import setAxis
import setData
import setPieData
import java.util.ArrayList

/**
 * @author Jzs created 2017/8/2
 */
class MPActivity : AppCompatActivity() {
    lateinit var mBinding: ActivityMpBinding
    private val mLineList: MutableList<DataEntity> = ArrayList()
    private var mLineData = LineData()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_mp)
        createXData()
        mLineData = makeLineData(mLineList)
        mBinding.lineChart.setDrawBorders(false)
        mBinding.lineChart.data = mLineData
        mBinding.lineChart.description.text = "功率"
        initPieChart()
        initBarChart()
    }

    inner class DataEntity {
        var HourNum: String? = null
        var HourPower = "0"
    }

    private fun createXData() {
        for (i in 6..21) {
            for (j in 0..5) {
                val hourDataEntity = DataEntity()
                if (j == 0) {
                    hourDataEntity.HourNum = "$i:$j"
                    hourDataEntity.HourPower = (i + j).toString()
                } else {
                    hourDataEntity.HourNum = i.toString() + ":" + j + 0
                    hourDataEntity.HourPower = (i + j).toString()
                }
                mLineList.add(hourDataEntity)
            }
        }
    }

    private fun makeLineData(list: List<DataEntity>): LineData {

        // 制作数据
        val y =
            ArrayList<Entry>()
        for (i in list.indices) {
            val hourDataEntity = list[i]
            val yData = java.lang.Float.valueOf(hourDataEntity.HourPower)
            val entry =
                Entry(i.toFloat(), yData) //(x,y)
            y.add(entry)
        }

        // y轴数据集
        val mLineDataSet = LineDataSet(y, "功率(W)")

        // 线宽
        mLineDataSet.lineWidth = 1.5f
        // 折线的颜色
        mLineDataSet.color = Color.WHITE
        //是否显示圆形球
        mLineDataSet.setDrawCircles(false)

        // 设置这项上显示的数据点的字体大小。
        mLineDataSet.valueTextSize = 10.0f
        mLineDataSet.valueTextColor = resources.getColor(R.color.orange_yellow)

        // 默认是直线,改变折线样式，用曲线。
        mLineDataSet.mode = LineDataSet.Mode.CUBIC_BEZIER
        // 曲线的平滑度，值越大越平滑。
        mLineDataSet.cubicIntensity = 0.2f

        // 填充曲线下方的区域，半透明。
        mLineDataSet.setDrawFilled(true)
        mLineDataSet.fillAlpha = 128
        mLineDataSet.fillColor = resources.getColor(R.color.deep_blue_light)
        mLineDataSet.valueTextColor = Color.WHITE

        // 设置折线上显示数据的格式。如果不设置，将默认显示float数据格式。
        mLineDataSet.valueFormatter =
            IValueFormatter { value: Float, entry: Entry?, dataSetIndex: Int, viewPortHandler: ViewPortHandler? -> "" }
        val mLineDataSets: MutableList<ILineDataSet> = ArrayList()
        if (y.size != 0) {
            mLineDataSets.add(mLineDataSet)
        }
        return LineData(mLineDataSets)
    }

    private fun initPieChart() {
        mBinding.pieChart.description.text = "性别比例"
        val pieNlEntries: MutableList<PieEntry> = ArrayList()
        pieNlEntries.add(PieEntry(30f, "女"))
        pieNlEntries.add(PieEntry(20f, "男"))

        setPieData(mBinding.pieChart, pieNlEntries, "性别")
    }

    fun initBarChart() {
        mBinding.barChart.description.text = "地区"
        setAxis(mBinding.barChart)
        setData(mBinding.barChart)
    }
}