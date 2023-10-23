package game.intern.test;

import android.content.Context;
import android.media.MediaPlayer;

import game.intern.test.R;

public class SoundPlayer {
    private static MediaPlayer  sePlayer;
    private MediaPlayer  bgmPlayer;
    private final Context context;
    private static int testSE;
    private static int testSE2;
    private static int testBGM;
    private float seVolume = 0.8f; // SEの初期音量
    private float bgmVolume = 0.8f; // BGMの初期音量

    public SoundPlayer(Context context) {
        this.context = context; // コンテキストを設定

        // SE用を初期化
        sePlayer = new MediaPlayer();

        // BGM用を初期化
        bgmPlayer = new MediaPlayer();

        //サウンドのロード
        testSE = R.raw.setest;
        testSE2 = R.raw.setest2;
        testBGM = R.raw.bgmtest;

        // MediaPlayerにサウンドファイルを設定
        sePlayer = MediaPlayer.create(context, testSE);
        bgmPlayer = MediaPlayer.create(context, testBGM);

    }

    public  void release(){
        if (sePlayer != null) {
            sePlayer.release();
            sePlayer = null;
        }
        if (bgmPlayer != null) {
            bgmPlayer.release();
            bgmPlayer = null;
        }
    }

    // 音量調整機能
    public float getSEVolume() {
        return seVolume;
    }

    public void setSEVolume(float sevolume) {
        seVolume = sevolume;
        sePlayer.setVolume(seVolume, seVolume);
    }

    public float getBGMVolume() {
        return bgmVolume;
    }
    public void setBGMVolume(float bgmvolume) {
        bgmVolume = bgmvolume;
        bgmPlayer.setVolume(bgmVolume, bgmVolume);
    }

    // サウンド再生関数
// サウンド再生関数
    public void setTestSE() {
        sePlayer = MediaPlayer.create(context, testSE);
        sePlayer.setVolume(seVolume, seVolume);
        sePlayer.start();
    }

    public void setTestSE2() {
        sePlayer = MediaPlayer.create(context, testSE2);
        sePlayer.setVolume(seVolume, seVolume);
        sePlayer.start();
    }

    public void setTestBGM() {
        bgmPlayer = MediaPlayer.create(context, testBGM);
        bgmPlayer.setVolume(bgmVolume, bgmVolume);
        bgmPlayer.setLooping(true); // BGMをループ再生
        bgmPlayer.start();
    }


}
