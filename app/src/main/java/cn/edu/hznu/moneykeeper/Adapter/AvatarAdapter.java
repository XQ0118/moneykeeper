package cn.edu.hznu.moneykeeper.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.LinkedList;

import cn.edu.hznu.moneykeeper.Avatar;
import cn.edu.hznu.moneykeeper.R;


/**
 * Created by noisa on 2016/3/15.
 */
public class AvatarAdapter extends BaseAdapter {
    private LinkedList<Avatar> Avatarlist;
    private Context mContext;
    private Avatar avatar;

    public AvatarAdapter(LinkedList<Avatar> Avatarlist, Context mContext) {
        this.Avatarlist = Avatarlist;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return Avatarlist.size();
    }

    @Override
    public Object getItem(int position) {
        return Avatarlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_pick, parent, false);
            holder = new ViewHolder();
            holder.img_avatar = (ImageView) convertView.findViewById(R.id.avatar);
            holder.txt_name = (TextView) convertView.findViewById(R.id.name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.img_avatar.setBackgroundResource(Integer.parseInt(Avatarlist.get(position).getaId()));
        holder.txt_name.setText(Avatarlist.get(position).getaName());
        return convertView;
    }

    static class ViewHolder{
        ImageView img_avatar;
        TextView txt_name;
    }
}
