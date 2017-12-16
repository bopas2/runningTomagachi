package com.example.langt.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.File;
import java.util.Scanner;
import java.io.PrintWriter;

public class MainActivity extends AppCompatActivity {

    @Override
    int gold;
    int mood;
    int level;
    int xp;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startUp();
    }

    public static void startUp() {
        Scanner fileReader = new Scanner(new File("data.txt"));
        gold = fileReader.nextInt();
        mood = fileReader.nextInt();
        level = fileReader.nextInt();
        xp = fileReader.nextInt();

    }
    public static void buyFood() {
        if(gold > 50) {
            adjustMood(50);
        }
    }
    public static void adjustMood(int addedMood) {
        updateMoodBar;
    }
}
