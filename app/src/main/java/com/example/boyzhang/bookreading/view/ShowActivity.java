package com.example.zhangweikang.book_search;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.test.UiThreadTest;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ShowActivity extends AppCompatActivity {
    private final String lock="lock";
    private static final String TAG = "ShowActivity";
    private List<ForBean> mBeans;
    private List<Page> mPage;
    private List<Nextpage> qq;
    private Document document;
    private ListView lv;
    private String[] mListStr = {"name:ZWK","mature","age","live_area"};
    private  String page_url;
    private MyAdapter me;
    private ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);
        Intent it2 = getIntent();
        Bundle bd = it2.getExtras();
        if(bd != null){
            page_url=bd.getString("a");
            Log.i(TAG,"new url:"+page_url);
            mBeans = new ArrayList<>();
            mPage = new ArrayList<Page>();
            qq = new ArrayList<Nextpage>();
            jsoupData1(page_url);
            jsoupPage(page_url);
            Log.e(TAG,"run::::"+mBeans.size());
            ListView list_item = (ListView) findViewById(R.id.lllist);
            me = new MyAdapter(mBeans,ShowActivity.this);
            list_item.setAdapter(me);
            String a=" https://www.gxwztv.com/ba27696.shtml";
            jsoupGet(a);
            img = (ImageView)findViewById(R.id.img);
        }
        Log.i(TAG,"END");

 }


    public void jsoupData1(final String a){
        mBeans.clear();
        new Thread(new Runnable() {
            @Override
            public void run() {
            synchronized (lock) {
                try {
                    Log.i(TAG, "run: " + a);
                    document = (Document) Jsoup.connect(a)
                            .timeout(10000)
                            .get();
                    Elements noteList = document.select("div.panel-body");
                    Elements li = noteList.select("li.list-group-item.clearfix");
                    Log.i(TAG, "run:liNumber" + li.size());

                    for (Element element : li) {
                        ForBean bean = new ForBean();
                        bean.setNovelname(element.select("div.col-xs-3").text());
                        Log.i(TAG, "小说名称:" + bean.getNovelname());
                        bean.setNovellink(element.select("a").attr("abs:href"));
                        Log.i(TAG, "小说链接: " + bean.getNovellink());
                        bean.setLatestname(element.select("div.col-xs-4").text());
                        Log.i(TAG, "最新章节:" + bean.getLatestname());
                        bean.setLatestlink(element.select("a").attr("abs:href"));
                        Log.i(TAG, "最新章节链接:" + bean.getLatestlink());
                        String b = element.select("span.time").text();
                        String c = element.select("div.col-xs-2").text();
                        c = c.substring(0, c.indexOf(b));
                        bean.setAuthorName(c);
                        Log.i(TAG, "作者名称:" + bean.getAuthorName());
                        bean.setTime(b);
                        Log.i(TAG, "发表时间: " + bean.getTime());
                        if(bean.getNovelname().equals("小说名称")== false){
//                            bean.setImage(jsoupGetextra(bean.getNovellink()));
                            mBeans.add(bean);
                        }
                    }
                    lock.notify();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            }
        }).start();
       synchronized (lock) {
           try {
               lock.wait();
           }catch (Exception e){
               e.printStackTrace();
           }
       }

    }


    public void jsoupPage(final String a){
        mPage.clear();
        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (lock) {
                    try {
                        document = (Document) Jsoup.connect(a)
                                .timeout(10000)
                                .get();
                        Elements noteList = document.select("ul.pagination.pagination-sm");
                        Elements li = noteList.select("li");

                        for (Element element : li) {
                            Page page_former = new Page();
                            page_former.setPage(element.select("li").text());
                            Log.i(TAG, "page" + page_former.getPage());
                            page_former.setPage_link(element.select("a").attr("abs:href"));
                            Log.i(TAG, "link" + page_former.getPage_link());
                            mPage.add(page_former);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();


    }

        public void jsoupGet(final String a){
            final Nextpage yp = new Nextpage();
            new Thread(new Runnable() {
                @Override
                public void run() {

                    try{
                        Nextpage ll = new Nextpage();
                        document = (Document)Jsoup.connect(a).timeout(10000).get();
                        Elements noteList = document.select("div.panel-body");
                        Elements li = noteList.select("div.col-xs-8");
                        Elements lii = li .select("div.panel.panel-default.mt20");
                        Elements liii = lii .select("div.panel-body");
                        String pic = document.select("img.img-thumbnail").attr("abs:src");
                        if(pic == null){
                            Log.e(TAG,"no pic down");
                        }
                        Bitmap lone = decodeUriAsBitmapFromNet(pic);
                        if(lone == null)
                        {
                            Log.e(TAG,"no bitmap");
                        }
                        Drawable drawable = new BitmapDrawable(lone);
                        ll.setImage(pic);
                        ll.setPrimary_text(liii.select("p#shot").text());
                        Log.e(TAG,"runn:"+ll.getPrimary_text());
                        qq.add(ll);
                        Log.e(TAG,"RUNN::"+qq.get(0).getPrimary_text());
                    }
                    catch(Exception e){
                        e.printStackTrace();
                    }
                }
            }).start();
        }


//    @Override
//    public void onItemClick(AdapterView<?> parent,View view,int position,long id){
//        TextView textView = (TextView)view.findViewById(R.id.novel_name);
//        Intent novel =new Intent(ShowActivity.this,Book.class);
//        novel.putExtra("novel",((ForBean)adapter.getItem(position)).getNovellink());
//        startActivity(novel);
//
//    }
private Bitmap decodeUriAsBitmapFromNet(String url) {
    URL fileUrl = null;
    Bitmap bitmap = null;

    try {
        fileUrl = new URL(url);
    } catch (MalformedURLException e) {
        e.printStackTrace();
    }

    try {
        HttpURLConnection conn = (HttpURLConnection) fileUrl
                .openConnection();
        conn.setDoInput(true);
        conn.connect();
        InputStream is = conn.getInputStream();
        bitmap = BitmapFactory.decodeStream(is);
        is.close();
    } catch (IOException e) {
        e.printStackTrace();
    }
    return bitmap;

}
}
