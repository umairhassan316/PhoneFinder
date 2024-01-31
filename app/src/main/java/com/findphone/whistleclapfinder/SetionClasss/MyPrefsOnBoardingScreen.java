package com.findphone.whistleclapfinder.SetionClasss;

import android.content.Context;
import android.content.SharedPreferences;

public class MyPrefsOnBoardingScreen {

    private static final String PREFS_NAME = "MyPrefsOnBoardingScreen";
    private static final String MY_PREFS_ON_BOARDING_MAIN_KEY = "ON_BOARDING_MAIN_KEY";

    public static void clearMyPrefsOnBoardingScreen(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    public static void addMyPrefsOnBoardingScreen(Context context, boolean isChecked) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(MY_PREFS_ON_BOARDING_MAIN_KEY, isChecked);
        editor.apply();
    }

    public static boolean getMyPrefsOnBoardingScreen(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        boolean vibrationModeName = sharedPreferences.getBoolean(MY_PREFS_ON_BOARDING_MAIN_KEY, false);
        return vibrationModeName;
    }







}
