package com.example.test;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

public class MyApplication extends Application{

    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;
    private static final String PREFERENCES_NAME = "MyAppSettings";

    public static final String BGM_VOLUME = "bgm_volume";
    public static final String SE_VOLUME = "se_volume";

    @Override
    public void onCreate() {
        super.onCreate();
        sharedPreferences = getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public static float getBGMVolume() {
        return sharedPreferences.getFloat(BGM_VOLUME, 1.0f); // デフォルト値は最大音量
    }

    public static void setBGMVolume(float volume) {
        editor.putFloat(BGM_VOLUME, volume).apply();
    }

    public static float getSEVolume() {
        return sharedPreferences.getFloat(SE_VOLUME, 1.0f); // デフォルト値は最大音量
    }

    public static void setSEVolume(float volume) {
        editor.putFloat(SE_VOLUME, volume).apply();
    }

}
