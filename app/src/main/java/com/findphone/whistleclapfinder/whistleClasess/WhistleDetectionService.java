package com.findphone.whistleclapfinder.whistleClasess;

import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.findphone.whistleclapfinder.R;

public class WhistleDetectionService extends Service implements WhistleDetectorThread.OnWhistleListener {

    private static final int ONGOING_NOTIFICATION_ID = 1;
    private static final String CHANNEL_ID = "com.findphone.whistleclapfinder.whistleDetection";

    Handler handler;
    private WhistleDetectorThread whistleDetectorThread;
    private WhistleRecorderThread recorderThread;

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
        Log.d("checksound1", "onStartCommand");

        try {
            if (intent != null && intent.getExtras() != null) {
                String receivedData = intent.getStringExtra("action");
                if (receivedData != null) {
                    if (receivedData.equals("Start")) {
                        startWhistleDetection();
                        Log.d("checksound11111", "Whistle   "+receivedData + " $receivedData");



                        // Set the flag to start as a foreground service

                        Intent serviceIntent = new Intent(this, WhistleForegroundService.class);
                        startForegroundService(serviceIntent);

                    } else if (receivedData.equals("Stop")) {

                        stopWhistleDetection();
                        stopSelf();
                        Log.d("checksound11111", "Whistle   "+receivedData + " $receivedData");


                    }
                }
            } else {
                Log.d("checksound11111", "intent is null OR intent.getExtras() is null");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("checksound", "Exception " + e.getMessage());
        }




////        // Create a notification for the foreground service
//        Notification notification = createNotification();
////        // Start the service as a foreground service
//        startForeground(ONGOING_NOTIFICATION_ID, notification);




        // Return START_STICKY to ensure that the service is restarted if it's killed by the system
        return START_STICKY;
    }

    private void startWhistleDetection() {
        try {
            stopWhistleDetection();
            Log.d("checksound11111", "stopWhistleDetection()");
        } catch (Exception e) {
            Log.d("checksound11111", "stopWhistleDetection()  " + e.getMessage() + "  Exception ");
            e.printStackTrace();
        }

        recorderThread = new WhistleRecorderThread();
        recorderThread.startRecording();
        whistleDetectorThread = new WhistleDetectorThread(recorderThread, this);
        whistleDetectorThread.setOnWhistleListener(this);
        whistleDetectorThread.start();
    }

    private void stopWhistleDetection() {
        if (whistleDetectorThread != null) {
            whistleDetectorThread.stopDetection();
            whistleDetectorThread.setOnWhistleListener(null);
            whistleDetectorThread = null;
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
