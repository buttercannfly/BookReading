package com.example.boyzhang.bookreading.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.boyzhang.bookreading.R;
import com.example.boyzhang.bookreading.overlay.Chapter;
import com.example.boyzhang.bookreading.overlay.Genre;

import java.util.List;

/**
 * Created by boyzhang on 2018/5/21.
 */

public class GenreAdapter extends BaseAdapter {
    List<Genre> genreList;
    private LayoutInflater layoutInflater;

    public GenreAdapter(List<Genre> list, Context context){
        genreList = list;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount(){
        return genreList == null ? 0 : genreList.size();
    }

    @Override
    public Genre getItem(int position){
        return genreList.get(position);
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public View getView(int position, View contentView, ViewGroup parent){
        Genre chapter = getItem(position);
        //if(view == null){
        View view = layoutInflater.inflate(R.layout.borderitem, null);
        //}
        TextView textView = (TextView) view.findViewById(R.id.border_item);
        textView.setText(chapter.name);
        return view;
    }
}
