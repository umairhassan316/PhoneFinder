package com.findphone.whistleclapfinder.SetionClasss;

import android.content.Context;
import android.content.SharedPreferences;

public class MyPrefsExtensionMainSetting {

    private static final String PREFS_NAME = "MyPrefsExtensionMainSetting";
    private static final String MY_PREFS_EXTENSION_MAIN_KEY = "EXTENSION_MAIN_KEY";

    public static void clearMyPrefsExtensionMainSetting(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    public static void addMyPrefsExtensionMainSetting(Context context, boolean isChecked) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(MY_PREFS_EXTENSION_MAIN_KEY, isChecked);
        editor.apply();
    }

    public static boolean getMyPrefsExtensionMainSetting(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        boolean firsttime = sharedPreferences.getBoolean(MY_PREFS_EXTENSION_MAIN_KEY, false);
        return firsttime;
    }







}
