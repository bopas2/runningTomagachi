package com.example.langt.myapplication;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by Lpars on 12/16/2017.
 */
//Pedometer
public class YourService extends Service implements StepListener{
    int stepCount = 0;
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
        stepCount++;
    }
}
