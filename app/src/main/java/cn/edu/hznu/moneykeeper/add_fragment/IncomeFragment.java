package cn.edu.hznu.moneykeeper.add_fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.edu.hznu.moneykeeper.Adapter.IconAdapter;
import cn.edu.hznu.moneykeeper.R;

public class IncomeFragment extends Fragment {

    private int mCurrentPosition;
    public static int income_chosenPos;
    private View rootView;

    // ViewPager icon
    private List<Integer> icon;
    private List<String> titles;
    private GridView gridViewicon_IN;
    private BaseAdapter iconAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        if (null == rootView) {
            rootView = inflater.inflate(R.layout.fragment_income, container,
                    false);
            initView(rootView);
        }
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (null != parent) {
            parent.removeView(rootView);
        }
        return rootView;
    }

    private void initView(View rootView) {
        /* 初始化icon */
        initIcon(rootView);
    }

    /*
     * 初始化icon
     */
    private void initIcon(View rootView2) {
        gridViewicon_IN = (GridView) rootView.findViewById(R.id.gridview);
        // icon
        icon = new ArrayList<Integer>();
        icon.add(R.mipmap.type_pluralism);
        icon.add(R.mipmap.type_handling_fee);
        icon.add(R.mipmap.type_unexpected_income);
        icon.add(R.mipmap.type_salary);

        //title
        titles = new ArrayList<>();
        titles.add("薪资");
        titles.add("礼金");
        titles.add("其他");
        titles.add("兼职");
        // 适配器
        iconAdapter = new IconAdapter(getActivity(), icon, titles);
        // 添加控件适配器
        gridViewicon_IN.setAdapter(iconAdapter);
        // 添加GridView的监听事件
        gridViewicon_IN.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Toast.makeText(getActivity(), "你选择了" + (position + 1) + " 号图片", Toast.LENGTH_SHORT).show();
                IncomeFragment.income_chosenPos = position+100;
                TextView title_edit = (TextView) getActivity().findViewById(R.id.et_cost_title);
                TextView textView = (TextView) view.findViewById(R.id.gridview_expend_item_tv);
                String message = textView.getText().toString();
                title_edit.setText(message);

//                mCurrentPosition = (Integer) view.getTag();

            }
        });
    }

    private int getmCurrentPosition(){
        return mCurrentPosition;
    }

}