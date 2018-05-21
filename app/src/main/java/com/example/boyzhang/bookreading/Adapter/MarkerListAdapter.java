package com.example.boyzhang.bookreading.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.boyzhang.bookreading.R;
import com.example.boyzhang.bookreading.overlay.Chapter;
import com.example.boyzhang.bookreading.overlay.Marker;

import java.util.List;

/**
 * Created by boyzhang on 2018/5/20.
 */

public class MarkerListAdapter extends BaseAdapter {
    private List<Marker> markerList;
    private LayoutInflater layoutInflater;
    private View view;
    public MarkerListAdapter(){}

    public MarkerListAdapter(List<Marker> list, Context context){
        markerList = list;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount(){
        return markerList == null ? 0 : markerList.size();
    }

    @Override
    public Marker getItem(int position){
        return markerList.get(position);
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public View getView(int position, View contentView, ViewGroup parent){
        Marker marker = getItem(position);
        //if(view == null){
        View view = layoutInflater.inflate(R.layout.popupwindow_marker_item, null);
        //}
        TextView textView = (TextView) view.findViewById(R.id.item_markerchapter);
        textView.setText(marker.getName());
        TextView textView1 = view.findViewById(R.id.item_markercontent);
        textView.setText(marker.getContent());
        return view;
    }
}
