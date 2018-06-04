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
import java.util.List;
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
    public String []chapterContent={"abc","abc","abc"};
    private ArrayList<String[]>chapterContentAll=new ArrayList<>();
    private  Map<String ,String> chapterAddress=new HashMap<>();
    private  ArrayList<String> chapterList=new ArrayList<>();
    private ArrayList<String> linklist = new ArrayList<>();
    public boolean state = false;
    public boolean state2 = false;
    /*DownLoad(String urlSearch){
        this.urlSearch=urlSearch;
        DealUrlSearch();
        Log.i("分界线","分界线");
       // AllDownLoad();
    }*/
    public DownLoad(){
        ;
    }
    public void DealUrlSearch(final String urlSearch){
       // Log.e("TRY进入","TRY进入");
        Thread Dealurlsearch=new Thread(){
            @Override
            public void run() {
                synchronized (lockForUrlsearch) {
                    try {
                        Log.e("dwnload", "url"+urlSearch);
                        if(urlSearch == null || urlSearch.length() == 0){
                            Log.e("err", "url not found");
                            return;
                        }
                        Document document = Jsoup.connect(urlSearch).get();
                        Element element = document.getElementById("chapters-list");
                        Elements elements = element.select("a");
                        // Log.e("TRY进入","TRY进入");
                        for (int i = 1; i < elements.size(); i++) {
                            String sh = elements.get(i).attr("href");
                            sh = urlSearch + sh.substring(sh.lastIndexOf('/') + 1, sh.length());
                            linklist.add(sh);
                            chapterAddress.put(elements.get(i).text(),sh);
                            chapterList.add(elements.get(i).text());
                            Log.e("章节名字",elements.get(i).text()+" "+sh);
                        }
                        lockForUrlsearch.notify();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                state = true;
            }
        };
        Dealurlsearch.start();
    }
    public String[] PartDownLoad(String urlSearch,int index, String allUrl){
        Log.e("url", " " +urlSearch);
        if(urlSearch != null && urlSearch.length() != 0){
            synchronized (lockForUrlsearch){
                state2 = false;
                try {
                    chapterContent = Down(urlSearch);
                }catch (Exception e){
                    Log.i("PrtDown", "error");
                }
            }
            return chapterContent;
        }
        return new String[]{};
        /*synchronized (lockForUrlsearch) {
            try {
                lockForUrlsearch.wait();
               // Log.i("寻址结束","寻址结束");
                int indexOfChapter = index;
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
        return null;*/
    }
    public  ArrayList<String []> AllDownLoad(String url){
        if(linklist.size() == 0)
        DealUrlSearch(url);
        synchronized (lockForUrlsearch) {
            try{
                state2 = false;
                for (int i = 0; i < linklist.size(); i++) {
                    //Log.e("AllDownLoad--FOR", "进入" + chapterList.size());
                    String chapterName = chapterList.get(i);
                    String chapterAddress_Url = chapterAddress.get(chapterName);
                    synchronized (lockForDown) {
                        chapterContentAll.add(Down(chapterAddress_Url));
                    }
                }

            }catch (Exception e){
                e.printStackTrace();
            }
            state2 = true;
        }
        return chapterContentAll;
    }
    private String[]  Down(final String url){
        Log.e("download","down");
        final String[] Content = new String[1];
        Thread down=new Thread(){
            @Override
            public void run() {
                synchronized (lockForDown) {
                    try {
                        Document document = Jsoup.connect(url).get();
                        Element element = document.getElementById("txtContent");
                        String[] chapterContent = element.text().split(String.valueOf(' '));
                        StringBuffer content = new StringBuffer();
                        for (String b : chapterContent) {
                            content.append(b + "\n" + "  ");
                            //Log.i("每一行", b);
                        }
                        Content[0] = content.toString();
                    } catch (Exception e) {
                        Log.i("error", e.toString());
                        e.printStackTrace();
                    }
                    state2 = true;
                }
            }
        };
        down.start();
        return Content;
    }
    public List<String> returnChapterList(){
        return chapterList;
    }
    public List<String> returnLinkList(){return linklist;}
}
