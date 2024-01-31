package com.findphone.whistleclapfinder.SetionClasss;

import android.content.Context;
import android.content.SharedPreferences;

public class MyPrefsUseForOnlyHeartbeatVibration {

    private static final String PREFS_NAME = "MyPrefsUseForOnlyHeartbeatVibration";
    private static final String VALUE_KEY = "valueKey";
    private static final String FIRST_TIME_HEARTBEAT_VIBRATION_KEY = "firsttime";

    public static void clearMyPrefsUseForOnlyHeartbeatVibration(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    public static void addFistTimeIsCheckedMyPrefsUseForOnlyHeartbeatVibration(Context context, boolean isChecked) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(FIRST_TIME_HEARTBEAT_VIBRATION_KEY, isChecked);
        editor.apply();
    }

    public static void addVibrateDurationMyPrefsUseForOnlyHeartbeatVibration(Context context, int value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(VALUE_KEY, value);
        editor.apply();
    }

    public static boolean getFistTimeIsCheckedMyPrefsUseForOnlyHeartbeatVibration(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        boolean firsttime = sharedPreferences.getBoolean("firsttime", false);
        return firsttime;
    }


    public static int getVibrateDurationMyPrefsUseForOnlyHeartbeatVibration(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        int value = sharedPreferences.getInt(VALUE_KEY, 0);
        return value;
    }





}
