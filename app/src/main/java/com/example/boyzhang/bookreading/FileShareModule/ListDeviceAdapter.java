package com.example.boyzhang.bookreading.FileShareModule;

import android.content.Context;
import android.net.wifi.p2p.WifiP2pDevice;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.boyzhang.bookreading.R;


import java.util.List;

/**
 * Created by LunHui on 2018/4/28.
 */

public class ListDeviceAdapter extends BaseAdapter {

    private Context mContext;
    private int layoutResource;
    private List<WifiP2pDevice> datas;

    public ListDeviceAdapter(Context c, int resource, List<WifiP2pDevice> data){
        mContext=c;
        layoutResource=resource;
        datas=data;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int i) {
        return datas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        TextView deviceItem;

        if(view == null){
            view= LayoutInflater.from(mContext).inflate(layoutResource,null);
            deviceItem=(TextView)view.findViewById(R.id.textDeviceItem);
            view.setTag(R.id.textDeviceItem,deviceItem);
        }else{
            deviceItem=(TextView) view.getTag(R.id.textDeviceItem);
        }

        deviceItem.setText(datas.get(i).deviceName);

        return view;
    }
}
