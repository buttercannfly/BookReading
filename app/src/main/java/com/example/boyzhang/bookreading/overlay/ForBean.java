package com.example.boyzhang.bookreading.overlay;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import com.example.boyzhang.bookreading.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZHANGWEIKANG on 2018/4/27.
 */

public class ForBean  implements Parcelable {
    public String text;
    public String filepath;
    public String Novelname;
    public String Novellink;
    public String ImageLink;
    public String latestname;
    public String latestlink;
    public String authorName;
    public String time;
    public List<String> chapterList;

    protected ForBean(Parcel in){
        Novelname = in.readString();
        authorName = in.readString();
        Novellink = in.readString();
        ImageLink = in.readString();
    }
    public ForBean(){
        chapterList = new ArrayList<>();
    }
    public String getIamge(){
        return filepath;
    }
    public void setImage(String path){
        this.filepath = path;
    }

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
    public String getImageLink(){return ImageLink;}
    public void setImageLink(String novellink){this.ImageLink=novellink;}
    public String getLatestlink(){return latestlink;}
    public void setLatestlink(String latestlink){this.latestlink=latestlink;}
    public String getTime(){return time;}
    public void setTime(String time){this.time=time;}
    public String getAuthorName(){return authorName;}
    public void setAuthorName(String authorName){this.authorName=authorName;}
    public void setChapterList(List<String> list){
        chapterList = list;
    }
    public List<String> grtChapterList(List<String> list){
        return chapterList;
    }
    @Override
    public void writeToParcel(Parcel dest, int flags){
        //书名
        dest.writeString(Novelname);
        //作者名
        dest.writeString(authorName);
        //书链接
        dest.writeString(Novellink);
        //图片链接
        dest.writeString(ImageLink);

    }
    @Override
    public int describeContents(){
        return 0;
    }
    @Override
    public String toString(){
        return "parceable";
    }
    public static final Creator<ForBean> CREATOR = new Creator<ForBean>() {
        @Override
        public ForBean createFromParcel(Parcel in) {
            return new ForBean(in);
        }

        @Override
        public ForBean[] newArray(int size) {
            return new ForBean[size];
        }
    };
}
