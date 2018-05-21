package com.example.boyzhang.bookreading.overlay;

import io.realm.RealmObject;

/**
 * Created by boyzhang on 2018/5/7.
 */

public class Chapter_book extends RealmObject{
    private String chapterName;
    private int chapterIndex;
    private String chapterContent;

    public Chapter_book(){}
    public Chapter_book(int index, String chapterName){
        chapterIndex = index;
        this.chapterName = chapterName;
    }
    public void setChapterName(String name){
        chapterName = name;
    }
    public String getChapterName(){
        return chapterName;
    }
    public void setChapterIndex(int index){
        chapterIndex = index;
    }
    public int getChapterIndex(){
        return chapterIndex;
    }
    public void setChapterContent(String content){
        chapterContent = content;
    }
    public String getChapterContent(){
        return chapterContent;
    }
    public Chapter creatChapter(){
        return new Chapter(chapterIndex, chapterName);
    }
}
