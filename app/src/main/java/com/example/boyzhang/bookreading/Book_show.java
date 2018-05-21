package com.example.boyzhang.bookreading;

import android.app.Activity;
import android.content.Context;
import android.gesture.GestureOverlayView;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.Settings;
import android.text.method.ScrollingMovementMethod;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.boyzhang.bookreading.Adapter.ChapterListAdapter;
import com.example.boyzhang.bookreading.Adapter.MarkerListAdapter;
import com.example.boyzhang.bookreading.overlay.BookInfo;
import com.example.boyzhang.bookreading.overlay.Chapter;
import com.example.boyzhang.bookreading.overlay.Chapter_book;
import com.example.boyzhang.bookreading.overlay.DownLoad;
import com.example.boyzhang.bookreading.overlay.Marker;
import com.example.boyzhang.bookreading.overlay.Marker_book;
import com.example.boyzhang.bookreading.overlay.Realm_book;
import com.example.boyzhang.bookreading.view.Circle;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmList;
import io.realm.RealmResults;

//http://www.wzzw.la/28/28945/
// OnGestureListener 为手势识别监听器接口
public class Book_show extends Activity implements View.OnTouchListener, GestureDetector.OnGestureListener,View.OnClickListener{
    Realm realm;
    Realm_book book = new Realm_book();
    BookInfo bookInfo;
    DownLoad downLoad;
    private int fontsize;
    TextView chapterNow;
    private String chapterNow_name;
    TextView booktxt;
    ScrollView scrollView;
    private DisplayMetrics metrics;
    GestureDetector gestureDetector;
    LinearLayout layout_menu;
    LinearLayout layout_book;
    boolean isVisible = false;
    final int min_distance =50;
    final int min_speed = 100;
    private int mCurrentX = 0, mCurrentY = 0;// TextView左上角的像素值
    private int mScreenWidth, mScreenHeight;// 屏幕分辨率
    private int colorId;
    Context context;
    //当前章节
    int index = 1;
    //手势结果标识, state == 1时为上滑， 为-1时是下滑
    int state;
    //亮度
    int light = -1;
    //上textview的高度
    int last_height = 0;
    //下textview的高度
    int pro_height = 0;
    boolean test = true;
    //上滑更新标志
    boolean upstate = true;
    //下滑标志
    boolean downstate = true;
    boolean atBootm = false;
    //link为中间滑动标志
    boolean link;
    boolean shouldhavenext;
    List<Chapter> chapterList;
    List<Marker> markerList;
    private PopupWindow mPopWindow;
    private ImageButton returnButton;
    private ImageView more;
    private ImageView catalog;
    private ImageView font;
    private ImageView setting;
    private SeekBar lightBar;
    private TextView fontAdd;
    private TextView fontMinus;
    private LinearLayout color_graygreen;
    private LinearLayout color_green;
    private LinearLayout color_gray;
    private LinearLayout color_yellow;
    TextView fontSize;
    View.OnClickListener popupwindowListener;
    private Handler handler;
    int hight = 0;
    int moveheight;
    RealmResults<Realm_book> realm_books;
    String content1 = "城市的道路交通是城市基础设施的重要组成部分，是城市的血管和生命线，随着近年来城市经济的快速发展，城市化步伐的加快，许多城市都在迅速地拉大城市的框架，进行了许多的基础设施建设和道路建设。这些密集的设施和道路，大大加剧了城市交通的复杂程度。虽然城市也有了越来越多的公共交通设施，但由于城市人口愈加密集，车辆愈加拥堵，人们的出行依然不太方便，特别是对于一些对城市设施部署不熟悉的人。而现在提供路线推荐的许多应用，如百度地图，高德地图等，虽然能为用户推荐线路，但这些推荐往往只是针对单一交通方案的推荐，比如单一的公交路线。但是有的时候，人们出行并不只是要选择一种单一的交通方式，而且由于城市交通方式的复杂性以及基础设施分布的复杂性，这种单一的交通方案推荐往往不能满足预期要求。因此实现一种能综合考虑多种交通方案，并能满足用户时间和金钱花费预期要求的出行路线推荐方法是非常有必要的。\n" +
            "在路线推荐方面，已经有了很多研究成果。一般情况下，路线规划算法大多是基于基本的图搜索算法 Dijkstra、Floyd 等[1]。而现实中路径选择经常使用的流行技术有动态规划，扩展的A*算法等。目前路线规划的研究主要分为两种：一种是纯路线规划算法的研究；另一种是基于对日常生活中人们的大量行为（如轨迹等）的分析，挖掘出里面的知识来进行路线的规划。2008年前后，以KIT为主的研究院产出了多个路线规划加速算法，其中影响较大的有contraction hierarchies[2]和highway hierarchies[3]，加之Microsoft提出的Customizable Route Planning[4]，与传统的A*算法，基本支撑起目前工业界地图产品的路线规划服务。\n" +
            "对于行车路线推荐或者规划算法，文献[5][6][7]中根据出租车经过路段的频率将路段进行等级划分，结合Dijkstra算法，提出了基于出租车司机经验的分层路径规划方法，用于进行行车路线的推荐。文献[8]中针对传统的路径规划算法在大规模路网中效率较低而且没有考虑实际交通中的各种因素这种情况，基于出租车的轨迹数据提出一种通过出租车数据挖掘司机在路径选择上的经验，并根据出租车在各时段的速度和频次利用贝叶斯分类器对路网进行分层，使用分层路径规划算法进行行车路线规划的算法，该方法是在文献[3]基础上进行的改进。文献[9]中首先分析了GPS数据，然后通过区域极值点的道路交叉点，用改进的Prim路径选择算法恢复地图，最后将SPFA(最短路径算法)应用于之前恢复的路网中从而得到最优路径规划。在文献[10]，由于传统的路径规划算法并不一定能计算得到现实中最优路径的问题，两者之间的误差可能会很大，提出了一种融合了出租车驾驶经验并以时间为度量的路径。 //这段可以删除\n" +
            "但是以上这些论文研究中考虑的都是将各个单一交通模式的交通设施网络与换乘点结合起来，实际上在进行按照最短路线算法进行规划，并不能真正反映某些交通模式的通行路线，例如出租车路线，实际行驶过程中很大部分路段并不是按照最短路线来进行行驶的，另外在某些路段可能采用其他的交通方式更为合适，这些研究在当前的地图服务或者路线推荐服务中并没有实现。所以基于以上的原因，本文提出了一种将出租车、地铁与步行混合的出行路线推荐方法，综合考虑出租车，步行和地铁三种交通方案，能够根据各种交通模式的特性和利弊，为人们的出行提供一种综合的路线推荐方法。\n";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        context = getApplicationContext();
        metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        mScreenWidth = metrics.widthPixels;
        mScreenHeight = metrics.heightPixels;// 获得屏幕分辨率
        setContentView(R.layout.activity_book_show);

        downLoad = new DownLoad();
        handler = new Handler();
        realm = Realm.getDefaultInstance();
        bookInfo = getIntent().getParcelableExtra("bookInfo");
        String name = bookInfo.getBookName();
        index = bookInfo.getNowChapterIndex();
        chapterNow_name = bookInfo.getNewChapter();
        realm_books = realm.where(Realm_book.class)
                .equalTo("bookname", name).findAll();

        gestureDetector = new GestureDetector(this, this);
        layout_book = (LinearLayout) findViewById(R.id.layout_book);
        layout_menu = (LinearLayout)findViewById(R.id.layout_menu);
        chapterNow = findViewById(R.id.chaptername);
        scrollView =(ScrollView)findViewById(R.id.scrollView);
        booktxt = (TextView) findViewById(R.id.bookcontent);
        returnButton = (ImageButton)findViewById(R.id.iv_return);
        more = (ImageView)findViewById(R.id.iv_more);
        catalog = (ImageView)findViewById(R.id.catalog);
        font = (ImageView)findViewById(R.id.font);
        setting = (ImageView)findViewById(R.id.setting);
        popupwindowListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = v.getId();
                switch(id){
                    case R.id.resource:{
                        //点击换源时

                        break;
                    }
                    case R.id.marker:{
                        //点击书签时

                        break;
                    }
                    default: break;
                }
            }
        };

        returnButton.setOnClickListener(new View.OnClickListener() {
            //返回上一层
            @Override
            public void onClick(View v) {

            }
        });
        more.setOnClickListener(new View.OnClickListener() {
            //暂未开通
            @Override
            public void onClick(View v) {
                //turnoffMenu();
                showPopUpWindow_more();
            }
        });
        catalog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                turnoffMenu();
                showPopUoWindow_chapter();
            }
        });
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                turnoffMenu();
                showPopUoWindow_setting();
            }
        });
        //layout_book.setBackgroundColor(colorId);
        scrollView.setOnTouchListener(this);
        scrollView.setFocusable(true);
        scrollView.setClickable(true);
        scrollView.setLongClickable(true);
        //booktxt.setMovementMethod(ScrollingMovementMethod.getInstance());
        /*Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .name("mybook.realm") //文件名
                .build();
        realm = Realm.getInstance(config);
        RealmResults<Realm_book> userList = realm.where(Realm_book.class)
                .equalTo("bookname", "第一本书").findAll();*/
        InitChapterList();
        String s = loadFile();
        chapterNow.setText(index + " " + chapterNow_name);
        booktxt.setText( s);
        /*Runnable runnable = new Runnable() {
            @Override
            public void run() {
                scrollView.scrollTo(0, moveheight);
            }
        };
        new Thread(runnable).start();*/
        //InitSettingMenu();
    }

    public void InitChapterList(){
        String s = "章节";
        String title = "标题";
        chapterList = new ArrayList<>();
        for(int i = 0; i < 100; i++){
            chapterList.add(new Chapter(i, s + i + title));
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                Realm realm = Realm.getDefaultInstance();
                final ArrayList<String[]> arrayList = downLoad.AllDownLoad("http://www.wzzw.la/28/28945/");
                while (arrayList.size() < 3){
                    SystemClock.sleep(100);
                    Log.e("size", arrayList.size() + "");
                }
                System.out.println(arrayList.get(0)[0]);
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        //先查找后得到User对象
                        RealmResults<Realm_book> realm_books = realm.where(Realm_book.class)
                                .equalTo("bookname", bookInfo.getBookName()).findAll();
                        if(realm_books.size() <= 0){
                            return;
                        }
                        Realm_book realm_book = realm_books.get(0);
                        realm_book.chapterList = new RealmList<Chapter_book>();
                        for(int i = 0; i < 3; i++){
                            realm_book.chapterList.add(new Chapter_book());
                            String[] strings = arrayList.get(i);
                            String s = new String(chapterList.get(i).getName());
                            s = s + "\n" + "\n" + "\n";
                            for(int j = 0; j < strings.length; j++){
                                s = s + "  " + strings[j] + "\n";
                            }
                            s = s + "\n" + "\n" + "\n";
                            realm_book.chapterList.get(i).setChapterContent(s);
                        }
                    }
                });
                realm.close();
            }
        }).start();
    }

    public void InitMarkerList(){
        List<Marker_book> marker_books = book.bookmarkerList;
        markerList = new ArrayList<>();
        if(marker_books != null)
        for(int i = 0; i < marker_books.size(); i++){
            markerList.add(book.bookmarkerList.get(i).creatMarker());
        }
        else{
            for(int i = 0; i < 5; i++){
                markerList.add(new Marker("书签 " + i, "书签内容"));
            }
        }
    }
    //只能在showPopUoWindow_setting()中实例化对象，否则因为生存周期的问题没法响应
    /*public void InitSettingMenu(){
        View contentView = LayoutInflater.from(Book_show.this).inflate(R.layout.popupwindow_setting, null);
        lightBar = contentView.findViewById(R.id.light_bar);
        fontAdd = (TextView) contentView.findViewById(R.id.fontAdd);
        fontMinus =  contentView.findViewById(R.id.fontMinus);
        color_gray = (LinearLayout) contentView.findViewById(R.id.color_gray);
        color_graygreen= (LinearLayout) contentView.findViewById(R.id.color_graygreen);
        color_green = (LinearLayout)contentView.findViewById(R.id.color_green);
        color_yellow = (LinearLayout) contentView.findViewById(R.id.color_yellow);

        lightBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.w("tag", progress + " ");
                 /*Window window = getWindow();
                 WindowManager.LayoutParams layoutParams = window.getAttributes();
                 layoutParams.screenBrightness = progress / 255f;
                 window.setAttributes(layoutParams);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        fontAdd.setOnClickListener(this);
        fontMinus.setOnClickListener(this);
        color_yellow.setOnClickListener(this);
        color_green.setOnClickListener(this);
        color_graygreen.setOnClickListener(this);
        color_gray.setOnClickListener(this);
    }*/
    //当系统开启自动调节亮度时，获取的不准确
    public int getSystemLight(){
        Log.w("tag", light + "light");
        if(light == -1){
            try{
                light = Settings.System.getInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return light;
    }

    public void showPopUoWindow_chapter(){
        View parentView = LayoutInflater.from(Book_show.this).inflate(R.layout.activity_book_show, null);
        int width = parentView.getWidth();
        View contentView = LayoutInflater.from(Book_show.this).inflate(R.layout.popupwindow_chapter, null);
        mPopWindow = new PopupWindow(contentView, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.MATCH_PARENT, true);
        mPopWindow.setContentView(contentView);
        //点击外面消失
        //mPopWindow.setOutsideTouchable(true);
        //mPopWindow.setBackgroundDrawable(new BitmapDrawable());
        InitChapterList();
        InitMarkerList();
        ChapterListAdapter adapter = new ChapterListAdapter(chapterList, contentView.getContext());
        MarkerListAdapter markerListAdapter = new MarkerListAdapter(markerList, contentView.getContext());
        final ListView listView = contentView.findViewById(R.id.popupwindow_chapter);
        ListView markerView = contentView.findViewById(R.id.popupwindow_marker);
        final FrameLayout markerList = contentView.findViewById(R.id.markerlist);
        final FrameLayout catalogList = contentView.findViewById(R.id.cataloglist);
        final TextView catalogText = contentView.findViewById(R.id.catalog);
        catalogText.setTextColor(Color.GREEN);
        final TextView markerText = contentView.findViewById(R.id.marker);
        markerText.setTextColor(Color.GRAY);
        catalogText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                catalogList.setVisibility(View.VISIBLE);
                markerList.setVisibility(View.INVISIBLE);
                markerText.setTextColor(Color.GRAY);
                catalogText.setTextColor(Color.GREEN);
            }
        });
        markerText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                catalogList.setVisibility(View.INVISIBLE);
                markerList.setVisibility(View.VISIBLE);
                markerText.setTextColor(Color.GREEN);
                catalogText.setTextColor(Color.GRAY);
            }
        });
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //更新当前章节
                index = position + 1;
                //更新当前章节内容
                String s = book.chapterList.get(position).getChapterContent();
                if(s.length() == 0){
                   s = content1;
                }
                booktxt.setText(s);
                //更新last_height, pro_height
                pro_height = 0;
                last_height = 0;
            }
        });
        markerView.setAdapter(markerListAdapter);
        markerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Marker_book marker_book = book.bookmarkerList.get(position);
                loadFromMarker(marker_book);
            }
        });
        //显示PopupWindow
        View rootview = LayoutInflater.from(Book_show.this).inflate(R.layout.activity_book_show, null);
        mPopWindow.setTouchable(true);
        mPopWindow.showAtLocation(rootview, Gravity.LEFT, 0, 0);
    }
    public void showPopUoWindow_setting(){
        View contentView = LayoutInflater.from(Book_show.this).inflate(R.layout.popupwindow_setting, null);
        mPopWindow = new PopupWindow(contentView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT, true);
        mPopWindow.setContentView(contentView);
        mPopWindow.setTouchable(true);
        mPopWindow.setFocusable(true);
        //不能这样写
        //InitSettingMenu();
        fontSize = contentView.findViewById(R.id.fontSize);
        contentView.findViewById(R.id.fontAdd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int fontsize = getNumber(fontSize.getText().toString());
                fontsize++;
                Log.i("tag", fontsize + "size+");
                booktxt.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontsize);
                fontSize.setText(fontsize + "");
            }
        });
        contentView.findViewById(R.id.fontMinus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int fontsize = getNumber(fontSize.getText().toString());
                fontsize--;
                Log.i("tag", fontsize + "size+");
                booktxt.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontsize);
                fontSize.setText(fontsize + "");
            }
        });
        contentView.findViewById(R.id.color_yellow).setOnClickListener(this);
        contentView.findViewById(R.id.color_green).setOnClickListener(this);
        contentView.findViewById(R.id.color_gray).setOnClickListener(this);
        contentView.findViewById(R.id.color_graygreen).setOnClickListener(this);
        SeekBar lightBar = contentView.findViewById(R.id.light_bar);
        final int process = getSystemLight();
        System.out.println("process" + process);
        if(process != -1){
            lightBar.setProgress(process);
        }
        else lightBar.setProgress(100);
        //变量名写错了
        lightBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                 Log.w("tag", progress + "s ");
                 Window window = getWindow();
                 WindowManager.LayoutParams layoutParams = window.getAttributes();
                 light = progress;
                 Log.w("tag", "light" + light);
                 layoutParams.screenBrightness = progress / 255f;
                 window.setAttributes(layoutParams);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        //显示PopupWindow
        View rootview = LayoutInflater.from(Book_show.this).inflate(R.layout.activity_book_show, null);
        mPopWindow.showAtLocation(rootview, Gravity.BOTTOM, 0, 0);
    }
    public void showPopUpWindow_more(){
        View contentView = LayoutInflater.from(Book_show.this).inflate(R.layout.popupwindow_more, null);
        mPopWindow = new PopupWindow(contentView, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT, true);
        mPopWindow.setContentView(contentView);
        TextView resource = (TextView) contentView.findViewById(R.id.resource);
        TextView marker = (TextView) contentView.findViewById(R.id.marker);
        resource.setText("换源");
        marker.setText("书签");
        mPopWindow.setTouchable(true);
        mPopWindow.showAsDropDown(more);
    }
    public int getNumber(String s){
        System.out.println(s);
        int num = (s.charAt(0) - '0') * 10 + s.charAt(1) - '0';
        return num;
    }

    @Override
    public void onClick(View v){
        int id = v.getId();
        switch (id){
            /*case R.id.fontAdd :{
                //字体加
                int fontsize = getResources().getInteger(R.integer.font_size);
                fontsize++;
                Log.i("tag", fontsize + "size+");
                booktxt.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontsize);
                //fontSize.setText(fontsize);
                break;
            }
            case R.id.fontMinus :{
                //字体减
                int fontsize = getResources().getInteger(R.integer.font_size);
                fontsize--;
                Log.i("tag", fontsize + "size-");
                booktxt.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontsize);
                //fontSize.setText(fontsize);
                break;
            }*/
            case R.id.color_gray :{
                //背景
                layout_book.setBackgroundColor(getResources().getColor(R.color.gray));
                colorId = R.color.gray;
                Log.i("tag", "color1");
                break;
            }
            case R.id.color_graygreen :{
                layout_book.setBackgroundColor(getResources().getColor(R.color.graygreen));
                colorId = R.color.graygreen;
                Log.i("tag", "color2");
                break;
            }
            case R.id.color_green :{
                layout_book.setBackgroundColor(getResources().getColor(R.color.green));
                colorId = R.color.green;
                Log.i("tag", "color3");
                break;
            }
            case R.id.color_yellow :{
                layout_book.setBackgroundColor(getResources().getColor(R.color.yellow));
                colorId = R.color.yellow;
                Log.i("tag", "color4");
                break;
            }
            default:break;
        }
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        //Toast.makeText(Book_show.this, "touch", Toast.LENGTH_SHORT ).show();
        //Log.i("tag", "touch");
        Log.i("tag", "onTouch" + light);
        System.out.println(hight + " " + booktxt.getMeasuredHeight());
        return gestureDetector.onTouchEvent(event);
    }
    //将该activity上的触碰事件交给GestureDetector处理
    @Override
    public boolean onTouchEvent(MotionEvent me){
        //Toast.makeText(Book_show.this, "touch", Toast.LENGTH_SHORT ).show();
        //Log.i("tag", "touch");
        Log.w("tag","OnTouchEvent");
        System.out.println("onTouchEvent");
        return gestureDetector.onTouchEvent(me);
    }

    //用户按下屏幕就会触发
    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    //滑屏，用户按下触摸屏、快速移动后松开，由1个MotionEvent ACTION_DOWN, 多个ACTION_MOVE, 1个ACTION_UP触发
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        //Toast.makeText(Book_show.this, "fling", Toast.LENGTH_SHORT ).show();
        //向上滑动时
        if(e1.getY() - e2.getY() > 0 ){
            if(isBootm()){
                link = true;
                //下章内容
                String s = getNextChapter(index);
                if(last_height != 0){
                    int allheight = booktxt.getMeasuredHeight();
                    Log.e("tag", "allheight" + allheight);
                    final int moveheight = allheight - last_height;
                    last_height = moveheight;
                    //content1为当前章节尾
                    booktxt.setText(content1);
                    //content1为下章内容
                    booktxt.append(s);
                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            //当cooktxt未更新时
                            /*while (hight == booktxt.getMeasuredHeight()){
                                //SystemClock.sleep(100);
                            }*/
                            upstate = true;
                            scrollView.scrollTo(0, moveheight - scrollView.getMeasuredHeight());
                            Log.w("tag", "height =" + booktxt.getMeasuredHeight() + " " + moveheight);
                        }
                    };
                    new Thread(runnable).start();
                }
                else{
                    if(pro_height == 0)
                        last_height = booktxt.getMeasuredHeight();
                    else last_height = booktxt.getMeasuredHeight() - pro_height;
                    booktxt.append(content1);
                    upstate = true;
                }
            }
            //到底时
            /*Log.e("scroll", scrollView.getScrollY() + " " + last_height);
            if(last_height != 0 && scrollView.getScrollY() >= last_height && upstate){
                index++;
                upstate = false;
                chapterNow.setText(index + "");
            }*/
            /*if(isOver()){
                final int move_height = scrollView.getScrollY() - last_height;
                hight = booktxt.getMeasuredHeight();
                booktxt.setText(content1);
                Log.w("tag", "heightnow=" + hight);
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        //当cooktxt未更新时
                        while (hight == booktxt.getMeasuredHeight()){
                            //SystemClock.sleep(100);
                        }
                        scrollView.scrollTo(0, move_height);
                        Log.w("tag", "height =" + booktxt.getMeasuredHeight() + " " + move_height  );
                    }
                };
                new Thread(runnable).start();
            }*/
        }
        else{
            if(isTop()){
                link = false;
                String s = getLastChapter(index);
                if(pro_height != 0){
                    //原textview高度
                    int allheight = booktxt.getMeasuredHeight();
                    //CharSequence s = booktxt.getText();
                    //content1为上章内容
                    booktxt.setText(s);
                    //content1为当前内容
                    booktxt.append(content1);
                    //设置了新内容但是不会立即绘制出来，此时下面的hight认为原高度
                    final int moveheight = allheight - pro_height;
                    pro_height = moveheight;
                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            //当cooktxt未更新时
                            last_height = booktxt.getMeasuredHeight() - pro_height;
                            Log.e("movw", "last_height"+ last_height + "pro_height" + pro_height + "all" + booktxt.getMeasuredHeight());
                            scrollView.scrollTo(0, booktxt.getMeasuredHeight() - pro_height);
                            downstate = true;
                        }
                    };
                    new Thread(runnable).start();
                }
                else{
                    link = false;
                    if(last_height == 0){
                        pro_height = booktxt.getMeasuredHeight();
                    }
                    else pro_height = booktxt.getMeasuredHeight() - last_height;
                    //上涨内容
                    booktxt.setText(content1);
                    //content1为当前内容
                    booktxt.append(content1);
                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            //当cooktxt未更新时
                            while (pro_height == booktxt.getMeasuredHeight()){
                            }
                            last_height = booktxt.getMeasuredHeight() - pro_height;
                            Log.e("movw", "last_height"+ last_height + "pro_height" + pro_height + "all" + booktxt.getMeasuredHeight());
                            scrollView.scrollTo(0, last_height);
                            Log.e("movw", scrollView.getScrollY() + "");
                            downstate = true;
                        }
                    };
                    new Thread(runnable).start();
                }
            }
            //到顶时
            /*if(pro_height != 0 && downstate && scrollView.getScrollY() < booktxt.getMeasuredHeight() - pro_height - scrollView.getHeight()){
                Log.e("movw", pro_height + " proheight" + scrollView.getScrollY() + "scrollmove" + booktxt.getMeasuredHeight() + "h");
                index--;
                downstate = false;
                chapterNow.setText(index + "");
            }*/
        }
        if(last_height != 0 && scrollView.getScrollY() >= last_height && link){
            index++;
            chapterNow.setText(index + "");
            link = false;
        }
        if(last_height != 0 && scrollView.getScrollY() < last_height && !link){
            index--;
            chapterNow.setText(index + "");
            link = true;
        }
        return true;
    }
    //长按触摸屏，超过一定时长，就会触发这个事件
    @Override
    public void onLongPress(MotionEvent e) {
        //Toast.makeText(Book_show.this, "longPress", Toast.LENGTH_SHORT ).show();
        Log.e("touch", "long");
        if(isVisible){
            turnoffMenu();
        }

    }
    //在屏幕上拖动事件。无论是用手拖动view，或者是以抛的动作滚动，都会多次触发,这个方法在ACTION_MOVE动作发生时就会触发
    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
                            float distanceY) {
        //Toast.makeText(Book_show.this, "scrool", Toast.LENGTH_SHORT ).show();
        // TODO Auto-generated method stub
        //向上滑动时
        return false;
    }
    //如果按下的时间超过瞬间，而且在按下的时候没有松开或者是拖动的
    @Override
    public void onShowPress(MotionEvent e) {
        //Toast.makeText(Book_show.this, "press", Toast.LENGTH_SHORT ).show();
        Log.e("touch", "show");
        return;
    }
    //一次单独的轻击抬起操作,也就是轻击一下屏幕，立刻抬起来，才会有这个触发
    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        //Toast.makeText(Book_show.this, "singgle", Toast.LENGTH_SHORT ).show();
        //book_menu.setVisibility(View.VISIBLE);
        //booktxt.setVisibility(View.INVISIBLE);
        Log.e("touch", "sign");
        if(!isVisible){
            turnonMenu();
        }
        else{
            turnoffMenu();
        }
        //isBootm();
        return true;
    }
    public void turnoffMenu(){
        layout_menu.setVisibility(View.INVISIBLE);
        isVisible = false;
    }

    public void turnonMenu(){
        layout_menu.setVisibility(View.VISIBLE);
        isVisible = true;
    }
    public boolean isBootm(){
        int height =  booktxt.getMeasuredHeight();
        int length = scrollView.getScrollY();
        int height1 = booktxt.getHeight();
        int height2 = scrollView.getHeight();
        Log.i("bootm", "in");
        //滑动到底部
        if(booktxt.getMeasuredHeight() <= scrollView.getScrollY() + scrollView.getHeight()){

            Log.i("tag", "bootm" + atBootm);
            atBootm = true;
            return true;
        }
        return false;
    }
    //加载阅读状态
    public String loadFile(){
        if(realm_books.size() > 0){
            Log.e("realm", "yes");
            book = realm_books.get(0);
        }
        index = book.getNowChapterIndex();
        fontsize = book.getFontSize();
        colorId = book.getColorId();
        shouldhavenext = book.getShould_haveNext();
        moveheight = book.getMoveHeight();
        /*new Thread(new Runnable() {
            @Override
            public void run() {
                chapterList = getChapterList();
            }
        }).start();*/
        //设置两章内容
        String s = new String();
        if(book.chapterList.size() != 0)
            s = s + book.chapterList.get(0).getChapterContent();
        else return content1;
        //需要先判断有没有该章节, 从0开始，所以index没加1
        if(shouldhavenext ){
            last_height = book.getLast_height();
            pro_height = book.getPro_height();
            s = s + content1;

            /*if(book.haveTheChapter(index)){
                booktxt.append(book.chapterList.get(1).getChapterContent());
            }*/
        }
        return s;
    }
    //保存阅读状态
    public void saveFile(){
        if(realm_books.size() > 0){
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    //先查找后得到User对象
                    Realm_book realm_book = realm_books.get(0);
                    realm_book.setColorId(colorId);
                    realm_book.setFontSize(fontsize);
                    realm_book.setMoveHeight(scrollView.getScrollY());
                    realm_book.setIndex(index);
                    realm_book.setShould_havenext(shouldhavenext);
                    realm_book.setLast_height(last_height);
                    realm_book.setPro_height(pro_height);
                    if(updateChapterList()){
                        chapterList = getChapterList();
                        for(int i = book.chapterList.size(); i < chapterList.size(); i++){
                            book.chapterList.add(new Chapter_book(i + 1, chapterList.get(i).getName()));
                        }
                    }


                }
            });
        }
    }
    //从书签开始
    public void loadFromMarker(Marker_book marker_book){
        index = marker_book.getChapterIndex();
        chapterNow_name = marker_book.getChapterName();
        moveheight = marker_book.getMoveHeight();
        //pro_height = marker_book.getPro_height();
        //last_height = marker_book.getLast_height();
        shouldhavenext = book.getShould_haveNext();
        //设置两章内容
        new Thread(new Runnable() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        booktxt.setText(content1);
                        //需要先判断有没有该章节, 从0开始，所以index没加1
                        if(shouldhavenext){
                            last_height = book.getLast_height();
                            pro_height = book.getPro_height();
                            if(book.haveTheChapter(index)){
                                booktxt.append(content1);
                            }
                            scrollView.scrollTo(0, moveheight);
                        }
                    }
                });
            }
        }).start();
    }

    //判断到顶
    public boolean isTop(){
        if(scrollView.getScrollY() == 0) {
            Log.w("tag", "Top");
            return true;
        }
        return false;
    }
    //夜间模式
    public void nightMode(){
        layout_book.setBackgroundColor(getResources().getColor(R.color.black));
        booktxt.setTextColor(getResources().getColor(R.color.white));
    }
    //关闭夜间模式
    public void turnOff_nightMode(){
        layout_book.setBackgroundColor(getResources().getColor(colorId));
        booktxt.setTextColor(getResources().getColor(R.color.white));
    }

    public void addMarker(){

    }
    public int getMoveHeight(){
        int scrollY = scrollView.getScrollY();
        //当前章节在pro里
        if(scrollY > last_height){
            return scrollY - last_height;
        }
        //当前章节在last里
        else if(scrollY + scrollView.getHeight() < last_height){
            return scrollY;
        }
        //一个尴尬的位置
        else{
            shouldhavenext = true;
            return scrollY;
        }

    }
    public String getNextChapter(int i){
        String s = content1;
        if(book.chapterList.get(i).getChapterContent().length() != 0)
            return book.chapterList.get(i).getChapterContent();
        else{
            //download

            //存储章节内容
        }
        return s;
    }
    public boolean updateChapterList(){
        //获取最新的章节
        if(book.chapterList.size() != getChapterList().size())
            return true;
        else return false;

    }
    public List<Chapter> getChapterList(){
        //返回最新章节列表
        return null;
    }

    public String getLastChapter(int i){
        String s = content1;
        if(i >= 2){
            if(book.chapterList.get(i - 2).getChapterContent().length() != 0){
                return book.chapterList.get(i - 2).getChapterContent();
            }
            else{
                //download

                //save
            }
        }
        return s;
    }
    public void downLoad(){
        //存储全本内容
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        realm.close();
    }

}
