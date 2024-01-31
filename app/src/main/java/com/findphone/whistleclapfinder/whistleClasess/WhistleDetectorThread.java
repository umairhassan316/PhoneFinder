package com.findphone.whistleclapfinder.whistleClasess;

import static android.content.Context.MODE_PRIVATE;
import static com.findphone.whistleclapfinder.SetionClasss.MyPrefsUseForMainSettingFlashLightModeSelect.getMyPrefsUseForMainSettingFlashLightModeSelect;
import static com.findphone.whistleclapfinder.SetionClasss.MyPrefsUseForOnlyHeartbeatVibration.clearMyPrefsUseForOnlyHeartbeatVibration;
import static com.findphone.whistleclapfinder.SetionClasss.MyPrefsVibrationModeMainSetting.getMyPrefsMyPrefsVibrationModeMainSetting;
import static com.findphone.whistleclapfinder.whistleClasess.utils.MyUtils.seekbarFun;
import static com.findphone.whistleclapfinder.whistleClasess.utils.MyUtils.soundDisableEnableFun;
import static com.findphone.whistleclapfinder.whistleClasess.utils.MyUtils.timeDurationstopServiceAfterComplete;
import static com.findphone.whistleclapfinder.whistleClasess.utils.MyUtils.useForAudioMode;
import static com.findphone.whistleclapfinder.whistleClasess.utils.MyUtils.whenWhistleOrClapsDetectScreenLightOn;

import java.util.LinkedList;

import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.ConsumerIrManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.findphone.whistleclapfinder.R;
import com.findphone.whistleclapfinder.settingClassess.SettingFlashLightDefualt;
import com.findphone.whistleclapfinder.settingClassess.SettingFlashLightDiscoMode;
import com.findphone.whistleclapfinder.settingClassess.SettingFlashLightSOSMode;
import com.findphone.whistleclapfinder.whistleClasess.utils.MyUtils;
import com.musicg.api.WhistleApi;
import com.musicg.api.ClapApi;
import com.musicg.wave.WaveHeader;

public class WhistleDetectorThread extends Thread {


    private Context context;
     CameraManager cameraManager;
    private boolean whistleDetected = false; // Move this flag to the class level
    private MediaPlayer mediaPlayer;


    String cameraId = null; // Assuming the first camera



    private WhistleRecorderThread recorder;
    private WaveHeader waveHeader;
    private WhistleApi whistleApi;
    private ConsumerIrManager.CarrierFrequencyRange frequencyRange;

    private ClapApi clapApi;

    private Thread _thread;

    private LinkedList<Boolean> whistleResultList = new LinkedList<Boolean>();
    private int numWhistles;
    private int totalWhistlesDetected = 0;
    private int whistleCheckLength = 3;
    private int whistlePassScore = 3;

    public WhistleDetectorThread(WhistleRecorderThread recorder, Context detectionService) {
        this.recorder = recorder;
        this.context = detectionService;
        this.cameraManager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);

        AudioRecord audioRecord = recorder.getAudioRecord();

        int bitsPerSample = 0;
        if (audioRecord.getAudioFormat() == AudioFormat.ENCODING_PCM_16BIT) {
            bitsPerSample = 16;
        } else if (audioRecord.getAudioFormat() == AudioFormat.ENCODING_PCM_8BIT) {
            bitsPerSample = 8;
        }

        int channel = 0;
        // whistle detection only supports mono channel
        if (audioRecord.getChannelConfiguration() == AudioFormat.CHANNEL_IN_MONO) {
            channel = 1;
        }

        waveHeader = new WaveHeader();
        waveHeader.setChannels(channel);
        waveHeader.setBitsPerSample(bitsPerSample);
        waveHeader.setSampleRate(audioRecord.getSampleRate());
        whistleApi = new WhistleApi(waveHeader);
        clapApi = new ClapApi(waveHeader);
    }

    private void initBuffer() {
        numWhistles = 0;
        whistleResultList.clear();

        // init the first frames
        for (int i = 0; i < whistleCheckLength; i++) {
            whistleResultList.add(false);
        }
        // end init the first frames
    }

    public void start() {
        _thread = new Thread(this);
        _thread.start();
    }

    public void stopDetection() {
        Log.d("checksound11111", "stopDetection() stopDetection() stopDetection() stopDetection()");

        Log.d("checksound11111", "Stop and release the MediaPlayer");
        // Stop and release the MediaPlayer.
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }

        _thread = null;
        try {
            cameraManager.setTorchMode(cameraId, false);
        } catch (CameraAccessException e) {
            throw new RuntimeException(e);
        }



    }

    @Override
    public void run() {
        Log.d("checksound", "DetectorThread started...");

        try {
            byte[] buffer;
            initBuffer();

            Thread thisThread = Thread.currentThread();
            while (_thread == thisThread) {
                // detect sound
                buffer = recorder.getFrameBytes();

                // audio analyst
                if (buffer != null) {
                    // sound detected
                    // MainActivity.whistleValue = numWhistles;

                    // whistle detection
                    // System.out.println("*Whistle:");

                    try {
                        Log.d("checksound11111", "try");

                        boolean isWhistle = whistleApi.isWhistle(buffer);
                        Log.d("checksound", "isWhistle : " + isWhistle + " " + buffer.length);
                            /* Below shared prefrence use becuse if user selected tune apply whan user speak whistle to start tune*/
                        SharedPreferences sharedPreferences = context.getSharedPreferences("MyPreferencesSelectItem", MODE_PRIVATE);
                        Boolean isRingTuneSelected = sharedPreferences.getBoolean("isRingTuneSelected", false);
                        Boolean flashLight = sharedPreferences.getBoolean("flashLight", false);
                        Boolean isVibration = sharedPreferences.getBoolean("isVibration", false);
                        Boolean isSound = sharedPreferences.getBoolean("isSound", false);
                        int audioResourceId = sharedPreferences.getInt("audioResourceId", 0);
                        int soundSeekbar = sharedPreferences.getInt("soundSeekbar", 0);
                        int timeDurationService = sharedPreferences.getInt("timeDurationService", 0);

                        // Declare a boolean flag to track whether the whistle detection code has already been executed.


                            // Inside your detection logic, check the flag before running the code.
                        // Move the flag reset logic outside of the if block
                        if (isRingTuneSelected && isWhistle && !whistleDetected) {



                                Log.d("checksound", "isWhistle Sound here and if mobile silent then set normal mode and flash litgh blinking");

                                Log.d("checksound1111111111", "if (isWhistle){ 1");

                                // The screen has been turned off
                                whenWhistleOrClapsDetectScreenLightOn(context);

                                // if true vibration is on if false vibration not run
//                                isVibrationSet(context,isVibration);

                                // soundSeekbar this seekbar control funcation below
                                seekbarFun(context,soundSeekbar);

                            // sound Disable and Enable this control funcation below
                            soundDisableEnableFun(context,true,soundSeekbar);

                            // time duration if for example duration 10 second after 10 second service off this control funcation below
                            timeDurationstopServiceAfterComplete(timeDurationService,context,audioResourceId); // time duaration set for example if 10 second after 10 second whisle fourground service and backgroudn service both stoped







                            // is check if tune sub setting flashlight on or off if false then off if true then off
                            if (flashLight){

                                Log.d("checkflashligh"," 1111  flashLight is "+flashLight);

                                // is check main setting add functionality flash light means user who apply flashlight mode like default or discoMode or sosMode

                                String flashLightMode = getMyPrefsUseForMainSettingFlashLightModeSelect(context);

                                if (flashLightMode.equals("default")){

                                    boolean defaultIsChecked = true;
                                    boolean discoModeIsChecked = false;
                                    boolean sosModeIsChecked = false;

                                    Log.d("checkflashligh"," aaaa  flashLight is default ");


                                    onOffFlashLight(context,defaultIsChecked,discoModeIsChecked,sosModeIsChecked);

                                }else if (flashLightMode.equals("discoMode")){

                                    boolean defaultIsChecked = false;
                                    boolean discoModeIsChecked = true;
                                    boolean sosModeIsChecked = false;

                                    onOffFlashLight(context, defaultIsChecked,discoModeIsChecked,sosModeIsChecked);

                                    Log.d("checkflashligh"," bbbb  flashLight is discoMode ");

                                }else if (flashLightMode.equals("sosMode")){


                                    boolean defaultIsChecked = false;
                                    boolean discoModeIsChecked = false;
                                    boolean sosModeIsChecked = true;

                                    onOffFlashLight(context, defaultIsChecked,discoModeIsChecked,sosModeIsChecked);

                                    Log.d("checkflashligh"," cccc  flashLight is sosMode ");

                                }


                            }else {
                                Log.d("checkflashligh"," 2222  flashLight is "+flashLight);
                            }


                            // is check if tune sub setting vibration on or off if false then off if true then off
                            if (isVibration){

                                // is check main setting add functionality vibration means user who apply vibration mode like default or strongVibration or heartbeat and tiktok

                                String vibrationMode = getMyPrefsMyPrefsVibrationModeMainSetting(context);

                                if (vibrationMode.equals("defaultVibration")){

                                    boolean defaultIsChecked = true;
                                    boolean strongVibrationIsChecked = false;
                                    boolean heartbeatVibrationIsChecked = false;
                                    boolean tikTokVibrationIsChecked = false;


                                    onOffVibration(defaultIsChecked,strongVibrationIsChecked,heartbeatVibrationIsChecked,tikTokVibrationIsChecked);



                                }else if (vibrationMode.equals("strong vibration")){

                                    boolean defaultIsChecked = false;
                                    boolean strongVibrationIsChecked = true;
                                    boolean heartbeatVibrationIsChecked = false;
                                    boolean tikTokVibrationIsChecked = false;


                                    onOffVibration(defaultIsChecked,strongVibrationIsChecked,heartbeatVibrationIsChecked,tikTokVibrationIsChecked);


                                }else if (vibrationMode.equals("heartbeat")){

                                    boolean defaultIsChecked = false;
                                    boolean strongVibrationIsChecked = false;
                                    boolean heartbeatVibrationIsChecked = true;
                                    boolean tikTokVibrationIsChecked = false;


                                    onOffVibration(defaultIsChecked,strongVibrationIsChecked,heartbeatVibrationIsChecked,tikTokVibrationIsChecked);

                                }else if (vibrationMode.equals("tiktok")){

                                    boolean defaultIsChecked = false;
                                    boolean strongVibrationIsChecked = false;
                                    boolean heartbeatVibrationIsChecked = false;
                                    boolean tikTokVibrationIsChecked = true;


                                    onOffVibration(defaultIsChecked,strongVibrationIsChecked,heartbeatVibrationIsChecked,tikTokVibrationIsChecked);

                                }


                            }



                                /* below function use for check audio mode if silent mode apply normal mode */
                                useForAudioMode(context);
//                                /* apply tune */
//                                playAudio(audioResourceId);

                                Log.d("checkTimer", "On BrodCast Recier Timer Run wakeLock.acquire()");
//

                                // Set the flag to true to indicate that the code has been executed.
                                whistleDetected = true;
                                Log.d("checksdferound1111111111", "whistleDetected = true;");





                                // below thread into use handler for whistleDetected boolean set this use reason of whisle detect fist time then true and thread into hander set whistleDetected then false means again whisle detect is ready
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
                                            whistleDetected = false;
                                            Log.d("checksdferound1111111111", "whistleDetected = fales;");
                                        }
                                    },timeDurationService+1000);//timeDurationService is user set time duration in tune sub setting 1000 plus add means when user time duration is complete then after 1 second whistleDetected false



                                }
                            }).start();

                            // below function is use for open screen overlay
                            MyUtils.overlayScreenOpen(context);



                            }else {
                            Log.d("checksound11111", "else  whistle  run");


                        }








                        if (whistleResultList.getFirst()) {
                            numWhistles--;
                        }

                        whistleResultList.removeFirst();
                        whistleResultList.add(isWhistle);

                        if (isWhistle) {
                            numWhistles++;
                        }

                        // Debug.e("", "numWhistles : " + numWhistles);

                        if (numWhistles >= whistlePassScore) {
                            // clear buffer
                            initBuffer();
                            totalWhistlesDetected++;

                            Log.d("checksound", "totalWhistlesDetected : "
                                    + totalWhistlesDetected);

                            if (onWhistleListener != null) {
                                onWhistleListener.onWhistle();
                            }
                        }
                    } catch (Exception e) {
                        Log.d("checksound11111", "Exception " + e.getCause());
                    }
                    // end whistle detection





                    /* below claps work */

                    try {
                        boolean isClaps = clapApi.isClap(buffer);
                        Log.d("checksound", "isClaps : " + isClaps + " " + buffer.length);

//                        if (isClaps){
//                            Log.d("checksound", "isClaps Sound here and if mobile silent then set normal mode and flash litgh blinking");
//
//
//                            Log.d("checksound11111", " if (isClaps){");
//
//                            // The screen has been turned off
//                            whenWhistleOrClapsDetectScreenLightOn(context);
//
//                            /*To turn on the flashlight : To turn on the flashlight, you can use the CameraManager.*/
//
//                             cameraManager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
//                            try {
//                                cameraId = cameraManager.getCameraIdList()[0];
//                            } catch (CameraAccessException e) {
//                                throw new RuntimeException(e);
//                            }
//                            try {
//                                cameraManager.setTorchMode(cameraId, true); // Turn on flashlight
//                            } catch (CameraAccessException e) {
//                                throw new RuntimeException(e);
//                            }
//
//                            // Start blinking the flashlight
//                            startFlashlightBlinking(cameraManager,cameraId, true,context);
//
//                            /* below function use for check audio mode if silent mode apply normal mode */
//                            useForAudioMode(context);
//
//                            Log.d("checkTimer", "On BrodCast Recier Timer Run wakeLock.acquire()");
//
//                        }

                        if (whistleResultList.getFirst()) {
                            numWhistles--;
                        }

                        whistleResultList.removeFirst();
                        whistleResultList.add(isClaps);

                        if (isClaps) {
                            numWhistles++;
                        }

                        // Debug.e("", "numWhistles : " + numWhistles);

                        if (numWhistles >= whistlePassScore) {
                            // clear buffer
                            initBuffer();
                            totalWhistlesDetected++;

                            Log.d("checksound", "totalWhistlesDetected : "
                                    + totalWhistlesDetected);

                            if (onWhistleListener != null) {
                                onWhistleListener.onWhistle();
                            }
                        }
                    } catch (Exception e) {
                        Log.d("checksound", "Exception " + e.getCause());
                    }







                } else {
                    // Debug.e("", "no sound detected");
                    // no sound detected
                    if (whistleResultList.getFirst()) {
                        numWhistles--;
                    }
                    whistleResultList.removeFirst();
                    whistleResultList.add(false);

                    // MainActivity.whistleValue = numWhistles;
                }
                // end audio analyst
            }

            Log.d("checksound", "Terminating detector thread...");


            /* below funcation for stop flash light vibration  */

//            cameraManager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
//            try {
//                cameraId = cameraManager.getCameraIdList()[0];
//            } catch (CameraAccessException e) {
//                throw new RuntimeException(e);
//            }
//
//
//            // flish light  off
//            startFlashlightBlinking(cameraManager,cameraId,false, context);
//            // if true vibration is on if false vibration not run
//            isVibrationSet(context,false);







        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onOffVibration(boolean defaultIsChecked, boolean strongVibrationIsChecked, boolean heartbeatVibrationIsChecked, boolean tikTokVibrationIsChecked) {
        // Clear MHeartbeat Vibration
        clearMyPrefsUseForOnlyHeartbeatVibration(context);

        // Set vibration settings
        SettingFlashLightDefualt.isVibrationSetDefault(context, defaultIsChecked);
        SettingFlashLightDefualt.isVibrationSetStrong(context, strongVibrationIsChecked);
        SettingFlashLightDefualt.isVibrationSetHeartbeat(context, heartbeatVibrationIsChecked);
        SettingFlashLightDefualt.isVibrationSetTickTok(context, tikTokVibrationIsChecked);
    }

    private void onOffFlashLight(Context context, boolean defaultIsChecked, boolean discoModeIsChecked, boolean sosModeIsChecked) {

        Log.d("checkflashligh"," onOffFlashLight "+" defaultIsChecked  "+defaultIsChecked+" discoModeIsChecked "+discoModeIsChecked+" sosModeIsChecked "+sosModeIsChecked);

        CameraManager cameraManager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
        String cameraId = null;

        try {
            cameraId = cameraManager.getCameraIdList()[0];
        } catch (CameraAccessException e) {
            throw new RuntimeException(e);
        }

        // Default flash light OFF
        SettingFlashLightDefualt.startFlashlightBlinking(cameraManager, cameraId, defaultIsChecked, context);

        // Disco mode flash light OFF
        SettingFlashLightDiscoMode.startFlashlightBlinking(cameraManager, cameraId, discoModeIsChecked, context);

        // SOS Mode flash light ON
        SettingFlashLightSOSMode.startFlashlightBlinking(cameraManager, cameraId, sosModeIsChecked, context);
    }

    private void playAudio(int audioResourceId) {
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



    private OnWhistleListener onWhistleListener;

    public void setOnWhistleListener(OnWhistleListener onWhistleListener) {
        this.onWhistleListener = onWhistleListener;
    }

    public interface OnWhistleListener {
        void onWhistle();
    }

    public int getTotalWhistlesDetected() {
        return totalWhistlesDetected;
    }
}
