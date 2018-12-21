package cn.edu.hznu.moneykeeper.Util;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.util.List;

public class StringAxisValueFormatter implements IAxisValueFormatter {

    private List<String> xValues;

    public StringAxisValueFormatter(List<String> xValues) {
        this.xValues = xValues;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        try{
            if (value < 0 || value > (xValues.size() - 1)){//使得两侧柱子完全显示
                return "";
            }
            return xValues.get((int)value);
        }catch (Exception e){
            return "";
        }

    }
}
