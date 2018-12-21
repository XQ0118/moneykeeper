package cn.edu.hznu.moneykeeper;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.litepal.LitePal;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import cn.edu.hznu.moneykeeper.Adapter.IconAdapter;
import cn.edu.hznu.moneykeeper.Util.DateUtils;
import cn.edu.hznu.moneykeeper.Util.GetNowTime;
import cn.edu.hznu.moneykeeper.add_fragment.ExpendFragment;
import cn.edu.hznu.moneykeeper.add_fragment.IncomeFragment;

import static cn.edu.hznu.moneykeeper.Util.DateUtils.FORMAT_M;
import static cn.edu.hznu.moneykeeper.Util.DateUtils.FORMAT_Y;
import static cn.edu.hznu.moneykeeper.Util.InsertImage.insertIconTitle;

public class EditAddActivity extends AppCompatActivity implements View.OnClickListener{

    /*keyboard*/
    //计算器
    protected boolean isDot;
    protected String num = "0";               //整数部分
    protected String dotNum = ".00";          //小数部分
    protected final int MAX_NUM = 99999;    //最大整数
    protected final int DOT_NUM = 2;          //小数部分最大位数
    protected int count = 0;

    private TextView moneyTv;
    /*keyboard*/

    /*ListView*/
    private List<CostBean> mCostBeanList;
    /*ListView*/

    //数据库交互
    private EditText cost_note;
    private TextView cost_title;
    private ImageView cost_icon;


    //选择时间
    protected int mYear;
    protected int mMonth;
    protected int mDay;
    protected String days;
    private TextView dateTv;  //时间选择

    //用于滑动切换的fragment
    private final String TAG = "SpeedDialActivity";

    private RadioGroup mRadioGroup;
    private ExpendFragment mExpendFragment;
    private IncomeFragment mIncomeFragment;

    private android.support.v4.app.FragmentTransaction transaction;
    private boolean flag = false;

    //设置默认选择第一个分类
    private List<Integer> icon;
    private List<String> titles;
    //记录上一次点击后的分类
    private GridView gridViewtitle;
    public int mCurrentPosition  = 0;
    private IconAdapter iconAdapter;
    public String last_title, last_date, last_money, last_note, last_dateinfo, last_colortype;
    public int last_type;

    protected void initTime(){
        //设置日期选择器初始日期
        mYear = Integer.parseInt(DateUtils.getCurYear(FORMAT_Y));
        mMonth = Integer.parseInt(DateUtils.getCurMonth(FORMAT_M));
        //设置当前 日期
        days = DateUtils.getCurDateStr("yyyy-MM-dd");
        dateTv.setText(days);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //控制弹出的软键盘不上布局上移
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_edit_add);
        //数据库数据列表
        mCostBeanList = new ArrayList<>();

        //主要字符串数据的声明
        cost_title = (TextView) findViewById(R.id.et_cost_title);
        moneyTv = (TextView) findViewById(R.id.et_cost_money);
        cost_note = (EditText) findViewById(R.id.et_cost_note);
        dateTv = (TextView) findViewById(R.id.btnDatePickerDialog);
        moneyTv = findViewById(R.id.et_cost_money);

        /*keyboard*/
        ImageView tb_clear = findViewById(R.id.tb_note_clear);
        tb_clear.setOnClickListener(this);
        TextView tb_done = findViewById(R.id.tb_calc_num_done);
        tb_done.setOnClickListener(this);
        RelativeLayout tb_del = findViewById(R.id.tb_calc_num_del);
        tb_del.setOnClickListener(this);
        TextView tb_dot = findViewById(R.id.tb_calc_num_dot);
        tb_dot.setOnClickListener(this);
        TextView tb_0 = findViewById(R.id.tb_calc_num_0);
        tb_0.setOnClickListener(this);
        TextView tb_1 = findViewById(R.id.tb_calc_num_1);
        tb_1.setOnClickListener(this);
        TextView tb_2 = findViewById(R.id.tb_calc_num_2);
        tb_2.setOnClickListener(this);
        TextView tb_3 = findViewById(R.id.tb_calc_num_3);
        tb_3.setOnClickListener(this);
        TextView tb_4 = findViewById(R.id.tb_calc_num_4);
        tb_4.setOnClickListener(this);
        TextView tb_5 = findViewById(R.id.tb_calc_num_5);
        tb_5.setOnClickListener(this);
        TextView tb_6 = findViewById(R.id.tb_calc_num_6);
        tb_6.setOnClickListener(this);
        TextView tb_7 = findViewById(R.id.tb_calc_num_7);
        tb_7.setOnClickListener(this);
        TextView tb_8 = findViewById(R.id.tb_calc_num_8);
        tb_8.setOnClickListener(this);
        TextView tb_9 = findViewById(R.id.tb_calc_num_9);
        tb_9.setOnClickListener(this);
        /*keyboard*/

        //初始化界面组件
        setTitleStatus();

        //1是支出，0是收入
        if(last_type == 1){
            //初始化fragment数据;GridView页面
            init_date();
            //设置RadioGroup选中事件
            setupWidgets();
        }else {
            init_date2();
            setupWidgets();
        }
        //初始化当前日期
        initTime();
        //点击事件TextView打开事件选择器
        dateTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimeSelector();
            }
        });
    }

    //支出fragment初始化数据
    private void init_date(){
        flag = false;
        transaction = getSupportFragmentManager().beginTransaction();
        if (null == mExpendFragment) {
            mExpendFragment = new ExpendFragment();
        }
        transaction.add(R.id.fragment_container, mExpendFragment);
        // Commit the transaction
        transaction.commit();
        RadioButton button_right = findViewById(R.id.rbLeft);
        button_right.setChecked(true);

    }
    //收入fragment初始化数据
    private void init_date2(){
        flag = true;
        transaction = getSupportFragmentManager().beginTransaction();
        if (null == mIncomeFragment) {
            mIncomeFragment = new IncomeFragment();
        }
        transaction.add(R.id.fragment_container, mIncomeFragment);
        // Commit the transaction
        transaction.commit();
        RadioButton button_right = findViewById(R.id.rbRight);
        button_right.setChecked(true);

    }

    //设置RadioGroup选中事件
    private void setupWidgets() {

        mRadioGroup = (RadioGroup) findViewById(R.id.radiogroup);
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub

                switch (checkedId) {
                    case R.id.rbLeft:
                        init_date(); //支出
                        Log.v(TAG, "setupWidgets():radio0 clicked");
                        if (null == mExpendFragment) {
                            mExpendFragment = new ExpendFragment();
                        }
                        transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.fragment_container, mExpendFragment);
                        // Commit the transaction
                        transaction.commit();
                        break;

                    case R.id.rbRight:
                        init_date2();  //收入
                        Log.v(TAG, "setupWidgets():radio2 clicked");

                        if (null == mIncomeFragment) {
                            mIncomeFragment = new IncomeFragment();
                        }
                        transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.fragment_container, mIncomeFragment);
                        // Commit the transaction
                        transaction.commit();
                        break;

                    default:
                        break;
                }
            }
        });
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        // dataEncapsulation.closeDataBase_speedDial();
    }

    /**
     * 设置状态
     */
    protected void setTitleStatus() {

        //设置编辑选择的分类,时间，备注，金额
        TextView title_edit = (TextView) findViewById(R.id.et_cost_title);
        TextView date_edit = (TextView) findViewById(R.id.btnDatePickerDialog);
        EditText note_edit = (EditText) findViewById(R.id.et_cost_note);
        TextView money_edit = (TextView) findViewById(R.id.et_cost_money);
        Intent intent = getIntent();
        last_title = intent.getStringExtra("title_edit");
        last_date = intent.getStringExtra("date_edit");
        last_note = intent.getStringExtra("note_edit");
        last_money = intent.getStringExtra("money_edit");
        last_dateinfo = intent.getStringExtra("dateinfo_edit");
        last_colortype = intent.getStringExtra("colortype_edit");
        last_type = Integer.parseInt(last_colortype);
        title_edit.setText(last_title);
        date_edit.setText(last_date);
        note_edit.setText(last_note);
        money_edit.setText(last_money);
    }


    /**
     * 显示日期选择器
     */
    public void showTimeSelector() {
        new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                mYear = i;
                mMonth = i1;
                mDay = i2;
                if (mMonth + 1 < 10) {
                    if (mDay < 10) {
                        days = new StringBuffer().append(mYear).append("-").append("0").
                                append(mMonth + 1).append("-").append("0").append(mDay).toString();
                    } else {
                        days = new StringBuffer().append(mYear).append("-").append("0").
                                append(mMonth + 1).append("-").append(mDay).toString();
                    }

                } else {
                    if (mDay < 10) {
                        days = new StringBuffer().append(mYear).append("-").
                                append(mMonth + 1).append("-").append("0").append(mDay).toString();
                    } else {
                        days = new StringBuffer().append(mYear).append("-").
                                append(mMonth + 1).append("-").append(mDay).toString();
                    }

                }
                dateTv.setText(days);

            }
        }, mYear, mMonth, mDay).show();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tb_calc_num_done://确定
                doCommit();
                break;
            case R.id.tb_calc_num_1:
                calcMoney(1);
                break;
            case R.id.tb_calc_num_2:
                calcMoney(2);
                break;
            case R.id.tb_calc_num_3:
                calcMoney(3);
                break;
            case R.id.tb_calc_num_4:
                calcMoney(4);
                break;
            case R.id.tb_calc_num_5:
                calcMoney(5);
                break;
            case R.id.tb_calc_num_6:
                calcMoney(6);
                break;
            case R.id.tb_calc_num_7:
                calcMoney(7);
                break;
            case R.id.tb_calc_num_8:
                calcMoney(8);
                break;
            case R.id.tb_calc_num_9:
                calcMoney(9);
                break;
            case R.id.tb_calc_num_0:
                calcMoney(0);
                break;
            case R.id.tb_calc_num_dot:
                if (dotNum.equals(".00")) {
                    isDot = true;
                    dotNum = ".";
                }
                moneyTv.setText(num + dotNum);
                break;
            case R.id.tb_note_clear://清空
                doClear();
                break;
            case R.id.tb_calc_num_del://删除
                doDelete();
                break;
        }
    }

    public void doCommit() {
        if ((num+dotNum).equals("0.00")) {
            Toast.makeText(this, "唔姆，你还没修改金额", Toast.LENGTH_SHORT).show();
            return;
        }else {
            updateOldItem();
            saveDataToLite();

        }
    }

    public void saveDataToLite(){
        //将数据存到数据库中
        CostBean costBean = new CostBean();
        costBean.setCostTitle(cost_title.getText().toString());
        costBean.setCostNote(cost_note.getText().toString());
        costBean.setCostDate(days);
        //默认false是支出，true是收入; 1是支出的红色，0是收入的绿色
        if(flag == true){
            costBean.setCostImg(IncomeFragment.income_chosenPos);
            costBean.setColorType(0);
            costBean.setCostMoney(moneyTv.getText().toString());
        }
        else {
            costBean.setCostImg(ExpendFragment.expend_chosenPos);
            costBean.setColorType(1);
            costBean.setCostMoney(moneyTv.getText().toString());
        }
        costBean.setCostDateinfo(GetNowTime.getInfoTime());
        costBean.save();
        finish();
    }

    public void updateOldItem(){

        //删除选中的数据
//      List<CostBean> dateinfo = LitePal.where("costDateinfo = ?",last_dateinfo)
//                .find(CostBean.class);
        LitePal.deleteAll(CostBean.class,"costDateinfo = ?", last_dateinfo);
        Toast.makeText(EditAddActivity.this,"更新成功",Toast.LENGTH_SHORT).show();
    }

    /**
     * 清空金额
     */
    public void doClear() {
        num = "0";
        count = 0;
        dotNum = ".00";
        isDot = false;
        moneyTv.setText("0.00");
    }

    /**
     * 删除上次输入
     */
    public void doDelete() {
        if (isDot) {
            if (count > 0) {
                dotNum = dotNum.substring(0, dotNum.length() - 1);
                count--;
            }
            if (count == 0) {
                isDot = false;
                dotNum = ".00";
            }
            moneyTv.setText(num + dotNum);
        } else {
            if (num.length() > 0)
                num = num.substring(0, num.length() - 1);
            if (num.length() == 0)
                num = "0";
            moneyTv.setText(num + dotNum);
        }
    }

    /**
     * 计算金额
     *
     * @param money
     */
    protected void calcMoney(int money) {
        if (num.equals("0") && money == 0)
            return;
        if (isDot) {
            if (count < DOT_NUM) {
                count++;
                dotNum += money;
                moneyTv.setText(num + dotNum);
            }
        } else if (Integer.parseInt(num) < MAX_NUM) {
            if (num.equals("0"))
                num = "";
            num += money;
            moneyTv.setText(""+num + dotNum);
        }
    }

}
