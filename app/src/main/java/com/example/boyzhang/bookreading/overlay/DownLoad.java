package com.example.boyzhang.bookreading.overlay;

import android.content.Context;
import android.util.Log;

import org.jsoup.Jsoup;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import android.content.Context;
import java.io.IOException;
import java.io.InputStream;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Created by K on 2018/5/18.
 */

public class DownLoad {
    //private String urlSearch;
    final  String lockForUrlsearch="lock";
    final String lockForDown="lock";
    private String []chapterContent={"abc","abc","abc"};
    private ArrayList<String[]>chapterContentAll=new ArrayList<>();
    private  Map<String ,String> chapterAddress=new HashMap<>();
    private  ArrayList<String> chapterList=new ArrayList<>();
    /*DownLoad(String urlSearch){
        this.urlSearch=urlSearch;
        DealUrlSearch();
        Log.i("分界线","分界线");
       // AllDownLoad();
    }*/
    public DownLoad(){
        ;
    }
    private void DealUrlSearch(final String urlSearch){
       // Log.e("TRY进入","TRY进入");
        Thread Dealurlsearch=new Thread(){
            @Override
            public void run() {
                synchronized (lockForUrlsearch) {
                    try {
                        Document document = Jsoup.connect(urlSearch).get();
                        Element element = document.getElementById("chapters-list");
                        Elements elements = element.select("a");
                        // Log.e("TRY进入","TRY进入");
                        for (int i = 1; i < elements.size(); i++) {
                            String sh = elements.get(i).attr("href");
                            sh = urlSearch + sh.substring(sh.lastIndexOf('/') + 1, sh.length());
                            chapterAddress.put(elements.get(i).text(),sh);
                            chapterList.add(elements.get(i).text());
                            Log.e("章节名字",elements.get(i).text()+" "+sh);
                        }
                        lockForUrlsearch.notify();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        Dealurlsearch.start();
    }
    public String[] PartDownLoad(String urlSearch,String chapterName){
        DealUrlSearch(urlSearch);
        synchronized (lockForUrlsearch) {
            try {
                lockForUrlsearch.wait();
               // Log.i("寻址结束","寻址结束");
                int indexOfChapter = chapterList.indexOf(chapterName);
               // Log.e("索引",String.valueOf(indexOfChapter)+" "+chapterAddress.get(chapterList.get(indexOfChapter+1)));
                if (indexOfChapter == chapterList.size() - 1) {
                    return null;
                }

                Down(chapterAddress.get(chapterList.get(indexOfChapter+1)));
                synchronized (lockForDown) {
                    lockForDown.wait();
                    return chapterContent;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return null;
    }
    public  ArrayList<String []> AllDownLoad(String url){
        DealUrlSearch(url);
        synchronized (lockForUrlsearch) {
            try{
                lockForUrlsearch.wait();
                for (int i = 0; i < 3; i++) {
                    //Log.e("AllDownLoad--FOR", "进入" + chapterList.size());
                    String chapterName = chapterList.get(i);
                    String chapterAddress_Url = chapterAddress.get(chapterName);
                    Down(chapterAddress_Url);
                    synchronized (lockForDown) {
                        lockForDown.wait();
                        chapterContentAll.add(chapterContent);
                    }
                }

            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return chapterContentAll;
    }
    private void  Down(final String url){
        Thread down=new Thread(){
            @Override
            public void run() {
                synchronized (lockForDown) {
                    try {
                        Document document = Jsoup.connect(url).get();
                        Element element = document.getElementById("txtContent");
                        chapterContent = element.text().split(String.valueOf(' '));

                        for (String b : chapterContent) {
                            Log.i("每一行", b);
                        }
                        lockForDown.notify();
                    } catch (Exception e) {
                        Log.i("error", e.toString());
                        e.printStackTrace();
                    }
                }
            }
        };
        down.start();
    }
}
