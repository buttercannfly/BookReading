package com.example.boyzhang.bookreading.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.boyzhang.bookreading.R;
import com.example.boyzhang.bookreading.overlay.Chapter;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by boyzhang on 2018/5/16.
 */

public class ChapterListAdapter extends BaseAdapter {
    private List<Chapter> chapterList;
    private LayoutInflater layoutInflater;
    private  View view;
    public ChapterListAdapter(){}

    public ChapterListAdapter(List<Chapter> list, Context context){
        chapterList = list;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount(){
        return chapterList == null ? 0 : chapterList.size();
    }

    @Override
    public Chapter getItem(int position){
        return chapterList.get(position);
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public View getView(int position, View contentView, ViewGroup parent){
        Chapter chapter = getItem(position);
        //if(view == null){
        View view = layoutInflater.inflate(R.layout.popupwindow_chapter_item, null);
        //}
        TextView textView = (TextView) view.findViewById(R.id.item_chaptername);
        textView.setText(chapter.getName());
        return view;
    }

}
