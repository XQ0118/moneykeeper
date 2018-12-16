package cn.edu.hznu.moneykeeper;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
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
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
        import android.support.v7.widget.Toolbar;

        import org.litepal.LitePal;

        import java.util.ArrayList;
        import java.util.List;

        import cn.edu.hznu.moneykeeper.Adapter.CostListAdapter;
import cn.edu.hznu.moneykeeper.Adapter.HeadPagerAdapter;

public class MainActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private List<View> mViews;
    private LayoutInflater mInflater;
    private HeadPagerAdapter mPagerAdapter;
    private List<ImageView> mDots;//定义一个集合存储2个dot
    private int oldPosition;//记录当前点的位置。

    /*CostBeanlist*/
    private List<CostBean> mCostBeanList;
    /*CostBeanlist*/
    /*ListView*/
    private ListView costList;
    /*ListView*/



    private FloatingActionButton fab;
    private Intent intent;
    private CostListAdapter adapter;
    private View statusBarView;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //初始化数据库
        LitePal.initialize(MainActivity.this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.home_toolbar);
        setSupportActionBar(toolbar);

        //设置HeadViewPager
        setmViewPager();
        initDots();
        mPagerAdapter = new HeadPagerAdapter(mViews);
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
                mDots.get(oldPosition).setImageResource(R.mipmap.dot_normal);
                mDots.get(i).setImageResource(R.mipmap.dot_focused);
                oldPosition = i;
            }

            @Override
            public void onPageSelected(int i) { }

            @Override
            public void onPageScrollStateChanged(int i) { }
        });

        //数据库金额显示listView
        mCostBeanList = new ArrayList<>();
        //初始化账单
        initCostData();

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

    //设置viewpager
    private void setmViewPager(){
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mInflater = getLayoutInflater();
        mViews = new ArrayList<View>();
        View view_left = mInflater.inflate(R.layout.headpager_left, null);
        View view_right = mInflater.inflate(R.layout.headpager_right, null);
        mViews.add(view_left);
        mViews.add(view_right);
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
        oldPosition = 0;
        mDots.get(oldPosition).setImageResource(R.mipmap.dot_focused);
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

        //长按list_item删除
        costList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
                View viewDialog = inflater.inflate(R.layout.my_dialog, null);


                builder.setView(viewDialog);
                builder.setPositiveButton("删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //删除所有数据
                        //mDatabaseHelper.deleteAllData();
                        //删除选中的数据
                        CostBean id = mCostBeanList.get(position);
                        LitePal.deleteAll(CostBean.class,"costDateinfo = ?", id.getCostDateinfo());

                        mCostBeanList.remove(position);
                        adapter.notifyDataSetChanged();
                        costList.setAdapter(adapter);

                    }
                });
                builder.setNegativeButton("取消", null);
                builder.create().show();
                return false;
            }
        });
    }

    //重置MainActivity页面内容
    @Override
    protected void onResume() {
        super.onResume();
        initCostData();
        //设置状态栏的渐变颜色
        getWindow().getDecorView().addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                initStatusBar();
                getWindow().getDecorView().removeOnLayoutChangeListener(this);
            }
        });
        initStatusBar();
    }



}


