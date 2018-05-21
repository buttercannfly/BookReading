package com.example.boyzhang.bookreading.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.boyzhang.bookreading.R;
import com.example.boyzhang.bookreading.overlay.BookInfo;
import com.example.boyzhang.bookreading.overlay.Chapter;
import com.example.boyzhang.bookreading.overlay.Realm_book;

import java.util.List;

/**
 * Created by boyzhang on 2018/5/17.
 */

public class BookShelfAdapter extends BaseAdapter {
    List<BookInfo> bookList;
    LayoutInflater layoutInflater;
    View view;

    public BookShelfAdapter(Context context, List<BookInfo> list){
        bookList = list;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount(){
        return bookList== null ? 0 : bookList.size();
    }

    @Override
    public BookInfo getItem(int position){
        return bookList.get(position);
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public View getView(int position, View contentView, ViewGroup parent){
        BookInfo bookInfo = bookList.get(position);
        //if(view == null){
        View  view = layoutInflater.inflate(R.layout.bookshelf_item, null);
       // }
        ImageView imageView = (ImageView)view.findViewById(R.id.bookcover);
        //以后再加入
        imageView.setImageResource(R.drawable.icon_more_nor);
        TextView textView = (TextView) view.findViewById(R.id.bookname);
        textView.setText(bookInfo.getBookName());
        TextView textView1 = (TextView) view.findViewById(R.id.newChapter);
        textView1.setText(bookInfo.getNewChapter());
        TextView textView2 = (TextView) view.findViewById(R.id.process);
        textView2.setText(bookInfo.getNowChapterIndex() + "/" + bookInfo.getAllChapter());
        return view;
    }
}
