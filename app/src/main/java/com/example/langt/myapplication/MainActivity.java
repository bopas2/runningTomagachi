package com.example.langt.myapplication;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import android.os.Handler;


public class MainActivity extends AppCompatActivity implements GoldListener{
    int gold;
    int mood;
    int level;
    int xp;
    int stepCount = 0;
    static int allSteps;
    Calendar date = Calendar.getInstance();
    /*ImageView neutral;
    ImageView sad;
    ImageView happy;
    ImageView dead;*/


    ImageView food;
    ImageButton kitty;
    TextView text;
    ProgressBar pp;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        YourService.Register(this);
        startService(new Intent(this, YourService.class));
        text = findViewById(R.id.goldDisplay);
        startUp();
        kitty = findViewById(R.id.cat);
        whichCat();
        updateProgress();
        food = findViewById(R.id.food);
        setMood();
    }

    //exports data to the text file
    public void exportData() {
        //gold-mood-level-xp
        String data = gold + "@" + mood + "@" + level + "@" + xp + "@" + stepCount; //How data is formatted, useful for reading later
        try {
            FileOutputStream fOut = openFileOutput("data.txt", Context.MODE_PRIVATE); //open stream to file "data"
            fOut.write(data.getBytes());    //write the string 'data' to data.txt (must convert string to bytes)
            fOut.close();   //close dat boi!
        }
        catch (Exception e) {
            e.printStackTrace(); //one day i will understand why we need a try catch, but that day is not today
        }
    }
    //reads information from text file and sets variables to the data
    public void startUp() {
        String collected = ""; //will later equal the entire data
        FileInputStream fis = null; //will declare later
        try {
            fis = openFileInput("data.txt"); //instantiate the stream that lets us read the file
            byte[] dataArray = new byte[fis.available()]; //creates an array that's size is the size of the data (bytes)
            if(dataArray.length == 0) { //if there is no data, we set all values to base values
                gold = 0;
                mood = 20;
                level = 0;
                xp = 0;
                stepCount = 0;
            }
            else {
                while(fis.read(dataArray) != -1) {
                    collected = new String(dataArray); //gets all the bytes into one string
                }
                //at this point collected == 'gold-mood-level-xp' format, we break it up and set our instance variables to their values
                String[] parts = collected.split("@");
                System.out.println(collected);
                gold = Integer.parseInt(parts[0]);
                mood = Integer.parseInt(parts[1]);
                level = Integer.parseInt(parts[2]);
                xp = Integer.parseInt(parts[3]);
                stepCount = Integer.parseInt(parts[4]);
            }

        } catch (FileNotFoundException e) { //file not found so we set base values to the instance variables
            e.printStackTrace();
            gold = 0;
            mood = 20;
            level = 0;
            xp = 0;
            stepCount = 0;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(fis != null) {
                    fis.close();
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println(mood);
        updateGoldDisplay();
    }
    public void updateGoldDisplay() {
        text.setText("Gold: " + gold);
    }

    public void buyFood(View v) {
        if(gold >= 10) {
            gold -= 10;
            updateGoldDisplay();
            food.setVisibility(View.VISIBLE);
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    food.setVisibility(View.INVISIBLE);
                }
            }, 1000);
            adjustmood(5);
        }
        exportData();
    }

    public void adjustmood(int a) {
        mood += a;
        if(mood > 100)
            mood = 100;
        if(mood < 0)
            mood = 0;
        exportData();
        whichCat();
        updateProgress();
    }
    public void updateProgress() {
        pp = (ProgressBar)(findViewById(R.id.progressBar));
        pp.setScaleY(3f);
        pp.setProgress(mood,true);
    }
    public void whichCat() {
        if(mood > 20 && mood < 80)
            kitty.setImageResource(R.drawable.neutral);
        else if(mood >= 80 && mood <= 100)
            kitty.setImageResource(R.drawable.happy);
        else if (mood < 1)
            kitty.setImageResource(R.drawable.deadcat);
        else if(mood <= 20)
            kitty.setImageResource(R.drawable.sad);
    }

    public void pet(View v)
    {
        kitty.setImageResource(R.drawable.pet);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                whichCat();
            }
        }, 1000);
    }

    public void setMood()
    {
        Timer time = new Timer();
        time.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adjustmood(-1);
                        exportData();
                        updateProgress();
                        whichCat();
                    }
                });
            }
        }, 0, 1000);
    }
    @Override
    public void goldMail() {
        gold++;
        exportData();
        updateGoldDisplay();
    }

    public void allSteps(){
        allSteps++;
        exportData();
    }
}
