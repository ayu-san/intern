package com.example.test;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;


public class SelectActivity extends AppCompatActivity {

    private Button normalbutton;
    private Button hardbutton;

    private boolean isConditionNormal = false;
    private boolean isConditionHard = false;


    private SoundPlayer soundPlayer;
    private TapEffect tapEffect;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);

        //sharedPreferencesの初期化
        SharedPreferences sharedPreferences = getSharedPreferences("isConditionNormal", MODE_PRIVATE);
        SharedPreferences sharedPreferences2 = getSharedPreferences("isConditionHard", MODE_PRIVATE);

        //sharedPreferencesの設定
        //isConditionNormal = sharedPreferences.getBoolean("isConditionNormal",false);
        //isConditionHard = sharedPreferences2.getBoolean("isConditionHard",false);

        Button eazybutton = findViewById(R.id.eazy);
        normalbutton = findViewById(R.id.normal);
        hardbutton = findViewById(R.id.Hard);
        ImageButton backbutton = findViewById(R.id.backbutton);

        //最初はどちらも無効化しておく
        normalbutton.setEnabled(false);
        hardbutton.setEnabled(false);

        soundPlayer = new SoundPlayer(this);

        FrameLayout tapEffectContainer = findViewById(R.id.tap_effect);
        tapEffect = new TapEffect(this,tapEffectContainer);

        tapEffectContainer.setOnTouchListener((view, motionEvent) -> {
            if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                tapEffect.show(motionEvent.getX(), motionEvent.getY());
            }
            return false;
        });



        //easyボタンを押したとき
        eazybutton.setOnClickListener((View v)->{
            soundPlayer.setTestSE();

            onConditionNormal();
            startActivity(new Intent(this, MainActivity.class));
        });

        //normalボタンを押したとき
        normalbutton.setOnClickListener((View v)->{
            soundPlayer.setTestSE();

            onConditionHard();
            startActivity(new Intent(this, MainActivity.class));
        });

        //hardボタンを押したとき
        hardbutton.setOnClickListener((View v)->{
            soundPlayer.setTestSE();

            startActivity(new Intent(this, MainActivity.class));
        });

        //戻るボタンを押したとき
        backbutton.setOnClickListener((View v)->{
            soundPlayer.setTestSE2();

            startActivity(new Intent(this, TitleActivity.class));
        });

        //条件をクリアしたら有効可
        if(isConditionNormal){
            isConditionNormal = true;
            normalbutton.setEnabled(true);
        }

        //条件をクリアしたら有効可
        if(isConditionHard){
            isConditionHard = true;
            hardbutton.setEnabled(true);
        }

    }

    @Override
    public void onBackPressed() {
        // 戻るボタンのデフォルトの動作を無効化（何もしない）
    }

    // 条件が満たされた場合に呼び出すメソッド
    private void onConditionNormal() {
        isConditionNormal = true;
        normalbutton.setEnabled(true);

        // 条件が満たされたことをSharedPreferencesに保存
        //SharedPreferences.Editor editor = sharedPreferences.edit();
        //editor.putBoolean("isConditionNormal", true);
        //editor.apply();

    }

    private void onConditionHard() {
        isConditionHard = true;
        hardbutton.setEnabled(true);

        // 条件が満たされたことをSharedPreferencesに保存
        //SharedPreferences.Editor editor2 = sharedPreferences2.edit();
        //editor2.putBoolean("isConditionHard", true);
        //editor2.apply();
    }

}
