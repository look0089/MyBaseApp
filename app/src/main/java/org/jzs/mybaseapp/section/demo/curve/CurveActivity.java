package org.jzs.mybaseapp.section.demo.curve;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.LinearLayout;

import org.jzs.mybaseapp.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jzs created 2017/8/2
 */

public class CurveActivity extends AppCompatActivity {

    private LinearLayout customCurveChart1, customCurveChart2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_curve);
        customCurveChart1 = (LinearLayout) findViewById(R.id.customCurveChart1);
        initCurveChart1();
        customCurveChart2 = (LinearLayout) findViewById(R.id.customCurveChart2);
        initCurveChart2();

    }

    /**
     * 初始化曲线图数据
     */
    private void initCurveChart1() {
        String[] xLabel = {"00", "04", "08", "12", "16", "24"};
        String[] yLabel = {"0", "100", "200", "300", "400", "500", "600", "700", "800", "900", "1000", "1100"};
        int[] data1 = {300, 500, 550, 500, 300, 700};
        int[] data2 = {400, 600, 650, 600, 400, 800};
        int[] data3 = {500, 700, 750, 700, 500, 900};
        List<int[]> data = new ArrayList<>();
        List<Integer> color = new ArrayList<>();
        data.add(data1);
        color.add(R.color.color14);
        data.add(data2);
        color.add(R.color.color13);
        data.add(data3);
        color.add(R.color.color25);
        customCurveChart1.addView(new CustomCurveChart(this, xLabel, yLabel, data, color, false));
    }

    private void initCurveChart2() {
        String[] xLabel = {"0点", "1点", "2点", "3点", "4点", "5点", "6点", "7点", "8点", "9点", "10点", "11点"};
        String[] yLabel = {"0", "100", "200", "300", "400", "500", "600", "700", "800", "900", "1000", "1100"};
        int[] data1 = {300, 500, 550, 500, 300, 700, 800, 750, 550, 600, 400, 300};
        int[] data2 = {500, 700, 750, 700, 500, 900, 1000, 950, 750, 800, 600, 500};
        List<int[]> data = new ArrayList<>();
        List<Integer> color = new ArrayList<>();
        data.add(data1);
        color.add(R.color.color26);
        data.add(data2);
        color.add(R.color.color14);
        customCurveChart2.addView(new CustomCurveChart(this, xLabel, yLabel, data, color, true));
    }
}
