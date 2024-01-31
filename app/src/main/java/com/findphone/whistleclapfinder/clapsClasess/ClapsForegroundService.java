package com.findphone.whistleclapfinder.clapsClasess;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.findphone.whistleclapfinder.R;
import com.findphone.whistleclapfinder.activity.HomeActivity;

public class ClapsForegroundService extends Service implements ClapsDetectorThread.OnWhistleListener {

    private static final int ONGOING_NOTIFICATION_ID = 1;
    private static final String CHANNEL_ID = "com.findphone.whistleclapfinder.clapingDetection";

    Handler handler;
    private ClapsDetectorThread whistleDetectorThread;
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
        Log.d("checksound1", "onStartCommand");

        try {
            if (intent != null && intent.getExtras() != null) {
                String receivedData = intent.getStringExtra("action");
                if (receivedData != null) {
                    if (receivedData.equals("Start")) {
                        startWhistleDetection();
                        Log.d("checksound111111111", receivedData + " $receivedData");


//                        // Set the flag to start as a foreground service
//                        SharedPreferences preferences = getSharedPreferences("whistle_start_service_prefs", MODE_PRIVATE);
//                        SharedPreferences.Editor editor = preferences.edit();
//                        editor.putBoolean("whistl_start_foreground_service", true);
//                        editor.apply();

//                        Intent serviceIntent = new Intent(this, MyForegroundService.class);
//                        startForegroundService(serviceIntent);

                    } else if (receivedData.equals("Stop")) {
                        stopWhistleDetection();
                        stopForeground(true);
                        stopSelf();
                        Log.d("checksound111111111", receivedData + " $receivedData");
                    }
                }
            } else {
                Log.d("checksound111111111", "intent is null OR intent.getExtras() is null");

            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("checksound", "Exception " + e.getMessage());
        }



        // Create a notification for the foreground service
        Notification notification = createNotification();
        // Start the service as a foreground service
        startForeground(ONGOING_NOTIFICATION_ID, notification);


        // Return START_STICKY to ensure that the service is restarted if it's killed by the system
        return START_STICKY;
    }

    private Notification createNotification() {
        // Create a notification channel for Android Oreo and above
        createNotificationChannel();

        // Build the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.dog_icon)
                .setContentTitle("Whistle Detection Service is Running")
                .setContentText("Your whistle detection service is actively running in the background.")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        // Create an Intent to launch your app's main activity when the notification is clicked
//        Intent intent = new Intent(this, HomeActivity.class);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
//        builder.setContentIntent(pendingIntent);

        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, new Intent(getApplicationContext(), HomeActivity.class), PendingIntent.FLAG_IMMUTABLE|PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);



        // Return the notification
        return builder.build();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Whistle Detection Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void startWhistleDetection() {
        try {
            stopWhistleDetection();
            Log.d("checksound11111", "stopWhistleDetection()");
        } catch (Exception e) {
            Log.d("checksound11111", "stopWhistleDetection()  " + e.getMessage() + "  Exception ");
            e.printStackTrace();
        }

        recorderThread = new ClapsRecorderThread();
        recorderThread.startRecording();
        whistleDetectorThread = new ClapsDetectorThread(recorderThread, this);
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
