package cn.edu.hznu.moneykeeper.Adapter;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class HeadPagerAdapter extends PagerAdapter {

    private List<View> mViews;

    public HeadPagerAdapter(List<View> mViews){
        this.mViews = mViews;
    }

    @Override
    public int getCount() {
        return mViews.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    //添加页面方法
    @Override
    public Object instantiateItem(ViewGroup container, int position){
        //添加view
        container.addView(mViews.get(position));
        return mViews.get(position);
    }

    //删除页面
    @Override
    public void destroyItem(ViewGroup container, int position, Object object){
        //删除View
        container.removeView(mViews.get(position));
    }
}
