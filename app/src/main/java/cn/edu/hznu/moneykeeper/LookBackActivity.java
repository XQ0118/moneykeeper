package cn.edu.hznu.moneykeeper;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Calendar;

import cn.edu.hznu.moneykeeper.Util.DatePickerDialog;
import cn.edu.hznu.moneykeeper.Util.DateUtils;

import static cn.edu.hznu.moneykeeper.Util.DateUtils.FORMAT_Y;

public class LookBackActivity extends AppCompatActivity {

    //选择时间
    protected int mYear;
    protected int mMonth;
    protected int mDay;
    protected String days;
    private TextView dateTv;  //时间选择

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_look_back);

        dateTv = (TextView) findViewById(R.id.date_year);
        initTime();
        LinearLayout choose_year = (LinearLayout) findViewById(R.id.btn_choose_year);
        choose_year.setOnClickListener(new View.OnClickListener() {

            Calendar c = Calendar.getInstance();
            @Override
            public void onClick(View v) {
                new DatePickerDialog(LookBackActivity.this, 0, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker startDatePicker, int startYear, int startMonthOfYear,
                                          int startDayOfMonth) {
                        String textString = String.format("%d", startYear);
                        dateTv.setText(textString);
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




}
