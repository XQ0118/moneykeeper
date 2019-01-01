package cn.edu.hznu.moneykeeper.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import cn.edu.hznu.moneykeeper.CostBean;
import cn.edu.hznu.moneykeeper.R;
import cn.edu.hznu.moneykeeper.Util.InsertImage;

import static org.litepal.LitePalApplication.getContext;

public class CostListAdapter extends BaseAdapter{

    private List<CostBean> mList;
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private List<Object> objects;


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
            viewHolder.mTvCostNote = (TextView) convertView.findViewById(R.id.tv_note);
            viewHolder.mIvCostImg = (ImageView) convertView.findViewById(R.id.iv_icon);
            viewHolder.mTvCostMoney = (TextView) convertView.findViewById(R.id.tv_cost);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        CostBean bean = mList.get(position);
        viewHolder.mTvCostTitle.setText(bean.costTitle);
        viewHolder.mTvCostData.setText(bean.costDate);
        viewHolder.mTvCostNote.setText(bean.costNote);
        viewHolder.mIvCostImg.setImageResource(InsertImage.insertIcon(bean.costImg));
        //默认false是支出，true是收入; 1是支出的红色，0是收入的绿色
        if( bean.getColorType() == 1){
            //red
            viewHolder.mTvCostMoney.setTextColor(Color.parseColor("#FF6F6F"));
            NumberFormat nf = new DecimalFormat("#,###.##");
            String str = nf.format(Double.parseDouble(bean.costMoney));
            viewHolder.mTvCostMoney.setText("-"+str);
        }else {
            //green
            viewHolder.mTvCostMoney.setTextColor(Color.parseColor("#02AE7C"));
            NumberFormat nf = new DecimalFormat("#,###.##");
            String str = nf.format(Double.parseDouble(bean.costMoney));
            viewHolder.mTvCostMoney.setText("+"+str);
        }

        return convertView;
    }

    private static class ViewHolder {
        public TextView mTvCostTitle;
        public TextView mTvCostData;
        public TextView mTvCostMoney;
        public TextView mTvCostNote;
        public ImageView mIvCostImg;

    }
}

