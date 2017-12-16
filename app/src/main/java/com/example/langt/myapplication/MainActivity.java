package com.example.langt.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    int gold;
    int mood;
    int level;
    int xp;
    int dayOfYear;
    int lastDay;
    Calendar date = Calendar.getInstance();
    ImageView neutral;
    ImageView sad;
    ImageView happy;
    ImageView dead;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gold = 500; mood = 10; dayOfYear = 0;
        exportData();
        startUp();
        whichCat();
//        setMood();
    }

    //exports data to the text file
    public void exportData() {
        //gold-mood-level-xp
        String data = gold + "-" + mood + "-" + level + "-" + xp + "-" + dayOfYear; //How data is formatted, useful for reading later
        try {
            FileOutputStream fOut = openFileOutput("data", MODE_WORLD_READABLE); //open stream to file "data"
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
            fis = openFileInput("data"); //instantiate the stream that lets us read the file
            byte[] dataArray = new byte[fis.available()]; //creates an array that's size is the size of the data (bytes)
            if(dataArray.length == 0) { //if there is no data, we set all values to base values
                gold = 0;
                mood = 100;
                level = 0;
                xp = 0;
                dayOfYear = date.get(Calendar.DAY_OF_YEAR);
                lastDay = dayOfYear;
            }
            else {
                while(fis.read(dataArray) != -1) {
                    collected = new String(dataArray); //gets all the bytes into one string
                }
                //at this point collected == 'gold-mood-level-xp' format, we break it up and set our instance variables to their values
                String[] parts = collected.split("-");
                System.out.println(collected);
                gold = Integer.parseInt(parts[0]);
                mood = Integer.parseInt(parts[1]);
                level = Integer.parseInt(parts[2]);
                xp = Integer.parseInt(parts[3]);
                lastDay = Integer.parseInt(parts[4]);
            }

        } catch (FileNotFoundException e) { //file not found so we set base values to the instance variables
            e.printStackTrace();
            gold = 0;
            mood = 100;
            level = 0;
            xp = 0;
            dayOfYear = date.get(Calendar.DAY_OF_YEAR);
            lastDay = dayOfYear;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fis.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println(mood);
        updateGoldDisplay();
    }
    private TextView textOut;
    public void updateGoldDisplay() {
        textOut = (TextView) findViewById(R.id.goldDisplay);
        textOut.setText("Gold: " + gold + "");
    }

    public void buyFood(View v) {
        if(gold >= 50) {
            gold -= 50;
            updateGoldDisplay();
            adjustmood(10);
        }
    }
    public void adjustmood(int a) {
        mood += a;
        System.out.println(mood);
        whichCat();
    }
    public void whichCat() {
        neutral = findViewById(R.id.Neutral); neutral.setVisibility(View.INVISIBLE);
        happy = findViewById(R.id.Happy); happy.setVisibility(View.INVISIBLE);
        sad = findViewById(R.id.Sad); sad.setVisibility(View.INVISIBLE);
        dead = findViewById(R.id.dead); dead.setVisibility(View.INVISIBLE);
        if(mood > 20 && mood < 80)
            neutral.setVisibility(View.VISIBLE);
        else if(mood >= 80)
            happy.setVisibility(View.VISIBLE);
        else if (mood < 1)
            dead.setVisibility(View.VISIBLE);
        else if(mood <= 20)
            sad.setVisibility(View.VISIBLE);
    }

    public void setMood()
    {
        if(dayOfYear == 0)
            lastDay = Calendar.DAY_OF_YEAR;
        else {
            dayOfYear = date.get(Calendar.DAY_OF_YEAR);
            if (dayOfYear - lastDay > 0)
                adjustmood(-(dayOfYear - lastDay) * 5);
            else if (dayOfYear < lastDay)
                adjustmood(-((365 - lastDay) + dayOfYear) * 5);
        }
    }

}
