import android.graphics.Color
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.formatter.IValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.utils.ViewPortHandler
import kotlin.math.roundToInt


fun setData(chart: BarChart) {
    val sets: MutableList<IBarDataSet> = ArrayList()
    // 此处有两个DataSet，所以有两条柱子，BarEntry（）中的x和y分别表示显示的位置和高度
    // x是横坐标，表示位置，y是纵坐标，表示高度
    val barEntries1: MutableList<BarEntry> = ArrayList()
    for (i in 0..3) {
        barEntries1.add(BarEntry(i.toFloat(), i.toFloat()))
    }
    val barDataSet1 = BarDataSet(barEntries1, "")
    barDataSet1.setDrawValues(true); // 不显示值
    barDataSet1.setValueTextColors(arrayListOf(Color.parseColor("#1B8FFE"))); // 字体添加颜色，按顺序给数据上色，不足则重复使用,也可以在单个dataSet上添加
    barDataSet1.valueTextSize = 10f;   // 文字大小
    barDataSet1.valueFormatter = object : IValueFormatter {  // 所有数据显示的数据值

        override fun getFormattedValue(
            value: Float,
            entry: Entry?,
            dataSetIndex: Int,
            viewPortHandler: ViewPortHandler?
        ): String {
            if (value.toInt() > 0) {
                return value.roundToInt().toString()
            }
            return ""
        }
    }
    barDataSet1.color = Color.parseColor("#1B8FFE") // 柱子的颜色
    barDataSet1.label = "男" // 设置标签之后，图例的内容默认会以设置的标签显示

    val barEntries2: MutableList<BarEntry> = ArrayList()
    for (i in 0..3) {
        barEntries2.add(BarEntry(i.toFloat(), (i + 2).toFloat()))
    }
    val barDataSet2 = BarDataSet(barEntries2, "")
    barDataSet2.setDrawValues(true); // 不显示值
    barDataSet2.setValueTextColors(arrayListOf(Color.parseColor("#ff8c00"))); // 字体添加颜色，按顺序给数据上色，不足则重复使用,也可以在单个dataSet上添加
    barDataSet2.valueTextSize = 10f   // 文字大小
    barDataSet2.valueFormatter = object : IValueFormatter {  // 所有数据显示的数据值

        override fun getFormattedValue(
            value: Float,
            entry: Entry?,
            dataSetIndex: Int,
            viewPortHandler: ViewPortHandler?
        ): String {
            if (value.toInt() > 0) {
                return value.roundToInt().toString()
            }
            return ""
        }
    }
    barDataSet2.color = Color.parseColor("#ff8c00") // 柱子的颜色
    barDataSet2.label = "女" // 设置标签之后，图例的内容默认会以设置的标签显示

    sets.add(barDataSet2)
    sets.add(barDataSet1)

    val barData = BarData(sets)
    barData.barWidth = 0.3f // 设置柱子的宽度
    /**
     * float groupSpace    //柱状图组之间的间距
     * float barSpace   //每条柱状图之间的间距  一组两个柱状图
     * float barWidth     //每条柱状图的宽度     一组两个柱状图
     * (barWidth + barSpace) * barAmount + groupSpace = 1.00
     * 3个数值 加起来 必须等于 1 即100% 按照百分比来计算 组间距 柱状图间距 柱状图宽度
     */
    val barAmount: Int = sets.size//需要显示柱状图的类别 数量

    //设置组间距占比30% 每条柱状图宽度占比 70% /barAmount  柱状图间距占比 0%
    val groupSpace = 0.3f //柱状图组之间的间距
    val barSpace = 0.05f
    val barWidth = (1f - groupSpace) / barAmount - 0.05f
    //设置柱状图宽度
    barData.setBarWidth(barWidth)
    //(起始点、柱状图组间距、柱状图之间间距)
    barData.groupBars(-0.5f, groupSpace, barSpace)

    chart.setData(barData)
}

fun setAxis(chart: BarChart) {
    var list = arrayListOf<String>("厦门", "龙岩", "漳州", "福州")
    // 设置x轴
    val xAxis: XAxis = chart.xAxis
    xAxis.position = XAxis.XAxisPosition.BOTTOM // 设置x轴显示在下方，默认在上方
    xAxis.setDrawGridLines(false) // 将此设置为true，绘制该轴的网格线。
    xAxis.labelCount = 4// 设置x轴上的标签个数
    xAxis.textSize = 14f // x轴上标签的大小
    // 设置x轴显示的值的格式
    xAxis.valueFormatter = IAxisValueFormatter { value, axis ->
        if (value.toInt() < 4) {
            list[value.toInt()]
        } else {
            ""
        }
    }
    xAxis.yOffset = 15f // 设置标签对x轴的偏移量，垂直方向

    // 设置y轴，y轴有两条，分别为左和右
    val yAxisRight: YAxis = chart.getAxisRight()
//    yAxisRight.axisMaximum = list.maxOf {
//        maxOf(it.devoffline, it.devonline).toFloat() + 30f
//    }// 设置y轴的最大值
    yAxisRight.axisMinimum = 0f // 设置y轴的最小值
    val yAxisLeft: YAxis = chart.getAxisLeft()
//    yAxisLeft.axisMaximum = list.maxOf {
//        maxOf(it.devoffline, it.devonline).toFloat() + 30f
//    }
    yAxisLeft.axisMinimum = 0f
    yAxisLeft.textSize = 14f // 设置y轴的标签大小
    yAxisLeft.isEnabled = false // 不显示右边的y轴
    yAxisRight.labelCount = 5;
    yAxisRight.valueFormatter = IAxisValueFormatter { value, axis ->
        value.toInt().toString()
    }
}

fun setLegend(chart: BarChart) {
    val legend: Legend = chart.getLegend()
    legend.setFormSize(10f) // 图例的图形大小
    legend.setTextSize(10f) // 图例的文字大小
    legend.setDrawInside(false) // 设置图例在图中
    legend.setOrientation(Legend.LegendOrientation.HORIZONTAL) // 图例的方向为垂直
    legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT) //显示位置，水平右对齐
    legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP) // 显示位置，垂直上对齐
    // 设置水平与垂直方向的偏移量
}


fun setPieData(mPieChart: PieChart, pieEntries: MutableList<PieEntry>, lable: String) {
    val iPieDataSet = PieDataSet(pieEntries, "aa")
    iPieDataSet.colors = arrayListOf(Color.parseColor("#ff8c00"), Color.parseColor("#1B8FFE"))
    iPieDataSet.setValueTextColors(arrayListOf(Color.parseColor("#000000"), Color.parseColor("#000000")))
    iPieDataSet.sliceSpace = 0f // 每块之间的距离
    iPieDataSet.valueFormatter = IValueFormatter { value, entry, dataSetIndex, viewPortHandler ->   // 所有数据显示的数据值
        value.roundToInt().toString()
    }
    //数据连接线距图形片内部边界的距离，为百分数
    iPieDataSet.valueLinePart1OffsetPercentage = 100f
    //设置连接线的颜色
    iPieDataSet.valueLineColor = Color.LTGRAY
    // 连接线在饼状图外面
    iPieDataSet.yValuePosition = PieDataSet.ValuePosition.OUTSIDE_SLICE
    iPieDataSet.valueTextSize = 12f

    //选中时候突出的间距
    iPieDataSet.selectionShift = 0f
    // 不显示图例
    mPieChart.legend.isEnabled = false

    // 设置pieChart图表是否可以手动旋转
    mPieChart.isRotationEnabled = true
    val pieData = PieData(iPieDataSet)
    mPieChart.data = pieData
    mPieChart.setDrawEntryLabels(true)
    mPieChart.setUsePercentValues(false) // 表内数据用百分比替代，而不是原先的值。并且ValueFormatter中提供的值也是该百分比的。默认false
    mPieChart.centerText = lable // 圆环中心的文字，会自动适配不会被覆盖
    mPieChart.setCenterTextSize(16f)
    mPieChart.centerTextRadiusPercent = 100f // 中心文本边界框矩形半径比例，默认是100%.
    mPieChart.holeRadius = 65f // 设置中心圆半径占整个饼形图圆半径（图表半径）的百分比。默认50%
    mPieChart.transparentCircleRadius =
        0f // 设置环形与中心圆之间的透明圆环半径占图表半径的百分比。默认55%（比如，中心圆为50%占比，而透明环设置为55%占比，要去掉中心圆的占比，也就是环只有5%的占比）
    mPieChart.setTransparentCircleAlpha(0) // 上述透明圆环的透明度[0-255]，默认100
    mPieChart.isUsePercentValuesEnabled
    mPieChart.maxAngle = 360f // 设置整个饼形图的角度，默认是360°即一个整圆，也可以设置为弧，这样现实的值也会重新计算
}