package com.example.boyzhang.bookreading.overlay;

import android.media.Image;
import android.os.Parcel;
import android.os.Parcelable;

import io.realm.Realm;

/**
 * Created by boyzhang on 2018/5/17.
 */

public class BookInfo implements Parcelable {
    int index;
    String name;
    String chapterName;
    int allchapter;

    public BookInfo(int index, String name, String chapterName, int allchapter){
        this.index = index;
        this.name = name;
        this.chapterName = chapterName;
        this.allchapter = allchapter;
    }
    protected BookInfo(Parcel in){
        index = in.readInt();
        chapterName = in.readString();
        name = in.readString();
    }
    //public void setNowChapter(int name);
    public String getNewChapter(){
        return chapterName;
    }
    public int getAllChapter(){
        return allchapter;
    }
    public int getNowChapterIndex(){
        return index;
    }
    public String getBookName(){
        return name;
    }
    @Override
    public void writeToParcel(Parcel dest, int flags){
        //当前章节
        dest.writeInt(index);
        //当前章节名字
        dest.writeString(chapterName);
        //书名
        dest.writeString(name);

    }
    @Override
    public int describeContents(){
        return 0;
    }
    @Override
    public String toString(){
        return "parceable";
    }
    public static final Creator<BookInfo> CREATOR = new Creator<BookInfo>() {
        @Override
            public BookInfo createFromParcel(Parcel in) {
            return new BookInfo(in);
        }

        @Override
        public BookInfo[] newArray(int size) {
            return new BookInfo[size];
        }
    };
}
