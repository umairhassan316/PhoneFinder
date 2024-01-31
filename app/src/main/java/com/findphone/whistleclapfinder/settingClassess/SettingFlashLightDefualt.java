package com.findphone.whistleclapfinder.settingClassess;

import static com.findphone.whistleclapfinder.SetionClasss.MyPrefsUseForOnlyHeartbeatVibration.addFistTimeIsCheckedMyPrefsUseForOnlyHeartbeatVibration;
import static com.findphone.whistleclapfinder.SetionClasss.MyPrefsUseForOnlyHeartbeatVibration.addVibrateDurationMyPrefsUseForOnlyHeartbeatVibration;
import static com.findphone.whistleclapfinder.SetionClasss.MyPrefsUseForOnlyHeartbeatVibration.getFistTimeIsCheckedMyPrefsUseForOnlyHeartbeatVibration;
import static com.findphone.whistleclapfinder.SetionClasss.MyPrefsUseForOnlyHeartbeatVibration.getVibrateDurationMyPrefsUseForOnlyHeartbeatVibration;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Handler;
import android.os.Looper;
import android.os.Vibrator;
import android.util.Log;

import org.jetbrains.annotations.NotNull;

public class SettingFlashLightDefualt {

    static boolean isFlashlightOn = false;
    static boolean shouldToggleFlashlight = true;

    public static void startFlashlightBlinking(CameraManager cameraManager, String cameraId, boolean isStop, Context context) {

         Handler handler = new Handler(Looper.getMainLooper());
        long blinkDelayMillis = 1000; // Adjust the delay as needed\

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



    /* if set vibration on then vibration start if not then stop */

    static boolean isStartisStopDefault = false;
    static boolean shouldToggleVibrationDefault = true;


    public static void isVibrationSetDefault(Context context, Boolean isVibration){

        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);

        Handler handler = new Handler(Looper.getMainLooper());
        long blinkDelayMillis = 1000; // Adjust the delay as needed\
        shouldToggleVibrationDefault = true;

        if (isVibration){

            final Runnable toggleRunnable = new Runnable() {
                @SuppressLint("SuspiciousIndentation")
                @Override
                public void run() {

                    if (shouldToggleVibrationDefault){
                    Log.d("TAG", "shouldToggleFlashlight true");
                    if (isStartisStopDefault) {
                        // Turn off the flashlight

                        isStartisStopDefault = false;
                        Log.d("TAG", "setTorchMode Off");
                    } else {
                        // Turn on the flashlight

                            int vibrateDuration = 1000; // 1 second
                            vibrator.vibrate(vibrateDuration);
                        isStartisStopDefault = true;
                            Log.d("TAG", "setTorchMode On");

                    }
                    // Schedule the next toggle with the current blinkDelayMillis
                    handler.postDelayed(this, blinkDelayMillis);

                }
                }
            };

// Start the initial toggle
            handler.post(toggleRunnable);

        }else {
            Log.d("isVibrationisVibration", " false  "+ String.valueOf(isVibration));
            vibrator.cancel();
            shouldToggleVibrationDefault = false;

        }

    }



    static boolean isStartisStopStrong = false;
    static boolean shouldToggleVibrationStrong = true;
    public static void isVibrationSetStrong(@NotNull Context context, boolean isVibration) {


        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);

        Handler handler = new Handler(Looper.getMainLooper());
        long blinkDelayMillis = 300; // Adjust the delay as needed\
        shouldToggleVibrationStrong = true;

        if (isVibration){

            final Runnable toggleRunnable = new Runnable() {
                @Override
                public void run() {

                    if (shouldToggleVibrationStrong){
                        Log.d("TAG", "shouldToggleFlashlight true");
                        if (isStartisStopStrong) {
                            // Turn off the flashlight

                            isStartisStopStrong = false;
                            Log.d("TAG", "setTorchMode Off");
                        } else {
                            // Turn on the flashlight

                            int vibrateDuration = 300; // 1 second
                            vibrator.vibrate(vibrateDuration);
                            isStartisStopStrong = true;
                            Log.d("TAG", "setTorchMode On");

                        }
                        // Schedule the next toggle with the current blinkDelayMillis
                        handler.postDelayed(this, blinkDelayMillis);

                    }
                }
            };

// Start the initial toggle
            handler.post(toggleRunnable);

        }else {
            Log.d("isVibrationisVibration", " false  "+ String.valueOf(isVibration));
            vibrator.cancel();
            shouldToggleVibrationStrong = false;

        }


    }


    static boolean isStartisStopHeartbeat = false;
    static boolean shouldToggleVibrationHeartbeat = true;

    private static final String PREFS_NAME = "MyPrefsUseForOnlyHeartbeatVibration";
    private static final String VALUE_KEY = "valueKey";



    public static void isVibrationSetHeartbeat(@NotNull Context context, boolean isVibration) {

        Log.d("TAG111", "111   isVibrationSetHeartbeat");






        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);

        Handler handler = new Handler(Looper.getMainLooper());

        long blinkDelayMillis = 2000; // Adjust the delay as needed\
        shouldToggleVibrationHeartbeat = true;

        if (isVibration){

            final Runnable toggleRunnable = new Runnable() {
                @Override
                public void run() {

                    if (shouldToggleVibrationHeartbeat){
                        Log.d("TAG111", "222   shouldToggleFlashlight true");
                        if (isStartisStopHeartbeat) {
                            // Turn off the flashlight

                            isStartisStopHeartbeat = false;
                            Log.d("TAG111", "333   setTorchMode Off");
                        } else {
                            // Turn on the flashlight

                            // FIRST_TIME_HEARTBEAT_VIBRATION_KEY_GET
                            boolean firsttime = getFistTimeIsCheckedMyPrefsUseForOnlyHeartbeatVibration(context);


                            if (!firsttime){
                                // FIRST_TIME_HEARTBEAT_VIBRATION_KEY_ADD
                                addFistTimeIsCheckedMyPrefsUseForOnlyHeartbeatVibration(context,true);
                            }

                            // VIBRATION_DURATION_GET
                            int value =   getVibrateDurationMyPrefsUseForOnlyHeartbeatVibration(context);

                            value += 1000; // Increase the value by 1000

                            // VIBRATION_DURATION_ADD_SESTION
                            addVibrateDurationMyPrefsUseForOnlyHeartbeatVibration(context,value);


                            int vibrateDuration = value; // Use the updated value as the vibrate duration


//                            int vibrateDuration = 3000; // 1 second
                            vibrator.vibrate(vibrateDuration);
                            isStartisStopHeartbeat = true;
                            Log.d("TAG111", "444   setTorchMode On  "+vibrateDuration);

                        }
                        // Schedule the next toggle with the current blinkDelayMillis
                        handler.postDelayed(this, blinkDelayMillis);

                    }
                }
            };

// Start the initial toggle
            handler.post(toggleRunnable);

        }else {
            Log.d("TAG111", "555   false  "+ String.valueOf(isVibration));
            vibrator.cancel();
            shouldToggleVibrationHeartbeat = false;

        }




    }


    static boolean isStartisStopTickTok = false;
    static boolean shouldToggleVibrationTickTok = true;
    public static void isVibrationSetTickTok(@NotNull Context context, boolean isVibration) {

        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);

        Handler handler = new Handler(Looper.getMainLooper());
        long blinkDelayMillis = 100; // Adjust the delay as needed\
        shouldToggleVibrationTickTok = true;

        if (isVibration){

            final Runnable toggleRunnable = new Runnable() {
                @Override
                public void run() {

                    if (shouldToggleVibrationTickTok){
                        Log.d("TAG", "shouldToggleFlashlight true");
                        if (isStartisStopTickTok) {
                            // Turn off the flashlight

                            isStartisStopTickTok = false;
                            Log.d("TAG", "setTorchMode Off");
                        } else {
                            // Turn on the flashlight

                            int vibrateDuration = 100; // 1 second
                            vibrator.vibrate(vibrateDuration);
                            isStartisStopTickTok = true;
                            Log.d("TAG", "setTorchMode On");

                        }
                        // Schedule the next toggle with the current blinkDelayMillis
                        handler.postDelayed(this, blinkDelayMillis);

                    }
                }
            };

// Start the initial toggle
            handler.post(toggleRunnable);

        }else {
            Log.d("isVibrationisVibration", " false  "+ String.valueOf(isVibration));
            vibrator.cancel();
            shouldToggleVibrationTickTok = false;

        }

    }
}
