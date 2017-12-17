package com.example.langt.myapplication;
import android.content.Context;
import android.hardware.*;
import java.lang.*;
/**
 * Created by Alex on 12/16/2017.
 */
public class Accelerameter implements SensorEventListener{
    private SensorManager sensorManager;
    StepListener listener;
    public Accelerameter(Context context, StepListener listener){
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        this.listener = listener;
    }
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        listener.step();
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
    public void SetUp(){
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER), SensorManager.SENSOR_DELAY_FASTEST);
    }
    public void Destroy(){
        sensorManager.unregisterListener(this);
    }
}
