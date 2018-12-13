package cn.edu.hznu.moneykeeper;

import android.app.DatePickerDialog;
import android.app.FragmentTransaction;
import android.inputmethodservice.KeyboardView;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.edu.hznu.moneykeeper.Adapter.NotePagerAdapter;
import cn.edu.hznu.moneykeeper.Util.DateUtils;
import cn.edu.hznu.moneykeeper.Util.GetNowTime;
import cn.edu.hznu.moneykeeper.Util.KeyBoardUtil;
import cn.edu.hznu.moneykeeper.add_fragment.ExpendFragment;
import cn.edu.hznu.moneykeeper.add_fragment.IncomeFragment;

import static cn.edu.hznu.moneykeeper.Util.DateUtils.FORMAT_M;
import static cn.edu.hznu.moneykeeper.Util.DateUtils.FORMAT_Y;

public class AddcostActivity extends AppCompatActivity {
    /*keyboard*/
//    private EditText mInput;
    private KeyboardView mKeyboard;
    private String setText = "";

    /*keyboard*/

    /*ListView*/
    private List<CostBean> mCostBeanList;
    /*ListView*/

    private EditText title, input_money;
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

        title = (EditText) findViewById(R.id.et_cost_title);
        input_money = (EditText) findViewById(R.id.et_cost_money);
        datePicker = (DatePicker) findViewById(R.id.db_cost_date);
        dateTv = (TextView) findViewById(R.id.btnDatePickerDialog);
        btn_adddata = (Button) findViewById(R.id.btn_add_cost);

        //初始化界面组件
        init_date();
        setupWidgets();
        

        initKeyBoardView();
        initKeyBoardEvent();
        initTime();

        dateTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimeSelector();
            }
        });

        btn_adddata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //将数据存到数据库中
                CostBean costBean = new CostBean();
                costBean.setCostTitle(title.getText().toString());
                costBean.setCostMoney(input_money.getText().toString());
//                costBean.setCostDate(Integer.parseInt(DateUtils.getCurYear(FORMAT_Y)) + "-" + (datePicker.getMonth()+1)
//                        + "-" + datePicker.getDayOfMonth());
                costBean.setCostDate(days);
                costBean.setCostDateinfo(GetNowTime.getInfoTime());
                costBean.save();
                finish();
            }
        });
    }

    private void init_date(){
        transaction = getSupportFragmentManager().beginTransaction();
        if (null == mExpendFragment) {
            mExpendFragment = new ExpendFragment();
        }
        transaction.add(R.id.fragment_container, mExpendFragment);
        // Commit the transaction
        transaction.commit();
    }

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

    /*keyboard*/
    private void initKeyBoardView() {
        mKeyboard = findViewById(R.id.ky_keyboard);
        input_money = findViewById(R.id.et_cost_money);
        input_money.setText(setText);//设置EditText控件的内容
        input_money.setSelection(setText.length());//将光标移至文字末尾
    }

    private void initKeyBoardEvent(){
        KeyBoardUtil keyBoardUtil = new KeyBoardUtil(mKeyboard, input_money).showKeyboard();

    }
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


}
