package com.example.boyzhang.bookreading;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class TextViewTest extends AppCompatActivity {

    TextView textView;
    String s = new String("这是一个测试\n，测试，这是一个测试\n，测试，这是一个测试\n，测试，");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_view_test);
        textView = findViewById(R.id.textTest);
        //textView.setText(s);

    }
}
