package com.example.boyzhang.bookreading.overlay;

/**
 * Created by boyzhang on 2018/5/20.
 */

public class Marker {
    String chapterName;
    String chapterContent;
    public Marker(String name, String content){
        chapterContent = content;
        chapterName = name;
    }
    public String getName(){
        if(chapterName != null)
        return chapterName;
        else return "这是一个书签";
    }
    public String getContent(){return chapterContent;}
}
