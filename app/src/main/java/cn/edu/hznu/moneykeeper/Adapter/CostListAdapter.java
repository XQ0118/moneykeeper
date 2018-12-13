package cn.edu.hznu.moneykeeper.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.edu.hznu.moneykeeper.CostBean;
import cn.edu.hznu.moneykeeper.R;

import static org.litepal.LitePalApplication.getContext;

public class CostListAdapter extends BaseAdapter{

    private List<CostBean> mList;
    private Context mContext;
    private LayoutInflater mLayoutInflater;

    public CostListAdapter(Context context, List<CostBean> list){
        this.mContext = context;
        this.mList = list;
        this.mLayoutInflater = LayoutInflater.from(context);
    }


    @Override
    public int getCount() {
        return this.mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        if(convertView == null){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, null);
            //convertView = mLayoutInflater.inflate(R.layout.list_item, null);
            viewHolder.mTvCostTitle = (TextView) convertView.findViewById(R.id.tv_title);
            viewHolder.mTvCostData = (TextView) convertView.findViewById(R.id.tv_date);
            viewHolder.mTvCostMoney = (TextView) convertView.findViewById(R.id.tv_cost);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        CostBean bean = mList.get(position);
        viewHolder.mTvCostTitle.setText(bean.costTitle);
        viewHolder.mTvCostData.setText(bean.costDate);
        viewHolder.mTvCostMoney.setText(bean.costMoney);
        return convertView;
    }

    private static class ViewHolder {
        public TextView mTvCostTitle;
        public TextView mTvCostData;
        public TextView mTvCostMoney;

    }
}

