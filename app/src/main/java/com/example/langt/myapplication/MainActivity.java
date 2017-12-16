package com.example.langt.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.*;
import java.util.stream.Stream;

public class MainActivity extends AppCompatActivity {


    int gold;
    int mood;
    int level;
    int xp;
    Button foodbtn;
    InputStream fin;
    String data = "data.txt";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startUp();
        foodbtn = findViewById(R.id.button);

    }

    public void onDraw()
    {
        if(gold < 1)
            foodbtn.setVisibility(View.GONE);

    }

    public void startUp() {
        File file = new File(getApplicationContext().getFilesDir() + File.separator +  data);
        if (!file.exists()) {
            file.mkdir();
        }
        fin = new FileInputStream(data);
        InputStreamReader isr = new InputStreamReader(fin);
    }
    public static void buyFood()
    {

    }

}
