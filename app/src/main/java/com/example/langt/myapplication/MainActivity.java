package com.example.langt.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.util.Scanner;
import java.io.PrintWriter;

public class MainActivity extends AppCompatActivity {


    int gold;
    int mood;
    int level;
    int xp;
    Button foodbtn;

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

        gold = fileReader.nextInt();
        mood = fileReader.nextInt();
        level = fileReader.nextInt();
        xp = fileReader.nextInt();

    }
    public static void buyFood()
    {

    }

}
