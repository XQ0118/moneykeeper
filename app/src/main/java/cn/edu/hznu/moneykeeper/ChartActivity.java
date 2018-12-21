package cn.edu.hznu.moneykeeper;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.view.MenuItem;
import android.view.WindowManager;

import com.github.mikephil.charting.charts.BarChart;

import java.util.ArrayList;
import java.util.List;

import cn.edu.hznu.moneykeeper.Util.BarChartManager;

public class ChartActivity extends AppCompatActivity {

    private BarChart barChart1, barChart2;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_chart_);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("BarChart");

        testBarChart();
    }

    private void testBarChart() {
        BarChartManager barChartManager1 = new BarChartManager(barChart1);
        BarChartManager barChartManager2 = new BarChartManager(barChart2);

        //设置x轴的数据
        ArrayList<String> xValues0 = new ArrayList<>();
        xValues0.add("早晨");
        xValues0.add("上午");
        xValues0.add("中午");
        xValues0.add("下午");
        xValues0.add("晚上");

        //设置x轴的数据
        ArrayList<Float> xValues = new ArrayList<>();
        for (int i = 1; i < 6; i++) {
            xValues.add((float) i);
        }

        //设置y轴的数据()
        List<List<Float>> yValues = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            List<Float> yValue = new ArrayList<>();
            for (int j = 0; j < 5; j++) {
                yValue.add((float) (Math.random() * 8)+2);
            }
            yValues.add(yValue);
        }

        //颜色集合
        List<Integer> colors = new ArrayList<>();
        colors.add(Color.GREEN);
        colors.add(Color.BLUE);
        colors.add(Color.RED);
        colors.add(Color.CYAN);

        //线的名字集合
        List<String> names = new ArrayList<>();
        names.add("柱状一");
        names.add("柱状二");
        names.add("柱状三");
        names.add("柱状四");

        //创建多条柱状的图表
        barChartManager1.showBarChart(xValues, yValues.get(0), names.get(1), colors.get(3));
        barChartManager2.showBarChart(xValues0, yValues,names);
    }



}
