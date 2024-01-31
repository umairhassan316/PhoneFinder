package com.findphone.whistleclapfinder.whistleClasess.whistledatabase;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

@Entity(tableName = "audio_items_whistle")
public class AudioModelWhisleDatabase implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "flash_light")
    private boolean flashLight = false;

    @ColumnInfo(name = "vibration")
    private boolean vibration = false;

    @ColumnInfo(name = "sound")
    private boolean sound = false;

    @ColumnInfo(name = "sound_seekbar")
    private int soundSeekbar;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "imageResourceId")
    private String imageResourceId;

    @ColumnInfo(name = "audioResourceId")
    private int audioResourceId;

    @ColumnInfo(name = "timeDurationService")
    private int timeDurationService;

    private boolean isPlaying;

    // Custom serialization method
    public byte[] toByteArray() throws IOException {
        // Convert the object to a byte array using Java's ObjectOutputStream
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        objectOutputStream.writeObject(this);
        objectOutputStream.close();
        return outputStream.toByteArray();
    }

    // Custom deserialization method
    public static AudioModelWhisleDatabase fromByteArray(byte[] byteArray) throws IOException, ClassNotFoundException {
        // Convert the byte array back to an AudioModel object
        ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArray);
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
        return (AudioModelWhisleDatabase) objectInputStream.readObject();
    }


    public AudioModelWhisleDatabase() {
    }

    public AudioModelWhisleDatabase(boolean flashLight, boolean vibration, boolean sound, int soundSeekbar, String title, String imageResourceId, int audioResourceId, int timeDurationService) {
        this.flashLight = flashLight;
        this.vibration = vibration;
        this.sound = sound;
        this.soundSeekbar = soundSeekbar;
        this.title = title;
        this.imageResourceId = imageResourceId;
        this.audioResourceId = audioResourceId;
        this.timeDurationService = timeDurationService;
        this.isPlaying = false;

    }


    public int getId() {
        return id;
    }

    public boolean isFlashLight() {
        return flashLight;
    }

    public boolean isVibration() {
        return vibration;
    }

    public boolean isSound() {
        return sound;
    }

    public int getSoundSeekbar() {
        return soundSeekbar;
    }

    public String getTitle() {
        return title;
    }

    public String getImageResourceId() {
        return imageResourceId;
    }

    public int getAudioResourceId() {
        return audioResourceId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setFlashLight(boolean flashLight) {
        this.flashLight = flashLight;
    }

    public void setVibration(boolean vibration) {
        this.vibration = vibration;
    }

    public void setSound(boolean sound) {
        this.sound = sound;
    }

    public void setSoundSeekbar(int soundSeekbar) {
        this.soundSeekbar = soundSeekbar;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setImageResourceId(String imageResourceId) {
        this.imageResourceId = imageResourceId;
    }

    public void setAudioResourceId(int audioResourceId) {
        this.audioResourceId = audioResourceId;
    }


    public int getTimeDurationService() {
        return timeDurationService;
    }

    public void setTimeDurationService(int timeDurationService) {
        this.timeDurationService = timeDurationService;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }
}
