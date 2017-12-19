package com.example.langt.myapplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Frag1.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Frag1#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Frag1 extends Fragment implements GoldListener{
    int gold = 0;
    int mood;
    int level;
    int stepCount = 0;
    int counter = 1;
    long lastTime;
    long currTime;
    int dayStep = 0;
    String data;
    int first = 1;
    Timer ani;
    int stepGoal = 10000;

    ImageView food;
    ImageButton kitty;
    TextView text;
    TextView textSteps;
    TextView textLvl;
    ProgressBar pp;
    TextView stats;
    TextView bubble;
    ToggleButton onoff;
    Calendar date;
    EditText goal;
    View view;
    Button purchase;
    AppCompatActivity main;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Frag1() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Frag1.
     */
    // TODO: Rename and change types and number of parameters
    public static Frag1 newInstance(String param1, String param2) {
        Frag1 fragment = new Frag1();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        main = (MainActivity)getActivity();
        view = inflater.inflate(R.layout.fragment_frag1, container, false);
        YourService.Register(this);
        main.startService(new Intent(main, YourService.class));
        text = view.findViewById(R.id.goldDisplay);
        textLvl = view.findViewById(R.id.textView2);
        textSteps = view.findViewById(R.id.textView3);
        startUp();
        ani = new Timer();
        kitty = view.findViewById(R.id.cat);
        kitty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pet(view);
            }
        });
        purchase = view.findViewById(R.id.button5);
        purchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buyFood(view);
            }
        });
        whichCat();
        updateProgress();
        food = view.findViewById(R.id.food);
        goal = view.findViewById(R.id.goalsetter);
        bubble = view.findViewById(R.id.opening);
        goal.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE){
                    stepGoal = Integer.parseInt(textView.getText().toString());
                    goal.setVisibility(View.GONE);
                    bubble.setVisibility(View.GONE);
                }
                return false;
            }
        });
        date = Calendar.getInstance(java.util.TimeZone.getTimeZone("America/New_York"));


        RelativeLayout layout = (RelativeLayout)view.findViewById(R.id.relativeLayout);

        AnimationDrawable anim = (AnimationDrawable) layout.getBackground();
        anim.start();
        //food.setBackgroundTintList(ColorStateList.valueOf(transparent))

        setMood();
        if(first == 1)
        {
            goal.setVisibility(View.VISIBLE);
            bubble.setVisibility(View.VISIBLE);
            first = 0;
        }
        return view;
    }

    //exports data to the text file
    public void exportData() {
        //gold-mood-level-step
        data = gold + "@" + mood + "@" + level + "@" + stepCount + "@" + dayStep + "@" + lastTime + "@" + first; //How data is formatted, useful for reading later
        try {
            FileOutputStream fOut = main.openFileOutput("data.txt", Context.MODE_PRIVATE); //open stream to file "data"
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
            fis = main.openFileInput("data.txt"); //instantiate the stream that lets us read the file
            byte[] dataArray = new byte[fis.available()]; //creates an array that's size is the size of the data (bytes)
            if(dataArray.length == 0) { //if there is no data, we set all values to base values
                gold = 0;
                mood = 50;
                level = 0;
                stepCount = 0;
                dayStep = 0;
                lastTime = 0;
                first = 1;
            }
            else {
                while(fis.read(dataArray) != -1) {
                    collected = new String(dataArray); //gets all the bytes into one string
                }
                //at this point collected == 'gold-mood-level-xp' format, we break it up and set our instance variables to their values
                String[] parts = collected.split("@"); //gold-mood-level-step

                gold = Integer.parseInt(parts[0]);
                mood = Integer.parseInt(parts[1]);
                level = Integer.parseInt(parts[2]);
                stepCount = Integer.parseInt(parts[3]);
                dayStep = Integer.parseInt(parts[4]);
                lastTime = Long.parseLong(parts[5]);
                first = Integer.parseInt(parts[6]);
            }

        } catch (FileNotFoundException e) { //file not found so we set base values to the instance variables
            e.printStackTrace();
            gold = 0;
            mood = 50;
            level = 0;
            stepCount = 0;
            dayStep = 0;
            lastTime = 0;
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
        //System.out.println(mood);
        updateGoldDisplay();
        updateLvlDisplay();
        updateStepDisplay();
    }
    public void updateGoldDisplay() {
        text.setText("Gold: " + gold);
    }
    public void updateStepDisplay() {
        textSteps.setText("Total Steps: " + stepCount);
    }
    public int updateLvlDisplay() {
        return (int)((Math.pow(stepCount,.98))/100);
    }
    public void buyFood(View v) {
        if(gold >= 10) {
            gold -= 10;
            updateGoldDisplay();
            food.setImageResource(R.drawable.bowl);
            food.setVisibility(View.VISIBLE);
            ani.schedule(new TimerTask() {
                @Override
                public void run() {
                    main.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            food.setImageResource(R.drawable.halffood);
                        }
                    });
                }
            }, 1000);
            ani.schedule(new TimerTask() {
                @Override
                public void run() {
                    main.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            food.setImageResource(R.drawable.emptyfood);
                        }
                    });
                }
            }, 2000);
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

    public void showStats(View v)
    {
        onoff = view.findViewById(R.id.statsButton);
        stats = view.findViewById(R.id.statsWindow);
        updateLvlDisplay();
        stats.setText("Try to get to " + stepGoal + " steps!\nLevel: " + updateLvlDisplay() + "\nSteps today: " + dayStep + "\n Total steps walked: " + stepCount + "\nGold: " + gold + "\n\n\n\ntap to exit");


        if(counter == 1) {
            stats.setVisibility(View.VISIBLE);
            counter--;
        }

        else if(counter == 0)
        {
            stats.setVisibility(View.GONE);
            counter++;
        }

    }

    public void updateProgress() {
        pp = (ProgressBar)(view.findViewById(R.id.progressBar));
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
                main.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adjustmood(-1);
                        exportData();
                        updateProgress();
                        whichCat();
                    }
                });
            }
        }, 0, 10000);
    }
    @Override
    public void goldMail() {
        gold++;
        exportData();
        updateGoldDisplay();

    }

    @Override
    public void allSteps() {
        stepCount++;
        updateLvlDisplay();
        updateStepDisplay();
        if(dayStep == 0)
            lastTime = currTime = date.getTimeInMillis();
        else
        {
            currTime = date.getTimeInMillis();
            if(currTime < lastTime)
                dayStep = 0;
        }
        dayStep++;
        lastTime = currTime;
        exportData();

        if(dayStep == stepGoal)
        {
            gold += 100;
            Toast.makeText(main.getApplicationContext(), "Goal reached! Plus 100,000 gold!", Toast.LENGTH_SHORT).show();
        }
    }
    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
