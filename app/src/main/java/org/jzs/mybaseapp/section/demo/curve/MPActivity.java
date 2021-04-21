package org.jzs.mybaseapp.section.demo.curve;

import androidx.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import org.jzs.mybaseapp.R;
import org.jzs.mybaseapp.databinding.ActivityMpBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jzs created 2017/8/2
 */

public class MPActivity extends AppCompatActivity {

    private ActivityMpBinding mBinding;
    private List<DataEntity> mLineList = new ArrayList<>();
    private LineData mLineData = new LineData();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_mp);
        createXData();
        mLineData = makeLineData(mLineList);
        mBinding.lineChart.setDrawBorders(false);
        mBinding.lineChart.setData(mLineData);
    }

    public class DataEntity {
        public String HourNum;
        public String HourPower = "0";
    }

    private void createXData() {
        for (int i = 6; i < 22; i++) {
            for (int j = 0; j < 6; j++) {
                DataEntity hourDataEntity = new DataEntity();
                if (j == 0) {
                    hourDataEntity.HourNum = i + ":" + j;
                    hourDataEntity.HourPower = i + j + "";
                } else {
                    hourDataEntity.HourNum = i + ":" + j + 0;
                    hourDataEntity.HourPower = i + j + "";
                }
                mLineList.add(hourDataEntity);
            }
        }
    }


    private LineData makeLineData(List<DataEntity> list) {

        // 制作数据
        ArrayList<Entry> y = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            DataEntity hourDataEntity = list.get(i);
            Float yData = Float.valueOf(hourDataEntity.HourPower);
            Entry entry = new Entry(i, yData);//(x,y)
            y.add(entry);
        }

        // y轴数据集
        LineDataSet mLineDataSet = new LineDataSet(y, "功率(W)");

        // 线宽
        mLineDataSet.setLineWidth(1.5f);
        // 折线的颜色
        mLineDataSet.setColor(Color.WHITE);
        //是否显示圆形球
        mLineDataSet.setDrawCircles(false);

        // 设置这项上显示的数据点的字体大小。
        mLineDataSet.setValueTextSize(10.0f);
        mLineDataSet.setValueTextColor(getResources().getColor(R.color.orange_yellow));

        // 默认是直线,改变折线样式，用曲线。
        mLineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        // 曲线的平滑度，值越大越平滑。
        mLineDataSet.setCubicIntensity(0.2f);

        // 填充曲线下方的区域，半透明。
        mLineDataSet.setDrawFilled(true);
        mLineDataSet.setFillAlpha(128);
        mLineDataSet.setFillColor(getResources().getColor(R.color.deep_blue_light));
        mLineDataSet.setValueTextColor(Color.WHITE);

        // 设置折线上显示数据的格式。如果不设置，将默认显示float数据格式。
        mLineDataSet.setValueFormatter((value, entry, dataSetIndex, viewPortHandler) -> "");

        List<ILineDataSet> mLineDataSets = new ArrayList<ILineDataSet>();
        if (y.size() != 0) {
            mLineDataSets.add(mLineDataSet);
        }
        LineData mLineData = new LineData(mLineDataSets);
        return mLineData;
    }
}
