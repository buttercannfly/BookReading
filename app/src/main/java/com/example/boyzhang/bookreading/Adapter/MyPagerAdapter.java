package com.example.boyzhang.bookreading.Adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.boyzhang.bookreading.R;
import com.example.boyzhang.bookreading.view.ViewPage;

import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by boyzhang on 2018/5/25.
 */

public class MyPagerAdapter extends PagerAdapter {
    private List<String> list;
    private LayoutInflater inflater;
    private Context context;

    public MyPagerAdapter(){}
    public MyPagerAdapter(List<String> list, Context context){
        this.list = list;
        //inflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        String s = list.get(position);
        //View view = inflater.inflate(R.layout.view_page_item, null);
        TextView textView = new TextView(context);
        textView.setText(s);
        textView.setGravity(Gravity.CENTER);
        container.addView(textView);
        return textView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View)object);
    }
}
