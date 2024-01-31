package com.findphone.whistleclapfinder.settingClassess;

import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Looper;
import android.os.Vibrator;
import android.util.Log;

public class SettingFlashLightSOSMode {

    static boolean isFlashlightOn = false;
    static boolean isStartisStop = false;
    static boolean shouldToggleVibration = true;
    static boolean shouldToggleFlashlight = true;

    static MediaPlayer mediaPlayer;
    public static void startFlashlightBlinking(CameraManager cameraManager, String cameraId, boolean isStop, Context context) {

         Handler handler = new Handler(Looper.getMainLooper());
        long blinkDelayMillis = 100; // Adjust the delay as needed\

        final Runnable toggleRunnable = new Runnable() {
            @Override
            public void run() {
                try {
                    if (shouldToggleFlashlight) {
                        Log.d("checksound11", "shouldToggleFlashlight true");
                        if (isFlashlightOn) {
                            // Turn off the flashlight
                            cameraManager.setTorchMode(cameraId, false);
                            isFlashlightOn = false;
                            Log.d("checksound11", "setTorchMode Off");
                        } else {
                            // Turn on the flashlight
                            cameraManager.setTorchMode(cameraId, isStop);
                            isFlashlightOn = true;
                            Log.d("checksound11", "setTorchMode On");
                        }
                        // Schedule the next toggle with the current blinkDelayMillis
                        handler.postDelayed(this, blinkDelayMillis);
                    }else {
                        Log.d("checksound11", "shouldToggleFlashlight false");
                        cameraManager.setTorchMode(cameraId, false);

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                shouldToggleFlashlight = true;
                                Log.d("checksound11", "shouldToggleFlashlight true");
                            }
                        },2000);


//                        shouldToggleFlashlight = true;
                    }
                } catch (CameraAccessException e) {
                    e.printStackTrace();
                }
            }
        };

// Start the initial toggle
        handler.post(toggleRunnable);

// To stop the handler when needed (e.g., when isStop is true)

        if (!isStop){
            Log.d("checksound11", "isStop true");
            shouldToggleFlashlight = false;

        }

    }




}
