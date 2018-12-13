package cn.edu.hznu.moneykeeper.add_fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;


import java.util.LinkedList;

import cn.edu.hznu.moneykeeper.Adapter.AvatarAdapter;
import cn.edu.hznu.moneykeeper.Avatar;
import cn.edu.hznu.moneykeeper.R;


public class PageFragment extends Fragment {
    public static final String ARG_PAGE = "ARG_PAGE";
    private int mPage;//此变量纯粹用来做Fragment内容的展示编号，后期会删除

    private LinkedList<Avatar> Avatarlist = null;
    //    private Context mContext;
    private AvatarAdapter avatarAdapter;
    private GridView item_pick;

    public static PageFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        PageFragment pageFragment = new PageFragment();
        pageFragment.setArguments(args);
        return pageFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE)
        ;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_page, container, false);
        /*TextView textView = (TextView) view;
        textView.setText("Fragment #" + mPage);*/
        item_pick = (GridView) view.findViewById(R.id.grid_avatar);//
        Avatarlist = new LinkedList<>();
        //下面这5个add方法后续需要调整
        Avatarlist.add(new Avatar(R.mipmap.ic_launcher, "亚巴顿"));
        Avatarlist.add(new Avatar(R.mipmap.ic_launcher, "炼金术师"));
        Avatarlist.add(new Avatar(R.mipmap.ic_launcher, "远古冰魂"));
        Avatarlist.add(new Avatar(R.mipmap.ic_launcher, "敌法师"));
        Avatarlist.add(new Avatar(R.mipmap.ic_launcher, "天穹守望者"));
        avatarAdapter = new AvatarAdapter(Avatarlist,getContext());
        item_pick.setAdapter(avatarAdapter);
        return view;
    }
}
