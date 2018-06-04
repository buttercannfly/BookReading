package com.example.boyzhang.bookreading;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.boyzhang.bookreading.Adapter.MyAdapter;
import com.example.boyzhang.bookreading.overlay.DownPictureAndIntroduction;
import com.example.boyzhang.bookreading.overlay.ForBean;
import com.example.boyzhang.bookreading.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

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
    //private List<Nextpage> qq;
    private Document document;
    private ListView lv;
    private String[] mListStr = {"name:ZWK","mature","age","live_area"};
    private String page_url;
    private MyAdapter me;
    private ImageView img;
    private Handler handler;
    private boolean done;
    ListView list_item;
    private boolean isDone;
    private boolean isDone1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);
        Intent it2 = getIntent();
        Bundle bd = it2.getExtras();
        handler = new Handler();
        if(bd != null){
            page_url=bd.getString("a");
            Log.i(TAG,"new url:"+page_url);
            mBeans = new ArrayList<>();
            Log.e(TAG,"run::::"+mBeans.size());
            list_item = (ListView) findViewById(R.id.lllist);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    jsoupData1(page_url);
                    while (!done){

                    }
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            me = new MyAdapter(mBeans,ShowActivity.this);
                            list_item.setAdapter(me);
                        }
                    });
                }
            }).start();
            list_item.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    ForBean bean = mBeans.get(position);
                    Intent intent = new Intent(ShowActivity.this, bookInfo_show.class);
                    intent.putExtra("bean", bean);
                    startActivity(intent);
                }
            });
            //jsoupPage(page_url);
            String a=" https://www.gxwztv.com/ba27696.shtml";
            //jsoupGet(a);
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
                        bean.setImageLink(element.select("a").attr("abs:href"));
                        Log.i(TAG, "小说链接: " + bean.getImageLink());
                        String tg = bean.getImageLink();
                        jsoupD(tg, bean);
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
                    done = true;
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

    public void jsoupD(final String a, final ForBean bean)
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    document = (Document) Jsoup.connect(a)
                            .timeout(10000)
                            .get();
                    Elements noteList = document.select("div.col-xs-8");
                    Elements li = noteList.select("ul.list-group");
                    String back = li.select("a.btn.btn-danger").attr("abs:href");
                    bean.setNovellink(back);
                    Log.i("ty","run:"+back);
                    isDone1 = true;
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();

    }


    /*public void jsoupPage(final String a){
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


    }*/

        /*public void jsoupGet(final String a){
            final Nextpage yp = new Nextpage();
            new Thread(new Runnable() {
                @Override
                public void run() {

                    try{
                        Nextpage ll = new Nextpage();
                        document = (Document) Jsoup.connect(a).timeout(10000).get();
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
        }*/


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
