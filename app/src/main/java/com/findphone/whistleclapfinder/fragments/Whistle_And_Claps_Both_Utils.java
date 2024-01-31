package com.findphone.whistleclapfinder.fragments;

import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.media.MediaPlayer;
import android.util.Log;

import com.findphone.whistleclapfinder.settingClassess.SettingFlashLightDefualt;
import com.findphone.whistleclapfinder.settingClassess.SettingFlashLightDiscoMode;
import com.findphone.whistleclapfinder.settingClassess.SettingFlashLightSOSMode;

public class Whistle_And_Claps_Both_Utils {
    static MediaPlayer whistleMediaPlayer; // this media play variable use a whistle
    static MediaPlayer clapsMediaPlayer;  // this media play variable use a claps

    // below function use for if user service stop or after duration complete is audio stop if flashlight ON so flash light stop and if vibration ON Then vibration stop
    public static void whistleAudioStopAndFlashLightStopAndVibrationStop(Context context) {


        CameraManager cameraManager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
        String cameraId = null;
        try {
            cameraId = cameraManager.getCameraIdList()[0];
        } catch (CameraAccessException e) {
            throw new RuntimeException(e);
        }

        // below Main Setting Flashlight setting if 3 into select one and any set if run then duration complete 3 into one is off

        // Default flash light OFF
        SettingFlashLightDefualt.startFlashlightBlinking(cameraManager, cameraId, false, context);

        // Disco mode flash light OFF
        SettingFlashLightDiscoMode.startFlashlightBlinking(cameraManager, cameraId, false, context);

        // SOS Mode flash light ON
        SettingFlashLightSOSMode.startFlashlightBlinking(cameraManager, cameraId, false, context);


        // Set vibration settings turn off all but into run is one every time not run all one time
        SettingFlashLightDefualt.isVibrationSetDefault(context, false);
        SettingFlashLightDefualt.isVibrationSetStrong(context, false);
        SettingFlashLightDefualt.isVibrationSetHeartbeat(context, false);
        SettingFlashLightDefualt.isVibrationSetTickTok(context, false);




        // music tune is off
        if (whistleMediaPlayer != null) {
            whistleMediaPlayer.stop();
            whistleMediaPlayer.release();
            whistleMediaPlayer = null;

        }
    }




    public static void whistlePlayAudio(int audioResourceId, Context context) {
        Log.d("checkplayaudiorun","audio play 1");
        if (whistleMediaPlayer != null && whistleMediaPlayer.isPlaying()) {
            // If the MediaPlayer is already playing, stop it.
            whistleMediaPlayer.stop();
            whistleMediaPlayer.release();
            whistleMediaPlayer = null;
            Log.d("checkplayaudiorun","audio play 2");
        }

        whistleMediaPlayer = MediaPlayer.create(context, audioResourceId);
        Log.d("checkplayaudiorun","audio play 3");



        // Set a loop to continuously play the audio.
        whistleMediaPlayer.setLooping(true);

        // Start audio playback in a new thread.
        new Thread(new Runnable() {
            public void run() {
                if (whistleMediaPlayer != null) {
                    whistleMediaPlayer.start();
                }
            }
        }).start();
    }


    // below function use for if user service stop or after duration complete is audio stop if flashlight ON so flash light stop and if vibration ON Then vibration stop
    public static void clapsAudioStopAndFlashLightStopAndVibrationStop(Context context) {


        CameraManager cameraManager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
        String cameraId = null;
        try {
            cameraId = cameraManager.getCameraIdList()[0];
        } catch (CameraAccessException e) {
            throw new RuntimeException(e);
        }

        // below Main Setting Flashlight setting if 3 into select one and any set if run then duration complete 3 into one is off

        // Default flash light OFF
        SettingFlashLightDefualt.startFlashlightBlinking(cameraManager, cameraId, false, context);

        // Disco mode flash light OFF
        SettingFlashLightDiscoMode.startFlashlightBlinking(cameraManager, cameraId, false, context);

        // SOS Mode flash light ON
        SettingFlashLightSOSMode.startFlashlightBlinking(cameraManager, cameraId, false, context);


        // Set vibration settings turn off all but into run is one every time not run all one time
        SettingFlashLightDefualt.isVibrationSetDefault(context, false);
        SettingFlashLightDefualt.isVibrationSetStrong(context, false);
        SettingFlashLightDefualt.isVibrationSetHeartbeat(context, false);
        SettingFlashLightDefualt.isVibrationSetTickTok(context, false);


        // music tune is off
        if (clapsMediaPlayer != null) {
            clapsMediaPlayer.stop();
            clapsMediaPlayer.release();
            clapsMediaPlayer = null;

        }
    }


    public static void clapsPlayAudio(int audioResourceId, Context context) {
        Log.d("checkplayaudiorun","audio play 1");
        if (clapsMediaPlayer != null && clapsMediaPlayer.isPlaying()) {
            // If the MediaPlayer is already playing, stop it.
            clapsMediaPlayer.stop();
            clapsMediaPlayer.release();
            clapsMediaPlayer = null;
            Log.d("checkplayaudiorun","audio play 2");
        }

        clapsMediaPlayer = MediaPlayer.create(context, audioResourceId);
        Log.d("checkplayaudiorun","audio play 3");



        // Set a loop to continuously play the audio.
        clapsMediaPlayer.setLooping(true);

        // Start audio playback in a new thread.
        new Thread(new Runnable() {
            public void run() {
                if (clapsMediaPlayer != null) {
                    clapsMediaPlayer.start();
                }
            }
        }).start();
    }
}
