package cn.edu.hznu.moneykeeper;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.litepal.LitePal;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.edu.hznu.moneykeeper.Adapter.CostListAdapter;
import cn.edu.hznu.moneykeeper.Adapter.HeadPagerAdapter;
import cn.edu.hznu.moneykeeper.Util.DateUtils;

import static cn.edu.hznu.moneykeeper.Util.DateUtils.FORMAT_M;
import static cn.edu.hznu.moneykeeper.Util.DateUtils.FORMAT_Y;

public class LookInfoActivity extends AppCompatActivity {


    private List<ImageView> mDots;//定义一个集合存储2个dot


    //选择时间

    public double month_cost_total = 0.00;
    public double month_income_total = 0.00;
    public TextView total_money, income_money;

    /*CostBeanlist*/
    private List<CostBean> mCostBeanList;
    /*CostBeanlist*/
    /*ListView*/
    private ListView costList;
    private CostListAdapter adapter;
    /*ListView*/


    private View mFooter; //footer
    private FloatingActionButton fab;
    private Intent intent;
    private View statusBarView;

    public String intentdays;
    public TextView showdays;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                initStatusBar();
                getWindow().getDecorView().removeOnLayoutChangeListener(this);
            }
        });
        setContentView(R.layout.activity_look_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.info_toolbar);

        setSupportActionBar(toolbar);
        income_money = findViewById(R.id.income_money_month);
        total_money = findViewById(R.id.expend_money_month);
        showdays = findViewById(R.id.info_date_year);

        Intent intent = getIntent();
        intentdays = intent.getStringExtra("intentdays");
        showdays.setText(intentdays);
        //初始化数据库
        LitePal.initialize(LookInfoActivity.this);

        //数据库金额显示listView
        mCostBeanList = new ArrayList<>();

        //初始化账单
        initCostData();

        LinearLayout home = (LinearLayout) findViewById(R.id.back_to_home_info);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //footer
        mFooter = LayoutInflater.from(LookInfoActivity.this).inflate(R.layout.my_footer, null);//加载footer布局
        costList.addFooterView(mFooter, "",false);

    }

    //初始化listview数据
    private void initCostData() {

        mCostBeanList.clear();
        List<CostBean> costBeans = LitePal.findAll(CostBean.class);
        for(CostBean costBean: costBeans) {
            if(costBean.getCostDate().substring(0, 7).equals(intentdays)){
                costBean.getCostTitle();
                costBean.getCostMoney();
                costBean.getCostDate();
                //costBean.getCostNote();
                mCostBeanList.add(costBean);
            }

        }

        costList = findViewById(R.id.lv_main);

        adapter = new CostListAdapter(LookInfoActivity.this, mCostBeanList);
        adapter.notifyDataSetChanged();
        costList.setAdapter(adapter);
        LinearLayout mEmptyTv = (LinearLayout) findViewById(R.id.nothing_view);
        costList.setEmptyView(mEmptyTv);
        Collections.reverse(mCostBeanList);

    }

    //重置MainActivity页面内容
    @Override
    protected void onResume() {
        super.onResume();

        //设置状态栏的渐变颜色
        getWindow().getDecorView().addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                initStatusBar();
                getWindow().getDecorView().removeOnLayoutChangeListener(this);
            }
        });

        //设置顶部栏的渐变色
        initStatusBar();
        //初始化金额数据
        initCostData();
        //重置当前月的支出
        month_cost_total = 0.00;
        month_income_total = 0.00;
        getPerMonthCost();
        getPerMonthIncome();


    }

    //初始化状态栏
    private void initStatusBar() {
        if (statusBarView == null) {
            //利用反射机制修改状态栏背景
            int identifier = getResources().getIdentifier("statusBarBackground", "id", "android");
            statusBarView = getWindow().findViewById(identifier);
        }
        if (statusBarView != null) {
            statusBarView.setBackgroundResource(R.drawable.bg_gradient);
        }
    }

    //获取当前月的支出
    public void getPerMonthCost(){

        //查询出当前月所有的金额数据
        List<CostBean> monthCostList = LitePal.findAll(CostBean.class);
        for(CostBean costBean: monthCostList){
            //状态1表示支出，0表示收入
            if(costBean.colorType == 1 && costBean.getCostDate().substring(0, 7) .equals(intentdays)  ){
                month_cost_total = month_cost_total + Double.parseDouble(costBean.getCostMoney());
            }
        }

        NumberFormat nf = new DecimalFormat("#,###.##");
        String str = nf.format(month_cost_total);
        if(month_cost_total==0.00){
            total_money.setText("0.00");
        }else {
            total_money.setText(str);
        }
    }

    //获取当前月的收入
    public void getPerMonthIncome(){

        //查询出当前月所有的金额数据
        List<CostBean> monthCostList = LitePal.findAll(CostBean.class);
        for(CostBean costBean: monthCostList){
            //状态1表示支出，0表示收入
            if(costBean.colorType == 0 && costBean.getCostDate().substring(0, 7) .equals(intentdays)  ){
                month_income_total = month_income_total + Double.parseDouble(costBean.getCostMoney());
            }
        }

        NumberFormat nf = new DecimalFormat("#,###.##");
        String str = nf.format(month_income_total);
        if(month_income_total == 0.00){
            income_money.setText("0.00");
        }
        else {
            income_money.setText(str);
        }
    }
}
