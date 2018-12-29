package cn.edu.hznu.moneykeeper.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.litepal.LitePal;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import cn.edu.hznu.moneykeeper.CostBean;
import cn.edu.hznu.moneykeeper.R;
import cn.edu.hznu.moneykeeper.Util.DateUtils;
import cn.edu.hznu.moneykeeper.Util.InsertImage;

import static org.litepal.LitePalApplication.getContext;

public class MonthListAdapter extends SimpleAdapter {

    private Context context; /*运行环境*/
    private List<Map<String, Object>> listItem;  /*数据源*/
    private LayoutInflater listContainer; // 视图容器
    private class ViewHolder {
        public TextView mTvCostExpend; //月支出
        public TextView mTvCostIncome; //月收入
        public TextView mTvCostSurplus; //结余
        public TextView mTvMonth; //月份
        public ImageView mIvMonthImg; //month_icon
    }
    /*construction function*/
    public MonthListAdapter(Context context,
                            List<Map<String, Object>> data, int resource,
                            String[] from, int[] to) {
        super(context, data, resource, from, to);
        this.listContainer = LayoutInflater.from(context); // 创建视图容器并设置上下文
        this.context=context;
        listItem=data;
    }
    /**
     * @see android.widget.Adapter#getCount()
     */
    public int getCount() {

        return listItem.size();
    }

    /**
     * @see android.widget.Adapter#getItem(int)
     */
    public Object getItem(int position) {

        return listItem.get(position);
    }

    /**
     * @see android.widget.Adapter#getItemId(int)
     */
    public long getItemId(int position) {

        return position;
    }


    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.monthslist_item, null);
//            viewHolder.mIvMonthImg = (ImageView) convertView.findViewById(R.id.iv_month_icon);
            viewHolder.mTvMonth = (TextView) convertView.findViewById(R.id.tv_month);
            viewHolder.mTvCostExpend = (TextView) convertView.findViewById(R.id.tv_month_expend_money);
            viewHolder.mTvCostIncome = (TextView) convertView.findViewById(R.id.tv_month_income_money);
            viewHolder.mTvCostSurplus= (TextView) convertView.findViewById(R.id.tv_month_surplus_money);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        return convertView;
    }
}

