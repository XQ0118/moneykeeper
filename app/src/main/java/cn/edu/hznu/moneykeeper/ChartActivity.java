package cn.edu.hznu.moneykeeper;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.litepal.LitePal;

import java.net.InterfaceAddress;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import cn.edu.hznu.moneykeeper.Util.DatePickerDialog;
import cn.edu.hznu.moneykeeper.Util.DateUtils;
import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;

import static cn.edu.hznu.moneykeeper.Util.DateUtils.FORMAT_Y;

public class ChartActivity extends AppCompatActivity {

    private boolean isCubic = false;
    public final static int[] months = new int[]{1,2,3,4,5,6,7,8,9,10,11,12};
    //选择时间
    protected int mYear;
    protected String days;
    protected String days_next;
    private TextView dateTv;  //时间选择
    public String year_need;
    public String month_1 = "01",
                  month_2 = "02",
                  month_3 = "03",
                  month_4 = "04",
                  month_5 = "05",
                  month_6 = "06",
                  month_7 = "07",
                  month_8 = "08",
                  month_9 = "09",
                  month_10 = "10",
                  month_11 = "11",
                  month_12 = "12";
    public double cost_month_1 = 0.00 ,
            cost_month_2 = 0.00,
            cost_month_3 = 0.00,
            cost_month_4 = 0.00,
            cost_month_5 = 0.00,
            cost_month_6 = 0.00,
            cost_month_7 = 0.00,
            cost_month_8 = 0.00,
            cost_month_9 = 0.00,
            cost_month_10 = 0.00,
            cost_month_11 = 0.00,
            cost_month_12 = 0.00;

    private LineChartView mChart;
    private LineChartData mData;
    public double[] perMonthCosts = new double[12];
    private List<AxisValue> mAxisXValues = new ArrayList<AxisValue>();
    public List<Line> lines = new ArrayList<>();
    public List<PointValue> values = new ArrayList<>();
    public Axis axisX,axisY;

    public List<CostBean> monthCostList;

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
        //初始化日期
        initTime();

        //chart
        mChart = (LineChartView) findViewById(R.id.linechart);

            //先清空金额数组
        for(int i=0;i<12;i++){
            perMonthCosts[i] = 0.0;
        }
            //处理数据
        setYearData(dateTv.getText().toString());

        getAxisPoints();
        //绘图
        setChartData();


        //时间选择器
        final LinearLayout choose_year = (LinearLayout) findViewById(R.id.btn_choose_year);
        choose_year.setOnClickListener(new View.OnClickListener() {
            Calendar c = Calendar.getInstance();
            @Override
            public void onClick(View v) {
                new DatePickerDialog(ChartActivity.this, 0, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker startDatePicker, int startYear, int startMonthOfYear,
                                          int startDayOfMonth) {
                        year_need = String.format("%d", startYear);
                        dateTv.setText(year_need);

                        cost_month_1 = 0.0;
                        cost_month_2 = 0.0;
                        cost_month_3 = 0.0;
                        cost_month_4 = 0.0;
                        cost_month_5 = 0.0;
                        cost_month_6 = 0.0;
                        cost_month_7 = 0.0;
                        cost_month_8 = 0.0;
                        cost_month_9 = 0.0;
                        cost_month_10 = 0.0;
                        cost_month_11 = 0.0;
                        cost_month_12 = 0.0;
                        //先清空金额数组
                        for(int i=0;i<12;i++){
                            perMonthCosts[i] = 0.0;
                        }

                        values.clear();
                        mAxisXValues.clear();
                        lines.clear();
                        //处理数据
                        setYearData(year_need);

                        getAxisPoints();

                        //x
                        axisX = new Axis();
                        mData.setAxisXBottom(axisX);
                        //绘图
                        setChartData();


                    }

                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE)).show();
            }
        });



    }

    //初始化当前年份
    protected void initTime(){
        //设置日期选择器初始日期
        mYear = Integer.parseInt(DateUtils.getCurYear(FORMAT_Y));
        //设置当前 日期
        days = DateUtils.getCurDateStr("yyyy");
        dateTv.setText(days);
    }

    public void setYearData(String years){
        //查询出某年某月所有的金额数据
        monthCostList = LitePal.findAll(CostBean.class);

        for(CostBean costBean: monthCostList){
            //状态1表示支出，0表示收入
            // 判断年
            if(costBean.colorType == 1 && costBean.getCostDate().substring(0, 4).equals(years)){
                //查询月
                if(costBean.getCostDate().substring(5, 7) .equals(month_1)){
                    cost_month_1 = cost_month_1 + Double.parseDouble(costBean.getCostMoney());
                }

                if(costBean.getCostDate().substring(5, 7) .equals(month_2)){
                    cost_month_2 = cost_month_2 + Double.parseDouble(costBean.getCostMoney());
                }

                if(costBean.getCostDate().substring(5, 7) .equals(month_3)){
                    cost_month_3 = cost_month_3 + Double.parseDouble(costBean.getCostMoney());
                }

                if(costBean.getCostDate().substring(5, 7) .equals(month_4)){
                    cost_month_4 = cost_month_4 + Double.parseDouble(costBean.getCostMoney());
                }

                if(costBean.getCostDate().substring(5, 7) .equals(month_5)){
                    cost_month_5 = cost_month_5 + Double.parseDouble(costBean.getCostMoney());
                }

                if(costBean.getCostDate().substring(5, 7) .equals(month_6)){
                    cost_month_6 = cost_month_6 + Double.parseDouble(costBean.getCostMoney());
                }

                if(costBean.getCostDate().substring(5, 7) .equals(month_7)){
                    cost_month_7 = cost_month_7 + Double.parseDouble(costBean.getCostMoney());
                }

                if(costBean.getCostDate().substring(5, 7) .equals(month_8)){
                    cost_month_8 = cost_month_8 + Double.parseDouble(costBean.getCostMoney());
                }

                if(costBean.getCostDate().substring(5, 7) .equals(month_9)){
                    cost_month_9 = cost_month_9 + Double.parseDouble(costBean.getCostMoney());
                }

                if(costBean.getCostDate().substring(5, 7) .equals(month_10)){
                    cost_month_10 = cost_month_10 + Double.parseDouble(costBean.getCostMoney());
                }

                if(costBean.getCostDate().substring(5, 7) .equals(month_11)){
                    cost_month_11 = cost_month_11 + Double.parseDouble(costBean.getCostMoney());
                }

                if(costBean.getCostDate().substring(5, 7) .equals(month_12)){
                    cost_month_12 = cost_month_12 + Double.parseDouble(costBean.getCostMoney());
                }
            }
            else {
                continue;
            }
        }


        perMonthCosts[0] = cost_month_1;
        perMonthCosts[1] = cost_month_2;
        perMonthCosts[2] = cost_month_3;
        perMonthCosts[3] = cost_month_4;
        perMonthCosts[4] = cost_month_5;
        perMonthCosts[5] = cost_month_6;
        perMonthCosts[6] = cost_month_7;
        perMonthCosts[7] = cost_month_8;
        perMonthCosts[8] = cost_month_9;
        perMonthCosts[9] = cost_month_10;
        perMonthCosts[10] = cost_month_11;
        perMonthCosts[11] = cost_month_12;

    }


    /**
     * 图表的每个点的显示
     */
    private void getAxisPoints() {
        for (int i = 0; i < 12; i++) {
            // 构造函数传参 位置 值
            values.add(new PointValue(i, (float) perMonthCosts[i]));
        }
    }

    //绘图
    private void setChartData() {

//        //点
//        for(int i = 0; i < 12; i++){
//            values.add(new PointValue(i, (float) perMonthCosts[i]));
//        }
//
        //设置x轴
        for (int i = 0; i < months.length; i++) {
            mAxisXValues.add(new AxisValue(i).setLabel(String.valueOf(months[i])));
        }

        //线
     Line line = new Line(values);
        line.setCubic(isCubic); //取消曲线折断
        line.setStrokeWidth(1);//设置线的宽度
        line.setPointRadius(2);//坐标点大小
        line.setCubic(true);//曲线是否平滑，即是曲线还是折线
        line.setFilled(true);//是否填充曲线的面积
//        line.setHasLabels(true);//曲线的数据坐标是否加上备注
        line.setHasLabelsOnlyForSelected(true);//点击数据坐标提示数据
        line.setColor(Color.parseColor("#9576fc"));
        line.setShape(ValueShape.CIRCLE);
        line.setPointColor(Color.parseColor("#9576fc"));
        lines.add(line);

        mData = new LineChartData();
        mData.setLines(lines);
        mChart.setLineChartData(mData);


        //x
        axisX = new Axis().setHasLines(true);
        axisX.setMaxLabelChars(1);
        axisX.setTextColor(Color.WHITE);//设置x轴字体的颜色
        axisX.setValues(mAxisXValues);
        axisX.setName("时间线");
        mData.setAxisXBottom(axisX);

//        //Y轴
//        axisY = new Axis();
//        mData.setAxisYLeft(axisY);

    }

    @SuppressLint("MissingSuperCall")
    public void onResume() {
        super.onResume();
        setChartData();
//        //处理数据
//        setYearData(year_need);
//
//        //先清除
//        values.clear();
//        mAxisXValues.clear();
//        lines.clear();
//
//        getAxisPoints();
//        //绘图
//        setChartData();
//        cost_month_1 = 0.0;
//        cost_month_2 = 0.0;
//        cost_month_3 = 0.0;
//        cost_month_4 = 0.0;
//        cost_month_5 = 0.0;
//        cost_month_6 = 0.0;
//        cost_month_7 = 0.0;
//        cost_month_8 = 0.0;
//        cost_month_9 = 0.0;
//        cost_month_10 = 0.0;
//        cost_month_11 = 0.0;
//        cost_month_12 = 0.0;
//        //先清空金额数组
//        for(int i=0;i<12;i++){
//            perMonthCosts[i] = 0.0;
//        }

    }


}
