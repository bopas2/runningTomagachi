package com.example.langt.myapplication;

import android.app.Service;
import android.content.Intent;
import android.icu.util.Calendar;
import android.icu.util.TimeZone;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by Lpars on 12/16/2017.
 */
//Pedometer
public class YourService extends Service implements StepListener{

    TimeZone tz = TimeZone.getTimeZone("EST");
    Calendar reset = Calendar.getInstance(tz);
    long lastTime;
    long currTime;
    static int stepCount = 0;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // do your jobs here
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void step() {
        if(stepCount ==0)
            lastTime = currTime = reset.getTimeInMillis();
        else
        {
            currTime = reset.getTimeInMillis();
            if(currTime < lastTime)
                stepCount = 0;
        }
        stepCount++;
        lastTime = currTime;
        if(stepCount % 100 == 0)
            MainActivity.addGold();
    }

    public static int getSteps()
    {
        return stepCount;
    }
}
