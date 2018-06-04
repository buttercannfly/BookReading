package com.example.boyzhang.bookreading.overlay;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Documented;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.ByteBuffer;

/**
 * Created by K on 2018/5/21.
 */

public class DownPictureAndIntroduction {
    public Bitmap bitmap;
    public boolean state;
    String name;
    public String filepath;
    public DownPictureAndIntroduction(String name){
        this.name = name;
    }
    public void downPicture(final String url){
        new Thread(){
            public void run(){
                try{
                   // Log.e("进入","进入");
                    Document document= Jsoup.connect(url).get();
                    Elements elements=document.select("div.col-xs-2");
                    Element element=elements.get(0);
                    String imageUrl=element.select("img.img-thumbnail").attr("src");
                    //Log.e("图片链接",imageUrl);
                   // Log.e("大小",String.valueOf(elements.size()));

                    //Log.e("图片链结","图片连接");
                    URL url=new URL(imageUrl);
                    HttpURLConnection connection=(HttpURLConnection)url.openConnection();
                    connection.setDoInput(true);
                    connection.connect();
                    InputStream is = connection.getInputStream();
                    BufferedInputStream bis = new BufferedInputStream(is);
                    bitmap = BitmapFactory.decodeStream(bis);

                    bis.close();
                    is.close();
                    saveBitmapToSDCard(bitmap, name);
                    state = true;
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }.start();
    }
    /**
     * 保存bitmap到SD卡
     * @param bitmap
     * @param imagename
     */
    public  void saveBitmapToSDCard(Bitmap bitmap, String imagename) {
        String path = "/sdcard/BookReading/Picture/"+imagename;
        File file = new File(path);
        if(file.exists()){
            file.delete();
        }
        FileOutputStream out;
        try{
            out = new FileOutputStream(file);
            if(bitmap.compress(Bitmap.CompressFormat.PNG, 90, out))
            {
                out.flush();
                out.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e){
            e.printStackTrace();
        }
        filepath = path;
    }
}
