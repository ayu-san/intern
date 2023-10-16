package com.example.test;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.SoundPool;

public class SoundPlayer {
    private static SoundPool soundPool;
    private static int testSE;
    private static int testSE2;
    private float seVolume = 0.8f; // SEの初期音量

    public SoundPlayer(Context context) {

        AudioAttributes attributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();

        soundPool = new SoundPool.Builder()
                .setAudioAttributes(attributes)
                .build();

        //サウンドのロード
        testSE = soundPool.load(context, R.raw.setest, 1);
        testSE2 = soundPool.load(context, R.raw.setest2, 1);
    }

    //音量調整機能
    public void setSEVolume(float volume) {
        seVolume = volume;
    }
    public void setBGMVolume() {
        // BGMの初期音量
    }

    public void release(){soundPool.release();}//soundPoolの解放

    //サウンド読み込み関数
    public void setTestSE(){soundPool.play(testSE,seVolume, seVolume, 1, 0, 1.0f);}
    public void setTestSE2(){soundPool.play(testSE2,seVolume, seVolume, 1, 0, 1.0f);}

}
