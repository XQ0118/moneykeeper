package cn.edu.hznu.moneykeeper;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.edu.hznu.moneykeeper.Util.DateUtils;
import cn.edu.hznu.moneykeeper.Util.GetNowTime;
import cn.edu.hznu.moneykeeper.Util.KeyBoardUtil;

import static cn.edu.hznu.moneykeeper.Util.DateUtils.FORMAT_M;
import static cn.edu.hznu.moneykeeper.Util.DateUtils.FORMAT_Y;
import static cn.edu.hznu.moneykeeper.Util.DateUtils.FORMAT_YMD;

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

        initView();
        initEvent();
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


    /*keyboard*/
    private void initView() {
        mKeyboard = findViewById(R.id.ky_keyboard);
        input_money = findViewById(R.id.et_cost_money);
        input_money.setText(setText);//设置EditText控件的内容
        input_money.setSelection(setText.length());//将光标移至文字末尾
    }

    private void initEvent(){
        KeyBoardUtil keyBoardUtil = new KeyBoardUtil(mKeyboard, input_money).showKeyboard();

    }
    /*keyboard*/

    public void finishAddAndToMainActivity(){
        Intent myIntent = new Intent();
        myIntent = new Intent(AddcostActivity.this, MainActivity.class);
        startActivity(myIntent);
        this.finish();
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


}
