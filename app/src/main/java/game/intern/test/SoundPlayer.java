package game.intern.test;

import android.content.Context;
import android.media.MediaPlayer;

import game.intern.test.R;

public class SoundPlayer {
    private static MediaPlayer  sePlayer;
    private MediaPlayer  bgmPlayer;
    private final Context context;
    private float seVolume = 0.8f; // SEの初期音量
    private float bgmVolume = 0.8f; // BGMの初期音量

    public SoundPlayer(Context context) {
        this.context = context; // コンテキストを設定

        // SE用を初期化
        sePlayer = new MediaPlayer();

        // BGM用を初期化
        bgmPlayer = new MediaPlayer();

        //サウンドのロード
        int testSE = R.raw.se_reflection;
        int testBGM = R.raw.stage1;

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

    public void pauseBGM() {
        if (bgmPlayer != null && bgmPlayer.isPlaying()) {
            bgmPlayer.pause();
        }
    }

    public void resumeBGM() {
        if (bgmPlayer != null && !bgmPlayer.isPlaying()) {
            bgmPlayer.start();
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

    // MediaPlayer インスタンスを再利用するメソッドを作成
    private void playMedia(MediaPlayer mediaPlayer, int soundResourceId, float volume) {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer = MediaPlayer.create(context, soundResourceId);
            mediaPlayer.setVolume(volume, volume);
            mediaPlayer.start();
        }
    }

    public void setSE(int se) {
        playMedia(sePlayer, se, seVolume);
    }//setSE(R.raw.setest2)

    public void setBGM(int bgm)
    {
        if (bgmPlayer != null) {
            bgmPlayer.stop();
            bgmPlayer.reset();
            bgmPlayer = MediaPlayer.create(context, bgm);
            bgmPlayer.setVolume(bgmVolume, bgmVolume);
            bgmPlayer.setLooping(true);
            bgmPlayer.start();
        }
    }

}
