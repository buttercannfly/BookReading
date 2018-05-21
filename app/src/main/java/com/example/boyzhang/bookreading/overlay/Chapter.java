package com.example.boyzhang.bookreading.overlay;

/**
 * Created by boyzhang on 2018/5/16.
 */

public class Chapter {
    private int index;
    private String name;
    public Chapter(int index, String s){
        this.index = index;
         name = s;
    }
    public int getIndex(){return index;}
    public String getName(){return name;}
}
