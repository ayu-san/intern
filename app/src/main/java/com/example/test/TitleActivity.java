package com.example.test;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.graphics.drawable.Drawable;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class TitleActivity extends AppCompatActivity {

    private SoundPlayer soundPlayer;

    private TapEffect tapEffect;

    // 画像リソースの名前のリスト
    private static final String[] imageResourceNames = {
            "enemy","character_image","yajirusi","titlelogo"
    };

    @SuppressLint({"MissingInflatedId", "ClickableViewAccessibility"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title);

        Button startButton = findViewById(R.id.startbutton);
        Button settingButton = findViewById(R.id.settingbutton);
        Button asobiButton = findViewById(R.id.asobibutton);
        Button cregitButton = findViewById(R.id.cregitbutton);

        ImageView titlLogo = findViewById(R.id.titlelogo);
        Animation floatAnimation  = AnimationUtils.loadAnimation(this,R.anim.scale_up_down);
        titlLogo.startAnimation(floatAnimation);

        soundPlayer = new SoundPlayer(this);

        FrameLayout tapEffectContainer = findViewById(R.id.tap_effect);
        tapEffect = new TapEffect(this,tapEffectContainer);

        tapEffectContainer.setOnTouchListener((view, motionEvent) -> {
            if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                tapEffect.show(motionEvent.getX(), motionEvent.getY());
            }
            return false;
        });

        //始めるボタンを押したとき
        startButton.setOnClickListener((View v)->{

            showLevelUp();

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
                soundPlayer.setTestSE();

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
                soundPlayer.setTestSE();

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
                soundPlayer.setTestSE();

                alertDialog.dismiss(); // ダイアログを閉じる
            });

        });

    }
    // ボタンクリック音を再生するメソッド

    @Override
    public void onBackPressed() {
        // 戻るボタンのデフォルトの動作を無効化（何もしない）
    }

    private void setRandomImageForImageButton(ImageButton imageButton) {
        // ランダムな画像を選択
        Random random = new Random();
        int randomIndex = random.nextInt(imageResourceNames.length);
        String randomImageResourceName = imageResourceNames[randomIndex];

        // リソースIDを取得
        int resID = getResources().getIdentifier(randomImageResourceName, "drawable", getPackageName());

        // ImageButtonに画像を設定
        imageButton.setImageResource(resID);
    }

    //レベルアップ画面表示関数
    public void showLevelUp(){
        //設定ダイアログの読み込み
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_levelup,null);

        //ダイアログビューの設定
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView)
                .setCancelable(false);

        //AlertDialogを表示
        AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false); // ダイアログの外側をクリックしても閉じない
        alertDialog.show();

        ImageButton item1 = dialogView.findViewById(R.id.item1);
        ImageButton item2 = dialogView.findViewById(R.id.item2);
        Button noselect = dialogView.findViewById(R.id.noSelect);

        setRandomImageForImageButton(item1);
        setRandomImageForImageButton(item2);

        item1.setOnClickListener(view -> {
            // ImageButtonの現在の画像を取得
            Drawable currentDrawable = item1.getDrawable();

            //画像によって処理を変える
            if(currentDrawable.getConstantState().equals(getResources().getDrawable(R.drawable.character_image).getConstantState())){
                startActivity(new Intent(TitleActivity.this, SelectActivity.class));
            } else if (currentDrawable.getConstantState().equals(getResources().getDrawable(R.drawable.enemy).getConstantState())) {
                startActivity(new Intent(TitleActivity.this, SelectActivity.class));
            }

        });

        item2.setOnClickListener(view -> {
            // ImageButtonの現在の画像を取得
            Drawable currentDrawable = item2.getDrawable();

            //画像によって処理を変える
            if(currentDrawable.getConstantState().equals(getResources().getDrawable(R.drawable.character_image).getConstantState())){
                startActivity(new Intent(TitleActivity.this, SelectActivity.class));
            } else if (currentDrawable.getConstantState().equals(getResources().getDrawable(R.drawable.enemy).getConstantState())) {
                startActivity(new Intent(TitleActivity.this, SelectActivity.class));
            }

        });

        noselect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

    }


}
