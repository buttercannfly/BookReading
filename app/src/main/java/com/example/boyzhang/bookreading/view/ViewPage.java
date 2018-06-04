package com.example.boyzhang.bookreading.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.example.boyzhang.bookreading.Adapter.MyPagerAdapter;
import com.example.boyzhang.bookreading.R;

import java.util.ArrayList;
import java.util.List;

public class ViewPage extends AppCompatActivity {
    private String[] strings = new String[]{"第一页!!!!!!!!!!!!!", "第二页!!!!!!!!!!", "第三页!!!!!!!!!"};
    List<String> list;
    ViewPager viewPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_page);
        viewPage =  findViewById(R.id.page);
        InitList();
        MyPagerAdapter adapter = new MyPagerAdapter(list, getApplicationContext());
        viewPage.setAdapter(adapter);
    }

    public void InitList(){
        list = new ArrayList<>();
        for(int i = 0; i < strings.length; i++){
            list.add(strings[i]);
        }
    }
}
