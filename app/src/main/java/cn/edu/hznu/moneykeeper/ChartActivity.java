package cn.edu.hznu.moneykeeper;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import cn.edu.hznu.moneykeeper.Util.DatePickerDialog;
import cn.edu.hznu.moneykeeper.Util.DateUtils;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.view.LineChartView;

import static cn.edu.hznu.moneykeeper.Util.DateUtils.FORMAT_Y;

public class ChartActivity extends AppCompatActivity {

    //选择时间
    protected int mYear;
    protected int mMonth;
    protected int mDay;
    protected String days;
    private TextView dateTv;  //时间选择

    private LineChartView mChart;
    private Map<String,Integer> table = new TreeMap<>();
    private LineChartData mData;
    private List<AxisValue> mAxisXValues = new ArrayList<AxisValue>();
    private String[] data = {"1月","1月","1月","1月","1月","1月","1月","1月"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
        Toolbar toolbar = (Toolbar) findViewById(R.id.chart_toolbar);
        setSupportActionBar(toolbar);

        //Toolbar的事件---返回
        ImageButton home = (ImageButton) findViewById(R.id.back_to_home);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //回顾事件--
        TextView look_back = (TextView) findViewById(R.id.look_back);
        look_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChartActivity.this, LookBackActivity.class);
                startActivity(intent);
            }
        });

        //年份选择
        dateTv = (TextView) findViewById(R.id.date_year);
        initTime();
        LinearLayout choose_year = (LinearLayout) findViewById(R.id.btn_choose_year);
        choose_year.setOnClickListener(new View.OnClickListener() {

            Calendar c = Calendar.getInstance();
            @Override
            public void onClick(View v) {
                new DatePickerDialog(ChartActivity.this, 0, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker startDatePicker, int startYear, int startMonthOfYear,
                                          int startDayOfMonth) {
                        String textString = String.format("%d", startYear);
                        dateTv.setText(textString);
                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE)).show();

            }
        });

        //chart
        mChart = (LineChartView) findViewById(R.id.linechart);
        List<CostBean> allData = (List<CostBean>) getIntent().getSerializableExtra("cost_list_chart");
        //
        getAxisXLables();//获取x轴的标注

        setAllData(allData);
        //设置chart类型
        setChartData();

    }

    //初始化当前年份
    protected void initTime(){
        //设置日期选择器初始日期
        mYear = Integer.parseInt(DateUtils.getCurYear(FORMAT_Y));
        //设置当前 日期
        days = DateUtils.getCurDateStr("yyyy");
        dateTv.setText(days);
    }


    private void getAxisXLables() {
        for (int i = 0; i < data.length; i++) {
            mAxisXValues.add(new AxisValue(i).setLabel(data[i]));
        }
    }


    private void setChartData() {
        List<Line> lines = new ArrayList<>();
        List<PointValue> values = new ArrayList<>();
        //点

        int indexX = 0;
        for(Integer value: table.values()){
            values.add(new PointValue(indexX, value));
            indexX=indexX+30;
        }

        //线
        Line line = new Line(values);
        line.setCubic(true);//曲线是否平滑，即是曲线还是折线
        line.setFilled(true);//是否填充曲线的面积
        line.setHasLabels(true);//曲线的数据坐标是否加上备注
        //      line.setHasLabelsOnlyForSelected(true);//点击数据坐标提示数据
        line.setColor(Color.parseColor("#9576fc"));
        line.setShape(ValueShape.CIRCLE);
        line.setPointColor(Color.parseColor("#9576fc"));
        lines.add(line);
        mData = new LineChartData();
        mData.setLines(lines);
        mChart.setLineChartData(mData);

        Axis axisX = new Axis();

        Axis axisY = new Axis().setHasLines(true);

        axisX.setTextColor(Color.WHITE);//设置x轴字体的颜色

        axisY.setTextColor(Color.WHITE);//设置y轴字体的颜色

        axisX.setName("时间线");
        axisY.setName("金额");

        mData.setAxisXBottom(axisX);

        mData.setAxisYLeft(axisY);

    }

    private void setAllData(List<CostBean> allData) {
        if(allData != null){
            for (int i = 0; i <allData.size() ; i++) {
                CostBean costBean = allData.get(i);
                String costDate = costBean.costDate;
                double costMoney = Double.parseDouble(costBean.costMoney);
                if(!table.containsKey(costDate)){
                    table.put(costDate, (int) costMoney);
                }else {
                    double originMoney = table.get(costDate);
                    table.put(costDate, (int) (originMoney+costMoney));
                }
            }
        }
    }

    @SuppressLint("MissingSuperCall")
    public void onResume() {
        super.onResume();

        //设置chart类型
        setChartData();
    }
}
