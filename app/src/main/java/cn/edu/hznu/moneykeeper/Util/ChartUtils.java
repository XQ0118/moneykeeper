package cn.edu.hznu.moneykeeper.Util;

import android.graphics.Color;

import java.util.List;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.view.LineChartView;

public class ChartUtils {

    //绘图
    public static void setChartData(LineChartView mChart, LineChartData mData, List<AxisValue> mAxisXValues,
                              List<Line> lines, List<PointValue> values, List<PointValue> values2,  Axis axisX, Axis axisY,
                              double[] perMonthCosts, double[] perMonthIncomes,  int[] months) {

//        //点
        for(int i = 0; i < 12; i++){
            values.add(new PointValue(i, (float) perMonthCosts[i]));
        }

        for(int i = 0; i < 12; i++){
            values2.add(new PointValue(i, (float) perMonthIncomes[i]));
        }

        //设置x轴
        for (int i = 0; i < months.length; i++) {
            mAxisXValues.add(new AxisValue(i).setLabel(String.valueOf(months[i]+"月")));
        }

        //线
        Line line = new Line(values);  //支出
        Line line1 = new Line(values2); //收入


        line.setStrokeWidth(1);//设置线的宽度
        line.setPointRadius(3);//坐标点大小
        line.setCubic(true);//曲线是否平滑，即是曲线还是折线
//        line.setFilled(true);//是否填充曲线的面积
        line.setHasLabels(true);//曲线的数据坐标是否加上备注
        line.setHasLabelsOnlyForSelected(true);//点击数据坐标提示数据
        line.setColor(Color.parseColor("#FF6F6F"));
        line.setShape(ValueShape.CIRCLE);
        line.setPointColor(Color.parseColor("#FF6F6F"));


        line1.setStrokeWidth(1);//设置线的宽度
        line1.setPointRadius(3);//坐标点大小
        line1.setCubic(true);//曲线是否平滑，即是曲线还是折线
//        line1.setFilled(true);//是否填充曲线的面积
        line1.setHasLabels(true);//曲线的数据坐标是否加上备注
        line1.setHasLabelsOnlyForSelected(true);//点击数据坐标提示数据
        line1.setColor(Color.parseColor("#02AE7C"));
        line1.setShape(ValueShape.CIRCLE);
        line1.setPointColor(Color.parseColor("#02AE7C"));

        lines.add(line);
        lines.add(line1);

        mData = new LineChartData();
        mData.setLines(lines);
        mChart.setLineChartData(mData);


        //x
        axisX = new Axis().setHasLines(true);
        axisX.setMaxLabelChars(1);
        axisX.setLineColor(Color.parseColor("#B0BBC5")); //x轴颜色
        axisX.setTextColor(Color.parseColor("#73798C"));//设置x轴字体的颜色
        axisX.setValues(mAxisXValues);
        mData.setAxisXBottom(axisX);

//        //Y轴
//        axisY = new Axis();
//        mData.setAxisYLeft(axisY);

    }
}
