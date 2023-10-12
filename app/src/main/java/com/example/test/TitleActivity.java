package com.example.test;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class TitleActivity extends AppCompatActivity {

    private Button startButton;
    private Button settingButton;
    private Button asobiButton;
    private Button cregitButton;
    private ImageView titlLogo;

    private SoundPlayer soundPlayer;
    private FrameLayout frameLayout;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title);

        startButton = findViewById(R.id.startbutton);
        settingButton = findViewById(R.id.settingbutton);
        asobiButton = findViewById(R.id.asobibutton);
        cregitButton = findViewById(R.id.cregitbutton);

        titlLogo = findViewById(R.id.titlelogo);
        Animation floatAnimation  = AnimationUtils.loadAnimation(this,R.anim.scale_up_down);
        titlLogo.startAnimation(floatAnimation);

        soundPlayer = new SoundPlayer(this);

        frameLayout = findViewById(R.id.tap_effect);

        frameLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    ShowTouchEffect(motionEvent.getX(), motionEvent.getY());
                }

                return false;
            }
        });

        //始めるボタンを押したとき
        startButton.setOnClickListener((View v)->{

            soundPlayer.setTestSE();

            startActivity(new Intent(this, SelectActivity.class));
        });

        //設定ボタンを押したとき
        settingButton.setOnClickListener((View v)->{

            soundPlayer.setTestSE();

            //設定ダイアログの読み込み
            View dialogView = getLayoutInflater().inflate(R.layout.dialog_settings,null);

            //ダイアログビューの設定
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setView(dialogView)
                    .setCancelable(false);

            //AlertDialogを表示
            AlertDialog alertDialog = builder.create();
            alertDialog.setCanceledOnTouchOutside(false); // ダイアログの外側をクリックしても閉じない
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

            //戻る
            Button closeButton = dialogView.findViewById(R.id.closeButtonSetting);
            closeButton.setOnClickListener((View view)->{
                alertDialog.dismiss(); // ダイアログを閉じる
            });

        });

        //遊び方ボタンを押したとき
        asobiButton.setOnClickListener((View v)->{

            soundPlayer.setTestSE();

            //設定ダイアログの読み込み
            View dialogView = getLayoutInflater().inflate(R.layout.dialog_asobi,null);

            //ダイアログビューの設定
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setView(dialogView)
                    .setCancelable(false);

            //AlertDialogを表示
            AlertDialog alertDialog = builder.create();
            alertDialog.setCanceledOnTouchOutside(false); // ダイアログの外側をクリックしても閉じない
            alertDialog.show();

            //戻る
            Button closeButton = dialogView.findViewById(R.id.closeButtonAsobi);
            closeButton.setOnClickListener((View view)->{
                alertDialog.dismiss(); // ダイアログを閉じる
            });

        });

        //クレジットボタンを押したとき
        cregitButton.setOnClickListener((View v)->{

            soundPlayer.setTestSE2();

            //設定ダイアログの読み込み
            View dialogView = getLayoutInflater().inflate(R.layout.dialog_cregit,null);

            //ダイアログビューの設定
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setView(dialogView)
                    .setCancelable(false);

            //AlertDialogを表示
            AlertDialog alertDialog = builder.create();
            alertDialog.setCanceledOnTouchOutside(false); // ダイアログの外側をクリックしても閉じない
            alertDialog.show();

            //戻る
            Button closeButton = dialogView.findViewById(R.id.closeButtonCregit);
            closeButton.setOnClickListener((View view)->{
                alertDialog.dismiss(); // ダイアログを閉じる
            });

        });

    }
    // ボタンクリック音を再生するメソッド

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

    private void ShowTouchEffect(float x, float y){
        // エフェクト用の ImageView を作成
        ImageView effectImageView = new ImageView(this);
        effectImageView.setImageResource(R.drawable.tap_effect_drawable); // エフェクト画像を設定
        effectImageView.setX(x-40); // X 座標を設定
        effectImageView.setY(y-80); // Y 座標を設定

// 初期サイズを設定
        int initialSize = 180;
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(initialSize, initialSize);
        effectImageView.setLayoutParams(layoutParams);

        frameLayout.addView(effectImageView);

        // アニメーション: エフェクトのサイズを縮小
        int finalSize = 100; // 最終的なサイズを設定
        ValueAnimator widthAnimator = ValueAnimator.ofInt(initialSize, finalSize);
        ValueAnimator heightAnimator = ValueAnimator.ofInt(initialSize, finalSize);

        widthAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int value = (int) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = effectImageView.getLayoutParams();
                layoutParams.width = value;
                effectImageView.setLayoutParams(layoutParams);
            }
        });

        heightAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int value = (int) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = effectImageView.getLayoutParams();
                layoutParams.height = value;
                effectImageView.setLayoutParams(layoutParams);
            }
        });

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(widthAnimator, heightAnimator);
        animatorSet.setDuration(300); // アニメーションの時間（ミリ秒）
        animatorSet.start();

        // エフェクトのアニメーション（例：フェードアウト）
        effectImageView.animate().alpha(0).setDuration(300).withEndAction(new Runnable() {
            @Override
            public void run() {
                // エフェクトをコンテナから削除
                frameLayout.removeView(effectImageView);
            }
        });
    }

}
