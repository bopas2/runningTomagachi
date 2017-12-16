package com.example.langt.myapplication;

import android.content.Context;
import android.hardware.*;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.lang.*;
/**
 * Created by Alex on 12/16/2017.
 */

public class Accelerameter implements SensorEventListener{
    SensorManager sensorManager;
    float x,y,z;
    public void Accelerameter(Context context){
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        x = sensorEvent.values[0];
        y = sensorEvent.values[1];
        z = sensorEvent.values[2];
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public void SetUp(){
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
    }
    public void Destroy(){
        sensorManager.unregisterListener(this);
    }
    public float[] Retrieve(){
        return new float[] {x,y,z};
    }
}
