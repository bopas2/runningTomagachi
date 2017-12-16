package com.example.langt.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;
import java.io.PrintWriter;

public class MainActivity extends AppCompatActivity {


    int gold;
    int mood;
    int level;
    int xp;
    Button foodbtn;
    InputStream fin;
    String data = "data.txt";
    ImageView neutral;
    ImageView sad;
    ImageView happy;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startUp();
        foodbtn = findViewById(R.id.button);
        neutral = findViewById(R.id.Neutral);
        happy = findViewById(R.id.Happy);
        sad = findViewById(R.id.Sad);
        if(mood > 20 && mood < 80)
            neutral.setVisibility(View.VISIBLE);
        else if(mood >= 80)
            happy.setVisibility(View.VISIBLE);
        else if(mood <= 20)
            sad.setVisibility(View.VISIBLE);

    }


    private TextView textOut; //used for adjusting the textbox output

    //exports data to the text file
    public void exportData() {
        //gold-mood-level-xp
        String data = gold + "-" + mood + "-" + level + "-" + "xp"; //How data is formatted, useful for reading later
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
            }
            else {
                while(fis.read(dataArray) != -1) {
                    collected = new String(dataArray); //gets all the bytes into one string
                }
                //at this point collected == 'gold-mood-level-xp' format, we break it up and set our instance variables to their values
                String[] parts = collected.split("-");
                gold = Integer.parseInt(parts[0]);
                mood = Integer.parseInt(parts[1]);
                level = Integer.parseInt(parts[2]);
                xp = Integer.parseInt(parts[3]);
            }

        } catch (FileNotFoundException e) { //file not found so we set base values to the instance variables
            e.printStackTrace();
            gold = 0;
            mood = 100;
            level = 0;
            xp = 0;
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

        textOut = (TextView) findViewById(R.id.textView2);
        textOut.setText(gold + "/" + mood + "/" + level + "/" + xp); //currently being used to test what appears and what doesn't
    }
    public static void buyFood()
    {

    }

}
