package com.findphone.whistleclapfinder.SetionClasss;

import android.content.Context;
import android.content.SharedPreferences;

public class MyPrefsUseForMainSettingFlashLightModeSelect {

    private static final String PREFS_NAME = "MyPrefsUseForMainSettingFlashLightModeSelect";
    private static final String VALUE_KEY_FlashLightMode = "FlashLightMode";

    public static void clearMyPrefsUseForMainSettingFlashLightModeSelect(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }


    public static void addMyPrefsUseForMainSettingFlashLightModeSelect(Context context, String  modeName) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(VALUE_KEY_FlashLightMode, modeName);
        editor.apply();
    }

    public static String getMyPrefsUseForMainSettingFlashLightModeSelect(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String modeName = sharedPreferences.getString(VALUE_KEY_FlashLightMode, "");
        return modeName;
    }





}
