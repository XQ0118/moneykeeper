package cn.edu.hznu.moneykeeper;

import android.app.DatePickerDialog;
import android.inputmethodservice.KeyboardView;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import cn.edu.hznu.moneykeeper.Adapter.IconAdapter;
import cn.edu.hznu.moneykeeper.Util.DateUtils;
import cn.edu.hznu.moneykeeper.Util.GetNowTime;
import cn.edu.hznu.moneykeeper.Util.KeyBoardUtil;
import cn.edu.hznu.moneykeeper.add_fragment.ExpendFragment;
import cn.edu.hznu.moneykeeper.add_fragment.IncomeFragment;

import static cn.edu.hznu.moneykeeper.Util.DateUtils.FORMAT_M;
import static cn.edu.hznu.moneykeeper.Util.DateUtils.FORMAT_Y;

public class AddcostActivity extends AppCompatActivity implements View.OnClickListener  {

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

    private EditText title, note;
    private DatePicker datePicker;
    private Button btn_adddata, btnDate ;

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

    //设置默认选择第一个分类
    private List<Integer> icon;
    private List<String> titles;
    //记录上一次点击后的分类
    private GridView gridViewtitle;
    public String lasttitle;
    private IconAdapter iconAdapter;

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
        setContentView(R.layout.activity_addcost);

        mCostBeanList = new ArrayList<>();
        /*keyboard*/
        moneyTv = findViewById(R.id.et_cost_money);

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

        title = (EditText) findViewById(R.id.et_cost_title);
        moneyTv = (TextView) findViewById(R.id.et_cost_money);
        note = (EditText) findViewById(R.id.et_cost_note);
        datePicker = (DatePicker) findViewById(R.id.db_cost_date);
        dateTv = (TextView) findViewById(R.id.btnDatePickerDialog);

        //初始化界面组件
        init_date();
        setupWidgets();
        setTitleStatus();

        //初始化当前日期
        initTime();

        dateTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimeSelector();
            }
        });

//        btn_adddata.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //将数据存到数据库中
//                CostBean costBean = new CostBean();
//                costBean.setCostTitle(title.getText().toString());
//                costBean.setCostMoney(input_money.getText().toString());
//                costBean.setCostNote(note.getText().toString());
////                costBean.setCostDate(Integer.parseInt(DateUtils.getCurYear(FORMAT_Y)) + "-" + (datePicker.getMonth()+1)
////                        + "-" + datePicker.getDayOfMonth());
//                costBean.setCostDate(days);
//                costBean.setCostDateinfo(GetNowTime.getInfoTime());
//                costBean.save();
//                finish();
//            }
//        });
    }

    //fragment初始化数据
    private void init_date(){
        transaction = getSupportFragmentManager().beginTransaction();
        if (null == mExpendFragment) {
            mExpendFragment = new ExpendFragment();
        }
        transaction.add(R.id.fragment_container, mExpendFragment);
        // Commit the transaction
        transaction.commit();
    }

    //设置fragment
    private void setupWidgets() {

        mRadioGroup = (RadioGroup) findViewById(R.id.radiogroup);
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub

                switch (checkedId) {
                    case R.id.rbLeft:
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

        //设置选择的分类
        EditText title_edit = (EditText) findViewById(R.id.et_cost_title);

        lasttitle = "餐饮";
        title_edit.setText(lasttitle);

    }

    /*keyboard*/

    /*keyboard*/

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
        final SimpleDateFormat sdf = new SimpleDateFormat(" HH:mm:ss");

        if ((num + dotNum).equals("0.00")) {
            Toast.makeText(this, "唔姆，你还没输入金额", Toast.LENGTH_SHORT).show();
            return;
        }else {
            //将数据存到数据库中
            CostBean costBean = new CostBean();
            costBean.setCostTitle(title.getText().toString());
            costBean.setCostMoney(moneyTv.getText().toString());
            costBean.setCostNote(note.getText().toString());
//          costBean.setCostDate(Integer.parseInt(DateUtils.getCurYear(FORMAT_Y)) + "-" + (datePicker.getMonth()+1)
//          + "-" + datePicker.getDayOfMonth());
            costBean.setCostDate(days);
            costBean.setCostDateinfo(GetNowTime.getInfoTime());
            costBean.save();
            finish();

        }

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
