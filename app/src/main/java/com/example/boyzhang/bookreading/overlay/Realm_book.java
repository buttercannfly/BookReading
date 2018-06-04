package com.example.boyzhang.bookreading.overlay;

import android.media.Image;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by boyzhang on 2018/5/7.
 */

public class Realm_book extends RealmObject {
     @PrimaryKey
     private String bookname;
     private int index;
     private int fontSize;
     private int colorId;
     private boolean shouldhavenext;
     public RealmList<Chapter_book> chapterList;
     public RealmList<Marker_book> bookmarkerList;
     //private List<String> chapterName = new ArrayList<>();
     //private ArrayList<Integer> chapterIndex = new ArrayList<Integer>();
     //private List<Integer> bookmarkList = new ArrayList<>();
     //private Map<String, Integer> bookmarker = new LinkedHashMap<>();
     //不为真实结果
      String newChapter;
      //Image image;
      int allChapter;
      int nowChapter;
      int moveheight;
      int last_height;
      int pro_height;
      String link;
      String filepath;
     //预期设置为下章缓存功能，初期不开通
     //private String nextChapter;

     public Realm_book(String name, String newChapter, int nowChapter, int allChapter){
         this.bookname = name;
         this.newChapter = newChapter;
         this.nowChapter = nowChapter;
         this.allChapter = allChapter;
         chapterList = new RealmList<Chapter_book>();
         bookmarkerList = new RealmList<Marker_book>();
     }

     public Realm_book(){
         chapterList = new RealmList<Chapter_book>();
         bookmarkerList = new RealmList<Marker_book>();
     }

    //将对象属性进行序列化

    public void setChapterList(String[] name,int[] index){
         if(!chapterList.isEmpty() && chapterList.size() != name.length) chapterList.clear();
         for(int i = 0; i < name.length; i++){
             Chapter_book chapter_book = new Chapter_book();
             chapter_book.setChapterName(name[i]);
             chapter_book.setChapterIndex(index[i]);
             chapterList.add(chapter_book);
         }
     }
     public RealmList<Chapter_book> getChapterList(){
         return chapterList;
     }
     //index为当前章节
     public boolean addBookmark(String chapterName, int index){
         //如果已经有此章节
         for (Marker_book marker : bookmarkerList){
             if(marker.getChapterIndex() <= index){
                 if(marker.getChapterIndex() == index){
                     //说明已经存在
                     return false;
                 }
             }
             else break;
         }
         Marker_book marker_book = new Marker_book();
         marker_book.setChapterIndex(index);
         marker_book.setChapterName(chapterName);
         bookmarkerList.add(marker_book);
         return true;
     }
     public RealmList<Marker_book> getBookmark(){
         return bookmarkerList;
     }
     /*public void setChapterName(String[] chapter_name){
         if(!chapterName.isEmpty()) chapterName.clear();
         for (String name : chapter_name){
             chapterName.add(name);
         }
     }
     public String[] getChapterName(){
         String[] chapter_name = new String[chapterName.size()];
         int index = 0;
         for (String name : chapterName)
             chapter_name[index++] = name;
         return chapter_name;
     }*/
     public BookInfo creatBokkInfo(){
         //return new BookInfo(index, bookname, getNowChapterName(), getAllChapter());
         return new BookInfo(index, bookname, getNewChapter(), chapterList.size());
     }
     public void setBookContent(List<String[]> bookContent){
         for(int i = 0; i < bookContent.size(); i++){
             String[] arr = bookContent.get(i);
             StringBuffer s = new StringBuffer();
             for(int j = 0; j < arr.length; j++){
                 s.append(arr[i]);
             }
             chapterList.get(i).setChapterContent(s.toString());
         }
     }
     public List<Chapter_book> getBookContent(){
         return chapterList;
     }
     public int getAllChapter(){
         return chapterList == null ? 0 : chapterList.size();
     }
     public int getNowChapterIndex(){
         return index;
     }
     public void setIndex(int index){
         this.index = index;
     }
     public String getNowChapterName(){
         return chapterList.get(index).getChapterName();
     }
     public String getBookName(){
         return bookname;
     }
     public void setBookName(String name){
        bookname = name;
    }
     public void updateState(){
        //获取最新章节
    }
    public String getNewChapter(){
         return "章节";
        //return chapterList.get(chapterList.size() - 1).getChapterName();
    }
    public void setMoveHeight(int height){
        moveheight = height;
    }
    public int getMoveHeight(){
        return moveheight;
    }
    public boolean haveTheChapter(int index){
        String s = chapterList.get(index).getChapterContent();
        return s.length() == 0 ? false : true;
    }
    public void setChapterContent(String[] content, int index){
        StringBuffer s = new StringBuffer();
        for(int i = 0; i < content.length; i++){
            s.append(content[i]);
        }
        chapterList.get(index).setChapterContent(s.toString());

    }
    public String getChapterContent(int index){

        return chapterList.get(index - 1).getChapterContent();
    }
    public int getFontSize(){
        return fontSize;
    }
    public void setFontSize(int fontSize){
        this.fontSize = fontSize;
    }
    public int getColorId(){
        return colorId;
    }
    public void setColorId(int Id){
        colorId = Id;
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
    public String getIamge(){
        return filepath;
    }
    public void setImage(String path){
        this.filepath = path;
    }
    public String getLink(){
        return link;
    }
    public void setLink(String link){
        this.link = link;
    }

}
