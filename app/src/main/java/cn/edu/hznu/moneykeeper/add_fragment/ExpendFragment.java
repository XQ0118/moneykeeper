package cn.edu.hznu.moneykeeper.add_fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EdgeEffect;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.edu.hznu.moneykeeper.Adapter.IconAdapter;
import cn.edu.hznu.moneykeeper.AddcostActivity;
import cn.edu.hznu.moneykeeper.R;

public class ExpendFragment extends Fragment {

    private View rootView;

    // ViewPager icon
    private List<Integer> icon;
    private List<String> titles;
    private GridView gridViewicon;
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
            rootView = inflater.inflate(R.layout.fragment_expend, container,
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
        gridViewicon = (GridView) rootView.findViewById(R.id.gridview);
        // icon
        icon = new ArrayList<Integer>();
        icon.add(R.mipmap.type_eat);
        icon.add(R.mipmap.type_calendar);
        icon.add(R.mipmap.type_3c);
        icon.add(R.mipmap.type_candy);
        icon.add(R.mipmap.type_pill);
        icon.add(R.mipmap.type_movie);
        icon.add(R.mipmap.type_wallet);
        icon.add(R.mipmap.type_unexpected_income);
        icon.add(R.mipmap.type_clothes);

        //title
        titles = new ArrayList<>();
        titles.add("餐饮");
        titles.add("日用品");
        titles.add("电子产品");
        titles.add("零食");
        titles.add("医疗");
        titles.add("娱乐");
        titles.add("还款");
        titles.add("其他");
        titles.add("服饰");
        // 适配器
        iconAdapter = new IconAdapter(getActivity(), icon, titles);
        // 添加控件适配器
        gridViewicon.setAdapter(iconAdapter);
        // 添加GridView的监听事件
        gridViewicon.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Toast.makeText(getActivity(), "你选择了" + (position + 1) + " 号图片", Toast.LENGTH_SHORT).show();

                EditText title_edit = (EditText) getActivity().findViewById(R.id.et_cost_title);
                TextView textView = (TextView) view.findViewById(R.id.gridview_expend_item_tv);
                String message = textView.getText().toString();
                title_edit.setText(message);
            }
        });
    }


}
