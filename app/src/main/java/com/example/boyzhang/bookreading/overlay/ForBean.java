package com.example.zhangweikang.book_search;

import android.nfc.Tag;
import android.util.Log;

import static android.content.ContentValues.TAG;

/**
 * Created by ZHANGWEIKANG on 2018/4/27.
 */

public class ForBean {
    public String text;
    public String image;
    public String Novelname;
    public String Novellink;
    public String latestname;
    public String latestlink;
    public String authorName;
    public String time;
    
   public String getImage(){return image;}
    public void setImage(String image){this.image = image;}

    public String getText(){
        return text;
    }
    public void setText(String text){
        this .text=text;
    }
    public String getNovelname(){return Novelname;}
    public void setNovelname(String novelname){
        Log.i("sdjaid","23184194");
        this.Novelname= novelname;
    }
    public String getLatestname(){return latestname;}
    public void setLatestname(String latestname){this.latestname=latestname;}
    public String getNovellink(){return Novellink;}
    public void setNovellink(String novellink){this.Novellink=novellink;}
    public String getLatestlink(){return latestlink;}
    public void setLatestlink(String latestlink){this.latestlink=latestlink;}
    public String getTime(){return time;}
    public void setTime(String time){this.time=time;}
    public String getAuthorName(){return authorName;}
    public void setAuthorName(String authorName){this.authorName=authorName;}
}
