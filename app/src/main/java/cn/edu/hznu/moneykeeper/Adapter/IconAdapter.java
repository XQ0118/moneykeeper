package cn.edu.hznu.moneykeeper.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.edu.hznu.moneykeeper.R;

public class IconAdapter extends BaseAdapter {

    //定义Context
    private Context mContext;

    //定义整型数组Icon
    private List<Integer> data;

    //图标下的文字
    private List<String> title;


    public IconAdapter(Context context, List<Integer> data, List<String> title) {
        super();
        this.mContext = context;
        this.data = data;
        this.title = title;
    }


    @Override
    public int getCount() {
        return data.size();
    }

    public int getTitleCount() {
        return title.size();
    }
    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    public Object getTitleItem(int position) {
        return title.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    public long getTitleItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.gridview_expend_item, null);
            holder = new ViewHolder();

            holder.iv = (ImageView) convertView.findViewById(R.id.gridview_expend_item_iv);
            holder.tv = (TextView) convertView.findViewById(R.id.gridview_expend_item_tv);


            convertView.setTag(holder);// 如果convertView为空就 把holder赋值进去
        } else {
            holder = (ViewHolder) convertView.getTag();// 如果convertView不为空，那么就在convertView中getTag()拿出来
        }
        //icon
        Integer current = data.get(position);
        holder.iv.setImageResource(current);
        //title
        String current_title = title.get(position);
        holder.tv.setText(current_title);

        return convertView;
    }

    static class ViewHolder {
        ImageView iv;
        TextView tv;
    }

}
