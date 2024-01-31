package com.findphone.whistleclapfinder.clapsClasess;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.util.Log;

import androidx.core.app.ActivityCompat;

public class ClapsRecorderThread {


    private Context context;


    public void setContext(Context mainActivity4) {
        this.context = mainActivity4;

    }
    private AudioRecord audioRecord;
    private int channelConfiguration;
    private int audioEncoding;
    private int sampleRate;
    private int frameByteSize; // for 1024 fft size (16bit sample size)
    byte[] buffer;

    public ClapsRecorderThread() {
        sampleRate = 44100;
        frameByteSize = 1024 * 2;

        channelConfiguration = AudioFormat.CHANNEL_IN_MONO;
        audioEncoding = AudioFormat.ENCODING_PCM_16BIT;

        int recBufSize = AudioRecord.getMinBufferSize(sampleRate,
                channelConfiguration, audioEncoding); // need to be larger than
        // size of a frame

     if (context!=null){
         if (ActivityCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
             // TODO: Consider calling
             //    ActivityCompat#requestPermissions
             // here to request the missing permissions, and then overriding
             //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
             //                                          int[] grantResults)
             // to handle the case where the user grants the permission. See the documentation
             // for ActivityCompat#requestPermissions for more details.
             return;
         }
         Log.d("checksound","Context not Null");
     }else {
         Log.d("checksound","Context Is Null");
     }



        audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC,
                sampleRate, channelConfiguration, audioEncoding, recBufSize);
        buffer = new byte[frameByteSize];
    }

    public AudioRecord getAudioRecord() {
        return audioRecord;
    }

    public boolean isRecording() {
        if (audioRecord.getRecordingState() == AudioRecord.RECORDSTATE_RECORDING) {
            return true;
        }

        return false;
    }

    public void startRecording() {
        try {
            audioRecord.startRecording();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopRecording() {
        try {
            audioRecord.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public byte[] getFrameBytes() {
        audioRecord.read(buffer, 0, frameByteSize);

        // analyze sound
        int totalAbsValue = 0;
        short sample = 0;
        float averageAbsValue = 0.0f;

        for (int i = 0; i < frameByteSize; i += 2) {
            sample = (short) ((buffer[i]) | buffer[i + 1] << 8);
            totalAbsValue += Math.abs(sample);
        }
        averageAbsValue = totalAbsValue / frameByteSize / 2;

        Log.d("checksound", "averageAbsValue : " + averageAbsValue);

        // no input
        if (averageAbsValue < 30) {
            return null;
        }

        return buffer;
    }


}
