package com.findphone.whistleclapfinder.clapsClasess;

import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.RequiresApi;

public class ClapsDetectionService extends Service implements
        ClapsDetectorThread.OnWhistleListener {

    Handler handler;
    private ClapsDetectorThread clapsDetectorThread;
//DetectorThread detectorThread = new DetectorThread();
//detectorThread.setContext(this);
    private ClapsRecorderThread recorderThread;

    @Override
    public void onCreate() {
        super.onCreate();
        handler = new Handler();
        Log.d("checksound", "onCreate()");


    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.d("checksound11111", "onStartCommand  Claps");

        try {

            Log.d("checksound1", "try {");

            if (intent != null && intent.getExtras() != null) {
                String receivedData = intent.getStringExtra("action");
                if (receivedData != null) {

                    if (receivedData.equals("Start")){

                        startWhistleDetection();
                        Log.d("checksound11111", "Claps   "+receivedData+" $receivedData");

                        // Set the flag to start as a foreground service

                        Intent serviceIntent = new Intent(this, ClapsForegroundService.class);
                            startForegroundService(serviceIntent);


                    }else if (receivedData.equals("Stop")){
                        // Now you have the received String data and can use it in your service
                        // For example, you can log it
                        stopWhistleDetection();
                        stopSelf();
                        Log.d("checksound11111", "Claps   "+receivedData+" $receivedData");
                    }


//                    // Now you have the received String data and can use it in your service
//                    // For example, you can log it
//                    stopWhistleDetection();
//                    stopSelf();
//                    Log.d("checksound11111", "Received data: $receivedData");
                }
            } else {

//                startWhistleDetection();
                Log.d("checksound11111", "intent is null OR intent.getExtras() is null");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("checksound", "Exception  "+e.getMessage());
        }


        return super.onStartCommand(intent, flags, startId);
    }




    private void startWhistleDetection() {

        try {
            stopWhistleDetection();
            Log.d("checksound11111", "stopWhistleDetection()");
        } catch (Exception e) {
            Log.d("checksound11111", "stopWhistleDetection()  "+e.getMessage()+"  Exception ");
            e.printStackTrace();
        }

        recorderThread = new ClapsRecorderThread();
        recorderThread.startRecording();
        clapsDetectorThread = new ClapsDetectorThread(recorderThread,this);
        clapsDetectorThread.setOnWhistleListener(this);
        clapsDetectorThread.start();

    }

    private void stopWhistleDetection() {
        if (clapsDetectorThread != null) {
            clapsDetectorThread.stopDetection();
            clapsDetectorThread.setOnWhistleListener(null);
            clapsDetectorThread = null;
        }

        if (recorderThread != null) {
            recorderThread.stopRecording();
            recorderThread = null;
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }



    @Override
    public void onWhistle() {
        Log.d("checksound", "onWhistle()");
    }
    }
