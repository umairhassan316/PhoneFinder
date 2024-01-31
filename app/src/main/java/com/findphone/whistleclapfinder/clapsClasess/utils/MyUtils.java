package com.findphone.whistleclapfinder.clapsClasess.utils;

import static com.findphone.whistleclapfinder.SetionClasss.MyPrefsExtensionMainSetting.getMyPrefsExtensionMainSetting;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.os.PowerManager;
import android.os.Vibrator;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.findphone.whistleclapfinder.R;
import com.findphone.whistleclapfinder.activity.RippleBackground;
import com.findphone.whistleclapfinder.activity.SplashActivity;
import com.findphone.whistleclapfinder.fragments.Whistle_And_Claps_Both_Utils;

import java.io.ByteArrayOutputStream;
import java.util.Locale;

public class MyUtils {

    static boolean isFlashlightOn = false;
    static boolean isStartisStop = false;
    static boolean shouldToggleVibration = true;
    static boolean shouldToggleFlashlight = true;

    static MediaPlayer mediaPlayer;
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



    public static void whenWhistleOrClapsDetectScreenLightOn(Context context) {

        Log.d("checkscreenlight","whenWhistleOrClapsDetectScreenLightOn  ");


        // below setion this setting into use extation screen light on off set if true then when start ring tune then screen on if false then screen not on use this SettingActivity
        boolean isCheckedSharedPrefrence = getMyPrefsExtensionMainSetting(context);


        if (isCheckedSharedPrefrence){

             final PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
             final PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "MyApp:ScreenOnWakeLock");
             wakeLock.acquire();

             Log.d("checkscreenlight","Screen Light Check  "+isCheckedSharedPrefrence);

         }else {
             Log.d("checkscreenlight","Screen Light Check  "+isCheckedSharedPrefrence);

         }




    }

    /* below function use for check audio mode if silent mode apply normal mode */
    public static void useForAudioMode(Context context) {

        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        int ringerMode = audioManager.getRingerMode();

        if (ringerMode == AudioManager.RINGER_MODE_SILENT) {
            Log.d("checkTimer", "RINGER_MODE_SILENT");
            // Silent mode is active, change it if necessary
            audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);

        }else if (ringerMode == AudioManager.RINGER_MODE_NORMAL) {
            // Silent mode is active, change it if necessary
            Log.d("checkTimer", "RINGER_MODE_NORMAL");
        }else if (ringerMode == AudioManager.RINGER_MODE_VIBRATE) {
            // Silent mode is active, change it if necessary
            Log.d("checkTimer", "RINGER_MODE_VIBRATE");
        }



    }
    /* if set vibration on then vibration start if not then stop */
    public static void isVibrationSet(Context context, Boolean isVibration){

        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);

        Handler handler = new Handler(Looper.getMainLooper());
        long blinkDelayMillis = 1000; // Adjust the delay as needed\
        shouldToggleVibration = true;

        if (isVibration){

            final Runnable toggleRunnable = new Runnable() {
                @Override
                public void run() {

                    if (shouldToggleVibration){
                    Log.d("checksound11", "shouldToggleFlashlight true");
                    if (isStartisStop) {
                        // Turn off the flashlight

                        isStartisStop = false;
                        Log.d("checksound11", "setTorchMode Off");
                    } else {
                        // Turn on the flashlight

                            int vibrateDuration = 1000; // 1 second
                            vibrator.vibrate(vibrateDuration);
                            isStartisStop = true;
                            Log.d("checksound11", "setTorchMode On");

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
            shouldToggleVibration = false;

        }

    }


    public static void seekbarFun(Context context, int seekbarValue){
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, seekbarValue, 0);


    }

    public static void soundDisableEnableFun(Context context, Boolean isSound, int soundSeekbar){

        if (isSound){
            AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, soundSeekbar, 0);
        }else {
            AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
        }




    }


    public static void timeDurationstopServiceAfterComplete(long DURATION_MILLISECONDS, Context context, int audioResourceId) {

        /* apply tune */
//        playAudio(audioResourceId,context);
        Whistle_And_Claps_Both_Utils.clapsPlayAudio(audioResourceId,context);

        Thread thread = new Thread();
        Thread finalThread = thread;
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                // Start a countdown timer for the specified duration
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {

                        CountDownTimer countDownTimer;

                        // Start a countdown timer for the specified duration
                        countDownTimer = new CountDownTimer(DURATION_MILLISECONDS, DURATION_MILLISECONDS) {
                            @Override
                            public void onTick(long millisUntilFinished) {
                                // This method is called at regular intervals while the timer is running
                            }

                            @Override
                            public void onFinish() {
                                // This method is called when the timer finishes (after 10 seconds)
                                Log.d("checksound1111111111", "onFinish 1");

                                /* below function for stop flash light vibration and audio */
//
//                                CameraManager cameraManager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
//                                String cameraId = null;
//                                try {
//                                    cameraId = cameraManager.getCameraIdList()[0];
//                                } catch (CameraAccessException e) {
//                                    throw new RuntimeException(e);
//                                }
////
////
////                                // flish light  off
////                                startFlashlightBlinking(cameraManager,cameraId,false, context);
////
//
//                                // below Main Setting Flashlight setting if 3 into select one and any set if run then duration complete 3 into one is off
//
//                                // Default flash light OFF
//                                SettingFlashLightDefualt.startFlashlightBlinking(cameraManager, cameraId, false, context);
//
//                                // Disco mode flash light OFF
//                                SettingFlashLightDiscoMode.startFlashlightBlinking(cameraManager, cameraId, false, context);
//
//                                // SOS Mode flash light ON
//                                SettingFlashLightSOSMode.startFlashlightBlinking(cameraManager, cameraId, false, context);
//
//
//                                // Set vibration settings turn off all but into run is one every time not run all one time
//                                SettingFlashLightDefualt.isVibrationSetDefault(context, false);
//                                SettingFlashLightDefualt.isVibrationSetStrong(context, false);
//                                SettingFlashLightDefualt.isVibrationSetHeartbeat(context, false);
//                                SettingFlashLightDefualt.isVibrationSetTickTok(context, false);
//
//
//
//
//                                // if true vibration is on if false vibration not run
//                                isVibrationSet(context,false);
//
//                                // music tune is off
//                                if (mediaPlayer != null) {
//                                    mediaPlayer.stop();
//                                    mediaPlayer.release();
//                                    mediaPlayer = null;

//                                }


                                Whistle_And_Claps_Both_Utils.clapsAudioStopAndFlashLightStopAndVibrationStop(context);























//                                Intent intent = new Intent("onFinish");
//                                intent.putExtra("key", "onFinish");
//                                // put your all data using put extra
//
//                                // put your all data using put extra
//                                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);


                                // You can also stop the thread if it's no longer needed
                                finalThread.interrupt();
                            }
                        };
                        countDownTimer.start();

                        Log.d("checksound1111111111", "stopServiceAfterDelay");
                    }
                });
            }
        });
        thread.start();




    }

    private static void playAudio(int audioResourceId, Context context) {
        Log.d("checkplayaudiorun","audio play 1");
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            // If the MediaPlayer is already playing, stop it.
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
            Log.d("checkplayaudiorun","audio play 2");
        }

        mediaPlayer = MediaPlayer.create(context, audioResourceId);
        Log.d("checkplayaudiorun","audio play 3");



        // Set a loop to continuously play the audio.
        mediaPlayer.setLooping(true);

        // Start audio playback in a new thread.
        new Thread(new Runnable() {
            public void run() {
                if (mediaPlayer != null) {
                    mediaPlayer.start();
                }
            }
        }).start();
    }


    public static Bitmap getBitmapFromDrawable(Context context, int drawableResourceId) {
        Drawable drawable = ContextCompat.getDrawable(context, drawableResourceId);

        if (drawable != null) {
            // Check if the drawable is a BitmapDrawable
            if (drawable instanceof BitmapDrawable) {
                return ((BitmapDrawable) drawable).getBitmap();
            }

            // For other drawable types, you can create a Bitmap and draw the drawable onto it
            Bitmap bitmap = Bitmap.createBitmap(
                    drawable.getIntrinsicWidth(),
                    drawable.getIntrinsicHeight(),
                    Bitmap.Config.ARGB_8888
            );
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);

            return bitmap;
        }

        return null;
    }

    public static String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    public static Bitmap base64ToBitmap(String base64Image) {
        if (base64Image != null) {
            byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        }
        return null;
    }

    public static String imageConverter(Context context, int dogIcon) {
        Bitmap bitmap = MyUtils.getBitmapFromDrawable(context, dogIcon);
        String base64Image = MyUtils.bitmapToBase64(bitmap); // Replace 'yourBitmap' with your actual Bitmap
        return base64Image;
    }

    public static String soundTitleTranslator(Context context, String nameKey) {
        String currentLanguage = Locale.getDefault().getLanguage();
// Use the translation function to get the correct language-specific text
        String displayName = getTranslatedName(context, currentLanguage, nameKey);

        return displayName;
    }



    public static String getTranslatedName(@NonNull Context context, @NonNull String language, @NonNull String nameKey) {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier(nameKey, "string", context.getPackageName());

        try {
            if (resourceId != 0) {
                Locale locale = new Locale(language);
                Configuration configuration = new Configuration(resources.getConfiguration());
                configuration.setLocale(locale);
                Resources localizedResources = context.createConfigurationContext(configuration).getResources();
                return localizedResources.getString(resourceId); // Use getString from localizedResources
            } else {
                return nameKey; // Return the key if no translation is found
            }
        } catch (Exception e) {
            Log.e("TranslationHelper", "Error getting translated name: " + e.getMessage());
            return nameKey; // Return the key in case of an error
        }
    }

    public static void  overlayScreenOpen(Context context) {


        new Thread(new Runnable() {
            @Override
            public void run() {

                // Create a Handler associated with the main (UI) thread
                Handler handler = new Handler(Looper.getMainLooper());

                // In your background thread or Runnable:
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Your UI-related code goes here




                        Log.e("checksdferound1111111111", "overlayScreenOpen");
                        WindowManager.LayoutParams mParams = new WindowManager.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
                                PixelFormat.TRANSLUCENT);
                        LayoutInflater inflater = LayoutInflater.from(context);
                        View overlayView = inflater.inflate(R.layout.activity_service_stop, null);

                        // Optionally, set up any UI elements in your overlayView
                        TextView closeBtn = overlayView.findViewById(R.id.closeBtn);
                        ImageView imageView = overlayView.findViewById(R.id.icon1);
                        RippleBackground content = overlayView.findViewById(R.id.content);




                        content.startRippleAnimation();

                        // Load the animation from the XML file
                        Animation waveAnimation = AnimationUtils.loadAnimation(context, R.anim.stop_screen_animation);
                        // Start the animation on the ImageView
                        imageView.startAnimation(waveAnimation);



                        closeBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // Handle the close button click event
                                // Remove the overlay window from the WindowManager


                                // Start the OverlayActivity when a whistle is detected
                                Intent overlayIntent = new Intent(context, SplashActivity.class);
                                overlayIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(overlayIntent);

                                Whistle_And_Claps_Both_Utils.clapsAudioStopAndFlashLightStopAndVibrationStop(context);  // all claps program off means audio stop vibration stop flashlight stop

                                WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
                                wm.removeView(overlayView);
                                content.stopRippleAnimation();

                            }
                        });


                        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
                        wm.addView(overlayView, mParams);

                        Log.d("checksdferound1111111111", "ovlay screen in show");
                    }
                },100);//timeDurationService is user set time duration in tune sub setting 1000 plus add means when user time duration is complete then after 1 second whistleDetected false



            }
        }).start();








    }



}
