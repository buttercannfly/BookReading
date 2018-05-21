package com.example.boyzhang.bookreading.FileShareModule;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.boyzhang.bookreading.R;


import java.util.List;

/**
 * Created by LunHui on 2018/4/27.
 */

public class ListFileAdapter extends BaseAdapter {
    private Context mContext = null;
    private List<String> paths = null;
    private int resource = 0;

    public ListFileAdapter(Context context, int resource, List<String> paths) {
        this.resource = resource;
        mContext = context;
        this.paths = paths;
    }

    @Override
    public int getCount() {
        return paths.size();
    }

    @Override
    public Object getItem(int i) {
        return paths.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        TextView textView;
        Button btnDel;

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(resource, null);
            textView = (TextView) convertView.findViewById(R.id.textFilePath);
            btnDel=(Button)convertView.findViewById(R.id.btnDelItem);
            Holder holder = new Holder(textView,btnDel);
            convertView.setTag(holder);
        } else {
            Holder holder = (Holder) convertView.getTag();
            textView = holder.textView;
            btnDel=holder.btnDel;
        }

        btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paths.remove(position);
                ListFileAdapter.this.notifyDataSetChanged();
            }
        });

        String path = paths.get(position);
        textView.setText(path);

        return convertView;
    }

    private class Holder {
        TextView textView;
        Button btnDel;

        Holder(TextView t,Button b) {
            textView = t;
            btnDel=b;
        }
    }
}
