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
        return sharedPreferences.getFloat(BGM_VOLUME, 0.8f); // デフォルト値は8割
    }

    public static void setBGMVolume(float volume) {
        editor.putFloat(BGM_VOLUME, volume).apply();
    }

    public static float getSEVolume() {
        return sharedPreferences.getFloat(SE_VOLUME, 0.8f); // デフォルト値は8割
    }

    public static void setSEVolume(float volume) {
        editor.putFloat(SE_VOLUME, volume).apply();
    }

    // BGMの現在の音量を保存
    public static void saveCurrentBGMVolume(float volume) {
        editor.putFloat(BGM_VOLUME, volume).apply();
    }

    // SEの現在の音量を保存
    public static void saveCurrentSEVolume(float volume) {
        editor.putFloat(SE_VOLUME, volume).apply();
    }

}
