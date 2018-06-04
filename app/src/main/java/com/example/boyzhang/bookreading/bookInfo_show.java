package com.example.boyzhang.bookreading;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.boyzhang.bookreading.overlay.BookInfo;
import com.example.boyzhang.bookreading.overlay.Chapter;
import com.example.boyzhang.bookreading.overlay.Chapter_book;
import com.example.boyzhang.bookreading.overlay.DownLoad;
import com.example.boyzhang.bookreading.overlay.DownPictureAndIntroduction;
import com.example.boyzhang.bookreading.overlay.ForBean;
import com.example.boyzhang.bookreading.overlay.Realm_book;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

public class bookInfo_show extends AppCompatActivity implements View.OnClickListener{

    private ForBean bean;
    ImageView imageView;
    TextView textView;
    Button textView1;
    Button textView2;
    Button textView3;
    Realm realm;
    List<String> chapterlist;
    List<String> linkList;
    BookInfo bookInfo;
    byte[] bytes;
    Handler handler;
    boolean done;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_info_show);
        realm = Realm.getDefaultInstance();
        bean = getIntent().getParcelableExtra("bean");
        imageView = findViewById(R.id.image);
        textView = findViewById(R.id.name);
        textView1 = findViewById(R.id.text1);
        textView2 = findViewById(R.id.text2);
        textView3 = findViewById(R.id.text3);
        textView1.setOnClickListener(this);
        textView3.setOnClickListener(this);
        textView2.setOnClickListener(this);
        handler = new Handler();
        textView.setText(bean.getNovellink());
        getIamge(bean);

    }
    public void getIamge(final ForBean bean){
        final DownPictureAndIntroduction downPictureAndIntroduction = new DownPictureAndIntroduction(bean.getImageLink());
        final String s = bean.getImageLink();
        Log.e("put",s + "1");
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.e("put",s + "1");
                downPictureAndIntroduction.downPicture(s);
                while (!downPictureAndIntroduction.state){

                }
                String path = downPictureAndIntroduction.filepath;
                bean.setImage(path);
                final Bitmap bitmap = downPictureAndIntroduction.bitmap;
                Log.e("put","path:" + path);

                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        imageView.setImageBitmap(bitmap);
                    }
                });
            }
        }).start();
    }
    @Override
    public void onClick(View v){
        int id = v.getId();
        switch (id){
            case R.id.text2:{
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        DownLoad downLoad = new DownLoad();
                        downLoad.DealUrlSearch(bean.getNovellink());
                        while (! downLoad.state){

                        }
                        linkList = downLoad.returnLinkList();
                        List<String> list = downLoad.returnChapterList();
                        final Realm_book realm_book = new Realm_book();
                        realm_book.setBookName(bean.getNovelname());
                        realm_book.setIndex(1);
                        realm_book.setLink(bean.getNovellink());
                        realm_book.setImage(bean.getIamge());
                        for(int i = 0; i < list.size(); i++){
                            Chapter_book chapter_book = new Chapter_book(i, list.get(i));
                            chapter_book.seurl(linkList.get(i));
                            realm_book.chapterList.add(chapter_book);
                        }
                        if(chapterlist != null){
                            for(int i = 0; i < list.size(); i++){
                                realm_book.chapterList.get(i).setChapterContent(chapterlist.get(i));
                            }
                        }
                        bookInfo = realm_book.creatBokkInfo();
                        Realm realm = Realm.getDefaultInstance();
                        realm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                realm.copyToRealmOrUpdate(realm_book);
                            }
                        });
                        realm.close();
                        done = true;
                    }
                }).start();
                while ( !done);
                Intent intent = new Intent(bookInfo_show.this, Book_show.class);
                intent.putExtra("bookInfo", bookInfo);
                startActivity(intent);
                break;
            }
            case R.id.text3:{

                List<String[]> list = new DownLoad().AllDownLoad(bean.getNovellink());
                chapterlist = new ArrayList<>();
                for(int i = 0; i < list.size(); i++){
                    String[] strings = list.get(i);
                    String s = new String();
                    s = s + "\n" + "\n" + "\n";
                    for(int j = 0; j < strings.length; j++){
                        s = s + "  " + strings[j] + "\n";
                    }
                    s = s + "\n" + "\n" + "\n";
                    chapterlist.add(s);
                    Log.e("down", "下载");
                }

            }
            case R.id.text1:{
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("err", bean.getNovellink());
                        DownLoad downLoad = new DownLoad();
                        downLoad.DealUrlSearch(bean.getNovellink());
                        while (! downLoad.state){

                        }
                        linkList = downLoad.returnLinkList();
                        List<String> list = downLoad.returnChapterList();
                        final Realm_book realm_book = new Realm_book();
                        realm_book.setBookName(bean.getNovelname());
                        realm_book.setIndex(1);
                        realm_book.setLink(bean.getNovellink());
                        realm_book.setImage(bean.getIamge());
                        for(int i = 0; i < list.size(); i++){
                            Chapter_book chapter_book = new Chapter_book(i, list.get(i));
                            chapter_book.seurl(linkList.get(i));
                            realm_book.chapterList.add(chapter_book);
                        }
                        if(chapterlist != null){
                            for(int i = 0; i < list.size() && i < chapterlist.size(); i++){
                                realm_book.chapterList.get(i).setChapterContent(chapterlist.get(i));
                            }
                        }
                        Realm realm = Realm.getDefaultInstance();
                        realm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                realm.copyToRealmOrUpdate(realm_book);
                            }
                        });
                        realm.close();

                    }
                }).start();
                break;
            }

        }
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        realm.close();
    }
}
