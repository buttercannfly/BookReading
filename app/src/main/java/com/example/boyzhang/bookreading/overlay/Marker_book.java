package com.example.boyzhang.bookreading.overlay;

import io.realm.RealmObject;

/**
 * Created by boyzhang on 2018/5/7.
 */

public class Marker_book extends RealmObject{
    private String marker_name;
    private int marker_Index;
    private String marker_content;
    private int moveHeight;
    int last_height;
    int pro_height;
    private boolean shouldhavenext;

    public Marker_book(){}
    public Marker_book(String name, String content, int index, int lastheight, int proheight, int moveHeight, boolean shouldhavenext){
        marker_Index = index;
        marker_name = name;
        marker_content = content.substring(0, 30);
        last_height = lastheight;
        pro_height = proheight;
        this.shouldhavenext = shouldhavenext;
        this.moveHeight = moveHeight;
    }

    public void setChapterName(String name){
        marker_name = name;
    }
    public String getChapterName(){
        return marker_name;
    }
    public void setChapterIndex(int index){
        marker_Index = index;
    }
    public int getChapterIndex(){
        return marker_Index;
    }
    public String getMarker_content(){return  marker_content;}
    public void setMarker_content(String s){marker_content = s;}
    public int getMoveHeight(){return moveHeight;}
    public void setMoveHeight(int moveHeight){
        this.moveHeight = moveHeight;
    }
    public boolean getShould_haveNext(){return shouldhavenext;}
    public void setShould_havenext(boolean state){shouldhavenext = state;}
    public void setLast_height(int height){
        last_height = height;
    }
    public int getLast_height(){return last_height;}
    public void setPro_height(int height){
        pro_height = height;
    }
    public int getPro_height(){return pro_height;}
    public Marker creatMarker(){
        return new Marker(marker_name, marker_content);
    }

}
