package cn.edu.hznu.moneykeeper.chart_fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.litepal.LitePal;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.edu.hznu.moneykeeper.Adapter.MonthListAdapter;
import cn.edu.hznu.moneykeeper.CostBean;
import cn.edu.hznu.moneykeeper.R;
import cn.edu.hznu.moneykeeper.Util.DateUtils;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.view.LineChartView;

import static cn.edu.hznu.moneykeeper.Util.ChartUtils.setChartData;
import static cn.edu.hznu.moneykeeper.Util.DateUtils.FORMAT_Y;


public class BeforeLastFragment extends Fragment {

    public  String month_1 = "01",
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
    public  double cost_month_1 = 0.00 ,
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
    public  double[] perMonthCosts = new double[12];

    public  double income_month_1 = 0.00 ,
            income_month_2 = 0.00,
            income_month_3 = 0.00,
            income_month_4 = 0.00,
            income_month_5 = 0.00,
            income_month_6 = 0.00,
            income_month_7= 0.00,
            income_month_8 = 0.00,
            income_month_9 = 0.00,
            income_month_10 = 0.00,
            income_month_11 = 0.00,
            income_month_12 = 0.00;
    public  double[] perMonthIncomes = new double[12];

    public List<CostBean> yearsCostList;

    private boolean isCubic = false;
    public final static int[] months = new int[]{1,2,3,4,5,6,7,8,9,10,11,12};
    //选择时间
    protected int mYear;
    protected String days;
    protected int year_temp;
    private TextView dateTv;  //时间选择
    public String year_need;


    public LineChartView mChart;
    public LineChartData mData;
    public List<AxisValue> mAxisXValues = new ArrayList<AxisValue>();
    public List<Line> lines = new ArrayList<>();
    public List<PointValue> values = new ArrayList<>();
    public List<PointValue> values2 = new ArrayList<>();
    public Axis axisX,axisY;

    private TextView total_expend, total_income;
    private double total_expend_money = 0.00, total_income_money = 0.00;

    //listview
    private LinearLayout mEmptyTv;
    private ListView mListView;
    private MonthListAdapter mMonthListAdapter;
    private List<Map<String, Object>> list;

    private View rootView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        if (null == rootView) {
            rootView = inflater.inflate(R.layout.fragment_beforelast, container,
                    false);
            mChart = (LineChartView) rootView.findViewById(R.id.chart_left);
            mListView = (ListView) rootView.findViewById(R.id.lv_main_left);
            mEmptyTv = (LinearLayout) rootView.findViewById(R.id.nothing_view);
            total_expend = (TextView) rootView.findViewById(R.id.expend_money_year);
            total_income = (TextView) rootView.findViewById(R.id.income_money_year);

            initView(rootView);
        }
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (null != parent) {
            parent.removeView(rootView);
        }
        return rootView;
    }

    private void initView(View rootView) {
        //获取到目标年份
        initBeforeLastYear();
        //总金额
        total_expend_money = 0.00;
        total_income_money = 0.00;
        initYearCostBean();

        //先清空数据
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
        //先清空数据
        income_month_1 = 0.0;
        income_month_2 = 0.0;
        income_month_3 = 0.0;
        income_month_4 = 0.0;
        income_month_5 = 0.0;
        income_month_6 = 0.0;
        income_month_7 = 0.0;
        income_month_8 = 0.0;
        income_month_9 = 0.0;
        income_month_10 = 0.0;
        income_month_11 = 0.0;
        income_month_12 = 0.0;
        //先清空金额数组
        for(int i=0;i<12;i++){
            perMonthIncomes[i] = 0.0;
        }

        values.clear();
        values2.clear();
        mAxisXValues.clear();
        lines.clear();



        //根据目标年份获取参数
        setYearCostData(year_need);
        setYearInComeData(year_need);
        //chart
        setChartData(mChart, mData, mAxisXValues, lines, values, values2, axisX, axisY, perMonthCosts, perMonthIncomes,  months);

        //listview
        initDataList();
        initListView();
    }
    @Override
    public void onResume() {
        super.onResume();
        initBeforeLastYear();
        //总金额
        total_expend_money = 0.00;
        total_income_money = 0.00;
        initYearCostBean();
        //listview
        initDataList();
        initListView();
    }

    //初始化当前年份
    protected void initBeforeLastYear(){
        //设置日期选择器初始日期
        mYear = Integer.parseInt(DateUtils.getCurYear(FORMAT_Y));
        //设置当前 日期
        days = DateUtils.getCurDateStr("yyyy");
        year_temp = Integer.parseInt(days);
        year_need = String.valueOf(year_temp - 2);
        dateTv = getActivity().findViewById(R.id.date_year);
        dateTv.setText(year_need);
    }

    //设置总金额
    protected void initYearCostBean() {
        //设置日期选择器初始日期
        mYear = Integer.parseInt(DateUtils.getCurYear(FORMAT_Y));
        //设置当前 日期
        days = DateUtils.getCurDateStr("yyyy");
        year_temp = Integer.parseInt(days);
        year_need = String.valueOf(year_temp - 2);
        yearsCostList = LitePal.findAll(CostBean.class);
        for (CostBean costBean : yearsCostList) {
            //1支出0收入
            if (costBean.colorType == 1 && costBean.getCostDate().substring(0, 4).equals(year_need)) {
                //查询年
                total_expend_money = total_expend_money + Double.parseDouble(costBean.getCostMoney());
            }
            if (costBean.colorType == 0 && costBean.getCostDate().substring(0, 4).equals(year_need)) {
                //查询年
                total_income_money = total_income_money + Double.parseDouble(costBean.getCostMoney());
            }
        }

        NumberFormat nf = new DecimalFormat("#,###.##");
        String str = nf.format(total_expend_money);
        total_expend.setText("¥ "+str);
        String str1 = nf.format(total_income_money);
        total_income.setText("¥ "+str1);
    }


    public  void setYearCostData(String years){
        //查询出某年某月所有的金额数据
        yearsCostList = LitePal.findAll(CostBean.class);

        for(CostBean costBean: yearsCostList){
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

    //income
    public  void setYearInComeData(String years){
        //查询出某年某月所有的金额数据
        yearsCostList = LitePal.findAll(CostBean.class);

        for(CostBean costBean: yearsCostList){
            //状态1表示支出，0表示收入
            // 判断年
            if(costBean.colorType == 0 && costBean.getCostDate().substring(0, 4).equals(years)){
                //查询月
                if(costBean.getCostDate().substring(5, 7) .equals(month_1)){
                    income_month_1 = income_month_1 + Double.parseDouble(costBean.getCostMoney());
                }

                if(costBean.getCostDate().substring(5, 7) .equals(month_2)){
                    income_month_2 = income_month_2 + Double.parseDouble(costBean.getCostMoney());
                }

                if(costBean.getCostDate().substring(5, 7) .equals(month_3)){
                    income_month_3 = income_month_3 + Double.parseDouble(costBean.getCostMoney());
                }

                if(costBean.getCostDate().substring(5, 7) .equals(month_4)){
                    income_month_4 = income_month_4 + Double.parseDouble(costBean.getCostMoney());
                }

                if(costBean.getCostDate().substring(5, 7) .equals(month_5)){
                    income_month_5 = income_month_5 + Double.parseDouble(costBean.getCostMoney());
                }

                if(costBean.getCostDate().substring(5, 7) .equals(month_6)){
                    income_month_6 = income_month_6 + Double.parseDouble(costBean.getCostMoney());
                }

                if(costBean.getCostDate().substring(5, 7) .equals(month_7)){
                    income_month_7 = income_month_7 + Double.parseDouble(costBean.getCostMoney());
                }

                if(costBean.getCostDate().substring(5, 7) .equals(month_8)){
                    income_month_8 = income_month_8 + Double.parseDouble(costBean.getCostMoney());
                }

                if(costBean.getCostDate().substring(5, 7) .equals(month_9)){
                    income_month_9 = income_month_9 + Double.parseDouble(costBean.getCostMoney());
                }

                if(costBean.getCostDate().substring(5, 7) .equals(month_10)){
                    income_month_10 = income_month_10 + Double.parseDouble(costBean.getCostMoney());
                }

                if(costBean.getCostDate().substring(5, 7) .equals(month_11)){
                    income_month_11 = income_month_11 + Double.parseDouble(costBean.getCostMoney());
                }

                if(costBean.getCostDate().substring(5, 7) .equals(month_12)){
                    income_month_12 = income_month_12 + Double.parseDouble(costBean.getCostMoney());
                }
            }
            else {
                continue;
            }
        }

        perMonthIncomes[0] = income_month_1;
        perMonthIncomes[1] = income_month_2;
        perMonthIncomes[2] = income_month_3;
        perMonthIncomes[3] = income_month_4;
        perMonthIncomes[4] = income_month_5;
        perMonthIncomes[5] = income_month_6;
        perMonthIncomes[6] = income_month_7;
        perMonthIncomes[7] = income_month_8;
        perMonthIncomes[8] = income_month_9;
        perMonthIncomes[9] = income_month_10;
        perMonthIncomes[10] = income_month_11;
        perMonthIncomes[11] = income_month_12;
    }


    //listview
    protected void initListView(){

        initDataList();
        // key值数组，适配器通过key值取value，与列表项组件一一对应
        String[] from = { "year", "months", "expend", "income", "surplus" };
        // 列表项组件Id 数组
        int[] to = { R.id.tv_year, R.id.tv_month, R.id.tv_month_expend_money,
                R.id.tv_month_income_money, R.id.tv_month_surplus_money };

        SimpleAdapter adapter = new SimpleAdapter(getContext(), list, R.layout.monthslist_item, from, to);
        mListView.setAdapter(adapter);
        mListView.setEmptyView(mEmptyTv);
    }

    private void initDataList() {

        list = new ArrayList<Map<String, Object>>();
        for (int i = 11; i >=0; i--) {
            if((perMonthCosts[i] !=0.00) || (perMonthIncomes[i] != 0.00)){
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("year", year_need + "年");
                map.put("months",  i+1+"月");
                map.put("expend", "¥"+ perMonthCosts[i]);
                map.put("income", "¥"+  perMonthIncomes[i]);
                map.put("surplus","¥"+ sub(perMonthIncomes[i] , perMonthCosts[i]));
                list.add(map);
            }

        }

    }

    //double减法
    public static Double sub(Double v1,Double v2){
        BigDecimal b1 = new BigDecimal(v1.toString());
        BigDecimal b2 = new BigDecimal(v2.toString());
        return b1.subtract(b2).doubleValue();
    }


}
