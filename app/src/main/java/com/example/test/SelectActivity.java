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
import android.widget.ScrollView;

import androidx.appcompat.app.AppCompatActivity;


public class SelectActivity extends AppCompatActivity {

    private Button normalbutton;
    private Button hardbutton;
    private Button hellbutton;
    private Button heavenbutton;

    private boolean isConditionNormal = false;
    private boolean isConditionHard = false;
    private boolean isConditionHell = false;
    private boolean isConditionHeaven = false;

    SharedPreferences sharedPreferences;

    private SoundPlayer soundPlayer;
    private TapEffect tapEffect;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);

        //sharedPreferencesの初期化
        sharedPreferences = getSharedPreferences("isCondition", MODE_PRIVATE);

        //sharedPreferencesの設定
        isConditionNormal = sharedPreferences.getBoolean("isConditionNormal",false);
        isConditionHard = sharedPreferences.getBoolean("isConditionHard",false);
        isConditionHell = sharedPreferences.getBoolean("isConditionHell",false);
        isConditionHeaven = sharedPreferences.getBoolean("isConditionHeaven",false);

        Button eazybutton = findViewById(R.id.eazy);
        normalbutton = findViewById(R.id.normal);
        hardbutton = findViewById(R.id.Hard);
        hellbutton = findViewById(R.id.hell);
        heavenbutton = findViewById(R.id.heaven);
        ImageButton backbutton = findViewById(R.id.backbutton);

        ScrollView scrollView = findViewById(R.id.scroll);
        scrollView.setScrollbarFadingEnabled(false);

        //最初はどちらも無効化しておく
        normalbutton.setEnabled(false);
        hardbutton.setEnabled(false);
        hellbutton.setEnabled(false);
        heavenbutton.setEnabled(false);

        // ボタンの初期状態を設定
        initializeButtonState();

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
            setAndSaveCondition(normalbutton,"isConditionNormal",sharedPreferences,isConditionNormal);
            startActivity(new Intent(this, MainActivity.class));
        });

        //normalボタンを押したとき
        normalbutton.setOnClickListener((View v)->{
            soundPlayer.setTestSE();

            setAndSaveCondition(hardbutton,"isConditionHard",sharedPreferences,isConditionHard);
            startActivity(new Intent(this, MainActivity.class));
        });

        //hardボタンを押したとき
        hardbutton.setOnClickListener((View v)->{
            soundPlayer.setTestSE();

            setAndSaveCondition(hellbutton,"isConditionHell",sharedPreferences,isConditionHell);
            startActivity(new Intent(this, MainActivity.class));
        });

        //hellボタンを押したとき
        hellbutton.setOnClickListener((View v)->{
            soundPlayer.setTestSE();

            setAndSaveCondition(heavenbutton,"isConditionHeaven",sharedPreferences,isConditionHeaven);
            startActivity(new Intent(this, MainActivity.class));
        });

        //heavenボタンを押したとき
        heavenbutton.setOnClickListener((View v)->{
            soundPlayer.setTestSE();

            startActivity(new Intent(this, MainActivity.class));
        });

        //戻るボタンを押したとき
        backbutton.setOnClickListener((View v)->{
            soundPlayer.setTestSE2();

            //initData(sharedPreferences,"isCondition");

            startActivity(new Intent(this, TitleActivity.class));
        });

    }

    @Override
    public void onBackPressed() {
        // 戻るボタンのデフォルトの動作を無効化（何もしない）
    }

    // 条件が満たされた場合に呼び出すメソッド
    private void setAndSaveCondition(Button button, String conditionName, SharedPreferences sharedPreferencesd, boolean value){
        // SharedPreferencesから条件を読み込み、デフォルト値を使用する
        boolean isConditionMet = sharedPreferencesd.getBoolean(conditionName, value);

        // SharedPreferencesに状態を保存
        isConditionMet = true;
        button.setEnabled(true);
        SharedPreferences.Editor editor = sharedPreferencesd.edit();
        editor.putBoolean(conditionName,true);
        editor.apply();
    }

    // ボタンの初期状態を設定
    private void initializeButtonState() {
        normalbutton.setEnabled(isConditionNormal);
        hardbutton.setEnabled(isConditionHard);
        hellbutton.setEnabled(isConditionHell);
        heavenbutton.setEnabled(isConditionHeaven);
    }

    //デバッグ用データ初期化
    private void initData(SharedPreferences preferences1,String conditionName){
        preferences1 = getSharedPreferences(conditionName,MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences1.edit();
        editor.clear();
        editor.apply();
    }

}
