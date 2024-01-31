package com.findphone.whistleclapfinder.SetionClasss;

import android.content.Context;
import android.content.SharedPreferences;

public class MyPrefsVibrationModeMainSetting {

    private static final String PREFS_NAME = "MyPrefsMyPrefsVibrationModeMainSetting";
    private static final String MY_PREFS_VIBRATION_MAIN_KEY = "VIBRATION_MAIN_KEY";

    public static void clearMyPrefsMyPrefsVibrationModeMainSetting(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    public static void addMyPrefsMyPrefsVibrationModeMainSetting(Context context, String vibrationModeName) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(MY_PREFS_VIBRATION_MAIN_KEY, vibrationModeName);
        editor.apply();
    }

    public static String getMyPrefsMyPrefsVibrationModeMainSetting(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String vibrationModeName = sharedPreferences.getString(MY_PREFS_VIBRATION_MAIN_KEY, "");
        return vibrationModeName;
    }







}
