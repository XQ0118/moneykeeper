package cn.edu.hznu.moneykeeper;



        import android.app.AlertDialog;
        import android.content.DialogInterface;
        import android.content.Intent;

        import android.os.Build;
        import android.support.annotation.RequiresApi;
        import android.support.design.widget.FloatingActionButton;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.widget.AdapterView;
        import android.widget.ListView;
        import android.support.v7.widget.Toolbar;

        import org.litepal.LitePal;

        import java.util.ArrayList;
        import java.util.List;

        import cn.edu.hznu.moneykeeper.Adapter.CostListAdapter;


public class MainActivity extends AppCompatActivity {

    /*CostBeanlist*/
    private List<CostBean> mCostBeanList;
    /*CostBeanlist*/
    /*ListView*/
    private ListView costList;
    /*ListView*/



    private FloatingActionButton fab;
    private Intent intent;
    private CostListAdapter adapter;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LitePal.initialize(MainActivity.this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.home_toolbar);
        setSupportActionBar(toolbar);
        //setContentView(R.layout.fabmenu);

        mCostBeanList = new ArrayList<>();

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

    private void initCostData() {

        mCostBeanList.clear();
        List<CostBean> costBeans = LitePal.findAll(CostBean.class);
        for(CostBean costBean: costBeans) {
            costBean.getCostTitle();
            costBean.getCostMoney();
            costBean.getCostDate();
            Log.d("TAG",costBean.costTitle);
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
    }



}


