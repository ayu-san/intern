package com.example.test;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class TitleActivity extends AppCompatActivity {

    private Button startButton;
    private Button settingButton;
    private Button asobiButton;
    private Button cregitButton;

    private SoundPlayer soundPlayer;
    private boolean shouldShowPauseDialog = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title);

        startButton = findViewById(R.id.startbutton);
        settingButton = findViewById(R.id.settingbutton);
        asobiButton = findViewById(R.id.asobibutton);
        cregitButton = findViewById(R.id.cregitbutton);

        soundPlayer = new SoundPlayer(this);

        //始めるボタンを押したとき
        startButton.setOnClickListener((View v)->{

            soundPlayer.setTestSE();

            shouldShowPauseDialog = false;
            startActivity(new Intent(this, SelectActivity.class));
        });

        //設定ボタンを押したとき
        settingButton.setOnClickListener((View v)->{

            soundPlayer.setTestSE();

            //設定ダイアログの読み込み
            View dialogView = getLayoutInflater().inflate(R.layout.dialog_settings,null);

            //ダイアログビューの設定
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setView(dialogView);

            //AlertDialogを表示
            AlertDialog alertDialog = builder.create();
            alertDialog.show();

            SeekBar seekBarBGM = dialogView.findViewById(R.id.BGMseekBar);
            SeekBar seekBarSE = dialogView.findViewById(R.id.SEseekBar);

            // シークバーの初期値を設定
            seekBarBGM.setProgress((int) (MyApplication.getBGMVolume() * 100));
            seekBarSE.setProgress((int) (MyApplication.getSEVolume() * 100));

            seekBarBGM.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    //BGMの音量設定
                    float bgmvolume = progress / 100.0f;
                    MyApplication.setBGMVolume(bgmvolume);
                    soundPlayer.setBGMVolume(bgmvolume);
                }
                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                    // シークバーの操作が開始されたときの処理
                }
                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    // シークバーの操作が終了したときの処理
                }
            });

            seekBarSE.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    //SEの音量設定
                    float sevolume = progress / 100.0f;
                    MyApplication.setSEVolume(sevolume);
                    soundPlayer.setSEVolume(sevolume);
                }
                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                }
                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                }
            });

        });

        //遊び方ボタンを押したとき
        asobiButton.setOnClickListener((View v)->{

            soundPlayer.setTestSE();

            //設定ダイアログの読み込み
            View dialogView = getLayoutInflater().inflate(R.layout.dialog_asobi,null);

            //ダイアログビューの設定
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setView(dialogView);

            //AlertDialogを表示
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        });

        //クレジットボタンを押したとき
        cregitButton.setOnClickListener((View v)->{

            soundPlayer.setTestSE2();

            //設定ダイアログの読み込み
            View dialogView = getLayoutInflater().inflate(R.layout.dialog_cregit,null);

            //ダイアログビューの設定
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setView(dialogView);

            //AlertDialogを表示
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        });

    }
    // ボタンクリック音を再生するメソッド

    @Override
    protected void onPause(){
        super.onPause();
        if (shouldShowPauseDialog) {
            showPauseDialog();
        }
    }

    @Override
    public void onBackPressed() {
        // 戻るボタンのデフォルトの動作を無効化（何もしない）
    }

    public void closeDialog(View view) {
        // ダイアログを閉じる
        if (view != null) {
            AlertDialog alertDialog = (AlertDialog) view.getTag();
            if (alertDialog != null) {
                alertDialog.dismiss();
            }
        }
    }

    public void showPauseDialog() {
        shouldShowPauseDialog = true;
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_pause, null);

        //ダイアログビューの設定
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);

        //AlertDialogを表示
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        //BGM.SE設定
        SeekBar seekBarbgm = dialogView.findViewById(R.id.BGMseekBarPause);
        SeekBar seekBarse = dialogView.findViewById(R.id.SEseekBarPause);

        // シークバーの初期値を設定
        seekBarbgm.setProgress((int) (MyApplication.getBGMVolume() * 100));
        seekBarse.setProgress((int) (MyApplication.getSEVolume() * 100));

        seekBarbgm.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //BGMの音量設定
                float bgmvolume = progress / 100.0f;
                MyApplication.setBGMVolume(bgmvolume);
                soundPlayer.setBGMVolume(bgmvolume);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seekBarse.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //BGMの音量設定
                float sevolume = progress / 100.0f;
                MyApplication.setSEVolume(sevolume);
                soundPlayer.setSEVolume(sevolume);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //リトライ
        // ダイアログ内のボタンにクリックリスナーを設定
        Button retry = dialogView.findViewById(R.id.retry);
        retry.setOnClickListener((View view) -> {
            // ボタンが押されたときの処理
            startActivity(new Intent(this, TitleActivity.class));
            alertDialog.dismiss(); // ダイアログを閉じる
        });

        //ステージ選択へ
        // ダイアログ内のボタンにクリックリスナーを設定
        Button gotoStageSelect = dialogView.findViewById(R.id.gotoStageSelect);
        gotoStageSelect.setOnClickListener((View view) -> {
            // ボタンが押されたときの処理
            startActivity(new Intent(this, SelectActivity.class));
            alertDialog.dismiss(); // ダイアログを閉じる
        });

        //タイトルへ
        // ダイアログ内のボタンにクリックリスナーを設定
        Button gotoTitle = dialogView.findViewById(R.id.gotoTitle);
        gotoTitle.setOnClickListener((View view) -> {
            // ボタンが押されたときの処理
            startActivity(new Intent(this, TitleActivity.class));
            alertDialog.dismiss(); // ダイアログを閉じる
        });


    }
}
