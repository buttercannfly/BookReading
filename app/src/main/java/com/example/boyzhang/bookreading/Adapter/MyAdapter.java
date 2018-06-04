package com.example.zhangweikang.book_search;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by ZHANGWEIKANG on 2018/5/19.
 */

public class MyAdapter extends BaseAdapter {

    private List<ForBean> mBean;
    private LayoutInflater inflater;
    public MyAdapter(){}

    public MyAdapter(List<ForBean> mBean, Context context){
        this.mBean = mBean;
        this.inflater = LayoutInflater.from(context);

    }

    @Override
    public int getCount(){
        return mBean == null?0:mBean.size();
    }

    @Override
    public ForBean getItem(int position){
        return mBean.get(position);
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View view =inflater.inflate(R.layout.item_home,null);
        ForBean mbean = getItem(position);
        TextView tv_novlename = (TextView) view.findViewById(R.id.novel_name);
        TextView tv_latest=(TextView)view.findViewById(R.id.latest_name);
        TextView tv_author = (TextView) view.findViewById(R.id.author_name);
        TextView tv_time = (TextView) view.findViewById(R.id.span_time);
        tv_novlename.setText(mbean.getNovelname());
        tv_latest.setText(mbean.getLatestname());
        tv_author.setText(mbean.getAuthorName());
        tv_time.setText(mbean.getTime());
        return view;
    }
}
