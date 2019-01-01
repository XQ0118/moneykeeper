package cn.edu.hznu.moneykeeper;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.support.v7.widget.Toolbar;
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

public class MainActivity extends AppCompatActivity {

    private Context mContext;
    private ViewPager mViewPager;
    private List<View> mViews;
    private LayoutInflater mInflater;
    private HeadPagerAdapter mPagerAdapter;
    private List<ImageView> mDots;//定义一个集合存储2个dot
    private int oldPosition;//记录当前点的位置。
    private ImageButton btn_chart;
    private ImageButton btn_set;

    //选择时间
    protected int mYear;
    protected int mMonth;
    protected String months;
    private TextView dateTv;  //时间选择
    public double month_cost_total = 0.00;
    public double month_income_total = 0.00;
    public TextView total_money, income_money;
    public TextView tv_delete_title;

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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
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
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.home_toolbar);

        setSupportActionBar(toolbar);
        income_money = findViewById(R.id.income_money_month);
        total_money = findViewById(R.id.expend_money_month);
        //初始化数据库
        LitePal.initialize(MainActivity.this);

        //数据库金额显示listView
        mCostBeanList = new ArrayList<>();

        //初始化底部圆点
        initDots();

        //初始化账单
        initCostData();

        //fab按钮
        fab = findViewById(R.id.btn_Add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(MainActivity.this, "记一笔", Toast.LENGTH_SHORT).show();
                intent = new Intent(MainActivity.this, AddcostActivity.class);
                //intent.putExtra("flag","money");
                startActivity(intent);
            }
        });

        //footer
        mFooter = LayoutInflater.from(MainActivity.this).inflate(R.layout.my_footer, null);//加载footer布局
        costList.addFooterView(mFooter, "",false);
        //chart
        btn_chart = (ImageButton) findViewById(R.id.title_chart);
        btn_chart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent  intent = new Intent(MainActivity.this, ChartActivity.class);
                startActivity(intent);
            }
        });
        btn_set = (ImageButton) findViewById(R.id.title_setting);
        btn_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
                View viewDialog = inflater.inflate(R.layout.dialog_deleteall, null);
                tv_delete_title = (TextView) viewDialog.findViewById(R.id.delete_item_info);

                builder.setView(viewDialog);
                builder.setPositiveButton("删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //删除所有数据
                        LitePal.deleteAll(CostBean.class);
                        mCostBeanList.clear();
                        adapter.notifyDataSetChanged();
                        costList.setAdapter(adapter);
                        //重置当前月的支出
                        month_cost_total = 0.00;
                        month_income_total = 0.00;
                        getPerMonthCost();
                        getPerMonthIncome();
                        initDots();

                    }
                });
                builder.setNegativeButton("取消", null);
                builder.create().show();

            }
        });


    }



    //底部圆点集合的初始化
    @SuppressLint("WrongViewCast")
    private void initDots() {
        //初始化三个dot
        mDots = new ArrayList<ImageView>();
        ImageView dotFirst = (ImageView) findViewById(R.id.dot_first);
        ImageView dotFSecond = (ImageView) findViewById(R.id.dot_second);
        mDots.add(dotFirst);
        mDots.add(dotFSecond);
        if(!total_money.getText().toString().equals("0.00")){
            mDots.get(0).setImageResource(R.mipmap.dot_focused);
        }else {
            mDots.get(0).setImageResource(R.mipmap.dot_normal);
        }
         if(!income_money.getText().toString().equals("0.00")){
            mDots.get(1).setImageResource(R.mipmap.dot_focused);
        }else {
             mDots.get(1).setImageResource(R.mipmap.dot_normal);
         }


    }

    //初始化listview数据
    private void initCostData() {

        mCostBeanList.clear();
        List<CostBean> costBeans = LitePal.findAll(CostBean.class);
        for(CostBean costBean: costBeans) {
            costBean.getCostTitle();
            costBean.getCostMoney();
            costBean.getCostDate();
            //costBean.getCostNote();
            mCostBeanList.add(costBean);
        }

        costList = findViewById(R.id.lv_main);

        adapter = new CostListAdapter(MainActivity.this, mCostBeanList);
        adapter.notifyDataSetChanged();
        costList.setAdapter(adapter);
        LinearLayout mEmptyTv = (LinearLayout) findViewById(R.id.nothing_view);
        costList.setEmptyView(mEmptyTv);
        Collections.reverse(mCostBeanList);

        //长按list_item删除
        costList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
                View viewDialog = inflater.inflate(R.layout.my_dialog, null);
                tv_delete_title = (TextView) viewDialog.findViewById(R.id.delete_item_info);
                CostBean delete_title = mCostBeanList.get(position);
                String delete_title_info = delete_title.costTitle;
                String delete_title_money = delete_title.costMoney;
                String delete_str = String.format("%s ¥%s", delete_title_info, delete_title_money);

                tv_delete_title.setText(delete_str);

                builder.setView(viewDialog);
                builder.setPositiveButton("删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //删除选中的数据
                        CostBean id = mCostBeanList.get(position);
                        LitePal.deleteAll(CostBean.class,"costDateinfo = ?", id.getCostDateinfo());

                        mCostBeanList.remove(position);
                        adapter.notifyDataSetChanged();
                        costList.setAdapter(adapter);
                        //重置当前月的支出
                        month_cost_total = 0.00;
                        month_income_total = 0.00;
                        getPerMonthCost();
                        getPerMonthIncome();
                        initDots();

                    }
                });
                builder.setNegativeButton("取消", null);
                builder.create().show();
                return true;
            }
        });

        //短按list_item进入编辑页面
        costList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CostBean id_edit = mCostBeanList.get(position);
                List<CostBean> costBeanList = LitePal.where("costDateinfo = ?",String.valueOf(id_edit.getCostDateinfo()))
                        .find(CostBean.class);
                String title_edit = null;
                String date_edit = null;
                String note_edit = null;
                String money_edit = null;
                String dateinfo_edit = null;
                int colortype_edit = 0;

                for(CostBean costBean: costBeanList){
                    title_edit = costBean.getCostTitle();
                    date_edit = costBean.getCostDate();
                    note_edit = costBean.getCostNote();
                    money_edit = costBean.getCostMoney();
                    dateinfo_edit = costBean.getCostDateinfo();
                    colortype_edit = costBean.getColorType();
                    Log.d("test",title_edit);
                    Log.d("test",date_edit);
                    Log.d("test",note_edit);
                    Log.d("test",money_edit);
                    Log.d("test",dateinfo_edit);
                }

                Intent intent = new Intent(MainActivity.this, EditAddActivity.class);
                intent.putExtra("title_edit",title_edit);
                intent.putExtra("date_edit",date_edit);
                intent.putExtra("note_edit",note_edit);
                intent.putExtra("money_edit",money_edit);
                intent.putExtra("dateinfo_edit",dateinfo_edit);
                intent.putExtra("colortype_edit", colortype_edit+"");
                startActivity(intent);
            }
        });
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
        initDots();

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
        //设置日期选择器初始日期
        mYear = Integer.parseInt(DateUtils.getCurYear(FORMAT_Y));
        mMonth = Integer.parseInt(DateUtils.getCurMonth(FORMAT_M));
        //设置当前月
        months = DateUtils.getCurDateStr("yyyy-MM");
        //查询出当前月所有的金额数据
        List<CostBean> monthCostList = LitePal.findAll(CostBean.class);
        for(CostBean costBean: monthCostList){
            //状态1表示支出，0表示收入
            if(costBean.colorType == 1 && costBean.getCostDate().substring(0, 7) .equals(months)  ){
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
        //设置日期选择器初始日期
        mYear = Integer.parseInt(DateUtils.getCurYear(FORMAT_Y));
        mMonth = Integer.parseInt(DateUtils.getCurMonth(FORMAT_M));
        //设置当前月
        months = DateUtils.getCurDateStr("yyyy-MM");
        //查询出当前月所有的金额数据
        List<CostBean> monthCostList = LitePal.findAll(CostBean.class);
        for(CostBean costBean: monthCostList){
            //状态1表示支出，0表示收入
            if(costBean.colorType == 0 && costBean.getCostDate().substring(0, 7) .equals(months)  ){
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
