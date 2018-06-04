package com.example.boyzhang.bookreading;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.boyzhang.bookreading.Adapter.BookShelfAdapter;
import com.example.boyzhang.bookreading.FileShareModule.FileShareActivity;
import com.example.boyzhang.bookreading.overlay.BookInfo;
import com.example.boyzhang.bookreading.overlay.Realm_book;
import com.example.boyzhang.bookreading.ShowActivity;
import com.example.boyzhang.bookreading.view.ViewPage;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmSchema;
import io.realm.RealmConfiguration;
import io.realm.exceptions.RealmMigrationNeededException;
import io.realm.RealmList;
import io.realm.RealmResults;

public class MainActivity extends Activity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener{
    Realm realm;
    BookShelfAdapter adapter;
    List<BookInfo> bookList;
    EditText editText;
    ListView listView;
    Handler handler;
    Button button;
    ImageView imageView;
    ImageView boardview;
    String[] permissions = new String[]{"Manifest.permission.WRITE_EXTERNAL_STORAGE"};
    public static String new_url = "https://www.gxwztv.com/search.htm?keyword=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //如果没有写入权限
        //if(ContextCompat.checkSelfPermission(this, permissions[0]) != PackageManager.PERMISSION_GRANTED){
        //    ActivityCompat.requestPermissions(this, permissions, 111);
        //}

        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(config);
        realm = Realm.getDefaultInstance();
        editText = findViewById(R.id.edit);
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    findViewById(R.id.linearLayout).setVisibility(View.GONE);
                }
                else{
                    findViewById(R.id.linearLayout).setVisibility(View.VISIBLE);
                }
            }
        });
        /*RealmConfiguration config = new RealmConfiguration.Builder()
                .name("mybook.realm") //文件名
                .schemaVersion(0)
                .build();
        Realm.setDefaultConfiguration(config);*/

        /*RealmResults<Realm_book> userList = realm.where(Realm_book.class)
                .equalTo("bookname", "第一本书").findAll();
        if(userList.size() != 0)
            Log.i("tag", "get");
        Log.i("tag", "no");*/
        button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = editText.getText().toString();
                new_url=new_url+ s;
                Intent main_intent = new Intent(MainActivity.this,TextViewTest.class);
                Bundle bd = new Bundle();
                bd.putString("a",new_url);
                main_intent.putExtras(bd);
//                jsoupPage(new_url);
                new_url="https://www.gxwztv.com/search.htm?keyword=";
                startActivity(main_intent);
            }
        });
        listView = (ListView) findViewById(R.id.bookshelf);
        imageView = findViewById(R.id.share);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FileShareActivity.class);
                Log.i("info", "change");
                startActivity(intent);
            }
        });
        boardview = findViewById(R.id.list);
        boardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ViewPage.class);
                Log.i("info", "change");
                startActivity(intent);
            }
        });
        handler = new Handler();
        InitBookList();
        adapter = new BookShelfAdapter(getApplicationContext(), bookList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);
        //Intent intent = new Intent(MainActivity.this, Book_show.class);*/
       // startActivity(intent);
    }

    public void InitBookList(){
        bookList = new ArrayList<>();
        RealmResults<Realm_book> books = realm.where(Realm_book.class).findAll();
        Log.e("tag", books.size() + "size");
        if(!books.isEmpty()){
            for(int i = 0; i < books.size(); i++){
                bookList.add(books.get(i).creatBokkInfo());
            }
            return;
        }
    }

    public void addToRealm(final Realm_book realm_book){
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(realm_book);
            }
        });
    }

    public List<BookInfo> getBookList(){
        RealmResults<Realm_book> books = realm.where(Realm_book.class).findAll();
        List<BookInfo> bookInfos = new ArrayList<>();
        for(int i = 0; i < books.size(); i++){
            bookInfos.add(books.get(i).creatBokkInfo());
        }
        return bookInfos;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        BookInfo bookInfo = bookList.get(position);
        Intent intent = new Intent(MainActivity.this, Book_show.class);
        intent.putExtra("bookInfo", bookInfo);
        startActivity(intent);

    }
    //长按删除
    @Override
    public boolean onItemLongClick(AdapterView<?> arg0, View arg1, final int arg2, long arg3){
        BookInfo bookInfo = bookList.get(arg2);
        String bookname = bookInfo.getBookName();
        final RealmResults<Realm_book> realm_books = realm.where(Realm_book.class)
                .equalTo("bookname", bookname).findAll();
        if(realm_books.size() > 0)
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Log.e("delete", realm_books.get(0).getBookName());
                realm_books.get(0).deleteFromRealm();
            }
        });
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        List<BookInfo> bookInfos = getBookList();
                        adapter = new BookShelfAdapter(getApplicationContext(), bookInfos);
                        listView.setAdapter(adapter);
                        Log.e("up","update");
                    }
                });
            }
        };
        new Thread(runnable).start();
        return true;
    }


    @Override
    public void onRestart(){
        super.onRestart();
        RealmResults<Realm_book> books = realm.where(Realm_book.class).findAll();
        if(!books.isEmpty()){
            bookList = new ArrayList<>();
            for(int i = 0; i < books.size(); i++)
                bookList.add(books.get(i).creatBokkInfo());
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        adapter = new BookShelfAdapter(getApplicationContext(), bookList);
                        listView.setAdapter(adapter);
                        Log.e("up","update");
                    }
                });
            }
        }).start();

    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        realm.close();
    }
}
