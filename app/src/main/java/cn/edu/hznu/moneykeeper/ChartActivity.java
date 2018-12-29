package cn.edu.hznu.moneykeeper;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import cn.edu.hznu.moneykeeper.chart_fragment.BeforeLastFragment;
import cn.edu.hznu.moneykeeper.chart_fragment.LastYearFragment;
import cn.edu.hznu.moneykeeper.chart_fragment.ThisYearFragment;


public class ChartActivity extends AppCompatActivity {

    //用于滑动切换的fragment
    private final String TAG = "SpeedDialActivity";

    private RadioGroup mRadioGroup;
    private BeforeLastFragment mBeforeLastFragment;
    private LastYearFragment mLastYearFragment;
    private ThisYearFragment mThisYearFragment;
    private android.support.v4.app.FragmentTransaction transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
        Toolbar toolbar = (Toolbar) findViewById(R.id.chart_toolbar);
        setSupportActionBar(toolbar);
        //Toolbar的事件---返回
        LinearLayout home = (LinearLayout) findViewById(R.id.back_to_home);
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

        //初始化fragment数据;今年
        initThisYearFragment();
        //设置RadioGroup选中事件
        setupWidgets();


    }

    //fragment初始化数据
    private void initBeforeLastFragment(){

        transaction = getSupportFragmentManager().beginTransaction();
        if (null == mBeforeLastFragment) {
            mBeforeLastFragment = new BeforeLastFragment();
        }
        transaction.add(R.id.chart_fragment_container, mBeforeLastFragment);
        // Commit the transaction
        transaction.commit();
        RadioButton button_left = findViewById(R.id.chart_rbLeft);


    }
    //fragment初始化数据
    private void initLastYearFragment(){

        transaction = getSupportFragmentManager().beginTransaction();
        if (null == mLastYearFragment) {
            mLastYearFragment = new LastYearFragment();
        }
        transaction.add(R.id.chart_fragment_container, mLastYearFragment);
        // Commit the transaction
        transaction.commit();
        RadioButton button_center = findViewById(R.id.chart_rbCenter);


    }
    //fragment初始化数据
    private void initThisYearFragment(){

        transaction = getSupportFragmentManager().beginTransaction();
        if (null == mThisYearFragment) {
            mThisYearFragment = new ThisYearFragment();
        }
        transaction.add(R.id.chart_fragment_container, mThisYearFragment);
        // Commit the transaction
        transaction.commit();
        RadioButton button_right = findViewById(R.id.chart_rbRight);
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
                    case R.id.chart_rbLeft:
                        initBeforeLastFragment(); //前年

                        Log.v(TAG, "setupWidgets():radio0 clicked");
                        if (null == mBeforeLastFragment) {
                            mBeforeLastFragment = new BeforeLastFragment();
                        }
                        transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.chart_fragment_container, mBeforeLastFragment);
                        // Commit the transaction
                        transaction.commit();

                        break;
                    case R.id.chart_rbCenter:
                        initLastYearFragment();  //去年
                        Log.v(TAG, "setupWidgets():radio2 clicked");
                        if (null == mLastYearFragment) {
                            mLastYearFragment = new LastYearFragment();
                        }
                        transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.chart_fragment_container, mLastYearFragment);
                        // Commit the transaction
                        transaction.commit();
                        break;
                    case R.id.chart_rbRight:
                        initThisYearFragment();  //今年
                        Log.v(TAG, "setupWidgets():radio3 clicked");
                        if (null == mThisYearFragment) {
                            mThisYearFragment = new ThisYearFragment();
                        }
                        transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.chart_fragment_container, mThisYearFragment);
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


}
