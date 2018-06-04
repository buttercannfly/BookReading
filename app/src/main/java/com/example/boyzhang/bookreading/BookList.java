package com.example.boyzhang.bookreading;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.example.boyzhang.bookreading.Adapter.GenreAdapter;
import com.example.boyzhang.bookreading.overlay.Genre;

import java.util.ArrayList;
import java.util.List;

public class BookList extends AppCompatActivity {
    Button button1eft;
    Button buttonright;
    ListView catView;
    ListView boardView;
    List<Genre> genreList = new ArrayList<>();
    List<Genre> genreList2 = new ArrayList<>();
    String[] catName = new String[]{
            new String("玄幻"),
            new String("历史"),
            new String("武侠")
    };
    String[] boardName = new String[]{
            new String("人气榜"),
            new String("新书榜"),
            new String("完结榜")
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);
        button1eft = findViewById(R.id.board);
        buttonright = findViewById(R.id.cat);
        boardView = findViewById(R.id.boardlist);
        catView = findViewById(R.id.catlist);
        for(int i = 0; i < 3; i++){
            Genre genre = new Genre();
            genre.name = boardName[i];
            genreList.add(genre);
        }
        for(int i = 0; i < 3; i++){
            Genre genre = new Genre();
            genre.name = catName[i];
            genreList2.add(genre);
        }
        GenreAdapter adapter = new GenreAdapter(genreList, this.getApplicationContext());
        GenreAdapter adapter1 = new GenreAdapter(genreList2, this.getApplicationContext());
        boardView.setAdapter(adapter);
        catView.setAdapter(adapter1);
        buttonright.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                catView.setVisibility(View.INVISIBLE);
                boardView.setVisibility(View.VISIBLE);
            }
        });
        button1eft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                catView.setVisibility(View.VISIBLE);
                boardView.setVisibility(View.INVISIBLE);
            }
        });
        boardView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(BookList.this, BookListShow.class);
                startActivity(intent);
            }
        });

    }
}
