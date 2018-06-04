package com.novelreader.novelreader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
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
    public byte[] bytes;
    public boolean state;
    DownPictureAndIntroduction(){
    }
    public void downPicture(){
        new Thread(){
            public void run(){
                try{
                   // Log.e("进入","进入");
                    Document document= Jsoup.connect("https://www.gxwztv.com/ba598.shtml").get();
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
                    bytes=BitMapToByte(bitmap);
                    state = true;
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }.start();
    }
    private byte[] BitMapToByte(Bitmap bitmap){
        int number=bitmap.getByteCount();
        ByteBuffer buf = ByteBuffer.allocate(number);
        bitmap.copyPixelsToBuffer(buf);
        byte[] by = buf.array();
        return by;
    }
}
