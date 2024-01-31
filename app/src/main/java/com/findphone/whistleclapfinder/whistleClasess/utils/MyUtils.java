package com.findphone.whistleclapfinder.whistleClasess.utils;

import static com.findphone.whistleclapfinder.SetionClasss.MyPrefsExtensionMainSetting.getMyPrefsExtensionMainSetting;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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

import org.jetbrains.annotations.NotNull;

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
        Log.d("checksdferound1111111111", "onFinish start");




        /* apply tune */
        Whistle_And_Claps_Both_Utils.whistlePlayAudio(audioResourceId,context);

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
                                Log.d("checksdferound1111111111", "onFinish 1");

                                //DURATION_MILLISECONDS this is a user time duration after complete time then tune and flash light and vibration off if condition On


//                                CameraManager cameraManager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
//                                String cameraId = null;
//                                try {
//                                    cameraId = cameraManager.getCameraIdList()[0];
//                                } catch (CameraAccessException e) {
//                                    throw new RuntimeException(e);
//                                }
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
//                                // music tune is off
//                                if (mediaPlayer != null) {
//                                    mediaPlayer.stop();
//                                    mediaPlayer.release();
//                                    mediaPlayer = null;
//
//                                }


                                Whistle_And_Claps_Both_Utils.whistleAudioStopAndFlashLightStopAndVibrationStop(context); // all whistle program off means audio stop vibration stop flashlight stop




//                                WindowManager.LayoutParams mParams = new WindowManager.LayoutParams(
//                                        200,
//                                        200,
//                                        WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
//                                        WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
//                                        PixelFormat.TRANSLUCENT);
//                                LayoutInflater inflater = LayoutInflater.from(context);
//                                View testView = inflater.inflate(R.layout.activity_service_stop, null);
//
//                                WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
//                                wm.addView(testView, mParams);












//                                Intent serviceIntent = new Intent(context, WhistleDetectionService.class);
//                                serviceIntent.putExtra("action", "Stop");
//                                context.startService(serviceIntent); // Stop the service
//
//                                Intent serviceIntent1 = new Intent(context, WhistleForegroundService.class);
//                                serviceIntent1.putExtra("action", "Stop");
//                                context.startService(serviceIntent1); // Stop the service







                                // below handler use becuse i need not auto service stop when user whisle then start after any selecte time duration into stop and after 2 second then service start this use belong in whisle WhistleDetectorThread class in





                                // below function use for if complete time duration then indicate FindByWhistleFragment D Activate button

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
                        ImageView icon = overlayView.findViewById(R.id.icon);
                        ImageView imageView = overlayView.findViewById(R.id.icon1);
                        RippleBackground content = overlayView.findViewById(R.id.content);
                        LinearLayout activeDactiveBtn = overlayView.findViewById(R.id.activeDactiveBtn);
                        TextView onOffTextView = overlayView.findViewById(R.id.onOffTextView);
                        TextView nameText = overlayView.findViewById(R.id.nameText);



                        content.startRippleAnimation();


                        // Load the animation from the XML file
                        Animation waveAnimation = AnimationUtils.loadAnimation(context, R.anim.stop_screen_animation);
                        // Start the animation on the ImageView
                        imageView.startAnimation(waveAnimation);


                        SharedPreferences sharedPreferences = context.getSharedPreferences("MyPreferencesSelectItem", Context.MODE_PRIVATE);
                        String base64Image = sharedPreferences.getString("image_key", "");
                        try {
                            Bitmap imageBitmap = base64ToBitmap(base64Image);

                            if (imageBitmap!=null){
                                icon.setImageBitmap(imageBitmap);
                                modeIsActiveLayoutElementsSet(activeDactiveBtn,context,onOffTextView,nameText);
                            }else{

                            }

                        }catch (NullPointerException nullPointerException){

                        }



        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the close button click event
                // Remove the overlay window from the WindowManager


                // Start the OverlayActivity when a whistle is detected
                Intent overlayIntent = new Intent(context, SplashActivity.class);
                overlayIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(overlayIntent);

                Whistle_And_Claps_Both_Utils.whistleAudioStopAndFlashLightStopAndVibrationStop(context);  // all whistle program off means audio stop vibration stop flashlight stop

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





    private static void modeIsActiveLayoutElementsSet(LinearLayout activeDactiveBtn, Context context, TextView onOffTextView, TextView nameText) {
        activeDactiveBtn.setBackgroundResource(R.drawable.mode_active_layout_bg);

        nameText.setText(context.getString(R.string.tap_to_turn_off));
        int textColor = Color.parseColor("#2D66FA"); // Replace with your desired color
        nameText.setTextColor(textColor);

        onOffTextView.setText(context.getString(R.string.on));
        int onOffTextViewTextColor = Color.parseColor("#2D66FA"); // Replace with your desired color
        onOffTextView.setTextColor(onOffTextViewTextColor);

    }


    public static boolean checkInternet(Context context){

       boolean isCheckInternet = false;

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            // connected to the internet
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI || activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                // internet connected connected
                isCheckInternet = true;

        } else {
            // not connected to the internet
                isCheckInternet = false;
        }
return isCheckInternet;

}

}
